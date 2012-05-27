package org.jboss.rusheye;

import org.jboss.rusheye.gui.InterfaceFrame;
import org.jboss.rusheye.gui.ProjectManagerFrame;
import org.jboss.rusheye.project.Project;
import org.jboss.rusheye.project.ProjectFactory;

/**
 * Hello world!
 *
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

            }
        });
    }
    
    public static InterfaceFrame interfaceFrame;
    public static ProjectManagerFrame projectFrame;
    public static Project mainProject = ProjectFactory.emptyProject();
}
