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
 * @see uci.gef.JGraphFrame */

public class CmdOpenWindow extends Cmd {

  public CmdOpenWindow(String className, String name) {
    super(name, NO_ICON);
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

  static final long serialVersionUID = -4053109533615914080L;

} /* end class CmdOpenWindow */
