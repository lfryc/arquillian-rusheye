/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view;

import javax.swing.JPanel;
import org.jboss.rusheye.manager.gui.view.image.ImagePool;
import org.jboss.rusheye.manager.gui.view.image.ImageView;
import org.jboss.rusheye.manager.project.TestCase;

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
