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
import java.awt.event.*;
import com.sun.java.swing.*;

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
public abstract class Cmd extends AbstractAction
implements java.io.Serializable {
  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Arguments that configure the Action instance. */
  protected Hashtable _args;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new Action with the given arguments */
  public Cmd(Hashtable args, String name) {
    super(name, loadIconResource(imageName(name), name));
    _args = args;
  }

  /** Construct a new Cmd with no arguments */
  public Cmd(String name) { this(null, name); }


  protected static ImageIcon loadIconResource(String imgName, String desc) {
    ImageIcon res = null;
    try {
      java.net.URL imgURL = Cmd.class.getResource(imgName);
      return new ImageIcon(imgURL, desc);
    }
    catch (Exception ex) { return new ImageIcon(desc); }
  }

  protected static String imageName(String name) {
    return "Images/" + stripJunk(name) + ".gif";
  }

  protected static String stripJunk(String s) {
    String res = "";
    int len = s.length();
    for (int i = 0; i < len; i++) {
      char c = s.charAt(i);
      if (Character.isJavaLetterOrDigit(c)) res += c;
    }
    return res;
  }

  ////////////////////////////////////////////////////////////////
  // enabling and disabling

  public void updateEnabled() { setEnabled(shouldBeEnabled()); }

  /** return true if this action should be available to the user. This
   *  method should examine the ProjectBrowser that owns it.  Sublass
   *  implementations of this method should always call
   *  super.shouldBeEnabled first. */
  public boolean shouldBeEnabled() { return true; }



  ////////////////////////////////////////////////////////////////
  // accessors

  /** Return a name for this Cmd suitable for display to the user */
  public String getName() { return (String) getValue(NAME); }
  public void setName(String n) { putValue(NAME, n); }

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

  /** Reply true if this Cmd instance has the named argument defined. */
  protected boolean containsArg(String key) {
    return _args != null && _args.containsKey(key);
  }

  ////////////////////////////////////////////////////////////////
  // Cmd API

  /** Return a URL that has user and programmer documentation.
   *  <A HREF="../features.html#view_Cmd_documentation">
   *  <TT>FEATURE: view_Cmd_documentation</TT></A>
   */
  public String about() {
    return "http://www.ics.uci.edu/~jrobbins/gef/Docs.html#" +
		 getClass().getName();
  }

  public void actionPerformed(ActionEvent ae) { doIt(); }
  
  /** Perform whatever Cmd this Cmd is meant to do. Subclasses
   *  should override this to do whatever is intended. When the Cmd
   *  executes, it should store enough information to undo itself later
   *  if needed.
   *  @param e  The event that caused this Cmd to be
   *  performed. Generally it is a bad idea to depend on the
   *  contents of this event too much.
   */
  public abstract void doIt();

  /** Undo the Cmd using information stored during its
   *  execution. <p>
   *  needs-more-work: This is not currently implemented.
   */
  public abstract void undoIt();

  // needs-more-work: do I need a separate redo()? Should doIt() take
  // flag to indicate if this is the first time the Cmd is being
  // done, or it it is actually being redone? What information does
  // undoIt() need to store to support redo?

  ////////////////////////////////////////////////////////////////
  // registered Cmds

  /** A list of Cmd instances that should appear in lists for the
   *  user to pick from. Registered Cmds server mainly to support
   *  user interface prototyping: you can add Cmds to the
   *  ExecuteCmdWindow and not have to woory about where it should
   *  eventually go in the user interface. */
  private static Vector _registeredCmds = new Vector();

  /** Return a list of "well-known" Cmd instances that should
   *  appear in lists for the user to pick from.
   * @see ExecuteCmdWindow */
  public static Enumeration registeredCmds() {
    return _registeredCmds.elements();
  }

  /** Add a "well-known" Cmd */
  public static void register(Cmd c) { _registeredCmds.addElement(c); }

  /** Return the "well-known" Cmd at a given index. Useful for
   *  displaying a list of "well-known" cmds. */
  public static Cmd cmdAtIndex(int i) {
    return (Cmd) _registeredCmds.elementAt(i);
  }

} /* end class Cmd */

