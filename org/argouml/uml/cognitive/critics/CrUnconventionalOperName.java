
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



// File: CrUnconventionalOperName.java
// Classes: CrUnconventionalOperName
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.util.*;

import org.argouml.kernel.*;
import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;

public class CrUnconventionalOperName extends CrUML {

    public CrUnconventionalOperName() {
	setHeadline("Choose a Better MOperation Name");
	addSupportedDecision(CrUML.decNAMING);
	setKnowledgeTypes(Critic.KT_SYNTAX);
	addTrigger("feature_name");
    }

    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(org.argouml.model.ModelFacade.isAOperation(dm))) return NO_PROBLEM;
	MOperation oper = (MOperation) dm;
	String myName = oper.getName();
	if (myName == null || myName.equals("")) return NO_PROBLEM;
	String nameStr = myName;
	if (nameStr == null || nameStr.length() == 0) return NO_PROBLEM;
	char initalChar = nameStr.charAt(0);
	if ((oper.getStereotype() != null) && 
	    ("create".equals(oper.getStereotype().getName()) ||
	     "constructor".equals(oper.getStereotype().getName())))
	    return NO_PROBLEM;
	if (!Character.isLowerCase(initalChar)) return PROBLEM_FOUND;
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

    public boolean stillValid(ToDoItem i, Designer dsgr) {
	if (!isActive()) return false;
	VectorSet offs = i.getOffenders();
	MFeature f = (MFeature) offs.firstElement();
	if (!predicate(f, dsgr)) return false;
	VectorSet newOffs = computeOffenders(f);
	boolean res = offs.equals(newOffs);
	return res;
    }


    /** candidateForConstructor tests if the operation name is the same
     * as the class name. If so, an alternative path in the wizard is 
     * possible where we are suggested to make the operation a constructor.
     */
    protected boolean candidateForConstructor(MModelElement me) {
	if (!(org.argouml.model.ModelFacade.isAOperation(me))) return false;
	MOperation oper = (MOperation) me;
	String myName = oper.getName();
	if (myName == null || myName.equals("")) return false;
	MModelElement cl = oper.getOwner();
	String nameCl = cl.getName();
	if (nameCl == null || nameCl.equals("")) return false;
	if (myName.equals(nameCl)) return true;
	return false;
    }


    public void initWizard(Wizard w) {
	if (w instanceof WizOperName) {
	    ToDoItem item = w.getToDoItem();
	    MModelElement me = (MModelElement) item.getOffenders().elementAt(0);
	    String sug = me.getName();
	    sug = sug.substring(0, 1).toLowerCase() + sug.substring(1);
	    boolean cand = candidateForConstructor(me);
	    String ins = "Change the operation name to start with a " +
		"lowercase letter";
	    if (cand)
		ins = ins + " or make it a constructor";
	    ins = ins + ".";
	    ((WizOperName) w).setInstructions(ins);
	    ((WizOperName) w).setSuggestion(sug);
	    ((WizOperName) w).setPossibleConstructor(cand);
	}
    }
    public Class getWizardClass(ToDoItem item) { return WizOperName.class; }

} /* end class CrUnconventionalOperName */
