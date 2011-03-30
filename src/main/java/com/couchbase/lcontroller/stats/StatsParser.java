package com.couchbase.lcontroller.stats;

import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class StatsParser {
	
	public static IntervalStats parse(String json) {
		IntervalStats stats = new IntervalStats();
		JsonFactory factory = new JsonFactory();
		try {
			JsonParser p = factory.createJsonParser(json.getBytes());
			p.nextToken();
			p.nextToken();
			if (p.getCurrentName().equals("ops")) {
				p.nextToken();
				stats.setOperations(p.getIntValue());
			}
			
			
			while(p.nextToken() != JsonToken.END_OBJECT) {
				p.nextToken();
				p.nextToken();
				TransactionStats tstats = new TransactionStats();
				if (p.getCurrentName().equals("totallatency")) {
					p.nextToken();
					tstats.setTotalLatency(p.getLongValue());
					p.nextToken();
				}
				if (p.getCurrentName().equals("stats")) {
					p.nextToken();
					while(p.nextToken() != JsonToken.END_OBJECT) {
						int index = new Integer(p.getCurrentName()).intValue();
						JsonToken token = p.nextToken();
						if	(token == JsonToken.VALUE_NUMBER_INT) {
							tstats.setStat(index + "", p.getIntValue());
						}
					}
				}
				stats.setTransactionStats("GET", tstats);
				p.nextToken();
				if (p.getCurrentName().equals("returncodes")) {
					p.nextToken();
					while (p.nextToken() != JsonToken.END_OBJECT) {
						Integer code = new Integer(p.getCurrentName());
						int[] list = new int[1];
						JsonToken token = p.nextToken();
						list[0] = p.getIntValue();
						if	(token == JsonToken.VALUE_NUMBER_INT) {
							//if (returncodes.containsKey(code))
								//list[0] += returncodes.get(code)[0];
							//returncodes.put(code, list);
						}
					}
				}
			}
		} catch (JsonParseException e) {
		} catch (IOException e) {
		}
		return stats;
	}
}
