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
import java.util.Iterator;

import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;

/**
 * A critic to detect when a class requires a constructor.<p>
 *
 * The critic will trigger whenever a class has instance variables that are
 * uninitialised and there is no constructor. It will not trigger for
 * certain stereotyped classes.<p>
 *
 * This critic is part of a compound critic.<p>
 *
 * See the ArgoUML User Manual: Constructor Needed
 */
public class CrConstructorNeeded extends CrUML {

    /**
     * Constructor for the critic.<p>
     *
     * Sets up the resource name, which will allow headline and description
     * to found for the current locale. Provides a design issue category
     * (STORAGE) and adds triggers for metaclasses "behaviouralFeature" and
     * "structuralFeature".
     */
    public CrConstructorNeeded() {
        setupHeadAndDesc();
        addSupportedDecision(UMLDecision.STORAGE);
        addKnowledgeType(Critic.KT_CORRECTNESS);

        // These may not actually make any difference at present (the code
        // behind addTrigger needs more work).

        addTrigger("behavioralFeature");
        addTrigger("structuralFeature");
    }

    /**
     * The trigger for the critic.<p>
     *
     * First see if we have any instance variables that are not
     * initialised. If not there is no problem. If there are any uninitialised
     * instance variables, then look for a constructor.<p>
     *
     * @param  dm    the {@link java.lang.Object Object} to be checked against
     *               the critic.
     *
     * @param  dsgr  the {@link org.argouml.cognitive.Designer Designer}
     *               creating the model. Not used, this is for future
     *               development of ArgoUML.
     *
     * @return       {@link #PROBLEM_FOUND PROBLEM_FOUND} if the critic is
     *               triggered, otherwise {@link #NO_PROBLEM NO_PROBLEM}.
     */
    @Override
    public boolean predicate2(Object dm, Designer dsgr) {

        // Only look at classes
        if (!(Model.getFacade().isAClass(dm))) {
            return NO_PROBLEM;
        }


	// We don't consider secondary stuff.
	if (!(Model.getFacade().isPrimaryObject(dm)))
	    return NO_PROBLEM;

        // Types don't need a constructor.
        if (Model.getFacade().isType(dm)) {
            return NO_PROBLEM;
        }

        // Utilities usually do not require a constructor either
        if (Model.getFacade().isUtility(dm)) {
            return NO_PROBLEM;
        }

        // Check for uninitialised instance variables and
        // constructor.
        Collection operations = Model.getFacade().getOperations(dm);

        Iterator opers = operations.iterator();

        while (opers.hasNext()) {
            if (Model.getFacade().isConstructor(opers.next())) {
                // There is a constructor.
                return NO_PROBLEM;
            }
        }

        Iterator attrs = Model.getFacade().getAttributes(dm).iterator();

        while (attrs.hasNext()) {
            Object attr = attrs.next();

            if (Model.getFacade().isStatic(attr))
                continue;

            if (Model.getFacade().isInitialized(attr))
                continue;

            // We have found a non-static one that is not initialized.
            return PROBLEM_FOUND;
        }

        // yeah right...we don't have an operation (and thus no
        return NO_PROBLEM;
    }


    /*
     * @see org.argouml.cognitive.critics.Critic#initWizard(
     *      org.argouml.cognitive.ui.Wizard)
     */
    @Override
    public void initWizard(Wizard w) {
	if (w instanceof WizAddConstructor) {
	    ToDoItem item = (ToDoItem) w.getToDoItem();
	    Object me = item.getOffenders().get(0);
	    String ins = super.getInstructions();
	    String sug = null;
	    if (me != null)
		sug = Model.getFacade().getName(me);
	    if ("".equals(sug)) {
		sug = super.getDefaultSuggestion();
            }
	    ((WizAddConstructor) w).setInstructions(ins);
	    ((WizAddConstructor) w).setSuggestion(sug);
	}
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#getWizardClass(org.argouml.cognitive.ToDoItem)
     */
    @Override
    public Class getWizardClass(ToDoItem item) {
	return WizAddConstructor.class;
    }
}
