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

// File: NodePrinter.java
// Classes: NodePrinter
// Original Author: ics125b spring 1996
// $Id$

package uci.gef.demo;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import uci.gef.*;

/** An example subclass of NetNode for use in the Example application.
 *
 * @see Example */
public class NodePrinter extends SampleNode {

  ////////////////////////////////////////////////////////////////
  // instance variables

  PortPower powerPort;
  PortData dataPort;
  
   /** Initialize a new NodePrinter from the given default node and
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
    addPort(powerPort = new PortPower(this, PortPower.RECEPTICAL));
    addPort(dataPort = new PortData(this));
  }

  public FigNode makePresentation(Layer lay) {
    URL imageURL = null;
    try { imageURL = new URL("http://www.ics.uci.edu/~jrobbins/images/printer.gif"); }
    catch (java.net.MalformedURLException e) { }
    Fig obj1 = new FigImage(0, 0, imageURL, new Hashtable());
    Image i = Globals.getImage(imageURL);
    Globals.waitForImages();
    Fig obj2 = new FigRect( 2, -7, 14, 14, Color.black, Color.white);
    FigRRect obj3 = new FigRRect( -8, 10, 8, 15, Color.black, Color.black);
    obj3.setCornerRadius(3);


    Vector temp_list = new Vector();
    temp_list.addElement(obj1);
    temp_list.addElement(obj2);
    temp_list.addElement(obj3);
    FigNode fn = new FigNode(this, temp_list);

    fn.bindPort(powerPort, obj2);
    fn.bindPort(dataPort, obj3);

    return fn;
  }

} /* end class NodePrinter */
