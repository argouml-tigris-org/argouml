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

// File: LayerPageBreaks.java
// Classes: LayerPageBreaks
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** Paint horizontal and vertical lines showing page braks for
 *  printing. This feature is common to many drawing applications
 *  (e.g., MacDraw). LayerPageBreaks is a Layer, just like any other so
 *  it can be composed, hidden, and reordered. */

public class LayerPageBreaks extends Layer {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** True means paint PageBreaks lines. */
  private boolean _paintLines = true;

  /** The color of the grid lines or dots. */
  protected Color _color = Color.darkGray;

  /** The size of the page in pixels. */
  protected Dimension _pageSize = new Dimension(612, 792);

  ////////////////////////////////////////////////////////////////
  // constructors

  public LayerPageBreaks() { super("PageBreaks"); }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Set the size of the page in pixels. */
  public void setPageSize(Dimension d) { _pageSize = d; }

  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Paint the PageBreaks lines or dots by repeatedly bitblting a
   * precomputed 'stamp' onto the given Graphics */
  public synchronized void paintContents(Graphics g) {
    if (g instanceof PrintGraphics) return; // for printing under Java 1.1
    if (!_paintLines) return;
    if (_pageSize == null) return;
    Rectangle clip = g.getClipRect();
    int x = clip.x / _pageSize.width * _pageSize.width - _pageSize.width;
    int y = clip.y / _pageSize.height * _pageSize.height - _pageSize.height;
    int right = clip.x + clip.width;
    int bot = clip.y + clip.height;
    int stepsX = (right - x) / _pageSize.width + 1;
    int stepsY = (bot - y) / _pageSize.height + 1;
    g.setColor(_color);

    while (stepsX > 0) {
      g.drawLine(x - 1, 0, x - 1, bot);
      x += _pageSize.width;
      --stepsX;
    }
    while (stepsY > 0) {
      g.drawLine(0, y - 1, right, y - 1);
      y += _pageSize.height;
      --stepsY;
    }
  }

  ////////////////////////////////////////////////////////////////
  // user interface

  /** Toggle whether page break lines are drawn on the screen.
   *  Needs-More-Work: Eventually this will open a dialog box to set
   *  all parameters. */
  public void adjust() {
    _paintLines = !_paintLines;
    refreshEditors();
  }

} /* end class LayerPageBreaks */
