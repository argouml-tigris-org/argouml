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

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;



public class Event extends ModelElementImpl {
  //% public State _state[];
  public Vector _state;
  //% public Parameter _parameters[];
  public Vector _parameters;
  //% public Transition _transition[];
  public Vector _transition;
  
  public Event() { }
  public Event(Name name) { super(name); }
  public Event(String nameStr) { super(new Name(nameStr)); }

  public Vector getState() { return _state; }
  public void setState(Vector x) throws PropertyVetoException {
    if (_state == null) _state = new Vector();
    fireVetoableChangeNoCompare("state", _state, x);
    _state = x;
  }
  public void addState(State x) throws PropertyVetoException {
    if (_state == null) _state = new Vector();
    fireVetoableChange("state", _state, x);
    _state.addElement(x);
  }
  public void removeState(State x) throws PropertyVetoException {
    if (_state == null) return;
    fireVetoableChange("state", _state, x);
    _state.removeElement(x);
  }
  
  public Vector getParameters() { return _parameters; }
  public void setParameters(Vector x) throws PropertyVetoException {
    if (_parameters == null) _parameters = new Vector();
    fireVetoableChangeNoCompare("parameters", _parameters, x);
    _parameters = x;
  }
  public void addParameters(Parameter x) throws PropertyVetoException {
    if (_parameters == null) _parameters = new Vector();
    fireVetoableChange("parameters", _parameters, x);
    _parameters.addElement(x);
  }
  public void removeParameters(Parameter x) throws PropertyVetoException {
    if (_parameters == null) return;
    fireVetoableChange("parameters", _parameters, x);
    _parameters.removeElement(x);
  }

  public Vector getTransition() { return _transition; }
  public void setTransition(Vector x) throws PropertyVetoException {
    if (_transition == null) _transition = new Vector();
    fireVetoableChangeNoCompare("transition", _transition, x);
    _transition = x;
  }
  public void addTransition(Transition x) throws PropertyVetoException {
    if (_transition == null) _transition = new Vector();
    fireVetoableChange("transition", _transition, x);
    _transition.addElement(x);
  }
  public void removeTransition(Transition x) throws PropertyVetoException {
    if (_transition == null) return;
    fireVetoableChange("transition", _transition, x);
    _transition.removeElement(x);
  }
  static final long serialVersionUID = 6658724545936512448L;
}
