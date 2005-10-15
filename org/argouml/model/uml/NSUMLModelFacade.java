// $Id$
// Copyright (c) 2003-2005 The Regents of the University of California. All
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

package org.argouml.model.uml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.model.Facade;
import org.argouml.model.UmlException;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.MElementListener;
import ru.novosoft.uml.MExtension;
import ru.novosoft.uml.behavior.activity_graphs.MActionState;
import ru.novosoft.uml.behavior.activity_graphs.MActivityGraph;
import ru.novosoft.uml.behavior.activity_graphs.MCallState;
import ru.novosoft.uml.behavior.activity_graphs.MClassifierInState;
import ru.novosoft.uml.behavior.activity_graphs.MObjectFlowState;
import ru.novosoft.uml.behavior.activity_graphs.MPartition;
import ru.novosoft.uml.behavior.activity_graphs.MSubactivityState;
import ru.novosoft.uml.behavior.collaborations.MAssociationEndRole;
import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.behavior.collaborations.MCollaboration;
import ru.novosoft.uml.behavior.collaborations.MInteraction;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MAction;
import ru.novosoft.uml.behavior.common_behavior.MActionSequence;
import ru.novosoft.uml.behavior.common_behavior.MArgument;
import ru.novosoft.uml.behavior.common_behavior.MAttributeLink;
import ru.novosoft.uml.behavior.common_behavior.MCallAction;
import ru.novosoft.uml.behavior.common_behavior.MComponentInstance;
import ru.novosoft.uml.behavior.common_behavior.MCreateAction;
import ru.novosoft.uml.behavior.common_behavior.MDataValue;
import ru.novosoft.uml.behavior.common_behavior.MDestroyAction;
import ru.novosoft.uml.behavior.common_behavior.MException;
import ru.novosoft.uml.behavior.common_behavior.MInstance;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.common_behavior.MLinkEnd;
import ru.novosoft.uml.behavior.common_behavior.MNodeInstance;
import ru.novosoft.uml.behavior.common_behavior.MObject;
import ru.novosoft.uml.behavior.common_behavior.MReception;
import ru.novosoft.uml.behavior.common_behavior.MReturnAction;
import ru.novosoft.uml.behavior.common_behavior.MSendAction;
import ru.novosoft.uml.behavior.common_behavior.MSignal;
import ru.novosoft.uml.behavior.common_behavior.MStimulus;
import ru.novosoft.uml.behavior.common_behavior.MTerminateAction;
import ru.novosoft.uml.behavior.common_behavior.MUninterpretedAction;
import ru.novosoft.uml.behavior.state_machines.MCallEvent;
import ru.novosoft.uml.behavior.state_machines.MChangeEvent;
import ru.novosoft.uml.behavior.state_machines.MCompositeState;
import ru.novosoft.uml.behavior.state_machines.MEvent;
import ru.novosoft.uml.behavior.state_machines.MFinalState;
import ru.novosoft.uml.behavior.state_machines.MGuard;
import ru.novosoft.uml.behavior.state_machines.MPseudostate;
import ru.novosoft.uml.behavior.state_machines.MSignalEvent;
import ru.novosoft.uml.behavior.state_machines.MSimpleState;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.behavior.state_machines.MStubState;
import ru.novosoft.uml.behavior.state_machines.MSubmachineState;
import ru.novosoft.uml.behavior.state_machines.MSynchState;
import ru.novosoft.uml.behavior.state_machines.MTimeEvent;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.behavior.use_cases.MActor;
import ru.novosoft.uml.behavior.use_cases.MExtend;
import ru.novosoft.uml.behavior.use_cases.MExtensionPoint;
import ru.novosoft.uml.behavior.use_cases.MInclude;
import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.foundation.core.MAbstraction;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationClass;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MBehavioralFeature;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MComment;
import ru.novosoft.uml.foundation.core.MComponent;
import ru.novosoft.uml.foundation.core.MConstraint;
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MDependency;
import ru.novosoft.uml.foundation.core.MElement;
import ru.novosoft.uml.foundation.core.MElementResidence;
import ru.novosoft.uml.foundation.core.MFeature;
import ru.novosoft.uml.foundation.core.MFlow;
import ru.novosoft.uml.foundation.core.MGeneralizableElement;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MMethod;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MNode;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.core.MPermission;
import ru.novosoft.uml.foundation.core.MRelationship;
import ru.novosoft.uml.foundation.core.MStructuralFeature;
import ru.novosoft.uml.foundation.core.MUsage;
import ru.novosoft.uml.foundation.data_types.MActionExpression;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;
import ru.novosoft.uml.foundation.data_types.MCallConcurrencyKind;
import ru.novosoft.uml.foundation.data_types.MChangeableKind;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MMessageDirectionKind;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MMultiplicityRange;
import ru.novosoft.uml.foundation.data_types.MOperationDirectionKind;
import ru.novosoft.uml.foundation.data_types.MOrderingKind;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;
import ru.novosoft.uml.foundation.data_types.MPseudostateKind;
import ru.novosoft.uml.foundation.data_types.MScopeKind;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.model_management.MElementImport;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.model_management.MPackage;
import ru.novosoft.uml.model_management.MSubsystem;

/**
 * Facade object for the Model subsystem in ArgoUML.<p>
 *
 * The purpose of this Facade object is to allow for decoupling other
 * subsystems from the insides of the model. For this purpose all of
 * the methods in this class give away and accept handles (of type
 * {@link java.lang.Object}) to the objects within the model.<p>
 *
 * This is just recognizers and getters.<p>
 *
 * To manipulate the objects of the model there is a set of factories
 * and helpers within the Model subsystem.<p>
 *
 * Signatures for all recognizers are:
 * <ul>
 * <li>public boolean isATYPE(Object handle)
 * <li>public boolean isPROPERTY(Object handle)
 * <li>public boolean hasPROPERTY(Object handle)
 * </ul>
 *
 * Signatures for all getters are:
 * <ul>
 * <li>public Object getROLENAME(Object handle) - 1..1
 * <li>public Iterator/Collection getROLENAMEs(Object handle) - 0..*
 * <li>public String getName(Object handle) - Name
 * </ul>
 *
 * @author Linus Tolke
 */
public class NSUMLModelFacade implements Facade {
    ////////////////////////////////////////////////////////////////
    // constants

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(NSUMLModelFacade.class);

    /**
     * The model implementation.
     */
    private NSUMLModelImplementation nsmodel;

    /**
     * Constructor.
     *
     * @param implementation To get other helpers and factories.
     */
    NSUMLModelFacade(NSUMLModelImplementation implementation) {
        nsmodel = implementation;
    }

    /**
     * Recognizer for Abstraction.
     *
     * @param handle candidate
     * @return true if handle is an Abstraction
     */
    public boolean isAAbstraction(Object handle) {
        return handle instanceof MAbstraction;
    }

    /**
     * Recognizer for Action.
     *
     * @param handle candidate
     * @return true if handle is an Action
     */
    public boolean isAAction(Object handle) {
        return handle instanceof MAction;
    }

    /**
     * Recognizer for ActionExpression.
     *a
     * @param handle candidate
     * @return true if handle is an ActionExpression
     */
    public boolean isAActionExpression(Object handle) {
        return handle instanceof MActionExpression;
    }

    /**
     * Recognizer for ActionSequence.
     *
     * @param handle candidate
     * @return true if handle is an action sequence
     */
    public boolean isAActionSequence(Object handle) {
        return handle instanceof MActionSequence;
    }

    /**
     * Recognizer for Action state.
     *
     * @param handle candidate
     * @return true if handle is an Action state
     */
    public boolean isAActionState(Object handle) {
        return handle instanceof MActionState;
    }

    /**
     * Recognizer for CallState.
     *
     * @param handle candidate
     * @return true if handle is an call state
     */
    public boolean isACallState(Object handle) {
        return handle instanceof MCallState;
    }

    /**
     * Recognizer for ObjectFlowState.
     *
     * @param handle candidate
     * @return true if handle is an objectflow state
     */
    public boolean isAObjectFlowState(Object handle) {
        return handle instanceof MObjectFlowState;
    }

    /**
     * Recognizer for SubactivityState.
     *
     * @param handle candidate
     * @return true if handle is an subactivity state
     */
    public boolean isASubactivityState(Object handle) {
        return handle instanceof MSubactivityState;
    }

    /**
     * Recognizer for Actor.
     *
     * @param handle candidate
     * @return true if handle is an Actor
     */
    public boolean isAActor(Object handle) {
        return handle instanceof MActor;
    }

    /**
     * Recognizer for AggregationKind.
     *
     * @param handle candidate
     * @return true if handle is an AggregationKind
     */
    public boolean isAAggregationKind(Object handle) {
        return handle instanceof MAggregationKind;
    }
    
    /**
     * @see org.argouml.model.Facade#isAArgument(java.lang.Object)
     */
    public boolean isAArgument(Object modelElement) {
        return modelElement instanceof MArgument;
    }

    /**
     * Recognizer for Association.
     *
     * @param handle candidate
     * @return true if handle is an Association
     */
    public boolean isAAssociation(Object handle) {
        return handle instanceof MAssociation;
    }

    /**
     * Recognizer for AssociationEnd.
     *
     * @param handle candidate
     * @return true if handle is an AssociationEnd
     */
    public boolean isAAssociationEnd(Object handle) {
        return handle instanceof MAssociationEnd;
    }

    /**
     * Recognizer for AssociationRole.
     *
     * @param handle candidate
     * @return true if handle is an AssociationRole
     */
    public boolean isAAssociationRole(Object handle) {
        return handle instanceof MAssociationRole;
    }

    /**
     * Recognizer for AssociationEndRole.
     *
     * @param handle candidate
     * @return true if handle is an AssociationEndRole
     */
    public boolean isAAssociationEndRole(Object handle) {
        return handle instanceof MAssociationEndRole;
    }

    /**
     * Recognizer for Attribute.
     *
     * @param handle candidate
     * @return true if handle is an Attribute
     */
    public boolean isAAttribute(Object handle) {
        return handle instanceof MAttribute;
    }

    /**
     * Recognizer for asynchronisity of an action.
     *
     * @param handle candidate
     * @return true if the argument is asynchronous
     */
    public boolean isAsynchronous(Object handle) {
        if (handle instanceof MAction) {
            return ((MAction) handle).isAsynchronous();
        }
	return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for abstract classes and operations.
     *
     * @param handle candidate
     * @return true if handle is abstract.
     */
    public boolean isAbstract(Object handle) {
        if (handle instanceof MOperation) {
            return ((MOperation) handle).isAbstract();
        }
        if (handle instanceof MGeneralizableElement) {
            return ((MGeneralizableElement) handle).isAbstract();
        }
        if (handle instanceof MAssociation) {
            return ((MAssociation) handle).isAbstract();
        }
        // isAbstarct() is not a typo! mistake in nsuml!
        if (handle instanceof MReception) {
            return ((MReception) handle).isAbstarct();
        }
        // ...
	return illegalArgumentBoolean(handle);
    }



    /**
     * Recognizer for ActivityGraph.
     *
     * @param handle candidate
     * @return true if handle is ActivityGraph.
     */
    public boolean isAActivityGraph(Object handle) {
        return handle instanceof MActivityGraph;
    }

    /**
     * Recognizer for bases. A base is an object that is some form of
     * an element in the model. MBase in Novosoft terms.
     *
     * @param handle candidate
     * @return true if handle is a base.
     */
    public boolean isABase(Object handle) {
        return handle instanceof MBase;
    }


    /**
     * Recognizer for behavioral features.
     *
     * @param handle candidate
     * @return true if handle is a behavioral feature
     */
    public boolean isABehavioralFeature(Object handle) {
        return handle instanceof MBehavioralFeature;
    }

    /**
     * Recognizer for CallAction.
     *
     * @param handle candidate
     * @return true if handle is a CallAction
     */
    public boolean isACallAction(Object handle) {
        return handle instanceof MCallAction;
    }

    /**
     * Recognizer for CallEvent.
     *
     * @param handle candidate
     * @return true if handle is a CallEvent
     */
    public boolean isACallEvent(Object handle) {
        return handle instanceof MCallEvent;
    }

    /**
     * Recognizer for ChangeEvent.
     *
     * @param handle candidate
     * @return true if handle is a ChangeEvent
     */
    public boolean isAChangeEvent(Object handle) {
        return handle instanceof MChangeEvent;
    }

    /**
     * Recognizer for Class.
     *
     * @param handle candidate
     * @return true if handle is a Class
     */
    public boolean isAClass(Object handle) {
        return handle instanceof MClass;
    }

    /**
    * Recognizer for AssociationClass.
    *
    * @param handle candidate
    * @return true if handle is an AssociationClass
    */
    public boolean isAAssociationClass(Object handle) {
        return handle instanceof MAssociationClass;
    }

    /**
     * Recognizer for a Element that is Classifier and RelationShip.
     *
     * @param handle candidate
     * @return true if handle is a Classifier and a Relationship
     */
    public boolean isAClassifierAndARelationship(Object handle) {
        return ((handle instanceof MClassifier)
                && (handle instanceof MRelationship));
    }

    /**
     * Recognizer for Classifier.
     *
     * @param handle candidate
     * @return true if handle is a Classifier
     */
    public boolean isAClassifier(Object handle) {
        return handle instanceof MClassifier;
    }

    /**
     * Recognizer for ClassifierInState.
     *
     * @param handle candidate
     * @return true if handle is a ClassifierInState
     */
    public boolean isAClassifierInState(Object handle) {
        return handle instanceof MClassifierInState;
    }

    /**
     * Recognizer for ClassifierRole.
     *
     * @param handle candidate
     * @return true if handle is a ClassifierRole
     */
    public boolean isAClassifierRole(Object handle) {
        return handle instanceof MClassifierRole;
    }

    /**
     * Recognizer for Comment.
     *
     * @param handle candidate
     * @return true if handle is a Comment
     */
    public boolean isAComment(Object handle) {
        return handle instanceof MComment;
    }

    /**
     * Recognizer for Collaboration.
     *
     * @param handle candidate
     * @return true if handle is a Collaboration
     */
    public boolean isACollaboration(Object handle) {
        return handle instanceof MCollaboration;
    }

    /**
     * Recognizer for Component.
     *
     * @param handle candidate
     * @return true if handle is a Component
     */
    public boolean isAComponent(Object handle) {
        return handle instanceof MComponent;
    }

    /**
     * Recognizer for ComponentInstance.
     *
     * @param handle candidate
     * @return true if handle is a ComponentInstance
     */
    public boolean isAComponentInstance(Object handle) {
        return handle instanceof MComponentInstance;
    }

    /**
     * Recognizer for Constraint.
     *
     * @param handle candidate
     * @return true if handle is a Constraint
     */
    public boolean isAConstraint(Object handle) {
        return handle instanceof MConstraint;
    }

    /**
     * Recognizer for CreateAction.
     *
     * @param handle candidate
     * @return true if handle is a CreateAction
     */
    public boolean isACreateAction(Object handle) {
        return handle instanceof MCreateAction;
    }

    /**
     * Recognizer for DataType.
     *
     * @param handle candidate
     * @return true if handle is a DataType
     */
    public boolean isADataType(Object handle) {
        return handle instanceof MDataType;
    }

    /**
     * Recognizer for DataValue.
     *
     * @param handle candidate
     * @return true if handle is a DataValue
     */
    public boolean isADataValue(Object handle) {
        return handle instanceof MDataValue;
    }

    /**
     * Recognizer for Dependency.
     *
     * @param handle candidate
     * @return true if handle is a Dependency
     */
    public boolean isADependency(Object handle) {
        return handle instanceof MDependency;
    }

    /**
     * Recognizer for DestroyAction.
     *
     * @param handle candidate
     * @return true if handle is a DestroyAction
     */
    public boolean isADestroyAction(Object handle) {
        return handle instanceof MDestroyAction;
    }

    /**
     * Recognizer for CompositeState.
     *
     * @param handle candidate
     * @return true if handle is a CompositeState
     */
    public boolean isACompositeState(Object handle) {
        return handle instanceof MCompositeState;
    }

    /**
     * Recognizer for Element.
     *
     * @param handle candidate
     * @return true if handle is an Element
     */
    public boolean isAElement(Object handle) {
        return handle instanceof MElement;
    }

    /**
     * Recognizer for ElementImport.
     *
     * @param handle candidate
     * @return true if handle is an ElementImport
     */
    public boolean isAElementImport(Object handle) {
        return handle instanceof MElementImport;
    }

    /**
     * Recognizer for ElementListener.
     *
     * @param handle candidate
     * @return true if handle is an ElementListener
     */
    public boolean isAElementListener(Object handle) {
        return handle instanceof MElementListener;
    }

    /**
     * Recognizer for ElementResidence.
     *
     * @param handle candidate
     * @return true if handle is an ElementResidence
     */
    public boolean isAElementResidence(Object handle) {
        return handle instanceof MElementResidence;
    }

    /**
     * Recognizer for Event.
     *
     * @param handle candidate
     * @return true if handle is an Event
     */
    public boolean isAEvent(Object handle) {
        return handle instanceof MEvent;
    }

    /**
     * Recognizer for Exception.
     *
     * @param handle candidate
     * @return true if handle is an Exception
     */
    public boolean isAException(Object handle) {
        return handle instanceof MException;
    }

    /**
     * Recognizer for Expression.
     *
     * @param handle candidate
     * @return true if handle is an Expression
     */
    public boolean isAExpression(Object handle) {
        return handle instanceof MExpression;
    }

    /**
     * Recognizer for Extend.
     *
     * @param handle candidate
     * @return true if handle is an Extend
     */
    public boolean isAExtend(Object handle) {
        return handle instanceof MExtend;
    }

    /**
     * Recognizer for ExtensionPoint.
     *
     * @param handle candidate
     * @return true if handle is an ExtensionPoint
     */
    public boolean isAExtensionPoint(Object handle) {
        return handle instanceof MExtensionPoint;
    }

    /**
     * Recognizer for Feature.
     *
     * @param handle candidate
     * @return true if handle is a Feature
     */
    public boolean isAFeature(Object handle) {
        return handle instanceof MFeature;
    }

    /**
     * Recognizer for FinalState.
     *
     * @param handle candidate
     * @return true if handle is a FinalState
     */
    public boolean isAFinalState(Object handle) {
        return handle instanceof MFinalState;
    }

    /**
     * Recognizer for Flow.
     *
     * @param handle candidate
     * @return true if handle is a Flow
     */
    public boolean isAFlow(Object handle) {
        return handle instanceof MFlow;
    }

    /**
     * Recognizer for Guard.
     *
     * @param handle candidate
     * @return true if handle is a Guard
     */
    public boolean isAGuard(Object handle) {
        return handle instanceof MGuard;
    }

    /**
     * Recognizer for GeneralizableElement.
     *
     * @param handle candidate
     * @return true if handle is a GeneralizableElement
     */
    public boolean isAGeneralizableElement(Object handle) {
        return handle instanceof MGeneralizableElement;
    }

    /**
     * Recognizer for GeneralizableElement.
     *
     * @param handle candidate
     * @return true if handle is a GeneralizableElement
     */
    public boolean isAGeneralization(Object handle) {
        return handle instanceof MGeneralization;
    }

    /**
     * Recognizer for Include.
     *
     * @param handle candidate
     * @return true if handle is an Include
     */
    public boolean isAInclude(Object handle) {
        return handle instanceof MInclude;
    }

    /**
     * Recognizer for Instance.
     *
     * @param handle candidate
     * @return true if handle is a Instance
     */
    public boolean isAInstance(Object handle) {
        return handle instanceof MInstance;
    }

    /**
     * Recognizer for Interaction.
     *
     * @param handle candidate
     * @return true if handle is a Interaction
     */
    public boolean isAInteraction(Object handle) {
        return handle instanceof MInteraction;
    }

    /**
     * Recognizer for Interface.
     *
     * @param handle candidate
     * @return true if handle is a Interface
     */
    public boolean isAInterface(Object handle) {
        return handle instanceof MInterface;
    }

    /**
     * Recognizer for Link.
     *
     * @param handle candidate
     * @return true if handle is a Link
     */
    public boolean isALink(Object handle) {
        return handle instanceof MLink;
    }

    /**
     * Recognizer for LinkEnd.
     *
     * @param handle candidate
     * @return true if handle is a LinkEnd
     */
    public boolean isALinkEnd(Object handle) {
        return handle instanceof MLinkEnd;
    }

    /**
     * Recognizer for Message.
     *
     * @param handle candidate
     * @return true if handle is a Method
     */
    public boolean isAMessage(Object handle) {
        return handle instanceof MMessage;
    }

    /**
     * Recognizer for Method.
     *
     * @param handle candidate
     * @return true if handle is a Method
     */
    public boolean isAMethod(Object handle) {
        return handle instanceof MMethod;
    }

    /**
     * Recognizer for Model.
     *
     * @param handle candidate
     * @return true if handle is a Model
     */
    public boolean isAModel(Object handle) {
        return handle instanceof MModel;
    }

    /**
     * Recognizer for ModelElement.
     *
     * @param handle candidate
     * @return true if handle is a ModelElement
     */
    public boolean isAModelElement(Object handle) {
        return handle instanceof MModelElement;
    }

    /**
     * Recognizer for Multiplicity.
     *
     * @param handle candidate
     * @return true if handle is a Multiplicity
     */
    public boolean isAMultiplicity(Object handle) {
        return handle instanceof MMultiplicity;
    }

    /**
     * Recognizer for MultiplicityRange.
     *
     * @param handle candidate
     * @return true if handle is a MultiplicityRange
     */
    public boolean isAMultiplicityRange(Object handle) {
        return handle instanceof MMultiplicityRange;
    }

    /**
     * Recognizer for Namespace.
     *
     * @param handle candidate
     * @return true if handle is a Namespace
     */
    public boolean isANamespace(Object handle) {
        return handle instanceof MNamespace;
    }

    /**
     * Recognizer for N-ary Association.
     *
     * @param handle candidate
     * @return true if handle is an Association
     */
    public boolean isANaryAssociation(Object handle) {
        if (handle instanceof MAssociation) {
            return (getConnections(handle).size() > 2);
        }
        return false;
    }


    /**
     * Recognizer for a Node.
     *
     * @param handle candidate
     * @return true if handle is a Node
     */
    public boolean isANode(Object handle) {
        return handle instanceof MNode;
    }

    /**
     * Recognizer for a NodeInstance.
     *
     * @param handle candidate
     * @return true if handle is a NodeInstance
     */
    public boolean isANodeInstance(Object handle) {
        return handle instanceof MNodeInstance;
    }

    /**
     * Recognizer for Operation.
     *
     * @param handle candidate
     * @return true if handle is an Operation
     */
    public boolean isAOperation(Object handle) {
        return handle instanceof MOperation;
    }

    /**
     * Recognizer for Object.
     *
     * @param handle candidate
     * @return true if handle is an Object
     */
    public boolean isAObject(Object handle) {
        return handle instanceof MObject;
    }

    /**
     * Recognizer for Parameter.
     *
     * @param handle candidate
     * @return true if handle is a Parameter
     */
    public boolean isAParameter(Object handle) {
        return handle instanceof MParameter;
    }

    /**
     * Recognizer for Partition.
     *
     * @param handle candidate
     * @return true if handle is a Partition
     */
    public boolean isAPartition(Object handle) {
        return handle instanceof MPartition;
    }

    /**
     * Recognizer for Permission.
     *
     * @param handle candidate
     * @return true if handle is an Permission
     */
    public boolean isAPermission(Object handle) {
        return handle instanceof MPermission;
    }

    /**
     * Recognizer for Package.
     *
     * @param handle candidate
     * @return true if handle is a Package
     */
    public boolean isAPackage(Object handle) {
        return handle instanceof MPackage;
    }

    /**
     * Recognizer for Pseudostate.
     *
     * @param handle candidate
     * @return true if handle is a Pseudostate
     */
    public boolean isAPseudostate(Object handle) {
        return handle instanceof MPseudostate;
    }

    /**
     * Recognizer for PseudostateKind.
     *
     * @param handle candidate
     * @return true if handle is a PseudostateKind
     */
    public boolean isAPseudostateKind(Object handle) {
        return handle instanceof MPseudostateKind;
    }

    /**
     * Returns the Kind of a Pseudostate.
     *
     * TODO: - Do we need this as well as getKind - I think not
     *
     * @param handle the Pseudostate
     * @return the Kind
     */
    public Object getPseudostateKind(Object handle) {
        if (handle instanceof MPseudostate) {
            return ((MPseudostate) handle).getKind();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the Kind of a Pseudostate or Parameter.
     *
     * @param handle the Pseudostate or Parameter
     * @return the Kind
     */
    public Object getKind(Object handle) {
        if (handle instanceof MPseudostate) {
            return ((MPseudostate) handle).getKind();
        }
        if (handle instanceof MParameter) {
            return ((MParameter) handle).getKind();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the receiver object of a message or stimulus.
     *
     * @param handle candidate
     * @return receiver
     */
    public Object getReceiver(Object handle) {
        if (handle instanceof MStimulus) {
            return ((MStimulus) handle).getReceiver();
        }
        if (handle instanceof MMessage) {
            return ((MMessage) handle).getReceiver();
        }
        return illegalArgumentObject(handle);
    }

    /**
     * Returns the Link belonging to the given LinkEnd.
     *
     * @param handle the LinkEnd
     * @return the Link
     */
    public Object getLink(Object handle) {
        if (handle instanceof MLinkEnd) {
            return ((MLinkEnd) handle).getLink();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Check whether two pseudostatekinds are equal/of the same type.
     *
     * @return true if the are the same type
     * @param ps1 one kind
     * @param ps2 one kind
     */
    public boolean equalsPseudostateKind(Object ps1, Object ps2) {
        if (isAPseudostateKind(ps1)) {
            return ((MPseudostateKind) ps1).equals(ps2);
        }
	return illegalArgumentBoolean(ps1);
    }

    /**
     * Recognizer for Reception.
     *
     * @param handle candidate
     * @return true if handle is a Reception
     */
    public boolean isAReception(Object handle) {
        return handle instanceof MReception;
    }

    /**
     * Recognizer for Returnaction.
     *
     * @param handle candidate
     * @return true if handle is a returnaction
     */
    public boolean isAReturnAction(Object handle) {
        return handle instanceof MReturnAction;
    }

    /**
     * Recognizer for Relationship.
     *
     * @param handle candidate
     * @return true if handle is a Relationship
     */
    public boolean isARelationship(Object handle) {
        return handle instanceof MRelationship;
    }

    /**
     * Recognizer for SendAction.
     *
     * @param handle candidate
     * @return true if handle is a SendAction
     */
    public boolean isASendAction(Object handle) {
        return handle instanceof MSendAction;
    }

    /**
     * Recognizer for Signal.
     *
     * @param handle candidate
     * @return true if handle is a Signal
     */
    public boolean isASignal(Object handle) {
        return handle instanceof MSignal;
    }

    /**
     * Recognizer for SignalEvent.
     *
     * @param handle candidate
     * @return true if handle is a SignalEvent
     */
    public boolean isASignalEvent(Object handle) {
        return handle instanceof MSignalEvent;
    }

    /**
     * Recognizer for SimpleState.
     *
     * @param handle candidate
     * @return true if handle is a SimpleState
     */
    public boolean isASimpleState(Object handle) {
        return handle instanceof MSimpleState;
    }

    /**
     * Recognizer for StateMachine.
     *
     * @param handle candidate
     * @return true if handle is a StateMachine
     */
    public boolean isAStateMachine(Object handle) {
        return handle instanceof MStateMachine;
    }

    /**
     * Recognizer for stimulus.
     *
     * @param handle candidate
     * @return true if handle is a stimulus
     */
    public boolean isAStimulus(Object handle) {
        return handle instanceof MStimulus;
    }

    /**
     * Recognizer for StateVertex.
     *
     * @param handle candidate
     * @return true if handle is a StateVertex
     */
    public boolean isAStateVertex(Object handle) {
        return handle instanceof MStateVertex;
    }

    /**
     * Recognizer for Stereotype.
     *
     * @param handle candidate
     * @return true if handle is a Stereotype
     */
    public boolean isAStereotype(Object handle) {
        return handle instanceof MStereotype;
    }

    /**
     * Recognizer for StructuralFeature.
     *
     * @param handle candidate
     * @return true if handle is a StructuralFeature
     */
    public boolean isAStructuralFeature(Object handle) {
        return handle instanceof MStructuralFeature;
    }

    /**
     * Recognizer for State.
     *
     * @param handle candidate
     * @return true if handle is a State
     */
    public boolean isAState(Object handle) {
        return handle instanceof MState;
    }

    /**
     * Recognizer for StubState.
     *
     * @param handle candidate
     * @return true if handle is a StubState
     */
    public boolean isAStubState(Object handle) {
        return handle instanceof MStubState;
    }

    /**
     * Recognizer for SubmachineState.
     *
     * @param handle candidate
     * @return true if handle is a SubmachineState
     */
    public boolean isASubmachineState(Object handle) {
        return handle instanceof MSubmachineState;
    }

    /**
     * Recognizer for Subsystem.
     *
     * @param handle candidate
     * @return true if handle is a Subsystem
     */
    public boolean isASubsystem(Object handle) {
        return handle instanceof MSubsystem;
    }

    /**
     * Recognizer for SynchState.
     *
     * @param handle candidate
     * @return true if handle is a SynchState
     */
    public boolean isASynchState(Object handle) {
        return handle instanceof MSynchState;
    }

    /**
     * Recognizer for TaggedValue.
     *
     * @param handle candidate
     * @return true if handle is a TaggedValue
     */
    public boolean isATaggedValue(Object handle) {
        return handle instanceof MTaggedValue;
    }
    
    /**
     * @see org.argouml.model.Facade#isATerminateAction(java.lang.Object)
     */
    public boolean isATerminateAction(Object handle) {
        return handle instanceof MTerminateAction;
    }

    /**
     * Recognizer for Transition.
     *
     * @param handle candidate
     * @return true if handle is a Transition
     */
    public boolean isATransition(Object handle) {
        return handle instanceof MTransition;
    }

    /**
     * Recognizer for TimeEvent.
     *
     * @param handle candidate
     * @return true if handle is a TimeEvent
     */
    public boolean isATimeEvent(Object handle) {
        return handle instanceof MTimeEvent;
    }

    /**
     * @see org.argouml.model.Facade#isAUninterpretedAction(java.lang.Object)
     */
    public boolean isAUninterpretedAction(Object handle) {
        return handle instanceof MUninterpretedAction;
    }
    
    /**
     * Recognizer for Usage.
     *
     * @param handle candidate
     * @return true if handle is a Usage
     */
    public boolean isAUsage(Object handle) {
        return handle instanceof MUsage;
    }

    /**
     * Recognizer for a Use Case.
     *
     * @param handle candidate
     * @return true if handle is a Transition
     */
    public boolean isAUseCase(Object handle) {
        return handle instanceof MUseCase;
    }

    /**
     * Recognizer for VisibilityKind.
     *
     * @param handle candidate
     * @return true if handle is a VisibilityKind
     */
    public boolean isAVisibilityKind(Object handle) {
        return handle instanceof MVisibilityKind;
    }

    /**
     * Recognizer for Classes that are Active.
     *
     * @param handle candidate
     * @return true if Class is Active
     */
    public boolean isActive(Object handle) {
        if (handle instanceof MClass) {
            return ((MClass) handle).isActive();
        }
    return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for attributes that are changeable.
     *
     * @param handle candidate
     * @return true if handle is changeable
     */
    public boolean isChangeable(Object handle) {
        if (handle != null && handle instanceof MStructuralFeature) {
            MChangeableKind changeability =
                ((MStructuralFeature) handle).getChangeability();
            return MChangeableKind.CHANGEABLE.equals(changeability);

        } else if (handle != null && handle instanceof MAssociationEnd) {
            MChangeableKind changeability =
                ((MAssociationEnd) handle).getChangeability();
            return MChangeableKind.CHANGEABLE.equals(changeability);
        }
        // ...
	return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for attributes with classifier scope.
     *
     * @param handle candidate
     * @return true if handle has classifier scope.
     */
    public boolean isClassifierScope(Object handle) {
        if (handle instanceof MAttribute) {
            MAttribute a = (MAttribute) handle;
            return MScopeKind.CLASSIFIER.equals(a.getOwnerScope());
        }
        if (handle instanceof MFeature) {
            MFeature f = (MFeature) handle;
            return MScopeKind.CLASSIFIER.equals(f.getOwnerScope());
        }
        // ...
	return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for concurrent composite state.
     *
     * @param handle composite state
     * @return true if concurent.
     */
    public boolean isConcurrent(Object handle) {
        if (handle instanceof MCompositeState) {
            return ((MCompositeState) handle).isConcurent();
        }
    return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for ConcurrentRegion.
     *
     * @param handle candidate
     * @return true if handle is a ConcurrentRegion
     */
    public boolean isAConcurrentRegion(Object handle) {
        if ((handle instanceof MCompositeState)
                && (getContainer(handle) != null)) {
            return (isConcurrent(getContainer(handle)));
        }
        return false;
    }


    /**
     * @see org.argouml.model.Facade#isConstructor(java.lang.Object)
     */
    public boolean isConstructor(Object handle) {
        MOperation operation = null;
        if (handle instanceof MMethod) {
            operation = ((MMethod) handle).getSpecification();
            if (operation == null) {
                // This is not a well formed model in a strict sense. See the
                // multiplicity in UML 1.3 Figure 2-5.
                return false;
            }
        } else if (handle instanceof MOperation) {
            operation = (MOperation) handle;
        } else {
            return illegalArgumentBoolean(handle);
        }

        Object stereo = null;
        Iterator iter = getStereotypes(operation).iterator();
        while (iter.hasNext()) {
            stereo = iter.next();
            if (nsmodel.getExtensionMechanismsHelper().isStereotypeInh(
                    stereo,
                    "create",
                    "BehavioralFeature")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the given element is Frozen.
     *
     * @param handle candidate
     * @return boolean true if Frozen
     */
    public boolean isFrozen(Object handle) {
        if (handle instanceof MChangeableKind) {
            MChangeableKind ck = (MChangeableKind) handle;
            return MChangeableKind.FROZEN.equals(ck);
        }
	return illegalArgumentBoolean(handle);
    }

    /**
     * Returns true if a given associationend is a composite.
     * @param handle candidate
     * @return boolean
     */
    public boolean isComposite(Object handle) {
        if (isAAssociationEnd(handle)) {
            boolean composite = false;
            MAssociationEnd end = (MAssociationEnd) handle;
            if (end.getAggregation() != null
                && end.getAggregation().equals(MAggregationKind.COMPOSITE)) {
                composite = true;
            }
            return composite;
        }
	return illegalArgumentBoolean(handle);
    }

    /**
     * Returns true if a given associationend is a composite.
     * @param handle candidate
     * @return boolean
     */
    public boolean isAggregate(Object handle) {
        if (isAAssociationEnd(handle)) {
            boolean composite = false;
            MAssociationEnd end = (MAssociationEnd) handle;
            if (end.getAggregation() != null
                && end.getAggregation().equals(MAggregationKind.AGGREGATE)) {
                composite = true;
            }
            return composite;
        }
	return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for attributes that are initialized.
     *
     * @param handle candidate
     * @return true if the attribute is initialized.
     */
    public boolean isInitialized(Object handle) {
        if (handle instanceof MAttribute) {
            MExpression init = ((MAttribute) handle).getInitialValue();

            if (init != null
                && init.getBody() != null
                && init.getBody().trim().length() > 0) {
                return true;
            }
            return false;
        }

        // ...
	return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for attributes with instance scope.
     *
     * @param handle candidate
     * @return true if handle has instance scope.
     */
    public boolean isInstanceScope(Object handle) {
        if (handle instanceof MFeature) {
            MFeature a = (MFeature) handle;
            return MScopeKind.INSTANCE.equals(a.getOwnerScope());
        }
        // ...
	return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for internal transitions.
     *
     * @author mvw
     * @param handle candidate
     * @return true if handle is an internal transition.
     */
    public boolean isInternal(Object handle) {
        if (handle instanceof MTransition) {
            Object state = getState(handle);
            Object end0 = getSource(handle);
            Object end1 = getTarget(handle);
            if (end0 != null) {
                return ((state == end0) && (state == end1));
	    }
        }
        return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for leafs.
     *
     * @param handle candidate GeneralizableElement
     * @return true if handle is a leaf
     */
    public boolean isLeaf(Object handle) {

        if (handle instanceof MGeneralizableElement) {
            return ((MGeneralizableElement) handle).isLeaf();
        }
        if (handle instanceof MOperation) {
            return ((MOperation) handle).isLeaf();
        }
        if (handle instanceof MReception) {
            return ((MReception) handle).isLeaf();
        }
	return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for roots.
     *
     * @param handle candidate GeneralizableElement
     * @return true if handle is a leaf
     */
    public boolean isRoot(Object handle) {

        if (handle instanceof MGeneralizableElement) {
            return ((MGeneralizableElement) handle).isRoot();
        }
        if (handle instanceof MOperation) {
            return ((MOperation) handle).isRoot();
        }
        if (handle instanceof MReception) {
            return ((MReception) handle).isRoot();
        }
	return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for specifications.
     *
     * @param handle candidate ModelElement
     * @return true if handle is a specification
     */
    public boolean isSpecification(Object handle) {

        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).isSpecification();
        }
	return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for Navigable elements.
     *
     * @param handle candidate
     * @return true if handle is navigable
     */
    public boolean isNavigable(Object handle) {
        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd) handle).isNavigable();
        }

        // ...
	return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for primary objects.<p>
     *
     * A primary object is an object that is created by the parser or
     * by a user.
     * Object that are created when importing some other object are not.<p>
     *
     * @param handle candidate
     * @return true if primary object.
     */
    public boolean isPrimaryObject(Object handle) {
        if (handle instanceof MModelElement) {
            MModelElement element = (MModelElement) handle;
            for (Iterator i = element.getTaggedValues().iterator();
		 i.hasNext();
		 ) {
                MTaggedValue tv = (MTaggedValue) i.next();
                if ((GENERATED_TAG).equals(tv.getTag())) {
                    return false;
                }
            }
            return true;
        }
        // ...
	return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for attributes with private.
     *
     * @param handle candidate
     * @return true if handle has private
     */
    public boolean isPrivate(Object handle) {
        if (handle instanceof MModelElement) {
            MModelElement element = (MModelElement) handle;
            return MVisibilityKind.PRIVATE.equals(element.getVisibility());
        }
        // ...
	return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for attributes with public.
     *
     * @param handle candidate
     * @return true if handle has public
     */
    public boolean isPublic(Object handle) {
        if (handle instanceof MModelElement) {
            MModelElement element = (MModelElement) handle;
            return MVisibilityKind.PUBLIC.equals(element.getVisibility());
        }
        // ...
	return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for MBehaviouralFeature's that are queries.
     *
     * @param handle candidate
     * @return true if it is a query
     */
    public boolean isQuery(Object handle) {

        if (handle instanceof MBehavioralFeature) {
	    return ((MBehavioralFeature) handle).isQuery();
        }

	return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for attributes with protected.
     *
     * @param handle candidate
     * @return true if handle has protected
     */
    public boolean isProtected(Object handle) {
        if (handle instanceof MModelElement) {
            MModelElement element = (MModelElement) handle;
            return MVisibilityKind.PROTECTED.equals(element.getVisibility());
        }
        // ...
	return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for realize.
     *
     * @param handle candidate
     * @return true if handle has a realize stereotype
     */
    public boolean isRealize(Object handle) {
        return isStereotype(handle, "realize");
    }

    /**
     * Recognizer for return.
     *
     * @param handle candidate parameter
     * @return true if handle is a return parameter.
     */
    public boolean isReturn(Object handle) {
        if (handle instanceof MParameter) {
            MParameter p = (MParameter) handle;
            return MParameterDirectionKind.RETURN.equals(p.getKind());

        }
        // ...
	return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for singleton.
     *
     * @param handle candidate
     * @return true if handle is a singleton.
     */
    public boolean isSingleton(Object handle) {
        return isStereotype(handle, "singleton");
    }

    /**
     * Recognizer for model elements with a given stereotype.
     *
     * @param handle candidate model element
     * @param stereotypename a string that is the stereotype name.
     * @return true if handle is an object that has the given stereotype.
     */
    public boolean isStereotype(Object handle, String stereotypename) {
        if (handle instanceof MModelElement) {
            MModelElement element = (MModelElement) handle;
            MStereotype meSt = element.getStereotype();

            if (meSt == null) {
                return false;
            }

            String name = meSt.getName();
            if (name == null) {
                return false;
            }

            return name.equalsIgnoreCase(stereotypename);
        }

        // ...
	return illegalArgumentBoolean(handle);
    }

    /**
     * Returns true if the given CompositeState is the top state.
     *
     * @param handle CompositeState
     * @return boolean true if top state
     */
    public boolean isTop(Object handle) {
	if (isACompositeState(handle)) {
            return ((MCompositeState) handle).getStateMachine() != null;
	}
	return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for type.
     *
     * @param handle candidate
     * @return true if handle is a type.
     */
    public boolean isType(Object handle) {
        return isStereotype(handle, "type");
    }

    /**
     * Recognizer for utility.
     *
     * @param handle candidate
     * @return true if handle is a utility.
     */
    public boolean isUtility(Object handle) {
        return isStereotype(handle, "utility");
    }

    ////////////////////////////////////////////////////////////////
    // Recognizer methods for the diagrams (in alphabetic order)

//    /**
//     * Recognizer for Diagram.
//     *
//     * @param handle candidate
//     * @return true if handle is a diagram.
//     */
//    public boolean isADiagram(Object handle) {
//        return handle instanceof Diagram;
//    }

    ////////////////////////////////////////////////////////////////
    // Getters for the UML model (in alphabetic order)

    /**
     * Returns the association connected to an association end or
     * the association belonging to the given link.
     *
     * @param handle is the link
     * @return association end
     */
    public Object getAssociation(Object handle) {

        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd) handle).getAssociation();
        }
        if (handle instanceof MLink) {
        	return ((MLink) handle).getAssociation();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the association end between some classifier and some associaton.
     *
     * @param handle is the classifier
     * @param assoc is the association
     * @return association end
     */
    public Object getAssociationEnd(Object handle, Object assoc) {
        if (handle instanceof MClassifier
	        && assoc instanceof MAssociation) {
	    MClassifier classifier = (MClassifier) handle;

	    Iterator it = classifier.getAssociationEnds().iterator();
	    while (it.hasNext()) {
		MAssociationEnd end = (MAssociationEnd) it.next();
		if (((MAssociation) assoc).getConnections().contains(end)) {
		    return end;
		}
	    }
	    return null;
	}

	return illegalArgumentObject(handle, assoc);
    }

    /**
     * The list of Association Ends.
     *
     * @param handle the object that we get the association ends from.
     * @return Collection with association ends.
     */
    public Collection getAssociationEnds(Object handle) {
        if (handle instanceof MClassifier) {
            Collection endc = ((MClassifier) handle).getAssociationEnds();
            return endc;
        }

        //...
	return illegalArgumentCollection(handle);
    }

    /**
     * The list of association roles.
     *
     * @param handle the object that we get the association roles from.
     * @return Collection of association roles.
     */
    public Collection getAssociationRoles(Object handle) {
        if (handle instanceof MAssociation) {
            return ((MAssociation) handle).getAssociationRoles();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * The list of Attributes.
     *
     * @param handle classifier to examine.
     * @return iterator with attributes.
     */
    public Collection getAttributes(Object handle) {
        if (handle instanceof MClassifier) {
            MClassifier c = (MClassifier) handle;
            return getStructuralFeatures(c);
        }

        // ...
	return illegalArgumentCollection(handle);
    }

    /**
     * The baseclass of some stereotype.
     *
     * @param handle the stereotype
     * @return the baseclass
     */
    public Object getBaseClass(Object handle) {
        if (isAStereotype(handle)) {
            return ((MStereotype) handle).getBaseClass();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * The base of some model element.<p>
     *
     * There is a bug in NSUML which gets the addition and base
     * relationships back to front for include relationships. Solve
     * by reversing their accessors in the code.
     *
     * @param handle the model element
     * @return the base
     */
    public Object getBase(Object handle) {
        if (handle instanceof MAssociationEndRole) {
            return ((MAssociationEndRole) handle).getBase();
        } else if (handle instanceof MAssociationRole) {
            return ((MAssociationRole) handle).getBase();
        } else if (handle instanceof MExtend) {
            return ((MExtend) handle).getBase();
        } else if (handle instanceof MInclude) {
            // See issue 2034
            return ((MInclude) handle).getAddition();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Get the bases of a classifier role.
     *
     *
     * @param handle classifier role.
     * @return the bases.
     */
    public Collection getBases(Object handle) {
        if (handle instanceof MClassifierRole) {
            return ((MClassifierRole) handle).getBases();
	}
	return illegalArgumentCollection(handle);
    }

    /**
     * Get the behaviors of a Modelelement.
     *
     *
     * @param handle modelelement to examine.
     * @return the behaviors.
     */
    public Collection getBehaviors(Object handle) {
        if (isAModelElement(handle)) {
            return ((MModelElement) handle).getBehaviors();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Get the behavioral feature of an parameter.
     *
     * @param handle expression.
     * @return the behavioral feature.
     */
    public Object getBehavioralFeature(Object handle) {
        if (handle instanceof MParameter) {
            return ((MParameter) handle).getBehavioralFeature();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * @see org.argouml.model.Facade#getBody(java.lang.Object)
     */
    public Object getBody(Object handle) {
        if (handle instanceof MMethod) {
            return ((MMethod) handle).getBody();
        }
        if (handle instanceof MConstraint) {
            return ((MConstraint) handle).getBody();
        }
        if (handle instanceof MExpression) {
            return ((MExpression) handle).getBody();
        }
        if (handle instanceof MComment) {
            /* In UML 1.3, get it from the name.
             * From UML 1.4, get it from the body.*/
            return ((MComment) handle).getName();
        }
	return illegalArgumentObject(handle);
    }

    /**
     *  Return the Synch State's bound.
     *  @param handle the synch State
     *  @return bound
     */
    public int getBound(Object handle) {
        if (handle instanceof MSynchState) {
            return ((MSynchState) handle).getBound();
        }
        return illegalArgumentInt(handle);
    }

    /**
     * Return Changeability of a StructuralFeature or a AssociationEnd.
     *
     * @param handle the StructuralFeature or AssociationEnd
     * @return the Changeability
     */
    public Object getChangeability(Object handle) {
        if (handle instanceof MStructuralFeature) {
            return ((MStructuralFeature) handle).getChangeability();
        }
        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd) handle).getChangeability();
        }
	return illegalArgumentObject(handle);
    }


    /**
     * Get the child of a generalization.
     *
     * @param handle generalization.
     * @return the child.
     */
    public Object getChild(Object handle) {
        if (handle instanceof MGeneralization) {
            return ((MGeneralization) handle).getChild();
        }

        // ...
	return illegalArgumentObject(handle);
    }

    /**
     * Get the children of some generalizable element.
     *
     * @param handle to the generalizable element.
     * @return a collection with all children.
     */
    public Collection getChildren(Object handle) {
        if (isAGeneralizableElement(handle)) {
            return ((MGeneralizableElement) handle).getChildren();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Gets the classifiers roles of some model element.
     *
     * @param handle the model element
     * @return the classifiers roles of the instance
     */
    public Collection getClassifierRoles(Object handle) {
        if (handle instanceof MFeature) {
            return ((MFeature) handle).getClassifierRoles();
        }
        if (handle instanceof MClassifier) {
            return ((MClassifier) handle).getClassifierRoles();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Get the classifier of an Association End.
     *
     * @param handle The Association End to get from.
     * @return The classifier of the Association End.
     */
    public Object getClassifier(Object handle) {
        if (isAAssociationEnd(handle)) {
            return ((MAssociationEnd) handle).getType();
        }
        return illegalArgumentObject(handle);
    }


    /**
     * Gets the classifierss of some instance.
     *
     * @param handle the instance
     * @return the classifierss of the instance
     */
    public Collection getClassifiers(Object handle) {
        if (handle instanceof MInstance) {
            return ((MInstance) handle).getClassifiers();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Gets the classifiers in state of some model element.
     *
     * @param handle the model element
     * @return the classifierss in state
     */
    public Collection getClassifiersInState(Object handle) {
        if (handle instanceof MClassifier) {
            return ((MClassifier) handle).getClassifiersInState();
        }
        if (handle instanceof MState) {
            return ((MState) handle).getClassifiersInState();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Gets the clients of some dependency.
     *
     * @param handle the dependency
     * @return the clients of the dependency
     */
    public Collection getClients(Object handle) {
        if (isADependency(handle)) {
            return ((MDependency) handle).getClients();
        }
        return illegalArgumentCollection(handle);
    }

    /**
     * Get the client dependencies of some classifier.
     *
     * @param handle to the classifier.
     * @return an iterator with all client dependencies.
     */
    public Collection getClientDependencies(Object handle) {
        if (isAModelElement(handle)) {
            Collection c = ((MModelElement) handle).getClientDependencies();
            return c;
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Get the condition of an extend.
     *
     * @param handle The extend.
     * @return the condition
     */
    public Object getCondition(Object handle) {
        if (handle instanceof MExtend) {
            return ((MExtend) handle).getCondition();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Get the concurrency of an operation.
     *
     * @param handle The operation.
     * @return the concurrency.
     */
    public Object getConcurrency(Object handle) {
        if (handle instanceof MOperation) {
            return ((MOperation) handle).getConcurrency();
        }
	return illegalArgumentObject(handle);
    }

    //    public short getConcurrency(Object handle) {
    //        if (handle != null && handle instanceof MOperation) {
    //            return ((MOperation) handle).getConcurrency()
    //                == MCallConcurrencyKind.GUARDED
    //                ? GUARDED
    //                : SEQUENTIAL;
    //        }
    //        illegalArgument(handle);
    //        return (short) 0;
    //    }

    /**
     * The list of connections to an association or link.
     *
     * @param handle to the association or link
     * @return a Collection with all connections.
     */
    public Collection getConnections(Object handle) {
        if (handle instanceof MAssociation) {
            return ((MAssociation) handle).getConnections();
        }
        if (handle instanceof MLink) {
            return ((MLink) handle).getConnections();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the effect of some transition.
     *
     * @param handle is the transition
     * @return the effect
     */
    public Object getEffect(Object handle) {
        if (handle instanceof MTransition) {
            return ((MTransition) handle).getEffect();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Get the residences of an element.
     *
     * @param handle the model element that we are getting the residences of
     * @return the residence collection
     */
    public Collection getElementResidences(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getElementResidences();
        }
        // ...
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the ElementImports of this ModelElement.
     *
     * @param handle the ModelElement
     * @return the collection of ElementImports
     */
    public Collection getElementImports2(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getElementImports2();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the entry action to a state.
     *
     * @param handle is the state
     * @return the entry
     */
    public Object getEntry(Object handle) {
        if (handle instanceof MState) {
            return ((MState) handle).getEntry();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the exit action to a state.
     *
     * @param handle is the state
     * @return the exit action
     */
    public Object getExit(Object handle) {
        if (handle instanceof MState) {
            return ((MState) handle).getExit();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Get the Expression belonging to an Argument, Guard, ChangeEvent or timeEvent.
     *
     * @param handle the Object to get the Expression from
     * @return Object the Expression
     */
    public Object getExpression(Object handle) {
        if (handle instanceof MGuard) {
            return ((MGuard) handle).getExpression();
	}
        if (handle instanceof MChangeEvent) {
            return ((MChangeEvent) handle).getChangeExpression();
        }
        if (handle instanceof MTimeEvent) {
            return ((MTimeEvent) handle).getWhen();
        }
        if (handle instanceof MArgument) {
            return ((MArgument)handle).getValue();
        }
        return illegalArgumentObject(handle);
    }

    /**
     * Returns all extends of a use case or extension point.
     *
     * @param handle is the use case or the extension point
     * @return the extends
     */
    public Collection getExtends(Object handle) {
        if (handle instanceof MUseCase) {
            return ((MUseCase) handle).getExtends();
        }
        if (handle instanceof MExtensionPoint) {
            return ((MExtensionPoint) handle).getExtends();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns all extends of a use case.
     *
     * @param handle is the use case
     * @return the extends
     */
    public Collection getExtends2(Object handle) {
        if (handle instanceof MUseCase) {
            return ((MUseCase) handle).getExtends2();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Gets the use case extension of an extend.
     *
     * @param handle is the extend
     * @return The extension
     */
    public Object getExtension(Object handle) {
        if (handle instanceof MExtend) {
            return ((MExtend) handle).getExtension();
	}
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the Extensionpoint at given index-number.
     *
     * @param handle Extend
     * @param index int
     * @return ExtensionPoint
     */
    public Object getExtensionPoint(Object handle, int index) {
        if (handle instanceof MExtend) {
            return ((MExtend) handle).getExtensionPoint(index);
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns all extends of a use case.
     *
     * @param handle is the use case or the extend
     * @return the extends
     */
    public Collection getExtensionPoints(Object handle) {
        if (handle instanceof MUseCase) {
            return ((MUseCase) handle).getExtensionPoints();
        }
        if (handle instanceof MExtend) {
            return ((MExtend) handle).getExtensionPoints();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * The list of Features from a Classifier.
     *
     * @param handle Classifier to retrieve from.
     * @return Collection with Features
     */
    public Collection getFeatures(Object handle) {
        if (handle instanceof MClassifier) {
            return ((MClassifier) handle).getFeatures();
	}
	return illegalArgumentCollection(handle);
    }

    /**
     * Gets the generalization between two generalizable elements.
     * Returns null if there is none.
     *
     * @param handle is the child
     * @param parent is the parent
     * @return The generalization
     */
    public Object getGeneralization(Object handle, Object parent) {
        if (handle instanceof MGeneralizableElement
	        && parent instanceof MGeneralizableElement) {
	    Iterator it = getGeneralizations(handle).iterator();
	    while (it.hasNext()) {
		MGeneralization gen = (MGeneralization) it.next();
		if (gen.getParent() == parent) {
		    return gen;
		}
	    }
	    return null;
	}

	return illegalArgumentObject(handle, parent);
    }

    /**
     * The list of Generalizations from a GeneralizableElement.
     *
     * @param handle GeneralizableElement to retrieve from.
     * @return Generalizations
     */
    public Collection getGeneralizations(Object handle) {
        if (handle instanceof MGeneralizableElement) {
            MGeneralizableElement ge = (MGeneralizableElement) handle;
            return ge.getGeneralizations();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Gets the guard for some given transition.
     *
     * @param handle is the transition
     * @return Object
     */
    public Object getGuard(Object handle) {
        if (isATransition(handle)) {
            return ((MTransition) handle).getGuard();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the Icon of a Stereotype.
     *
     * @param handle the Stereotype to get the Icon from
     * @return the Icon
     */
    public Object getIcon(Object handle) {
        if (handle instanceof MStereotype) {
            return ((MStereotype) handle).getIcon();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Gets the component of some element residence.
     *
     * @param handle is an element residence
     * @return component
     */
    public Object getImplementationLocation(Object handle) {
        if (handle instanceof MElementResidence) {
            return ((MElementResidence) handle).getImplementationLocation();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the includes for some use case.
     *
     * @param handle is the use case
     * @return the includes as a Collection
     */
    public Collection getIncludes(Object handle) {
        if (handle instanceof MUseCase) {
            return ((MUseCase) handle).getIncludes();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the includes for some use case.
     *
     * @param handle is the use case
     * @return the includes as a Collection
     */
    public Collection getIncludes2(Object handle) {
        if (handle instanceof MUseCase) {
            return ((MUseCase) handle).getIncludes2();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the incoming transitions for some statevertex.
     *
     * @param handle is the state vertex
     * @return Collection
     */
    public Collection getIncomings(Object handle) {
        if (isAStateVertex(handle)) {
            return ((MStateVertex) handle).getIncomings();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the initial value for some attribute.
     *
     * @param handle is the attribute
     * @return initial value
     */
    public Object getInitialValue(Object handle) {
        if (handle instanceof MAttribute) {
            return ((MAttribute) handle).getInitialValue();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the instance of an AttributeLink or LinkEnd.
     *
     * @param handle is the attribute link or link end
     * @return initial value
     */
    public Object getInstance(Object handle) {
        if (handle instanceof MAttributeLink) {
            return ((MAttributeLink) handle).getInstance();
        }
        if (handle instanceof MLinkEnd) {
            return ((MLinkEnd) handle).getInstance();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the Instances for some Clasifier.
     *
     * @param handle is the classifier
     * @return Collection
     */
    public Collection getInstances(Object handle) {
        if (handle instanceof MClassifier) {
            return ((MClassifier) handle).getInstances();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the collection of States for some ClasifierInState.
     *
     * @param handle is the classifierInState
     * @return Collection
     */
    public Collection getInStates(Object handle) {
        if (handle instanceof MClassifierInState) {
            return ((MClassifierInState) handle).getInStates();
        }
    return illegalArgumentCollection(handle);
    }

    /**
     * Returns the interaction for some message.
     *
     * @param handle is the message
     * @return the interaction
     */
    public Object getInteraction(Object handle) {
        if (handle instanceof MMessage) {
            return ((MMessage) handle).getInteraction();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the interactions belonging to a collaboration.
     *
     * @param handle is the collaboration
     * @return Collection
     */
    public Collection getInteractions(Object handle) {
        if (handle instanceof MCollaboration) {
            return ((MCollaboration) handle).getInteractions();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the internal transitions belonging to a state.
     *
     * @param handle is the state
     * @return Collection
     */
    public Collection getInternalTransitions(Object handle) {
        if (handle instanceof MState) {
            return ((MState) handle).getInternalTransitions();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the messages belonging to some interaction.
     *
     * @param handle candidate
     * @return Collection
     */
    public Collection getMessages(Object handle) {
        if (isAInteraction(handle)) {
            return ((MInteraction) handle).getMessages();
        }
        if (handle instanceof MAssociationRole) {
            return ((MAssociationRole) handle).getMessages();
        }
        if (handle instanceof MAction) {
            return ((MAction) handle).getMessages();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the messages belonging to some other message.
     *
     * @param handle is the message
     * @return Collection
     */
    public Collection getMessages3(Object handle) {
        if (handle instanceof MMessage) {
            return ((MMessage) handle).getMessages3();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Get the messages that are activated by the given message.
     *
     * @param handle Message
     * @return the Collection of Messages
     */
    public Collection getMessages4(Object handle) {
        if (handle instanceof MMessage) {
            return ((MMessage) handle).getMessages4();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the messages received by the given classifier role.
     *
     * @param handle is the classifier role
     * @return Collection
     */
    public Collection getMessages1(Object handle) {
        if (handle instanceof MClassifierRole) {
            return ((MClassifierRole) handle).getMessages1();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the messages send by the given classifier role.
     *
     * @param handle is the classifier role
     * @return Collection
     */
    public Collection getMessages2(Object handle) {
        if (handle instanceof MClassifierRole) {
            return ((MClassifierRole) handle).getMessages2();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Get the model of some model element.
     *
     * @param handle to the model element.
     * @return model for the model element.
     */
    public Object getModel(Object handle) {
        if (isAModelElement(handle)) {
            MModel m = ((MModelElement) handle).getModel();
            return m;
        }
	return illegalArgumentObject(handle);
    }

    /**
     * @param handle an Element Import.
     * @return the model element
     */
    public Object getModelElement(Object handle) {
        if (handle instanceof MElementImport) {
	    return ((MElementImport) handle).getModelElement();
	}
	return illegalArgumentObject(handle);
    }

    /**
     * Get the Multiplicity from a model element.
     *
     * @param handle model element to retrieve from.
     * @return multiplicity
     */
    public Object getMultiplicity(Object handle) {
        if ((handle instanceof MAssociationEnd)) {
            return ((MAssociationEnd) handle).getMultiplicity();
	}
        if ((handle instanceof MAssociationRole)) {
            return ((MAssociationRole) handle).getMultiplicity();
	}
        if ((handle instanceof MClassifierRole)) {
            return ((MClassifierRole) handle).getMultiplicity();
	}
        if ((handle instanceof MStructuralFeature)) {
            return ((MStructuralFeature) handle).getMultiplicity();
	}
	return illegalArgumentObject(handle);
    }

    /**
     * Get the Ranges from a Multiplicity.
     *
     * @param handle multiplicity to retrieve from.
     * @return iterator containing ranges
     */
    public Iterator getRanges(Object handle) {
        if ((handle instanceof MMultiplicity)) {
	    Collection c = ((MMultiplicity) handle).getRanges();
	    if (c == null) {
		return null;
	    }
	    return c.iterator();
        }
        illegalArgument(handle);
        return null;
    }

    /**
     * Get the comments of an element.
     *
     * @param handle the model element that we are getting the comments of
     * @return the comment (or null)
     */
    public Collection getComments(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getComments();
	}
        // ...
	return illegalArgumentCollection(handle);
    }


    /**
     * Get the modelelement that were commented.
     *
     * @param handle the comment that we are getting the model elements of
     * @return the modelelements (or null)
     */
    public Collection getAnnotatedElements(Object handle) {
        if (handle instanceof MComment) {
            return ((MComment) handle).getAnnotatedElements();
        }
        return illegalArgumentCollection(handle);
    }


    /**
     * Get the communication connection of an message.
     *
     * @param handle the message that we are getting the communication
     * connection
     * @return the communication connection
     */
    public Object getCommunicationConnection(Object handle) {
        if (handle instanceof MMessage) {
            return ((MMessage) handle).getCommunicationConnection();
	}
        // ...
	return illegalArgumentObject(handle);
    }

    /**
     * Get the communication link of a stimulus.
     *
     * @param handle the message that we are getting the communication link
     * @return the communication link
     */
    public Object getCommunicationLink(Object handle) {
        if (handle instanceof MStimulus) {
            return ((MStimulus) handle).getCommunicationLink();
	}
        // ...
	return illegalArgumentObject(handle);
    }

    /**
     * Get the collaborations of an element.
     *
     * @param handle the model element that we are getting the
     * collaborations of.
     * @return the collaborations
     */
    public Collection getCollaborations(Object handle) {
        if (handle instanceof MOperation) {
            return ((MOperation) handle).getCollaborations();
	}
        if (handle instanceof MClassifier) {
            return ((MClassifier) handle).getCollaborations();
	}
        // ...
	return illegalArgumentCollection(handle);
    }

    /**
     * Get the component instance of an instance.
     *
     * @param handle is the instance
     * @return the component instance
     */
    public Object getComponentInstance(Object handle) {
        if (handle instanceof MInstance) {
            return ((MInstance) handle).getComponentInstance();
	}
        // ...
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the collection of ConstrainingElements of a Collaboration.
     *
     * @param handle the Collaboration
     * @return the collection of ConstrainingElements
     */
    public Collection getConstrainingElements(Object handle) {
        if (handle instanceof MCollaboration) {
            return ((MCollaboration) handle).getConstrainingElements();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the collection of ConstrainedElements of a constraint.
     *
     * @param handle the Constraint
     * @return the collection of ConstrainedElements
     */
    public Collection getConstrainedElements(Object handle) {
        if (handle instanceof MConstraint) {
            return ((MConstraint) handle).getConstrainedElements();
        }
        return illegalArgumentCollection(handle);
    }

    /**
     * Get the collection of all constraints of the given ModelElement.
     *
     * @param handle the ModelElement
     * @return the collection of all constraints
     */
    public Collection getConstraints(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getConstraints();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the container for the given modelelement. The container is the
     * owner of the modelelement. It will be null for elements that don't have
     * an owner. All elements except for the root element in a project should
     * have an owner. The root element is allways a model.<p>
     *
     * In  the future, this function could return the container of Figs too.
     *
     * @param handle is the base
     * @return Object
     */
    public Object getModelElementContainer(Object handle) {
        if (handle instanceof MBase) {
            return ((MBase) handle).getModelElementContainer();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the CompositeState that is the container of
     * the given StateVertex.
     *
     * @param handle the StateVertex
     * @return the CompositeState that is the container
     */
    public Object getContainer(Object handle) {
        if (handle instanceof MStateVertex) {
            return ((MStateVertex) handle).getContainer();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the collection of ModelElements contained in a Partition.
     *
     * @param handle the Partition
     * @return the contents of the Partition
     */
    public Collection getContents(Object handle) {
        if (handle instanceof MPartition) {
            return ((MPartition) handle).getContents();
        }
        return illegalArgumentCollection(handle);
    }

    /**
     * Returns the context of some given statemachine or the context
     * of some given interaction.
     *
     * @param handle the statemachine or the interaction
     * @return the context of the statemachine or interaction or null
     * if the statemachine or interaction doesn't have a context.
     */
    public Object getContext(Object handle) {
        if (isAStateMachine(handle)) {
            return ((MStateMachine) handle).getContext();
        }
        if (isAInteraction(handle)) {
            return ((MInteraction) handle).getContext();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Return the collection of the Contexts of a given Signal.
     *
     * @param handle the Signal
     * @return a collection of the Contexts
     */
    public Collection getContexts(Object handle) {
        if (handle instanceof MSignal) {
            return ((MSignal) handle).getContexts();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Return the collection of Actions that create/instantiate
     * the given Classifier.
     *
     * @param handle the Classifier
     * @return a collection containing all the creating actions
     */
    public Collection getCreateActions(Object handle) {
        if (handle instanceof MClassifier) {
            return ((MClassifier) handle).getCreateActions();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Get the default value of a parameter.
     *
     * @param handle the parameter that we are getting the defaultvalue from
     * @return the default value
     */
    public Object getDefaultValue(Object handle) {
        if (handle instanceof MParameter) {
            return ((MParameter) handle).getDefaultValue();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Get deferrable events of a state.
     *
     * @param handle the state that we are getting the deferrable event from
     * @return the deferrable events collection
     */
    public Collection getDeferrableEvents(Object handle) {
        if (handle instanceof MState) {
            return ((MState) handle).getDeferrableEvents();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the context of some given statemachine or the context
     * of some given interaction.
     *
     * @param handle the statemachine or the interaction
     * @return the context of the statemachine or interaction or null
     * if the statemachine or interaction doesn't have a context.
     */
    public Collection getDeploymentLocations(Object handle) {
        if (isAComponent(handle)) {
            return ((MComponent) handle).getDeploymentLocations();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Get the discriminator.
     *
     * @param handle the Generalization
     * @return the discriminator a String
     */
    public Object getDiscriminator(Object handle) {
        if (handle instanceof MGeneralization) {
            return ((MGeneralization) handle).getDiscriminator();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Get the dispatchaction of a stimulus.
     *
     * @param handle the stimulus that we are getting the dispatchaction of
     * @return the dispatchaction (or null)
     */
    public Object getDispatchAction(Object handle) {
        if (handle instanceof MStimulus) {
            return ((MStimulus) handle).getDispatchAction();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the do activity action of a state.
     *
     * @param handle is the state
     * @return the do activity
     */
    public Object getDoActivity(Object handle) {
        if (handle instanceof MState) {
            return ((MState) handle).getDoActivity();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Return the Links of a given Association.
     *
     * @param handle the Association
     * @return the collection of Links
     */
    public Collection getLinks(Object handle) {
        if (handle instanceof MAssociation) {
            return ((MAssociation) handle).getLinks();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Return the LinkEnds of a given Instance or AssociationEnd.
     *
     * @param handle the candidate
     * @return the collection of LinkEnds
     */
    public Collection getLinkEnds(Object handle) {
        if (handle instanceof MInstance) {
            return ((MInstance) handle).getLinkEnds();
        }
        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd) handle).getLinkEnds();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Gets a location of some extension point.
     *
     * @param handle extension point
     * @return the location
     */
    public String getLocation(Object handle) {
        if (handle instanceof MExtensionPoint) {
            return ((MExtensionPoint) handle).getLocation();
        }
	return illegalArgumentString(handle);
    }

    /**
     * Get the methods of an operation.
     *
     * @param handle the operation that we are getting the methods of
     * @return methods collection (or null)
     */
    public Collection getMethods(Object handle) {
        if (handle instanceof MOperation) {
            return ((MOperation) handle).getMethods();
        }
        // ...
	return illegalArgumentCollection(handle);
    }

    /**
     * Get the namespace of an element.
     *
     * @param handle the model element that we are getting the namespace of
     * @return the namespace (or null)
     */
    public Object getNamespace(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getNamespace();
        }
        // ...
	return illegalArgumentObject(handle);
    }

    /**
     * Get the node instance of a component instance.
     *
     * @param handle the model element that we are getting the node instance of
     * @return the node instance
     */
    public Object getNodeInstance(Object handle) {
        if (handle instanceof MComponentInstance) {
            return ((MComponentInstance) handle).getNodeInstance();
        }
        // ...
	return illegalArgumentObject(handle);
    }

    /**
     * The collection of object flow states.
     *
     * @param handle the classifier
     * @return collection of object flow states
     */
    public Collection getObjectFlowStates(Object handle) {
        if (handle instanceof MClassifier) {
            return ((MClassifier) handle).getObjectFlowStates();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Get the operation of a Call Action or Call Event.
     *
     * @param handle the model element that we are getting the operation of
     * @return the Operation
     */
    public Object getOperation(Object handle) {
        if (handle instanceof MCallAction) {
            return ((MCallAction) handle).getOperation();
        }
        if (handle instanceof MCallEvent) {
            return ((MCallEvent) handle).getOperation();
        }
        // ...
	return illegalArgumentObject(handle);
    }

    /**
     * Get the occurrences of an operation.
     * @param handle the Opration
     * @return the collection of occurrences
     */
    public Collection getOccurrences(Object handle) {
        if (handle instanceof MOperation) {
            return ((MOperation) handle).getOccurrences();
        }
        return illegalArgumentCollection(handle);
    }

    /**
     * Get the list of operations.
     *
     * @param handle classifier to examine.
     * @return Collection with operations.
     */
    public Collection getOperations(Object handle) {
        if (handle instanceof MClassifier) {
            MClassifier c = (MClassifier) handle;
            return getOperations(c);
        }

        // ...
	return illegalArgumentCollection(handle);
    }

    /**
     * Get the list of Operations of this classifier and all inherited.
     *
     * @param handle classifier to examine.
     * @return Iterator with operations.
     */
    public Iterator getOperationsInh(Object handle) {
        if (handle instanceof MClassifier) {
            MClassifier c = (MClassifier) handle;

            // TODO: We are converting back and forth between collections and
            // iterators. I (Linus) prefer iterators.
            return nsmodel.getCoreHelper().getOperationsInh(c).iterator();
        }

        // ...
	illegalArgument(handle);
	return null;
    }

    /**
     * Returns the opposite end of an association end.
     *
     * @param handle is the association end
     * @return Object the opposite end.
     */
    public Object getOppositeEnd(Object handle) {
        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd) handle).getOppositeEnd();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Get ordering of an association end.
     *
     * @param handle association end to retrieve from
     * @return ordering
     */
    public Object getOrdering(Object handle) {
        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd) handle).getOrdering();
        }

        // ...
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the list of Transitions outgoing from the given stateVertex.
     *
     * @param handle statevertex
     * @return Collection
     */
    public Collection getOutgoings(Object handle) {
        if (isAStateVertex(handle)) {
            return ((MStateVertex) handle).getOutgoings();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Get the list of Associations Ends connected to this association end.
     *
     * @param handle association end to start from
     * @return Iterator with all connected association ends.
     */
    public Collection getOtherAssociationEnds(Object handle) {
        if (handle instanceof MAssociationEnd) {
            MAssociation a = ((MAssociationEnd) handle).getAssociation();

            if (a == null) {
                return emptyCollection();
            }

            Collection allEnds = a.getConnections();
            if (allEnds == null) {
                return emptyCollection();
            }

            // TODO: An Iterator filter would be nice here instead of the
            // mucking around with the Collection.
            allEnds = new ArrayList(allEnds);
            allEnds.remove(handle);
            return allEnds;
        }

        // ...
	return illegalArgumentCollection(handle);
    }
    
    /**
     * Get the list of Link Ends connected to this link end.
     *
     * @param handle link end to start from
     * @return Iterator with all connected link ends.
     */
    public Collection getOtherLinkEnds(Object handle) {
        if (handle instanceof MLinkEnd) {
            MLink link = ((MLinkEnd) handle).getLink();

            if (link == null) {
                return emptyCollection();
            }

            Collection allEnds = link.getConnections();
            if (allEnds == null) {
                return emptyCollection();
            }

            // TODO: An Iterator filter would be nice here instead of the
            // mucking around with the Collection.
            allEnds = new ArrayList(allEnds);
            allEnds.remove(handle);
            return allEnds;
        }

        // ...
    return illegalArgumentCollection(handle);
    }

    /**
     * The list of owned elements of the the package.
     *
     * @param handle package to retrieve from.
     * @return Iterator with operations
     */
    public Collection getOwnedElements(Object handle) {
        if (handle instanceof MNamespace) {
            return ((MNamespace) handle).getOwnedElements();
        }

        // ...
	return illegalArgumentCollection(handle);
    }

    /**
     * Get the owner scope of a feature.
     *
     * @param handle feature
     * @return owner scope
     */
    public Object getOwnerScope(Object handle) {
        if (handle instanceof MFeature) {
            return ((MFeature) handle).getOwnerScope();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Get the powertype of a generalization.
     *
     * @param handle generalization
     * @return powertype
     */
    public Object getPowertype(Object handle) {
        if (handle instanceof MGeneralization) {
            return ((MGeneralization) handle).getPowertype();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Get the powertype ranges of a classifier.
     *
     * @param handle classifier to retrieve from
     * @return collection of poertype ranges
     */
    public Collection getPowertypeRanges(Object handle) {
        if (handle instanceof MClassifier) {
            return ((MClassifier) handle).getPowertypeRanges();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Get the predecessors of a message.
     *
     * @param handle message to retrieve from
     * @return collection of predecessors
     */
    public Collection getPredecessors(Object handle) {
        if (handle instanceof MMessage) {
            return ((MMessage) handle).getPredecessors();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Get the qualified attributes of an association end.
     *
     * @param handle association end to retrieve from
     * @return Collection with qualifiers.
     */
    public Collection getQualifiers(Object handle) {
        if (handle instanceof MAssociationEnd) {
            Collection qualifiers = ((MAssociationEnd) handle).getQualifiers();
            return qualifiers;
        }
        return illegalArgumentCollection(handle);
    }

    /**
     * Determine if the passed parameter has a RETURN direction kind.
     *
     * @return true if it is a return direction kind
     * @param handle is the parameter
     */
    public boolean hasReturnParameterDirectionKind(Object handle) {
        if (handle instanceof MParameter) {
	    MParameter parameter = (MParameter) handle;
	    return (MParameterDirectionKind.RETURN.equals(parameter.getKind()));
	}
	return illegalArgumentBoolean(handle);
    }

    /**
     * Returns the Package that is connected by the given ElementImport.
     *
     * @param handle the ElementImport
     * @return the Package
     */
    public Object getPackage(Object handle) {
        if (handle instanceof MElementImport) {
            return ((MElementImport) handle).getPackage();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Get a parameter of a behavioral feature.
     *
     * @param handle behavioral feature to retrieve from
     * @param n parameter number
     * @return parameter.
     */
    public Object getParameter(Object handle, int n) {
        if (handle instanceof MBehavioralFeature) {
            return ((MBehavioralFeature) handle).getParameter(n);
	}
	return illegalArgumentObject(handle);
    }

    /**
     * Get the parameters of a Object Flow State, Behavioral Feature,
     * Classifier or Event.
     *
     * @param handle operation to retrieve from
     * @return Iterator with operations.
     */
    public Collection getParameters(Object handle) {
        if (handle instanceof MObjectFlowState) {
            return ((MObjectFlowState) handle).getParameters();
        }
        if (handle instanceof MBehavioralFeature) {
            return ((MBehavioralFeature) handle).getParameters();
        }
        if (handle instanceof MEvent) {
            return ((MEvent) handle).getParameters();
        }
        if (handle instanceof MClassifier) {
            return ((MClassifier) handle).getParameters();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Get the parent of a generalization.
     *
     * @param handle generalization.
     * @return the parent.
     */
    public Object getParent(Object handle) {
        if (handle instanceof MGeneralization) {
            return ((MGeneralization) handle).getParent();
        }

        // ...
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the raised signals of an operation.
     *
     * @param handle is the operation
     * @return raised signals
     */
    public Collection getRaisedSignals(Object handle) {
        if (handle instanceof MOperation) {
            return ((MOperation) handle).getRaisedSignals();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the receptions of a signal.
     *
     * @param handle is the signal
     * @return receptions
     */
    public Collection getReceptions(Object handle) {
        if (handle instanceof MSignal) {
            return ((MSignal) handle).getReceptions();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the recurrence iteration expression of an action.
     *
     * @param handle is the action.
     * @return the recurrence
     */
    public Object getRecurrence(Object handle) {
        if (handle instanceof MAction) {
            return ((MAction) handle).getRecurrence();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the represented classifier of a collaboration.
     *
     * @param handle is the collaboration
     * @return represented classifier
     */
    public Object getRepresentedClassifier(Object handle) {
        if (handle instanceof MCollaboration) {
            return ((MCollaboration) handle).getRepresentedClassifier();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the represented operation of a collaboration.
     *
     * @param handle is the collaboration
     * @return represented operation
     */
    public Object getRepresentedOperation(Object handle) {
        if (handle instanceof MCollaboration) {
            return ((MCollaboration) handle).getRepresentedOperation();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the script belonging to a given action.
     *
     * @param handle is the action
     * @return the script
     */
    public Object getScript(Object handle) {
        if (handle instanceof MAction) {
            return ((MAction) handle).getScript();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the sender object of a stimulus or a message.
     *
     * @param handle is the stimulus or message
     * @return the sender
     */
    public Object getSender(Object handle) {
        if (handle instanceof MStimulus) {
            return ((MStimulus) handle).getSender();
        }
        if (handle instanceof MMessage) {
            return ((MMessage) handle).getSender();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the sender object of a stimulus or a message.
     *
     * TODO: Check if this javadoc comment is really correct?
     *
     * @param handle is the object
     * @return the signal
     */
    public Object getSignal(Object handle) {
        if (handle instanceof MSendAction) {
            return ((MSendAction) handle).getSignal();
        }
        if (handle instanceof MSignalEvent) {
            return ((MSignalEvent) handle).getSignal();
        }
        if (handle instanceof MReception) {
            return ((MReception) handle).getSignal();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Get the resident element.
     *
     * @param handle is the element residence
     * @return resident element
     */
    public Object getResident(Object handle) {
        if (handle instanceof MElementResidence) {
            return ((MElementResidence) handle).getResident();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the collection of elements in a given component.
     *
     * @param handle the component
     * @return the Collection of ResidentElements
     */
    public Collection getResidentElements(Object handle) {
        if (handle instanceof MComponent) {
            return ((MComponent) handle).getResidentElements();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns a collection with all residents belonging to the given
     * node.
     *
     * @param handle is the node, nodeinstance, componentinstance
     * @return Collection
     */
    public Collection getResidents(Object handle) {
        if (isANode(handle)) {
            return ((MNode) handle).getResidents();
        }
        if (isANodeInstance(handle)) {
            return ((MNodeInstance) handle).getResidents();
        }
        if (isAComponentInstance(handle)) {
            return ((MComponentInstance) handle).getResidents();
        }

	return illegalArgumentCollection(handle);
    }

    /**
     * Gets the source for a given transition.
     *
     * @param handle is the transition
     * @return Object (MStateVertex)
     */
    public Object getSource(Object handle) {
        if (isATransition(handle)) {
            return ((MTransition) handle).getSource();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Gets the source for some given flow.
     *
     * @param handle is the flow
     * @return Collection
     */
    public Collection getSources(Object handle) {
        if (handle instanceof MFlow) {
            return ((MFlow) handle).getSources();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the sourceflows of a model element.
     *
     * @param handle is the model element
     * @return a collection of sourceflows
     */
    public Collection getSourceFlows(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getSourceFlows();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * The list of Specializations from a GeneralizableElement.
     *
     * @param handle GeneralizableElement to retrieve from.
     * @return Collection of Specializations.
     */
    public Collection getSpecializations(Object handle) {
        if (handle instanceof MGeneralizableElement) {
            MGeneralizableElement ge = (MGeneralizableElement) handle;
            return ge.getSpecializations();
        }

        // ...
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the state machine belonging to some given state or transition
     * If you need to find the StateMachine for an internal transition,
     * or for ANY state,
     * use StateMachinesHelper.getStateMachine() instead.
     *
     * @param handle is the state or transition
     * @return Object
     */
    public Object getStateMachine(Object handle) {
        if (handle instanceof MState) {
            return ((MState) handle).getStateMachine();
        }
        if (handle instanceof MTransition) {
            return ((MTransition) handle).getStateMachine();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the state belonging to some given transition.
     *
     * @param handle is the transition
     * @return Object
     */
    public Object getState(Object handle) {
        if (handle instanceof MTransition) {
            return ((MTransition) handle).getState();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the states from a deferable event.
     *
     * @param handle is the event
     * @return Object
     */
    public Collection getStates(Object handle) {
        if (handle instanceof MEvent) {
            return ((MEvent) handle).getStates();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the stereotypes belonging to some given model element.<p>
     *
     * Note! For UML version 1.3 there can only be one stereotype
     * per model element. This means that the returned Collection will
     * only have zero or one elements. Don't write any code that rely
     * on this! Consider it to be a Collection with zero or more
     * elements as it will be in later UML versions.
     *
     * @param handle The model element.
     * @return a Collection with all stereotypes or empty if none.
     */
    public Collection getStereotypes(Object handle) {
        if (isAModelElement(handle)) {
            // This returns a collection as we have an eye on the future
            // and multiple stereotypes in UML1.5
            MStereotype stereo = ((MModelElement) handle).getStereotype();
            if (stereo != null) {
                List list = new ArrayList(1);
                list.add(stereo);
                return list;
            }
            return Collections.EMPTY_LIST;
        }
        return illegalArgumentList(handle);
    }

    /**
     * Returns the stimuli belonging to some given link.
     *
     * @param handle is the link
     * @return Object
     */
    public Collection getStimuli(Object handle) {
        if (isALink(handle)) {
            return ((MLink) handle).getStimuli();
        }
        if (isAAction(handle)) {
            return ((MAction)handle).getStimuli();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the Stimuli that are received by the given Instance.
     *
     * @param handle the Instance
     * @return the collection of stimuli
     */
    public Collection getStimuli2(Object handle) {
        if (handle instanceof MInstance) {
            return ((MInstance) handle).getStimuli2();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the Stimuli that are send by the given Instance.
     *
     * @param handle the Instance
     * @return the collection of stimuli
     */
    public Collection getStimuli3(Object handle) {
        if (handle instanceof MInstance) {
            return ((MInstance) handle).getStimuli3();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns a collection with all subvertices belonging to the given
     * composite state.
     *
     * @param handle is the composite state
     * @return Collection
     */
    public Collection getSubvertices(Object handle) {
        if (isACompositeState(handle)) {
            return ((MCompositeState) handle).getSubvertices();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the submachie of a submachine state.
     *
     * @param handle is the submachine state
     * @return submachine
     */
    public Object getSubmachine(Object handle) {
        if (handle instanceof MSubmachineState) {
            return ((MSubmachineState) handle).getSubmachine();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the submachine of a submachine state.
     *
     * @param handle is the submachine state
     * @return submachine
     */
    public Collection getSubmachineStates(Object handle) {
        if (handle instanceof MStateMachine) {
            return ((MStateMachine) handle).getSubmachineStates();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * The list of SupplierDependencies from a ModelElement.
     *
     * @param handle model element.
     * @return Iterator with the supplier dependencies.
     */
    public Collection getSupplierDependencies(Object handle) {
        if (handle instanceof MModelElement) {
            MModelElement me = (MModelElement) handle;
            return me.getSupplierDependencies();
        }

        // ...
	return illegalArgumentCollection(handle);
    }

    /**
     * The top of a state machine.
     *
     * @param handle the state machine
     * @return the top
     */
    public Object getTop(Object handle) {
        if (handle instanceof MStateMachine) {
            return ((MStateMachine) handle).getTop();
        }

        // ...
	return illegalArgumentObject(handle);
    }

    /**
     * Get the transition of a guard or action.
     *
     * @param handle the guard or action
     * @return the transition
     */
    public Object getTransition(Object handle) {
        if (handle instanceof MGuard) {
            return ((MGuard) handle).getTransition();
        }
        if (handle instanceof MAction) {
            return ((MAction) handle).getTransition();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Get the trigger of a transition.
     *
     * @param handle the transition
     * @return the trigger
     */
    public Object getTrigger(Object handle) {
        if (handle instanceof MTransition) {
            return ((MTransition) handle).getTrigger();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * The type of a StructuralFeature, AssociationEnd, Parameter or
     * ObjectFlowState.
     *
     * @param handle the StructuralFeature, AssociationEnd, Parameter or
     *  ObjectFlowState
     * @return the type
     */
    public Object getType(Object handle) {
        if (handle instanceof MStructuralFeature) {
            return ((MAttribute) handle).getType();
        }
        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd) handle).getType();
        }
        if (handle instanceof MParameter) {
            return ((MParameter) handle).getType();
        }
        if (handle instanceof MObjectFlowState) {
            return ((MObjectFlowState) handle).getType();
        }

        // ...
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the target of some transition.
     *
     * @param handle is the transition
     * @return Object
     */
    public Object getTarget(Object handle) {
        if (isATransition(handle)) {
            return ((MTransition) handle).getTarget();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the target scope of some model element.
     *
     * @param handle is the model element
     * @return Object
     */
    public Object getTargetScope(Object handle) {
        if (handle instanceof MStructuralFeature) {
            return ((MStructuralFeature) handle).getTargetScope();
        }
        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd) handle).getTargetScope();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the targetflows of a model element.
     *
     * @param handle is the model element
     * @return a collection of targetflows
     */
    public Collection getTargetFlows(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getTargetFlows();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the upper bound of the multiplicity of the given handle (an
     * associationend).
     *
     * @param handle is the model element
     * @return int
     */
    public int getUpper(Object handle) {
        if (isAAssociationEnd(handle)) {
            int upper = 0;
            MAssociationEnd end = (MAssociationEnd) handle;
            if (end.getMultiplicity() != null) {
                upper = end.getMultiplicity().getUpper();
            }
            return upper;
        }
        if (isAMultiplicity(handle)) {
            MMultiplicity up = (MMultiplicity) handle;
            return up.getUpper();
        }
        if (isAMultiplicityRange(handle)) {
            MMultiplicityRange up = (MMultiplicityRange) handle;
            return up.getUpper();
        }
        illegalArgument(handle);
	return 0;
    }

    /**
     * Returns the use case of an extension point.
     *
     * @param handle is the extension point
     * @return a use case
     */
    public Object getUseCase(Object handle) {
        if (handle instanceof MExtensionPoint) {
            return ((MExtensionPoint) handle).getUseCase();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the upper bound of the multiplicity of the given handle (an
     * associationend).
     *
     * @param handle is the model element
     * @return int
     */
    public int getLower(Object handle) {
        if (isAAssociationEnd(handle)) {
            int lower = 0;
            MAssociationEnd end = (MAssociationEnd) handle;
            if (end.getMultiplicity() != null) {
                lower = end.getMultiplicity().getLower();
            }
            return lower;
        }
        if (isAMultiplicity(handle)) {
            MMultiplicity low = (MMultiplicity) handle;
            return low.getLower();
        }
        if (isAMultiplicityRange(handle)) {
            MMultiplicityRange low = (MMultiplicityRange) handle;
            return low.getLower();
        }
        illegalArgument(handle);
	return 0;
    }

    /**
     * Returns the transitions belonging to the given handle. The handle can be
     * a statemachine or a composite state or an event.
     * If it's a statemachine the
     * transitions will be given back belonging to that statemachine. If it's a
     * compositestate the internal transitions of that compositestate will be
     * given back.
     * If it's an event, all transitions triggered by this event
     * will be given back.
     *
     * @param handle is the model element
     * @return Collection
     */
    public Collection getTransitions(Object handle) {
        if (isAStateMachine(handle)) {
            return ((MStateMachine) handle).getTransitions();
        } else if (isACompositeState(handle)) {
            return ((MCompositeState) handle).getInternalTransitions();
        } else if (isAEvent(handle)) {
            return ((MEvent) handle).getTransitions();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * This method returns all attributes of a given Classifier.
     *
     * @param handle is the classifier you want to have the attributes for.
     * @return a collection of the attributes
     */
    public Collection getStructuralFeatures(Object handle) {
        Collection result = new ArrayList();
        if (isAClassifier(handle)) {
            MClassifier mclassifier = (MClassifier) handle;

            Iterator features = mclassifier.getFeatures().iterator();
            while (features.hasNext()) {
                MFeature feature = (MFeature) features.next();
                if (isAStructuralFeature(feature)) {
                    result.add(feature);
                }
            }
            return result;
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * This method returns all operations of a given Classifier.
     *
     * @param mclassifier the classifier you want to have the operations for
     * @return a collection of the operations
     */
    private Collection getOperations(MClassifier mclassifier) {
        Collection result = new ArrayList();
        Iterator features = mclassifier.getFeatures().iterator();
        while (features.hasNext()) {
            MFeature feature = (MFeature) features.next();
            if (isAOperation(feature)) {
                result.add(feature);
            }
        }
        return result;
    }

    /**
     * Returns the Specification of a given Reception.
     *
     * @param handle the Reception
     * @return String the Specification
     */
    public String getSpecification(Object handle) {
        if (handle instanceof MReception) {
            return ((MReception) handle).getSpecification();
        }
        return illegalArgumentString(handle);
    }

    /**
     * Returns all Interfaces of which this class is a realization.
     *
     * @param handle  the class you want to have the interfaces for
     * @return a collection of the Interfaces
     */
    public Collection getSpecifications(Object handle) {
        Collection result = new Vector();
        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd) handle).getSpecifications();
        }
        if (handle instanceof MClassifier) {
            Collection deps = ((MClassifier) handle).getClientDependencies();
            Iterator depIterator = deps.iterator();
            while (depIterator.hasNext()) {
                MDependency dep = (MDependency) depIterator.next();
                if ((dep instanceof MAbstraction)
                        && dep.getStereotype() != null
                        && dep.getStereotype().getName() != null
                        && dep.getStereotype().getName().equals("realize")) {
                    MInterface i =
			(MInterface) dep.getSuppliers().toArray()[0];
                    result.add(i);
                }
            }
            return result;
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the suppliers of a dependency.
     *
     * @param handle is the dependency
     * @return a collection of the suppliers
     */
    public Collection getSuppliers(Object handle) {
        if (handle instanceof MDependency) {
            return ((MDependency) handle).getSuppliers();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * @see org.argouml.model.Facade#getAction(Object)
     */
    public Object getAction(Object handle) {
        if (handle instanceof MMessage) {
            return ((MMessage) handle).getAction();
        }
        if (handle instanceof MArgument) {
            return ((MArgument)handle).getAction();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the activator belonging to some message.
     *
     * @param handle is the message
     * @return the activator
     */
    public Object getActivator(Object handle) {
        if (handle instanceof MMessage) {
            return ((MMessage) handle).getActivator();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the actual arguments for a given action.
     *
     * @param handle is the action
     * @return the actual arguments
     */
    public Collection getActualArguments(Object handle) {
        if (handle instanceof MAction) {
            return ((MAction) handle).getActualArguments();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns an addition for a given inlcude.
     * There is a bug in NSUML which gets the addition and base
     * relationships back to front for include relationships. Solve
     * reversing their accessors in the code
     *
     * @param handle is the include
     * @return the addition
     */
    public Object getAddition(Object handle) {
        if (handle instanceof MInclude) {
            // See issue 2034
            return ((MInclude) handle).getBase();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns the AggregationKind of a given AssociationEnd.
     *
     * @param handle the AssociationEnd
     * @return the AggregationKind
     */
    public Object getAggregation(Object handle) {
        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd) handle).getAggregation();
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Returns all associated classes for some given classifier.
     * Returns an empty collection if the given argument handle is not
     * a classifier.  The given parameter is included in the returned
     * collection if it has a self-referencing association.
     *
     * @param handle is the classifier
     * @return Collection
     */
    public Collection getAssociatedClasses(Object handle) {
        Collection col = new ArrayList();
        if (handle instanceof MClassifier) {
            MClassifier classifier = (MClassifier) handle;
            Collection ends = classifier.getAssociationEnds();
            Iterator it = ends.iterator();
            Set associations = new HashSet();
            while (it.hasNext()) {
                MAssociationEnd ae = (MAssociationEnd) it.next();
                associations.add(ae.getAssociation());
            }
            Collection otherEnds = new ArrayList();
            it = associations.iterator();
            while (it.hasNext()) {
                otherEnds.addAll(((MAssociation) it.next()).getConnections());
            }
            otherEnds.removeAll(ends);
            it = otherEnds.iterator();
            while (it.hasNext()) {
                col.add(((MAssociationEnd) it.next()).getType());
            }
            return col;
        }
	return illegalArgumentCollection(handle);
    }

    ////////////////////////////////////////////////////////////////
    // Common getters

    /**
     * The name of a model element or some diagram part.
     *
     * @param handle that points out the object.
     * @return the name
     */
    public String getName(Object handle) {
        String name = null;
        if (handle instanceof MModelElement) {
            name = ((MModelElement) handle).getName();
//        } else if (handle instanceof Diagram) {
//            name = ((Diagram) handle).getName();
        } else if (handle instanceof MMultiplicity) {
            name = handle.toString();
        } else if (handle instanceof MOrderingKind) {
            name = ((MOrderingKind) handle).getName();
        } else if (handle instanceof MAggregationKind) {
            name = ((MAggregationKind) handle).getName();
        } else if (handle instanceof MVisibilityKind) {
            name = ((MVisibilityKind) handle).getName();
        } else if (handle instanceof MCallConcurrencyKind) {
            name = ((MCallConcurrencyKind) handle).getName();
        } else if (handle instanceof MParameterDirectionKind) {
        		name = ((MParameterDirectionKind) handle).getName();
        } else {
            illegalArgument(handle);
        }
        if (name != null) {
            // The following code is a workaround for issue
            // http://argouml.tigris.org/issues/show_bug.cgi?id=2847.
            // The cause is
            // not known and the best fix available for the moment is to remove
            // the corruptions as they are found.
            int pos = 0;
            boolean fixed = false;
            while ((pos = name.indexOf(0xffff)) >= 0) {
                name =
                    name.substring(0, pos)
                    + name.substring(pos + 1, name.length());
                fixed = true;
            }
            if (fixed) {
                try {
                    throw new UmlException(
                            "Illegal character stripped out of element name");
                } catch (UmlException e) {
                    LOG.warn("0xFFFF detected in element name", e);
                }
                nsmodel.getCoreHelper().setName(handle, name);
            }
        }
	return name;
    }

    /**
     * Return the owner of a feature or its
     * association end if it is a
     * qualified attribute.
     *
     * @param handle is the feature
     * @return classifier
     */
    public Object getOwner(Object handle) {
        if ((handle instanceof MAttribute)
                && ((MAttribute) handle).getAssociationEnd() != null) {
            return ((MAttribute) handle).getAssociationEnd();
        }
        if (handle instanceof MFeature) {
            return ((MFeature) handle).getOwner();
        }
        return illegalArgumentObject(handle);
    }

    /**
     * Return the tag of a tagged value.
     *
     * @param handle The tagged value belongs to this.
     * @return The found tag as a String.
     */
    public String getTag(Object handle) {
        if (handle instanceof MTaggedValue) {
            return ((MTaggedValue) handle).getTag();
        }
	return illegalArgumentString(handle);
    }

    /**
     * Return the tagged values iterator of a model element.
     *
     * @param handle The tagged values belong to this.
     * @return The tagged values iterator
     */
    public Iterator getTaggedValues(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getTaggedValues().iterator();
        }
	illegalArgument(handle);
	return null;
    }

    /**
     * Returns the TaggedValues of a ModelElement.
     *
     * @param handle the ModelElement
     * @return the Collection of TaggedValues
     */
    public Collection getTaggedValuesCollection(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getTaggedValues();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Return the tagged value with a specific tag.
     *
     * @param handle The model element the tagged value belongs to.
     * @param name The tag name.
     * @return The found tag, null if not found
     */
    public Object getTaggedValue(Object handle, String name) {
        if (handle instanceof MModelElement) {
            MModelElement me = ((MModelElement) handle);
            Iterator i = me.getTaggedValues().iterator();
            while (i.hasNext()) {
                MTaggedValue tv = (MTaggedValue) i.next();
                if (tv.getTag().equals(name)) {
                    return tv;
                }
            }
	    return null;
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Return the value of a tagged value with a specific tag.
     *
     * @param handle The model element that the tagged value belongs to.
     * @param name The tag name.
     * @return The value of the found tag. "" if not found.
     */
    public String getTaggedValueValue(Object handle, String name) {
        Object taggedValue = getTaggedValue(handle, name);
        if (taggedValue == null) {
            return "";
        }
        return getValueOfTag(taggedValue);
    }

    /**
     * Return the key (tag) of some tagged value.
     *
     * TODO: This does exactly the same as getTag(Object). Remove one of them.
     *
     * @param handle The tagged value.
     * @return The found value as String.
     */
    public String getTagOfTag(Object handle) {
        if (handle instanceof MTaggedValue) {
            return ((MTaggedValue) handle).getTag();
        }
	return illegalArgumentString(handle);
    }

    /**
     * Returns the Value of some UML Object.
     *
     * @param handle Object
     * @return Object the exact type depends on the handle type
     * (String, Expression, Instance, TaggedValue...)
     */
    public Object getValue(Object handle) {
        if (handle instanceof MTaggedValue) {
            return ((MTaggedValue) handle).getValue();
        }
        if (handle instanceof MArgument) {
            return ((MArgument) handle).getValue();
        }
        if (handle instanceof MExtension) {
            return ((MExtension) handle).getValue();
        }
        if (handle instanceof MAttributeLink) {
            return ((MAttributeLink) handle).getValue();
        }
        if (handle instanceof MAggregationKind) {
            return new Integer(((MAggregationKind) handle).getValue());
        }
        if (handle instanceof MOrderingKind) {
            return new Integer(((MOrderingKind) handle).getValue());
        }
        if (handle instanceof MOperationDirectionKind) {
            return new Integer(((MOperationDirectionKind) handle).getValue());
        }
        if (handle instanceof MVisibilityKind) {
            return new Integer(((MVisibilityKind) handle).getValue());
        }
        if (handle instanceof MScopeKind) {
            return new Integer(((MScopeKind) handle).getValue());
        }
        if (handle instanceof MMessageDirectionKind) {
            return new Integer(((MMessageDirectionKind) handle).getValue());
        }
        if (handle instanceof MChangeableKind) {
            return new Integer(((MChangeableKind) handle).getValue());
        }
        if (handle instanceof MPseudostateKind) {
            return new Integer(((MPseudostateKind) handle).getValue());
        }
        if (handle instanceof MCallConcurrencyKind) {
            return new Integer(((MCallConcurrencyKind) handle).getValue());
        }
	return illegalArgumentObject(handle);
    }

    /**
     * Return the value of some tagged value.
     *
     * @param handle The tagged value.
     * @return The found value as String.
     */
    public String getValueOfTag(Object handle) {
        if (handle instanceof MTaggedValue) {
            return ((MTaggedValue) handle).getValue();
        }
	return illegalArgumentString(handle);
    }

    /**
     * Return the UUID of this element.
     *
     * @param base base element (MBase type)
     * @return UUID
     */
    public String getUUID(Object base) {
        if (isABase(base)) {
            return ((MBase) base).getUUID();
        }
	illegalArgument(base);
	return "";
    }

    /**
     * Return the visibility of this element.
     *
     * @param handle an nsuml model element
     * @return visibility
     */
    public Object getVisibility(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getVisibility();
        }
        //
	return illegalArgumentObject(handle);
    }
    
    /** 
     * @see org.argouml.model.Facade#getWhen(java.lang.Object)
     */
    public Object getWhen(Object target) {
        if (isATimeEvent(target)) {
            return ((MTimeEvent) target).getWhen();
        }
        return illegalArgumentObject(target);
    }

    /**
     * Get the partitions from a container.
     *
     * @param container The container.
     * @return The partitions (a collection).
     */
    public Collection getPartitions(Object container) {
        if (container instanceof MActivityGraph) {
            return ((MActivityGraph) container).getPartitions();
        }
        //
	return illegalArgumentCollection(container);
    }

    /**
     *  Return the Stub State's referenced state.
     *  @param o Stub State
     *  @return referenced state
     */
    public String getReferenceState(Object o) {
        if (o instanceof MStubState) {
            return ((MStubState) o).getReferenceState();
        }
        return illegalArgumentString(o);
    }

    ////////////////////////////////////////////////////////////////
    // Other querying methods

    /**
     * Returns a named object in the given object by calling it's lookup method.
     *
     * @param handle the object that we search through
     * @param name of the model element
     * @return found object, null otherwise
     */
    public Object lookupIn(Object handle, String name) {
        if (handle instanceof MModel) {
            return ((MModel) handle).lookup(name);
        }
        if (handle instanceof MNamespace) {
            return ((MNamespace) handle).lookup(name);
        }
        if (handle instanceof MClassifier) {
            return ((MClassifier) handle).lookup(name);
        }
        return illegalArgumentObject(handle);
    }

    ////////////////////////////////////////////////////////////////
    // Getter methods

    /**
     * Returns the name of the UML Model class, e.g. it it will return
     * Class for an object of type MClass.
     *
     * @param handle MBase
     * @return classname of modelelement
     */
    public String getUMLClassName(Object handle) {
        if (handle instanceof MBase) {
            return ((MBase) handle).getUMLClassName();
        }
	return illegalArgumentString(handle);
    }

    ////////////////////////////////////////////////////////////////
    // Convenience methods

    /**
     * The empty set.
     *
     * @return an empty collection.
     */
    private Collection emptyCollection() {
        return Collections.EMPTY_LIST;
    }

    /**
     * Get a string representation of the class type.
     * Purpose: documenting an exception
     *
     * @param handle the Class or null
     * @return String
     */
    protected String getClassNull(Object handle) {
	if (handle == null) {
	    return "[null]";
	}
        return "[" + handle + "/" + handle.getClass() + "]";
    }

    /**
     * Method that throws an error when a method is called with
     * an incorrect argument.
     *
     * @param arg is the incorrect argument.
     */
    private void illegalArgument(Object arg) {
	throw new IllegalArgumentException("Unrecognized object "
					   + getClassNull(arg));
    }

    /**
     * Method that throws an error when a method is called with
     * an incorrect argument.<p>
     *
     * @param arg is the incorrect argument.
     * @return a boolean for use in the return statement.
     */
    private boolean illegalArgumentBoolean(Object arg) {
	illegalArgument(arg);
	return false;
    }

    /**
     * Method that throws an error when a method is called with
     * an incorrect argument.<p>
     *
     * @param arg is the incorrect argument.
     * @return Object for use in the return statement.
     */
    private Object illegalArgumentObject(Object arg) {
	illegalArgument(arg);
	return null;
    }

    /**
     * Method that throws an error when a method is called with
     * an incorrect argument.<p>
     *
     * @param arg is the incorrect argument.
     * @return Collection for use in the return statement.
     */
    private Collection illegalArgumentCollection(Object arg) {
        illegalArgument(arg);
        return null;
    }

    /**
     * Method that throws an error when a method is called with
     * an incorrect argument.<p>
     *
     * @param arg is the incorrect argument.
     * @return Int for use in the return statement.
     */
    private int illegalArgumentInt(Object arg) {
        illegalArgument(arg);
        return 0;
    }

    /**
     * Method that throws an error when a method is called with
     * an incorrect argument.<p>
     *
     * @param arg is the incorrect argument.
     * @return List for use in the return statement.
     */
    private List illegalArgumentList(Object arg) {
        illegalArgument(arg);
        return null;
    }

    /**
     * Method that throws an error when a method is called with
     * an incorrect argument.<p>
     *
     * @param arg is the incorrect argument.
     * @return String for use in the return statement.
     */
    private String illegalArgumentString(Object arg) {
	illegalArgument(arg);
	return null;
    }

    /**
     * Method that throws an error when a method is called
     * with an incorrect argument. At least one of the arguments given
     * is incorrect.<p>
     *
     * @param arg1 is one of the argument, possibly incorrect.
     * @param arg2 is one of the argument, possibly incorrect.
     * @return Return value is set to Object.
     */
    private Object illegalArgument(Object arg1, Object arg2) {
	throw new IllegalArgumentException("Unrecognized object "
					   + getClassNull(arg1) + " or "
					   + getClassNull(arg2));
    }

    /**
     * Method that throws an error when a method is called
     * with an incorrect argument. At least one of the arguments given
     * is incorrect.<p>
     *
     * @param arg1 is one of the argument, possibly incorrect.
     * @param arg2 is one of the argument, possibly incorrect.
     * @return Object for use in the return statement.
     */
    private Object illegalArgumentObject(Object arg1, Object arg2) {
	illegalArgument(arg1, arg2);
	return null;
    }
    
    /**
     * @see org.argouml.model.Facade#getTipString(java.lang.Object)
     */
    public String getTipString(Object modelElement) {
        return modelElement.toString();
    }


    /**
     * @see org.argouml.model.Facade#toString(java.lang.Object)
     */
    public String toString(Object modelElement) {
        return modelElement.toString();
    }


	/**
	 * Not implemented in the UML 1.3 metamodel.
	 * @see org.argouml.model.Facade#isATagDefinition(java.lang.Object)
	 */
	public boolean isATagDefinition(Object handle) {
		return false;
	}

	/**
	 * Not implemented in the UML 1.3 metamodel.
	 * @see org.argouml.model.Facade#isATagDefinition(java.lang.Object)
	 */
	public boolean isAEnumeration(Object handle) {
		return false;
	}

    /**
     * Not implemented in the UML 1.3 metamodel.
     * @see org.argouml.model.Facade#getTagDefinition(java.lang.Object)
     */
    public Object getTagDefinition(Object handle) {
        return null;
    }

    /**
     * Not implemented in the UML 1.3 metamodel.
     * @see org.argouml.model.Facade#getTagDefinitions(java.lang.Object)
     */
    public Collection getTagDefinitions(Object handle) {
        return null;
    }

    /**
     * Not implemented in the UML 1.3 metamodel.
     * @see org.argouml.model.Facade#getEnumerationLiterals(java.lang.Object)
     */
    public List getEnumerationLiterals(Object handle) {
        return null;
    }
	
}
