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




package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.PropertyVetoException;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Data_Types.Name;

public class GeneralizableElementImpl extends NamespaceImpl
implements GeneralizableElement {
  public boolean _isRoot = false;
  public boolean _isLeaf = false;
  public boolean _isAbstract = false;
  //% public Generalization _generalization[];
  public Vector _generalization = new Vector();
  //% public Generalization _specialization[];
  public Vector _specialization = new Vector();

  public GeneralizableElementImpl() { }
  public GeneralizableElementImpl(Name name) { super(name); }
  public GeneralizableElementImpl(String nameStr) {
    super(new Name(nameStr));
  }


  public boolean getIsRoot() { return _isRoot; }
  public void setIsRoot(boolean x) throws PropertyVetoException {
    fireVetoableChange("isRoot", _isRoot, x);
    _isRoot = x;
  }

  public boolean getIsLeaf() { return _isLeaf; }
  public void setIsLeaf(boolean x) throws PropertyVetoException {
    fireVetoableChange("isLeaf", _isLeaf, x);
    _isLeaf = x;
  }

  public boolean getIsAbstract() { return _isAbstract; }
  public void setIsAbstract(boolean x) throws PropertyVetoException {
    fireVetoableChange("isAbstract", _isAbstract, x);
    _isAbstract = x;
  }

  public Vector getGeneralization() { return _generalization; }
  public void setGeneralization(Vector x) throws PropertyVetoException {
    if (_generalization == null) _generalization = new Vector();
    fireVetoableChangeNoCompare("generalization", _generalization, x);
    _generalization = x;
    java.util.Enumeration enum = _generalization.elements();
    while (enum.hasMoreElements()) {
      Generalization g = (Generalization) enum.nextElement();
      g.setNamespace(getNamespace());
    }
  }
  public void addGeneralization(Generalization x) throws PropertyVetoException {
    if (_generalization == null) _generalization = new Vector();
    if (_generalization.contains(x)) return;
    fireVetoableChange("generalization", _generalization, x);
    _generalization.addElement(x);
    x.setNamespace(getNamespace());
  }
  public void removeGeneralization(Generalization x) throws PropertyVetoException {
    if (_generalization == null || !_generalization.contains(x)) return;
    fireVetoableChange("generalization", _generalization, x);
    _generalization.removeElement(x);
  }

  public Vector getSpecialization() { return _specialization; }
  public void setSpecialization(Vector x) throws PropertyVetoException {
    if (_specialization == null) _specialization = new Vector();
    fireVetoableChangeNoCompare("specalization", _specialization, x);
    _specialization = x;
    java.util.Enumeration enum = _specialization.elements();
    while (enum.hasMoreElements()) {
      Generalization g = (Generalization) enum.nextElement();
      g.setNamespace(getNamespace());
    }
  }
  public void addSpecialization(Generalization x) throws PropertyVetoException {
    if (_specialization == null) _specialization = new Vector();
    if (_specialization.contains(x)) return;
    fireVetoableChange("specalization", _specialization, x);
     _specialization.addElement(x);
    x.setNamespace(getNamespace());
  }
  public void removeSpecialization(Generalization x) throws PropertyVetoException {
    if (_specialization == null || !_specialization.contains(x)) return;
    fireVetoableChange("specalization", _specialization, x);
    _specialization.removeElement(x);
  }

  public Vector alsoTrash() {
    Vector res = super.alsoTrash();
    if (_specialization != null) {
      for (int i = 0; i < _specialization.size(); i++)
	res.addElement(_specialization.elementAt(i));
    }
    if (_generalization != null) {
      for (int i = 0; i < _generalization.size(); i++)
	res.addElement(_generalization.elementAt(i));
    }
    return res;
  }

  static final long serialVersionUID = -3189360194197209778L;
}
