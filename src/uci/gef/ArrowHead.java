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

public abstract class ArrowHead
{
	protected final int			arrow_width = 5, arrow_height = 16;
	protected Color arrowLineColor = Color.black;
	protected Color arrowFillColor = Color.black;

	Color getLineColor()
	{
		return arrowLineColor;
	}

	void setLineColor(Color newColor)
	{
		arrowLineColor = newColor;
	}

	Color getFillColor()
	{
		return arrowFillColor;
	}

	void setFillColor(Color newColor)
	{
		arrowFillColor = newColor;
	}

	public void paint(Graphics g, Point start, Point end) {
		// put painting here
	}
}
