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
  }
  public void removeOwnedElement(ElementOwnership x) throws PropertyVetoException {
    fireVetoableChange("ownedElement", _ownedElement, x);
    _ownedElement.removeElement(x);
  }
  public void addPublicOwnedElement(ModelElement x) throws PropertyVetoException {
    ElementOwnership eo = new ElementOwnership(this, VisibilityKind.PUBLIC, x);
    addOwnedElement(eo);
    updateElementOwnership(eo, x);
  }
  public void addPrivateOwnedElement(ModelElement x) throws PropertyVetoException {
    ElementOwnership eo = new ElementOwnership(this, VisibilityKind.PRIVATE, x);
    addOwnedElement(eo);
    updateElementOwnership(eo, x);
  }
  public void addProtectedOwnedElement(ModelElement x)
       throws PropertyVetoException {
    ElementOwnership eo = new ElementOwnership(this, VisibilityKind.PROTECTED, x);
    addOwnedElement(eo);
    updateElementOwnership(eo, x);
  }
  public void addUnspecOwnedElement(ModelElement x)
       throws PropertyVetoException {
    ElementOwnership eo = new ElementOwnership(this, VisibilityKind.UNSPEC, x);
    addOwnedElement(eo);
    updateElementOwnership(eo, x);
  }


  protected void updateElementOwnership(ElementOwnership eo, ModelElement me)
       throws PropertyVetoException {
    if (me.getElementOwnership() != null) {
      // needs-more-work: remove from old package if any
    }
    me.setElementOwnership(eo);
  }
}

