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

// File: ActionSelectAll.java
// Classes: ActionSelectAll
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;

/** Action to select all the Figs in the editor's current
 *  view.
 *  <A HREF="../features.html#select_all">
 *  <TT>FEATURE: select_all</TT></A>
 *
 * @see ModeSelect#bindKeys */

public class ActionSelectAll extends Action {

  public ActionSelectAll() { }

  public String name() { return "Select All Figs in Layer"; }

  public void doIt(java.awt.Event e) {
    Editor ce = Globals.curEditor();
    Vector diagramContents = ce.getLayerManager().contents();
    ce.select(diagramContents);
  }

  public void undoIt() {
    System.out.println("Undo does not make sense for ActionSelectAll");
  }

} /* end class ActionSelectAll */

