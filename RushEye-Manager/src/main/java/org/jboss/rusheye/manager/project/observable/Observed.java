/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project.observable;

/**
 *
 * @author hcube
 */
public interface Observed {

    public void addObserver(Observer o);

    public void removeObserver(Observer o);
}
