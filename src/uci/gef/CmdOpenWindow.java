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

// File: CmdOpenWindow.java
// Classes: CmdOpenWindow
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;

/** Cmd to open a user interface dialog window. Given the name of a
 *  subclass of Frame, this Cmd makes a new instance and calls
 *  show().  For example, used to open a list of some availible
 *  commands.
 *
 * @see uci.gef.MenuManager */

public class CmdOpenWindow extends Cmd {

  public CmdOpenWindow(String className, String name) {
    super(name);
    setArg("className", className);
  }

  public void doIt() {
    String className = (String) getArg("className");
    Frame window;
    Class clazz;
    if (className != null) {
      Globals.showStatus("Opening window for " + className);
      try { clazz = Class.forName(className); }
      catch (java.lang.ClassNotFoundException ignore) { return; }

      try { window = (Frame) clazz.newInstance(); }
      catch (java.lang.IllegalAccessException ignore) { return; }
      catch (java.lang.InstantiationException ignore) { return; }
      window.show();
      return;
    }
    System.out.println("invalid window name");
  }

  public void undoIt() {
    System.out.println("undo CmdOpenWindow is not supported");
  }

} /* end class CmdOpenWindow */
