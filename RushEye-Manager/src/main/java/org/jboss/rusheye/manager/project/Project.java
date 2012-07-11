/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project;

import java.io.*;
import org.jboss.rusheye.manager.project.testcase.TestCase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jboss.rusheye.manager.Main;
import org.jboss.rusheye.manager.exception.ManagerException;
import org.jboss.rusheye.manager.project.observable.Observed;
import org.jboss.rusheye.manager.project.observable.Observer;
import org.jboss.rusheye.parser.ManagerParser;
import org.jboss.rusheye.suite.ResultConclusion;

/**
 *
 * @author hcube
 */
public class Project implements Observed {

    private TestCase root;
    private TestCase currentCase;
    
    private String patternPath;
    private String samplesPath;
    private String maskPath;
    
    private File suiteDescriptor;
    private File resultDescriptor;
    
    private LoadType loadType;
    private List<Observer> observers;
    
    String result = null;

    public Project() {
        root = new TestCase();
        loadType = LoadType.EMPTY;
        observers = new ArrayList<Observer>();
    }

    public Project(String patternPath, String samplesPath) {
        this();
        this.patternPath = patternPath;
        this.samplesPath = samplesPath;
        try {
            root = this.parseDirs();
        } catch (ManagerException ex) {
            ex.printStackTrace();
        }

        loadType = LoadType.DIRS;
    }

    public Project(File suiteDescriptor) {
        this();

        this.suiteDescriptor = suiteDescriptor;

        ManagerParser parser = new ManagerParser();
        root = parser.parseFileToManagerCases(this.suiteDescriptor);
        for (int i = 0; i < root.getChildCount(); ++i)
            System.out.println(root.getChildAt(i));
        loadType = LoadType.SUITE;
    }

    public String getPatternPath() {
        return patternPath;
    }

    public void setPatternPath(String patternPath) {
        this.patternPath = patternPath;
        notifyObservers();
    }

    public String getSamplesPath() {
        return samplesPath;
    }

    public void setSamplesPath(String samplesPath) {
        this.samplesPath = samplesPath;
        notifyObservers();
    }

    public TestCase getRoot() {
        return root;
    }

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

    public TestCase getCurrentCase() {
        return currentCase;
    }

    public void setCurrentCase(TestCase currentCase) {
        this.currentCase = currentCase;
    }

    public TestCase findTest(String name) {
        return root.findTest(name);
    }

    public TestCase findTest(String testName, String patternName) {
        return root.findTest("Test Cases." + testName + "." + patternName);
    }

    public File getSuiteDescriptor() {
        return suiteDescriptor;
    }

    public void setSuiteDescriptor(File suiteDescriptor) {
        this.suiteDescriptor = suiteDescriptor;
    }

    public String getMaskPath() {
        return maskPath;
    }

    public void setMaskPath(String maskPath) {
        this.maskPath = maskPath;
    }

    public LoadType getProjectType() {
        return loadType;
    }

    public File getResultDescriptor() {
        return resultDescriptor;
    }

    public void setResultDescriptor(File resultDescriptor) {
        this.resultDescriptor = resultDescriptor;
    }

    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    private void notifyObservers() {
        for (Observer o : observers)
            o.update(this);
    }

    public void loadResultAsString() {
        result = "";
        try {
            FileInputStream fstream = new FileInputStream(Main.mainProject.getResultDescriptor());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                result += strLine + "\n";
            }
            in.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    public String getResult(){
        return result;
    }
}
