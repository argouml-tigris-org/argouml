// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.




// File: CmdPrint.java
// Classes: CmdPrint
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;
import java.io.*;



/** Cmd to Print a diagram.  Only works under JDK 1.1. */

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
       Globals.showStatus("page size is: " + d);
       // Needs-More-Work: multipage printing
       pg.clipRect(0, 0, d.width, d.height);
       if (pg != null) {
     	ce.print(pg);
     	pg.dispose(); // flush page
       }
       pjob.end();
     }
     Globals.showStatus("Printing finished");

  }

  public void undoIt() {
    System.out.println("Undo does not make sense for CmdPrint");
  }

} /* end class CmdPrint */

