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

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.critics.Critic;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;

/**
 * A critic to check that the classifiers associated with the ends of an
 * association are in the same namespace as the association.<p>
 *
 * This is the fourth well-formedness rule for associations in the UML 1.3
 * standard (see section 2.5.3 of the standard).<p>
 *
 * See the ArgoUML User Manual: Classifier not in Namespace of its Association
 *
 * @author Jason Robbins
 */
public class CrCrossNamespaceAssoc extends CrUML {
    /**
     * Constructor for the critic.<p>
     *
     * Sets up the resource name, which will allow headline and description
     * to found for the current locale. Provides a design issue category
     * (MODULARITY) and a knowledge type (SYNTAX).
     */
    public CrCrossNamespaceAssoc() {
        setupHeadAndDesc();
        addSupportedDecision(UMLDecision.MODULARITY);
        setKnowledgeTypes(Critic.KT_SYNTAX);
    }

    /**
     * The trigger for the critic.<p>
     *
     * Get the association. Then loop through the association ends, checking
     * that their associated classifiers are in the namespace, i.e. are part of
     * the same model or subsystem.<p>
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
        // Only look at associations
        if (!Model.getFacade().isAAssociation(dm)) {
            return NO_PROBLEM;
        }

        Object ns = Model.getFacade().getNamespace(dm);
        if (ns == null) {
            return PROBLEM_FOUND;
        }

        // Get the Association and its connections.
        // Iterate over all the AssociationEnds and check that each connected
        // classifier is in the same sub-system or model
        Iterator assocEnds = Model.getFacade().getConnections(dm).iterator();
        while (assocEnds.hasNext()) {
            // The next AssociationEnd, and its classifier. Check the
            // classifier is in the namespace of the association. If not we
            // have a problem.
            Object clf = Model.getFacade().getType(assocEnds.next());
            if (clf != null && ns != Model.getFacade().getNamespace(clf)) {
                return PROBLEM_FOUND;
            }
        }
        // If we drop out there is no problem
        return NO_PROBLEM;
    }
} /* end class CrCrossNamespaceAssoc */
