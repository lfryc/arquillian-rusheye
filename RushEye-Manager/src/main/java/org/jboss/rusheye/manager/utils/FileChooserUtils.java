/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.utils;

import java.awt.Component;
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
    
    public static JFileChooser fileChooser(){
        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.setCurrentDirectory(new java.io.File("."));
        
        return fc;
    }
    
    public static JFileChooser saveChooser(){
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new java.io.File("."));

        return fc;
    }
    
    public static File chooseFile(JFileChooser fc, Component parent) {
        
        int returnVal = fc.showOpenDialog(parent);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            return file;
        }
        else return null;
    }
    
    public static File openDir(String msg, Component parent) {
        JFileChooser fc = FileChooserUtils.dirChooser();
        if (fc != null) {
            fc.setDialogTitle(msg);
            return FileChooserUtils.chooseFile(fc, parent);
        } else 
            return null;
    }
}
