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
import org.jboss.rusheye.suite.ResultConclusion;

/**
 *
 * @author hcube
 */
public class ChartRetrieverImpl implements ChartRetriever {

    RushEyeStatistics statistics;

    public ChartRetrieverImpl() {
        this.statistics = new RushEyeStatistics();
    }

    public ChartRetrieverImpl(RushEyeStatistics stats) {
        this.statistics = stats;
    }

    @Override
    public Image generateChart() {
        try {
            if(statistics.calculateSum()==0){
                return new BufferedImage(600, 450, BufferedImage.TYPE_INT_ARGB);
            }
            Plot plot = Plots.newPlot(DataUtil.scaleWithinRange(0, statistics.calculateSum(), statistics.getValues()));


            BarChart chart = GCharts.newBarChart(plot);

            AxisStyle axisStyle = AxisStyle.newAxisStyle(Color.BLACK, 13, AxisTextAlignment.CENTER);
            AxisLabels tests = AxisLabelsFactory.newAxisLabels("Tests", 50.0);
            tests.setAxisStyle(axisStyle);

            AxisLabels con = AxisLabelsFactory.newAxisLabels("Conclusion", 50.0);
            con.setAxisStyle(axisStyle);

            String labels[] = new String[ResultConclusion.values().length];
            for (int i = 0; i < ResultConclusion.values().length; ++i)
                labels[i] = ResultConclusion.values()[i].toString();

            chart.addXAxisLabels(AxisLabelsFactory.newAxisLabels(labels));
            chart.addXAxisLabels(con);

            chart.addYAxisLabels(AxisLabelsFactory.newNumericRangeAxisLabels(0, statistics.calculateSum()));
            chart.addYAxisLabels(tests);

            chart.setSize(600, 450);
            chart.setBarWidth(80);
            chart.setSpaceWithinGroupsOfBars(20);
            chart.setDataStacked(true);
            chart.setTitle("Test results", Color.BLACK, 16);
            chart.setGrid(100, 10, 3, 2);
            chart.setBackgroundFill(Fills.newSolidFill(Color.ALICEBLUE));
            LinearGradientFill fill = Fills.newLinearGradientFill(0, Color.LAVENDER, 100);
            fill.addColorAndOffset(Color.WHITE, 0);
            chart.setAreaFill(fill);


            URL url = new URL(chart.toURLString());

            return java.awt.Toolkit.getDefaultToolkit().getDefaultToolkit().createImage(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new BufferedImage(600, 450, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void setStatistics(RushEyeStatistics stats) {
        this.statistics = stats;
    }
}
