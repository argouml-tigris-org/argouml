// $Id: GoDiagramToEdge.java 12669 2007-05-26 19:02:43Z tfmorris $
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

package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.argouml.i18n.Translator;
import org.tigris.gef.base.Diagram;

/**
 * Rule for Diagram->Edge.
 *
 */
public class GoDiagramToEdge extends AbstractPerspectiveRule {

    /*
     * @see org.argouml.ui.explorer.rules.PerspectiveRule#getRuleName()
     */
    public String getRuleName() {
	return Translator.localize ("misc.diagram.edge");
    }

    /*
     * @see org.argouml.ui.explorer.rules.PerspectiveRule#getChildren(java.lang.Object)
     */
    public Collection getChildren(Object parent) {
        if (parent instanceof Diagram) {
            return ((Diagram) parent).getEdges();
        }
        return null;
    }

    /*
     * @see org.argouml.ui.explorer.rules.PerspectiveRule#getDependencies(java.lang.Object)
     */
    public Set getDependencies(Object parent) {
        // TODO: What?
	return Collections.EMPTY_SET;
    }
}
