package com.couchbase.lcontroller.tests;

import java.util.LinkedList;
import java.util.List;

import com.couchbase.lcontroller.exporter.HTMLExporter;
import com.couchbase.lcontroller.rest.DLClient;
import com.couchbase.lcontroller.stats.IntervalStats;
import com.couchbase.lcontroller.stats.StatsParser;

public class GetStepTest implements LGTest {
	DLClient client;
	List<String> clusterAddresses;
	
	public GetStepTest(String lgHost) {
		this.client = new DLClient(lgHost);
		this.clusterAddresses = new LinkedList<String>();
	}
	
	public void addCluster(String host, String port) {
		clusterAddresses.add(host);
	}
	
	public void initTest(int opinterval, int oplimit, int timeinterval, int warmuptime) {
		client.setValue("memcached.address", "10.2.1.11");
		client.setValue("memcached.port", "11211");
		client.setValue("dotransactions", "false");
		client.setValue("threadcount", "16");
		client.setValue("operationcount", "1000000");
		client.setValue("recordcount", "100000");
		client.setValue("valuelength", "256");
	}
	
	public void runTest(int opinterval, int oplimit, int timeinterval, int warmuptime) {
		List<IntervalStats> stats = new LinkedList<IntervalStats>();
		HTMLExporter exporter = new HTMLExporter("Step Test", "results");
		
		int ops = 0;
		
		client.setValue("memcached.address", "10.2.1.11");
		client.setValue("memcached.port", "11211");
		client.setValue("valuelength", "256");
		client.setValue("threadcount", "16");
		client.setValue("operationcount", "100000000");
		client.setValue("recordcount", "100000");
		client.setValue("dotransactions", "true");
		client.setValue("memupdateproportion", "0.0");
		client.setValue("memgetproportion", "1.0");

		client.start();
				

		while ((ops += opinterval) <= oplimit) {
			// Warm up time
			client.setValue("target", ops + "");
			try {
				Thread.sleep(warmuptime * 1000);
			} catch (InterruptedException e) {
				System.out.println("Warmup interupted");
			}
			client.getStats();
			
			// Testing time
			try {
				Thread.sleep(timeinterval * 1000);
			} catch (InterruptedException e) {
				System.out.println("Test interupted");
			}
			IntervalStats istats = StatsParser.parse(client.getStats());
			stats.add(istats);
			System.out.println(istats.getOperations() + ":" + istats.getTransactionStats("GET").getTotalLatency());
			//grapher.addDataPoint(ops, stats.getOperations());
		}
		exporter.addDataset("Membase 1.6.5", stats);
		//exporter.addDataset("Membase 1.6.4", lstats);
		
		
		exporter.export();
	}
}
