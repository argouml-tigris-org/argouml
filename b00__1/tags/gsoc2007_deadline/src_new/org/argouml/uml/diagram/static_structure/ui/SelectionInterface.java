// $Id:SelectionInterface.java 12612 2007-05-13 18:30:40Z tfmorris $
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

package org.argouml.uml.diagram.static_structure.ui;

import javax.swing.Icon;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.SelectionNodeClarifiers2;
import org.tigris.gef.presentation.Fig;

/**
 * @author jrobbins@ics.uci.edu
 */
public class SelectionInterface extends SelectionNodeClarifiers2 {

    private static Icon realiz =
        ResourceLoaderWrapper.lookupIconResource("Realization");

    private static Icon inherit =
        ResourceLoaderWrapper.lookupIconResource("Generalization");

    private static Icon icons[] = 
    {inherit,
     realiz,
     null,
     null,
     null,
    };
    
    private static String instructions[] = 
    {"Add an interface",
     "Add a realization",
     null,
     null,
     null,
     "Move object(s)",
    };
    
    /**
     * Construct a new SelectionInterface for the given Fig.
     *
     * @param f
     *            The given Fig.
     */
    public SelectionInterface(Fig f) {
        super(f);
    }

    @Override
    protected Object getNewNode(int index) {
        if (index == 0) {
            index = getButton();
        }
        if (index == TOP) {
            return Model.getCoreFactory().buildInterface();
        } else {
            return Model.getCoreFactory().buildClass();
        }
    }

    @Override
    protected Object getNewEdgeType(int index) {
        if (index == TOP) {
            return Model.getMetaTypes().getGeneralization();
        } else if (index == BOTTOM) {
            return Model.getMetaTypes().getAbstraction();
        }
        return null;
    }


    @Override
    protected Object getNewNodeType(int index) {
        if (index == TOP) {
            return Model.getMetaTypes().getInterface();
        } else if (index == BOTTOM) {
            return Model.getMetaTypes().getUMLClass();
        }
        return null;
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
    protected boolean isReverseEdge(int index) {
        if (index == 11) {
            return true;
        }
        return false;
    }

}
