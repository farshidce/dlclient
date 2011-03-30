package com.couchbase.lcontroller.stats;

import java.util.HashMap;

public class IntervalStats {
	
	private int ops;
	private HashMap<String, TransactionStats> transactions;
	private HashMap<String, Integer> returncodes;
	
	public IntervalStats() {
		ops = 0;
		transactions = new HashMap<String, TransactionStats>();
		returncodes = new HashMap<String, Integer>();
	}
	
	public void setOperations(int ops) {
		this.ops = ops;
	}
	
	public int getOperations() {
		return ops;
	}
	
	public void setTransactionStats(String type, TransactionStats stats) {
		transactions.put(type, stats);
	}
	
	public TransactionStats getTransactionStats(String type) {
		return transactions.get(type);
	}
	
	public void setReturnCode(String code, int number) {
		
	}
	
}
