/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author hcube
 */
public class ImagePool {

    public static final String PATTERN = "pattern";
    public static final String SAMPLE = "sample";
    public static final String DIFF = "diff";
    
    private Map<String, BufferedImage> pool;

    public ImagePool() {
        pool = new HashMap<String, BufferedImage>();
    }

    public void put(String key, BufferedImage value) {
        pool.put(key, value);
    }
    
    public void put(String key,String path){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
            
        } catch (IOException ex) {
            try {
                img = ImageIO.read(new File("empty.png"));
            } catch (IOException ex1) {
                ex.printStackTrace();
            }
            System.out.println(ex.toString());
        }
        put(key,img);
    }

    public BufferedImage get(String key) {
        return pool.get(key);
    }
}
