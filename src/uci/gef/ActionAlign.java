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

// File: ActionAlign.java
// Classes: ActionAlign
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.awt.*;

/** An Action to align 2 or more objects relative to each other.
 *  <A HREF="../features.html#align_objects">
 *  <TT>FEATURE: align_objects</TT></A>
 */
public class ActionAlign extends Action {
  ////////////////////////////////////////////////////////////////
  // constants

  /** Constants specifying the type of alignment requested. */
  public static final int ALIGN_TOPS = 0;
  public static final int ALIGN_BOTTOMS = 1;
  public static final int ALIGN_LEFTS = 2;
  public static final int ALIGN_RIGHTS = 3;

  public static final int ALIGN_CENTERS = 4;
  public static final int ALIGN_H_CENTERS = 5;
  public static final int ALIGN_V_CENTERS = 6;

  public static final int ALIGN_TO_GRID = 7;

  ////////////////////////////////////////////////////////////////
  // instanve variables

  /** Specification of the type of alignment requested */
  protected int direction;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new ActionAlign.
   *
   * @param dir The desired alignment direction, one of the constants
   * listed above. */
  public ActionAlign(int dir) { direction = dir; }

  ////////////////////////////////////////////////////////////////
  // Action API

  public String name() { return "Align Objects"; }

  public void doIt(Event e) {
    Editor ce = Globals.curEditor();
    SelectionManager sm = ce.getSelectionManager();
    if (sm.getLocked()) {
       Globals.showStatus("Cannot Modify Locked Objects");
       return;
    }
    sm.startTrans();
    sm.align(direction);
    sm.endTrans();
  }

  public void undoIt() { }

} /* end class ActionAlign */

