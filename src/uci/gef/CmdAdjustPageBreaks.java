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

// File: CmdAdjustPageBreaks.java
// Classes: CmdAdjustPageBreaks
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.Event;

/** An Cmd to modify the way that the PageBreaks Layer of the
 *  current document looks.  For now it just cycles among a few
 *  predefined looks.  Needs-More-Work: Should put up a PageBreaks
 *  preference dialog box or use the property sheet.  */
public class CmdAdjustPageBreaks extends Cmd {

  /** Construct a new CmdAdjustPageBreaks */
  public CmdAdjustPageBreaks() {
    super("Adjust PageBreaks");
  }

  public void doIt() {
    Editor ce = Globals.curEditor();
    Layer pageBreaks = (Layer)ce.getLayerManager().findLayerNamed("PageBreaks");
    if (pageBreaks != null) {
      pageBreaks.adjust();
    }
  }

  public void undoIt() { }

} /* end class CmdAdjustPageBreaks */
