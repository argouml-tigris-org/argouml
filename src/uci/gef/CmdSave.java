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


// File: CmdSave.java
// Classes: CmdSave
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;
import java.io.*;



/** Cmd to save the current document to a binary file using Sun's
 *  ObjectSerialization library availible from <a href =
 *  "http://chatsubo.javasoft.com/current/serial/-1.html">
 *  java.sun.com</a>. The written file contains the Editor object and
 *  all objects reachable through instance variables of the Editor
 *  (e.g., the selections, the views, the contents of the views, the
 *  net-level description of the graph, etc.).  UI objects such as
 *  Windows, Frames, Panels, and Images are not stored because I have
 *  marked those instance variables as transient in the source
 *  code. <p>
 *
 *  One advantage of this approach to saving and loading is that
 *  developers using GEF can add subclasses (e.g., to NetNode) which
 *  introduce new instance variables, and those will be saved and
 *  loaded without the developers having to special load and save
 *  methods. However, make sure that you do not point to any AWT
 *  objects unless those instance variables are transient because those
 *  cannot be saved. <p>
 *
 *  Needs-More-Work: the files produced by a save are not really good
 *  for anything other than reloading into this tool, or another Java
 *  program that uses ObjectSerialization. At this time GEF provides no
 *  support for saving or loading textual representations of documents
 *  that could be used in other tools.<p>
 *  <A HREF="../features.html#load_and_save">
 *  <TT>FEATURE: load_and_save</TT></A>
 *  <A HREF="../features.html#cross_development_environments">
 *  <TT>FEATURE: cross_development_environments</TT></A>
 *  <A HREF="../bugs.html#save_memory_hog">
 *  <FONT COLOR=660000><B>BUG: save_memory_hog</B></FONT></A>
 *  <A HREF="../bugs.html#saved_file_versioning">
 *  <FONT COLOR=660000><B>BUG: saved_file_versioning</B></FONT></A>
 *  <A HREF="../bugs.html#save_under_jdk1_0_2">
 *  <FONT COLOR=660000><B>BUG: save_under_jdk1_0_2</B></FONT></A>
 *
 * @see CmdOpen */

public class CmdSave extends Cmd implements FilenameFilter {

  public CmdSave() { super("Save..."); }

  /** Only allow the user to select files that match the fiven
   *  filename pattern. Needs-More-Work: this is not used yet. */
  public CmdSave(String filterPattern) {
    this();
    setArg("filterPattern", filterPattern);
  }

  public void doIt() {
    try {
      Editor ce = Globals.curEditor();
      FileDialog fd = new
     	FileDialog(ce.findFrame(), "Save Diagram", FileDialog.SAVE);
      fd.setFilenameFilter(this);
      fd.setDirectory(".");
      fd.show();
      String filename = fd.getFile(); // blocking
      String path = fd.getDirectory(); // blocking
      if (filename != null) {
     	Globals.showStatus("Writing " + path + filename + "...");
     	FileOutputStream f = new FileOutputStream(path + filename);
     	ObjectOutput s = new ObjectOutputStream(f);
    	System.out.println("Cmd save...");
    	s.writeObject(ce);
    	System.out.println("save done");
    	Globals.showStatus("Wrote " + path + filename);
    	f.close();
	//ce.setTitle(filename);
      }
    }
    catch (FileNotFoundException ignore) {
      System.out.println("got an FileNotFoundException");
    }
    //    catch (java.lang.ClassMismatchException ignore) {
    //      System.out.println("got an ClassMismatchException");
    //    }
    catch (IOException ignore) {
      System.out.println("got an IOException");
      ignore.printStackTrace();

    }
  }

  /** Only let the user select files that match the filter. This does
   *  not seem to be called under JDK 1.0.2 on solaris. I have not
   *  finished this method, it currently accepts all filenames. <p>
   *
   *  Needs-More-Work: the source code for this method is duplicated in
   *  CmdOpen#accept.  */
  public boolean accept(File dir, String name) {
    System.out.println("checking: "+ dir + " " + name);
    if (containsArg("filterPattern")) {
      // if pattern dosen't match, return false
      return true;
    }
    return true; // no pattern was specified
  }

  public void undoIt() {
    System.out.println("Undo does not make sense for CmdSave");
  }

} /* end class CmdSave */

