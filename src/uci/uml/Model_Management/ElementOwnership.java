// Source file: Model_Management/ElementReference.java

package uci.uml.Model_Management;

import java.util.*;

import java.beans.PropertyVetoException;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

public class ElementOwnership {
  public VisibilityKind _visibility;
  public ModelElement _modelElement;
  public Namespace _namespace;

  public ElementOwnership() { }
  public ElementOwnership(Namespace n, VisibilityKind vk, ModelElement me) {
    this();
    setNamespace(n);
    setVisibility(vk);
    setModelElement(me);
  }
  
  public VisibilityKind getVisibility() { return _visibility; }
  public void setVisibility(VisibilityKind x) {
    _visibility = x;
  }

  public ModelElement getModelElement() { return _modelElement; }
  public void setModelElement(ModelElement x) {
    _modelElement = x;
  }

  public Namespace getNamespace() { return _namespace; }
  public void setNamespace(Namespace x) {
    _namespace = x;
  }
  
}
