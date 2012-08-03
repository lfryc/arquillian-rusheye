/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.parser;

import javax.swing.JOptionPane;
import org.jboss.rusheye.manager.Main;

/**
 * Thread where we run parser instance. Parsing is slow, so we don't want to
 * lock all gui while doing it.
 *
 * @author Jakub D.
 */
public class ParserThread implements Runnable {

    private ManagerParser parser;

    public ParserThread(ManagerParser p) {
        this.parser = p;
    }

    public void run() {
        parser.parseFile(Main.mainProject.getSuiteDescriptorFile());
        JOptionPane.showMessageDialog(Main.interfaceFrame, "Parsing done", "Parse", JOptionPane.INFORMATION_MESSAGE);

        Main.mainProject.loadResultAsString();
    }
}
