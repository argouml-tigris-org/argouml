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

// File: CmdRemovePoint.java
// Classes: CmdRemovePoint
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.Event;

/** Cmd to remove the selected (last manipulated) point from a FigPoly.
 *
 * @see FigPoly
 */

public class CmdRemovePoint extends Cmd {

  protected int _selectedHandle = 0;

  public CmdRemovePoint() { super("Remove Point From Polygon"); }

  public CmdRemovePoint(int i) { this(); _selectedHandle = i; }

  /** Ask the current editor's selected Fig to remove its point. */
  public void doIt() {
    Fig f = null;
    Selection sel = null;
    Editor ce = Globals.curEditor();
    SelectionManager sm = ce.getSelectionManager();
    if (sm.getLocked()) {
      Globals.showStatus("Cannot Modify Locked Objects");
      return;
    }

    if (sm.selections().isEmpty()) return;
    sel = (Selection) sm.selections().firstElement();
    f = (Fig) sel.getContent();
    // // if (f instanceof FigPoly) {
    //FigPoly p = (FigPoly) f;
    f.startTrans();
    f.removePoint(_selectedHandle);
    f.endTrans();
    //  //    }
    // //     if (f != null && f instanceof ArcPerzRectiline) {
    // //         ArcPerzRectiline p = (ArcPerzRectiline) f;
    // //         p.startTrans();
    // //         p.removePoint(_selectedHandle);
    // //         p.endTrans();
    // //     }
  }

  public void undoIt() {
    System.out.println("this operation currently cannot be undone");
  }

} /* end class CmdRemovePoint */
