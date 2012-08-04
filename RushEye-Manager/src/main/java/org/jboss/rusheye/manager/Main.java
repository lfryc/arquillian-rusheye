package org.jboss.rusheye.manager;

import org.jboss.rusheye.manager.gui.charts.RushEyeStatistics;
import org.jboss.rusheye.manager.gui.frames.*;
import org.jboss.rusheye.manager.project.Project;

/**
 * Main class. Loading and storing main frames and project.
 *
 * @author Jakub D.
 */
public class Main 
{
    public static void main(String[] args) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                interfaceFrame=new InterfaceFrame();                
                
                statFrame = new StatisticsFrame();
                statFrame.setVisible(false);
                
                mainProject = Project.emptyProject();
                
            }
        });
        
    }
    
    public static InterfaceFrame interfaceFrame;
    public static StatisticsFrame statFrame;
    
    public static Project mainProject;
    
}
