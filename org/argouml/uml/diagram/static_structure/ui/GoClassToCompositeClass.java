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
import ru.novosoft.uml.foundation.data_types.*;

import org.argouml.ui.*;

public class GoClassToCompositeClass implements TreeModel {

  public String toString() { return "Class->Composite Class"; }
  
  public Object getRoot() {
      throw 
	  new UnsupportedOperationException("getRoot should never be called");
  }
  public void setRoot(Object r) { }

  public Object getChild(Object parent, int index) {
    if (parent instanceof MClass) {
      Vector children = getChildren((MClass)parent);
      return (children == null) ? null : children.elementAt(index);
    }
    throw new UnsupportedOperationException("getChild should never get here");
  }

  public int getChildCount(Object parent) {
    if (parent instanceof MClass) {
      Vector children = getChildren((MClass)parent);
      return (children == null) ? 0 : children.size();
    }
    return 0;
  }

  public int getIndexOfChild(Object parent, Object child) {
    int res = 0;
    if (parent instanceof MClass) {
      Vector children = getChildren((MClass)parent);
      if (children == null) return -1;
      if (children.contains(child))
	return children.indexOf(child);
    }
    return -1;
  }

  public Vector getChildren(MClass parentClass) {
    Vector res = new Vector();
    Vector ends = new Vector(parentClass.getAssociationEnds());
    if (ends == null) return res;
    java.util.Enumeration enum = ends.elements();
    while (enum.hasMoreElements()) {
      MAssociationEnd ae = (MAssociationEnd) enum.nextElement();
      if (!ae.getAggregation().equals(MAggregationKind.COMPOSITE)) continue;
      MAssociation asc = ae.getAssociation();
      Vector allEnds = new Vector(asc.getConnections());
      MAssociationEnd otherEnd = null;
      if (ae == allEnds.elementAt(0))
	otherEnd = (MAssociationEnd) allEnds.elementAt(1);
      if (ae == allEnds.elementAt(1))
	otherEnd = (MAssociationEnd) allEnds.elementAt(0);
      MClassifier assocClass = otherEnd.getType();
      if (assocClass != null && !res.contains(assocClass))
	res.addElement(assocClass);
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
