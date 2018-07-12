package com.examples.springboot;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.ws.config.annotation.EnableWs;

/**
 * 
 * @author Prashanth Errabelli
 *
 */

@SpringBootApplication(scanBasePackages = "com.examples.springboot")
@EnableWs
@EnableWebMvc
public class ExampleApplication2 extends SpringBootServletInitializer {

	private static final String ENVIRONMENT_NAME = "DAI_ENV";

	/*
	 * Create required HandlerMapping, to avoid several default HandlerMapping
	 * instances being created
	 */
	@Bean
	public HandlerMapping handlerMapping() {
		return new RequestMappingHandlerMapping();
	}

	/*
	 * Create required HandlerAdapter, to avoid several default HandlerAdapter
	 * instances being created
	 */
	@Bean
	public HandlerAdapter handlerAdapter() {
		return new RequestMappingHandlerAdapter();
	}

	/*
	 * optimization - avoids creating default exception resolvers; not required
	 * as the serverless container handles all exceptions
	 *
	 * By default, an ExceptionHandlerExceptionResolver is created which creates
	 * many dependent object, including an expensive ObjectMapper instance.
	 */
	@Bean
	public HandlerExceptionResolver handlerExceptionResolver() {
		return new HandlerExceptionResolver() {

			@Override
			public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
					Object handler, Exception ex) {
				return null;
			}
		};
	}

	public static void main(String[] args) {

		String environmentName = System.getenv(ENVIRONMENT_NAME);
		new SpringApplicationBuilder().sources(ExampleApplication2.class).profiles(environmentName).build().run(args);
	}

}
