// Copyright (c) 2003-2005 The Regents of the University of California. All
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

import org.apache.log4j.Logger;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLToDoItem;
import org.tigris.gef.util.VectorSet;

import java.util.Collection;
import java.util.Iterator;

/**
 * UML 1.5 Well-formedness rule [3] for Composite States.
 *
 * @author pepargouml@yahoo.es
 */
public class CrMultipleShallowHistoryStates extends CrUML {

    protected static Logger cat =
            Logger.getLogger(CrMultipleShallowHistoryStates.class);

    public CrMultipleShallowHistoryStates() {
        setHeadline("Remove Extra Shallow History States");
        addSupportedDecision(CrUML.DEC_STATE_MACHINES);
        addTrigger("parent");
        addTrigger("kind");
    }

    public boolean predicate2(Object dm, Designer dsgr) {
        if (!(Model.getFacade().isAPseudostate(dm))) return NO_PROBLEM;
        Object k = Model.getFacade().getPseudostateKind(dm);
        if (!Model.getFacade()
                .equalsPseudostateKind(k,
                        Model.getPseudostateKind().getShallowHistory()))
            return NO_PROBLEM;

        // container state / composite state
        Object cs = Model.getFacade().getModelElementContainer(dm);
        if (cs == null) {
            cat.debug("null parent state");
            return NO_PROBLEM;
        }
        Collection peers = Model.getFacade().getSubvertices(cs);
        int initialStateCount = 0;
        for (Iterator iter = peers.iterator(); iter.hasNext();) {
            Object sv = iter.next();
            if (Model.getFacade().isAPseudostate(sv)
                    && Model.getFacade()
                    .equalsPseudostateKind(
                            Model.getFacade().getPseudostateKind(sv),
                            Model.getPseudostateKind().getShallowHistory()))
                initialStateCount++;
        }
        if (initialStateCount > 1) return PROBLEM_FOUND;
        return NO_PROBLEM;
    }

    public ToDoItem toDoItem(Object dm, Designer dsgr) {
        VectorSet offs = computeOffenders(dm);
        return new UMLToDoItem(this, offs, dsgr);
    }

    protected VectorSet computeOffenders(Object ps) {
        VectorSet offs = new VectorSet(ps);
        Object cs = Model.getFacade().getModelElementContainer(ps);
        if (cs == null) {
            cat.debug("null parent in still valid");
            return offs;
        }
        Collection peers = Model.getFacade().getSubvertices(cs);
        for (Iterator iter = peers.iterator(); iter.hasNext();) {
            Object sv = iter.next();
            if (Model.getFacade().isAPseudostate(sv)
                    && Model.getFacade().
                    equalsPseudostateKind(
                            Model.getFacade().getPseudostateKind(sv),
                            Model.getPseudostateKind().getShallowHistory())) {
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

} /* end class CrMultipleShallowHistoryStates */

