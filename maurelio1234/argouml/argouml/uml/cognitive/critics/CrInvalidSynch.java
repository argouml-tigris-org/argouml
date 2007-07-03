// $Id: CrInvalidSynch.java 11516 2006-11-25 04:30:15Z tfmorris $
// Copyright (c) 2003-2006 The Regents of the University of California. All
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

import java.util.Iterator;

import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;

/**
 * UML 1.5 Well-formedness rule [2] for Sync States.
 *
 * @author pepargouml@yahoo.es
 */
public class CrInvalidSynch extends CrUML {

    /**
     * The constructor.
     */
    public CrInvalidSynch() {
        setupHeadAndDesc();
        addSupportedDecision(UMLDecision.STATE_MACHINES);
        addTrigger("incoming");
        addTrigger("outgoing");
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(java.lang.Object,
     *      org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
        Object destinationRegion = null;
        Object sourceRegion = null;
        Object aux = null;
        Object tr = null;
        if (!Model.getFacade().isASynchState(dm))
            return NO_PROBLEM;
        Iterator outgoing = Model.getFacade().getOutgoings(dm).iterator();
        while (outgoing.hasNext()) {
            tr = outgoing.next();
            aux = Model.getFacade().getContainer(Model.getFacade().
                    getTarget(tr));
            if (destinationRegion == null)
                destinationRegion = aux;
            else if (!aux.equals(destinationRegion))
                return PROBLEM_FOUND;
        }
        Iterator incoming = Model.getFacade().getIncomings(dm).iterator();
        while (incoming.hasNext()) {
            tr = incoming.next();
            aux = Model.getFacade().getContainer(Model.getFacade().
                    getSource(tr));
            if (sourceRegion == null)
                sourceRegion = aux;
            else if (!aux.equals(sourceRegion))
                return PROBLEM_FOUND;
        }

        if (destinationRegion != null
                && !Model.getFacade().isAConcurrentRegion(destinationRegion)
        )
            return PROBLEM_FOUND;

        if (sourceRegion != null
                && !Model.getFacade().isAConcurrentRegion(sourceRegion)
        )
            return PROBLEM_FOUND;

        return NO_PROBLEM;
    }

} /* end class CrInvalidSynch */

