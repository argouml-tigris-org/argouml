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


public class AssociationRole extends Association {
  public Multiplicity _multiplicity;
  public Association _base;
  public Collaboration _collaboration;
  public Vector _associationEndRole;
  public Vector _messages;

  public AssociationRole() { }
  public AssociationRole(Name name) { super(name); }
  public AssociationRole(String nameStr) { super(new Name(nameStr)); }
  public AssociationRole(Classifier srcC, Classifier dstC) {
    super();
    try {
      AssociationEndRole src = new AssociationEndRole(srcC);
      AssociationEndRole dst = new AssociationEndRole(dstC);
      addAssociationEndRole(src);
      //srcC.addAssociationEnd(src);
      addAssociationEndRole(dst);
      //dstC.addAssociationEnd(dst);
      setNamespace(srcC.getNamespace());
    }
    catch (PropertyVetoException pce) { }
  }

  public Multiplicity getMultiplicity() { return _multiplicity; }
  public void setMultiplicity(Multiplicity x) throws PropertyVetoException {
    fireVetoableChange("multiplicity", _multiplicity, x);
     _multiplicity = x;
  }

  public Association getBase() { return _base; }
  public void setBase(Association x) throws PropertyVetoException {
    fireVetoableChange("base", _base, x);
    _base = x;
  }

  public Collaboration getCollaboration() { return _collaboration; }
  public void setCollaboration(Collaboration x) throws PropertyVetoException {
    fireVetoableChange("collaboration", _collaboration, x);
     _collaboration = x;
     setNamespace(_collaboration);
  }

  public Vector getMessages() { return _messages; }
  public void setMessages(Vector x) throws PropertyVetoException {
    fireVetoableChange("messages", _messages, x);
     _messages = x;
  }

  public void addMessage(Message x)
  throws PropertyVetoException {
    if (_messages == null) _messages = new Vector();
    fireVetoableChange("messages", _messages, x);
    _messages.addElement(x);
    //x.setOwner(this);
    //x.setNamespace(this);
  }


  public Vector getAssociationEndRole() { return (Vector) _associationEndRole; }
  public void setAssociationEndRole(Vector x) throws PropertyVetoException {
    if (_associationEndRole == null) _associationEndRole = new Vector();
    fireVetoableChangeNoCompare("associationEndRole", _associationEndRole, x);
    _associationEndRole = x;
    java.util.Enumeration enum = _associationEndRole.elements();
    while (enum.hasMoreElements()) {
      AssociationEndRole ae = (AssociationEndRole) enum.nextElement();
      ae.setAssociationRole(this);
    }
  }
  public void addAssociationEndRole(AssociationEndRole x) throws PropertyVetoException {
    if (_associationEndRole == null) _associationEndRole = new Vector();
    if (_associationEndRole.contains(x)) return;
    fireVetoableChange("associationEndRole", _associationEndRole, x);
    _associationEndRole.addElement(x);
    x.setAssociationRole(this);
  }
  public void removeAssociationEndRole(AssociationEndRole x) throws PropertyVetoException {
    if (_associationEndRole == null) return;
    if (!_associationEndRole.contains(x)) return;
    fireVetoableChange("associationEndRole", _associationEndRole, x);
    _associationEndRole.removeElement(x);
    if (x.getAssociationRole() == this) x.setAssociationRole(null);
  }

  public Object prepareForTrash() throws PropertyVetoException {
    Vector conns = getAssociationEndRole();
    for (int i = 0; i < conns.size(); i++) {
      try {
	((AssociationEndRole)conns.elementAt(i)).setBase(null);
	((AssociationEndRole)conns.elementAt(i)).setType(null);
      }
      catch (PropertyVetoException pve) { }
    }
    //needs-more-work
    return super.prepareForTrash();
  }

  public void recoverFromTrash(Object momento) throws PropertyVetoException {
    // needs-more-work
    super.recoverFromTrash(momento);
  }

  public Vector alsoTrash() {
    Vector res = super.alsoTrash();
    if (_messages != null) {
      for (int i = 0; i < _messages.size(); i++)
	res.addElement(_messages.elementAt(i));
    }
    return res;
  }

  /*public void removeBehavioralFeature(Feature x)
  throws PropertyVetoException {
    if (_behavioralFeature == null) return;
    fireVetoableChange("behavioralFeature", _behavioralFeature, x);
    _behavioralFeature.removeElement(x);
    x.setOwner(null);
  }
  public BehavioralFeature findBehavioralFeature(Name n) {
    Vector beh = getBehavioralFeature();
    int behSize = beh.size();
    for (int i = 0; i < behSize; i++) {
      BehavioralFeature bf = (BehavioralFeature) beh.elementAt(i);
      if (bf.getName().equals(n)) return bf;
    }
    return null;
  }*/
  static final long serialVersionUID = 5767831899229440182L;
}
