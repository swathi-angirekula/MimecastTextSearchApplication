package com.mycompany.mimecasttest.textsearch.exception;

import com.mycompany.mimecasttest.textsearch.properties.MessageProperty;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Swathi Angirekula
 *
 */
@Slf4j
public class CustomAppException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7953470594106580051L;

	/**
	 * @param messageProperty
	 */
	public CustomAppException(MessageProperty messageProperty) {
		super(messageProperty.message());
		log.debug(messageProperty.message());
	}
}
