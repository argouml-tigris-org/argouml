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

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Critic;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;

/**
 * A critic to check that only one end of a binary association is an
 * aggregation.<p>
 *
 * This is the second well-formedness rule for associations in the UML 1.3
 * standard (see section 2.5.3 of the standard).<p>
 *
 * <em>Note</em>. This only applies to binary associations. There is a
 * separate critic (see {@link org.argouml.uml.cognitive.critics.CrNWayAgg})
 * which deals with 3- or more-way assocations.<p>
 *
 * See <a href=
 * "http://argouml.tigris.org/documentation/snapshots/manual/argouml.html/
 * #s2.ref.critics_multiple_agg">ArgoUML
 * User Manual: Two Aggregate ends (roles) in binary Association</a>
 *
 * @author jrobbins
 */
public class CrMultipleAgg extends CrUML {

    /**
     * Constructor for the critic.<p>
     *
     * Sets up the resource name, which will allow headline and description
     * to found for the current locale. Provides a design issue category
     * (CONTAINMENT), a knowledge type (SEMANTICS) and add triggers for
     * "end_aggregation".<p>
     */
    public CrMultipleAgg() {
        setupHeadAndDesc();
        addSupportedDecision(UMLDecision.CONTAINMENT);
        setKnowledgeTypes(Critic.KT_SEMANTICS);

        // These may not actually make any difference at present (the code
        // behind addTrigger needs more work).

        addTrigger("end_aggregation");
    }

    /**
     * The trigger for the critic.<p>
     *
     * Check that the number of ends is two, otherwise this should be
     * handled by the critic for N-way assocations (see {@link
     * org.argouml.uml.cognitive.critics.CrNWayAgg}).<p>
     *
     * We do not handle association roles, which are a subclass of
     * association. An association role should be fine, if its parent is OK,
     * since it must be more tightly constrained than its parent.<p>
     *
     * <em>Note</em>. ArgoUML does not currently have a constructor to check
     * that an association role is more tightly constrained than its
     * parent.<p>
     *
     * Then loop through the ends, counting the number of aggregate
     * ends. Note that we look for aggregation explicitly, rather than just
     * absence of "no aggregation", so we don't trigger if the aggregation is
     * just undefined.<p>
     *
     * @param  dm    the {@link java.lang.Object Object} to be checked against
     *               the critic.
     *
     * @param  dsgr  the {@link org.argouml.cognitive.Designer Designer}
     *               creating the model. Not used, this is for future
     *               development of ArgoUML.
     *
     * @return       {@link #PROBLEM_FOUND PROBLEM_FOUND} if the critic is
     *               triggered, otherwise {@link #NO_PROBLEM NO_PROBLEM}.
     */
    public boolean predicate2(Object dm, Designer dsgr) {

        // Only for associations

        if (!(Model.getFacade().isAAssociation(dm))) {
            return NO_PROBLEM;
        }

        // Get the assocations and connections. No problem (there is a separate
        // critic) if this is not a binary association or is an association
        // role.

        Object asc = /*(MAssociation)*/ dm;

        if (Model.getFacade().isAAssociationRole(asc)) {
            return NO_PROBLEM;
        }

        Collection   conns = Model.getFacade().getConnections(asc);

        if ((conns == null) || (conns.size() != 2)) {
            return NO_PROBLEM;
        }

        // Loop through the associations, counting the ends with aggregations

        int      aggCount = 0;
        Iterator assocEnds = conns.iterator();
        while (assocEnds.hasNext()) {
            Object ae = /*(MAssociationEnd)*/ assocEnds.next();
            if (Model.getFacade().isAggregate(ae)
                    || Model.getFacade().isComposite(ae)) {
                aggCount++;
            }
        }

        // A problem if we found more than 1 aggregation

        if (aggCount > 1) {
            return PROBLEM_FOUND;
        } else {
            return NO_PROBLEM;
        }
    }

    /**
     * Find the class which will handle the wizard behaviour.<p>
     *
     * @param  item  the {@link ToDoItem} that triggered the critic.
     *
     * @return       {@link Class} of the wizard.
     */

    public Class getWizardClass(ToDoItem item) {
        return WizAssocComposite.class;
    }

} /* end class CrMultipleAgg */
