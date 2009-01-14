// $Id$
// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.awt.Graphics;

import org.tigris.gef.base.Globals;
import org.tigris.gef.base.PathItemPlacementStrategy;
import org.tigris.gef.base.SelectionManager;
import org.tigris.gef.base.SelectionReshape;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;

/**
 *
 *
 *
 * @author jrobbins
 */
public class SelectionEdgeClarifiers extends SelectionReshape {

    ////////////////////////////////////////////////////////////////
    // constructors

    /** Construct a new SelectionEdgeClarifiers for the given Fig
     *
     * @param f the given fig
     */
    public SelectionEdgeClarifiers(Fig f) { super(f); }

    /**
     * This extends the standard selection painting to also highlight
     * the editable text labels and their placement strategies should
     * there be only one selected item.
     *
     * @see org.tigris.gef.base.Selection#paint(java.awt.Graphics)
     * @param g the graphics object
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int selectionCount =
            Globals.curEditor().getSelectionManager().selections().size();
        if (selectionCount == 1) {
            FigEdgeModelElement edge = (FigEdgeModelElement) getContent();
            edge.paintClarifiers(g);
	    for (PathItemPlacementStrategy strategy
	            : edge.getPathItemStrategies()) {
	        strategy.paint(g);
	    }
	}
    }

} /* end class SelectionEdgeClarifiers */

