// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
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


package uci.uml.Behavioral_Elements.Collaborations;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;


public class ClassifierRole extends Classifier {
  public Multiplicity _multiplicity;
  public Vector _associationEndRole;
  public Vector _message;
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
  }

  static final long serialVersionUID = -817836238906155325L;
}
