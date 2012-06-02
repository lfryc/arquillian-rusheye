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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean getAllowsChildren() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isLeaf() {
        if(children.isEmpty()) return true;
        else return false;
    }

    public Enumeration children() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public String toString(){
        return name;
    }
}
