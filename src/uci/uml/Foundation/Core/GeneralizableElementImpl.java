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


// Source file: Foundation/Core/GeneralizableElement.java

package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.PropertyVetoException;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Data_Types.Name;

public class GeneralizableElementImpl extends NamespaceImpl
implements GeneralizableElement {
  public Boolean _isRoot = Boolean.FALSE;
  public Boolean _isLeaf = Boolean.FALSE;
  public Boolean _isAbstract = Boolean.FALSE;
  //% public Generalization _generalization[];
  public Vector _generalization;
  //% public Generalization _specialization[];
  public Vector _specialization;

  public GeneralizableElementImpl() { }
  public GeneralizableElementImpl(Name name) { super(name); }
  public GeneralizableElementImpl(String nameStr) {
    super(new Name(nameStr));
  }
  
  public boolean isRoot() { return _isRoot.booleanValue(); }
  public Boolean getIsRoot() { return _isRoot; }
  public void setIsRoot(Boolean x) throws PropertyVetoException {
    fireVetoableChange("isRoot", _isRoot, x);
    _isRoot = x;
  }
  
  public boolean isLeaf() { return _isLeaf.booleanValue(); }
  public Boolean getIsLeaf() { return _isLeaf; }
  public void setIsLeaf(Boolean x) throws PropertyVetoException {
    fireVetoableChange("isLeaf", _isLeaf, x);
    _isLeaf = x;
  }
  
  public boolean isAbstract() { return _isAbstract.booleanValue(); }
  public Boolean getIsAbstract() { return _isAbstract; }
  public void setIsAbstract(Boolean x) throws PropertyVetoException {
    fireVetoableChange("isAbstract", _isAbstract, x);
    _isAbstract = x;
  }
  
  public Vector getGeneralization() {
    return _generalization;
  }
  public void setGeneralization(Vector x) throws PropertyVetoException {
    fireVetoableChange("generalization", _generalization, x);
    _generalization = x;
  }
  public void addGeneralization(Generalization x) throws PropertyVetoException {
    if (_generalization == null) _generalization = new Vector();
    if (_generalization.contains(x)) return;
    fireVetoableChange("generalization", _generalization, x);
    _generalization.addElement(x);
  }
  public void removeGeneralization(Generalization x) throws PropertyVetoException {
    if (_generalization == null || !_generalization.contains(x)) return;
    fireVetoableChange("generalization", _generalization, x);
    _generalization.removeElement(x);
  }
  
   public Vector getSpecialization() {
     return _specialization;
   }
   public void setSpecialization(Vector x) throws PropertyVetoException {
     fireVetoableChange("specalization", _specialization, x);
     _specialization = x;
   }
   public void addSpecialization(Generalization x) throws PropertyVetoException {
     if (_specialization == null) _specialization = new Vector();
    if (_specialization.contains(x)) return;
     fireVetoableChange("specalization", _specialization, x);
     _specialization.addElement(x);
   }
   public void removeSpecialization(Generalization x) throws PropertyVetoException {
    if (_specialization == null || !_specialization.contains(x)) return;
     fireVetoableChange("specalization", _specialization, x);
     _specialization.removeElement(x);
   }

}
