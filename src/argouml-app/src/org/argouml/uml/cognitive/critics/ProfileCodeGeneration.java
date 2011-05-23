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

import org.argouml.cognitive.CompoundCritic;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Translator;
import org.argouml.model.Model;
import org.argouml.profile.Profile;

/**
 * Profile which contains the critics that define stricter good practices for
 * UML models targeted for code generation
 * 
 * @author maurelio1234
 */
public class ProfileCodeGeneration extends Profile {

    private Set<Critic>  critics = null;

    private static Critic crMissingClassName;
    
    private static Critic crDisambigClassName = new CrDisambigClassName();

    private static Critic crNoTransitions = new CrNoTransitions();

    private static Critic crNoIncomingTransitions =
        new CrNoIncomingTransitions();

    private static Critic crNoOutgoingTransitions =
        new CrNoOutgoingTransitions();
        
    // Compound critics

    // only classes with name need a constructor
    private static CompoundCritic crCompoundConstructorNeeded;

    private static CompoundCritic clsNaming;
    
    private static CompoundCritic noTrans1 =
        new CompoundCritic(crNoTransitions, crNoIncomingTransitions);

    private static CompoundCritic noTrans2 =
        new CompoundCritic(crNoTransitions, crNoOutgoingTransitions);
        
    /**
     * Default Constructor 
     * 
     * @param profileGoodPractices the instance of the required profile 
     */
    public ProfileCodeGeneration(ProfileGoodPractices profileGoodPractices) {
        crMissingClassName = profileGoodPractices.getCrMissingClassName();
        addProfileDependency("GoodPractices");
    }

    private void loadCritics() {

        critics = new HashSet<Critic>();
        
        crCompoundConstructorNeeded = new CompoundCritic(
                crMissingClassName, new CrConstructorNeeded());

        clsNaming = new CompoundCritic(crMissingClassName,
                crDisambigClassName);
            
        critics.add(crCompoundConstructorNeeded);
        
        // code generation
        critics.add(clsNaming);
        critics.add(new CrDisambigStateName());
        critics.add(crDisambigClassName);
        critics.add(new CrIllegalName());
        critics.add(new CrReservedName());
        if (Model.getFacade().getUmlVersion().startsWith("1")) {
            critics.add(new CrNoInitialState());
        }
        critics.add(new CrNoTriggerOrGuard());
        critics.add(new CrNoGuard());
                   
        critics.add(new CrOperNameConflict());
        critics.add(new CrNoInstanceVariables());
        critics.add(new CrNoAssociations());
        critics.add(new CrNoOperations());
        critics.add(new CrUselessAbstract());
        critics.add(new CrUselessInterface());
        critics.add(new CrNavFromInterface());
        critics.add(new CrUnnavigableAssoc());
        critics.add(new CrAlreadyRealizes());
        critics.add(new CrMultipleInitialStates());
        critics.add(new CrUnconventionalOperName());
        critics.add(new CrUnconventionalAttrName());
        critics.add(new CrUnconventionalClassName());
        critics.add(new CrUnconventionalPackName());
        critics.add(new CrNodeInsideElement());
        critics.add(new CrNodeInstanceInsideElement());
        critics.add(new CrComponentWithoutNode());
        critics.add(new CrCompInstanceWithoutNode());
        critics.add(new CrClassWithoutComponent());
        critics.add(new CrInterfaceWithoutComponent());
        critics.add(new CrObjectWithoutComponent());
        critics.add(new CrInstanceWithoutClassifier());
        critics.add(noTrans1);
        critics.add(noTrans2);                                  
        
        this.setCritics(critics);
    }
    
    @Override
    public Set<Critic> getCritics() {
        if (critics == null) {
            loadCritics();
        }
        return super.getCritics();
    }
    
    @Override
    public String getDisplayName() {
        return Translator.localize(
                "misc.project.profile-critics-for-codegeneration");
    }

   /*
    * @see org.argouml.profile.Profile#getProfileIdentifier()
    */
    public String getProfileIdentifier() {
        return "CodeGeneration";
    }
}
