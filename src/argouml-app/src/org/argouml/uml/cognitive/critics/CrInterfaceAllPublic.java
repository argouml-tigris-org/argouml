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

import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;

/**
 * Well-formedness rule [3] for Interface. See page 32 of UML 1.1
 *
 * @author jrobbins
 *  Semantics. OMG document ad/97-08-04.
 */
public class CrInterfaceAllPublic extends CrUML {

    /**
     * The constructor.
     */
    public CrInterfaceAllPublic() {
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.PLANNED_EXTENSIONS);
	setKnowledgeTypes(Critic.KT_SYNTAX);
	addTrigger("behavioralFeature");
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *      java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(Model.getFacade().isAInterface(dm))) {
	    return NO_PROBLEM;
	}
	Object inf = dm;
	Collection bf = Model.getFacade().getFeatures(inf);
	if (bf == null) {
	    return NO_PROBLEM;
	}
	Iterator features = bf.iterator();
	while (features.hasNext()) {
	    Object f = features.next();
	    if (Model.getFacade().getVisibility(f) == null) {
	        return NO_PROBLEM;
	    }
	    if (!Model.getFacade().getVisibility(f)
                .equals(Model.getVisibilityKind().getPublic())) {
	        return PROBLEM_FOUND;
	    }
	}
	return NO_PROBLEM;
    }

} /* end class CrInterfaceAllPublic */
