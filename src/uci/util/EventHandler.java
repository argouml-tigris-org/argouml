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

// File: EventHandler.java
// Classes: EventHandler
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.util;

import java.util.*;
import java.awt.*;

/** Class that basically reimplements some of the event dispatching
 *  logic of java.awt.Component under JDK 1.0.2. I did this to achieve
 *  familiar event handing in objects that did not subclass from
 *  Component. In addtion to what is provided in Component, there are
 *  also hooks for pre- and post- event handling and for transforming
 *  an event before it is handled. Needs-More-Work: this hsould all be
 *  redone when I switch to the JDK 1.1 event model. */

public class EventHandler extends Observable implements IEventHandler {

  /** Prehandle event, transform it, dispatch it to one of many event
   *  handler methods (e.g., mouseDown()), and posthandle the event.
   *  Returns true if the event was successfuly handled, false if the
   *  event should be passed on to some other object that might handle
   *  it. */
  public boolean handleEvent(Event origEvent) {
    boolean res;
    preHandleEvent(origEvent);
    Event e = transformEvent(origEvent);
    res = dispatchEvent(e);
    postHandleEvent(e);
    return res;
  }

  /** Call one of the several event handling methods defined in JDK
   *  1.0.2. For example, mouseDown(). */
  public boolean dispatchEvent(Event e) {
    boolean res;
    switch(e.id) {
    case Event.ACTION_EVENT:
      if (e.target instanceof MenuItem) res = handleMenuEvent(e);
      else res = true;
      break;
    case Event.MOUSE_ENTER: res = mouseEnter(e, e.x, e.y); break;
    case Event.MOUSE_EXIT: res = mouseExit(e, e.x, e.y); break;
    case Event.MOUSE_MOVE: res = mouseMove(e, e.x, e.y); break;
    case Event.MOUSE_DOWN: res = mouseDown(e, e.x, e.y); break;
    case Event.MOUSE_DRAG: res = mouseDrag(e, e.x, e.y); break;
    case Event.MOUSE_UP: res = mouseUp(e, e.x, e.y); break;
    case Event.KEY_PRESS: res = keyDown(e, e.key); break;
    case Event.KEY_ACTION: res = keyDown(e, e.key); break;
    default: res = false;
    }
    return res;
  }

  /** Subclasses may override to do something special before the event
   *  is handled. */
  public void preHandleEvent(Event e) { }


  /** Subclasses may override to do something special after the event
   *  is handled. */
  public void postHandleEvent(Event e) { }

  /** Subclasses may override to change the event before it is handled */
  public Event transformEvent(Event e) { return e; }


  /** Mode's define event handlers. Subclasses of Mode define specific
   *  handlers and invoke Actions. By default all these event handler
   *  methods just return false. By convention if a Mode can not handle
   *  a given event, it should call its inheritied event handler and
   *  return the result that it produces. */
  public boolean mouseEnter(Event e, int x, int y) { return false; }
  public boolean mouseExit(Event e, int x, int y) { return false; }
  public boolean mouseUp(Event e, int x, int y) { return false; }
  public boolean mouseDown(Event e, int x, int y) { return false; }
  public boolean mouseDrag(Event e, int x, int y) { return false; }
  public boolean mouseMove(Event e, int x, int y) { return false; }
  public boolean keyUp(Event e, int key) { return false; }
  public boolean keyDown(Event e, int key) { return false; }
  public boolean handleMenuEvent(Event e) { return false; }

  public void paint(Graphics g) { } // subclasses must override
  // maybe I should use an abstract class...

} /* end class EventHandler */

