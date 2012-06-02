package org.jboss.rusheye.manager;

import org.jboss.rusheye.manager.gui.InterfaceFrame;
import org.jboss.rusheye.manager.gui.ProjectManagerFrame;
import org.jboss.rusheye.manager.gui.SingleViewMenuFrame;
import org.jboss.rusheye.manager.gui.view.TextView;
import org.jboss.rusheye.manager.project.Project;
import org.jboss.rusheye.manager.project.ProjectFactory;
import org.jboss.rusheye.manager.project.TestCase;

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

                singleFrame = new SingleViewMenuFrame();
                singleFrame.setVisible(false);
                
                console = new TextView();
            }
        });
    }
    
    public static InterfaceFrame interfaceFrame;
    public static ProjectManagerFrame projectFrame;
    public static SingleViewMenuFrame singleFrame;
    
    public static TextView console;
    
    public static Project mainProject = ProjectFactory.emptyProject();
    
}
