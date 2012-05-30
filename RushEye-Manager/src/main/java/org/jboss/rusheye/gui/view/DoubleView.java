/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.gui.view;

import java.awt.image.BufferedImage;
import org.jboss.rusheye.gui.view.image.ImageView;

public class DoubleView extends javax.swing.JPanel {

    private ImageView imageView1;
    private ImageView imageView2;

    public DoubleView(String path1, String path2) {
        imageView1 = new ImageView(path1);
        imageView2 = new ImageView(path2);

        initComponents();
    }

    public DoubleView(BufferedImage img1, BufferedImage img2) {
        imageView1 = new ImageView(img1);
        imageView2 = new ImageView(img2);

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
