// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.




// Source file: Foundation/Core/Dependency.java

package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.*;
import uci.uml.Foundation.Data_Types.*;

/** By default a Dependency is in the Namespace of its Supplier. */

public class Dependency extends ModelElementImpl {
  public String _description;
  //% public ModelElement _supplier[];
  public Vector _supplier = new Vector();
  //% public ModelElement _client[];
  public Vector _client = new Vector();
  //% public Dependency _subDependency[];
  public Vector _subDependency = new Vector();
  public Dependency _owningDependency;

  public Dependency() { }
  public Dependency(Name name) { super(name); }
  public Dependency(String nameStr) { super(new Name(nameStr)); }

  public Dependency(ModelElement aSupplier, ModelElement aClient) {
    super(new Name("==>"));
    try {
      addSupplier(aSupplier);
      addClient(aClient);
    }
    catch (PropertyVetoException pve) { }
  }

  public Name getName() {
    Name n = super.getName();
    if (_supplier == null || _supplier.size() == 0) return n;
    if (_client == null || _client.size() == 0) return n;
    ModelElement aSupplier = (ModelElement) _supplier.elementAt(0);
    ModelElement aClient = (ModelElement) _client.elementAt(0);
    n.setBody(aSupplier.getName().getBody() +
	      " ==> " +
	      aClient.getName().getBody());
    return n;
  }

  public String getDescription() { return _description; }
  public void setDescription(String x) throws PropertyVetoException {
    _description = x;
  }

  public Vector getSupplier() { return (Vector) _supplier;}
  public void setSupplier(Vector x) throws PropertyVetoException {
    if (_supplier != null) {
      int size = _supplier.size();
      for (int i = 0; i < size; i++) {
	ModelElement me = (ModelElement) _supplier.elementAt(i);
	me.removeProvision(this);
      }
    }
    if (_supplier == null) _supplier = new Vector();
    fireVetoableChangeNoCompare("supplier", _supplier, x);
    _supplier = x;
    if (_supplier != null) {
      int size = _supplier.size();
      for (int i = 0; i < size; i++) {
	ModelElement me = (ModelElement) _supplier.elementAt(i);
	me.addProvision(this);
      }
    }
  }
  public void addSupplier(ModelElement x) throws PropertyVetoException {
    if (_supplier == null) _supplier = new Vector();
    if (_supplier.contains(x)) return;
    fireVetoableChange("supplier", _supplier, x);
    _supplier.addElement(x);
    //setNamespace(x.getNamespace());
    x.addProvision(this);
  }
  public void removeSupplier(ModelElement x) throws PropertyVetoException {
    if (_supplier == null) return;
    if (!_supplier.contains(x)) return;
    _supplier.removeElement(x);
    fireVetoableChange("supplier", _supplier, x);
    x.removeProvision(this);
  }

  // needs-more-work: changed to particapant in UML 1.3
  public Vector getClient() { return (Vector) _client;}
  public void setClient(Vector x) throws PropertyVetoException {
    if (_client != null) {
      int size = _client.size();
      for (int i = 0; i < size; i++) {
	ModelElement me = (ModelElement) _client.elementAt(i);
	me.removeRequirement(this);
      }
    }
    if (_client == null) _client = new Vector();
    fireVetoableChange("client", _client, x);
    _client = x;
    if (_client != null) {
      int size = _client.size();
      for (int i = 0; i < size; i++) {
	ModelElement me = (ModelElement) _client.elementAt(i);
	me.addRequirement(this);
      }
    }
    // needs-more-work: set all providers
  }
  public void addClient(ModelElement x) throws PropertyVetoException {
    if (_client == null) _client = new Vector();
    if (_client.contains(x)) return;
    fireVetoableChange("client", _client, x);
    _client.addElement(x);
    x.addRequirement(this);
  }
  public void removeClient(ModelElement x) throws PropertyVetoException {
    if (_client == null) return;
    if (!_client.contains(x)) return;
    fireVetoableChange("client", _client, x);
    _client.removeElement(x);
    x.removeRequirement(this);
  }

  // needs-more-work: subdependencies not handled yet
  public Vector getSubDependency() { return (Vector) _subDependency;}
  public void setSubDependency(Vector x) throws PropertyVetoException {
    _subDependency = x;
  }
  public void addSubDependency(Dependency x) throws PropertyVetoException {
    if (_subDependency == null) _subDependency = new Vector();
    _subDependency.addElement(x);
  }
  public void removeSubDependency(Dependency x) throws PropertyVetoException {
    _subDependency.removeElement(x);
  }

  public Dependency getOwningDependency() { return _owningDependency; }
  public void setOwningDependency(Dependency x) throws PropertyVetoException {
    _owningDependency = x;
  }

  public Object prepareForTrash() throws PropertyVetoException {
    setSupplier(new Vector());
    setClient(new Vector());
    return super.prepareForTrash();
    //needs-more-work
  }

  public void recoverFromTrash(Object momento) throws PropertyVetoException {
    // needs-more-work
    super.recoverFromTrash(momento);
  }

  static final long serialVersionUID = 5158638856882915937L;
}
