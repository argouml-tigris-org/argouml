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

import javax.swing.Icon;

import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;

/**
 * Critic to detect whether a package name obeys to certain rules:
 * it should only contain lower case alpha chars.
 */
public class CrUnconventionalPackName extends AbstractCrUnconventionalName {

    /**
     * The constructor.
     */
    public CrUnconventionalPackName() {
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.NAMING);
	setKnowledgeTypes(Critic.KT_SYNTAX);
	addTrigger("name");
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *      java.lang.Object, org.argouml.cognitive.Designer)
     */
    @Override
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(Model.getFacade().isAPackage(dm))) {
	    return NO_PROBLEM;
	}

	String myName = Model.getFacade().getName(dm);
	if (myName == null || myName.equals("")) {
	    return NO_PROBLEM;
	}
	String nameStr = myName;
	if (nameStr == null || nameStr.length() == 0) {
	    return NO_PROBLEM;
	}
	int size = nameStr.length();
	for (int i = 0; i < size; i++) {
	    char c = nameStr.charAt(i);
	    if (!Character.isLowerCase(c)) {
	        return PROBLEM_FOUND;
	    }
	}
	return NO_PROBLEM;
    }

    /*
     * @see org.argouml.cognitive.Poster#getClarifier()
     */
    public Icon getClarifier() {
	return ClClassName.getTheInstance();
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#initWizard(
     *         org.argouml.cognitive.ui.Wizard)
     */
    @Override
    public void initWizard(Wizard w) {
	if (w instanceof WizMEName) {
	    ToDoItem item = (ToDoItem) w.getToDoItem();
	    Object me = item.getOffenders().get(0);
	    String ins = super.getInstructions();
	    String nameStr = Model.getFacade().getName(me);
	    String sug = computeSuggestion(nameStr);
	    ((WizMEName) w).setInstructions(ins);
	    ((WizMEName) w).setSuggestion(sug);
	}
    }

    /*
     * @see org.argouml.uml.cognitive.critics.AbstractCrUnconventionalName#computeSuggestion(java.lang.String)
     */
    public String computeSuggestion(String nameStr) {

        String sug = "";
        if (nameStr != null) {
            int size = nameStr.length();
            for (int i = 0; i < size; i++) {
                char c = nameStr.charAt(i);
                if (Character.isLowerCase(c)) {
                    sug += c;
                } else if (Character.isUpperCase(c)) {
                    sug += Character.toLowerCase(c);
                }
            }
        }
        if (sug.equals("")) {
            sug = "packageName";
        }
        return sug;
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#getWizardClass(org.argouml.cognitive.ToDoItem)
     */
    @Override
    public Class getWizardClass(ToDoItem item) {
        return WizMEName.class;
    }

}
