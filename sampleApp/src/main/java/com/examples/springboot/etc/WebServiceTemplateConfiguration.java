package com.examples.springboot.etc;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

@Configuration
public class WebServiceTemplateConfiguration {

	private static Logger logger = LoggerFactory.getLogger(WebServiceTemplateConfiguration.class);

	@Value("${ws.http.pool.config.max_total}")
	private int httpPoolConfigMaxTotal;
	@Value("${ws.http.pool.config.max_per_route}")
	private int httpPoolConfigMaxPerRoute;
	@Value("${ws.http.pool.connection.request_timeout}")
	private int connectionRequestTimeout;
	@Value("${ws.http.connection_timeout}")
	private int connectionTimeout;
	@Value("${ws.http.socket_timeout}")
	private int socketTimeout;

	@Bean
	public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(httpPoolConfigMaxTotal);
		connectionManager.setDefaultMaxPerRoute(httpPoolConfigMaxPerRoute);
		return connectionManager;
	}

	@Bean
	public RequestConfig defaultRequestConfig() {
		return RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
				.setConnectTimeout(connectionTimeout).setSocketTimeout(socketTimeout).build();
	}

	@Bean
	public SaajSoapMessageFactory saajSoapMessageFactory() {
		SaajSoapMessageFactory messageFactory = null;
		try {
			messageFactory = new SaajSoapMessageFactory(MessageFactory.newInstance());
			messageFactory.afterPropertiesSet();
		} catch (SOAPException e) {
			logger.error("Error creating SOAP Message Factory: ", e);
		}
		return messageFactory;
	}
}
