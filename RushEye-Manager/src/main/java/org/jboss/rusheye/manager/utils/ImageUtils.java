/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author hcube
 */
public class ImageUtils {

    public static BufferedImage scale(BufferedImage img, double scale) {
        int newWidth = (int) (img.getWidth() * scale);
        int newHeight = (int) (img.getHeight() * scale);

        BufferedImage result = new BufferedImage(newWidth, newHeight, img.getType());
        Graphics2D g = result.createGraphics();
        g.drawImage(img, 0, 0, newWidth, newHeight, null);
        g.dispose();

        return result;
    }

    public static int getR(int color) {
        return (color >> 16) & 0x000000ff;
    }

    public static int getG(int color) {
        return (color >> 8) & 0x000000ff;
    }

    public static int getB(int color) {
        return (color) & 0x000000ff;
    }

    public static int toRgb(int r, int g, int b) {
        return r << 16 | g << 8 | b;
    }
    
}
