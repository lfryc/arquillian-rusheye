/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.image.listeners;

import java.awt.Rectangle;
import java.awt.event.*;
import org.jboss.rusheye.manager.gui.view.image.ImageView;

/**
 *
 * @author hcube
 */
public class ZoomDragMouseListener implements MouseListener, MouseWheelListener, MouseMotionListener {

    private ImageView parent;
    private boolean inside;
    private boolean drag;
    private int x = -1;
    private int y = -1;
    private final double scaleMod = 0.5;

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
                
                parent.getPicture().scrollRectToVisible(new Rectangle((int)(px - mwe.getXOnScreen() + 41),(int)(py - mwe.getYOnScreen() + 107),visible.width,visible.height));
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
            if (x - tmpX == 0 && y - tmpY == 0) {
                return;
            }

            double v_x = tmpX - x;
            double v_y = tmpY - y;
            System.out.println(tmpX + " " + tmpY + " : " + v_x + " " + v_y);


            Rectangle parentRect = parent.getPicture().getVisibleRect();

            parent.getPicture().scrollRectToVisible(new Rectangle((int) (parentRect.getX() - v_x / 1.5), (int) (parentRect.getY() - v_y / 1.5), (int) parentRect.getWidth(), (int) parentRect.getHeight()));

            x = tmpX;
            y = tmpY;

        }
    }

    public void mouseMoved(MouseEvent me) {
    }
}
