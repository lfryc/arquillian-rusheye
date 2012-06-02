/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.image;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 *
 * @author hcube
 */
public class ZoomMouseListener implements MouseListener, MouseWheelListener {

    private ImageView parent;
    private boolean inside;
    private final double scaleMod = 0.1;

    public ZoomMouseListener() {
    }

    public ZoomMouseListener(ImageView parent) {
        this.parent = parent;
    }

    public void mouseClicked(MouseEvent me) {
        //not important
    }

    public void mousePressed(MouseEvent me) {
        //not important
    }

    public void mouseReleased(MouseEvent me) {
        //not important
    }

    public void mouseEntered(MouseEvent me) {
        System.out.println("on");
        inside = true;
    }

    public void mouseExited(MouseEvent me) {
        System.out.println("off");
        inside = false;
    }

    public void mouseWheelMoved(MouseWheelEvent mwe) {
        System.out.println("scroll");
        if (inside) {
            System.out.println(mwe.getXOnScreen());
            System.out.println(mwe.getYOnScreen());
            int notches = mwe.getWheelRotation();
            if (parent != null) {
                if (notches < 0) {
                    parent.changeScale(scaleMod);
                } else {
                    parent.changeScale(-scaleMod);
                }
            }
            System.out.println(parent.getScale());
        }
    }
}
