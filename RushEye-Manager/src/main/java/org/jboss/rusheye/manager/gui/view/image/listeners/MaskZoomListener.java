/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.image.listeners;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import org.jboss.rusheye.manager.gui.view.image.ImageView;
import org.jboss.rusheye.manager.gui.view.mask.ManagerMask;
import org.jboss.rusheye.manager.gui.view.mask.MaskedScrollableImage;
import org.jboss.rusheye.manager.gui.view.mask.Rect;

/**
 *
 * @author cube
 */
public class MaskZoomListener extends ZoomListener implements MouseMotionListener {

    private boolean drawing;
    private MaskedScrollableImage pic;
    private Point start;
    private Point stop;
    private ManagerMask currentMask;

    public MaskZoomListener(ImageView imgView) {
        super(imgView);
        pic = (MaskedScrollableImage) imgView.getPicture();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //Set mask params
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (inside) {
            drawing = true;
            start = new Point(e.getXOnScreen() - (int)(41*parent.getScale()), e.getYOnScreen() - (int)(107*parent.getScale()));
            System.out.println(start);

            currentMask = new ManagerMask();
            currentMask.setShape(new Rect(start, start));
            pic.addMask(currentMask);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        drawing = false;

        stop = new Point(e.getXOnScreen() - (int)(41*parent.getScale()), e.getYOnScreen() - (int)(107*parent.getScale()));
        currentMask.setShape(new Rect(start, stop));
        System.out.println(stop);
    }

    public void mouseDragged(MouseEvent e) {
        if (drawing) {
            stop = new Point(e.getXOnScreen() - (int)(41*parent.getScale()), e.getYOnScreen() - (int)(107*parent.getScale()));
            currentMask.setShape(new Rect(start, stop));
            pic.repaint();
        }
    }

    public void mouseMoved(MouseEvent e) {
    }
}
