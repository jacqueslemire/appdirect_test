package com.fortiq.appdirect.challenge.webapp.security;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.Authenticator;
import org.picketlink.authentication.internal.IdmAuthenticator;

public class AuthenticationProducer {

	@Inject Instance<OpenIdAuthenticator> openIdAuthenticator;
	@Inject Instance<IdmAuthenticator> idmAuthenticator;
	
	@Produces
	@PicketLink
	public Authenticator selectAuthenticator() {
		OpenIdAuthenticator openIdAuth = openIdAuthenticator.get();
		if( openIdAuth.isOpenIdAuth() ) {
			return openIdAuth;
		} else {
			return idmAuthenticator.get();
		}
		
    }
	
}
