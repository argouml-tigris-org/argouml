package uci.gef;

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/** Draws a triangluar arrow head at the end of a FigEdge */

public class ArrowHeadHalfTriangle extends ArrowHead {

  public void paint(Graphics g, Point start, Point end) {
    int    xFrom, xTo, yFrom, yTo;
    double denom, x, y, dx, dy, cos, sin;
    Polygon triangle;

    xFrom  = start.x;
    xTo   = end.x;
    yFrom  = start.y;
    yTo   = end.y;

    dx   	= (double)(xTo - xFrom);
    dy   	= (double)(yTo - yFrom);
    denom 	= dist(dx, dy);
    if (denom == 0) return;

    cos = arrow_height/denom;
    sin = arrow_width /denom;
    x   = xTo - cos*dx;
    y   = yTo - cos*dy;
    int x1  = (int)(x - sin*dy);
    int y1  = (int)(y + sin*dx);
    int x2  = (int)(x + sin*dy);
    int y2  = (int)(y - sin*dx);

    triangle = new Polygon();
    triangle.addPoint(xTo, yTo);
    triangle.addPoint(xFrom, yFrom);
    triangle.addPoint(x2, y2);

    g.setColor(arrowFillColor);
    g.fillPolygon(triangle);
    g.setColor(arrowLineColor);
    g.drawPolygon(triangle);
  }

  private double dist(int x0, int y0, int x1, int y1) {
    double dx, dy;
    dx = (double)(x0-x1);
    dy = (double)(y0-y1);
    return Math.sqrt(dx*dx+dy*dy);
  }

  private double dist(double dx, double dy) {
    return Math.sqrt(dx*dx+dy*dy);
  }
}
