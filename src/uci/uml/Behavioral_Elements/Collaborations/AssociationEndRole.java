// Source file: f:/jr/projects/uml/Behavioral_Elements/Collaborations/AssociationEndRole.java

package uci.uml.Behavioral_Elements.Collaborations;

import java.util.*;

import uci.uml.Foundation.Core.AssociationEnd;


public class AssociationEndRole extends AssociationEnd {
  public AssociationEnd _base;
  public AssociationRole _associationRole;
  //public ClassifierRole /type;
    
  public AssociationEndRole() { }

  public AssociationEnd getBase() { return _base; }
  public void setBase(AssociationEnd x) {
     _base = x;
  }

  public AssociationRole getAssociationRole() {
    return _associationRole;
  }
  public void setAssociationRole(AssociationRole x) {
    _associationRole = x;
  }
  
}
