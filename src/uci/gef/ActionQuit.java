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

// File: ActionQuit.java
// Classes: ActionQuit
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;

/** Action to close all windows and exit the application.
 *  Needs-More-Work: Needs to keep track of all open windows. */

public class ActionQuit extends Action {

  public ActionQuit() { }

  public String name() { return "Quit Application"; }

  public void doIt(java.awt.Event e) {
    Globals.quit();
  }

  public void undoIt() {
    System.out.println("Undo does not make sense for ActionQuit");
  }

} /* end class ActionQuit */
