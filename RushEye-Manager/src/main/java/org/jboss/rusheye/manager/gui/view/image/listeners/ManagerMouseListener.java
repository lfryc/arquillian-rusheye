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
public abstract class ManagerMouseListener implements MouseListener {

    protected boolean inside;
    
    public void mouseClicked(MouseEvent e) {  
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        inside = true;
    }

    public void mouseExited(MouseEvent e) {
        inside = false;
    }
    
}
