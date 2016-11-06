package com.rest.api;


import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import com.rest.api.httpclient.HttpApiClient;
import com.rest.api.httpclient.HttpApiResponce;



public class TestGetMethod {
	
	@Test
	public void testGet() {
		HttpApiResponce response = HttpApiClient.Get("http://localhost:8080/landlords");
		System.out.println(response.getStatusCode());
		System.out.println(response.getResponceContent());
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
	}
	
	@Test
	public void testGetWithId() {
		HttpApiResponce responce = HttpApiClient.Get("http://localhost:8080/landlords/rkPqhyJW");
		System.out.println(responce.getStatusCode());
		System.out.println(responce.getResponceContent());
		Assert.assertTrue("Status Code is not Proper", responce.getStatusCode() == HttpStatus.SC_OK || responce.getStatusCode() == HttpStatus.SC_NOT_FOUND);
	}

}
