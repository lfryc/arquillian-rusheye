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
            start = new Point(e.getXOnScreen() - 41, e.getYOnScreen() - 107);
            System.out.println(start);

            currentMask = new ManagerMask();
            currentMask.setShape(new Rect(start, start));
            pic.addMask(currentMask);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        drawing = false;

        stop = new Point(e.getXOnScreen() - 41, e.getYOnScreen() - 107);
        currentMask.setShape(new Rect(start, stop));
        System.out.println(stop);
    }

    public void mouseDragged(MouseEvent e) {
        if (drawing) {
            currentMask.setShape(new Rect(start, new Point(e.getXOnScreen() - 41, e.getYOnScreen() - 107)));
            System.out.println(pic.getMasks().size() + " " + pic.getMasks().get(0));
            pic.repaint();
        }
    }

    public void mouseMoved(MouseEvent e) {
    }
}
