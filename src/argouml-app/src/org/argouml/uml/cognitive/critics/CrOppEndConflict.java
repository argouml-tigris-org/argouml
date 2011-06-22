/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    maurelio1234
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
import java.util.List;
import java.util.Set;

import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;

/**
 * Well-formedness rule [2] for MClassifier. See page 29 of UML 1.1
 * Semantics. OMG document ad/97-08-04.
 *
 * Well-formedness rule [3] for Classifier. See page 55 of UML 1.4
 * Semantics. OMG document UML 1.4.2 formal/04-07-02.
 * @author jrobbins
 */
//TODO: split into an inherited attr critic and a local
//attr critic
public class CrOppEndConflict extends CrUML {

    /**
     * The constructor.
     */
    public CrOppEndConflict() {
        setupHeadAndDesc();
        addSupportedDecision(UMLDecision.INHERITANCE);
        addSupportedDecision(UMLDecision.RELATIONSHIPS);
        addSupportedDecision(UMLDecision.NAMING);
        setKnowledgeTypes(Critic.KT_SYNTAX);
        addTrigger("associationEnd");
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *      java.lang.Object, org.argouml.cognitive.Designer)
     */
    @Override
    public boolean predicate2(Object dm, Designer dsgr) {
        boolean problem = NO_PROBLEM;
        if (Model.getFacade().isAClassifier(dm)) {
            Collection col = Model.getCoreHelper().getAssociations(dm);
            List names = new ArrayList();
            Iterator it = col.iterator();
            String name = null;
            while (it.hasNext()) {
                name = Model.getFacade().getName(it.next());
                if (name == null || name.equals("")) {
                    continue;
                }
                if (names.contains(name)) {
                    problem = PROBLEM_FOUND;
                    break;
                }
            }
        }
        return problem;
    }
    
    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#getCriticizedDesignMaterials()
     */
    @Override
    public Set<Object> getCriticizedDesignMaterials() {
        Set<Object> ret = new HashSet<Object>();
        ret.add(Model.getMetaTypes().getAssociationEnd());
        return ret;
    }
    
} /* end class CrOppEndConflict */
