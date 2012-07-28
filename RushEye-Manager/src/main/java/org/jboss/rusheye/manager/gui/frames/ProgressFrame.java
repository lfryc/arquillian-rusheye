/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.frames;

import javax.swing.JFrame;
import org.jboss.rusheye.manager.project.observable.Observed;
import org.jboss.rusheye.manager.project.observable.Observer;

/**
 *
 * @author hcube
 */
public class ProgressFrame  extends JFrame implements Observer{

    @Override
    public void update(Observed o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
