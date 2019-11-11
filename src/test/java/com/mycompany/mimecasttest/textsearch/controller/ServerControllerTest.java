package com.mycompany.mimecasttest.textsearch.controller;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.main.web-application-type=reactive", webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = "server.port=8080")
class ServerControllerTest {

	public String SERVER = "http://localhost:8080";

	@Mock
	private ServerController serverController;

	@Test
	void test_getServers() throws Exception {
		Mockito.when(serverController.getServers()).thenReturn(Arrays.asList(SERVER));
		Assertions.assertEquals(serverController.getServers().get(0), SERVER);
	}

	@Test
	void test_pingServer() {
		Assertions.assertTrue(ServerController.pingServer("localhost"));
		Assertions.assertFalse(ServerController.pingServer("swathi123"));
	}
}
