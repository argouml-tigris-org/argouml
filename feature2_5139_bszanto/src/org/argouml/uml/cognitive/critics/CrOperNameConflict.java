// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.Icon;

import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;

/**
 * A critic to detect when a class has operations with two matching
 * signatures.<p>
 *
 * Takes each operation in turn and compares its signature with all
 * earlier operations. This version corrects and earlier bug, which
 * checked for matching names as well as types in the parameter
 * list.<p>
 *
 * <em>Warning</em>. The algorithm in is quadratic in the number of
 * operations. It could be computationally demanding on a design where
 * classes have a lot of operations. See the {@link #predicate2}
 * method for possible solutions.<p>
 *
 * See the ArgoUML User Manual: Change Names or Signatures in &lt;artifact&gt;
 *
 * @author jrobbins@ics.uci.edu
 */

public class CrOperNameConflict extends CrUML {

    /**
     * Constructor for the critic.<p>
     *
     * Sets up the resource name, which will allow headline and
     * description to found for the current locale. Provides design
     * issue categories (METHODS, NAMING), sets a knowledge type
     * (SYNTAX) and adds triggers for metaclasses "behaviouralFeature"
     * and feature_name".<p>
     */
    public CrOperNameConflict() {
        setupHeadAndDesc();
        addSupportedDecision(UMLDecision.METHODS);
        addSupportedDecision(UMLDecision.NAMING);

        setKnowledgeTypes(Critic.KT_SYNTAX);

        // These may not actually make any difference at present (the code
        // behind addTrigger needs more work).

        addTrigger("behavioralFeature");
        addTrigger("feature_name");
    }


    /**
     * The trigger for the critic.<p>
     *
     * Finds all the operations for the given classifier. Takes each
     * operation in turn and compares its signature with all earlier
     * operations. This version corrects an earlier bug, which checked
     * for matching names as well as types in the parameter list.<p>
     *
     * <em>Note</em>. The signature ignores any return parameters in
     * looking for a match. This is in line with Java/C++.<p>
     *
     * We do not need to worry about signature clashes that are
     * inherited (overloading). This is something encouraged in many
     * OO environments to facilitate polymorphism.<p>
     *
     * This algorithm is quadratic in the number of operations. If
     * this became a problem, we would have to consider sorting the
     * operations list and comparing only adjacent pairs
     * (potentially O(n log n) performance).<p>
     *
     * @param  dm    the {@link Object} to be checked against the critic.
     *
     * @param  dsgr  the {@link Designer} creating the model. Not used,
     *               this is for future development of ArgoUML.
     *
     * @return       {@link #PROBLEM_FOUND PROBLEM_FOUND} if the critic is
     *               triggered, otherwise {@link #NO_PROBLEM NO_PROBLEM}.
     */
    @Override
    public boolean predicate2(Object dm, Designer dsgr) {

        // Only do this for classifiers

        if (!(Model.getFacade().isAClassifier(dm))) {
            return NO_PROBLEM;
        }

        // Get all the features (giving up if there are none). Then loop
        // through finding all operations. Each time we find one, we compare
        // its signature with all previous (held in collection operSeen), and then
        // if it doesn't match add it to the collection.

        Collection operSeen = new ArrayList();
        for (Object op : Model.getFacade().getOperations(dm)) {

            // Compare against all earlier operations. If there's a match we've
            // found the problem
            for (Object o : operSeen) {
                if (signaturesMatch(op, o)) {
                    return PROBLEM_FOUND;
                }
            }

            // Add to the collection and round to look at the next one

            operSeen.add(op);
        }

        // If we drop out here, there was no match and we have no problem

        return NO_PROBLEM;
    }


    /**
     * Return the icon to be used for the clarifier for this critic.<p>
     *
     * A clarifier is the graphical highlight used to show the
     * presence of a critique. For example wavy colored underlines
     * beneath operations.<p>
     *
     * In this case it will be a wavy line under the second of the
     * clashing operations.<p>
     *
     * @return       The {@link javax.swing.Icon Icon} to use.
     */
    @Override
    public Icon getClarifier() {
        return ClOperationCompartment.getTheInstance();
    }


    /**
     * Sees if the signatures of two Operations are the same.<p>
     *
     * Checks for matching operation name, and list of parameter
     * types. The order of the parameters is significant.
     *
     * This version also checks for the parameter kind, since
     * otherwise, "op(int a)" and "op():int" appear to have the same
     * signature. Purists would probably suggest that the kind should
     * match exactly. However we only differentiate the return
     * parameter(s). It is unlikely that any practical OO language
     * would be able to distinguish instantiation of in from out from
     * inout parameters.<p>
     *
     * We ignore return parameters completely. This is in line with
     * Java/C++ which regard <code>int x(int, int)</code> and
     * <code>double x(int, int)</code> as having the same
     * signature.<p>
     *
     * If you need to modify this method, take care, since there are
     * numerous "telegraph pole" problems involved in working through
     * pairs of mixed lists.<p>
     *
     * @param op1 the first operation whose signature is being compared.
     * @param op2 the second operation whose signature is being compared.
     *
     * @return    <code>true</code> if the signatures match, <code>false</code>
     *            otherwise.
     */
    private boolean signaturesMatch(Object op1, Object op2) {

	// Check that the names match.

	String name1 = Model.getFacade().getName(op1);
	if (name1 == null) {
	    return false;
	}

	String name2 = Model.getFacade().getName(op2);
	if (name2 == null) {
	    return false;
	}

	if (!name1.equals(name2)) {
	    return false;
	}

	// Check that the parameter lists match.

	Iterator params1 = Model.getFacade().getParameters(op1).iterator();
	Iterator params2 = Model.getFacade().getParameters(op2).iterator();

	while (params1.hasNext()
	       && params2.hasNext()) {

	    // Get the next non-return parameter. Null if non left.
	    Object p1 = null;
	    while (p1 == null && params1.hasNext()) {
		p1 = params1.next();
		if (Model.getFacade().isReturn(p1))
		    p1 = null;
	    }

	    Object p2 = null;
	    while (p2 == null && params1.hasNext()) {
		p2 = params1.next();
		if (Model.getFacade().isReturn(p2))
		    p2 = null;
	    }

	    if (p1 == null && p2 == null)
		return true;	// Both lists have the same length

	    // Different lengths:
	    if (p1 == null || p2 == null) {
                return false;
            }

	    // Compare the type of the parameters. If any of the types is
	    // null, then we have a match.
	    Object p1type = Model.getFacade().getType(p1);
	    if (p1type == null) {
		continue;
	    }

	    Object p2type = Model.getFacade().getType(p2);
	    if (p2type == null) {
		continue;
	    }

	    if (!p1type.equals(p2type)) {
		return false;
	    }

	    // This pair of params where the same. Lets check the next pair.
	}

	if (!params1.hasNext() && !params2.hasNext()) {
            // Both lists have the same length.
            return true;
        }

	return false;
    }

}
