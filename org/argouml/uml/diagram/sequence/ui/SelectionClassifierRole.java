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

package org.argouml.uml.diagram.sequence.ui;

import org.argouml.uml.diagram.ui.SelectionNodeClarifiers;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.Handle;

/**
 * A custom select object to handle the special requirements of reshaping
 * a classifier role.
 *
 * @author Bob Tarling
 */
public class SelectionClassifierRole extends SelectionNodeClarifiers {

    /**
     * The constructor.
     *
     * @param f the fig
     */
    public SelectionClassifierRole(Fig f) {
        super(f);
    }

    /**
     * Make sure that the north facing handles cannot be dragged as
     * part of a resize.
     *
     * @see org.tigris.gef.base.Selection#dragHandle(int, int, int, int,
     * org.tigris.gef.presentation.Handle)
     */
    public void dragHandle(int mX, int mY, int anX, int anY, Handle hand) {

        if (!getContent().isResizable()) {
            return;
        }

        switch (hand.index) {
	case Handle.NORTHWEST :
	case Handle.NORTH :
	case Handle.NORTHEAST :
	    return;
	default:
        }

        super.dragHandle(mX, mY, anX, anY, hand);
    }

    /**
     * @see org.argouml.uml.diagram.ui.SelectionNodeClarifiers#getNewNode(int)
     */
    protected Object getNewNode(int buttonCode) {
        return null;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 3570571152608122095L;
}
