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

import java.util.Collection;
import java.util.Vector;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import org.argouml.application.api.Argo;
import org.argouml.model.uml.UmlHelper;
import org.argouml.ui.AbstractGoRule;

import ru.novosoft.uml.foundation.core.MClassifier;

public class GoClassifierToStr extends AbstractGoRule {

  public String getRuleName() {
    return Argo.localize ("Tree", "misc.class.attribute");
  }
  
  public Object getRoot() {
      throw
	  new UnsupportedOperationException("getRoot should never be called");
  } 
  public void setRoot(Object r) { }

  public Object getChild(Object parent, int index) {
    if (parent instanceof MClassifier) {
      MClassifier cls = (MClassifier) parent;
	  Collection behs = UmlHelper.getHelper().getCore().getAttributes(cls);
	  Vector v = new Vector(behs);
      return v.elementAt(index);
	}
    throw 
	new UnsupportedOperationException("getChild should never be get here");
  }

  public Collection getChildren(Object parent) { 
      throw
          new UnsupportedOperationException("getChildren should not be called");
  }
  
  public int getChildCount(Object parent) {
    if (parent instanceof MClassifier) {
      Collection str = UmlHelper.getHelper().getCore().getAttributes((MClassifier) parent);
      return (str == null) ? 0 : str.size();
    }
    return 0;
  }
  
  public int getIndexOfChild(Object parent, Object child) {
    if (parent instanceof MClassifier) {
      Vector str = new Vector(UmlHelper.getHelper().getCore().getAttributes((MClassifier)parent));
      if (str.contains(child)) return str.indexOf(child);
    }
    return -1;
  }

  public boolean isLeaf(Object node) {
    return !(node instanceof MClassifier && getChildCount(node) > 0);
  }

  public void valueForPathChanged(TreePath path, Object newValue) { }
  public void addTreeModelListener(TreeModelListener l) { }
  public void removeTreeModelListener(TreeModelListener l) { }
  
}
