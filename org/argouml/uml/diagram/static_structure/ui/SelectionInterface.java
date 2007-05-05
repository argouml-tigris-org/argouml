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

package org.argouml.uml.diagram.static_structure.ui;

import javax.swing.Icon;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.SelectionNodeClarifiers2;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;

/**
 * @author jrobbins@ics.uci.edu
 */
public class SelectionInterface extends SelectionNodeClarifiers2 {
    /**
     * Remember the pressed button, 
     * for the case where the mouse is released not above a fig.
     */
    private int code;

    private static Icon realiz =
        ResourceLoaderWrapper.lookupIconResource("Realization");

    private static Icon inherit =
        ResourceLoaderWrapper.lookupIconResource("Generalization");

    private static Icon icons[] = {null,
                                   null,
                                   null,
                                   null,
                                   null,
                                   null,
                                   null,                                   
                                   null,
                                   null,
                                   null, // 9
                                   inherit,
                                   realiz,
                                   null,
                                   null,
                                   null,
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

    /*
     * @see org.tigris.gef.base.SelectionButtons#createEdgeAbove(
     *         org.tigris.gef.graph.MutableGraphModel, java.lang.Object)
     */
    protected Object createEdgeAbove(MutableGraphModel gm, Object newNode) {
        return gm.connect(getContent().getOwner(), newNode,
               (Class) Model.getMetaTypes().getGeneralization());
    }

    /*
     * @see org.tigris.gef.base.SelectionButtons#createEdgeUnder(
     *      org.tigris.gef.graph.MutableGraphModel, java.lang.Object)
     */
    protected Object createEdgeUnder(MutableGraphModel gm, Object newNode) {
        return gm.connect(newNode, getContent().getOwner(), (Class) Model
                .getMetaTypes().getAbstraction());
    }

    /*
     * @see org.tigris.gef.base.SelectionButtons#getNewNode(int)
     */
    protected Object getNewNode(int buttonCode) {
        if (buttonCode < 10) {
            buttonCode = code;
        }
        if (buttonCode == 10) {
            return Model.getCoreFactory().buildInterface();
        } else {
            return Model.getCoreFactory().buildClass();
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 7209387830978444644L;


    /*
     * @see org.argouml.uml.diagram.ui.SelectionNodeClarifiers2#getNewEdgeType(int)
     */
    protected Object getNewEdgeType(int index) {
        if (index == 10) {
            return Model.getMetaTypes().getGeneralization();
        } else if (index == 11) {
            return Model.getMetaTypes().getAbstraction();
        }
        return null;
    }

    /*
     * @see org.argouml.uml.diagram.ui.SelectionNodeClarifiers2#getNewNodeType(int)
     */
    protected Object getNewNodeType(int index) {
        if (index == 10) {
            return Model.getMetaTypes().getInterface();
        } else if (index == 11) {
            return Model.getMetaTypes().getUMLClass();
        }
        return null;
    }

    /*
     * @see org.argouml.uml.diagram.ui.SelectionNodeClarifiers2#getIcons()
     */
    protected Icon[] getIcons() {
        return icons;
    }

    /*
     * @see org.argouml.uml.diagram.ui.SelectionNodeClarifiers2#getInstructions(int)
     */
    protected String getInstructions(int index) {
        if (index == 10) {
            return "Add an interface";
        } else if (index == 11) {
            return "Add a realization";
        } else if (index == -1) {
            return "Move object(s)";
        }
        return null;
    }

    /*
     * @see org.argouml.uml.diagram.ui.SelectionNodeClarifiers2#isDragEdgeReverse(int)
     */
    protected boolean isDragEdgeReverse(int index) {
        if (index == 11) {
            return true;
        }
        return false;
    }

    /*
     * @see org.argouml.uml.diagram.ui.SelectionNodeClarifiers2#isEdgePostProcessRequested()
     */
    protected boolean isEdgePostProcessRequested() {
        return false;
    }
}
