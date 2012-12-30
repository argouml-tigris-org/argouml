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

package org.argouml.deployment2.diagram;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.DeploymentDiagram;
import org.argouml.model.Model;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.DiagramElement;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.deployment.ui.DeploymentDiagramRenderer;
import org.argouml.uml.diagram.deployment.ui.FigComponent;
import org.argouml.uml.diagram.deployment.ui.FigObject;
import org.argouml.uml.diagram.static_structure.ui.FigClass;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.static_structure.ui.FigInterface;
import org.argouml.uml.diagram.ui.ActionSetAddAssociationMode;
import org.argouml.uml.diagram.ui.ActionSetMode;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.RadioAction;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.diagram.use_case.ui.FigActor;
import org.argouml.util.ToolBarUtility;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;

/**
 * Diagram for UML2 Deployment Diagram
 * @author Bob Tarling
 * @author Thomas Neustupny
 */
public class UMLDeploymentDiagram extends UMLDiagram implements DeploymentDiagram {

    private static final List acceptList = Arrays.asList(new Object[] {
            Model.getMetaTypes().getActor(),
            Model.getMetaTypes().getAssociation(),
            Model.getMetaTypes().getUMLClass(),
            Model.getMetaTypes().getComment(),
            Model.getMetaTypes().getComponent(),
            Model.getMetaTypes().getInterface(),
            Model.getMetaTypes().getNode(),
            Model.getMetaTypes().getObject(),
            Model.getMetaTypes().getPort()
    });

    /**
     * Construct a Deployment Diagram. Default constructor used by PGML parser during
     * diagram load. It should not be used by other callers.
     * @deprecated only for use by PGML parser
     */
    @Deprecated
    public UMLDeploymentDiagram() {
        super(new DeploymentDiagramGraphModel());
    }
    
    /**
     * Construct a new Deployment Diagram.
     * 
     * @param name the name of the new diagram
     * @param namespace the owner of the new diagram
     */
    public UMLDeploymentDiagram(String name, Object namespace) {
        super(name, namespace, new DeploymentDiagramGraphModel());
        if (name == null || name.trim().length() == 0) {
            name = getLabelName() + Model.getFacade().getName(namespace);
            try {
                setName(name);
            } catch (PropertyVetoException pve) {
                // nothing we can do about veto, so just ignore it
            }
        }
        initialize(namespace);
    }

    @Override
    public void initialize(Object o) {

        setNamespace(o);

        DeploymentDiagramGraphModel gm = createGraphModel();
        gm.setHomeModel(namespace);
        DeploymentDiagramRenderer rend = new DeploymentDiagramRenderer();

        LayerPerspective lay = new LayerPerspectiveMutable(
                Model.getFacade().getName(namespace), gm);
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
        setLayer(lay);

        Model.getPump().addModelEventListener(this, namespace, 
                new String[] {"remove"});
    }

    private DeploymentDiagramGraphModel createGraphModel() {
        if ((getGraphModel() instanceof DeploymentDiagramGraphModel)) {
            return (DeploymentDiagramGraphModel) getGraphModel();
        } else {
            return new DeploymentDiagramGraphModel();
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
        
        actions.add(getNodeAction(Model.getMetaTypes().getNode()));
        actions.add(getNodeAction(Model.getMetaTypes().getPort()));
        actions.add(getNodeAction(Model.getMetaTypes().getComponent()));
        actions.add(getEdgeAction(Model.getMetaTypes().getGeneralization()));
        actions.add(getEdgeAction(Model.getMetaTypes().getAbstraction()));
        actions.add(getEdgeAction(Model.getMetaTypes().getDependency()));
        actions.add(getAssociationActions());
        actions.add(getNodeAction(Model.getMetaTypes().getObject()));
        actions.add(getEdgeAction(Model.getMetaTypes().getLink()));
        
        return actions.toArray();
    }
    
    private Object[] getAssociationActions() {
        Object[] actions = {
            getAssociationActions(Model.getAggregationKind().getNone()),
            getAssociationActions(Model.getAggregationKind().getAggregate()),
            getAssociationActions(Model.getAggregationKind().getComposite()),
        };
        ToolBarUtility.manageDefault(actions, "diagram.deployment.association");
        return actions;
    }

    public String getLabelName() {
        return Translator.localize("label.deployment-diagram");
    }

    private Action getNodeAction(Object metaType) {
        String label = getLabel(metaType);
        return new RadioAction(new CmdCreateNode(metaType, label));
    }

    protected Action[] getAssociationActions(
            final Object aggregation) {
        return new Action[] {
                getAssociationAction(aggregation, false),
                getAssociationAction(aggregation, true)                
        };
    }
    
    protected Action getAssociationAction(
            final Object aggregation,
            final boolean unidirectional) {
        String label;
        if (Model.getAggregationKind().getComposite() == aggregation) {
            label = "composition";
        } else if (Model.getAggregationKind().getAggregate() == aggregation) {
            label = "aggregation";
        } else {
            label = "association";
        }
        if (unidirectional) {
            label = "uni" + label;
        }
        label = "button.new-" + label;
        return 
            new RadioAction(
                new ActionSetAddAssociationMode(
                    Model.getAggregationKind().getNone(),
                    unidirectional,
                    label));
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

    public void encloserChanged(
            org.tigris.gef.presentation.FigNode enclosed, 
            org.tigris.gef.presentation.FigNode oldEncloser,
            org.tigris.gef.presentation.FigNode newEncloser) {
    }

    @Override
    public boolean doesAccept(Object objectToAccept) {
        for (Object element : acceptList) {
            if (Model.getFacade().isA(
                    Model.getMetaTypes().getName(objectToAccept), element)) {
                return true;
            }
        }
        return false;
    }
    

    public DiagramElement createDiagramElement(
            final Object modelElement,
            final Rectangle bounds) {
        
        FigNodeModelElement figNode = null;
        
        DiagramSettings settings = getDiagramSettings();
        
        if (Model.getFacade().isANode(modelElement)) {
            figNode = new FigNode(modelElement, bounds, settings);
        } else if (Model.getFacade().isAPort(modelElement)) {
            figNode = new FigPort(modelElement, bounds, settings);
        } else if (Model.getFacade().isAAssociation(modelElement)) {
            figNode =
                createNaryAssociationNode(modelElement, bounds, settings);
        } else if (Model.getFacade().isAComponent(modelElement)) {
            figNode = new FigComponent(modelElement, bounds, settings);
        } else if (Model.getFacade().isAClass(modelElement)) {
            figNode = new FigClass(modelElement, bounds, settings);
        } else if (Model.getFacade().isAInterface(modelElement)) {
            figNode = new FigInterface(modelElement, bounds, settings);
        } else if (Model.getFacade().isAObject(modelElement)) {
            figNode = new FigObject(modelElement, bounds, settings);
        } else if (Model.getFacade().isAActor(modelElement)) {
            figNode = new FigActor(modelElement, bounds, settings);
        } else if (Model.getFacade().isAComment(modelElement)) {
            figNode = new FigComment(modelElement, bounds, settings);
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
}
