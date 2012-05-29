/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.gui.view.image;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 *
 * @author hcube
 */
public class ScrollAdjustmentListener implements AdjustmentListener {
    
    ScrollableImage parentPicture;
    ScrollableImage otherPicture;

    ScrollAdjustmentListener(ScrollableImage picture) {
        parentPicture = picture;
    }

    ScrollAdjustmentListener(ScrollableImage picture, ScrollableImage other) {
        parentPicture = picture;
        otherPicture = other;
    }

    public void adjustmentValueChanged(AdjustmentEvent ae) {
        otherPicture.scrollRectToVisible(parentPicture.getVisibleRect());
    }

    
}
