package com.rest.api.httpclient;

import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;

public class HttpApiAsyncClient {
	
	
	private static CloseableHttpAsyncClient getHttpAsyncClient() {
		return HttpAsyncClientBuilder.create().build();
	}
	
	private static ResponseHandler<String> getBasicResponceHandler() {
		return new BasicResponseHandler();
	}
	
	public static HttpApiResponce Get(String uri) {
		try {
			return Get(new URI(uri));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public static HttpApiResponce Get(URI uri) throws InterruptedException, ExecutionException {
		Future<HttpResponse> httpResponce = null;
		HttpUriRequest get = RequestBuilder.get(uri).build();
		
		try(CloseableHttpAsyncClient httpClient = getHttpAsyncClient()) {
			httpClient.start();
			httpResponce = httpClient.execute(get, null);
			String responceContent = getBasicResponceHandler().handleResponse(httpResponce.get());
			return new HttpApiResponce(httpResponce.get().getStatusLine().getStatusCode(), responceContent);
		} catch (Exception e) {
			if(e instanceof HttpResponseException)
				return new HttpApiResponce(httpResponce.get().getStatusLine().getStatusCode(), e.getMessage());
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

}
