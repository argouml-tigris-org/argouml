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

package uci.gef.demo;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import uci.gef.*;

public class FigSampleNode extends FigNode {
  Fig obj1, obj2, obj3, obj4, obj5, obj6;
  FigText obj7;

  public FigSampleNode() {
    super();
    obj1 = new FigRect(-25, -25, 50, 50, Color.black, Color.white);
    obj2 = new FigCircle(-20, -20, 40, 40, Color.red, null);
    obj3 = new FigCircle( -5, -30, 10, 10, Color.black, Color.blue);
    obj4 = new FigCircle( -5,  20, 10, 10, Color.black, Color.blue);
    obj5 = new FigRect(-30,  -5, 10, 10, Color.black, Color.green);
    obj6 = new FigRect( 20,  -5, 10, 10, Color.black, Color.green);
    obj7 = new FigText( -10,  -10, 20, 20);
    obj7.setLineWidth(0);
    obj7.setJustification(FigText.JUSTIFY_CENTER);

    addFig(obj1);
    addFig(obj2);
    addFig(obj3);
    addFig(obj4);
    addFig(obj5);
    addFig(obj6);
    addFig(obj7);

  }

  public void setOwner(Object own) {
    super.setOwner(own);
    if (!(own instanceof SampleNode)) return;
    SampleNode node = (SampleNode) own;
    obj7.setText(""+ node.getNumber());
    bindPort(node.north, obj3);
    bindPort(node.south, obj4);
    bindPort(node.east, obj5);
    bindPort(node.west, obj6);
  }
  
} /* end class FigSampleNode */
