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

// File: SampleNode2.java
// Classes: SampleNode2
// Original Author: ics125b spring 1996
// $Id$

package uci.gef.demo;

import java.awt.*;
import java.io.*;
import java.util.*;
import uci.gef.*;

/** An example subclass of NetNode for use in the Example application.
 *
 * @see Example */

public class SampleNode2 extends SampleNode {

   /** Initialize a new SampleNode2 from the given default node and
    *  application specific model. <p>
    *
    *  Needs-More-Work: for now we construct the FigNode
    *  programatically, but eventually we will store it in a class
    *  variable and just refer to it, or copy it(?). That way the user
    *  can edit the FigNode(s) stored in the class variable and
    *  have those changes shown for all existing nodes, or for all
    *  future nodes. Maybe I should think about doing virtual
    *  copies?<p> */

  public void initialize(NetNode deft, Object model) {
    portList = new NetPort[4];
    portList[0] = new SamplePort(this);
    portList[1] = new SamplePort(this);
    portList[2] = new SamplePort2(this);
    portList[3] = new SamplePort2(this);
   }

  public FigNode makePresentation(Layer lay) {
    Fig obj1 = new FigRect(-25, -25, 50, 50, Color.black, Color.white);
    Fig obj2 = new FigRect(-13, -13, 26, 26, null, Color.pink);
    Fig obj3 = new FigCircle( -5, -24, 10, 10, Color.blue, Color.white);
    Fig obj4 = new FigCircle( -5,  14, 10, 10, Color.blue, Color.white);
    Fig obj5 = new FigRect(-24,  -5, 10, 10, Color.blue, Color.white);
    Fig obj6 = new FigRect( 14,  -5, 10, 10, Color.blue, Color.white);
    Vector temp_list = new Vector();
    temp_list.addElement(obj1);
    temp_list.addElement(obj2);
    temp_list.addElement(obj3);
    temp_list.addElement(obj4);
    temp_list.addElement(obj5);
    temp_list.addElement(obj6);
    FigNode fn = new FigNode(this, temp_list);
    fn.setBlinkPorts(true);
    fn.addPort(portList[0], obj3);
    fn.addPort(portList[1], obj4);
    fn.addPort(portList[2], obj5);
    fn.addPort(portList[3], obj6);
    return fn;
  }

} /* end class SampleNode2 */
