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



// File: CmdDistribute.java
// Classes: CmdDistribute
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;

/** An Cmd to align 2 or more objects relative to each other. */

public class CmdDistribute extends Cmd {

  ////////////////////////////////////////////////////////////////
  // constants

  /** Constants specifying the type of distribution requested. */
  public static final int H_SPACING = 0;
  public static final int H_CENTERS = 1;
  public static final int H_PACK    = 2;
  public static final int V_SPACING = 4;
  public static final int V_CENTERS = 5;
  public static final int V_PACK    = 6;

  ////////////////////////////////////////////////////////////////
  // instanve variables

  /** Specification of the type of distribution requested */
  protected int _request;
  protected Rectangle _bbox = null;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new CmdDistribute.
   *
   * @param dir The desired alignment direction, one of the constants
   * listed above. */
  public CmdDistribute(int r) {
    super("Distribute " + wordFor(r));
    _request = r;
  }

  protected static String wordFor(int r) {
    switch (r) {
    case H_SPACING:    return "Horizontal Spacing";
    case H_CENTERS:    return "Horizontal Centers";
    case H_PACK:       return "Leftward";
    case V_SPACING:    return "Vertical Spacing";
    case V_CENTERS:    return "Vertical Centers";
    case V_PACK:       return "Upward";
    }
    return "";
  }
  ////////////////////////////////////////////////////////////////
  // Cmd API

  public void doIt() {
    Editor ce = Globals.curEditor();
    Vector figs = (Vector) getArg("figs");
    Integer packGapInt = (Integer) getArg("gap");
    int packGap = 8;
    if (packGapInt != null) packGap = packGapInt.intValue();
    _bbox = (Rectangle) getArg("bbox");
    if (figs == null) {
      SelectionManager sm = ce.getSelectionManager();
      if (sm.getLocked()) {
	Globals.showStatus("Cannot Modify Locked Objects");
	return;
      }
      figs = sm.getFigs();
    }
    int leftMostCenter = 0, rightMostCenter = 0;
    int topMostCenter = 0, bottomMostCenter = 0;
    int size = figs.size();
    if (size == 0) return;

    // find the bbox of all selected objects
    Fig f = (Fig) figs.elementAt(0);
    if (_bbox == null) {
      _bbox = f.getBounds();
      leftMostCenter = _bbox.x + _bbox.width / 2;
      rightMostCenter = _bbox.x + _bbox.width / 2;
      topMostCenter = _bbox.y + _bbox.height / 2;
      bottomMostCenter = _bbox.y + _bbox.height / 2;
      for (int i = 1; i < size; i++) {
	f = (Fig) figs.elementAt(i);
	Rectangle r = f.getBounds();
	_bbox.add(r);
	leftMostCenter = Math.min(leftMostCenter, r.x + r.width / 2);
	rightMostCenter = Math.max(rightMostCenter, r.x + r.width / 2);
	topMostCenter = Math.min(topMostCenter, r.y + r.height / 2);
	bottomMostCenter = Math.max(bottomMostCenter, r.y + r.height / 2);
      }
    }

    // find the sum of the widths and heights of all selected objects
    int totalWidth = 0, totalHeight = 0;
    for (int i = 0; i < size; i++) {
      f = (Fig) figs.elementAt(i);
      totalWidth += f.getWidth();
      totalHeight += f.getHeight();
    }

    float gap = 0, oncenter = 0;
    float xNext = 0, yNext = 0;

    switch (_request) {
    case H_SPACING:
      xNext = _bbox.x;
      gap = (_bbox.width - totalWidth) / Math.max(size - 1, 1);
      break;
    case H_CENTERS:
      xNext = leftMostCenter;
      oncenter = (rightMostCenter - leftMostCenter) / Math.max(size - 1, 1);
      break;
    case H_PACK:
      xNext = _bbox.x;
      gap = packGap;
      break;
    case V_SPACING:
      yNext = _bbox.y;
      gap = (_bbox.height - totalHeight) / Math.max(size - 1, 1);
      break;
    case V_CENTERS:
      yNext = topMostCenter;
      oncenter = (bottomMostCenter - topMostCenter) / Math.max(size - 1, 1);
      break;
    case V_PACK:
      yNext = _bbox.y;
      gap = packGap;
      break;
    }

    //sort top-to-bottom or left-to-right, this maintains visual order
    //when we set the coordinates
    for (int i = 0; i < size; i++) {
      for (int j = i + 1; j < size; j++) {
	Fig fi = (Fig) figs.elementAt(i);
	Fig fj = (Fig) figs.elementAt(j);
	if (_request == H_SPACING || _request == H_CENTERS ||
	    _request == H_PACK) {
	  if (fi.getX() > fj.getX()) swap(figs, i, j);
	}
	else
	  if (fi.getY() > fj.getY()) swap(figs, i, j);
      }
    }


    for (int i = 0; i < size; i++) {
      f = (Fig) figs.elementAt(i);
      f.startTrans();
      switch (_request) {
      case H_SPACING:
      case H_PACK:
	f.setLocation((int) xNext, f.getY());
	xNext += f.getWidth() + gap;
	break;
      case H_CENTERS:
	f.setLocation((int) xNext - f.getWidth() / 2, f.getY());
	xNext += oncenter;
	break;
      case V_SPACING:
      case V_PACK:
	f.setLocation(f.getX(), (int) yNext);
	yNext += f.getHeight() + gap;
	break;
      case V_CENTERS:
	f.setLocation(f.getX(), (int) yNext - f.getHeight() / 2);
	yNext += oncenter;
	break;
      }
      f.endTrans();
    }
  }

  public void undoIt() { }


  protected void swap(Vector v, int i, int j) {
    Object temp = v.elementAt(i);
    v.setElementAt(v.elementAt(j), i);
    v.setElementAt(temp, j);
  }

  public Rectangle getLastBBox() { return _bbox; }

    static final long serialVersionUID = 1344866391413571335L;

} /* end class CmdDistribute */

