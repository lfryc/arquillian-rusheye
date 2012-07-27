/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.image.listeners;

import java.awt.Rectangle;
import java.awt.event.*;
import org.jboss.rusheye.manager.gui.view.image.ImageView;

/**
 * Manager Mouse Listener. MouseWheelListener is responsible for scaling.
 * MouseListener for checking whether we are over image or not.
 * MouseMotionListener for dragging image
 *
 * @author Jakub D.
 */
public class DragZoomMouseListener extends ZoomListener implements MouseMotionListener {

    private int x = -1;
    private int y = -1;
    
    /**
     * Constructor of listener. 
     * @param parent ImageView where listener is set
     */
    public DragZoomMouseListener(ImageView parent) {
        super(parent);
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        x = -1;
        y = -1;
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        if (inside) {
            int tmpX = me.getX();
            int tmpY = me.getY();
            if (x == -1 && y == -1) {
                x = tmpX;
                y = tmpY;
                return;
            }
            if (x - tmpX == 0 && y - tmpY == 0) {
                return;
            }

            double v_x = tmpX - x;
            double v_y = tmpY - y;

            Rectangle parentRect = parent.getPicture().getVisibleRect();

            parent.getPicture().scrollRectToVisible(new Rectangle((int) (parentRect.getX() - v_x / 1.5), (int) (parentRect.getY() - v_y / 1.5), (int) parentRect.getWidth(), (int) parentRect.getHeight()));

            x = tmpX;
            y = tmpY;

        }
    }

    public void mouseMoved(MouseEvent me) {
    }
}
