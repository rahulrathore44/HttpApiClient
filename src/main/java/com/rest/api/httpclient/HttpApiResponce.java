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
	
	@Override
	public String toString() {
		return String.format("%1s=%2d, %3s=%4s", "Status Code",this.statusCode,"Response Content",this.responceContent);
	}

}
