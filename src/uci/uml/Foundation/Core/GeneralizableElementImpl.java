// Source file: Foundation/Core/GeneralizableElement.java

package uci.uml.Foundation.Core;

import java.util.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Data_Types.Name;

public class GeneralizableElementImpl extends NamespaceImpl
implements GeneralizableElement {
  public Boolean _isRoot;
  public Boolean _isLeaf;
  public Boolean _isAbstract;
  //% public Generalization _generalization[];
  public Vector _generalization;
  //% public Generalization _specialization[];
  public Vector _specialization;

  public GeneralizableElementImpl() { }
  public GeneralizableElementImpl(Name name) { super(name); }
  public GeneralizableElementImpl(String nameStr) {
    super(new Name(nameStr));
  }
  
  public Boolean getIsRoot() { return _isRoot; }
  public void setIsRoot(Boolean x) { _isRoot = x; }
  
  public Boolean getIsLeaf() { return _isLeaf; }
  public void setIsLeaf(Boolean x) { _isLeaf = x; }
  
  public Boolean getIsAbstract() { return _isAbstract; }
  public void setIsAbstract(Boolean x) { _isAbstract = x; }
  
  public Vector getGeneralization() {
    return _generalization;
  }
  public void setGeneralization(Vector x) {
    _generalization = x;
  }
  public void addGeneralization(Generalization x) {
    if (_generalization == null) _generalization = new Vector();
    _generalization.addElement(x);
  }
  public void removeGeneralization(Generalization x) {
    _generalization.removeElement(x);
  }
  
  public Vector getSpecialization() {
    return _specialization;
  }
  public void setSpecialization(Vector x) {
    _specialization = x;
  }
  public void addSpecialization(Generalization x) {
    if (_specialization == null) _specialization = new Vector();
    _specialization.addElement(x);
  }
  public void removeSpecialization(Generalization x) {
    _specialization.removeElement(x);
  }

}
