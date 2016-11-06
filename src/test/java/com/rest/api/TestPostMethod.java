package com.rest.api;

import java.io.File;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import com.rest.api.httpclient.HttpApiClient;
import com.rest.api.httpclient.HttpApiResponce;


public class TestPostMethod {
	
	@Test
	public void testPost() {
		String jsonContent = "{" +
			  "\"firstName\": \"FirstTest" + (int)(Math.random() * 100) +  "\"," +
			  "\"lastName\": \"LastName" + (int)(Math.random() * 100) + "\"," +
			  "\"trusted\": false" +
			"}";
		HttpApiResponce responce = HttpApiClient.Post("http://localhost:8080/landlords", jsonContent);
		System.out.println(responce.getStatusCode());
		System.out.println(responce.getResponceContent());
		Assert.assertEquals(HttpStatus.SC_CREATED, responce.getStatusCode());
	}
	
	@Test
	public void testPostFromFile() {
		HttpApiResponce responce = HttpApiClient.Post("http://localhost:8080/landlords", new File("TestData.txt"));
		System.out.println(responce.getStatusCode());
		System.out.println(responce.getResponceContent());
		Assert.assertEquals(HttpStatus.SC_CREATED, responce.getStatusCode());
	}

}
