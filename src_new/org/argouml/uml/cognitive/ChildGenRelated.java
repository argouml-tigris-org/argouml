// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.uml.cognitive;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.model.Model;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.util.ChildGenerator;

/**
 * @stereotype singleton
 * @author jrobbins
 */
public class ChildGenRelated implements ChildGenerator {
    private static final ChildGenRelated SINGLETON = new ChildGenRelated();

    /**
     * @return Returns the singleton.
     */
    public static ChildGenRelated getSingleton() {
        return SINGLETON;
    }
    /**
     * Reply a java.util.Enumeration of the children of the given Object
     * Returns an enumeration or null if not possible to get the children.
     *
     * @see org.tigris.gef.util.ChildGenerator#gen(java.lang.Object)
     */
    public Enumeration gen(Object o) {

	Vector res = new Vector();

	if (Model.getFacade().isAPackage(o)) {
	    Collection ownedElements = Model.getFacade().getOwnedElements(o);
	    if (ownedElements != null)
		return null;
	}

	if (Model.getFacade().isAClassifier(o)) {
	    Object cls = /*(MClassifier)*/ o;
	    Collection assocEnds = Model.getFacade().getAssociationEnds(cls);
	    Iterator assocIterator = assocEnds.iterator();
	    while (assocIterator.hasNext()) {
		res.add(Model.getFacade().getAssociation(assocIterator.next()));
	    }

	    res.addAll(Model.getFacade().getFeatures(cls));
	    res.addAll(Model.getFacade().getBehaviors(cls));
	    return res.elements();
	}

	if (Model.getFacade().isAAssociation(o)) {
	    Object asc = /*(MAssociation)*/ o;
	    Collection assocEnds = Model.getFacade().getConnections(asc);
	    Iterator iter = assocEnds.iterator();
	    while (iter.hasNext()) {
		res.add(Model.getFacade().getType(iter.next()));
	    }
	    return res.elements();
	}

	if (Model.getFacade().isAStateMachine(o)) {
	    Object sm = /*(MStateMachine)*/ o;
	    Object top = Model.getFacade().getTop(sm);
	    if (top != null)
		res.addAll(Model.getFacade().getSubvertices(top));
	    res.add(Model.getFacade().getContext(sm)); //wasteful!
	    res.addAll(Model.getFacade().getTransitions(sm));
	    return res.elements();
	}

	if (Model.getFacade().isAStateVertex(o)) {
	    Object sv = /*(MStateVertex)*/ o;
	    res.addAll(Model.getFacade().getIncomings(sv));
	    res.addAll(Model.getFacade().getOutgoings(sv));

	    if (Model.getFacade().isAState(o)) {
		Object s = /*(MState)*/ o;
		res.addAll(Model.getFacade().getInternalTransitions(s));
	    }

	    if (Model.getFacade().isACompositeState(o)) {
		Object cs = /*(MCompositeState)*/ o;
		res.addAll(Model.getFacade().getSubvertices(cs));
	    }
	    return res.elements();
	}

	if (Model.getFacade().isATransition(o)) {
	    Object tr = /*(MTransition)*/ o;
	    res.add(Model.getFacade().getTrigger(tr));
	    res.add(Model.getFacade().getGuard(tr));
	    res.add(Model.getFacade().getEffect(tr));
	    res.add(Model.getFacade().getSource(tr));
	    res.add(Model.getFacade().getTarget(tr));
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
