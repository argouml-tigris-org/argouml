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

// File: NodeWall.java
// Classes: NodeWall
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
public class NodeWall extends SampleNode {

  ////////////////////////////////////////////////////////////////
  // instance variables

  PortPower powerPort1, powerPort2;
  
   /** Initialize a new NodeWall from the given default node and
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
    addPort(powerPort1 = new PortPower(this, PortPower.SOCKET));
    addPort(powerPort2 = new PortPower(this, PortPower.SOCKET));
   }

  public FigNode makePresentation(Layer lay) {
    Fig obj1 = new FigRect(0, 0, 200, 10, Color.black, Color.white);
    Fig obj2 = new FigRect( 3, 3, 14, 14, Color.black, Color.blue);
    Fig obj3 = new FigRect( 25, 3, 14, 14, Color.black, Color.blue);
    Vector temp_list = new Vector();
    temp_list.addElement(obj1);
    temp_list.addElement(obj2);
    temp_list.addElement(obj3);
    FigNode fn = new FigNode(this, temp_list);
    fn.bindPort(powerPort1, obj2);
    fn.bindPort(powerPort2, obj3);
    return fn;
  }

} /* end class NodeWall */
