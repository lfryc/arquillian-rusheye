/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.exception;

/**
 *
 * @author hcube
 */
public class ManagerException extends Exception {

    public ManagerException() {
        super();
    }

    public ManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagerException(String message) {
        super(message);
    }

    public ManagerException(Throwable cause) {
        super(cause);
    }

}
