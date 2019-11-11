package com.mycompany.mimecasttest.textsearch.controller;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.google.common.io.Files;
import com.mycompany.mimecasttest.textsearch.exception.CustomAppException;
import com.mycompany.mimecasttest.textsearch.message.TextSearchRequest;
import com.mycompany.mimecasttest.textsearch.message.TextSearchResponse;
import com.mycompany.mimecasttest.textsearch.properties.ApplicationProperties;
import com.mycompany.mimecasttest.textsearch.properties.MessageProperty;
import com.mycompany.mimecasttest.textsearch.validation.PathValidator;
import com.mycompany.mimecasttest.textsearch.validation.ServerValidator;
import com.mycompany.mimecasttest.textsearch.validation.Validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * @author Swathi Angirekula
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TextSearchEngine {

	private final ApplicationProperties applicationProperties;

	/**
	 * @param textSearchRequest
	 * @return
	 */
	public Flux<TextSearchResponse> search(TextSearchRequest textSearchRequest) {
		try {
			File file = Paths.get(textSearchRequest.getFilePath()).toFile();
			Validation.execute(
					ServerValidator.of(textSearchRequest.getServer(), applicationProperties.getServersAsSet()));
			Validation.execute(PathValidator.of(file, textSearchRequest.getServer()));
			return Flux.fromIterable(Files.fileTraverser().breadthFirst(file)).filter(f -> f.isFile() && f.canRead())
					.map(f -> this.searchFileContent(f, textSearchRequest)).filter(f -> f.getCount() > 0)
					.delayElements(Duration.ofMillis(500)); // This delay is added just for the sake of testing to
															// simulate the delay in processing and results are
															// displayed one after the other.
		} catch (CustomAppException e) {
			throw e;
		} catch (Exception e) {
			throw new CustomAppException(MessageProperty.INTERNAL_SERVER_ERROR.bind(textSearchRequest.getFilePath()));
		}
	}

	/**
	 * @param file
	 * @param textSearchRequest
	 * @return
	 */
	private TextSearchResponse searchFileContent(File file, TextSearchRequest textSearchRequest) {
		TextSearchResponse response;
		try (BufferedReader br = Files.newReader(file, Charset.defaultCharset())) {
			int count = countWordsInFile(textSearchRequest.getSearchTerm(), br.lines());
			response = TextSearchResponse.of(textSearchRequest.getServer(), file.getAbsolutePath(),
					textSearchRequest.getSearchTerm(), count);
			if (count == 0) {
				log.debug("No Match Found. " + response.toString());
			} else {
				log.debug("Match Found. " + response.toString());
			}
		} catch (Exception e) {
			response = TextSearchResponse.of(textSearchRequest.getServer(), file.getAbsolutePath(),
					textSearchRequest.getSearchTerm(), 0);
			log.debug("Exception while parsing. " + response.toString());
		}
		return response;
	}

	/**
	 * @param searchTerm
	 * @param linesStream
	 * @return
	 */
	private int countWordsInFile(String searchTerm, Stream<String> linesStream) {
		return linesStream.parallel().map(line -> countWordsInLine(line, searchTerm)).reduce(0, Integer::sum);
	}

	/**
	 * @param line
	 * @param searchTerm
	 * @return
	 */
	private int countWordsInLine(String line, String searchTerm) {
		Pattern pattern = Pattern.compile(searchTerm.toLowerCase());
		Matcher matcher = pattern.matcher(line.toLowerCase());

		int count = 0;
		int i = 0;
		while (matcher.find(i)) {
			count++;
			i = matcher.start() + 1;
		}
		return count;
	}

}
