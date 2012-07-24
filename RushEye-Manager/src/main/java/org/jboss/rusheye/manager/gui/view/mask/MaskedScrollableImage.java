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

    private MaskCase mask;

    public MaskedScrollableImage(ImageIcon icon, int m) {
        super(icon, m);
        mask = Main.mainProject.getMaskManager().getCurrentMask();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (mask != null) {
            for (int i = 0; i < mask.getChildCount(); ++i) {
                ((MaskCase) mask.getChildAt(i)).getShape().draw(g);
            }
        }
    }

    public void setMask(MaskCase mask) {
        this.mask = mask;
    }
}
