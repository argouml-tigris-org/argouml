package uci.gef;

import uci.gef.event.*;
import java.util.*;
import java.awt.*;
import java.io.*;

public class FigCube extends Fig implements Serializable {

    public FigCube(int x, int y, int w, int h, Color lColor, Color fColor){
        super(x, y, w, h , lColor, fColor);
    }

    public FigCube( int x, int y, int w, int h) {
      super(x, y, w, h);
    }

    public void paint(Graphics g){
        int x = getX();
        int y = getY();
        int w = getWidth();
        int h = getHeight();
        Color lColor = getLineColor();
        Color fColor = getFillColor();

	g.setColor(fColor);
	g.fillRect(x, y, w, h);
        g.setColor(lColor);
        g.drawRect(x, y, w, h);

 	g.setColor(fColor);
	g.fillPolygon(new int[]{x, x+20, x+w+20, x+w}, new int[]{y, y-20, y-20, y}, 4);
	g.setColor(lColor);
        g.drawPolygon(new int[]{x, x+20, x+w+20, x+w}, new int[]{y, y-20, y-20, y}, 4);

	g.setColor(fColor);
	g.fillPolygon(new int[]{x+w+20, x+w+20, x+w, x+w}, new int[]{y-20, y+h-20, y+h, y}, 4);
	g.setColor(lColor);
        g.drawPolygon(new int[]{x+w+20, x+w+20, x+w, x+w}, new int[]{y-20, y+h-20, y+h, y}, 4);
    }


}

