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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.Icon;

import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;

// Using Model through Facade

/**
 * Check the:
 * Well-formedness rule [2] for Classifier.
 * See page 29 of UML 1.1, Semantics. OMG document ad/97-08-04.
 * See page 2-49 in UML V1.3<p>
 *
 * In the process of modifying this to use the new Facade object
 * (Jan 2003) this was changed to no longer detect StructuralFeatures
 * with the same name but instead attributes with the same name.
 * This is in fact a more to the letter adherance to the UML
 * well-formedness rule but it is however a change.
 */
public class CrAttrNameConflict extends CrUML {

    /**
     * The constructor.
     *
     */
    public CrAttrNameConflict() {
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.INHERITANCE);
	addSupportedDecision(UMLDecision.STORAGE);
	addSupportedDecision(UMLDecision.NAMING);
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
     * @return true if there are two with the same name.
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(java.lang.Object, org.argouml.cognitive.Designer)
     */
    @Override
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(Model.getFacade().isAClassifier(dm))) {
            return NO_PROBLEM;
        }

	Collection<String> namesSeen = new ArrayList<String>();
	Iterator attrs = Model.getFacade().getAttributes(dm).iterator();
	while (attrs.hasNext()) {
	    String name = Model.getFacade().getName(attrs.next());
	    if (name == null || name.length() == 0) {
	        continue;
	    }

	    if (namesSeen.contains(name)) {
	        return PROBLEM_FOUND;
	    }
	    namesSeen.add(name);
	}
	return NO_PROBLEM;
    }

    /*
     * @see org.argouml.cognitive.Poster#getClarifier()
     */
    @Override
    public Icon getClarifier() {
	return ClAttributeCompartment.getTheInstance();
    }

    /**
     * @see org.argouml.uml.cognitive.critics.CrUML#getCriticizedMetatypes()
     */
    public Set<Object> getCriticizedMetatypes() {
        Set<Object> ret = new HashSet<Object>();
        ret.add(Model.getMetaTypes().getClassifier());
        return ret;
    }
    
}

