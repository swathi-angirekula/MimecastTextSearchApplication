package com.mycompany.mimecasttest.textsearch.controller;

import static com.mycompany.mimecasttest.textsearch.message.ResponseMessageEvent.ERROR;
import static com.mycompany.mimecasttest.textsearch.message.ResponseMessageEvent.SUCCESS;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.net.ConnectException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mycompany.mimecasttest.textsearch.exception.CustomAppException;
import com.mycompany.mimecasttest.textsearch.message.ErrorResponse;
import com.mycompany.mimecasttest.textsearch.message.ResponseMessageEvent;
import com.mycompany.mimecasttest.textsearch.message.TextSearchResponse;
import com.mycompany.mimecasttest.textsearch.properties.MessageProperty;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * These tests are using Mockito and
 * 
 * @author Swathi Angirekula
 *
 */
@Slf4j
@ExtendWith(SpringExtension.class)
class TextSearchControllerTest {

	private static final String proxyServer = "http://localhost:8080";

	@Mock
	TextSearchController textSearchController;

	@Test
	void testSearchFiles() {

		String path = requireNonNull(getClass().getClassLoader().getResource("folder1")).getPath();
		String searchString = "searchText1";
		List<String> servers = Arrays.asList(proxyServer);

		Mockito.when(textSearchController.search(path, searchString, servers, null)).thenReturn(Flux.from(Flux.just(
				new ResponseMessageEvent(SUCCESS, TextSearchResponse.of(proxyServer, "folder1", searchString, 1)))));
		final Flux<ResponseMessageEvent> searchResponses = textSearchController.search(path, searchString, servers,
				null);
		Assertions.assertEquals(Long.valueOf(1L), searchResponses.count().block());
		ResponseMessageEvent<TextSearchResponse> messageEvent = searchResponses.blockFirst();
		Assertions.assertNotNull(messageEvent.getResponse().getFilePath());
		Assertions.assertEquals(1, messageEvent.getResponse().getCount());
		Assertions.assertEquals(proxyServer, messageEvent.getResponse().getServer());
		Assertions.assertEquals(ResponseMessageEvent.SUCCESS, messageEvent.getStatus());
	}

	@Test
	void testHandleBusinessException() {
		CustomAppException ex = new CustomAppException(MessageProperty.INVALID_PATH.bind("folder1"));
		Mockito.when(textSearchController.handleException(ex)).thenReturn(
				Flux.just(new ResponseMessageEvent(ERROR, new ErrorResponse(ex.getMessage(), BAD_REQUEST.toString()))));
		final Flux<ResponseMessageEvent> searchResponses = textSearchController.handleException(ex);
		Assertions.assertEquals(Long.valueOf(1L), searchResponses.count().block());
		ResponseMessageEvent<ErrorResponse> messageEvent = searchResponses.blockFirst();
		Assertions.assertNotNull(messageEvent.getResponse().getMessage());
		Assertions.assertEquals(ResponseMessageEvent.ERROR, messageEvent.getStatus());
		Assertions.assertEquals(ex.getMessage(), messageEvent.getResponse().getMessage());
		Assertions.assertEquals(BAD_REQUEST.toString(), messageEvent.getResponse().getStatus());
	}

	@Test
	void testHandleConnectException() {
		ConnectException ex = new ConnectException("localhost:8080");
		Mockito.when(textSearchController.handleConnectException(ex)).thenReturn(Flux.just(
				new ResponseMessageEvent(ERROR, new ErrorResponse(ex.getMessage(), INTERNAL_SERVER_ERROR.toString()))));
		final Flux<ResponseMessageEvent> searchResponses = textSearchController.handleConnectException(ex);
		Assertions.assertEquals(Long.valueOf(1L), searchResponses.count().block());
		ResponseMessageEvent<ErrorResponse> messageEvent = searchResponses.blockFirst();
		Assertions.assertNotNull(messageEvent.getResponse().getMessage());
		Assertions.assertEquals(ResponseMessageEvent.ERROR, messageEvent.getStatus());
		Assertions.assertEquals(ex.getMessage(), messageEvent.getResponse().getMessage());
		Assertions.assertEquals(INTERNAL_SERVER_ERROR.toString(), messageEvent.getResponse().getStatus());
	}

}
