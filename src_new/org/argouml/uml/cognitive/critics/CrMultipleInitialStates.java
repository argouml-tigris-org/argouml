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

import org.apache.log4j.Logger;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.UMLToDoItem;

/**
 * A critic to detect when a state has no outgoing transitions.
 *
 * @author jrobbins
 */
public class CrMultipleInitialStates extends CrUML {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(CrMultipleInitialStates.class);

    /**
     * The constructor.
     */
    public CrMultipleInitialStates() {
        setupHeadAndDesc();
        addSupportedDecision(UMLDecision.STATE_MACHINES);
        addTrigger("parent");
        addTrigger("kind");
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *      java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
        if (!(Model.getFacade().isAPseudostate(dm))) {
            return NO_PROBLEM;
        }
        Object k = Model.getFacade().getKind(dm);
        if (!Model.getFacade().equalsPseudostateKind(
                k,
                Model.getPseudostateKind().getInitial())) {
	    return NO_PROBLEM;
        }

        // container state / composite state
        Object cs = Model.getFacade().getContainer(dm);
        if (cs == null) {
            LOG.debug("null parent state");
            return NO_PROBLEM;
        }
        Collection peers = Model.getFacade().getSubvertices(cs);
        int initialStateCount = 0;
        for (Iterator iter = peers.iterator(); iter.hasNext();) {
            Object sv = iter.next();
            if (Model.getFacade().isAPseudostate(sv)
                && Model.getFacade().
                	equalsPseudostateKind(
                	        Model.getFacade().getKind(sv),
                	        Model.getPseudostateKind().getInitial())) {
                initialStateCount++;
            }
        }
        if (initialStateCount > 1) {
            return PROBLEM_FOUND;
        }
        return NO_PROBLEM;
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#toDoItem( java.lang.Object,
     *      org.argouml.cognitive.Designer)
     */
    public ToDoItem toDoItem(Object dm, Designer dsgr) {
        ListSet offs = computeOffenders(dm);
        return new UMLToDoItem(this, offs, dsgr);
    }

    /**
     * @param ps the object to check
     * @return the list of offenders
     */
    protected ListSet computeOffenders(Object ps) {
        ListSet offs = new ListSet(ps);
        Object cs = Model.getFacade().getContainer(ps);
        if (cs == null) {
            LOG.debug("null parent in still valid");
            return offs;
	}
        Collection peers = Model.getFacade().getSubvertices(cs);

        for (Iterator iter = peers.iterator(); iter.hasNext();) {
            Object sv = iter.next();
            if (Model.getFacade().isAPseudostate(sv)
                && Model.getFacade().equalsPseudostateKind(
                        Model.getFacade().getKind(sv),
                        Model.getPseudostateKind().getInitial())) {
                offs.addElement(sv);
	    }
        }

        return offs;
    }

    /*
     * @see org.argouml.cognitive.Poster#stillValid(
     *      org.argouml.cognitive.ToDoItem, org.argouml.cognitive.Designer)
     */
    public boolean stillValid(ToDoItem i, Designer dsgr) {
        if (!isActive()) {
            return false;
        }
        ListSet offs = i.getOffenders();
        Object dm = offs.firstElement();
        ListSet newOffs = computeOffenders(dm);
        boolean res = offs.equals(newOffs);
        return res;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 4151051235876065649L;
}

