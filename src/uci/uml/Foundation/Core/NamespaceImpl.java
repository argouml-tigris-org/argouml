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



package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.PropertyVetoException;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;

public class NamespaceImpl extends ModelElementImpl implements Namespace {

  //% public ElementOwnership _ownedElement[];
  public Vector _ownedElement = new Vector();

  public NamespaceImpl() { }
  public NamespaceImpl(Name name) { super(name); }
  public NamespaceImpl(String nameStr) { super(new Name(nameStr)); }

  public Vector getOwnedElement() { return _ownedElement;}
  public void setOwnedElement(Vector x) throws PropertyVetoException {
    if (_ownedElement == null) _ownedElement = new Vector();
    fireVetoableChangeNoCompare("ownedElement", _ownedElement, x);
    _ownedElement = x;
  }
  public void addOwnedElement(ElementOwnership x) throws PropertyVetoException {
    if (_ownedElement == null) _ownedElement = new Vector();
    else if (_ownedElement.contains(x)) return;
    if (elementOwnershipFor(x.getModelElement(), null) != null) return;

    fireVetoableChange("ownedElement", _ownedElement, x);
    _ownedElement.addElement(x);
    x.getModelElement().setElementOwnership(x);
    x.setNamespace(this);
  }
  public void removeOwnedElement(ElementOwnership x) throws PropertyVetoException {
    //System.out.println("namespace removing: " + x);
    if (_ownedElement == null) return;
    else if (!_ownedElement.contains(x)) return;
    fireVetoableChange("ownedElement", _ownedElement, x);
    _ownedElement.removeElement(x);
    x.getModelElement().setElementOwnership(null);
  }
  public void addPublicOwnedElement(ModelElement x) throws PropertyVetoException {
    if (elementOwnershipFor(x, null) != null) return;
    ElementOwnership eo = new ElementOwnership(this, VisibilityKind.PUBLIC, x);
    addOwnedElement(eo);
  }
  public void addPrivateOwnedElement(ModelElement x) throws PropertyVetoException {
    if (elementOwnershipFor(x, null) != null) return;
    ElementOwnership eo = new ElementOwnership(this, VisibilityKind.PRIVATE, x);
    addOwnedElement(eo);
  }
  public void addProtectedOwnedElement(ModelElement x)
       throws PropertyVetoException {
    if (elementOwnershipFor(x, null) != null) return;
    ElementOwnership eo = new ElementOwnership(this, VisibilityKind.PROTECTED, x);
    addOwnedElement(eo);
  }
  public void addUnspecOwnedElement(ModelElement x)
       throws PropertyVetoException {
    if (elementOwnershipFor(x, null) != null) return;
    ElementOwnership eo = new ElementOwnership(this, VisibilityKind.PUBLIC, x);
    addOwnedElement(eo);
  }

  public Vector getModelElements() {
    Vector res = new Vector();
    int size = _ownedElement.size();
    for (int i = 0; i < size; i++) {
      ElementOwnership eo = (ElementOwnership) _ownedElement.elementAt(i);
      res.addElement(eo.getModelElement());
    }
    return res;
  }

  public ElementOwnership elementOwnershipFor(ModelElement me,
					      VisibilityKind vk) {
    int size = _ownedElement.size();
    for (int i = 0; i < size; i++) {
      ElementOwnership eo = (ElementOwnership) _ownedElement.elementAt(i);
      if (eo.getModelElement() == me &&
	  (vk == null || vk.equals(eo.getVisibility())))
	return eo;
    }
    return null;
  }

  public ElementOwnership findElementNamed(String name) {
    int size = _ownedElement.size();
    for (int i = 0; i < size; i++) {
      ElementOwnership eo = (ElementOwnership) _ownedElement.elementAt(i);
      ModelElement me = eo.getModelElement();
      if (me.getName().getBody().equals(name)) return eo;
    }
    return null;
  }

  public boolean contains(ModelElement me) { return contains(me, null); }

  public boolean contains(ModelElement me, VisibilityKind vk) {
    return elementOwnershipFor(me, vk) != null;
  }

  public boolean containsPublic(ModelElement me) {
    return elementOwnershipFor(me, VisibilityKind.PUBLIC) != null;
  }


  public Object prepareForTrash() throws PropertyVetoException {
    Namespace enclosing = getNamespace();
    int size = _ownedElement.size();
    for (int i = 0; i < size; i++) {
      ElementOwnership eo = (ElementOwnership) _ownedElement.elementAt(i);
      eo.setNamespace(enclosing);
      if (enclosing != null) enclosing.addOwnedElement(eo);
    }
    return null;
    //needs-more-work: remember old namespace
  }

  public void recoverFromTrash(Object momento) throws PropertyVetoException {
    // needs-more-work: restore old namespace
  }

  static final long serialVersionUID = -6554474404565654034L;
}

