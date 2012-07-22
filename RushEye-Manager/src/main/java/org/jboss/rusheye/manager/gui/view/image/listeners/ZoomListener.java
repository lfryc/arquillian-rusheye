/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.image.listeners;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import org.jboss.rusheye.manager.gui.view.image.ImageView;

/**
 *
 * @author cube
 */
public class ZoomListener extends ManagerMouseListener implements MouseWheelListener {

    protected ImageView parent;
    protected double scaleMod = 0.5;
    
    public ZoomListener(ImageView parent){
        this.parent = parent;
    }

    public void mouseWheelMoved(MouseWheelEvent mwe) {
        if (inside) {

            Rectangle visible = parent.getPicture().getVisibleRect();
            double px = visible.getX() + mwe.getXOnScreen() - 41;
            double py = visible.getY() + mwe.getYOnScreen() - 107;

            double xMax = parent.getImg().getWidth() * parent.getScale();
            double yMax = parent.getImg().getHeight() * parent.getScale();
            double px2 = px / xMax;
            double py2 = py / yMax;

            //System.out.println(mwe.getXOnScreen() + " " + mwe.getYOnScreen());
            //System.out.println(px + " " + py);
            //System.out.println(parent.getImg().getWidth() + " " + parent.getImg().getHeight() + " " + parent.getScale());
            //System.out.println(px2 + " " + py2);

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

                parent.getPicture().scrollRectToVisible(new Rectangle((int) (px - mwe.getXOnScreen() + 41), (int) (py - mwe.getYOnScreen() + 107), visible.width, visible.height));
            }
        }
    }
}
