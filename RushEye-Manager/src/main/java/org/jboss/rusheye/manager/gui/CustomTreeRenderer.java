/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui;

import java.awt.Component;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.jboss.rusheye.manager.project.TestCase;
import org.jboss.rusheye.suite.ResultConclusion;

/**
 * Custom renderer for JTree. Manages icons.
 *
 * @author Jakub D.
 */
public class CustomTreeRenderer extends DefaultTreeCellRenderer {

    private ImageIcon same, notTested, diff, pSame;

    /**
     * COstructor where we initialize icons.
     */
    public CustomTreeRenderer() {
        same = new ImageIcon("same.png");
        notTested = new ImageIcon("not_tested.png");
        diff = new ImageIcon("diff.png");
        pSame = new ImageIcon("pSame.png");
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel,
                expanded, leaf, row, hasFocus);

        if (value instanceof TestCase) {
            TestCase node = (TestCase) value;
            if (node.isLeaf()) {
                setIcon(getTestCaseIcon(node));
            }
        }
        return this;
    }

    private ImageIcon getTestCaseIcon(TestCase test) {
            if (test.getConclusion() == ResultConclusion.SAME)
                return same;
            else if (test.getConclusion() == ResultConclusion.PERCEPTUALLY_SAME)
                return pSame;
            else if (test.getConclusion() == ResultConclusion.DIFFER)
                return diff;
            else if (test.getConclusion() == ResultConclusion.NOT_TESTED || test.getConclusion() == null)
                return notTested;
            else
                return new ImageIcon(new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB));
    }
}