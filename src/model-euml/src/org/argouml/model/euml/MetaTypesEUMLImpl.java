// $Id$
// Copyright (c) 2007, The ArgoUML Project
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above copyright
//       notice, this list of conditions and the following disclaimer in the
//       documentation and/or other materials provided with the distribution.
//     * Neither the name of the ArgoUML Project nor the
//       names of its contributors may be used to endorse or promote products
//       derived from this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE ArgoUML PROJECT ``AS IS'' AND ANY
// EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE ArgoUML PROJECT BE LIABLE FOR ANY
// DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.argouml.model.euml;

import org.argouml.model.MetaTypes;
import org.argouml.model.NotImplementedException;
import org.eclipse.uml2.uml.Abstraction;
import org.eclipse.uml2.uml.Action;
import org.eclipse.uml2.uml.ActivityPartition;
import org.eclipse.uml2.uml.Actor;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.BehavioralFeature;
import org.eclipse.uml2.uml.CallAction;
import org.eclipse.uml2.uml.CallConcurrencyKind;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Collaboration;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Extend;
import org.eclipse.uml2.uml.FinalState;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Include;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.ObjectNode;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Reception;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.UseCase;
import org.eclipse.uml2.uml.VisibilityKind;


/**
 * The implementation of the MetaTypes for EUML2.
 */
final class MetaTypesEUMLImpl implements MetaTypes {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     * 
     * @param implementation
     *            The ModelImplementation.
     */
    public MetaTypesEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public Object getAbstraction() {
        return Abstraction.class;
    }

    public Object getAction() {
        return Action.class;
    }

    public Object getActionExpression() {
        // TODO Auto-generated method stub
        throw new NotYetImplementedException();
    }

    public Object getActionState() {
        // TODO: ActionState, CallState, and SubactivityState have been
        // replaced in UML 2.0 by explicitly modeled Actions
        return State.class;
    }

    public Object getActor() {
        return Actor.class;
    }

    public Object getAggregationKind() {
        return AggregationKind.class;
    }

    public Object getAssociation() {
        return Association.class;
    }

    public Object getAssociationClass() {
        return AssociationClass.class;
    }

    public Object getAssociationEnd() {
        // an AssociationEnd is now a Property owned by an Association
        return Property.class;
    }

    public Object getAssociationEndRole() {
        // TODO: In UML 2.0, ClassifierRole, AssociationRole, and
        // AssociationEndRole have been replaced by the internal 
        // structure of the Collaboration
        return Classifier.class;
    }

    public Object getAssociationRole() {
        // TODO: In UML 2.0, ClassifierRole, AssociationRole, and
        // AssociationEndRole have been replaced by the internal 
        // structure of the Collaboration
        return Classifier.class;
    }

    public Object getAttribute() {
        return Property.class;
    }

    public Object getBehavioralFeature() {
        return BehavioralFeature.class;
    }

    public Object getBooleanExpression() {
        // TODO Auto-generated method stub
        throw new NotYetImplementedException();
    }

    public Object getCallAction() {
        return CallAction.class;
    }

    public Object getCallConcurrencyKind() {
        return CallConcurrencyKind.class;
    }

    public Object getCallState() {
        // TODO: ActionState, CallState, and SubactivityState have been replaced
        // in UML 2.0 by explicitly modeled Actions
        return State.class;
    }

    public Object getClassifier() {
        return Classifier.class;
    }

    public Object getClassifierRole() {
        // TODO: In UML 2.0, ClassifierRole, AssociationRole, and
        // AssociationEndRole have been replaced by the internal 
        // structure of the Collaboration
        return Classifier.class;
    }

    public Object getCollaboration() {
        return Collaboration.class;
    }

    public Object getComment() {
        return Comment.class;
    }

    public Object getComponent() {
        return Component.class;
    }

    public Object getComponentInstance() {
        // TODO: Gone in UML 2.x
        // return place holder for now
        return InstanceSpecification.class;
    }

    public Object getCompositeState() {
        // TODO: no separate CompositeState in UML 2.1 - tfm
        return State.class;
    }

    public Object getCreateAction() {
        // TODO Auto-generated method stub
        throw new NotYetImplementedException();
    }

    public Object getDataType() {
        return DataType.class;
    }

    public Object getDependency() {
        return Dependency.class;
    }

    public Object getDestroyAction() {
        // TODO Auto-generated method stub
        throw new NotYetImplementedException();
    }

    public Object getEnumeration() {
        return Enumeration.class;
    }

    public Object getEnumerationLiteral() {
        return EnumerationLiteral.class;
    }

    public Object getEvent() {
        // TODO Auto-generated method stub
        throw new NotYetImplementedException();
    }

    public Object getException() {
        // TODO: Exception has been removed for UML 2.x
        // just return Signal for now - tfm
        return Signal.class;
    }

    public Object getExtend() {
        return Extend.class;
    }

    public Object getFinalState() {
        return FinalState.class;
    }

    public Object getGeneralizableElement() {
        // Gone in UML 2.x - just Classifier now
        return Classifier.class;
    }

    public Object getGeneralization() {
        return Generalization.class;
    }

    public Object getGuard() {
        // TODO Auto-generated method stub
        throw new NotYetImplementedException();
    }

    public Object getInclude() {
        return Include.class;
    }

    public Object getInstance() {
        // TODO: Check for changed semantics - tfm
        return InstanceSpecification.class;
    }

    public Object getInteraction() {
        return Interaction.class;
    }

    public Object getInterface() {
        return Interface.class;
    }

    public Object getLink() {
        // TODO: Gone in UML 2.1
        // It is now an InstanceSpecification with an 
        // Association as its classifier
        return InstanceSpecification.class;
    }

    public Object getMessage() {
        // TODO Auto-generated method stub
        throw new NotYetImplementedException();
    }

    public Object getModel() {
        return Model.class;
    }

    public Object getModelElement() {
        return Element.class;
    }

    public Object getMultiplicity() {
        return MultiplicityElement.class;
    }

    public String getName(Object element) {
        Class clazz;
        if (element instanceof Class) {
            clazz = (Class) element;
        } else {
            clazz = element.getClass();
        }
        String name = clazz.getName();

        // The name of the meta type is the class name (after the last .)
        // and before the next $ or end of class name.
        int startName = name.lastIndexOf('.') + 1;

        // Eclipse UML2 implementation classes end with "Impl"
        final String suffix = "Impl"; //$NON-NLS-1$
        int endName = name.length();
        if (name.endsWith(suffix)) {
            endName -= suffix.length();
        }

        return name.substring(startName, endName);
    }

    public Object getNamespace() {
        return Namespace.class;
    }

    public Object getNode() {
        return Node.class;
    }

    public Object getNodeInstance() {
        // TODO: Gone in UML 2.x
        // Return place holder for now
        return InstanceSpecification.class;
    }

    public Object getObject() {
        throw new NotImplementedException();
        // TODO: what to use: InstanceSpecification, ObjectNode, etc?
//        return org.eclipse.uml2.uml.ObjectNode.class;
    }

    public Object getObjectFlowState() {
        // TODO: not in UML 2.1
        return ObjectNode.class;
    }

    public Object getOperation() {
        return Operation.class;
    }

    public Object getPackage() {
        return org.eclipse.uml2.uml.Package.class;
    }

    public Object getParameter() {
        return Parameter.class;
    }

    public Object getParameterDirectionKind() {
        return ParameterDirectionKind.class;
    }

    public Object getPartition() {
        return ActivityPartition.class;
    }

    @SuppressWarnings("deprecation")
    public Object getPermission() {
        return PackageImport.class;
    }

    public Object getPackageImport() {
        return PackageImport.class;
    }
    
    public Object getPseudostate() {
        return Pseudostate.class;
    }

    public Object getPseudostateKind() {
        return PseudostateKind.class;
    }

    public Object getReception() {
        return Reception.class;
    }

    public Object getReturnAction() {
        // TODO Auto-generated method stub
        throw new NotYetImplementedException();
    }

    public Object getScopeKind() {
        // Not in UML 2.x - deprecated in Model API
        throw new NotImplementedException();
    }

    public Object getSendAction() {
        // TODO Auto-generated method stub
        throw new NotYetImplementedException();
    }

    public Object getSignal() {
        return Signal.class;
    }

    public Object getSimpleState() {
        // TODO: Gone in UML 2.1
        return State.class;
    }

    public Object getState() {
        return State.class;
    }

    public Object getStateMachine() {
        return StateMachine.class;
    }

    public Object getStateVertex() {
        // TODO: State & Vertex are independent classes in UML 2.1
        return State.class;
    }

    public Object getStereotype() {
        return Stereotype.class;
    }

    public Object getStimulus() {
        // TODO Auto-generated method stub
        throw new NotYetImplementedException();
    }

    public Object getStubState() {
        // TODO: gone in UML 2.1
        return State.class;
    }

    public Object getSubactivityState() {
        // TODO: ActionState, CallState, and SubactivityState have been replaced
        // in UML 2.0 by explicitly modeled Actions
        return State.class;
    }

    public Object getSubmachineState() {
        // TODO: gone in UML 2.1
        return State.class;
    }

    public Object getSubsystem() {
        // Changed in UML 2.1 - Component with <<subsystem>> stereotype
        // TODO: We should deprecate this?
        return Component.class;
    }

    public Object getSynchState() {
        // TODO: no separate SyncState in UML 2.1 - tfm
        return State.class;
    }

    public Object getTagDefinition() {
        // TODO: In UML 2.x a TagDefinition has become a Property on a Stereotype
        // Anything that uses this will probably need to be reviewed/changed.
        // Just return Propety for now.
        return Property.class;
    }

    public Object getTerminateAction() {
        // TODO Auto-generated method stub
        throw new NotYetImplementedException();
    }

    public Object getTransition() {
        return Transition.class;
    }

    public Object getUMLClass() {
        return org.eclipse.uml2.uml.Class.class;
    }

    public Object getUsage() {
        return Usage.class;
    }

    public Object getUseCase() {
        return UseCase.class;
    }

    public Object getVisibilityKind() {
        return VisibilityKind.class;
    }

}
