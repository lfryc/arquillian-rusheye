/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import org.jboss.rusheye.manager.Main;
import org.jboss.rusheye.core.DefaultImageComparator;
import org.jboss.rusheye.manager.gui.view.image.ImagePool;
import org.jboss.rusheye.manager.gui.view.image.ImageView;
import org.jboss.rusheye.manager.project.TestCase;
import org.jboss.rusheye.manager.utils.ImageUtils;
import org.jboss.rusheye.parser.DefaultConfiguration;
import org.jboss.rusheye.result.ResultEvaluator;
import org.jboss.rusheye.suite.ComparisonResult;
import org.jboss.rusheye.suite.Configuration;
import org.jboss.rusheye.suite.ResultConclusion;

/**
 *
 * @author hcube
 */
public class SingleView extends JPanel {

    private ImageView imageView;
    
    public SingleView(TestCase testCase){
        imageView = new ImageView(testCase,ImagePool.DIFF);
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
        add(imageView);
    }
    
}
