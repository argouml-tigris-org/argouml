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




// Source file: Foundation/Core/Parameter.java

package uci.uml.Foundation.Core;

import java.util.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;

/** Parameters are in the same Namespace as the BehavioralFeature that
 *  they are part of. */

public class Parameter extends ModelElementImpl {
  public static final Name RETURN_NAME = new Name("return");

  public Expression _defaultValue;
  public ParameterDirectionKind _kind = ParameterDirectionKind.IN;
  public BehavioralFeature _behavioralFeature;
  public Classifier _type;
  public Event _parameters;
  //% public ObjectFlowState _availability[];
  public Vector _availability = new Vector();
  public Signal _signal;

  public Parameter() { }
  public Parameter(Name name) { super(name); }
  public Parameter(String nameStr) { super(new Name(nameStr)); }
  public Parameter(Classifier type, ParameterDirectionKind kind, Name name) {
    this(name);
    setType(type);
    setKind(kind);
  }
  public Parameter(Classifier type, ParameterDirectionKind kind, String nameStr) {
    this(nameStr);
    setType(type);
    setKind(kind);
  }

  public Expression getDefaultValue() { return _defaultValue; }
  public void setDefaultValue(Expression x) {
    _defaultValue = x;
  }
  public ParameterDirectionKind getKind() { return _kind; }
  public void setKind(ParameterDirectionKind x) {
    _kind = x;
  }
  public BehavioralFeature getBehavioralFeature() {
    return _behavioralFeature;
  }
  public void setBehavioralFeature(BehavioralFeature x) {
    _behavioralFeature = x;
  }
  public Classifier getType() { return _type; }
  public void setType(Classifier x) {
    _type = x;
  }
  public Event getParameters() { return _parameters; }
  public void setParameters(Event x) {
    _parameters = x;
  }

  public Vector getAvailability() {
    return (Vector) _availability;
  }
  public void setAvailability(Vector x) {
    _availability = x;
  }
  public void addAvailability(ObjectFlowState x) {
    if (_availability == null) _availability = new Vector();
    _availability.addElement(x);
  }
  public void removeAvailability(ObjectFlowState x) {
    _availability.removeElement(x);
  }

  public Signal getSignal() { return _signal; }
  public void setM_Signal(Signal x) {
    _signal = x;
  }
  static final long serialVersionUID = -9197114372473714426L;
}
