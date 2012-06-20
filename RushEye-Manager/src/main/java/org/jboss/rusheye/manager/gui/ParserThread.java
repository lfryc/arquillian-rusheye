/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui;

import org.jboss.rusheye.manager.Main;
import org.jboss.rusheye.parser.Parser;

/**
 *
 * @author hcube
 */
public class ParserThread implements Runnable{
    
    private Parser parser;
    
    public ParserThread(Parser p){
        this.parser = p;
    }

    public void run() {
        parser.parseFile(Main.mainProject.getSuiteDescriptor());
    }
    
}
