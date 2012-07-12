/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;
import org.jboss.rusheye.manager.Main;
import org.jboss.rusheye.parser.Parser;

/**
 * Thread where we run parser instance. Parsing is slow, so we don't want to
 * lock all gui while doing it.
 *
 * @author Jakub D.
 */
public class ParserThread implements Runnable {

    private Parser parser;

    public ParserThread(Parser p) {
        this.parser = p;
    }

    public void run() {
        parser.parseFile(Main.mainProject.getSuiteDescriptor());
        JOptionPane.showMessageDialog(Main.interfaceFrame, "Parsing done", "Parse", JOptionPane.INFORMATION_MESSAGE);

        Main.mainProject.loadResultAsString();
    }
}
