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



// File: CrOperNameConflict.java
// Classes: CrOperNameConflict
// Original Author: jrobbins@ics.uci.edu
// $Id$

// 5 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Bug in detection of
// matching signatures fixed (was checking for matching parameter names, not
// just types). signaturesMatch() moved to CriticUtils.

// 8 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Signature simplified to
// ignore return types (like Java and C++). Javadoc notes this.


package org.argouml.uml.cognitive.critics;

import java.util.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;

import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;


/**
 * <p> A critic to detect when a class has operations with two matching
 *   signatures.</p>
 *
 * <p>Takes each operation in turn and compares its signature with all earlier
 *   operations. This version corrects and earlier bug, which checked for
 *   matching names as well as types in the parameter list.</p>
 *
 * <p><em>Warning</em>. The algorithm in is quadratic in the
 *   number of operations. It could be computationally demanding on a design
 *   where classes have a lot of operations. See the {@link
 *   #predicate2} method for possible solutions.</p>
 *
 * @see <a href="http://argouml.tigris.org/documentation/snapshots/manual/argouml.html/#s2.ref.oper_name_conflict">ArgoUML User Manual: Change Names or Signatures in &lt;artifact&gt;</a>
 */

public class CrOperNameConflict extends CrUML {

    /**
     * <p>Constructor for the critic.</p>
     *
     * <p>Sets up the resource name, which will allow headline and description
     *   to found for the current locale. Provides design issue categories
     *   (METHODS, NAMING), sets a knowledge type (SYNTAX) and adds triggers
     *   for metaclasses "behaviouralFeature" and feature_name".</p>
     *
     * @return  nothing returned since this is a constructor
     */
    public CrOperNameConflict() {

        setResource("CrOperNameConflict");

        addSupportedDecision(CrUML.decMETHODS);
        addSupportedDecision(CrUML.decNAMING);

        setKnowledgeTypes(Critic.KT_SYNTAX);

        // These may not actually make any difference at present (the code
        // behind addTrigger needs more work).

        addTrigger("behavioralFeature");
        addTrigger("feature_name");
    }


    /**
     * <p>The trigger for the critic.</p>
     *
     * <p>Finds all the operations for the given classifier. Takes each
     *   operation in turn and compares its signature with all earlier
     *   operations. This version corrects an earlier bug, which checked for
     *   matching names as well as types in the parameter list.</p>
     *
     * <p><em>Note</em>. The signature ignores any return parameters in looking
     *   for a match. This is in line with Java/C++.</p>
     *
     * <p>We do not need to worry about signature clashes that are inherited
     *   (overloading). This is something encouraged in many OO environments to
     *   facilitate polymorphism.</p>
     *
     * <p>This algorithm is quadratic in the number of operations. If this
     *   became a problem, we would have to consider sorting the operations
     *   vector and comparing only adjacent pairs (potentially O(n log n)
     *   performance).</p>
     *
     * @param  dm    the {@link java.lang.Object Object} to be checked against
     *               the critic.
     *
     * @param  dsgr  the {@link org.argouml.cognitive.Designer Designer}
     *               creating the model. Not used, this is for future
     *               development of ArgoUML.
     *
     * @return       {@link #PROBLEM_FOUND PROBLEM_FOUND} if the critic is
     *               triggered, otherwise {@link #NO_PROBLEM NO_PROBLEM}.  */
    
    public boolean predicate2(Object dm, Designer dsgr) {

        // Only do this for classifiers

        if (!(dm instanceof MClassifier)) {
            return NO_PROBLEM;
        }

        MClassifier cls = (MClassifier) dm;

        // Get all the features (giving up if there are none). Then loop
        // through finding all operations. Each time we find one, we compare
        // its signature with all previous (held in vector operSeen), and then
        // if it doesn't match add it to the vector.

        Collection str = cls.getFeatures();

        if (str == null) {
            return NO_PROBLEM;
        }

        Iterator enum   = str.iterator();
        Vector operSeen = new Vector();

        while (enum.hasNext()) {

            // Skip on if its not an operation

            MFeature f = (MFeature) enum.next();

            if (!(f instanceof MOperation)) {
                continue;
            }

            // Compare against all earlier operations. If there's a match we've
            // found the problem

            MOperation op   = (MOperation) f;
            int        size = operSeen.size();

            for (int i = 0; i < size; i++) {
                MOperation otherOp = (MOperation) operSeen.elementAt(i);

                if (CriticUtils.signaturesMatch(op, otherOp)) {
                    return PROBLEM_FOUND;
                }
            }

            // Add to the vector and round to look at the next one

            operSeen.addElement(op);
        }

        // If we drop out here, there was no match and we have no problem

        return NO_PROBLEM;
    }


    /**
     * <p>Return the icon to be used for the clarifier for this critic.</p>
     *
     * <p>A clarifier is the graphical highlight used to show the presence of a
     *   critique. For example wavy colored underlines beneath operations.</p>
     *
     * <p>In this case it will be a wavy line under the second of the clashing
     *   operations.</p>
     *
     * @return       The {@link javax.swing.Icon Icon} to use.  */
    
    public Icon getClarifier() {
        return ClOperationCompartment.TheInstance;
    }

} /* end class CrOperNameConflict.java */








