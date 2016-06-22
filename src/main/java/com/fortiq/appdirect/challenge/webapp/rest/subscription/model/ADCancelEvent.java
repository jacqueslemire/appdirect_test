package com.fortiq.appdirect.challenge.webapp.rest.subscription.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "event" )
public class ADCancelEvent {
	
	private String type;
	private ADMarketPlace marketplace;
	private String flag;
	private ADCreator creator;
	private ADCancelPayload payload;

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ADMarketPlace getMarketplace() {
		return marketplace;
	}
	public void setMarketplace(ADMarketPlace marketplace) {
		this.marketplace = marketplace;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public ADCreator getCreator() {
		return creator;
	}
	public void setCreator(ADCreator creator) {
		this.creator = creator;
	}
	public ADCancelPayload getPayload() {
		return payload;
	}
	public void setPayload(ADCancelPayload payload) {
		this.payload = payload;
	}

}
