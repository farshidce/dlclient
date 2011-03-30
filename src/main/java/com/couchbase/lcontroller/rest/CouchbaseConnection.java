package com.couchbase.lcontroller.rest;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class CouchbaseConnection {
	HttpClient client;
	
	public CouchbaseConnection() {
		client = new DefaultHttpClient();
	}
	
	public CouchbaseResponse sendRequest(GetMessage msg) {
		HttpResponse response = null;
		try {
			response = client.execute(msg.getRequest());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error: Couln't connection to server");
			System.exit(0);
		}
		
		return new CouchbaseResponse(response);
	}
}
