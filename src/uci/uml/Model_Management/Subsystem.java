// Source file: f:/jr/projects/uml/Model_Management/Subsystem.java

package uci.uml.Model_Management;

import java.util.*;

import uci.uml.Foundation.Core.*;


public class Subsystem extends Classifier implements Package {
  public Boolean _isInstantiable;
  
  public Subsystem() { }

  public Boolean getIsInstantiable() { return _isInstantiable; }
  public void setIsInstantiable(Boolean x) {
    _isInstantiable = x;
  }

  ////////////////////////////////////////////////////////////////
  // Package implementation
  
  public Vector _referencedElement;

  public Vector getReferencedElement() {
    return _referencedElement;
  }
  public void setReferencedElement(Vector x) {
    _referencedElement = x;
  }
  public void addReferencedElement(ModelElement x) {
    if (_referencedElement == null) _referencedElement = new Vector();
    _referencedElement.addElement(x);
  }
  public void removeReferencedElement(ModelElement x) {
    _referencedElement.removeElement(x);
  }

}
