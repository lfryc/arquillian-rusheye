/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.mask;

import java.io.File;
import org.jboss.rusheye.manager.project.testcase.MaskCase;

/**
 *
 * @author hcube
 */
public abstract class MaskConverter {

    protected MaskCase mask;

    public MaskConverter(MaskCase m) {
        mask = m;
    }

    public abstract void save(File file);
}
