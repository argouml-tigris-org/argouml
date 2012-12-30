/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 */

package org.argouml.state2.diagram;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.Model;
import org.argouml.model.StateDiagram;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.DiagramElement;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.state.ui.FigBranchState;
import org.argouml.uml.diagram.state.ui.FigDeepHistoryState;
import org.argouml.uml.diagram.state.ui.FigFinalState;
import org.argouml.uml.diagram.state.ui.FigForkState;
import org.argouml.uml.diagram.state.ui.FigInitialState;
import org.argouml.uml.diagram.state.ui.FigJoinState;
import org.argouml.uml.diagram.state.ui.FigJunctionState;
import org.argouml.uml.diagram.state.ui.FigShallowHistoryState;
import org.argouml.uml.diagram.state.ui.StateDiagramRenderer;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.ui.ActionSetMode;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.RadioAction;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.presentation.FigNode;

/**
 * Diagram for UML2 State Machine diagram.
 * @author Bob Tarling
 */
public class UMLStateDiagram extends UMLDiagram implements StateDiagram {

    /**
     * Construct a State Diagram. Default constructor used by PGML parser during
     * diagram load. It should not be used by other callers.
     * @deprecated only for use by PGML parser
     */
    @Deprecated
    public UMLStateDiagram() {
        super(new StateDiagramGraphModel());
    }
    
    /**
     * Construct a new State Machine diagram.
     * 
     * @param name the name of the new diagram
     * @param machine the owner of the diagram which will be a state machine
     */
    public UMLStateDiagram(String name, Object machine) {
        super(name, machine, new StateDiagramGraphModel());
        if (name == null || name.trim().length() == 0) {
            name = getLabelName() + Model.getFacade().getName(machine);
            try {
                setName(name);
            } catch (PropertyVetoException pve) {
                // nothing we can do about veto, so just ignore it
            }
        }
        initialize(machine);
    }

    @Override
    public void initialize(Object o) {

        setNamespace(o);

        StateDiagramGraphModel gm = createGraphModel();
        gm.setHomeModel(namespace);
        StateDiagramRenderer rend = new StateDiagramRenderer();

        LayerPerspective lay = new LayerPerspectiveMutable(
                Model.getFacade().getName(namespace), gm);
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
        setLayer(lay);

        Model.getPump().addModelEventListener(this, namespace, 
                new String[] {"remove"});
    }

    private StateDiagramGraphModel createGraphModel() {
        if ((getGraphModel() instanceof StateDiagramGraphModel)) {
            return (StateDiagramGraphModel) getGraphModel();
        } else {
            return new StateDiagramGraphModel();
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ((evt.getSource() == namespace)
                && (evt instanceof DeleteInstanceEvent)) {
            Model.getPump().removeModelEventListener(this, 
                    namespace, new String[] {"remove"});
            if (getProject() != null) {
                getProject().moveToTrash(this);
            } else {
                DiagramFactory.getInstance().removeDiagram(this);
            }
        }
    }


    protected Object[] getUmlActions() {
        
        ArrayList actions = new ArrayList();
        
        actions.add(getNodeAction(Model.getMetaTypes().getState()));
        actions.add(getEdgeAction(Model.getMetaTypes().getTransition()));
        actions.add(null);
        actions.add(getPseudoAction(Model.getPseudostateKind().getInitial()));
        actions.add(getNodeAction(Model.getMetaTypes().getFinalState()));
        actions.add(getPseudoAction(
                Model.getPseudostateKind().getEntryPoint()));
        actions.add(getPseudoAction(Model.getPseudostateKind().getExitPoint()));
        actions.add(getPseudoAction(Model.getPseudostateKind().getJunction()));
        actions.add(getPseudoAction(Model.getPseudostateKind().getChoice()));
        actions.add(getPseudoAction(Model.getPseudostateKind().getFork()));
        actions.add(getPseudoAction(Model.getPseudostateKind().getJoin()));
        actions.add(getPseudoAction(
                Model.getPseudostateKind().getShallowHistory()));
        actions.add(getPseudoAction(
                Model.getPseudostateKind().getDeepHistory()));
        
        return actions.toArray();
    }

    public String getLabelName() {
        return Translator.localize("label.statemachine-diagram");
    }

    private Action getNodeAction(Object metaType) {
        String label = getLabel(metaType);
        return new RadioAction(new CmdCreateNode(metaType, label));
    }

    private Action getPseudoAction(Object kind) {
        String label = 
            "button.new-" + Model.getFacade().getName(kind).toLowerCase();
        return new RadioAction(new CreatePseudostateAction(kind, label));
    }
    
    
    private Action getEdgeAction(Object metaType) {
        String label = getLabel(metaType);
        return new RadioAction(
                    new ActionSetMode(
                        ModeCreatePolyEdge.class,
                        "edgeClass",
                        metaType,
                        label));
    }
    
    private String getLabel(Object metaType) {
        return "button.new-"
            + Model.getMetaTypes().getName(metaType).toLowerCase();
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#relocate(java.lang.Object)
     */
    public boolean relocate(Object base) {
        return false;
    }

    public void encloserChanged(FigNode enclosed, 
            FigNode oldEncloser, FigNode newEncloser) {
    }

    @Override
    public boolean doesAccept(Object objectToAccept) {
        if (Model.getFacade().isAVertex(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAComment(objectToAccept)) {
            return true;
        } 
        return false;
    }
    

    public DiagramElement createDiagramElement(
            final Object modelElement,
            final Rectangle bounds) {
        
        FigNodeModelElement figNode = null;
        
        DiagramSettings settings = getDiagramSettings();
        
        if (Model.getFacade().isAFinalState(modelElement)) {
            figNode = new FigFinalState(modelElement, bounds, settings);
        } else if (Model.getFacade().isAState(modelElement)) {
            figNode = new FigVertex(modelElement, bounds, settings);
        } else if (Model.getFacade().isAComment(modelElement)) {
            figNode = new FigComment(modelElement, bounds, settings);
        } else if (Model.getFacade().isAPseudostate(modelElement)) {
            Object kind = Model.getFacade().getKind(modelElement);
            if (kind == null) {
                return null;
            }
            if (kind.equals(Model.getPseudostateKind().getInitial())) {
                figNode = new FigInitialState(modelElement, bounds, settings);
            } else if (kind.equals(
                    Model.getPseudostateKind().getEntryPoint())) {
                figNode = 
                        new FigEntryPoint(modelElement, bounds, settings);
            } else if (kind.equals(
                    Model.getPseudostateKind().getExitPoint())) {
                figNode = new FigExitPoint(modelElement, bounds, settings);
            } else if (kind.equals(
                    Model.getPseudostateKind().getChoice())) {
                figNode = new FigBranchState(modelElement, bounds, settings);
            } else if (kind.equals(
                    Model.getPseudostateKind().getJunction())) {
                figNode = new FigJunctionState(modelElement, bounds, settings);
            } else if (kind.equals(
                    Model.getPseudostateKind().getFork())) {
                figNode = new FigForkState(modelElement, bounds, settings);
            } else if (kind.equals(
                    Model.getPseudostateKind().getJoin())) {
                figNode = new FigJoinState(modelElement, bounds, settings);
            } else if (kind.equals(
                    Model.getPseudostateKind().getShallowHistory())) {
                figNode = new FigShallowHistoryState(modelElement, bounds, 
                        settings);
            } else if (kind.equals(
                    Model.getPseudostateKind().getDeepHistory())) {
                figNode = new FigDeepHistoryState(modelElement, bounds, 
                        settings);
            }
        }
        return figNode;
    }

    @Override
    public boolean isRelocationAllowed(Object base) {
        // TODO Auto-generated method stub
        return false;
    }

    public Collection getRelocationCandidates(Object root) {
        // TODO Auto-generated method stub
        return null;
    }
    
    private class CreatePseudostateAction extends CmdCreateNode {
        public CreatePseudostateAction(Object kind, String name) {
            super(Model.getMetaTypes().getPseudostate(), name);
            setArg("kind", kind);
        }

        public Object makeNode() {
            Object newNode = super.makeNode();
            Object kind = getArg("kind");
            Model.getCoreHelper().setKind(newNode, kind);
            return newNode;
        }
    }
}
