/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.image.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author cube
 */
public class MaskDrawListener extends InOutListener implements MouseMotionListener{
    
    private boolean drawing;
    
    @Override
    public void mouseClicked(MouseEvent e) {
        //Set mask params
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("DRAW START");
        drawing = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("DRAW STOP");
        drawing = false;
    }

    public void mouseDragged(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseMoved(MouseEvent e) {
        if(inside){
        //repaint rect
        }
    }
    
}
