/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Thomas Neustupny
 *    Bob Tarling
 *    Michiel van der Wulp
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

import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.model.ClassDiagram;
import org.argouml.model.CoreFactory;
import org.argouml.model.Model;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.DiagramEdgeSettings;
import org.argouml.uml.diagram.DiagramElement;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.deployment.ui.FigComponent;
import org.argouml.uml.diagram.deployment.ui.FigComponentInstance;
import org.argouml.uml.diagram.deployment.ui.FigMNode;
import org.argouml.uml.diagram.deployment.ui.FigNodeInstance;
import org.argouml.uml.diagram.deployment.ui.FigObject;
import org.argouml.uml.diagram.static_structure.ClassDiagramGraphModel;
import org.argouml.uml.diagram.ui.FigAbstraction;
import org.argouml.uml.diagram.ui.FigAssociation;
import org.argouml.uml.diagram.ui.FigAssociationClass;
import org.argouml.uml.diagram.ui.FigAssociationEnd;
import org.argouml.uml.diagram.ui.FigDependency;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigGeneralization;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.FigPermission;
import org.argouml.uml.diagram.ui.FigUsage;
import org.argouml.uml.diagram.ui.ModeCreateDependency;
import org.argouml.uml.diagram.ui.ModeCreatePermission;
import org.argouml.uml.diagram.ui.ModeCreateUsage;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.diagram.use_case.ui.FigActor;
import org.argouml.uml.diagram.use_case.ui.FigUseCase;
import org.argouml.uml.ui.foundation.core.ActionAddAttribute;
import org.argouml.uml.ui.foundation.core.ActionAddOperation;
import org.argouml.util.ToolBarUtility;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

/**
 * UML Class Diagram.
 *
 * @author jrobbins@ics.uci.edy
 */
public class UMLClassDiagram extends UMLDiagram implements ClassDiagram {

    private static final Logger LOG =
        Logger.getLogger(UMLClassDiagram.class.getName());

    ////////////////
    // actions for toolbar
    private Action actionAssociationClass;
    private Action actionClass;
    private Action actionInterface;
    private Action actionInstanceSpecification;
    private Action actionDependency;
    private Action actionPermission;
    private Action actionUsage;
    private Action actionLink;
    private Action actionGeneralization;
    private Action actionRealization;
    private Action actionPackage;
    private Action actionModel;
    private Action actionSubsystem;
    private Action actionAssociation;
    private Action actionAssociationEnd;
    private Action actionAggregation;
    private Action actionComposition;
    private Action actionUniAssociation;
    private Action actionUniAggregation;
    private Action actionUniComposition;
    private Action actionDataType;
    private Action actionEnumeration;
    private Action actionStereotype;
    private Action actionSignal;
    private Action actionException;

    /**
     * Construct a Class Diagram. Default constructor used by PGML parser during
     * diagram load. It should not be used by other callers.
     * @deprecated only for use by PGML parser
     */
    @Deprecated
    public UMLClassDiagram() {
        super(new ClassDiagramGraphModel());
    }


    /**
     * Construct a new class diagram with the given name, owned by the given
     * namespace.
     *
     * @param name the name for the new diagram
     * @param namespace the namespace for the new diagram
     */
    public UMLClassDiagram(String name, Object namespace) {
        super(name, namespace, new ClassDiagramGraphModel());
    }

    /**
     * Construct a Class Diagram owned by the given namespace. A default unique
     * diagram name is constructed.
     *
     * @param m the namespace
     */
    public UMLClassDiagram(Object m) {
        // We're going to change the name immediately, so just use ""
        super("", m, new ClassDiagramGraphModel());
        String name = getNewDiagramName();
        try {
            setName(name);
        } catch (PropertyVetoException pve) {
            LOG.log(Level.WARNING,
                    "Generated diagram name '" + name
                    + "' was vetoed by setName");
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#setNamespace(java.lang.Object)
     */
    public void setNamespace(Object ns) {
        if (!Model.getFacade().isANamespace(ns)) {
            LOG.log(Level.SEVERE,
                    "Illegal argument. "
                    + "Object " + ns + " is not a namespace");
            throw new IllegalArgumentException("Illegal argument. "
            			       + "Object " + ns
            			       + " is not a namespace");
        }
        boolean init = (null == getNamespace());
        super.setNamespace(ns);
        ClassDiagramGraphModel gm = (ClassDiagramGraphModel) getGraphModel();
        gm.setHomeModel(ns);
        if (init) {
            LayerPerspective lay =
                new LayerPerspectiveMutable(Model.getFacade().getName(ns), gm);
            ClassDiagramRenderer rend = new ClassDiagramRenderer(); // singleton
            lay.setGraphNodeRenderer(rend);
            lay.setGraphEdgeRenderer(rend);
            setLayer(lay);
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getUmlActions()
     */
    protected Object[] getUmlActions() {
        if (Model.getFacade().getUmlVersion().startsWith("1")) {
            Object[] actions = {
                getPackageActions(),
                getActionClass(),
                null,
                getAssociationActions(),
                getAggregationActions(),
                getCompositionActions(),
                getActionAssociationEnd(),
                getActionGeneralization(),
                null,
                getActionInterface(),
                getActionRealization(),
                null,
                getDependencyActions(),
                null,
                ActionAddAttribute.getTargetFollower(),
                ActionAddOperation.getTargetFollower(),
                getActionAssociationClass(),
                null,
                getDataTypeActions(),
            };

            return actions;
        } else {
            Object[] actions = {
                getPackageActions(),
                getActionClass(),
                null,
                getAssociationActions(),
                getAggregationActions(),
                getCompositionActions(),
                getActionAssociationEnd(),
                getActionGeneralization(),
                null,
                getActionInterface(),
                getActionRealization(),
                null,
                getDependencyActions(),
                null,
                ActionAddAttribute.getTargetFollower(),
                ActionAddOperation.getTargetFollower(),
                getActionAssociationClass(),
                getActionInstanceSpecification(),
                null,
                getDataTypeActions(),
            };

            return actions;
        }
    }

    /**
     * Return the actions for the miscellaneous pulldown menu.
     */
    private Object[] getDataTypeActions() {
        Object[] actions = {
            getActionDataType(),
            getActionEnumeration(),
            getActionStereotype(),
            getActionSignal(),
            getActionException(),
        };
        ToolBarUtility.manageDefault(actions, "diagram.class.datatype");
        return actions;
    }

    private Object getPackageActions() {
        // TODO: To enable models and subsystems, change this flag
        // Work started by Markus I believe where does this stand? - Bob.

        // Status as of Nov. 2008 - Figs created, property panels exist, more
        // work required on explorer and assumptions about models not being
        // nested - tfm
        if (false) {
            Object[] actions = {
                    getActionPackage(),
                    getActionModel(),
                    getActionSubsystem(),
            };
            ToolBarUtility.manageDefault(actions, "diagram.class.package");
            return actions;
        } else {
            return getActionPackage();
        }
    }

    /**
     * Return an array of dependency actions in the
     * pattern of which to build a popup toolbutton.
     */
    private Object[] getDependencyActions() {
        Object[] actions = {
            getActionDependency(),
            getActionPermission(),
            getActionUsage(),
        };
        ToolBarUtility.manageDefault(actions, "diagram.class.dependency");
        return actions;
    }

    /**
     * Return an array of association actions in the
     * pattern of which to build a popup toolbutton.
     */
    private Object[] getAssociationActions() {
        // This calls the getters to fetch actions even though the
        // action variables are defined is instances of this class.
        // This is because any number of action getters could have
        // been overridden in a descendant and it is the action from
        // that overridden method that should be returned in the array.
        Object[] actions = {
            getActionAssociation(),
            getActionUniAssociation(),
        };
        ToolBarUtility.manageDefault(actions, "diagram.class.association");
        return actions;
    }

    private Object[] getAggregationActions() {
        Object[] actions = {
            getActionAggregation(),
            getActionUniAggregation(),
        };
        ToolBarUtility.manageDefault(actions, "diagram.class.aggregation");
        return actions;
    }

    private Object[] getCompositionActions() {
        Object[] actions = {
            getActionComposition(),
            getActionUniComposition(),
        };
        ToolBarUtility.manageDefault(actions, "diagram.class.composition");
        return actions;
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getLabelName()
     */
    public String getLabelName() {
        return Translator.localize("label.class-diagram");
    }

    /**
     * @return Returns the actionAggregation.
     */
    protected Action getActionAggregation() {
        if (actionAggregation == null) {
            actionAggregation =
                makeCreateAssociationAction(
                        Model.getAggregationKind().getAggregate(),
                        false,
                        "button.new-aggregation");
        }
        return actionAggregation;
    }
    /**
     * @return Returns the actionAssociation.
     */
    protected Action getActionAssociation() {
        if (actionAssociation == null) {
            actionAssociation =
                makeCreateAssociationAction(
                        Model.getAggregationKind().getNone(),
                        false, "button.new-association");
        }
        return actionAssociation;
    }
    /**
     * @return Returns the actionAssociation.
     */
    protected Action getActionAssociationEnd() {
        if (actionAssociationEnd == null) {
            actionAssociationEnd =
                makeCreateAssociationEndAction("button.new-association-end");
        }
        return actionAssociationEnd;
    }

    /**
     * @return Returns the actionClass.
     */
    protected Action getActionClass() {
        if (actionClass == null) {
            actionClass =
                makeCreateNodeAction(Model.getMetaTypes().getUMLClass(),
                        	     "button.new-class");
        }

        return actionClass;
    }

    /**
    * @return Returns the actionAssociationClass.
    */
    protected Action getActionAssociationClass() {
        if (actionAssociationClass == null) {
            actionAssociationClass =
                makeCreateAssociationClassAction(
                        "button.new-associationclass");
        }
        return actionAssociationClass;
    }
    /**
     * @return Returns the actionComposition.
     */
    protected Action getActionComposition() {
        if (actionComposition == null) {
            actionComposition =
                makeCreateAssociationAction(
                        Model.getAggregationKind().getComposite(),
                        false, "button.new-composition");
        }
        return actionComposition;
    }

    /**
     * @return Returns the actionDepend.
     */
    protected Action getActionDependency() {
        if (actionDependency == null) {
            actionDependency = makeCreateDependencyAction(
        	    ModeCreateDependency.class,
                        Model.getMetaTypes().getDependency(),
                        "button.new-dependency");
        }
        return actionDependency;
    }

    /**
     * @return Returns the actionGeneralize.
     */
    protected Action getActionGeneralization() {
        if (actionGeneralization == null) {
            actionGeneralization = makeCreateGeneralizationAction();
        }

        return actionGeneralization;
    }

    /**
     * @return Returns the actionInterface.
     */
    protected Action getActionInterface() {
        if (actionInterface == null) {
            actionInterface =
                makeCreateNodeAction(
                        Model.getMetaTypes().getInterface(),
                        "button.new-interface");
        }
        return actionInterface;
    }

    /**
     * @return Returns the actionInterface.
     */
    protected Action getActionInstanceSpecification() {
        if (actionInstanceSpecification == null) {
            actionInstanceSpecification =
                makeCreateNodeAction(
                        Model.getMetaTypes().getInstanceSpecification(),
                        "button.new-instance-specification");
        }
        return actionInstanceSpecification;
    }

    /**
     * @return Returns the actionLink.
     */
    protected Action getActionLink() {
        if (actionLink == null) {
            actionLink =
                makeCreateEdgeAction(Model.getMetaTypes().getLink(), "Link");
        }

        return actionLink;
    }
    /**
     * @return Returns the actionModel.
     */
    protected Action getActionModel() {
        if (actionModel == null) {
            actionModel =
                makeCreateNodeAction(Model.getMetaTypes().getModel(), "Model");
        }

        return actionModel;
    }

    /**
     * @return Returns the actionPackage.
     */
    protected Action getActionPackage() {
        if (actionPackage == null) {
            actionPackage =
                makeCreateNodeAction(Model.getMetaTypes().getPackage(),
                        	     "button.new-package");
        }

        return actionPackage;
    }
    /**
     * @return Returns the actionPermission.
     */
    protected Action getActionPermission() {
        if (actionPermission == null) {
            actionPermission = makeCreateDependencyAction(
        	    ModeCreatePermission.class,
                        Model.getMetaTypes().getPackageImport(),
                        "button.new-permission");
        }

        return actionPermission;
    }

    /**
     * @return Returns the actionRealize.
     */
    protected Action getActionRealization() {
        if (actionRealization == null) {
            actionRealization =
                makeCreateEdgeAction(
                        Model.getMetaTypes().getAbstraction(),
                        "button.new-realization");
        }

        return actionRealization;
    }

    /**
     * @return Returns the actionSubsystem.
     */
    protected Action getActionSubsystem() {
        if (actionSubsystem == null) {
            actionSubsystem =
                makeCreateNodeAction(
                        Model.getMetaTypes().getSubsystem(),
                        "Subsystem");
        }
        return actionSubsystem;
    }

    /**
     * @return Returns the actionUniAggregation.
     */
    protected Action getActionUniAggregation() {
        if (actionUniAggregation == null) {
            actionUniAggregation =
                makeCreateAssociationAction(
                        Model.getAggregationKind().getAggregate(),
                        true,
                        "button.new-uniaggregation");
        }
        return actionUniAggregation;
    }

    /**
     * @return Returns the actionUniAssociation.
     */
    protected Action getActionUniAssociation() {
        if (actionUniAssociation == null) {
            actionUniAssociation =
                makeCreateAssociationAction(
                        Model.getAggregationKind().getNone(),
                        true,
                        "button.new-uniassociation");
        }
        return actionUniAssociation;
    }

    /**
     * @return Returns the actionUniComposition.
     */
    protected Action getActionUniComposition() {
        if (actionUniComposition == null) {
            actionUniComposition =
                makeCreateAssociationAction(
                        Model.getAggregationKind().getComposite(),
                        true,
                        "button.new-unicomposition");
        }
        return actionUniComposition;
    }

    /**
     * @return Returns the actionUsage.
     */
    protected Action getActionUsage() {
        if (actionUsage == null) {
            actionUsage = makeCreateDependencyAction(
        	    ModeCreateUsage.class,
        	    Model.getMetaTypes().getUsage(),
                        "button.new-usage");
        }
        return actionUsage;
    }

    /**
     * @return Returns the actionDataType.
     */
    private Action getActionDataType() {
        if (actionDataType == null) {
            actionDataType =
                makeCreateNodeAction(Model.getMetaTypes().getDataType(),
                    "button.new-datatype");
        }
        return actionDataType;
    }

    /**
     * @return Returns the actionEnumeration.
     */
    private Action getActionEnumeration() {
        if (actionEnumeration == null) {
            actionEnumeration =
                makeCreateNodeAction(Model.getMetaTypes().getEnumeration(),
                    "button.new-enumeration");
        }
        return actionEnumeration;
    }

    /**
     * @return Returns the actionStereotype.
     */
    private Action getActionStereotype() {
        if (actionStereotype == null) {
            actionStereotype =
                makeCreateNodeAction(Model.getMetaTypes().getStereotype(),
                    "button.new-stereotype");
        }
        return actionStereotype;
    }

    /**
     * @return Returns the actionStereotype.
     */
    private Action getActionSignal() {
        if (actionSignal == null) {
            actionSignal =
                makeCreateNodeAction(Model.getMetaTypes().getSignal(),
                    "button.new-signal");
        }
        return actionSignal;
    }

    /**
     * @return Returns the actionStereotype.
     */
    private Action getActionException() {
        if (actionException == null) {
            actionException =
                makeCreateNodeAction(Model.getMetaTypes().getException(),
                    "button.new-exception");
        }
        return actionException;
    }
    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#isRelocationAllowed(java.lang.Object)
     */
    public boolean isRelocationAllowed(Object base)  {
    	return Model.getFacade().isANamespace(base);
    }

    public Collection getRelocationCandidates(Object root) {
        return
        Model.getModelManagementHelper().getAllModelElementsOfKindWithModel(
            root, Model.getMetaTypes().getNamespace());
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#relocate(java.lang.Object)
     */
    public boolean relocate(Object base) {
        setNamespace(base);
        damage();
        return true;
    }

    public void encloserChanged(FigNode enclosed, FigNode oldEncloser,
            FigNode newEncloser) {
        // Do nothing.
    }

    @Override
    public boolean doesAccept(Object objectToAccept) {
        if (objectToAccept instanceof CommentEdge) {
            return true;
        } else if (Model.getFacade().isAClass(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAInterface(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAModel(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isASubsystem(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAPackage(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAComment(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAAssociation(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAEnumeration(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isADataType(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAStereotype(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAException(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isASignal(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAActor(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAUseCase(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAObject(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAInstanceSpecification(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isANodeInstance(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAComponentInstance(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isANode(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAComponent(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAAssociationEnd(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isADependency(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAGeneralization(objectToAccept)) {
            return true;
        }
        return false;

    }

    public DiagramElement createDiagramElement(
            final Object modelElement,
            final Rectangle bounds) {

        FigNodeModelElement figNode = null;
        FigEdge figEdge = null;

        DiagramSettings settings = getDiagramSettings();

        if (Model.getFacade().isAAssociationClass(modelElement)) {
            Object[] associationEnds =
                Model.getFacade().getConnections(modelElement).toArray();
            figEdge = new FigAssociationClass(
                    new DiagramEdgeSettings(
                            modelElement,
                            associationEnds[0],
                            associationEnds[1]),
                            settings);
            FigNode sourceFig =
                getFigNodeForAssociationEnd(associationEnds[0]);
            FigNode destFig =
                getFigNodeForAssociationEnd(associationEnds[1]);
            figEdge.setSourceFigNode(sourceFig);
            figEdge.setSourcePortFig(sourceFig);
            figEdge.setDestFigNode(destFig);
            figEdge.setDestPortFig(destFig);
        } else if (Model.getFacade().isAAssociationEnd(modelElement)) {
            figEdge = new FigAssociationEnd(modelElement, settings);
            Model.getFacade().getAssociation(modelElement);
            FigNode associationFN =
                (FigNode) getLayer().presentationFor(
                        Model.getFacade().getAssociation(modelElement));
            FigNode classifierFN =
                (FigNode) getLayer().presentationFor(Model.getFacade().getType(modelElement));

            figEdge.setSourcePortFig(associationFN);
            figEdge.setSourceFigNode(associationFN);
            figEdge.setDestPortFig(classifierFN);
            figEdge.setDestFigNode(classifierFN);
        } else if (Model.getFacade().isAAssociation(modelElement)
                && !Model.getFacade().isANaryAssociation(modelElement)) {
            Object[] associationEnds =
                Model.getFacade().getConnections(modelElement).toArray();
            figEdge = new FigAssociation(
                    new DiagramEdgeSettings(
                            modelElement,
                            associationEnds[0],
                            associationEnds[1]),
                            settings);
            FigNode sourceFig =
                getFigNodeForAssociationEnd(associationEnds[0]);
            FigNode destFig =
                getFigNodeForAssociationEnd(associationEnds[1]);
            figEdge.setSourceFigNode(sourceFig);
            figEdge.setSourcePortFig(sourceFig);
            figEdge.setDestFigNode(destFig);
            figEdge.setDestPortFig(destFig);
        } else if (Model.getFacade().isALink(modelElement)) {
            figEdge = new FigLink(modelElement, settings);
            Collection linkEndsColn = Model.getFacade().getConnections(modelElement);

            Object[] linkEnds = linkEndsColn.toArray();
            Object fromInst = Model.getFacade().getInstance(linkEnds[0]);
            Object toInst = Model.getFacade().getInstance(linkEnds[1]);

            FigNode fromFN = (FigNode) getLayer().presentationFor(fromInst);
            FigNode toFN = (FigNode) getLayer().presentationFor(toInst);
            figEdge.setSourcePortFig(fromFN);
            figEdge.setSourceFigNode(fromFN);
            figEdge.setDestPortFig(toFN);
            figEdge.setDestFigNode(toFN);
            figEdge.getFig().setLayer(getLayer());
        } else if (Model.getFacade().isAGeneralization(modelElement)) {
            figEdge = new FigGeneralization(modelElement, settings);

            Object supplier =
                (Model.getFacade().getSpecific(modelElement));
            Object client =
                (Model.getFacade().getGeneral(modelElement));

            FigNode supFN = (FigNode) getLayer().presentationFor(supplier);
            FigNode cliFN = (FigNode) getLayer().presentationFor(client);

            figEdge.setSourceFigNode(supFN);
            figEdge.setSourcePortFig(supFN);
            figEdge.setDestFigNode(cliFN);
            figEdge.setDestPortFig(cliFN);
        } else if (Model.getFacade().isADependency(modelElement)) {

            if (Model.getFacade().isAPackageImport(modelElement)) {
                figEdge = new FigPermission(modelElement, settings);
            } else if (Model.getFacade().isAUsage(modelElement)) {
                figEdge = new FigUsage(modelElement, settings);
            } else if (Model.getFacade().isAAbstraction(modelElement)) {
                figEdge = new FigAbstraction(modelElement, settings);
            } else {

                String name = "";
                for (Object stereotype : Model.getFacade().getStereotypes(modelElement)) {
                    name = Model.getFacade().getName(stereotype);
                    if (CoreFactory.REALIZE_STEREOTYPE.equals(name)) {
                        break;
                    }
                }
                if (CoreFactory.REALIZE_STEREOTYPE.equals(name)) {
                    // TODO: This code doesn't look like it will get reached because
                    // any abstraction/realization is going to take the
                    // isAAbstraction leg of the if before it gets to this more
                    // general case. - tfm 20080508
                    figEdge = new FigAbstraction(modelElement, settings);
                } else {
                    figEdge = new FigDependency(modelElement, settings);
                }
            }
            Object supplier =
                ((Model.getFacade().getSuppliers(modelElement).toArray())[0]);
            Object client =
                ((Model.getFacade().getClients(modelElement).toArray())[0]);
            FigNode supFN = (FigNode) getLayer().presentationFor(supplier);
            FigNode cliFN = (FigNode) getLayer().presentationFor(client);

            figEdge.setSourcePortFig(cliFN);
            figEdge.setSourceFigNode(cliFN);
            figEdge.setDestPortFig(supFN);
            figEdge.setDestFigNode(supFN);
            figEdge.getFig().setLayer(getLayer());
        } else if (modelElement instanceof CommentEdge) {
            CommentEdge ce = (CommentEdge) modelElement;
            Fig source = getLayer().presentationFor(ce.getSource());
            Fig dest = getLayer().presentationFor(ce.getDestination());
            if (source instanceof FigAssociationClass) {
                source = ((FigAssociationClass) source).getAssociationClass();
            }
            if (dest instanceof FigAssociationClass) {
                dest = ((FigAssociationClass) dest).getAssociationClass();
            }
            FigNode sourceFN = (FigNode) source;
            FigNode destFN = (FigNode) dest;
            figEdge = new FigEdgeNote(modelElement, settings);
            figEdge.setSourcePortFig(sourceFN);
            figEdge.setSourceFigNode(sourceFN);
            figEdge.setDestPortFig(destFN);
            figEdge.setDestFigNode(destFN);
            figEdge.getFig().setLayer(getLayer());
        }

        if (figEdge != null) {
            figEdge.computeRoute();
            return (DiagramElement) figEdge;
        }

        if (Model.getFacade().isAAssociation(modelElement)) {
            figNode =
                createNaryAssociationNode(modelElement, bounds, settings);
        } else if (Model.getFacade().isAStereotype(modelElement)) {
            // since UML2, this must appear before the isAClass clause
            figNode = new FigStereotypeDeclaration(modelElement, bounds,
                    settings);
        } else if (Model.getFacade().isAClass(modelElement)) {
            figNode = new FigClass(modelElement, bounds, settings);
        } else if (Model.getFacade().isAInterface(modelElement)) {
            figNode = new FigInterface(modelElement, bounds, settings);
        } else if (Model.getFacade().isAModel(modelElement)) {
            figNode = new FigModel(modelElement, bounds, settings);
        } else if (Model.getFacade().isASubsystem(modelElement)) {
            figNode = new FigSubsystem(modelElement, bounds, settings);
        } else if (Model.getFacade().isAPackage(modelElement)) {
            figNode = new FigPackage(modelElement, bounds, settings);
        } else if (Model.getFacade().isAComment(modelElement)) {
            figNode = new FigComment(modelElement, bounds, settings);
        } else if (Model.getFacade().isAEnumeration(modelElement)) {
            figNode = new FigEnumeration(modelElement, bounds, settings);
        } else if (Model.getFacade().isADataType(modelElement)) {
            figNode = new FigDataType(modelElement, bounds, settings);
        } else if (Model.getFacade().isAException(modelElement)) {
            figNode = new FigException(modelElement, bounds, settings);
        } else if (Model.getFacade().isASignal(modelElement)) {
            figNode = new FigSignal(modelElement, bounds, settings);
        } else if (Model.getFacade().isAActor(modelElement)) {
            figNode = new FigActor(modelElement, bounds, settings);
        } else if (Model.getFacade().isAUseCase(modelElement)) {
            figNode = new FigUseCase(modelElement, bounds, settings);
        } else if (Model.getFacade().isAObject(modelElement)) {
            figNode = new FigObject(modelElement, bounds, settings);
        } else if (Model.getFacade().isAInstanceSpecification(modelElement)) {
            figNode = new FigObject(modelElement, bounds, settings);
        } else if (Model.getFacade().isANodeInstance(modelElement)) {
            figNode = new FigNodeInstance(modelElement, bounds, settings);
        } else if (Model.getFacade().isAComponentInstance(modelElement)) {
            figNode = new FigComponentInstance(modelElement, bounds, settings);
        } else if (Model.getFacade().isANode(modelElement)) {
            figNode = new FigMNode(modelElement, bounds, settings);
        } else if (Model.getFacade().isAComponent(modelElement)) {
            figNode = new FigComponent(modelElement, bounds, settings);
        }
        if (figNode != null) {
            LOG.log(Level.FINE,
                    "Model element {0} converted to {1}",
                    new Object[]{modelElement, figNode});
        } else {
            LOG.log(Level.FINE, "Dropped object NOT added {0}", figNode);
        }
        return figNode;
    }

    protected FigNode getFigNodeForAssociationEnd(
            final Object associationEnd) {
        Object classifier =
            Model.getFacade().getClassifier(associationEnd);
        return getNodePresentationFor(getLayer(), classifier);
    }

    /**
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
