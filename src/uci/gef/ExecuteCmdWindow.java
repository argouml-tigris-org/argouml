// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.




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
    _execute.setEnabled(false);
    _about.setEnabled(false);
    buttons.add(_close);
    add("South", buttons);
    pack();
    Dimension d = getSize();
    setSize(d.width, Math.min(d.height * 2, 350));
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
      _execute.setEnabled(true);
      _about.setEnabled(true);
      return true;
    case Event.LIST_DESELECT:
      _execute.setEnabled(false);
      _about.setEnabled(false);
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

  static final long serialVersionUID = 7244235935139519043L;

} /* end class ExecuteCmdWindow */
