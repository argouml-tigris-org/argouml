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
import java.beans.*;

import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Foundation.Data_Types.*;


public abstract class BehavioralFeature extends Feature {
  public boolean _isQuery;
  //% public Parameter parameter[];
  public Vector _parameter = new Vector();
  //% public MMException raisedException[];
  public Vector _raisedException = new Vector();

  public BehavioralFeature() { }
  public BehavioralFeature(Name name) { super(name); }
  public BehavioralFeature(String nameStr) { super(new Name(nameStr)); }

  public boolean getIsQuery() { return _isQuery; }
  public void setIsQuery(boolean x) throws PropertyVetoException {
    fireVetoableChange("isQuery", _isQuery, x);
    _isQuery = x;
  }

  public Classifier getReturnType()  {
    Vector params = getParameter();
    if (params != null) {
      java.util.Enumeration enum = params.elements();
      while (enum.hasMoreElements()) {
	Parameter p = (Parameter) enum.nextElement();
	if (Parameter.RETURN_NAME.equals(p.getName())) {
	  return p.getType();
	}
      }
    }
    return null;
  }

  public void setReturnType(Classifier rt) throws PropertyVetoException {
    Parameter p = findParameter(Parameter.RETURN_NAME);
    if (p == null) {
      p = new Parameter(rt, ParameterDirectionKind.RETURN, Parameter.RETURN_NAME);
      addParameter(p);
      //System.out.println("just set return type");
    }
    else {
      p.setType(rt);
    }
  }

  public Parameter findParameter(Name n) {
    Vector params = getParameter();
    if (params == null) return null;
    int size = params.size();
    for (int i = 0; i < size; i++) {
      Parameter p = (Parameter) params.elementAt(i);
      Name pName = p.getName();
      if (pName == null) continue;
      if (pName.equals(n)) return p;
    }
    return null; // not found
  }

  public Vector getParameter() { return (Vector) _parameter;}
  public void setParameter(Vector x) throws PropertyVetoException {
    if (_parameter == null) _parameter = new Vector();
    fireVetoableChangeNoCompare("parameter", _parameter, x);
    _parameter = x;
//     java.util.Enumeration enum = _parameter.elements();
//     while (enum.hasMoreElements()) {
//       Parameter p = (Parameter) enum.nextElement();
//       p.setNamespace(getNamespace());
//     }
  }

  /** needs-more-work: explicitly set the return parameter! */
  public void addParameter(Parameter x) throws PropertyVetoException {
    if (_parameter == null) _parameter = new Vector();
    fireVetoableChange("parameter", _parameter, x);
    _parameter.addElement(x);
    //x.setNamespace(getNamespace());
  }
  public void removeParameter(Parameter x) throws PropertyVetoException {
    if (_parameter == null) return;
    fireVetoableChange("parameter", _parameter, x);
    _parameter.removeElement(x);
  }

  public Vector getRaisedException() { return (Vector) _raisedException;}
  public void setRaisedException(Vector x) throws PropertyVetoException {
    if (_raisedException == null) _raisedException = new Vector();
    fireVetoableChangeNoCompare("raisedException", _raisedException, x);
    _raisedException = x;
  }
  public void addRaisedException(MMException x) throws PropertyVetoException {
    if (_raisedException == null) _raisedException = new Vector();
    fireVetoableChange("raisedException", _raisedException, x);
    _raisedException.addElement(x);
  }
  public void removeRaisedException(MMException x)
       throws PropertyVetoException {
    if (_raisedException == null) return;
    fireVetoableChange("raisedException", _raisedException, x);
    _raisedException.removeElement(x);
  }

  static final long serialVersionUID = 7697916359219415113L;
}
