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



// File: CrMultipleInheritance.java.java
// Classes: CrMultipleInheritance.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.language.java.cognitive.critics;

import java.util.Collection;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.kernel.Wizard;
import org.argouml.model.ModelFacade;
import org.argouml.uml.cognitive.critics.CrUML;
import org.argouml.uml.cognitive.critics.WizCueCards;
/** Well-formedness rule [2] for MAssociationEnd. See page 28 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrMultipleInheritance extends CrUML {

    public CrMultipleInheritance() {
	setHeadline("Change Multiple Inheritance to Interfaces");
	addSupportedDecision(CrUML.decINHERITANCE);
	addSupportedDecision(CrUML.decCODE_GEN);
	addTrigger("generalization");
    }

    // TODO - I hate short variable names - what is dm?
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(ModelFacade.isAClassifier(dm))) return NO_PROBLEM;
	Object cls = /*(MClassifier)*/ dm;
	Collection gen = ModelFacade.getGeneralizations(cls);
	if (gen != null && gen.size() > 1)
	    return PROBLEM_FOUND;
	else
	    return NO_PROBLEM;
    }

    public void initWizard(Wizard w) {
	if (w instanceof WizCueCards) {
	    WizCueCards wcc = (WizCueCards) w;
	    ToDoItem item = w.getToDoItem();
	    Object modelElement = /*(MModelElement)*/ item.getOffenders().elementAt(0);
	    String nameStr = ModelFacade.getName(modelElement);
	    wcc.addCue("Remove the generalization arrow to one of the base " +
		       "classes of {name}.");
	    wcc.addCue("Optionally, use the MInterface tool to create a new " +
		       "MInterface for {name} to implement.");
	    wcc.addCue("Use the Realization tool to add a dashed arrow from " +
		       "{name} to the new MInterface.");
	    wcc.addCue("Move method declarations from the unused base class " +
		       "to the new MInterface and move method bodies down into " +
		       "{name}.");
	    wcc.addCue("If the unused base class is not used by anything else " +
		       "then it can be removed.");
	}
    }
    public Class getWizardClass(ToDoItem item) { return WizCueCards.class; }

} /* end class CrMultipleInheritance.java */