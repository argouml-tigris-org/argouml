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
  // constants

  /** The size of the dashes drawn when the Fig is dashed. */
  public final int DASH_LENGTH = 2;
  public final int GAP_LENGTH = 7;

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** True means paint PageBreaks lines. */
  private boolean _paintLines = false;

  /** The color of the grid lines or dots. */
  protected Color _color = Color.white;

  /** The size of the page in pixels.  Needs-More-Work: this is a
   *  hack.  To get the true page size I need to start a print job! */
  protected Dimension _pageSize = new Dimension(612-30, 792-55-20);

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
      drawDashedLine(g, 0, x - 1, 0, x - 1, bot);
      x += _pageSize.width;
      --stepsX;
    }
    while (stepsY > 0) {
      drawDashedLine(g, 0, 0, y - 1, right, y - 1);
      y += _pageSize.height;
      --stepsY;
    }
  }

  /* needs-more-work: this code is cut and paste from FigPoly */
  protected int drawDashedLine(Graphics g, int phase,
				int x1, int y1, int x2, int y2) {
    int segStartX, segStartY;
    int segEndX, segEndY;
    int dxdx = (x2 - x1) * (x2 - x1);
    int dydy = (y2 - y1) * (y2 - y1);
    int length = (int) Math.sqrt(dxdx + dydy);
    for (int i= phase; i < length - DASH_LENGTH; i += GAP_LENGTH) {
      segStartX = x1 + ((x2 - x1) * i) / length;
      segStartY = y1 + ((y2 - y1) * i) / length;
      i += DASH_LENGTH;
      if (i >= length) { segEndX = x2; segEndY = y2; }
      else {
	segEndX = x1 + ((x2 - x1) * i) / length;
	segEndY = y1 + ((y2 - y1) * i) / length;
      }
      g.drawLine(segStartX, segStartY, segEndX, segEndY );
    }
    // needs-more-work: phase not taken into account
    //return length % (DASH_LENGTH + DASH_LENGTH);
    return 0;
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

  static final long serialVersionUID = 4861970222441627645L;
} /* end class LayerPageBreaks */
