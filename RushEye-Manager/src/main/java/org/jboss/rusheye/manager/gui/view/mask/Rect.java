/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.mask;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author cube
 */
public class Rect implements Shape {

    private int x;
    private int y;
    private int width;
    private int height;

    public Rect(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        width = w;
        height = h;
    }

    public Rect(Point start, Point stop) {
        this.x = start.x;
        this.y = start.y;
        width = stop.x - start.x;
        height = stop.y - start.y;
    }

    @Override
    public void draw(Graphics g, double scale) {
        g.setColor(new Color(1,0,0,0.5f));
        g.fillRect((int)(x*scale), (int)(y*scale), (int)(width*scale), (int)(height*scale));
    }

    @Override
    public String toString() {
        String result = "";
        result += "X : " + this.x + "\n";
        result += "Y : " + this.y + "\n";
        result += "WIDTH : " + this.width + "\n";
        result += "HEIGHT : " + this.height + "\n";

        return result;
    }
}
