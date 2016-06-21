package com.fortiq.appdirect.challenge.webapp.security;

import java.io.IOException;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.picketlink.Identity;
import org.picketlink.idm.model.basic.User;

@Named
@Stateless
public class LogoutController {
	
	@Inject
	private FacesContext facesContext;
	
	@Inject
	private Identity identity;
	
	public String logout() throws IOException {
		User user = (User)identity.getAccount();
		String login = user.getLoginName();
		Boolean isOpenId = (Boolean)user.getAttribute( OpenIdAuthenticator.OPENID_USER_ATTRIBUTE ).getValue();
		identity.logout();
		if( isOpenId != null && isOpenId ) {
			facesContext.getExternalContext().redirect("http://www.appdirect.com/applogout?openid=" + login); // Should be configurable obviously
			return null;
		}
		return "loggedOut";
	}
}
