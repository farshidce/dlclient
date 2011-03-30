package com.couchbase.lcontroller.stats;

import java.util.HashMap;

import org.jfree.data.category.DefaultCategoryDataset;

public class TransactionStats {
	private HashMap<String, Integer> tstats;
	private long totallatency;
	
	public TransactionStats() {
		tstats = new HashMap<String, Integer>();
		totallatency = 0;
	}
	
	public void setStat(String key, int value) {
		tstats.put(key, new Integer(value));
	}
	
	public void setTotalLatency(long totallatency) {
		this.totallatency = totallatency;
	}
	
	public long getTotalLatency() {
		return totallatency;
	}
	
	public void addSeries(DefaultCategoryDataset data, String name) {
		
		/*
		for (int i = 0; i < 20; i++) {
			data.addValue(tstats.get(i + "").doubleValue(), i + "", "Membase 1.6.5"); 
		}*/
		
		data.addValue(tstats.get(0 + "").doubleValue(), "0-256us", name); 
		data.addValue(tstats.get(1 + "").doubleValue(), "256us-512us", name); 
		data.addValue(tstats.get(2 + "").doubleValue(), "512us-1ms", name); 
		data.addValue(tstats.get(3 + "").doubleValue(), "1ms-2ms", name); 
		data.addValue(tstats.get(4 + "").doubleValue(), "2ms-4ms", name); 
	}
}
