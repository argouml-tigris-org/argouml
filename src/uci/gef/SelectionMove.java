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




// File: SelectionMove.java
// Classes: SelectionMove
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** Selection object that allows the user to move the selected Fig,
 *  but not to resize it. */

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
    super.paint(g);
  }

  /** SelectionMove is used when there are no handles, so dragHandle
   * does nothing. Actually, hitHandle always returns -1 , so this
   * method should never even get called. */
  public void dragHandle(int mx, int my, int an_x,int an_y, Handle h) {
    /* do nothing */
  }

  /** Return -1 as a special code to indicate that the user clicked in
   *  the body of the Fig and wants to drag it around. */
  public void hitHandle(Rectangle r, Handle h) {
    h.index = -1;
    h.instructions = "Move Object(s)";
  }


  static final long serialVersionUID = -3988412251995936654L;
} /* end class SelectionMove */

