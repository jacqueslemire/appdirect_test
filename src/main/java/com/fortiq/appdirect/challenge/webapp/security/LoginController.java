package com.fortiq.appdirect.challenge.webapp.security;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.picketlink.Identity;
import org.picketlink.Identity.AuthenticationResult;

@Stateless
@Named
public class LoginController {

	@Inject
	private Identity identity;

	@Inject
	private FacesContext facesContext;

	public String login() {
		AuthenticationResult result = identity.login();
		if (AuthenticationResult.FAILED.equals(result)) {
			facesContext.addMessage(null,
					new FacesMessage("Authentication was unsuccessful. Please check your username and password."));
			return "/error.xhtml";
		}
		return "loggedIn";
	}
	
}
