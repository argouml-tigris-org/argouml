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

// File: ActionSetMode.java
// Classes: ActionSetMode
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;

/** Action that sets the next global editor mode. The global editor
 *  mode effects the next editor that you move the mouse into. For
 *  example, used in palette * buttons to set the next global mode to
 *  ModeCreateFigLine.
 *
 * @see PaletteFig */

public class ActionSetMode extends Action {
  /** The class of the desired mode. */
  private Class _desiredModeClass;

  /** Arguments to pass to the new mode instance after creation. */
  private Properties _modeArgs;

  /** Override Action constructor so that setModeClass is allways called. */
  public ActionSetMode(Properties args) {
    super(args);
    setModeClass();
  }

  /** Set the next global mode to the named mode. */
  public ActionSetMode(String modeName) {
    setArg("desiredModeName", modeName);
    setModeClass();
  }

  /** Set the next global mode to the named mode, and maybe make it sticky. */
  public ActionSetMode(String modeName, boolean sticky) {
    setArg("desiredModeName", modeName);
    setArg("shouldBeSticky", sticky ? "True" : "False");
    setModeClass();
  }

  /** Set the next global mode to the named mode, and maybe make it sticky. */
  public ActionSetMode(String modeName, Properties modeArgs) {
    setArg("desiredModeName", modeName);
    _modeArgs = modeArgs;
    setModeClass();
  }

  /** Set the next global mode to the named mode, and maybe make it sticky. */
  public ActionSetMode(String modeName, String modeArgKey, String modeArgVal) {
    setArg("desiredModeName", modeName);
    _modeArgs = new Properties();
    _modeArgs.put(modeArgKey, modeArgVal);
    setModeClass();
  }

  public String name() { return "Set Editor Mode"; }

  /** Find the class of the next global mode from the given name. */
  private void setModeClass() {
    String desiredModeName = (String) getArg("desiredModeName", "ModeSelect");
    try { _desiredModeClass = Class.forName(desiredModeName);}
    catch (java.lang.ClassNotFoundException ignore) { }
  }

  public void doIt(java.awt.Event e) {
    Mode mode;
    // needs-more-work: if mode is not defined, prompt the user
    try { mode = (Mode) _desiredModeClass.newInstance(); }
    catch (java.lang.InstantiationException ignore) {  return; }
    catch (java.lang.IllegalAccessException ignore) { return; }
    mode.args(_modeArgs);
    String shouldBeSticky = (String) getArg("shouldBeSticky");
    if (shouldBeSticky == null) Globals.mode(mode);
    else Globals.mode(mode, shouldBeSticky.equals("True"));
  }

  public void undoIt() {
    System.out.println("undo does not make sense for setting modes");
  }

} /* end class ActionSetMode */
