// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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



// File: CrUnconventionalAttrName.java
// Classes: CrUnconventionalAttrName
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.util.*;

import org.argouml.kernel.*;
import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;


/** Critic to detect whether an attribute name obeys to certain rules.
 *  <p>
 *  Checks for:
 *  <ul>
 *  <li> all lower case or
 *  <li> all upper case
 *  </ul>
 *  where trailing underscores are removed, and
 *  constants are not nagged at.
 */
public class CrUnconventionalAttrName extends CrUML {

    public CrUnconventionalAttrName() {
	setHeadline("Choose a Better MAttribute Name");
	addSupportedDecision(CrUML.decNAMING);
	setKnowledgeTypes(Critic.KT_SYNTAX);
	addTrigger("feature_name");
    }


    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(dm instanceof MAttribute)) return NO_PROBLEM;
	MAttribute attr = (MAttribute) dm;
	String myName = attr.getName();
	if (myName == null || myName.equals("")) return NO_PROBLEM;
	String nameStr = myName;
	if (nameStr == null || nameStr.length() == 0) return NO_PROBLEM;
	// remove trailing underscores, if
	// remaining string is of zero length obviously this is a problem.
	while (nameStr.startsWith("_")) nameStr = nameStr.substring(1);
	if (nameStr.length() == 0) return PROBLEM_FOUND;

	// check for all uppercase and/or mixed with underspores
	char initalChar = nameStr.charAt(0);
	boolean allCapitals = true;
	for (int i = 0; i < nameStr.length() && allCapitals; i++) {
	    if (!(Character.isUpperCase(nameStr.charAt(i)) || 
		  nameStr.charAt(i) == '_')) {
		allCapitals = false;
		continue;
	    }
	}
	if (allCapitals) return NO_PROBLEM;

	// check whether constant, constants are often weird and thus not a
	// problem
	MChangeableKind ck = attr.getChangeability();
	if (MChangeableKind.FROZEN.equals(ck)) return NO_PROBLEM;
	if (!Character.isLowerCase(initalChar)) {
	    return PROBLEM_FOUND;
	}
	return NO_PROBLEM;
    }

    public ToDoItem toDoItem(Object dm, Designer dsgr) {
	MFeature f = (MFeature) dm;
	VectorSet offs = computeOffenders(f);
	return new ToDoItem(this, offs, dsgr);
    }

    protected VectorSet computeOffenders(MFeature dm) {
	VectorSet offs = new VectorSet(dm);
	offs.addElement(dm.getOwner());
	return offs;
    }

    public Icon getClarifier() {
	return ClAttributeCompartment.TheInstance;
    }

    public boolean stillValid(ToDoItem i, Designer dsgr) {
	if (!isActive()) return false;
	VectorSet offs = i.getOffenders();
	MFeature f = (MFeature) offs.firstElement();
	if (!predicate(f, dsgr)) return false;
	VectorSet newOffs = computeOffenders(f);
	boolean res = offs.equals(newOffs);
	return res;
    }


    public void initWizard(Wizard w) {
	if (w instanceof WizMEName) {
	    ToDoItem item = w.getToDoItem();
	    MModelElement me = (MModelElement) item.getOffenders().elementAt(0);
	    String sug = me.getName();
	    if (sug.startsWith("_"))
		sug = "_" + sug.substring(1, 2).toLowerCase() + sug.substring(2);
	    else
		sug = sug.substring(0, 1).toLowerCase() + sug.substring(1);
	    String ins = "Change the attribute name to start with a " +
		"lowercase letter.";
	    ((WizMEName) w).setInstructions(ins);
	    ((WizMEName) w).setSuggestion(sug);
	}
    }
    public Class getWizardClass(ToDoItem item) { return WizMEName.class; }

} /* end class CrUnconventionalAttrName */

