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

package org.argouml.uml.diagram.collaboration.ui;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.collaborations.*;

import org.argouml.ui.*;

public class GoAssocRoleMessages implements TreeModel {

  public String getRuleName() { return "AssociationRole->Messages"; }

  public Object getRoot() {
      throw new UnsupportedOperationException("getRoot should never be called");
  }
  public void setRoot(Object r) { }

  public Object getChild(Object parent, int index) {
    Vector children = getChildren(parent);
    if (children != null) return children.elementAt(index);
    throw new UnsupportedOperationException("getChild should never be get here GoCollaborationDiagram");
  }

  public int getChildCount(Object parent) {
    Vector children = getChildren(parent);
    if (children != null) return children.size();
    return 0;
  }

  public int getIndexOfChild(Object parent, Object child) {
    Vector children = getChildren(parent);
    if (children != null && children.contains(child))
      return children.indexOf(child);
    return -1;
  }

  public Vector getChildren(Object parent) {
    if (!(parent instanceof MAssociationRole)) return null;
    return new Vector(((MAssociationRole)parent).getMessages());
  }

  public boolean isLeaf(Object node) {
    return !(node instanceof MAssociationRole && getChildCount(node) > 0);
  }

  public void valueForPathChanged(TreePath path, Object newValue) { }
  public void addTreeModelListener(TreeModelListener l) { }
  public void removeTreeModelListener(TreeModelListener l) { }

}
