/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.mask;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jboss.rusheye.manager.gui.view.BottomMenu;
import org.jboss.rusheye.manager.gui.view.image.ImageView;
import org.jboss.rusheye.manager.gui.view.image.Rule;
import org.jboss.rusheye.manager.gui.view.image.listeners.MaskZoomListener;
import org.jboss.rusheye.manager.project.testcase.TestCase;
import org.jboss.rusheye.manager.utils.ImageUtils;

/**
 *
 * @author cube
 */
public class DrawableImageView extends ImageView {

    public DrawableImageView(TestCase test, String key) {
        this.testCase = test;
        img = testCase.getImage(key);
        ImageIcon image = new ImageIcon(img);

        menu = new BottomMenu(this, key);
        initComponent(image);
    }
    
        private void initComponent(ImageIcon image) {
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        columnView = new Rule(Rule.HORIZONTAL, false, scale);
        rowView = new Rule(Rule.VERTICAL, false, scale);

        columnView.setPreferredWidth(image.getIconWidth());
        rowView.setPreferredHeight(image.getIconHeight());

        picture = new MaskedScrollableImage(image, columnView.getIncrement(),scale);

        pictureScrollPane = new JScrollPane(picture);
        pictureScrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.black));

        pictureScrollPane.setColumnHeaderView(columnView);
        pictureScrollPane.setRowHeaderView(rowView);

        pictureScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, new JPanel());
        pictureScrollPane.setCorner(JScrollPane.LOWER_LEFT_CORNER, new JPanel());
        pictureScrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, new JPanel());

        add(pictureScrollPane);

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        setListeners();
        
        add(menu);
    }

    @Override
    public void setListeners() {
        MaskZoomListener drawListener = new MaskZoomListener(this);
        picture.addMouseListener(drawListener);
        picture.addMouseWheelListener(drawListener);
        picture.addMouseMotionListener(drawListener);
    }
    
    @Override
    public void rescale() {
        //TODO bug in drawing mask in scale other than 1
        if (allowScale) {
            this.removeAll();
            ImageIcon image = new ImageIcon(ImageUtils.scale(img, scale));
            initComponent(image);
            this.validate();
        }
    }
}
