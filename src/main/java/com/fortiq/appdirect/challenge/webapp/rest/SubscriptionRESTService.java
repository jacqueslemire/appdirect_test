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

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.picketlink.idm.model.basic.User;

import com.fortiq.appdirect.challenge.webapp.rest.subscription.SubscriptionServices;
import com.fortiq.appdirect.challenge.webapp.rest.subscription.model.ADError;
import com.fortiq.appdirect.challenge.webapp.rest.subscription.model.ADCancelEvent;
import com.fortiq.appdirect.challenge.webapp.rest.subscription.model.ADCancelSuccess;
import com.fortiq.appdirect.challenge.webapp.rest.subscription.model.ADCreateEvent;
import com.fortiq.appdirect.challenge.webapp.rest.subscription.model.ADCreateSuccess;
import com.fortiq.appdirect.challenge.webapp.rest.utils.ADException;
import com.fortiq.appdirect.challenge.webapp.rest.utils.ADRestRequestUtils;

/**
 * JAX-RS AppDirect Subscription service
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

	/**
	 * Implements the AppDirect Subscription Create API
	 * @see {@link <a href="https://docs.appdirect.com/developer/distribution/event-notifications/subscription-events#create-subscription">Create Subscription</a>}
	 */
	@GET
	@Path("/create")
	public Response create(@QueryParam("url") String urlStr) {
		log.info("create: " + urlStr);
		try {
			return doCreate(urlStr);
		} catch (ADException e) {
			return createErrorResponse(e);
		} catch (Exception e) {
			return createErrorResponse("UNKNOWN_ERROR", "Unknown error");
		}
	}

	/**
	 * Implements the AppDirect Subscription Cancel API
	 * @see {@link <a href="https://docs.appdirect.com/developer/distribution/event-notifications/subscription-events#cancel-subscription">Cancel Subscription</a>}
	 */
	@GET
	@Path("/cancel")
	public Response cancel(@QueryParam("url") String urlStr) {
		log.info("cancel: " + urlStr);
		try {
			return doCancel(urlStr);
		} catch (ADException e) {
			return createErrorResponse(e);
		} catch (Exception e) {
			return createErrorResponse("UNKNOWN_ERROR", "Unknown error");
		}
	}
	
	private Response doCreate(String urlStr) throws ADException {
		
		ADCreateEvent event = aDRestRequestUtils.getEvent(ADCreateEvent.class, urlStr);
		
		User user = subscriptionServices.getUser(event.getCreator().getUuid());
		if (user != null) {
			return createErrorResponse("USER_ALREADY_EXISTS", "Account already exists: " + user.getLoginName());
		}
		user = subscriptionServices.createUser(event);
		ADCreateSuccess success = new ADCreateSuccess();
		success.setAccountIdentifier(user.getLoginName());
		return Response.ok().entity(success).build();
	}

	private Response doCancel(String urlStr) throws ADException {
		ADCancelEvent event = aDRestRequestUtils.getEvent(ADCancelEvent.class, urlStr);
		
		User user = subscriptionServices.getUser(event.getPayload().getAccount().getAccountIdentifier());
		if (user == null) {
			return createErrorResponse("ACCOUNT_NOT_FOUND", "Account not found: " + event.getPayload().getAccount().getAccountIdentifier());
		}
		subscriptionServices.deleteUser(user);
		ADCancelSuccess success = new ADCancelSuccess();
		return Response.ok().entity(success).build();
	}

	private Response createErrorResponse(ADException e) {
		return createErrorResponse(e.getErrorCode(), e.getMessage());
	}	
	
	private Response createErrorResponse(String errorCode, String message) {
		ADError error = new ADError();
		error.setErrorCode(errorCode);
		error.setMessage(message);
		return Response.ok().entity(error).build();
	}

}
