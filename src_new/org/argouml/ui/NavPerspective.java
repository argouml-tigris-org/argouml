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

import java.util.*;
import java.awt.*;
import java.io.Serializable;
import javax.swing.tree.*;
import javax.swing.event.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.model_management.*;

import org.argouml.kernel.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.deployment.ui.*;
import org.argouml.uml.diagram.static_structure.ui.*;
import org.argouml.uml.diagram.state.*;
import org.argouml.uml.diagram.state.ui.*;
import org.argouml.uml.diagram.collaboration.ui.*;
import org.argouml.uml.diagram.use_case.ui.*;
import org.argouml.uml.ui.behavior.common_behavior.GoSignalToReception;
import org.argouml.uml.diagram.sequence.ui.*;

import org.argouml.uml.cognitive.*;
import org.argouml.uml.cognitive.critics.*;



/** This defines a NavPerspective as a kind of TreeModel that is made
 *  up of rules from the files whose names begin with "Go".  It also
 *  defines several useful navigational perspectives. */

public class NavPerspective extends TreeModelComposite
implements Serializable, TreeModel, Cloneable {

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected EventListenerList _listenerList = new EventListenerList();


  ////////////////////////////////////////////////////////////////
  // static variables

  protected static Vector _registeredPerspectives = new Vector();
  protected static Vector _rules = new Vector();


  static {
    // this are meant for pane-1 of NavigatorPane, they all have
    // Project as their only prerequiste.  These trees tend to be 3
    // to 5 levels deep and sometimes have recursion.
    NavPerspective packageCentric = new NavPerspective("combobox.item.package-centric");
    NavPerspective diagramCentric = new NavPerspective("combobox.item.diagram-centric");
    NavPerspective inheritanceCentric = new NavPerspective("combobox.item.inheritance-centric");
    NavPerspective classAssociation = new NavPerspective("combobox.item.class-associations");
    NavPerspective navAssociation = new NavPerspective("combobox.item.navigable-associations");
    NavPerspective associationCentric = new NavPerspective("combobox.item.association-centric");
    NavPerspective aggregateCentric = new NavPerspective("combobox.item.aggregate-centric");
    NavPerspective compositeCentric = new NavPerspective("combobox.item.composite-centric");
    NavPerspective classStates = new NavPerspective("combobox.item.class-states");
    NavPerspective stateCentric = new NavPerspective("combobox.item.state-centric");
    NavPerspective stateTransitions = new NavPerspective("combobox.item.state-transitions");
    NavPerspective transitionCentric = new NavPerspective("combobox.item.transitions-centric");
    NavPerspective transitionPaths = new NavPerspective("combobox.item.transitions-paths");
//     NavPerspective useCaseCentric = new NavPerspective("combobox.item.usecase-centric");
    NavPerspective collabCentric = new NavPerspective("combobox.item.collaboration-centric");
    NavPerspective depCentric = new NavPerspective("combobox.item.dependency-centric");


    // These are intended for pane-2 of NavigatorPane, the tend to be
    // simple and shallow, and have something in pane-1 as a prerequiste
    NavPerspective useCaseToExtensionPoint =
        new NavPerspective("Extension Points of Use Case");
    NavPerspective classToBehStr = new NavPerspective("misc.features-of-class");
    NavPerspective classToBeh = new NavPerspective("misc.methods-of-class");
    NavPerspective classToStr = new NavPerspective("misc.attributes-of-class");
    NavPerspective machineToState = new NavPerspective("misc.states-of-class");
    NavPerspective machineToTransition = new NavPerspective("misc.transitions-of-class");

    // Subsystem is travsersed via Classifier. Eugenio
    GoFilteredChildren modelToPackages =
      new GoFilteredChildren("misc.package.subpackages",
			     new GoModelToElements(),
			     new PredAND(new PredInstanceOf(MPackage.class),
                             new PredNotInstanceOf(MSubsystem.class)));

    GoFilteredChildren modelToClassifiers =
      new GoFilteredChildren("misc.package.classifiers",
			     new GoModelToElements(),
                 new PredInstanceOf(MClassifier.class));

    // AssociationClass is traversed via Classifier. Eugenio
    GoFilteredChildren modelToAssociations =
      new GoFilteredChildren("misc.package.associations",
			     new GoModelToElements(),
			     new PredAND(new PredInstanceOf(MAssociation.class),
                             new PredNotInstanceOf(MAssociationClass.class)));
                             
    GoFilteredChildren modelToGeneralizations = 
    	new GoFilteredChildren("misc.package.generalizations",
    			new GoModelToElements(),
    			new PredInstanceOf(MGeneralization.class));

    // Extend and include are traversed via use case.

    GoFilteredChildren modelToExtendsAndIncludes =
      new GoFilteredChildren("Package->Extends/Includes",
			     new GoModelToElements(),
			     new PredOR(new PredInstanceOf(MExtend.class),
                                        new PredInstanceOf(MInclude.class)));
    
     GoFilteredChildren modelToDependencies = 
        new GoFilteredChildren("misc.package.dependencies",
                new GoModelToElements(),
                new PredInstanceOf(MDependency.class));
    

    GoFilteredChildren modelToInstances =
      new GoFilteredChildren("misc.package.instances",
			     new GoModelToElements(),
			     new PredInstanceOf(MObject.class));
    GoFilteredChildren modelToLinks =
      new GoFilteredChildren("misc.package.links",
			     new GoModelToElements(),
			     new PredInstanceOf(MLink.class));
    GoFilteredChildren modelToCollaboration =
      new GoFilteredChildren("misc.package.collaborations",
			     new GoModelToElements(),
			     new PredInstanceOf(MCollaboration.class));
	
	GoFilteredChildren modelToComponentInstance = 
		new GoFilteredChildren("misc.package.componentinstance",
			     new GoModelToElements(),
			     new PredInstanceOf(MComponentInstance.class));
			     
	GoFilteredChildren modelToNodeInstance = 
		new GoFilteredChildren("misc.package.nodeinstance",
			     new GoModelToElements(),
			     new PredInstanceOf(MNodeInstance.class));
	

    packageCentric.addSubTreeModel(new GoProjectModel());
    packageCentric.addSubTreeModel(new GoModelToDiagram());
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
    packageCentric.addSubTreeModel(new GoClassifierToStr());
    packageCentric.addSubTreeModel(new GoClassifierToBeh());
    // packageCentric.addSubTreeModel(new GoAssocRoleMessages());
    packageCentric.addSubTreeModel(new GoCollaborationInteraction());
    packageCentric.addSubTreeModel(new GoInteractionMessage());
    packageCentric.addSubTreeModel(new GoMessageAction());
    packageCentric.addSubTreeModel(new GoSignalToReception());
    packageCentric.addSubTreeModel(new GoLinkStimuli());
    packageCentric.addSubTreeModel(new GoStimulusAction());
    packageCentric.addSubTreeModel(new GoClassifierToCollaboration());
    packageCentric.addSubTreeModel(new GoOperationToCollaboration());
    
    // rules for statemachinediagram and activitydiagram
    packageCentric.addSubTreeModel(new GoBehavioralFeatureToStateMachine());
    packageCentric.addSubTreeModel(new GoClassifierToStateMachine());
    packageCentric.addSubTreeModel(new GoMachineToState());
    packageCentric.addSubTreeModel(new GoStateToSubstate());
    packageCentric.addSubTreeModel(new GoStateToInternalTrans());
    packageCentric.addSubTreeModel(new GoStateMachineToTransition());
	
    diagramCentric.addSubTreeModel(new GoProjectDiagram());
    diagramCentric.addSubTreeModel(new GoDiagramToNode());
    diagramCentric.addSubTreeModel(new GoDiagramToEdge());
    diagramCentric.addSubTreeModel(new GoUseCaseToExtensionPoint());
    diagramCentric.addSubTreeModel(new GoClassifierToStr());
    diagramCentric.addSubTreeModel(new GoClassifierToBeh());

    inheritanceCentric.addSubTreeModel(new GoProjectModel());
    inheritanceCentric.addSubTreeModel(new GoModelToBaseElements());
    inheritanceCentric.addSubTreeModel(new GoGenElementToDerived());

    classAssociation.addSubTreeModel(new GoProjectModel());
    classAssociation.addSubTreeModel(new GoModelToDiagram());
    classAssociation.addSubTreeModel(new GoModelToClass());
    //classAssociation.addSubTreeModel(new GoClassifierToBeh());
    //classAssociation.addSubTreeModel(new GoClassifierToStr());
    classAssociation.addSubTreeModel(new GoClassToAssocdClass());

    navAssociation.addSubTreeModel(new GoProjectModel());
    navAssociation.addSubTreeModel(new GoModelToDiagram());
    navAssociation.addSubTreeModel(new GoModelToClass());
    //navAssociation.addSubTreeModel(new GoClassifierToBeh());
    //navAssociation.addSubTreeModel(new GoClassifierToStr());
    navAssociation.addSubTreeModel(new GoClassToNavigableClass());

    aggregateCentric.addSubTreeModel(new GoProjectModel());
    aggregateCentric.addSubTreeModel(new GoModelToDiagram());
    aggregateCentric.addSubTreeModel(new GoModelToClass());
    aggregateCentric.addSubTreeModel(new GoClassToAggrClass());

    compositeCentric.addSubTreeModel(new GoProjectModel());
    compositeCentric.addSubTreeModel(new GoModelToDiagram());
    compositeCentric.addSubTreeModel(new GoModelToClass());
    compositeCentric.addSubTreeModel(new GoClassToCompositeClass());


    associationCentric.addSubTreeModel(new GoProjectModel());
    associationCentric.addSubTreeModel(new GoModelToDiagram());
    associationCentric.addSubTreeModel(new GoModelToAssociation());
    associationCentric.addSubTreeModel(new GoAssocToSource());
    associationCentric.addSubTreeModel(new GoAssocToTarget());

//     classStates.addSubTreeModel(new GoProjectModel());
//     classStates.addSubTreeModel(new GoModelToDiagram());
//     classStates.addSubTreeModel(new GoModelToClass());
//     classStates.addSubTreeModel(new GoElementToMachine());
//     classStates.addSubTreeModel(new GoMachineToState());

    stateCentric.addSubTreeModel(new GoProjectMachine());
    stateCentric.addSubTreeModel(new GoMachineDiagram());
    stateCentric.addSubTreeModel(new GoMachineToState());
    stateCentric.addSubTreeModel(new GoStateToSubstate());
    stateCentric.addSubTreeModel(new GoStateToIncomingTrans());
    stateCentric.addSubTreeModel(new GoStateToOutgoingTrans());

    transitionCentric.addSubTreeModel(new GoProjectMachine());
    transitionCentric.addSubTreeModel(new GoMachineDiagram());
    transitionCentric.addSubTreeModel(new GoMachineToTrans());
    transitionCentric.addSubTreeModel(new GoTransToSourceState());
    transitionCentric.addSubTreeModel(new GoTransToTargetState());

    transitionPaths.addSubTreeModel(new GoProjectMachine());
    transitionPaths.addSubTreeModel(new GoMachineDiagram());

    //transitionPaths.addSubTreeModel(new GoMachineToStartState());
    GoFilteredChildren machineToFinalState =
      new GoFilteredChildren("misc.state-machine.final-states",
			     new GoMachineToState(),
			     PredIsFinalState.TheInstance);
    GoFilteredChildren machineToInitialState =
      new GoFilteredChildren("misc.state-machine.initial-states",
			     new GoMachineToState(),
			     PredIsStartState.TheInstance);
    transitionPaths.addSubTreeModel(machineToInitialState);

    GoFilteredChildren compositeToFinalStates =
      new GoFilteredChildren("misc.state.final-substates",
			     new GoStateToSubstate(),
			     PredIsFinalState.TheInstance);
    GoFilteredChildren compositeToInitialStates =
      new GoFilteredChildren("misc.state.initial-substates",
			     new GoStateToSubstate(),
			     PredIsStartState.TheInstance);
    transitionPaths.addSubTreeModel(compositeToInitialStates);

    transitionPaths.addSubTreeModel(new GoStateToDownstream());
//     transitionPaths.addSubTreeModel(new GoStateToOutgoingTrans());
//     transitionPaths.addSubTreeModel(new GoTransToTargetState());

//     useCaseCentric.addSubTreeModel(new GoProjectModel());
//     useCaseCentric.addSubTreeModel(new GoModelToDiagram());
//     useCaseCentric.addSubTreeModel(new GoModelToUseCase());
//     useCaseCentric.addSubTreeModel(new GoModelToActor());

    collabCentric.addSubTreeModel(new GoProjectCollaboration());
    collabCentric.addSubTreeModel(new GoCollaborationDiagram());
    collabCentric.addSubTreeModel(new GoModelToElements());
    collabCentric.addSubTreeModel(new GoAssocRoleMessages());
    collabCentric.addSubTreeModel(new GoCollaborationInteraction());
    collabCentric.addSubTreeModel(new GoInteractionMessages());

    depCentric.addSubTreeModel(new GoProjectModel());
    depCentric.addSubTreeModel(new GoModelToDiagram());
    depCentric.addSubTreeModel(new GoModelToElements());
    depCentric.addSubTreeModel(new GoElement2DependentElement());

    useCaseToExtensionPoint.addSubTreeModel(new GoUseCaseToExtensionPoint());

    classToBehStr.addSubTreeModel(new GoClassifierToStr());
    classToBehStr.addSubTreeModel(new GoClassifierToBeh());

    classToBeh.addSubTreeModel(new GoClassifierToBeh());

    classToStr.addSubTreeModel(new GoClassifierToStr());

    machineToState.addSubTreeModel(new GoMachineToState());

    machineToTransition.addSubTreeModel(new GoMachineToTrans());

    registerPerspective(packageCentric);
    registerPerspective(diagramCentric);
    registerPerspective(inheritanceCentric);
    registerPerspective(classAssociation);
    registerPerspective(navAssociation);
    registerPerspective(associationCentric);
//     registerPerspective(classStates);
    registerPerspective(stateCentric);
    registerPerspective(transitionCentric);
    registerPerspective(transitionPaths);
    //registerPerspective(useCaseCentric);
    registerPerspective(collabCentric);
    registerPerspective(depCentric);

    registerRule(new GoProjectModel());
    registerRule(new GoModelToDiagram());
    registerRule(new GoModelToElements());
    registerRule(new GoModelToClass());
    registerRule(new GoModelToAssociation());
    registerRule(new GoModelToBaseElements());
    registerRule(new GoProjectDiagram());
    registerRule(new GoUseCaseToExtensionPoint());
    registerRule(new GoClassifierToBeh());
    registerRule(new GoClassifierToStr());
    registerRule(new GoDiagramToNode());
    registerRule(new GoDiagramToEdge());
    registerRule(new GoGenElementToDerived());
    registerRule(new GoClassToAssocdClass());
    registerRule(new GoClassToAggrClass());
    registerRule(new GoClassToCompositeClass());
    registerRule(new GoMachineToTrans());
    registerRule(new GoMachineToState());
    registerRule(machineToInitialState);
    registerRule(machineToFinalState);
    registerRule(new GoStateToSubstate());
    registerRule(compositeToInitialStates);
    registerRule(compositeToFinalStates);
    registerRule(new GoStateToIncomingTrans());
    registerRule(new GoStateToOutgoingTrans());
    registerRule(new GoStateToDownstream());
    registerRule(new GoStateToUpstream());
    registerRule(new GoTransToSourceState());
    registerRule(new GoTransToTargetState());
    registerRule(new GoModelToActor());
    registerRule(new GoModelToUseCase());
    registerRule(new GoAssocToTarget());
    registerRule(new GoAssocToSource());
    registerRule(new GoChildGenerator(new ChildGenDMElements(), 
				      "ChildGenDMElements"));
    registerRule(new GoChildGenerator(new ChildGenFind(), "ChildGenFind"));
    registerRule(new GoChildGenerator(new ChildGenRelated(),
				      "ChildGenRelated"));
    registerRule(new GoChildGenerator(new ChildGenUML(), "ChildGenUML"));
    // needs-more-work: this list is not updated
  }

  ////////////////////////////////////////////////////////////////
  // static methods

  public static void registerPerspective(NavPerspective np) {
    _registeredPerspectives.addElement(np);
  }

  public static void unregisterPerspective(NavPerspective np) {
    _registeredPerspectives.removeElement(np);
  }

  public static Vector getRegisteredPerspectives() {
    return _registeredPerspectives;
  }

  public static void registerRule(AbstractGoRule rule) {
    _rules.addElement(rule);
  }

  public static Vector getRegisteredRules() { return _rules; }

  ////////////////////////////////////////////////////////////////
  // constructor

  public NavPerspective(String name) { super(name); }

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  ////////////////////////////////////////////////////////////////
  // TreeModel implementation


  /**
   * Messaged when the user has altered the value for the item identified
   * by <I>path</I> to <I>newValue</I>.  If <I>newValue</I> signifies
   * a truly new value the model should post a treeNodesChanged
   * event.
   *
   * @param path path to the node that the user has altered.
   * @param newValue the new value from the TreeCellEditor.
   */
  public void valueForPathChanged(TreePath path, Object newValue) {
  }


  //
  //  Change Events
  //

  /*
   * Notify all listeners that have registered interest for
   * notification on this event type.  The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  protected void fireTreeNodesChanged(Object source, Object[] path,
				      int[] childIndices,
				      Object[] children) {
    // Guaranteed to return a non-null array
    Object[] listeners = _listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==TreeModelListener.class) {
	// Lazily create the event:
	if (e == null)
	  e = new TreeModelEvent(source, path,
				 childIndices, children);
	((TreeModelListener)listeners[i+1]).treeNodesChanged(e);
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
  protected void fireTreeNodesInserted(Object source, Object[] path,
				       int[] childIndices,
				       Object[] children) {
    // Guaranteed to return a non-null array
    Object[] listeners = _listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==TreeModelListener.class) {
	// Lazily create the event:
	if (e == null)
	  e = new TreeModelEvent(source, path,
				 childIndices, children);
	((TreeModelListener)listeners[i+1]).treeNodesInserted(e);
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
  protected void fireTreeNodesRemoved(Object source, Object[] path,
				      int[] childIndices,
				      Object[] children) {
    // Guaranteed to return a non-null array
    Object[] listeners = _listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==TreeModelListener.class) {
	// Lazily create the event:
	if (e == null)
	  e = new TreeModelEvent(source, path,
				 childIndices, children);
	((TreeModelListener)listeners[i+1]).treeNodesRemoved(e);
      }
    }
  }


  protected void fireTreeStructureChanged(Object source, Object[] path) {
    fireTreeStructureChanged(source, path, null, null);
  }

  /*
   * Notify all listeners that have registered interest for
   * notification on this event type.  The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  public void fireTreeStructureChanged(Object source, Object[] path,
					  int[] childIndices,
					  Object[] children) {
    // Guaranteed to return a non-null array
    Object[] listeners = _listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==TreeModelListener.class) {
	// Lazily create the event:
	if (e == null)
	  e = new TreeModelEvent(source, path,
				 childIndices, children);
	((TreeModelListener)listeners[i+1]).treeStructureChanged(e);
      }
    }
  }


  public void addTreeModelListener(TreeModelListener l) {
    _listenerList.add(TreeModelListener.class, l);
  }

  public void removeTreeModelListener(TreeModelListener l) {
    _listenerList.remove(TreeModelListener.class, l);
  }

} /* end class NavPerspective */
