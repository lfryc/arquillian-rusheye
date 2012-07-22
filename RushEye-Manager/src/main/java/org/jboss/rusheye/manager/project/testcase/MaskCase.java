/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project.testcase;

import org.jboss.rusheye.manager.gui.view.mask.Shape;
import org.jboss.rusheye.manager.project.tree.TreeNodeImpl;

/**
 *
 * @author hcube
 */
public class MaskCase extends TreeNodeImpl {

    private Shape shape;
    //private MaskType type;

    /**
     * @return the shape
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * @param shape the shape to set
     */
    public void setShape(Shape shape) {
        this.shape = shape;
    }
    
    
}
