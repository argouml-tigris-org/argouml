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

// File: Action.java
// Classes: Action
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;

/** Abstract class for all editor actions. The editor serves as a
 *  command shell for executing actions in much the same way that a
 *  DOS or UNIX commmand command shell executes programs. Each action
 *  can have a Hashtable of "command-line" arguments and also look at
 *  global variables (its environment). Once an instance of an Action
 *  is made, it can be sent the doIt() and undoIt() messages to perform
 *  that action. <p>
 *  needs-more-work: canDoIt, canUndoIt predicates control graying. <p>
 *  needs-more-work: Editor will keep a history of recent actions for
 *  undo. <p>
 *  <A HREF="../features.html#editing_actions">
 *  <TT>FEATURE: editing_actions</TT></A>
 *  <A HREF="../features.html#undo_and_redo">
 *  <TT>FEATURE: undo_and_redo</TT></A>
 * @see Editor
 * @see ExecuteActionWindow
 */
public abstract class Action implements java.io.Serializable {
  //implements GEF {
  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Arguments that configure the Action instance. */
  protected Hashtable _args;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new Action with the given arguments */
  public Action(Hashtable args) { _args = args; }

  /** Construct a new Action with no arguments */
  public Action() { this(null); }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Return a name for this action suitable for display to the user */
  public String name() { return this.getClass().getName(); }

  /** Get the object stored as an argument under the given name. */
  protected Object getArg(String key) {
    if (_args == null) return null;
    else return _args.get(key);
  }

  /** Get an argument by name.  If it's not defined then use the given
   *  default. */
  protected Object getArg(String key, Object defaultValue) {
    if (_args == null) return defaultValue;
    Object res = _args.get(key);
    if (res == null) return defaultValue;
    return res;
  }

  /** Store the given argument under the given name. */
  protected void setArg(String key, Object value) {
    if (_args == null) { _args = new Hashtable(); }
    _args.put(key, value);
  }

  /** Reply true if this Action instance has the named argument defined. */
  protected boolean containsArg(String key) {
    return _args != null && _args.containsKey(key);
  }

  ////////////////////////////////////////////////////////////////
  // Action API

  /** Return a URL that has user and programmer documentation.
   *  <A HREF="../features.html#view_action_documentation">
   *  <TT>FEATURE: view_action_documentation</TT></A>
   */
  public String about() {
    return "http://www.ics.uci.edu/~jrobbins/gef/Docs.html#" +
		 getClass().getName();
  }

  /** Perform whatever action this Action is meant to do. Subclasses
   *  should override this to do whatever is intended. When the action
   *  executes, it should store enough information to undo itself later
   *  if needed.
   *  @param e  The event that caused this Action to be
   *  performed. Generally it is a bad idea to depend on the
   *  contents of this event too much.
   */
  public abstract void doIt(java.awt.Event e);

  /** Undo the action using information stored during its
   *  execution. <p>
   *  needs-more-work: This is not currently implemented.
   */
  public abstract void undoIt();

  // needs-more-work: do I need a separate redo()? Should doIt() take
  // flag to indicate if this is the first time the action is being
  // done, or it it is actually being redone? What information does
  // undoIt() need to store to support redo?

  ////////////////////////////////////////////////////////////////
  // registered actions

  /** A list of Action instances that should appear in lists for the
   *  user to pick from. Registered actions server mainly to support
   *  user interface prototyping: you can add actions to the
   *  ExecuteActionWindow and not have to woory about where it should
   *  eventually go in the user interface. */
  private static Vector _registeredActions = new Vector();

  /** Return a list of "well-known" Action instances that should
   *  appear in lists for the user to pick from.
   * @see ExecuteActionWindow */
  public static Enumeration registeredActions() {
    return _registeredActions.elements();
  }

  /** Add a "well-known" Action */
  public static void register(Action a) { _registeredActions.addElement(a); }

  /** Return the "well-known" action at a given index. Useful for
   *  displaying a list of "well-known" actions. */
  public static Action actionAtIndex(int i) {
    return (Action) _registeredActions.elementAt(i);
  }

} /* end class Action */

