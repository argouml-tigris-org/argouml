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

import java.util.Collection;
import java.util.Iterator;

import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.UMLToDoItem;

/**
 * Critic to detect whether an operation name obeys to certain rules.
 */
public class CrUnconventionalOperName extends AbstractCrUnconventionalName {

    /**
     * The constructor.
     */
    public CrUnconventionalOperName() {
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
        if (!(Model.getFacade().isAOperation(dm))) {
            return NO_PROBLEM;
        }
        Object oper = /*(MOperation)*/ dm;
        String myName = Model.getFacade().getName(oper);
        if (myName == null || myName.equals("")) {
            return NO_PROBLEM;
        }
        String nameStr = myName;
        if (nameStr == null || nameStr.length() == 0) {
            return NO_PROBLEM;
        }
        char initalChar = nameStr.charAt(0);
        
        Collection stereos = Model.getFacade().getStereotypes(oper);
        Iterator i = stereos.iterator();
        while (i.hasNext()) {
            Object stereo = i.next();
            if ("create".equals(Model.getFacade().getName(stereo))
                || "constructor".equals(Model.getFacade().getName(stereo))) {
                return NO_PROBLEM;
            }
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
     * @param dm the object to be checked
     * @return the set of offenders
     */
    protected ListSet computeOffenders(Object dm) {
	ListSet offs = new ListSet(dm);
	offs.addElement(Model.getFacade().getOwner(dm));
	return offs;
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
	Object f = offs.firstElement();
	if (!predicate(f, dsgr)) {
	    return false;
	}
	ListSet newOffs = computeOffenders(f);
	boolean res = offs.equals(newOffs);
	return res;
    }


    /**
     * CandidateForConstructor tests if the operation name is the same
     * as the class name. If so, an alternative path in the wizard is
     * possible where we are suggested to make the operation a constructor.
     *
     * @param me the operation to check
     * @return true if this operation looks like a constructor
     */
    protected boolean candidateForConstructor(Object me) {
	if (!(Model.getFacade().isAOperation(me))) {
	    return false;
	}
	Object oper = me;
	String myName = Model.getFacade().getName(oper);
	if (myName == null || myName.equals("")) {
	    return false;
	}
	Object cl = Model.getFacade().getOwner(oper);
	String nameCl = Model.getFacade().getName(cl);
	if (nameCl == null || nameCl.equals("")) {
	    return false;
	}
	if (myName.equals(nameCl)) {
	    return true;
	}
	return false;
    }


    /*
     * @see org.argouml.cognitive.critics.Critic#initWizard(
     *         org.argouml.cognitive.ui.Wizard)
     */
    public void initWizard(Wizard w) {
	if (w instanceof WizOperName) {
	    ToDoItem item = (ToDoItem) w.getToDoItem();
	    Object me = /*(MModelElement)*/ item.getOffenders().elementAt(0);
	    String sug = Model.getFacade().getName(me);
	    sug = computeSuggestion(sug);
	    boolean cand = candidateForConstructor(me);
	    String ins;
            if (cand) {
	        ins = super.getLocalizedString("-ins-ext");
	    } else {
                ins = super.getInstructions();       
            }
	    ((WizOperName) w).setInstructions(ins);
	    ((WizOperName) w).setSuggestion(sug);
	    ((WizOperName) w).setPossibleConstructor(cand);
	}
    }

    /*
     * @see org.argouml.uml.cognitive.critics.AbstractCrUnconventionalName#computeSuggestion(java.lang.String)
     */
    public String computeSuggestion(String sug) {
        if (sug == null) {
            return "";
        }
        return sug.substring(0, 1).toLowerCase() + sug.substring(1);
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#getWizardClass(org.argouml.cognitive.ToDoItem)
     */
    public Class getWizardClass(ToDoItem item) { return WizOperName.class; }

} /* end class CrUnconventionalOperName */
