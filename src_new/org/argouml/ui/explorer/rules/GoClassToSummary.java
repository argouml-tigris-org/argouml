// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.argouml.model.ModelFacade;

/**
 * This class is a Go Rule for the "Class - centric" Navigation perspective.
 * Rule for Class->Summary.
 *
 * $Revision$
 *
 * @author  alexb, $Author$
 * @since argo 0.13.4, Created on 21 March 2003, 23:18
 */
public class GoClassToSummary extends AbstractPerspectiveRule {

    /**
     * @see org.argouml.ui.explorer.rules.PerspectiveRule#getRuleName()
     */
    public String getRuleName() {
	return "Class->Summary";
    }

    /**
     * @see org.argouml.ui.explorer.rules.PerspectiveRule#getChildren(java.lang.Object)
     */
    public Collection getChildren(Object parent) {
	if (ModelFacade.isAClass(parent)) {
	    ArrayList list = new ArrayList();

	    if (ModelFacade.getAttributes(parent).size() > 0)
                list.add(new AttributesNode(parent));

	    if (ModelFacade.getAssociationEnds(parent).size() > 0)
                list.add(new AssociationsNode(parent));

	    if (ModelFacade.getOperations(parent).size() > 0)
                list.add(new OperationsNode(parent));

	    if (hasIncomingDependencies( parent))
		list.add(new IncomingDependencyNode(parent));

	    if (hasOutGoingDependencies( parent))
                list.add(new OutgoingDependencyNode(parent));

	    if (hasInheritance( parent))
                list.add(new InheritanceNode(parent));

	    return list;
	}

	return null;
    }

    /**
     * @see org.argouml.ui.explorer.rules.PerspectiveRule#getDependencies(java.lang.Object)
     */
    public Set getDependencies(Object parent) {
        if (ModelFacade.isAClass(parent)) {
	    Set set = new HashSet();
	    set.add(parent);
	    set.addAll(ModelFacade.getAttributes(parent));
	    set.addAll(ModelFacade.getOperations(parent));
	    set.addAll(ModelFacade.getAssociationEnds(parent));
	    set.addAll(ModelFacade.getSupplierDependencies(parent));
	    set.addAll(ModelFacade.getClientDependencies(parent));
	    set.addAll(ModelFacade.getGeneralizations(parent));
	    set.addAll(ModelFacade.getSpecializations(parent));
	    return set;
	}

	return null;
    }

    private boolean hasIncomingDependencies(Object parent) {
	Iterator incomingIt =
	    ModelFacade.getSupplierDependencies(parent).iterator();

	while (incomingIt.hasNext()) {
	    // abstractions are represented in the Inheritance Node.
	    if (!ModelFacade.isAAbstraction(incomingIt.next()))
                return true;
	}

	return false;
    }
  
    private boolean hasOutGoingDependencies(Object parent) {
	Iterator incomingIt =
	    ModelFacade.getClientDependencies(parent).iterator();

	while (incomingIt.hasNext()) {
	    // abstractions are represented in the Inheritance Node.
	    if (!ModelFacade.isAAbstraction(incomingIt.next()))
                return true;
	}

	return false;
    }
 
    private boolean hasInheritance(Object parent) {
        Iterator incomingIt =
            ModelFacade.getSupplierDependencies(parent).iterator();
        Iterator outgoingIt =
            ModelFacade.getClientDependencies(parent).iterator();
        Iterator generalizationsIt = 
            ModelFacade.getGeneralizations(parent).iterator();
        Iterator specializationsIt = 
            ModelFacade.getSpecializations(parent).iterator();

	if (generalizationsIt.hasNext())
	    return true;

	if (specializationsIt.hasNext())
	    return true;

	while (incomingIt.hasNext()) {
	    // abstractions are represented in the Inheritance Node.
	    if (ModelFacade.isAAbstraction(incomingIt.next())) {
                return true;
            }
	}

	while (outgoingIt.hasNext()) {
	    // abstractions are represented in the Inheritance Node.
	    if (ModelFacade.isAAbstraction(outgoingIt.next())) {
                return true;
            }
	}

	return false;
    }
}
