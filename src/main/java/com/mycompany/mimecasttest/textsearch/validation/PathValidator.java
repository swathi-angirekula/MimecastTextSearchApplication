package com.mycompany.mimecasttest.textsearch.validation;

import java.io.File;

import com.mycompany.mimecasttest.textsearch.exception.CustomAppException;
import com.mycompany.mimecasttest.textsearch.properties.MessageProperty;

import lombok.RequiredArgsConstructor;

/**
 * @author Swathi Angirekula
 *
 */
@RequiredArgsConstructor
public class PathValidator implements Validator {

	private final File file;
	private final String server;

	public static PathValidator of(File file, String server) {
		return new PathValidator(file, server);
	}

	@Override
	public void run() {
		if (!file.exists()) {
			throw new CustomAppException(MessageProperty.INVALID_PATH.bind(file.getPath(), server));
		}
	}
}
