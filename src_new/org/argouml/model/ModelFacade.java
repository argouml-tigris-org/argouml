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
import org.argouml.uml.diagram.static_structure.ui.CommentEdge;
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
import ru.novosoft.uml.behavior.state_machines.MCallEvent;
import ru.novosoft.uml.behavior.state_machines.MChangeEvent;
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
import ru.novosoft.uml.foundation.data_types.MBooleanExpression;
import ru.novosoft.uml.foundation.data_types.MCallConcurrencyKind;
import ru.novosoft.uml.foundation.data_types.MChangeableKind;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MExpressionEditor;
import ru.novosoft.uml.foundation.data_types.MIterationExpression;
import ru.novosoft.uml.foundation.data_types.MMessageDirectionKind;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MMultiplicityRange;
import ru.novosoft.uml.foundation.data_types.MObjectSetExpression;
import ru.novosoft.uml.foundation.data_types.MOperationDirectionKind;
import ru.novosoft.uml.foundation.data_types.MOrderingKind;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;
import ru.novosoft.uml.foundation.data_types.MProcedureExpression;
import ru.novosoft.uml.foundation.data_types.MPseudostateKind;
import ru.novosoft.uml.foundation.data_types.MScopeKind;
import ru.novosoft.uml.foundation.data_types.MTimeExpression;
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
    public static final Object COMMENT = MComment.class;
    public static final Object COMPONENT = MComponent.class;
    public static final Object COMPONENT_INSTANCE = MComponentInstance.class;
    public static final Object EXCEPTION = MException.class;
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
    public static final Object PARTITION = MPartition.class;
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
    public static final Object EVENT = MEvent.class;

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


    /**
     * This tag is set on elements that are generated by reference when
     * importing.
     * If it is set, then the critics could ignore those objects if they want.
     */
    public static final String GENERATED_TAG = "GeneratedFromImport";


    ////////////////////////////////////////////////////////////////
    // Object Creation methods

    /**
     * Create a model object from the implementation.<P>
     *
     * This will allow abstraction of the create mechanism at a single point.
     *
     * TODO: Document the intention of this function.
     * It is not used anywhere in ArgoUML. 
     * BTW: Does it work? I (MVW) once did a test, and it didn't.
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
	return illegalArgumentBoolean(handle);
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
        if (handle instanceof MAssociation)
            return ((MAssociation) handle).isAbstract();
        // isAbstarct() is not a typo! mistake in nsuml!
        if (handle instanceof MReception)
            return ((MReception) handle).isAbstarct();
        // ...
	return illegalArgumentBoolean(handle);
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
     * an element in the model. MBase in Novosoft terms.
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
     * Recognizer for ChangeEvent
     *
     * @param handle candidate
     * @return true if handle is a ChangeEvent
     */
    public static boolean isAChangeEvent(Object handle) {
        return handle instanceof MChangeEvent;
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
	return illegalArgumentBoolean(handle);
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
     * Recognizer for Event
     *
     * @param handle candidate
     * @return true if handle is an Event
     */
    public static boolean isAEvent(Object handle) {
        return handle instanceof MEvent;
    }

    /**
     * Recognizer for Exception
     *
     * @param handle candidate
     * @return true if handle is an Exception
     */
    public static boolean isAException(Object handle) {
        return handle instanceof MException;
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
     * Recognizer for MultiplicityRange
     *
     * @param handle candidate
     * @return true if handle is a MultiplicityRange
     */
    public static boolean isAMultiplicityRange(Object handle) {
        return handle instanceof MMultiplicityRange;
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
     * Recognizer for Partition
     *
     * @param handle candidate
     * @return true if handle is a Partition
     */
    public static boolean isAPartition(Object handle) {
        return handle instanceof MPartition;
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentObject(handle);
    }

    public static Object getKind(Object handle) {
        if (handle instanceof MPseudostate) {
            return ((MPseudostate) handle).getKind();
        }
        if (handle instanceof MParameter) {
            return ((MParameter) handle).getKind();
        }
	return illegalArgumentObject(handle);
    }

    public static Object getLink(Object handle) {
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
    public static boolean equalsPseudostateKind(Object ps1, Object ps2) {
        if (isAPseudostateKind(ps1)) {
            return ((MPseudostateKind) ps1).equals(ps2);
        }
	return illegalArgumentBoolean(ps1);
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
     * Recognizer for SignalEvent
     *
     * @param handle candidate
     * @return true if handle is a SignalEvent
     */
    public static boolean isASignalEvent(Object handle) {
        return handle instanceof MSignalEvent;
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
     * Recognizer for TimeEvent
     *
     * @param handle candidate
     * @return true if handle is a TimeEvent
     */
    public static boolean isATimeEvent(Object handle) {
        return handle instanceof MTimeEvent;
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
    public static boolean isClassifierScope(Object handle) {
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
     * Recognizer for concurent composite state.
     *
     * @param handle composite state
     * @return true if concurent.
     */
    public static boolean isConcurent(Object handle) {
        if (handle instanceof MCompositeState) {
            return ((MCompositeState) handle).isConcurent();
        }
	return illegalArgumentBoolean(handle);
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
	return illegalArgumentBoolean(handle);
    }

    public static boolean isFrozen(Object handle) {
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
    public static boolean isComposite(Object handle) {
        if (isAAssociationEnd(handle)) {
            boolean composite = false;
            MAssociationEnd end = (MAssociationEnd) handle;
            if (end.getAggregation() != null
                && end.getAggregation().equals(MAggregationKind.COMPOSITE))
                composite = true;
            return composite;
        }
	return illegalArgumentBoolean(handle);
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
	return illegalArgumentBoolean(handle);
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
	return illegalArgumentBoolean(handle);
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
	return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for internal transitions.
     *
     * @author mvw
     * @param handle candidate
     * @return true if handle is an internal transition.
     */
    public static boolean isInternal(Object handle) {
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
	return illegalArgumentBoolean(handle);
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
	return illegalArgumentBoolean(handle);
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
	return illegalArgumentBoolean(handle);
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
    public static boolean isPrimaryObject(Object handle) {
        if (handle instanceof MModelElement) {
            MModelElement element = (MModelElement) handle;
            for (Iterator i = element.getTaggedValues().iterator();
		 i.hasNext(); ) {
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
	return illegalArgumentBoolean(handle);
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
	return illegalArgumentBoolean(handle);
    }

    /**
     * Recognizer for MBehaviouralFeature's that are queries.
     *
     * @param handle candidate
     * @return true if it is a query
     */
    public static boolean isQuery(Object handle) {

        if (handle instanceof MBehavioralFeature) {
	    return ((MBehavioralFeature) handle).isQuery();
        }

	return illegalArgumentBoolean(handle);
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
	return illegalArgumentBoolean(handle);
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
	return illegalArgumentBoolean(handle);
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
	return illegalArgumentBoolean(handle);
    }

    public static boolean isTop(Object handle) {
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
     * Returns the association connected to an association end or
     * the association belonging to the given link.
     *
     * @param handle is the link
     * @return association end
     */
    public static Object getAssociation(Object handle) {

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
    public static Object getAssociationEnd(Object handle, Object assoc) {
        if (handle instanceof MClassifier
	        && assoc instanceof MAssociation) {
	    MClassifier classifier = (MClassifier) handle;

	    Iterator it = classifier.getAssociationEnds().iterator();
	    while (it.hasNext()) {
		MAssociationEnd end = (MAssociationEnd) it.next();
		if (((MAssociation) assoc).getConnections().contains(end))
		    return end;
	    }
	    return null;
	}

	return illegalArgumentObject(handle, assoc);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentObject(handle);
    }

    /**
     * The base of some model element
     * There is a bug in NSUML which gets the addition and base 
     * relationships back to front for include relationships. Solve
     * by reversing their accessors in the code
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
    public static Collection getBases(Object handle) {
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
    public static Collection getBehaviors(Object handle) {
        if (isAModelElement(handle))
            return ((MModelElement) handle).getBehaviors();
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentObject(handle);
    }

    public static Object getChangeability(Object handle) {
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
    public static Object getChild(Object handle) {
        if (handle instanceof MGeneralization) {
            return ((MGeneralization) handle).getChild();
        }

        // ...
	return illegalArgumentObject(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
    }

    /**
     * Get the condition of an extend.
     *
     * @param handle The extend.
     * @return the condition
     */
    public static Object getCondition(Object handle) {
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
    public static Object getConcurrency(Object handle) {
        if (handle instanceof MOperation) {
            return ((MOperation) handle).getConcurrency();
        }
	return illegalArgumentObject(handle);
    }

    //    public static short getConcurrency(Object handle) {
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
    public static Collection getConnections(Object handle) {
        if (handle instanceof MAssociation) {
            return ((MAssociation) handle).getConnections();
        }
        if (handle instanceof MLink) {
            return ((MLink) handle).getConnections();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Determine if a model element contains a connection.
     *
     * @param handle is the model element
     * @param connection is the connection that is searched for.
     * @return true if the model element contains a connection
     */
    public boolean containsConnection(Object handle, Object connection) {
        if (handle instanceof MAssociation) {
            return ((MAssociation) handle).getConnections().contains(
                connection);
        }

        // ...
	return illegalArgumentBoolean(handle);
    }

    /**
     * Count the number of Connections or AssociationEnds to an Association.
     *
     * @param handle to the association.
     * @return an Iterator with all connections.
     * @deprecated by Linus Tolke as of 0.15.5. Use
     * {@link #getConnections(Object)}.size() instead.
     */
    public static int getConnectionCount(Object handle) {
        if (handle instanceof MAssociation) {
            return ((MAssociation) handle).getConnections().size();
        }

        // ...
	illegalArgument(handle);
	return 0;
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentCollection(handle);
    }

    public static Collection getElementImports2(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getElementImports2();
        }
	return illegalArgumentCollection(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentObject(handle);
    }

    public static Object getExpression(Object handle) {
        if (handle instanceof MGuard) {
            return ((MGuard) handle).getExpression();
	}
        if (handle instanceof MChangeEvent) {
            return ((MChangeEvent) handle).getChangeExpression();
        }
        if (handle instanceof MTimeEvent) {
            return ((MTimeEvent) handle).getWhen();
        }       
        return illegalArgumentObject(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
    }

    /**
     * Gets the use case extension of an extend
     *
     * @param handle is the extend
     * @return The extension
     */
    public static Object getExtension(Object handle) {
        if (handle instanceof MExtend) {
            return ((MExtend) handle).getExtension();
	}
	return illegalArgumentObject(handle);
    }

    public static Object getExtensionPoint(Object handle, int index) {
        if (handle instanceof MExtend) {
            return ((MExtend) handle).getExtensionPoint(index);
        }
	return illegalArgumentObject(handle);
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
	return illegalArgumentCollection(handle);
    }

    /**
     * The list of Features from a Classifier.
     *
     * @param handle Classifier to retrieve from.
     * @return Collection with Features
     */
    public static Collection getFeatures(Object handle) {
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
    public static Object getGeneralization(Object handle, Object parent) {
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
    public static Collection getGeneralizations(Object handle) {
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
    public static Object getGuard(Object handle) {
        if (isATransition(handle)) {
            return ((MTransition) handle).getGuard();
        }
	return illegalArgumentObject(handle);
    }

    public static Object getIcon(Object handle) {
        if (handle instanceof MStereotype) {
            return ((MStereotype) handle).getIcon();
        }
	return illegalArgumentObject(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
    }

    public static Collection getMessages4(Object handle) {
        if (handle instanceof MMessage) {
            return ((MMessage) handle).getMessages4();
        }
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
    }

    public static Collection getMessages2(Object handle) {
        if (handle instanceof MClassifierRole) {
            return ((MClassifierRole) handle).getMessages2();
        }
	return illegalArgumentCollection(handle);
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
	return illegalArgumentObject(handle);
    }

    /**
     * @param handle an Element Import.
     * @return the model element
     */
    public static Object getModelElement(Object handle) {
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
    public static Object getMultiplicity(Object handle) {
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
    public static Iterator getRanges(Object handle) {
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
    public static Collection getComments(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getComments();
	}
        // ...
	return illegalArgumentCollection(handle);
    }

    /**
     * Get the communication connection of an message.
     *
     * @param handle the message that we are getting the communication
     * connection
     * @return the communication connection
     */
    public static Object getCommunicationConnection(Object handle) {
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
    public static Object getCommunicationLink(Object handle) {
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
    public static Collection getCollaborations(Object handle) {
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
	illegalArgument(element);
    }

    /**
     * Get the component instance of an instance
     *
     * @param handle is the instance
     * @return the component instance
     */
    public static Object getComponentInstance(Object handle) {
        if (handle instanceof MInstance) {
            return ((MInstance) handle).getComponentInstance();
	}
        // ...
	return illegalArgumentObject(handle);
    }

    public static Collection getConstrainingElements(Object handle) {
        if (handle instanceof MCollaboration) {
            return ((MCollaboration) handle).getConstrainingElements();
        }
	return illegalArgumentCollection(handle);
    }

    public static Collection getConstraints(Object handle) {
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
    public static Object getModelElementContainer(Object handle) {
        if (handle instanceof MBase) {
            return ((MBase) handle).getModelElementContainer();
        }
	return illegalArgumentObject(handle);
    }

    public static Object getContainer(Object handle) {
        if (handle instanceof MStateVertex) {
            return ((MStateVertex) handle).getContainer();
        }
	return illegalArgumentObject(handle);
    }
    
    public static Collection getContents(Object handle) {
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
    public static Object getContext(Object handle) {
        if (isAStateMachine(handle)) {
            return ((MStateMachine) handle).getContext();
        }
        if (isAInteraction(handle)) {
            return ((MInteraction) handle).getContext();
        }
	return illegalArgumentObject(handle);
    }

    public static Collection getContexts(Object handle) {
        if (handle instanceof MSignal) {
            return ((MSignal) handle).getContexts();
        }
	return illegalArgumentCollection(handle);
    }

    public static Collection getCreateActions(Object handle) {
        if (handle instanceof MClassifier) {
            return ((MClassifier) handle).getCreateActions();
        }
	return illegalArgumentCollection(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentObject(handle);
    }
    
    /**
     * 
     * @param handle a generalization
     * @param discriminator the discriminator to set
     */
    public static void setDiscriminator(Object handle, String discriminator) {
        if (handle instanceof MGeneralization) {
             ((MGeneralization) handle).setDiscriminator(discriminator);
             return;
        }
        illegalArgument(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentObject(handle);
    }

    public static Collection getLinks(Object handle) {
        if (handle instanceof MAssociation) {
            return ((MAssociation) handle).getLinks();
        }
	return illegalArgumentCollection(handle);
    }

    public static Collection getLinkEnds(Object handle) {
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
    public static String getLocation(Object handle) {
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
    public static Collection getMethods(Object handle) {
        if (handle instanceof MOperation)
            return ((MOperation) handle).getMethods();
        // ...
	return illegalArgumentCollection(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentCollection(handle);
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
	illegalArgument(handle);
	return null;
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
    }

    /**
     * Determine if the passed parameter has a RETURN direction kind
     *
     * @return true if it is a return direction kind
     * @param handle is the parameter
     */
    public static boolean hasReturnParameterDirectionKind(Object handle) {
        if (handle instanceof MParameter) {
	    MParameter parameter = (MParameter) handle;
	    return (MParameterDirectionKind.RETURN.equals(parameter.getKind()));
	}
	return illegalArgumentBoolean(handle);
    }

    public static Object getPackage(Object handle) {
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
    public static Object getParameter(Object handle, int n) {
        if (handle instanceof MBehavioralFeature) {
            return ((MBehavioralFeature) handle).getParameter(n);
	}
	return illegalArgumentObject(handle);
    }

    /**
     * Get the parameters of a Object Flow State, Behavioral Feature, Classifier or Event.
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
	return illegalArgumentCollection(handle);
    }

    /**
     * Get the parent of a generalization.
     *
     * @param handle generalization.
     * @return the parent.
     */
    public static Object getParent(Object handle) {
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
    public static Collection getRaisedSignals(Object handle) {
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
    public static Collection getReceptions(Object handle) {
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
    public static Object getRecurrence(Object handle) {
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
    public static Object getRepresentedClassifier(Object handle) {
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
    public static Object getRepresentedOperation(Object handle) {
        if (handle instanceof MCollaboration) {
            return ((MCollaboration) handle).getRepresentedOperation();
        }
	return illegalArgumentObject(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentObject(handle);
    }

    public static Collection getResidentElements(Object handle) {
        if (handle instanceof MComponent) {
            return ((MComponent) handle).getResidentElements();
        }
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
    }

    /**
     * Gets the source for a given transition.
     *
     * @param handle is the transition
     * @return Object (MStateVertex)
     */
    public static Object getSource(Object handle) {
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
    public static Collection getSources(Object handle) {
        if (handle instanceof MFlow) {
            return ((MFlow) handle).getSources();
        }
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
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
    public static Object getStateMachine(Object handle) {
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
    public static Object getState(Object handle) {
        if (handle instanceof MTransition) {
            return ((MTransition) handle).getState();
        }
	return illegalArgumentObject(handle);
    }
    
    /**
     * Returns the states from a deferable event
     *
     * @param handle is the event
     * @return Object
     */
    public static Collection getStates(Object handle) {
        if (handle instanceof MEvent) {
            return ((MEvent) handle).getStates();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Returns the stereotype belonging to some given model element
     *
     * @param handle is a model element
     * @return Object
     * @deprecated 0.15 in favor of getStereotypes since UML 1.5 supports multiple stereotypes
     */
    public static Object getStereoType(Object handle) {
        if (isAModelElement(handle)) {
            return ((MModelElement) handle).getStereotype();
        }
	return illegalArgumentObject(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
    }

    public static Collection getStimuli2(Object handle) {
        if (handle instanceof MInstance) {
            return ((MInstance) handle).getStimuli2();
        }
	return illegalArgumentCollection(handle);
    }

    public static Collection getStimuli3(Object handle) {
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
    public static Collection getSubvertices(Object handle) {
        if (isACompositeState(handle)) {
            return ((MCompositeState) handle).getSubvertices();
        }
	return illegalArgumentCollection(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentCollection(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentCollection(handle);
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
        if (isAMultiplicityRange(handle)) {
            MMultiplicityRange up = (MMultiplicityRange) handle;
            return up.getUpper();
        }
        illegalArgument(handle);
	return 0;
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
	return illegalArgumentObject(handle);
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
        if (isAMultiplicityRange(handle)) {
            MMultiplicityRange low = (MMultiplicityRange) handle;
            return low.getLower();
        }
        illegalArgument(handle);
	return 0;
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
    public static Collection getStructuralFeatures(Object handle) {
        Collection result = new ArrayList();
        if (ModelFacade.isAClassifier(handle)) {
            MClassifier mclassifier = (MClassifier) handle;

            Iterator features = mclassifier.getFeatures().iterator();
            while (features.hasNext()) {
                MFeature feature = (MFeature) features.next();
                if (ModelFacade.isAStructuralFeature(feature))
                    result.add(feature);
            }
            return result;
        }
	return illegalArgumentCollection(handle);
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
    
    public static String getSpecification(Object handle) {
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
    public static Collection getSpecifications(Object handle) {
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
    public static Collection getSuppliers(Object handle) {
        if (handle instanceof MDependency) {
            return ((MDependency) handle).getSuppliers();
        }
	return illegalArgumentCollection(handle);
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
	return illegalArgumentObject(handle);
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
	return illegalArgumentObject(handle);
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
    public static Object getAddition(Object handle) {
        if (handle instanceof MInclude) {
            return ((MInclude) handle).getBase();
        }
	return illegalArgumentObject(handle);
    }

    public static Object getAggregation(Object handle) {
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
    public static Collection getAssociatedClasses(Object handle) {
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
        if (handle instanceof MCallConcurrencyKind) {
            return ((MCallConcurrencyKind) handle).getName();
        }
        illegalArgument(handle);
	return "";
    }

    /**
     * Return the owner of a feature.
     *
     * @param handle is the feature
     * @return classifier
     */
    public static Object getOwner(Object handle) {
        if (handle instanceof MFeature) {
            return ((MFeature) handle).getOwner();
        }
	return illegalArgumentObject(handle);
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
	return illegalArgumentObject(handle);
    }

    /**
     * Return the tagged values iterator of a model element.
     *
     * @param handle The tagged values belong to this.
     * @return The tagged values iterator
     */
    public static Iterator getTaggedValues(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getTaggedValues().iterator();
        }
	illegalArgument(handle);
	return null;
    }

    public static Collection getTaggedValuesCollection(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getTaggedValues();
        }
	return illegalArgumentCollection(handle);
    }

    /**
     * Return the tagged value with a specific tag.
     *
     * @param handle The tagged value belongs to this.
     * @param name The tag.
     * @return The found tag, null if not found
     */
    public static Object getTaggedValue(Object handle, String name) {
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
     * Return the key (tag) of some tagged value.
     *
     * @param handle The tagged value.
     * @return The found value, null if not found
     */
    public static String getTagOfTag(Object handle) {
        if (handle instanceof MTaggedValue) {
            return ((MTaggedValue) handle).getTag();
        }
	illegalArgument(handle);
	return "";
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
	return illegalArgumentObject(handle);
    }

    /**
     * Return the value of some tagged value.
     *
     * @param handle The tagged value.
     * @return The found value, null if not found
     */
    public static String getValueOfTag(Object handle) {
        if (handle instanceof MTaggedValue) {
            return ((MTaggedValue) handle).getValue();
        }
	return illegalArgumentString(handle);
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
        if (base instanceof CommentEdge) {
            return (String)((CommentEdge)base).getUUID();
        }
        //
	illegalArgument(base);
	return "";
    }

    /**
     *  Return the visibility of this element
     *  @param handle an nsuml model element
     *  @return visibility
     */
    public static Object getVisibility(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getVisibility();
        }
        //
	return illegalArgumentObject(handle);
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
    public static Object lookupIn(Object handle, String name) {
        if (handle instanceof MModel)
            return ((MModel) handle).lookup(name);
        if (handle instanceof MNamespace)
            return ((MNamespace) handle).lookup(name);
        if (handle instanceof MClassifier)
            return ((MClassifier) handle).lookup(name);
	return illegalArgumentObject(handle);
    }

    ////////////////////////////////////////////////////////////////
    // Model modifying methods

    /**
     * Adds a feature to some classifier.
     * @param handle classifier
     * @param f feature
     */
    public static void addFeature(Object handle, Object f) {
        if (handle instanceof MClassifier && f instanceof MFeature) {
            ((MClassifier) handle).addFeature((MFeature) f);
            return;
        }
	illegalArgument(handle);
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
	illegalArgument(classifierRole, instance);
    }


    /**
     * Adds a feature to some classifier.
     *
     * @param handle classifier
     * @param index position
     * @param f feature
     */
    public static void addFeature(Object handle, int index, Object f) {
        if (handle instanceof MClassifier && f instanceof MFeature) {
            ((MClassifier) handle).addFeature(index, (MFeature) f);
            return;
        }
	illegalArgument(handle, f);
    }

    public static void addLink(Object handle, Object link) {
	if (handle instanceof MAssociation && link instanceof MLink) {
	    ((MAssociation) handle).addLink((MLink) link);
	    return;
	}
	illegalArgument(handle, link);
    }

    public static void addMessage3(Object handle, Object mess) {
        if (handle instanceof MMessage && mess instanceof MMessage) {
            ((MMessage) handle).addMessage3((MMessage) mess);
            return;
        }
	illegalArgument(handle, mess);
    }

    /**
     * Adds a method to some operation and copies the op's attributes
     * to the method.
     *
     * @param handle is the operation
     * @param m is the method
     */
    public static void addMethod(Object handle, Object m) {
        if (handle instanceof MOperation
            && m instanceof MMethod) {
            ((MMethod) m).setVisibility(((MOperation) handle).getVisibility());
            ((MMethod) m).setOwnerScope(((MOperation) handle).getOwnerScope());
            ((MOperation) handle).addMethod((MMethod) m);
            return;
        }
	illegalArgument(handle, m);
    }

    /**
     * Adds a model element to some namespace.
     * @param handle namespace
     * @param me model element
     */
    public static void addOwnedElement(Object handle, Object me) {
        if (handle instanceof MNamespace && me instanceof MModelElement) {
            ((MNamespace) handle).addOwnedElement((MModelElement) me);
            return;
        }
	illegalArgument(handle, me);
    }

    public static void addParameter(Object handle, Object parameter) {
        if (parameter instanceof MParameter) {
            if (handle instanceof MObjectFlowState) {
                ((MObjectFlowState) handle).addParameter(
		         (MParameter) parameter);
                return;
            }
            if (handle instanceof MEvent) {
                ((MEvent) handle).addParameter((MParameter) parameter);
                return;
            }
            if (handle instanceof MBehavioralFeature) {
                ((MBehavioralFeature) handle).addParameter(
                    (MParameter) parameter);
                return;
            }
            if (handle instanceof MClassifier) {
                ((MClassifier) handle).addParameter((MParameter) parameter);
                return;
            }
        }
	illegalArgument(handle, parameter);
    }

    public static void addParameter(
        Object handle,
        int index,
        Object parameter) {
        if (parameter instanceof MParameter) {
            if (handle instanceof MEvent) {
                ((MEvent) handle).addParameter(index, (MParameter) parameter);
                return;
            }
            if (handle instanceof MBehavioralFeature) {
                ((MBehavioralFeature) handle).addParameter(
                        index,
                        (MParameter) parameter);
                return;
            }
        }
	illegalArgument(handle, parameter);
    }

    /**
     * Adds a predecessor to a message.
     *
     * @param handle the message
     * @param predecessor is the predecessor
     */
    public static void addPredecessor(Object handle, Object predecessor) {
        if (handle != null
            && handle instanceof MMessage
            && predecessor != null
            && predecessor instanceof MMessage) {
            ((MMessage) handle).addPredecessor((MMessage) predecessor);
            return;
        }
	illegalArgument(handle, predecessor);
    }

    public static void addRaisedSignal(Object handle, Object sig) {
        if (sig instanceof MSignal) {
            if (handle instanceof MMessage) {
                ((MBehavioralFeature) handle).addRaisedSignal((MSignal) sig);
                return;
            }
            if (handle instanceof MOperation) {
                ((MOperation) handle).addRaisedSignal((MSignal) sig);
                return;
            }
        }
	illegalArgument(handle, sig);
    }

    /**
     * Adds a stimulus to a action or link
     *
     * @param handle the action or link
     * @param stimulus is the stimulus
     */
    public static void addStimulus(Object handle, Object stimulus) {
        if (handle != null
            && stimulus != null
            && stimulus instanceof MStimulus) {
            if (handle instanceof MAction) {
                ((MAction) handle).addStimulus((MStimulus) stimulus);
                return;
            }
            if (handle instanceof MLink) {
                ((MLink) handle).addStimulus((MStimulus) stimulus);
                return;
            }
        }
	illegalArgument(handle, stimulus);
    }

    public static void addSubvertex(Object handle, Object subvertex) {
        if (handle instanceof MCompositeState
            && subvertex instanceof MStateVertex) {
            ((MCompositeState) handle).addSubvertex((MStateVertex) subvertex);
            return;
        }
	illegalArgument(handle, subvertex);
    }

    /**
     * Adds a supplier classifier to some abstraction.
     *
     * @param handle abstraction
     * @param cls supplier classifier
     */
    public static void addSupplier(Object handle, Object cls) {
        if (handle instanceof MAbstraction
            && cls instanceof MClassifier) {
            ((MAbstraction) handle).addSupplier((MClassifier) cls);
            return;
        }
	illegalArgument(handle, cls);
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
	illegalArgument(supplier, dependency);
    }

    /**
     * Adds an actual argument to an action
     * @param handle the action
     * @param argument the argument
     */
    public static void addActualArgument(Object handle, Object argument) {
        if (handle instanceof MAction && argument instanceof MArgument) {
            ((MAction) handle).addActualArgument((MArgument) argument);
            return;
        }
	illegalArgument(handle, argument);
    }
    
    /**
     * Adds an annotated element to a comment.
     * @param comment The comment to which the element is annotated
     * @param annotatedElement The element to annotate
     */
    public static void addAnnotatedElement(Object comment, Object annotatedElement) {
        if (comment instanceof MComment && annotatedElement instanceof MModelElement) {
            ((MComment)comment).addAnnotatedElement(((MModelElement)annotatedElement));
            return;
        }
        illegalArgument(comment, annotatedElement);
    }

    /**
     * This method adds a classifier to a classifier role.
     *
     * @param handle is the classifier role
     * @param c is the classifier
     */
    public static void addBase(Object handle, Object c) {
        if (handle instanceof MClassifierRole
                && c instanceof MClassifier) {
            ((MClassifierRole) handle).addBase((MClassifier) c);
	    return;
        }
	illegalArgument(handle, c);
    }

    public static void addClassifier(Object handle, Object classifier) {
        if (handle instanceof MInstance && classifier instanceof MClassifier) {
            ((MInstance) handle).addClassifier((MClassifier) classifier);
	    return;
        }
	illegalArgument(handle, classifier);
    }

    /**
     * Adds a client classifier to some abstraction.
     *
     * @param handle abstraction
     * @param cls client classifier
     */
    public static void addClient(Object handle, Object cls) {
        if (handle instanceof MAbstraction
                && cls instanceof MClassifier) {
            ((MAbstraction) handle).addClient((MClassifier) cls);
	    return;
        }
	illegalArgument(handle, cls);
    }

    /**
     * Adds a client dependency to some modelelement
     *
     * @param handle the modelelement
     * @param dependency the dependency
     */
    public static void addClientDependency(Object handle, Object dependency) {
        if (isAModelElement(handle)
                && isADependency(dependency)) {
            MModelElement me = (MModelElement) handle;
            me.addClientDependency((MDependency) dependency);
            return;
        }
	illegalArgument(handle, dependency);
    }

    public static void addTaggedValue(Object handle, Object taggedValue) {
        if (isAModelElement(handle) && isATaggedValue(taggedValue)) {
            ((MModelElement) handle).addTaggedValue((MTaggedValue) taggedValue);
            return;
        }
	illegalArgument(handle, taggedValue);
    }

    public static void removeActualArgument(Object handle, Object argument) {
        if (handle instanceof MAction && argument instanceof MArgument) {
            ((MAction) handle).removeActualArgument((MArgument) argument);
	    return;
        }
	illegalArgument(handle, argument);
    }

    /**
     * This method removes a classifier from a classifier role.
     *
     * @param handle is the classifier role
     * @param c is the classifier
     */
    public static void removeBase(Object handle, Object c) {
        if (handle instanceof MClassifierRole
                && c instanceof MClassifier) {
            ((MClassifierRole) handle).removeBase((MClassifier) c);
	    return;
        }
	illegalArgument(handle, c);
    }

    /**
     * This method removes a dependency from a model element.
     *
     * @param handle is the model element
     * @param dep is the dependency
     */
    public static void removeClientDependency(Object handle, Object dep) {
        if (handle instanceof MModelElement
                && dep instanceof MDependency) {
            ((MModelElement) handle).removeClientDependency((MDependency) dep);
            return;
        }
	illegalArgument(handle, dep);
    }

    public static void removeConstraint(Object handle, Object cons) {
        if (handle instanceof MModelElement && cons instanceof MConstraint) {
            ((MModelElement) handle).removeConstraint((MConstraint) cons);
            return;
        }
	illegalArgument(handle, cons);
    }

    public static void removeContext(Object handle, Object context) {
        if (handle instanceof MSignal
            && context instanceof MBehavioralFeature) {
            ((MSignal) handle).removeContext((MBehavioralFeature) context);
            return;
        }
	illegalArgument(handle, context);
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
	illegalArgument(handle, classifier);
    }

    /**
     * This method removes a feature from a classifier.
     *
     * @param handle is the classifier
     * @param feature to remove
     */
    public static void removeFeature(Object handle, Object feature) {
        if (handle instanceof MClassifier
                && feature instanceof MFeature) {
            ((MClassifier) handle).removeFeature((MFeature) feature);
            return;
        }
	illegalArgument(handle, feature);
    }

    /**
     * This method removes an extension point from a use case.
     *
     * @param uc is the use case
     * @param ep is the extension point
     */
    public static void removeExtensionPoint(Object uc, Object ep) {
        if (uc instanceof MUseCase
                && ep instanceof MExtensionPoint) {
            ((MUseCase) uc).removeExtensionPoint((MExtensionPoint) ep);
            return;
        }
	illegalArgument(uc, ep);
    }

    public static void removeMessage3(Object handle, Object mess) {
        if (handle instanceof MMessage && mess instanceof MMessage) {
            ((MMessage) handle).removeMessage3((MMessage) mess);
            return;
        }
	illegalArgument(handle, mess);
    }

    /**
     * Removes a owned model element from a namespace.
     *
     * @param handle is the name space
     * @param value is the model element
     */
    public static void removeOwnedElement(Object handle, Object value) {
        if (handle instanceof MNamespace
                && value instanceof MModelElement) {
            ((MNamespace) handle).removeOwnedElement((MModelElement) value);
            return;
        }
	illegalArgument(handle, value);
    }

    /**
     * This method removes a parameter from an operation.
     *
     * @param handle is the operation
     * @param p is the parameter
     */
    public static void removeParameter(Object handle, Object p) {
        if (handle instanceof MOperation
            && p instanceof MParameter) {
            ((MOperation) handle).removeParameter((MParameter) p);
            return;
        }
	illegalArgument(handle, p);
    }

    public static void removePredecessor(Object handle, Object message) {
        if (handle instanceof MMessage && message instanceof MMessage) {
            ((MMessage) handle).removePredecessor((MMessage) message);
            return;
        }
	illegalArgument(handle, message);
    }

    public static void removeReception(Object handle, Object reception) {
        if (handle instanceof MSignal && reception instanceof MReception) {
            ((MSignal) handle).removeReception((MReception) reception);
            return;
        }
	illegalArgument(handle, reception);
    }

    public static void removeSubvertex(Object handle, Object subvertex) {
        if (handle instanceof MCompositeState
            && subvertex instanceof MStateVertex) {
            ((MCompositeState) handle).removeSubvertex(
		    (MStateVertex) subvertex);
            return;
        }
	illegalArgument(handle, subvertex);
    }

    /**
     * Removes a named tagged value from a model element, ie subsequent calls
     * to getTaggedValue will return null for name, at least until a tagged
     * value with that name has been added again.
     *
     * @param handle the model element to remove the tagged value from
     * @param name the name of the tagged value
     * @throws IllegalArgumentException if handle isn't a model element
     */
    public static void removeTaggedValue(Object handle, String name) {
	if (handle instanceof MModelElement) {
	    MModelElement me = (MModelElement) handle;
	    me.removeTaggedValue(name);
	    return;
	}

	illegalArgument(handle);
    }

    /**
     * Set the base of some model element.
     *
     * @param handle is the model element
     * @param base is the base
     */
    public static void setBase(Object handle, Object base) {
	checkExists(handle);
	checkExists(base);

        if (handle instanceof MAssociationRole
            && base instanceof MAssociation) {
            ((MAssociationRole) handle).setBase((MAssociation) base);
            return;
        }
        if (handle instanceof MAssociationEndRole
            && base instanceof MAssociationEnd) {
            ((MAssociationEndRole) handle).setBase((MAssociationEnd) base);
            return;
        }
        if (handle instanceof MExtend && base instanceof MUseCase) {
            ((MExtend) handle).setBase((MUseCase) base);
            return;
        }
        if (handle instanceof MInclude && base instanceof MUseCase) {
            ((MInclude) handle).setAddition((MUseCase) base);
            return;
        }
	illegalArgument(handle, base);
    }

    /**
     * Set the baseclass of some stereotype.
     *
     * @param handle the stereotype
     * @param baseClass the baseclass
     */
    public static void setBaseClass(Object handle, Object baseClass) {
        if (isAStereotype(handle) && baseClass instanceof String) {
            ((MStereotype) handle).setBaseClass((String) baseClass);
            return;
        }
	illegalArgument(handle, baseClass);
    }

    /**
     * Sets a body of some method or expression.
     *
     * @param handle is the method, expression
     * @param expr is the body string for the expression
     */
    public static void setBody(Object handle, Object expr) {
        if (handle instanceof MMethod
            && (expr == null || expr instanceof MProcedureExpression)) {
            ((MMethod) handle).setBody((MProcedureExpression) expr);
            return;
        }

        if (handle instanceof MConstraint
            && (expr == null || expr instanceof MBooleanExpression)) {
            ((MConstraint) handle).setBody((MBooleanExpression) expr);
            return;
        }

        /* TODO: MVW: The next part is fooling the user of setBody()
         * in thinking that the body of the object is changed.
         * Instead, a new object is created as a side-effect.
         * There is no other way: a MExpression can not be altered,
         * once created! */
        if (handle instanceof MExpression) {
            MExpressionEditor expressionEditor =
		(MExpressionEditor) UmlFactory.getFactory().getDataTypes()
		    .createExpressionEditor(handle);
            expressionEditor.setBody((String) expr);
            handle = (Object) expressionEditor.toExpression();
            // this last step creates a new MExpression
            return;
        }
	illegalArgument(handle, expr);
    }

    /**
     * Sets the language of an expression.
     *
     * TODO: This operation is fooling the user
     * in thinking that the body of the object is changed.
     * Instead, a new object is created as a side-effect.
     * There is no other way: a MExpression can not be altered,
     * once created!
     * So, this operation should return the created object instead!
     *
     * @param handle is the expression
     * @param expr is the lang
     *
     * TODO: Rename the expr parameter to something a little less error-prone.
     */
    public static void setLanguage(Object handle, String expr) {
        if (handle instanceof MExpression) {
            MExpression expression = (MExpression) handle;
            MExpressionEditor expressionEditor = (MExpressionEditor)
                UmlFactory.getFactory().getDataTypes().
                    createExpressionEditor(handle);
            expressionEditor.setLanguage(expr);
            handle = expressionEditor.toExpression();

            return;
        }
	illegalArgument(handle, expr);
    }

    /**
     * Gets the language attribute of an Expression.
     *
     * @param handle is the Expression of which the language is retrieved
     */
    public static String getLanguage(Object handle) {
        if (handle instanceof MExpression) {
            return ((MExpression) handle).getLanguage();
        }
	return illegalArgumentString(handle);
    }

    /**
     * Sets a default value of some parameter.
     *
     * @param handle is the parameter
     * @param expr is the expression
     */
    public static void setDefaultValue(Object handle, Object expr) {
        if (handle instanceof MParameter && expr instanceof MExpression) {
            ((MParameter) handle).setDefaultValue((MExpression) expr);
            return;
        }
	illegalArgument(handle, expr);
    }

    /**
     * Sets the guard of a transition.
     *
     * @param handle to the transition
     * @param guard to be set. Can be null.
     */
    public static void setGuard(Object handle, Object guard) {
        if (handle instanceof MTransition
                && (guard == null || guard instanceof MGuard)) {
            ((MTransition) handle).setGuard((MGuard) guard);
            return;
        }
	illegalArgument(handle, guard);
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
	illegalArgument(handle, trans);
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
	illegalArgument(handle, event);
    }

    public static void setIcon(Object handle, Object icon) {
        if (handle instanceof MStereotype
                && (icon == null || icon instanceof String)) {
            ((MStereotype) handle).setIcon((String) icon);
            return;
        }
	illegalArgument(handle, icon);
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
	illegalArgument(handle, component);
    }

    public static void setIncludes(Object handle, Collection includes) {
        if (handle instanceof MUseCase) {
            ((MUseCase) handle).setIncludes(includes);
            return;
        }
	illegalArgument(handle, includes);
    }

    /**
     * Sets an initial value.
     *
     * @param at attribute that we set the initial value of
     * @param expr that is the value to set. Can be <tt>null</tt>.
     */
    public static void setInitialValue(Object at, Object expr) {
        if (at instanceof MAttribute
                && (expr == null || expr instanceof MExpression)) {
	    ((MAttribute) at).setInitialValue((MExpression) expr);
	    return;
	}
	illegalArgument(at, expr);
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
	illegalArgument(handle, inst);
    }

    /**
     * @param handle is the target.
     * @param intTrans is a collection of transitions.
     */
    public static void setInternalTransitions(
        Object handle,
        Collection intTrans) {
        if (handle instanceof MState) {
            ((MState) handle).setInternalTransitions(intTrans);
            return;
        }
	illegalArgument(handle);
    }

    public static void setMessages3(Object handle, Collection messages) {
        if (handle instanceof MMessage) {
            ((MMessage) handle).setMessages3(messages);
            return;
        }
	illegalArgument(handle);
    }

    /**
     * Sets a location of some extension point.
     *
     * @param handle is the extension point
     * @param loc is the location
     */
    public static void setLocation(Object handle, String loc) {
        if (handle instanceof MExtensionPoint) {
            ((MExtensionPoint) handle).setLocation(loc);
            return;
        }
	illegalArgument(handle);
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
        Object container) {
        if (handle instanceof MPartition
            && container instanceof MActivityGraph) {
            ((MPartition) handle).setActivityGraph((MActivityGraph) container);
        } else if (handle instanceof MModelElement &&
                container instanceof MPartition) {
            	((MPartition) container).addContents((MModelElement)handle);
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
	    illegalArgument(handle, container);
	}
    }

    /**
     * Sets a multiplicity of some model element.
     *
     * @param handle model element
     * @param arg multiplicity as string OR multiplicity object
     */
    public static void setMultiplicity(Object handle, Object arg) {
        if (arg instanceof String) {
            arg =
                ("1_N".equals(arg)) ? MMultiplicity.M1_N : MMultiplicity.M1_1;
        }

	if (arg instanceof MMultiplicity) {
	    MMultiplicity mult = (MMultiplicity) arg;

	    if (handle instanceof MAssociationRole) {
		((MAssociationRole) handle).setMultiplicity(mult);
		return;
	    }
	    if (handle instanceof MClassifierRole) {
		((MClassifierRole) handle).setMultiplicity(mult);
		return;
	    }
	    if (handle instanceof MStructuralFeature) {
		((MStructuralFeature) handle).setMultiplicity(mult);
		return;
	    }
	    if (handle instanceof MAssociationEnd) {
		((MAssociationEnd) handle).setMultiplicity(mult);
		return;
	    }
	}
	illegalArgument(handle, arg);
    }

    /**
     * Sets the classifiers of some instance.
     *
     * @param handle is the instance
     * @param v is the classifier vector
     */
    public static void setClassifiers(Object handle, Vector v) {
        if (handle instanceof MInstance) {
            ((MInstance) handle).setClassifiers(v);
            return;
        }
	illegalArgument(handle);
    }

    /**
     * Sets a name of some modelelement.
     *
     * @param handle is the model element
     * @param name to set
     */
    public static void setName(Object handle, String name) {
        if (handle instanceof MModelElement) {
            ((MModelElement) handle).setName(name);
            return;
        }
	illegalArgument(handle);
    }

    /**
     * Sets a namespace of some modelelement.
     *
     * @param handle is the model element
     * @param ns is the namespace. Can be <tt>null</tt>.
     */
    public static void setNamespace(Object handle, Object ns) {
        if (handle instanceof MModelElement
            && (ns == null || ns instanceof MNamespace)) {
            ((MModelElement) handle).setNamespace((MNamespace) ns);
            return;
        }
	illegalArgument(handle, ns);
    }

    /**
     * Sets the navigability of some association end.
     *
     * @param handle is the association end
     * @param flag is the navigability flag
     */
    public static void setNavigable(Object handle, boolean flag) {
        if (handle instanceof MAssociationEnd) {
            ((MAssociationEnd) handle).setNavigable(flag);
            return;
        }
	illegalArgument(handle);
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
	illegalArgument(handle);
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
	illegalArgument(handle, visibility);
    }

    /**
     * Set the visibility of some modelelement.
     *
     * @param handle is the model element
     * @param v is the visibility
     */
    public static void setVisibility(Object handle, short v) {
        if (handle instanceof MModelElement) {
	    MModelElement me = (MModelElement) handle;

            if (v == ACC_PRIVATE) {
                me.setVisibility(MVisibilityKind.PRIVATE);
            } else if (v == ACC_PROTECTED) {
                me.setVisibility(MVisibilityKind.PROTECTED);
            } else if (v == ACC_PUBLIC) {
                me.setVisibility(MVisibilityKind.PUBLIC);
            }
            return;
        }
	illegalArgument(handle);
    }

    public static void setNodeInstance(Object handle, Object nodeInstance) {
        if (handle instanceof MComponentInstance
	    && nodeInstance instanceof MNodeInstance) {
            ((MComponentInstance) handle).setNodeInstance(
                (MNodeInstance) nodeInstance);
            return;
        }
	illegalArgument(handle, nodeInstance);
    }

    public static void setOwner(Object handle, Object owner) {
        if (handle instanceof MFeature
            && (owner == null || owner instanceof MClassifier)) {
            ((MFeature) handle).setOwner((MClassifier) owner);
            return;
        }
	illegalArgument(handle, owner);
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
	illegalArgument(handle, operation);
    }

    public static void setOrdering(Object handle, Object ok) {
        if (handle instanceof MAssociationEnd && ok instanceof MOrderingKind) {
            ((MAssociationEnd) handle).setOrdering((MOrderingKind) ok);
            return;
        }
	illegalArgument(handle, ok);
    }

    /**
     * Set the owner scope of some feature.
     *
     * @param handle is the feature
     * @param os is the owner scope
     */
    public static void setOwnerScope(Object handle, short os) {
        if (handle instanceof MFeature) {
            if (os == CLASSIFIER_SCOPE) {
                ((MFeature) handle).setOwnerScope(MScopeKind.CLASSIFIER);
                return;
            } else if (os == INSTANCE_SCOPE) {
                ((MFeature) handle).setOwnerScope(MScopeKind.INSTANCE);
                return;
            }
        }
	illegalArgument(handle);
    }

    public static void setOwnerScope(Object handle, Object os) {
        if (handle instanceof MFeature
            && (os == null || os instanceof MScopeKind)) {
            ((MFeature) handle).setOwnerScope((MScopeKind) os);
            return;
        }
	illegalArgument(handle, os);
    }

    /**
     * Sets the extension points of some use cases.
     *
     * @param handle the use case
     * @param parameters is a Collection of extensionPoints
     */
    public static void setParameters(Object handle, Collection parameters) {
        if (handle instanceof MObjectFlowState) {
            ((MObjectFlowState) handle).setParameters(parameters);
            return;
        }
        if (handle instanceof MClassifier) {
            ((MClassifier) handle).setParameters(parameters);
            return;
        }
        if (handle instanceof MEvent && parameters instanceof List) {
            ((MEvent) handle).setParameters((List) parameters);
            return;
        }
        if (handle instanceof MBehavioralFeature
            && parameters instanceof List) {
            ((MBehavioralFeature) handle).setParameters((List) parameters);
            return;
        }
	illegalArgument(handle, parameters);
    }

    /**
     * Sets the target of some action or transition.
     *
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
            && element instanceof MStateVertex) {
            ((MTransition) handle).setTarget((MStateVertex) element);
            return;
        }
	illegalArgument(handle, element);
    }
    /**
     * Sets the state of an internal transition.
     *
     * @param handle the internal transition
     * @param element the state that contains this transition
     */
    public static void setState(Object handle, Object element) {
        if (handle instanceof MTransition
            && element instanceof MState) {
            ((MTransition) handle).setState((MState) element);
            return;
        }
        illegalArgument(handle, element);
    }

    /**
     * Set the target scope of some association end or structural feature.
     *
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
	illegalArgument(handle, scopeKind);
    }

    /**
     * Set the target scope of some association end.
     *
     * @param handle is the association end
     * @param ts is the target scope
     */
    public static void setTargetScope(Object handle, short ts) {
        if (handle instanceof MAssociationEnd) {
	    MAssociationEnd ae = (MAssociationEnd) handle;

            if (ts == CLASSIFIER_SCOPE) {
		ae.setTargetScope(MScopeKind.CLASSIFIER);
                return;
            } else if (ts == INSTANCE_SCOPE) {
		ae.setTargetScope(MScopeKind.INSTANCE);
                return;
            }
            return;
        }
	illegalArgument(handle);
    }

    public static void setComponentInstance(Object handle, Object c) {
        if (handle instanceof MInstance
	    && (c == null || c instanceof MComponentInstance)) {
            ((MInstance) handle).setComponentInstance((MComponentInstance) c);
            return;
        }
	illegalArgument(handle, c);
    }

    /**
     * Sets the communicationLink between a link c and a stimulus handle.
     *
     * @param handle the stimulus
     * @param c the link
     */
    public static void setCommunicationLink(Object handle, Object c) {
        if (handle instanceof MStimulus && c instanceof MLink) {
            ((MStimulus) handle).setCommunicationLink((MLink) c);
            return;
        }
	illegalArgument(handle, c);
    }

    /**
     * Set the concurrency of some operation.
     *
     * @param handle is the operation
     * @param c is the concurrency
     */
    public static void setConcurrency(Object handle, short c) {
        if (handle instanceof MOperation) {
            MOperation oper = (MOperation) handle;

            if (c == GUARDED) {
                oper.setConcurrency(MCallConcurrencyKind.GUARDED);
                return;
            } else if (c == SEQUENTIAL) {
                oper.setConcurrency(MCallConcurrencyKind.SEQUENTIAL);
            }
            return;
        }
	illegalArgument(handle);
    }

    /**
     * Set the concurrency of some operation.
     *
     * @param handle is the operation
     * @param concurrencyKind is the concurrency
     */
    public static void setConcurrency(
        Object handle,
        Object concurrencyKind) {
        if (handle instanceof MOperation
            && concurrencyKind instanceof MCallConcurrencyKind) {
            ((MOperation) handle).setConcurrency(
                (MCallConcurrencyKind) concurrencyKind);
            return;
        }
	illegalArgument(handle, concurrencyKind);
    }

    public static void setConcurent(Object handle, boolean concurrent) {
        if (handle instanceof MCompositeState) {
            ((MCompositeState) handle).setConcurent(concurrent);
            return;
        }
	illegalArgument(handle);
    }

    /**
     * Set the condition of an extend
     *
     * @param handle is the extend
     * @param booleanExpression is the condition
     */
    public static void setCondition(Object handle, Object booleanExpression) {
        if (handle instanceof MExtend
            && booleanExpression instanceof MBooleanExpression) {
            ((MExtend) handle).setCondition(
                (MBooleanExpression) booleanExpression);
            return;
        }
	illegalArgument(handle, booleanExpression);
    }

    /**
     * Set the container of a statevertex.
     *
     * @param handle is the stateVertex
     * @param compositeState is the container. Can be <tt>null</tt>.
     */
    public static void setContainer(Object handle, Object compositeState) {
        if (handle instanceof MStateVertex
            && (compositeState == null
                || compositeState instanceof MCompositeState)) {
            ((MStateVertex) handle).setContainer(
                (MCompositeState) compositeState);
            return;
        }
	illegalArgument(handle, compositeState);
    }

    public static void setContexts(Object handle, Collection c) {
        if (handle instanceof MSignal) {
            ((MSignal) handle).setContexts(c);
            return;
        }
	illegalArgument(handle);
    }

    /**
     * Sets the dispatch action for some stimulus.
     *
     * @param handle the stimulus
     * @param value the action. Can be <tt>null</tt>.
     */
    public static void setDispatchAction(Object handle, Object value) {
        if (handle instanceof MStimulus
            && (value == null || value instanceof MAction)) {
            ((MStimulus) handle).setDispatchAction((MAction) value);
            return;
        }
	illegalArgument(handle, value);
    }

    /**
     * Sets the do activity of a state
     *
     * @param handle is the state
     * @param value the activity. Can be <tt>null</tt>.
     */
    public static void setDoActivity(Object handle, Object value) {
        if (handle instanceof MState
            && (value == null || value instanceof MAction)) {
            ((MState) handle).setDoActivity((MAction) value);
            return;
        }
	illegalArgument(handle, value);
    }

    /**
     * Sets the effect of some transition
     *
     * @param handle is the transition
     * @param value is the effect. Can be <tt>null</tt>.
     */
    public static void setEffect(Object handle, Object value) {
        if (handle instanceof MTransition
            && (value == null || value instanceof MAction)) {
            ((MTransition) handle).setEffect((MAction) value);
            return;
        }
	illegalArgument(handle, value);
    }

    /**
     * Sets the entry action of some state.
     *
     * @param handle is the state
     * @param value is the action. Can be <tt>null</tt>.
     */
    public static void setEntry(Object handle, Object value) {
        if (handle instanceof MState
            && (value == null || value instanceof MAction)) {
            ((MState) handle).setEntry((MAction) value);
            return;
        }
	illegalArgument(handle, value);
    }

    /**
     * Sets the exit action of some state
     *
     * @param handle is the state
     * @param value is the action. Can be <tt>null</tt>.
     */
    public static void setExit(Object handle, Object value) {
        if (handle instanceof MState
            && (value == null || value instanceof MAction)) {
            ((MState) handle).setExit((MAction) value);
            return;
        }
	illegalArgument(handle, value);
    }

    public static void setExpression(Object handle, Object value) {
        if (handle instanceof MGuard
            && (value == null || value instanceof MBooleanExpression)) {
            ((MGuard) handle).setExpression((MBooleanExpression) value);
            return;
        }
        if (handle instanceof MChangeEvent
            && (value == null || value instanceof MBooleanExpression)) {
	    MChangeEvent ce = (MChangeEvent) handle;
	    ce.setChangeExpression((MBooleanExpression) value);
            return;
        }
        illegalArgument(handle, value);
    }

    /**
     * Sets the time-expression for a TimeEvent. 
     * @param handle Object (MTimeEvent)
     * @param value Object (MTimeExpression)
     */
    public static void setWhen(Object handle, Object value) {
        if (handle instanceof MTimeEvent
            && (value == null || value instanceof MTimeExpression)) {
            ((MTimeEvent) handle).setWhen((MTimeExpression) value);
            return;
        }
        illegalArgument(handle, value);
    }

    public static void setExtension(Object handle, Object ext) {
	checkExists(handle);
	checkExists(ext);

        if (handle instanceof MExtend
            && (ext == null || ext instanceof MUseCase)) {
            ((MExtend) handle).setExtension((MUseCase) ext);
            return;
        }
	illegalArgument(handle, ext);
    }

    /**
     * Sets the extension points of some use cases.
     *
     * @param handle the use case
     * @param extensionPoints is the extension points
     */
    public static void setExtensionPoints(
        Object handle,
        Collection extensionPoints) {
        if (handle instanceof MUseCase && extensionPoints instanceof List) {
            ((MUseCase) handle).setExtensionPoints(extensionPoints);
            return;
        }
	illegalArgument(handle, extensionPoints);
    }

    /**
     * Sets the features of some model element.
     *
     * @param handle the model element to set features to
     * @param features the list of features
     */
    public static void setFeatures(Object handle, Collection features) {
        if (handle instanceof MClassifier
            && features instanceof List) {
            ((MClassifier) handle).setFeatures((List) features);
            return;
        }
	illegalArgument(handle);
    }

    /**
     * Sets the aggregation of some model element.
     *
     * @param handle the model element to set aggregation
     * @param aggregationKind the aggregation kind
     */
    public static void setAggregation(Object handle, Object aggregationKind) {
        if (handle instanceof MAssociationEnd
            && aggregationKind instanceof MAggregationKind) {
            ((MAssociationEnd) handle).setAggregation(
                (MAggregationKind) aggregationKind);
                return;
        }
	illegalArgument(handle, aggregationKind);
    }

    /**
     * Sets the association of some model element.
     *
     * @param handle the model element to set association
     * @param association is the association
     */
    public static void setAssociation(Object handle, Object association) {
        if (association instanceof MAssociation) {
            if (handle instanceof MAssociationEnd) {
                ((MAssociationEnd) handle).setAssociation(
                    (MAssociation) association);
                return;
            }
            if (handle instanceof MLink) {
                ((MLink) handle).setAssociation((MAssociation) association);
                return;
            }
        }
	illegalArgument(handle, association);
    }

    public static void setChangeability(Object handle, Object ck) {
        if (ck == null || ck instanceof MChangeableKind) {
	    MChangeableKind changeableKind = (MChangeableKind) ck;

            if (handle instanceof MStructuralFeature) {
                ((MStructuralFeature) handle).setChangeability(changeableKind);
                return;
            }
            if (handle instanceof MAssociationEnd) {
                ((MAssociationEnd) handle).setChangeability(changeableKind);
                return;
            }
        }
	illegalArgument(handle, ck);
    }

    /**
     * Set the changeability of some feature.
     *
     * @param handle is the feature
     * @param flag is the changeability flag
     */
    public static void setChangeable(Object handle, boolean flag) {
        // FIXME: the implementation is ugly, because I have no spec
        // at hand...
        if (handle instanceof MStructuralFeature) {
            if (flag) {
                ((MStructuralFeature) handle).setChangeability(
                    MChangeableKind.CHANGEABLE);
                    return;
            }
            else {
                ((MStructuralFeature) handle).setChangeability(
                    MChangeableKind.FROZEN);
            return;
            }
        } else if (handle instanceof MAssociationEnd) {
            MAssociationEnd ae = (MAssociationEnd) handle;
            if (flag)
                ae.setChangeability(MChangeableKind.CHANGEABLE);
            else
                ae.setChangeability(MChangeableKind.FROZEN);
            return;
        }
	illegalArgument(handle);
    }

    public static void setChild(Object handle, Object child) {
        if (handle instanceof MGeneralization) {
            ((MGeneralization) handle).setChild((MGeneralizableElement) child);
            return;
        }
	illegalArgument(handle, child);
    }

    /**
     * Sets if of some model element is abstract.
     *
     * @param handle is the classifier
     * @param flag is true if it should be abstract
     */
    public static void setAbstract(Object handle, boolean flag) {

	if (handle instanceof MGeneralizableElement) {
	    ((MGeneralizableElement) handle).setAbstract(flag);
	    return;
	}
	if (handle instanceof MOperation) {
	    ((MOperation) handle).setAbstract(flag);
	    return;
	}
	if (handle instanceof MReception) {
	    ((MReception) handle).setAbstarct(flag);
	}
	illegalArgument(handle);
    }

    /**
     * Sets the addition to an include.
     * There is a bug in NSUML that reverses additions and bases for includes.
     * @param handle
     * @param useCase
     */
    public static void setAddition(Object handle, Object useCase) {
	checkExists(handle);
	checkExists(useCase);

        if (handle instanceof MInclude) {
            ((MInclude) handle).setBase((MUseCase) useCase);
            return;
        }
	illegalArgument(handle);
    }

    /**
     * Sets the action to a message
     *
     * @param handle is the message
     * @param action is the action
     */
    public static void setAction(Object handle, Object action) {
        if (handle instanceof MMessage
            && (action == null || action instanceof MAction)) {
            ((MMessage) handle).setAction((MAction) action);
            return;
        }
	illegalArgument(handle, action);
    }

    public static void setActivator(Object handle, Object message) {
        if (handle instanceof MMessage
            && (message == null || message instanceof MMessage)) {
            ((MMessage) handle).setActivator((MMessage) message);
            return;
        }
	illegalArgument(handle, message);
    }

    public static void setActive(Object handle, boolean active) {
        if (handle instanceof MClass) {
            ((MClass) handle).setActive(active);
            return;
        }
	illegalArgument(handle);
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
	illegalArgument(handle);
    }

    /**
     * Sets if some model element is a leaf.
     *
     * @param handle model element
     * @param flag is true if it is a leaf.
     */
    public static void setLeaf(Object handle, boolean flag) {
        if (handle instanceof MReception) {
            ((MReception) handle).setLeaf(flag);
            return;
        }
        if (handle instanceof MOperation) {
            ((MOperation) handle).setLeaf(flag);
            return;
        }
        if (handle instanceof MGeneralizableElement) {
            ((MGeneralizableElement) handle).setLeaf(flag);
            return;
        }
	illegalArgument(handle);
    }

    /**
     * Sets the raised signals of some behavioural feature.
     *
     * @param handle the behavioural feature
     * @param raisedSignals the raised signals
     */
    public static void setRaisedSignals(
        Object handle,
        Collection raisedSignals) {
        if (handle instanceof MBehavioralFeature) {
            ((MBehavioralFeature) handle).setRaisedSignals(raisedSignals);
            return;
        }
	illegalArgument(handle);
    }

    /**
     * Sets the receiver of some model element.
     * @param handle model element
     * @param receiver the receiver
     */
    public static void setReceiver(Object handle, Object receiver) {
        if (handle instanceof MMessage
            && receiver instanceof MClassifierRole) {
            ((MMessage) handle).setReceiver((MClassifierRole) receiver);
            return;
        }
        if (handle instanceof MStimulus && receiver instanceof MInstance) {
            ((MStimulus) handle).setReceiver((MInstance) receiver);
            return;
        }
	illegalArgument(handle, receiver);
    }

    public static void setRecurrence(Object handle, Object expr) {
        if (handle instanceof MAction
            && expr instanceof MIterationExpression) {
            ((MAction) handle).setRecurrence((MIterationExpression) expr);
            return;
        }
	illegalArgument(handle, expr);
    }

    /**
     * Sets the represented classifier of some collaboration
     *
     * @param handle the collaboration
     * @param classifier is the classifier
     */
    public static void setRepresentedClassifier(
        Object handle,
        Object classifier) {
        if (handle instanceof MCollaboration
            && classifier instanceof MClassifier) {
            ((MCollaboration) handle).setRepresentedClassifier(
                (MClassifier) classifier);
            return;
        }
	illegalArgument(handle, classifier);
    }

    /**
     * Sets the represented operation of some collaboration
     *
     * @param handle the collaboration
     * @param operation is the operation
     */
    public static void setRepresentedOperation(
        Object handle,
        Object operation) {
        if (handle instanceof MCollaboration
            && operation instanceof MOperation) {
            ((MCollaboration) handle).setRepresentedOperation(
                (MOperation) operation);
            return;
        }
	illegalArgument(handle, operation);
    }

    public static void setResident(Object handle, Object resident) {
        if (handle instanceof MElementResidence
            && (resident == null || resident instanceof MModelElement)) {
            ((MElementResidence) handle).setResident((MModelElement) resident);
            return;
        }
	illegalArgument(handle, resident);
    }

    /**
     * Sets the residents of some model element.
     *
     * @param handle the model element
     * @param residents collection
     */
    public static void setResidents(Object handle, Collection residents) {
        if (handle instanceof MNodeInstance) {
            ((MNodeInstance) handle).setResidents(residents);
            return;
        }
        if (handle instanceof MComponentInstance) {
            ((MComponentInstance) handle).setResidents(residents);
            return;
        }
        if (handle instanceof MNode) {
            ((MNode) handle).setResidents(residents);
            return;
        }
	illegalArgument(handle);
    }

    /**
     * Sets if some model element is a root.
     *
     * @param handle model element
     * @param flag is true if it is a root
     */
    public static void setRoot(Object handle, boolean flag) {
        if (handle instanceof MReception) {
            ((MReception) handle).setRoot(flag);
            return;
        }
        if (handle instanceof MOperation) {
            ((MOperation) handle).setRoot(flag);
            return;
        }
        if (handle instanceof MGeneralizableElement) {
            ((MGeneralizableElement) handle).setRoot(flag);
            return;
        }
	illegalArgument(handle);
    }

    public static void setScript(Object handle, Object expr) {
        if (handle instanceof MAction
            && (expr == null || expr instanceof MActionExpression)) {
            ((MAction) handle).setScript((MActionExpression) expr);
            return;
        }
	illegalArgument(handle, expr);
    }

    /**
     * Sets the sender of some model element.<p>
     *
     * @param handle model element
     * @param sender the sender
     */
    public static void setSender(Object handle, Object sender) {
        if (handle instanceof MMessage && sender instanceof MClassifierRole) {
            ((MMessage) handle).setSender((MClassifierRole) sender);
            return;
        }
        if (handle instanceof MStimulus && sender instanceof MInstance) {
            ((MStimulus) handle).setSender((MInstance) sender);
            return;
        }
	illegalArgument(handle, sender);
    }

    public static void setSignal(Object handle, Object signal) {
        if (signal == null || signal instanceof MSignal) {
            if (handle instanceof MSendAction) {
                ((MSendAction) handle).setSignal((MSignal) signal);
                return;
            }
            if (handle instanceof MReception) {
                ((MReception) handle).setSignal((MSignal) signal);
                return;
            }
            if (handle instanceof MSignalEvent) {
                ((MSignalEvent) handle).setSignal((MSignal) signal);
                return;
            }
        }
	illegalArgument(handle, signal);
    }

    /**
     * Sets the source state of some message.
     *
     * @param handle the message
     * @param state the source state
     */
    public static void setSource(Object handle, Object state) {
	if (handle instanceof MTransition && state instanceof MStateVertex) {
	    ((MTransition) handle).setSource((MStateVertex) state);
	    return;
	}
	illegalArgument(handle, state);
    }

    public static void setSources(Object handle, Collection specifications) {
        if (handle instanceof MFlow) {
            ((MFlow) handle).setSources(specifications);
            return;
        }
	illegalArgument(handle);
    }
    
    /**
     * 
     * @param handle a reception
     * @param specification the specification
     */
    public static void setSpecification(Object handle, String specification) {
        if (handle instanceof MReception) {
            ((MReception) handle).setSpecification(specification);
            return;
        }
        illegalArgument(handle);
    }

    public static void setSpecification(Object handle, boolean specification) {
        if (handle instanceof MModelElement) {
            ((MModelElement) handle).setSpecification(specification);
            return;
        }
	illegalArgument(handle);
    }

    /**
     * Sets the specifications of some association end.
     *
     * @param handle the association end
     * @param specifications collection
     */
    public static void setSpecifications(
        Object handle,
        Collection specifications) {
        if (handle instanceof MAssociationEnd) {
            ((MAssociationEnd) handle).setSpecifications(specifications);
            return;
        }
	illegalArgument(handle);
    }

    /**
     * Set some parameters kind.
     *
     * @param handle is the parameter
     * @param kind is the directionkind
     */
    public static void setKind(Object handle, Object kind) {
        if (handle instanceof MParameter
            && kind instanceof MParameterDirectionKind) {
            ((MParameter) handle).setKind((MParameterDirectionKind) kind);
            return;
        }
        if (handle instanceof MPseudostate
            && kind instanceof MPseudostateKind) {
            ((MPseudostate) handle).setKind((MPseudostateKind) kind);
            return;
        }
	illegalArgument(handle, kind);
    }

    /**
     * Set some parameters kind to 'in'.
     *
     * @param handle is the parameter
     */
    public static void setKindToIn(Object handle) {
        if (handle instanceof MParameter) {
	    ((MParameter) handle).setKind(MParameterDirectionKind.IN);
	    return;
        }
	illegalArgument(handle);
    }

    /**
     * Set some parameters kind to 'in/out'.
     *
     * @param handle is the parameter
     */
    public static void setKindToInOut(Object handle) {
        if (handle instanceof MParameter) {
	    ((MParameter) handle).setKind(MParameterDirectionKind.INOUT);
	    return;
        }
	illegalArgument(handle);
    }

    /**
     * Set some parameters kind to 'out'.
     *
     * @param handle is the parameter
     */
    public static void setKindToOut(Object handle) {
        if (handle instanceof MParameter) {
	    ((MParameter) handle).setKind(MParameterDirectionKind.OUT);
	    return;
        }
	illegalArgument(handle);
    }

    /**
     * Set some parameters kind to 'return'.
     *
     * @param handle is the parameter
     */
    public static void setKindToReturn(Object handle) {
        if (handle instanceof MParameter) {
	    ((MParameter) handle).setKind(MParameterDirectionKind.RETURN);
	    return;
        }
	illegalArgument(handle);
    }

    /**
     * Sets the parent of a generalization.
     *
     * @param handle generalization
     * @param parent generalizable element (parent)
     */
    public static void setParent(Object handle, Object parent) {
        if (handle instanceof MGeneralization
            && parent instanceof MGeneralizableElement) {
            ((MGeneralization) handle).setParent(
	            (MGeneralizableElement) parent);
            return;
        }
	illegalArgument(handle, parent);
    }

    public static void setPowertype(Object handle, Object pt) {
        if (handle instanceof MGeneralization && pt instanceof MClassifier) {
            ((MGeneralization) handle).setPowertype((MClassifier) pt);
            return;
        }
	illegalArgument(handle, pt);
    }

    public static void setPredecessors(
        Object handle,
        Collection predecessors) {
        if (handle instanceof MMessage) {
            ((MMessage) handle).setPredecessors(predecessors);
            return;
        }
	illegalArgument(handle, predecessors);
    }

    /**
     * Sets the query flag of a behavioral feature.
     *
     * @param handle is the behavioral feature
     * @param flag is the query flag
     */
    public static void setQuery(Object handle, boolean flag) {
        if (handle instanceof MBehavioralFeature) {
            ((MBehavioralFeature) handle).setQuery(flag);
            return;
        }
	illegalArgument(handle);
    }

    /**
     * Sets the type of some parameter.
     *
     * @param handle is the model element
     * @param type is the type (a classifier)
     */
    public static void setType(Object handle, Object type) {
        if (type == null || type instanceof MClassifier) {
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
	illegalArgument(handle, type);
    }

    /**
     * Set the UUID of this element
     *
     * @param handle base element (MBase type)
     * @param uuid is the UUID
     */
    public static void setUUID(Object handle, String uuid) {
        if (isABase(handle)) {
            ((MBase) handle).setUUID(uuid);
            return;
        }
	illegalArgument(handle);
    }

    public static void setTag(Object handle, Object tag) {
        if (handle instanceof MTaggedValue && tag instanceof String) {
            ((MTaggedValue) handle).setTag((String) tag);
            return;
        }
	illegalArgument(handle, tag);
    }

    /**
     * Sets a tagged value of some modelelement.
     *
     * @param handle is the model element
     * @param tag is the tag name (a string)
     * @param value is the value
     */
    public static void setTaggedValue(
        Object handle,
        String tag,
        String value) {
        if (handle instanceof MModelElement) {
	    ((MModelElement) handle).setTaggedValue(tag, value);
            return;
        }
	illegalArgument(handle);
    }

    public static void setTaggedValues(
        Object handle,
        Collection taggedValues) {
        if (handle instanceof MModelElement) {
            ((MModelElement) handle).setTaggedValues(taggedValues);
            return;
        }
	illegalArgument(handle);
    }

    /**
     * Sets a value of some taggedValue.
     *
     * @param handle is the tagged value
     * @param value is the value
     */
    public static void setValueOfTag(Object handle, String value) {
        if (handle instanceof MTaggedValue) {
            ((MTaggedValue) handle).setValue(value);
            return;
        }
	illegalArgument(handle);
    }

    /**
     * Sets a state machine of some state or transition.
     *
     * @param handle is the state or transition
     * @param stm is the state machine
     */
    public static void setStateMachine(Object handle, Object stm) {
        if (handle instanceof MState
            && (stm == null || stm instanceof MStateMachine)) {
            ((MState) handle).setStateMachine((MStateMachine) stm);
            return;
        }
        if (handle instanceof MTransition
            && (stm == null || stm instanceof MStateMachine)) {
            ((MTransition) handle).setStateMachine((MStateMachine) stm);
            return;
        }
	illegalArgument(handle, stm);
    }

    /**
     * Sets the stereotype of some modelelement. The method also
     * copies a stereotype that is not a part of the current model to
     * the current model.<p>
     *
     * <p>TODO: Currently does not copy the stereotype, but changes the
     * namespace to the new model (kidnapping it). That might possibly be
     * dangerous, especially if more complex profile models are developed.
     * This documentation should say what is supposed to be done. I think
     * it would have been better if the caller had been responsible for the
     * stereotype being in the right model and been adviced of
     * eg ModelManagementHelper.getCorrespondingElement(...). Or if that had
     * been used here. This function could possibly assert that the caller had
     * got it right.
     *
     * @param handle model element
     * @param stereo stereotype
     */
    public static void setStereotype(Object handle, Object stereo) {
        if (handle instanceof MModelElement) {
            MModelElement me = (MModelElement) handle;
            if (stereo instanceof MStereotype
                && me.getModel() != ((MStereotype) stereo).getModel()) {
                ((MStereotype) stereo).setNamespace(me.getModel());
            }
            if (stereo == null || stereo instanceof MStereotype) {
                me.setStereotype((MStereotype) stereo);
                return;
            }
        }
	illegalArgument(handle, stereo);
    }

    public static void setSubvertices(Object handle, Collection subvertices) {
        if (handle instanceof MCompositeState) {
            ((MCompositeState) handle).setSubvertices(subvertices);
            return;
        }
	illegalArgument(handle, subvertices);
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
	illegalArgument(handle, connection);
    }

    /**
     * Adds a constraint to some model element.
     *
     * @param handle model element
     * @param mc constraint
     */
    public static void addConstraint(Object handle, Object mc) {
        if (handle instanceof MModelElement && mc instanceof MConstraint) {
            ((MModelElement) handle).addConstraint((MConstraint) mc);
            return;
        }
	illegalArgument(handle, mc);
    }

    public static void addDeploymentLocation(Object handle, Object node) {
        if (handle instanceof MComponent && node instanceof MNode) {
            ((MComponent) handle).addDeploymentLocation((MNode) node);
            return;
        }
	illegalArgument(handle, node);
    }

    public static void addExtendedElement(
        Object handle,
        Object extendedElement) {
        if (handle instanceof MStereotype
            && extendedElement instanceof MExtensionPoint) {
            ((MStereotype) handle).addExtendedElement(
                (MModelElement) extendedElement);
            return;
        }
	illegalArgument(handle, extendedElement);
    }

    /**
     * Adds an extension point to some model element.
     *
     * @param handle is the model element
     * @param extensionPoint is the extension point
     */
    public static void addExtensionPoint(
        Object handle,
        Object extensionPoint) {
        if (extensionPoint instanceof MExtensionPoint) {
            if (handle instanceof MUseCase) {
                ((MUseCase) handle).addExtensionPoint(
                    (MExtensionPoint) extensionPoint);
                return;
            }
            if (handle instanceof MExtend) {
                ((MExtend) handle).addExtensionPoint(
                    (MExtensionPoint) extensionPoint);
                return;
            }
        }
	illegalArgument(handle, extensionPoint);
    }

    /**
     * Returns the name of the UML Model class, e.g. it it will return
     * Class for an object of type MClass.
     *
     * @param handle MBase
     * @return classname of modelelement
     */
    public static String getUMLClassName(Object handle) {
        if (handle instanceof MBase) {
            return ((MBase) handle).getUMLClassName();
        }
	return illegalArgumentString(handle);
    }

    ////////////////////////////////////////////////////////////////
    // Convenience methods

    /**
     * Tests if an element is marked removed.
     *
     * <p>Model specific: NSUML is littered with calls to a function also
     * named checkExists. That function is however a NOP (it is empty in
     * MBaseImpl, and it is final so it cannot be overridden anywhere).
     *
     * @param obj the element to test.
     * @throws IllegalStateException iff obj is marked removed.
     */
    private static void checkExists(Object obj) {
	if ((obj instanceof MBase) && ((MBase) obj).isRemoved()) {
	    throw new IllegalStateException("Operation on a removed object ["
					    + obj + "]");
	}
    }

    /**
     * The empty set.
     *
     * @return an empty collection.
     */
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

    /**
     * Method that throws an error when a ModelFacade method is called with
     * an incorrect argument.
     *
     * @param arg is the incorrect argument.
     */
    private static void illegalArgument(Object arg) {
	throw new IllegalArgumentException("Unrecognized object "
					   + getClassNull(arg));
    }

    /**
     * Method that throws an error when a ModelFacade method is called with
     * an incorrect argument.<p>
     *
     * @param arg is the incorrect argument.
     * @return a boolean for use in the return statement.
     */
    private static boolean illegalArgumentBoolean(Object arg) {
	illegalArgument(arg);
	return false;
    }

    /**
     * Method that throws an error when a ModelFacade method is called with
     * an incorrect argument.<p>
     *
     * @param arg is the incorrect argument.
     * @return Object for use in the return statement.
     */
    private static Object illegalArgumentObject(Object arg) {
	illegalArgument(arg);
	return null;
    }

    /**
     * Method that throws an error when a ModelFacade method is called with
     * an incorrect argument.<p>
     *
     * @param arg is the incorrect argument.
     * @return Collection for use in the return statement.
     */
    private static Collection illegalArgumentCollection(Object arg) {
	illegalArgument(arg);
	return null;
    }

    /**
     * Method that throws an error when a ModelFacade method is called with
     * an incorrect argument.<p>
     *
     * @param arg is the incorrect argument.
     * @return String for use in the return statement.
     */
    private static String illegalArgumentString(Object arg) {
	illegalArgument(arg);
	return null;
    }

    /**
     * Method that throws an error when a ModelFacade method is called
     * with an incorrect argument. At least one of the arguments given
     * is incorrect.<p>
     *
     * @param arg1 is one of the argument, possibly incorrect.
     * @param arg2 is one of the argument, possibly incorrect.
     */
    private static Object illegalArgument(Object arg1, Object arg2) {
	throw new IllegalArgumentException("Unrecognized object "
					   + getClassNull(arg1) + " or "
					   + getClassNull(arg2));
    }

    /**
     * Method that throws an error when a ModelFacade method is called
     * with an incorrect argument. At least one of the arguments given
     * is incorrect.<p>
     *
     * @param arg1 is one of the argument, possibly incorrect.
     * @param arg2 is one of the argument, possibly incorrect.
     * @return Object for use in the return statement.
     */
    private static Object illegalArgumentObject(Object arg1, Object arg2) {
	illegalArgument(arg1, arg2);
	return null;
    }
}
