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

// File: CmdUngroup.java
// Classes: CmdUngroup
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** Cmd to ungroup a selected group object.
 *  <A HREF="../features.html#basic_shapes_group">
 *  <TT>FEATURE: basic_shapes_group</TT></A>
 *
 * @see CmdGroup
 * @see FigGroup */

public class CmdUngroup extends Cmd {

  public CmdUngroup() { super("Ungroup"); }

  public void doIt() {
    Vector ungroupedItems = new Vector();
    Editor ce = Globals.curEditor();
    Vector selectedFigs = ce.getSelectionManager().getFigs();
    Enumeration eachDE = selectedFigs.elements();
    while (eachDE.hasMoreElements()) {
      Object o = eachDE.nextElement();
      if (o instanceof FigGroup) {
	FigGroup fg = (FigGroup) o;
	Enumeration eachFig = fg.elements();//?
	while (eachFig.hasMoreElements()) {
	  Fig f = (Fig) eachFig.nextElement();
	  ce.add(f);
	  ungroupedItems.addElement(f);
	}
	ce.remove(fg);
      }
    } /* end while each selected object */
    ce.getSelectionManager().deselectAll();
    ce.getSelectionManager().select(ungroupedItems);
  }

  public void undoIt() { System.out.println("not implemented yet"); }

} /* end class CmdUngroup */

