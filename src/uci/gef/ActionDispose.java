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

// File: ActionDispose.java
// Classes: ActionDispose
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.awt.Event;

/** Action to delete the Figs selected in the current
 *  editor, and dispose any underlying Net stuctures. This differs
 *  from ActionDelete in that when an underlying NetNode is disposed,
 *  it should delete all views on it, not just the selected one.
 *
 * @see NetPrimitive#dispose
 * @see ActionDelete */

public class ActionDispose extends Action {

  public ActionDispose() { }

  public String name() { return "Dispose of Objects"; }

  public void doIt(Event e) {
    Editor ce = Globals.curEditor();
    SelectionManager sm = ce.getSelectionManager();
    sm.dispose(ce); //?
  }

  public void undoIt() { }

} /* end class ActionDispose */
