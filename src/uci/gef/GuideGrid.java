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




// File: GuideGrid.java
// Classes: GuideGrid
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;

/** Constrains interactions to certain coordinates. In this case,
 *  GuideGrid makes objects snap to a grid. Note that GuideGrid is an
 *  invisible object that controls the behavior of the Editor and
 *  Modes. It is conceptually related to the visible grid (implemented
 *  in LayerGrid), but there is no functional relationship between them
 *  in this framework.  */

public class GuideGrid extends Guide {
  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Size of the grid. */
  protected int _gridSize = 8;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Make a new GuideGrid instance with the given grid size. */
  public GuideGrid(int size) { _gridSize = size; }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Reply the size of the grid to snap points to. */
  public int gridSize() { return _gridSize; }

  /** Set the size of the grid. */
  public void gridSize(int g) {  _gridSize = g; }

  ////////////////////////////////////////////////////////////////
  // Guide API

  /** Modify the given point to be on the guideline (In this case, a
   *  gridline) */
  public void snap(Point p) {
    p.x = (p.x + _gridSize / 2) / _gridSize * _gridSize;
    p.y = (p.y + _gridSize / 2) / _gridSize * _gridSize;
  }

  ////////////////////////////////////////////////////////////////
  // user interface

  /** Bring up a dialog box to set the grid snap parameters.
   *  Needs-More-Work: use the property sheet to change guide
   *  parameters. */
  public void adjust() {
    if (_gridSize >= 32) _gridSize = 4;
    else _gridSize *= 2;
  }

  static final long serialVersionUID = -7881039726350677403L;
} /* end class GuideGrid */
