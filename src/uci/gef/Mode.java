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



// File: Mode.java
// Classes: Mode
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.Serializable;
import uci.util.*;

/** This is the abstract superclass of all editor modes.  A Mode is
 *  responsible for handling most of the events that come to the
 *  Editor.  A Mode defines a context for interperting those events.
 *  For example, a mouse drag in ModeSelect will define a selection
 *  rectangle, while a mouse drag in ModeCreateEdge will drag out a
 *  rubberband line.  Placing the logic for most event handing in
 *  Modes is key to keeping the Editor source code small and
 *  manageable, and also key to allowing addition of new kinds of user
 *  interactions without always modifying Editor and having to
 *  integrate ones modifications with other contributors
 *  modifications.  Modes should interpert user input and ask the
 *  Editor to execute Cmds.  Placing the logic to manipulate the
 *  document into Cmds helps keep Mode's small and promotes sharing of
 *  Cmd code.
 *
 * @see Editor
 * @see Cmd
 * @see ModeSelect
 * @see ModeCreateEdge */

public abstract class Mode
implements Serializable, KeyListener, MouseListener, MouseMotionListener {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The Editor that is in this mode. Each Mode instance belongs to
   *  exactly one Editor instance.  */
  public Editor _editor;

  /** Arguments to this mode. These are usually set just after the
   *  mode is created, and the are used later.
   *
   * @see ModeCreateEdge
   */
  protected Hashtable _args;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new Mode instance with the given Editor as its
   * _editor */
  public Mode(Editor par) { setEditor(par); }

  /** Construct a new Mode instance without any Editor as its parent,
   * the parent must be filled in before the instance is actually
   * used. This constructor is needed because CmdSetMode can only
   * call Class.newInstance which does not pass constructor arguments.
   *
   * @see CmdSetMode */
  public Mode() { }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Set the parent Editor of this Mode */
  public void setEditor(Editor w) {
    _editor = w;
    setCursor(getInitialCursor());
  }

  /** Get the parent Editor of this Mode */
  public Editor getEditor() { return _editor; }

  /** Returns the cursor that should be shown when this Mode starts. */
  public Cursor getInitialCursor() {
    return Cursor.getDefaultCursor();
  }

  
  ////////////////////////////////////////////////////////////////
  // Arguments

  public void setArgs(Hashtable args) { _args = args; }
  public void setArg(String key, Object value) {
    if (_args == null) _args = new Hashtable();
    _args.put(key, value);
  }
  public Hashtable getArgs() { return _args; }
  public Object getArg(String s) {
    if (_args == null) return null;
    return _args.get(s);
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  public void keyPressed(KeyEvent ke) { }
  public void keyReleased(KeyEvent ke) { }
  public void keyTyped(KeyEvent ke) {  }

  public void mouseMoved(MouseEvent me) { }
  public void mouseDragged(MouseEvent me) { }

  public void mouseClicked(MouseEvent me) { }
  public void mousePressed(MouseEvent me) { }
  public void mouseReleased(MouseEvent me) { }
  public void mouseExited(MouseEvent me) { }
  public void mouseEntered(MouseEvent me) { }


  ////////////////////////////////////////////////////////////////
  // methods related to transitions among modes

  /** When a Mode handles a certain event that indicates that the user
   *  wants to exit that Mode (e.g., a mouse up event after a drag in
   *  ModeCreateEdge) the Mode calls done to set the parent Editor's
   *  Mode to some other Mode (normally ModeSelect). */
  public void done() {
    setCursor(Cursor.getDefaultCursor());
    _editor.finishMode();
  }

  /** When the user performs the first AWT Event that indicate that
   *  they want to do some work in this mode, then change the global
   *  next mode. For example, this is useful when the user clicks on
   *  a palette button which sets the global next mode. That Mode
   *  should only be given to one Editor, after that it should go
   *  back to ModeSelect, unless the sticky flag is set. */
  public void start() { Globals.nextMode(); }

  /** Some Mode's should never be exited, but by default any Mode can
   *  exit. Mode's which return false for canExit() will not be popped
   *  from a ModeManager.
   *
   * @see ModeManager */
  public boolean canExit() { return true; }

  ////////////////////////////////////////////////////////////////
  // feedback to the user

  /** Reply a string of instructions that should be shown in the
   *  statusbar when this mode starts. */
  public String instructions() { return "Mode: " + getClass().getName(); }

  /** Set the mouse cursor to some appropriate for this mode. */
  public void setCursor(Cursor c) {
    if (_editor != null) _editor.setCursor(c);
  }
  
  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Modes can paint themselves to give the user feedback. For
   *  example, ModePlace paints the object being placed. Mode's are
   *  drawn on top of (after) the Editor's current view and on top of
   *  any selections. */
  public void paint(Graphics g) { }

  /** Just calls paint(g) bt default. */
  public void print(Graphics g) { paint(g); }

  static final long serialVersionUID = 7960954871341784898L;
} /* end class Mode */

