/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.frames;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import org.jboss.rusheye.manager.Main;
import org.jboss.rusheye.manager.gui.charts.StatisticsPanel;

/**
 *
 * @author hcube
 */
public class StatisticsFrame extends JFrame {
    
    private StatisticsPanel panel;

    public StatisticsFrame() {
        panel = new StatisticsPanel();
        Main.mainProject.getParser().addObserver(panel);
        this.add(panel, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }
}
