/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.TreeNode;

/**
 *
 * @author hcube
 */
public class TestCaseNode implements TreeNode{
    
    private String name;
    private List<TestCaseNode> children;
    private TestCaseNode parent;
    private boolean allowsChildren;
    
    public TestCaseNode(){
        children = new ArrayList<TestCaseNode>();
    }

    public TreeNode getChildAt(int i) {
        return children.get(i);
    }

    public int getChildCount() {
        return children.size();
    }

    public TreeNode getParent() {
        return parent;
    }

    public int getIndex(TreeNode tn) {
        for(int i=0; i<children.size();++i){
            if(tn instanceof TestCaseNode){
                TestCaseNode tn2 = (TestCaseNode) tn;
                if(children.get(i).equals(tn2))return i;
            }
        }
        return -1;
    }

    public boolean getAllowsChildren() {
        return allowsChildren;
    }

    public boolean isLeaf() {
        if(children.isEmpty()) return true;
        else return false;
    }

    public Enumeration children() {
        return null;
    }
    
    @Override
    public boolean equals(Object o){
        TestCaseNode node = (TestCaseNode) o;
        if(this.getName().equals(node.getName()))return true;
        else return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.getName() != null ? this.getName().hashCode() : 0);
        hash = 31 * hash + (this.children != null ? this.children.hashCode() : 0);
        hash = 31 * hash + (this.parent != null ? this.parent.hashCode() : 0);
        hash = 31 * hash + (this.allowsChildren ? 1 : 0);
        return hash;
    }
    
    @Override
    public String toString(){
        return getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
