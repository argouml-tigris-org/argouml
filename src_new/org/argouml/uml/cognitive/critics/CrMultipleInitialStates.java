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

// File: CrMultipleInitialStates.java
// Classes: CrMultipleInitialStates
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.Iterator;
import org.apache.log4j.Logger;

import org.tigris.gef.util.VectorSet;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.ModelFacade;

/** A critic to detect when a state has no outgoing transitions. */

public class CrMultipleInitialStates extends CrUML {    

    protected static Logger cat = 
        Logger.getLogger(CrMultipleInitialStates.class);

    public CrMultipleInitialStates() {
        setHeadline("Remove Extra Initial States");
        addSupportedDecision(CrUML.decSTATE_MACHINES);
        addTrigger("parent");
        addTrigger("kind");
    }

    public boolean predicate2(Object dm, Designer dsgr) {
        if (!(ModelFacade.isAPseudostate(dm))) return NO_PROBLEM;
        Object k = ModelFacade.getPseudostateKind(dm);
        if (!ModelFacade.
            equalsPseudostateKind(k,
				  ModelFacade.INITIAL_PSEUDOSTATEKIND))
	    return NO_PROBLEM;

        // container state / composite state
        Object cs = ModelFacade.getModelElementContainer(dm);
        if (cs == null) { 
            cat.debug("null parent state"); 
            return NO_PROBLEM; 
        }
        Collection peers = ModelFacade.getSubvertices(cs);
        int initialStateCount = 0;
        int size = peers.size();
        for (Iterator iter = peers.iterator(); iter.hasNext();) {
            Object sv = iter.next();
            if (ModelFacade.isAPseudostate(sv) &&
                ModelFacade.
		    equalsPseudostateKind(ModelFacade.getPseudostateKind(sv), 
					  ModelFacade.INITIAL_PSEUDOSTATEKIND))
                initialStateCount++;
        }
        if (initialStateCount > 1) return PROBLEM_FOUND;
        return NO_PROBLEM;
    }

    public ToDoItem toDoItem(Object dm, Designer dsgr) {
        VectorSet offs = computeOffenders(dm);
        return new ToDoItem(this, offs, dsgr);
    }

    protected VectorSet computeOffenders(Object ps) {
        VectorSet offs = new VectorSet(ps);
        Object cs = ModelFacade.getModelElementContainer(ps);
        if (cs == null) { 
            cat.debug("null parent in still valid"); 
            return offs; 
	}
        Collection peers = ModelFacade.getSubvertices(cs);

        for (Iterator iter = peers.iterator(); iter.hasNext();) {
            Object sv = iter.next();
            if (ModelFacade.isAPseudostate(sv) &&
                ModelFacade.
                    equalsPseudostateKind(ModelFacade.getPseudostateKind(sv), 
					  ModelFacade.INITIAL_PSEUDOSTATEKIND))
	    {
                offs.addElement(sv);
	    }
        }

        return offs;
    }

    public boolean stillValid(ToDoItem i, Designer dsgr) {
        if (!isActive()) return false;
        VectorSet offs = i.getOffenders();
        Object dm = offs.firstElement();
        VectorSet newOffs = computeOffenders(dm);
        boolean res = offs.equals(newOffs);
        return res;
    }

} /* end class CrMultipleInitialStates */

