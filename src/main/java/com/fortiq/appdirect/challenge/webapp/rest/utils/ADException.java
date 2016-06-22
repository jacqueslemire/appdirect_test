package com.fortiq.appdirect.challenge.webapp.rest.utils;

public class ADException extends Exception {

	private static final long serialVersionUID = 4092107660692374439L;
	
	private String errorCode;
	
	public ADException( String errorCode, String errorMessage ) {
		this( errorCode, errorMessage, null );
	}

	public ADException( String errorCode, String errorMessage, Throwable cause ) {
		super( errorMessage, cause );
		this.errorCode = errorCode;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
}
