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

// File: CmdDelete.java
// Classes: CmdDelete
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.awt.Event;

/** Cmd to delete Figs from view.  This command does not
 *  do anything to any underlying Net or other model, it is strictly a
 *  manipulation of graphical objects.  Normally CmdDispose is the
 *  command users will want to execute.
 *
 * @see Fig
 * @see CmdDispose
 * @see Editor
 * @see LayerDiagram */

public class CmdDelete extends Cmd {

  public CmdDelete() { super("Delete"); }

  /** Tell the selected Figs to remove themselves from the
   *  the diagram it is in (and thus all editors). */
  public void doIt() {
    Editor ce = Globals.curEditor();
    SelectionManager sm = ce.getSelectionManager();
    sm.delete(); //?
  }

  public void undoIt() { }

} /* end class CmdDelete */
