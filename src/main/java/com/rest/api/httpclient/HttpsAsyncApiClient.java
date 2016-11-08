package com.rest.api.httpclient;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

public class HttpsAsyncApiClient {
	
	
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
	
	private static SSLContext getSSLContexts() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		TrustStrategy trustStrategy = new TrustStrategy() {
			
			@Override
			public boolean isTrusted(X509Certificate[] chain, String authType)
					throws CertificateException {
				return true;
			}
		};
		
		return SSLContexts.custom().loadTrustMaterial(trustStrategy).build();
	}
	
	private static CloseableHttpAsyncClient getHttpAsyncClient(SSLContext sslcontext){
		return HttpAsyncClients.custom()
				.setSSLContext(sslcontext)
				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
	}
	
	private static HttpApiResponce performRequest(HttpUriRequest method) throws InterruptedException, ExecutionException {
		Future<HttpResponse> httpResponse = null;
		try(CloseableHttpAsyncClient httpClient = getHttpAsyncClient(getSSLContexts())) {
			httpClient.start();
			httpResponse = httpClient.execute(method, null);
			return new HttpApiResponce(httpResponse.get().getStatusLine().getStatusCode(), getBasicHttpResponse().handleResponse(httpResponse.get()));
		} catch (Exception e) {
			if(e instanceof HttpResponseException)
				return new HttpApiResponce(httpResponse.get().getStatusLine().getStatusCode(), e.getMessage());
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	
	public static HttpApiResponce GetWithSSl(String uri,Map<String,String> customeHeader) throws InterruptedException, ExecutionException {
		try {
			return GetWithSSl(new URI(uri),customeHeader);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public static HttpApiResponce GetWithSSl(URI uri,Map<String,String> customeHeader) throws InterruptedException, ExecutionException {
		HttpUriRequest get = RequestBuilder.get(uri).build();

		if(customeHeader != null)
			get.setHeaders(getHeaders(customeHeader));
		
		return performRequest(get);
	}
	
	public static HttpApiResponce PostWithSSl(String uri,Object content,Map<String,String> customeHeader) throws InterruptedException, ExecutionException {
		try {
			return PostWithSSl(new URI(uri),content,customeHeader);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public static HttpApiResponce PostWithSSl(URI uri,Object content,Map<String,String> customeHeader) throws InterruptedException, ExecutionException {
		HttpPost post = new HttpPost(uri);
		
		if(customeHeader != null)
			post.setHeaders(getHeaders(customeHeader));
		
		if(content != null)
			post.setEntity(getHttpEntityType(content));
		
		return performRequest(post);
	}
	
	public static HttpApiResponce PutWithSSl(String uri,Object content,Map<String,String> customeHeader) throws InterruptedException, ExecutionException {
		try {
			return PutWithSSl(new URI(uri),content,customeHeader);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public static HttpApiResponce PutWithSSl(URI uri,Object content,Map<String,String> customeHeader) throws InterruptedException, ExecutionException {
		HttpPut put = new HttpPut(uri);
		
		if(customeHeader != null)
			put.setHeaders(getHeaders(customeHeader));
		
		if(content != null)
			put.setEntity(getHttpEntityType(content));
		
		return performRequest(put);
	}
	
	public static HttpApiResponce DeleteWithSSl(String uri,Map<String,String> customeHeader) throws InterruptedException, ExecutionException {
		try {
			return DeleteWithSSl(new URI(uri),customeHeader);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public static HttpApiResponce DeleteWithSSl(URI uri,Map<String,String> customeHeader) throws InterruptedException, ExecutionException {
		HttpUriRequest delete =  RequestBuilder.delete(uri).build();
		
		if(customeHeader != null)
			delete.setHeaders(getHeaders(customeHeader));
		
		return performRequest(delete);
	}
	
}
