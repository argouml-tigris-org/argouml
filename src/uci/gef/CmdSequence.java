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

// File: CmdSequence.java
// Classes: CmdSequence
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** Cmd to execute a sequence of Cmd's.  Needs-More-Work: This
 *  could be part of a user macro recording feature.  Or an easy way
 *  for programmers to define new Cmds that are basically sequence
 *  of existing Cmds.
 *  <A HREF="../features.html#macros">
 *  <TT>FEATURE: macros</TT></A>
 */

public class CmdSequence extends Cmd {

  private Vector _cmds;

  /** Construct a new CmdSequence */
  public CmdSequence() { super("Sequence of Commands"); }
  public CmdSequence(String name) { super(name); }
  public CmdSequence(Cmd a1, String name) {
    super(name);
    add(a1);
  }
  public CmdSequence(Cmd a1, Cmd a2, String name) {
    super(name);
    add(a1);
    add(a2);
  }
  public CmdSequence(Cmd a1, Cmd a2, Cmd a3, String name) {
    super(name);
    add(a1);
    add(a2);
    add(a3);
  }

  public void add(Cmd a) { _cmds.addElement(a); };

  public String dbgString() {
    String n = "Seq: ";
    Enumeration acts = _cmds.elements();
    while (acts.hasMoreElements()) {
      Cmd a = (Cmd) acts.nextElement();
      n = n + a.getName();
      if (acts.hasMoreElements()) n = n + ", ";
    }
    return n;
  }

  /** Call the undoIt method of each subCmd. */
  public void doIt() {
    Editor ce = Globals.curEditor();
    Enumeration acts = _cmds.elements();
    while (acts.hasMoreElements()) {
      Cmd a = (Cmd) acts.nextElement();
      ce.executeCmd(a, null);
    }
  }

  /** Call the undoIt method of each subCmd, in reverse order! */
  public void undoIt() {
    for (int i = _cmds.size() - 1; i >= 0; --i)
      ((Cmd)_cmds.elementAt(i)).undoIt();
  }

} /* end class CmdSequence */

