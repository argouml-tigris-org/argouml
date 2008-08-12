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


package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.Action;

import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.model.StateMachinesFactory;
import org.argouml.ui.targetmanager.TargetManager;
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
import org.tigris.gef.undo.UndoableAction;

//TODO: There is a cyclic dependency between this class and FigConcurrentRegion

/**
 * Add a concurrent region to a concurrent composite state
 *
 * @author pepargouml@yahoo.es
 */
public class ActionAddConcurrentRegion extends UndoableAction {

    ////////////////////////////////////////////////////////////////
    // static variables

    /** logger */
    private static final Logger LOG =
        Logger.getLogger(ActionAddConcurrentRegion.class);

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
        if (Model.getStateMachinesHelper().isTopState(target)) return false;
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
            final Object compositeState = figCompositeState.getOwner();
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
            
            if (!Model.getFacade().isConcurrent(compositeState)) {

                final Object region1 =
                    factory.buildCompositeState(compositeState);
                final FigConcurrentRegion region =
                    new FigConcurrentRegion(gm, region1,
                                            Color.white,
                                            rFig.width - 6,
                                            rFig.height - rName.height - 10);

                region.setLocation(f.getX() + 3, f.getY() + rName.height + 5);
                region.setEnclosingFig(figCompositeState);
                region.setLayer(lay);
                lay.add(region);

                if (mgm.canAddNode(region1)) {
                    mgm.getNodes().add(region1);
                    mgm.fireNodeAdded(region1);

                }

                if (!regionFigs.isEmpty()) {
                    for (int i = 0; i < regionFigs.size(); i++) {
                        FigStateVertex curFig = regionFigs.get(i);
                        curFig.setEnclosingFig(region);
                        region.addEnclosedFig(curFig);
                        curFig.redrawEnclosedFigs();
                    }
                }
            }

            final Object region2 = factory.buildCompositeState(compositeState);
            final FigConcurrentRegion regionNew =
                new FigConcurrentRegion(gm, region2, Color.black,
                        rFig.width - 6, 126);

            regionNew.setLocation(f.getX() + 3, f.getY() + rFig.height - 1);

            figCompositeState.setBounds(rFig.height + 130);
            regionNew.setEnclosingFig(figCompositeState);
            figCompositeState.addEnclosedFig(regionNew);
            regionNew.setLayer(lay);
            lay.add(regionNew);
            editor.getSelectionManager().select(f);
            if (mgm.canAddNode(region2)) {
                mgm.getNodes().add(region2);
                mgm.fireNodeAdded(region2);
            }

            Model.getStateMachinesHelper().setConcurrent(compositeState, true);

        } catch (Exception ex) {
            LOG.error("Exception caught", ex);
        }
    }

}
