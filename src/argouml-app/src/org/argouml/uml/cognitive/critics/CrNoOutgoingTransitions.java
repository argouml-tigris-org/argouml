/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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
import java.util.HashSet;
import java.util.Set;

import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;

/**
 * A critic to detect when a state has no outgoing transitions.
 * <p>
 * Also a transition from a state contained in the dm going out of the dm
 * counts as an "outgoing" transition thanks to issue 689.
 *
 * @author jrobbins
 */
public class CrNoOutgoingTransitions extends CrUML {

    /**
     * Constructor.
     */
    public CrNoOutgoingTransitions() {
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.STATE_MACHINES);
	addTrigger("outgoing");
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(java.lang.Object, org.argouml.cognitive.Designer)
     */
    @Override
    public boolean predicate2(Object dm, Designer dsgr) {
        if (!(Model.getFacade().isAStateVertex(dm))) {
            return NO_PROBLEM;
        }
        /* Now we are sure dm is a StateVertex. */

        if (Model.getFacade().isAPseudostate(dm)) {
            Object k = Model.getFacade().getKind(dm);
            if (k.equals(Model.getPseudostateKind().getChoice())) {
                return NO_PROBLEM;
            }
            if (k.equals(Model.getPseudostateKind().getJunction())) {
                return NO_PROBLEM;
            }
        }
        if (!Model.getFacade().isAState(dm)) {
            return NO_PROBLEM;
        }
        if (Model.getFacade().isAFinalState(dm)) {
            return NO_PROBLEM;
        }
        /* Now we are sure dm is a State. */
        Object stateMachine = Model.getFacade().getStateMachine(dm);
        if (stateMachine == null) {
            return NO_PROBLEM;
        }
        
        if (stateMachine != null && Model.getFacade().getTop(stateMachine) == dm) {
            /* If dm is the top state of the statemachine, then it is 
             * not supposed to have outgoing transitions. */
            return NO_PROBLEM;
        }

        Collection outgoing = Model.getFacade().getOutgoings(dm);
        if (outgoing == null || outgoing.size() > 0) {
            return NO_PROBLEM;
        }

        if (!Model.getFacade().isACompositeState(dm)) {
            return PROBLEM_FOUND;
        }
        /* Now we are sure dm is a Composite State. */

        /* Issue 689: Look for a transition that starts 
         * at a sub-state and goes out of the composite state: */
        Collection transitions = Model.getFacade().getTransitions(stateMachine);
        for (Object t : transitions) {
            Object sourceState = Model.getFacade().getSource(t);
            Object targetState = Model.getFacade().getTarget(t);
            if (isSomeSubvertexOf(sourceState, dm) && !isSomeSubvertexOf(targetState, dm)) {
                return NO_PROBLEM;
            }
        }

        return PROBLEM_FOUND;
    }

    /**
     * Test if a state is contained within a composite state recursively. 
     * This is done by checking if the parent of the subject 
     * equals the composite, or the parent of the parent, etc.
     * 
     * @param subject the StateVertex that is investigated
     * @param composite the Composite state that may or may not contain the subject
     * @return true if and only if the given composite contains recursively the given subject
     */
    private boolean isSomeSubvertexOf(Object subject, Object composite) {
        Object c = subject;
        while (c != null) {
            if (c == composite) {
                return true;
            }
            c = Model.getFacade().getContainer(c);
        }
        return false;
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#getCriticizedDesignMaterials()
     */
    @Override
    public Set<Object> getCriticizedDesignMaterials() {
        Set<Object> ret = new HashSet<Object>();
        ret.add(Model.getMetaTypes().getStateVertex());
        return ret;
    }
    
}
