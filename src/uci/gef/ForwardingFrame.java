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

// File: ForwardingFrame.java
// Class: ForwardingFrame
// original author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import uci.util.*;

/** A frame that delgates all of its event handling and repaint method
 *  calls to its EventHandler. Needs-More-Work: this will probably not
 *  be needed under the Java 1.1 event model. */

public class ForwardingFrame extends Frame implements ForwardingComponent {
  ////////////////////////////////////////////////////////////////
  // instance variables

  private EventHandler _eh;

  ////////////////////////////////////////////////////////////////
  // constructors

  public ForwardingFrame(Dimension d) { this(null, d); }

  public ForwardingFrame(EventHandler eh, Dimension d) {
    resize(d);
    _eh = eh;
    setBackground(Color.lightGray);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setEventHandler(EventHandler eh) { _eh = eh; }

  public EventHandler getEventHandler() { return _eh; }

  ////////////////////////////////////////////////////////////////
  // painting methods

  public void paint(Graphics g) { _eh.paint(g); }

  ////////////////////////////////////////////////////////////////
  // event handlers

  public boolean handleEvent(Event e) {
    return _eh.handleEvent(e) || super.handleEvent(e);
  }

  public boolean keyDown(Event e, int key) {
    return _eh.keyDown(e, key);
  }

  //  public boolean keyUp(Event e, int key) {
  //    return _eh.keyUp(e, key);
  //  }
  //
  //  public boolean action(Event e, Object what) {
  //    return _eh.action(e, what);
  //  }

  public boolean mouseExit(Event e, int x, int y) {
    return _eh.mouseExit(e, x, y);
  }

  public boolean mouseEnter(Event e, int x, int y) {
    return _eh.mouseEnter(e, x, y);
  }

  public boolean mouseUp(Event e, int x, int y) {
    return _eh.mouseUp(e, x, y);
  }

  public boolean mouseDown(Event e, int x, int y) {
    return _eh.mouseDown(e, x, y);
  }

  public boolean mouseMove(Event e,int x,int y) {
    return _eh.mouseMove(e, x, y);
  }

  public boolean mouseDrag(Event e,int x,int y) {
    return _eh.mouseDrag(e, x, y);
  }

  public boolean handleMenuEvent(Event e) {
    return _eh.handleMenuEvent(e);
  }

} /* end class ForwardingFrame */
