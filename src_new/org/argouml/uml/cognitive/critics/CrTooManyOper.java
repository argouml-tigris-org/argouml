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
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;

/**
 * A critic to detect when a classifier has to many operations). <p>
 *
 * TODO: exclude getter and setter operations from count
 */
public class CrTooManyOper extends AbstractCrTooMany {

    /**
     * The initial threshold.
     */
    private static final int OPERATIONS_THRESHOLD = 20;

    /**
     * The constructor.
     */
    public CrTooManyOper() {
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.METHODS);
	setThreshold(OPERATIONS_THRESHOLD);
	addTrigger("behavioralFeature");
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *      java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(Model.getFacade().isAClassifier(dm))) {
            return NO_PROBLEM;
        }

	// TODO: consider inherited attributes?
	Collection str = Model.getFacade().getFeatures(dm);
	if (str == null) {
            return NO_PROBLEM;
        }
	int n = 0;
	for (Iterator iter = str.iterator(); iter.hasNext();) {
	    if (Model.getFacade().isABehavioralFeature(iter.next())) {
		n++;
            }
	}
	if (n <= getThreshold()) {
            return NO_PROBLEM;
        }
	return PROBLEM_FOUND;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 3221965323817473947L;

} /* end class CrTooManyOper */
