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



// File: CmdAlign.java
// Classes: CmdAlign
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;

/** An Cmd to align 2 or more objects relative to each other. */

public class CmdAlign extends Cmd {

  ////////////////////////////////////////////////////////////////
  // constants

  /** Constants specifying the type of alignment requested. */
  public static final int ALIGN_TOPS = 0;
  public static final int ALIGN_BOTTOMS = 1;
  public static final int ALIGN_LEFTS = 2;
  public static final int ALIGN_RIGHTS = 3;

  public static final int ALIGN_CENTERS = 4;
  public static final int ALIGN_H_CENTERS = 5;
  public static final int ALIGN_V_CENTERS = 6;

  public static final int ALIGN_TO_GRID = 7;

  ////////////////////////////////////////////////////////////////
  // instanve variables

  /** Specification of the type of alignment requested */
  protected int direction;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new CmdAlign.
   *
   * @param dir The desired alignment direction, one of the constants
   * listed above. */
  public CmdAlign(int dir) {
    super("Align " + wordFor(dir)); //needs-more-work: direction
    direction = dir;
  }

  protected static String wordFor(int d) {
    switch (d) {
    case ALIGN_TOPS: return "Tops";
    case ALIGN_BOTTOMS: return "Bottoms";
    case ALIGN_LEFTS: return "Lefts";
    case ALIGN_RIGHTS: return "Rights";

    case ALIGN_CENTERS: return "Centers";
    case ALIGN_H_CENTERS: return "Horizontal Centers";
    case ALIGN_V_CENTERS: return "Vertical Centers";

    case ALIGN_TO_GRID: return "To Grid";
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
    if (size == 0) return;
    Rectangle bbox = ((Fig) figs.elementAt(0)).getBounds();
    for (int i = 1; i < size; i++)
      bbox.add(((Fig) figs.elementAt(i)).getBounds());

    for (int i = 0; i < size; i++) {
      Fig f = (Fig) figs.elementAt(i);
      f.startTrans();
      f.align(bbox, direction, ce);
      f.endTrans();
    }
  }

  public void undoIt() { }

  static final long serialVersionUID = -2725163107655680773L;

} /* end class CmdAlign */

