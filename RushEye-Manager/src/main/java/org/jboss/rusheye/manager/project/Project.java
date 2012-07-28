/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jboss.rusheye.manager.Main;
import org.jboss.rusheye.manager.exception.ManagerException;
import org.jboss.rusheye.manager.gui.charts.RushEyeStatistics;
import org.jboss.rusheye.manager.project.observable.Observed;
import org.jboss.rusheye.manager.project.observable.Observer;
import org.jboss.rusheye.parser.ManagerParser;
import org.jboss.rusheye.suite.ResultConclusion;

/**
 * Class where we store all data about RushEye Manager project.
 *
 * @author Jakub D.
 */
public class Project extends ProjectBase  {

    String resultDescriptorString = null;
    private MaskManager maskManager;
    private ManagerParser parser;
    private RushEyeStatistics statistics;
    
    public static Project emptyProject() {
        Project tmp = new Project();
        tmp.addObserver(Main.projectFrame);
        tmp.addObserver(Main.statFrame);
        return tmp;
    }
    
    @Deprecated
    public static Project projectFromDirs(String patternPath, String samplesPath) {
        return new Project(patternPath, samplesPath);
    }

    public static Project projectFromDescriptor(String descriptorPath) {
        Project tmp = new Project(new File(descriptorPath));
        tmp.addObserver(Main.projectFrame);
        tmp.addObserver(Main.statFrame);
        return tmp;
    }

    public static Project projectFromDescriptor(File descriptor) {
        Project tmp = new Project(descriptor);
        tmp.addObserver(Main.projectFrame);
        tmp.addObserver(Main.statFrame);
        return tmp;
    }

    public Project() {
        super();
        root = new TestCase();
        maskManager = new MaskManager();
        parser = new ManagerParser();
        parser.addObserver(this);
        statistics = new RushEyeStatistics();
        
    }

    public Project(File suiteDescriptor) {
        this();
        this.suiteDescriptor = suiteDescriptor;
        root = parser.parseFileToManagerCases(this.suiteDescriptor);
    }
    
    @Deprecated
    public Project(String patternPath, String samplesPath) {
        this();
        this.patternPath = patternPath;
        this.samplesPath = samplesPath;
        try {
            root = this.parseDirs();
        } catch (ManagerException ex) {
            ex.printStackTrace();
        }
    }

    @Deprecated
    public TestCase parseDirs() throws ManagerException {
        TestCase test = new TestCase();
        test.setAllowsChildren(true);
        test.setName("Test Cases");

        ArrayList<String> patternList = parseDir(patternPath);
        ArrayList<String> samplesList = parseDir(samplesPath);

        if (patternList.size() != samplesList.size()) {
            throw new ManagerException("Not the same file number in pattern and samples");
        }

        String lastCase = "";
        TestCase tmp = null;

        for (int i = 0; i < patternList.size(); ++i) {
            if (patternList.get(i).equals(samplesList.get(i))) {
                String parts[] = patternList.get(i).split("[.]");
                if (parts.length == 3) {//[case].[test].[extension]

                    if (parts[0].equals(lastCase) == false) {//if we get new case :
                        if (tmp != null) {
                            tmp.setParent(test);
                            tmp.setAllowsChildren(true);
                            test.addChild(tmp);//add last case
                        }

                        tmp = new TestCase();//create new case
                        tmp.setName(parts[0]);
                        lastCase = parts[0];
                    }

                    TestCase tmpTest = new TestCase();
                    tmpTest.setName(parts[1]);
                    tmpTest.setFilename(patternList.get(i));
                    tmpTest.setConclusion(ResultConclusion.NOT_TESTED);
                    tmpTest.setParent(tmp);
                    tmp.addChild(tmpTest);
                }
                if (parts.length == 2) {//normal filename
                }
            } else {
                throw new ManagerException("Pattern and sample name do not match");
            }
        }

        System.out.println(test.toString());

        return test;
    }
    
    @Deprecated
    private ArrayList<String> parseDir(String path) {
        File folder = new File(path);
        File[] files = folder.listFiles();

        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < files.length; ++i) {
            names.add(files[i].getName());
        }
        Collections.sort(names);
        return names;
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

    public MaskManager getMaskManager(){
        return maskManager;
    }
    
    public ManagerParser getParser(){
        return parser;
    }
    
    public RushEyeStatistics getStatistics(){
        return statistics;
    }

    @Override
    public void update(Observed o) {
        System.out.println("Update from parser");
        if(o instanceof ManagerParser){
            statistics = ((ManagerParser)o).getStatistics();
        }
    }
    
}
