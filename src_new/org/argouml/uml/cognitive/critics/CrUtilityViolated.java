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
// File: CrUtilityViolated.java
// Classes: CrUtilityViolated
// Original Author: jrobbins@ics.uci.edu

package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.argouml.cognitive.Designer;
import org.argouml.api.model.FacadeManager;
import org.argouml.model.uml.NsumlModelFacade;
import org.argouml.model.uml.foundation.core.CoreHelper;

/** A critic to detect when a class can never have instances (of
 *  itself of any subclasses). This is done by checking that there
 *  are no instance operations or attributes in the class itself
 *  or in any of the realized interfaces or inherited classes.
 */
public class CrUtilityViolated extends CrUML {
    
    public CrUtilityViolated() {
        setHeadline("Remove instance variables from Utility Class");
        addSupportedDecision(CrUML.decSTORAGE);
        addSupportedDecision(CrUML.decSTEREOTYPES);
        addSupportedDecision(CrUML.decCLASS_SELECTION);
        addTrigger("stereotype");
        addTrigger("behavioralFeature");
    }
    
    public boolean predicate2(Object dm, Designer dsgr) {
        // we could check for base class of the stereotype but the 
	// condition normally covers it all.
        if (!(FacadeManager.getUmlFacade().isAClassifier(dm)))
	    return NO_PROBLEM;
	if (!(FacadeManager.getUmlFacade().isUtility(dm)))
	    return NO_PROBLEM;

	Collection classesToCheck = new ArrayList();
	classesToCheck.addAll(CoreHelper.getHelper().getSupertypes(dm));
	classesToCheck.addAll(
	    CoreHelper.getHelper().getAllRealizedInterfaces(dm));
	classesToCheck.add(dm);
	Iterator it = classesToCheck.iterator();
	while (it.hasNext()) {
	    Object o = it.next();
	    if (!FacadeManager.getUmlFacade().isAInterface(o)) {
		Iterator it2 = FacadeManager.getUmlFacade().getAttributes(o).iterator();
		while (it2.hasNext()) {
		    if (FacadeManager.getUmlFacade().isInstanceScope(it2.next())) {
			return PROBLEM_FOUND;
		    }
		}
	    }
	    Iterator it2 = FacadeManager.getUmlFacade().getOperations(o).iterator();
	    while (it2.hasNext()) {
		if (FacadeManager.getUmlFacade().isInstanceScope(it2.next())) {
		    return PROBLEM_FOUND;
		}
	    }
	}
        return NO_PROBLEM;
    }
    
} /* end class CrUtilityViolated */
