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

} /* end class GuideGrid */
