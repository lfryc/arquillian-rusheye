/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.gui.view;

import org.jboss.rusheye.gui.view.image.ImageView;

public class DoubleView extends javax.swing.JPanel {
    
    private ImageView imageView1;
    private ImageView imageView2;

    public DoubleView() {
        imageView1 = new ImageView("empty.png");
        imageView2 = new ImageView("empty.png");
        
        initComponents();
    }
    
    public DoubleView(String path1,String path2){
        imageView1 = new ImageView(path1);
        imageView2 = new ImageView(path2);
        
        initComponents();
    }
    
    public void initComponents(){
        setLayout(new java.awt.GridLayout(1, 0));
        add(imageView1);
        add(imageView2);
    }

}
