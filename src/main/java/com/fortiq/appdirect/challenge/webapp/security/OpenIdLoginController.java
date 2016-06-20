package com.fortiq.appdirect.challenge.webapp.security;

import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.picketlink.Identity;
import org.picketlink.Identity.AuthenticationResult;
import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.Authenticator;
import org.picketlink.authentication.internal.IdmAuthenticator;

@Stateless
@Named
public class OpenIdLoginController {

	@Inject
	private Identity identity;

	@Inject
	private FacesContext facesContext;

	@Inject OpenIdAuthenticator openIdAuthenticator;
	
	public String login() {
		AuthenticationResult result = identity.login();
		if (AuthenticationResult.FAILED.equals(result)) {
			facesContext.addMessage(null,
					new FacesMessage("Error authenticating with the openid provider"));
			return "/error.xhtml";
		}
		return null;
	}

	public void redirectEvent( ComponentSystemEvent cse ) {
		String openId = (String) cse.getComponent().getAttributes().get("openid");
		openIdAuthenticator.setOpenId(openId);
		AuthenticationResult result = identity.login();
		if (AuthenticationResult.FAILED.equals(result)) {
			facesContext.addMessage(null,
					new FacesMessage("Error authenticating with the openid provider"));
		}
	}
	
	public void validateOpenId( ComponentSystemEvent cse ) {
		openIdAuthenticator.setValidation(true);
		AuthenticationResult result = identity.login();
		if (AuthenticationResult.FAILED.equals(result)) {
			facesContext.addMessage(null,
					new FacesMessage("Error validating the openid response"));
			facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, "/error.xhtml");
		} else {
			facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, "loggedIn");
		}
	}

}
