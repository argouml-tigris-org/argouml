// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
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
  public static final int V_SPACING = 2;
  public static final int V_CENTERS = 3;

  ////////////////////////////////////////////////////////////////
  // instanve variables

  /** Specification of the type of distribution requested */
  protected int _request;

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
    case H_SPACING: return "Horizontal Spacing";
    case H_CENTERS: return "Horizontal Centers";
    case V_SPACING: return "Vertical Spacing";
    case V_CENTERS: return "Vertical Centers";
    }
    return "";
  }
  ////////////////////////////////////////////////////////////////
  // Cmd API

  public void doIt() {
    Editor ce = Globals.curEditor();
    Vector figs = (Vector) getArg("figs");
    if (figs == null) {
      SelectionManager sm = ce.getSelectionManager();
      if (sm.getLocked()) {
	Globals.showStatus("Cannot Modify Locked Objects");
	return;
      }
      figs = sm.getFigs();
    }
    int size = figs.size();
    if (size <= 2) return;
    Fig f = (Fig) figs.elementAt(0);
    Rectangle bbox = f.getBounds();
    int leftMostCenter = bbox.x + bbox.width / 2;
    int rightMostCenter = bbox.x + bbox.width / 2;
    int topMostCenter = bbox.y + bbox.height / 2;
    int bottomMostCenter = bbox.y + bbox.height / 2;
    int totalWidth = f.getWidth(), totalHeight = f.getHeight();
    for (int i = 1; i < size; i++) {
      f = (Fig) figs.elementAt(i);
      Rectangle r = f.getBounds();
      bbox.add(r);
      leftMostCenter = Math.min(leftMostCenter, r.x + r.width / 2);
      rightMostCenter = Math.max(rightMostCenter, r.x + r.width / 2);
      topMostCenter = Math.min(topMostCenter, r.y + r.height / 2);
      bottomMostCenter = Math.max(bottomMostCenter, r.y + r.height / 2);
      totalWidth += f.getWidth();
      totalHeight += f.getHeight();
    }

    int gap = 0;
    int oncenter = 0;
    int xNext = 0;
    int yNext = 0;    
    
    switch (_request) {
    case H_SPACING:
      xNext = bbox.x;
      gap = (bbox.width - totalWidth) / (size - 1);
      break;
    case H_CENTERS:
      xNext = leftMostCenter;
      oncenter = (rightMostCenter - leftMostCenter) / (size - 1);
      break;
    case V_SPACING:
      yNext = bbox.y;
      gap = (bbox.height - totalHeight) / (size - 1);
      break;
    case V_CENTERS:
      yNext = topMostCenter;
      oncenter = (bottomMostCenter - topMostCenter) / (size - 1);
      break;
    }

    //sort top-to-bottom or left-to-rigth!
    for (int i = 0; i < size; i++) {
      for (int j = i + 1; j < size; j++) {
	Fig fi = (Fig) figs.elementAt(i);
	Fig fj = (Fig) figs.elementAt(j);
	if (_request == H_SPACING || _request == H_CENTERS) {
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
	f.setLocation(xNext, f.getY());
	xNext += f.getWidth() + gap;
	break;
      case H_CENTERS:
	f.setLocation(xNext - f.getWidth() / 2, f.getY());
	xNext += oncenter;
	break;
      case V_SPACING:
	f.setLocation(f.getX(), yNext);
	yNext += f.getHeight() + gap;
	break;
      case V_CENTERS:
	f.setLocation(f.getX(), yNext - f.getHeight() / 2);
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
} /* end class CmdDistribute */

