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

// File: ActionAdjustGrid.java
// Classes: ActionAdjustGrid
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.Event;

/** An Action to modify the way that the grid layer of the current
 *  document looks.  For now it just cycles among a few predefined
 *  looks.  Needs-More-Work: should put up a grid preference dialog
 *  box or use property sheet.  */
public class ActionAdjustGrid extends Action {

  public ActionAdjustGrid() { }

  public String name() { return "Adjust Grid Appearance"; }

  public void doIt(java.awt.Event e) {
    Editor ce = Globals.curEditor();
    Layer grid = (Layer) ce.getLayerManager().findLayerNamed("Grid");
    if (grid != null) {
      grid.adjust();
    }
  }

  public void undoIt() { }

} /* end class ActionAdjustGrid */
