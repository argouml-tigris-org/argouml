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


// File: CmdEditNode.java
// Classes: CmdEditNode
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;
import java.io.*;

/** Cmd to edit a node.  For now this just asks the node to edit
 *  itself via editNode().  In the future their might be some support
 *  for undo or some behavior common to all node editing operations.
 *
 * @see NetNode#editNode */

public class CmdEditNode extends Cmd {

  private NetNode _nodeToEdit;

  public CmdEditNode() {
    super("Edit Node");
    _nodeToEdit = null;
  }

  public CmdEditNode(NetNode n) {
    super("Edit Node");
    _nodeToEdit = n;
  }

  public void doIt() {
    if (_nodeToEdit != null) {
      _nodeToEdit.editNode();
      return;
    }
    Editor ce = Globals.curEditor();
    if (ce == null) return;
    Vector selectedFigs = ce.getSelectionManager().getFigs();
    Enumeration figs = selectedFigs.elements();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      if (f instanceof FigNode) {
        _nodeToEdit = (NetNode)((FigNode)f).getOwner();
	Globals.showStatus("Editing " + _nodeToEdit.toString());
        _nodeToEdit.editNode();
      }
    }
    _nodeToEdit = null;
  }

  public void undoIt() {
    System.out.println("Undo does not make sense for CmdEditNode");
  }

} /* end class CmdEditNode */
