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




// Source file: Foundation/Core/Feature.java

package uci.uml.Foundation.Core;

import java.util.*;
//nmw: import uci.uml.Behavioral_Elements.Collaborations.ClassifierRole;

import java.beans.PropertyVetoException;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;

public abstract class Feature extends ModelElementImpl {
  public ScopeKind _ownerScope = ScopeKind.INSTANCE;
  public VisibilityKind _visibility = VisibilityKind.UNSPEC;
  public Classifier _owner;
  //nmw: public ClassifierRole _classifierRole[];

  public Feature() { }
  public Feature(Name name) { super(name); }
  public Feature(String nameStr) { super(new Name(nameStr)); }

  public ScopeKind getOwnerScope() { return _ownerScope; }
  public void setOwnerScope(ScopeKind x) throws PropertyVetoException {
    fireVetoableChange("ownerScope", _ownerScope, x);
    _ownerScope = x;
  }
  public VisibilityKind getVisibility() { return _visibility; }
  public void setVisibility(VisibilityKind x) throws PropertyVetoException {
    fireVetoableChange("visibility", _visibility, x);
    _visibility = x;
  }
  public Classifier getOwner() { return _owner; }
  public void setOwner(Classifier x) throws PropertyVetoException {
    fireVetoableChange("owner", _visibility, x);
    _owner = x;
  }


  //public ClassifierRole[] getClassifierRole() { return _classifierRole;
  //}
  //public void setClassifierRole(ClassifierRole[] x) {
  //  classifierRole = x;
  //}

  ////////////////////////////////////////////////////////////////
  // events
   public void fireVetoableChange(String propertyName,
 				 Object oldValue, Object newValue)
        throws PropertyVetoException {
    super.fireVetoableChange(propertyName, oldValue, newValue);
    if (_owner != null) {
       _owner.fireVetoableChange("feature_"+propertyName,
				 oldValue, newValue);
    }
  }



  ////////////////////////////////////////////////////////////////
  // debugging

  public String toString() { return getName().getBody(); }

}
