// Source file: Model_Management/ElementReference.java

package uci.uml.Model_Management;

import java.util.*;

import uci.uml.Foundation.Data_Types.*;

public class ElementReference {
  public VisibilityKind _visibility;
  public Name _alias;

  public ElementReference() { }
  
  public VisibilityKind getVisibility() { return _visibility; }
  public void setVisibility(VisibilityKind x) {
    _visibility = x;
  }
  public Name getAlias() { return _alias; }
  public void setAlias(Name x) {
    _alias = x;
  }
  
}
