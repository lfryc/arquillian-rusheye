/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.jboss.rusheye.manager.project.TestCase;
import org.jboss.rusheye.suite.ResultConclusion;

/**
 *
 * @author hcube
 */
public class CustomTreeRenderer extends DefaultTreeCellRenderer {

    ImageIcon same, notTested, diff;

    public CustomTreeRenderer() {
        same = new ImageIcon("same.png");
        notTested = new ImageIcon("not_tested.png");
        diff = new ImageIcon("diff.png");
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel,
                expanded, leaf, row, hasFocus);

        if (value instanceof TestCase) {
            TestCase node = (TestCase) value;

            if (node.getConclusion() == ResultConclusion.PERCEPTUALLY_SAME)
                setIcon(same);
            else if (node.getConclusion() == ResultConclusion.DIFFER)
                setIcon(diff);
            else if (node.getConclusion() == ResultConclusion.NOT_TESTED)
                setIcon(notTested);
        }
        return this;
    }
}