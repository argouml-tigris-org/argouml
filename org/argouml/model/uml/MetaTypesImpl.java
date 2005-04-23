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

package org.argouml.model.uml;

import org.argouml.model.MetaTypes;

import ru.novosoft.uml.behavior.activity_graphs.MActionState;
import ru.novosoft.uml.behavior.activity_graphs.MCallState;
import ru.novosoft.uml.behavior.activity_graphs.MObjectFlowState;
import ru.novosoft.uml.behavior.activity_graphs.MPartition;
import ru.novosoft.uml.behavior.activity_graphs.MSubactivityState;
import ru.novosoft.uml.behavior.collaborations.MAssociationEndRole;
import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.behavior.collaborations.MCollaboration;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MAction;
import ru.novosoft.uml.behavior.common_behavior.MCallAction;
import ru.novosoft.uml.behavior.common_behavior.MComponentInstance;
import ru.novosoft.uml.behavior.common_behavior.MCreateAction;
import ru.novosoft.uml.behavior.common_behavior.MDestroyAction;
import ru.novosoft.uml.behavior.common_behavior.MException;
import ru.novosoft.uml.behavior.common_behavior.MInstance;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.common_behavior.MNodeInstance;
import ru.novosoft.uml.behavior.common_behavior.MObject;
import ru.novosoft.uml.behavior.common_behavior.MReception;
import ru.novosoft.uml.behavior.common_behavior.MReturnAction;
import ru.novosoft.uml.behavior.common_behavior.MSendAction;
import ru.novosoft.uml.behavior.common_behavior.MSignal;
import ru.novosoft.uml.behavior.common_behavior.MStimulus;
import ru.novosoft.uml.behavior.common_behavior.MTerminateAction;
import ru.novosoft.uml.behavior.state_machines.MCompositeState;
import ru.novosoft.uml.behavior.state_machines.MEvent;
import ru.novosoft.uml.behavior.state_machines.MFinalState;
import ru.novosoft.uml.behavior.state_machines.MGuard;
import ru.novosoft.uml.behavior.state_machines.MPseudostate;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.behavior.state_machines.MStubState;
import ru.novosoft.uml.behavior.state_machines.MSubmachineState;
import ru.novosoft.uml.behavior.state_machines.MSynchState;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.behavior.use_cases.MActor;
import ru.novosoft.uml.behavior.use_cases.MExtend;
import ru.novosoft.uml.behavior.use_cases.MInclude;
import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.foundation.core.MAbstraction;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationClass;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MComment;
import ru.novosoft.uml.foundation.core.MComponent;
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MDependency;
import ru.novosoft.uml.foundation.core.MGeneralizableElement;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MNode;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.core.MPermission;
import ru.novosoft.uml.foundation.core.MUsage;
import ru.novosoft.uml.foundation.data_types.MActionExpression;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;
import ru.novosoft.uml.foundation.data_types.MBooleanExpression;
import ru.novosoft.uml.foundation.data_types.MCallConcurrencyKind;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;
import ru.novosoft.uml.foundation.data_types.MPseudostateKind;
import ru.novosoft.uml.foundation.data_types.MScopeKind;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.model_management.MPackage;
import ru.novosoft.uml.model_management.MSubsystem;

/**
 * This class contains methods to retrieve objects that represent the
 * different UML types. These objects are use as tokens used to create
 * model elements in some methods.
 *
 * @see org.argouml.model.UmlFactory#buildConnection(
 *         Object, Object, Object, Object, Object, Object, Object)
 * @see org.argouml.model.UmlFactory#buildNode(Object)
 */
public final class MetaTypesImpl implements MetaTypes {
    /**
     * Constructor that forbids to instantiate.
     */
    MetaTypesImpl() {
    }

    /**
     * @return Returns the Abstraction.
     */
    public Object getAbstraction() {
        return MAbstraction.class;
    }

    /**
     * @return Returns the Action.
     */
    public Object getAction() {
        return MAction.class;
    }

    /**
     * @return Returns the ActionExpression.
     */
    public Object getActionExpression() {
        return MActionExpression.class;
    }

    /**
     * @return Returns the ActionState.
     */
    public Object getActionState() {
        return MActionState.class;
    }

    /**
     * @return Returns the Actor.
     */
    public Object getActor() {
        return MActor.class;
    }

    /**
     * @return Returns the AggregationKind.
     */
    public Object getAggregationKind() {
        return MAggregationKind.class;
    }

    /**
     * @return Returns the Association.
     */
    public Object getAssociation() {
        return MAssociation.class;
    }

    /**
     * @return Returns the AssociationClass.
     */
    public Object getAssociationClass() {
        return MAssociationClass.class;
    }

    /**
     * @return Returns the AssociationEnd.
     */
    public Object getAssociationEnd() {
        return MAssociationEnd.class;
    }

    /**
     * @return Returns the AssociationEndRole.
     */
    public Object getAssociationEndRole() {
        return MAssociationEndRole.class;
    }

    /**
     * @return Returns the AssociationRole.
     */
    public Object getAssociationRole() {
        return MAssociationRole.class;
    }

    /**
     * @return Returns the Attribute.
     */
    public Object getAttribute() {
        return MAttribute.class;
    }

    /**
     * @return Returns the BooleanExpression.
     */
    public Object getBooleanExpression() {
        return MBooleanExpression.class;
    }

    /**
     * @return Returns the CallAction.
     */
    public Object getCallAction() {
        return MCallAction.class;
    }

    /**
     * @return Returns the CallConcurrencyKind.
     */
    public Object getCallConcurrencyKind() {
        return MCallConcurrencyKind.class;
    }

    /**
     * @return Returns the CallState.
     */
    public Object getCallState() {
        return MCallState.class;
    }

    /**
     * This method has a special name because Java already uses the
     * getClass() method.
     *
     * @return Returns the Class.
     */
    public Object getUMLClass() {
        return MClass.class;
    }

    /**
     * @return Returns the Classifier.
     */
    public Object getClassifier() {
        return MClassifier.class;
    }

    /**
     * @return Returns the ClassifierRole.
     */
    public Object getClassifierRole() {
        return MClassifierRole.class;
    }

    /**
     * @return Returns the Collaboration.
     */
    public Object getCollaboration() {
        return MCollaboration.class;
    }

    /**
     * @return Returns the Comment.
     */
    public Object getComment() {
        return MComment.class;
    }

    /**
     * @return Returns the Component.
     */
    public Object getComponent() {
        return MComponent.class;
    }

    /**
     * @return Returns the ComponentInstance.
     */
    public Object getComponentInstance() {
        return MComponentInstance.class;
    }

    /**
     * @return Returns the CompositeState.
     */
    public Object getCompositeState() {
        return MCompositeState.class;
    }

    /**
     * @return Returns the CreateAction.
     */
    public Object getCreateAction() {
        return MCreateAction.class;
    }

    /**
     * @return Returns the Datatype.
     */
    public Object getDatatype() {
        return MDataType.class;
    }

    /**
     * @return Returns the Dependency.
     */
    public Object getDependency() {
        return MDependency.class;
    }

    /**
     * @return Returns the DestroyAction.
     */
    public Object getDestroyAction() {
        return MDestroyAction.class;
    }

    /**
     * @return Returns the Event.
     */
    public Object getEvent() {
        return MEvent.class;
    }

    /**
     * @return Returns the Exception.
     */
    public Object getException() {
        return MException.class;
    }

    /**
     * @return Returns the Extend.
     */
    public Object getExtend() {
        return MExtend.class;
    }

    /**
     * @return Returns the FinalState.
     */
    public Object getFinalState() {
        return MFinalState.class;
    }

    /**
     * @return Returns the GeneralizableElement.
     */
    public Object getGeneralizableElement() {
        return MGeneralizableElement.class;
    }

    /**
     * @return Returns the Generalization.
     */
    public Object getGeneralization() {
        return MGeneralization.class;
    }

    /**
     * @return Returns the Guard.
     */
    public Object getGuard() {
        return MGuard.class;
    }

    /**
     * @return Returns the Include.
     */
    public Object getInclude() {
        return MInclude.class;
    }

    /**
     * @return Returns the Instance.
     */
    public Object getInstance() {
        return MInstance.class;
    }

    /**
     * @return Returns the Interface.
     */
    public Object getInterface() {
        return MInterface.class;
    }

    /**
     * @return Returns the Link.
     */
    public Object getLink() {
        return MLink.class;
    }

    /**
     * @return Returns the Message.
     */
    public Object getMessage() {
        return MMessage.class;
    }

    /**
     * @return Returns the Model.
     */
    public Object getModel() {
        return MModel.class;
    }

    /**
     * @return Returns the ModelElement.
     */
    public Object getModelElement() {
        return MModelElement.class;
    }

    /**
     * @return Returns the Multiplicity.
     */
    public Object getMultiplicity() {
        return MMultiplicity.class;
    }

    /**
     * @return Returns the Namespace.
     */
    public Object getNamespace() {
        return MNamespace.class;
    }

    /**
     * @return Returns the Node.
     */
    public Object getNode() {
        return MNode.class;
    }

    /**
     * @return Returns the NodeInstance.
     */
    public Object getNodeInstance() {
        return MNodeInstance.class;
    }

    /**
     * @return Returns the Object.
     */
    public Object getObject() {
        return MObject.class;
    }

    /**
     * @return Returns the ObjectFlowState.
     */
    public Object getObjectFlowState() {
        return MObjectFlowState.class;
    }

    /**
     * @return Returns the Operation.
     */
    public Object getOperation() {
        return MOperation.class;
    }

    /**
     * @return Returns the Package.
     */
    public Object getPackage() {
        return MPackage.class;
    }

    /**
     * @return Returns the Parameter.
     */
    public Object getParameter() {
        return MParameter.class;
    }

    /**
     * @return Returns the ParameterDirectionKind.
     */
    public Object getParameterDirectionKind() {
        return MParameterDirectionKind.class;
    }

    /**
     * @return Returns the Partition.
     */
    public Object getPartition() {
        return MPartition.class;
    }

    /**
     * @return Returns the Permission.
     */
    public Object getPermission() {
        return MPermission.class;
    }

    /**
     * @return Returns the Pseudostate.
     */
    public Object getPseudostate() {
        return MPseudostate.class;
    }

    /**
     * @return Returns the PseudostateKind.
     */
    public Object getPseudostateKind() {
        return MPseudostateKind.class;
    }

    /**
     * @return Returns the Reception.
     */
    public Object getReception() {
        return MReception.class;
    }

    /**
     * @return Returns the ReturnAction.
     */
    public Object getReturnAction() {
        return MReturnAction.class;
    }

    /**
     * @return Returns the ScopeKind.
     */
    public Object getScopeKind() {
        return MScopeKind.class;
    }

    /**
     * @return Returns the SendAction.
     */
    public Object getSendAction() {
        return MSendAction.class;
    }

    /**
     * @return Returns the Signal.
     */
    public Object getSignal() {
        return MSignal.class;
    }

    /**
     * @return Returns the State.
     */
    public Object getState() {
        return MState.class;
    }

    /**
     * @return Returns the StateMachine.
     */
    public Object getStateMachine() {
        return MStateMachine.class;
    }

    /**
     * @return Returns the StateVertex.
     */
    public Object getStateVertex() {
        return MStateVertex.class;
    }

    /**
     * @return Returns the Stereotype.
     */
    public Object getStereotype() {
        return MStereotype.class;
    }

    /**
     * @return Returns the Stimulus.
     */
    public Object getStimulus() {
        return MStimulus.class;
    }

    /**
     * @return Returns the StubState.
     */
    public Object getStubState() {
        return MStubState.class;
    }

    /**
     * @return Returns the SubactivityState.
     */
    public Object getSubactivityState() {
        return MSubactivityState.class;
    }

    /**
     * @return Returns the SubmachineState.
     */
    public Object getSubmachineState() {
        return MSubmachineState.class;
    }

    /**
     * @return Returns the Subsystem.
     */
    public Object getSubsystem() {
        return MSubsystem.class;
    }

    /**
     * @return Returns the SynchState.
     */
    public Object getSynchState() {
        return MSynchState.class;
    }

    /**
     * @return Returns the TerminateAction.
     */
    public Object getTerminateAction() {
        return MTerminateAction.class;
    }

    /**
     * @return Returns the Transition.
     */
    public Object getTransition() {
        return MTransition.class;
    }

    /**
     * @return Returns the Usage.
     */
    public Object getUsage() {
        return MUsage.class;
    }

    /**
     * @return Returns the Use Case.
     */
    public Object getUseCase() {
        return MUseCase.class;
    }

    /**
     * @return Returns the VisibilityKind.
     */
    public Object getVisibilityKind() {
        return MVisibilityKind.class;
    }
}

