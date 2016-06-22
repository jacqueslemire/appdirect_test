package com.fortiq.appdirect.challenge.webapp.rest.subscription.model;

public class ADOrder {

	private String editionCode;
	private String pricingDuration;
	private ADOrderItem item;
	
	public String getEditionCode() {
		return editionCode;
	}
	public void setEditionCode(String editionCode) {
		this.editionCode = editionCode;
	}
	public String getPricingDuration() {
		return pricingDuration;
	}
	public void setPricingDuration(String pricingDuration) {
		this.pricingDuration = pricingDuration;
	}
	public ADOrderItem getItem() {
		return item;
	}
	public void setItem(ADOrderItem item) {
		this.item = item;
	}
	
}
