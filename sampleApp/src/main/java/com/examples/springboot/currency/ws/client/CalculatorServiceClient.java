package com.examples.springboot.currency.ws.client;

import javax.annotation.PostConstruct;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import com.examples.springboot.beans.ApiServiceFallBack;
import com.examples.springboot.beans.SoapServiceGetMessageResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * 
 * @author Prashanth Errabelli
 *
 */

@Component
public class CalculatorServiceClient extends WebServiceClient {

	private static final Logger logger = LoggerFactory.getLogger(CalculatorServiceClient.class);

	private static final String CONTEXT_PATH = "org.tempuri";

	// @Value("${hystrix.command.NotificationServiceGetMessage.execution.isolation.thread.timeoutInMilliseconds}")
	private int httpConnectionReadTimeout;

	private WebServiceTemplate getMessageWebServiceTemplate;

	private WebServiceTemplate deleteMessageWebServiceTemplate;

	@Autowired
	public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;

	@Autowired
	public RequestConfig defaultRequestConfig;

	@Autowired
	public SaajSoapMessageFactory saajSoapMessageFactory;

	@PostConstruct
	public void init() {

		// Single template should be fine, kept to add flexibility while adding
		// variable timeout
		// getMessageWebServiceTemplate =
		// this.buildWebServiceTemplate(poolingHttpClientConnectionManager,
		// defaultRequestConfig, saajSoapMessageFactory, CONTEXT_PATH,
		// httpConnectionReadTimeout);
		// deleteMessageWebServiceTemplate =
		// this.buildWebServiceTemplate(poolingHttpClientConnectionManager,
		// defaultRequestConfig, saajSoapMessageFactory, CONTEXT_PATH,
		// httpConnectionReadTimeout);
	}

	@HystrixCommand(commandKey = "NotificationServiceGetMessage", fallbackMethod = "getNotificationMessagesFallBack", threadPoolKey = "NotificationServiceGetMessagePool")
	public SoapServiceGetMessageResponse add(int a, int b) {

		return null;
	}

	public SoapServiceGetMessageResponse getNotificationMessagesFallBack(int a, int b, Throwable t) {

		if (t != null) {
			logger.error("Hystrix : During Notification GetMessage soap service call ", t);
		}

		ApiServiceFallBack serviceFallBack = null;
		if (t instanceof SoapFaultClientException) {
			SoapFaultClientException soapfault = (SoapFaultClientException) t;
			String faultMsg = soapfault.getFaultCode() + ":" + soapfault.getFaultCode().toString();
			serviceFallBack = new ApiServiceFallBack(faultMsg);
		} else {
			serviceFallBack = new ApiServiceFallBack("Backend api service is either down or under maintanence. ");

		}
		return new SoapServiceGetMessageResponse(-1, serviceFallBack);
	}
}
