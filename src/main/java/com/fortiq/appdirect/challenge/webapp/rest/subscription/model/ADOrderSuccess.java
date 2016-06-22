package com.fortiq.appdirect.challenge.webapp.rest.subscription.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "result" )
public class ADOrderSuccess {
	
	private Boolean success = true;
	private String accountIdentifier;
	
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getAccountIdentifier() {
		return accountIdentifier;
	}
	public void setAccountIdentifier(String accountIdentifier) {
		this.accountIdentifier = accountIdentifier;
	}
	
}
