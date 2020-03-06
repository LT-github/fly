package com.lt.fly.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix="doc")
@PropertySource(value = "classpath:spring_doc.properties", encoding = "UTF-8")
public class DocProperties {
	private String[] paths;

	public String[] getPaths() {
		return paths;
	}

	public void setPaths(String[] paths) {
		this.paths = paths;
	}
}
