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

// File: CmdNudge.java
// Classes: CmdNudge
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.Event;

/** Cmd to Nudge Figs by a small distance.  This is
 *  useful when you want to get diagrams to look just right and you
 *  are not to steady with the mouse.  Also allows user to keep hands
 *  on keyboard.
 *  <A HREF="../features.html#nudge_objects">
 *  <TT>FEATURE: nudge_objects</TT></A>
 *
 * @see Fig */

public class CmdNudge extends Cmd {
  ////////////////////////////////////////////////////////////////
  // constants
  public static final int LEFT = 1;
  public static final int RIGHT = 2;
  public static final int UP = 3;
  public static final int DOWN = 4;

  ////////////////////////////////////////////////////////////////
  // instance variables
  private int _direction;
  private int _magnitude;

  ////////////////////////////////////////////////////////////////
  // constructor

  public CmdNudge(int dir) { this(dir, 1); }
  public CmdNudge(int dir, int mag) {
    super("Nudge " + wordFor(dir)); //needs-more-work: direction
    _direction = dir;
    _magnitude = mag;
  }

  protected static String wordFor(int d) {
    switch (d) {
    case LEFT: return "Left";
    case RIGHT: return "Right";
    case UP: return "Up";
    case DOWN: return "Down";
    }
    return "";
  }

  ////////////////////////////////////////////////////////////////
  // Cmd API

  /** Move the selected items a few pixels in the given
   *  direction. Note that the sign convention is the opposite of
   *  CmdScroll. */
  public void doIt() {
    Editor ce = Globals.curEditor();
    SelectionManager sm = ce.getSelectionManager();
    if (sm.getLocked()) {
       Globals.showStatus("Cannot Modify Locked Objects");
       return;
    }

    int dx = 0, dy = 0;
    switch (_direction) {
      case LEFT: dx = 0 - _magnitude; break;
      case RIGHT: dx = _magnitude; break;
      case UP: dy = 0 - _magnitude; break;
      case DOWN: dy = _magnitude; break;
    }
    // Should I move it so that it aligns with the next grid?
    sm.startTrans();
    sm.translate(dx, dy);
    sm.endTrans();
  }

  public void undoIt() { System.out.println("Cannot undo CmdNudge, yet."); }

} /* end class CmdNudge */

