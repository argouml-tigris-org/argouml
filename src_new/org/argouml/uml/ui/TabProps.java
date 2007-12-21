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

package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.model.Model;
import org.argouml.ui.TabModelTarget;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.activity.ui.UMLActivityDiagram;
import org.argouml.uml.diagram.collaboration.ui.UMLCollaborationDiagram;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.ui.PropPanelString;
import org.argouml.uml.diagram.ui.PropPanelUMLActivityDiagram;
import org.argouml.uml.diagram.ui.PropPanelUMLClassDiagram;
import org.argouml.uml.diagram.ui.PropPanelUMLCollaborationDiagram;
import org.argouml.uml.diagram.ui.PropPanelUMLDeploymentDiagram;
import org.argouml.uml.diagram.ui.PropPanelUMLSequenceDiagram;
import org.argouml.uml.diagram.ui.PropPanelUMLStateDiagram;
import org.argouml.uml.diagram.ui.PropPanelUMLUseCaseDiagram;
import org.argouml.uml.diagram.use_case.ui.UMLUseCaseDiagram;
import org.argouml.uml.ui.behavior.activity_graphs.PropPanelActionState;
import org.argouml.uml.ui.behavior.activity_graphs.PropPanelActivityGraph;
import org.argouml.uml.ui.behavior.activity_graphs.PropPanelCallState;
import org.argouml.uml.ui.behavior.activity_graphs.PropPanelClassifierInState;
import org.argouml.uml.ui.behavior.activity_graphs.PropPanelObjectFlowState;
import org.argouml.uml.ui.behavior.activity_graphs.PropPanelPartition;
import org.argouml.uml.ui.behavior.activity_graphs.PropPanelSubactivityState;
import org.argouml.uml.ui.behavior.collaborations.PropPanelAssociationEndRole;
import org.argouml.uml.ui.behavior.collaborations.PropPanelAssociationRole;
import org.argouml.uml.ui.behavior.collaborations.PropPanelClassifierRole;
import org.argouml.uml.ui.behavior.collaborations.PropPanelCollaboration;
import org.argouml.uml.ui.behavior.collaborations.PropPanelInteraction;
import org.argouml.uml.ui.behavior.collaborations.PropPanelMessage;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelActionSequence;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelArgument;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelCallAction;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelComponentInstance;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelCreateAction;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelDestroyAction;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelException;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelLink;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelLinkEnd;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelNodeInstance;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelObject;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelReception;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelReturnAction;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelSendAction;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelSignal;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelStimulus;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelTerminateAction;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelUninterpretedAction;
import org.argouml.uml.ui.behavior.state_machines.PropPanelCallEvent;
import org.argouml.uml.ui.behavior.state_machines.PropPanelChangeEvent;
import org.argouml.uml.ui.behavior.state_machines.PropPanelCompositeState;
import org.argouml.uml.ui.behavior.state_machines.PropPanelFinalState;
import org.argouml.uml.ui.behavior.state_machines.PropPanelGuard;
import org.argouml.uml.ui.behavior.state_machines.PropPanelPseudostate;
import org.argouml.uml.ui.behavior.state_machines.PropPanelSignalEvent;
import org.argouml.uml.ui.behavior.state_machines.PropPanelSimpleState;
import org.argouml.uml.ui.behavior.state_machines.PropPanelStateMachine;
import org.argouml.uml.ui.behavior.state_machines.PropPanelStubState;
import org.argouml.uml.ui.behavior.state_machines.PropPanelSubmachineState;
import org.argouml.uml.ui.behavior.state_machines.PropPanelSynchState;
import org.argouml.uml.ui.behavior.state_machines.PropPanelTimeEvent;
import org.argouml.uml.ui.behavior.state_machines.PropPanelTransition;
import org.argouml.uml.ui.behavior.use_cases.PropPanelActor;
import org.argouml.uml.ui.behavior.use_cases.PropPanelExtend;
import org.argouml.uml.ui.behavior.use_cases.PropPanelExtensionPoint;
import org.argouml.uml.ui.behavior.use_cases.PropPanelInclude;
import org.argouml.uml.ui.behavior.use_cases.PropPanelUseCase;
import org.argouml.uml.ui.foundation.core.PropPanelAbstraction;
import org.argouml.uml.ui.foundation.core.PropPanelAssociation;
import org.argouml.uml.ui.foundation.core.PropPanelAssociationClass;
import org.argouml.uml.ui.foundation.core.PropPanelAssociationEnd;
import org.argouml.uml.ui.foundation.core.PropPanelAttribute;
import org.argouml.uml.ui.foundation.core.PropPanelClass;
import org.argouml.uml.ui.foundation.core.PropPanelComment;
import org.argouml.uml.ui.foundation.core.PropPanelComponent;
import org.argouml.uml.ui.foundation.core.PropPanelConstraint;
import org.argouml.uml.ui.foundation.core.PropPanelDataType;
import org.argouml.uml.ui.foundation.core.PropPanelDependency;
import org.argouml.uml.ui.foundation.core.PropPanelElementResidence;
import org.argouml.uml.ui.foundation.core.PropPanelEnumeration;
import org.argouml.uml.ui.foundation.core.PropPanelEnumerationLiteral;
import org.argouml.uml.ui.foundation.core.PropPanelFlow;
import org.argouml.uml.ui.foundation.core.PropPanelGeneralization;
import org.argouml.uml.ui.foundation.core.PropPanelInterface;
import org.argouml.uml.ui.foundation.core.PropPanelMethod;
import org.argouml.uml.ui.foundation.core.PropPanelNode;
import org.argouml.uml.ui.foundation.core.PropPanelOperation;
import org.argouml.uml.ui.foundation.core.PropPanelParameter;
import org.argouml.uml.ui.foundation.core.PropPanelPermission;
import org.argouml.uml.ui.foundation.core.PropPanelUsage;
import org.argouml.uml.ui.foundation.extension_mechanisms.PropPanelStereotype;
import org.argouml.uml.ui.foundation.extension_mechanisms.PropPanelTagDefinition;
import org.argouml.uml.ui.foundation.extension_mechanisms.PropPanelTaggedValue;
import org.argouml.uml.ui.model_management.PropPanelElementImport;
import org.argouml.uml.ui.model_management.PropPanelModel;
import org.argouml.uml.ui.model_management.PropPanelPackage;
import org.argouml.uml.ui.model_management.PropPanelSubsystem;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;
import org.tigris.swidgets.Horizontal;
import org.tigris.swidgets.Orientable;
import org.tigris.swidgets.Orientation;

/**
 * This is the tab on the details panel (DetailsPane) that holds the property
 * panel. On change of target, the property panel in TabProps is changed.
 * <p>
 * With the introduction of the TargetManager, this class holds its original
 * power of controlling its target. The property panels (subclasses of
 * PropPanel) for which this class is the container are being registered as
 * TargetListeners in the setTarget method of this class. They are not
 * registered with TargetManager but with this class to prevent race-conditions
 * while firing TargetEvents from TargetManager.
 */
public class TabProps
    extends AbstractArgoJPanel
    implements TabModelTarget {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(TabProps.class);

    private boolean shouldBeEnabled = false;
    private JPanel blankPanel = new JPanel();
    private Hashtable<Class, TabModelTarget> panels = new Hashtable<Class, TabModelTarget>();
    private JPanel lastPanel;
    private String panelClassBaseName = "";

    private Object target;

    /**
     * The list with targetlisteners, these are the property panels
     * managed by TabProps.
     * It should only contain one listener at a time.
     */
    private EventListenerList listenerList = new EventListenerList();

    /**
     * The constructor.
     *
     */
    public TabProps() {
        this("tab.properties", "ui.PropPanel");
    }

    /**
     * The constructor.
     *
     * @param tabName the name of the tab
     * @param panelClassBase the panel class base
     */
    public TabProps(String tabName, String panelClassBase) {
        super(tabName);
        TargetManager.getInstance().addTargetListener(this);
        setOrientation(Horizontal.getInstance());
        panelClassBaseName = panelClassBase;
        setLayout(new BorderLayout());
    }

    /**
     * Set the orientation of the property panel.
     *
     * @param orientation the new orientation for this property panel
     *
     * @see org.tigris.swidgets.Orientable#setOrientation(org.tigris.swidgets.Orientation)
     */
    @Override
    public void setOrientation(Orientation orientation) {
        super.setOrientation(orientation);
        Enumeration pps = panels.elements();
        while (pps.hasMoreElements()) {
            Object o = pps.nextElement();
            if (o instanceof Orientable) {
                Orientable orientable = (Orientable) o;
                orientable.setOrientation(orientation);
            }
        }
    }

    /**
     * Adds a property panel to the internal list. This allows a plugin to
     * add / register a new property panel at run-time.
     * This property panel will then
     * be displayed in the detatils pane whenever an element
     * of the given metaclass is selected.
     *
     * @param c the metaclass whose details show be displayed
     *          in the property panel p
     * @param p an instance of the property panel for the metaclass m
     *
     */
    public void addPanel(Class c, PropPanel p) {
        panels.put(c, p);
    }


    ////////////////////////////////////////////////////////////////
    // accessors
    /**
     * Sets the target of the property panel. The given target t
     * may either be a Diagram or a modelelement. If the target
     * given is a Fig, a check is made if the fig has an owning
     * modelelement and occurs on the current diagram.
     * If so, that modelelement is the target.
     *
     * @deprecated As of ArgoUml version 0.13.5,
     *         the visibility of this method will change in the future,
     *         replaced by {@link org.argouml.ui.targetmanager.TargetManager}.
     *
     * @param t the new target
     * @see org.argouml.ui.TabTarget#setTarget(java.lang.Object)
     */
    public void setTarget(Object t) {
        // targets ought to be UML objects or diagrams
        t = (t instanceof Fig) ? ((Fig) t).getOwner() : t;
        if (!(t == null || Model.getFacade().isAUMLElement(t)
                || t instanceof ArgoDiagram)) {
            return;
        }

        if (lastPanel != null) {
            remove(lastPanel);
            if (lastPanel instanceof TargetListener) {
                removeTargetListener((TargetListener) lastPanel);
            }
        }
        target = t;
        if (t == null) {
            add(blankPanel, BorderLayout.CENTER);
            shouldBeEnabled = false;
            lastPanel = blankPanel;
        } else {
            shouldBeEnabled = true;
            TabModelTarget newPanel = null;
            newPanel = findPanelFor(t);
            if (newPanel != null) {
                addTargetListener(newPanel);
            }
            if (newPanel instanceof JPanel) {
                add((JPanel) newPanel, BorderLayout.CENTER);
                shouldBeEnabled = true;
                lastPanel = (JPanel) newPanel;
            } else {
                add(blankPanel, BorderLayout.CENTER);
                shouldBeEnabled = false;
                lastPanel = blankPanel;
            }

        }
    }

    /*
     * @see org.argouml.ui.TabTarget#refresh()
     */
    public void refresh() {
        setTarget(TargetManager.getInstance().getTarget());
    }

    /**
     * Find the correct properties panel for the target.
     *
     * @param trgt the target class
     * @return the tab panel
     */
    private TabModelTarget findPanelFor(Object trgt) {
        // TODO: No test coverage for this or createPropPanel? - tfm
        
        /* 1st attempt: get a panel that we created before: */
        TabModelTarget p = panels.get(trgt.getClass());
        if (p != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Getting prop panel for: " + trgt.getClass().getName()
                        + ", " + "found (in cache?) " + p);
            }
            return p;
        }

        /* 2nd attempt: If we didn't find the panel then
         * use the factory to create a new one
	 */
        p = createPropPanel(trgt);
        if (p != null) {
            LOG.debug("Factory created " + p.getClass().getName()
                    + " for " + trgt.getClass().getName());
            panels.put(trgt.getClass(), p);
            if (p instanceof PropPanel) {
        	((PropPanel) p).buildToolbar();
            }
            return p;
        }

        LOG.error("Failed to create a prop panel for : " + trgt);
        return null;
    }

    /**
     * A factory method to create a PropPanel for a particular model
     * element.
     *
     * @param modelElement The model element
     * @return A new prop panel to display any model element of the given type
     */
    private TabModelTarget createPropPanel(Object modelElement) {

	TabModelTarget propPanel = null;


	
        // Create prop panels for diagrams
        if (modelElement instanceof UMLActivityDiagram) {
            propPanel = new PropPanelUMLActivityDiagram();
        } else if (modelElement instanceof UMLClassDiagram) {
            propPanel = new PropPanelUMLClassDiagram();
        } else if (modelElement instanceof UMLCollaborationDiagram) {
            propPanel = new PropPanelUMLCollaborationDiagram();
        } else if (modelElement instanceof UMLDeploymentDiagram) {
            propPanel = new PropPanelUMLDeploymentDiagram();
        } else if (modelElement instanceof UMLSequenceDiagram) {
            propPanel = new PropPanelUMLSequenceDiagram();
        } else if (modelElement instanceof UMLStateDiagram) {
            propPanel = new PropPanelUMLStateDiagram();
        } else if (modelElement instanceof UMLUseCaseDiagram) {
            propPanel = new PropPanelUMLUseCaseDiagram();
        }
            // TODO: This needs to be in type hierarchy order to work properly
            // and create the most specific property panel properly
            
        else if (Model.getFacade().isASubmachineState(modelElement)) {
            propPanel = new PropPanelSubmachineState();
        } else if (Model.getFacade().isASubactivityState(modelElement)) {
            propPanel = new PropPanelSubactivityState();
        } else if (Model.getFacade().isAAbstraction(modelElement)) {
            propPanel = new PropPanelAbstraction();
        } else if (Model.getFacade().isACallState(modelElement)) {
            propPanel = new PropPanelCallState();
        } else if (Model.getFacade().isAActionSequence(modelElement)) {
            propPanel = new PropPanelActionSequence();
        } else if (Model.getFacade().isAActionState(modelElement)) {
            propPanel = new PropPanelActionState();
        } else if (Model.getFacade().isAActivityGraph(modelElement)) {
            propPanel = new PropPanelActivityGraph();
        } else if (Model.getFacade().isAActor(modelElement)) {
            propPanel = new PropPanelActor();
        } else if (Model.getFacade().isAArgument(modelElement)) {
            propPanel = new PropPanelArgument();
        } else if (Model.getFacade().isAAssociationClass(modelElement)) {
            propPanel = new PropPanelAssociationClass();
        } else if (Model.getFacade().isAAssociationRole(modelElement)) {
            propPanel = new PropPanelAssociationRole();
        } else if (Model.getFacade().isAAssociation(modelElement)) {
            propPanel = new PropPanelAssociation();
        } else if (Model.getFacade().isAAssociationEndRole(modelElement)) {
            propPanel = new PropPanelAssociationEndRole();
        } else if (Model.getFacade().isAAssociationEnd(modelElement)) {
            propPanel = new PropPanelAssociationEnd();
        } else if (Model.getFacade().isAAttribute(modelElement)) {
            propPanel = new PropPanelAttribute();
        } else if (Model.getFacade().isACallAction(modelElement)) {
            propPanel = new PropPanelCallAction();
        } else if (Model.getFacade().isAClassifierInState(modelElement)) {
            propPanel = new PropPanelClassifierInState();
        } else if (Model.getFacade().isAClass(modelElement)) {
            propPanel = new PropPanelClass();
        } else if (Model.getFacade().isAClassifierRole(modelElement)) {
            propPanel = new PropPanelClassifierRole();
        } else if (Model.getFacade().isACollaboration(modelElement)) {
            propPanel = new PropPanelCollaboration();
        } else if (Model.getFacade().isAComment(modelElement)) {
            propPanel = new PropPanelComment();
        } else if (Model.getFacade().isAComponent(modelElement)) {
            propPanel = new PropPanelComponent();
        } else if (Model.getFacade().isAComponentInstance(modelElement)) {
            propPanel = new PropPanelComponentInstance();
        } else if (Model.getFacade().isACompositeState(modelElement)) {
            propPanel = new PropPanelCompositeState();
        } else if (Model.getFacade().isAConstraint(modelElement)) {
            propPanel = new PropPanelConstraint();
        } else if (Model.getFacade().isACreateAction(modelElement)) {
            propPanel = new PropPanelCreateAction();
        } else if (Model.getFacade().isAEnumeration(modelElement)) {
            propPanel = new PropPanelEnumeration();
        } else if (Model.getFacade().isADataType(modelElement)) {
            propPanel = new PropPanelDataType();
        } else if (Model.getFacade().isADestroyAction(modelElement)) {
            propPanel = new PropPanelDestroyAction();
        } else if (Model.getFacade().isAEnumerationLiteral(modelElement)) {
            propPanel = new PropPanelEnumerationLiteral();
        } else if (Model.getFacade().isAElementImport(modelElement)) {
            propPanel = new PropPanelElementImport();
        } else if (Model.getFacade().isAElementResidence(modelElement)) {
            propPanel = new PropPanelElementResidence();
        } else if (Model.getFacade().isAExtend(modelElement)) {
            propPanel = new PropPanelExtend();
        } else if (Model.getFacade().isAException(modelElement)) {
            propPanel = new PropPanelException();
        } else if (Model.getFacade().isAExtensionPoint(modelElement)) {
            propPanel = new PropPanelExtensionPoint();
        } else if (Model.getFacade().isAFinalState(modelElement)) {
            propPanel = new PropPanelFinalState();
        } else if (Model.getFacade().isAFlow(modelElement)) {
            propPanel = new PropPanelFlow();
        } else if (Model.getFacade().isAGeneralization(modelElement)) {
            propPanel = new PropPanelGeneralization();
        } else if (Model.getFacade().isAGuard(modelElement)) {
            propPanel = new PropPanelGuard();
        } else if (Model.getFacade().isAInclude(modelElement)) {
            propPanel = new PropPanelInclude();
        } else if (Model.getFacade().isAInteraction(modelElement)) {
            propPanel = new PropPanelInteraction();
        } else if (Model.getFacade().isAInterface(modelElement)) {
            propPanel = new PropPanelInterface();
        } else if (Model.getFacade().isALink(modelElement)) {
            propPanel = new PropPanelLink();
        } else if (Model.getFacade().isALinkEnd(modelElement)) {
            propPanel = new PropPanelLinkEnd();
        } else if (Model.getFacade().isAMessage(modelElement)) {
            propPanel = new PropPanelMessage();
        } else if (Model.getFacade().isAMethod(modelElement)) {
            propPanel = new PropPanelMethod();
        } else if (Model.getFacade().isAModel(modelElement)) {
            propPanel = new PropPanelModel();
        } else if (Model.getFacade().isANode(modelElement)) {
            propPanel = new PropPanelNode();
        } else if (Model.getFacade().isANodeInstance(modelElement)) {
            propPanel = new PropPanelNodeInstance();
        } else if (Model.getFacade().isAObject(modelElement)) {
            propPanel = new PropPanelObject();
        } else if (Model.getFacade().isAObjectFlowState(modelElement)) {
            propPanel = new PropPanelObjectFlowState();
        } else if (Model.getFacade().isAOperation(modelElement)) {
            propPanel = new PropPanelOperation();
        } else if (Model.getFacade().isAPackage(modelElement)) {
            propPanel = new PropPanelPackage();
        } else if (Model.getFacade().isAParameter(modelElement)) {
            propPanel = new PropPanelParameter();
        } else if (Model.getFacade().isAPartition(modelElement)) {
            propPanel = new PropPanelPartition();
        } else if (Model.getFacade().isAPackageImport(modelElement)) {
            propPanel = new PropPanelPermission();
        } else if (Model.getFacade().isAPseudostate(modelElement)) {
            propPanel = new PropPanelPseudostate();
        } else if (Model.getFacade().isAReception(modelElement)) {
            propPanel = new PropPanelReception();
        } else if (Model.getFacade().isAReturnAction(modelElement)) {
            propPanel = new PropPanelReturnAction();
        } else if (Model.getFacade().isASendAction(modelElement)) {
            propPanel = new PropPanelSendAction();
        } else if (Model.getFacade().isASignal(modelElement)) {
            propPanel = new PropPanelSignal();
        } else if (Model.getFacade().isASimpleState(modelElement)) {
            propPanel = new PropPanelSimpleState();
        } else if (Model.getFacade().isAStateMachine(modelElement)) {
            propPanel = new PropPanelStateMachine();
        } else if (Model.getFacade().isAStereotype(modelElement)) {
            propPanel = new PropPanelStereotype();
        } else if (Model.getFacade().isAStimulus(modelElement)) {
            propPanel = new PropPanelStimulus();
        } else if (Model.getFacade().isAStubState(modelElement)) {
            propPanel = new PropPanelStubState();
        } else if (Model.getFacade().isASubsystem(modelElement)) {
            propPanel = new PropPanelSubsystem();
        } else if (Model.getFacade().isASynchState(modelElement)) {
            propPanel = new PropPanelSynchState();
        } else if (Model.getFacade().isATaggedValue(modelElement)) {
            propPanel = new PropPanelTaggedValue();
        } else if (Model.getFacade().isATagDefinition(modelElement)) {
            propPanel = new PropPanelTagDefinition();
        } else if (Model.getFacade().isATerminateAction(modelElement)) {
            propPanel = new PropPanelTerminateAction();
        } else if (Model.getFacade().isATransition(modelElement)) {
            propPanel = new PropPanelTransition();
        } else if (Model.getFacade().isAUninterpretedAction(modelElement)) {
            propPanel = new PropPanelUninterpretedAction();
        } else if (Model.getFacade().isAUsage(modelElement)) {
            propPanel = new PropPanelUsage();
        } else if (Model.getFacade().isAUseCase(modelElement)) {
            propPanel = new PropPanelUseCase();
        } else if (Model.getFacade().isACallEvent(modelElement)) {
            propPanel = new PropPanelCallEvent();
        } else if (Model.getFacade().isAChangeEvent(modelElement)) {
            propPanel = new PropPanelChangeEvent();
        } else if (Model.getFacade().isASignalEvent(modelElement)) {
            propPanel = new PropPanelSignalEvent();
        } else if (Model.getFacade().isATimeEvent(modelElement)) {
            propPanel = new PropPanelTimeEvent();
        } else if (Model.getFacade().isADependency(modelElement)) {
            propPanel = new PropPanelDependency();
        } else if (modelElement instanceof FigText) {
            propPanel = new PropPanelString();
        }
        
        if (propPanel instanceof Orientable) {
            ((Orientable) propPanel).setOrientation(getOrientation());
        }
        
        if (propPanel instanceof PropPanel) {
            ((PropPanel) propPanel).setOrientation(getOrientation());
            ((PropPanel) propPanel).buildToolbar();
        }

        return propPanel;
    }

    /**
     * @return the name
     */
    protected String getClassBaseName() {
        return panelClassBaseName;
    }

    /**
     * Returns the current target.
     * @deprecated As of ArgoUml version 0.13.5,
     * the visibility of this method will change in the future, replaced by
     * {@link org.argouml.ui.targetmanager.TargetManager#getTarget()
     * TargetManager.getInstance().getTarget()}.
     *
     * @return the target
     * @see org.argouml.ui.TabTarget#getTarget()
     */
    public Object getTarget() {
        return target;
    }

    /**
     * Determines if the property panel should be enabled.
     * The property panel should always be enabled if the
     * target is an instance of a modelelement or an argodiagram.
     * If the target given is a Fig, a check is made if the fig
     * has an owning modelelement and occurs on
     * the current diagram. If so, that modelelement is the target.
     * @param t the target
     * @return true if property panel should be enabled
     * @see org.argouml.ui.TabTarget#shouldBeEnabled(Object)
     */
    public boolean shouldBeEnabled(Object t) {
        t = (t instanceof Fig) ? ((Fig) t).getOwner() : t;
        if ((t instanceof Diagram || Model.getFacade().isAUMLElement(t))
                && findPanelFor(t) != null) {
            shouldBeEnabled = true;
        } else {
            shouldBeEnabled = false;
        }

        return shouldBeEnabled;
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        setTarget(TargetManager.getInstance().getSingleTarget());
        fireTargetAdded(e);
        if (listenerList.getListenerCount() > 0) {
            validate();
            repaint();
        }

    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        setTarget(TargetManager.getInstance().getSingleTarget());
        fireTargetRemoved(e);
        validate();
        repaint();
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(TargetManager.getInstance().getSingleTarget());
        fireTargetSet(e);
        validate();
        repaint();
    }

    /**
     * Adds a listener.
     * @param listener the listener to add
     */
    private void addTargetListener(TargetListener listener) {
        listenerList.add(TargetListener.class, listener);
    }

    /**
     * Removes a target listener.
     * @param listener the listener to remove
     */
    private void removeTargetListener(TargetListener listener) {
        listenerList.remove(TargetListener.class, listener);
    }

    private void fireTargetSet(TargetEvent targetEvent) {
        //      Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TargetListener.class) {
                // Lazily create the event:
		((TargetListener) listeners[i + 1]).targetSet(targetEvent);
            }
        }
    }

    private void fireTargetAdded(TargetEvent targetEvent) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TargetListener.class) {
                // Lazily create the event:
		((TargetListener) listeners[i + 1]).targetAdded(targetEvent);
            }
        }
    }

    private void fireTargetRemoved(TargetEvent targetEvent) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TargetListener.class) {
                // Lazily create the event:
                ((TargetListener) listeners[i + 1]).targetRemoved(targetEvent);
            }
        }
    }

} /* end class TabProps */

