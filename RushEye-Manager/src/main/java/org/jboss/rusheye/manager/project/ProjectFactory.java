/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project;

import java.io.File;

/**
 *
 * @author hcube
 */
public class ProjectFactory {
    public static Project emptyProject(){
        return new Project();
    }
    
    public static Project projectFromDirs(String patternPath,String samplesPath){
        return new Project(patternPath,samplesPath);
    }
    
    public static Project projectFromDescriptor(String descriptorPath){
        return new Project(new File(descriptorPath));
    }
}
