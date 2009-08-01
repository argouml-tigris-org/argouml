// $Id: UMLClassDiagram.java 16702 2009-01-25 20:23:28Z bobtarling $
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

package org.argouml.class2.diagram;

import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.Collection;

import javax.swing.Action;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.deployment.ui.FigComponent;
import org.argouml.uml.diagram.deployment.ui.FigComponentInstance;
import org.argouml.uml.diagram.deployment.ui.FigMNode;
import org.argouml.uml.diagram.deployment.ui.FigNodeInstance;
import org.argouml.uml.diagram.deployment.ui.FigObject;
import org.argouml.uml.diagram.static_structure.ClassDiagramGraphModel;
import org.argouml.uml.diagram.ui.ModeCreateDependency;
import org.argouml.uml.diagram.ui.ModeCreatePermission;
import org.argouml.uml.diagram.ui.ModeCreateUsage;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.diagram.use_case.ui.FigActor;
import org.argouml.uml.diagram.use_case.ui.FigUseCase;
import org.argouml.uml.ui.foundation.core.ActionAddAttribute;
import org.argouml.uml.ui.foundation.core.ActionAddOperation;
import org.argouml.util.ToolBarUtility;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.presentation.FigNode;

/**
 * UML Class Diagram.
 * 
 * @author jrobbins@ics.uci.edy
 */
public class UMLClassDiagram2 extends UMLDiagram {

    private static final long serialVersionUID = -9192325790126361563L;

    private static final Logger LOG = Logger.getLogger(UMLClassDiagram2.class);

    ////////////////
    // actions for toolbar
    private Action actionAssociationClass;
    private Action actionClass;
    private Action actionInterface;
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
    public UMLClassDiagram2() {
        super(new ClassDiagramGraphModel());
    }


    /**
     * Construct a new class diagram with the given name, owned by the given
     * namespace.
     *
     * @param name the name for the new diagram
     * @param namespace the namespace for the new diagram
     */
    public UMLClassDiagram2(String name, Object namespace) {
        super(name, namespace, new ClassDiagramGraphModel());
    }

    /**
     * Construct a Class Diagram owned by the given namespace. A default unique
     * diagram name is constructed.
     * 
     * @param m the namespace
     */
    public UMLClassDiagram2(Object m) {
        // We're going to change the name immediately, so just use ""
        super("", m, new ClassDiagramGraphModel());
        String name = getNewDiagramName();
        try {
            setName(name);
        } catch (PropertyVetoException pve) {
            LOG.warn("Generated diagram name '" + name 
                    + "' was vetoed by setName");
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#setNamespace(java.lang.Object)
     */
    public void setNamespace(Object ns) {
        if (!Model.getFacade().isANamespace(ns)) {
            LOG.error("Illegal argument. "
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
            ClassDiagram2Renderer rend = new ClassDiagram2Renderer(); // singleton
            lay.setGraphNodeRenderer(rend);
            lay.setGraphEdgeRenderer(rend);
            setLayer(lay);
        }
    }
    
    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getUmlActions()
     */
    protected Object[] getUmlActions() {
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
        // been overridden in a descendent and it is the action from
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
        if (Model.getFacade().isAClass(objectToAccept)) {
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
        } else if (Model.getFacade().isANodeInstance(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAComponentInstance(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isANode(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAComponent(objectToAccept)) {
            return true;
        }
        return false;

    }
    
    @Override
    public FigNode drop(Object droppedObject, Point location) {        
        FigNode figNode = null;

        // If location is non-null, convert to a rectangle that we can use
        Rectangle bounds = null;
        if (location != null) {
            bounds = new Rectangle(location.x, location.y, 0, 0);
        }

        DiagramSettings settings = getDiagramSettings();
        
        if (Model.getFacade().isAAssociation(droppedObject)) {
            figNode =
                createNaryAssociationNode(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAClass(droppedObject)) {
            figNode = new FigClass2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAInterface(droppedObject)) {
            figNode = new FigInterface2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAModel(droppedObject)) {
            figNode = new FigModel2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isASubsystem(droppedObject)) {
            figNode = new FigSubsystem2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAPackage(droppedObject)) {
            figNode = new FigPackage2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAComment(droppedObject)) {
            figNode = new FigComment2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAEnumeration(droppedObject)) {
            figNode = new FigEnumeration2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isADataType(droppedObject)) {
            figNode = new FigDataType2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAStereotype(droppedObject)) {
            figNode = new FigStereotypeDeclaration2(droppedObject, bounds, 
                    settings);
        } else if (Model.getFacade().isAException(droppedObject)) {
            figNode = new FigException2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isASignal(droppedObject)) {
            figNode = new FigSignal2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAActor(droppedObject)) {
            figNode = new FigActor(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAUseCase(droppedObject)) {
            figNode = new FigUseCase(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAObject(droppedObject)) {
            figNode = new FigObject(droppedObject, bounds, settings);
        } else if (Model.getFacade().isANodeInstance(droppedObject)) {
            figNode = new FigNodeInstance(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAComponentInstance(droppedObject)) {
            figNode = new FigComponentInstance(droppedObject, bounds, settings);
        } else if (Model.getFacade().isANode(droppedObject)) {
            figNode = new FigMNode(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAComponent(droppedObject)) {
            figNode = new FigComponent(droppedObject, bounds, settings);
        }
        
        if (figNode != null) {
            // if location is null here the position of the new figNode is set
            // after in org.tigris.gef.base.ModePlace.mousePressed(MouseEvent e)
            if (location != null) {
                figNode.setLocation(location.x, location.y);
            }
            LOG.debug("Dropped object " + droppedObject + " converted to " 
                    + figNode);
        } else {
            LOG.debug("Dropped object NOT added " + droppedObject);
        }
        return figNode;
    }
}
