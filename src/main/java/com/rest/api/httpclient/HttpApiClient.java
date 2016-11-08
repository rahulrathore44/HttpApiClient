package com.rest.api.httpclient;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.Header;
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
import org.apache.http.message.BasicHeader;


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
	
	private static Header[] getHeaders(Map<String, String> customHeader) {
		BasicHeader[] header = new BasicHeader[customHeader.size()];
		int i = 0;
		for(String key : customHeader.keySet()){
			header[i++] = new BasicHeader(key, customHeader.get(key));
		}
		return header;
	}
	
	
	private static HttpApiResponce performRequest(HttpUriRequest method){
		CloseableHttpResponse httpResponce = null;
		try(CloseableHttpClient client = getCloseableHttpClient()) {
			httpResponce = client.execute(method);
			String responceContent = getBasicHttpResponse().handleResponse(httpResponce);
			return new HttpApiResponce(httpResponce.getStatusLine().getStatusCode(), responceContent);
		} catch (Exception e) {
			if(e instanceof HttpResponseException)
				return new HttpApiResponce(httpResponce.getStatusLine().getStatusCode(), e.getMessage());
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public static HttpApiResponce Get(String uri,Map<String, String> customHeader) {
		try {
			return Get(new URI(uri),customHeader);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public static HttpApiResponce Get(URI uri,Map<String, String> customHeader) {
		HttpGet get = new HttpGet(uri);
		
		if(null != customHeader)
			get.setHeaders(getHeaders(customHeader));
		
		return performRequest(get);
	}
	
	public static HttpApiResponce Post(String uri,Object content,Map<String, String> customHeader) {
		try {
			return Post(new URI(uri),content,customHeader);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public static HttpApiResponce Post(URI uri,Object content,Map<String, String> customHeader) {
		HttpPost post = new HttpPost(uri);

		if(content != null)
			post.setEntity(getHttpEntityType(content));
		if(null != customHeader)
			post.setHeaders(getHeaders(customHeader));
		
		return performRequest(post);
	}
	
	public static HttpApiResponce Put(String uri,Object content,Map<String, String> customHeader) {
		try {
			return Put(new URI(uri),content,customHeader);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public static HttpApiResponce Put(URI uri,Object content,Map<String, String> customHeader) {
		HttpPut put = new HttpPut(uri);
		
		if(content != null)
			put.setEntity(getHttpEntityType(content));
		if(null != customHeader)
			put.setHeaders(getHeaders(customHeader));
		
		return performRequest(put);
	}
	
	public static HttpApiResponce Delete(String uri,Map<String, String> customHeader) {
		try {
			return Delete(new URI(uri),customHeader);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public static HttpApiResponce Delete(URI uri,Map<String, String> customHeader) {
		HttpUriRequest delete =  RequestBuilder.delete(uri).build();
		
		if(null != customHeader)
			delete.setHeaders(getHeaders(customHeader));
		
		return performRequest(delete);
	}

}
