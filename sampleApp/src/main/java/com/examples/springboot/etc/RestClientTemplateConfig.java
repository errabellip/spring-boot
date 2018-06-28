package com.examples.springboot.etc;

import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Prashanth Errabelli
 * @date Mar 16, 2018 2:01:43 AM
 *
 */
@Configuration
public class RestClientTemplateConfig {

	@Value("${rs.http.pool.config.max_total}")
	private int httpPoolConfigMaxTotal;

	@Value("${rs.http.pool.config.max_per_route}")
	private int httpPoolConfigMaxPerRoute;

	@Value("${rs.http.pool.config.request_timeout}")
	private int connectionRequestTimeout;

	@Value("${rs.http.pool.config.socket_timeout}")
	private int connectionSocketTimeout;

	@Value("${rs.http.pool.config.connection_timeout}")
	private int connectionTimeout;

	@Bean
	public PoolingHttpClientConnectionManager rsPoolingHttpClientConnectionManager() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(httpPoolConfigMaxTotal);
		connectionManager.setDefaultMaxPerRoute(httpPoolConfigMaxPerRoute);
		return connectionManager;
	}

	@Bean
	public RequestConfig rsDefaultRequestConfig() {
		return RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
				.setConnectTimeout(connectionTimeout).setSocketTimeout(connectionSocketTimeout).build();
	}

	@Bean
	public RestTemplate accountsRestTemplate(@Qualifier("rsDefaultRequestConfig") RequestConfig defaultRequestConfig,
			@Qualifier("rsPoolingHttpClientConnectionManager") HttpClientConnectionManager httpClientConnectionManager) {

		final RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig).build();

		final HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(httpClientConnectionManager)
				.setDefaultRequestConfig(requestConfig).build();

		HttpComponentsClientHttpRequestFactory reqFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

		RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(reqFactory));

		List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
		for (HttpMessageConverter<?> converter : converters) {
			if (converter instanceof MappingJackson2HttpMessageConverter) {
				MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) converter;
				jsonConverter.setObjectMapper(new ObjectMapper());
			}
		}

		// restTemplate.setErrorHandler(errorHandler());
		return restTemplate;
	}
}
