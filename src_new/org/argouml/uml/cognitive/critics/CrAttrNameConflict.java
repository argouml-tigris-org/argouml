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



// File: CrAttrNameConflict.java
// Classes: CrAttrNameConflict
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import javax.swing.*;

import java.util.Vector;
import java.util.Iterator;

import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;

// Using Model through Facade
import org.argouml.api.model.FacadeManager;
import org.argouml.model.uml.NsumlModelFacade;


/** Check the:
 *  Well-formedness rule [2] for MClassifier. 
 *  See page 29 of UML 1.1, Semantics. OMG document ad/97-08-04.
 *  See page 2-49 in UML V1.3
 *
 *  <p>In the process of modifying this to use the new Facade object 
 *  (Jan 2003) this was changed to no longer detect StructuralFeatures 
 *  with the same name but instead attributes with the same name.
 *  This is in fact a more to the letter adherance to the UML 
 *  well-formedness rule but it is however a change.
 */
public class CrAttrNameConflict extends CrUML {

  public CrAttrNameConflict() {
    setHeadline("Revise MAttribute Names to Avoid Conflict");
    addSupportedDecision(CrUML.decINHERITANCE);
    addSupportedDecision(CrUML.decSTORAGE);
    addSupportedDecision(CrUML.decNAMING);
    setKnowledgeTypes(Critic.KT_SYNTAX);
    addTrigger("structuralFeature");
    addTrigger("feature_name");
  }

    /**
     * Examines the classifier and tells if we have two attributes
     * with the same name. Comparison is done with equals (contains).
     *
     * @param dm is the classifier
     * @param dsgr is not used.
     * @returns true if there are two with the same name.
     */
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(FacadeManager.getUmlFacade().isAClassifier(dm))) return NO_PROBLEM;

	Vector namesSeen = new Vector();

	Iterator enum = FacadeManager.getUmlFacade().getAttributes(dm).iterator();
	while (enum.hasNext()) {
	    String name = FacadeManager.getUmlFacade().getName(enum.next());
	    if (name == null || name.length() == 0) continue;

	    if (namesSeen.contains(name)) return PROBLEM_FOUND;
	    namesSeen.addElement(name);
	}
	return NO_PROBLEM;
    }

  public Icon getClarifier() {
    return ClAttributeCompartment.TheInstance;
  }

} /* end class CrAttrNameConflict.java */

