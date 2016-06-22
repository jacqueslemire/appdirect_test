package com.fortiq.appdirect.challenge.webapp.rest.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import oauth.signpost.OAuthConsumer;

@Stateless
public class ADRestRequestUtils {

	@Inject
	private Logger log;
	
	@Inject
	OAuthConsumer oAuthConsumer;

	public <X> X getEvent( Class<X> clazz, String eventUrl ) throws ADException {
		String contents = null;
		try {
			contents = getEventContents(eventUrl);
		} catch( Exception e ) {
			log.error("Error retrieving the appdirect event", e);
			throw new ADException( "TRANSPORT_ERROR", "Error retrieving the appdirect event" );
		}
		log.info("Response body: " + contents);
		
		X event = null;
		try {
			event = parseOrderEvent(clazz, contents);
		} catch (JAXBException jaxbe) {
			log.error("Error parsing the event response", jaxbe);
			throw new ADException( "INVALID_RESPONSE", "Error parsing the event response" );
		}
		return event;
	}
	
	@SuppressWarnings("unchecked")
	private <X> X parseOrderEvent( Class<X> clazz, String xml) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (X) unmarshaller.unmarshal(new StringReader(xml));
	}
	
	public String getEventContents(String urlStr) throws Exception {
		HttpURLConnection request = (HttpURLConnection) new URL(urlStr).openConnection();
		oAuthConsumer.sign(request);
		request.connect();
		if ((request.getResponseCode() / 100) != 2) {
			throw new Exception("Unexpected error code: " + request.getResponseCode() + " " + request.getResponseMessage());
		}
		String content = getContents(request);
		log.info(request.getHeaderFields());
		return content;
	}

	private String getContents(HttpURLConnection connection) throws IOException {
		InputStream in = connection.getInputStream();
		String encoding = connection.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		return IOUtils.toString(in, encoding);
	}

}
