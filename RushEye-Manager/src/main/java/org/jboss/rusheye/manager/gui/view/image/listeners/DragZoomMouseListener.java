/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.image.listeners;

import java.awt.Rectangle;
import java.awt.event.*;
import javax.swing.SwingUtilities;
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
     *
     * @param parent ImageView where listener is set
     */
    public DragZoomMouseListener(ImageView parent) {
        super(parent);
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        if (SwingUtilities.isLeftMouseButton(me)) {
            x = -1;
            y = -1;
        }
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        if (inside) {
            if (SwingUtilities.isLeftMouseButton(me)) {

                int tmpX = me.getXOnScreen();
                int tmpY = me.getYOnScreen();
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
                System.out.println(tmpX + " " + tmpY + " : " + v_x + " " + v_y);
                parent.getPicture().scrollRectToVisible(new Rectangle((int) (parentRect.getX() - v_x), (int) (parentRect.getY() - v_y), (int) parentRect.getWidth(), (int) parentRect.getHeight()));

                x = tmpX;
                y = tmpY;
            }

        }
    }

    public void mouseMoved(MouseEvent me) {
    }
}
