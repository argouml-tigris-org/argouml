// Copyright (c) 1995, 1996 Regents of the University of California.
// All rights reserved.
//
// This software was developed by the Arcadia project
// at the University of California, Irvine.
//
// Redistribution and use in source and binary forms are permitted
// provided that the above copyright notice and this paragraph are
// duplicated in all such forms and that any documentation,
// advertising materials, and other materials related to such
// distribution and use acknowledge that the software was developed
// by the University of California, Irvine.  The name of the
// University may not be used to endorse or promote products derived
// from this software without specific prior written permission.
// THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: ArrowHead.java
// Classes: ArrowHead
// Original Author: Adam Bonner
// $Id$

package uci.gef;

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class ArrowHeadTriangle extends ArrowHead
{
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
		triangle.addPoint(x1, y1);
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
