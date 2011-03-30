package com.couchbase.lcontroller;

import java.util.LinkedList;
import java.util.List;

import com.couchbase.lcontroller.exporter.HTMLExporter;
import com.couchbase.lcontroller.rest.DLClient;
import com.couchbase.lcontroller.stats.IntervalStats;
import com.couchbase.lcontroller.stats.StatsParser;
import com.couchbase.lcontroller.tests.GetStepTest;

public class LController {
	public static final int LIMIT = 30000;
	public static final int OP_INTERVAL = 10000;
	public static final int TIME_INTERVAL = 10;
	public static final int WARMUP_INTERVAL = 10;

	public static void main(String[] args) {
		DLClient client = new DLClient("10.2.1.15");
		List<IntervalStats> lstats = new LinkedList<IntervalStats>();
		HTMLExporter exporter = new HTMLExporter("Step Test", "results");
		
		//GetStepTest test = new GetStepTest("10.2.1.15");
		//test.addCluster("10.2.1.11", "11211");
		
		int ops = 10000;
		
		//client.setValue("memcached.address", "10.2.1.11");
		//client.setValue("memcached.port", "11211");
		//client.setValue("threadcount", "16");
		//client.setValue("operationcount", "100000000");
		//client.setValue("recordcount", "100000");
		//client.setValue("dotransactions", "true");
		client.setValue("valuelength", "256");
		client.setValue("memupdateproportion", "0.0");
		client.setValue("memgetproportion", "1.0");

		client.setValue("memcached.address", "10.2.1.11");
		client.setValue("memcached.port", "11211");
		client.setValue("dotransactions", "false");
		client.setValue("threadcount", "16");
		client.setValue("operationcount", "1000000");
		client.setValue("recordcount", "100000");

		client.start();
		//System.exit(0);		

		while ((ops += OP_INTERVAL) <= LIMIT) {
			// Warm up time
			client.setValue("target", ops + "");
			try {
				Thread.sleep(WARMUP_INTERVAL * 1000);
			} catch (InterruptedException e) {
				System.out.println("Warmup interupted");
			}
			client.getStats();
			
			// Testing time
			try {
				Thread.sleep(TIME_INTERVAL * 1000);
			} catch (InterruptedException e) {
				System.out.println("Test interupted");
			}
			IntervalStats stats = StatsParser.parse(client.getStats());
			lstats.add(stats);
			System.out.println(stats.getOperations() + ":" + stats.getTransactionStats("GET").getTotalLatency());
			System.out.println(stats.getTransactionStats("GET").getTotalLatency() / stats.getOperations());
			//grapher.addDataPoint(ops, stats.getOperations());
		}
		exporter.addDataset("Membase 1.6.5", lstats);
		//exporter.addDataset("Membase 1.6.4", lstats);
		
		
		exporter.export();
	}
}
