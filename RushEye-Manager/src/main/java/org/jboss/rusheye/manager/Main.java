package org.jboss.rusheye.manager;

import org.jboss.rusheye.manager.gui.frames.InterfaceFrame;
import org.jboss.rusheye.manager.gui.frames.ProjectManagerFrame;
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
                interfaceFrame.setVisible(true);
                
                projectFrame = new ProjectManagerFrame();
                projectFrame.setVisible(true);
                
                mainProject = Project.emptyProject();
                
                //interfaceFrame.setMenu();
            }
        });
        
    }
    
    public static InterfaceFrame interfaceFrame;
    public static ProjectManagerFrame projectFrame;
    
    public static Project mainProject;
    
}
