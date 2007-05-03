// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import javax.swing.Icon;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Critic;
import org.argouml.cognitive.ui.Wizard;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;

/**
 * Well-formedness rule for Package. Check that the alias for an imported
 * element doesn't conflict with any existing name. Section 2.14.3.3 of UML 1.4
 * spec.
 * <p>
 * Other types of name conflicts in a Namespace are checked by
 * {@link CrNameConflict}.
 * <p>
 * TODO: MVW: If an Alias is the same as another class' name, then this critic
 * fires, but the explanation and wizard are wrong!
 */
public class CrDisambigClassName extends CrUML {

    /**
     * The constructor.
     */
    public CrDisambigClassName() {
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.NAMING);
	setKnowledgeTypes(Critic.KT_SYNTAX);
	addTrigger("name");
	addTrigger("elementOwnership");
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *      java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
        // TODO: The WFR doesn't restrict this to Classifiers - tfm
	if (!(Model.getFacade().isAClassifier(dm))) {
	    return NO_PROBLEM;
	}
	Object classifier = dm;
	String designMaterialName = Model.getFacade().getName(classifier);
	//@ if (myNameString.equals(Name.UNSPEC)) return NO_PROBLEM;

	if (designMaterialName != null && designMaterialName.length() == 0) {
	    return NO_PROBLEM;
	}

	Collection elementImports = 
            Model.getFacade().getElementImports2(classifier);
	if (elementImports == null) {
	    return NO_PROBLEM;
	}
        // TODO: This is only checking immediate siblings when it needs
        // to be checking all imported elements both here and by our
        // parents and also taking into account visibility
	for (Iterator iter = elementImports.iterator(); iter.hasNext();) {
	    Object imp = iter.next();
	    Object pack = Model.getFacade().getPackage(imp);
            String alias = Model.getFacade().getAlias(imp);
            if (alias == null || alias.length() == 0) {
                alias = designMaterialName;
            }
	    Collection siblings = Model.getFacade().getOwnedElements(pack);
	    if (siblings == null) {
	        return NO_PROBLEM;
	    }
	    Iterator elems = siblings.iterator();
	    while (elems.hasNext()) {
		Object eo = elems.next();
		Object me = /*Model.getFacade().getModelElement(*/eo/*)*/;
                // TODO: The WFR doesn't restrict this to Classifiers - tfm
		if (!(Model.getFacade().isAClassifier(me))) {
		    continue;
		}
		if (me == classifier) {
		    continue;
		}
		String meName = Model.getFacade().getName(me);
		if (meName == null || meName.equals("")) {
		    continue;
		}
		if (meName.equals(alias)) {
		    return PROBLEM_FOUND;
		}
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
    public void initWizard(Wizard w) {
	if (w instanceof WizMEName) {
	    ToDoItem item = (ToDoItem) w.getToDoItem();
	    Object me = item.getOffenders().elementAt(0);
	    String sug = Model.getFacade().getName(me);
	    String ins = super.getInstructions();
	    ((WizMEName) w).setInstructions(ins);
	    ((WizMEName) w).setSuggestion(sug);
	    ((WizMEName) w).setMustEdit(true);
	}
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#getWizardClass(org.argouml.cognitive.ToDoItem)
     */
    public Class getWizardClass(ToDoItem item) { return WizMEName.class; }


} /* end class CrDisambigClassName */
