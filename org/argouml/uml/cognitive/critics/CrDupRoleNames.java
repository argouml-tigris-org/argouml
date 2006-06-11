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

import java.util.Iterator;
import java.util.Vector;

import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


// Use Model through Facade

/**
 * A critic to check that the ends of an association all have distinct
 * names.<p>
 *
 * This is the first well-formedness rule for associations in the UML 1.3
 * standard (see section 2.5.3 of the standard).<p>
 *
 * @see <a href=
 * "http://argouml.tigris.org/documentation/snapshots/manual/argouml.html/
 * #s2.ref.critics_dup_role_names">
 * ArgoUML User Manual: Duplicate end (role) names for &lt;association&gt;</a>
 *
 * @author Jason Robbins
 */
public class CrDupRoleNames extends CrUML {

    /**
     * <p>Constructor for the critic.</p>
     *
     * <p>Sets up the resource name, which will allow headline and description
     * to found for the current locale. Provides a design issue category
     * (NAMING) and add triggers for "connection" and "end_name".</p>
     */

    public CrDupRoleNames() {
        setupHeadAndDesc();
        addSupportedDecision(UMLDecision.NAMING);

        // These may not actually make any difference at present (the code
        // behind addTrigger needs more work).

        addTrigger("connection");
        addTrigger("end_name");
    }


    /**
     * <p>The trigger for the critic.</p>
     *
     * <p>We do not handle association roles, which are a subclass of
     *   association. An association role should be fine, if its parent is OK,
     *   since it must have the same or fewer ends than its parent.</p>
     *
     * <p><em>Note</em>. ArgoUML does not currently have a constructor to check
     *   that an association role is more tightly constrained than its
     *   parent.</p>
     *
     * <p>Then loop through the ends, building a vector of end names that we
     *   have seen, and looking to see if the current end is already in that
     *   vector. We ignore any ends that are unnamed, or have the empty string
     *   as name.</p>
     *
     * <p>Whilst this is an O(n^2) algorithm, most associations have only two
     *   ends, so this is unlikely to cause difficulty.</p>
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

        // Only work for associations

        if (!(Model.getFacade().isAAssociation(dm))) {
            return NO_PROBLEM;
        }

	// No problem if this is an association role.
	if (Model.getFacade().isAAssociationRole(dm)) {
	    return NO_PROBLEM;
	}

        // Loop through all the ends, comparing the name against those already
        // seen (ignoring any with no name).
        // No problem if there are no connections defined, we will fall
	// through immediatly.

        Vector   namesSeen = new Vector();

        Iterator conns = Model.getFacade().getConnections(dm).iterator();
        while (conns.hasNext()) {
            String name = Model.getFacade().getName(conns.next());

            // Ignore non-existent and empty names

            if ((name == null) || name.equals("")) {
                continue;
            }

            // Is the name already in the vector of those seen, if not add it
            // and go on round.

            if (namesSeen.contains(name)) {
                return PROBLEM_FOUND;
            }

            namesSeen.addElement(name);
        }

        // If we drop out there were no clashes

        return NO_PROBLEM;
    }

} /* end class CrDupRoleNames */

