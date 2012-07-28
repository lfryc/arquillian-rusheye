/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jboss.rusheye.manager.project.observable.Observed;
import org.jboss.rusheye.manager.project.observable.Observer;

/**
 *
 * @author hcube
 */
public abstract class ProjectBase implements Observed {

    protected TestCase root;
    protected TestCase currentCase;
    protected String patternPath;
    protected String samplesPath;
    protected String maskPath;
    protected File suiteDescriptor;
    protected File resultDescriptor;
    private List<Observer> observers;

    public ProjectBase() {
        observers = new ArrayList<Observer>();
    }

    public TestCase getCurrentCase() {
        return currentCase;
    }

    public void setCurrentCase(TestCase currentCase) {
        this.currentCase = currentCase;
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

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    /**
     * Part of observer pattern implementation.
     */
    private void notifyObservers() {
        for (Observer o : observers)
            o.update(this);
    }
}
