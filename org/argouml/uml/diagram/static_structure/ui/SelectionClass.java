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

import java.awt.event.MouseEvent;

import javax.swing.Icon;

import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.uml.diagram.deployment.DeploymentDiagramGraphModel;
import org.argouml.uml.diagram.ui.SelectionNodeClarifiers2;
import org.tigris.gef.base.Globals;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;

/**
 * @author jrobbins@ics.uci.edu
 */
public class SelectionClass extends SelectionNodeClarifiers2 {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(SelectionClass.class);

    private static Icon inherit =
        ResourceLoaderWrapper.lookupIconResource("Generalization");

    private static Icon assoc =
        ResourceLoaderWrapper.lookupIconResource("Association");

    private static Icon compos =
        ResourceLoaderWrapper.lookupIconResource("CompositeAggregation");

    private static Icon selfassoc =
        ResourceLoaderWrapper.lookupIconResource("SelfAssociation");

    private boolean useComposite;

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
                                   inherit,
                                   assoc,
                                   assoc,
                                   selfassoc,
    };
    
    // TODO: I18N required
    private static String instructions[] = {null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null, // 9
                                            "Add a superclass",
                                            "Add a subclass",
                                            "Add an associated class",
                                            "Add an associated class",
                                            "Add a self association",
                                            "Move object(s)",
    };

    private static Object edgeType[] = {null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,                                   
                                        null,
                                        null,
                                        null, // 9
                                        Model.getMetaTypes().getGeneralization(),
                                        Model.getMetaTypes().getGeneralization(),
                                        Model.getMetaTypes().getAssociation(),
                                        Model.getMetaTypes().getAssociation(),
                                        null, // no drag for self-association
    };

    /**
     * Construct a new SelectionClass for the given Fig.
     *
     * @param f The given Fig.
     */
    public SelectionClass(Fig f) { 
        super(f);
    }

    /*
     * @see org.argouml.uml.diagram.ui.SelectionNodeClarifiers2#getIcons()
     */
    protected Icon[] getIcons() {
        Icon workingIcons[] = icons;

        // No Generalizations on Deployment Diagram
        if (Globals.curEditor().getGraphModel() instanceof DeploymentDiagramGraphModel) {
            workingIcons[10] = null;
            workingIcons[11] = null;
        }
        if (useComposite) {
            workingIcons[12] = compos;
            workingIcons[12] = compos;
        } 
        return workingIcons;
    }
    
    /*
     * @see org.argouml.uml.diagram.ui.SelectionNodeClarifiers2#getInstructions(int)
     */
    protected String getInstructions(int index) {
        return instructions[index];
    }

    /*
     * @see org.argouml.uml.diagram.ui.SelectionNodeClarifiers2#getNewNodeType(int)
     */
    protected Object getNewNodeType(int i) {
        return Model.getMetaTypes().getUMLClass();
    }
    
    /*
     * @see org.argouml.uml.diagram.ui.SelectionNodeClarifiers2#getNewEdgeType(int)
     */
    protected Object getNewEdgeType(int i) {
        return edgeType[i];
    }
    
    /*
     * @see org.argouml.uml.diagram.ui.SelectionNodeClarifiers2#isDragEdgeReverse(int)
     */
    protected boolean isDragEdgeReverse(int i) {
        if (i == 11 || i == 13) {
            return true;
        } 
        return false;
    }
   
    /*
     * @see org.argouml.uml.diagram.ui.SelectionNodeClarifiers2#isEdgePostProcessRequested()
     */
    protected boolean isEdgePostProcessRequested() {
        return useComposite;
    }
    
    /*
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent me) {
        super.mouseEntered(me);
        useComposite = me.isShiftDown();
    }

    /*
     * @see org.tigris.gef.base.SelectionButtons#getNewNode(int)
     */
    protected Object getNewNode(int buttonCode) {
        return Model.getCoreFactory().buildClass();
    }

    /*
     * @see org.tigris.gef.base.SelectionButtons#createEdgeAbove(
     *         org.tigris.gef.graph.MutableGraphModel, java.lang.Object)
     */
    protected Object createEdgeAbove(MutableGraphModel mgm, Object newNode) {
        return mgm.connect(getContent().getOwner(), newNode,
			   (Class) Model.getMetaTypes().getGeneralization());
    }

    /*
     * @see org.tigris.gef.base.SelectionButtons#createEdgeLeft(
     *         org.tigris.gef.graph.MutableGraphModel, java.lang.Object)
     */
    protected Object createEdgeLeft(MutableGraphModel mgm, Object newNode) {
        return mgm.connect(newNode, getContent().getOwner(),
			   (Class) Model.getMetaTypes().getAssociation());
    }

    /*
     * @see org.tigris.gef.base.SelectionButtons#createEdgeRight(
     *         org.tigris.gef.graph.MutableGraphModel, java.lang.Object)
     */
    protected Object createEdgeRight(MutableGraphModel mgm, Object newNode) {
        return mgm.connect(getContent().getOwner(), newNode,
			   (Class) Model.getMetaTypes().getAssociation());
    }

    /*
     * @see org.tigris.gef.base.SelectionButtons#createEdgeToSelf(
     *         org.tigris.gef.graph.MutableGraphModel)
     */
    protected Object createEdgeToSelf(MutableGraphModel mgm) {
        return mgm.connect(getContent().getOwner(), getContent().getOwner(),
			   (Class) Model.getMetaTypes().getAssociation());
    }

    /*
     * @see org.tigris.gef.base.SelectionButtons#createEdgeUnder(
     *         org.tigris.gef.graph.MutableGraphModel, java.lang.Object)
     */
    protected Object createEdgeUnder(MutableGraphModel mgm, Object newNode) {
        return mgm.connect(newNode, getContent().getOwner(),
			   (Class) Model.getMetaTypes().getGeneralization());
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -5724040863222115747L;
} /* end class SelectionClass */
