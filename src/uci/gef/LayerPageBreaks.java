// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.


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

  public Vector getContents() { return null; }
  public Fig presentationFor(Object obj) { return null; }

  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Paint the PageBreaks lines or dots by repeatedly bitblting a
   * precomputed 'stamp' onto the given Graphics */
  public synchronized void paintContents(Graphics g) {
    if (g instanceof PrintGraphics) return; // for printing under Java 1.1
    if (!_paintLines) return;
    if (_pageSize == null) return;
    Rectangle clip = g.getClipBounds();
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
