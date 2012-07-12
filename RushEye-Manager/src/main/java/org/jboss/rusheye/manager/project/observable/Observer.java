/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project.observable;

/**
 * Observer design pattern interface. All frames where we show project data like
 * pattern/samples path in text fields, observe mainProject instance and update
 * changes.
 *
 * @author Jakub D.
 */
public interface Observer {

    public void update(Observed o);
}
