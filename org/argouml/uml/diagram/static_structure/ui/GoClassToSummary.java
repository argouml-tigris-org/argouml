// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.uml.diagram.static_structure.ui;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

import org.argouml.application.api.Argo;
import org.argouml.model.ModelFacade;
import org.argouml.ui.AbstractGoRule;

/**
 * This class is a Go Rule for the "Class - centric" Navigation perspective.
 *
 * $Revision$
 *
 * @author  alexb, $Author$
 * @since argo 0.13.4, Created on 21 March 2003, 23:18
 */
public class GoClassToSummary extends AbstractGoRule {

    public String getRuleName() {
	return Argo.localize ("Tree", "misc.class.attribute");
    }


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
	Iterator generalizationsIt = ModelFacade.getGeneralizations(parent);
	Iterator specializationsIt = ModelFacade.getSpecializations(parent);
          
	if (generalizationsIt.hasNext())
	    return true;
          
	if (specializationsIt.hasNext())
	    return true;
          
	while (incomingIt.hasNext()) {
              
	    // abstractions are represented in the Inheritance Node.
	    if (ModelFacade.isAAbstraction(incomingIt.next()))
                return true;
	}
          
	while (outgoingIt.hasNext()) {
              
	    // abstractions are represented in the Inheritance Node.
	    if (ModelFacade.isAAbstraction(outgoingIt.next()))
                return true;
	}
          
	return false;
    }

}
