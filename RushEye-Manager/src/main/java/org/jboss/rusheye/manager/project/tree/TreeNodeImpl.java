/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project.tree;

import java.util.Collections;
import java.util.Enumeration;
import javax.swing.tree.TreeNode;

/**
 * Implementation of javax.swing.tree.TreeNode. Object implementing such
 * interface can be use in JTree
 *
 * @author Jakub D.
 */
public class TreeNodeImpl implements TreeNode, Comparable<TreeNodeImpl> {

    private String name;
    private NodeList children;
    private TreeNodeImpl parent;
    private boolean allowsChildren;
    private boolean visible = true;

    public TreeNodeImpl() {
        children = new NodeList();
    }

    public TreeNode getChildAt(int i) {
        return visibleChildren().get(i);
    }

    public int getChildCount() {
        return visibleChildren().size();
    }

    public TreeNode getParent() {
        return parent;
    }

    public int getIndex(TreeNode tn) {
        for (int i = 0; i < visibleChildren().size(); ++i) {
            if (tn instanceof TreeNodeImpl) {
                TreeNodeImpl tn2 = (TreeNodeImpl) tn;
                if (visibleChildren().get(i).equals(tn2)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean getAllowsChildren() {
        return allowsChildren;
    }

    public boolean isLeaf() {
        if (visibleChildren().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public Enumeration children() {
        return visibleChildren();
    }

    public int compareTo(TreeNodeImpl t) {
        return name.compareTo(t.getName());
    }

    /**
     * Path to this node in tree.
     * @return path directing to this test node
     */
    public String getPath() {
        String result = name;
        if (this.getParent() != null) {
            result = parent.getPath() + "." + result;
        }

        return result;
    }

    public void addChild(TreeNodeImpl node) {
        children.add(node);
        node.setParent(this);
        Collections.sort(children);
    }

    public NodeList getAllChildren() {
        return children;
    }

    private NodeList visibleChildren() {
        NodeList result = new NodeList();

        for (TreeNodeImpl node : children) {
            if (node.isVisible()) {
                result.add(node);
            }
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TreeNodeImpl) {
            TreeNodeImpl node = (TreeNodeImpl) o;
            if (this.getName().equals(node.getName()))
                return true;
            else
                return false;
        } else
            return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 17 * hash + (this.allowsChildren ? 1 : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAllowsChildren(boolean allowsChildren) {
        this.allowsChildren = allowsChildren;
    }

    public void setParent(TreeNodeImpl parent) {
        this.parent = parent;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
