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

// File: Mode.java
// Classes: Mode
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;
import java.io.Serializable;
import uci.util.*;

/** This is the abstract superclass of all editor mode's.  A Mode is
 *  responsible for handling most of the events that come to the
 *  Editor.  A Mode defines a context for interperting those events.
 *  For example, a mouse drag in ModeSelect will define a selection
 *  rectangle, while a mouse drag in ModeCreateArc will drag out a
 *  rubberband arc.  Placing the logic for most event handing in
 *  Mode's is key to keeping the Editor source code small and
 *  manageable, and also key to allowing addition of new kinds of user
 *  interactions without always modifying Editor and having to
 *  integrate ones modifications with other contributors
 *  modifications.  Mode's should interpert user input and ask the
 *  Editor to execute Action's.  Placing the logic to manipulate the
 *  document into Action's helps keep Mode's small and promotes
 *  sharing of Action code.<p>
 *  <A HREF="../features.html#editing_modes">
 *  <TT>FEATURE: editing_modes</TT></A>
 *
 * @see Editor
 * @see Action
 * @see ModeSelect
 * @see ModeCreateArc */

public abstract class Mode extends EventHandler implements Serializable  {
  // implements GEF {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The Editor that is in this mode. Each Mode instance belongs to
   * exactly one Editor instance.  */
  public Editor _editor;

  public Hashtable _keybindings = null;

  protected Properties _args;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new Mode instance with the given Editor as its
   * _editor */
  public Mode(Editor par) { setEditor(par); bindKeys(); dumpKeys(); }

  /** Construct a new Mode instance without any Editor as its parent,
   * the parent must be filled in before the instance is actually
   * used. This constructor is needed because ActionSetMode can only
   * call Class.newInstance which does not pass constructor arguments.
   *
   * @see ActionSetMode
   * @see Class#newInstance */
  public Mode() { bindKeys(); }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Set the parent Editor of this Mode */
  public void setEditor(Editor w) { _editor = w; }

  /** Get the parent Editor of this Mode */
  public Editor getEditor() { return _editor; }

  ////////////////////////////////////////////////////////////////
  // key bindings

  public void args(Properties args) { _args = args; }
  public Properties args() { return _args; }

  /** Set all keybindings */
  public void keybindings(Hashtable kb) { _keybindings = kb; }

  /** get the keybindings */
  public Hashtable keybindings() { return _keybindings; }

  /** define a new keybinding (keystroke-action pair). Returns old
   * Action if there was one, otherwise null. */
  public Action bindKey(int key, Action act) { return bindKey(key, 0, act); }

  public Action bindKey(char key, Action act) {
    char modifiedKey = key;
    int mask = 0;
    if (Character.isUpperCase(key)) {
        modifiedKey = Character.toLowerCase(key);
        mask = Event.SHIFT_MASK;
    }
    return bindKey((int)key, mask, act);
  }

  public Action bindCtrlKey(int key, Action act) {
    return bindKey(key, Event.CTRL_MASK, act);
  }

  public Action bindCtrlKey(char key, Action act) {
    return bindKey((int)key, Event.CTRL_MASK, act);
  }

  public Action bindKey(int key, int mask, Action act) {
    Integer hashKey = new Integer(mask * 65335 + key);
    Action oldAct = keybinding(key, mask);
    if (_keybindings == null) _keybindings = new Hashtable();
    _keybindings.put(hashKey, act);
    return oldAct;
  }

  /** Remove a keybinding (keystroke-action pair). Returns old
   *  Action if there was one, otherwise null. */
  public Action unbindKey(int key) { return unbindKey(key, 0); }

  public Action unbindKey(int key, int mask) {
    if (_keybindings == null) return null;
    Integer hashKey = new Integer(mask * 65335 + key);
    Action oldAct = keybinding(key, mask);
    _keybindings.remove(hashKey);
    return oldAct;
  }

  /** Subclasses should override bindKeys to define their key
   *  bindings. Needs-More-Work: there are some examples of _future_
   *  keybindings here that should be implemented. */
  public void bindKeys() {
    // bind(escape, new ActionExitMode()); // Needs-More-Work: not implemented
    // bind(Event.F1, new ActionHelp()); // Needs-More-Work: not implemented
  }

  /** Lookup the Action bound to the given key code, or null of none. */
  public Action keybinding(int key) { return keybinding(key, 0); }

  public Action keybinding(int key, int mask) {
    if (_keybindings == null) return null;
    Integer hashKey = new Integer(mask * 65335 + key);
    return (Action)_keybindings.get(hashKey);
  }

  public void dumpKeys() { } //System.out.println(_keybindings.toString()); }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** Check if the key that was pressed is bound to an Action, and
   *  if so, execute that action. */
  public boolean keyDown(Event e, int key) {
    Action act = keybinding(key, e.modifiers);
    if (act != null) {
      _editor.executeAction(act, e);
      return true;
    }
    return super.keyDown(e, key);
  }

  ////////////////////////////////////////////////////////////////
  // methods related to transitions among modes

  /** When a Mode handles a certain event that indicates that the user
   * wants to exit that Mode (e.g., a mouse up event after a drag in
   * ModeCreateArc) the Mode calls done to set the parent Editor's
   * Mode to some other Mode (normally ModeSelect). */
  public void done() { _editor.finishMode(); }

  /** When the user performs the first AWT Event that indicate that
   *  they want to do some work in this mode, then change the global
   *  next mode. For example, this is useful when the user clicks on
   *  a palette button which sets the global next mode. That Mode
   *  should only be given to one Editor, after that it should go
   *  back to ModeSelect, unless the sticky checkbox is set. */
  public void start() { Globals.nextMode(); }

  public String instructions() { return "Mode: " + getClass().getName(); }

  /** Some Mode's should never be exited, but by default any Mode can
   *  exit. Mode's which return false for canExit() will not be popped
   *  from a ModeManager.
   *
   * @see ModeManager */
  public boolean canExit() { return true; }

  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Modes can paint themselves to give the user feedback. For
   *  example, ModePlace paints the object being placed. Mode's are
   *  drawn on top of (after) the Editor's current view and on top of
   *  any selections. */
  public void paint(Graphics g) { }

  public void print(Graphics g) { paint(g); }

} /* end class Mode */

