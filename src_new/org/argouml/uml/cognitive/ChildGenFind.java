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

// File: ChildGenFind.java
// Classes: ChildGenFind
// Original Author: jrobbins
// $Id$

package org.argouml.uml.cognitive;

import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.base.Diagram;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.util.*;

import org.argouml.kernel.*;

/** This class gives critics access to parts of the UML model of the
 *  design.  It defines a gen() function that returns the "children"
 *  of any given part of the UML model.  Basically, it goes from
 *  Project, to Models, to ModelElements.  Argo's critic Agency uses
 *  this to apply critics where appropriate.
 *
 * @see org.argouml.cognitive.critics.Agency 
 * @stereotype singleton
 */

public class ChildGenFind implements ChildGenerator {
  public static ChildGenFind SINGLETON = new ChildGenFind();

  /** Reply a Collection of the children of the given Object */
  public java.util.Enumeration gen(Object o) {
    if (o instanceof Project) {
      Project p = (Project) o;
	  Vector res = new Vector();
	  res.addAll(p.getUserDefinedModels());
	  res.addAll(p.getDiagrams());
	  return res.elements();
      // return new EnumerationComposite(p.getModels().elements(),
	  //		      p.getDiagrams().elements());
    }

//     if (o instanceof MPackage) {
//       Vector ownedElements = ((MPackage)o).getOwnedElements();
//       if (ownedElements != null) return ownedElements.elements();
//     }

//     if (o instanceof MElementImport) {
//       MModelElement me = ((MElementImport)o).getModelElement();
//       return new EnumerationSingle(me);  //wasteful!
//     }

//     if (o instanceof MModelElement) {
//       Vector behavior = ((MModelElement)o).getBehavior();
//       if (behavior != null) behavior.elements();
//     }

//     // TODO: associationclasses fit both of the next 2 cases

    if (o instanceof MClassifier) {
      MClassifier cls = (MClassifier) o;
	  //      EnumerationComposite res = new EnumerationComposite();
	  Vector res = new Vector(cls.getFeatures());
      res.addAll(cls.getBehaviors());
      return res.elements();
    }

    if (o instanceof MAssociation) {
      MAssociation asc = (MAssociation) o;
	  return new Vector(asc.getConnections()).elements();
	  //      Vector assocEnds = asc.getConnections();
      //if (assocEnds != null) return assocEnds.elements();
    }



//     // // needed?
//     if (o instanceof MStateMachine) {
//       MStateMachine sm = (MStateMachine) o;
//       EnumerationComposite res = new EnumerationComposite();
//       MState top = sm.getTop();
//       if (top != null) res.addSub(new EnumerationSingle(top));
//       res.addSub(sm.getTransitions());
//       return res;
//     }

//     // needed?
//     if (o instanceof MCompositeState) {
//       MCompositeState cs = (MCompositeState) o;
//       Vector substates = cs.getSubvertices();
//       if (substates != null) return substates.elements();
//     }

    // tons more cases

    if (o instanceof Diagram) {
      Diagram d = (Diagram) o;
	  
	  Vector res = new Vector();
	  res.addAll(d.getGraphModel().getNodes());
	  res.addAll(d.getGraphModel().getEdges());
	  return res.elements();
      //return new EnumerationComposite(d.getGraphModel().getNodes().elements(),
	  //			      d.getGraphModel().getEdges().elements());
    }

    if (o instanceof MState) {
      MState s = (MState) o;
      //Vector interns = s.getInternalTransition();
      //if (interns != null) return interns.elements();
	  return new Vector(s.getInternalTransitions()).elements();
    }

    if (o instanceof MTransition) {
      MTransition tr = (MTransition) o;
	  Vector res = new Vector();
	  res.add(tr.getTrigger());
	  res.add(tr.getGuard());
	  res.add(tr.getEffect());
	  /*
		Vector parts = new Vector();  // wasteful!!
		if (tr.getTrigger() != null) parts.addElement(tr.getTrigger());
		if (tr.getGuard() != null) parts.addElement(tr.getGuard());
		if (tr.getEffect() != null) parts.addElement(tr.getEffect());
		return parts.elements();
	  */
	  return res.elements();
    }

    return new Vector().elements();
  }
} /* end class ChildGenFind */

