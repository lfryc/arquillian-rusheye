/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.mask.converters;

import java.io.File;
import org.jboss.rusheye.manager.gui.view.mask.MaskCase;

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
