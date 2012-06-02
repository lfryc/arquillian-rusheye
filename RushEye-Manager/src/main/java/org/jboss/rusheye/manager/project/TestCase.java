/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author hcube
 */
public class TestCase implements Comparable<TestCase>{
    
    private ArrayList<Test> tests;
    private Test currentTest;
    private String caseName;
    private String extension;
    
    
    public TestCase(){
        tests = new ArrayList<Test>();
    }

    public int compareTo(TestCase t) {
        return caseName.compareTo(t.getCaseName());
    }

    public ArrayList<Test> getTests() {
        return tests;
    }

    public void setTestNames(ArrayList<Test> tests) {
        this.tests = tests;
    }
    
    public void addTest(Test test){
        tests.add(test);
        Collections.sort(tests);
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String toString(){
        String result = caseName + ":\n";
        
        for(int i=0;i<tests.size();++i){
            result += "\t" + tests.get(i).getName() + "\n";
        }
        
        return result;
    }

    public Test getCurrentTest() {
        return currentTest;
    }

    public void setCurrentTest(Test test) {
        this.currentTest = test;
    }

    public Test findTest(String name){
        for(Test test : tests){
            if(test.getName().equals(name)) return test;
        }
        return null;
    }
}
