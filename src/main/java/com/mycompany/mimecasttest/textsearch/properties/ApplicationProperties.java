package com.mycompany.mimecasttest.textsearch.properties;

import java.util.Arrays;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableSet;

import lombok.Data;

/**
 * @author Swathi Angirekula
 *
 */
@Component
@ConfigurationProperties("myapp")
@Data
public class ApplicationProperties {

	private String servers;
	private String proxyUrl;
	private String frontend;

	@Component
	public static class Documentation {
		public String title;

		public String description;

		public String version;
	}

	public Set<String> getServersAsSet() {
		return ImmutableSet.copyOf(Arrays.stream(this.getServers().split(",")).map(String::trim).iterator());
	}
}
