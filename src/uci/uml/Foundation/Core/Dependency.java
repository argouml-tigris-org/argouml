// Source file: Foundation/Core/Dependency.java

package uci.uml.Foundation.Core;

import java.util.*;

public abstract class Dependency extends ModelElementImpl {
  public String _description;
  //% public ModelElement _supplier[];
  public Vector _supplier;
  //% public ModelElement _client[];
  public Vector _client;
  //% public Dependency _subDependency[];
  public Vector _subDependency;
  public Dependency _owningDependency;
  
  public Dependency() { }
  
  public String getDescription() { return _description; }
  public void setDescription(String x) {
    _description = x;
  }

  public Vector getSupplier() { return _supplier; }
  public void setSupplier(Vector x) {
    _supplier = x;
  }
  public void addSupplier(ModelElement x) {
    if (_supplier == null) _supplier = new Vector();
    _supplier.addElement(x);
  }
  public void removeSupplier(ModelElement x) {
    _supplier.removeElement(x);
  }

  public Vector getClient() { return _client; }
  public void setClient(Vector x) {
    _client = x;
  }
  public void addClient(ModelElement x) {
    if (_client == null) _client = new Vector();
    _client.addElement(x);
  }
  public void removeClient(ModelElement x) {
    _client.removeElement(x);
  }

  public Vector getSubDependency() { return _subDependency; }
  public void setSubDependency(Vector x) {
    _subDependency = x;
  }
  public void addSubDependency(Dependency x) {
    if (_subDependency == null) _subDependency = new Vector();
    _subDependency.addElement(x);
  }
  public void removeSubDependency(Dependency x) {
    _subDependency.removeElement(x);
  }

  public Dependency getOwningDependency() { return _owningDependency; }
  public void setOwningDependency(Dependency x) {
    _owningDependency = x;
  }


}
