/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project;

import java.util.Collections;
import java.util.Enumeration;
import javax.swing.tree.TreeNode;

/**
 *
 * @author hcube
 */
public class TestNode implements TreeNode,Comparable<TestNode>{
    
    private String name;
    private NodeList children;
    private TestNode parent;
    private boolean allowsChildren;
    
    public TestNode(){
        children = new NodeList();
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
            if(tn instanceof TestNode){
                TestNode tn2 = (TestNode) tn;
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
        return children;
    }
    
    public void addChild(TestNode node){
        children.add(node);
        Collections.sort(children);
    }
    
        
    public String getPath(){
        String result=name;
        if(this.getParent()!=null){
            result = parent.getPath()+"."+result;
        }
        
        return result;
    }
    
    @Override
    public boolean equals(Object o){
        TestNode node = (TestNode) o;
        if(this.getName().equals(node.getName()))return true;
        else return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 17 * hash + (this.allowsChildren ? 1 : 0);
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

    public int compareTo(TestNode t) {
        return name.compareTo(t.getName());
    }

    public void setAllowsChildren(boolean allowsChildren) {
        this.allowsChildren = allowsChildren;
    }

    public void setParent(TestNode parent) {
        this.parent = parent;
    }
}
