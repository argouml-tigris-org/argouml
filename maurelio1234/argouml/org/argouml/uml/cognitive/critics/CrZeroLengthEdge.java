// $Id: CrZeroLengthEdge.java 12753 2007-06-04 18:07:56Z mvw $
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

import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.uml.cognitive.UMLDecision;
import org.tigris.gef.presentation.FigEdge;

/** A critic to detect when an edge is very short in order to suggest to
 *  improve the layout of the diagram.
 *
 * @author jrobbins
 */
public class CrZeroLengthEdge extends CrUML {
    ////////////////////////////////////////////////////////////////
    // constants
    private static final int THRESHOLD = 20;

    /**
     * The constructor.
     *
     */
    public CrZeroLengthEdge() {
	// TODO: {name} is not expanded for diagram objects
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.RELATIONSHIPS);
	addSupportedDecision(UMLDecision.INHERITANCE);
	addSupportedDecision(UMLDecision.STATE_MACHINES);
	setKnowledgeTypes(Critic.KT_PRESENTATION);
    }

    ////////////////////////////////////////////////////////////////
    // critiquing API

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *      java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(dm instanceof FigEdge)) return NO_PROBLEM;
	FigEdge fe = (FigEdge) dm;
	int length = fe.getPerimeterLength();
	if (length > THRESHOLD) return NO_PROBLEM;
	return PROBLEM_FOUND;
    }


} /* end class CrZeroLengthEdge */

