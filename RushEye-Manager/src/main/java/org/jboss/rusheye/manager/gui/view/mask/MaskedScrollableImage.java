/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.mask;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import org.jboss.rusheye.manager.Main;
import org.jboss.rusheye.manager.gui.view.image.ScrollableImage;
import org.jboss.rusheye.manager.project.testcase.MaskCase;

/**
 *
 * @author cube
 */
public class MaskedScrollableImage extends ScrollableImage {
    private double scale = 1;

    public MaskedScrollableImage(ImageIcon icon, int m, double scale) {
        super(icon, m);
        
        this.scale = scale;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        MaskCase  mask = Main.mainProject.getMaskManager().getCurrentMask();
        if (mask != null) {
            for (int i = 0; i < mask.getChildCount(); ++i) {
                ((MaskCase) mask.getChildAt(i)).getShape().draw(g,scale);
            }
        }
    }

}
