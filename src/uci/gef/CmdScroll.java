// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.




// File: CmdScroll.java
// Classes: CmdSroll
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** Cmd scroll the view.  Needs-More-Work: not implemented yet.  */

public class CmdScroll extends Cmd {
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

  public CmdScroll(int dir) {
    super("Scroll " + wordFor(dir), NO_ICON);
    _direction = dir;
    _magnitude = 16; // Needs-More-Work: prefs
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
  
  /** Scroll the view of the current editor in the given direction.
   *  Needs-More-Work: not implemented yet.  */
  public void doIt() {
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
    System.out.println("Cannot undo CmdScroll, yet.");
  }

  static final long serialVersionUID = 1541329454323562639L;

} /* end class CmdScroll */
