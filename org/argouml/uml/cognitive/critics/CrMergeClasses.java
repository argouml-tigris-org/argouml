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

package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.ModelFacade;

import ru.novosoft.uml.foundation.data_types.MMultiplicity;


/** A critic to check whether to classes sharing a 1..1 association can or
 *  should be combined.
 */
public class CrMergeClasses extends CrUML {

    public CrMergeClasses() {
	setHeadline("Consider Combining Classes");
	setPriority(ToDoItem.LOW_PRIORITY);
	addSupportedDecision(CrUML.decCLASS_SELECTION); 
	addTrigger("associationEnd");
    }


    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(ModelFacade.isAClass(dm))) return NO_PROBLEM;
	Object cls = /*(MClass)*/ dm;
	Collection ends = ModelFacade.getAssociationEnds(cls);
	if (ends == null || ends.size() != 1) return NO_PROBLEM;
	Object myEnd = /*(MAssociationEnd)*/ ends.iterator().next();
	Object asc = ModelFacade.getAssociation(myEnd);
	List conns = new ArrayList(ModelFacade.getConnections(asc));
	Object ae0 = /*(MAssociationEnd)*/ conns.get(0);
	Object ae1 = /*(MAssociationEnd)*/ conns.get(1);
	// both ends must be classes, otherwise there is nothing to merge
	if (!(ModelFacade.isAClass(ModelFacade.getType(ae0)) && 
	      ModelFacade.isAClass(ModelFacade.getType(ae1)))) 
	    return NO_PROBLEM;
	// both ends must be navigable, otherwise there is nothing to merge
	if (!(ModelFacade.isNavigable(ae0) && 
	      ModelFacade.isNavigable(ae1)))
	    return NO_PROBLEM;
	if (ModelFacade.getMultiplicity(ae0).equals(MMultiplicity.M1_1) &&
	    ModelFacade.getMultiplicity(ae1).equals(MMultiplicity.M1_1))
	    return PROBLEM_FOUND;
	return NO_PROBLEM;
    }

} /* end class CrMergeClasses */