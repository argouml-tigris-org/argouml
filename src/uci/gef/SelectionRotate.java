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


// File: SelectionRotate.java
// Classes: SelectionRotate
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

public class SelectionRotate extends Selection {

  /** The margin between the contents bbox and the frame */
  public static final int BORDER_WIDTH = 4;

  /** Construct a new SelectionRotate around the given DiagramElement */
  public SelectionRotate(Fig f) { super(f); }

  /** Paint the selection. */
  public void paint(Graphics g) {
    int x = _content.getX();
    int y = _content.getY();
    int w = _content.getWidth();
    int h = _content.getHeight();
    g.setColor(Globals.getPrefs().handleColorFor(_content));
    g.fillOval(x - HAND_SIZE, y - HAND_SIZE, HAND_SIZE, HAND_SIZE);
    g.fillOval(x + w, y - HAND_SIZE, HAND_SIZE, HAND_SIZE);
    g.fillOval(x - HAND_SIZE, y + h, HAND_SIZE, HAND_SIZE);
    g.fillOval(x + w, y + h, HAND_SIZE, HAND_SIZE);
  }

  /** SelectionRotate is used when there are no handles, so dragHandle
   * does nothing. Actually, hitHandle always returns -1 , so this
   * method should never even get called. */
  public void dragHandle(int mx, int my, int an_x,int an_y, Handle h) {
    /* do nothing */
  }

  public int hitHandle(Rectangle r) { return -2; }


} /* end class SelectionRotate */

