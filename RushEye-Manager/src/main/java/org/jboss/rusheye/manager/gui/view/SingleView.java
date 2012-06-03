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
import org.jboss.rusheye.manager.gui.SingleViewMenuFrame;
import org.jboss.rusheye.manager.gui.view.image.ImageView;
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
    private String path1, path2;
    private BufferedImage pattern, sample, diff;
    private ComparisonResult result;
    private Configuration configuration;
    private ResultConclusion conclusion;

    public SingleView(String path1, String path2, int state) {
        this.path1 = path1;
        this.path2 = path2;

        try {
            pattern = ImageIO.read(new File(this.path1));
            sample = ImageIO.read(new File(this.path2));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        configuration = new DefaultConfiguration();
        result = new DefaultImageComparator().compare(pattern, sample, configuration.getPerception(),
                configuration.getMasks());
        conclusion = new ResultEvaluator().evaluate(configuration.getPerception(), result);
        if (Main.mainProject.getCurrentCase().isChecked() == false) {
            Main.mainProject.getCurrentCase().setConclusion(conclusion);
        }

        diff = result.getDiffImage();

        switch (state) {
            case SingleViewMenuFrame.SAMPLE:
                imageView = new ImageView(sample);
                break;
            case SingleViewMenuFrame.PATTERN:
                imageView = new ImageView(pattern);
                break;
            case SingleViewMenuFrame.DIFF:
                imageView = new ImageView(diff);
                break;
        }

        initComponents();
    }

    private void initComponents() {
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
        add(imageView);
    }

    public void focus() {
        int w = diff.getWidth();
        int h = diff.getHeight();

        int x = 0;
        int y = 0;
        outer:
        for (int i = 0; i < w; ++i) {
            for (int k = 0; k < h; ++k) {
                int color = diff.getRGB(i, k);
                int r = ImageUtils.getR(color);
                int g = ImageUtils.getG(color);
                int b = ImageUtils.getB(color);

                //we find beginnig of blue bounding box
                if (b > r && b > g) {
                    x = i;
                    y = k;
                    break outer;
                }
            }
        }
        imageView.getPicture().scrollRectToVisible(new Rectangle(x, y,(int)imageView.getPicture().getVisibleRect().getWidth(), (int)imageView.getPicture().getVisibleRect().getHeight()));
        System.out.println(imageView.getPicture().getVisibleRect());
    }
}
