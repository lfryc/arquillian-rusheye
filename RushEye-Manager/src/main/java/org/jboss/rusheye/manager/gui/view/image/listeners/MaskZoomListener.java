/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.image.listeners;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import org.jboss.rusheye.manager.Main;
import org.jboss.rusheye.manager.gui.view.image.ImageView;
import org.jboss.rusheye.manager.gui.view.mask.MaskedScrollableImage;
import org.jboss.rusheye.manager.gui.view.mask.Rect;
import org.jboss.rusheye.manager.project.testcase.MaskCase;

/**
 *
 * @author cube
 */
public class MaskZoomListener extends ZoomListener implements MouseMotionListener {

    private boolean drawing;
    private MaskedScrollableImage pic;
    private Point start;
    private Point stop;
    private MaskCase currentMask;

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

            //start = new Point(e.getXOnScreen() - (int)(41*parent.getScale()), e.getYOnScreen() - (int)(107*parent.getScale()));

            Point corner = Main.interfaceFrame.getLocation();
            Rectangle visible = parent.getPicture().getVisibleRect();
            
            System.out.println(corner + " " + visible.x + " " + visible.y + " " + e.getX() + " " + e.getY());
            
            int px = (int) (visible.getX() + e.getX() - corner.x - 41);
            int py = (int) (visible.getY() + e.getY() - corner.y - 107);
            
            start = new Point(px, py);

            System.out.println(start + " " + Main.mainProject.getMaskManager().getCurrentMask().getChildCount());

            currentMask = new MaskCase();
            currentMask.setShape(new Rect(start, start));
            currentMask.setName("Rect " + (Main.mainProject.getMaskManager().getCurrentMask().getChildCount() + 1));
            Main.mainProject.getMaskManager().getCurrentMask().addChild(currentMask);

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        drawing = false;

        Point corner = Main.interfaceFrame.getLocation();

            Rectangle visible = parent.getPicture().getVisibleRect();
            int px = (int) (visible.getX() + e.getX() - corner.x - 41);
            int py = (int) (visible.getY() + e.getY() - corner.y - 107);

            stop = new Point(px, py);

        currentMask.setShape(calculateRect());

        Main.maskFrame.updateTreeModel();
        System.out.println(stop);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (drawing) {

            Point corner = Main.interfaceFrame.getLocation();

            Rectangle visible = parent.getPicture().getVisibleRect();
            int px = (int) (visible.getX() + e.getX() - corner.x - 41);
            int py = (int) (visible.getY() + e.getY() - corner.y - 107);

            stop = new Point(px, py);

            currentMask.setShape(calculateRect());

            pic.repaint();
        }
    }

    public Rect calculateRect() {
        if (stop.x - start.x >= 0 && stop.y - start.y >= 0) {
            //4
            return new Rect(start, stop);
        } else if (stop.x - start.x >= 0 && stop.y - start.y < 0) {
            //1
            return new Rect(new Point(start.x, stop.y), new Point(stop.x, start.y));
        } else if (stop.x - start.x < 0 && stop.y - start.y < 0) {
            //2
            return new Rect(stop, start);
        } else if (stop.x - start.x < 0 && stop.y - start.y >= 0) {
            //3
            return new Rect(new Point(stop.x, start.y), new Point(start.x, stop.y));
        }
        return null;
    }

    public void mouseMoved(MouseEvent e) {
    }
}
