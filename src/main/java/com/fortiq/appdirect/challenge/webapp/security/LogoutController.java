package com.fortiq.appdirect.challenge.webapp.security;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.picketlink.Identity;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.basic.User;

import com.fortiq.appdirect.challenge.webapp.rest.subscription.SubscriptionServices;

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
		Attribute<Serializable> isAppDirect = user.getAttribute( SubscriptionServices.APPDIRECT_USER_ATTRIBUTE );
		Boolean isOpenId = isAppDirect == null ? false : (Boolean)isAppDirect.getValue();
		identity.logout();
		if( isOpenId != null && isOpenId ) {
			facesContext.getExternalContext().redirect("http://www.appdirect.com/applogout?openid=" + login); // Should be configurable obviously
			return null;
		}
		return "loggedOut";
	}
}
