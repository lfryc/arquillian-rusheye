package org.jboss.rusheye;

import org.jboss.rusheye.gui.InterfaceFrame;
import org.jboss.rusheye.gui.ProjectManagerFrame;
import org.jboss.rusheye.gui.SingleViewMenuFrame;
import org.jboss.rusheye.project.Project;
import org.jboss.rusheye.project.ProjectFactory;
import org.jboss.rusheye.project.TestCase;

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
            }
        });
    }
    
    public static InterfaceFrame interfaceFrame;
    public static ProjectManagerFrame projectFrame;
    public static SingleViewMenuFrame singleFrame;
    
    public static Project mainProject = ProjectFactory.emptyProject();
    public static TestCase current = new TestCase();
}
