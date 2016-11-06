package com.rest.api.httpclient;

import java.io.File;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
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
	
	private static HttpEntity getHttpEntityType(Object httpEntity) {
		if(httpEntity instanceof String)
			return new StringEntity((String)httpEntity, ContentType.APPLICATION_JSON);
		return new FileEntity((File)httpEntity, ContentType.APPLICATION_JSON);
		
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
			httpResponce = httpClient.execute(get, new FutureCallback<HttpResponse>() {
				@Override
				public void failed(Exception ex) {
				}
				@Override
				public void completed(HttpResponse result) {
					System.out.println("Current : " + Thread.currentThread().getId());
				}
				@Override
				public void cancelled() {
				}
			});
			String responceContent = getBasicResponceHandler().handleResponse(httpResponce.get());
			return new HttpApiResponce(httpResponce.get().getStatusLine().getStatusCode(), responceContent);
		} catch (Exception e) {
			if(e instanceof HttpResponseException)
				return new HttpApiResponce(httpResponce.get().getStatusLine().getStatusCode(), e.getMessage());
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public static HttpApiResponce Post(URI uri,Object content) throws InterruptedException, ExecutionException {
		Future<HttpResponse> httpResponse = null;
		HttpPost post = new HttpPost(uri);
		post.setEntity(getHttpEntityType(content));
		try(CloseableHttpAsyncClient httpClient = getHttpAsyncClient()) {
		httpResponse = httpClient.execute(post, null);
		String responceContent = getBasicResponceHandler().handleResponse(httpResponse.get());
		return new HttpApiResponce(httpResponse.get().getStatusLine().getStatusCode(), responceContent);
		} catch (Exception e) {
			if(e instanceof HttpResponseException)
				return new HttpApiResponce(httpResponse.get().getStatusLine().getStatusCode(), e.getMessage());
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

}
