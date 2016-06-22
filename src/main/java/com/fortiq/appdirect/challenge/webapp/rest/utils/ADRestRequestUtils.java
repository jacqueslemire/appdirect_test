package com.fortiq.appdirect.challenge.webapp.rest.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import oauth.signpost.OAuthConsumer;

@Stateless
public class ADRestRequestUtils {

	@Inject
	private Logger log;
	
	@Inject
	OAuthConsumer oAuthConsumer;

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
