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

// File: ModeManager.java
// Classes: ModeManager
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.Serializable;
import uci.util.*;

/** ModeManager is a composite Mode that is made up of several other
 *  Modes.  Events are passed to the submodes for handling.  The
 *  submodes are prioritized according to their order on the stack,
 *  i.e., the last mode added gets the first chance to handle an
 *  Event.  */

public class ModeManager
implements Serializable, MouseListener, MouseMotionListener, KeyListener {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The stack of Mode's that are all active simultainously, the
   *  order of Mode's on the stack is their priority, i.e., the
   *  topmost Mode gets the first chance to handle an incoming
   *  Event. Needs-More-Work: this is a time critical part of the
   *  system and should be faster, use an array instead of a Vector.*/
  private Vector _modes = new Vector();

  public Editor _editor;

  /** Set the parent Editor of this Mode */
  public void setEditor(Editor w) { _editor = w; }

  /** Get the parent Editor of this Mode */
  public Editor getEditor() { return _editor; }

  ////////////////////////////////////////////////////////////////
  // constructors

  /**  Construct an empty ModeManager. */
  public ModeManager(Editor ed) { _editor = ed; }

// //   /**  Construct a ModeManager with the given mode(s). */
// //   public ModeManager(Mode m1) { push(m1); }

// //   /**  Construct a ModeManager with the given mode(s). */
// //   public ModeManager(Mode m1, Mode m2) { push(m1); push(m2); }

  ////////////////////////////////////////////////////////////////
  //  accessors

  /** Reply the stack of Mode's. */
  public Vector getModes() { return _modes; }

  /** Set the entire stack of Mode's. */
  public void setModes(Vector newModes) { _modes = newModes; }

  /** Reply the top (first) Mode. */
  public Mode top() {
    if (_modes.isEmpty()) return null;
    else return (Mode)_modes.lastElement();
  }

  /** Add the given mode to the composite iff another instance
   * of the same class is not already on the stack. */
  public void push(Mode newMode) {
    Class newModeClass = newMode.getClass();
    Enumeration subs = _modes.elements();
    while (subs.hasMoreElements()) {
      Mode m = (Mode) subs.nextElement();
      if (m.getClass() == newModeClass) return;
    }
    _modes.addElement(newMode);
  }

  /** Remove the topmost Mode iff it can exit. */
  public Mode pop() {
    if (_modes.isEmpty()) return null;
    Mode res = top();
    if (res.canExit()) _modes.removeElement(res);
    return res;
  }

  /** Remove all Modes that can exit. */
  public void popAll() {
    while (!_modes.isEmpty() && top().canExit())
      _modes.removeElement(top());
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** Pass the event to each mode in the stack until one handles it. */
  public boolean handleEvent(Event e) {
    System.out.println("old handleEvent called in ModeManager");
    return true;
//     boolean res = super.handleEvent(e);
//     for (int i = _modes.size() - 1; i >= 0 && !res; --i) {
//       Mode m = ((Mode)_modes.elementAt(i));
//       if (Dbg.on) Dbg.log("DebugDispatch2", "passing event to mode #" + i);
//       try { res = m.handleEvent(e); }
//       catch (java.lang.Throwable ex) {
// 	System.out.println("While passing event " + e.toString() +
// 			   " to mode " + m.toString() +
// 			   " the following error occured:");
// 	ex.printStackTrace();
//       }
//     }
//     return res;
  }

  /** Give each Mode a chance to prehandle the event. */
  public void preHandleEvent(Event e) {
//     checkModeTransitions(e);
//     for (int i = 1; i < _modes.size(); ++i) {
//       //DebugLog.log("DebugDispatch2", "prehandeling event with mode #" + i);
//       ((Mode)_modes.elementAt(i)).preHandleEvent(e);
//     }
//     super.preHandleEvent(e);
  }

  /** Give each Mode a chance to posthandle the event. */
  public void postHandleEvent(Event e) {
//     super.postHandleEvent(e);
//     for (int i = _modes.size() - 1; i >= 0; --i) {
//       //DebugLog.log("DebugDispatch2", "posthandeling event with mode #" + i);
//       ((Mode)_modes.elementAt(i)).postHandleEvent(e);
//     }
  }


  public void keyTyped(KeyEvent ke) {
    checkModeTransitions(ke);    
    for (int i = _modes.size() - 1; i >= 0 && !ke.isConsumed(); --i) {
      Mode m = ((Mode)_modes.elementAt(i));
      m.keyTyped(ke);
    }
  }

  public void keyReleased(KeyEvent ke) { }
  public void keyPressed(KeyEvent ke) {
    for (int i = _modes.size() - 1; i >= 0 && !ke.isConsumed(); --i) {
      Mode m = ((Mode)_modes.elementAt(i));
      m.keyPressed(ke);
    }
  }
  
  public void mouseMoved(MouseEvent me) {
    for (int i = _modes.size() - 1; i >= 0 && !me.isConsumed(); --i) {
      Mode m = ((Mode)_modes.elementAt(i));
      m.mouseMoved(me);
    }
  }

  public void mouseDragged(MouseEvent me) {
    for (int i = _modes.size() - 1; i >= 0 && !me.isConsumed(); --i) {
      Mode m = ((Mode)_modes.elementAt(i));
      m.mouseDragged(me);
    }
  }

  public void mouseClicked(MouseEvent me) {
    checkModeTransitions(me);    
    for (int i = _modes.size() - 1; i >= 0 && !me.isConsumed(); --i) {
      Mode m = ((Mode)_modes.elementAt(i));
      m.mouseClicked(me);
    }
  }

  public void mousePressed(MouseEvent me) {
    checkModeTransitions(me);
    for (int i = _modes.size() - 1; i >= 0 && !me.isConsumed(); --i) {
      Mode m = ((Mode)_modes.elementAt(i));
      m.mousePressed(me);
    }
  }

  public void mouseReleased(MouseEvent me) {
    checkModeTransitions(me);    
    for (int i = _modes.size() - 1; i >= 0 && !me.isConsumed(); --i) {
      Mode m = ((Mode)_modes.elementAt(i));
      m.mouseReleased(me);
    }
  }

  public void mouseEntered(MouseEvent me) {
    for (int i = _modes.size() - 1; i >= 0 && !me.isConsumed(); --i) {
      Mode m = ((Mode)_modes.elementAt(i));
      m.mouseEntered(me);
    }
  }

  public void mouseExited(MouseEvent me) {
    for (int i = _modes.size() - 1; i >= 0 && !me.isConsumed(); --i) {
      Mode m = ((Mode)_modes.elementAt(i));
      m.mouseExited(me);
    }
  }

  
  ////////////////////////////////////////////////////////////////
  // mode transitions

  /** Check for events that should cause transitions from one Mode to
   *  another or otherwise change the ModeManager. Really this should be
   *  specified in a subclass of ModeManager, because ModeManager should
   *  not make assumptions about the look-and-feel of all future
   *  applications.  Needs-More-Work: I would like to put the
   *  transition from ModeSelect to ModeModify here, but there are too
   *  many interactions, so that code is still in ModeSelect. */
  public void checkModeTransitions(InputEvent ie) {
    if ((top() instanceof ModeSelect || top() instanceof ModeModify)
	&& ie.getID() == MouseEvent.MOUSE_PRESSED) {
      MouseEvent me = (MouseEvent) ie;
      int x = me.getX(), y = me.getY();
      Fig underMouse = _editor.hit(x, y);
      if (underMouse instanceof FigNode) {
	Object startPort = ((FigNode) underMouse).hitPort(x, y);
	if (startPort != null) {
	  //user clicked on a port, now drag an edge
	  Mode createArc = new ModeCreateArc(_editor);
	  push(createArc);
	  createArc.mousePressed(me);
	}
      }
    }
  }

  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Paint each mode in the stack: bottom to top. */
  public void paint(Graphics g) {
    Enumeration modes = _modes.elements();
    while (modes.hasMoreElements()) {
      Mode m = (Mode) modes.nextElement();
      m.paint(g);
    }
  }

} /* end class ModeManager */
