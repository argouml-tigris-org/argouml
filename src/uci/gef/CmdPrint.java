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

  public static int OVERLAP = 0;

  public CmdPrint() { super("Print..."); }

  public CmdPrint(String diagramName) {
    this();
    setDiagramName(diagramName);
  }

  public void setDiagramName(String name) {
    setArg("diagramName", name);
  }

  public void setPrintPageNumbers(boolean b) {
    setArg("printPageNumbers", b ? Boolean.TRUE : Boolean.FALSE);
  }

  public void doIt() {
    Editor ce = Globals.curEditor();
    String diagramName = (String) getArg("diagramName");
    Boolean printPageNums = (Boolean) getArg("printPageNumbers");
    boolean printNumbers = true;
    if (printPageNums != null)
      printNumbers = printPageNums.booleanValue();

    String jobName = "Print Diagram";
    if (diagramName != null) jobName = diagramName;

    Toolkit tk = Toolkit.getDefaultToolkit();
    Frame someFrame = Globals.someFrame();
    PrintJob pjob = tk.getPrintJob(someFrame, jobName, new Properties());
    if (pjob != null) {
      Graphics pg = pjob.getGraphics();
      Dimension d = pjob.getPageDimension();

      int leftMargin = 15;
      int topMargin = 15;
      int rightMargin = 15;
      int bottomMargin = 40;
      int footer = 20;
      int printableWidth = d.width - leftMargin - rightMargin;
      int printableHeight = d.height - topMargin - bottomMargin - footer;
      //System.out.println("pjob.getPageDimension() = " + d);
      //Globals.showStatus("page size is: " + d);

      // For the printable area, tha actual origen of Argo is (11,12),
      // and the printable area from Argo is width = 586, and height = 769.  
      // This was done on a 300 dpi printer.  The origen was translated
      // by a value of 15 to provide a bit of a buffer for different printers.
      Fig f = null;
      Rectangle rectSize = null;
      Rectangle drawingArea = new Rectangle(0,0);
      Enumeration enum = ce.figs();
      int count = 0;
      while (enum.hasMoreElements()) { 
	f = (Fig) enum.nextElement();
	rectSize = f.getBounds();
	drawingArea.add(rectSize);
      }
      int pageNum = 1;
      for (int y=0; y <= drawingArea.height; y+=printableHeight-OVERLAP) {
	for (int x=0; x <= drawingArea.width; x+=printableWidth-OVERLAP) {
	  if (pg == null) { pg = pjob.getGraphics(); pageNum++; }
	  Globals.showStatus("Printing page " + pageNum);
	  pg.setClip(0, 0, d.width, d.height);
	  pg.clipRect(leftMargin, topMargin,
		      printableWidth, printableHeight);
	  pg.translate(-x + rightMargin, -y + topMargin);
	  ce.print(pg);
	  //System.out.println("x="+x+", y=" + y);
	  pg.setClip(-30000, -30000, 60000, 60000);
	  if (diagramName != null) {
	    pg.setFont(new Font("TimesRoman", Font.PLAIN, 9));
	    pg.setColor(Color.black);
	    pg.drawString(diagramName,
			  x + 10, y + printableHeight + footer);
	  }
	  if (printNumbers) {
	    pg.setFont(new Font("TimesRoman", Font.PLAIN, 9));
	    pg.setColor(Color.black);
	    pg.drawString("Page " + pageNum,
			  x + printableWidth - 40,
			  y + printableHeight + footer);
	  }
	  pg.dispose(); // flush page
	  pg = null;
	}
      }
      pjob.end();
    }
    Globals.showStatus("Printing finished");
  }

  public void undoIt() {
    System.out.println("Undo does not make sense for CmdPrint");
  }

  static final long serialVersionUID = -2426429729992407570L;

} /* end class CmdPrint */

