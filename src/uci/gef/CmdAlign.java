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

// File: CmdAlign.java
// Classes: CmdAlign
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.awt.*;

/** An Cmd to align 2 or more objects relative to each other.
 *  <A HREF="../features.html#align_objects">
 *  <TT>FEATURE: align_objects</TT></A>
 */
public class CmdAlign extends Cmd {
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

  /** Construct a new CmdAlign.
   *
   * @param dir The desired alignment direction, one of the constants
   * listed above. */
  public CmdAlign(int dir) {
    super("Align " + wordFor(dir)); //needs-more-work: direction
    direction = dir;
  }

  protected static String wordFor(int d) {
    switch (d) {
    case ALIGN_TOPS: return "Tops";
    case ALIGN_BOTTOMS: return "Bottoms";
    case ALIGN_LEFTS: return "Left";
    case ALIGN_RIGHTS: return "Rights";

    case ALIGN_CENTERS: return "Centers";
    case ALIGN_H_CENTERS: return "Horizontal Centers";
    case ALIGN_V_CENTERS: return "Vertical Centers";

    case ALIGN_TO_GRID: return "To Grid";
    }
    return "";
  }
  ////////////////////////////////////////////////////////////////
  // Cmd API

  public void doIt() {
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

} /* end class CmdAlign */

