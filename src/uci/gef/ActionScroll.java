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

// File: ActionScroll.java
// Classes: ActionSroll
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** Action scroll the view.  Needs-More-Work: not implemented yet.  */

public class ActionScroll extends Action {
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

  public ActionScroll(int dir) {
    _direction = dir;
    _magnitude = 16; // Needs-More-Work: prefs
    }

  /** Scroll the view of the current editor in the given direction.
   *  Needs-More-Work: not implemented yet.  */
  public void doIt(Event e) {
      int deltaX = 0, deltaY = 0;
      // Needs-More-Work
      switch (_direction) {
        case LEFT: deltaX = _magnitude; break;
        case RIGHT: deltaX = 0 - _magnitude; break;
        case UP: deltaY = _magnitude; break;
        case DOWN: deltaY = 0 - _magnitude; break;
      }
      // Needs-More-Work: now do something with deltas...
      System.out.println("Scrolling by " + deltaX + ", " + deltaY);
  }

  public void undoIt() {
    System.out.println("Cannot undo ActionScroll, yet.");
  }

} /* end class ActionScroll */
