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




// File: CmdSelectNear.java
// Classes: CmdSelectNear
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.Event;

/** Cmd to SelectNear Figs by a small distance.  This is useful when you
 *  want to get diagrams to look just right and you are not to steady
 *  with the mouse.  Also allows user to keep hands on keyboard.
 *
 * @see Fig */

public class CmdSelectNear extends Cmd {
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

  public CmdSelectNear(int dir) { this(dir, 1); }
  public CmdSelectNear(int dir, int mag) {
    super("SelectNear " + wordFor(dir), NO_ICON); //needs-more-work: direction
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

  public void undoIt() { System.out.println("Cannot undo CmdSelectNear, yet."); }

} /* end class CmdSelectNear */

