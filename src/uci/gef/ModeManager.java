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
import uci.gef.event.*;
import javax.swing.event.EventListenerList;

/** ModeManager keeps track of all the Modes for a given Editor.
 *  Events are passed to the Modes for handling.  The submodes are
 *  prioritized according to their order on a stack, i.e., the last
 *  Mode added gets the first chance to handle an Event.  */

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

  /** The Editor that owns this ModeManager. */
  public Editor _editor;

  /** Set the parent Editor of this ModeManager */
  public void setEditor(Editor w) { _editor = w; }

  protected EventListenerList _listeners = new EventListenerList();

  /** Get the parent Editor of this ModeManager */
  public Editor getEditor() { return _editor; }

  ////////////////////////////////////////////////////////////////
  // constructors

  /**  Construct a ModeManager with no modes. */
  public ModeManager(Editor ed) { _editor = ed; }

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

  /** Add the given Mode to the stack iff another instance
   *  of the same class is not already on the stack. */
  public void push(Mode newMode) {
    if (!includes(newMode.getClass())) {
      _modes.addElement(newMode);
      //fireModeChanged();
    }
  }

  /** Remove the topmost Mode iff it can exit. */
  public Mode pop() {
    if (_modes.isEmpty()) return null;
    Mode res = top();
    if (res.canExit()) {
      _modes.removeElement(res);
      fireModeChanged();
    }
    return res;
  }

  /** Remove all Modes that can exit. */
  public void popAll() {
    while (!_modes.isEmpty() && top().canExit())
      _modes.removeElement(top());
  }

  public boolean includes(Class modeClass) {
    Enumeration subs = _modes.elements();
    while (subs.hasMoreElements()) {
      Mode m = (Mode) subs.nextElement();
      if (m.getClass() == modeClass) return true;
    }
    return false;
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** Pass events to all modes in order, until one consumes it. */
  public void keyTyped(KeyEvent ke) {
    checkModeTransitions(ke);
    for (int i = _modes.size() - 1; i >= 0 && !ke.isConsumed(); --i) {
      Mode m = ((Mode)_modes.elementAt(i));
      m.keyTyped(ke);
    }
  }

  /** Do nothing, this event are not passed to the Modes. */
  public void keyReleased(KeyEvent ke) { }

  /** Pass events to all modes in order, until one consumes it. */
  public void keyPressed(KeyEvent ke) {
    for (int i = _modes.size() - 1; i >= 0 && !ke.isConsumed(); --i) {
      Mode m = ((Mode)_modes.elementAt(i));
      m.keyPressed(ke);
    }
  }

  /** Pass events to all modes in order, until one consumes it. */
  public void mouseMoved(MouseEvent me) {
    for (int i = _modes.size() - 1; i >= 0; --i) { // && !me.isConsumed()
      Mode m = ((Mode)_modes.elementAt(i));
      m.mouseMoved(me);
    }
  }

  /** Pass events to all modes in order, until one consumes it. */
  public void mouseDragged(MouseEvent me) {
    for (int i = _modes.size() - 1; i >= 0; --i) { // && !me.isConsumed()
      Mode m = ((Mode)_modes.elementAt(i));
      m.mouseDragged(me);
    }
  }

  /** Pass events to all modes in order, until one consumes it. */
  public void mouseClicked(MouseEvent me) {
    checkModeTransitions(me);
    for (int i = _modes.size() - 1; i >= 0 && !me.isConsumed(); --i) {
      Mode m = ((Mode)_modes.elementAt(i));
      m.mouseClicked(me);
    }
  }

  /** Pass events to all modes in order, until one consumes it. */
  public void mousePressed(MouseEvent me) {
    checkModeTransitions(me);
    for (int i = _modes.size() - 1; i >= 0; --i) { // && !me.isConsumed()
      Mode m = ((Mode)_modes.elementAt(i));
      m.mousePressed(me);
    }
  }

  /** Pass events to all modes in order, until one consumes it. */
  public void mouseReleased(MouseEvent me) {
    checkModeTransitions(me);
    for (int i = _modes.size() - 1; i >= 0; --i) { // && !me.isConsumed()
      Mode m = ((Mode)_modes.elementAt(i));
      m.mouseReleased(me);
    }
    //fireModeChanged();
  }

  /** Pass events to all modes in order, until one consumes it. */
  public void mouseEntered(MouseEvent me) {
    for (int i = _modes.size() - 1; i >= 0 && !me.isConsumed(); --i) {
      Mode m = ((Mode)_modes.elementAt(i));
      m.mouseEntered(me);
    }
  }

  /** Pass events to all modes in order, until one consumes it. */
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
    if (!top().canExit() && ie.getID() == MouseEvent.MOUSE_PRESSED) {
      MouseEvent me = (MouseEvent) ie;
      int x = me.getX(), y = me.getY();
      Fig underMouse = _editor.hit(x, y);
      if (underMouse instanceof FigNode) {
	Object startPort = ((FigNode) underMouse).hitPort(x, y);
	if (startPort != null) {
	  //user clicked on a port, now drag an edge
	  Mode createArc = new ModeCreateEdge(_editor);
	  push(createArc);
	  createArc.mousePressed(me);
	}
      }
    }
  }

  ////////////////////////////////////////////////////////////////
  // mode events

  public void addModeChangeListener(ModeChangeListener listener) {
    _listeners.add(ModeChangeListener.class, listener);
  }

  public void removeModeChangeListener(ModeChangeListener listener) {
    _listeners.remove(ModeChangeListener.class, listener);
  }

  protected void fireModeChanged() {
    Object[] listeners = _listeners.getListenerList();
    ModeChangeEvent e = null;
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == ModeChangeListener.class) {
       	if (e == null) e = new ModeChangeEvent(_editor, getModes());
       	//needs-more-work: should copy vector, use JGraph as src?
       	((ModeChangeListener)listeners[i+1]).modeChange(e);
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
