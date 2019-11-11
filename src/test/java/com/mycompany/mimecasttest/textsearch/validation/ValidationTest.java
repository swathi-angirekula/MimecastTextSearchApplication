package com.mycompany.mimecasttest.textsearch.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mycompany.mimecasttest.textsearch.controller.TextSearchEngine;
import com.mycompany.mimecasttest.textsearch.exception.CustomAppException;
import com.mycompany.mimecasttest.textsearch.message.TextSearchRequest;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.main.web-application-type=reactive")
public class ValidationTest {

	@Autowired
	TextSearchEngine fileSearchService;

	@Test
	void invalidPathException() {
		TextSearchRequest sr = TextSearchRequest.of("notlocalhost", "aaa", "bbb");
		Assertions.assertThrows(CustomAppException.class, () -> fileSearchService.search(sr));
	}
}
