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

// File: ClassNode.java
// Classes: ClassNode
// Original Author: ics125b spring 1996
// $Id$

package uci.uml.visual.demo;

import java.awt.*;
import java.util.*;
import java.io.*;
import uci.gef.*;
import uci.uml.visual.*;

/** An example subclass of NetNode for use in the Example application.
 *
 * @see Example */
public class ClassNode extends NetNode {

  NetPort north;

   /** Initialize a new ClassNode from the given default node and
    *  application specific model. <p>
    *
    *  Needs-More-Work: for now we construct the FigNode
    *  programatically, but eventually we will store it in a class
    *  variable and just refer to it, or copy it(?). That way the user
    *  can edit the FigNode(s) stored in the class variable and
    *  have those changes shown for all existing nodes, or for all
    *  future nodes. Maybe I should think about doing virtual copies?<p>
    */

  public void initialize(NetNode deft, Object model) {
    addPort(north = new NetPort(this));
   }

  public FigNode makePresentation(Layer lay) {
    Fig obj1 = new FigRect(0, 0, 100, 70, Color.black, Color.green);
    Vector temp_list = new Vector();
    temp_list.addElement(obj1);
    FigClass fn = new FigClass(this, temp_list);
    fn.bindPort(north, obj1);
    return fn;
  }

  /** Sample event handler: prints a message to the console. */
  public boolean mouseEnter(Event e, int x, int y) {
    //    System.out.println("sample node got mouseEnter");
    return super.mouseEnter(e, x, y);
  }

  /** Sample event handler: prints a message to the console. */
  public boolean mouseExit(Event e, int x, int y) {
    //    System.out.println("sample node got mouseExit");
    return super.mouseExit(e, x, y);
  }

  /** Sample event handler: prints a message to the console. */
  public boolean mouseUp(Event e, int x, int y) {
    //    System.out.println("sample node got mouseUp");
    return super.mouseUp(e, x, y);
  }

  /** Sample event handler: prints a message to the console. */
  public boolean mouseDown(Event e, int x, int y) {
    //    System.out.println("sample node got mouseDown");
    return super.mouseDown(e, x, y);
  }

  /** Sample event handler: prints a message to the console. */
  public boolean mouseDrag(Event e, int x, int y) {
    //    System.out.println("sample node got mouseDrag");
    return super.mouseDrag(e, x, y);
  }

  /** Sample event handler: prints a message to the console. */
  public boolean mouseMove(Event e, int x, int y) {
    //    System.out.println("sample node got mouseMove");
    return super.mouseMove(e, x, y);
  }

  /** Sample event handler: prints a message to the console. */
  public boolean keyUp(Event e, int key) {
    //    System.out.println("sample node got keyUp");
    return super.keyUp(e, key);
  }

  /** Sample event handler: prints a message to the console. */
  public boolean keyDown(Event e, int key) {
    //    System.out.println("sample node got keyDown");
    return super.keyDown(e, key);
  }

  /** Sample event handler: prints a message to the console. */
  public boolean handleMenuEvent(Event e) {
    //    System.out.println("sample node got handleMenuEvent");
    return super.handleMenuEvent(e);
  }

} /* end class ClassNode */
