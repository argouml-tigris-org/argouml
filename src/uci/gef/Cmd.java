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




// File: Action.java
// Classes: Action
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

import uci.util.Util;

/** Abstract class for all editor commands. The editor serves as a
 *  command shell for executing actions in much the same way that a
 *  DOS or UNIX commmand command shell executes programs. Each command
 *  can have a Hashtable of "command-line" arguments and also look at
 *  global variables (its environment). Once an instance of a Cmd is
 *  made, it can be sent the doIt() and undoIt() messages to perform
 *  that action. <p>
 *
 *  Since this is subclassed from class AbstractAction in the Swing
 *  user interface library, Cmd objects can be easily added to menus
 *  and toolbars. <p>
 *
 *  needs-more-work: canDoIt, canUndoIt predicates control
 *  graying. <p>
 *  
 *  needs-more-work: Editor will keep a history of recent
 *  actions for undo. <p>
 *
 * @see Editor
 * @see ExecuteActionWindow */

public abstract class Cmd extends AbstractAction
implements java.io.Serializable {

  ////////////////////////////////////////////////////////////////
  // constants

  // by default, every command has an icon
  public static final boolean HAS_ICON = true;
  public static final boolean NO_ICON = false;

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Arguments that configure the Cmd instance. */
  protected Hashtable _args;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new Cmd with the given arguments */
  public Cmd(Hashtable args, String name) {
    super(name);
    Icon icon = Util.loadIconResource(name, name);
    if (icon != null) putValue(Action.SMALL_ICON, icon);
    _args = args;
  }

  public Cmd(Hashtable args, String name, boolean hasIcon) {
    super(name);
    if (hasIcon) {
      Icon icon = Util.loadIconResource(name, name);
      if (icon != null) putValue(Action.SMALL_ICON, icon);
    }
    _args = args;
  }

  public Cmd(String name, boolean hasIcon) {
    this (null, name, hasIcon);
  }

  public Cmd(Hashtable args, String name, ImageIcon icon) {
    super(name, icon);
    _args = args;
  }

  /** Construct a new Cmd with no arguments */
  public Cmd(String name) { this(null, name); }

  ////////////////////////////////////////////////////////////////
  // enabling and disabling

  /** Determine if this Cmd should be shown as grayed out in menus and
   *  toolbars. */
  public void updateEnabled() { setEnabled(shouldBeEnabled()); }

  /** Return true if this action should be available to the user. This
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

  static final long serialVersionUID = -7733814100559726063L;

} /* end class Cmd */

