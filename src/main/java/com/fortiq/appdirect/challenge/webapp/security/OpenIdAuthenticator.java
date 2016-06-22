package com.fortiq.appdirect.challenge.webapp.security;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.cdi.SocialAuth;
import org.picketlink.authentication.BaseAuthenticator;
import org.picketlink.idm.model.basic.User;

import java.io.Serializable;

@Named
@RequestScoped
public class OpenIdAuthenticator extends BaseAuthenticator implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger logger;

	@Inject
	SocialAuth socialauth;

    @Inject
    private IdentityServices identityServices;

	private String openId;

	private boolean isValidation;

	@Override
	public void authenticate() {
		if (isValidation()) {
			connect();
		} else {
			redirect();
		}
	}

	public void connect() {
		Profile profile = null; 
		try {
			socialauth.connect();
			profile = socialauth.getUserProfile();
		} catch (Exception e) {
			logger.error("Error logging in through SocialAuth", e);
			setStatus(AuthenticationStatus.FAILURE);
			return;
		}
		User user = identityServices.getUserForOpenId(profile.getValidatedId());
		if( user == null ) {
			setStatus(AuthenticationStatus.FAILURE);
		} else {
			setAccount( user );
			setStatus(AuthenticationStatus.SUCCESS);
		}
	}

	public void redirect() {
		socialauth.init();
		socialauth.setViewUrl("/openIdValidate.xhtml");
		socialauth.setId(openId);
		try {
			socialauth.login();
		} catch (Exception e) {
			logger.error("Error logging in through SocialAuth", e);
			setStatus(AuthenticationStatus.FAILURE);
		}
		setStatus(AuthenticationStatus.DEFERRED);
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public boolean isValidation() {
		return isValidation;
	}

	public void setValidation(boolean isValidation) {
		this.isValidation = isValidation;
	}

	public boolean isOpenIdAuth() {
		return this.openId != null || isValidation();
	}

}
