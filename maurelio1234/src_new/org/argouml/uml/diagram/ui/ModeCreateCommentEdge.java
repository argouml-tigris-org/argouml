// $Id: ModeCreateCommentEdge.java 12908 2007-06-24 18:22:05Z mvw $
// Copyright (c) 2005-2007 The Regents of the University of California. All
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

import org.argouml.model.Model;
import org.argouml.uml.CommentEdge;
import org.tigris.gef.presentation.Fig;

/**
 * A Mode to interpret user input while creating a comment edge.
 * The comment can connect an existing comment node to any other existing
 * node or edge.
 */
public final class ModeCreateCommentEdge extends ModeCreateGraphEdge {
    
    /*
     * If we're drawing to an edge then only allow if the start is a comment
     * @see org.argouml.uml.diagram.ui.ModeCreateGraphEdge#isConnectionValid(org.tigris.gef.presentation.Fig, org.tigris.gef.presentation.Fig)
     */
    protected final boolean isConnectionValid(Fig source, Fig dest) {
	if (dest instanceof FigNodeModelElement) {
            return Model.getFacade().isAComment(source.getOwner());
	} else {
	    return true;
	}
    }

    protected final Object getMetaType() {
	return CommentEdge.class;
    }
}
