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

package org.argouml.uml.cognitive;

import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.base.Diagram;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.util.*;

/** @stereotype singleton
 */
public class ChildGenRelated implements ChildGenerator {
  public static ChildGenRelated SINGLETON = new ChildGenRelated();

    /** Reply a java.util.Enumeration of the children of the given Object 
     *
     * @return an enumeration or null if not possible to get the children.
     */
	public Enumeration gen(Object o) {
		
		Vector res = new Vector();
		
		if (o instanceof MPackage) {
			Collection ownedElements = ((MPackage)o).getOwnedElements();
			if (ownedElements != null)
		 
				//Enum is not used in Argo, why is it there?
				//return new Enum(ownedElements.elements(), EOElement.SINGLETON);
			return null;
		}
		
		if (o instanceof MClassifier) {
			MClassifier cls = (MClassifier) o;
			Collection assocEnds = cls.getAssociationEnds();
			Iterator assocIterator = assocEnds.iterator();
			while (assocIterator.hasNext()) {
				res.add(((MAssociationEnd)assocIterator.next()).getAssociation());
			}

			res.addAll(cls.getFeatures());
			res.addAll(cls.getBehaviors());
			return res.elements();
		}
		
		if (o instanceof MAssociation) {
			MAssociation asc = (MAssociation) o;
			List assocEnds = asc.getConnections();
			Iterator iter = assocEnds.iterator();
			while (iter.hasNext()) {
				res.add(((MAssociationEnd)iter.next()).getType());
			}
			return res.elements();
		}
		
		if (o instanceof MStateMachine) {
			MStateMachine sm = (MStateMachine) o;
			MState top = sm.getTop();
			if (top != null)
				res.addAll(((MCompositeState)top).getSubvertices());
			res.add(sm.getContext()); //wasteful!
			res.addAll(sm.getTransitions());
			return res.elements();
		}
		
		if (o instanceof MStateVertex) {
			MStateVertex sv = (MStateVertex) o;
			res.addAll(sv.getIncomings());
			res.addAll(sv.getOutgoings());
			
			if (o instanceof MState) {
				MState s = (MState) o;
				res.addAll(s.getInternalTransitions());
			}
			
			if (o instanceof MCompositeState) {
				MCompositeState cs = (MCompositeState) o;
				res.addAll(cs.getSubvertices());
			}
			return res.elements();
		}
		
		if (o instanceof MTransition) {
			MTransition tr = (MTransition) o;
			res.add(tr.getTrigger());
			res.add(tr.getGuard());
			res.add(tr.getEffect());
			res.add(tr.getSource());
			res.add(tr.getTarget());
			return res.elements();
		}
		
		// tons more cases
		
		if (o instanceof Diagram) {
			Diagram d = (Diagram) o;
			res.add(d.getGraphModel().getNodes());
			res.add(d.getGraphModel().getEdges());
		}
		return res.elements();
	}
} /* end class ChildGenRelated */


class EOElement implements Functor {
	public static EOElement SINGLETON = new EOElement();
	public Object apply(Object x) {
		if (!(x instanceof MElementImport)) return x;
		return ((MElementImport)x).getModelElement();
	}
} /* end class EOElement */
