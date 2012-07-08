/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager;

import java.util.Random;

/**
 *
 * @author hcube
 */
public enum Tips {
    T1("");
    
    private String text;
    
    private Tips(String s){
        text  = s;
    }
    
    private String getText(){
        return text;
    }
    
    public String getRandom(){
        return Tips.values()[new Random().nextInt(Tips.values().length)].getText();
    }
    
}
