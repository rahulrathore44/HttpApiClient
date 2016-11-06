package com.rest.api.httpclient;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;


public class HttpApiClient {
	private static CloseableHttpClient getCloseableHttpClient(){
		return HttpClientBuilder.create().build();
	}
	
	private static ResponseHandler<String> getBasicHttpResponse() {
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
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Cannot execute GET method");
	}
	
	public static HttpApiResponce Get(URI uri) {
		CloseableHttpResponse httpResponce = null;
		HttpGet get = new HttpGet(uri);
		try(CloseableHttpClient client = getCloseableHttpClient()) {
			httpResponce = client.execute(get);
			String responceContent = getBasicHttpResponse().handleResponse(httpResponce);
			return new HttpApiResponce(httpResponce.getStatusLine().getStatusCode(), responceContent);
		} catch (Exception e) {
			if(e instanceof HttpResponseException)
				return new HttpApiResponce(httpResponce.getStatusLine().getStatusCode(), e.getMessage());
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public static HttpApiResponce Post(String uri,Object content) {
		try {
			return Post(new URI(uri),content);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Cannot execute POST method");
	}
	
	public static HttpApiResponce Post(URI uri,Object content) {
		CloseableHttpResponse httpResponce = null;
		HttpPost post = new HttpPost(uri);
		post.setEntity(getHttpEntityType(content));
		try(CloseableHttpClient client = getCloseableHttpClient()) {
			httpResponce = client.execute(post);
			String responceContent = getBasicHttpResponse().handleResponse(httpResponce);
			return new HttpApiResponce(httpResponce.getStatusLine().getStatusCode(), responceContent);
		} catch (Exception e) {
			if(e instanceof HttpResponseException)
				return new HttpApiResponce(httpResponce.getStatusLine().getStatusCode(), e.getMessage());
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public static HttpApiResponce Put(String uri,Object content) {
		try {
			return Put(new URI(uri),content);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Cannot execute PUT method");
	}
	
	public static HttpApiResponce Put(URI uri,Object content) {
		CloseableHttpResponse httpResponce = null;
		HttpPut put = new HttpPut(uri);
		put.setEntity(getHttpEntityType(content));
		try (CloseableHttpClient httpClient = getCloseableHttpClient()){
			httpResponce = httpClient.execute(put);
			String responceContent = getBasicHttpResponse().handleResponse(httpResponce);
			return new HttpApiResponce(httpResponce.getStatusLine().getStatusCode(), responceContent);
		} catch (Exception e) {
			if(e instanceof HttpResponseException)
				return new HttpApiResponce(httpResponce.getStatusLine().getStatusCode(), e.getMessage());
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public static HttpApiResponce Delete(String uri) {
		try {
			return Delete(new URI(uri));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Cannot execute DELETE method");
	}
	
	public static HttpApiResponce Delete(URI uri) {
		CloseableHttpResponse httpResponce = null;
		HttpUriRequest delete =  RequestBuilder.delete(uri).build();
		try(CloseableHttpClient httpClient = getCloseableHttpClient()) {
			httpResponce = httpClient.execute(delete);
			String responceContent = getBasicHttpResponse().handleResponse(httpResponce);
			return new HttpApiResponce(httpResponce.getStatusLine().getStatusCode(), responceContent);
		} catch (Exception e) {
			if(e instanceof HttpResponseException)
				return new HttpApiResponce(httpResponce.getStatusLine().getStatusCode(), e.getMessage());
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

}
