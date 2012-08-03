/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jboss.rusheye.manager.Main;
import org.jboss.rusheye.manager.project.observable.Observed;
import org.jboss.rusheye.manager.project.observable.Observer;
import org.jboss.rusheye.suite.VisualSuite;

/**
 *
 * @author hcube
 */
public abstract class ProjectBase implements Observer {

    protected TestCase root;
    protected TestCase currentCase;
    protected String patternPath;
    protected String samplesPath;
    protected String maskPath;
    protected File suiteDescriptorFile;
    protected VisualSuite suiteDescriptor;
    protected File resultDescriptor;
    protected List<Observer> observers;

    public ProjectBase() {
        observers = new ArrayList<Observer>();
    }

    public TestCase getCurrentCase() {
        return currentCase;
    }

    public void setCurrentCase(TestCase currentCase) {
        this.currentCase = currentCase;
    }


    public String getMaskPath() {
        return maskPath;
    }

    public void setMaskPath(String maskPath) {
        this.maskPath = maskPath;
    }

    public File getResultDescriptor() {
        return resultDescriptor;
    }

    public void setResultDescriptor(File resultDescriptor) {
        this.resultDescriptor = resultDescriptor;
    }

    public String getPatternPath() {
        return patternPath;
    }

    public void setPatternPath(String patternPath) {
        this.patternPath = patternPath;
        updateFrames();
    }

    public String getSamplesPath() {
        return samplesPath;
    }

    public void setSamplesPath(String samplesPath) {
        this.samplesPath = samplesPath;
        updateFrames();
    }

    public TestCase getRoot() {
        return root;
    }

    public File getSuiteDescriptorFile() {
        return suiteDescriptorFile;
    }

    public void setSuiteDescriptorFile(File suiteDescriptorFile) {
        this.suiteDescriptorFile = suiteDescriptorFile;
    }

    public VisualSuite getSuiteDescriptor() {
        return suiteDescriptor;
    }

    public void setSuiteDescriptor(VisualSuite suiteDescriptor) {
        this.suiteDescriptor = suiteDescriptor;
    }

    protected abstract void updateFrames();


}
