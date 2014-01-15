/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2014 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    maurelio1234
 *    Bob Tarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2006 The Regents of the University of California. All
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;

/**
 * Well-formedness rule [2] for Classifier. See page 29 of UML 1.1
 * Semantics. OMG document ad/97-08-04.
 *
 * Well-formedness rule [4] for Classifier. See page 55 of UML 1.4
 * Semantics. OMG document UML 1.4.2 formal/04-07-02.
 * 
 * @author jrobbins
 */
//TODO: split into one critic for inherited problems and
//one for problems directly in this class.
public class CrOppEndVsAttr extends CrUML {

    /**
     * The constructor.
     */
    public CrOppEndVsAttr() {
        setupHeadAndDesc();
        addSupportedDecision(UMLDecision.INHERITANCE);
        addSupportedDecision(UMLDecision.RELATIONSHIPS);
        addSupportedDecision(UMLDecision.NAMING);
        setKnowledgeTypes(Critic.KT_SYNTAX);
        addTrigger("associationEnd");
        addTrigger("structuralFeature");
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *         java.lang.Object, org.argouml.cognitive.Designer)
     */
    @Override
    public boolean predicate2(Object dm, Designer dsgr) {
        if (!(Model.getFacade().isAClassifier(dm))) {
            return NO_PROBLEM;
        }
        Object cls = /*(Classifier)*/ dm;
        Collection<String> namesSeen = new ArrayList<String>();
        Collection features = Model.getFacade().getFeatures(cls);

        // warn about inherited name conflicts, different critic?
        for (Object feature : features) {
            if (Model.getFacade().isAStructuralFeature(feature)) {
                final String sfName = Model.getFacade().getName(feature);
                if (sfName != null && !"".equals(sfName)) {
                    namesSeen.add(sfName);
                }
            }
        }

        Collection assocEnds = Model.getFacade().getAssociationEnds(cls);

        // warn about inherited name conflicts, different critic?
        Iterator myEnds = assocEnds.iterator();
        while (myEnds.hasNext()) {
            Object myAe = /*(MAssociationEnd)*/ myEnds.next();
            Object asc =
                /*(MAssociation)*/
                Model.getFacade().getAssociation(myAe);
            Collection conn = Model.getFacade().getConnections(asc);

            if (Model.getFacade().isAAssociationRole(asc)) {
                conn = Model.getFacade().getConnections(asc);
            }
            if (conn == null) {
                continue;
            }

            for (Object ae : conn) {
                if (Model.getFacade().getType(ae) == cls) {
                    continue;
                }
                final String aeName = Model.getFacade().getName(ae);
                if (aeName==null || "".equals(aeName)) {
                    continue;
                }

                if (namesSeen.contains(aeName)) {
                    return PROBLEM_FOUND;
                }
            }
        }
        return NO_PROBLEM;
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#getCriticizedDesignMaterials()
     */
    @Override
    public Set<Object> getCriticizedDesignMaterials() {
        Set<Object> ret = new HashSet<Object>();
        ret.add(Model.getMetaTypes().getClassifier());
        return ret;
    }
    
    /**
     * The UID.
     */
    private static final long serialVersionUID = 5784567698177480475L;
}
