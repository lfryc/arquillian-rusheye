/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.image.listeners;

import java.awt.Point;
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
            
            start = new Point(e.getXOnScreen() - (int)(41*parent.getScale()), e.getYOnScreen() - (int)(107*parent.getScale()));
            
            System.out.println(start);

            currentMask = new MaskCase();
            currentMask.setShape(new Rect(start, start));
            //TODO hack
            Main.mainProject.getMaskManager().getRoot().addChild(currentMask);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        drawing = false;

        stop = new Point(e.getXOnScreen() - (int)(41*parent.getScale()), e.getYOnScreen() - (int)(107*parent.getScale()));
        
        currentMask.setShape(calculateRect());
        
        System.out.println(stop);
    }

    public void mouseDragged(MouseEvent e) {
        if (drawing) {
            stop = new Point(e.getXOnScreen() - (int)(41*parent.getScale()), e.getYOnScreen() - (int)(107*parent.getScale()));
            
            currentMask.setShape(calculateRect());
            
            pic.repaint();
        }
    }
    
    public Rect calculateRect(){
        if(stop.x-start.x >= 0 && stop.y-start.y >= 0){
                //4
                return new Rect(start, stop);
            }
            else if(stop.x-start.x >= 0 && stop.y-start.y < 0){
                //1
                return new Rect(new Point(start.x,stop.y), new Point(stop.x, start.y));
            }
            else if(stop.x-start.x < 0 && stop.y-start.y < 0){
                //2
                return new Rect(stop,start);
            }
            else if(stop.x-start.x < 0 && stop.y-start.y >= 0){
                //3
                return new Rect(new Point(stop.x,start.y), new Point(start.x, stop.y));
            }
        return null;
    }

    public void mouseMoved(MouseEvent e) {
    }
}
