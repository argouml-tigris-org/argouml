// $Id:CrMergeClasses.java 11516 2006-11-25 04:30:15Z tfmorris $
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;

/**
 * A critic to check whether to classes sharing a 1..1 association can or
 * should be combined.
 */
public class CrMergeClasses extends CrUML {

    /**
     * The constructor.
     */
    public CrMergeClasses() {
        setupHeadAndDesc();
	setPriority(ToDoItem.LOW_PRIORITY);
	addSupportedDecision(UMLDecision.CLASS_SELECTION);
	addTrigger("associationEnd");
    }


    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *      java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(Model.getFacade().isAClass(dm))) {
	    return NO_PROBLEM;
	}
	Object cls = dm;
	Collection ends = Model.getFacade().getAssociationEnds(cls);
	if (ends == null || ends.size() != 1) {
	    return NO_PROBLEM;
	}
	Object myEnd = ends.iterator().next();
	Object asc = Model.getFacade().getAssociation(myEnd);
	List conns = new ArrayList(Model.getFacade().getConnections(asc));
        // Do we have 2 connection ends?
        if (conns == null || conns.size() != 2) {
            return NO_PROBLEM;
        }
	Object ae0 = conns.get(0);
	Object ae1 = conns.get(1);
	// both ends must be classes, otherwise there is nothing to merge
	if (!(Model.getFacade().isAClass(Model.getFacade().getType(ae0))
            && Model.getFacade().isAClass(Model.getFacade().getType(ae1)))) {
	    return NO_PROBLEM;
	}
	// both ends must be navigable, otherwise there is nothing to merge
	if (!(Model.getFacade().isNavigable(ae0)
            && Model.getFacade().isNavigable(ae1))) {
	    return NO_PROBLEM;
	}
	if (Model.getFacade().getLower(ae0) == 1
                && Model.getFacade().getUpper(ae0) == 1
                && Model.getFacade().getLower(ae1) == 1
                && Model.getFacade().getUpper(ae1) == 1) {
	    return PROBLEM_FOUND;
	}
	return NO_PROBLEM;
    }

} /* end class CrMergeClasses */
