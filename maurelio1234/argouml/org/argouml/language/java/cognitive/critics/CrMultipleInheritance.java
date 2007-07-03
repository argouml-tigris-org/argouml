// $Id: CrMultipleInheritance.java 12635 2007-05-22 20:35:29Z mvw $
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

package org.argouml.language.java.cognitive.critics;

import java.util.Collection;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.critics.CrUML;
import org.argouml.uml.cognitive.critics.WizCueCards;

/**
 * Well-formedness rule [2] for MAssociationEnd. See page 28 of UML 1.1
 * Semantics. OMG document ad/97-08-04.
 */
public class CrMultipleInheritance extends CrUML {

    /**
     * The constructor.
     */
    public CrMultipleInheritance() {
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.INHERITANCE);
	addSupportedDecision(UMLDecision.CODE_GEN);
	addTrigger("generalization");
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     * java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object designMaterial, Designer dsgr) {
	if (!(Model.getFacade().isAClassifier(designMaterial))) {
	    return NO_PROBLEM;
	}
	Object cls = /*(MClassifier)*/ designMaterial;
	Collection gen = Model.getFacade().getGeneralizations(cls);
	if (gen != null && gen.size() > 1) {
	    return PROBLEM_FOUND;
	}
        return NO_PROBLEM;
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#initWizard(
     *         org.argouml.cognitive.ui.Wizard)
     */
    public void initWizard(Wizard w) {
	if (w instanceof WizCueCards) {
	    WizCueCards wcc = (WizCueCards) w;
	    wcc.addCue("Remove the generalization arrow to one of the base "
		   + "classes of {name}.");
	    wcc.addCue("Optionally, use the MInterface tool to create a new "
		   + "MInterface for {name} to implement.");
	    wcc.addCue("Use the Realization tool to add a dashed arrow from "
		   + "{name} to the new MInterface.");
	    wcc.addCue("Move method declarations from the unused base class "
		   + "to the new MInterface and move method bodies down into "
		   + "{name}.");
	    wcc.addCue("If the unused base class is not used by anything else "
		   + "then it can be removed.");
	}
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#getWizardClass(org.argouml.cognitive.ToDoItem)
     */
    public Class getWizardClass(ToDoItem item) {
        return WizCueCards.class;
    }

} /* end class CrMultipleInheritance.java */
