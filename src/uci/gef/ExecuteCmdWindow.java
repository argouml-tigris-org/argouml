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

// File: ExecuteCmdWindow.java
// Classes: ExecuteCmdWindow
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;

/** The ExecuteCmdWindow lists out several Cmds that the user
 *  can execute by clicking on the Cmd name. This allows
 *  programmers to add new Cmds without having to work out where
 *  exactly in the interface to put it, and allows users to execute
 *  Cmds without having to know the right keystrokes or get into
 *  the proper mode. Users can also see on-line help for each Cmd
 *  from this list.
 *  <A HREF="../features.html#execute_cmd_window">
 *  <TT>FEATURE: execute_cmd_window</TT></A>
 *  <A HREF="../features.html#view_cmd_documentation">
 *  <TT>FEATURE: view_cmd_documentation</TT></A>
 */

public class ExecuteCmdWindow extends Frame {
  ////////////////////////////////////////////////////////////////
  // instance variables

  List _cmdNames = new List();
  Button _execute = new Button("Execute");
  /** <A HREF="../features.html#view_cmd_documentation">
   *  <TT>FEATURE: view_cmd_documentation</TT></A>
   */
  Button _about = new Button("About");
  Button _close = new Button("Close");

  ////////////////////////////////////////////////////////////////
  // constructors

  public ExecuteCmdWindow() {
    Enumeration acts = Cmd.registeredCmds();
    while (acts.hasMoreElements()) {
      Cmd a = (Cmd) acts.nextElement();
      _cmdNames.addItem(a.getName());
    }
    setBackground(Color.lightGray);
    add("Center", _cmdNames);
    Panel buttons = new Panel();
    buttons.setLayout(new FlowLayout());
    buttons.add(_execute);
    buttons.add(_about);
    _execute.disable();
    _about.disable();
    buttons.add(_close);
    add("South", buttons);
    pack();
    Dimension d = size();
    resize(d.width, Math.min(d.height * 2, 350));
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  public boolean handleEvent(Event e) {
    switch(e.id) {
    case Event.WINDOW_DESTROY:
      if (e.target == this) {
	close();
	return true;
      }
      break;
    case Event.LIST_SELECT:
      _execute.enable();
      _about.enable();
      return true;
    case Event.LIST_DESELECT:
      _execute.disable();
      _about.disable();
      return true;
    }
    return super.handleEvent(e);
  }

  public boolean getCmd(Event e, Object what) {
    if (e.target == _close) {
      close();
      return true;
    }
    else if (e.target == _execute) {
      executeCommand(e);
      return true;
    }
    else if (e.target == _about) {
      describeCommand();
      return true;
    }
    else if (e.target == _cmdNames) {
      executeCommand(e);
      return true;
    }
    return false;
  }

  public void close() { dispose(); }

  ////////////////////////////////////////////////////////////////
  // user interface

  protected void executeCommand(Event e) {
    Editor ce = Globals.curEditor();
    System.out.println("executing: " + _cmdNames.getSelectedItem());
    Cmd act = Cmd.cmdAtIndex(_cmdNames.getSelectedIndex());
    ce.executeCmd(act, null);
  }

  protected void describeCommand() {
    Editor ce = Globals.curEditor();
    Cmd act = Cmd.cmdAtIndex(_cmdNames.getSelectedIndex());
    Cmd showIt = null;
    try { showIt = new CmdShowURL(act.about()); }
    catch (Exception e) { System.out.println("malformed?"); }
    ce.executeCmd(showIt, null);
  }

} /* end class ExecuteCmdWindow */
