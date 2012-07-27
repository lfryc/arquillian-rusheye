/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.mask;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.jboss.rusheye.manager.project.testcase.MaskCase;

/**
 *
 * @author hcube
 */
public class MaskToImageConverter extends MaskConverter {

    public MaskToImageConverter(MaskCase m) {
        super(m);
    }

    @Override
    public void save(File file) {
        BufferedImage image =
                new BufferedImage(1024, 768, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = image.createGraphics();
        for (int i = 0; i < mask.getChildCount(); ++i) {
            MaskCase m = (MaskCase) mask.getChildAt(i);
            m.getShape().draw(g2,1);
        }

        try {
            ImageIO.write(image, "png", file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
