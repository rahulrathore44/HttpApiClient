package com.rest.api;


import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rest.api.httpclient.HttpApiAsyncClient;
import com.rest.api.httpclient.HttpApiClient;
import com.rest.api.httpclient.HttpApiResponce;
import com.rest.api.httpclient.HttpsAsyncApiClient;
import com.rest.api.model.ResponceBody;

class GetLookup extends Thread{
	
	@Override
	public void run() {
		HttpApiResponce response = HttpApiAsyncClient.Get("http://localhost:8080/landlords",null);
		System.out.println(response.getStatusCode());
		System.out.println(response.getResponceContent());
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
	}
}

public class TestGetMethod {
	
	@Test
	public void testGet() {
		@SuppressWarnings("serial")
		HttpApiResponce response = HttpApiClient.Get("http://localhost:8080/landlords",new HashMap<String, String>(){{
			put("test", "One");
		}});
		System.out.println(response.getStatusCode());
		System.out.println(response.getResponceContent());
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
	}
	
	@Test
	public void testGetWithId() {
		HttpApiResponce responce = HttpApiClient.Get("http://localhost:8080/landlords/rkPqhyJW",null);
		System.out.println(responce.getStatusCode());
		System.out.println(responce.getResponceContent());
		Assert.assertTrue("Status Code is not Proper", responce.getStatusCode() == HttpStatus.SC_OK || responce.getStatusCode() == HttpStatus.SC_NOT_FOUND);
	}
	
	@Test
	public void testGetAsync() {
		@SuppressWarnings("serial")
		HttpApiResponce response = HttpApiAsyncClient.Get("http://localhost:8080/landlords",new HashMap<String, String>(){{
			put("test", "One");
		}});
		System.out.println(response.getStatusCode());
		System.out.println(response.getResponceContent());
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
	}
	
	@Test
	public void testGetAsyncWithMultipleThread() throws InterruptedException {
		GetLookup lookup[] = new GetLookup[20];
		for (int i = 0; i < lookup.length; i++) {
			lookup[i] = new GetLookup();
			lookup[i].start();
			lookup[i].join();
		}
	}
	
	@Test
	public void testGetWithDeserialization() {
		HttpApiResponce response = HttpApiAsyncClient.Get("http://localhost:8080/landlords",null);
		System.out.println(response.getStatusCode());
		System.out.println(response.getResponceContent());
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
		GsonBuilder builder = new GsonBuilder();
		Gson responceBody = builder.serializeNulls().setPrettyPrinting().create();
		ResponceBody[] output = responceBody.fromJson(response.getResponceContent(), ResponceBody[].class);
		Assert.assertNotNull(output);
	}
	
	@Test
	public void tetGetWithSSl() {
		HttpApiResponce reponse = null;
		try {
			reponse = HttpsAsyncApiClient.GetWithSSl("https://www.youtube.com/", null);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println(reponse.getStatusCode());
		System.out.println(reponse.getResponceContent());
	}

}
