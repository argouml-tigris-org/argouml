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



package uci.uml.Foundation.Core;

//nmw: import uci.uml.Behavioral_Elements.Collaborations.AssociationRole;
import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Data_Types.Name;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Behavioral_Elements.Common_Behavior.Link;

/** By default, Associations are in the same Namespace as their source
 *  Classifier */

public class Association extends GeneralizableElementImpl
implements IAssociation {
  //% public AssociationEnd _connection[];
  public Vector _connection = new Vector();
  //nmw: public AssociationRole associationRole[];
  //% public Link _link[];
  public Vector _link = new Vector();
    
  public Association() { }
  public Association(Name name) { super(name); }
  public Association(String nameStr) { super(new Name(nameStr)); }
  public Association(String nameStr,AssociationEnd from,AssociationEnd to){
    super(new Name(nameStr));
    try {
      addConnection(from);
      addConnection(to);
    }
    catch (PropertyVetoException pce) { }
  }
  public Association(Name name, Name srcN, Classifier srcC,
		     Multiplicity srcM, AggregationKind srcA,
		     Name dstN, Classifier dstC, Multiplicity dstM,
		     AggregationKind dstA) { 
    super(name);
    try {
      AssociationEnd src = new AssociationEnd(srcN, srcC, srcM, srcA);
      AssociationEnd dst = new AssociationEnd(dstN, dstC, dstM, dstA);
      addConnection(src);
      addConnection(dst);
    }
    catch (PropertyVetoException pce) { }
  }

  public Association(Classifier srcC, Classifier dstC) { 
    super();
    try {
      AssociationEnd src = new AssociationEnd(srcC);
      AssociationEnd dst = new AssociationEnd(dstC);
      addConnection(src);
      //srcC.addAssociationEnd(src);
      addConnection(dst);
      //dstC.addAssociationEnd(dst);
      setNamespace(srcC.getNamespace());
    }
    catch (PropertyVetoException pce) { }
  }

  public Vector getConnection() { return (Vector) _connection; }
  public void setConnection(Vector x) throws PropertyVetoException {
    if (_connection == null) _connection = new Vector();    
    fireVetoableChange("connection", _connection, x);
    _connection = x;
    java.util.Enumeration enum = _connection.elements();
    while (enum.hasMoreElements()) {
      AssociationEnd ae = (AssociationEnd) enum.nextElement();
      ae.setAssociation(this);
    }
  }
  public void addConnection(AssociationEnd x) throws PropertyVetoException {
    if (_connection == null) _connection = new Vector();
    fireVetoableChange("connection", _connection, x);
    _connection.addElement(x);
    x.setAssociation(this);
  }
  public void removeConnection(AssociationEnd x) throws PropertyVetoException {
    if (_connection == null) return;
    fireVetoableChange("connection", _connection, x);
    _connection.removeElement(x);
  }
 
  //- public AssociationRole[] getAssociationRole() {
  //-   return AssociationRole;
  //- }
  //- public void setAssociationRole(AssociationRole[] x) {
  //-   AssociationRole = x;
  //- }

  public Vector getLink() { return (Vector) _link;}
  public void setLink(Vector x) throws PropertyVetoException {
    if (_link == null) _link = new Vector();
    fireVetoableChange("link", _link, x);
    _link = x;
  }
  public void addLink(Link x) throws PropertyVetoException {
    if (_link == null) _link = new Vector();
    fireVetoableChange("link", _link, x);
    _link.addElement(x);
  }
  public void removeLink(Link x) throws PropertyVetoException {
    if (_link == null) return;
    fireVetoableChange("link", _link, x);
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
    s += "\n]";
    return s;
  }
  
}

