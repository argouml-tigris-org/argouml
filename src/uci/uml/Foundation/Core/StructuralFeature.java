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




// Source file: Foundation/Core/StructuralFeature.java

package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Data_Types.*;


public abstract class StructuralFeature extends Feature {
  public Multiplicity _multiplicity = Multiplicity.ONE;
  public ChangeableKind _changeable = ChangeableKind.NONE;
  public ScopeKind _targetScope = ScopeKind.INSTANCE;
  public Classifier _type;

  public StructuralFeature() { }
  public StructuralFeature(Name name) { super(name); }
  public StructuralFeature(String nameStr) { super(new Name(nameStr)); }
  public StructuralFeature(Name name, Classifier type) {
    super(name);
    try {
      setType(type);
    }
    catch (PropertyVetoException pve) { }
  }
  public StructuralFeature(String nameStr, Classifier type) {
    this(new Name(nameStr), type);
  }

  public Multiplicity getMultiplicity() { return _multiplicity; }
  public void setMultiplicity(Multiplicity x) throws PropertyVetoException {
    fireVetoableChange("multiplicity", _multiplicity, x);
    _multiplicity = x;
  }
  public ChangeableKind getChangeable() { return _changeable; }
  public void setChangeable(ChangeableKind x) throws PropertyVetoException {
    fireVetoableChange("changeable", _changeable, x);
    _changeable = x;
  }
  public ScopeKind getTargetScope() { return _targetScope; }
  public void setTargetScope(ScopeKind x) throws PropertyVetoException {
    fireVetoableChange("targetScope", _targetScope, x);
    _targetScope = x;
  }
  public Classifier getType() { return _type; }
  public void setType(Classifier x) throws PropertyVetoException {
    fireVetoableChange("type", _type, x);
    _type = x;
  }
  static final long serialVersionUID = -4325107555487189539L;
}
