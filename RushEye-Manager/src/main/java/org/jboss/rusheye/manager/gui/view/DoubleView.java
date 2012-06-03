/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view;

import java.awt.image.BufferedImage;
import org.jboss.rusheye.manager.gui.view.image.ImageView;

public class DoubleView extends javax.swing.JPanel {

    private ImageView imageView1;
    private ImageView imageView2;

    public DoubleView(String path1, String path2) {
        imageView1 = new ImageView(path1);
        imageView2 = new ImageView(path2);
        
        imageView1.setAllowScale(false);
        imageView2.setAllowScale(false);

        initComponents();
    }

    public void initComponents() {
        imageView1.addScrollListener(imageView2.getPicture());
        imageView2.addScrollListener(imageView1.getPicture());
        
        setLayout(new java.awt.GridLayout(1, 0));
        add(imageView1);
        add(imageView2);
    }
}
