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



// File: CmdSpawn.java
// Classes: CmdSpawn
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.awt.*;

/** Cmd to open a new editor on the same document as in the current
 *  editor.  Works by making a new JGraphFrame with a clone of the
 *  current editor. The argument "dimension" may be set to th desired
 *  size of the new window.
 *
 * @see Editor
 * @see JGraphFrame */

public class CmdSpawn extends Cmd {

  public CmdSpawn() { super("Spawn Editor", NO_ICON); }

  public void doIt() {
    Editor ce = Globals.curEditor();
    Editor ed = (Editor) ce.clone();
    String title = (String) getArg("title", "new window");
    JGraphFrame jgf = new JGraphFrame(title, ed);
    // use clone because ce may be of a subclass of Editor
    Object d = getArg("dimension");
    if (d instanceof Dimension) jgf.setSize((Dimension)d);
    jgf.show();
  }

  public void undoIt() { System.out.println("Cannot undo CmdSpawn"); }

  static final long serialVersionUID = 4713965679378778473L;

} /* end class CmdSpawn */
