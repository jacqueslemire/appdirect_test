package com.fortiq.appdirect.challenge.webapp.rest.subscription.model;

public class ADCreatePayload {

	private ADCompany company;
	private ADOrder order;
	
	public ADCompany getCompany() {
		return company;
	}
	public void setCompany(ADCompany company) {
		this.company = company;
	}
	public ADOrder getOrder() {
		return order;
	}
	public void setOrder(ADOrder order) {
		this.order = order;
	}
	
}
