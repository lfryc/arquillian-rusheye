/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project.testcase;

import org.jboss.rusheye.manager.gui.view.mask.Shape;
import org.jboss.rusheye.manager.project.tree.TreeNodeImpl;

/**
 *
 * @author Jakub D.
 */
public class MaskCase extends TreeNodeImpl {

    private Shape shape;
    private MaskType type;

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public MaskType getType() {
        return type;
    }

    public void setType(MaskType type) {
        this.type = type;
    }
    
    
}
