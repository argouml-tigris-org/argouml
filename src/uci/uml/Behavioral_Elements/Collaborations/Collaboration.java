// Source file: f:/jr/projects/uml/Behavioral_Elements/Collaborations/Collaboration.java

package uci.uml.Behavioral_Elements.Collaborations;

import java.util.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;

//needs-more-work: switch to Vector 

public class Collaboration extends NamespaceImpl {
  //public AssociationRole /ownedElement[];
  public Operation _representedOperation;
  //public ClassifierRole /ownedElement[];
  public Interaction _interaction[];
  public ModelElement _constrainingElement[];
    
  public Collaboration() { }


  public Operation getRepresentedOperation() {
    return _representedOperation;
  }
  public void setRepresentedOperation(Operation x) {
    _representedOperation = x;
  }

  public Interaction[] getInteraction() { return _interaction; }
  public void setInteraction(Interaction[] x) {
    _interaction = x;
  }

  public ModelElement[] getConstrainingElement() {
    return _constrainingElement;
  }
  public void setConstrainingElement(ModelElement[] x) {
    _constrainingElement = x;
  }
  

}
