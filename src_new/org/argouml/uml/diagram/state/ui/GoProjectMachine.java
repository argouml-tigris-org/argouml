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

package org.argouml.uml.diagram.state.ui;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;

import org.argouml.kernel.Project;
import org.argouml.ui.*;

public class GoProjectMachine implements TreeModel {

  public String getRuleName() { return "Project->Machine"; }

  public Object getRoot() {
      throw
	  new UnsupportedOperationException("getRoot should never be called");
  }
  public void setRoot(Object r) { }

  public Object getChild(Object parent, int index) {
    Vector children = getChildren(parent);
    if (children != null) return children.elementAt(index);
    throw
	new UnsupportedOperationException("getChild should never be get here");
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
    if (!(parent instanceof Project)) return null;
    Vector res = new Vector();
    Vector models = ((Project)parent).getModels();
    if (models == null) return null;
    java.util.Enumeration enum = models.elements();
    while (enum.hasMoreElements()) {
      MNamespace ns = (MNamespace) enum.nextElement();
      accumMachines(ns, res);
    }
    return res;
  }

  public void accumMachines(MNamespace m, Vector res) {
    Vector oes = new Vector(m.getOwnedElements());
    int size = oes.size();
    for (int i = 0; i < size; i++) {
      MModelElement me = (MModelElement) oes.elementAt(i);
      Vector beh = new Vector(me.getBehaviors());
      int behSize = beh.size();
      for (int j = 0; j < behSize; j++) {
	res.addElement(beh.elementAt(j));
      }
      if (me instanceof MNamespace) accumMachines((MNamespace)me, res);
    }
  }

  public boolean isLeaf(Object node) {
    return !(node instanceof Project && getChildCount(node) > 0);
  }

  public void valueForPathChanged(TreePath path, Object newValue) { }
  public void addTreeModelListener(TreeModelListener l) { }
  public void removeTreeModelListener(TreeModelListener l) { }

}
