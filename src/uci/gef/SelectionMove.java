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

// File: SelectionMove.java
// Classes: SelectionMove
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** Selection object for DiagramElement's that do not have handles. A
 *  colorded rectangular frame is drawn just outside the bounding box
 *  of the DiagramElement. The box has things that look like handles on
 *  its four corners so that it looks more like a selection and less
 *  like the NetNode highlighting.  However, these fake handles
 *  cannot be dragged to resize the DiagramElement.
 *  <A HREF="../features.html#selections_box">
 *  <TT>FEATURE: selections_box</TT></A>
 *
 * @see FigText
 * @see FigNode */

public class SelectionMove extends Selection {

  /** The margin between the contents bbox and the frame */
  public static final int BORDER_WIDTH = 4;

  /** Construct a new SelectionMove around the given DiagramElement */
  public SelectionMove(Fig f) { super(f); }

  /** Paint the selection. */
  public void paint(Graphics g) {
    int x = _content.getX();
    int y = _content.getY();
    int w = _content.getWidth();
    int h = _content.getHeight();
    g.setColor(Globals.getPrefs().handleColorFor(_content));
    g.drawRect(x - BORDER_WIDTH, y - BORDER_WIDTH,
	       w + BORDER_WIDTH * 2 - 1, h + BORDER_WIDTH * 2 - 1);
    g.drawRect(x - BORDER_WIDTH - 1, y - BORDER_WIDTH - 1,
	       w + BORDER_WIDTH * 2 + 2 - 1, h + BORDER_WIDTH * 2 + 2 - 1);
    g.fillRect(x - HAND_SIZE, y - HAND_SIZE, HAND_SIZE, HAND_SIZE);
    g.fillRect(x + w, y - HAND_SIZE, HAND_SIZE, HAND_SIZE);
    g.fillRect(x - HAND_SIZE, y + h, HAND_SIZE, HAND_SIZE);
    g.fillRect(x + w, y + h, HAND_SIZE, HAND_SIZE);
  }

  /** SelectionMove is used when there are no handles, so dragHandle
   * does nothing. Actually, hitHandle always returns -1 , so this
   * method should never even get called. */
  public void dragHandle(int mx, int my, int an_x,int an_y, Handle h) {
    /* do nothing */
  }

  public int hitHandle(Rectangle r) { return -1; }


} /* end class SelectionMove */

