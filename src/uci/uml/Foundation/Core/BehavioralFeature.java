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

}
