package com.fortiq.appdirect.challenge.webapp.security;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.config.ConfigProperty;
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
	
	@Inject
    @ConfigProperty(name = "appdirect.logout.url")
	private String adLogoutUrl;
	
	public String logout() throws IOException {
		
		User user = (User)identity.getAccount();
		identity.logout();
		
		// AppDirect users are redirected to a predefined url
		if( isAppDirectUser( user ) ) {
			facesContext.getExternalContext().redirect(adLogoutUrl + user.getLoginName());
			return null;
		}
		return "loggedOut";
	}
	
	private boolean isAppDirectUser( User user ) {
		Attribute<Serializable> isAppDirect = user.getAttribute( SubscriptionServices.APPDIRECT_USER_ATTRIBUTE );
		return isAppDirect == null ? false : (Boolean)isAppDirect.getValue();
	}
}
