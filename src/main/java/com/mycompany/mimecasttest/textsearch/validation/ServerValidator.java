package com.mycompany.mimecasttest.textsearch.validation;

import java.util.Set;

import com.mycompany.mimecasttest.textsearch.exception.CustomAppException;
import com.mycompany.mimecasttest.textsearch.properties.MessageProperty;

import lombok.RequiredArgsConstructor;

/**
 * @author Swathi Angirekula
 *
 */
@RequiredArgsConstructor(staticName = "of")
public class ServerValidator implements Validator {

	private final String server;
	private final Set<String> servers;

	@Override
	public void run() {
		if (!servers.contains(server)) {
			throw new CustomAppException(MessageProperty.INVALID_SERVER.bind(server));
		}
	}

}
