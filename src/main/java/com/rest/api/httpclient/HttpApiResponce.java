package com.rest.api.httpclient;

public class HttpApiResponce {
	
	int statusCode;
	
	public int getStatusCode() {
		return statusCode;
	}

	public String getResponceContent() {
		return responceContent;
	}

	String responceContent;
	
	public HttpApiResponce(int statusCode,String responceContent) {
		this.statusCode = statusCode;
		this.responceContent = responceContent;
	}

}
