package com.mycompany.mimecasttest.textsearch.controller;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mycompany.mimecasttest.textsearch.exception.CustomAppException;
import com.mycompany.mimecasttest.textsearch.message.TextSearchRequest;
import com.mycompany.mimecasttest.textsearch.message.TextSearchResponse;
import com.mycompany.mimecasttest.textsearch.properties.ApplicationProperties;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.main.web-application-type=reactive")
class TextSearchEngineTest {

	@Autowired
	private TextSearchEngine textSearchEngine;
	@Autowired
	private ApplicationProperties applicationProperties;
	private String rootPath;

	@BeforeEach
	void setup() throws URISyntaxException {
		rootPath = Paths.get(getClass().getClassLoader().getResource("folder1").toURI()).toString();
	}

	@Test
	void testSearch_multipleMatches() {
		TextSearchRequest sr = TextSearchRequest.of(applicationProperties.getProxyUrl(), rootPath, "searchText2");
		final Flux<TextSearchResponse> searchResult = textSearchEngine.search(sr);
		Assertions.assertEquals(3L, searchResult.count().block().longValue());
	}

	@Test
	void testSearch_singleMatch() {
		TextSearchRequest sr = TextSearchRequest.of(applicationProperties.getProxyUrl(), rootPath, "searchText1");
		final Flux<TextSearchResponse> searchResult = textSearchEngine.search(sr);
		Assertions.assertEquals(1L, searchResult.count().block().longValue());
	}

	@Test
	void testSearch_multipleMatches_Mimecast() {
		TextSearchRequest sr = TextSearchRequest.of(applicationProperties.getProxyUrl(), rootPath, "mimecast");
		final Flux<TextSearchResponse> searchResult = textSearchEngine.search(sr);
		Assertions.assertEquals(4, searchResult.blockFirst().getCount());
	}

	@Test
	void test_search_at_invalid_path() {
		TextSearchRequest sr = TextSearchRequest.of(applicationProperties.getProxyUrl(), "abacaa", "askjd");
		Assertions.assertThrows(CustomAppException.class, () -> textSearchEngine.search(sr));
	}

}
