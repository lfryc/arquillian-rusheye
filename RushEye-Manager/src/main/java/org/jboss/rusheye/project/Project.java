/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.project;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.rusheye.exception.ManagerException;

/**
 *
 * @author hcube
 */
public class Project {
    
    private List<TestCase> cases;
    
    private String patternPath;
    private String samplesPath;
    
    Project(){
        cases = new ArrayList<TestCase>();
    }
    
    Project(String patternPath, String samplesPath) {
        this();
        this.patternPath = patternPath;
        this.samplesPath = samplesPath;
        try {
            this.parseDirs();
        } catch (ManagerException ex) {
            ex.printStackTrace();
        }
    }

    public String getPatternPath() {
        return patternPath;
    }

    public void setPatternPath(String patternPath) {
        this.patternPath = patternPath;
    }

    public String getSamplesPath() {
        return samplesPath;
    }

    public void setSamplesPath(String samplesPath) {
        this.samplesPath = samplesPath;
    }
    
    public List<TestCase> getCases() {
        return cases;
    }

    public void parseDirs() throws ManagerException {
        ArrayList<String> patternList = parseDir(patternPath);
        ArrayList<String> samplesList = parseDir(samplesPath);
        
        if(patternList.size() != samplesList.size()) throw new ManagerException("Not the same file number in pattern and samples");
        
        String lastCase = "";
        TestCase tmp = null;
        
        for(int i=0;i<patternList.size(); ++i){
            if(patternList.get(i).equals(samplesList.get(i))){
                String parts[] = patternList.get(i).split("[.]");
                if( !parts[0].equals(lastCase) ){//if we get new case :
                    if(tmp != null) cases.add(tmp);//add last case
                    tmp = new TestCase();//create new case
                    tmp.setCaseName(parts[0]);
                    tmp.setExtension(parts[2]);
                    lastCase = parts[0];
                }
                tmp.addTestName(parts[1]);
            }
            else throw new ManagerException("Pattern and sample name do not match");
        }
        
        for(TestCase current : cases){
            System.out.println(current);
        }
        System.out.println(cases.size());
        
    }
    
    private ArrayList<String> parseDir(String path){
        File folder = new File(path);
        File[] files = folder.listFiles();
        
        ArrayList<String> names = new ArrayList<String>();
        for(int i=0;i<files.length;++i){
            names.add(files[i].getName());
        }
        Collections.sort(names);
        return names;
    }

    public TestCase findCase(String name){
        for(TestCase current : cases) if(current.getCaseName().equals(name)) return current;
        return null;
    }

}
