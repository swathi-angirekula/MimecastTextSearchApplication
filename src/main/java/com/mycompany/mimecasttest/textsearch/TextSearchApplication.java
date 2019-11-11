package com.mycompany.mimecasttest.textsearch;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.web.reactive.config.EnableWebFlux;

import com.mycompany.mimecasttest.textsearch.properties.ApplicationProperties;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Swathi Angirekula
 *
 */
@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
@EnableWebFlux
@EnableIntegration
public class TextSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(TextSearchApplication.class, args);
		TextSearchApplication app = new TextSearchApplication();
		app.displayMessage();
		Scanner sc = new Scanner(System.in);
		String confirmExit = "";

		while (!confirmExit.equalsIgnoreCase("Y")) {
			confirmExit = sc.nextLine();
		}
		sc.close();
		System.exit(0);
	}

	/**
	 * This method is used to display static message at the application Startup for
	 * the users.
	 */
	public void displayMessage() {
		String msg = "\n********************************************\n"
				+ "The TextSearchApplication  application is started.\n"
				+ "********************************************\n"
				+ "Refer to the Swagger documentation at : http://localhost:8080/swagger-ui.html\n"
				+ "-----------------------------------------------------------------------------\n"
				+ "Enter Y/y to stop.\n";
		log.info(msg);
	}

}
