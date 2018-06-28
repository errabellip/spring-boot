package com.examples.springboot.currency.ws.client;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import org.springframework.ws.transport.http.HttpComponentsMessageSender.RemoveSoapHeadersInterceptor;

import com.examples.springboot.etc.LogSOAPMessageInterceptor;

public class WebServiceClient {

	private static Logger logger = LoggerFactory.getLogger(WebServiceClient.class);

	// @Value("${notification.endpoint}")
	private String notificationEndpoint;

	protected WebServiceTemplate buildWebServiceTemplate(PoolingHttpClientConnectionManager httpClientConnectionManager,
			RequestConfig defaultRequestConfig, SaajSoapMessageFactory messageFactory, String contextPath,
			int readTimeout) {

		final RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig).setSocketTimeout(readTimeout)
				.build();
		final HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(httpClientConnectionManager)
				.setDefaultRequestConfig(requestConfig).addInterceptorFirst(new RemoveSoapHeadersInterceptor()).build();

		final WebServiceTemplate webServiceTemplate = new WebServiceTemplate(messageFactory);
		webServiceTemplate.setMessageSender(new HttpComponentsMessageSender(httpClient));
		ClientInterceptor[] interceptors = new ClientInterceptor[] { new LogSOAPMessageInterceptor() };
		webServiceTemplate.setInterceptors(interceptors);
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		try {
			marshaller.setContextPath(contextPath);
			marshaller.afterPropertiesSet();
		} catch (Exception ex) {
			logger.error("Error setting properties on Jaxb2Marshaller: ", ex);
		}
		webServiceTemplate.setDefaultUri(notificationEndpoint);
		webServiceTemplate.setMarshaller(marshaller);
		webServiceTemplate.setUnmarshaller(marshaller);
		return webServiceTemplate;
	}

}
