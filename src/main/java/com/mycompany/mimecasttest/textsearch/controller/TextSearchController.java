package com.mycompany.mimecasttest.textsearch.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.mycompany.mimecasttest.textsearch.exception.CustomAppException;
import com.mycompany.mimecasttest.textsearch.message.ErrorResponse;
import com.mycompany.mimecasttest.textsearch.message.ResponseMessageEvent;
import com.mycompany.mimecasttest.textsearch.message.TextSearchRequest;
import com.mycompany.mimecasttest.textsearch.properties.ApplicationProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * @author Swathi Angirekula
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class TextSearchController {
	private static final String SEARCH = "/search";

	private final TextSearchEngine textSearchEngine;
	private final ApplicationProperties applicationProperties;

	@GetMapping(value = SEARCH, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ResponseMessageEvent> search(@RequestParam(value = "path") String path,
			@RequestParam(value = "searchString") String searchString,
			@RequestParam(value = "servers") List<String> servers, ServerHttpRequest request) {
		return Flux.fromIterable(servers).flatMap(server -> this.searchServer(request, server, path, searchString));
	}

	private Flux<ResponseMessageEvent> searchServer(ServerHttpRequest request, String server, String path,
			String searchString) {
		if (server.equals(applicationProperties.getProxyUrl())) {
			return textSearchEngine.search(TextSearchRequest.of(server, path, searchString))
					.map(ResponseMessageEvent::success);
		} else if (this.getRequestUrl(request).equals(server)) {
			return textSearchEngine.search(TextSearchRequest.of(server, path, searchString))
					.map(ResponseMessageEvent::success);
		} else {
			return WebClient.builder().baseUrl(server).build().get()
					.uri(uriBuilder -> uriBuilder.path(SEARCH).queryParam("servers", server).queryParam("path", path)
							.queryParam("searchString", searchString).build())
					.accept(MediaType.TEXT_EVENT_STREAM).retrieve().bodyToFlux(ResponseMessageEvent.class);
		}
	}

	private String getRequestUrl(ServerHttpRequest request) {
		String requestUrl = null;
		try {
			URL url = new URL(request.getURI().toString());
			requestUrl = url.getProtocol() + "://" + request.getURI().getAuthority();
		} catch (MalformedURLException e) {
			return "";
		}
		return requestUrl;
	}

	@ExceptionHandler(CustomAppException.class)
	public Flux<ResponseMessageEvent> handleException(CustomAppException ex) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_EVENT_STREAM);
		return Flux.just(ResponseMessageEvent.error(new ErrorResponse(ex.getMessage(), BAD_REQUEST.toString())));
	}

	@ExceptionHandler(ConnectException.class)
	public Flux<ResponseMessageEvent> handleConnectException(ConnectException ex) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_EVENT_STREAM);
		return Flux.just(ResponseMessageEvent.error(
				new ErrorResponse(this.buildCustomErrorMessageFromException(ex), INTERNAL_SERVER_ERROR.toString())));
	}

	private String buildCustomErrorMessageFromException(ConnectException ex) {
		return String.format("Selected server is unavailable");
	}

}
