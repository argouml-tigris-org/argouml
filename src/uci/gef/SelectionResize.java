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


// File: SelectionResize.java
// Classes: SelectionResize
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** A Selection class to represent selections on Fig's that
 *  present handles. Needs-More-Work: in an early version of this graph
 *  editing framework the Fig's did their own painting of
 *  handles. I want to get away from that, but it has not happened
 *  yet.
 *  <A HREF="../features.html#selections_handles">
 *  <TT>FEATURE: selections_handles</TT></A>
 */

public class SelectionResize extends Selection {
  ////////////////////////////////////////////////////////////////
  // constants
  public final int MIN_SIZE = 4;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new SelectionResize for the given Fig */
  public SelectionResize(Fig f) { super(f); }

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
  public int hitHandle(Rectangle r) {
    int cx = _content.getX();
    int cy = _content.getY();
    int cw = _content.getWidth();
    int ch = _content.getHeight();
    Rectangle testRect = new Rectangle(0, 0, 0, 0);
    testRect.reshape(cx-HAND_SIZE/2, cy-HAND_SIZE/2,
		     HAND_SIZE, ch+HAND_SIZE/2);
    boolean leftEdge = r.intersects(testRect);
    testRect.reshape(cx+cw-HAND_SIZE/2, cy-HAND_SIZE/2,
		     HAND_SIZE, ch+HAND_SIZE/2);
    boolean rightEdge = r.intersects(testRect);
    testRect.reshape(cx-HAND_SIZE/2, cy-HAND_SIZE/2,
		     cw+HAND_SIZE/2, HAND_SIZE);
    boolean topEdge = r.intersects(testRect);
    testRect.reshape(cx-HAND_SIZE/2, cy+ch-HAND_SIZE/2,
		     cw+HAND_SIZE/2, HAND_SIZE);
    boolean bottomEdge = r.intersects(testRect);
    // needs-more-work: midpoints for side handles
    if (leftEdge && topEdge) return 0;
    if (rightEdge && topEdge) return 2;
    if (leftEdge && bottomEdge) return 5;
    if (rightEdge && bottomEdge) return 7;
    // needs-more-work: side handles
    return -1;
  }

  /** Paint the handles at the four corners and midway along each edge
   * of the bounding box.  */
  public void paint(Graphics g) {
    int cx = _content.getX();
    int cy = _content.getY();
    int cw = _content.getWidth();
    int ch = _content.getHeight();
    g.setColor(Globals.getPrefs().handleColorFor(_content));
    g.fillRect(cx - HAND_SIZE/2, cy - HAND_SIZE/2, HAND_SIZE, HAND_SIZE);
    g.fillRect(cx + cw - HAND_SIZE/2, cy - HAND_SIZE/2, HAND_SIZE, HAND_SIZE);
    g.fillRect(cx - HAND_SIZE/2, cy + ch - HAND_SIZE/2, HAND_SIZE, HAND_SIZE);
    g.fillRect(cx + cw - HAND_SIZE/2, cy + ch - HAND_SIZE/2, HAND_SIZE, HAND_SIZE);
  }

  /** Change some attribute of the selected Fig when the user drags one of its
   *  handles. Needs-More-Work: someday I might implement resizing that
   *  maintains the aspect ratio. */
  public void dragHandle(int mX, int mY, int anX, int anY, Handle hand) {
    int x = _content.getX(), y = _content.getY();
    int w = _content.getWidth(), h = _content.getHeight();
    int newX = x, newY = y, newW = w, newH = h;
    switch (hand.index) {
    case -1:
      _content.translate(mX + anX, mY + anY);
      return;
    case 0:
      newW = x + w - mX;
      newW = (newW < MIN_SIZE) ? MIN_SIZE : newW;
      newH = y + h - mY;
      newH = (newH < MIN_SIZE) ? MIN_SIZE : newH;
      newX = x + w - newW;
      newY = y + h - newH;
      break;
    case 1: break;
    case 2:
      newW = mX - x;
      newW = (newW < MIN_SIZE) ? MIN_SIZE : newW;
      newH = y + h - mY;
      newH = (newH < MIN_SIZE) ? MIN_SIZE : newH;
      newY = y + h - newH;
      break;
    case 3: break;
    case 4: break;
    case 5:
      newW = x + w - mX;
      newW = (newW < MIN_SIZE) ? MIN_SIZE : newW;
      newH = mY - y;
      newH = (newH < MIN_SIZE) ? MIN_SIZE : newH;
      newX = x + w - newW;
      break;
    case 6: break;
    case 7:
      newW = mX - x;
      newW = (newW < MIN_SIZE) ? MIN_SIZE : newW;
      newH = mY - y;
      newH = (newH < MIN_SIZE) ? MIN_SIZE : newH;
      break;
    default:
      System.out.println("invalid handle number");
      break;
    }
    _content.setBounds(newX, newY, newW, newH);
  }

} /* end class SelectionResize */

