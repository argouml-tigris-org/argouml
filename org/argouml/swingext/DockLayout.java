/*
 * DockLayout.java
 *
 * Created on 23 February 2003, 17:14
 */

/**
 * Layout Manager to control positions of docked toolbars
 * @author Christopher Bach
 */

package org.argouml.swingext;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.SwingConstants;

public class DockLayout extends BorderLayout{
    private ArrayList north = new ArrayList(1);
    private ArrayList south = new ArrayList(1);
    private ArrayList east = new ArrayList(1);
    private ArrayList west = new ArrayList(1);
    private Component center = null;

    public static final int VERTICAL = SwingConstants.VERTICAL;
    public static final int HORIZONTAL = SwingConstants.HORIZONTAL;
    
    public DockLayout() {
    }
    
    public void addLayoutComponent(Component c, Object con)   {
        synchronized (c.getTreeLock()) {
            if (con != null)         {
                String s = con.toString();
                if (s.equals(NORTH)) north.add(c);
                else if (s.equals(SOUTH)) south.add(c);
                else if (s.equals(EAST)) east.add(c);
                else if (s.equals(WEST)) west.add(c);
                else if (s.equals(CENTER)) center = c;
            }      
        }
    }
    
    public void removeLayoutComponent(Component c)   {
        north.remove(c);
        south.remove(c);
        east.remove(c); 
        west.remove(c);
        if (c == center) {
            center = null;
        }
    }   

    public void layoutContainer(Container target) {
        synchronized (target.getTreeLock()) {
            Insets insets = target.getInsets();
            int top = insets.top;
            int bottom = target.getHeight() - insets.bottom;
            int left = insets.left;
            int right = target.getWidth() - insets.right;
            int northHeight = getPreferredDimension(north).height;
            int southHeight = getPreferredDimension(south).height;
            int eastWidth = getPreferredDimension(east).width;
            int westWidth = getPreferredDimension(west).width;
            placeComponents(north, left, top, right - left, northHeight, HORIZONTAL);
            top += (northHeight + getVgap());
            placeComponents(south, left, bottom - southHeight, right - left, southHeight, HORIZONTAL);
            bottom -= (southHeight + getVgap());
            placeComponents(east, right - eastWidth, top, eastWidth, bottom - top, VERTICAL);
            right -= (eastWidth + getHgap());
            placeComponents(west, left, top, westWidth, bottom - top, VERTICAL);
            left += (westWidth + getHgap());
            if (center != null) {
                center.setBounds(left, top, right - left, bottom - top);
            }
        }
    }
    // Returns the ideal width for a vertically oriented toolbar   
    // and the ideal height for a horizontally oriented tollbar:   
    private Dimension getPreferredDimension(ArrayList comps) {      
        int w = 0, h = 0;      
        for (int i=0; i < comps.size(); i++) {
            Component c = (Component)(comps.get(i));         
            Dimension d = c.getPreferredSize();         
            w = Math.max(w, d.width);         
            h = Math.max(h, d.height);      
        }      
        return new Dimension(w, h);   
    }   

    private void placeComponents(ArrayList comps, int x, int y, int w, int h, int orientation) {
        int offset = 0;      
        Component c = null;      
        if (orientation == HORIZONTAL) {         
            offset = x;         
            for (int i=0; i < comps.size(); i++) {            
                c = (Component)(comps.get(i));            
                int cwidth = c.getPreferredSize().width;            
                if (i == comps.size() - 1) cwidth = w - offset;            
                c.setBounds(x + offset, y, cwidth, h);            
                offset += cwidth;         
            }      
        } else {
            for (int i=0; i < comps.size(); i++) {
                c = (Component)(comps.get(i));        
                int cheight = c.getPreferredSize().height;            
                if (i == comps.size() - 1) cheight = h - offset;            
                c.setBounds(x, y + offset, w, cheight);            
                offset += cheight;
            }      
        }   
    }
}
