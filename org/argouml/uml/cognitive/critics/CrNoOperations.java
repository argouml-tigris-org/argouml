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

// File: CrNoOperations.javoa
// Classes: CrNoOperations
// Original Author: jrobbins@ics.uci.edu

package org.argouml.uml.cognitive.critics;

import java.util.*;
import javax.swing.*;

import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;

// Uses Model through ModelFacade.
import org.argouml.model.ModelFacade;


/** A critic to detect when a class or its base class doesn't 
 * have any operations.
 */
public class CrNoOperations extends CrUML {

    public CrNoOperations() {
	setHeadline("Add Operations to <ocl>self</ocl>");
	addSupportedDecision(CrUML.decBEHAVIOR);
	setKnowledgeTypes(Critic.KT_COMPLETENESS);
	addTrigger("behavioralFeature");
    }

    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(ModelFacade.isAClass(dm))) return NO_PROBLEM;

	if (!(ModelFacade.isPrimaryObject(dm))) return NO_PROBLEM;

        // if the object does not have a name,
        // than no problem
        if ((ModelFacade.getName(dm) == null) ||
            ("".equals(ModelFacade.getName(dm))))
            return NO_PROBLEM;

 	// types can probably contain operations, but we should not nag at them
	// not having any.
	if (ModelFacade.isType(dm)) return NO_PROBLEM;

	// utility is a namespace collection - also not strictly 
	// required to have operations.
	if (ModelFacade.isUtility(dm)) return NO_PROBLEM;

	//TODO: different critic or special message for classes
	//that inherit all ops but define none of their own.
	
	if (findInstanceOperationInInherited(dm, 0))
	    return NO_PROBLEM;

	return PROBLEM_FOUND;
    }

    public Icon getClarifier() {
	return ClOperationCompartment.TheInstance;
    }

    private boolean findInstanceOperationInInherited(Object dm, int depth)
    {
	Iterator enum = ModelFacade.getOperations(dm).iterator();

	while (enum.hasNext()) {
	    if (ModelFacade.isInstanceScope(enum.next()))
		return true;
	}

	if (depth > 50)
	    return false;

	Iterator iter = ModelFacade.getGeneralizations(dm);

	while (iter.hasNext()) {
	    Object parent = ModelFacade.getParent(iter.next());

	    if (parent == dm)
		continue;

	    if (ModelFacade.isAClassifier(parent))
		if (findInstanceOperationInInherited(parent, depth + 1))
		    return true;
	}

	return false;
    }
} /* end class CrNoOperations */

