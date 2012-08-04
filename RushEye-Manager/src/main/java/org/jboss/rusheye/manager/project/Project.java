/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project;

import java.io.*;
import org.jboss.rusheye.manager.Main;
import org.jboss.rusheye.manager.gui.charts.RushEyeStatistics;
import org.jboss.rusheye.manager.project.observable.Observed;
import org.jboss.rusheye.parser.ManagerParser;

/**
 * Class where we store all data about RushEye Manager project.
 *
 * @author Jakub D.
 */
public class Project extends ProjectBase {

    String resultDescriptorString = null;
    private MaskManager maskManager;
    private ManagerParser parser;
    private RushEyeStatistics statistics;

    public static Project emptyProject() {
        Project tmp = new Project();
        return tmp;
    }

    public static Project projectFromDescriptor(String descriptorPath) {
        return new Project(new File(descriptorPath));
    }

    public Project() {
        super();
        root = new TestCase();
        maskManager = new MaskManager();
        statistics = new RushEyeStatistics();
        createParser();
    }

    public Project(File suiteFile) {
        this();

        this.suiteDescriptorFile = suiteFile;
        this.suiteDescriptor = parser.loadSuite(suiteFile);

        root = parser.parseSuiteToManagerCases(this.suiteDescriptor);
    }

    public ManagerParser createParser() {
        parser = new ManagerParser();
        parser.addObserver(this);
        return parser;
    }

    /**
     * Method that search recursively through tests tree.
     *
     * @param name path representation of test.
     * @return matching test case.
     */
    public TestCase findTest(String name) {
        return root.findTest(name);
    }

    /**
     * Method that search recursively through tests tree.
     *
     * @param testName name of test
     * @param patternName name of pattern
     * @return matching test case.
     */
    public TestCase findTest(String testName, String patternName) {
        return root.findTest("Test Cases." + testName + "." + patternName);
    }

    /**
     * Loads result xml as string.
     */
    public void loadResultAsString() {
        resultDescriptorString = "";
        try {
            FileInputStream fstream = new FileInputStream(Main.mainProject.getResultDescriptor());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                resultDescriptorString += strLine + "\n";
            }
            in.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public String getResult() {
        return resultDescriptorString;
    }

    public MaskManager getMaskManager() {
        return maskManager;
    }

    public ManagerParser getParser() {
        return parser;
    }

    public RushEyeStatistics getStatistics() {
        return statistics;
    }

    @Override
    public void update(Observed o) {
        System.out.println("Update from parser");
        if (o instanceof ManagerParser) {
            statistics = ((ManagerParser) o).getStatistics();
            updateFrames();
        }
    }

    @Override
    public void updateFrames() {
        Main.interfaceFrame.getProjectFrame().updateCheckBoxes(statistics);
        Main.interfaceFrame.getProjectFrame().update(this);
        Main.interfaceFrame.getStatFrame().update(this);
    }
}
