// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
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


// File: CmdSetAttr.java
// Classes: CmdSetAttr
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** Cmd to set the graphical attributes of a selected
 *  Fig.  For example, this is used to set line color and
 *  fill color.  Multiple graphical attributes can be set at once, that
 *  could allow the definition of styles in the future.
 *
 * @see Fig */

public class CmdSetAttr extends Cmd {

  /** the graphical attributes to apply to the selection. */
  private Hashtable _newAttrs = new Hashtable(3);

  /** Construct a new CmdSetAttr that sets one attribute value. */
  public CmdSetAttr(String attrName, Object value, String name) {
    super(name);
    _newAttrs.put(attrName, value);
  }

  /** Construct a new CmdSetAttr that sets one attribute value. */
  public CmdSetAttr(String attrName, Object value) {
    super("Set Object Attributes");
    _newAttrs.put(attrName, value);
  }

  /** Construct a new CmdSetAttr that sets all the given attributes. */
  public CmdSetAttr(Hashtable gAttrs) {
    super("Set Object Attributes");
    _newAttrs = gAttrs;
  }

  public void doIt() {
    Editor curEditor = Globals.curEditor();
    if (curEditor == null) return;
    SelectionManager sm = curEditor.getSelectionManager();
    if (sm.getLocked()) {
      Globals.showStatus("Cannot Modify Locked Objects");
      return;
    }

    if (sm.size() != 0) {
      sm.startTrans();
      //sel.put(_newAttrs);
      sm.endTrans();
      curEditor.repairDamage();
      /* I must explicitly call repairDamage because that editor might
	 not be getting any events... */
    }
    else {
      curEditor.put(_newAttrs);
    }
  }

  public void undoIt() {
    System.out.println("CmdSetAttr.undoIt() is not implemented yet");
  }

} /* end class CmdSetAttr */
