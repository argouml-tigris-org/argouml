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

// File: SampleNode.java
// Classes: SampleNode
// Original Author: ics125b spring 1996
// $Id$

package uci.gef.demo;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import uci.gef.*;

/** An example subclass of NetNode for use in the Example application.
 *
 * @see Example */
public class SampleNode extends NetNode {

  SamplePort north, east, west, south;

   /** Initialize a new SampleNode from the given default node and
    *  application specific model. <p>
    *
    *  Needs-More-Work: for now we construct the FigNode
    *  programatically, but eventually we will store it in a class
    *  variable and just refer to it, or copy it(?). That way the user
    *  can edit the FigNode(s) stored in the class variable and
    *  have those changes shown for all existing nodes, or for all
    *  future nodes. Maybe I should think about doing virtual copies?<p>
    */

  public void initialize(Hashtable args) {
    addPort(east = new SamplePort2(this));
    addPort(west = new SamplePort2(this));
    addPort(north = new SamplePort(this));
    addPort(south = new SamplePort(this));
   }

  public FigNode makePresentation(Layer lay) {
    Fig obj1 = new FigRect(-25, -25, 50, 50, Color.black, Color.white);
    Fig obj2 = new FigCircle(-20, -20, 40, 40, Color.red, null);
    Fig obj3 = new FigCircle( -5, -30, 10, 10, Color.black, Color.blue);
    Fig obj4 = new FigCircle( -5,  20, 10, 10, Color.black, Color.blue);
    Fig obj5 = new FigRect(-30,  -5, 10, 10, Color.black, Color.green);
    Fig obj6 = new FigRect( 20,  -5, 10, 10, Color.black, Color.green);
    Vector temp_list = new Vector();
    temp_list.addElement(obj1);
    temp_list.addElement(obj2);
    temp_list.addElement(obj3);
    temp_list.addElement(obj4);
    temp_list.addElement(obj5);
    temp_list.addElement(obj6);
    FigNode fn = new FigNode(this, temp_list);
    fn.bindPort(north, obj3);
    fn.bindPort(south, obj4);
    fn.bindPort(east, obj5);
    fn.bindPort(west, obj6);
    return fn;
  }

  /** Sample event handler: prints a message to the console. */
  public void mouseEntered(MouseEvent e) {
    //    System.out.println("sample node got mouseEnter");
  }

  /** Sample event handler: prints a message to the console. */
  public void mouseExited(MouseEvent e) {
    //    System.out.println("sample node got mouseExit");
  }

  /** Sample event handler: prints a message to the console. */
  public void mouseReleased(MouseEvent e) {
    //    System.out.println("sample node got mouseUp");
  }

  /** Sample event handler: prints a message to the console. */
  public void mousePressed(MouseEvent e) {
    //    System.out.println("sample node got mouseDown");
  }

  /** Sample event handler: prints a message to the console. */
  public void mouseClicked(MouseEvent e) {
    //    System.out.println("sample node got mouseDown");
  }

  /** Sample event handler: prints a message to the console. */
  public void mouseDragged(MouseEvent e) {
    //    System.out.println("sample node got mouseDrag");
  }

  /** Sample event handler: prints a message to the console. */
  public void mouseMoved(MouseEvent e) {
    //    System.out.println("sample node got mouseMove");
  }

  /** Sample event handler: prints a message to the console. */
  public void keyTyped(KeyEvent e) {
    //    System.out.println("sample node got keyUp");
  }

  /** Sample event handler: prints a message to the console. */
  public void keyReleased(KeyEvent e) {
    //    System.out.println("sample node got keyUp");
  }

  /** Sample event handler: prints a message to the console. */
  public void keyPressed(KeyEvent e) {
    //    System.out.println("sample node got keyDown");
  }

} /* end class SampleNode */
