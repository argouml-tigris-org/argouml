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


public class AssociationEndRole extends AssociationEnd {
  public AssociationEnd _base;
  public AssociationRole _associationRole;

  public AssociationEndRole() { }
  public AssociationEndRole(Name name) { super(name); }
  public AssociationEndRole(String nameStr) { super(new Name(nameStr)); }
  public AssociationEndRole(Classifier c) {
    super();
    try { setType(c); }
    catch (PropertyVetoException pce) { }
  }

  public AssociationEnd getBase() { return _base; }
  public void setBase(AssociationEnd x) throws PropertyVetoException {
    fireVetoableChange("base", _base, x);
     _base = x;
  }

  public AssociationRole getAssociationRole() {
    return _associationRole;
  }
  public void setAssociationRole(AssociationRole x) throws PropertyVetoException {
    if (_associationRole == x) return;
    AssociationRole old = _associationRole;
    fireVetoableChange("associationRole", _associationRole, x);
    _associationRole = x;
    if (old != null) old.removeAssociationEndRole(this);
    if (_associationRole != null)
      _associationRole.addAssociationEndRole(this);
  }

  ////////////////////////////////////////////////////////////////
  // events
  public void fireVetoableChange(String propertyName,
				 Object oldValue, Object newValue)
       throws PropertyVetoException {
    super.fireVetoableChange(propertyName, oldValue, newValue);
    if (_associationRole instanceof ElementImpl) {
       ((ElementImpl)_associationRole).fireVetoableChange("end_"+propertyName,
				 oldValue, newValue);
    }
  }

  static final long serialVersionUID = 177940593529064742L;
}
