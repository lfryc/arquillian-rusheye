/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.mask;

import java.awt.Color;
import java.awt.Graphics;

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

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.drawRect(x, y, width, height);
    }
}
