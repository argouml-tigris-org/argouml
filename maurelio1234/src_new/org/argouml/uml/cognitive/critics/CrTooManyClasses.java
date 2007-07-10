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

import org.argouml.cognitive.Designer;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;

/**
 * A critic to detect when a classdiagram has too many classes. <p>
 *
 * TODO: currently it checks for nodes (classes, interfaces, comments).
 * This critic should be rewritten to work with namespaces.
 */
public class CrTooManyClasses extends AbstractCrTooMany {

    /**
     * The initial threshold.
     */
    private static final int CLASS_THRESHOLD = 20;

    /**
     * The constructor.
      */
    public CrTooManyClasses() {
	// TODO: <ocl>self.name</ocl> is not expanded for diagram objects
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.CLASS_SELECTION);
	setThreshold(CLASS_THRESHOLD);
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *      java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(dm instanceof UMLClassDiagram)) {
            return NO_PROBLEM;
        }
	UMLClassDiagram d = (UMLClassDiagram) dm;

	if (d.getGraphModel().getNodes().size() <= getThreshold()) {
            return NO_PROBLEM;
        }
	return PROBLEM_FOUND;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -3270186791825482658L;

} /* end class CrTooManyClasses */

