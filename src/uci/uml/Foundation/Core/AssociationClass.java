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

//nmw: import uci.uml.Behavioral_Elements.Collaborations.AssociationRole;
import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Data_Types.Name;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Behavioral_Elements.Common_Behavior.Link;


public class AssociationClass extends MMClass
implements IAssociation {


  public AssociationClass() { }
  public AssociationClass(Name name) { super(name); }
  public AssociationClass(String nameStr) { super(new Name(nameStr)); }

  ////////////////////////////////////////////////////////////////
  // IAssociation implementation
  public Vector _connection = new Vector();
  public Vector _link = new Vector();

  public Vector getConnection() { return (Vector) _connection;}
  public void setConnection(Vector x) {
    _connection = x;
  }
  public void addConnection(AssociationEnd x) throws PropertyVetoException {
    if (_connection == null) _connection = new Vector();
    _connection.addElement(x);
    x.setAssociation(this);
  }
  public void removeConnection(AssociationEnd x) {
    _connection.removeElement(x);
  }


  public Vector getLink() { return (Vector) _link;}
  public void setLink(Vector x) {
    _link = x;
  }
  public void addLink(Link x) {
    if (_link == null) _link = new Vector();
    _link.addElement(x);
  }
  public void removeLink(Link x) {
    _link.removeElement(x);
  }

  ////////////////////////////////////////////////////////////////
  // utility methods

  public boolean hasCompositeEnd() {
    if (_connection == null) return false;
    java.util.Enumeration enum = _connection.elements();
    while (enum.hasMoreElements()) {
      AssociationEnd ae = (AssociationEnd) enum.nextElement();
      if (AggregationKind.COMPOSITE.equals(ae.getAggregation()))
	return true;
    }
    return false;
  }

  public boolean hasAggregateEnd() {
    if (_connection == null) return false;
    java.util.Enumeration enum = _connection.elements();
    while (enum.hasMoreElements()) {
      AssociationEnd ae = (AssociationEnd) enum.nextElement();
      if (AggregationKind.AGG.equals(ae.getAggregation()))
	return true;
    }
    return false;
  }

  ////////////////////////////////////////////////////////////////
  // debugging


  public String dbgString() {
    String s = "";
    Vector v;
    java.util.Enumeration enum;

    String slash = "";
    if (containsStereotype(Stereotype.DERIVED)) slash = "/";

    s += getOCLTypeStr() + "(" + slash + getName().getBody().toString() + ")[";

    String stereos = dbgStereotypes();
    if (!stereos.equals("")) s += "\n" + stereos;

    String tags = dbgTaggedValues();
    if (tags != "") s += "\n" + tags;

    if ((v = getConnection()) != null) {
      s += "\n  connections:";
      enum = v.elements();
      while (enum.hasMoreElements())
	s += "\n  | " + ((AssociationEnd)enum.nextElement()).dbgString();
    }
    if ((v = getStructuralFeature()) != null) {
      s += "\n  attributes:";
      enum = v.elements();
      while (enum.hasMoreElements())
	s += "\n  | " + ((Attribute)enum.nextElement()).dbgString();
    }
    if ((v = getBehavioralFeature()) != null) {
      s += "\n  operations:";
      enum = v.elements();
      while (enum.hasMoreElements())
	s += "\n  | " + ((Operation) enum.nextElement()).dbgString();
    }
    s += "\n]";
    return s;
  }

  static final long serialVersionUID = -1912175742436278489L;
}

