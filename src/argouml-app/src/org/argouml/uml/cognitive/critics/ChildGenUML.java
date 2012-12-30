/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.argouml.util.IteratorEnumeration;
import org.argouml.util.SingleElementIterator;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.util.ChildGenerator;

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

    private static final Logger LOG =
        Logger.getLogger(ChildGenUML.class.getName());

    /**
     * Reply a java.util.Enumeration of the children of the given Object
     *
     * @param o the object to return the children of
     * @return an enumeration of the children of the given Object
     * @see org.tigris.gef.util.ChildGenerator#gen(java.lang.Object)
     * @deprecated for 0.25.4 by tfmorris. Only for use with legacy GEF
     *             interfaces. Use {@link #gen2(Object)} for new applications.
     */
    @Deprecated
    public Enumeration gen(Object o) {
        return new IteratorEnumeration(gen2(o));
    }

    /**
     * Return an Iterator of the children of the given Object
     *
     * @param o object to return the children of
     * @return an iterator over the children of the given object
     * @see org.tigris.gef.util.ChildGenerator#gen(java.lang.Object)
     */
    public Iterator gen2(Object o) {

        if (o == null) {
            LOG.log(Level.FINE, "Object is null");
        } else {
//                LOG.log(Level.FINE, "Finding children for " + o.getClass());
        }

	if (o instanceof Project) {
	    Project p = (Project) o;
            Collection result = new ArrayList();
            result.addAll(p.getUserDefinedModelList());
            result.addAll(p.getDiagramList());
            return result.iterator();
	}

	if (o instanceof Diagram) {
	    Collection figs = ((Diagram) o).getLayer().getContents();
	    if (figs != null) {
	        return figs.iterator();
	    }
	}

	// argument can be an instanceof a Fig which we ignore

	if (Model.getFacade().isAPackage(o)) {
	    Collection ownedElements =
                Model.getFacade().getOwnedElements(o);
	    if (ownedElements != null) {
	        return ownedElements.iterator();
	    }
	}

	if (Model.getFacade().isAElementImport(o)) {
	    Object me = Model.getFacade().getModelElement(o);
	    if (me != null) {
	        return new SingleElementIterator(me);
	    }
	}


	// TODO: associationclasses fit both of the next 2 cases

	if (Model.getFacade().isAClassifier(o)) {
            Collection result = new ArrayList();
	    result.addAll(Model.getFacade().getFeatures(o));

	    Collection sms = Model.getFacade().getBehaviors(o);
	    //Object sm = null;
	    //if (sms != null && sms.size() > 0)
		//sm = sms.elementAt(0);
	    //if (sm != null) res.addSub(new EnumerationSingle(sm));
            if (sms != null) {
                result.addAll(sms);
            }
	    return result.iterator();
	}

	if (Model.getFacade().isAAssociation(o)) {
	    List assocEnds = (List) Model.getFacade().getConnections(o);
	    if (assocEnds != null) {
	        return assocEnds.iterator();
	    }
	    //TODO: AssociationRole
	}

	// // needed?
	if (Model.getFacade().isAStateMachine(o)) {
            Collection result = new ArrayList();
	    Object top = Model.getStateMachinesHelper().getTop(o);
	    if (top != null) {
                result.add(top);
	    }
	    result.addAll(Model.getFacade().getTransitions(o));
	    return result.iterator();
	}

	// needed?
	if (Model.getFacade().isACompositeState(o)) {
	    Collection substates = Model.getFacade().getSubvertices(o);
	    if (substates != null) {
	        return substates.iterator();
	    }
	}

        if (Model.getFacade().isAOperation(o)) {
            Collection params = Model.getFacade().getParameters(o);
            if (params != null) {
                return params.iterator();
            }
        }

        if (Model.getFacade().isAModelElement(o)) {
	    Collection behavior = Model.getFacade().getBehaviors(o);
	    if (behavior != null) {
	        return behavior.iterator();
	    }
	}

        // TODO: We can probably use this instead of all of the above
        // legacy UML 1.3 code - tfm - 20070915
        if (Model.getFacade().isAUMLElement(o)) {
            Collection result = Model.getFacade().getModelElementContents(o);
            return result.iterator();
        }

        return Collections.emptySet().iterator();
    }
}
