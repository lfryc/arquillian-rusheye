/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.image;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jboss.rusheye.manager.gui.view.BottomMenu;
import org.jboss.rusheye.manager.project.TestCase;
import org.jboss.rusheye.manager.utils.ImageUtils;

/**
 *
 * @author hcube
 */
public class ImageView extends JPanel {

    private Rule columnView, rowView;
    private ScrollableImage picture;
    private double scale = 1;
    private JScrollPane pictureScrollPane;
    private BufferedImage img;
    private boolean allowScale = true;
    private BottomMenu menu;
    private TestCase testCase;

    public ImageView(TestCase testCase, String key) {
        this.testCase = testCase;
        img = testCase.getImage(key);
        ImageIcon image = new ImageIcon(img);
        
        menu = new BottomMenu(this);
        initComponent(image);
    }
    
    public void changeImage(String key){
        this.removeAll();
        
        img = testCase.getImage(key);
        ImageIcon image = new ImageIcon(img);
        initComponent(image);
        this.validate();
    }

    private void initComponent(ImageIcon image) {
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        columnView = new Rule(RuleOrientation.HORIZONTAL, false, scale);
        rowView = new Rule(RuleOrientation.VERTICAL, false, scale);

        columnView.setPreferredWidth(image.getIconWidth());
        rowView.setPreferredHeight(image.getIconHeight());

        picture = new ScrollableImage(image, columnView.getIncrement());

        pictureScrollPane = new JScrollPane(picture);
        pictureScrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.black));

        pictureScrollPane.setColumnHeaderView(columnView);
        pictureScrollPane.setRowHeaderView(rowView);

        pictureScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, new JPanel());
        pictureScrollPane.setCorner(JScrollPane.LOWER_LEFT_CORNER, new JPanel());
        pictureScrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, new JPanel());

        add(pictureScrollPane);
        
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        addZoomListener();
        
        add(menu);
    }
    
        public void focus() {
        
        int w = img.getWidth();
        int h = img.getHeight();

        int x = 0;
        int y = 0;
        outer:
        for (int i = 0; i < w; ++i) {
            for (int k = 0; k < h; ++k) {
                int color = img.getRGB(i, k);
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
        this.getPicture().scrollRectToVisible(new Rectangle(x, y,(int)this.getPicture().getVisibleRect().getWidth(), (int)this.getPicture().getVisibleRect().getHeight()));
        System.out.println(this.getPicture().getVisibleRect());
        
    }

    public void addScrollListener(ScrollableImage other) {
        ScrollAdjustmentListener scrollListener = new ScrollAdjustmentListener(picture, other);

        pictureScrollPane.getHorizontalScrollBar().addAdjustmentListener(scrollListener);
        pictureScrollPane.getVerticalScrollBar().addAdjustmentListener(scrollListener);
    }

    public void addZoomListener() {
        ZoomMouseListener zoomListener = new ZoomMouseListener(this);
        picture.addMouseListener(zoomListener);
        picture.addMouseWheelListener(zoomListener);
    }

    public ScrollableImage getPicture() {
        return picture;
    }
    
    public BufferedImage getImg(){
        return img;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
        rescale();
    }

    public void changeScale(double val) {
        this.scale += val;
        rescale();
    }

    public void rescale() {
        if (allowScale) {
            this.removeAll();
            ImageIcon image = new ImageIcon(ImageUtils.scale(img, scale));
            initComponent(image);
            this.validate();
        }
    }

    public boolean isAllowScale() {
        return allowScale;
    }

    public void setAllowScale(boolean allowScale) {
        this.allowScale = allowScale;
    }

}
