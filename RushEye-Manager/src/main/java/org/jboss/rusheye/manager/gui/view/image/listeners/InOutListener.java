/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.image.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author cube
 */
public class InOutListener implements MouseListener {

    protected boolean inside;
    
    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseEntered(MouseEvent e) {
        inside = true;
    }

    public void mouseExited(MouseEvent e) {
        inside = false;
    }
    
}
