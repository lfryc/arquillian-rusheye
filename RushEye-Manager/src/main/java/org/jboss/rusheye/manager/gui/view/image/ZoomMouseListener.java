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
public class ZoomMouseListener implements MouseListener, MouseWheelListener{

    public void mouseClicked(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mousePressed(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseReleased(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseEntered(MouseEvent me) {
        System.out.println("on");
    }

    public void mouseExited(MouseEvent me) {
        System.out.println("off");
    }

    public void mouseWheelMoved(MouseWheelEvent mwe) {
        System.out.println("scroll");
    }
    
}
