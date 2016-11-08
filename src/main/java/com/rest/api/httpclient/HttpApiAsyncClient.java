package com.rest.api.httpclient;

import java.io.File;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;

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
	
	private static Header[] getHeaders(Map<String, String> customHeader) {
		BasicHeader[] header = new BasicHeader[customHeader.size()];
		int i = 0;
		for(String key : customHeader.keySet()){
			header[i++] = new BasicHeader(key, customHeader.get(key));
		}
		return header;
	}
	
	private static HttpApiResponce performRequest(HttpUriRequest method) throws InterruptedException, ExecutionException {
		Future<HttpResponse> httpResponce = null;
		try(CloseableHttpAsyncClient httpClient = getHttpAsyncClient()) {
			httpClient.start();
			httpResponce = httpClient.execute(method, null);
			String responceContent = getBasicResponceHandler().handleResponse(httpResponce.get());
			return new HttpApiResponce(httpResponce.get().getStatusLine().getStatusCode(), responceContent);
		} catch (Exception e) {
			if(e instanceof HttpResponseException)
				return new HttpApiResponce(httpResponce.get().getStatusLine().getStatusCode(), e.getMessage());
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public static HttpApiResponce Get(String uri,Map<String, String> customHeader) {
		try {
			return Get(new URI(uri),customHeader);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public static HttpApiResponce Get(URI uri,Map<String, String> customHeader) throws InterruptedException, ExecutionException {
		HttpUriRequest get = RequestBuilder.get(uri).build();
		
		if(customHeader != null)
			get.setHeaders(getHeaders(customHeader));
		
		return performRequest(get);
	}
	
	
	public static HttpApiResponce Post(String uri,Object content,Map<String, String> customHeader) {
		try {
			return Post(new URI(uri),content,customHeader);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public static HttpApiResponce Post(URI uri,Object content,Map<String, String> customHeader) throws InterruptedException, ExecutionException {
		HttpPost post = new HttpPost(uri);
		
		if(customHeader != null)
			post.setHeaders(getHeaders(customHeader));
		if(content != null)
			post.setEntity(getHttpEntityType(content));
		
		return performRequest(post);
	}
	
	public static HttpApiResponce Put(String uri,Object content,Map<String, String> customHeader) {
		try {
			return Put(new URI(uri),content,customHeader);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public static HttpApiResponce Put(URI uri,Object content,Map<String, String> customHeader) throws InterruptedException, ExecutionException {
		HttpPut put = new HttpPut(uri);
		
		if(customHeader != null)
			put.setHeaders(getHeaders(customHeader));
		if(content != null)
			put.setEntity(getHttpEntityType(content));
		
		return performRequest(put);
		
	}
	
	
	public static HttpApiResponce Delete(String uri,Map<String, String> customHeader) {
		try {
			return Delete(new URI(uri),customHeader);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public static HttpApiResponce Delete(URI uri,Map<String, String> customHeader) throws InterruptedException, ExecutionException {
		
		HttpUriRequest delete = RequestBuilder.delete(uri).build();
		
		if(customHeader != null)
			delete.setHeaders(getHeaders(customHeader));
		
		return performRequest(delete);
	}

}
