/* $Id$
 *******************************************************************************
 * Copyright (c) 2010-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *******************************************************************************
 */

package org.argouml.transformer;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.state.ui.FigCompositeState;
import org.argouml.uml.diagram.state.ui.FigSimpleState;
import org.argouml.uml.diagram.state.ui.FigTransition;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

/**
 * This class transforms a SimpleState into a CompositeState. <p>
 *
 * This involves copying the following:
 * from State:
 *      entry : Action
 *      exit : Action
 *      deferrableEvent : Event
 *      internalTransition : Transition
 *      doActivity : Action
 *      stateMachine : StateMachine
 * from StateVertex:
 *      container : CompositeState
 *      outgoing : Transition
 *      incoming : Transition
 * from ModelElement:
 *      name
 *      and maybe others...
 *
 * @author michiel
 */
class SimpleStateTransformer implements Transformer {

    private static final Logger LOG =
        Logger.getLogger(SimpleStateTransformer.class.getName());

    public List<Action> actions(Project p, Object sourceModelElement) {
        assert Model.getFacade().isASimpleState(sourceModelElement);
        List<Action> result = new ArrayList<Action>();
        result.add(new SimpleStateToCompositeState(p, sourceModelElement));
        return result;
    }

    public boolean canTransform(Object sourceModelElement) {
        return Model.getFacade().isASimpleState(sourceModelElement);
    }

    class SimpleStateToCompositeState extends TransformerAction {

        SimpleStateToCompositeState(Project project, Object sourceModelElement) {
            super(Translator.localize("transform.button.new-compositestate"),
                    project, sourceModelElement);
        }

        public void actionPerformed(ActionEvent e) {
            LOG.log(Level.FINE,
                    "Transforming a SimpleState into a CompositeState");
//            Editor editor = Globals.curEditor();
//            GraphModel gm = editor.getGraphModel();
//            LayerDiagram lay =
//                ((LayerDiagram) editor.getLayerManager().getActiveLayer());


            super.actionPerformed(e);
            Object entry = Model.getFacade().getEntry(getSource());
            Object exit = Model.getFacade().getExit(getSource());
            /* Need to copy the lists since we will be moving the elements: */
            Collection deferEvts = new ArrayList(Model.getFacade().getDeferrableEvents(getSource()));
            Collection intTrans = new ArrayList(Model.getFacade().getInternalTransitions(getSource()));
            Object doActivity = Model.getFacade().getDoActivity(getSource());
            Object sm = Model.getFacade().getStateMachine(getSource());
            Object container = Model.getFacade().getContainer(getSource());
            Collection outgoings = new ArrayList(Model.getFacade().getOutgoings(getSource()));
            Collection incomings = new ArrayList(Model.getFacade().getIncomings(getSource()));

            outgoings.removeAll(intTrans);
            incomings.removeAll(intTrans);

            LOG.log(Level.FINE,
                    "Transformer found {0} internal transitions.",
                    intTrans.size());
            LOG.log(Level.FINE,
                    "Transformer found {0} incoming transitions.",
                    incomings.size());
            LOG.log(Level.FINE,
                    "Transformer found {0} outgoing transitions.",
                    outgoings.size());

            String name = Model.getFacade().getName(getSource());
            Object cs = Model.getStateMachinesFactory().buildCompositeState(container);
            Model.getStateMachinesHelper().setEntry(getSource(), null);
            Model.getStateMachinesHelper().setEntry(cs, entry);
            Model.getStateMachinesHelper().setExit(getSource(), null);
            Model.getStateMachinesHelper().setExit(cs, exit);
            for (Object de : deferEvts) {
                Model.getStateMachinesHelper().removeDeferrableEvent(getSource(), de);
                Model.getStateMachinesHelper().addDeferrableEvent(cs, de);
            }
            Model.getStateMachinesHelper().setInternalTransitions(getSource(), Collections.EMPTY_LIST);
            Model.getStateMachinesHelper().setInternalTransitions(cs, intTrans);
            Model.getStateMachinesHelper().setDoActivity(getSource(), null);
            Model.getStateMachinesHelper().setDoActivity(cs, doActivity);
            Model.getStateMachinesHelper().setStateMachine(cs, sm);
            for (Object to : outgoings) {
                Model.getStateMachinesHelper().setSource(to, null);
                Model.getStateMachinesHelper().setSource(to, cs);
            }
            for (Object ti : incomings) {
                Model.getCommonBehaviorHelper().setTarget(ti, null);
                Model.getCommonBehaviorHelper().setTarget(ti, cs);
            }
            Model.getCoreHelper().setName(cs, name);



            // This is not necessarily the current diagram!
            Collection<Fig> figs = getProject().findAllPresentationsFor(getSource());

            LOG.log(Level.FINE,
                    "Transformer found {0} representations (Figs).",
                    figs.size());

            for (Fig ssFig : figs) {

                LOG.log(Level.FINE, "Transformer found a Fig: {0}", ssFig);

                assert ssFig.getOwner() == getSource();
                assert ssFig instanceof FigSimpleState;
                Rectangle bounds = ssFig.getBounds();
                DiagramSettings settings = ((FigNodeModelElement) ssFig).getSettings();
                Layer lay = ssFig.getLayer();
                /* Remove the old fig from the diagram, so we can draw the
                 * new one in its place: */
                ssFig.removeFromDiagram();

                FigCompositeState fcs =
                    new FigCompositeState(cs, bounds, settings);
                lay.add(fcs);
                fcs.setBounds(bounds);
                ((LayerPerspective) lay).putInPosition(fcs);
                fcs.renderingChanged();

                for (Object to : outgoings) {
                    makeTransition(settings, lay, to);
                }
                for (Object ti : incomings) {
                    makeTransition(settings, lay, ti);
                }
            }

            Model.getUmlFactory().delete(getSource());
//            p.moveToTrash(source);
        }

        private void makeTransition(DiagramSettings settings, Layer lay,
                Object transUml) {
            FigTransition transFig = new FigTransition(transUml, settings);
            setPorts(lay, transFig);
            lay.add(transFig);
            transFig.computeRoute();
            transFig.renderingChanged();
        }

        /**
         * TODO: Copied from UmlDiagramRenderer.
         *
         * Find the Figs in the given layer that should be the source and
         * destination and attach these to either end of the FigEdge
         * @param layer the layer to look for the FigNodes
         * @param newEdge The edge to attach
         */
        protected final void setPorts(Layer layer, FigEdge newEdge) {
            Object modelElement = newEdge.getOwner();
            if (newEdge.getSourcePortFig() == null) {
                Object tSource;
                tSource = Model.getUmlHelper().getSource(modelElement);
                FigNode sourceNode = getNodePresentationFor(layer, tSource);
                assert (sourceNode != null) : "No FigNode found for " + tSource;
                setSourcePort(newEdge, sourceNode);
            }
            if (newEdge.getDestPortFig() == null) {
                Object tDest;
                tDest = Model.getUmlHelper().getDestination(newEdge.getOwner());
                setDestPort(newEdge, getNodePresentationFor(layer, tDest));
            }
            if (newEdge.getSourcePortFig() == null
                    || newEdge.getDestPortFig() == null) {
                throw new IllegalStateException("Edge of type "
                    + newEdge.getClass().getName()
                    + " created with no source or destination port");
            }
        }

        private void setSourcePort(FigEdge edge, FigNode source) {
            edge.setSourcePortFig(source);
            edge.setSourceFigNode(source);
        }

        private void setDestPort(FigEdge edge, FigNode dest) {
            edge.setDestPortFig(dest);
            edge.setDestFigNode(dest);
        }

        /**
         * TODO: Copied from UmlDiagramRenderer.
         *
         * Get the FigNode from the given layer that represents the given
         * model element.
         * The FigNode portion of an association class is returned in preference
         * to the FigEdge portion.
         * If no FigNode is found then a FIgEdge is searched for and the FigNode
         * that acts as its edge port is returned.
         * @param lay the layer containing the Fig
         * @param modelElement the model element to find presentation for
         * @return the FigNode presentation of the model element
         */
        private FigNode getNodePresentationFor(Layer lay, Object modelElement) {
            assert modelElement != null : "A modelElement must be supplied";
            for (Object fig : lay.getContentsNoEdges()) {

                if (fig instanceof FigNode
                        && modelElement.equals(((FigNode) fig).getOwner())) {
                    return ((FigNode) fig);
                }
            }
            for (Object fig : lay.getContentsEdgesOnly()) {
                if (fig instanceof FigEdgeModelElement
                        && modelElement.equals(((FigEdgeModelElement) fig)
                                .getOwner())) {
                    return ((FigEdgeModelElement) fig).getEdgePort();
                }
            }
            return null;
        }
    }

}

