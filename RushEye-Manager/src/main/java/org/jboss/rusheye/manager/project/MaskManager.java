/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project;

import org.jboss.rusheye.manager.gui.view.mask.MaskCase;
import org.jboss.rusheye.manager.gui.view.mask.MaskType;

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
        
        MaskCase newCase = new MaskCase();
        newCase.setName("mask-" + (root.getChildCount() + 1));
        newCase.setType(MaskType.SELECTIVE_ALPHA);

        root.addChild(newCase);
        
        currentMask = newCase;
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
