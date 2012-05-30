/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.project;

import org.jboss.rusheye.suite.ResultConclusion;

/**
 *
 * @author hcube
 */
public class Test implements Comparable<Test> {
    private String name;
    private ResultConclusion conclusion;
    private boolean checked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResultConclusion getConclusion() {
        return conclusion;
    }

    public void setConclusion(ResultConclusion conclusion) {
        this.conclusion = conclusion;
    }

    public int compareTo(Test t) {
        return name.compareTo(t.getName());
    }
    
    @Override
    public String toString(){
        return name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
