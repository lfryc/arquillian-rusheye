/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view;

import javax.swing.JPanel;
import org.jboss.rusheye.manager.gui.view.image.ImagePool;
import org.jboss.rusheye.manager.gui.view.image.ImageView;
import org.jboss.rusheye.manager.gui.view.mask.DrawableImageView;
import org.jboss.rusheye.manager.project.testcase.TestCase;

/**
 *
 * @author cube
 */
public class MaskView extends JPanel {

    private DrawableImageView imageView;

    public MaskView(TestCase testCase) {
        imageView = new DrawableImageView(testCase, ImagePool.DIFF);
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
        add(imageView);
    }
}