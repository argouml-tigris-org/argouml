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

// File: PathConvPercent.java
// Classes: PathConvPercent
// Original Author: abonner@ics.uci.edu
// $Id$

package uci.gef;

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;

class PathConvPercent extends PathConv
{
	float percent = 0;
	int offset = 0;

	public PathConvPercent(Fig theFig, float newPercent, int newOffset)
	{
		super(theFig);
		setPercentOffset(newPercent, newOffset);
	}

	public Point getPoint()
	{
		int figLength = pathFigure.getPerimeterLength();
    int pointToGet = (int) (figLength * percent);

    Point linePoint = pathFigure.pointAlongPerimeter(pointToGet);

    System.out.println("lP=" + linePoint + " ptG=" + pointToGet + " figLen=" + figLength);

    Point offsetAmount = getOffsetAmount(pathFigure.pointAlongPerimeter(pointToGet + 5), pathFigure.pointAlongPerimeter(pointToGet - 5), offset);

    return new Point(linePoint.x + offsetAmount.x, linePoint.y + offsetAmount.y);
	}

	public void setPercentOffset(float newPercent, int newOffset)
	{
		percent = newPercent;
		offset = newOffset;
	}

	public void setClosestPoint(Point newPoint)
	{
	}
}
