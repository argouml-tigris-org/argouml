// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.


// File: FigCube.java
// Classes: FigCube
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$



package uci.gef;

import uci.gef.event.*;
import java.util.*;
import java.awt.*;
import java.io.*;

/**
 * This class is needed to paint cubes (the only 3dim Element in UML)
 **/
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

