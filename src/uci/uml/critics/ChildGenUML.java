// File: ChildGenUML.java
// Classes: ChildGenUML
// Original Author: jrobbins
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.util.*;
import uci.uml.ui.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Model_Management.*;

public class ChildGenUML implements ChildGenerator {
  /** Reply a Enumeration of the children of the given Object */
  public Enumeration gen(Object o) {
    if (o instanceof Project) {
      return ((Project)o).getModels().elements();
    }

    if (o instanceof Package) {
      Vector ownedElements = ((Package)o).getOwnedElement();
      if (ownedElements != null) return ownedElements.elements();
    }
    
    if (o instanceof ElementOwnership) {
      ModelElement me = ((ElementOwnership)o).getModelElement();
      return new EnumerationSingle(me);
    }

//     // needs-more-work: associationclasses fit both of the next 2 cases

//     if (o instanceof Classifier) {
//       Classifier c = (Classifier) o;
//       EnumerationComposite res = new EnumerationComposite();      
//       res.addSub(c.getBehavioralFeature());
//       res.addSub(c.getStructuralFeature());
//       return res;
//     }

//     if (o instanceof IAssociation) {
//       IAssociation asc = (IAssociation) o;
//       Vector assocEnds = asc.getConnection();
//       if (assocEnds != null) return assocEnds.elements();
//     }
    

    
    
    
    // // needed?
//     if (o instanceof StateMachine) {
//       StateMachine sm = (StateMachine) o;
//       EnumerationComposite res = new EnumerationComposite();      
//       State top = sm.getTop();
//       if (top != null) res.addSub(EnumerationSingle(top));
//       res.addSub(sm.getTransitions());
//       return res;
//     }

//     // needed?
//     if (o instanceof CompositeState) {
//       CompositeState cs = (CompositeState) o;
//       Vector substates = cs.getSubstates();
//       if (substates != null) return substates.elements();
//     }

    // tons more cases
    
    return EnumerationEmpty.theInstance();
  }
} /* end class ChildGenUML */

