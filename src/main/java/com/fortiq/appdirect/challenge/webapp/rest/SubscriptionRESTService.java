/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fortiq.appdirect.challenge.webapp.rest;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Enumeration;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.picketlink.idm.model.basic.User;

import com.fortiq.appdirect.challenge.webapp.rest.subscription.SubscriptionServices;
import com.fortiq.appdirect.challenge.webapp.rest.subscription.model.ADOrderError;
import com.fortiq.appdirect.challenge.webapp.rest.subscription.model.ADOrderEvent;
import com.fortiq.appdirect.challenge.webapp.rest.subscription.model.ADOrderSuccess;
import com.fortiq.appdirect.challenge.webapp.rest.utils.ADRestRequestUtils;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the
 * members table.
 */
@Path("/subscription")
@Produces("application/xml")
@RequestScoped
public class SubscriptionRESTService {

	@Context
	private HttpServletRequest httpRequest;

	@Inject
	private Logger log;

	@Inject
	ADRestRequestUtils aDRestRequestUtils;
	
	@Inject
	private SubscriptionServices subscriptionServices;

	private static final String SAMPLE_CONTENTS = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><event xmlns:atom=\"http://www.w3.org/2005/Atom\"><type>SUBSCRIPTION_ORDER</type><marketplace><baseUrl>https://www.appdirect.com</baseUrl><partner>APPDIRECT</partner></marketplace><flag>DEVELOPMENT</flag><creator><email>jacques.lemire@gmail.com</email><firstName>Jacques</firstName><language>en</language><lastName>Lemire</lastName><openId>https://www.appdirect.com/openid/id/6583d099-f5a2-4a3e-aa29-559dc43fabe4</openId><uuid>6583d099-f5a2-4a3e-aa29-559dc43fabe4</uuid></creator><payload><company><country>US</country><name>Fortiq inc.</name><phoneNumber>514-604-0886</phoneNumber><uuid>3e76a881-a784-401e-88fd-655477dae0c0</uuid></company><configuration/><order><editionCode>free</editionCode><pricingDuration>MONTHLY</pricingDuration></order></payload></event>";

	@GET
	@Path("/create")
	public Response create(@QueryParam("url") String urlStr) {
		log.info("create: " + urlStr);
		try {
			return doCreate(urlStr);
		} catch (Exception e) {
			return createErrorResponse("UNKNOWN_ERROR", "Unknown error");
		}
	}

	private Response doCreate(String urlStr) {
		String contents = null;
		try {
			contents = aDRestRequestUtils.getEventContents(urlStr);
		} catch( Exception e ) {
			log.error("Error retrieving the appdirect event", e);
			return createErrorResponse("TRANSPORT_ERROR", "Error retrieving the appdirect event");
		}
		log.info("Response body: " + contents);
		
		ADOrderEvent event = null;
		try {
			event = parseOrderEvent(contents);
		} catch (JAXBException jaxbe) {
			log.error("Error parsing the event response", jaxbe);
			return createErrorResponse("INVALID_RESPONSE", "Error parsing the event response");
		}
		
		User user = subscriptionServices.getUser(event);
		if (user != null) {
			return createErrorResponse("USER_ALREADY_EXISTS", "Account already exists: " + user.getLoginName());
		}
		user = subscriptionServices.createUser(event);
		ADOrderSuccess success = new ADOrderSuccess();
		success.setAccountIdentifier(user.getLoginName());
		return Response.ok().entity(success).build();
	}

	private Response createErrorResponse(String errorCode, String message) {
		ADOrderError error = new ADOrderError();
		error.setErrorCode(errorCode);
		error.setMessage(message);
		return Response.ok().entity(error).build();
	}

	private void traceRequest() {
		Enumeration<String> headerNames = httpRequest.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = httpRequest.getHeader(key);
			log.info(key + ": " + value);
		}
	}

	private String getMockResponse() {
		return SAMPLE_CONTENTS;
	}

	private ADOrderEvent parseOrderEvent(String xml) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(ADOrderEvent.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (ADOrderEvent) unmarshaller.unmarshal(new StringReader(xml));
	}

}
