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
// THIS SOFTWARE IS PROVIDED `AS IS' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: CmdPrint.java
// Classes: CmdPrint
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;
import java.io.*;



/** Cmd to Print a diagram.  Only works under JDK 1.1. <p>
 *  <A HREF="../features.html#printing">
 *  <TT>FEATURE: printing</TT></A>
 *  <A HREF="../features.html#cross_development_environments">
 *  <TT>FEATURE: cross_development_environments</TT></A>
 */

public class CmdPrint extends Cmd {

  public CmdPrint() { super("Print..."); }

  public void doIt() {

     Editor ce = Globals.curEditor();
     Toolkit tk = Toolkit.getDefaultToolkit();
     Frame someFrame = Globals.someFrame();
     PrintJob pjob = tk.getPrintJob(someFrame, "Printing Test",
				    new Properties());
     if (pjob != null) {
       Graphics pg = pjob.getGraphics();
       Dimension d = pjob.getPageDimension();
       System.out.println("page size is: " + d);
       // Needs-More-Work: multipage printing
       pg.clipRect(0, 0, d.width, d.height);
       if (pg != null) {
     	ce.print(pg);
     	pg.dispose(); // flush page
       }
       pjob.end();
     }
     System.out.println("Printing finished");

  }

  public void undoIt() {
    System.out.println("Undo does not make sense for CmdPrint");
  }

} /* end class CmdPrint */

