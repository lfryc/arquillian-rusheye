/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project;

import java.io.File;
import org.jboss.rusheye.manager.Main;

/**
 *
 * @author hcube
 */
public class ProjectFactory {

    public static Project emptyProject() {
        Project tmp = new Project();
        tmp.addObserver(Main.projectFrame);
        return tmp;
    }

    public static Project projectFromDirs(String patternPath, String samplesPath) {
        return new Project(patternPath, samplesPath);
    }

    public static Project projectFromDescriptor(String descriptorPath) {
        Project tmp = new Project(new File(descriptorPath));
        tmp.addObserver(Main.projectFrame);
        return tmp;
    }

    public static Project projectFromDescriptor(File descriptor) {
        Project tmp = new Project(descriptor);
        tmp.addObserver(Main.projectFrame);
        return tmp;
    }
}
