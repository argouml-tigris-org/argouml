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

