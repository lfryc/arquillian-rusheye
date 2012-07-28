/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.charts;

import javax.swing.JPanel;

/**
 *
 * @author hcube
 */
public class StatisticsPanel extends JPanel {

    ChartRetriever chartRetriever;

    public StatisticsPanel(RushEyeStatistics stats) {
        chartRetriever = new ChartRetrieverImpl(stats);
    }
    
}
