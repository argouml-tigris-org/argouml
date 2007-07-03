// $Id: SelectionComment.java 12908 2007-06-24 18:22:05Z mvw $
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

package org.argouml.uml.diagram.static_structure.ui;

import javax.swing.Icon;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.ui.SelectionNodeClarifiers2;
import org.tigris.gef.presentation.Fig;

/**
 * The buttons on Selection for a Comment.
 *
 * @author mvw@tigris.org
 */
public class SelectionComment extends SelectionNodeClarifiers2 {

    private static Icon linkIcon =
        ResourceLoaderWrapper.lookupIconResource("CommentLink");

    private static Icon icons[] =
    {linkIcon,
     linkIcon,
     linkIcon,
     linkIcon,
     null,
    };

    // TODO: I18N required
    private static String instructions[] =
    {"Link this comment",
     "Link this comment",
     "Link this comment",
     "Link this comment",
     null,
     "Move object(s)",
    };

    /**
     * Construct a new SelectionComment for the given Fig.
     *
     * @param f the given Fig
     */
    public SelectionComment(Fig f) {
        super(f);
    }

    @Override
    protected Icon[] getIcons() {
        return icons;
    }

    @Override
    protected String getInstructions(int index) {
        return instructions[index - BASE];
    }

    @Override
    protected Object getNewEdgeType(int index) {
        return CommentEdge.class;
    }

    @Override
    protected Object getNewNodeType(int index) {
        return Model.getMetaTypes().getComment();
    }

    @Override
    protected Object getNewNode(int index) {
        /* Alternatively, we could just return null here, 
         * so that you can not create a comment just 
         * linked to a comment this way - which is a bit uncommon,
         * but not illegal, so for consistency, we better allow it. 
         */
//        return null;
        return Model.getCoreFactory().createComment();
    }

}

