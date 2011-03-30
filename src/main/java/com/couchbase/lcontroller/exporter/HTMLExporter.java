package com.couchbase.lcontroller.exporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.couchbase.lcontroller.LController;
import com.couchbase.lcontroller.stats.IntervalStats;

public class HTMLExporter {
	private HashMap<String, List<IntervalStats>> data;
	private String name;
	private File dir;
	
	public HTMLExporter(String name, String dir) {
		this.data = new HashMap<String, List<IntervalStats>>();
		this.name = name;
		this.dir = new File(dir);
	}
	
	public void addDataset(String name, List<IntervalStats> stats) {
		data.put(name, stats);
	}
	
	public void export() {
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}
		
		createMainGraph();
		
		try {
			FileWriter fstream = new FileWriter(dir.getAbsolutePath() + "/index.html");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("<html>");
			out.write("<head>");
			out.write("<title>" + name + "</title>");
			out.write("<body>");
			out.write("<h1><center>" + name + "</center></h1>");
			out.write("<center>");
			out.write("<table cellpadding=\"4\">");
			out.write("<tr>");
			out.write("<td><center>Operations Done: ???</center></td>");
			out.write("<td><center>Min. Latency: ???</center></td>");
			out.write("<td><center>95th Percentile: ???</center></td>");
			out.write("</tr>");
			out.write("<tr>");
			out.write("<td><center>Average Latency: ???</center></td>");
			out.write("<td><center>Max. Latency: ???</center></td>");
			out.write("<td><center>99th Percentile: ???</center></td>");
			out.write("</tr>");
			out.write("<tr>");
			out.write("<td colspan=\"3\"><img src=\"img/result.jpg\" width=\"750\" height=\"450\"></td>");
			out.write("</tr>");
			out.write("</table>");
			
			out.write("<table cellpadding=\"4\">");
			
			// This limit will cause an error
			for (int i = 0; i < data.get(data.keySet().iterator().next()).size(); i++) {
				createBarGraph("results/img/barchart" + i + ".jpg", i);
				out.write("<tr>");
				out.write("<td><img src=\"img/barchart" + i + ".jpg\" width=\"750\" height=\"450\"></td>");
				out.write("</tr>");
			}
			
			out.write("</table>");
			out.write("</center>");
			
			out.write("</body>");
			out.write("</html>");
			
			out.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	private void createBarGraph(String name, int index) {
		DefaultCategoryDataset bardata = new DefaultCategoryDataset();
		Iterator<String> itr = data.keySet().iterator();
		
		while(itr.hasNext()) {
			String dataset = itr.next();
			System.out.println(dataset);
			List<IntervalStats> stats = data.get(dataset);
			
			stats.get(index).getTransactionStats("GET").addSeries(bardata, dataset);
			
			//int throughput = stats.get(index).getOperations() / LController.TIME_INTERVAL;
			//String title = "Latencies at " + throughput + " ops/sec";
		}
		
		

		try {
			JFreeChart chart = ChartFactory.createBarChart("Temp", "Latency", "Number of Operations",
					bardata, PlotOrientation.VERTICAL, true, true, false);
			ChartUtilities.saveChartAsJPEG(new File(name), chart, 750, 450);
		} catch (IOException e) {
			System.err.println("Problem occurred creating chart.");
		}
	}
	
	// Need to look at other operations
	private void createMainGraph() {
		XYSeriesCollection xyDataset = new XYSeriesCollection();
		Iterator<String> itr = data.keySet().iterator();
		while(itr.hasNext()) {
			String dataset = itr.next();
			List<IntervalStats> stats = data.get(dataset);
			XYSeries series = new XYSeries(dataset);
			
			for (int i = 0; i < stats.size(); i++) {
				long throughput = stats.get(i).getTransactionStats("GET").getTotalLatency() / stats.get(i).getOperations();
				series.add(((i+1) * LController.OP_INTERVAL), throughput);
			}
			xyDataset.addSeries(series);
		
		}

		try {
			JFreeChart chart = ChartFactory.createXYLineChart("Test Summary", "Ops/Sec", "Latency", xyDataset,
					PlotOrientation.VERTICAL,true, true, false);
			ChartUtilities.saveChartAsJPEG(new File("results/img/result.jpg"), chart, 750, 450);
		} catch (IOException e) {
			System.err.println("Problem occurred creating chart.");
		}
	}
	
}
