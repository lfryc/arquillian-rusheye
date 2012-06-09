/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.image;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 *
 * @author hcube
 */
public class ScrollAdjustmentListener implements AdjustmentListener {
    
    ImageView parentView;
    ImageView otherView;

    ScrollAdjustmentListener(ImageView view) {
        parentView = view;
    }

    ScrollAdjustmentListener(ImageView view, ImageView other) {
        parentView = view;
        otherView = other;
    }

    public void adjustmentValueChanged(AdjustmentEvent ae) {
        otherView.getPicture().scrollRectToVisible(parentView.getPicture().getVisibleRect());
    }

    
}
