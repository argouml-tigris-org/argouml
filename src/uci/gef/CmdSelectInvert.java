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




// File: CmdSelectInvert.java
// Classes: CmdSelectInvert
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;

/** Cmd to select all the Figs in the editor's current
 *  view that were not previously selected.
 *
 */

public class CmdSelectInvert extends Cmd {

  public CmdSelectInvert() { super("Invert Selection", NO_ICON); }

  public void doIt() {
    Editor ce = Globals.curEditor();
    Vector selected = ce.getSelectionManager().getFigs();
    Vector diagramContents = ce.getLayerManager().getContents();
    Vector inverse = new Vector(diagramContents.size());
    Enumeration contEnum = diagramContents.elements();
    while (contEnum.hasMoreElements()) {
      Object dc = contEnum.nextElement();
      if (!selected.contains(dc)) inverse.addElement(dc);
    }
    ce.getSelectionManager().select(inverse);
  }

  public void undoIt() {
    System.out.println("Undo does not make sense for CmdSelectInvert");
  }

  static final long serialVersionUID = 6936776969114546088L;

} /* end class CmdSelectInvert */

