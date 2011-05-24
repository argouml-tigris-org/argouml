/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    thn
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2005-2006 The Regents of the University of California. All
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

package org.argouml.model.mdr;

import java.util.ArrayList;
import java.util.Collection;

import javax.jmi.model.PrimitiveType;

import org.argouml.model.MetaTypes;
import org.argouml.model.NotImplementedException;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.activitygraphs.CallState;
import org.omg.uml.behavioralelements.activitygraphs.ClassifierInState;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;
import org.omg.uml.behavioralelements.activitygraphs.Partition;
import org.omg.uml.behavioralelements.activitygraphs.SubactivityState;
import org.omg.uml.behavioralelements.collaborations.AssociationEndRole;
import org.omg.uml.behavioralelements.collaborations.AssociationRole;
import org.omg.uml.behavioralelements.collaborations.ClassifierRole;
import org.omg.uml.behavioralelements.collaborations.Collaboration;
import org.omg.uml.behavioralelements.collaborations.CollaborationInstanceSet;
import org.omg.uml.behavioralelements.collaborations.Interaction;
import org.omg.uml.behavioralelements.collaborations.InteractionInstanceSet;
import org.omg.uml.behavioralelements.collaborations.Message;
import org.omg.uml.behavioralelements.commonbehavior.Action;
import org.omg.uml.behavioralelements.commonbehavior.ActionSequence;
import org.omg.uml.behavioralelements.commonbehavior.Argument;
import org.omg.uml.behavioralelements.commonbehavior.AttributeLink;
import org.omg.uml.behavioralelements.commonbehavior.CallAction;
import org.omg.uml.behavioralelements.commonbehavior.ComponentInstance;
import org.omg.uml.behavioralelements.commonbehavior.CreateAction;
import org.omg.uml.behavioralelements.commonbehavior.DataValue;
import org.omg.uml.behavioralelements.commonbehavior.DestroyAction;
import org.omg.uml.behavioralelements.commonbehavior.Instance;
import org.omg.uml.behavioralelements.commonbehavior.Link;
import org.omg.uml.behavioralelements.commonbehavior.LinkEnd;
import org.omg.uml.behavioralelements.commonbehavior.LinkObject;
import org.omg.uml.behavioralelements.commonbehavior.NodeInstance;
import org.omg.uml.behavioralelements.commonbehavior.Reception;
import org.omg.uml.behavioralelements.commonbehavior.ReturnAction;
import org.omg.uml.behavioralelements.commonbehavior.SendAction;
import org.omg.uml.behavioralelements.commonbehavior.Signal;
import org.omg.uml.behavioralelements.commonbehavior.Stimulus;
import org.omg.uml.behavioralelements.commonbehavior.SubsystemInstance;
import org.omg.uml.behavioralelements.commonbehavior.TerminateAction;
import org.omg.uml.behavioralelements.commonbehavior.UmlException;
import org.omg.uml.behavioralelements.commonbehavior.UninterpretedAction;
import org.omg.uml.behavioralelements.statemachines.CallEvent;
import org.omg.uml.behavioralelements.statemachines.ChangeEvent;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.Event;
import org.omg.uml.behavioralelements.statemachines.FinalState;
import org.omg.uml.behavioralelements.statemachines.Guard;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.statemachines.SignalEvent;
import org.omg.uml.behavioralelements.statemachines.SimpleState;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.statemachines.StateVertex;
import org.omg.uml.behavioralelements.statemachines.StubState;
import org.omg.uml.behavioralelements.statemachines.SubmachineState;
import org.omg.uml.behavioralelements.statemachines.SynchState;
import org.omg.uml.behavioralelements.statemachines.TimeEvent;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.behavioralelements.usecases.Actor;
import org.omg.uml.behavioralelements.usecases.Extend;
import org.omg.uml.behavioralelements.usecases.ExtensionPoint;
import org.omg.uml.behavioralelements.usecases.Include;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.behavioralelements.usecases.UseCaseInstance;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.Artifact;
import org.omg.uml.foundation.core.AssociationClass;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.BehavioralFeature;
import org.omg.uml.foundation.core.Binding;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Comment;
import org.omg.uml.foundation.core.Component;
import org.omg.uml.foundation.core.Constraint;
import org.omg.uml.foundation.core.DataType;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.Element;
import org.omg.uml.foundation.core.ElementResidence;
import org.omg.uml.foundation.core.Enumeration;
import org.omg.uml.foundation.core.EnumerationLiteral;
import org.omg.uml.foundation.core.Feature;
import org.omg.uml.foundation.core.Flow;
import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.Interface;
import org.omg.uml.foundation.core.Method;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.Node;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.core.Permission;
import org.omg.uml.foundation.core.PresentationElement;
import org.omg.uml.foundation.core.ProgrammingLanguageDataType;
import org.omg.uml.foundation.core.Relationship;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.StructuralFeature;
import org.omg.uml.foundation.core.TagDefinition;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.core.TemplateArgument;
import org.omg.uml.foundation.core.TemplateParameter;
import org.omg.uml.foundation.core.UmlAssociation;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.core.Usage;
import org.omg.uml.foundation.datatypes.ActionExpression;
import org.omg.uml.foundation.datatypes.AggregationKind;
import org.omg.uml.foundation.datatypes.ArgListsExpression;
import org.omg.uml.foundation.datatypes.BooleanExpression;
import org.omg.uml.foundation.datatypes.CallConcurrencyKind;
import org.omg.uml.foundation.datatypes.Expression;
import org.omg.uml.foundation.datatypes.IterationExpression;
import org.omg.uml.foundation.datatypes.MappingExpression;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.ObjectSetExpression;
import org.omg.uml.foundation.datatypes.ParameterDirectionKind;
import org.omg.uml.foundation.datatypes.ProcedureExpression;
import org.omg.uml.foundation.datatypes.PseudostateKind;
import org.omg.uml.foundation.datatypes.ScopeKind;
import org.omg.uml.foundation.datatypes.TimeExpression;
import org.omg.uml.foundation.datatypes.TypeExpression;
import org.omg.uml.foundation.datatypes.VisibilityKind;
import org.omg.uml.modelmanagement.ElementImport;
import org.omg.uml.modelmanagement.Model;
import org.omg.uml.modelmanagement.Subsystem;
import org.omg.uml.modelmanagement.UmlPackage;

/**
 * This class contains methods to retrieve objects that represent the different
 * UML types. These objects are use as tokens used to create model elements in
 * some methods.
 * 
 * @see org.argouml.model.UmlFactory#buildConnection( Object, Object, Object,
 *      Object, Object, Object, Object)
 * @see org.argouml.model.UmlFactory#buildNode(Object)
 */
final class MetaTypesMDRImpl implements MetaTypes {

    /**
     * Constructor that forbids to instantiate.
     */
    MetaTypesMDRImpl() {
        // forbid instantiation
    }
    
    private static final Collection<Class<?>> allMetaTypes;
    static {
        // TODO: Bob says - how can we auto-generate this list?
        allMetaTypes = new ArrayList<Class<?>>(125);
        allMetaTypes.add(Abstraction.class);
        allMetaTypes.add(ActionSequence.class);
        allMetaTypes.add(ActionState.class);
        allMetaTypes.add(Actor.class);
        allMetaTypes.add(ArgListsExpression.class);
        allMetaTypes.add(Argument.class);
        allMetaTypes.add(Artifact.class);
        allMetaTypes.add(AssociationEnd.class);
        allMetaTypes.add(AssociationEndRole.class);
        allMetaTypes.add(AssociationRole.class);
        allMetaTypes.add(AttributeLink.class);
        allMetaTypes.add(Expression.class);
        allMetaTypes.add(BooleanExpression.class);
        allMetaTypes.add(Multiplicity.class);
        allMetaTypes.add(MultiplicityRange.class);
        allMetaTypes.add(TypeExpression.class);
        allMetaTypes.add(MappingExpression.class);
        allMetaTypes.add(ProcedureExpression.class);
        allMetaTypes.add(ObjectSetExpression.class);
        allMetaTypes.add(ActionExpression.class);
        allMetaTypes.add(IterationExpression.class);
        allMetaTypes.add(TimeExpression.class);
        allMetaTypes.add(Element.class);
        allMetaTypes.add(ModelElement.class);
        allMetaTypes.add(GeneralizableElement.class);
        allMetaTypes.add(Namespace.class);
        allMetaTypes.add(Classifier.class);
        allMetaTypes.add(UmlClass.class);
        allMetaTypes.add(DataType.class);
        allMetaTypes.add(Feature.class);
        allMetaTypes.add(StructuralFeature.class);
        allMetaTypes.add(Interface.class);
        allMetaTypes.add(Constraint.class);
        allMetaTypes.add(Relationship.class);
        allMetaTypes.add(UmlAssociation.class);
        allMetaTypes.add(Attribute.class);
        allMetaTypes.add(BehavioralFeature.class);
        allMetaTypes.add(Operation.class);
        allMetaTypes.add(Parameter.class);
        allMetaTypes.add(Method.class);
        allMetaTypes.add(Generalization.class);
        allMetaTypes.add(AssociationClass.class);
        allMetaTypes.add(Dependency.class);
        allMetaTypes.add(PresentationElement.class);
        allMetaTypes.add(Usage.class);
        allMetaTypes.add(Binding.class);
        allMetaTypes.add(Component.class);
        allMetaTypes.add(Node.class);
        allMetaTypes.add(Permission.class);
        allMetaTypes.add(Comment.class);
        allMetaTypes.add(Flow.class);
        allMetaTypes.add(ElementResidence.class);
        allMetaTypes.add(Enumeration.class);
        allMetaTypes.add(EnumerationLiteral.class);
        allMetaTypes.add(Stereotype.class);
        allMetaTypes.add(TagDefinition.class);
        allMetaTypes.add(TaggedValue.class);
        allMetaTypes.add(ProgrammingLanguageDataType.class);
        allMetaTypes.add(TemplateArgument.class);
        allMetaTypes.add(Instance.class);
        allMetaTypes.add(Signal.class);
        allMetaTypes.add(Action.class);
        allMetaTypes.add(CreateAction.class);
        allMetaTypes.add(DestroyAction.class);
        allMetaTypes.add(UninterpretedAction.class);
        allMetaTypes.add(Object.class);
        allMetaTypes.add(Link.class);
        allMetaTypes.add(LinkObject.class);
        allMetaTypes.add(DataValue.class);
        allMetaTypes.add(CallAction.class);
        allMetaTypes.add(SendAction.class);
        allMetaTypes.add(Reception.class);
        allMetaTypes.add(LinkEnd.class);
        allMetaTypes.add(ReturnAction.class);
        allMetaTypes.add(TerminateAction.class);
        allMetaTypes.add(Stimulus.class);
        allMetaTypes.add(UmlException.class);
        allMetaTypes.add(ComponentInstance.class);
        allMetaTypes.add(NodeInstance.class);
        allMetaTypes.add(SubsystemInstance.class);
        allMetaTypes.add(UseCase.class);
        allMetaTypes.add(UseCaseInstance.class);
        allMetaTypes.add(Extend.class);
        allMetaTypes.add(Include.class);
        allMetaTypes.add(ExtensionPoint.class);
        allMetaTypes.add(StateMachine.class);
        allMetaTypes.add(Event.class);
        allMetaTypes.add(StateVertex.class);
        allMetaTypes.add(State.class);
        allMetaTypes.add(TimeEvent.class);
        allMetaTypes.add(CallEvent.class);
        allMetaTypes.add(SignalEvent.class);
        allMetaTypes.add(Transition.class);
        allMetaTypes.add(CompositeState.class);
        allMetaTypes.add(ChangeEvent.class);
        allMetaTypes.add(Guard.class);
        allMetaTypes.add(Pseudostate.class);
        allMetaTypes.add(SimpleState.class);
        allMetaTypes.add(SubmachineState.class);
        allMetaTypes.add(SynchState.class);
        allMetaTypes.add(StubState.class);
        allMetaTypes.add(FinalState.class);
        allMetaTypes.add(Collaboration.class);
        allMetaTypes.add(ClassifierRole.class);
        allMetaTypes.add(Message.class);
        allMetaTypes.add(Interaction.class);
        allMetaTypes.add(InteractionInstanceSet.class);
        allMetaTypes.add(CollaborationInstanceSet.class);
        allMetaTypes.add(ActivityGraph.class);
        allMetaTypes.add(Partition.class);
        allMetaTypes.add(SubactivityState.class);
        allMetaTypes.add(CallState.class);
        allMetaTypes.add(ObjectFlowState.class);
        allMetaTypes.add(ClassifierInState.class);
        allMetaTypes.add(UmlPackage.class);
        allMetaTypes.add(Model.class);
        allMetaTypes.add(Subsystem.class);
        allMetaTypes.add(ElementImport.class);
    };
    
    public Collection getAllMetaTypes() {
        return allMetaTypes;
    }
    
    public String getName(Object modelElement) {
        Class<?> clazz;
        if (modelElement instanceof Class) {
            clazz = (Class<?>) modelElement;
        } else {
            clazz = modelElement.getClass();
        }
        String name = clazz.getName();

        // The name of the meta type is the class name (after the last .)
        // and before the next $ or end of class name.
        int startName = name.lastIndexOf('.') + 1;

        // MDR classes may have a UML or Uml prefix which should be removed.
        if (name.regionMatches(true, startName, "UML", 0, 3)) {
            startName += 3;
        }

        int endName = name.indexOf('$', startName);
        if (endName < 0) {
            endName = name.length();
        }

        return name.substring(startName, endName);
    }

    /*
     * @see org.argouml.model.MetaTypes#getAbstraction()
     */
    public Object getAbstraction() {
        return Abstraction.class;
    }

    public Object getAcceptEventAction() {
        throw new NotImplementedException("Not a UML 1.4 element");
    }

    public Object getAction() {
        return Action.class;
    }

    public Object getActionExpression() {
        return ActionExpression.class;
    }

    public Object getActionState() {
        return ActionState.class;
    }

    public Object getActivity() {
        return ActivityGraph.class;
    }

    public Object getActivityParameterNode() {
        throw new NotImplementedException("This is not a UML1.4 class");
    }

    public Object getActor() {
        return Actor.class;
    }

    public Object getAggregationKind() {
        return AggregationKind.class;
    }
    
    public Object getArtifact() {
        return Artifact.class;
    }

    public Object getAssociation() {
        return UmlAssociation.class;
    }

    public Object getAssociationClass() {
        return AssociationClass.class;
    }

    public Object getAssociationEnd() {
        return AssociationEnd.class;
    }

    public Object getAssociationEndRole() {
        return AssociationEndRole.class;
    }

    public Object getAssociationRole() {
        return AssociationRole.class;
    }

    public Object getAttribute() {
        return Attribute.class;
    }

    public Object getBehavioralFeature() {
        return BehavioralFeature.class;    
    }
    
    public Object getBinding() {
        return Binding.class;
    }

    public Object getBooleanExpression() {
        return BooleanExpression.class;
    }

    public Object getCallAction() {
        return CallAction.class;
    }

    public Object getCallBehaviorAction() {
        throw new NotImplementedException("This is not a UML1.4 class");
    }

    public Object getCallConcurrencyKind() {
        return CallConcurrencyKind.class;
    }

    public Object getCallState() {
        return CallState.class;
    }

    public Object getCentralBufferNode() {
        throw new NotImplementedException("This is not a UML1.4 class");
    }

    public Object getUMLClass() {
        return UmlClass.class;
    }

    public Object getClassifier() {
        return Classifier.class;
    }

    public Object getClassifierRole() {
        return ClassifierRole.class;
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
        return ComponentInstance.class;
    }

    public Object getComponentRealization() {
        throw new NotImplementedException("This is not a UML1.4 class");
    }

    public Object getCompositeState() {
        return CompositeState.class;
    }
    
    public Object getConnector() {
        throw new NotImplementedException("This is not a UML1.4 class");
    }

    public Object getConstraint() {
        return Constraint.class;
    }

    public Object getControlFlow() {
        throw new NotImplementedException("This is not a UML1.4 class");
    }

    public Object getCreateAction() {
        return CreateAction.class;
    }

    public Object getCreateObjectAction() {
        throw new NotImplementedException("This is not a UML1.4 class");
    }
    
    public Object getDataStoreNode() {
        throw new NotImplementedException("This is not a UML1.4 class");
    }

    public Object getDataType() {
        return DataType.class;
    }

    public Object getDependency() {
        return Dependency.class;
    }

    public Object getDestroyAction() {
        return DestroyAction.class;
    }

    public Object getDestroyObjectAction() {
        throw new NotImplementedException("This is not a UML1.4 class");
    }

    public Object getEnumeration() {
        return Enumeration.class;
    }
    
    public Object getEnumerationLiteral() {
        return EnumerationLiteral.class;
    }

    public Object getElementImport() {
        return ElementImport.class;
    }
    
    public Object getEvent() {
        return Event.class;
    }

    public Object getException() {
        return UmlException.class;
    }

    public Object getExpansionNode() {
        throw new NotImplementedException("This is not a UML1.4 class");
    }

    public Object getExtend() {
        return Extend.class;
    }

    public Object getExtensionPoint() {
        return ExtensionPoint.class;
    }

    public Object getFinalState() {
        return FinalState.class;
    }

    public Object getGeneralizableElement() {
        return GeneralizableElement.class;
    }

    public Object getGeneralization() {
        return Generalization.class;
    }

    public Object getGuard() {
        return Guard.class;
    }

    public Object getInclude() {
        return Include.class;
    }

    public Object getInputPin() {
        throw new NotImplementedException("Not a UML1.4 element");
    }

    public Object getInstance() {
        return Instance.class;
    }

    public Object getInstanceSpecification() {
        throw new NotImplementedException("Not a UML1.4 element");
    }

    public Object getInterface() {
        return Interface.class;
    }

    public Object getLifeline() {
        return ClassifierRole.class;
    }
    
    public Object getLink() {
        return Link.class;
    }

    public Object getMessage() {
        return Message.class;
    }

    public Object getModel() {
        return Model.class;
    }

    public Object getModelElement() {
        return ModelElement.class;
    }

    public Object getMultiplicity() {
        return Multiplicity.class;
    }

    public Object getNamespace() {
        return Namespace.class;
    }

    public Object getNode() {
        return Node.class;
    }

    public Object getNodeInstance() {
        return NodeInstance.class;
    }

    public Object getObject() {
        return org.omg.uml.behavioralelements.commonbehavior.Object.class;
    }

    public Object getObjectFlow() {
        throw new NotImplementedException("This is not a UML1.4 class");
    }

    public Object getObjectFlowState() {
        return ObjectFlowState.class;
    }

    public Object getOperation() {
        return Operation.class;
    }

    public Object getOpaqueExpression() {
        throw new NotImplementedException("Not a UML 1.4 element");
    }

    public Object getOutputPin() {
        throw new NotImplementedException("Not a UML1.4 element");
    }

    public Object getPackage() {
        return UmlPackage.class;
    }

    public Object getParameter() {
        return Parameter.class;
    }

    public Object getParameterDirectionKind() {
        return ParameterDirectionKind.class;
    }

    public Object getPartition() {
        return Partition.class;
    }

    public Object getPackageImport() {
        return Permission.class;
    }

    public Object getPort() {
        throw new NotImplementedException("Not a UML1.4 element");
    }

    public Object getProfile() {
        return Package.class;
    }

    public Object getProperty() {
        return Attribute.class;
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

    public Object getRegion() {
        throw new NotImplementedException("Not a UML 1.4 element");
    }

    public Object getReturnAction() {
        return ReturnAction.class;
    }

    public Object getScopeKind() {
        return ScopeKind.class;
    }

    public Object getSendAction() {
        return SendAction.class;
    }

    public Object getSendSignalAction() {
        throw new NotImplementedException("Not a UML 1.4 element");
    }

    public Object getSignal() {
        return Signal.class;
    }

    public Object getSimpleState() {
        return SimpleState.class;
    }

    public Object getState() {
        return State.class;
    }

    public Object getStateMachine() {
        return StateMachine.class;
    }

    public Object getStateVertex() {
        return StateVertex.class;
    }

    public Object getStereotype() {
        return Stereotype.class;
    }

    public Object getStimulus() {
        return Stimulus.class;
    }

    public Object getStubState() {
        return StubState.class;
    }

    public Object getSubactivityState() {
        return SubactivityState.class;
    }

    public Object getSubmachineState() {
        return SubmachineState.class;
    }

    public Object getSubsystem() {
        return Subsystem.class;
    }

    public Object getSynchState() {
        return SynchState.class;
    }
    
    public Object getTemplateArgument() {
        return TemplateArgument.class;
    }
    
    public Object getTemplateParameter() {
        return TemplateParameter.class;
    }

    public Object getTerminateAction() {
        return TerminateAction.class;
    }

    public Object getTransition() {
        return Transition.class;
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

    public Object getTagDefinition() {
        return TagDefinition.class;
    }

    public Object getTaggedValue() {
        return TaggedValue.class;
    }

    public Object getInteraction() {
        return Interaction.class;
    }

    public Object getActionSequence() {
        return ActionSequence.class;
    }

    public Object getArgument() {
        return Argument.class;
    }

    public Object getAttributeLink() {
        return AttributeLink.class;
    }

    public Object getCallEvent() {
        return CallEvent.class;
    }

    public Object getChangeEvent() {
        return ChangeEvent.class;
    }

    public Object getClassifierInState() {
        return ClassifierInState.class;
    }

    public Object getCollaborationInstanceSet() {
        return CollaborationInstanceSet.class;
    }

    public Object getDataValue() {
        return DataValue.class;
    }

    public Object getElement() {
        return Element.class;
    }

    public Object getElementResidence() {
        return ElementResidence.class;
    }

    public Object getExpression() {
        return Expression.class;
    }

    public Object getFeature() {
        return Feature.class;
    }

    public Object getFlow() {
        return Flow.class;
    }

    public Object getInteractionInstanceSet() {
        return InteractionInstanceSet.class;
    }

    public Object getLinkEnd() {
        return LinkEnd.class;
    }

    public Object getLinkObject() {
        return LinkObject.class;
    }

    public Object getLiteralBoolean() {
        throw new NotImplementedException("Not a UML 1.4 element");
    }

    public Object getLiteralInteger() {
        throw new NotImplementedException("Not a UML 1.4 element");
    }

    public Object getLiteralString() {
        throw new NotImplementedException("Not a UML 1.4 element");
    }

    public Object getMethod() {
        return Method.class;
    }

    public Object getMultiplicityRange() {
        return MultiplicityRange.class;
    }

    public Object getPrimitiveType() {
        return PrimitiveType.class;
    }

    public Object getRelationship() {
        return Relationship.class;
    }

    public Object getSignalEvent() {
        return SignalEvent.class;
    }

    public Object getStructuralFeature() {
        return StructuralFeature.class;
    }

    public Object getSubsystemInstance() {
        return SubsystemInstance.class;
    }

    public Object getTimeEvent() {
        return TimeEvent.class;
    }

    public Object getUninterpretedAction() {
        return UninterpretedAction.class;
    }


}
