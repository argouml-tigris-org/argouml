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




// File: ChildGenUML.java
// Classes: ChildGenUML
// Original Author: jrobbins
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.util.*;
import uci.gef.*;
import uci.uml.ui.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Model_Management.*;

/** This class gives critics access to parts of the UML model of the
 *  design.  It defines a gen() function that returns the "children"
 *  of any given part of the UML model.  Basically, it goes from
 *  Project, to Models, to ModelElements.  Argo's critic Agency uses
 *  this to apply critics where appropriate.
 *
 * @see uci.argo.kernel.Agency */

public class ChildGenUML implements ChildGenerator {
  public static ChildGenUML SINGLETON = new ChildGenUML();

  /** Reply a Enumeration of the children of the given Object */
  public Enumeration gen(Object o) {
    if (o instanceof Project) {
      Project p = (Project) o;
      return new EnumerationComposite(p.getModels().elements(),
				      p.getDiagrams().elements());
    }

    if (o instanceof Diagram) {
      Vector figs = ((Diagram)o).getLayer().getContents();
      if (figs != null) return figs.elements();
    }

    if (o instanceof MMPackage) {
      Vector ownedElements = ((MMPackage)o).getOwnedElement();
      if (ownedElements != null) return ownedElements.elements();
    }

    if (o instanceof ElementOwnership) {
      ModelElement me = ((ElementOwnership)o).getModelElement();
      return new EnumerationSingle(me);  //wasteful!
    }

    if (o instanceof ModelElement) {
      Vector behavior = ((ModelElement)o).getBehavior();
      if (behavior != null) behavior.elements();
    }

    // needs-more-work: associationclasses fit both of the next 2 cases

    if (o instanceof Classifier) {
      Classifier cls = (Classifier) o;
      EnumerationComposite res = new EnumerationComposite();
      res.addSub(cls.getBehavioralFeature());
      res.addSub(cls.getStructuralFeature());

      Vector sms = cls.getBehavior();
      StateMachine sm = null;
      if (sms != null && sms.size() > 0) sm = (StateMachine) sms.elementAt(0);
      if (sm != null) res.addSub(new EnumerationSingle(sm));
      return res;
    }

    if (o instanceof IAssociation) {
      IAssociation asc = (IAssociation) o;
      Vector assocEnds = asc.getConnection();
      if (assocEnds != null) return assocEnds.elements();
      //needs-more-work: AssociationRole
    }





    // // needed?
    if (o instanceof StateMachine) {
      StateMachine sm = (StateMachine) o;
      EnumerationComposite res = new EnumerationComposite();
      State top = sm.getTop();
      if (top != null) res.addSub(new EnumerationSingle(top));
      res.addSub(sm.getTransitions());
      return res;
    }

    // needed?
    if (o instanceof CompositeState) {
      CompositeState cs = (CompositeState) o;
      Vector substates = cs.getSubstate();
      if (substates != null) return substates.elements();
    }

    // tons more cases

    return EnumerationEmpty.theInstance();
  }
} /* end class ChildGenUML */

