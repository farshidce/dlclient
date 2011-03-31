package com.couchbase.lcontroller;

public class DLClient {
	
	private CouchbaseConnection conn;
	private String hostname;
	
	public DLClient(String hostname) {
		conn = new CouchbaseConnection();
		this.hostname = hostname;
	}
	
	public String getConfig() {
		GetMessage message = new GetMessage(hostname, "/cluster/get-config");
		CouchbaseResponse response = conn.sendRequest(message);
		return response.getBody();
	}
	
	public String setConfig(String config) {
		return "Not Implemented";
	}
	
	public String getValue(String key) {
		String url = "/cluster/get-value?name=" + key;
		GetMessage message = new GetMessage(hostname, url);
		CouchbaseResponse response = conn.sendRequest(message);
		return response.getBody();
	}
	
	public String setValue(String key, String value) {
		String url = "/cluster/set-value?name=" + key + "&value=" + value;
		GetMessage message = new GetMessage(hostname, url);
		CouchbaseResponse response = conn.sendRequest(message);
		return response.getBody();
	}
	
	public String getStats() {
		String url = "/cluster/get-stats";
		GetMessage message = new GetMessage(hostname, url);
		CouchbaseResponse response = conn.sendRequest(message);
		return response.getBody();
	}
	
	public boolean getStatus() {
		String url = "/cluster/get-status";
		GetMessage message = new GetMessage(hostname, url);
		CouchbaseResponse response = conn.sendRequest(message);
		if (response.getBody().equals("Running\n"))
			return true;
		return false;
	}
	
	public String start() {
		String url = "/cluster/run";
		GetMessage message = new GetMessage(hostname, url);
		CouchbaseResponse response = conn.sendRequest(message);
		return response.getBody();
	}
	
	public String stop() {
		return "Not Implemented";
	}
}
