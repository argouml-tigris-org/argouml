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



// File: ModeCreateFigInk.java
// Classes: ModeCreateFigInk
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.awt.event.MouseEvent;

/** A Mode to interpert user input while creating a FigInk. When
 *  creating a FigInk, new points are being added on mouseDragged, and
 *  a single mouseReleased ends the Mode.  A new point is added
 *  whenever the mouse moves a minimum distance. */

public class ModeCreateFigInk extends ModeCreate {

  ////////////////////////////////////////////////////////////////
  // constants

  /** the minium distance that the mouse must move before a new point
   *  is added. */
  public static final int MIN_DELTA = 4;

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The position of the last point that was added. */
  protected int _lastX, _lastY;

  ////////////////////////////////////////////////////////////////
  // Mode API

  public String instructions() { return "Drag to draw a stream of ink"; }

  ////////////////////////////////////////////////////////////////
  // ModeCreate API

  /** Create a new FigInk instance based on the given mouse down
   *  event and the state of the parent Editor. */
  public Fig createNewItem(MouseEvent me, int snapX, int snapY) {
    FigInk p = new FigInk(snapX, snapY);
    _lastX = snapX; _lastY = snapY;
    return p;
  }

  protected void creationDrag(int x, int y) { }

  ////////////////////////////////////////////////////////////////
  // Event handlers

  /** Dragging adds points to the ink. */
  public void mouseDragged(MouseEvent me) {
    if (me.isConsumed()) return;
    int x = me.getX(), y = me.getY();
    FigInk ink = (FigInk)_newItem;
    if (!nearLast(x, y)) {
      _editor.damaged(_newItem); // startTrans?
      ink.addPoint(x, y);
      _editor.damaged(_newItem); // endTrans?
      _lastX = x; _lastY = y;
    }
    me.consume();
  }

  /** Internal function to test if the current point is so close to
   *  the last point that it should not be added to the ink. */
  protected boolean nearLast(int x, int y) {
    return x > _lastX - MIN_DELTA &&
      x < _lastX + MIN_DELTA &&
      y > _lastY - MIN_DELTA &&
      y < _lastY + MIN_DELTA;
  }

  static final long serialVersionUID = -6846010734902778871L;
} /* end class ModeCreateFigInk */

