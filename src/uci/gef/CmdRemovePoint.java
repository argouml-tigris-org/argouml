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




// File: CmdRemovePoint.java
// Classes: CmdRemovePoint
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.Event;

/** Cmd to remove the selected (last manipulated) point from a FigPoly.
 *
 * @see FigPoly
 */

public class CmdRemovePoint extends Cmd {

  protected int _selectedHandle = 0;

  public CmdRemovePoint() { super("Remove Point From Polygon", NO_ICON); }

  public CmdRemovePoint(int i) { this(); _selectedHandle = i; }

  /** Ask the current editor's selected Fig to remove its point. */
  public void doIt() {
    Fig f = null;
    Selection sel = null;
    Editor ce = Globals.curEditor();
    SelectionManager sm = ce.getSelectionManager();
    if (sm.getLocked()) {
      Globals.showStatus("Cannot Modify Locked Objects");
      return;
    }

    if (sm.selections().isEmpty()) return;
    sel = (Selection) sm.selections().firstElement();
    f = (Fig) sel.getContent();
    f.startTrans();
    f.removePoint(_selectedHandle);
    f.endTrans();
  }

  public void undoIt() {
    System.out.println("this operation currently cannot be undone");
  }

  static final long serialVersionUID = 1761687061656172699L;

} /* end class CmdRemovePoint */
