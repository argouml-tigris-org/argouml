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

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Critic;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.UMLToDoItem;

/**
 * Critic to detect whether an attribute name obeys to certain rules.<p>
 *
 * Checks for:
 * <ul>
 * <li> all lower case or
 * <li> all upper case
 * </ul>
 * where trailing underscores are removed, and
 * constants are not nagged at.
 */
public class CrUnconventionalAttrName extends AbstractCrUnconventionalName {

    /**
     * The constructor.
     */
    public CrUnconventionalAttrName() {
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.NAMING);
	setKnowledgeTypes(Critic.KT_SYNTAX);
	addTrigger("feature_name");
    }


    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *      java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!Model.getFacade().isAAttribute(dm)) {
	    return NO_PROBLEM;
	}

	Object attr = /*(MAttribute)*/ dm;
	String nameStr = Model.getFacade().getName(attr);
	if (nameStr == null || nameStr.equals("")) {
	    return NO_PROBLEM;
	}

	int pos = 0;
	int length = nameStr.length();

	for (; pos < length && nameStr.charAt(pos) == '_'; pos++) {
	}

	// If the name is only underscores
	if (pos >= length) {
	    return PROBLEM_FOUND;
	}

	// check for all uppercase and/or mixed with underscores
	char initalChar = nameStr.charAt(pos);
	boolean allCapitals = true;
	for (; pos < length; pos++) {
	    if (!Character.isUpperCase(nameStr.charAt(pos))
		&& nameStr.charAt(pos) != '_') {
		allCapitals = false;
		break;
	    }
	}
	if (allCapitals) {
	    return NO_PROBLEM;
	}

	// check whether constant, constants are often weird and thus not a
	// problem
	Object ck = Model.getFacade().getChangeability(attr);
	if (ck != null && Model.getFacade().isFrozen(ck)) {
	    return NO_PROBLEM;
	}

	if (!Character.isLowerCase(initalChar)) {
	    return PROBLEM_FOUND;
	}

	return NO_PROBLEM;
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#toDoItem( java.lang.Object,
     *      org.argouml.cognitive.Designer)
     */
    public ToDoItem toDoItem(Object dm, Designer dsgr) {
	Object f = dm;
	ListSet offs = computeOffenders(f);
	return new UMLToDoItem(this, offs, dsgr);
    }

    /**
     * @param dm the feature
     * @return the set of offenders
     */
    protected ListSet computeOffenders(Object dm) {
	ListSet offs = new ListSet(dm);
	offs.addElement(Model.getFacade().getOwner(dm));
	return offs;
    }

    /*
     * @see org.argouml.uml.cognitive.critics.AbstractCrUnconventionalName#computeSuggestion(java.lang.String)
     */
    public String computeSuggestion(String name) {
	String sug;
	int nu;

	if (name == null) {
	    return "attr";
	}

	for (nu = 0; nu < name.length(); nu++) {
	    if (name.charAt(nu) != '_') {
		break;
	    }
	}

	if (nu > 0) {
	    sug = name.substring(0, nu);
	} else {
	    sug = "";
	}

	if (nu < name.length()) {
	    sug += Character.toLowerCase(name.charAt(nu));
	}

	if (nu + 1 < name.length()) {
	    sug += name.substring(nu + 1);
	}

	return sug;
    }
    
    /*
     * @see org.argouml.cognitive.Poster#getClarifier()
     */
    public Icon getClarifier() {
	return ClAttributeCompartment.getTheInstance();
    }

    /*
     * @see org.argouml.cognitive.Poster#stillValid(
     *      org.argouml.cognitive.ToDoItem, org.argouml.cognitive.Designer)
     */
    public boolean stillValid(ToDoItem i, Designer dsgr) {
	if (!isActive()) {
	    return false;
	}
	ListSet offs = i.getOffenders();
	Object f = /*(MFeature)*/ offs.firstElement();
	if (!predicate(f, dsgr)) {
	    return false;
	}
	ListSet newOffs = computeOffenders(f);
	boolean res = offs.equals(newOffs);
	return res;
    }


    /*
     * @see org.argouml.cognitive.critics.Critic#initWizard(
     *         org.argouml.cognitive.ui.Wizard)
     */
    public void initWizard(Wizard w) {
	if (w instanceof WizMEName) {
	    ToDoItem item = (ToDoItem) w.getToDoItem();
	    Object me =
		/*(MModelElement)*/ item.getOffenders().elementAt(0);
	    String sug = computeSuggestion(Model.getFacade().getName(me));
	    String ins = super.getInstructions();
	    ((WizMEName) w).setInstructions(ins);
	    ((WizMEName) w).setSuggestion(sug);
	}
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#getWizardClass(org.argouml.cognitive.ToDoItem)
     */
    public Class getWizardClass(ToDoItem item) { return WizMEName.class; }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 4741909365018862474L;

} /* end class CrUnconventionalAttrName */
