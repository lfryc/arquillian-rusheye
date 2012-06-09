/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project;

import java.util.ArrayList;
import java.util.Collections;
import org.jboss.rusheye.suite.ResultConclusion;

/**
 *
 * @author hcube
 */
public class TestCase extends TestNode {

    private ResultConclusion conclusion;
    private boolean checked;
    private String filename;

    public TestCase() {
        super();
    }

    public ResultConclusion getConclusion() {
        return conclusion;
    }

    public void setConclusion(ResultConclusion conclusion) {
        this.conclusion = conclusion;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String toString() {
        return this.getName();
    }

    public TestCase findTest(String path) {
        if (this.getPath().equals(path)) {
            return this;
        } else {
            for (int i = 0; i < this.getChildCount(); ++i) {
                TestCase child = (TestCase) this.getChildAt(i);
                TestCase result = child.findTest(path);
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setVisibility(ResultConclusion con) {
        if (conclusion == null || conclusion == con) {
            this.setVisible(true);
        } else {
            this.setVisible(false);
        }

        for (int i = 0; i < this.getAllChildren().size(); ++i) {
            ((TestCase) this.getAllChildren().get(i)).setVisibility(con);
        }
        
        collapseInvalidLeafs();
    }
    
    private void collapseInvalidLeafs(){
        if(conclusion == null && this.getChildCount()==0) this.setVisible(false);
        for (int i = 0; i < this.getAllChildren().size(); ++i) {
            ((TestCase) this.getAllChildren().get(i)).collapseInvalidLeafs();
        }
    }

    public void setAllVisible() {
        this.setVisible(true);

        for (int i = 0; i < this.getAllChildren().size(); ++i) {
            ((TestCase) this.getAllChildren().get(i)).setAllVisible();
        }
    }
}
