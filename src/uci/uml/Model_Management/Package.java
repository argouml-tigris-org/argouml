// Source file: f:/jr/projects/uml/Model_Management/Package.java


package uci.uml.Model_Management;

import java.util.*;
import uci.uml.Foundation.Core.GeneralizableElement;
import uci.uml.Foundation.Core.ModelElement;


public interface Package extends GeneralizableElement {
  // public ModelElement _referencedElement[];

  public Vector getReferencedElement();
  public void setReferencedElement(Vector x);
  
}
