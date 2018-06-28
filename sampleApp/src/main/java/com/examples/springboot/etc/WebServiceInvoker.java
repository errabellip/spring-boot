package com.examples.springboot.etc;

import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;

public class WebServiceInvoker<T> {

	private T request;
	private String soapAction;

	public WebServiceInvoker(T request,String soapAction) {
		super();
		this.request = request;
		this.soapAction = soapAction;
	}

	public <V> V invokeSOAPService(WebServiceTemplate webServiceTemplate) {
		@SuppressWarnings("unchecked")
		V response = (V) webServiceTemplate.marshalSendAndReceive(request, message -> {
			SoapMessage soapMessage = (SoapMessage) message;
			soapMessage.setSoapAction(soapAction);
		});
		return response;
	}
}

