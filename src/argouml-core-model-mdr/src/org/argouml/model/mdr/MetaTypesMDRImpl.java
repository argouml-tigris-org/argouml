// $Id$
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

import org.argouml.model.MetaTypes;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.CallState;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;
import org.omg.uml.behavioralelements.activitygraphs.Partition;
import org.omg.uml.behavioralelements.activitygraphs.SubactivityState;
import org.omg.uml.behavioralelements.collaborations.AssociationEndRole;
import org.omg.uml.behavioralelements.collaborations.AssociationRole;
import org.omg.uml.behavioralelements.collaborations.ClassifierRole;
import org.omg.uml.behavioralelements.collaborations.Collaboration;
import org.omg.uml.behavioralelements.collaborations.Interaction;
import org.omg.uml.behavioralelements.collaborations.Message;
import org.omg.uml.behavioralelements.commonbehavior.Action;
import org.omg.uml.behavioralelements.commonbehavior.CallAction;
import org.omg.uml.behavioralelements.commonbehavior.ComponentInstance;
import org.omg.uml.behavioralelements.commonbehavior.CreateAction;
import org.omg.uml.behavioralelements.commonbehavior.DestroyAction;
import org.omg.uml.behavioralelements.commonbehavior.Instance;
import org.omg.uml.behavioralelements.commonbehavior.Link;
import org.omg.uml.behavioralelements.commonbehavior.NodeInstance;
import org.omg.uml.behavioralelements.commonbehavior.Reception;
import org.omg.uml.behavioralelements.commonbehavior.ReturnAction;
import org.omg.uml.behavioralelements.commonbehavior.SendAction;
import org.omg.uml.behavioralelements.commonbehavior.Signal;
import org.omg.uml.behavioralelements.commonbehavior.Stimulus;
import org.omg.uml.behavioralelements.commonbehavior.TerminateAction;
import org.omg.uml.behavioralelements.commonbehavior.UmlException;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.Event;
import org.omg.uml.behavioralelements.statemachines.FinalState;
import org.omg.uml.behavioralelements.statemachines.Guard;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.statemachines.SimpleState;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.statemachines.StateVertex;
import org.omg.uml.behavioralelements.statemachines.StubState;
import org.omg.uml.behavioralelements.statemachines.SubmachineState;
import org.omg.uml.behavioralelements.statemachines.SynchState;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.behavioralelements.usecases.Actor;
import org.omg.uml.behavioralelements.usecases.Extend;
import org.omg.uml.behavioralelements.usecases.ExtensionPoint;
import org.omg.uml.behavioralelements.usecases.Include;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.Artifact;
import org.omg.uml.foundation.core.AssociationClass;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.BehavioralFeature;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Comment;
import org.omg.uml.foundation.core.Component;
import org.omg.uml.foundation.core.Constraint;
import org.omg.uml.foundation.core.DataType;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.Enumeration;
import org.omg.uml.foundation.core.EnumerationLiteral;
import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.Interface;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.Node;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.core.Permission;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.TagDefinition;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.core.UmlAssociation;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.core.Usage;
import org.omg.uml.foundation.datatypes.ActionExpression;
import org.omg.uml.foundation.datatypes.AggregationKind;
import org.omg.uml.foundation.datatypes.BooleanExpression;
import org.omg.uml.foundation.datatypes.CallConcurrencyKind;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.ParameterDirectionKind;
import org.omg.uml.foundation.datatypes.PseudostateKind;
import org.omg.uml.foundation.datatypes.ScopeKind;
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

    /**
     * Constructor that forbids to instantiate.
     */
    MetaTypesMDRImpl() {
        // forbid instantiation
    }

    /*
     * @see org.argouml.model.MetaTypes#getAbstraction()
     */
    public Object getAbstraction() {
        return Abstraction.class;
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

    public Object getBooleanExpression() {
        return BooleanExpression.class;
    }

    public Object getCallAction() {
        return CallAction.class;
    }

    public Object getCallConcurrencyKind() {
        return CallConcurrencyKind.class;
    }

    public Object getCallState() {
        return CallState.class;
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

    public Object getCompositeState() {
        return CompositeState.class;
    }
    
    public Object getConstraint() {
        return Constraint.class;
    }

    public Object getCreateAction() {
        return CreateAction.class;
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

    public Object getInstance() {
        return Instance.class;
    }

    public Object getInterface() {
        return Interface.class;
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

    public Object getObjectFlowState() {
        return ObjectFlowState.class;
    }

    public Object getOperation() {
        return Operation.class;
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

    @SuppressWarnings("deprecation")
    public Object getPermission() {
        return Permission.class;
    }

    public Object getPackageImport() {
        return Permission.class;
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
        return ReturnAction.class;
    }

    public Object getScopeKind() {
        return ScopeKind.class;
    }

    public Object getSendAction() {
        return SendAction.class;
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

}
