// $Id:CrMultipleShallowHistoryStates.java 12950 2007-07-01 08:10:04Z tfmorris $
// Copyright (c) 2003-2007 The Regents of the University of California. All
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

import org.apache.log4j.Logger;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.UMLToDoItem;

/**
 * UML 1.5 Well-formedness rule [3] for Composite States.
 *
 * @author pepargouml@yahoo.es
 */
public class CrMultipleShallowHistoryStates extends CrUML {
    /**
     * Logger.
     */
    private static final Logger LOG =
            Logger.getLogger(CrMultipleShallowHistoryStates.class);

    /**
     * The constructor.
     */
    public CrMultipleShallowHistoryStates() {
        setupHeadAndDesc();
        addSupportedDecision(UMLDecision.STATE_MACHINES);
        addTrigger("parent");
        addTrigger("kind");
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *      java.lang.Object, org.argouml.cognitive.Designer)
     */
    @Override
    public boolean predicate2(Object dm, Designer dsgr) {
        if (!(Model.getFacade().isAPseudostate(dm))) {
            return NO_PROBLEM;
        }
        Object k = Model.getFacade().getKind(dm);
        if (!Model.getFacade()
                .equalsPseudostateKind(k,
                        Model.getPseudostateKind().getShallowHistory())) {
            return NO_PROBLEM;
        }

        // container state / composite state
        Object cs = Model.getFacade().getContainer(dm);
        if (cs == null) {
            LOG.debug("null parent state");
            return NO_PROBLEM;
        }
        
        int initialStateCount = 0;
        Collection peers = Model.getFacade().getSubvertices(cs);
        for (Object sv : peers) {
            if (Model.getFacade().isAPseudostate(sv)
                    && Model.getFacade().equalsPseudostateKind(
                            Model.getFacade().getKind(sv),
                            Model.getPseudostateKind().getShallowHistory())) {
                initialStateCount++;
            }
        }
        if (initialStateCount > 1) {
            return PROBLEM_FOUND;
        }
        return NO_PROBLEM;
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#toDoItem(java.lang.Object,
     *      org.argouml.cognitive.Designer)
     */
    @Override
    public ToDoItem toDoItem(Object dm, Designer dsgr) {
        ListSet offs = computeOffenders(dm);
        return new UMLToDoItem(this, offs, dsgr);
    }

    /**
     * @param ps the design material
     * @return the offenders
     */
    protected ListSet computeOffenders(Object ps) {
        ListSet offs = new ListSet(ps);
        Object cs = Model.getFacade().getContainer(ps);
        if (cs == null) {
            LOG.debug("null parent in still valid");
            return offs;
        }
        Collection peers = Model.getFacade().getSubvertices(cs);
        for (Object sv : peers) {
            if (Model.getFacade().isAPseudostate(sv)
                    && Model.getFacade().equalsPseudostateKind(
                            Model.getFacade().getKind(sv),
                            Model.getPseudostateKind().getShallowHistory())) {
                offs.add(sv);
            }
        }
        return offs;
    }

    /*
     * @see org.argouml.cognitive.Poster#stillValid(
     *      org.argouml.cognitive.ToDoItem, org.argouml.cognitive.Designer)
     */
    @Override
    public boolean stillValid(ToDoItem i, Designer dsgr) {
        if (!isActive()) {
            return false;
        }
        ListSet offs = i.getOffenders();
        Object dm = offs.get(0);
        ListSet newOffs = computeOffenders(dm);
        boolean res = offs.equals(newOffs);
        return res;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -8324054401013865193L;
}

