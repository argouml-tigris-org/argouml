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

// File: CmdGroup.java
// Classes: CmdGroup
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;

/** Cmd to group all the Fig's selected in the current
 *  editor into a single FigGroup. Needs-More-Work: should use
 *  composite DiagramElement, not composite Fig.  Needs-More-Work:
 *  DiagramElement's other than Figs are ignored.
 *  <A HREF="../features.html#basic_shapes_group">
 *  <TT>FEATURE: basic_shapes_group</TT></A>
 *
 * @see FigGroup
 * @see CmdUngroup  */

public class CmdGroup extends Cmd {

  public CmdGroup() { super("Group"); }

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

} /* end class CmdGroup */

