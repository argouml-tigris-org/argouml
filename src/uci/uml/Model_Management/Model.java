// Source file: f:/jr/projects/uml/Model_Management/Model.java

package uci.uml.Model_Management;

import java.util.*;

import uci.uml.Foundation.Core.*;

public class Model extends GeneralizableElementImpl implements Package {
    
  public Model() { }

  ////////////////////////////////////////////////////////////////
  // Package implementation
  
  //% public ModelElement _referencedElement[];
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
