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



// File: ModeCreateFigSpline.java
// Classes: ModeCreateFigSpline
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.MouseEvent;

/** A Mode to interpert user input while creating a FigSpline. All of
 *  the actual event handling is inherited from ModeCreate. This class
 *  just implements the differences needed to make it specific to
 *  polygons. */

public class ModeCreateFigSpline extends ModeCreateFigPoly {


  ////////////////////////////////////////////////////////////////
  // Mode API

  public String instructions() {
    return "Click to add a point; Double-click to finish";
  }

  ////////////////////////////////////////////////////////////////
  // ModeCreate API

  /** Create a new FigRect instance based on the given mouse down
   *  event and the state of the parent Editor. */
  public Fig createNewItem(MouseEvent me, int snapX, int snapY) {
    FigSpline p = new FigSpline(snapX, snapY);
    p.addPoint(snapX, snapY); // add the first point twice
    _startX = _lastX = snapX; _startY = _lastY = snapY;
    _npoints = 2;
    return p;
  }

  ////////////////////////////////////////////////////////////////
  // Event handlers


  public void mouseReleased(MouseEvent me) {
    if (me.isConsumed()) return;
    int x = me.getX(), y = me.getY();
    if (_npoints > 2 && nearLast(x, y)) {
      FigSpline p = (FigSpline) _newItem;
      _editor.damaged(_newItem);
      _handle.index = p.getNumPoints() - 1;
      p.moveVertex(_handle, _startX, _startY, true);

      p.removePoint(_handle.index);

      _npoints = 0;
      _editor.damaged(p);
      _editor.add(p);
      _editor.getSelectionManager().select(p);
      _newItem = null;
      done();
      me.consume();
      return;
    }
    _lastX = x; _lastY = y;
    me.consume();
  }


} /* end class ModeCreateFigSpline */

