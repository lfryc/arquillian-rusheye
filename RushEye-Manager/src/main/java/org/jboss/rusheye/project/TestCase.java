/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.project;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author hcube
 */
public class TestCase implements Comparable<TestCase>{
    
    private ArrayList<String> testNames;
    private String caseName;
    private String extension;
    
    public TestCase(){
        testNames = new ArrayList<String>();
    }

    public int compareTo(TestCase t) {
        return caseName.compareTo(t.getCaseName());
    }

    public ArrayList<String> getTestNames() {
        return testNames;
    }

    public void setTestNames(ArrayList<String> testNames) {
        this.testNames = testNames;
    }
    
    public void addTestName(String name){
        testNames.add(name);
        Collections.sort(testNames);
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
        
        for(int i=0;i<testNames.size();++i){
            result += "\t" + testNames.get(i) + "\n";
        }
        
        return result;
    }

}
