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




// File: SelectionLowerRight.java
// Classes: SelectionLowerRight
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** A Selection class to represent selections on Figs that present
 *  resize handles. The selected Fig can be moved or resized. Figrect,
 *  FigRRect, FigCircle, and FigGroup are some of the Figs that
 *  normally use this Selection.  The selected Fig is told it's new
 *  bounding box, and some Figs (like FigGroup or FigPoly) do
 *  calculations to scale themselves.*/

public class SelectionLowerRight extends Selection {

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new SelectionLowerRight for the given Fig */
  public SelectionLowerRight(Fig f) { super(f); }

  /** Return a handle ID for the handle under the mouse, or -1 if
   *  none. Needs-More-Work: in the future, return a Handle instance or
   *  null. <p>
   *  <pre>
   *   0-------1-------2
   *   |               |
   *   3               4
   *   |               |
   *   5-------6-------7
   * </pre>
   */
  public void hitHandle(Rectangle r, Handle h) {
    int cx = _content.getX();
    int cy = _content.getY();
    int cw = _content.getWidth();
    int ch = _content.getHeight();
    Rectangle testRect = new Rectangle(cx+cw, cy+ch, HAND_SIZE, HAND_SIZE);
    if (r.intersects(testRect)) {
      h.index = 7; h.instructions = "Resize object";
    }
    else { h.index = -1; }
  }

  /** Paint the handles at the four corners and midway along each edge
   * of the bounding box.  */
  public void paint(Graphics g) {
    int cx = _content.getX();
    int cy = _content.getY();
    int cw = _content.getWidth();
    int ch = _content.getHeight();
    g.setColor(Globals.getPrefs().handleColorFor(_content));

    //dashed
    g.drawRect(cx - HAND_SIZE/2, cy - HAND_SIZE/2,
	       cw + HAND_SIZE-1, ch + HAND_SIZE-1);
    g.fillRect(cx + cw, cy + ch, HAND_SIZE, HAND_SIZE);
    super.paint(g);
  }

  /** Change some attribute of the selected Fig when the user drags one of its
   *  handles. Needs-More-Work: someday I might implement resizing that
   *  maintains the aspect ratio. */
  public void dragHandle(int mX, int mY, int anX, int anY, Handle hand) {
    int x = _content.getX(), y = _content.getY();
    int w = _content.getWidth(), h = _content.getHeight();
    int newX = x, newY = y, newW = w, newH = h;
    Dimension minSize = _content.getMinimumSize();
    int minWidth = minSize.width, minHeight = minSize.height;
    switch (hand.index) {
    case -1:
      _content.translate(anX - mX, anY - mY);
      return;
    case 0: break;
    case 1: break;
    case 2: break;
    case 3: break;
    case 4: break;
    case 5: break;
    case 6: break;
    case 7:
      newW = mX - x;
      newW = (newW < minWidth) ? minWidth : newW;
      newH = mY - y;
      newH = (newH < minHeight) ? minHeight : newH;
      break;
    default:
      System.out.println("invalid handle number");
      break;
    }
    _content.setBounds(newX, newY, newW, newH);
  }

  static final long serialVersionUID = 5186482042725814027L;
} /* end class SelectionLowerRight */

