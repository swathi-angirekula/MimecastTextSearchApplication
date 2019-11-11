package com.mycompany.mimecasttest.textsearch.controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.mimecasttest.textsearch.properties.ApplicationProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Swathi Angirekula
 *
 */
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class ServerController {

	private final ApplicationProperties applicationProperties;

	@GetMapping(value = "/servers")
	public List<String> getServers() {
		return applicationProperties.getServersAsSet().stream().filter(ServerController::pingServer)
				.collect(Collectors.toList());
	}

	public static boolean pingServer(String url) {
		try {
			url = "http://".concat(url).concat(":8080");
			log.debug("Trying to ping : " + url);
			URL u = new URL(url);
			try (Socket socket = new Socket()) {
				socket.connect(new InetSocketAddress(u.getHost(), u.getPort()), 1000);
				return true;
			} catch (IOException e) {
				log.debug("Unable to ping : " + url + ".Ignoring this Server.");
				return false;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
