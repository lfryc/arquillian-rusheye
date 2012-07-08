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
    T1("Here is some useless info you will never read anyway..."),
    T2("There are 3 types of projects.\nWhen you run Manager you open empty project[something more]");
    
    private String text;
    
    private Tips(String s){
        text  = s;
    }
    
    private String getText(){
        return text;
    }
    
    public static String getRandom(){
        return Tips.values()[new Random().nextInt(Tips.values().length)].getText();
    }
    
}
