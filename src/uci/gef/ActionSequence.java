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

// File: ActionSequence.java
// Classes: ActionSequence
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** Action to execute a sequence of Action's.  Needs-More-Work: This
 *  could be part of a user macro recording feature.  Or an easy way
 *  for programmers to define new actions that are basically sequence
 *  of existing actions.
 *  <A HREF="../features.html#macros">
 *  <TT>FEATURE: macros</TT></A>
 */

public class ActionSequence extends Action {

  private Vector actions;

  /** Construct a new ActionSequence */
  public ActionSequence() { }
  public ActionSequence(Action a1) { add(a1); }
  public ActionSequence(Action a1, Action a2) { add(a1); add(a2); }
  public ActionSequence(Action a1, Action a2, Action a3) {
    add(a1); add(a2); add(a3);
  }

  public void add(Action a) { actions.addElement(a); };

  public String name() {
    String n = "Seq: ";
    Enumeration acts = actions.elements();
    while (acts.hasMoreElements()) {
      Action a = (Action) acts.nextElement();
      n = n + a.name();
      if (acts.hasMoreElements()) n = n + ", ";
    }
    return n;
  }

  /** Call the undoIt method of each subAction. */
  public void doIt(Event e) {
    Editor ce = Globals.curEditor();
    Enumeration acts = actions.elements();
    while (acts.hasMoreElements()) {
      Action a = (Action) acts.nextElement();
      ce.executeAction(a, e);
    }
  }

  /** Call the undoIt method of each subAction, in reverse order! */
  public void undoIt() {
    for (int i = actions.size() - 1; i >= 0; --i)
      ((Action)actions.elementAt(i)).undoIt();
  }

} /* end class ActionSequence */

