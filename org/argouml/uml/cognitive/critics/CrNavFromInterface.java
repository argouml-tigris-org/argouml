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



// File: CrNavFromInterface.java
// Classes: CrNavFromInterface.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

// 27 Feb 2002: Jeremy Bennett (mail@jeremybennett.com). Fixed to deal with
// picking up navigation problems at the wrong end! Solution suggested by
// Chavdar Botev. Tidied up to remove deprecated stuff.

package org.argouml.uml.cognitive.critics;

import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.collaborations.*;

import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;


/**
 * <p> A critic to detect navigation from an Interface to a Class in an
 * Association. This is not permitted in UML, since it would require the
 * Interface to hold state to represent the association reference.</p>
 *
 * <p>The critic will trigger whenever an association between an interface and
 * a class is navigable <em>from</em> the interface. In an ideal world, it
 * wouldn't be possible to create this in ArgoUML.</p>
 *
 * <p>Internally we use some of the static utility methods of the {@link
 * org.argouml.cognitive.critics.CriticUtils CriticUtils} class.</p>
 *
 * @see <a href="http://argouml.tigris.org/documentation/snapshots/manual/argouml.html/#s2.ref.critics_nav_from_interface">ArgoUML User Manual: N</a>
 */

public class CrNavFromInterface extends CrUML {

    /**
     * <p>Constructor for the critic.</p>
     *
     * <p>Sets up the resource name, which will allow headline and description
     * to found for the current locale (replaces deprecated setHeadline and sd
     * methods). Provides a design issue category (RELATIONSHIPS) and knowledge
     * type (SYNTAX). Adds trigger "end_navigable".</p>
     *
     * @return nothing returned since this is a constructor.
     */

    public CrNavFromInterface() {

        // Set the resource label, which will get the headline and description
        // appropriate for the locale

        setResource("CrNavFromInterface");

        // Specify design issue category and knowledge type

        addSupportedDecision(CrUML.decRELATIONSHIPS);
        setKnowledgeTypes(Critic.KT_SYNTAX);

        // This may not actually make any difference at present (the code
        // behind addTrigger needs more work).

        addTrigger("end_navigable");
    }

    /**
     * <p>The trigger for the critic.</p>
     *
     * <p>Applies to Associations only, not AssociationRoles. The reason is
     * that an AssociationRole cannot have greater navigability than the
     * Association it specializes, so if the critic has addressed the
     * Association, the AssociationRole will effectively be addressed. There
     * may of course be a need for a critic to check that Association Roles do
     * match their parents in this respect!</p>
     *
     * <p>As a consequence, we also don't need to check for associations with
     * ClassifierRoles.</p>
     * 
     * <p>Iterate over all the AssociationEnds. We only have a problem if:</p>
     * <ol>
     *   <li><p>There is an end connected to an Interface; and</p></li>
     *   <li><p>An end other than that end is navigable.</p></li>
     * </ol>
     *
     * @param  dm    the object to be checked against the critic
     * @param  dsgr  the designer creating the model. Not used, this is for
     *               future development of ArgoUML
     * @return       {@link #PROBLEM_FOUND PROBLEM_FOUND} if the critic is
     *               triggered, otherwise {@link #NO_PROBLEM NO_PROBLEM}.  */
    
    public boolean predicate2(Object dm, Designer dsgr) {

        // Only look at Associations

        if (!(dm instanceof MAssociation)) {
            return NO_PROBLEM;
        }

        if (dm instanceof MAssociationRole) {
            return NO_PROBLEM;
        }

        // Get the Association and its connections.

        MAssociation asc = (MAssociation) dm;
        Collection conns = asc.getConnections();

        // Iterate over all the AssociationEnds. We only have a problem if 1)
        // there is an end connected to an Interface and 2) an end other than
        // that end is navigable. 

        Iterator enum = conns.iterator();

        boolean haveInterfaceEnd  = false ;  // End at an Interface?
        boolean otherEndNavigable = false ;  // Navigable other end?

        while (enum.hasNext()) {

            // The next AssociationEnd

            MAssociationEnd ae = (MAssociationEnd) enum.next();

            // If its an interface we have an interface end, otherwise its
            // something else and we should see if it is navigable. We don't
            // check that the end is a Classifier, rather than its child
            // ClassifierRole, since we have effectively eliminated that
            // possiblity in rejecting AssociationRoles above.

            if (ae.getType() instanceof MInterface) {
                haveInterfaceEnd = true;
            }
            else if (ae.isNavigable()) {
                otherEndNavigable = true;
            }

            // We can give up looking if we've hit both criteria

            if (haveInterfaceEnd && otherEndNavigable) {
                return PROBLEM_FOUND;
            }
        }

        // If we drop out we didn't meet both criteria, and all is well.

        return NO_PROBLEM;
    }

} /* end class CrNavFromInterface */

