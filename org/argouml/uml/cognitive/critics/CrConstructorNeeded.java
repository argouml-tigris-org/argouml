// $Id$
// Copyright (c) 1996-2003 The Regents of the University of California. All
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

// File: CrConstructorNeeded.java
// Classes: CrConstructorNeeded
// Original Author: jrobbins@ics.uci.edu

package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.Iterator;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Critic;
import org.argouml.kernel.Wizard;
import org.argouml.model.ModelFacade;

/**
 * <p> A critic to detect when a class requires a constructor.</p>
 *
 * <p>The critic will trigger whenever a class has instance variables that are
 * uninitialised and there is no constructor. It will not trigger for 
 * certain stereotyped classes.</p>
 * <p>this critic is part of a compound critic</p>
 *
 * @see <a href=
 * "http://argouml.tigris.org/documentation/snapshots/manual/argouml.html/
 * #s2.ref.critics_constructor_needed">
 * ArgoUML User Manual: Constructor Needed</a>
 */
public class CrConstructorNeeded extends CrUML {

    /**
     * <p>Constructor for the critic.</p>
     *
     * <p>Sets up the resource name, which will allow headline and description
     * to found for the current locale. Provides a design issue category
     * (STORAGE) and adds triggers for metaclasses "behaviouralFeature" and
     * "structuralFeature".</p>
     */

    public CrConstructorNeeded() {

        setResource("CrConstructorNeeded");

        addSupportedDecision(CrUML.DEC_STORAGE);
        addKnowledgeType(Critic.KT_CORRECTNESS);

        // These may not actually make any difference at present (the code
        // behind addTrigger needs more work).

        addTrigger("behavioralFeature");
        addTrigger("structuralFeature");
    }

    /**
     * <p>The trigger for the critic.</p>
     *
     * <p>First see if we have any instance variables that are not
     * initialised. If not there is no problem. If there are any uninitialised
     * instance variables, then look for a constructor.</p>
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

    public boolean predicate2(Object dm, Designer dsgr) {

        // Only look at classes
        if (!(ModelFacade.isAClass(dm))) {
            return NO_PROBLEM;
        }
        
            
	// We don't consider secondary stuff.
	if (!(ModelFacade.isPrimaryObject(dm))) 
	    return NO_PROBLEM;

        // Types don't need a constructor.
        if (ModelFacade.isType(dm)) {
            return NO_PROBLEM;
        }
        
        // Utilities usually do not require a constructor either
        if (ModelFacade.isUtility(dm)) {
            return NO_PROBLEM;
        }

        // Check for uninitialised instance variables and
        // constructor.
        Collection operations = ModelFacade.getOperations(dm);

        Iterator opers = operations.iterator();

        while (opers.hasNext()) {
            if (ModelFacade.isConstructor(opers.next())) {
                // There is a constructor.
                return NO_PROBLEM;
            }
        }

        Iterator attrs = ModelFacade.getAttributes(dm).iterator();

        while (attrs.hasNext()) {
            Object attr = attrs.next();

            if (!ModelFacade.isInstanceScope(attr))
                continue;

            if (ModelFacade.isInitialized(attr))
                continue;

            // We have found one with instance scope that is not initialized.
            return PROBLEM_FOUND;
        }

        // yeah right...we don't have an operation (and thus no 
        return NO_PROBLEM;
    }

    
    /**
     * @see org.argouml.cognitive.critics.Critic#initWizard(org.argouml.kernel.Wizard)
     */
    public void initWizard(Wizard w) {
	if (w instanceof WizAddConstructor) {
	    ToDoItem item = (ToDoItem) w.getToDoItem();
	    Object me = /*(MModelElement)*/ item.getOffenders().elementAt(0);
	    String ins = "Set the name of the new constructor.";
	    String sug = null;
	    if (me != null)
		sug = ModelFacade.getName(me);
	    if ("".equals(sug))
		sug = "newOperation";
	    ((WizAddConstructor) w).setInstructions(ins);
	    ((WizAddConstructor) w).setSuggestion(sug);
	}
    }
    
    /**
     * @see org.argouml.cognitive.critics.Critic#getWizardClass(org.argouml.cognitive.ToDoItem)
     */
    public Class getWizardClass(ToDoItem item) {
	return WizAddConstructor.class;
    }
} /* end class CrConstructorNeeded */
