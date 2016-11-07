package com.rest.api;


import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import com.rest.api.httpclient.HttpApiClient;
import com.rest.api.httpclient.HttpApiResponce;


public class TestPutMethod {
	
	@Test
	public void testPut() {
		String jsonContent = "{" +
				  "\"firstName\": \"FirstTest" + (int)(Math.random() * 100) +  "\"," +
				  "\"lastName\": \"LastName" + (int)(Math.random() * 100) + "\"," +
				  "\"trusted\": false" +
				"}";
		HttpApiResponce responce = HttpApiClient.Put("http://localhost:8080/landlords/iCDg3eSE", jsonContent,null);
		System.out.println(responce.getStatusCode());
		System.out.println(responce.getResponceContent());
		Assert.assertTrue("Status Code is not Proper", HttpStatus.SC_OK == responce.getStatusCode() || HttpStatus.SC_NOT_FOUND == responce.getStatusCode());
	}

}
