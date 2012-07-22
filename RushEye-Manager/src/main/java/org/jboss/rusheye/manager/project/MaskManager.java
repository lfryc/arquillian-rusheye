/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project;

import java.util.ArrayList;
import java.util.List;
import org.jboss.rusheye.manager.gui.view.mask.ManagerMask;

/**
 *
 * @author hcube
 */
public class MaskManager {

    private List<ManagerMask> masks;
    
    public MaskManager(){
        masks = new ArrayList<ManagerMask>();
    }

    public List<ManagerMask> getMasks() {
        return masks;
    }

    public void setMasks(List<ManagerMask> masks) {
        this.masks = masks;
    }
}
