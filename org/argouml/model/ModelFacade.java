// $Id$
// Copyright (c) 2003-2004 The Regents of the University of California. All
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

package org.argouml.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.model.uml.foundation.extensionmechanisms.ExtensionMechanismsHelper;
import org.argouml.uml.MMUtil;
import org.tigris.gef.base.Diagram;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.MElementListener;
import ru.novosoft.uml.MExtension;
import ru.novosoft.uml.behavior.activity_graphs.MActionState;
import ru.novosoft.uml.behavior.activity_graphs.MActivityGraph;
import ru.novosoft.uml.behavior.activity_graphs.MClassifierInState;
import ru.novosoft.uml.behavior.activity_graphs.MObjectFlowState;
import ru.novosoft.uml.behavior.activity_graphs.MPartition;
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
import ru.novosoft.uml.behavior.state_machines.MCallEvent;
import ru.novosoft.uml.behavior.state_machines.MCompositeState;
import ru.novosoft.uml.behavior.state_machines.MEvent;
import ru.novosoft.uml.behavior.state_machines.MFinalState;
import ru.novosoft.uml.behavior.state_machines.MGuard;
import ru.novosoft.uml.behavior.state_machines.MPseudostate;
import ru.novosoft.uml.behavior.state_machines.MSignalEvent;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MStateImpl;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.behavior.state_machines.MSubmachineState;
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
import ru.novosoft.uml.foundation.data_types.MBooleanExpression;
import ru.novosoft.uml.foundation.data_types.MCallConcurrencyKind;
import ru.novosoft.uml.foundation.data_types.MChangeableKind;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MExpressionEditor;
import ru.novosoft.uml.foundation.data_types.MIterationExpression;
import ru.novosoft.uml.foundation.data_types.MMessageDirectionKind;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MObjectSetExpression;
import ru.novosoft.uml.foundation.data_types.MOperationDirectionKind;
import ru.novosoft.uml.foundation.data_types.MOrderingKind;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;
import ru.novosoft.uml.foundation.data_types.MProcedureExpression;
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
 * Facade object for the Model component in ArgoUML.<p>
 *
 * The purpose of this Facade object is to allow for decoupling other modules
 * from the insides of the model. For this purpose all of the methods in this
 * class give away and accept handles (of type java.lang.Object) to the
 * objects within the model.<p>
 *
 * This is just getters and recognizers. This is because the Model
 * component has an extremely complicated internal data structure
 * with lots of internal dependencies. To manipulate these there is
 * a whole set of factories and helpers within the Model that is to
 * be used but to use them you need knowledge of the internals of
 * the Model, specifically the NS-UML objects.<p>
 *
 * All methods in this facade are static.<p>
 *
 * Signature for all recognizers in this Facade:
 * <ul>
 * <li>public static boolean isA<TYPE>(Object handle)
 * <li>public static boolean is<PROPERTY>(Object handle)
 * </ul>
 *
 * Signature for all getters in this Facade:
 * <ul>
 * <li>public static Object get<TYPE>(Object handle) - 1..1
 * <li>public static Iterator get<TYPES>(Object handle) - 0..*
 * <li>public static String getName(Object handle) - Name
 * </ul>
 *
 * @stereotype utility
 * @author Linus Tolke
 */
public class ModelFacade {
    ////////////////////////////////////////////////////////////////
    // constants

    // TODO: deprecate all of these constants in favor of a separate declaration

    public static final short ACC_PUBLIC = 1;
    public static final short ACC_PRIVATE = 2;
    public static final short ACC_PROTECTED = 3;

    public static final short CLASSIFIER_SCOPE = 1;
    public static final short INSTANCE_SCOPE = 2;

    public static final short GUARDED = 1;
    public static final short SEQUENTIAL = 2;

    // Types of line
    public static final Object ABSTRACTION = MAbstraction.class;
    public static final Object ASSOCIATION = MAssociation.class;
    public static final Object ASSOCIATION_CLASS = MAssociationClass.class;
    public static final Object ASSOCIATION_ROLE = MAssociationRole.class;
    public static final Object COLLABORATION = MCollaboration.class;
    public static final Object DEPENDENCY = MDependency.class;
    public static final Object EXTEND = MExtend.class;
    public static final Object GENERALIZATION = MGeneralization.class;
    public static final Object INCLUDE = MInclude.class;
    public static final Object LINK = MLink.class;
    public static final Object MESSAGE = MMessage.class;
    public static final Object PERMISSION = MPermission.class;
    public static final Object SIGNAL = MSignal.class;
    public static final Object USAGE = MUsage.class;
    public static final Object TRANSITION = MTransition.class;

    // Types of node
    public static final Object ACTOR = MActor.class;
    public static final Object CLASS = MClass.class;
    public static final Object CLASSIFIER = MClassifier.class;
    public static final Object CLASSIFIER_ROLE = MClassifierRole.class;
    public static final Object COMPONENT = MComponent.class;
    public static final Object COMPONENT_INSTANCE = MComponentInstance.class;
    public static final Object INSTANCE = MInstance.class;
    public static final Object INTERFACE = MInterface.class;
    public static final Object NODE = MNode.class;
    public static final Object NODE_INSTANCE = MNodeInstance.class;
    public static final Object OBJECT = MObject.class;
    public static final Object PACKAGE = MPackage.class;
    public static final Object MODEL = MModel.class;
    public static final Object SUBSYSTEM = MSubsystem.class;
    public static final Object STATE = MState.class;
    public static final Object STATEIMPL = MStateImpl.class;
    public static final Object COMPOSITESTATE = MCompositeState.class;
    public static final Object STATEVERTEX = MStateVertex.class;
    public static final Object PSEUDOSTATE = MPseudostate.class;
    public static final Object FINALSTATE = MFinalState.class;
    public static final Object USE_CASE = MUseCase.class;

    // Invisible model elements
    public static final Object ACTION = MAction.class;
    public static final Object ACTION_EXPRESSION = MActionExpression.class;
    public static final Object ACTION_STATE = MActionState.class;
    public static final Object ASSOCIATION_END = MAssociationEnd.class;
    public static final Object ASSOCIATION_END_ROLE = MAssociationEndRole.class;
    public static final Object CALL_ACTION = MCallAction.class;
    public static final Object CALLCONCURRENCYKIND = MCallConcurrencyKind.class;
    public static final Object CREATE_ACTION = MCreateAction.class;
    public static final Object DESTROY_ACTION = MDestroyAction.class;
    public static final Object TERMINATE_ACTION = MTerminateAction.class;   
    public static final Object NAMESPACE = MNamespace.class;
    public static final Object RECEPTION = MReception.class;
    public static final Object RETURN_ACTION = MReturnAction.class;
    public static final Object SCOPEKIND = MScopeKind.class;
    public static final Object SEND_ACTION = MSendAction.class;
    public static final Object STEREOTYPE = MStereotype.class;
    public static final Object PARAMETER = MParameter.class;
    public static final Object PARAMETERDIRECTIONKIND =
        MParameterDirectionKind.class;
    public static final Object GENERALAIZABLE_ELEMENT =
        MGeneralizableElement.class;
    public static final Object DATATYPE = MDataType.class;
    public static final Object STATEMACHINE = MStateMachine.class;

    public static final Object ATTRIBUTE = MAttribute.class;
    public static final Object OPERATION = MOperation.class;

    public static final Object MULTIPLICITY = MMultiplicity.class;

    public static final Object VISIBILITYKIND = MVisibilityKind.class;

    public static final Object MODELELEMENT = MModelElement.class;
    public static final Object STIMULUS = MStimulus.class;

    public static final Object AGGREGATIONKIND = MAggregationKind.class;
    public static final Object BOOLEAN_EXPRESSION = MBooleanExpression.class;
    public static final Object GUARD = MGuard.class;

    public static final Object ADD_ONLY_CHANGEABLEKIND =
        MChangeableKind.ADD_ONLY;
    public static final Object CHANGEABLE_CHANGEABLEKIND =
        MChangeableKind.CHANGEABLE;
    public static final Object FROZEN_CHANGEABLEKIND = MChangeableKind.FROZEN;

    public static final Object CONCURRENT_CONCURRENCYKIND =
        MCallConcurrencyKind.CONCURRENT;

    public static final Object GUARDED_CONCURRENCYKIND =
        MCallConcurrencyKind.GUARDED;

    public static final Object SEQUENTIAL_CONCURRENCYKIND =
        MCallConcurrencyKind.SEQUENTIAL;

    public static final Object PSEUDOSTATEKIND = MPseudostateKind.class;
    public static final Object INITIAL_PSEUDOSTATEKIND =
        MPseudostateKind.INITIAL;
    public static final Object DEEPHISTORY_PSEUDOSTATEKIND =
        MPseudostateKind.DEEP_HISTORY;
    public static final Object SHALLOWHISTORY_PSEUDOSTATEKIND =
        MPseudostateKind.SHALLOW_HISTORY;
    public static final Object FORK_PSEUDOSTATEKIND = MPseudostateKind.FORK;
    public static final Object JOIN_PSEUDOSTATEKIND = MPseudostateKind.JOIN;
    public static final Object JUNCTION_PSEUDOSTATEKIND =
        MPseudostateKind.JUNCTION;
    public static final Object BRANCH_PSEUDOSTATEKIND = MPseudostateKind.BRANCH;

    public static final Object PUBLIC_VISIBILITYKIND = MVisibilityKind.PUBLIC;
    public static final Object PRIVATE_VISIBILITYKIND = MVisibilityKind.PRIVATE;
    public static final Object PROTECTED_VISIBILITYKIND =
        MVisibilityKind.PROTECTED;

    public static final Object AGGREGATE_AGGREGATIONKIND =
        MAggregationKind.AGGREGATE;
    public static final Object COMPOSITE_AGGREGATIONKIND =
        MAggregationKind.COMPOSITE;
    public static final Object NONE_AGGREGATIONKIND = MAggregationKind.NONE;

    public static final Object ORDERED_ORDERINGKIND = MOrderingKind.ORDERED;
    public static final Object UNORDERED_ORDERINGKIND = MOrderingKind.UNORDERED;
    public static final Object SORTED_ORDERINGKIND = MOrderingKind.SORTED;

    public static final Object M1_1_MULTIPLICITY = MMultiplicity.M1_1;

    public static final Object M0_1_MULTIPLICITY = MMultiplicity.M0_1;

    public static final Object M0_N_MULTIPLICITY = MMultiplicity.M0_N;

    public static final Object M1_N_MULTIPLICITY = MMultiplicity.M1_N;

    public static final Object CLASSIFIER_SCOPEKIND = MScopeKind.CLASSIFIER;

    public static final Object INSTANCE_SCOPEKIND = MScopeKind.INSTANCE;

    public static final Object INOUT_PARAMETERDIRECTIONKIND =
        MParameterDirectionKind.INOUT;

    public static final Object IN_PARAMETERDIRECTIONKIND =
        MParameterDirectionKind.IN;

    public static final Object OUT_PARAMETERDIRECTIONKIND =
        MParameterDirectionKind.OUT;

    public static final Object RETURN_PARAMETERDIRECTIONKIND =
        MParameterDirectionKind.RETURN;


    ////////////////////////////////////////////////////////////////
    // Object Creation methods

    /**
     * Create a model object from the implementation.<P>
     * 
     * This will allow abstraction of the create mechanism at a single point.
     * 
     * @param entity Class to create - 
     * must implement {@link org.argouml.model.UmlModelEntity}
     * @return the created object or null if it cannot create the class.
     */
    public static Object create(ModelEntity entity) {
        if (entity instanceof UmlModelEntity) {
            return UmlFactory.getFactory().create((UmlModelEntity) entity);
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////
    // Recognizer methods for the UML model (in alphabetic order)

    /**
     * Recognizer for Abstraction.
     *
     * @param handle candidate
     * @return true if handle is an Abstraction
     */
    public static boolean isAAbstraction(Object handle) {
        return handle instanceof MAbstraction;
    }

    /**
     * Recognizer for Action.
     *
     * @param handle candidate
     * @return true if handle is an Action
     */
    public static boolean isAAction(Object handle) {
        return handle instanceof MAction;
    }

    /**
     * Recognizer for ActionExpression
     *
     * @param handle candidate
     * @return true if handle is an ActionExpression
     */
    public static boolean isAActionExpression(Object handle) {
        return handle instanceof MActionExpression;
    }

    /**
     * Recognizer for ActionSequence
     *
     * @param handle candidate
     * @return true if handle is an action sequence
     */
    public static boolean isAActionSequence(Object handle) {
        return handle instanceof MActionSequence;
    }

    /**
     * Recognizer for Action state
     *
     * @param handle candidate
     * @return true if handle is an Action state
     */
    public static boolean isAActionState(Object handle) {
        return handle instanceof MActionState;
    }

    /**
     * Recognizer for Actor
     *
     * @param handle candidate
     * @return true if handle is an Actor
     */
    public static boolean isAActor(Object handle) {
        return handle instanceof MActor;
    }

    /**
     * Recognizer for AggregationKind
     *
     * @param handle candidate
     * @return true if handle is an AggregationKind
     */
    public static boolean isAAggregationKind(Object handle) {
        return handle instanceof MAggregationKind;
    }

    /**
     * Recognizer for Association.
     *
     * @param handle candidate
     * @return true if handle is an Association
     */
    public static boolean isAAssociation(Object handle) {
        return handle instanceof MAssociation;
    }

    /**
     * Recognizer for AssociationEnd.
     *
     * @param handle candidate
     * @return true if handle is an AssociationEnd
     */
    public static boolean isAAssociationEnd(Object handle) {
        return handle instanceof MAssociationEnd;
    }

    /**
     * Recognizer for AssociationRole
     *
     * @param handle candidate
     * @return true if handle is an AssociationRole
     */
    public static boolean isAAssociationRole(Object handle) {
        return handle instanceof MAssociationRole;
    }

    /**
     * Recognizer for AssociationEndRole
     *
     * @param handle candidate
     * @return true if handle is an AssociationEndRole
     */
    public static boolean isAAssociationEndRole(Object handle) {
        return handle instanceof MAssociationEndRole;
    }

    /**
     * Recognizer for Attribute
     *
     * @param handle candidate
     * @return true if handle is an Attribute
     */
    public static boolean isAAttribute(Object handle) {
        return handle instanceof MAttribute;
    }

    /**
     * Recognizer for asynchronisity of an action
     *
     * @param handle candidate
     * @return true if the argument is asynchronous
     */
    public static boolean isAsynchronous(Object handle) {
        if (handle instanceof MAction) {
            return ((MAction) handle).isAsynchronous();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Recognizer for abstract classes and operations.
     *
     * @param handle candidate
     * @return true if handle is abstract.
     */
    public static boolean isAbstract(Object handle) {
        if (handle instanceof MOperation)
            return ((MOperation) handle).isAbstract();
        if (handle instanceof MGeneralizableElement)
            return ((MGeneralizableElement) handle).isAbstract();
        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }
    
    

    /**
     * Recognizer for ActivityGraph
     *
     * @param handle candidate
     * @return true if handle is ActivityGraph.
     */
    public static boolean isAActivityGraph(Object handle) {
        return handle instanceof MActivityGraph;
    }

    /**
     * Recognizer for bases. A base is an object that is some form of
     *  an element in the model. MBase in Novosoft terms.
     *
     * @param handle candidate
     * @return true if handle is a base.
     */
    public static boolean isABase(Object handle) {
        return handle instanceof MBase;
    }
    

    /**
     * Recognizer for behavioral features. 
     *
     * @param handle candidate
     * @return true if handle is a behavioral feature
     */
    public static boolean isABehavioralFeature(Object handle) {
        return handle instanceof MBehavioralFeature;
    }

    /**
     * Recognizer for CallAction
     *
     * @param handle candidate
     * @return true if handle is a CallAction
     */
    public static boolean isACallAction(Object handle) {
        return handle instanceof MCallAction;
    }

    /**
     * Recognizer for CallEvent
     *
     * @param handle candidate
     * @return true if handle is a CallEvent
     */
    public static boolean isACallEvent(Object handle) {
        return handle instanceof MCallEvent;
    }

    /**
     * Recognizer for Class
     *
     * @param handle candidate
     * @return true if handle is a Class
     */
    public static boolean isAClass(Object handle) {
        return handle instanceof MClass;
    }

    /**
     * Recognizer for Classifier
     *
     * @param handle candidate
     * @return true if handle is a Classifier
     */
    public static boolean isAClassifier(Object handle) {
        return handle instanceof MClassifier;
    }

    /**
     * Recognizer for ClassifierRole
     *
     * @param handle candidate
     * @return true if handle is a ClassifierRole
     */
    public static boolean isAClassifierRole(Object handle) {
        return handle instanceof MClassifierRole;
    }

    /**
     * Recognizer for Comment
     *
     * @param handle candidate
     * @return true if handle is a Comment
     */
    public static boolean isAComment(Object handle) {
        return handle instanceof MComment;
    }

    /**
     * Recognizer for Collaboration
     *
     * @param handle candidate
     * @return true if handle is a Collaboration
     */
    public static boolean isACollaboration(Object handle) {
        return handle instanceof MCollaboration;
    }

    /**
     * Recognizer for Component
     *
     * @param handle candidate
     * @return true if handle is a Component
     */
    public static boolean isAComponent(Object handle) {
        return handle instanceof MComponent;
    }

    /**
     * Recognizer for ComponentInstance
     *
     * @param handle candidate
     * @return true if handle is a ComponentInstance
     */
    public static boolean isAComponentInstance(Object handle) {
        return handle instanceof MComponentInstance;
    }

    /**
     * Recognizer for Constraint
     *
     * @param handle candidate
     * @return true if handle is a Constraint
     */
    public static boolean isAConstraint(Object handle) {
        return handle instanceof MConstraint;
    }

    /**
     * Recognizer for CreateAction
     *
     * @param handle candidate
     * @return true if handle is a CreateAction
     */
    public static boolean isACreateAction(Object handle) {
        return handle instanceof MCreateAction;
    }

    public static boolean isActive(Object handle) {
        if (handle instanceof MClass) {
            return ((MClass) handle).isActive();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Recognizer for DataType
     *
     * @param handle candidate
     * @return true if handle is a DataType
     */
    public static boolean isADataType(Object handle) {
        return handle instanceof MDataType;
    }

    /**
     * Recognizer for DataValue
     *
     * @param handle candidate
     * @return true if handle is a DataValue
     */
    public static boolean isADataValue(Object handle) {
        return handle instanceof MDataValue;
    }

    /**
     * Recognizer for Dependency
     *
     * @param handle candidate
     * @return true if handle is a Dependency
     */
    public static boolean isADependency(Object handle) {
        return handle instanceof MDependency;
    }

    /**
     * Recognizer for DestroyAction
     *
     * @param handle candidate
     * @return true if handle is a DestroyAction
     */
    public static boolean isADestroyAction(Object handle) {
        return handle instanceof MDestroyAction;
    }

    /**
     * Recognizer for CompositeState
     *
     * @param handle candidate
     * @return true if handle is a CompositeState
     */
    public static boolean isACompositeState(Object handle) {
        return handle instanceof MCompositeState;
    }

    /**
     * Recognizer for Element
     *
     * @param handle candidate
     * @return true if handle is an Element
     */
    public static boolean isAElement(Object handle) {
        return handle instanceof MElement;
    }

    /**
     * Recognizer for ElementImport
     *
     * @param handle candidate
     * @return true if handle is an ElementImport
     */
    public static boolean isAElementImport(Object handle) {
        return handle instanceof MElementImport;
    }

    /**
     * Recognizer for ElementListener
     *
     * @param handle candidate
     * @return true if handle is an ElementListener
     */
    public static boolean isAElementListener(Object handle) {
        return handle instanceof MElementListener;
    }

    /**
     * Recognizer for ElementResidence
     *
     * @param handle candidate
     * @return true if handle is an ElementResidence
     */
    public static boolean isAElementResidence(Object handle) {
        return handle instanceof MElementResidence;
    }

    /**
     * Recognizer for Expression
     *
     * @param handle candidate
     * @return true if handle is an Expression
     */
    public static boolean isAExpression(Object handle) {
        return handle instanceof MExpression;
    }

    /**
     * Recognizer for Extend
     *
     * @param handle candidate
     * @return true if handle is an Extend
     */
    public static boolean isAExtend(Object handle) {
        return handle instanceof MExtend;
    }

    /**
     * Recognizer for ExtensionPoint
     *
     * @param handle candidate
     * @return true if handle is an ExtensionPoint
     */
    public static boolean isAExtensionPoint(Object handle) {
        return handle instanceof MExtensionPoint;
    }

    /**
     * Recognizer for Feature
     *
     * @param handle candidate
     * @return true if handle is a Feature
     */
    public static boolean isAFeature(Object handle) {
        return handle instanceof MFeature;
    }

    /**
     * Recognizer for FinalState
     *
     * @param handle candidate
     * @return true if handle is a FinalState
     */
    public static boolean isAFinalState(Object handle) {
        return handle instanceof MFinalState;
    }

    /**
     * Recognizer for Flow
     *
     * @param handle candidate
     * @return true if handle is a Flow
     */
    public static boolean isAFlow(Object handle) {
        return handle instanceof MFlow;
    }

    /**
     * Recognizer for Guard
     *
     * @param handle candidate
     * @return true if handle is a Guard
     */
    public static boolean isAGuard(Object handle) {
        return handle instanceof MGuard;
    }

    /**
     * Recognizer for GeneralizableElement
     *
     * @param handle candidate
     * @return true if handle is a GeneralizableElement
     */
    public static boolean isAGeneralizableElement(Object handle) {
        return handle instanceof MGeneralizableElement;
    }

    /**
     * Recognizer for GeneralizableElement
     *
     * @param handle candidate
     * @return true if handle is a GeneralizableElement
     */
    public static boolean isAGeneralization(Object handle) {
        return handle instanceof MGeneralization;
    }

    /**
     * Recognizer for Include
     *
     * @param handle candidate
     * @return true if handle is an Include
     */
    public static boolean isAInclude(Object handle) {
        return handle instanceof MInclude;
    }

    /**
     * Recognizer for Instance
     *
     * @param handle candidate
     * @return true if handle is a Instance
     */
    public static boolean isAInstance(Object handle) {
        return handle instanceof MInstance;
    }

    /**
     * Recognizer for Interaction
     *
     * @param handle candidate
     * @return true if handle is a Interaction
     */
    public static boolean isAInteraction(Object handle) {
        return handle instanceof MInteraction;
    }

    /**
     * Recognizer for Interface
     *
     * @param handle candidate
     * @return true if handle is a Interface
     */
    public static boolean isAInterface(Object handle) {
        return handle instanceof MInterface;
    }

    /**
     * Recognizer for Link
     *
     * @param handle candidate
     * @return true if handle is a Link
     */
    public static boolean isALink(Object handle) {
        return handle instanceof MLink;
    }

    /**
     * Recognizer for LinkEnd
    	 *
    	 * @param handle candidate
    	 * @return true if handle is a LinkEnd
    	 */
    public static boolean isALinkEnd(Object handle) {
        return handle instanceof MLinkEnd;
    }

    /**
     * Recognizer for Message
     *
     * @param handle candidate
     * @return true if handle is a Method
     */
    public static boolean isAMessage(Object handle) {
        return handle instanceof MMessage;
    }

    /**
     * Recognizer for Method
     *
     * @param handle candidate
     * @return true if handle is a Method
     */
    public static boolean isAMethod(Object handle) {
        return handle instanceof MMethod;
    }

    /**
     * Recognizer for Model
     *
     * @param handle candidate
     * @return true if handle is a Model
     */
    public static boolean isAModel(Object handle) {
        return handle instanceof MModel;
    }

    /**
     * Recognizer for ModelElement
     *
     * @param handle candidate
     * @return true if handle is a ModelElement
     */
    public static boolean isAModelElement(Object handle) {
        return handle instanceof MModelElement;
    }

    /**
     * Recognizer for Multiplicity
     *
     * @param handle candidate
     * @return true if handle is a Multiplicity
     */
    public static boolean isAMultiplicity(Object handle) {
        return handle instanceof MMultiplicity;
    }

    /**
     * Recognizer for Namespace
     *
     * @param handle candidate
     * @return true if handle is a Namespace
     */
    public static boolean isANamespace(Object handle) {
        return handle instanceof MNamespace;
    }

    /**
     * Recognizer for a Node
     *
     * @param handle candidate
     * @return true if handle is a Node
     */
    public static boolean isANode(Object handle) {
        return handle instanceof MNode;
    }

    /**
     * Recognizer for a NodeInstance
     *
     * @param handle candidate
     * @return true if handle is a NodeInstance
     */
    public static boolean isANodeInstance(Object handle) {
        return handle instanceof MNodeInstance;
    }

    /**
     * Recognizer for Operation
     *
     * @param handle candidate
     * @return true if handle is an Operation
     */
    public static boolean isAOperation(Object handle) {
        return handle instanceof MOperation;
    }

    /**
     * Recognizer for Object
     *
     * @param handle candidate
     * @return true if handle is an Object
     */
    public static boolean isAObject(Object handle) {
        return handle instanceof MObject;
    }

    /**
     * Recognizer for Parameter
     *
     * @param handle candidate
     * @return true if handle is a Parameter
     */
    public static boolean isAParameter(Object handle) {
        return handle instanceof MParameter;
    }

    /**
     * Recognizer for Permission
     *
     * @param handle candidate
     * @return true if handle is an Permission
     */
    public static boolean isAPermission(Object handle) {
        return handle instanceof MPermission;
    }

    /**
     * Recognizer for Package
     *
     * @param handle candidate
     * @return true if handle is a Package
     */
    public static boolean isAPackage(Object handle) {
        return handle instanceof MPackage;
    }

    /**
     * Recognizer for Pseudostate
     *
     * @param handle candidate
     * @return true if handle is a Pseudostate
     */
    public static boolean isAPseudostate(Object handle) {
        return handle instanceof MPseudostate;
    }

    /**
     * Recognizer for PseudostateKind
     *
     * @param handle candidate
     * @return true if handle is a PseudostateKind
     */
    public static boolean isAPseudostateKind(Object handle) {
        return handle instanceof MPseudostateKind;
    }

    // TODO: - Do we need this as well as getKind - I think not
    public static Object getPseudostateKind(Object handle) {
        if (handle instanceof MPseudostate) {
            return ((MPseudostate) handle).getKind();
        }
        throw new IllegalArgumentException("Unrecognized handle " + 
					   getClassNull(handle));
    }

    /**
     * Returns the receiver object of a message or stimulus
     * @param handle candidate
     * @return receiver
     */
    public static Object getReceiver(Object handle) {
        if (handle instanceof MStimulus) {
            return ((MStimulus) handle).getReceiver();
        }
        if (handle instanceof MMessage) {
            return ((MMessage) handle).getReceiver();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    public static Object getKind(Object handle) {
        if (handle instanceof MPseudostate) {
            return ((MPseudostate) handle).getKind();
        }
        if (handle instanceof MParameter) {
            return ((MParameter) handle).getKind();
        }
        throw new IllegalArgumentException("Unrecognized handle " + 
					   getClassNull(handle));
    }

    public static Object getLink(Object handle) {
        if (handle instanceof MLinkEnd) {
            return ((MLinkEnd) handle).getLink();
        }
        throw new IllegalArgumentException("Unrecognized handle " + 
					   getClassNull(handle));
    }

    /**
     * Check whether two pseudostatekinds are equal/of the same type.
     *
     * @return true if the are the same type
     * @param ps1 one kind
     * @param ps2 one kind
     */
    public static boolean equalsPseudostateKind(Object ps1, Object ps2) {
        if (isAPseudostateKind(ps1)) {
            return ((MPseudostateKind) ps1).equals(ps2);
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(ps1));
    }

    /**
     * Recognizer for Reception
     *
     * @param handle candidate
     * @return true if handle is a Reception
     */
    public static boolean isAReception(Object handle) {
        return handle instanceof MReception;
    }

    /**
     * Recognizer for Returnaction
     *
     * @param handle candidate
     * @return true if handle is a returnaction
     */
    public static boolean isAReturnAction(Object handle) {
        return handle instanceof MReturnAction;
    }

    /**
     * Recognizer for Relationship
     *
     * @param handle candidate
     * @return true if handle is a Relationship
     */
    public static boolean isARelationship(Object handle) {
        return handle instanceof MRelationship;
    }

    /**
     * Recognizer for SendAction
     *
     * @param handle candidate
     * @return true if handle is a SendAction
     */
    public static boolean isASendAction(Object handle) {
        return handle instanceof MSendAction;
    }

    /**
     * Recognizer for Signal
     *
     * @param handle candidate
     * @return true if handle is a Signal
     */
    public static boolean isASignal(Object handle) {
        return handle instanceof MSignal;
    }

    /**
     * Recognizer for StateMachine
     *
     * @param handle candidate
     * @return true if handle is a StateMachine
     */
    public static boolean isAStateMachine(Object handle) {
        return handle instanceof MStateMachine;
    }

    /**
     * Recognizer for stimulus
     *
     * @param handle candidate
     * @return true if handle is a stimulus
     */
    public static boolean isAStimulus(Object handle) {
        return handle instanceof MStimulus;
    }

    /**
     * Recognizer for StateVertex
     *
     * @param handle candidate
     * @return true if handle is a StateVertex
     */
    public static boolean isAStateVertex(Object handle) {
        return handle instanceof MStateVertex;
    }

    /**
     * Recognizer for Stereotype
     *
     * @param handle candidate
     * @return true if handle is a Stereotype
     */
    public static boolean isAStereotype(Object handle) {
        return handle instanceof MStereotype;
    }

    /**
     * Recognizer for StructuralFeature
     *
     * @param handle candidate
     * @return true if handle is a StructuralFeature
     */
    public static boolean isAStructuralFeature(Object handle) {
        return handle instanceof MStructuralFeature;
    }

    /**
     * Recognizer for State
     *
     * @param handle candidate
     * @return true if handle is a State
     */
    public static boolean isAState(Object handle) {
        return handle instanceof MState;
    }

    /**
     * Recognizer for Subsystem
     *
     * @param handle candidate
     * @return true if handle is a Subsystem
     */
    public static boolean isASubsystem(Object handle) {
        return handle instanceof MSubsystem;
    }

    /**
     * Recognizer for TaggedValue
     *
     * @param handle candidate
     * @return true if handle is a TaggedValue
     */
    public static boolean isATaggedValue(Object handle) {
        return handle instanceof MTaggedValue;
    }

    /**
     * Recognizer for Transition
     *
     * @param handle candidate
     * @return true if handle is a Transition
     */
    public static boolean isATransition(Object handle) {
        return handle instanceof MTransition;
    }

    /**
     * Recognizer for Usage
     *
     * @param handle candidate
     * @return true if handle is a Usage
     */
    public static boolean isAUsage(Object handle) {
        return handle instanceof MUsage;
    }

    /**
     * Recognizer for a Use Case
     *
     * @param handle candidate
     * @return true if handle is a Transition
     */
    public static boolean isAUseCase(Object handle) {
        return handle instanceof MUseCase;
    }

    /**
     * Recognizer for VisibilityKind
     *
     * @param handle candidate
     * @return true if handle is a VisibilityKind
     */
    public static boolean isAVisibilityKind(Object handle) {
        return handle instanceof MVisibilityKind;
    }

    /**
     * Recognizer for attributes that are changeable
     *
     * @param handle candidate
     * @return true if handle is changeable
     */
    public static boolean isChangeable(Object handle) {
        if (handle != null && handle instanceof MAttribute) {
            MChangeableKind changeability =
                ((MAttribute) handle).getChangeability();
            return MChangeableKind.CHANGEABLE.equals(changeability);

        } else if (handle != null && handle instanceof MAssociationEnd) {
            MChangeableKind changeability =
                ((MAssociationEnd) handle).getChangeability();
            return MChangeableKind.CHANGEABLE.equals(changeability);
        }
        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Recognizer for attributes with classifier scope.
     *
     * @param handle candidate
     * @return true if handle has classifier scope.
     */
    public static boolean isClassifierScope(Object handle) {
        if (handle instanceof MAttribute) {
            MAttribute a = (MAttribute) handle;
            return MScopeKind.CLASSIFIER.equals(a.getOwnerScope());
        }
        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Recognizer for concurent composite state.
     *
     * @param handle composite state
     * @return true if concurent.
     */
    public static boolean isConcurent(Object handle) {
        if (handle instanceof MCompositeState) {
            return ((MCompositeState) handle).isConcurent();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Recognizer for constructor.
     *
     * @param handle candidate
     * @return true if handle is a constructor.
     */
    public static boolean isConstructor(Object handle) {
        Object stereo = null;
        if (isAOperation(handle)) {
            if (ModelFacade.getStereotypes(handle).size() > 0) {
                stereo = ModelFacade.getStereotypes(handle).iterator().next();
            }
            if (ExtensionMechanismsHelper.getHelper()
                    .isStereotypeInh(stereo, "create", "BehavioralFeature")) {
                return true;
            }
            return false;
        }
        if (isAMethod(handle)) {
            Object specification =
                CoreHelper.getHelper().getSpecification(handle);
            if (ModelFacade.getStereotypes(specification).size() > 0) {
                stereo =
                    ModelFacade.getStereotypes(specification).iterator().next();
            }
            if (ExtensionMechanismsHelper.getHelper()
                    .isStereotypeInh(stereo, "create", "BehavioralFeature")) {
                return true;
            }
            return false;
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    public static boolean isFrozen(Object handle) {
        if (handle instanceof MChangeableKind) {
            MChangeableKind ck = (MChangeableKind) handle;
            return MChangeableKind.FROZEN.equals(ck);
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns true if a given associationend is a composite.
     * @param handle candidate
     * @return boolean
     */
    public static boolean isComposite(Object handle) {
        if (isAAssociationEnd(handle)) {
            boolean composite = false;
            MAssociationEnd end = (MAssociationEnd) handle;
            if (end.getAggregation() != null
                && end.getAggregation().equals(MAggregationKind.COMPOSITE))
                composite = true;
            return composite;
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns true if a given associationend is a composite.
     * @param handle candidate
     * @return boolean
     */
    public static boolean isAggregate(Object handle) {
        if (isAAssociationEnd(handle)) {
            boolean composite = false;
            MAssociationEnd end = (MAssociationEnd) handle;
            if (end.getAggregation() != null
                && end.getAggregation().equals(MAggregationKind.AGGREGATE))
                composite = true;
            return composite;
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Recognizer for attributes that are initialized.
     *
     * @param handle candidate
     * @return true if the attribute is initialized.
     */
    public static boolean isInitialized(Object handle) {
        if (handle instanceof MAttribute) {
            MExpression init = ((MAttribute) handle).getInitialValue();

            if (init != null
                && init.getBody() != null
                && init.getBody().trim().length() > 0)
                return true;
            return false;
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Recognizer for attributes with instance scope.
     *
     * @param handle candidate
     * @return true if handle has instance scope.
     */
    public static boolean isInstanceScope(Object handle) {
        if (handle instanceof MFeature) {
            MFeature a = (MFeature) handle;
            return MScopeKind.INSTANCE.equals(a.getOwnerScope());
        }
        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Recognizer for leafs
     *
     * @param handle candidate GeneralizableElement
     * @return true if handle is a leaf
     */
    public static boolean isLeaf(Object handle) {

        if (handle instanceof MGeneralizableElement) {
            return ((MGeneralizableElement) handle).isLeaf();
        }
        if (handle instanceof MOperation) {
            return ((MOperation) handle).isLeaf();
        }
        if (handle instanceof MReception) {
            return ((MReception) handle).isLeaf();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Recognizer for roots
     *
     * @param handle candidate GeneralizableElement
     * @return true if handle is a leaf
     */
    public static boolean isRoot(Object handle) {

        if (handle instanceof MGeneralizableElement) {
            return ((MGeneralizableElement) handle).isRoot();
        }
        if (handle instanceof MOperation) {
            return ((MOperation) handle).isRoot();
        }
        if (handle instanceof MReception) {
            return ((MReception) handle).isRoot();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Recognizer for specifications
     *
     * @param handle candidate ModelElement
     * @return true if handle is a specification
     */
    public static boolean isSpecification(Object handle) {

        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).isSpecification();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Recognizer for Navigable elements
     *
     * @param handle candidate
     * @return true if handle is navigable
     */
    public static boolean isNavigable(Object handle) {
        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd) handle).isNavigable();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Recognizer for primary objects.
     * A primary object is an object that is created by the parser or
     * by a user.
     * Object that are created when importing some other object are not.
     *
     * @param handle candidate
     * @return true if primary object.
     */
    public static boolean isPrimaryObject(Object handle) {
        if (handle instanceof MModelElement) {
            MModelElement element = (MModelElement) handle;
            for (Iterator i = element.getTaggedValues().iterator();
		 i.hasNext(); ) {
                MTaggedValue tv = (MTaggedValue) i.next();
                if ((MMUtil.GENERATED_TAG).equals(tv.getTag())) {
                    return false;
                }
            }
            return true;
        }
        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Recognizer for attributes with private
     *
     * @param handle candidate
     * @return true if handle has private
     */
    public static boolean isPrivate(Object handle) {
        if (handle instanceof MModelElement) {
            MModelElement element = (MModelElement) handle;
            return MVisibilityKind.PRIVATE.equals(element.getVisibility());
        }
        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Recognizer for attributes with public
     *
     * @param handle candidate
     * @return true if handle has public
     */
    public static boolean isPublic(Object handle) {
        if (handle instanceof MModelElement) {
            MModelElement element = (MModelElement) handle;
            return MVisibilityKind.PUBLIC.equals(element.getVisibility());
        }
        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Recognizer for MBehaviouralFeature's that are queries.
     *
     * @param handle candidate
     * @return true if it is a query
     */
    public static boolean isQuery(Object handle) {

        if (!(handle instanceof MBehavioralFeature)) {
            throw new IllegalArgumentException("Unrecognized object " +
					       getClassNull(handle));
        }

        return ((MBehavioralFeature) handle).isQuery();
    }

    /**
     * Recognizer for attributes with protected
     *
     * @param handle candidate
     * @return true if handle has protected
     */
    public static boolean isProtected(Object handle) {
        if (handle instanceof MModelElement) {
            MModelElement element = (MModelElement) handle;
            return MVisibilityKind.PROTECTED.equals(element.getVisibility());
        }
        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Recognizer for realize
     *
     * @param handle candidate
     * @return true if handle has a realize stereotype
     */
    public static boolean isRealize(Object handle) {
        return isStereotype(handle, "realize");
    }

    /**
     * Recognizer for return
     *
     * @param handle candidate parameter
     * @return true if handle is a return parameter.
     */
    public static boolean isReturn(Object handle) {
        if (handle instanceof MParameter) {
            MParameter p = (MParameter) handle;
            return MParameterDirectionKind.RETURN.equals(p.getKind());

        }
        // ...
        throw new IllegalArgumentException("Unrecognized object " +
					   getClassNull(handle));
    }

    /**
     * Recognizer for singleton.
     *
     * @param handle candidate
     * @return true if handle is a singleton.
     */
    public static boolean isSingleton(Object handle) {
        return isStereotype(handle, "singleton");
    }

    /**
     * Recognizer for model elements with a given stereotype.
     *
     * @param handle candidate model element
     * @param stereotypename a string that is the stereotype name.
     * @return true if handle is an object that has the given stereotype.
     */
    public static boolean isStereotype(Object handle, String stereotypename) {
        if (handle instanceof MModelElement) {
            MModelElement element = (MModelElement) handle;
            MStereotype meSt = element.getStereotype();

            if (meSt == null)
                return false;

            String name = meSt.getName();
            if (name == null)
                return false;

            return name.equalsIgnoreCase(stereotypename);
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    public static boolean isTop(Object handle) {
	if (isACompositeState(handle))
            return ((MCompositeState) handle).getStateMachine() != null;
	throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Recognizer for type.
     *
     * @param handle candidate
     * @return true if handle is a type.
     */
    public static boolean isType(Object handle) {
        return isStereotype(handle, "type");
    }

    /**
     * Recognizer for utility.
     *
     * @param handle candidate
     * @return true if handle is a utility.
     */
    public static boolean isUtility(Object handle) {
        return isStereotype(handle, "utility");
    }

    ////////////////////////////////////////////////////////////////
    // Recognizer methods for the diagrams (in alphabetic order)

    /**
     * Recognizer for Diagram.
     *
     * @param handle candidate
     * @return true if handle is a diagram.
     */
    public static boolean isADiagram(Object handle) {
        return handle instanceof Diagram;
    }

    ////////////////////////////////////////////////////////////////
    // Getters for the UML model (in alphabetic order)

    /**
     * Returns the association end between some classifier and some
     * associaton or the association belonging to the given link.
     *
     * @param end is the link
     * @return association end
     */
    public static Object getAssociation(Object end) {

        if (end instanceof MAssociationEnd) {
            return ((MAssociationEnd) end).getAssociation();
        }
        if (end instanceof MLink) {
        	return ((MLink) end).getAssociation();
        }
        throw new IllegalArgumentException("Unrecognized object " + end);
    }

    /**
     * Returns the association end between some classifier and some associaton.
     *
     * @param type is the classifier
     * @param assoc is the association
     * @return association end
     */
    public static Object getAssociationEnd(Object type, Object assoc) {
        if (!(type instanceof MClassifier
            && assoc instanceof MAssociation))
            throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(type) + " or " +
                                           getClassNull(assoc));
        Iterator it = ((MClassifier) type).getAssociationEnds().iterator();
        while (it.hasNext()) {
            MAssociationEnd end = (MAssociationEnd) it.next();
            if (((MAssociation) assoc).getConnections().contains(end))
                return end;
        }
        return null;
    }

    /**
     * The list of Association Ends
     *
     * @param handle the object that we get the association ends from.
     * @return Collection with association ends.
     */
    public static Collection getAssociationEnds(Object handle) {
        if (handle instanceof MClassifier) {
            Collection endc = ((MClassifier) handle).getAssociationEnds();
            return endc;
        }

        //...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * The list of association roles
     *
     * @param handle the object that we get the association roles from.
     * @return Collection of association roles.
     */
    public static Collection getAssociationRoles(Object handle) {
        if (handle instanceof MAssociation) {
            return ((MAssociation) handle).getAssociationRoles();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * The list of Attributes.
     *
     * @param handle classifier to examine.
     * @return iterator with attributes.
     */
    public static Collection getAttributes(Object handle) {
        if (handle instanceof MClassifier) {
            MClassifier c = (MClassifier) handle;
            // TODO: We are converting back and forth between collections and
            // iterators. I (Linus) prefer iterators.
            //return getStructuralFeatures(c).iterator();
            //...But I (thn) got CVS conflicts, so:
            return getStructuralFeatures(c);
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * The baseclass of some stereotype
     * @param handle the stereotype
     * @return the baseclass
     */
    public static Object getBaseClass(Object handle) {
        if (isAStereotype(handle)) {
            return ((MStereotype) handle).getBaseClass();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * The base of some model element
     * @param handle the model element
     * @return the base
     */
    public static Object getBase(Object handle) {
        if (handle instanceof MAssociationEndRole) {
            return ((MAssociationEndRole) handle).getBase();
        } else if (handle instanceof MAssociationRole) {
            return ((MAssociationRole) handle).getBase();
        } else if (handle instanceof MExtend) {
            return ((MExtend) handle).getBase();
        } else if (handle instanceof MInclude) {
            return ((MInclude) handle).getBase();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the bases of a classifier role.
     *
     *
     * @param handle classifier role.
     * @return the bases.
     */
    public static Collection getBases(Object handle) {
        if (handle instanceof MClassifierRole)
            return ((MClassifierRole) handle).getBases();
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the behaviors of a Modelelement.
     *
     *
     * @param handle modelelement to examine.
     * @return the behaviors.
     */
    public static Collection getBehaviors(Object handle) {
        if (isAModelElement(handle))
            return ((MModelElement) handle).getBehaviors();
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the behavioral feature of an parameter.
     *
     * @param handle expression.
     * @return the behavioral feature.
     */
    public static Object getBehavioralFeature(Object handle) {
        if (handle instanceof MParameter)
            return ((MParameter) handle).getBehavioralFeature();
        throw new IllegalArgumentException("Unrecognized object "
					   + getClassNull(handle));
    }

    /**
     * Get the body of an method/constraint/expression.
     *
     *
     * @param handle expression.
     * @return the body.
     */
    public static Object getBody(Object handle) {
        if (handle instanceof MMethod)
            return ((MMethod) handle).getBody();
        if (handle instanceof MConstraint)
            return ((MConstraint) handle).getBody();
        if (handle instanceof MExpression)
            return ((MExpression) handle).getBody();
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    public static Object getChangeability(Object handle) {
        if (handle instanceof MStructuralFeature) {
            return ((MStructuralFeature) handle).getChangeability();
        }
        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd) handle).getChangeability();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the child of a generalization.
     *
     * @param handle generalization.
     * @return the child.
     */
    public static Object getChild(Object handle) {
        if (handle instanceof MGeneralization) {
            return ((MGeneralization) handle).getChild();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the children of some generalizable element
     *
     * @param handle to the generalizable element.
     * @return a collection with all children.
     */
    public static Collection getChildren(Object handle) {
        if (isAGeneralizableElement(handle)) {
            return ((MGeneralizableElement) handle).getChildren();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Gets the classifiers roles of some model element
     * @param handle the model element
     * @return the classifiers roles of the instance
     */
    public static Collection getClassifierRoles(Object handle) {
        if (handle instanceof MFeature) {
            return ((MFeature) handle).getClassifierRoles();
        }
        if (handle instanceof MClassifier) {
            return ((MClassifier) handle).getClassifierRoles();
        }       
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Gets the classifierss of some instance
     * @param handle the instance
     * @return the classifierss of the instance
     */
    public static Collection getClassifiers(Object handle) {
        if (handle instanceof MInstance) {
            return ((MInstance) handle).getClassifiers();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Gets the classifiers in state of some model element
     * @param handle the model element
     * @return the classifierss in state
     */
    public static Collection getClassifiersInState(Object handle) {
        if (handle instanceof MClassifier) {
            return ((MClassifier) handle).getClassifiersInState();
        }
        if (handle instanceof MState) {
            return ((MState) handle).getClassifiersInState();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Gets the clients of some dependency
     * @param handle the dependency
     * @return the clients of the dependency
     */
    public static Collection getClients(Object handle) {
        if (isADependency(handle)) {
            return ((MDependency) handle).getClients();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the client dependencies of some classifier
     *
     * @param handle to the classifier.
     * @return an iterator with all client dependencies.
     */
    public static Collection getClientDependencies(Object handle) {
        if (isAModelElement(handle)) {
            Collection c = ((MModelElement) handle).getClientDependencies();
            return c;
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the condition of an extend.
     *
     * @param o extend
     * @return the condition
     */
    public static Object getCondition(Object o) {
        if (o != null && o instanceof MExtend) {
            return ((MExtend) o).getCondition();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(o));
    }

    /**
     * Get the concurrency of an operation.
     *
     * @param o operation.
     * @return the concurrency.
     */
    public static Object getConcurrency(Object o) {
        if (o != null && o instanceof MOperation) {
            return ((MOperation) o).getConcurrency();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(o));
    }

    //    public static short getConcurrency(Object o) {
    //        if (o != null && o instanceof MOperation) {
    //            return ((MOperation) o).getConcurrency()
    //                == MCallConcurrencyKind.GUARDED
    //                ? GUARDED
    //                : SEQUENTIAL;
    //        }
    //        throw new IllegalArgumentException("Unrecognized object " + o);
    //    }
    //
    /**
     * The list of connections to an association or link.
     *
     * @param handle to the association or link
     * @return a Collection with all connections.
     */
    public static Collection getConnections(Object handle) {
        if (handle instanceof MAssociation) {
            return ((MAssociation) handle).getConnections();
        }
        if (handle instanceof MLink) {
            return ((MLink) handle).getConnections();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Determine if a model element contains a connection.
     *
     * @param element is the model element
     * @param connection is the connection that is searched for.
     * @return true if the model element contains a connection
     */
    public boolean containsConnection(Object element, Object connection) {
        if (element instanceof MAssociation) {
            return ((MAssociation) element).getConnections().contains(
                connection);
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object "
					   + getClassNull(element));
    }

    /**
     * Count the number of Connections or AssociationEnds to an Association.
     *
     * @param handle to the association.
     * @return an Iterator with all connections.
     */
    public static int getConnectionCount(Object handle) {
        if (handle instanceof MAssociation) {
            return ((MAssociation) handle).getConnections().size();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object "
					   + getClassNull(handle));
    }

    /**
     * Returns the effect of some transition
     *
     * @param handle is the transition
     * @return the effect
     */
    public static Object getEffect(Object handle) {
        if (handle instanceof MTransition) {
            return ((MTransition) handle).getEffect();
        }
        throw new IllegalArgumentException("Unrecognized object "
					   + getClassNull(handle));
    }

    /**
     * Get the residences of an element.
     *
     * @param handle the model element that we are getting the residences of
     * @return the residence collection
     */
    public static Collection getElementResidences(Object handle) {
        if (handle instanceof MModelElement)
            return ((MModelElement) handle).getElementResidences();
        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    public static Collection getElementImports2(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getElementImports2();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the entry action to a state
     *
     * @param handle is the state
     * @return the entry
     */
    public static Object getEntry(Object handle) {
        if (handle instanceof MState) {
            return ((MState) handle).getEntry();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the exit action to a state
     *
     * @param handle is the state
     * @return the exit action
     */
    public static Object getExit(Object handle) {
        if (handle instanceof MState) {
            return ((MState) handle).getExit();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    public static Object getExpression(Object handle) {
        if (handle instanceof MGuard)
            return ((MGuard) handle).getExpression();
        
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
        
    }

    /**
     * Returns all extends of a use case or extension point
     *
     * @param handle is the use case or the extension point
     * @return the extends
     */
    public static Collection getExtends(Object handle) {
        if (handle instanceof MUseCase) {
            return ((MUseCase) handle).getExtends();
        }
        if (handle instanceof MExtensionPoint) {
            return ((MExtensionPoint) handle).getExtends();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns all extends of a use case
     *
     * @param handle is the use case
     * @return the extends
     */
    public static Collection getExtends2(Object handle) {
        if (handle instanceof MUseCase) {
            return ((MUseCase) handle).getExtends2();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Gets the use case extension of an extend
     *
     * @param handle is the extend
     * @return The extension
     */
    public static Object getExtension(Object handle) {
        if (handle instanceof MExtend)
            return ((MExtend) handle).getExtension();
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    public static Object getExtensionPoint(Object handle, int index) {
        if (handle instanceof MExtend) {
            return ((MExtend) handle).getExtensionPoint(index);
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns all extends of a use case
     *
     * @param handle is the use case or the extend
     * @return the extends
     */
    public static Collection getExtensionPoints(Object handle) {
        if (handle instanceof MUseCase) {
            return ((MUseCase) handle).getExtensionPoints();
        }
        if (handle instanceof MExtend) {
            return ((MExtend) handle).getExtensionPoints();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * The list of Features from a Classifier.
     *
     * @param handle Classifier to retrieve from.
     * @return Collection with Features
     */
    public static Collection getFeatures(Object handle) {
        if (handle != null && handle instanceof MClassifier)
            return ((MClassifier) handle).getFeatures();
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Gets the generalization between two generalizable elements.
     * Returns null if there is none.
     *
     * @param child is the child
     * @param parent is the parent
     * @return The generalization
     */
    public static Object getGeneralization(Object child, Object parent) {
        if (!(child instanceof MGeneralizableElement
             && parent instanceof MGeneralizableElement))
            throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(child) + " or " +
                                           getClassNull(parent));
        Iterator it = getGeneralizations(child).iterator();
        while (it.hasNext()) {
            MGeneralization gen = (MGeneralization) it.next();
            if (gen.getParent() == parent) {
                return gen;
            }
        }
        return null;
    }

    /**
     * The list of Generalizations from a GeneralizableElement.
     *
     * @param handle GeneralizableElement to retrieve from.
     * @return Generalizations
     */
    public static Collection getGeneralizations(Object handle) {
        if (handle instanceof MGeneralizableElement) {
            MGeneralizableElement ge = (MGeneralizableElement) handle;
            return ge.getGeneralizations();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Gets the guard for some given transition.
     *
     * @param handle is the transition
     * @return Object
     */
    public static Object getGuard(Object handle) {
        if (isATransition(handle)) {
            return ((MTransition) handle).getGuard();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    public static Object getIcon(Object handle) {
        if (handle instanceof MStereotype) {
            return ((MStereotype) handle).getIcon();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Gets the component of some element residence
     *
     * @param handle is an element residence
     * @return component
     */
    public static Object getImplementationLocation(Object handle) {
        if (handle instanceof MElementResidence) {
            return ((MElementResidence) handle).getImplementationLocation();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the includes for some use case
     *
     * @param handle is the use case
     * @return the includes as a Collection
     */
    public static Collection getIncludes(Object handle) {
        if (handle instanceof MUseCase) {
            return ((MUseCase) handle).getIncludes();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the includes for some use case
     *
     * @param handle is the use case
     * @return the includes as a Collection
     */
    public static Collection getIncludes2(Object handle) {
        if (handle instanceof MUseCase) {
            return ((MUseCase) handle).getIncludes();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the incoming transitions for some statevertex
     *
     * @param handle is the state vertex
     * @return Collection
     */
    public static Collection getIncomings(Object handle) {
        if (isAStateVertex(handle)) {
            return ((MStateVertex) handle).getIncomings();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the initial value for some attribute.
     *
     * @param handle is the attribute
     * @return initial value
     */
    public static Object getInitialValue(Object handle) {
        if (handle instanceof MAttribute) {
            return ((MAttribute) handle).getInitialValue();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the instance of an AttributeLink or LinkEnd
     *
     * @param handle is the attribute link or link end
     * @return initial value
     */
    public static Object getInstance(Object handle) {
        if (handle instanceof MAttributeLink) {
            return ((MAttributeLink) handle).getInstance();
        }
        if (handle instanceof MLinkEnd) {
            return ((MLinkEnd) handle).getInstance();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the Instances for some Clasifier
     *
     * @param handle is the classifier
     * @return Collection
     */
    public static Collection getInstances(Object handle) {
        if (handle instanceof MClassifier) {
            return ((MClassifier) handle).getInstances();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the interaction for some message
     *
     * @param handle is the message
     * @return the interaction
     */
    public static Object getInteraction(Object handle) {
        if (handle instanceof MMessage) {
            return ((MMessage) handle).getInteraction();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the interactions belonging to a collaboration
     *
     * @param handle is the collaboration
     * @return Collection
     */
    public static Collection getInteractions(Object handle) {
        if (handle instanceof MCollaboration) {
            return ((MCollaboration) handle).getInteractions();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the internal transitions belonging to a state
     *
     * @param handle is the state
     * @return Collection
     */
    public static Collection getInternalTransitions(Object handle) {
        if (handle instanceof MState) {
            return ((MState) handle).getInternalTransitions();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the messages belonging to some interaction
     *
     * @param handle candidate
     * @return Collection
     */
    public static Collection getMessages(Object handle) {
        if (isAInteraction(handle)) {
            return ((MInteraction) handle).getMessages();
        }
        if (handle instanceof MAssociationRole) {
            return ((MAssociationRole) handle).getMessages();
        }
        if (handle instanceof MAction) {
            return ((MAction) handle).getMessages();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the messages belonging to some other message
     *
     * @param handle is the message
     * @return Collection
     */
    public static Collection getMessages3(Object handle) {
        if (handle instanceof MMessage) {
            return ((MMessage) handle).getMessages3();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    public static Collection getMessages4(Object handle) {
        if (handle instanceof MMessage) {
            return ((MMessage) handle).getMessages4();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the messages belonging to some classifier role
     *
     * @param handle is the classifier role
     * @return Collection
     */
    public static Collection getMessages1(Object handle) {
        if (handle instanceof MClassifierRole) {
            return ((MClassifierRole) handle).getMessages1();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    public static Collection getMessages2(Object handle) {
        if (handle instanceof MClassifierRole) {
            return ((MClassifierRole) handle).getMessages2();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the model of some model element
     *
     * @param handle to the model element.
     * @return model for the model element.
     */
    public static Object getModel(Object handle) {
        if (isAModelElement(handle)) {
            MModel m = ((MModelElement) handle).getModel();
            return m;
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * @param elemImport an Element Import.
     * @return the model element
     */
    public static Object getModelElement(Object elemImport) {
        if (!(elemImport instanceof MElementImport))
            throw new IllegalArgumentException("Unrecognized object " +
					       getClassNull(elemImport));

        return ((MElementImport) elemImport).getModelElement();
    }

    /**
     * Get the Multiplicity from a model element.
     *
     * @param handle model element to retrieve from.
     * @return multiplicity
     */
    public static Object getMultiplicity(Object handle) {
        if ((handle instanceof MAssociationEnd))
            return ((MAssociationEnd) handle).getMultiplicity();
        if ((handle instanceof MAssociationRole))
            return ((MAssociationRole) handle).getMultiplicity();
        if ((handle instanceof MClassifierRole))
            return ((MClassifierRole) handle).getMultiplicity();
        if ((handle instanceof MStructuralFeature))
            return ((MStructuralFeature) handle).getMultiplicity();
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the comments of an element.
     *
     * @param handle the model element that we are getting the comments of
     * @return the comment (or null)
     */
    public static Collection getComments(Object handle) {
        if (handle instanceof MModelElement)
            return ((MModelElement) handle).getComments();
        // ...
        throw new IllegalArgumentException("Unrecognized object " +
					   getClassNull(handle));
    }

    /**
     * Get the communication connection of an message.
     *
     * @param handle the message that we are getting the communication
     * connection
     * @return the communication connection
     */
    public static Object getCommunicationConnection(Object handle) {
        if (handle instanceof MMessage)
            return ((MMessage) handle).getCommunicationConnection();
        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the communication link of a stimulus.
     *
     * @param handle the message that we are getting the communication link
     * @return the communication link
     */
    public static Object getCommunicationLink(Object handle) {
        if (handle instanceof MStimulus)
            return ((MStimulus) handle).getCommunicationLink();
        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the collaborations of an element.
     *
     * @param handle the model element that we are getting the collaborations of
     * @return the collaborations
     */
    public static Collection getCollaborations(Object handle) {
        if (handle instanceof MOperation)
            return ((MOperation) handle).getCollaborations();
        if (handle instanceof MClassifier)
            return ((MClassifier) handle).getCollaborations();
        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Add a new comment to a model element
     *
     * @param element the element to which the comment is to be added
     * @param comment the comment for the model element
     */
    public static void addComment(Object element, Object comment) {
        if (element instanceof MModelElement && comment instanceof MComment) {
            ((MModelElement) element).addComment((MComment) comment);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(element));
    }

    /**
     * Get the component instance of an instance
     *
     * @param handle is the instance
     * @return the component instance
     */
    public static Object getComponentInstance(Object handle) {
        if (handle instanceof MInstance)
            return ((MInstance) handle).getComponentInstance();
        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    public static Collection getConstrainingElements(Object handle) {
        if (handle instanceof MCollaboration) {
            return ((MCollaboration) handle).getConstrainingElements();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    public static Collection getConstraints(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getConstraints();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
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
    public static Object getModelElementContainer(Object handle) {
        if (handle instanceof MBase) {
            return ((MBase) handle).getModelElementContainer();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    public static Object getContainer(Object handle) {
        if (handle instanceof MStateVertex) {
            return ((MStateVertex) handle).getContainer();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the context of some given statemachine or the context
     * of some given interaction.
     *
     * @param handle the statemachine or the interaction
     * @return the context of the statemachine or interaction or null
     * if the statemachine or interaction doesn't have a context.
     */
    public static Object getContext(Object handle) {
        if (isAStateMachine(handle)) {
            return ((MStateMachine) handle).getContext();
        }
        if (isAInteraction(handle)) {
            return ((MInteraction) handle).getContext();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    public static Collection getContexts(Object handle) {
        if (handle instanceof MSignal) {
            return ((MSignal) handle).getContexts();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    public static Collection getCreateActions(Object handle) {
        if (handle instanceof MClassifier) {
            return ((MClassifier) handle).getCreateActions();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the default value of a parameter
     *
     * @param handle the parameter that we are getting the defaultvalue from
     * @return the default value
     */
    public static Object getDefaultValue(Object handle) {
        if (handle instanceof MParameter) {
            return ((MParameter) handle).getDefaultValue();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get deferrable events of a state
     *
     * @param handle the state that we are getting the deferrable event from
     * @return the deferrable events collection
     */
    public static Collection getDeferrableEvents(Object handle) {
        if (handle instanceof MState) {
            return ((MState) handle).getDeferrableEvents();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the context of some given statemachine or the context
     * of some given interaction
     * @param handle the statemachine or the interaction
     * @return the context of the statemachine or interaction or null
     * if the statemachine or interaction doesn't have a context.
     */
    public static Collection getDeploymentLocations(Object handle) {
        if (isAComponent(handle)) {
            return ((MComponent) handle).getDeploymentLocations();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the discriminator.
     *
     * @param handle the Generalization
     * @return the discriminator a String
     */
    public static Object getDiscriminator(Object handle) {
        if (handle instanceof MGeneralization) {
            return ((MGeneralization) handle).getDiscriminator();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the dispatchaction of a stimulus.
     *
     * @param handle the stimulus that we are getting the dispatchaction of
     * @return the dispatchaction (or null)
     */
    public static Object getDispatchAction(Object handle) {
        if (handle instanceof MStimulus) {
            return ((MStimulus) handle).getDispatchAction();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the do activity action of a state
     *
     * @param handle is the state
     * @return the do activity
     */
    public static Object getDoActivity(Object handle) {
        if (handle instanceof MState) {
            return ((MState) handle).getDoActivity();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    public static Collection getLinks(Object handle) {
        if (handle instanceof MAssociation) {
            return ((MAssociation) handle).getLinks();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    public static Collection getLinkEnds(Object handle) {
        if (handle instanceof MInstance) {
            return ((MInstance) handle).getLinkEnds();
        }
        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd) handle).getLinkEnds();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Gets a location of some extension point.
     * @param handle extension point
     * @return the location
     */
    public static String getLocation(Object handle) {
        if (handle instanceof MExtensionPoint) {
            return ((MExtensionPoint) handle).getLocation();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the namespace of an element.
     *
     * @param handle the model element that we are getting the namespace of
     * @return the namespace (or null)
     */
    public static Object getNamespace(Object handle) {
        if (handle instanceof MModelElement)
            return ((MModelElement) handle).getNamespace();
        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the node instance of a component instance.
     *
     * @param handle the model element that we are getting the node instance of
     * @return the node instance
     */
    public static Object getNodeInstance(Object handle) {
        if (handle instanceof MComponentInstance)
            return ((MComponentInstance) handle).getNodeInstance();
        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * The collection of object flow states
     *
     * @param handle the classifier
     * @return collection of object flow states
     */
    public static Collection getObjectFlowStates(Object handle) {
        if (handle instanceof MClassifier) {
            return ((MClassifier) handle).getObjectFlowStates();
        }
        throw new IllegalArgumentException("Unrecognized object " +
					   getClassNull(handle));
    }

    /**
     * Get the operation of a Call Action or Call Event.
     *
     * @param handle the model element that we are getting the operation of
     * @return the Operation
     */
    public static Object getOperation(Object handle) {
        if (handle instanceof MCallAction) {
            return ((MCallAction) handle).getOperation();
        }
        if (handle instanceof MCallEvent) {
            return ((MCallEvent) handle).getOperation();
        }
        // ...
        throw new IllegalArgumentException("Unrecognized object " +
					   getClassNull(handle));
    }

    /**
     * Get the list of operations.
     *
     * @param handle classifier to examine.
     * @return Collection with operations.
     */
    public static Collection getOperations(Object handle) {
        if (handle instanceof MClassifier) {
            MClassifier c = (MClassifier) handle;
            return getOperations(c);
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the list of Operations of this classifier and all inherited.
     *
     * @param handle classifier to examine.
     * @return Iterator with operations.
     */
    public static Iterator getOperationsInh(Object handle) {
        if (handle instanceof MClassifier) {
            MClassifier c = (MClassifier) handle;

            // TODO: We are converting back and forth between collections and
            // iterators. I (Linus) prefer iterators.
            return CoreHelper.getHelper().getOperationsInh(c).iterator();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the opposite end of an association end.
     *
     * @param handle is the association end
     * @return Object the opposite end.
     */
    public static Object getOppositeEnd(Object handle) {
        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd) handle).getOppositeEnd();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get ordering of an association end
     *
     * @param handle association end to retrieve from
     * @return ordering
     */
    public static Object getOrdering(Object handle) {
        if (handle instanceof MAssociationEnd)
            return ((MAssociationEnd) handle).getOrdering();

        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the list of Transitions outgoing from the given stateVertex.
     *
     * @param handle statevertex
     * @return Collection
     */
    public static Collection getOutgoings(Object handle) {
        if (ModelFacade.isAStateVertex(handle)) {
            return ((MStateVertex) handle).getOutgoings();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the list of Associations Ends connected to this association end.
     *
     * @param handle association end to start from
     * @return Iterator with all connected association ends.
     */
    public static Collection getOtherAssociationEnds(Object handle) {
        if (handle instanceof MAssociationEnd) {
            MAssociation a = ((MAssociationEnd) handle).getAssociation();

            if (a == null)
                return emptyCollection();

            Collection allEnds = a.getConnections();
            if (allEnds == null)
                return emptyCollection();

            // TODO: An Iterator filter would be nice here instead of the
            // mucking around with the Collection.
            allEnds = new ArrayList(allEnds);
            allEnds.remove(handle);
            return allEnds;
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * The list of owned elements of the the package.
     *
     * @param handle package to retrieve from.
     * @return Iterator with operations
     */
    public static Collection getOwnedElements(Object handle) {
        if (handle instanceof MNamespace) {
            return ((MNamespace) handle).getOwnedElements();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the owner scope of a feature
     *
     * @param handle feature
     * @return owner scope
     */
    public static Object getOwnerScope(Object handle) {
        if (handle instanceof MFeature) {
            return ((MFeature) handle).getOwnerScope();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the powertype of a generalization
     *
     * @param handle generalization
     * @return powertype
     */
    public static Object getPowertype(Object handle) {
        if (handle instanceof MGeneralization) {
            return ((MGeneralization) handle).getPowertype();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the powertype ranges of a classifier.
     *
     * @param handle classifier to retrieve from
     * @return collection of poertype ranges
     */
    public static Collection getPowertypeRanges(Object handle) {
        if (handle instanceof MClassifier) {
            return ((MClassifier) handle).getPowertypeRanges();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the predecessors of a message.
     *
     * @param handle message to retrieve from
     * @return collection of predecessors
     */
    public static Collection getPredecessors(Object handle) {
        if (handle instanceof MMessage) {
            return ((MMessage) handle).getPredecessors();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Determine if the passed parameter has a RETURN direction kind
     *
     * @return true if it is a return direction kind
     * @param o is the object
     */
    public static boolean hasReturnParameterDirectionKind(Object o) {
        if (!(o instanceof MParameter))
            throw new IllegalArgumentException("Unrecognized object " +
                getClassNull(o));
        
        MParameter parameter = (MParameter) o;
        return (MParameterDirectionKind.RETURN.equals(parameter.getKind()));
    }

    public static Object getPackage(Object handle) {
        if (handle instanceof MElementImport) {
            return ((MElementImport) handle).getPackage();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get a parameter of a behavioral feature.
     *
     * @param handle behavioral feature to retrieve from
     * @param n parameter number
     * @return parameter.
     */
    public static Object getParameter(Object handle, int n) {
        if (handle instanceof MBehavioralFeature)
            return ((MBehavioralFeature) handle).getParameter(n);

        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the parameters of a behavioral feature.
     *
     * @param handle operation to retrieve from
     * @return Iterator with operations.
     */
    public static Collection getParameters(Object handle) {
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
        throw new IllegalArgumentException("Unrecognized object " +
					   getClassNull(handle));
    }

    /**
     * Get the parent of a generalization.
     *
     * TODO: Check that the concepts parent and child exist in the UML model.
     *
     * @param handle generalization.
     * @return the parent.
     */
    public static Object getParent(Object handle) {
        if (handle instanceof MGeneralization) {
            return ((MGeneralization) handle).getParent();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the raised signals of an operation.
     * 
     * @param handle is the operation
     * @return raised signals
     */
    public static Collection getRaisedSignals(Object handle) {
        if (handle instanceof MOperation) {
            return ((MOperation) handle).getRaisedSignals();
        }
        throw new IllegalArgumentException("Unrecognized handle: " + 
					   getClassNull(handle));
    }

    /**
     * Returns the receptions of a signal.
     *
     * @param handle is the signal
     * @return receptions
     */
    public static Collection getReceptions(Object handle) {
        if (handle instanceof MSignal) {
            return ((MSignal) handle).getReceptions();
        }
        throw new IllegalArgumentException("Unrecognized handle: " + 
					   getClassNull(handle));
    }

    /**
     * Returns the recurrence iteration expression of an action.
     *
     * @param handle is the action.
     * @return the recurrence
     */
    public static Object getRecurrence(Object handle) {
        if (handle instanceof MAction) {
            return ((MAction) handle).getRecurrence();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the represented classifier of a collaboration.
     *
     * @param handle is the collaboration
     * @return represented classifier
     */
    public static Object getRepresentedClassifier(Object handle) {
        if (handle instanceof MCollaboration) {
            return ((MCollaboration) handle).getRepresentedClassifier();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the represented operation of a collaboration.
     *
     * @param handle is the collaboration
     * @return represented operation
     */
    public static Object getRepresentedOperation(Object handle) {
        if (handle instanceof MCollaboration) {
            return ((MCollaboration) handle).getRepresentedOperation();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the script belonging to a given action
     *
     * @param handle is the action
     * @return the script
     */
    public static Object getScript(Object handle) {
        if (handle instanceof MAction) {
            return ((MAction) handle).getScript();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the sender object of a stimulus or a message
     *
     * @param handle is the stimulus or message
     * @return the sender
     */
    public static Object getSender(Object handle) {
        if (handle instanceof MStimulus) {
            return ((MStimulus) handle).getSender();
        }
        if (handle instanceof MMessage) {
            return ((MMessage) handle).getSender();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the sender object of a stimulus or a message
     * TODO: Check if this javadoc comment is really correct?
     *
     * @param handle is the object
     * @return the signal
     */
    public static Object getSignal(Object handle) {
        if (handle instanceof MSendAction) {
            return ((MSendAction) handle).getSignal();
        }
        if (handle instanceof MSignalEvent) {
            return ((MSignalEvent) handle).getSignal();
        }
        if (handle instanceof MReception) {
            return ((MReception) handle).getSignal();
        }
        throw new IllegalArgumentException("Unrecognized object " +
					   getClassNull(handle));
    }

    /**
     * Get the resident element
     *
     * @param handle is the element residence
     * @return resident element
     */
    public static Object getResident(Object handle) {
        if (handle instanceof MElementResidence) {
            return ((MElementResidence) handle).getResident();
        }
        throw new IllegalArgumentException("Unrecognized object " +
					   getClassNull(handle));
    }

    public static Collection getResidentElements(Object handle) {
        if (handle instanceof MComponent) {
            return ((MComponent) handle).getResidentElements();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns a collection with all residents belonging to the given
     * node.
     *
     * @param handle is the node
     * @return Collection
     */
    public static Collection getResidents(Object handle) {
        if (isANode(handle)) {
            return ((MNode) handle).getResidents();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Gets the source for some given transitions.
     *
     * @param handle is the transition
     * @return Object
     */
    public static Object getSource(Object handle) {
        if (isATransition(handle)) {
            return ((MTransition) handle).getSource();
        }
        throw new IllegalArgumentException("Unrecognized object " +
					   getClassNull(handle));
    }

    public static Collection getSources(Object handle) {
        if (handle instanceof MFlow) {
            return ((MFlow) handle).getSources();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the sourceflows of a model element
     *
     * @param handle is the model element
     * @return a collection of sourceflows
     */
    public static Collection getSourceFlows(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getSourceFlows();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * The list of Specializations from a GeneralizableElement.
     *
     * @param handle GeneralizableElement to retrieve from.
     * @return Collection of Specializations.
     */
    public static Collection getSpecializations(Object handle) {
        if (handle instanceof MGeneralizableElement) {
            MGeneralizableElement ge = (MGeneralizableElement) handle;
            return ge.getSpecializations();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the state machine belonging to some given state or transition
     *
     * @param handle is the state or transition
     * @return Object
     */
    public static Object getStateMachine(Object handle) {
        if (handle instanceof MState) {
            return ((MState) handle).getStateMachine();
        }
        if (handle instanceof MTransition) {
            return ((MTransition) handle).getStateMachine();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the state belonging to some given transition.
     *
     * @param handle is the transition
     * @return Object
     */
    public static Object getState(Object handle) {
        if (handle instanceof MTransition) {
            return ((MTransition) handle).getState();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the stereotype belonging to some given model element
     *
     * @param handle is a model element
     * @return Object
     * @deprecated 0.15 in favor of getStereotypes
     */
    public static Object getStereoType(Object handle) {
        if (isAModelElement(handle)) {
            return ((MModelElement) handle).getStereotype();
        }
        throw new IllegalArgumentException("Unrecognized object " +
					   getClassNull(handle));
    }

    /**
     * Returns the stereotypes belonging to some given model element
     *
     * @param handle is the model element
     * @return stereotype collection
     */
    public static Collection getStereotypes(Object handle) {
        if (isAModelElement(handle)) {
            // This returns a collection as we have an eye on the future
            // and multiple stereotypes in UML1.5
            ArrayList list = new ArrayList(1);
            list.add(((MModelElement) handle).getStereotype());
            return list;
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the stimuli belonging to some given link
     *
     * @param handle is the link
     * @return Object
     */
    public static Collection getStimuli(Object handle) {
        if (isALink(handle)) {
            return ((MLink) handle).getStimuli();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    public static Collection getStimuli2(Object handle) {
        if (handle instanceof MInstance) {
            return ((MInstance) handle).getStimuli2();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    public static Collection getStimuli3(Object handle) {
        if (handle instanceof MInstance) {
            return ((MInstance) handle).getStimuli3();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns a collection with all subvertices belonging to the given
     * composite state.
     *
     * @param handle is the composite state
     * @return Collection
     */
    public static Collection getSubvertices(Object handle) {
        if (isACompositeState(handle)) {
            return ((MCompositeState) handle).getSubvertices();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the submachie of a submachine state
     *
     * @param handle is the submachine state
     * @return submachine
     */
    public static Object getSubmachine(Object handle) {
        if (handle instanceof MSubmachineState) {
            return ((MSubmachineState) handle).getStateMachine();
        }
        throw new IllegalArgumentException("Unrecognized object " +
					   getClassNull(handle));
    }

    /**
     * Returns the submachine of a submachine state
     *
     * @param handle is the submachine state
     * @return submachine
     */
    public static Collection getSubmachineStates(Object handle) {
        if (handle instanceof MStateMachine) {
            return ((MStateMachine) handle).getSubmachineStates();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * The list of SupplierDependencies from a ModelElement.
     *
     * @param handle model element.
     * @return Iterator with the supplier dependencies.
     */
    public static Collection getSupplierDependencies(Object handle) {
        if (handle instanceof MModelElement) {
            MModelElement me = (MModelElement) handle;
            return me.getSupplierDependencies();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " +
					   getClassNull(handle));
    }

    /**
     * The top of a state machine
     *
     * @param handle the state machine
     * @return the top
     */
    public static Object getTop(Object handle) {
        if (handle instanceof MStateMachine) {
            return ((MStateMachine) handle).getTop();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the transition of a guard or action
     *
     * @param handle the guard or action
     * @return the transition
     */
    public static Object getTransition(Object handle) {
        if (handle instanceof MGuard) {
            return ((MGuard) handle).getTransition();
        }
        if (handle instanceof MAction) {
            return ((MAction) handle).getTransition();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Get the trigger of a transition
     *
     * @param handle the transition
     * @return the trigger
     */
    public static Object getTrigger(Object handle) {
        if (handle instanceof MTransition) {
            return ((MTransition) handle).getTrigger();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * The type of an attribute
     *
     * @param handle the attribute
     * @return the type
     */
    public static Object getType(Object handle) {
        if (handle instanceof MStructuralFeature) {
            return ((MAttribute) handle).getType();
        }
        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd) handle).getType();
        }
        if (handle instanceof MParameter) {
            return ((MParameter) handle).getType();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the target of some transition
     *
     * @param handle is the transition
     * @return Object
     */
    public static Object getTarget(Object handle) {
        if (isATransition(handle)) {
            return ((MTransition) handle).getTarget();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the target scope of some model element
     *
     * @param handle is the model element
     * @return Object
     */
    public static Object getTargetScope(Object handle) {
        if (handle instanceof MStructuralFeature) {
            return ((MStructuralFeature) handle).getTargetScope();
        }
        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd) handle).getTargetScope();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the targetflows of a model element
     *
     * @param handle is the model element
     * @return a collection of targetflows
     */
    public static Collection getTargetFlows(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getTargetFlows();
        }
        throw new IllegalArgumentException("Unrecognized object " +
					   getClassNull(handle));
    }

    /**
     * Returns the upper bound of the multiplicity of the given handle (an
     * associationend).
     *
     * @param handle is the model element
     * @return int
     */
    public static int getUpper(Object handle) {
        if (isAAssociationEnd(handle)) {
            int upper = 0;
            MAssociationEnd end = (MAssociationEnd) handle;
            if (end.getMultiplicity() != null)
                upper = end.getMultiplicity().getUpper();
            return upper;
        }
	if (isAMultiplicity(handle)) {
	    MMultiplicity up = (MMultiplicity) handle;
	    return up.getUpper();
	}
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the use case of an extension point
     *
     * @param handle is the extension point
     * @return a use case
     */
    public static Object getUseCase(Object handle) {
        if (handle instanceof MExtensionPoint) {
            return ((MExtensionPoint) handle).getUseCase();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the upper bound of the multiplicity of the given handle (an
     * associationend).
     *
     * @param handle is the model element
     * @return int
     */
    public static int getLower(Object handle) {
        if (isAAssociationEnd(handle)) {
            int lower = 0;
            MAssociationEnd end = (MAssociationEnd) handle;
            if (end.getMultiplicity() != null)
                lower = end.getMultiplicity().getLower();
            return lower;
        }
	if (isAMultiplicity(handle)) {
	    MMultiplicity low = (MMultiplicity) handle;
	    return low.getLower();
	}
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the transitions belonging to the given handle. The handle can be
     * a statemachine or a composite state. If it's a statemachine the
     * transitions will be given back belonging to that statemachine. If it's a
     * compositestate the internal transitions of that compositestate will be
     * given back.
     *
     * @param handle is the model element
     * @return Collection
     */
    public static Collection getTransitions(Object handle) {
        if (isAStateMachine(handle)) {
            return ((MStateMachine) handle).getTransitions();
        } else if (isACompositeState(handle)) {
            return ((MCompositeState) handle).getInternalTransitions();
        }
        throw new IllegalArgumentException("Unrecognized object " +
					   getClassNull(handle));
    }

    /**
     * This method returns all attributes of a given Classifier.
     *
     * @param classifier the classifier you want to have the attributes for
     * @return a collection of the attributes
     */
    public static Collection getStructuralFeatures(Object classifier) {
        Collection result = new ArrayList();
        if (ModelFacade.isAClassifier(classifier)) {
            MClassifier mclassifier = (MClassifier) classifier;

            Iterator features = mclassifier.getFeatures().iterator();
            while (features.hasNext()) {
                MFeature feature = (MFeature) features.next();
                if (ModelFacade.isAStructuralFeature(feature))
                    result.add(feature);
            }
            return result;
        }
        throw new IllegalArgumentException("Unrecognized object : " + 
            getClassNull(classifier));
    }

    /**
     * This method returns all operations of a given Classifier
     *
     * @param mclassifier the classifier you want to have the operations for
     * @return a collection of the operations
     */
    protected static Collection getOperations(MClassifier mclassifier) {
        Collection result = new ArrayList();
        Iterator features = mclassifier.getFeatures().iterator();
        while (features.hasNext()) {
            MFeature feature = (MFeature) features.next();
            if (ModelFacade.isAOperation(feature))
                result.add(feature);
        }
        return result;
    }

    /**
     * Returns all Interfaces of which this class is a realization.
     * @param cls  the class you want to have the interfaces for
     * @return a collection of the Interfaces
     */
    public static Collection getSpecifications(Object cls) {
        Collection result = new Vector();
        if (cls instanceof MAssociationEnd) {
            return ((MAssociationEnd) cls).getSpecifications();
        }
        if (cls instanceof MClassifier) {
            Collection deps = ((MClassifier) cls).getClientDependencies();
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
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(cls));
    }

    /**
     * Returns the suppliers of a dependency.
     *
     * @param handle is the dependency
     * @return a collection of the suppliers
     */
    public static Collection getSuppliers(Object handle) {
        if (handle instanceof MDependency) {
            return ((MDependency) handle).getSuppliers();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the action belonging to some message
     *
     * @param handle is the message
     * @return the action
     */
    public static Object getAction(Object handle) {
        if (handle instanceof MMessage) {
            return ((MMessage) handle).getAction();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the activator belonging to some message
     *
     * @param handle is the message
     * @return the activator
     */
    public static Object getActivator(Object handle) {
        if (handle instanceof MMessage) {
            return ((MMessage) handle).getActivator();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns the actual arguments for a given action.
     *
     * @param handle is the action
     * @return the actual arguments
     */
    public static Collection getActualArguments(Object handle) {
        if (handle instanceof MAction) {
            return ((MAction) handle).getActualArguments();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns an addition for a given inlcude.
     *
     * @param handle is the include
     * @return the addition
     */
    public static Object getAddition(Object handle) {
        if (handle instanceof MInclude) {
            return ((MInclude) handle).getAddition();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    public static Object getAggregation(Object handle) {
        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd) handle).getAggregation();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Returns all associated classes for some given classifier. Returns an
     * empty collection if the given argument o is not a classifier. The given
     * parameter is included in the returned collection if it has a self-
     * referencing association.
     *
     * @param o is the classifier
     * @return Collection
     */
    public static Collection getAssociatedClasses(Object o) {
        Collection col = new ArrayList();
        if (o instanceof MClassifier) {
            MClassifier classifier = (MClassifier) o;
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
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(o));
    }

    ////////////////////////////////////////////////////////////////
    // Common getters

    /**
     * The name of a model element or some diagram part.
     *
     * @param handle that points out the object.
     * @return the name
     */
    public static String getName(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getName();
        }
        if (handle instanceof Diagram) {
            return ((Diagram) handle).getName();
        }
        if (handle instanceof MOrderingKind) {
            return ((MOrderingKind) handle).getName();
        }
        if (handle instanceof MAggregationKind) {
            return ((MAggregationKind) handle).getName();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Return the owner of a feature.
     *
     * @param f is the feature
     * @return classifier
     */
    public static Object getOwner(Object f) {
        if (f instanceof MFeature) {
            return ((MFeature) f).getOwner();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(f));
    }

    /**
     * Return the tag of a tagged value
     *
     * @param handle The tagged value belongs to this.
     * @return The found tag, null if not found
     */
    public static Object getTag(Object handle) {
        if (handle instanceof MTaggedValue) {
            return ((MTaggedValue) handle).getTag();
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Return the tagged values iterator of a model element.
     *
     * @param modelElement The tagged values belong to this.
     * @return The tagged values iterator
     */
    public static Iterator getTaggedValues(Object modelElement) {
        if (modelElement != null && modelElement instanceof MModelElement) {
            return ((MModelElement) modelElement).getTaggedValues().iterator();
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(modelElement));
    }

    public static Collection getTaggedValuesCollection(Object modelElement) {
        if (modelElement != null && modelElement instanceof MModelElement) {
            return ((MModelElement) modelElement).getTaggedValues();
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(modelElement));
    }

    /**
     * Return the tagged value with a specific tag.
     *
     * @param modelElement The tagged value belongs to this.
     * @param name The tag.
     * @return The found tag, null if not found
     */
    public static Object getTaggedValue(Object modelElement, String name) {
        if (modelElement != null && modelElement instanceof MModelElement) {
            MModelElement me = ((MModelElement) modelElement);
            Iterator i = me.getTaggedValues().iterator();
            while (i.hasNext()) {
                MTaggedValue tv = (MTaggedValue) i.next();
                if (tv.getTag().equals(name)) {
                    return tv;
                }
            }
	    return null;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(modelElement));
    }

    /**
     * Return the key (tag) of some tagged value.
     *
     * @param tv The tagged value.
     * @return The found value, null if not found
     */
    public static String getTagOfTag(Object tv) {
        if (tv instanceof MTaggedValue) {
	    if (tv != null) {
            return ((MTaggedValue) tv).getTag();
	    }
	    return null;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(tv));
    }

    public static Object getValue(Object handle) {
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
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Return the value of some tagged value.
     *
     * @param tv The tagged value.
     * @return The found value, null if not found
     */
    public static String getValueOfTag(Object tv) {
        if (tv != null && tv instanceof MTaggedValue) {
            return ((MTaggedValue) tv).getValue();
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(tv));
    }

    /**
     * Return the UUID of this element
     *
     * @param base base element (MBase type)
     * @return UUID
     */
    public static String getUUID(Object base) {
        if (isABase(base)) {
            return ((MBase) base).getUUID();
        }
        //
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(base));
    }

    /**
     *  Return the visibility of this element
     *  @param element an nsuml model element
     *  @return visibility
     */
    public static Object getVisibility(Object element) {
        if (element instanceof MModelElement) {
            return ((MModelElement) element).getVisibility();
        }
        //
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(element));
    }

    ////////////////////////////////////////////////////////////////
    // Other querying methods

    /**
     * Returns a named object in the given object by calling it's lookup method.
     *
     * @param o the object that we search through
     * @param name of the model element
     * @return found object, null otherwise
     */
    public static Object lookupIn(Object o, String name) {
        if (o instanceof MModel)
            return ((MModel) o).lookup(name);
        if (o instanceof MNamespace)
            return ((MNamespace) o).lookup(name);
        if (o instanceof MClassifier)
            return ((MClassifier) o).lookup(name);
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(o));
    }

    ////////////////////////////////////////////////////////////////
    // Model modifying methods

    /**
     * Adds a feature to some classifier.
     * @param cls classifier
     * @param f feature
     */
    public static void addFeature(Object cls, Object f) {
        if (cls instanceof MClassifier && f instanceof MFeature) {
            ((MClassifier) cls).addFeature((MFeature) f);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(cls));
    }
    
    /**
     * Adds an instance to a classifier role.
     *
     * @param classifierRole is the classifier role
     * @param instance is the instance to add
     */
    public static void addInstance(Object classifierRole, Object instance) {
    	if (classifierRole instanceof MClassifierRole
	        && instance instanceof MInstance) {
	    MClassifierRole clr = (MClassifierRole) classifierRole;
	    clr.addInstance((MInstance) instance);
	    return;
    	}
	throw new IllegalArgumentException("Unrecognized object "
					   + getClassNull(classifierRole)
					   + " or "
					   + getClassNull(instance));
    }


    /**
     * Adds a feature to some classifier.
     *
     * @param cls classifier
     * @param index position
     * @param f feature
     */
    public static void addFeature(Object cls, int index, Object f) {
        if (cls instanceof MClassifier && f instanceof MFeature) {
            ((MClassifier) cls).addFeature(index, (MFeature) f);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object " + cls
					   + " or " + f);
    }
    
    public static void addLink(Object association, Object link) {
	if (association instanceof MAssociation && link instanceof MLink) {
	    ((MAssociation) association).addLink((MLink) link);
	    return;
	}
	throw new IllegalArgumentException("Unrecognized object "
					   + association + " or " + link);
    }

    public static void addMessage3(Object handle, Object mess) {
        if (handle instanceof MMessage && mess instanceof MMessage) {
            ((MMessage) handle).addMessage3((MMessage) mess);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Adds a method to some operation and copies the op's attributes
     * to the method.
     *
     * @param o is the operation
     * @param m is the method
     */
    public static void addMethod(Object o, Object m) {
        if (o instanceof MOperation
            && m instanceof MMethod) {
            ((MMethod) m).setVisibility(((MOperation) o).getVisibility());
            ((MMethod) m).setOwnerScope(((MOperation) o).getOwnerScope());
            ((MOperation) o).addMethod((MMethod) m);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(o) + " or " + getClassNull(m));
    }

    /**
     * Adds a model element to some namespace.
     * @param ns namespace
     * @param me model element
     */
    public static void addOwnedElement(Object ns, Object me) {
        if (ns instanceof MNamespace && me instanceof MModelElement) {
            ((MNamespace) ns).addOwnedElement((MModelElement) me);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object "
					   + getClassNull(ns)
					   + " or " + getClassNull(me));
    }

    public static void addParameter(Object target, Object parameter) {
        if (parameter instanceof MParameter) {
            if (target instanceof MObjectFlowState) {
                ((MObjectFlowState) target).addParameter(
		         (MParameter) parameter);
                return;
            }
            if (target instanceof MEvent) {
                ((MEvent) target).addParameter((MParameter) parameter);
                return;
            }
            if (target instanceof MBehavioralFeature) {
                ((MBehavioralFeature) target).addParameter(
                    (MParameter) parameter);
                return;
            }
            if (target instanceof MClassifier) {
                ((MClassifier) target).addParameter((MParameter) parameter);
                return;
            }
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(parameter));
    }

    public static void addParameter(
        Object target,
        int index,
        Object parameter) {
        if (parameter instanceof MParameter) {
            if (target instanceof MEvent) {
                ((MEvent) target).addParameter(index, (MParameter) parameter);
                return;
            }
            if (target instanceof MBehavioralFeature) {
                ((MBehavioralFeature) target).addParameter(
                        index,
                        (MParameter) parameter);
                return;
            }
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(parameter));
    }

    /**
     * Adds a predecessor to a message.
     *
     * @param target the message
     * @param predecessor is the predecessor
     */
    public static void addPredecessor(Object target, Object predecessor) {
        if (target != null
            && target instanceof MMessage
            && predecessor != null
            && predecessor instanceof MMessage) {
            ((MMessage) target).addPredecessor((MMessage) predecessor);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(predecessor));
    }

    public static void addRaisedSignal(Object target, Object sig) {
        if (sig instanceof MSignal) {
            if (target instanceof MMessage) {
                ((MBehavioralFeature) target).addRaisedSignal((MSignal) sig);  
                return;
            }
            if (target instanceof MOperation) {
                ((MOperation) target).addRaisedSignal((MSignal) sig);
                return;
            }
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(sig));
    }

    /**
     * Adds a stimulus to a action or link
     *
     * @param target the action or link
     * @param stimulus is the stimulus
     */
    public static void addStimulus(Object target, Object stimulus) {
        if (target != null
            && stimulus != null
            && stimulus instanceof MStimulus) {
            if (target instanceof MAction) {
                ((MAction) target).addStimulus((MStimulus) stimulus);
                return;
            }
            if (target instanceof MLink) {
                ((MLink) target).addStimulus((MStimulus) stimulus);
                return;
            }
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(stimulus));
    }

    public static void addSubvertex(Object handle, Object subvertex) {
        if (handle instanceof MCompositeState
            && subvertex instanceof MStateVertex) {
            ((MCompositeState) handle).addSubvertex((MStateVertex) subvertex);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle) + " or " +
					   getClassNull(subvertex));
    }

    /**
     * Adds a supplier classifier to some abstraction.
     * @param a abstraction
     * @param cls supplier classifier
     */
    public static void addSupplier(Object a, Object cls) {
        if (a != null
            && cls != null
            && a instanceof MAbstraction
            && cls instanceof MClassifier) {
            ((MAbstraction) a).addSupplier((MClassifier) cls);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(a) + " or " + getClassNull(cls));
    }

    /**
     * Adds a supplier dependency to some modelelement
     * @param supplier the supplier
     * @param dependency the dependency
     */
    public static void addSupplierDependency(
        Object supplier,
        Object dependency) {
        if (isAModelElement(supplier) && isADependency(dependency)) {
            MModelElement me = (MModelElement) supplier;
            me.addSupplierDependency((MDependency) dependency);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(supplier) + " or " + getClassNull(dependency));
    }

    public static void addActualArgument(Object handle, Object argument) {
        if (handle instanceof MAction && argument instanceof MArgument) {
            ((MAction) handle).addActualArgument((MArgument) argument);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(handle) + " or " + getClassNull(argument));
    }

    /**
     * This method adds a classifier to a classifier role.
     *
     * @param o is the classifier role
     * @param c is the classifier
     */
    public static void addBase(Object o, Object c) {
        if (o != null
            && c != null
            && o instanceof MClassifierRole
            && c instanceof MClassifier) {
            ((MClassifierRole) o).addBase((MClassifier) c);
	    return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(o) + " or " + getClassNull(c));
    }

    public static void addClassifier(Object handle, Object classifier) {
        if (handle instanceof MInstance && classifier instanceof MClassifier) {
            ((MInstance) handle).addClassifier((MClassifier) classifier);
	    return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(handle) + " or " + getClassNull(classifier));
    }

    /**
     * Adds a client classifier to some abstraction.
     * @param a abstraction
     * @param cls client classifier
     */
    public static void addClient(Object a, Object cls) {
        if (a != null
            && cls != null
            && a instanceof MAbstraction
            && cls instanceof MClassifier) {
            ((MAbstraction) a).addClient((MClassifier) cls);
	    return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(a) + " or " + getClassNull(cls));
    }

    /**
     * Adds a client dependency to some modelelement
     * @param handle the modelelement
     * @param dependency the dependency
     */
    public static void addClientDependency(Object handle, Object dependency) {
        if (handle != null
            && dependency != null
            && isAModelElement(handle)
            && isADependency(dependency)) {
            MModelElement me = (MModelElement) handle;
            me.addClientDependency((MDependency) dependency);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(handle) + " or " + getClassNull(dependency));
    }

    public static void addTaggedValue(Object target, Object taggedValue) {
        if (isAModelElement(target) && isATaggedValue(taggedValue)) {
            ((MModelElement) target).addTaggedValue((MTaggedValue) taggedValue);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(taggedValue));
    }

    public static void removeActualArgument(Object handle, Object argument) {
        if (handle instanceof MAction && argument instanceof MArgument) {
            ((MAction) handle).removeActualArgument((MArgument) argument);
	    return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(handle) + " or " + getClassNull(argument));
    }

    /**
     * This method removes a classifier from a classifier role.
     *
     * @param o is the classifier role
     * @param c is the classifier
     */
    public static void removeBase(Object o, Object c) {
        if (o != null
            && c != null
            && o instanceof MClassifierRole
            && c instanceof MClassifier) {
            ((MClassifierRole) o).removeBase((MClassifier) c);
	    return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(o) + " or " + getClassNull(c));
    }

    /**
     * This method removes a dependency from a model element.
     *
     * @param o is the model element
     * @param dep is the dependency
     */
    public static void removeClientDependency(Object o, Object dep) {
        if (o != null
            && dep != null
            && o instanceof MModelElement
            && dep instanceof MDependency) {
            ((MModelElement) o).removeClientDependency((MDependency) dep);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(o) + " or " + getClassNull(dep));
    }

    public static void removeConstraint(Object handle, Object cons) {
        if (handle instanceof MModelElement && cons instanceof MConstraint) {
            ((MModelElement) handle).removeConstraint((MConstraint) cons);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(handle) + " or " + getClassNull(cons));
    }

    public static void removeContext(Object handle, Object context) {
        if (handle instanceof MSignal
            && context instanceof MBehavioralFeature) {
            ((MSignal) handle).removeContext((MBehavioralFeature) context);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(handle) + " or " + getClassNull(context));
    }

    /**
     * This method classifier from an instance
     *
     * @param handle is the instance
     * @param classifier is the classifier
     */
    public static void removeClassifier(Object handle, Object classifier) {
        if (handle instanceof MInstance && classifier instanceof MClassifier) {
            ((MInstance) handle).removeClassifier((MClassifier) classifier);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " +
	    getClassNull(classifier));
    }

    /**
     * This method removes a feature from a classifier.
     *
     * @param cls is the classifier
     * @param feature to remove
     */
    public static void removeFeature(Object cls, Object feature) {
        if (cls != null
            && feature != null
            && cls instanceof MClassifier
            && feature instanceof MFeature) {
            ((MClassifier) cls).removeFeature((MFeature) feature);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(cls) + " or " + getClassNull(feature));
    }

    /**
     * This method removes an extension point from a use case.
     *
     * @param uc is the use case
     * @param ep is the extension point
     */
    public static void removeExtensionPoint(Object uc, Object ep) {
        if (uc != null
            && ep != null
            && uc instanceof MUseCase
            && ep instanceof MExtensionPoint) {
            ((MUseCase) uc).removeExtensionPoint((MExtensionPoint) ep);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(uc) + " or " + getClassNull(ep));
    }

    public static void removeMessage3(Object handle, Object mess) {
        if (handle instanceof MMessage && mess instanceof MMessage) {
            ((MMessage) handle).removeMessage3((MMessage) mess);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Removes a owned model element from a namespace.
     *
     * @param handle is the name space
     * @param value is the model element
     */
    public static void removeOwnedElement(Object handle, Object value) {
        if (handle != null
            && value != null
            && handle instanceof MNamespace
            && value instanceof MModelElement) {
            ((MNamespace) handle).removeOwnedElement((MModelElement) value);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(value));
    }

    /**
     * This method removes a parameter from an operation.
     *
     * @param o is the operation
     * @param p is the parameter
     */
    public static void removeParameter(Object o, Object p) {
        if (o != null
            && p != null
            && o instanceof MOperation
            && p instanceof MParameter) {
            ((MOperation) o).removeParameter((MParameter) p);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(o) + " or " + getClassNull(p));
    }

    public static void removePredecessor(Object handle, Object message) {
        if (handle instanceof MMessage && message instanceof MMessage) {
            ((MMessage) handle).removePredecessor((MMessage) message);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(handle) + " or " + getClassNull(message));
    }

    public static void removeReception(Object handle, Object reception) {
        if (handle instanceof MSignal && reception instanceof MReception) {
            ((MSignal) handle).removeReception((MReception) reception);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(reception));
    }

    public static void removeSubvertex(Object handle, Object subvertex) {
        if (handle instanceof MCompositeState
            && subvertex instanceof MStateVertex) {
            ((MCompositeState) handle).removeSubvertex(
		    (MStateVertex) subvertex);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object " +
                       getClassNull(handle) + " or " +
                       getClassNull(subvertex));
    }
    
    /**
     * Set the base of some model element
     *
     * @param target is the model element
     * @param base is the base
     */
    public static void setBase(Object target, Object base) {
        if (target instanceof MAssociationRole
            && base instanceof MAssociation) {
            ((MAssociationRole) target).setBase((MAssociation) base);
            return;
        }
        if (target instanceof MAssociationEndRole
            && base instanceof MAssociationEnd) {
            ((MAssociationEndRole) target).setBase((MAssociationEnd) base);
            return;
        }
        if (target instanceof MExtend && base instanceof MUseCase) {
            ((MExtend) target).setBase((MUseCase) base);
            return;
        }
        if (target instanceof MInclude && base instanceof MUseCase) {
            ((MInclude) target).setBase((MUseCase) base);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(base));
    }

    /**
     * Set the baseclass of some stereotype
     * @param handle the stereotype
     * @param baseClass the baseclass
     */
    public static void setBaseClass(Object handle, Object baseClass) {
        if (isAStereotype(handle) && baseClass instanceof String) {
            ((MStereotype) handle).setBaseClass((String) baseClass);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Sets a body of some method or expression.
     *
     * @param m is the method, expression
     * @param expr body
     */
    public static void setBody(Object m, Object expr) {
        if (m instanceof MMethod
            && (expr == null || expr instanceof MProcedureExpression)) {
            ((MMethod) m).setBody((MProcedureExpression) expr);
            return;
        }

        if (m instanceof MConstraint
            && (expr == null || expr instanceof MBooleanExpression)) {
            ((MConstraint) m).setBody((MBooleanExpression) expr);
            return;
        }

        if (m instanceof MExpression) {
            MExpression expression = (MExpression) m;
            MExpressionEditor expressionEditor =
		(MExpressionEditor) UmlFactory.getFactory().getDataTypes()
		.createExpressionEditor(m);
            expressionEditor.setBody((String) expr);
            expression = expressionEditor.toExpression();
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(m) + " or " + getClassNull(expr));
    }

    /**
     * Sets the language of an expression.
     *
     * @param m is the expression
     * @param expr is the lang
     *
     * TODO: Rename the expr parameter to something a little less error-prone.
     */
    public static void setLanguage(Object m, String expr) {
        if (m instanceof MExpression) {
            MExpression expression = (MExpression) m;
            MExpressionEditor expressionEditor = (MExpressionEditor)
                UmlFactory.getFactory().getDataTypes().
                    createExpressionEditor(m);
            expressionEditor.setLanguage(expr);
            m = expressionEditor.toExpression();
           
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(m) + " or " + getClassNull(expr));
    }
    
    public static String getLanguage(Object expr) {
        if (expr instanceof MExpression) {
            return ((MExpression) expr).getLanguage();
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(expr));
    }
    
    /**
     * Sets a default value of some parameter.
     *
     * @param p is the parameter
     * @param expr is the expression
     */
    public static void setDefaultValue(Object p, Object expr) {
        if (p instanceof MParameter && expr instanceof MExpression) {
            ((MParameter) p).setDefaultValue((MExpression) expr);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(p) + " or " + getClassNull(expr));
    }

    /**
     * Sets the guard of a transition.
     * @param handle to the transition
     * @param guard to be set
     */
    public static void setGuard(Object handle, Object guard) {
        if (handle instanceof MTransition
            && (guard == null || guard instanceof MGuard)) {
            ((MTransition) handle).setGuard((MGuard) guard);
            return;
        }
        throw new IllegalArgumentException(
            "Object "
	    + getClassNull(guard)
	    + " cannot be owned by "
	    + getClassNull(handle));
    }

    public static void setTransition(Object handle, Object trans) {
        if (trans instanceof MTransition) {
            if (handle instanceof MGuard) {
                ((MGuard) handle).setTransition((MTransition) trans);
                return;
            }
            if (handle instanceof MAction) {
                ((MAction) handle).setTransition((MTransition) trans);
                return;
            }
        }
        throw new IllegalArgumentException(
            "Object " + trans + " cannot be owned by " + handle);
    }

    /**
     * Sets the trigger event of a transition.
     *
     * @param handle is the transition
     * @param event is the trigger event
     */
    public static void setTrigger(Object handle, Object event) {
        if (handle instanceof MTransition
            && (event == null || event instanceof MEvent)) {
            ((MTransition) handle).setTrigger((MEvent) event);
            return;
        }
        throw new IllegalArgumentException(
            "Object " + event + " cannot be owned by " + handle);
    }

    public static void setIcon(Object handle, Object icon) {
        if (handle instanceof MStereotype
            && (icon == null || icon instanceof String)) {
            ((MStereotype) handle).setIcon((String) icon);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(handle) + " or " + getClassNull(icon));
    }

    public static void setImplementationLocation(
        Object handle,
        Object component) {
        if (handle instanceof MElementResidence
            && (component == null || component instanceof MComponent)) {
            ((MElementResidence) handle).setImplementationLocation(
                (MComponent) component);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(handle) + " or " + getClassNull(component));
    }

    public static void setIncludes(Object target, Collection includes) {
        if (target instanceof MUseCase) {
            ((MUseCase) target).setIncludes(includes);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(includes));
    }

    /**
     * Sets an initial value.
     *
     * @param at attribute that we set the initial value of
     * @param expr that is the value to set
     */
    public static void setInitialValue(Object at, Object expr) {
        if (at instanceof MAttribute
                && (expr == null || expr instanceof MExpression)) {
	    ((MAttribute) at).setInitialValue((MExpression) expr);
	    return;
	}
	throw new IllegalArgumentException("Unrecognized object " + at
					   + " or " + expr);
    }

    public static void setInstance(Object handle, Object inst) {
        if (inst == null || inst instanceof MInstance) {
            if (handle instanceof MLinkEnd) {
                ((MLinkEnd) handle).setInstance((MInstance) inst);
                return;
            }
            if (handle instanceof MAttributeLink) {
                ((MAttributeLink) handle).setInstance((MInstance) inst);
                return;
            }
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(inst));
    }

    public static void setInternalTransitions(
        Object target,
        Collection intTrans) {
        if (target instanceof MState) {
            ((MState) target).setInternalTransitions(intTrans);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(intTrans));
    }

    public static void setMessages3(Object handle, Collection messages) {
        if (handle instanceof MMessage) {
            ((MMessage) handle).setMessages3(messages);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object " +
					   getClassNull(handle));
    }

    /**
     * Sets a location of some extension point.
     *
     * @param ep is the extension point
     * @param loc is the location
     */
    public static void setLocation(Object ep, String loc) {
        if (ep != null && ep instanceof MExtensionPoint) {
            ((MExtensionPoint) ep).setLocation(loc);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(ep));
    }

    /**
     * <p>Sets the container that owns the handle. This must be set
     * correctly so every modelelement except the root model does have
     * an owner. Otherwise the saving/loading will fail.</p>
     *
     * <p><b>Warning: when changing the implementation of this method
     * be warned that the sequence of the if then else tree DOES
     * matter.</b> Most notabely, do not move the setNamespace method
     * any level up in the tree.</p>
     *
     * <p><b>Warning: the implementation does not support setting the
     * owner of actions.</b> Use setState1 etc. on action for that
     * goal</p>
     *
     * @param handle The modelelement that must be added to the container
     * @param container The owning modelelement
     * @exception IllegalArgumentException when the handle or
     * container is null or if the handle cannot be added to the
     * container.
     */
    public static void setModelElementContainer(
        Object handle,
        Object container)
        throws IllegalArgumentException {
        if (handle == null || container == null) {
            throw new IllegalArgumentException(
                "Neither the modelelement to be "
                    + "added as the container "
                    + "nor the one to which "
                    + "the modelelement is added "
                    + "may be null");
        }
        if (handle instanceof MPartition
            && container instanceof MActivityGraph) {
            ((MPartition) handle).setActivityGraph((MActivityGraph) container);
        } else if (
            handle instanceof MConstraint
                && container instanceof MStereotype) {
            MConstraint c = (MConstraint) handle;
            c.setConstrainedElement2((MStereotype) container);
        } else if (
            handle instanceof MInteraction
                && container instanceof MCollaboration) {
            ((MInteraction) handle).setContext((MCollaboration) container);
        } else if (
            handle instanceof MElementResidence
                && container instanceof MComponent) {
            MElementResidence er = (MElementResidence) handle;
            er.setImplementationLocation((MComponent) container);
        } else if (
            handle instanceof MAttributeLink
                && container instanceof MInstance) {
            ((MAttributeLink) handle).setInstance((MInstance) container);
        } else if (
            handle instanceof MMessage && container instanceof MInteraction) {
            ((MMessage) handle).setInteraction((MInteraction) container);
        } else if (handle instanceof MLinkEnd && container instanceof MLink) {
            ((MLinkEnd) handle).setLink((MLink) container);
        } else if (
            handle instanceof MAttributeLink
                && container instanceof MLinkEnd) {
            ((MAttributeLink) handle).setLinkEnd((MLinkEnd) container);
        } else if (
            handle instanceof MTaggedValue
                && container instanceof MStereotype) {
            ((MTaggedValue) handle).setStereotype((MStereotype) container);
        } else if (
            handle instanceof MTaggedValue
                && container instanceof MModelElement) {
            ((MTaggedValue) handle).setModelElement((MModelElement) container);
        } else if (
            handle instanceof MStateVertex
                && container instanceof MCompositeState) {
            ((MStateVertex) handle).setContainer((MCompositeState) container);
        } else if (
            handle instanceof MElementImport
                && container instanceof MPackage) {
            ((MElementImport) handle).setPackage((MPackage) container);
        } else if (
            handle instanceof MTransition && container instanceof MState) {
            ((MTransition) handle).setState((MState) container);
        } else if (
            handle instanceof MState && container instanceof MStateMachine) {
            ((MState) handle).setStateMachine((MStateMachine) container);
        } else if (
            handle instanceof MTransition
                && container instanceof MStateMachine) {
            ((MTransition) handle).setStateMachine((MStateMachine) container);
        } else if (
            handle instanceof MAction && container instanceof MTransition) {
            ((MAction) handle).setTransition((MTransition) container);
        } else if (
            handle instanceof MGuard && container instanceof MTransition) {
            ((MGuard) handle).setTransition((MTransition) container);
        } else if (
            handle instanceof MModelElement
                && container instanceof MNamespace) {
            ((MModelElement) handle).setNamespace((MNamespace) container);
        } else {
            throw new IllegalArgumentException(
                "Object "
                    + handle.toString()
                    + " cannot be owned by "
                    + container.toString());
        }
    }

    /**
     * Sets a multiplicity of some model element.
     * @param target model element
     * @param mult multiplicity as string OR multiplicity object
     */
    public static void setMultiplicity(Object target, Object mult) {
        if (mult instanceof String) {
            mult =
                ("1_N".equals(mult)) ? MMultiplicity.M1_N : MMultiplicity.M1_1;
        }

        if (target instanceof MAssociationRole) {
            ((MAssociationRole) target).setMultiplicity((MMultiplicity) mult);
            return;
        }
        if (target instanceof MClassifierRole) {
            ((MClassifierRole) target).setMultiplicity((MMultiplicity) mult);
            return;
        }
        if (target instanceof MStructuralFeature) {
            ((MStructuralFeature) target).setMultiplicity((MMultiplicity) mult);
            return;
        }
        if (target instanceof MAssociationEnd) {
            ((MAssociationEnd) target).setMultiplicity((MMultiplicity) mult);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(mult));
    }

    /**
     * Sets the classifiers of some instance.
     *
     * @param o is the instance
     * @param v is the classifier vector
     */
    public static void setClassifiers(Object o, Vector v) {
        if (o instanceof MInstance) {
            ((MInstance) o).setClassifiers(v);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(o));
    }

    /**
     * Sets a name of some modelelement.
     *
     * @param o is the model element
     * @param name to set
     */
    public static void setName(Object o, String name) {
        if (o instanceof MModelElement) {
            ((MModelElement) o).setName(name);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(o));
    }

    /**
     * Sets a namespace of some modelelement.
     *
     * @param handle is the model element
     * @param ns is the namespace
     */
    public static void setNamespace(Object handle, Object ns) {
        if (handle instanceof MModelElement
            && (ns == null || ns instanceof MNamespace)) {
            ((MModelElement) handle).setNamespace((MNamespace) ns);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(ns));
    }

    /**
     * Sets the navigability of some association end.
     *
     * @param o is the association end
     * @param flag is the navigability flag
     */
    public static void setNavigable(Object o, boolean flag) {
        if (o != null && o instanceof MAssociationEnd) {
            ((MAssociationEnd) o).setNavigable(flag);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(o));
    }

    public static void setValue(Object handle, Object value) {
        if (handle instanceof MArgument) {
            ((MArgument) handle).setValue((MExpression) value);
            return;
        }
        if (handle instanceof MAttributeLink) {
            ((MAttributeLink) handle).setValue((MInstance) value);
            return;
        }
        if (handle instanceof MExtension) {
            ((MExtension) handle).setValue(value);
            return;
        }
        if (handle instanceof MTaggedValue) {
            ((MTaggedValue) handle).setValue((String) value);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(handle));
    }

    /**
     * Set the visibility of some modelelement.
     *
     * @param handle element
     * @param visibility is the visibility
     */
    public static void setVisibility(Object handle, Object visibility) {
        if (visibility instanceof MVisibilityKind) {
            if (handle instanceof MModelElement) {
                ((MModelElement) handle).setVisibility(
                    (MVisibilityKind) visibility);
                return;
            }
            if (handle instanceof MElementResidence) {
                ((MElementResidence) handle).setVisibility(
                    (MVisibilityKind) visibility);
                    return;
            }
            if (handle instanceof MElementImport) {
                ((MElementImport) handle).setVisibility(
                    (MVisibilityKind) visibility);
                    return;
            }
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(handle) + " or " + getClassNull(visibility));
    }

    /**
     * Set the visibility of some modelelement.
     *
     * @param o is the model element
     * @param v is the visibility
     */
    public static void setVisibility(Object o, short v) {
        if (o != null && o instanceof MModelElement) {
            if (v == ACC_PRIVATE) {
                ((MModelElement) o).setVisibility(MVisibilityKind.PRIVATE);
            } else if (v == ACC_PROTECTED) {
                ((MModelElement) o).setVisibility(MVisibilityKind.PROTECTED);
            } else if (v == ACC_PUBLIC) {
                ((MModelElement) o).setVisibility(MVisibilityKind.PUBLIC);
            }
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(o));
    }

    public static void setNodeInstance(Object handle, Object nodeInstance) {
        if (handle instanceof MComponentInstance && 
            nodeInstance instanceof MNodeInstance) {
            ((MComponentInstance) handle).setNodeInstance(
                (MNodeInstance) nodeInstance);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(handle) + " or " + getClassNull(nodeInstance));
    }

    public static void setOwner(Object handle, Object owner) {
        if (handle instanceof MFeature
            && (owner == null || owner instanceof MClassifier)) {
            ((MFeature) handle).setOwner((MClassifier) owner);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(owner));
    }

    public static void setOperation(Object handle, Object operation) {
        if (handle instanceof MCallAction) {
            ((MCallAction) handle).setOperation((MOperation) operation);
            return;
        }
        if (handle instanceof MCallEvent) {
            ((MCallEvent) handle).setOperation((MOperation) operation);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(operation));
    }

    public static void setOrdering(Object handle, Object ok) {
        if (handle instanceof MAssociationEnd && ok instanceof MOrderingKind) {
            ((MAssociationEnd) handle).setOrdering((MOrderingKind) ok);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(ok));
    }

    /**
     * Set the owner scope of some feature.
     *
     * @param f is the feature
     * @param os is the owner scope
     */
    public static void setOwnerScope(Object f, short os) {
        if (f != null && f instanceof MFeature) {
            if (os == CLASSIFIER_SCOPE) {
                ((MFeature) f).setOwnerScope(MScopeKind.CLASSIFIER);
                return;
            } else if (os == INSTANCE_SCOPE) {
                ((MFeature) f).setOwnerScope(MScopeKind.INSTANCE);
                return;
            }
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(f));
    }

    public static void setOwnerScope(Object f, Object os) {
        if (f instanceof MFeature
            && (os == null || os instanceof MScopeKind)) {
            ((MFeature) f).setOwnerScope((MScopeKind) os);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(f) + " or " + 
	    getClassNull(os));
    }

    /**
     * Sets the extension points of some use cases.
     *
     * @param target the use case
     * @param parameters is a Collection of extensionPoints
     */
    public static void setParameters(Object target, Collection parameters) {
        if (target instanceof MObjectFlowState) {
            ((MObjectFlowState) target).setParameters(parameters);
            return;
        }
        if (target instanceof MClassifier) {
            ((MClassifier) target).setParameters(parameters);
            return;
        }
        if (target instanceof MEvent && parameters instanceof List) {
            ((MEvent) target).setParameters((List) parameters);
            return;
        }
        if (target instanceof MBehavioralFeature
            && parameters instanceof List) {
            ((MBehavioralFeature) target).setParameters((List) parameters);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(parameters));
    }

    /**
     * Sets the target of some action or transition.
     * @param handle the model element
     * @param element the target of the model elemnet
     */
    public static void setTarget(Object handle, Object element) {
        if (handle instanceof MAction
            && element instanceof MObjectSetExpression) {
            ((MAction) handle).setTarget((MObjectSetExpression) element);
            return;
        }
        if (handle instanceof MTransition
            && element instanceof MClassifierRole) {
            ((MTransition) handle).setTarget((MStateVertex) element);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(element));
    }

    /**
     * Set the target scope of some association end or structural feature
     * @param handle the model element
     * @param scopeKind the target scope
     */
    public static void setTargetScope(Object handle, Object scopeKind) {
        if (scopeKind instanceof MScopeKind) {
            if (handle instanceof MStructuralFeature) {
                ((MStructuralFeature) handle).setTargetScope(
                    (MScopeKind) scopeKind);
                return;
            }
            if (handle instanceof MAssociationEnd) {
                ((MAssociationEnd) handle)
		    .setTargetScope((MScopeKind) scopeKind);
                return;
            }
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(scopeKind));
    }

    /**
     * Set the target scope of some association end.
     *
     * @param ae is the association end
     * @param ts is the target scope
     */
    public static void setTargetScope(Object ae, short ts) {
        if (ae != null && ae instanceof MAssociationEnd) {
            if (ts == CLASSIFIER_SCOPE) {
                ((MAssociationEnd) ae).setTargetScope(MScopeKind.CLASSIFIER);
                return;
            } else if (ts == INSTANCE_SCOPE) {
                ((MAssociationEnd) ae).setTargetScope(MScopeKind.INSTANCE);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " +
            getClassNull(ae));
    }

    public static void setComponentInstance(Object o, Object c) {
        if (o instanceof MInstance && c instanceof MComponentInstance) {
            ((MInstance) o).setComponentInstance((MComponentInstance) c);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " + 
            getClassNull(o) + " or " + getClassNull(c));
    }
    
    /**
     * Sets the communicationLink between a link c and a stimulus o.
     * @param o the stimulus
     * @param c the link
     */
    public static void setCommunicationLink(Object o, Object c) {
        if (o instanceof MStimulus && c instanceof MLink) {
            ((MStimulus) o).setCommunicationLink((MLink) c);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " + 
            getClassNull(o) + " or " + getClassNull(c));
    }

    /**
     * Set the concurrency of some operation.
     *
     * @param o is the operation
     * @param c is the concurrency
     */
    public static void setConcurrency(Object o, short c) {
        if (o instanceof MOperation) {
            MOperation oper = (MOperation) o;
            if (c == GUARDED) {
                oper.setConcurrency(MCallConcurrencyKind.GUARDED);
                return;
            } else if (c == SEQUENTIAL) {
                oper.setConcurrency(MCallConcurrencyKind.SEQUENTIAL);
            }
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " + 
            getClassNull(o));
    }

    /**
     * Set the concurrency of some operation.
     *
     * @param operation is the operation
     * @param concurrencyKind is the concurrency
     */
    public static void setConcurrency(
        Object operation,
        Object concurrencyKind) {
        if (operation instanceof MOperation
            && concurrencyKind instanceof MCallConcurrencyKind) {
            ((MOperation) operation).setConcurrency(
                (MCallConcurrencyKind) concurrencyKind);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(operation) + " or " + 
	    getClassNull(concurrencyKind));
    }

    public static void setConcurent(Object handle, boolean concurrent) {
        if (handle instanceof MCompositeState) {
            ((MCompositeState) handle).setConcurent(concurrent);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Set the condition of an extend
     *
     * @param extend is the extend
     * @param booleanExpression is the condition
     */
    public static void setCondition(Object extend, Object booleanExpression) {
        if (extend instanceof MExtend
            && booleanExpression instanceof MBooleanExpression) {
            ((MExtend) extend).setCondition(
                (MBooleanExpression) booleanExpression);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(extend) + " or " + 
	    getClassNull(booleanExpression));
    }

    /**
     * Set the container of a statevertex.
     *
     * @param handle is the stateVertex
     * @param compositeState is the container
     */
    public static void setContainer(Object handle, Object compositeState) {
        if (handle instanceof MStateVertex
            && (compositeState == null
                || compositeState instanceof MCompositeState)) {
            ((MStateVertex) handle).setContainer(
                (MCompositeState) compositeState);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(compositeState));
    }

    public static void setContexts(Object handle, Collection c) {
        if (handle instanceof MSignal) {
            ((MSignal) handle).setContexts(c);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " + 
            getClassNull(handle));
    }

    /**
     * Sets the dispatch action for some stimulus
     * @param handle the stimulus
     * @param value the action
     */
    public static void setDispatchAction(Object handle, Object value) {
        if (handle instanceof MStimulus
            && (value == null || value instanceof MAction)) {
            ((MStimulus) handle).setDispatchAction((MAction) value);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(value));
    }

    /**
     * Sets the do activity of a state
     *
     * @param handle is the state
     * @param value the activity
     */
    public static void setDoActivity(Object handle, Object value) {
        if (handle instanceof MState
            && (value == null || value instanceof MAction)) {
            ((MState) handle).setDoActivity((MAction) value);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(value));
    }

    /**
     * Sets the effect of some transition
     *
     * @param handle is the transition
     * @param value is the effect
     */
    public static void setEffect(Object handle, Object value) {
        if (handle instanceof MTransition
            && (value == null || value instanceof MAction)) {
            ((MTransition) handle).setEffect((MAction) value);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(value));
    }

    /**
     * Sets the entry action of some state
     *
     * @param handle is the state
     * @param value is the action
     */
    public static void setEntry(Object handle, Object value) {
        if (handle instanceof MState
            && (value == null || value instanceof MAction)) {
            ((MState) handle).setEntry((MAction) value);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(value));
    }

    /**
     * Sets the exit action of some state
     *
     * @param handle is the state
     * @param value is the action
     */
    public static void setExit(Object handle, Object value) {
        if (handle instanceof MState
            && (value == null || value instanceof MAction)) {
            ((MState) handle).setExit((MAction) value);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(value));
    }

    public static void setExpression(Object handle, Object value) {
        if (handle instanceof MGuard
            && (value == null || value instanceof MBooleanExpression)) {
            ((MGuard) handle).setExpression((MBooleanExpression) value);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(value));
    }

    public static void setExtension(Object handle, Object ext) {
        if (handle instanceof MExtend
            && (ext == null || ext instanceof MUseCase)) {
            ((MExtend) handle).setExtension((MUseCase) ext);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(ext));
    }

    /**
     * Sets the extension points of some use cases.
     *
     * @param target the use case
     * @param extensionPoints is the extension points
     */
    public static void setExtensionPoints(
        Object target,
        Collection extensionPoints) {
        if (target instanceof MUseCase && extensionPoints instanceof List) {
            ((MUseCase) target).setExtensionPoints(extensionPoints);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(extensionPoints));
    }

    /**
     * Sets the features of some model element.
     * @param element the model element to set features to
     * @param features the list of features
     */
    public static void setFeatures(Object element, Collection features) {
        if (element != null
            && element instanceof MClassifier
            && features instanceof List) {
            ((MClassifier) element).setFeatures((List) features);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " + 
            getClassNull(element));
    }

    /**
     * Sets the aggregation of some model element.
     *
     * @param element the model element to set aggregation
     * @param aggregationKind the aggregation kind
     */
    public static void setAggregation(Object element, Object aggregationKind) {
        if (element instanceof MAssociationEnd
            && aggregationKind instanceof MAggregationKind) {
            ((MAssociationEnd) element).setAggregation(
                (MAggregationKind) aggregationKind);
                return;
        }
        throw new IllegalArgumentException("Unrecognized object : " + 
            getClassNull(element));
    }

    /**
     * Sets the association of some model element.
     *
     * @param target the model element to set association
     * @param association is the association
     */
    public static void setAssociation(Object target, Object association) {
        if (association instanceof MAssociation) {
            if (target instanceof MAssociationEnd) {
                ((MAssociationEnd) target).setAssociation(
                    (MAssociation) association);
                return;
            }
            if (target instanceof MLink) {
                ((MLink) target).setAssociation((MAssociation) association);
                return;
            }
        }
        throw new IllegalArgumentException("Unrecognized object : " + 
            getClassNull(target) + " or " + getClassNull(association));
    }

    public static void setChangeability(Object o, Object ck) {
        if (ck == null || ck instanceof MChangeableKind) {
            if (o instanceof MStructuralFeature) {
                ((MStructuralFeature) o).setChangeability((MChangeableKind) ck);
                return;
            }
            if (o instanceof MAssociationEnd) {
                ((MAssociationEnd) o).setChangeability((MChangeableKind) ck);
                return;
            }
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(o) + " or " + 
	    getClassNull(ck));
    }

    /**
     * Set the changeability of some feature.
     *
     * @param o is the feature
     * @param flag is the changeability flag
     */
    public static void setChangeable(Object o, boolean flag) {
        // FIXME: the implementation is ugly, because I have no spec
        // at hand...
        if (o instanceof MStructuralFeature) {
            if (flag) {
                ((MStructuralFeature) o).setChangeability(
                    MChangeableKind.CHANGEABLE);
                    return;
            }
            else {
                ((MStructuralFeature) o).setChangeability(
                    MChangeableKind.FROZEN);
            return;
            }
        } else if (o instanceof MAssociationEnd) {
            MAssociationEnd ae = (MAssociationEnd) o;
            if (flag)
                ae.setChangeability(MChangeableKind.CHANGEABLE);
            else
                ae.setChangeability(MChangeableKind.FROZEN);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " + 
            getClassNull(o));
    }

    public static void setChild(Object target, Object child) {
        if (target instanceof MGeneralization) {
            ((MGeneralization) target).setChild((MGeneralizableElement) child);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(child));
    }

    /**
     * Sets if of some model element is abstract.
     *
     * @param target is the classifier
     * @param flag is true if it should be abstract
     */
    public static void setAbstract(Object target, boolean flag) {
        
	if (target instanceof MGeneralizableElement) {
	    ((MGeneralizableElement) target).setAbstract(flag);
	    return;
	}
	if (target instanceof MOperation) {
	    ((MOperation) target).setAbstract(flag);
	    return;
	}
	if (target instanceof MReception) {
	    ((MReception) target).setAbstarct(flag);
	}
        throw new IllegalArgumentException("Unrecognized object : " + 
					   getClassNull(target));
    }

    public static void setAddition(Object target, Object useCase) {
        if (target != null && target instanceof MInclude) {
            ((MInclude) target).setAddition((MUseCase) useCase);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " + 
            getClassNull(target));
    }

    /**
     * Sets the action to a message
     *
     * @param message is the message
     * @param action is the action
     */
    public static void setAction(Object message, Object action) {
        if (message instanceof MMessage
            && (action == null || action instanceof MAction)) {
            ((MMessage) message).setAction((MAction) action);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(message) + " or " + 
	    getClassNull(action));
    }

    public static void setActivator(Object handle, Object message) {
        if (handle instanceof MMessage
            && (message == null || message instanceof MMessage)) {
            ((MMessage) handle).setActivator((MMessage) message);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(message));
    }

    public static void setActive(Object handle, boolean active) {
        if (handle instanceof MClass) {
            ((MClass) handle).setActive(active);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Sets the asynchronous property of an action.
     *
     * @param handle the action
     * @param value the value to alter the asynchronous property to
     */
    public static void setAsynchronous(Object handle, boolean value) {
        if (handle instanceof MAction) {
            ((MAction) handle).setAsynchronous(value);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(handle));
    }

    /**
     * Sets if some model element is a leaf.
     *
     * @param target model element
     * @param flag is true if it is a leaf.
     */
    public static void setLeaf(Object target, boolean flag) {
        if (target instanceof MReception) {
            ((MReception) target).setLeaf(flag);
            return;
        }
        if (target instanceof MOperation) {
            ((MOperation) target).setLeaf(flag);
            return;
        }
        if (target instanceof MGeneralizableElement) {
            ((MGeneralizableElement) target).setLeaf(flag);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object " +
					   getClassNull(target));
    }

    /**
     * Sets the raised signals of some behavioural feature.
     * @param target the behavioural feature
     * @param raisedSignals the raised signals
     */
    public static void setRaisedSignals(
        Object target,
        Collection raisedSignals) {
        if (target instanceof MBehavioralFeature) {
            ((MBehavioralFeature) target).setRaisedSignals(raisedSignals);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(target));
    }

    /**
     * Sets the receiver of some model element.
     * @param target model element
     * @param receiver the receiver
     */
    public static void setReceiver(Object target, Object receiver) {
        if (target instanceof MMessage
            && receiver instanceof MClassifierRole) {
            ((MMessage) target).setReceiver((MClassifierRole) receiver);
            return;
        }
        if (target instanceof MStimulus && receiver instanceof MInstance) {
            ((MStimulus) target).setReceiver((MInstance) receiver);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(receiver));
    }

    public static void setRecurrence(Object target, Object expr) {
        if (target instanceof MAction
            && expr instanceof MIterationExpression) {
            ((MAction) target).setRecurrence((MIterationExpression) expr);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(expr));
    }

    /**
     * Sets the represented classifier of some collaboration
     *
     * @param target the collaboration
     * @param classifier is the classifier
     */
    public static void setRepresentedClassifier(
        Object target,
        Object classifier) {
        if (target instanceof MCollaboration
            && classifier instanceof MClassifier) {
            ((MCollaboration) target).setRepresentedClassifier(
                (MClassifier) classifier);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(classifier));
    }

    /**
     * Sets the represented operation of some collaboration
     *
     * @param target the collaboration
     * @param operation is the operation
     */
    public static void setRepresentedOperation(
        Object target,
        Object operation) {
        if (target instanceof MCollaboration
            && operation instanceof MOperation) {
            ((MCollaboration) target).setRepresentedOperation(
                (MOperation) operation);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(operation));
    }

    public static void setResident(Object handle, Object resident) {
        if (handle instanceof MElementResidence
            && (resident == null || resident instanceof MModelElement)) {
            ((MElementResidence) handle).setResident((MModelElement) resident);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(resident));
    }

    /**
     * Sets the residents of some model element.
     *
     * @param target the model element
     * @param residents collection
     */
    public static void setResidents(Object target, Collection residents) {
        if (target instanceof MNodeInstance) {
            ((MNodeInstance) target).setResidents(residents);
            return;
        }
        if (target instanceof MComponentInstance) {
            ((MComponentInstance) target).setResidents(residents);
            return;
        }
        if (target instanceof MNode) {
            ((MNode) target).setResidents(residents);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(target));
    }

    /**
     * Sets if some model element is a root.
     *
     * @param target model element
     * @param flag is true if it is a root
     */
    public static void setRoot(Object target, boolean flag) {
        if (target instanceof MReception) {
            ((MReception) target).setRoot(flag);
            return;
        }
        if (target instanceof MOperation) {
            ((MOperation) target).setRoot(flag);
            return;
        }
        if (target instanceof MGeneralizableElement) {
            ((MGeneralizableElement) target).setRoot(flag);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(target));
    }

    public static void setScript(Object target, Object expr) {
        if (target instanceof MAction
            && (expr == null || expr instanceof MActionExpression)) {
            ((MAction) target).setScript((MActionExpression) expr);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(expr));
    }
    /**
     * Sets the sender of some model element.
     * @param target model element
     * @param sender the sender
     */
    public static void setSender(Object target, Object sender) {
        if (target instanceof MMessage && sender instanceof MClassifierRole) {
            ((MMessage) target).setSender((MClassifierRole) sender);
            return;
        }
        if (target instanceof MStimulus && sender instanceof MInstance) {
            ((MStimulus) target).setSender((MInstance) sender);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(sender));
    }

    public static void setSignal(Object target, Object signal) {
        if (signal == null || signal instanceof MSignal) {
            if (target instanceof MSendAction) {
                ((MSendAction) target).setSignal((MSignal) signal);
                return;
            }
            if (target instanceof MReception) {
                ((MReception) target).setSignal((MSignal) signal);
                return;
            }
            if (target instanceof MSignalEvent) {
                ((MSignalEvent) target).setSignal((MSignal) signal);
                return;
            }
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(signal));
    }

    /**
     * Sets the source state of some message.
     *
     * @param target the message
     * @param state the source state
     */
    public static void setSource(Object target, Object state) {
        if (target instanceof MMessage && state instanceof MClassifierRole) {
            ((MTransition) target).setSource((MState) state);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(state));
    }

    public static void setSources(Object target, Collection specifications) {
        if (target instanceof MFlow) {
            ((MFlow) target).setSources(specifications);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(target));
    }

    public static void setSpecification(Object target, boolean specification) {
        if (target instanceof MModelElement) {
            ((MModelElement) target).setSpecification(specification);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(target));
    }

    /**
     * Sets the specifications of some association end.
     *
     * @param target the association end
     * @param specifications collection
     */
    public static void setSpecifications(
        Object target,
        Collection specifications) {
        if (target instanceof MAssociationEnd) {
            ((MAssociationEnd) target).setSpecifications(specifications);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object " + 
					   getClassNull(target));
    }

    /**
     * Set some parameters kind.
     *
     * @param target is the parameter
     * @param kind is the directionkind
     */
    public static void setKind(Object target, Object kind) {
        if (target instanceof MParameter
            && kind instanceof MParameterDirectionKind) {
            ((MParameter) target).setKind((MParameterDirectionKind) kind);
            return;
        }
        if (target instanceof MPseudostate
            && kind instanceof MPseudostateKind) {
            ((MPseudostate) target).setKind((MPseudostateKind) kind);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(kind));
    }

    /**
     * Set some parameters kind to 'in'.
     * @param p is the parameter
     */
    public static void setKindToIn(Object p) {
        if (p != null && p instanceof MParameter) {
	    ((MParameter) p).setKind(MParameterDirectionKind.IN);
	    return;
        }
        throw new IllegalArgumentException("Unrecognized object : " + 
            getClassNull(p));
    }

    /**
     * Set some parameters kind to 'in/out'.
     * @param p is the parameter
     */
    public static void setKindToInOut(Object p) {
        if (p != null && p instanceof MParameter) {
	    ((MParameter) p).setKind(MParameterDirectionKind.INOUT);
	    return;
        }
        throw new IllegalArgumentException("Unrecognized object : " + 
            getClassNull(p));
    }

    /**
     * Set some parameters kind to 'out'.
     * @param p is the parameter
     */
    public static void setKindToOut(Object p) {
        if (p != null && p instanceof MParameter) {
	    ((MParameter) p).setKind(MParameterDirectionKind.OUT);
	    return;
        }
        throw new IllegalArgumentException("Unrecognized object : " + 
            getClassNull(p));
    }

    /**
     * Set some parameters kind to 'return'.
     * @param p is the parameter
     */
    public static void setKindToReturn(Object p) {
        if (p != null && p instanceof MParameter) {
	    ((MParameter) p).setKind(MParameterDirectionKind.RETURN);
	    return;
        }
        throw new IllegalArgumentException("Unrecognized object : " + 
            getClassNull(p));
    }

    /**
     * Sets the parent of a generalization.
     *
     * @param target generalization
     * @param parent generalizable element (parent)
     */
    public static void setParent(Object target, Object parent) {
        if (target instanceof MGeneralization
            && parent instanceof MGeneralizableElement) {
            ((MGeneralization) target).setParent(
	            (MGeneralizableElement) parent);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(parent));
    }

    public static void setPowertype(Object target, Object pt) {
        if (target instanceof MGeneralization && pt instanceof MClassifier) {
            ((MGeneralization) target).setPowertype((MClassifier) pt);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + 
	    getClassNull(target) + " or " + 
	    getClassNull(pt));
    }

    public static void setPredecessors(
        Object target,
        Collection predecessors) {
        if (target instanceof MMessage) {
            ((MMessage) target).setPredecessors(predecessors);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(predecessors));
    }

    /**
     * Sets the query flag of a behavioral feature.
     *
     * @param bf is the behavioral feature
     * @param flag is the query flag
     */
    public static void setQuery(Object bf, boolean flag) {
        if (bf instanceof MBehavioralFeature) {
            ((MBehavioralFeature) bf).setQuery(flag);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " + 
            getClassNull(bf));
    }

    /**
     * Sets the type of some parameter.
     *
     * @param handle is the model element
     * @param type is the type (a classifier)
     */
    public static void setType(Object handle, Object type) {
        if (type instanceof MClassifier) {
            if (handle instanceof MObjectFlowState) {
		((MObjectFlowState) handle).setType((MClassifier) type);
		return;
	    }
            if (handle instanceof MClassifierInState) {
		((MClassifierInState) handle).setType((MClassifier) type);
		return;
	    }
            if (handle instanceof MParameter) {
		((MParameter) handle).setType((MClassifier) type);
		return;
	    }
            if (handle instanceof MAssociationEnd) {
		((MAssociationEnd) handle).setType((MClassifier) type);
		return;
	    }
            if (handle instanceof MStructuralFeature) {
		((MStructuralFeature) handle).setType((MClassifier) type);
		return;
	    }
	    
        }
        throw new IllegalArgumentException("Unrecognized object : " + 
            getClassNull(handle) + " or " + getClassNull(type));
    }

    /**
     * Set the UUID of this element
     *
     * @param base base element (MBase type)
     * @param uuid is the UUID
     */
    public static void setUUID(Object base, String uuid) {
        if (isABase(base)) {
            ((MBase) base).setUUID(uuid);
            return;
        } 
        throw new IllegalArgumentException("Unrecognized object " + 
					       getClassNull(base));
    }

    public static void setTag(Object target, Object tag) {
        if (target instanceof MTaggedValue && tag instanceof String) {
            ((MTaggedValue) target).setTag((String) tag);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(tag));
    }

    /**
     * Sets a tagged value of some modelelement.
     *
     * @param target is the model element
     * @param tag is the tag name (a string)
     * @param value is the value
     */
    public static void setTaggedValue(
        Object target,
        String tag,
        String value) {
        if (target instanceof MModelElement) {
	    ((MModelElement) target).setTaggedValue(tag, value);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " + 
            getClassNull(target));
    }

    public static void setTaggedValues(
        Object target,
        Collection taggedValues) {
        if (target instanceof MModelElement) {
            ((MModelElement) target).setTaggedValues(taggedValues);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(taggedValues));
    }

    /**
     * Sets a value of some taggedValue.
     *
     * @param tv is the tagged value
     * @param value is the value
     */
    public static void setValueOfTag(Object tv, String value) {
        if (tv instanceof MTaggedValue) {
            ((MTaggedValue) tv).setValue(value);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " + 
            getClassNull(tv));
    }

    /**
     * Sets a state machine of some state.
     *
     * @param st is the state
     * @param stm is the state machine
     */
    public static void setStateMachine(Object st, Object stm) {
        if (st != null
            && st instanceof MState
            && (stm == null || stm instanceof MStateMachine)) {
            ((MState) st).setStateMachine((MStateMachine) stm);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object : " + 
            getClassNull(st) + " or " + getClassNull(stm));
    }

    /**
     * Sets the stereotype of some modelelement. The method also copies a
     * stereotype that is not a part of the current model to the current model.
     * @param m model element
     * @param stereo stereotype
     */
    public static void setStereotype(Object m, Object stereo) {
        if (m != null && m instanceof MModelElement) {
            MModelElement me = (MModelElement) m;
            if (stereo instanceof MStereotype
                && me.getModel() != ((MStereotype) stereo).getModel()) {
                ((MStereotype) stereo).setNamespace(me.getModel());
                return;
            }
            if (stereo == null || stereo instanceof MStereotype) {
                me.setStereotype((MStereotype) stereo);
                return;
            }
        }
        throw new IllegalArgumentException("Unrecognized object : " + 
            getClassNull(m) + " or " + getClassNull(stereo));
    }

    public static void setSubvertices(Object handle, Collection subvertices) {
        if (handle instanceof MCompositeState) {
            ((MCompositeState) handle).setSubvertices(subvertices);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object " +
                       getClassNull(handle) + " or " +
                       getClassNull(subvertices));
    }
    
    public static void addConnection(Object handle, Object connection) {
        if (handle instanceof MAssociation
            && connection instanceof MAssociationEnd) {
            ((MAssociation) handle).addConnection((MAssociationEnd) connection);
            return;
        }
        if (handle instanceof MLink
            && connection instanceof MLinkEnd) {
            ((MLink) handle).addConnection((MLinkEnd) connection);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(connection));
    }

    /**
     * Adds a constraint to some model element.
     * @param me model element
     * @param mc constraint
     */
    public static void addConstraint(Object me, Object mc) {
        if (me instanceof MModelElement && mc instanceof MConstraint) {
            ((MModelElement) me).addConstraint((MConstraint) mc);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(me) + " or " + 
	    getClassNull(mc));
    }

    public static void addDeploymentLocation(Object handle, Object node) {
        if (handle instanceof MComponent && node instanceof MNode) {
            ((MComponent) handle).addDeploymentLocation((MNode) node);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle) + " or " + 
	    getClassNull(node));
    }

    public static void addExtendedElement(
        Object target,
        Object extendedElement) {
        if (target instanceof MStereotype
            && extendedElement instanceof MExtensionPoint) {
            ((MStereotype) target).addExtendedElement(
                (MModelElement) extendedElement);
            return;
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(extendedElement));
    }

    /**
     * Adds an extension point to some model element.
     *
     * @param target is the model element
     * @param extensionPoint is the extension point
     */
    public static void addExtensionPoint(
        Object target,
        Object extensionPoint) {
        if (target != null
            && extensionPoint != null
            && extensionPoint instanceof MExtensionPoint) {
            if (target instanceof MUseCase) {
                ((MUseCase) target).addExtensionPoint(
                    (MExtensionPoint) extensionPoint);
                return;
            }
            if (target instanceof MExtend) {
                ((MExtend) target).addExtensionPoint(
                    (MExtensionPoint) extensionPoint);
                return;
            }
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(target) + " or " + 
	    getClassNull(extensionPoint));
    }

    /**
     * getUMLClassName returns the name of the UML Model class, e.g. it
     * it will return Class for an object of type MClass.
     *
     * @param handle MBase
     * @return classname of modelelement
     */
    public static String getUMLClassName(Object handle) {
        if (handle instanceof MBase) {
            return ((MBase) handle).getUMLClassName();
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + getClassNull(handle));
    }

    ////////////////////////////////////////////////////////////////
    // Convenience methods

    /**
     * The empty set.
     *
     * @return an empty iterator.
     */
    private static Iterator emptyIterator() {
        return Collections.EMPTY_SET.iterator();
    }

    private static Collection emptyCollection() {
        return Collections.EMPTY_LIST;
    }

    protected static String getClassNull(Object handle) {
	if (handle == null) {
	    return "[null]";
	} else {
	    return "[" + handle + "/" + handle.getClass() + "]";
	}
    }
}
