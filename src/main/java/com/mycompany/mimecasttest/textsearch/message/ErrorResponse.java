package com.mycompany.mimecasttest.textsearch.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Swathi Angirekula
 *
 */
@Getter
@AllArgsConstructor
public class ErrorResponse implements Response {

	private String message;
	private String status;

	public static ErrorResponse of(String message, String status) {
		return new ErrorResponse(message, status);
	}

}
