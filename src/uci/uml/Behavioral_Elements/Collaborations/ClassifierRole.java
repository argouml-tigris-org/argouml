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


package uci.uml.Behavioral_Elements.Collaborations;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;


public class ClassifierRole extends Classifier {
  public Multiplicity _multiplicity;
  public Vector _associationEndRole;
  public Vector _message;
  public String _baseString = "";
  public Classifier _base;
  public Vector _availableFeature;
  public Collaboration _collaboration;

  public ClassifierRole() { }
  public ClassifierRole(Name name) { super(name); }
  public ClassifierRole(String nameStr) { super(new Name(nameStr)); }

  public Multiplicity getMultiplicity() { return _multiplicity; }
  public void setMultiplicity(Multiplicity x) throws PropertyVetoException {
    fireVetoableChange("multiplicity", _multiplicity, x);
    _multiplicity = x;
  }

  public String getBaseString() { return _baseString; }
  public void setBaseString(String s) throws PropertyVetoException {
    fireVetoableChange("basestring", _baseString, s);
    _baseString = s;
  }

  public Classifier getBase() { return _base; }
  public void setBase(Classifier x) throws PropertyVetoException {
    fireVetoableChange("base", _base, x);
    _base = x;
  }

  public Vector getAssociationEnd() {
    return getAssociationEndRole();
  }
  public void setAssociationEnd(Vector x) throws PropertyVetoException {
    setAssociationEndRole(x);
  }
  public void addAssociationEnd(AssociationEnd ae) throws PropertyVetoException {
    if (ae instanceof AssociationEndRole)
      addAssociationEndRole((AssociationEndRole)ae);
    else
      super.addAssociationEnd(ae);
  }
  public void removeAssociationEnd(AssociationEnd ae) throws PropertyVetoException {
    if (ae instanceof AssociationEndRole)
      removeAssociationEndRole((AssociationEndRole)ae);
    else
      super.addAssociationEnd(ae);
  }

  public Vector getAssociationEndRole() {
    return _associationEndRole;
  }
  public void setAssociationEndRole(Vector x) throws PropertyVetoException {
    if (_associationEndRole == null) _associationEndRole = new Vector();
    fireVetoableChangeNoCompare("associationEndRole", _associationEndRole, x);
    _associationEndRole = x;
  }
  public void addAssociationEndRole(AssociationEndRole x)
       throws PropertyVetoException {
    if (_associationEndRole == null) _associationEndRole = new Vector();
    fireVetoableChange("associationEndRole", _associationEndRole, x);
    _associationEndRole.addElement(x);
  }
  public void removeAssociationEndRole(AssociationEndRole x)
       throws PropertyVetoException {
    if (_associationEndRole == null) return;
    fireVetoableChange("associationEndRole", _associationEndRole, x);
    _associationEndRole.removeElement(x);
  }


  public Vector getMessage() { return _message; }
  public void setMessage(Vector x) throws PropertyVetoException {
    if (_message == null) _message = new Vector();
    fireVetoableChangeNoCompare("message", _message, x);
    _message = x;
  }
  public void addMessage(Message x) throws PropertyVetoException {
    if (_message == null) _message = new Vector();
    fireVetoableChange("message", _message, x);
    _message.addElement(x);
  }
  public void removeMessage(Message x) throws PropertyVetoException {
    if (_message == null) return;
    fireVetoableChange("message", _message, x);
    _message.removeElement(x);
  }

  public Vector getAvailableFeature() { return _availableFeature; }
  public void setAvailableFeature(Vector x) throws PropertyVetoException {
    if (_availableFeature == null) _availableFeature = new Vector();
    fireVetoableChangeNoCompare("availableFeature", _availableFeature, x);
    _availableFeature = x;
  }
  public void addAvailableFeature(Feature x) throws PropertyVetoException {
    if (_availableFeature == null) _availableFeature = new Vector();
    fireVetoableChange("availableFeature", _availableFeature, x);
    _availableFeature.addElement(x);
  }
  public void removeAvailableFeature(Feature x) throws PropertyVetoException {
    if (_availableFeature == null) return;
    fireVetoableChange("availableFeature", _availableFeature, x);
    _availableFeature.removeElement(x);
  }

  public Collaboration getCollaboration() { return _collaboration; }
  public void setCollaboration(Collaboration x) throws PropertyVetoException {
    fireVetoableChange("collaboration", _collaboration, x);
     _collaboration = x;
     setNamespace(_collaboration);
  }

  public Object prepareForTrash() throws PropertyVetoException {
    setCollaboration(null);
    //needs-more-work
    return super.prepareForTrash();
  }

  public void recoverFromTrash(Object momento) throws PropertyVetoException {
    // needs-more-work
    super.recoverFromTrash(momento);
  }


  public Vector alsoTrash() {
    Vector res = super.alsoTrash();
    if (_associationEndRole != null) {
      for (int i = 0; i < _associationEndRole.size(); i++) {
	AssociationEndRole aer = (AssociationEndRole) _associationEndRole.elementAt(i);
	res.addElement(aer.getAssociationRole());
      }
    }
    return res;
  }

  static final long serialVersionUID = -817836238906155325L;
}
