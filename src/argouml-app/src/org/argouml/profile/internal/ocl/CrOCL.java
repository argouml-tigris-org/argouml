/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
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

package org.argouml.profile.internal.ocl;

import java.util.List;
import java.util.Set;

import org.argouml.cognitive.Decision;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.profile.internal.ocl.uml14.Uml14ModelInterpreter;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.critics.CrUML;

/**
 * Represents an critics defined as an OCL expression in a profile
 * 
 * @author maurelio1234
 */
public class CrOCL extends CrUML {

    /**
     * the OCL Interpreter
     */
    private OclInterpreter interpreter = null;

    /**
     * the OCL string
     */
    private String ocl = null;

    /**
     * the design materials to be criticized
     */
    private Set<Object> designMaterials;
    
    /**
     * Creates a new OCL critic
     * 
     * @param oclConstraint ocl expression
     * @param headline headline
     * @param description description
     * @param moreInfoURL the info url
     * @param knowledgeTypes the knowledge types
     * @param supportedDecisions the decisions
     * @param priority the priority
     * @throws InvalidOclException if the ocl is not valid
     * 
     * TODO: Do these need to be Lists or can they be simple Collections?
     */
    public CrOCL(String oclConstraint, String headline, String description,
            Integer priority, List<Decision> supportedDecisions,
            List<String> knowledgeTypes, String moreInfoURL)
        throws InvalidOclException {
        interpreter = 
            new OclInterpreter(oclConstraint, new Uml14ModelInterpreter());
        this.ocl = oclConstraint;
        
        addSupportedDecision(UMLDecision.PLANNED_EXTENSIONS);
        setPriority(ToDoItem.HIGH_PRIORITY);

        List<String> triggers = interpreter.getTriggers();
        designMaterials = interpreter.getCriticizedDesignMaterials();
        
        for (String string : triggers) {
            addTrigger(string);
        }

        if (headline == null) {
            super.setHeadline("OCL Expression");
        } else {
            super.setHeadline(headline);
        }

        if (description == null) {
            super.setDescription("");
        } else {
            super.setDescription(description);
        }

        if (priority == null) {
            setPriority(ToDoItem.HIGH_PRIORITY);
        } else {
            setPriority(priority);
        }

        if (supportedDecisions != null) {
            for (Decision d : supportedDecisions) {
                addSupportedDecision(d);
            }
        }

        if (knowledgeTypes != null) {
            for (String k : knowledgeTypes) {
                addKnowledgeType(k);
            }
        }

        if (moreInfoURL != null) {
            setMoreInfoURL(moreInfoURL);
        }
    }
    

    /*
     * @see org.argouml.cognitive.Critic#getCriticizedDesignMaterials()
     */
    @Override
    public Set<Object> getCriticizedDesignMaterials() {
        return designMaterials;
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(java.lang.Object,
     *      org.argouml.cognitive.Designer)
     */
    @Override
    public boolean predicate2(Object dm, Designer dsgr) {
        if (!interpreter.applicable(dm)) {
            return NO_PROBLEM;
        } else {
            if (interpreter.check(dm)) {
                return NO_PROBLEM;
            } else {
                return PROBLEM_FOUND;
            }
        }
    }

    /**
     * @return the ocl constraint
     */
    public String getOCL() {
        return ocl;
    }

}
