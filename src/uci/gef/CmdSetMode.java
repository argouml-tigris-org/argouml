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

// File: CmdSetMode.java
// Classes: CmdSetMode
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;

/** Cmd that sets the next global editor mode. The global editor
 *  mode effects the next editor that you move the mouse into. For
 *  example, used in palette * buttons to set the next global mode to
 *  ModeCreateFigLine.
 *
 * @see PaletteFig */

public class CmdSetMode extends Cmd {
  /** Arguments to pass to the new mode instance after creation. */
  protected Hashtable _modeArgs;

  public CmdSetMode(Properties args) {
    super(args, "Set Editor Mode");
  }

  /** Set the next global mode to the named mode. */
  public CmdSetMode(Class modeClass) {
    super("Set Editor Mode");
    setArg("desiredModeClass", modeClass);
  }

  public CmdSetMode(Class modeClass, String name) {
    super(name);
    setArg("desiredModeClass", modeClass);
  }

  /** Set the next global mode to the named mode, and maybe make it sticky. */
  public CmdSetMode(Class modeClass, boolean sticky) {
    super("Set Editor Mode");
    setArg("desiredModeClass", modeClass);
    setArg("shouldBeSticky", sticky ? Boolean.TRUE : Boolean.FALSE);
  }

  /** Set the next global mode to the named mode, and maybe make it sticky. */
  public CmdSetMode(Class modeClass, Hashtable modeArgs) {
    super("Set Editor Mode");
    setArg("desiredModeClass", modeClass);
    _modeArgs = modeArgs;
  }

  public CmdSetMode(Class modeClass, String arg, Object value) {
    super("Set Editor Mode");
    _modeArgs = new Hashtable(1);
    _modeArgs.put(arg, value);
    setArg("desiredModeClass", modeClass);
  }

  public void doIt() {
    Mode mode;
    Class desiredModeClass = (Class) getArg("desiredModeClass");
    // needs-more-work: if mode is not defined, prompt the user
    try { mode = (Mode) desiredModeClass.newInstance(); }
    catch (java.lang.InstantiationException ignore) {  return; }
    catch (java.lang.IllegalAccessException ignore) { return; }
    mode.setArgs(_modeArgs);
    Boolean shouldBeSticky = (Boolean) getArg("shouldBeSticky");
    if (shouldBeSticky == null) Globals.mode(mode);
    else Globals.mode(mode, shouldBeSticky.booleanValue());
  }

  public void undoIt() {
    System.out.println("undo does not make sense for setting modes");
  }

} /* end class CmdSetMode */
