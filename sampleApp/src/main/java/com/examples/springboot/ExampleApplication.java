package com.examples.springboot;

import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 
 * @author Prashanth Errabelli
 *
 */

// @SpringBootApplication(scanBasePackages = "com.examples.springboot")
// @EnableAutoConfiguration
public class ExampleApplication {

	private static final String ENVIRONMENT_NAME = "DAI_ENV";

	public static void main(String[] args) {

		String environmentName = System.getenv(ENVIRONMENT_NAME);
		new SpringApplicationBuilder().sources(ExampleApplication.class).profiles(environmentName).build().run(args);
	}

}
