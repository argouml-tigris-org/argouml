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

import java.beans.PropertyVetoException;

import javax.swing.Action;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.static_structure.ClassDiagramGraphModel;
import org.argouml.uml.diagram.ui.ModeCreateDependency;
import org.argouml.uml.diagram.ui.ModeCreatePermission;
import org.argouml.uml.diagram.ui.ModeCreateUsage;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.ui.foundation.core.ActionAddAttribute;
import org.argouml.uml.ui.foundation.core.ActionAddOperation;
import org.argouml.util.ToolBarUtility;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;

/**
 * UML Class Diagram.
 * 
 * @author jrobbins@ics.uci.edy
 */
public class UMLClassDiagram extends UMLDiagram implements TargetListener {

    /**
     * The UID.
     */
    private static final long serialVersionUID = -9192325790126361563L;

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(UMLClassDiagram.class);

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
    private Action actionAttribute;
    private Action actionOperation;
    private Action actionDataType;
    private Action actionEnumeration;
    private Action actionStereotype;
    private Action actionSignal;
    private Action actionException;

    /**
     * Constructor used by reflection in persistence
     */
    public UMLClassDiagram() {
        super();
        addTargetListener();
        // TODO: All super constructors should take a GraphModel
        setGraphModel(createGraphModel());
    }

    /**
     * Constructor.
     *
     * @param name the name for the new diagram
     * @param namespace the namespace for the new diagram
     */
    public UMLClassDiagram(String name, Object namespace) {
        super(name, namespace);
        addTargetListener();
    }

    /**
     * The constructor. A default unique diagram name is constructed.
     * @param m the namespace
     */
    public UMLClassDiagram(Object m) {
        super(m);
        addTargetListener();
        String name = getNewDiagramName();
        try {
            setName(name);
        } catch (PropertyVetoException pve) {
            LOG.warn("Generated diagram name '" + name 
                    + "' was vetoed by setName");
        }
    }

    /**
     * Add ourselves as a TargetListener after initializing any actions
     * that the listener callback requires.
     */
    private void addTargetListener() {
        actionAttribute = new ActionAddAttribute();
        actionAttribute.setEnabled(false);
        actionOperation = new ActionAddOperation();
        actionOperation.setEnabled(false);
        TargetManager.getInstance().addTargetListener(this);
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
        ClassDiagramGraphModel gm = createGraphModel();
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
    
    // TODO: Needs to be tidied up after stable release. Graph model
    // should be created in constructor
    private ClassDiagramGraphModel createGraphModel() {
	if ((getGraphModel() instanceof ClassDiagramGraphModel)) {
	    return (ClassDiagramGraphModel) getGraphModel();
	} else {
	    return new ClassDiagramGraphModel();
	}
    }
    

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getUmlActions()
     */
    protected Object[] getUmlActions() {
        Object[] actions = {
            getActionPackage(),
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
            getActionAttribute(),
            getActionOperation(),
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

    // TODO: To enable models and subsystems,
    // replace getActionPackage() in the function getUmlActions() above
    // with getPackageActions(). Work started by Markus I believe
    // where does this stand? - Bob.
    private Object[] getPackageActions() {
        Object[] actions = {
            getActionPackage(),
            getActionModel(),
            getActionSubsystem(),
        };
        ToolBarUtility.manageDefault(actions, "diagram.class.package");
        return actions;
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

    /**
     * Create a new diagram name.
     * @return String
     */
    protected String getNewDiagramName() {
        String name = getLabelName() + " " + getNextDiagramSerial();
        if (!ProjectManager.getManager().getCurrentProject()
	        .isValidDiagramName(name)) {
            name = getNewDiagramName();
        }
        return name;
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
                        Model.getMetaTypes().getPermission(),
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
     * @return Returns the actionAttribute.
     */
    private Action getActionAttribute() {
        return actionAttribute;
    }

    /**
     * @return Returns the actionOperation.
     */
    private Action getActionOperation() {
        return actionOperation;
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

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#relocate(java.lang.Object)
     */
    public boolean relocate(Object base) {
        setNamespace(base);
        damage();
        return true;
    }

    public void targetAdded(TargetEvent e) {
	enableActionByTargets();
    }

    public void targetRemoved(TargetEvent e) {
	enableActionByTargets();
    }

    public void targetSet(TargetEvent e) {
	enableActionByTargets();
    }
    
    private void enableActionByTargets() {
        boolean enable = false;
        Object target = TargetManager.getInstance().getSingleModelTarget();
        if (Model.getFacade().isAClassifier(target)
            || Model.getFacade().isAFeature(target)
            || Model.getFacade().isAAssociationEnd(target)) {
            enable = true;
        }
        actionAttribute.setEnabled(enable);
        actionOperation.setEnabled(enable);
    }

    /**
     * 
     * @see org.argouml.uml.diagram.ArgoDiagram#remove()
     */
    @Override
    public void remove() {
	super.remove();
	TargetManager.getInstance().removeTargetListener(this);
    }
    
    
    
} /* end class UMLClassDiagram */
