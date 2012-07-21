/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view;

import org.jboss.rusheye.manager.gui.view.image.ImagePool;
import org.jboss.rusheye.manager.gui.view.image.ImageView;
import org.jboss.rusheye.manager.project.testcase.TestCase;

/**
 * Double view. It consist of 2 image views.
 * 
 * @author Jakub D.
 */
public class DoubleView extends javax.swing.JPanel {

    private ImageView imageView1;
    private ImageView imageView2;

    public DoubleView(TestCase testCase) {
        imageView1 = new ImageView(testCase, ImagePool.SAMPLE);
        imageView2 = new ImageView(testCase, ImagePool.DIFF);

        initComponents();
    }

    private void initComponents() {
        imageView1.setAllowScale(false);
        imageView2.setAllowScale(false);

        imageView1.initScrollListener(imageView2);
        imageView2.initScrollListener(imageView1);

        //imageView1.addScrollListener();
        //imageView2.addScrollListener();

        imageView1.setListeners();
        imageView2.setListeners();

        
        setLayout(new java.awt.GridLayout(1, 0));
        add(imageView1);
        add(imageView2);
    }
}
