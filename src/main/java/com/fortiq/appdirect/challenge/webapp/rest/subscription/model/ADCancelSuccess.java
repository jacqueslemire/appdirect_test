package com.fortiq.appdirect.challenge.webapp.rest.subscription.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "result" )
public class ADCancelSuccess {
	
	private Boolean success = true;
	
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
}
