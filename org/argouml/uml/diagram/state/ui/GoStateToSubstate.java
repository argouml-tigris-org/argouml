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
import ru.novosoft.uml.behavior.state_machines.*;

import org.argouml.ui.*;

public class GoStateToSubstate extends AbstractGoRule {

  public String getRuleName() { return "State->Substates"; }

  public Object getRoot() {
      throw
	  new UnsupportedOperationException("getRoot should never be called");
  }
  public void setRoot(Object r) { }

  public Object getChild(Object parent, int index) {
    Vector children = new Vector(getChildren(parent));
    return (children == null) ? null : children.elementAt(index);
  }

  public int getChildCount(Object parent) {
    Collection children = getChildren(parent);
    return (children == null) ? 0 : children.size();
  }

  public int getIndexOfChild(Object parent, Object child) {
    Vector children = new Vector(getChildren(parent));
    return (children == null) ? -1 : children.indexOf(child);
  }

  public boolean isLeaf(Object node) {
    return !(getChildCount(node) > 0);
  }

  public Collection getChildren(Object parent) {
    if (!(parent instanceof MCompositeState)) return null;
    MCompositeState cs = (MCompositeState) parent;
    Vector subs = new Vector(cs.getSubvertices());
	// attention: there is a typing-error in the UML.mdl here:
    if (!cs.isConcurent()) return subs;
    Vector children = new Vector();
    java.util.Enumeration enum = subs.elements();
    while (enum.hasMoreElements()) {
      MCompositeState sub = (MCompositeState) enum.nextElement();
      Vector subsubs = new Vector(sub.getSubvertices());
      if (subsubs != null) {
	java.util.Enumeration subEnum = subsubs.elements();
	while (subEnum.hasMoreElements()) {
	  children.addElement(subEnum.nextElement());
	}
      }
    }
    return children;
  }


  public void valueForPathChanged(TreePath path, Object newValue) { }
  public void addTreeModelListener(TreeModelListener l) { }
  public void removeTreeModelListener(TreeModelListener l) { }

}
