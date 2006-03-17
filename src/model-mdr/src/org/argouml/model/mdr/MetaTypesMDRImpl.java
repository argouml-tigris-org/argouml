// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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
import org.omg.uml.behavioralelements.usecases.Include;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.AssociationClass;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.BehavioralFeature;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Comment;
import org.omg.uml.foundation.core.Component;
import org.omg.uml.foundation.core.DataType;
import org.omg.uml.foundation.core.Dependency;
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
public final class MetaTypesMDRImpl implements MetaTypes {

    /**
     * Given a model element instance returns the name of its meta type.
     * @param modelElement The ModelElement on which extract the name
     * @return A string which is the metatype name of the modelElement 
     */
    public String getName(Object modelElement) {
        Class clazz;
        if (modelElement instanceof Class) {
            clazz = (Class) modelElement;
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

    /**
     * @return Returns the Abstraction.
     */
    public Object getAbstraction() {
        return Abstraction.class;
    }

    /**
     * @return Returns the Action.
     */
    public Object getAction() {
        return Action.class;
    }

    /**
     * @return Returns the ActionExpression.
     */
    public Object getActionExpression() {
        return ActionExpression.class;
    }

    /**
     * @return Returns the ActionState.
     */
    public Object getActionState() {
        return ActionState.class;
    }

    /**
     * @return Returns the Actor.
     */
    public Object getActor() {
        return Actor.class;
    }

    /**
     * @return Returns the AggregationKind.
     */
    public Object getAggregationKind() {
        return AggregationKind.class;
    }

    /**
     * @return Returns the Association.
     */
    public Object getAssociation() {
        return UmlAssociation.class;
    }

    /**
     * @return Returns the AssociationClass.
     */
    public Object getAssociationClass() {
        return AssociationClass.class;
    }

    /**
     * @return Returns the AssociationEnd.
     */
    public Object getAssociationEnd() {
        return AssociationEnd.class;
    }

    /**
     * @return Returns the AssociationEndRole.
     */
    public Object getAssociationEndRole() {
        return AssociationEndRole.class;
    }

    /**
     * @return Returns the AssociationRole.
     */
    public Object getAssociationRole() {
        return AssociationRole.class;
    }

    /**
     * @return Returns the Attribute.
     */
    public Object getAttribute() {
        return Attribute.class;
    }

    /**
     * @see org.argouml.model.MetaTypes#getBehavioralFeature()
     */
    public Object getBehavioralFeature() {
        return BehavioralFeature.class;    
    }

    /**
     * @return Returns the BooleanExpression.
     */
    public Object getBooleanExpression() {
        return BooleanExpression.class;
    }

    /**
     * @return Returns the CallAction.
     */
    public Object getCallAction() {
        return CallAction.class;
    }

    /**
     * @return Returns the CallConcurrencyKind.
     */
    public Object getCallConcurrencyKind() {
        return CallConcurrencyKind.class;
    }

    /**
     * @return Returns the CallState.
     */
    public Object getCallState() {
        return CallState.class;
    }

    /**
     * This method has a special name because Java already uses the getClass()
     * method.
     * 
     * @return Returns the Class.
     */
    public Object getUMLClass() {
        return UmlClass.class;
    }

    /**
     * @return Returns the Classifier.
     */
    public Object getClassifier() {
        return Classifier.class;
    }

    /**
     * @return Returns the ClassifierRole.
     */
    public Object getClassifierRole() {
        return ClassifierRole.class;
    }

    /**
     * @return Returns the Collaboration.
     */
    public Object getCollaboration() {
        return Collaboration.class;
    }

    /**
     * @return Returns the Comment.
     */
    public Object getComment() {
        return Comment.class;
    }

    /**
     * @return Returns the Component.
     */
    public Object getComponent() {
        return Component.class;
    }

    /**
     * @return Returns the ComponentInstance.
     */
    public Object getComponentInstance() {
        return ComponentInstance.class;
    }

    /**
     * @return Returns the CompositeState.
     */
    public Object getCompositeState() {
        return CompositeState.class;
    }

    /**
     * @return Returns the CreateAction.
     */
    public Object getCreateAction() {
        return CreateAction.class;
    }

    /**
     * @return Returns the Datatype.
     */
    public Object getDataType() {
        return DataType.class;
    }

    /**
     * @return Returns the Dependency.
     */
    public Object getDependency() {
        return Dependency.class;
    }

    /**
     * @return Returns the DestroyAction.
     */
    public Object getDestroyAction() {
        return DestroyAction.class;
    }

    /**
     * @return Returns the Event.
     */
    public Object getEvent() {
        return Event.class;
    }

    /**
     * @return Returns the Exception.
     */
    public Object getException() {
        return UmlException.class;
    }

    /**
     * @return Returns the Extend.
     */
    public Object getExtend() {
        return Extend.class;
    }

    /**
     * @return Returns the FinalState.
     */
    public Object getFinalState() {
        return FinalState.class;
    }

    /**
     * @return Returns the GeneralizableElement.
     */
    public Object getGeneralizableElement() {
        return GeneralizableElement.class;
    }

    /**
     * @return Returns the Generalization.
     */
    public Object getGeneralization() {
        return Generalization.class;
    }

    /**
     * @return Returns the Guard.
     */
    public Object getGuard() {
        return Guard.class;
    }

    /**
     * @return Returns the Include.
     */
    public Object getInclude() {
        return Include.class;
    }

    /**
     * @return Returns the Instance.
     */
    public Object getInstance() {
        return Instance.class;
    }

    /**
     * @return Returns the Interface.
     */
    public Object getInterface() {
        return Interface.class;
    }

    /**
     * @return Returns the Link.
     */
    public Object getLink() {
        return Link.class;
    }

    /**
     * @return Returns the Message.
     */
    public Object getMessage() {
        return Message.class;
    }

    /**
     * @return Returns the Model.
     */
    public Object getModel() {
        return Model.class;
    }

    /**
     * @return Returns the ModelElement.
     */
    public Object getModelElement() {
        return ModelElement.class;
    }

    /**
     * @return Returns the Multiplicity.
     */
    public Object getMultiplicity() {
        return Multiplicity.class;
    }

    /**
     * @return Returns the Namespace.
     */
    public Object getNamespace() {
        return Namespace.class;
    }

    /**
     * @return Returns the Node.
     */
    public Object getNode() {
        return Node.class;
    }

    /**
     * @return Returns the NodeInstance.
     */
    public Object getNodeInstance() {
        return NodeInstance.class;
    }

    /**
     * @return Returns the Object.
     */
    public Object getObject() {
        return org.omg.uml.behavioralelements.commonbehavior.Object.class;
    }

    /**
     * @return Returns the ObjectFlowState.
     */
    public Object getObjectFlowState() {
        return ObjectFlowState.class;
    }

    /**
     * @return Returns the Operation.
     */
    public Object getOperation() {
        return Operation.class;
    }

    /**
     * @return Returns the Package.
     */
    public Object getPackage() {
        return UmlPackage.class;
    }

    /**
     * @return Returns the Parameter.
     */
    public Object getParameter() {
        return Parameter.class;
    }

    /**
     * @return Returns the ParameterDirectionKind.
     */
    public Object getParameterDirectionKind() {
        return ParameterDirectionKind.class;
    }

    /**
     * @return Returns the Partition.
     */
    public Object getPartition() {
        return Partition.class;
    }

    /**
     * @return Returns the Permission.
     */
    public Object getPermission() {
        return Permission.class;
    }

    /**
     * @return Returns the Pseudostate.
     */
    public Object getPseudostate() {
        return Pseudostate.class;
    }

    /**
     * @return Returns the PseudostateKind.
     */
    public Object getPseudostateKind() {
        return PseudostateKind.class;
    }

    /**
     * @return Returns the Reception.
     */
    public Object getReception() {
        return Reception.class;
    }

    /**
     * @return Returns the ReturnAction.
     */
    public Object getReturnAction() {
        return ReturnAction.class;
    }

    /**
     * @return Returns the ScopeKind.
     */
    public Object getScopeKind() {
        return ScopeKind.class;
    }

    /**
     * @return Returns the SendAction.
     */
    public Object getSendAction() {
        return SendAction.class;
    }

    /**
     * @return Returns the Signal.
     */
    public Object getSignal() {
        return Signal.class;
    }
    
    /**
     * @return Returns the SimpleState.
     */
    public Object getSimpleState() {
        return SimpleState.class;
    }

    /**
     * @return Returns the State.
     */
    public Object getState() {
        return State.class;
    }

    /**
     * @return Returns the StateMachine.
     */
    public Object getStateMachine() {
        return StateMachine.class;
    }

    /**
     * @return Returns the StateVertex.
     */
    public Object getStateVertex() {
        return StateVertex.class;
    }

    /**
     * @return Returns the Stereotype.
     */
    public Object getStereotype() {
        return Stereotype.class;
    }

    /**
     * @return Returns the Stimulus.
     */
    public Object getStimulus() {
        return Stimulus.class;
    }

    /**
     * @return Returns the StubState.
     */
    public Object getStubState() {
        return StubState.class;
    }

    /**
     * @return Returns the SubactivityState.
     */
    public Object getSubactivityState() {
        return SubactivityState.class;
    }

    /**
     * @return Returns the SubmachineState.
     */
    public Object getSubmachineState() {
        return SubmachineState.class;
    }

    /**
     * @return Returns the Subsystem.
     */
    public Object getSubsystem() {
        return Subsystem.class;
    }

    /**
     * @return Returns the SynchState.
     */
    public Object getSynchState() {
        return SynchState.class;
    }

    /**
     * @return Returns the TerminateAction.
     */
    public Object getTerminateAction() {
        return TerminateAction.class;
    }

    /**
     * @return Returns the Transition.
     */
    public Object getTransition() {
        return Transition.class;
    }

    /**
     * @return Returns the Usage.
     */
    public Object getUsage() {
        return Usage.class;
    }

    /**
     * @return Returns the Use Case.
     */
    public Object getUseCase() {
        return UseCase.class;
    }

    /**
     * @return Returns the VisibilityKind.
     */
    public Object getVisibilityKind() {
        return VisibilityKind.class;
    }

    /**
     * @see org.argouml.model.MetaTypes#getTagDefinition()
     * @return Returns the TagDefinition
     */
    public Object getTagDefinition() {
        return TagDefinition.class;
    }

    /**
     * @see org.argouml.model.MetaTypes#getInteraction()
     */
    public Object getInteraction() {
        return Interaction.class;
    }
}
