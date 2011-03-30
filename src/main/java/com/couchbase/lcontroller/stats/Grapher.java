package com.couchbase.lcontroller.stats;

import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Grapher {
	private XYSeries series;
	
	public Grapher() {
		series = new XYSeries("Couchbase Step Test");
	}
	
	public void addDataPoint(int x, int y) {
		series.add(x, y);
	}
	
	public void makeGraph() {	
		XYSeriesCollection dataset = new XYSeriesCollection(series);
		JFreeChart chart = ChartFactory.createXYLineChart("XY Chart", "x-axis", "y-axis", dataset, PlotOrientation.VERTICAL, true, false, false);
		try {
			ChartUtilities.saveChartAsJPEG(new File("result.jpg"), chart, 500, 300);
		} catch (IOException e) {
			
		}
		System.out.println("Here");
	}
}
