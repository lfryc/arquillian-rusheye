/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import org.jboss.rusheye.manager.Main;
import org.jboss.rusheye.manager.gui.charts.StatisticsPanel;
import org.jboss.rusheye.manager.project.Project;
import org.jboss.rusheye.manager.project.observable.Observed;
import org.jboss.rusheye.manager.project.observable.Observer;

/**
 *
 * @author hcube
 */
public class StatisticsFrame extends JFrame implements Observer {
    
    private StatisticsPanel panel;

    public StatisticsFrame() {
        panel = new StatisticsPanel();
        panel.setPreferredSize(new Dimension(640,480));
        this.add(panel, BorderLayout.CENTER);
        this.pack();
    }

    @Override
    public void update(Observed o) {
        System.out.println("Notified");
        if(o instanceof Project)
        panel.update(((Project)o).getStatistics());
    }
}
