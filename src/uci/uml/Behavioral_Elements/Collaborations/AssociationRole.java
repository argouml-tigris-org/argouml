// Source file: f:/jr/projects/uml/Behavioral_Elements/Collaborations/AssociationRole.java

package uci.uml.Behavioral_Elements.Collaborations;

import java.util.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;


public class AssociationRole extends Association {
  public Multiplicity _multiplicity;
  public Association _base;
  //public AssociationEndRole /connection[];
  public Collaboration _collaboration;
    
  public AssociationRole() { }

  public Multiplicity getMultiplicity() { return _multiplicity; }
  public void setMultiplicity(Multiplicity x) {
     _multiplicity = x;
  }

  public Association getBase() { return _base; }
  public void setBase(Association x) {
     _base = x;
  }

  public Collaboration getCollaboration() { return _collaboration; }
  public void setCollaboration(Collaboration x) {
     _collaboration = x;
  }
  
}
