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

// File: ActionLoad.java
// Classes: ActionLoad
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;
import java.io.*;




/** Action to Load a previously saved document document. 
 *  <A HREF="../features.html#load_and_save">
 *  <TT>FEATURE: load_and_save</TT></A>
 *  <A HREF="../features.html#cross_development_environments">
 *  <TT>FEATURE: cross_development_environments</TT></A>
 *  <A HREF="../bugs.html#save_memory_hog">
 *  <FONT COLOR=660000><B>BUG: save_memory_hog</B></FONT></A>
 *  <A HREF="../bugs.html#saved_file_versioning">
 *  <FONT COLOR=660000><B>BUG: saved_file_versioning</B></FONT></A>
 *
 * @see ActionSave
 */

public class ActionLoad extends Action implements FilenameFilter {

  public ActionLoad() { }

  /** Only allow the user to select files that match the fiven
   *  filename pattern.  Needs-More-Work: This is not used yet. */
  public ActionLoad(String filterPattern) {
    setArg("filterPattern", filterPattern);
  }

  public String name() { return "Load Document"; }

  public void doIt(Event e) {
  try {
      Editor ce = Globals.curEditor();
      FileDialog fd = new
     	FileDialog(ce.frame(), "Load Diagram", FileDialog.LOAD);
      fd.setFilenameFilter(this);
      fd.setDirectory(".");
     fd.show();
     String filename = fd.getFile(); // blocking
     String path = fd.getDirectory(); // blocking
     if (filename != null) {
    	Globals.showStatus("Reading " + path + filename + "...");
    	FileInputStream f = new FileInputStream(path + filename);
    	ObjectInput s = new ObjectInputStream(f);
    	System.out.println("action load...");
    	Editor ed = (Editor)s.readObject();
    	System.out.println("load done, showing editor");
	System.out.println(ed.toString());
	System.out.println(ed.getLayerManager().toString());
    	Globals.showStatus("Read " + path + filename);
    	ed.show();
	ed.setTitle(filename);
    	f.close();
     }
    }
    catch (FileNotFoundException ignore) {
      System.out.println("got an FileNotFoundException");
     }
    //    catch (ClassMismatchException ignore) {
    //      System.out.println("got an ClassMismatchException");
    //     }
    catch (java.lang.ClassNotFoundException ignore) {
      System.out.println("got an ClassNotFoundException");
     }
    catch (IOException ignore) {
      System.out.println("got an IOException");
    }
  }

  /** Only let the user select files that match the filter. This does
   * not seem to be called under JDK 1.0.2 on solaris. I have not
   * finished this method, it currently accepts all filenames. <p>
   *
   * Needs-More-Work: The source code for this function is duplicated
   * in ActionSave#accept.  */
  public boolean accept(File dir, String name) {
    System.out.println("checking: "+ dir + " " + name);
    if (containsArg("filterPattern")) {
      // if pattern dosen't match, return false
      return true;
    }
    return true; // no pattern was specified
  }

  public void undoIt() {
    System.out.println("Undo does not make sense for ActionLoad");
  }

} /* end class ActionLoad */

