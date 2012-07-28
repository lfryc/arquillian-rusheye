/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.charts;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import org.jboss.rusheye.manager.Main;
import org.jboss.rusheye.manager.project.observable.Observed;
import org.jboss.rusheye.manager.project.observable.Observer;
import org.jboss.rusheye.parser.ManagerParser;

/**
 *
 * @author hcube
 */
public class StatisticsPanel extends JPanel {

    private ChartRetriever chartRetriever;
    private Image image;

    public StatisticsPanel() {
        image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        chartRetriever = new ChartRetrieverImpl();
    }

    public StatisticsPanel(RushEyeStatistics stats) {
        this();
        chartRetriever = new ChartRetrieverImpl(stats);
    }

    public void update(RushEyeStatistics stats) {
        System.out.println("UPDATE PANEL" + stats);

        chartRetriever.setStatistics(stats);
        image = chartRetriever.generateChart();
        
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }
}
