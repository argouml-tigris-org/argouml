/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michiel van der Wulp
 *    Katharina Fahnenbruck
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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
import java.util.Collection;

import javax.swing.Icon;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.uml.diagram.deployment.DeploymentDiagramGraphModel;
import org.argouml.uml.diagram.ui.SelectionClassifierBox;
import org.argouml.uml.diagram.ui.SelectionNodeClarifiers2;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.ModeCreateEdgeAndNode;
import org.tigris.gef.presentation.Fig;


/**
 * @author jrobbins@ics.uci.edu
 */
public class SelectionClass extends SelectionClassifierBox {

    private static Icon inherit =
        ResourceLoaderWrapper.lookupIconResource("Generalization");

    private static Icon assoc =
        ResourceLoaderWrapper.lookupIconResource("Association");

    private static Icon compos =
        ResourceLoaderWrapper.lookupIconResource("CompositeAggregation");

    private static Icon selfassoc =
        ResourceLoaderWrapper.lookupIconResource("SelfAssociation");

    private boolean useComposite;

    private static Icon icons[] = 
    {inherit,
     inherit,
     assoc,
     assoc,
     selfassoc,
    };
    
    // TODO: I18N required
    private static String instructions[] = 
    {"Add a superclass",
     "Add a subclass",
     "Add an associated class",
     "Add an associated class",
     "Add a self association",
     "Move object(s)",
    };

    private static Object edgeType[] = 
    {Model.getMetaTypes().getGeneralization(),
     Model.getMetaTypes().getGeneralization(),
     Model.getMetaTypes().getAssociation(),
     Model.getMetaTypes().getAssociation(),
     Model.getMetaTypes().getAssociation(),
    };

    /**
     * Construct a new SelectionClass for the given Fig.
     *
     * @param f The given Fig.
     */
    public SelectionClass(Fig f) { 
        super(f);
    }

    @Override
    protected Icon[] getIcons() {
        Icon workingIcons[] = new Icon[icons.length];
        System.arraycopy(icons, 0, workingIcons, 0, icons.length);

        // No Generalizations on Deployment Diagram
        if (Globals.curEditor().getGraphModel() 
                instanceof DeploymentDiagramGraphModel) {
            workingIcons[TOP - BASE] = null;
            workingIcons[BOTTOM - BASE] = null;
        }
        if (useComposite) {
            workingIcons[LEFT - BASE] = compos;
            workingIcons[RIGHT - BASE] = compos;
        } 
        // Readonly class: no generalization, no association to self
        if (Model.getModelManagementHelper().isReadOnly(
                getContent().getOwner())) {
            return new Icon[] {null, inherit, null, null, null };
        }
        return workingIcons;
    }
    
    @Override
    protected String getInstructions(int index) {
        return instructions[index - BASE];
    }

    @Override
    protected Object getNewNodeType(int i) {
        return Model.getMetaTypes().getUMLClass();
    }

    @Override
    protected Object getNewEdgeType(int i) {
        if (i == 0) {
            i = getButton();
        }
        return edgeType[i - 10];
    }

    @Override
    protected boolean isReverseEdge(int i) {
        if (i == BOTTOM || i == LEFT) {
            return true;
        } 
        return false;
    }
    
    @Override
    protected boolean isDraggableHandle(int index) {
        // Self-association isn't draggable
        if (index == LOWER_LEFT) {
            return false;
        }
        return true;
    }

    @Override
    protected boolean isEdgePostProcessRequested() {
        return useComposite;
    }

    @Override
    protected ModeCreateEdgeAndNode getNewModeCreateEdgeAndNode(Editor ce,
            Object theEdgeType, boolean postProcess,
            SelectionNodeClarifiers2 nodeCreator) {
        return  new ModeCreateEdgeAndNodeWithComposition(ce,
                theEdgeType, postProcess, nodeCreator);
    }

    @Override
    protected void postProcessEdge2(Object newEdge) {
        if (Model.getFacade().isAAssociation(newEdge)) {
            Collection assocEnds = Model.getFacade().getConnections(newEdge);
            Object firstAE = assocEnds.iterator().next();
            Object aggregationKind = Model.getAggregationKind().getComposite();
            Model.getCoreHelper().setAggregation1(firstAE, aggregationKind);
        }
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        super.mouseEntered(me);
        useComposite = me.isShiftDown();
    }

    @Override
    protected Object getNewNode(int index) {
        return Model.getCoreFactory().buildClass();
    }

    /**
     * My derived version that handles the creation of composition associations.
     *
     * @author Michiel
     */
    class ModeCreateEdgeAndNodeWithComposition extends ModeCreateEdgeAndNode {

        public ModeCreateEdgeAndNodeWithComposition(Editor ce, Object edgeType,
                boolean postProcess, SelectionNodeClarifiers2 nodeCreator) {
            super(ce, edgeType, postProcess, nodeCreator);
        }

        @Override
        protected void postProcessEdge(Object newEdge) {
            postProcessEdge2(newEdge);
        }
    }

}
