package com.couchbase.lcontroller.rest;

import org.apache.http.client.methods.HttpGet;

public class GetMessage {
	private static final String PORT = "8182";
	private HttpGet request;
	
	public GetMessage(String host, String url) {
		request = new HttpGet("http://" + host + ":" + PORT + url);
	}
	
	public HttpGet getRequest() {
		return request;
	}
}
