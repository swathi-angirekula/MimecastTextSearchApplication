package com.mycompany.mimecasttest.textsearch.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mycompany.mimecasttest.textsearch.message.jackson.deserializer.ResponseMessageDeserializer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Swathi Angirekula
 *
 * @param <T>
 */
@JsonDeserialize(using = ResponseMessageDeserializer.class)
@RequiredArgsConstructor
@Getter
public class ResponseMessageEvent<T extends Response> {

	public static final String SUCCESS = "success";
	public static final String ERROR = "error";

	private final String status;
	private final T response;

	public static final ResponseMessageEvent<Response> success(Response response) {
		return new ResponseMessageEvent<>(SUCCESS, response);
	}

	public static final ResponseMessageEvent<Response> error(Response response) {
		return new ResponseMessageEvent<>(ERROR, response);
	}
}
