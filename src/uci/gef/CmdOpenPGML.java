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

// File: CmdOpen.java
// Classes: CmdOpen
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.net.*;

import uci.util.*;
import uci.xml.pgml.*;

/** Cmd to Load a previously saved document document. The loaded
 *  editor is displayed in a new JGraphFrame.
 *
 * @see CmdSave */

public class CmdOpenPGML extends Cmd implements FilenameFilter {

  public CmdOpenPGML() {
    super("Open PGML...", NO_ICON);
    setArg("filterPattern", "*.pgml");
  }

  public void doIt() {
      Editor ce = Globals.curEditor();
      FileDialog fd =
	new FileDialog(ce.findFrame(), "Open...", FileDialog.LOAD);
      fd.setFilenameFilter(this);
      fd.setDirectory(Globals.getLastDirectory());
      fd.show();
      String filename = fd.getFile(); // blocking
      String path = fd.getDirectory(); // blocking
      Globals.setLastDirectory(path);

      if (filename != null) {
	try {
	  Globals.showStatus("Reading " + path + filename + "...");
	  URL url = Util.fileToURL(new File(path + filename));
	  Diagram diag = PGMLParser.SINGLETON.readDiagram(url);
	  Editor ed = new Editor(diag);
	  //System.out.println("load done, showing editor");
	  //System.out.println(ed.toString());
	  //System.out.println(ed.getLayerManager().toString());
	  Globals.showStatus("Read " + path + filename);
	  JGraphFrame jgf = new JGraphFrame(path + filename, ed);
	  Object d = getArg("dimension");
	  if (d instanceof Dimension) jgf.setSize((Dimension)d);
	  jgf.setVisible(true);
	}
	catch (MalformedURLException murle) { System.out.println("bad URL"); }
	catch (IOException e) { System.out.println("IOExcept in openpgml"); }
      }
  }

  /** Only let the user select files that match the filter. This does
   * not seem to be called under JDK 1.0.2 on solaris. I have not
   * finished this method, it currently accepts all filenames. <p>
   *
   * Needs-More-Work: The source code for this function is duplicated
   * in CmdSave#accept.  */
  public boolean accept(File dir, String name) {
    System.out.println("checking: "+ dir + " " + name);
    if (containsArg("filterPattern")) {
      // if pattern dosen't match, return false
      return true;
    }
    return true; // no pattern was specified
  }

  public void undoIt() {
    System.out.println("Undo does not make sense for CmdOpen");
  }

  
  static final long serialVersionUID = 00000000000000L;

} /* end class CmdOpenPGML */
