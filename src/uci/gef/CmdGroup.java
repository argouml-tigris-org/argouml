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




// File: CmdGroup.java
// Classes: CmdGroup
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;

/** Cmd to group all the Fig's selected in the current
 *  editor into a single FigGroup. 
 *
 * @see FigGroup
 * @see CmdUngroup  */

public class CmdGroup extends Cmd {

  public CmdGroup() { super("Group", NO_ICON); }

  public void doIt() {
    Editor ce = Globals.curEditor();
    Vector selectedFigs = ce.getSelectionManager().getFigs();
    FigGroup _newItem = new FigGroup();
    Enumeration eachDE = selectedFigs.elements();
    while (eachDE.hasMoreElements()) {
      Object o = eachDE.nextElement();
      if (o instanceof Fig) {
	Fig f = (Fig) o;
	_newItem.addFig(f);
      }
    }
    eachDE = selectedFigs.elements();
    while (eachDE.hasMoreElements()) {
      Object o = eachDE.nextElement();
      if (o instanceof Fig) {
	Fig f = (Fig) o;
	ce.remove(f);
      }
    }
    ce.add(_newItem);
    ce.getSelectionManager().deselectAll();
    ce.getSelectionManager().select(_newItem);
  }

  public void undoIt() { System.out.println("not done yet"); }

  static final long serialVersionUID = -348801435790084205L;

} /* end class CmdGroup */

