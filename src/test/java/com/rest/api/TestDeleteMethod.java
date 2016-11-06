package com.rest.api;



import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import com.rest.api.httpclient.HttpApiClient;
import com.rest.api.httpclient.HttpApiResponce;

public class TestDeleteMethod {
	
	@Test
	public void testDelete() {
		HttpApiResponce responce = HttpApiClient.Delete("http://localhost:8080/landlords/mYAtYSJy");
		System.out.println(responce.getStatusCode());
		System.out.println(responce.getResponceContent());
		Assert.assertTrue(responce.getStatusCode() == HttpStatus.SC_OK || responce.getStatusCode() == HttpStatus.SC_NOT_FOUND);
	}

}
