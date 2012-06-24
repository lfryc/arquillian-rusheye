/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.image;

import java.awt.Rectangle;
import java.awt.event.*;

/**
 *
 * @author hcube
 */
public class ZoomDragMouseListener implements MouseListener, MouseWheelListener, MouseMotionListener  {

    private ImageView parent;
    private boolean inside;
    private boolean drag;
    private int x = -1;
    private int y = -1;
    private final double scaleMod = 0.5;

    public ZoomDragMouseListener() {
    }

    public ZoomDragMouseListener(ImageView parent) {
        this.parent = parent;
    }

    public void mouseClicked(MouseEvent me) {
        //not important
    }

    public void mousePressed(MouseEvent me) {
        System.out.println("drag on");
        
    }

    public void mouseReleased(MouseEvent me) {
        System.out.println("drag off");
        x = -1;
        y = -1;
    }

    public void mouseEntered(MouseEvent me) {
        inside = true;
        System.out.println("on");
    }

    public void mouseExited(MouseEvent me) {
        inside = false;
        System.out.println("off");
    }

    public void mouseWheelMoved(MouseWheelEvent mwe) {
        if (inside) {
            System.out.println(mwe.getXOnScreen());
            System.out.println(mwe.getYOnScreen());
            int notches = mwe.getWheelRotation();
            if (parent != null) {
                if (notches < 0) {
                    parent.changeScale(scaleMod);
                } else {
                    if (parent.getScale() - scaleMod > 0) {
                        parent.changeScale(-scaleMod);
                    }
                }
            }
        }
    }

    public boolean getDrag() {
        return drag;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void mouseDragged(MouseEvent me) {
        if (inside) {
            int tmpX = me.getX();
            int tmpY = me.getY();
            if (x == -1 && y == -1) {
                x = tmpX;
                y = tmpY;
                return;
            }
            if(x-tmpX ==0 && y-tmpY==0){
                return;
            }
            
            double v_x = tmpX - x;
            double v_y = tmpY - y;
            System.out.println(tmpX + " " + tmpY + " : " + v_x + " " + v_y);
            
          
            Rectangle parentRect= parent.getPicture().getVisibleRect();
            
            parent.getPicture().scrollRectToVisible(new Rectangle((int)(parentRect.getX() - v_x/1.5), (int)(parentRect.getY() - v_y/1.5),(int)parentRect.getWidth(), (int)parentRect.getHeight()));
            
            x = tmpX;
            y = tmpY;
            
        }
    }

    public void mouseMoved(MouseEvent me) {
    }
}
