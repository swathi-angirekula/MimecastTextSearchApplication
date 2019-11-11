package com.mycompany.mimecasttest.textsearch.message.jackson.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.mycompany.mimecasttest.textsearch.exception.CustomAppException;
import com.mycompany.mimecasttest.textsearch.message.ErrorResponse;
import com.mycompany.mimecasttest.textsearch.message.ResponseMessageEvent;
import com.mycompany.mimecasttest.textsearch.message.TextSearchResponse;
import com.mycompany.mimecasttest.textsearch.properties.MessageProperty;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponseMessageDeserializer extends JsonDeserializer<ResponseMessageEvent> {
	@Override
	public ResponseMessageEvent<?> deserialize(final JsonParser jp, final DeserializationContext ctxt) {
		log.debug("Response Message Deserialization completed.");
		try {
			final ObjectCodec oc = jp.getCodec();
			final JsonNode node = oc.readTree(jp);
			final String status = node.get("status").asText();
			if (status.equals(ResponseMessageEvent.SUCCESS)) {
				final String server = node.get("response").get("server").asText();
				final String filePath = node.get("response").get("filePath").asText();
				final String searchText = node.get("response").get("searchText").asText();
				final long count = node.get("response").get("count").asLong();
				return ResponseMessageEvent.success(TextSearchResponse.of(server, filePath, searchText, count));
			} else {
				final String message = node.get("response").get("message").asText();
				return ResponseMessageEvent.error(ErrorResponse.of(message, status));
			}
		} catch (IOException e) {
			log.error("Response Message Deserialization failed.");
			throw new CustomAppException(MessageProperty.RESPONSE_MESSAGE_EVENT_DESERIALIZATION_ERROR);
		} finally {
			log.debug("Response Message Deserialization completed.");
		}
	}
}
