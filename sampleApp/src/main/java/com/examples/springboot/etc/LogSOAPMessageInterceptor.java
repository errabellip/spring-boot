package com.examples.springboot.etc;

import java.io.StringWriter;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

public class LogSOAPMessageInterceptor implements ClientInterceptor {

	private static Logger logger = LoggerFactory.getLogger(LogSOAPMessageInterceptor.class);

	@Override
	public void afterCompletion(MessageContext arg0, Exception arg1) throws WebServiceClientException {
		// No-op
	}

	@Override
	public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
		WebServiceMessage webServiceMessage = messageContext.getResponse();
		SaajSoapMessage message = (SaajSoapMessage) webServiceMessage;
		logSoapMessage("SOAP fault message:", message.getSaajMessage());
		return true;
	}

	@Override
	public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
		WebServiceMessage webServiceMessage = messageContext.getRequest();
		SaajSoapMessage message = (SaajSoapMessage) webServiceMessage;
		logSoapMessage("SOAP request message:", message.getSaajMessage());
		return true;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
		WebServiceMessage webServiceMessage = messageContext.getResponse();
		SaajSoapMessage message = (SaajSoapMessage) webServiceMessage;
		logSoapMessage("SOAP response message :", message.getSaajMessage());
		return true;
	}

	private String getSoapMessageToString(Source source) {
		String soapReqMsgString = null;
		Transformer transformer = null;
		StreamResult result = null;
		try {

			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			result = new StreamResult(new StringWriter());
			transformer.transform(source, result);

		} catch (TransformerConfigurationException e) {
			logger.error(e.getMessage());
		} catch (TransformerFactoryConfigurationError e) {
			logger.error(e.getMessage());
		} catch (TransformerException e) {
			logger.error(e.getMessage());
		}

		soapReqMsgString = result.getWriter().toString();

		return soapReqMsgString;
	}

	private void logSoapMessage(String logMsg, SOAPMessage soapMessage) {

		Source source = null;
		try {
			source = soapMessage.getSOAPPart().getContent();
		} catch (SOAPException e) {
			logger.error(e.getMessage());
		}
		String soapRequestString = getSoapMessageToString(source);
		if (logger.isDebugEnabled()) {
			logger.debug(logMsg + "\n\n" + soapRequestString);
		}

	}
}

