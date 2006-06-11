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

// $Id$
package org.argouml.uml.cognitive.critics;

import java.util.Iterator;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.critics.Critic;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;

/**
 * Well-formedness rule [2] for MGeneralizableElement. See page 31 of UML 1.1
 * Semantics. OMG document ad/97-08-04.
 * In UML 1.3 it is rule [2] in section 2.5.3.18 page 2-54.
 * Remove final keyword or remove subclasses
 *
 * @author jrobbins
 */
public class CrFinalSubclassed extends CrUML {

    /**
     * The constructor.
     */
    public CrFinalSubclassed() {
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.INHERITANCE);
	setKnowledgeTypes(Critic.KT_SEMANTICS);
	addTrigger("specialization");
	addTrigger("isLeaf");
    }

    /**
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     * java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!Model.getFacade().isAGeneralizableElement(dm)) {
	    return NO_PROBLEM;
	}

	if (!Model.getFacade().isLeaf(dm)) {
	    return NO_PROBLEM;
	}

	Iterator specs = Model.getFacade().getSpecializations(dm).iterator();
	return specs.hasNext() ? PROBLEM_FOUND : NO_PROBLEM;
    }

} /* end class CrFinalSubclassed.java */

