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

package org.argouml.uml.diagram.static_structure.ui;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;

import org.argouml.ui.*;

public class GoClassToAssocdClass implements TreeModel {

  public String toString() { return "Class->Associated Class"; }

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
    if (!(parent instanceof MClass)) return null;
    Vector res = new Vector();
    Vector ends = new Vector(((MClass)parent).getAssociationEnds());
    if (ends == null) return null;
    java.util.Enumeration enum = ends.elements();
    while (enum.hasMoreElements()) {
      MAssociationEnd ae = (MAssociationEnd) enum.nextElement();
      MAssociation asc = ae.getAssociation();
      Vector allEnds = new Vector(asc.getConnections());
      MAssociationEnd otherEnd = null;
      if (ae == allEnds.elementAt(0))
	otherEnd = (MAssociationEnd) allEnds.elementAt(1);
      if (ae == allEnds.elementAt(1))
	otherEnd = (MAssociationEnd) allEnds.elementAt(0);
      if (otherEnd != null && !res.contains(otherEnd.getType()))
	res.addElement(otherEnd.getType());
      // needs-more-work: handle n-way Associations
    }
    return res;
  }

  public boolean isLeaf(Object node) {
    return !(node instanceof MClass && getChildCount(node) > 0);
  }

  public void valueForPathChanged(TreePath path, Object newValue) { }
  public void addTreeModelListener(TreeModelListener l) { }
  public void removeTreeModelListener(TreeModelListener l) { }

}
