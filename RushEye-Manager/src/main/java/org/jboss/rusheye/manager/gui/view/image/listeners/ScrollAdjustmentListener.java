/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.image.listeners;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import org.jboss.rusheye.manager.gui.view.image.ImageView;

/**
 *
 * @author hcube
 */
public class ScrollAdjustmentListener implements AdjustmentListener {
    
    ImageView parentView;
    ImageView otherView;

    public ScrollAdjustmentListener(ImageView view) {
        parentView = view;
    }

    public ScrollAdjustmentListener(ImageView view, ImageView other) {
        parentView = view;
        otherView = other;
    }

    public void adjustmentValueChanged(AdjustmentEvent ae) {
        otherView.getPicture().scrollRectToVisible(parentView.getPicture().getVisibleRect());
    }

    
}
