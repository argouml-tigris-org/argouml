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

// File: ActionAdjustGuide.java
// Classes: ActionAdjustGuide
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.Event;

/** An Action to modify the way that the Guides constrain the mouse
 *  points entered by the user.  This does not change the apperance of
 *  the LayerGrid.  Needs-More-Work: Should put up a grid preference
 *  dialog box or use the property sheet.  */

public class ActionAdjustGuide extends Action {

  public ActionAdjustGuide() { }

  public String name() { return "Adjust Grid Snap"; }

  public void doIt(java.awt.Event e) {
    Editor ce = Globals.curEditor();
    Guide guide = ce.getGuide();
    if (guide != null) {
      guide.adjust();
    }
  }

  public void undoIt() { }

} /* end class ActionAdjustGuide */
