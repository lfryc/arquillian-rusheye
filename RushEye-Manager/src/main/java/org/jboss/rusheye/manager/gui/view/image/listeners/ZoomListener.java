/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.image.listeners;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import org.jboss.rusheye.manager.Main;
import org.jboss.rusheye.manager.gui.view.image.ImageView;

/**
 *
 * @author cube
 */
public class ZoomListener extends ManagerMouseListener implements MouseWheelListener {

    protected ImageView parent;
    protected double scaleMod = 0.5;

    public ZoomListener(ImageView parent) {
        this.parent = parent;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {
        if (inside) {

            Rectangle visible = parent.getPicture().getVisibleRect();
            double px = mwe.getX();
            double py = mwe.getY();

            double xMax = parent.getImg().getWidth() * parent.getScale();
            double yMax = parent.getImg().getHeight() * parent.getScale();
            double px2 = px / xMax;
            double py2 = py / yMax;

            int notches = mwe.getWheelRotation();
            if (parent != null) {
                if (notches < 0) {
                    parent.changeScale(scaleMod);
                } else {
                    if (parent.getScale() - scaleMod > 0) {
                        parent.changeScale(-scaleMod);
                    }
                }

                xMax = parent.getImg().getWidth() * parent.getScale();
                yMax = parent.getImg().getHeight() * parent.getScale();

                px = px2 * xMax;
                py = py2 * yMax;

                parent.getPicture().scrollRectToVisible( new Rectangle((int) (px - mwe.getX()), (int) (py - mwe.getY()), visible.width, visible.height));
            }
        }
    }
}
