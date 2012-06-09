/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import javax.swing.JFileChooser;
import org.jboss.rusheye.manager.Main;

/**
 *
 * @author hcube
 */
public class FileChooserUtils {
    
    public static JFileChooser dirChooser(){
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        fc.setCurrentDirectory(new java.io.File("."));
        
        return fc;
    }
    
    public static JFileChooser saveChooser(){
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new java.io.File("."));

        return fc;
    }
}
