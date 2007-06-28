// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.util.ChildGenerator;
import org.tigris.gef.util.EnumerationComposite;
import org.tigris.gef.util.EnumerationEmpty;
import org.tigris.gef.util.EnumerationSingle;

/**
 * This class gives critics access to parts of the UML model of the
 * design.  It defines a gen() function that returns the "children"
 * of any given part of the UML model.  Basically, it goes from
 * Project, to Models, to ModelElements.  Argo's critic Agency uses
 * this to apply critics where appropriate.
 *
 * TODO: This thinks it knows all the composition associations of the
 * the UML metamodel, but it is a) incomplete and b) not updated for
 * UML 1.4.  This should be done using information from the metamodel
 * rather than hardwired code. - tfm - 20070205
 * 
 * @see org.argouml.cognitive.Agency
 * @see org.argouml.cognitive.Designer
 * @author jrobbins
 */
public class ChildGenUML implements ChildGenerator {

    private static final Logger LOG = Logger.getLogger(ChildGenUML.class);

    /**
     * Reply a java.util.Enumeration of the children of the given Object
     * TODO: GEF has moved away from vectors to collections
     * returning an iterator would now seem better.
     *
     * @see org.tigris.gef.util.ChildGenerator#gen(java.lang.Object)
     */
    public Enumeration gen(Object o) {

        if (o == null) {
            LOG.debug("Object is null");
        } else {
            LOG.debug("Findin g children for " + o.getClass());
        }

	if (o instanceof Project) {
	    Project p = (Project) o;
	    return new EnumerationComposite(p.getUserDefinedModels().elements(),
					    p.getDiagrams().elements());
	}

	if (o instanceof Diagram) {
	    Collection figs = ((Diagram) o).getLayer().getContents();
	    if (figs != null) {
	        return new Vector(figs).elements();
	    }
	}

	if (Model.getFacade().isAPackage(o)) {
	    Vector ownedElements =
		new Vector(Model.getFacade().getOwnedElements(o));
	    if (ownedElements != null) {
	        return ownedElements.elements();
	    }
	}

	if (Model.getFacade().isAElementImport(o)) {
	    Object me = Model.getFacade().getModelElement(o);
	    return new EnumerationSingle(me);  //wasteful!
	}


	// TODO: associationclasses fit both of the next 2 cases

	if (Model.getFacade().isAClassifier(o)) {
	    EnumerationComposite res = new EnumerationComposite();
	    res.addSub(new Vector(Model.getFacade().getFeatures(o)));

	    Vector sms = new Vector(Model.getFacade().getBehaviors(o));
	    //Object sm = null;
	    //if (sms != null && sms.size() > 0)
		//sm = sms.elementAt(0);
	    //if (sm != null) res.addSub(new EnumerationSingle(sm));
            if (sms != null) {
                res.addSub(sms.elements());
            }
	    return res;
	}

	if (Model.getFacade().isAAssociation(o)) {
	    Vector assocEnds = new Vector(Model.getFacade().getConnections(o));
	    if (assocEnds != null) {
	        return assocEnds.elements();
	    }
	    //TODO: MAssociationRole
	}

	// // needed?
	if (Model.getFacade().isAStateMachine(o)) {
	    EnumerationComposite res = new EnumerationComposite();
	    Object top = Model.getStateMachinesHelper().getTop(o);
	    if (top != null) {
	        res.addSub(new EnumerationSingle(top));
	    }
	    res.addSub(new Vector(Model.getFacade().getTransitions(o)));
	    return res;
	}

	// needed?
	if (Model.getFacade().isACompositeState(o)) {
	    Vector substates = new Vector(Model.getFacade().getSubvertices(o));
	    if (substates != null) {
	        return substates.elements();
	    }
	}

        if (Model.getFacade().isAOperation(o)) {
            Vector params = new Vector(Model.getFacade().getParameters(o));
            if (params != null) {
                return params.elements();
            }
        }

        if (Model.getFacade().isAModelElement(o)) {
	    Vector behavior = new Vector(Model.getFacade().getBehaviors(o));
	    if (behavior != null) {
	        return behavior.elements();
	    }
	}

	// tons more cases
        LOG.debug("No children found for: " + o.getClass());

	return EnumerationEmpty.theInstance();
    }
} /* end class ChildGenUML */
