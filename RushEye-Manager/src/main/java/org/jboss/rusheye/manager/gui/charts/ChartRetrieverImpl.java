/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.charts;

import com.googlecode.charts4j.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author hcube
 */
public class ChartRetrieverImpl implements ChartRetriever {

    RushEyeStatistics statistics;

    public ChartRetrieverImpl(RushEyeStatistics stats) {
        this.statistics = stats;
    }

    @Override
    public Image generateChart() {
        try {
            Plot plot = Plots.newPlot(Data.newData(statistics.getValues()));
            plot.addShapeMarkers(Shape.DIAMOND, Color.BLUE, 12);
            
            BarChart barChart = GCharts.newBarChart(plot);
            barChart.setSize(400, 200);

            URL url = new URL(barChart.toURLString());

            return java.awt.Toolkit.getDefaultToolkit().getDefaultToolkit().createImage(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return new BufferedImage(400,200,BufferedImage.TYPE_INT_ARGB);
    }
}
