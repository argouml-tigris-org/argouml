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

package uci.uml.ui.nav;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Behavioral_Elements.Use_Cases.*;

public class GoModelToActor implements TreeModelPrereqs {

  public String toString() { return "Package->Actor"; }
  
  public Object getRoot() {
    System.out.println("getRoot should never be called");
    return null;
  }
  public void setRoot(Object r) { }

  public Object getChild(Object parent, int index) {
    if (parent instanceof MMPackage) {
      Vector eos = ((MMPackage)parent).getOwnedElement();
      for (int i = 0; i < eos.size(); i++) {
	ElementOwnership eo = (ElementOwnership) eos.elementAt(i);
	ModelElement me = eo.getModelElement();
	if (me instanceof Actor) index--;
	if (index == 0) return me;
      }
    }
    System.out.println("getChild should never get here GoModelToActor");
    return null;
  }

  public int getChildCount(Object parent) {
    int res = 0;
    if (parent instanceof MMPackage) {
      Vector oes = ((MMPackage) parent).getOwnedElement();
      if (oes == null) return 0;
      java.util.Enumeration enum = oes.elements();
      while (enum.hasMoreElements()) {
	ElementOwnership eo = (ElementOwnership) enum.nextElement();
	ModelElement me = eo.getModelElement();
	if (me instanceof Actor) res++;
      }
    }
    return res;
  }

  public int getIndexOfChild(Object parent, Object child) {
    int res = 0;
    if (parent instanceof MMPackage) {
      Vector oes = ((MMPackage)parent).getOwnedElement();
      if (oes == null) return -1;
      java.util.Enumeration enum = oes.elements();
      while (enum.hasMoreElements()) {
	ElementOwnership eo = (ElementOwnership) enum.nextElement();
	ModelElement me = eo.getModelElement();
	if (me == child) return res;
	if (me instanceof Actor) res++;
      }
    }
    return -1;
  }

  public boolean isLeaf(Object node) {
    return !(node instanceof MMPackage && getChildCount(node) > 0);
  }

  public void valueForPathChanged(TreePath path, Object newValue) { }
  public void addTreeModelListener(TreeModelListener l) { }
  public void removeTreeModelListener(TreeModelListener l) { }

  public Vector getPrereqs() {
    Vector pros = new Vector();
    pros.addElement(Model.class);
    return pros;
  }
  public Vector getProvidedTypes() {
    Vector pros = new Vector();
    pros.addElement(Actor.class);
    return pros;
  }

}
