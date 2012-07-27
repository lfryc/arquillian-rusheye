/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project;

import java.util.ArrayList;
import java.util.List;
import org.jboss.rusheye.manager.gui.view.mask.MaskCase;

/**
 *
 * @author hcube
 */
public class MaskManager {

    private MaskCase root;
    private MaskCase currentMask;
    
    public MaskManager(){
        root = new MaskCase();
        root.setName("Masks root");
    }

    public MaskCase getRoot() {
        return root;
    }

    public void setRoot(MaskCase root) {
        this.root = root;
    }

    public MaskCase getCurrentMask() {
        return currentMask;
    }

    public void setCurrentMask(MaskCase currentMask) {
        this.currentMask = currentMask;
    }
}
