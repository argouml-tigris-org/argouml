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

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.ui.*;

public class GoModelToBaseElements implements TreeModel {
    protected static Category cat = Category.getInstance(GoModelToBaseElements.class);

  public String getRuleName() {
    return Argo.localize ("Tree", "misc.package.base-class");
  }
  
  public Object getRoot() {
      throw
	  new UnsupportedOperationException("getRoot should never be called");
  } 
  public void setRoot(Object r) { }

  public Object getChild(Object parent, int index) {
    if (parent instanceof MPackage) {
      Vector eos = new Vector(((MPackage)parent).getOwnedElements());
      java.util.Enumeration eoEnum = eos.elements();
      while (eoEnum.hasMoreElements()) {
	MModelElement me = (MModelElement)eoEnum.nextElement();
	if (me instanceof MGeneralizableElement) {
	  Collection gens = ((MGeneralizableElement)me).getGeneralizations();
	  if (gens == null || gens.size() == 0) index--;
	  if (index == -1) return me;
	}
      }
      cat.warn("getChild not enough base elements found!");
    }
    throw new UnsupportedOperationException("getChild should not get here");
  }
  
  public int getChildCount(Object parent) {
    if (parent instanceof MPackage) {
      int count = 0;
      Vector eos = new Vector(((MPackage)parent).getOwnedElements());
      java.util.Enumeration eoEnum = eos.elements();
      while (eoEnum.hasMoreElements()) {
	MModelElement me =(MModelElement) eoEnum.nextElement();
	if (me instanceof MGeneralizableElement) {
	  Collection gens = ((MGeneralizableElement)me).getGeneralizations();
	  if (gens == null || gens.size() == 0) count++;
	}
      }
      return count;
    }
    return 0;
  }
  
  public int getIndexOfChild(Object parent, Object child) {
    if (parent instanceof MPackage) {
      int count = 0;
      Vector eos = new Vector(((MPackage)parent).getOwnedElements());
      java.util.Enumeration eoEnum = eos.elements();
      while (eoEnum.hasMoreElements()) {
	MModelElement me = (MModelElement) eoEnum.nextElement();
	if (me instanceof MGeneralizableElement) {
	  Collection gens = ((MGeneralizableElement)me).getGeneralizations();
	  if (gens == null || gens.size() == 0) return count;
	  count++;
	}
      }
      return count;
    }
    return -1;
  }

  public boolean isLeaf(Object node) {
    return !(node instanceof MPackage && getChildCount(node) > 0);
  }
  
  public void valueForPathChanged(TreePath path, Object newValue) { }
  public void addTreeModelListener(TreeModelListener l) { }
  public void removeTreeModelListener(TreeModelListener l) { }

}
