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




// File: ChildGenRelated.java
// Classes: ChildGenRelated
// Original Author: jrobbins
// $Id$

package uci.uml.ui;

import java.util.*;

import uci.util.*;
import uci.gef.Diagram;
import uci.graph.GraphModel;
import uci.uml.ui.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Model_Management.*;

public class ChildGenRelated implements ChildGenerator {
  public static ChildGenRelated SINGLETON = new ChildGenRelated();

  /** Reply a Enumeration of the children of the given Object */
  public Enumeration gen(Object o) {

    EnumerationComposite res = new EnumerationComposite();

    if (o instanceof MMPackage) {
      Vector ownedElements = ((MMPackage)o).getOwnedElement();
      if (ownedElements != null)
	return new Enum(ownedElements.elements(), EOElement.SINGLETON);
    }

    if (o instanceof Classifier) {
      Classifier cls = (Classifier) o;
      Vector assocEnds = cls.getAssociationEnd();
      VectorSet assoc = new VectorSet();
      int numAssoc = assocEnds.size();
      for (int i = 0; i < numAssoc; i++) {
	AssociationEnd ae = (AssociationEnd) assocEnds.elementAt(i);
	assoc.addElement(ae.getAssociation());
      }
      res.addSub(assoc.elements());
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
      VectorSet classes = new VectorSet();
      int numAssoc = assocEnds.size();
      for (int i = 0; i < numAssoc; i++) {
	AssociationEnd ae = (AssociationEnd) assocEnds.elementAt(i);
	classes.addElement(ae.getType());
      }
      return classes.elements();
    }

    if (o instanceof StateMachine) {
      StateMachine sm = (StateMachine) o;
      State top = sm.getTop();
      if (top != null)
	res.addSub(((CompositeState)top).getSubstate().elements());
      res.addSub(new EnumerationSingle(sm.getContext())); //wasteful!
      res.addSub(sm.getTransitions());
      return res;
    }

    if (o instanceof StateVertex) {
      StateVertex sv = (StateVertex) o;
      Vector incoming = sv.getIncoming();
      Vector outgoing = sv.getOutgoing();
      if (incoming != null) res.addSub(incoming.elements());
      if (outgoing != null) res.addSub(outgoing.elements());

      if (o instanceof State) {
	State s = (State) o;
	Vector internal = s.getInternalTransition();
	if (internal != null) res.addSub(internal.elements());
      }

      if (o instanceof CompositeState) {
	CompositeState cs = (CompositeState) o;
	Vector substates = cs.getSubstate();
	if (substates != null) res.addSub(substates.elements());
      }
      return res;
    }

    if (o instanceof Transition) {
      Transition tr = (Transition) o;
      Vector parts = new Vector();  // wasteful!!
      if (tr.getTrigger() != null) parts.addElement(tr.getTrigger());
      if (tr.getGuard() != null) parts.addElement(tr.getGuard());
      if (tr.getEffect() != null) parts.addElement(tr.getEffect());
      res.addSub(new EnumerationSingle(tr.getSource()));
      res.addSub(new EnumerationSingle(tr.getTarget()));
      res.addSub(parts.elements());
      return res;
    }

    // tons more cases

    if (o instanceof Diagram) {
      Diagram d = (Diagram) o;
      return new EnumerationComposite(d.getGraphModel().getNodes().elements(),
				      d.getGraphModel().getEdges().elements());
    }



    return EnumerationEmpty.theInstance();
  }
} /* end class ChildGenRelated */


class EOElement implements Functor {
  public static EOElement SINGLETON = new EOElement();
  public Object apply(Object x) {
    if (!(x instanceof ElementOwnership)) return x;
    return ((ElementOwnership)x).getModelElement();
  }
} /* end class EOElement */
