// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.


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
    super(new Name(aSupplier.getName().getBody() + "-->" +
		   aClient.getName().getBody())); 
    // needs-more-work
    //try {
      addSupplier(aSupplier);
      addClient(aClient);
    //}
    //catch (PropertyVetoException pve) { }
  }

  
  public String getDescription() { return _description; }
  public void setDescription(String x) {
    _description = x;
  }

  public Vector getSupplier() { return (Vector) _supplier.clone(); }
  public void setSupplier(Vector x) {
    _supplier = x;
  }
  public void addSupplier(ModelElement x) {
    if (_supplier == null) _supplier = new Vector();
    _supplier.addElement(x);
    //setNamespace(x.getNamespace());
  }
  public void removeSupplier(ModelElement x) {
    _supplier.removeElement(x);
  }

  public Vector getClient() { return (Vector) _client.clone(); }
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

  public Vector getSubDependency() { return (Vector) _subDependency.clone(); }
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
