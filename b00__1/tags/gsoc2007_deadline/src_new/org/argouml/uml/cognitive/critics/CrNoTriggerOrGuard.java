// $Id:CrNoTriggerOrGuard.java 12753 2007-06-04 18:07:56Z mvw $
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

import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;
/**
 * A critic that checks for missing trigger and/or guard.
 *
 *
 * @author jrobbins
 */
public class CrNoTriggerOrGuard extends CrUML {

    /**
     * The constructor.
     */
    public CrNoTriggerOrGuard() {
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.STATE_MACHINES);
	setKnowledgeTypes(Critic.KT_COMPLETENESS);
	addTrigger("trigger");
	addTrigger("guard");
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *      java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(Model.getFacade().isATransition(dm))) {
            return NO_PROBLEM;
        }
	
        Object transition = /*(MTransition)*/ dm;
        Object target = Model.getFacade().getTarget(transition);

        if (!(Model.getFacade().isAPseudostate(target))) {
            return NO_PROBLEM;
        }

	Object trigger = Model.getFacade().getTrigger(transition);
	Object guard = Model.getFacade().getGuard(transition);
	Object source = Model.getFacade().getSource(transition);
	
	
	//	 WFR Transitions, OMG UML 1.3
	Object k = Model.getFacade().getKind(target);
	if (Model.getFacade().
            equalsPseudostateKind(k,
                    Model.getPseudostateKind().getJoin())) {
            return NO_PROBLEM;
        }
	if (!(Model.getFacade().isAState(source))) {
            return NO_PROBLEM;
        }
	if (Model.getFacade().getDoActivity(source) != null) {
            return NO_PROBLEM;
        }
	boolean hasTrigger =
	    (trigger != null
            && Model.getFacade().getName(trigger) != null
            && Model.getFacade().getName(trigger).length() > 0);
	if (hasTrigger) {
            return NO_PROBLEM;
        }
	boolean noGuard =
            (guard == null
            || Model.getFacade().getExpression(guard) == null
            || Model.getFacade().getBody(
                Model.getFacade().getExpression(guard)) == null
            || Model
                .getFacade().getBody(Model.getFacade().getExpression(guard))
                .toString().length() == 0);
	if (noGuard) {
            return PROBLEM_FOUND;
        }
	return NO_PROBLEM;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -301548543890007262L;
}
