/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.model.StateMachinesFactory;
import org.argouml.ui.UndoableAction;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.state.StateDiagramGraphModel;
import org.argouml.uml.diagram.state.ui.FigCompositeState;
import org.argouml.uml.diagram.state.ui.FigConcurrentRegion;
import org.argouml.uml.diagram.state.ui.FigStateVertex;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.LayerDiagram;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;

//TODO: There is a cyclic dependency between this class and FigConcurrentRegion

/**
 * Add a concurrent region to a concurrent composite state
 * <p>
 * This action can be executed with either
 * the composite concurrent state selected,
 * or one of its concurrent regions.
 * <p>
 * TODO: Move all the magic numbers to constants
 *
 * @author pepargouml@yahoo.es
 */
public class ActionAddConcurrentRegion extends UndoableAction {


    /** logger */
    private static final Logger LOG =
        Logger.getLogger(ActionAddConcurrentRegion.class.getName());

    /**
     * Constructor
     */
    public ActionAddConcurrentRegion() {
        super(Translator.localize("action.add-concurrent-region"),
                ResourceLoaderWrapper.lookupIcon(
                        "action.add-concurrent-region"));
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION,
                Translator.localize("action.add-concurrent-region"));
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    /*
     * @see javax.swing.Action#isEnabled()
     */
    public boolean isEnabled() {
        Object target = TargetManager.getInstance().getModelTarget();
        if (Model.getStateMachinesHelper().isTopState(target)) {
            return false;
        }
        return TargetManager.getInstance().getModelTargets().size() < 2;
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        super.actionPerformed(ae);
        try {
            /*Here the actions to divide a region*/
            Fig f = TargetManager.getInstance().getFigTarget();

            if (Model.getFacade().isAConcurrentRegion(f.getOwner())) {
                f = f.getEnclosingFig();
            }

            final FigCompositeState figCompositeState = (FigCompositeState) f;

            final List<FigConcurrentRegion> regionFigs =
                ((List<FigConcurrentRegion>) f.getEnclosedFigs().clone());
            final Object umlCompositeState = figCompositeState.getOwner();
            Editor editor = Globals.curEditor();
            GraphModel gm = editor.getGraphModel();
            LayerDiagram lay =
                ((LayerDiagram) editor.getLayerManager().getActiveLayer());

            Rectangle rName =
                ((FigNodeModelElement) f).getNameFig().getBounds();
            Rectangle rFig = f.getBounds();
            if (!(gm instanceof MutableGraphModel)) {
                return;
            }

            StateDiagramGraphModel mgm = (StateDiagramGraphModel) gm;

            final StateMachinesFactory factory =
                Model.getStateMachinesFactory();

            if (!Model.getFacade().isConcurrent(umlCompositeState)) {

                final Object umlRegion1 =
                    factory.buildCompositeState(umlCompositeState);
                Rectangle bounds = new Rectangle(
                        f.getX() + FigConcurrentRegion.INSET_HORZ,
                        f.getY() + rName.height
                            + FigConcurrentRegion.INSET_VERT,
                        rFig.width - 2 * FigConcurrentRegion.INSET_HORZ,
                        rFig.height - rName.height
                            - 2 * FigConcurrentRegion.INSET_VERT);
                DiagramSettings settings = figCompositeState.getSettings();
                final FigConcurrentRegion firstRegionFig =
                    new FigConcurrentRegion(
                        umlRegion1, bounds, settings);
                /* The 1st region has an invisible divider line
                 * (the box is always invisible): */
                firstRegionFig.setLineColor(ArgoFig.INVISIBLE_LINE_COLOR);

                firstRegionFig.setEnclosingFig(figCompositeState);
                firstRegionFig.setLayer(lay);
                lay.add(firstRegionFig);

                if (mgm.canAddNode(umlRegion1)) {
                    mgm.getNodes().add(umlRegion1);
                    mgm.fireNodeAdded(umlRegion1);
                }

                /* Throw out any previous elements that were
                 * enclosed but are not a concurrent region;
                 * let's move them onto the first region: */
                if (!regionFigs.isEmpty()) {
                    for (int i = 0; i < regionFigs.size(); i++) {
                        FigStateVertex curFig = regionFigs.get(i);
                        curFig.setEnclosingFig(firstRegionFig);
                        firstRegionFig.addEnclosedFig(curFig);
                        curFig.redrawEnclosedFigs();
                    }
                }
            }

            final Object umlRegion2 =
                factory.buildCompositeState(umlCompositeState);
            // TODO: What are these magic numbers?
            Rectangle bounds = new Rectangle(
                    f.getX() + FigConcurrentRegion.INSET_HORZ,
                    f.getY() + rFig.height - 1, //linewidth?
                    rFig.width - 2 * FigConcurrentRegion.INSET_HORZ,
                    126);
            DiagramSettings settings = figCompositeState.getSettings();
            final FigConcurrentRegion newRegionFig =
                new FigConcurrentRegion(umlRegion2, bounds, settings);
            /* The divider line should be visible, so no need to change its color. */

            /* Make the composite state 1 region higher: */
            figCompositeState.setCompositeStateHeight(
                    rFig.height + newRegionFig.getInitialHeight());
            newRegionFig.setEnclosingFig(figCompositeState);
            figCompositeState.addEnclosedFig(newRegionFig);
            newRegionFig.setLayer(lay);
            lay.add(newRegionFig);
            editor.getSelectionManager().select(f);
            if (mgm.canAddNode(umlRegion2)) {
                mgm.getNodes().add(umlRegion2);
                mgm.fireNodeAdded(umlRegion2);
            }

            /* TODO: Verify this.
             * IIUC, then this triggers the CompountStateFig
             * to draw itself correctly.
             * Hence, there was a reason to wait this long
             * to make the state concurrent. */
            Model.getStateMachinesHelper().setConcurrent(
                    umlCompositeState, true);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Exception caught", ex);
        }
    }

}
