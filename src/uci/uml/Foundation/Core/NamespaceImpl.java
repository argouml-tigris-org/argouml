// Source file: Foundation/Core/Namespace.java

package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.PropertyVetoException;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;

public class NamespaceImpl extends ModelElementImpl implements Namespace {

  //% public ElementOwnership _ownedElement[];
  public Vector _ownedElement;
  
  public NamespaceImpl() { }
  public NamespaceImpl(Name name) { super(name); }
  public NamespaceImpl(String nameStr) { super(new Name(nameStr)); }

  public Vector getOwnedElement() { return _ownedElement; }
  public void setOwnedElement(Vector x) throws PropertyVetoException {
    fireVetoableChange("ownedElement", _ownedElement, x);
    _ownedElement = x;
  }
  public void addOwnedElement(ElementOwnership x) throws PropertyVetoException {
    fireVetoableChange("ownedElement", _ownedElement, x);
    if (_ownedElement == null) _ownedElement = new Vector();
    _ownedElement.addElement(x);
    x.getModelElement().setElementOwnership(x);    
  }
  public void removeOwnedElement(ElementOwnership x) throws PropertyVetoException {
    if (_ownedElement == null) return;
    fireVetoableChange("ownedElement", _ownedElement, x);
    _ownedElement.removeElement(x);
    x.getModelElement().setElementOwnership(null);
  }
  public void addPublicOwnedElement(ModelElement x) throws PropertyVetoException {
    ElementOwnership eo = new ElementOwnership(this, VisibilityKind.PUBLIC, x);
    addOwnedElement(eo);
  }
  public void addPrivateOwnedElement(ModelElement x) throws PropertyVetoException {
    ElementOwnership eo = new ElementOwnership(this, VisibilityKind.PRIVATE, x);
    addOwnedElement(eo);
  }
  public void addProtectedOwnedElement(ModelElement x)
       throws PropertyVetoException {
    ElementOwnership eo = new ElementOwnership(this, VisibilityKind.PROTECTED, x);
    addOwnedElement(eo);
  }
  public void addUnspecOwnedElement(ModelElement x)
       throws PropertyVetoException {
    ElementOwnership eo = new ElementOwnership(this, VisibilityKind.UNSPEC, x);
    addOwnedElement(eo);
  }


  public ElementOwnership elementOwnershipFor(ModelElement me,
					      VisibilityKind vk) {
    java.util.Enumeration eoEnum = _ownedElement.elements();
    while (eoEnum.hasMoreElements()) {
      ElementOwnership eo = (ElementOwnership) eoEnum.nextElement();
      if (eo.getModelElement() == me &&
	  (vk == null || vk == eo.getVisibility()))
	return eo;
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


}

