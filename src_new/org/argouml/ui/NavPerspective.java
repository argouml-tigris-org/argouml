// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: NavPerspective.java
// Classes: NavPerspective
// Original Author: your email address here
// $Id$

// 16 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to support
// the display of extends/includes and extension points in the package centric
// view.

package org.argouml.ui;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.argouml.kernel.PredAND;
import org.argouml.kernel.PredInstanceOf;
import org.argouml.kernel.PredNotInstanceOf;
import org.argouml.kernel.PredOR;
import org.argouml.uml.diagram.collaboration.ui.GoAssocRoleMessages;
import org.argouml.uml.diagram.collaboration.ui.GoClassifierToCollaboration;
import org.argouml.uml.diagram.collaboration.ui.GoCollaborationDiagram;
import org.argouml.uml.diagram.collaboration.ui.GoCollaborationInteraction;
import org.argouml.uml.diagram.collaboration.ui.GoInteractionMessage;
import org.argouml.uml.diagram.collaboration.ui.GoMessageAction;
import org.argouml.uml.diagram.collaboration.ui.GoModelToCollaboration;
import org.argouml.uml.diagram.collaboration.ui.GoOperationToCollaboration;
import org.argouml.uml.diagram.collaboration.ui.GoProjectToCollaboration;
import org.argouml.uml.diagram.deployment.ui.GoDiagramToNode;
import org.argouml.uml.diagram.sequence.ui.GoLinkStimuli;
import org.argouml.uml.diagram.sequence.ui.GoStimulusToAction;
import org.argouml.uml.diagram.state.PredIsFinalState;
import org.argouml.uml.diagram.state.PredIsStartState;
import org.argouml.uml.diagram.state.ui.GoMachineDiagram;
import org.argouml.uml.diagram.state.ui.GoMachineToState;
import org.argouml.uml.diagram.state.ui.GoMachineToTrans;
import org.argouml.uml.diagram.state.ui.GoProjectToStateMachine;
import org.argouml.uml.diagram.state.ui.GoStateMachineToTransition;
import org.argouml.uml.diagram.state.ui.GoStateToDoActivity;
import org.argouml.uml.diagram.state.ui.GoStateToDownstream;
import org.argouml.uml.diagram.state.ui.GoStateToEntry;
import org.argouml.uml.diagram.state.ui.GoStateToExit;
import org.argouml.uml.diagram.state.ui.GoStateToIncomingTrans;
import org.argouml.uml.diagram.state.ui.GoStateToInternalTrans;
import org.argouml.uml.diagram.state.ui.GoStateToOutgoingTrans;
import org.argouml.uml.diagram.state.ui.GoCompositeStateToSubvertex;
import org.argouml.uml.diagram.state.ui.GoTransitionToSource;
import org.argouml.uml.diagram.state.ui.GoTransitionToTarget;
import org.argouml.uml.diagram.static_structure.ui.GoClassToAssociatedClass;
import org.argouml.uml.diagram.static_structure.ui.GoClassToNavigableClass;
import org.argouml.uml.diagram.static_structure.ui.GoModelToClass;
import org.argouml.uml.diagram.static_structure.ui.GoClassToSummary;
import org.argouml.uml.diagram.static_structure.ui.GoSummaryToAssociation;
import org.argouml.uml.diagram.static_structure.ui.GoSummaryToAttribute;
import org.argouml.uml.diagram.static_structure.ui.GoSummaryToInheritance;
import org.argouml.uml.diagram.static_structure.ui.GoSummaryToIncomingDependency;
import org.argouml.uml.diagram.static_structure.ui.GoSummaryToOutgoingDependency;
import org.argouml.uml.diagram.static_structure.ui.GoSummaryToOperation;
import org.argouml.uml.diagram.ui.GoBehavioralFeatureToStateDiagram;
import org.argouml.uml.diagram.ui.GoBehavioralFeatureToStateMachine;
import org.argouml.uml.diagram.ui.GoClassifierToBeh;
import org.argouml.uml.diagram.ui.GoClassifierToStateMachine;
import org.argouml.uml.diagram.ui.GoClassifierToStructuralFeature;
import org.argouml.uml.diagram.ui.GoDiagramToEdge;
import org.argouml.uml.diagram.ui.GoFilteredChildren;
import org.argouml.uml.diagram.ui.GoGenElementToDerived;
import org.argouml.uml.diagram.ui.GoInteractionMessages;
import org.argouml.uml.diagram.ui.GoModelToBaseElements;
import org.argouml.uml.diagram.ui.GoModelToDiagram;
import org.argouml.uml.diagram.ui.GoModelToElements;
import org.argouml.uml.diagram.ui.GoOperationToCollaborationDiagram;
import org.argouml.uml.diagram.ui.GoProjectToDiagram;
import org.argouml.uml.diagram.ui.GoProjectToModel;
import org.argouml.uml.diagram.use_case.ui.GoUseCaseToExtensionPoint;
import org.argouml.uml.ui.behavior.common_behavior.GoSignalToReception;
import org.argouml.uml.ui.foundation.core.GoModelElementToComment;

import ru.novosoft.uml.behavior.collaborations.MCollaboration;
import ru.novosoft.uml.behavior.common_behavior.MComponentInstance;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.common_behavior.MNodeInstance;
import ru.novosoft.uml.behavior.common_behavior.MObject;
import ru.novosoft.uml.behavior.use_cases.MExtend;
import ru.novosoft.uml.behavior.use_cases.MInclude;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MDependency;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.model_management.MPackage;
import ru.novosoft.uml.model_management.MSubsystem;

/** This defines a NavPerspective as a kind of TreeModel that is made
 *  up of rules from the files whose names begin with "Go".
 *
 * <p>It also defines several useful navigational perspectives.
 *
 * $Id$
 */
public class NavPerspective
    extends TreeModelComposite
    implements
        Serializable {

    ////////////////////////////////////////////////////////////////
    // instance variables

    /** needs documenting */
    protected EventListenerList _listenerList = new EventListenerList();

    ////////////////////////////////////////////////////////////////
    // static variables

    /** needs documenting */
    protected static Vector _registeredPerspectives = new Vector();
    
    /** needs documenting */
    protected static Vector _rules = new Vector();

    /**
     * statically initialise all perspectives
     */
    static {
        // ----------- navigation perspectives ---------------------
        
        // this are meant for pane-1 of NavigatorPane, they all have
        // Project as their only prerequiste.  These trees tend to be 3
        // to 5 levels deep and sometimes have recursion.
        NavPerspective packageCentric =
            new NavPerspective("combobox.item.package-centric");
        
        NavPerspective diagramCentric =
            new NavPerspective("combobox.item.diagram-centric");
        
        NavPerspective inheritanceCentric =
            new NavPerspective("combobox.item.inheritance-centric");
        
        NavPerspective classAssociation =
            new NavPerspective("combobox.item.class-associations");
        
        NavPerspective navAssociation =
            new NavPerspective("combobox.item.navigable-associations");
        
        NavPerspective aggregateCentric =
            new NavPerspective("combobox.item.aggregate-centric");
        
        NavPerspective compositeCentric =
            new NavPerspective("combobox.item.composite-centric");
        
        NavPerspective classStates =
            new NavPerspective("combobox.item.class-states");
        
        NavPerspective stateCentric =
            new NavPerspective("combobox.item.state-centric");
        
        NavPerspective stateTransitions =
            new NavPerspective("combobox.item.state-transitions");
        
        NavPerspective transitionCentric =
            new NavPerspective("combobox.item.transitions-centric");
        
        NavPerspective transitionPaths =
            new NavPerspective("combobox.item.transitions-paths");
        
        NavPerspective collabCentric =
            new NavPerspective("combobox.item.collaboration-centric");
        
        NavPerspective depCentric =
            new NavPerspective("combobox.item.dependency-centric");

        // These are intended for pane-2 of NavigatorPane, the tend to be
        // simple and shallow, and have something in pane-1 as a prerequiste
        // TODO i8n
        NavPerspective useCaseToExtensionPoint =
            new NavPerspective("Extension Points of Use Case");
        
        NavPerspective classToBehStr =
            new NavPerspective("misc.features-of-class");
        
        NavPerspective classToBeh = new NavPerspective("misc.methods-of-class");
        
        NavPerspective classToStr =
            new NavPerspective("misc.attributes-of-class");
        
        NavPerspective machineToState =
            new NavPerspective("misc.states-of-class");
        
        NavPerspective machineToTransition =
            new NavPerspective("misc.transitions-of-class");
        
        // TODO i8n
        NavPerspective classCentric =
            new NavPerspective("Class - centric");

        // -------------- GO rules --------------------------
        
        // Subsystem is travsersed via Classifier. Eugenio
        GoFilteredChildren modelToPackages =
            new GoFilteredChildren(
                "misc.package.subpackages",
                new GoModelToElements(),
                new PredAND(
                    new PredInstanceOf(MPackage.class),
                    new PredNotInstanceOf(MSubsystem.class)));

        GoFilteredChildren modelToClassifiers =
            new GoFilteredChildren(
                "misc.package.classifiers",
                new GoModelToElements(),
                new PredInstanceOf(MClassifier.class));

        // AssociationClass is traversed via Classifier. Eugenio
        GoFilteredChildren modelToAssociations =
            new GoFilteredChildren(
                "misc.package.associations",
                new GoModelToElements(),
                new PredAND(
                    new PredInstanceOf(MAssociation.class),
                    new PredNotInstanceOf(MAssociationClass.class)));

        GoFilteredChildren modelToGeneralizations =
            new GoFilteredChildren(
                "misc.package.generalizations",
                new GoModelToElements(),
                new PredInstanceOf(MGeneralization.class));

        // Extend and include are traversed via use case.
        GoFilteredChildren modelToExtendsAndIncludes =
            new GoFilteredChildren(
                "Package->Extends/Includes",
                new GoModelToElements(),
                new PredOR(
                    new PredInstanceOf(MExtend.class),
                    new PredInstanceOf(MInclude.class)));

        GoFilteredChildren modelToDependencies =
            new GoFilteredChildren(
                "misc.package.dependencies",
                new GoModelToElements(),
                new PredInstanceOf(MDependency.class));

        GoFilteredChildren modelToInstances =
            new GoFilteredChildren(
                "misc.package.instances",
                new GoModelToElements(),
                new PredInstanceOf(MObject.class));
        GoFilteredChildren modelToLinks =
            new GoFilteredChildren(
                "misc.package.links",
                new GoModelToElements(),
                new PredInstanceOf(MLink.class));
        GoFilteredChildren modelToCollaboration =
            new GoFilteredChildren(
                "misc.package.collaborations",
                new GoModelToElements(),
                new PredInstanceOf(MCollaboration.class));

        GoFilteredChildren modelToComponentInstance =
            new GoFilteredChildren(
                "misc.package.componentinstance",
                new GoModelToElements(),
                new PredInstanceOf(MComponentInstance.class));

        GoFilteredChildren modelToNodeInstance =
            new GoFilteredChildren(
                "misc.package.nodeinstance",
                new GoModelToElements(),
                new PredInstanceOf(MNodeInstance.class));

        GoFilteredChildren machineToFinalState =
            new GoFilteredChildren(
                "misc.state-machine.final-states",
                new GoMachineToState(),
                PredIsFinalState.TheInstance);
        GoFilteredChildren machineToInitialState =
            new GoFilteredChildren(
                "misc.state-machine.initial-states",
                new GoMachineToState(),
                PredIsStartState.TheInstance);
        transitionPaths.addSubTreeModel(machineToInitialState);

        GoFilteredChildren compositeToFinalStates =
            new GoFilteredChildren(
                "misc.state.final-substates",
                new GoCompositeStateToSubvertex(),
                PredIsFinalState.TheInstance);
        GoFilteredChildren compositeToInitialStates =
            new GoFilteredChildren(
                "misc.state.initial-substates",
                new GoCompositeStateToSubvertex(),
                PredIsStartState.TheInstance);
        
        // ---------------- building the perspectives
        
        packageCentric.addSubTreeModel(new GoProjectToModel());
        packageCentric.addSubTreeModel(new GoModelToDiagram());
        packageCentric.addSubTreeModel(new GoModelElementToComment());
        packageCentric.addSubTreeModel(modelToPackages);
        packageCentric.addSubTreeModel(modelToClassifiers);
        packageCentric.addSubTreeModel(modelToAssociations);
        packageCentric.addSubTreeModel(modelToExtendsAndIncludes);
        packageCentric.addSubTreeModel(modelToInstances);
        packageCentric.addSubTreeModel(modelToLinks);
        packageCentric.addSubTreeModel(new GoModelToCollaboration());
        packageCentric.addSubTreeModel(modelToComponentInstance);
        packageCentric.addSubTreeModel(modelToNodeInstance);
        packageCentric.addSubTreeModel(modelToGeneralizations);
        packageCentric.addSubTreeModel(modelToDependencies);
        packageCentric.addSubTreeModel(new GoUseCaseToExtensionPoint());
        packageCentric.addSubTreeModel(new GoClassifierToStructuralFeature());
        packageCentric.addSubTreeModel(new GoClassifierToBeh());
        packageCentric.addSubTreeModel(new GoCollaborationInteraction());
        packageCentric.addSubTreeModel(new GoInteractionMessage());
        packageCentric.addSubTreeModel(new GoMessageAction());
        packageCentric.addSubTreeModel(new GoSignalToReception());
        packageCentric.addSubTreeModel(new GoLinkStimuli());
        packageCentric.addSubTreeModel(new GoStimulusToAction());
        packageCentric.addSubTreeModel(new GoClassifierToCollaboration());
        packageCentric.addSubTreeModel(new GoOperationToCollaboration());
        packageCentric.addSubTreeModel(new GoOperationToCollaborationDiagram());

        // rules for statemachinediagram and activitydiagram
        packageCentric.addSubTreeModel(new GoBehavioralFeatureToStateMachine());
        packageCentric.addSubTreeModel(new GoBehavioralFeatureToStateDiagram());
        packageCentric.addSubTreeModel(new GoClassifierToStateMachine());
        packageCentric.addSubTreeModel(new GoMachineToState());
        packageCentric.addSubTreeModel(new GoCompositeStateToSubvertex());
        packageCentric.addSubTreeModel(new GoStateToInternalTrans());
        packageCentric.addSubTreeModel(new GoStateMachineToTransition());
        packageCentric.addSubTreeModel(new GoStateToDoActivity());
        packageCentric.addSubTreeModel(new GoStateToEntry());
        packageCentric.addSubTreeModel(new GoStateToExit());

        diagramCentric.addSubTreeModel(new GoProjectToDiagram());
        diagramCentric.addSubTreeModel(new GoDiagramToNode());
        diagramCentric.addSubTreeModel(new GoDiagramToEdge());
        diagramCentric.addSubTreeModel(new GoUseCaseToExtensionPoint());
        diagramCentric.addSubTreeModel(new GoClassifierToStructuralFeature());
        diagramCentric.addSubTreeModel(new GoClassifierToBeh());

        inheritanceCentric.addSubTreeModel(new GoProjectToModel());
        inheritanceCentric.addSubTreeModel(new GoModelToBaseElements());
        inheritanceCentric.addSubTreeModel(new GoGenElementToDerived());

        classAssociation.addSubTreeModel(new GoProjectToModel());
        classAssociation.addSubTreeModel(new GoModelToDiagram());
        classAssociation.addSubTreeModel(new GoModelToClass());
        classAssociation.addSubTreeModel(new GoClassToAssociatedClass());

        navAssociation.addSubTreeModel(new GoProjectToModel());
        navAssociation.addSubTreeModel(new GoModelToDiagram());
        navAssociation.addSubTreeModel(new GoModelToClass());
        navAssociation.addSubTreeModel(new GoClassToNavigableClass());               

        stateCentric.addSubTreeModel(new GoProjectToStateMachine());
        stateCentric.addSubTreeModel(new GoMachineDiagram());
        stateCentric.addSubTreeModel(new GoMachineToState());
        stateCentric.addSubTreeModel(new GoCompositeStateToSubvertex());
        stateCentric.addSubTreeModel(new GoStateToIncomingTrans());
        stateCentric.addSubTreeModel(new GoStateToOutgoingTrans());

        transitionCentric.addSubTreeModel(new GoProjectToStateMachine());
        transitionCentric.addSubTreeModel(new GoMachineDiagram());
        transitionCentric.addSubTreeModel(new GoMachineToTrans());
        transitionCentric.addSubTreeModel(new GoTransitionToSource());
        transitionCentric.addSubTreeModel(new GoTransitionToTarget());

        transitionPaths.addSubTreeModel(new GoProjectToStateMachine());
        transitionPaths.addSubTreeModel(new GoMachineDiagram());
        transitionPaths.addSubTreeModel(compositeToInitialStates);
        transitionPaths.addSubTreeModel(new GoStateToDownstream());

        collabCentric.addSubTreeModel(new GoProjectToCollaboration());
        collabCentric.addSubTreeModel(new GoCollaborationDiagram());
        collabCentric.addSubTreeModel(new GoModelToElements());
        collabCentric.addSubTreeModel(new GoAssocRoleMessages());
        collabCentric.addSubTreeModel(new GoCollaborationInteraction());
        collabCentric.addSubTreeModel(new GoInteractionMessages());        

        useCaseToExtensionPoint.addSubTreeModel(new GoUseCaseToExtensionPoint());

        classToBehStr.addSubTreeModel(new GoClassifierToStructuralFeature());
        classToBehStr.addSubTreeModel(new GoClassifierToBeh());

        classToBeh.addSubTreeModel(new GoClassifierToBeh());

        classToStr.addSubTreeModel(new GoClassifierToStructuralFeature());

        machineToState.addSubTreeModel(new GoMachineToState());

        machineToTransition.addSubTreeModel(new GoMachineToTrans());

        classCentric.addSubTreeModel(new GoProjectToModel());
        classCentric.addSubTreeModel(new GoModelToDiagram());
        classCentric.addSubTreeModel(modelToPackages);
        classCentric.addSubTreeModel(modelToClassifiers);
        classCentric.addSubTreeModel(new GoClassToSummary());
        classCentric.addSubTreeModel(new GoSummaryToAssociation());
        classCentric.addSubTreeModel(new GoSummaryToAttribute());
        classCentric.addSubTreeModel(new GoSummaryToOperation());
        classCentric.addSubTreeModel(new GoSummaryToInheritance());
        classCentric.addSubTreeModel(new GoSummaryToIncomingDependency());
        classCentric.addSubTreeModel(new GoSummaryToOutgoingDependency());
        
        registerPerspective(packageCentric);
        registerPerspective(classCentric);
        registerPerspective(diagramCentric);
        registerPerspective(inheritanceCentric);
        registerPerspective(classAssociation);
        registerPerspective(navAssociation);
        registerPerspective(stateCentric);
        registerPerspective(transitionCentric);
        registerPerspective(transitionPaths);
        registerPerspective(collabCentric);
        registerPerspective(depCentric);
    }/** end of static initialiser... */

    ////////////////////////////////////////////////////////////////
    // static methods

    /** needs documenting */
    public static void registerPerspective(NavPerspective np) {
        _registeredPerspectives.addElement(np);
    }

    /** needs documenting */
    public static void unregisterPerspective(NavPerspective np) {
        _registeredPerspectives.removeElement(np);
    }

    /** needs documenting */
    public static Vector getRegisteredPerspectives() {
        return _registeredPerspectives;
    }

    /** needs documenting */
    public static void registerRule(AbstractGoRule rule) {
        _rules.addElement(rule);
    }

    /** needs documenting */
    public static Vector getRegisteredRules() {
        return _rules;
    }

    ////////////////////////////////////////////////////////////////
    // constructor

    /** needs documenting */
    public NavPerspective(String name) {
        super(name);
    }

    /** needs documenting */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    ////////////////////////////////////////////////////////////////
    // Some methods from DefaultTreeModel

    /**
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    protected void fireTreeNodesChanged(
                    Object source,
                    Object[] path,
                    int[] childIndices,
                    Object[] children) {
            
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e =
                        new TreeModelEvent(
                            source,
                            path,
                            childIndices,
                            children);
                ((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
            }
        }
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    protected void fireTreeNodesInserted(
                    Object source,
                    Object[] path,
                    int[] childIndices,
                    Object[] children) {
            
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e =
                        new TreeModelEvent(
                            source,
                            path,
                            childIndices,
                            children);
                ((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
            }
        }
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    protected void fireTreeNodesRemoved(
                    Object source,
                    Object[] path,
                    int[] childIndices,
                    Object[] children) {
            
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e =
                        new TreeModelEvent(
                            source,
                            path,
                            childIndices,
                            children);
                ((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
            }
        }
    }

    protected void fireTreeStructureChanged(Object source, Object[] path) {
        fireTreeStructureChanged(source, path, null, null);
    }

    /**
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    public void fireTreeStructureChanged(
                    Object source,
                    Object[] path,
                    int[] childIndices,
                    Object[] children) {
            
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e =
                        new TreeModelEvent(
                            source,
                            path,
                            childIndices,
                            children);
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }

    ////////////////////////////////////////////////////////////////
    // TreeModel implementation

    /** needs documenting */
    public void addTreeModelListener(TreeModelListener l) {
        _listenerList.add(TreeModelListener.class, l);
    }

    /** needs documenting */
    public void removeTreeModelListener(TreeModelListener l) {
        _listenerList.remove(TreeModelListener.class, l);
    }

    /**
     * Will return the first found child object in the navtree. The child can be 
     * a TreeNode in case the super will be called to handle this. In all other
     * cases we try to handle it in a recursive way.
     * TODO this does not work yet since the implementation of getChildren of
     * AbstractGoRule only takes one level into account.
     * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
     */
    public int getIndexOfChild(Object parent, Object child) {
        
        if (child == null || parent == null) return -1;
        if (child instanceof TreeNode)
            return super.getIndexOfChild(parent, child);
        else {
            int helperindex = -1;
            for (int i = 0; i < _subTreeModels.size(); i++) {
                Object o = _subTreeModels.get(i);
               
                    AbstractGoRule rule =
                        (AbstractGoRule) _subTreeModels.get(i);
                    // if (!rule.isLeaf(parent)) {
                        // the given parent turns up to have children
                        helperindex = rule.getIndexOfChild(parent, child);
                        if (helperindex > -1) { // we found the correct element
                            return i + helperindex;
                        } else {
                            helperindex = getHelperIndex(rule, parent, child);
                            if (helperindex > -1) {
                                return i + helperindex;
                            }
                        }
                    // }

            }
        }

        return -1;
    }

    /** needs documenting */
    private int getHelperIndex(
                    AbstractGoRule rule,
                    Object parent,
                    Object child) {
            
        if (parent == child)
            throw new IllegalStateException("Parent cannot equal child");
        if (rule.getChildCount(parent) == 0)
            return -1;
        else {
            int index = rule.getIndexOfChild(parent, child);
            if (index == -1) {
                // the level directly under the parent does not contain the child

                    int counter = 0;
                    Iterator it = rule.getChildren(parent).iterator();
                    while (it.hasNext()) {
                        index = getHelperIndex(rule, it.next(), child);
                        if (index > -1)
                            return counter + index;
                        counter++;
                    }
               

            }
        }

        return -1;
    }

} /* end class NavPerspective */
