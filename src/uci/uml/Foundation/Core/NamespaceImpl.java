// Source file: Foundation/Core/Namespace.java

package uci.uml.Foundation.Core;

import java.util.*;
import  uci.uml.Foundation.Data_Types.*;

public class NamespaceImpl extends ModelElementImpl implements Namespace {

  //% public ModelElement _ownedElement[];
  public Vector _ownedElement;
  
  public NamespaceImpl() { }
  public NamespaceImpl(Name name) { super(name); }
  public NamespaceImpl(String nameStr) { super(new Name(nameStr)); }

  public Vector getOwnedElement() { return _ownedElement; }
  public void setOwnedElement(Vector x) {
    _ownedElement = x;
  }
  public void addOwnedElement(ModelElement x) {
    if (_ownedElement == null) _ownedElement = new Vector();
    _ownedElement.addElement(x);
  }
  public void removeOwnedElement(ModelElement x) {
    _ownedElement.removeElement(x);
  }

}
