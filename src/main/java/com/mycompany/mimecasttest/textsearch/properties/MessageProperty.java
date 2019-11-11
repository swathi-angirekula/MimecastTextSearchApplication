package com.mycompany.mimecasttest.textsearch.properties;

import java.io.Serializable;

/**
 * @author Swathi Angirekula
 *
 */
public enum MessageProperty implements Serializable {

	INVALID_PATH("invalid.path"), INVALID_SERVER("invalid.server"), INTERNAL_SERVER_ERROR("internal.server.error"),
	RESPONSE_MESSAGE_EVENT_DESERIALIZATION_ERROR("response.message.event.deserialization.error");

	private String[] args = {};

	private String key;

	MessageProperty() {
	}

	MessageProperty(String key) {
		this.key = key;
	}

	public String key() {
		return key;
	}

	public String message() {
		return MessageSource.get().message(key, args);
	}

	public MessageProperty bind(String... args) {
		this.args = args;
		return this;
	}

}
