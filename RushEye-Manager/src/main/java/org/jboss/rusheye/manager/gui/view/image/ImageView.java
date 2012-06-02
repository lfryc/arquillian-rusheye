/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
    
    public ImageView(BufferedImage img) {
        this.img = img;
        ImageIcon image = new ImageIcon(img);
        initComponent(image);
    }

    public ImageView(String imagePath) {
        ImageIcon image = new ImageIcon(imagePath);
        initComponent(image);
    }

    private void initComponent(ImageIcon image) {
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        columnView = new Rule(RuleOrientation.HORIZONTAL, false,scale);
        rowView = new Rule(RuleOrientation.VERTICAL, false,scale);

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
    }
    
    public void addScrollListener(ScrollableImage other){
        ScrollAdjustmentListener scrollListener = new ScrollAdjustmentListener(picture,other);
        
        pictureScrollPane.getHorizontalScrollBar().addAdjustmentListener(scrollListener);
        pictureScrollPane.getVerticalScrollBar().addAdjustmentListener(scrollListener);
    }
    
    public void addZoomListener(){
        ZoomMouseListener zoomListener = new ZoomMouseListener(this);
        picture.addMouseListener(zoomListener);
        picture.addMouseWheelListener(zoomListener);
    }
    
    public ScrollableImage getPicture(){
        return picture;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
        rescale();
    }
    
    public void changeScale(double val){
        this.scale += val;
        rescale();
    }
    
    public void rescale(){
        this.removeAll();
        ImageIcon image = new ImageIcon(ImageUtils.scale(img, scale));
        initComponent(image);
        this.validate();
    }
    
}
