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

package org.argouml.uml.diagram.deployment.ui;

import javax.swing.Icon;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.SelectionNodeClarifiers2;
import org.tigris.gef.presentation.Fig;

/**
 * @author 5eichler@informatik.uni-hamburg.de
 */
public class SelectionNode extends SelectionNodeClarifiers2 {

    private static Icon associationIcon =
            ResourceLoaderWrapper.lookupIconResource("Association");

    private static Icon icons[] = 
    {associationIcon,
     associationIcon,
     associationIcon,
     associationIcon,
     null,
    };
    
    // TODO: I18N required
    private static String instructions[] = 
    {"Add a node",
     "Add a node",
     "Add a node",
     "Add a node",
     null,
     "Move object(s)",
    };

    /**
     * Construct a new SelectionNode for the given Fig.
     *
     * @param f The given Fig.
     */
    public SelectionNode(Fig f) {
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
        return Model.getMetaTypes().getAssociation();
    }

    @Override
    protected Object getNewNode(int index) {
        return Model.getCoreFactory().createNode();
    }
    
    @Override
    protected Object getNewNodeType(int index) {
        return Model.getMetaTypes().getNode();
    }

    @Override
    protected boolean isReverseEdge(int index) {
        if (index == BOTTOM || index == LEFT) {
            return true;
        }
        return false;
    }

}

