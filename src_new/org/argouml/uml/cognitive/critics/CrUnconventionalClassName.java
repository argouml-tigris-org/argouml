
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



// File: CrUnconventionalClassName.java
// Classes: CrUnconventionalClassName
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;

import org.argouml.kernel.*;
import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;

public class CrUnconventionalClassName extends CrUML {

    public CrUnconventionalClassName() {
	setHeadline("Capitalize Class Name <ocl>self</ocl>");
	addSupportedDecision(CrUML.decNAMING);
	setKnowledgeTypes(Critic.KT_SYNTAX);
	addTrigger("name");
    }

    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(org.argouml.model.ModelFacade.isAClass(dm)) && !(org.argouml.model.ModelFacade.isAInterface(dm)))
	    return NO_PROBLEM;
	MClassifier cls = (MClassifier) dm;
	String myName = cls.getName();
	if (myName == null || myName.equals("")) return NO_PROBLEM;
	String nameStr = myName;
	if (nameStr == null || nameStr.length() == 0) return NO_PROBLEM;
	char initialChar = nameStr.charAt(0);
	if (!Character.isUpperCase(initialChar)) return PROBLEM_FOUND;
	return NO_PROBLEM;
    }

    public Icon getClarifier() {
	return ClClassName.TheInstance;
    }

    public void initWizard(Wizard w) {
	if (w instanceof WizMEName) {
	    ToDoItem item = w.getToDoItem();
	    MModelElement me = (MModelElement) item.getOffenders().elementAt(0);
	    String sug = me.getName();
	    sug = sug.substring(0, 1).toUpperCase() + sug.substring(1);
	    String ins = "Change the class name to start with an " +
		"uppercase letter.";
	    ((WizMEName) w).setInstructions(ins);
	    ((WizMEName) w).setSuggestion(sug);
	}
    }
    public Class getWizardClass(ToDoItem item) { return WizMEName.class; }

} /* end class CrUnconventionalClassName */
