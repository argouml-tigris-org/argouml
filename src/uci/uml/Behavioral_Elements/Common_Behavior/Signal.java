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

package uci.uml.Behavioral_Elements.Common_Behavior;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.State_Machines.*;


public class Signal extends GeneralizableElementImpl implements Request {
  public Vector _reception;
  public Vector _parameter;
  public Vector _occurrence;
  public Vector _raises;

  public Signal() { }

  public Vector getReception() { return _reception; }
  public void setReception(Vector x) throws PropertyVetoException{
    if (_reception == null) _reception = new Vector();
    fireVetoableChange("reception", _reception, x);
    _reception = x;
  }
  public void addReception(Reception x) throws PropertyVetoException {
    if (_reception == null) _reception = new Vector();
    fireVetoableChange("reception", _reception, x);
    _reception.addElement(x);
  }
  public void removeReception(Reception x) throws PropertyVetoException {
    if (_reception == null) return;
    fireVetoableChange("reception", _reception, x);
    _reception.removeElement(x);
  }

  public Vector getParameter() { return _parameter; }
  public void setParameter(Vector x) throws PropertyVetoException {
    if (_parameter == null) _parameter = new Vector();
    fireVetoableChange("parameter", _parameter, x);
    _parameter = x;
    java.util.Enumeration enum = _parameter.elements();
    while (enum.hasMoreElements()) {
      Parameter p = (Parameter) enum.nextElement();
      p.setNamespace(getNamespace());
    }
  }
  public void addParameter(Parameter x) throws PropertyVetoException {
    if (_parameter == null) _parameter = new Vector();
    fireVetoableChange("parameter", _parameter, x);
    _parameter.addElement(x);
    x.setNamespace(getNamespace());
  }
  public void removeParameter(Parameter x) throws PropertyVetoException {
    if (_parameter == null) return;
    fireVetoableChange("parameter", _parameter, x);
    _parameter.removeElement(x);
  }

  public Vector getOccurrence() { return _occurrence; }
  public void setOccurrence(Vector x) throws PropertyVetoException {
    if (_occurrence == null) _occurrence = new Vector();
    fireVetoableChange("occurances", _occurrence, x);
    _occurrence = x;
  }
  public void addOccurrence(SignalEvent x) throws PropertyVetoException {
    if (_occurrence == null) _occurrence = new Vector();
    fireVetoableChange("occurances", _occurrence, x);
    _occurrence.addElement(x);
  }
  public void removeOccurrence(SignalEvent x) throws PropertyVetoException {
    if (_occurrence == null) return;
    fireVetoableChange("occurances", _occurrence, x);
    _occurrence.removeElement(x);
  }

  public Vector getRaises() { return _raises; }
  public void setRaises(Vector x) throws PropertyVetoException {
    if (_raises == null) _raises = new Vector();
    fireVetoableChange("raises", _raises, x);
    _raises = x;
  }

  // needs-more-work: maintain 2-way connection
  public void addRaises(SendAction x) throws PropertyVetoException {
    if (_raises == null) _raises = new Vector();
    fireVetoableChange("raises", _raises, x);
    _raises.addElement(x);
    //x.setSignal(this);
  }
  public void removeRaises(SendAction x) throws PropertyVetoException {
    if (_raises == null) return;
    fireVetoableChange("raises", _raises, x);
    _raises.removeElement(x);
  }


  ////////////////////////////////////////////////////////////////
  // Request implementation 

  public Vector _action;

  public Vector getAction() { return _action; }
  public void setAction(Vector x) throws PropertyVetoException {
    if (_action == null) _action = new Vector();
    fireVetoableChange("action", _action, x);
    _action = x;
  }
  public void addAction(MMAction x) throws PropertyVetoException {
    if (_action == null) _action = new Vector();
    fireVetoableChange("action", _action, x);
    _action.addElement(x);
  }
  public void removeAction(MMAction x) throws PropertyVetoException {
    if (_action == null) return;
    fireVetoableChange("action", _action, x);
    _action.removeElement(x);
  }

  static final long serialVersionUID = 2196339430138627081L;
}
