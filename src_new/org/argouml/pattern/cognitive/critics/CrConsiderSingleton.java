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

// File: CrConsiderSingleton.java
// Classes: CrConsiderSingleton
// Original Author: jrobbins@ics.uci.edu

package org.argouml.pattern.cognitive.critics;

import java.util.*;

import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;
import org.argouml.uml.*;
import org.argouml.uml.cognitive.critics.*;

// Use Model through NsumlModelFacade.
import org.argouml.api.FacadeManager;
import org.argouml.model.uml.NsumlModelFacade;

/**
 * <p>A critic to detect when a class can never have more than one instance (of
 * itself of any subclasses), and thus whether it is suitable for declaration
 * as a Singleton (with stereotype &laquo;Singleton&raquo;.<p>
 *
 * @see <a href="http://argouml.tigris.org/documentation/snapshots/manual/argouml.html/#s2.ref.critics_singleton_violated">ArgoUML User Manual: Singleton Violated</a>
 */
public class CrConsiderSingleton extends CrUML {

    /**
     * <p>Constructor for the critic.</p>
     *
     * <p>Sets up the resource name, which will allow headline and description
     * to be found for the current locale. Provides a design issue category
     * (PATTERNS), sets a priority for any to-do items (LOW) and adds triggers
     * for metaclasses "stereotype", "structuralFeature" and
     * "associationEnd".</p>
     */

    public CrConsiderSingleton() {

        setResource("CrConsiderSingleton");

        addSupportedDecision(CrUML.decPATTERNS);
        setPriority(ToDoItem.LOW_PRIORITY);

        // These may not actually make any difference at present (the code
        // behind addTrigger needs more work).

        addTrigger("stereotype");
        addTrigger("structuralFeature");
        addTrigger("associationEnd");
    }


    /**
     * <p>The trigger for the critic.</p>
     *
     * <p>First check we are already a Singleton.</p>
     *
     * <p>Otherwise plausible candidates for the Singleton design pattern are
     * classes with no instance variables (i.e. non-static attributes) and no
     * outgoing associations.<p>
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

        // Only look at classes...

        if (!(FacadeManager.getUmlFacade().isAClass(dm))) {
            return NO_PROBLEM;
        }
        
        // with a name...
        if (FacadeManager.getUmlFacade().getName(dm) == null ||
            "".equals(FacadeManager.getUmlFacade().getName(dm))) {
                return NO_PROBLEM;
        }
        
        // ... and not incompletely imported
        if (!(FacadeManager.getUmlFacade().isPrimaryObject(dm))) return NO_PROBLEM;

        // Check for Singleton stereotype, uninitialised instance variables and
        // outgoing associations, as per JavaDoc above.

        if (FacadeManager.getUmlFacade().isSingleton(dm)) {
            return NO_PROBLEM;
        }

	if (FacadeManager.getUmlFacade().isUtility(dm)) {
	    return NO_PROBLEM;
	}

	// If there is an attribute with instance scope => no problem
	Iterator iter = FacadeManager.getUmlFacade().getAttributes(dm).iterator();

	while (iter.hasNext()) {
	    if (FacadeManager.getUmlFacade().isInstanceScope(iter.next()))
		return NO_PROBLEM;
	}


	// If there is an outgoing association => no problem
	Iterator ends = FacadeManager.getUmlFacade().getAssociationEnds(dm).iterator();

	while (ends.hasNext()) {
	    Iterator otherends = 
		FacadeManager.getUmlFacade().getOtherAssociationEnds(ends.next()).iterator();

	    while (otherends.hasNext()) {
		if (FacadeManager.getUmlFacade().isNavigable(otherends.next()))
		    return NO_PROBLEM;
	    }
	}

	return PROBLEM_FOUND;
    }

} /* end class CrConsiderSingleton */

