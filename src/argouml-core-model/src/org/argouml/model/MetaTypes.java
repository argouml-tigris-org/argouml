/* $Id$
 *******************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    thn
 *******************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2005-2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.model;

import java.util.Collection;


/**
 * This interface contains methods to retrieve objects that represent the
 * different UML types. These objects are use as tokens used to create
 * model elements in some methods.
 *
 * TODO: There are parts of ArgoUML which assume that the objects returned
 * here are Classes.  We should formalize that by changing the return types to
 * match. tfm - 20070620 Bob says - there should be no such assumption in
 * ArgoUML. Can we document where these are?
 * 
 * @see org.argouml.model.UmlFactory#buildConnection(
 *         Object, Object, Object, Object, Object, Object, Object)
 * @see org.argouml.model.UmlFactory#buildNode(Object)
 */
public interface MetaTypes {
    
    /**
     * Get all metatypes available in the model implementation
     */
    Collection getAllMetaTypes();
    
    /**
     * Given a Element instance returns the name of its meta type.
     *
     * @return The name of the meta type.
     * @param element The model element.
     */
    String getName(Object element);

    /**
     * @return Returns the Abstraction.
     */
    Object getAbstraction();

    /**
     * @return Returns the SendSignalAction.
     */
    Object getAcceptEventAction();

    /**
     * @return Returns the Action.
     */
    Object getAction();

    /**
     * @return Returns the ActionExpression.
     */
    Object getActionExpression();

    /**
     * @return Returns the ActionState.
     */
    Object getActionState();
    
    /**
     * @return the Activity class for UML2
     */
    Object getActivity();

    /**
     * @return the ActivityParameterNode class for UML2
     */
    Object getActivityParameterNode();

    /**
     * @return Returns the Actor.
     */
    Object getActor();

    /**
     * @return Returns the AggregationKind.
     */
    Object getAggregationKind();
    
    /**
     * @return return Artifact type.
     */
    Object getArtifact();

    /**
     * @return Returns the Association.
     */
    Object getAssociation();

    /**
     * @return Returns the AssociationClass.
     */
    Object getAssociationClass();

    /**
     * @return Returns the AssociationEnd.
     */
    Object getAssociationEnd();

    /**
     * @return Returns the AssociationEndRole.
     */
    Object getAssociationEndRole();

    /**
     * @return Returns the AssociationRole.
     */
    Object getAssociationRole();

    /**
     * @return Returns the Attribute.
     */
    Object getAttribute();

    /**
     * @return Returns the BehavioralFeature.
     */
    Object getBehavioralFeature();

    /**
     * @return the implementation class representing a Binding
     */
    Object getBinding();
    
    /**
     * @return Returns the BooleanExpression.
     */
    Object getBooleanExpression();

    /**
     * @return Returns the CallAction.
     */
    Object getCallAction();

    /**
     * @return Returns the CreateObjectAction.
     */
    Object getCallBehaviorAction();

    /**
     * @return Returns the CallConcurrencyKind.
     */
    Object getCallConcurrencyKind();

    /**
     * @return Returns the CallState.
     */
    Object getCallState();

    /**
     * @return the CentralBufferNode class for UML2
     */
    Object getCentralBufferNode();

    /**
     * Return the UML Class class. This method has a special name because Java
     * already uses the getClass() method.
     * 
     * @return Returns the Class.
     */
    Object getUMLClass();

    /**
     * @return Returns the Classifier.
     */
    Object getClassifier();

    /**
     * @return Returns the ClassifierRole.
     */
    Object getClassifierRole();

    /**
     * @return Returns the Collaboration.
     */
    Object getCollaboration();

    /**
     * @return Returns the Comment.
     */
    Object getComment();

    /**
     * @return Returns the Component.
     */
    Object getComponent();

    /**
     * @return Returns the Component.
     */
    Object getComponentRealization();

    /**
     * @return Returns the ComponentInstance.
     */
    Object getComponentInstance();

    /**
     * @return Returns the CompositeState.
     */
    Object getCompositeState();

    /**
     * @return Return the Constraint type.
     */
    Object getConstraint();

    /**
     * @return Returns the ControlFlow class for UML2
     */
    Object getControlFlow();
    
    /**
     * @return Returns the Connector class for UML2
     */
    Object getConnector();
    
    /**
     * @return Returns the CreateAction.
     */
    Object getCreateAction();

    /**
     * @return Returns the CreateObjectAction.
     */
    Object getCreateObjectAction();

    /**
     * @return Returns the Datatype.
     */
    Object getDataType();

    /**
     * @return Returns the DataStoreNode class for UML2.
     */
    Object getDataStoreNode();

    /**
     * @return Returns the Dependency.
     */
    Object getDependency();

    /**
     * @return Returns the DestroyAction.
     */
    Object getDestroyAction();
    
    /**
     * @return Returns the DestroyObjectAction.
     */
    Object getDestroyObjectAction();
    
    /**
     * @return the ElementImport class.
     */
    Object getElementImport();

    /**
     * @return Returns the Enumeration.
     */
    Object getEnumeration();

    /**
     * @return Returns the EnumerationLiteral.
     */
    Object getEnumerationLiteral();
    
    /**
     * @return Returns the Event.
     */
    Object getEvent();

    /**
     * @return Returns the Exception.
     */
    Object getException();

    /**
     * @return Returns the ExpansionNode class for UML2
     */
    Object getExpansionNode();

    /**
     * @return Returns the Extend.
     */
    Object getExtend();
    
    /**
     * @return Returns the ExtensionPoint type.
     */
    Object getExtensionPoint();
    
    /**
     * @return Returns the FinalState.
     */
    Object getFinalState();

    // Flow is not in UML 2.x - don't implement

    /**
     * @return Returns the GeneralizableElement.
     */
    Object getGeneralizableElement();

    /**
     * @return Returns the Generalization.
     */
    Object getGeneralization();

    /**
     * @return Returns the Guard.
     */
    Object getGuard();

    /**
     * @return Returns the Include.
     */
    Object getInclude();

    /**
     * @return Returns the InputPin.
     */
    Object getInputPin();

    /**
     * @return Returns the Instance.
     */
    Object getInstance();

    /**
     * @return Returns the InstanceSpecification.
     * @since UML 2
     */
    Object getInstanceSpecification();

    /**
     * @return Returns the Interface.
     */
    Object getInterface();

    /**
     * @return Returns the Lifeline.
     */
    Object getLifeline();

    /**
     * @return Returns the Link.
     */
    Object getLink();

    /**
     * @return Returns the Message.
     */
    Object getMessage();

    /**
     * @return Returns the Model.
     */
    Object getModel();

    /**
     * @return Returns the ModelElement.
     */
    Object getModelElement();

    /**
     * @return Returns the Multiplicity.
     */
    Object getMultiplicity();

    /**
     * @return Returns the Namespace.
     */
    Object getNamespace();

    /**
     * @return Returns the Node.
     */
    Object getNode();

    /**
     * @return Returns the NodeInstance.
     */
    Object getNodeInstance();

    /**
     * @return Returns the Object.
     */
    Object getObject();

    /**
     * @return Returns the ObjectFlowState.
     */
    Object getObjectFlowState();

    /**
     * @return Returns the ObjectFlow class for UML2
     */
    Object getObjectFlow();

    /**
     * @return Returns the OpaqueExpression
     */
    Object getOpaqueExpression();

    /**
     * @return Returns the Operation.
     */
    Object getOperation();
    
    /**
     * @return Returns the OutputPin.
     */
    Object getOutputPin();

    /**
     * @return Returns the Package.
     */
    Object getPackage();

    /**
     * @return Returns the Parameter.
     */
    Object getParameter();

    /**
     * @return Returns the ParameterDirectionKind.
     */
    Object getParameterDirectionKind();

    /**
     * @return Returns the Partition.
     */
    Object getPartition();

    /**
     * @return Returns the Port.
     */
    Object getPort();

    /**
     * @return Return the PackageImport type.
     * @since UML 2.0
     */
    Object getPackageImport();

    /**
     * @return Return the Profile type.
     * @since UML 2.0
     */
    Object getProfile();

    /**
     * @return Return the Property type.
     * @since UML 2.0
     */
    Object getProperty();
    
    /**
     * @return Returns the Pseudostate.
     */
    Object getPseudostate();

    /**
     * @return Returns the PseudostateKind.
     */
    Object getPseudostateKind();

    /**
     * @return Returns the Reception.
     */
    Object getReception();

    /**
     * @return Returns the Region.
     */
    Object getRegion();

    /**
     * @return Returns the ReturnAction.
     */
    Object getReturnAction();

    /**
     * @return Returns the ScopeKind.
     */
    Object getScopeKind();

    /**
     * @return Returns the SendAction.
     */
    Object getSendAction();

    /**
     * @return Returns the SendSignalAction.
     */
    Object getSendSignalAction();

    /**
     * @return Returns the Signal.
     */
    Object getSignal();

    /**
     * @return Returns the SimpleState.
     */
    Object getSimpleState();

    /**
     * @return Returns the State.
     */
    Object getState();

    /**
     * @return Returns the StateMachine.
     */
    Object getStateMachine();

    /**
     * @return Returns the StateVertex.
     */
    Object getStateVertex();

    /**
     * @return Returns the Stereotype.
     */
    Object getStereotype();

    /**
     * @return Returns the Stimulus.
     */
    Object getStimulus();

    /**
     * @return Returns the StubState.
     */
    Object getStubState();

    /**
     * @return Returns the SubactivityState.
     */
    Object getSubactivityState();

    /**
     * @return Returns the SubmachineState.
     */
    Object getSubmachineState();

    /**
     * @return Returns the Subsystem.
     */
    Object getSubsystem();

    /**
     * @return Returns the SynchState.
     */
    Object getSynchState();

    /**
     * Return the TagDefinition class.
     * 
     * @since UML 1.4
     * @return Returns the TagDefinition, or null in UML 1.3 Model subsystem
     *         implementations.
     */
    Object getTagDefinition();


    /**
     * Return the TaggedValue class.
     * 
     * @return Returns the TaggedValue class
     */
    Object getTaggedValue();  
    
    /**
     * @return the implementation class representing a TemplateArgument
     */
    public Object getTemplateArgument();
    
    /**
     * @return the implementation class representing a TemplateParameter
     */
    public Object getTemplateParameter();
    
    /**
     * @return Returns the TerminateAction.
     */
    Object getTerminateAction();

    /**
     * @return Returns the Transition.
     */
    Object getTransition();

    /**
     * @return Returns the Usage.
     */
    Object getUsage();

    /**
     * @return Returns the Use Case.
     */
    Object getUseCase();

    /**
     * @return Returns the VisibilityKind.
     */
    Object getVisibilityKind();

    /**
     * @return Returns the Interaction
     */
    Object getInteraction();

    Object getActionSequence();

    Object getArgument();

    Object getAttributeLink();

    Object getCallEvent();

    Object getChangeEvent();

    Object getClassifierInState();

    Object getCollaborationInstanceSet();

    Object getDataValue();

    Object getElement();

    Object getElementResidence();

    Object getExpression();

    Object getFeature();

    Object getFlow();

    Object getInteractionInstanceSet();

    Object getLinkEnd();

    Object getLinkObject();

    Object getLiteralBoolean();

    Object getLiteralInteger();

    Object getLiteralString();

    Object getMethod();

    Object getMultiplicityRange();

    Object getPrimitiveType();

    Object getRelationship();

    Object getSignalEvent();

    Object getStructuralFeature();

    Object getSubsystemInstance();

    Object getTimeEvent();

    Object getUninterpretedAction();

}
