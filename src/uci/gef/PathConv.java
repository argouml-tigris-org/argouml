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

// File: PathConv.java
// Classes: PathConv
// Original Author: abonner@ics.uci.edu
// $Id$

package uci.gef;

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public abstract class PathConv
{
	Fig pathFigure; 	// The intermediate path figure
	
	public PathConv(Fig theFig)
	{
		pathFigure = theFig;
	}

	abstract public Point getPoint();
	abstract public void setClosestPoint(Point newPoint);

  protected Point getOffsetAmount(Point p1, Point p2, int offset)
  {
    // slope of the line we're finding the normal to
    // is slope, and the normal is the negative reciprocal
    // slope is (p1.y - p2.y) / (p1.x - p2.x)
    // so recip is - (p1.x - p2.x) / (p1.y - p2.y)

    int recipnumerator = (p1.x - p2.x) * -1;
    int recipdenominator = (p1.y - p2.y);

    // find the point offset on the line that gives a 
    // correct offset

    double a = offset / Math.sqrt(recipnumerator * recipnumerator+ recipdenominator * recipdenominator);
    Point newPoint = new Point((int) (recipdenominator * a), (int) (recipnumerator * a));

    System.out.println("p1=" + p1 + " p2=" + p2 + " off=" + offset);
    System.out.println("a=" + a + " rn=" + recipnumerator + " rd=" + recipdenominator + " nP=" + newPoint);

    return newPoint;
  }
}
