// $Id$
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

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.model.ModelFacade;

import org.tigris.gef.base.Diagram;
import org.tigris.gef.util.ChildGenerator;

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
		
	if (ModelFacade.isAPackage(o)) {
	    Collection ownedElements = ModelFacade.getOwnedElements(o);
	    if (ownedElements != null)
		return null;
	}
		
	if (ModelFacade.isAClassifier(o)) {
	    Object cls = /*(MClassifier)*/ o;
	    Collection assocEnds = ModelFacade.getAssociationEnds(cls);
	    Iterator assocIterator = assocEnds.iterator();
	    while (assocIterator.hasNext()) {
		res.add(ModelFacade.getAssociation(assocIterator.next()));
	    }

	    res.addAll(ModelFacade.getFeatures(cls));
	    res.addAll(ModelFacade.getBehaviors(cls));
	    return res.elements();
	}
		
	if (ModelFacade.isAAssociation(o)) {
	    Object asc = /*(MAssociation)*/ o;
	    Collection assocEnds = ModelFacade.getConnections(asc);
	    Iterator iter = assocEnds.iterator();
	    while (iter.hasNext()) {
		res.add(ModelFacade.getType(iter.next()));
	    }
	    return res.elements();
	}
		
	if (ModelFacade.isAStateMachine(o)) {
	    Object sm = /*(MStateMachine)*/ o;
	    Object top = ModelFacade.getTop(sm);
	    if (top != null)
		res.addAll(ModelFacade.getSubvertices(top));
	    res.add(ModelFacade.getContext(sm)); //wasteful!
	    res.addAll(ModelFacade.getTransitions(sm));
	    return res.elements();
	}
		
	if (ModelFacade.isAStateVertex(o)) {
	    Object sv = /*(MStateVertex)*/ o;
	    res.addAll(ModelFacade.getIncomings(sv));
	    res.addAll(ModelFacade.getOutgoings(sv));
			
	    if (ModelFacade.isAState(o)) {
		Object s = /*(MState)*/ o;
		res.addAll(ModelFacade.getInternalTransitions(s));
	    }
			
	    if (ModelFacade.isACompositeState(o)) {
		Object cs = /*(MCompositeState)*/ o;
		res.addAll(ModelFacade.getSubvertices(cs));
	    }
	    return res.elements();
	}
		
	if (ModelFacade.isATransition(o)) {
	    Object tr = /*(MTransition)*/ o;
	    res.add(ModelFacade.getTrigger(tr));
	    res.add(ModelFacade.getGuard(tr));
	    res.add(ModelFacade.getEffect(tr));
	    res.add(ModelFacade.getSource(tr));
	    res.add(ModelFacade.getTarget(tr));
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