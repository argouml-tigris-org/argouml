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

package org.argouml.uml.diagram.ui;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;

import org.tigris.gef.util.*;

import org.argouml.application.api.Argo;
import org.argouml.ui.*;

public class GoFilteredChildren extends AbstractGoRule {

  ////////////////////////////////////////////////////////////////
  // instance vars
  String _name = "unamed filtered rule";
  Predicate _pred = PredicateTrue.theInstance();
  TreeModel _tm;

  ////////////////////////////////////////////////////////////////
  // constructor
  public GoFilteredChildren(String name, TreeModel tm, Predicate pred) {
    _name = Argo.localize ("Tree", name);
    _tm = tm;
    _pred = pred;
  }


  ////////////////////////////////////////////////////////////////
  // accessors

  // needs-more-work
  
  ////////////////////////////////////////////////////////////////
  // TreeModel implementation
  
  public String getRuleName() { return _name; }
  
  public Object getRoot() { return _tm.getRoot(); }
  
  public Object getChild(Object parent, int index) {
    int unfilteredCount = _tm.getChildCount(parent);
    int filteredCount = 0;
    for (int i = 0; i < unfilteredCount; ++i) {
      Object kid = _tm.getChild(parent, i);
      if (_pred.predicate(kid)) {
	if (filteredCount == index) return kid;
	filteredCount++;
      }
    }
    return null;
  }

  public Collection getChildren(Object parent) { 
      throw
          new UnsupportedOperationException("getChildren should not be called");
  }
  
  public int getChildCount(Object parent) {
    int unfilteredCount = _tm.getChildCount(parent);
    int filteredCount = 0;
    for (int i = 0; i < unfilteredCount; ++i) {
      Object kid = _tm.getChild(parent, i);
      if (_pred.predicate(kid)) filteredCount++;
    }
    return filteredCount;
  }
  
  private boolean hasChildren(Object parent) {
    int unfilteredCount = _tm.getChildCount(parent);
    for (int i = 0; i < unfilteredCount; ++i) {
      Object kid = _tm.getChild(parent, i);
      if (_pred.predicate(kid)) return true;
    }
    return false;
  }
  
  public int getIndexOfChild(Object parent, Object child) {
    int unfilteredCount = _tm.getChildCount(parent);
    int filteredCount = 0;
    for (int i = 0; i < unfilteredCount; ++i) {
      Object kid = _tm.getChild(parent, i);
      if (_pred.predicate(kid)) {
	if (kid == child) return filteredCount;
	filteredCount++;
      }
    }
    return -1;
  }
  
  public void valueForPathChanged(TreePath path, Object newValue) { }
  public void addTreeModelListener(TreeModelListener l) { }
  public void removeTreeModelListener(TreeModelListener l) { }
  
  public boolean isLeaf(Object node) {
    return !hasChildren(node);
  }

}
