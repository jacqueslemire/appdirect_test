package com.fortiq.appdirect.challenge.webapp.security.openid;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.picketlink.Identity;
import org.picketlink.Identity.AuthenticationResult;

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
			return null;
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
