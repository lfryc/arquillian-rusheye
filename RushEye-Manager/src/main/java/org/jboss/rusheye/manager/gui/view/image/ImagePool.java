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
import javax.imageio.ImageIO;

/**
 * Map of images. Here we store pattern/sample/diff for every TestCse in Project.
 *
 * @author Jakub D.
 */
public class ImagePool {

    public static final String PATTERN = "pattern";
    public static final String SAMPLE = "sample";
    public static final String DIFF = "diff";
    public static final String FAKE = "fake";
    private Map<String, BufferedImage> pool;

    public ImagePool() {
        pool = new HashMap<String, BufferedImage>();
    }

    public void put(String key, BufferedImage value) {
        pool.put(key, value);
    }
    /**
     * 
     * @param key key for map
     * @param path path to image file
     */
    public void put(String key, String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
            put(key, img);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public BufferedImage get(String key) {
        return pool.get(key);
    }
    
    public void remove(String key){
        pool.remove(key);
    }
}
