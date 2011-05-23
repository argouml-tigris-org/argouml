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

// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

import java.util.HashSet;
import java.util.Set;

import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Translator;
import org.argouml.model.Model;
import org.argouml.profile.Profile;

/**
 * Profile which contains the critics that define optional good practices for
 * general UML models
 * 
 * @author maurelio1234
 */
public class ProfileGoodPractices extends Profile {

    private Set<Critic>  critics = null;

    private CrMissingClassName crMissingClassName = new CrMissingClassName();

    /**
     * Default Constructor 
     */
    public ProfileGoodPractices() {
        super();
    }

    private void loadCritics() {
        // general
        critics = new HashSet<Critic>();
        critics.add(new CrEmptyPackage());
        critics.add(new CrNodesOverlap());
        critics.add(new CrZeroLengthEdge());
        critics.add(new CrCircularComposition());
        critics.add(new CrMissingAttrName());
        critics.add(crMissingClassName);
        critics.add(new CrMissingStateName());
        critics.add(new CrMissingOperName());
        critics.add(new CrNonAggDataType());
        critics.add(new CrSubclassReference());
        critics.add(new CrTooManyAssoc());
        critics.add(new CrTooManyAttr());
        critics.add(new CrTooManyOper());
        critics.add(new CrTooManyTransitions());
        if (Model.getFacade().getUmlVersion().startsWith("1")) {
            critics.add(new CrTooManyStates());
        }
        critics.add(new CrTooManyClasses());
        critics.add(new CrWrongLinkEnds());
        critics.add(new CrUtilityViolated());

        this.setCritics(critics);
    }
    
    @Override
    public Set<Critic> getCritics() {
        if (critics == null ) {
            loadCritics();
        }
        return super.getCritics();
    }
    
    @Override
    public String getDisplayName() {
        return Translator.localize(
                "misc.project.profile-critics-for-good-practices");
    }


    @Override
    public String getProfileIdentifier() {
        return "GoodPractices";
    }

    /**
     * @return the missing class name critic
     */
    public Critic getCrMissingClassName() {
        return crMissingClassName;
    }
}
