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

// File: CrNoInstanceVariables.java
// Classes: CrNoInstanceVariables
// Original Author: jrobbins@ics.uci.edu

package org.argouml.uml.cognitive.critics;

import java.util.*;
import javax.swing.*;

import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;

// Uses Model through ModelFacade.
import org.argouml.model.ModelFacade;

/** A critic to detect if a class has instance variables.
 *  The critic fires currently only if a class and its base classes have
 *  no attributes at all.
 *  This is not neccesarily correct and the critic will have to deal with
 *  static attributes or attributes which are defined in a base class but are 
 *  private.
 */
public class CrNoInstanceVariables extends CrUML {

  public CrNoInstanceVariables() {
    setHeadline("Add Instance Variables to <ocl>self</ocl>");
    addSupportedDecision(CrUML.decSTORAGE);
    setKnowledgeTypes(Critic.KT_COMPLETENESS);
    addTrigger("structuralFeature");
  }

    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(ModelFacade.getInstance().isAClass(dm))) return NO_PROBLEM;

	if (!(ModelFacade.getInstance().isPrimaryObject(dm))) return NO_PROBLEM;

        // if the object does not have a name,
        // than no problem
        if ((ModelFacade.getInstance().getName(dm) == null) ||
            ("".equals(ModelFacade.getInstance().getName(dm))))
            return NO_PROBLEM;

	// types can probably have variables, but we should not nag at them
	// not having any.
	if (ModelFacade.getInstance().isType(dm)) return NO_PROBLEM;

	// utility is a namespace collection - also not strictly 
	// required to have variables.
	if (ModelFacade.getInstance().isUtility(dm)) return NO_PROBLEM;

	if (findChangeableInstanceAttributeInInherited(dm, 0))
	    return NO_PROBLEM;

	return PROBLEM_FOUND;
    }

    public Icon getClarifier() {
	return ClAttributeCompartment.TheInstance;
    }

    /**
     * Searches for attributes that are changeable instance attributes.
     *
     * @param handle the classifier to examine
     * @param number of levels searched
     * @returns true if an attribute can be found in this class
     *		or in any of its generalizations.
     */
    private boolean findChangeableInstanceAttributeInInherited(Object dm,
							       int depth) {

	Iterator enum = ModelFacade.getInstance().getAttributes(dm).iterator();

	while (enum.hasNext()) {
	    Object attr = enum.next();

	    // If we find an instance variable that is not a constant
	    // we have succeeded
	    if (ModelFacade.getInstance().isInstanceScope(attr)
		&& ModelFacade.getInstance().isChangeable(attr))
		return true;
	}

	// I am only prepared to go this far.
	if (depth > 50)
	    return false;

	Iterator iter = ModelFacade.getInstance().getGeneralizations(dm);

	while (iter.hasNext()) {
	    Object parent = ModelFacade.getInstance().getParent(iter.next());

	    if (parent == dm)
		continue;

	    if (ModelFacade.getInstance().isAClassifier(parent))
		if (findChangeableInstanceAttributeInInherited(parent,
							       depth + 1))
		    return true;
	}

	return false;
    }

} /* end class CrNoInstanceVariables */

