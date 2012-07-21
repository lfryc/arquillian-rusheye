/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.mask;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import org.jboss.rusheye.manager.gui.view.image.ScrollableImage;

/**
 *
 * @author cube
 */
public class MaskedScrollableImage extends ScrollableImage {

    private List<ManagerMask> masks;
    
    public MaskedScrollableImage(ImageIcon icon, int m) {
        super(icon, m);
        masks = new ArrayList<ManagerMask>();
    }
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
        for(ManagerMask mask : getMasks()){
            mask.getShape().draw(g);
        }
    }

    /**
     * @return the masks
     */
    public List<ManagerMask> getMasks() {
        return masks;
    }

    /**
     * @param masks the masks to set
     */
    public void setMasks(List<ManagerMask> masks) {
        this.masks = masks;
    }
    
    
}
