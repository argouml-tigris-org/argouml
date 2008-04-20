// $Id$
// Copyright (c) 1996-2008 The Regents of the University of California. All
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

package org.argouml.model.mdr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jmi.reflect.InvalidObjectException;
import javax.jmi.reflect.RefObject;

import org.apache.log4j.Logger;
import org.argouml.model.DummyModelCommand;
import org.argouml.model.IllegalModelElementConnectionException;
import org.argouml.model.InvalidElementException;
import org.argouml.model.MetaTypes;
import org.argouml.model.Model;
import org.argouml.model.UmlFactory;
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
import org.omg.uml.behavioralelements.statemachines.CompositeState;
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
import org.omg.uml.foundation.core.Primitive;
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
import org.omg.uml.foundation.datatypes.AggregationKind;
import org.omg.uml.modelmanagement.ElementImport;
import org.omg.uml.modelmanagement.Subsystem;
import org.omg.uml.modelmanagement.UmlPackage;

/**
 * Root factory for UML model element instance creation.<p>
 *
 * @since ARGO0.19.5
 * @author Ludovic Ma&icirc;tre
 * based on NSUML implementation by:
 * @author Thierry Lach
 */
class UmlFactoryMDRImpl extends AbstractUmlModelFactoryMDR implements
        UmlFactory {

    /**
     * The logger.
     */
    private static final Logger LOG = Logger.getLogger(UmlFactoryMDRImpl.class);

    /**
     * The model implementation.
     */
    private MDRModelImplementation modelImpl;

    /**
     * The meta types factory.
     */
    private MetaTypes metaTypes;

    /**
     * A map of valid connections keyed by the connection type. The constructor
     * builds this from the data in the VALID_CONNECTIONS array
     */
    private Map<Class<?>, List<Class<?>[]>> validConnectionMap =
        new HashMap<Class<?>, List<Class<?>[]>>();
    
    /**
     * A map of the valid model elements that are valid to be contained 
     * by other model elements.
     */
    private HashMap<Class<?>, Class<?>[]> validContainmentMap = 
        new HashMap<Class<?>, Class<?>[]>();

    /**
     * The instance that we are deleting.
     */
    private Set<RefObject> elementsToBeDeleted = new HashSet<RefObject>();

    /**
     * Ordered list of elements to be deleted.
     */
    private List<RefObject> elementsInDeletionOrder = 
        new ArrayList<RefObject>();

    /**
     * The top object is the first object given to the UmlFactory when calling
     * the delete method.
     */
    private Object top;

    /**
     * The mutex for this class.
     */
    private Object lock = new Byte[0];

    /**
     * An array of valid connections, the combination of connecting class and
     * node classes must exist as a row in this list to be considered valid.
     * <ul>
     * <li>The 1st column is the connecting element.
     * <li>The 2nd column is the "from" element type.
     * <li>The 3rd column is the "to" element type.
     * The 3rd column is optional, if not given then it is assumed to be
     * the same as the "from" element.
     * <li>The existence of a 4th column indicates that the connection is valid
     * in one direction only.
     * </ul>
     * TODO: This encodes not only what is legal in UML, but also what ArgoUML
     * knows how to create, so not all legal connections are included. Probably
     * should be split into two pieces: 1) legal UML (here) and 2) supported (in
     * ArgoUML application someplace) - tfm - 20060325<p>
     * See also issue 3863.<p>
     *
     * Most of these are subtypes of Relationship which includes Association,
     * Dependency, Flow, Generalization, Extend, and Include. Dependency
     * includes Binding, Abstraction, Usage, and Permission. AssociationRole and
     * AssociationClass are Associations. The remaining items (Link, Transition,
     * AssociationEnd, Message) are non-Relationship types which ArgoUML treats
     * as connections/edges.
     */
    // TODO: This should be built by reflection from the metamodel - tfm
    //       Update for UML 1.4 metamodel if not replaced by reflection
    private static final Class<?>[][] VALID_CONNECTIONS = {
        {Generalization.class,   GeneralizableElement.class, },
        {Dependency.class,       ModelElement.class, },
        // Although Usage & Permission are Dependencies, they need to
        // be include separately because of the way lookup works
        {Usage.class,            ModelElement.class, },
        {Permission.class,       ModelElement.class, },
        // The following is specifically for Realizations
        {Abstraction.class, UmlClass.class, Interface.class, null, },
        // The next 3 restrictions for Abstraction seem to be Argo specific
        // not something the UML spec requires - tfm - 20070215
        {Abstraction.class, UmlClass.class, UmlClass.class, null, },
        {Abstraction.class, UmlPackage.class, UmlPackage.class, null, },
        {Abstraction.class, Component.class, Interface.class, null, },
        {UmlAssociation.class,     Classifier.class, }, 
        {AssociationRole.class,  ClassifierRole.class, },
        {Extend.class,           UseCase.class, },
        {Include.class,          UseCase.class, },
        {Link.class, Instance.class, }, 
        {Transition.class,       StateVertex.class, },
        {AssociationClass.class, UmlClass.class, }, 
        {AssociationEnd.class, Classifier.class, UmlAssociation.class, },
        {Message.class, ClassifierRole.class },
    };

    /**
     * Package-private constructor.
     *
     * @param implementation
     *            To get other helpers and factories.
     */
    UmlFactoryMDRImpl(MDRModelImplementation implementation) {
        modelImpl = implementation;
        metaTypes = modelImpl.getMetaTypes();

        buildValidConnectionMap();
        
        buildValidContainmentMap();
    }

    private void buildValidConnectionMap() {
        // A list of valid connections between elements, the
        // connection type first and then the elements to be connected

        for (int i = 0; i < VALID_CONNECTIONS.length; ++i) {
            final Class<?> connection = VALID_CONNECTIONS[i][0];
            List<Class<?>[]> validItems = validConnectionMap.get(connection);
            if (validItems == null) {
                validItems = new ArrayList<Class<?>[]>();
                validConnectionMap.put(connection, validItems);
            }
            if (VALID_CONNECTIONS[i].length < 3) {
                // If there isn't a 3rd column then this represents a connection
                // of elements of the same type.
                Class<?>[] modeElementPair = new Class[2];
                modeElementPair[0] = VALID_CONNECTIONS[i][1];
                modeElementPair[1] = VALID_CONNECTIONS[i][1];
                validItems.add(modeElementPair);
            } else {
                // If there is a 3rd column then this represents a connection
                // of between 2 different types of element.
                Class<?>[] modeElementPair = new Class[2];
                modeElementPair[0] = VALID_CONNECTIONS[i][1];
                modeElementPair[1] = VALID_CONNECTIONS[i][2];
                validItems.add(modeElementPair);
                // If the array hasn't been flagged to indicate otherwise
                // swap elements the elements and add again.
                if (VALID_CONNECTIONS[i].length < 4) {
                    Class<?>[] reversedModeElementPair = new Class[2];
                    reversedModeElementPair[0] = VALID_CONNECTIONS[i][2];
                    reversedModeElementPair[1] = VALID_CONNECTIONS[i][1];
                    validItems.add(reversedModeElementPair);
                }
            }
        }
    }
    
    /**
     * Initializes the validContainmentMap based on the rules for 
     * valid containment of elements.
     * 
     * @author Scott Roberts
     */
    private void buildValidContainmentMap() {
       
        validContainmentMap.clear();

        validContainmentMap.put(ModelElement.class, 
                new Class<?>[] {
                    Stereotype.class
                });
        
        // specifies valid elements for a Model to contain 
        validContainmentMap.put(org.omg.uml.modelmanagement.Model.class,
            new Class<?>[] {
                ComponentInstance.class, NodeInstance.class
            });

        // specifies valid elements for a Package to contain
        validContainmentMap.put(UmlPackage.class, 
            new Class<?>[] {
                UmlPackage.class, Actor.class,
                UseCase.class, UmlClass.class,
                Interface.class, Component.class,
                Node.class,
                Enumeration.class, DataType.class,
                Exception.class, Signal.class
            });
                
        // specifies valid elements for a class to contain
        validContainmentMap.put(UmlClass.class, 
            new Class<?>[] { 
                Attribute.class, Operation.class,
                UmlClass.class, Reception.class
            });
        
        // specifies valid elements for an Interface to contain
        validContainmentMap.put(Interface.class, 
                new Class<?>[] { 
                    Operation.class, Reception.class
                });
        
        // specifies valid elements for an Actor to contain
        validContainmentMap.put(Actor.class, 
            new Class<?>[] { 
                Reception.class
            });
        
        // specifies valid elements for a Use Case to contain
        validContainmentMap.put(UseCase.class, 
            new Class<?>[] { 
                ExtensionPoint.class, Attribute.class, 
                Operation.class, Reception.class
            });
        
        // specifies valid elements for a Component to contain
        validContainmentMap.put(Component.class, 
            new Class<?>[] { 
                Reception.class
            });
        
        // specifies valid elements for a Node to contain
        validContainmentMap.put(Node.class, 
                new Class<?>[] { 
                    Reception.class
                });
        
        // specifies valid elements for a Enumeration to contain
        validContainmentMap.put(Enumeration.class, 
                new Class<?>[] { 
                    EnumerationLiteral.class, Operation.class 
                });
        
        // specifies valid elements for a DataType to contain
        validContainmentMap.put(DataType.class, 
                new Class<?>[] { 
                    Operation.class 
                });
    }
        
    public Object buildConnection(Object elementType, Object fromElement,
            Object fromStyle, Object toElement, Object toStyle,
            Object unidirectional, Object namespace)
        throws IllegalModelElementConnectionException {

        if (!isConnectionValid(elementType, fromElement, toElement, true)) {
            throw new IllegalModelElementConnectionException("Cannot make a "
                    + elementType.getClass().getName() + " between a "
                    + fromElement.getClass().getName() + " and a "
                    + toElement.getClass().getName());
        }

        Object connection = null;

        if (elementType == metaTypes.getAssociation()) {
            connection =
                getCore().buildAssociation((Classifier) fromElement,
                    (AggregationKind) fromStyle, (Classifier) toElement,
                    (AggregationKind) toStyle, (Boolean) unidirectional);
        } else if (elementType == metaTypes.getAssociationEnd()) {
            if (fromElement instanceof UmlAssociation) {
                connection =
                    getCore().buildAssociationEnd(toElement, fromElement);
            } else if (fromElement instanceof Classifier) {
                connection =
                    getCore().buildAssociationEnd(fromElement, toElement);
            }
        } else if (elementType
                == metaTypes.getAssociationClass()) {
            connection =
                getCore().buildAssociationClass(fromElement, toElement);
        } else if (elementType == metaTypes.getAssociationRole()) {
            connection =
                getCollaborations().buildAssociationRole(fromElement,
                    fromStyle, toElement, toStyle, (Boolean) unidirectional);
        } else if (elementType == metaTypes.getGeneralization()) {
            connection = getCore().buildGeneralization(fromElement, toElement);
        } else if (elementType == metaTypes.getPackageImport()) {
            connection = getCore().buildPackageImport(fromElement, toElement);
        } else if (elementType == metaTypes.getUsage()) {
            connection = getCore().buildUsage(fromElement, toElement);
        } else if (elementType == metaTypes.getGeneralization()) {
            connection = getCore().buildGeneralization(fromElement, toElement);
        } else if (elementType == metaTypes.getDependency()) {
            connection = getCore().buildDependency(fromElement, toElement);
        } else if (elementType == metaTypes.getAbstraction()) {
            connection =
                getCore().buildRealization(fromElement, toElement, namespace);
        } else if (elementType == metaTypes.getLink()) {
            connection = getCommonBehavior().buildLink(fromElement, toElement);
        } else if (elementType == metaTypes.getExtend()) {
            // Extend, but only between two use cases. Remember we draw from the
            // extension port to the base port.
            connection = getUseCases().buildExtend(toElement, fromElement);
        } else if (elementType == metaTypes.getInclude()) {
            connection = getUseCases().buildInclude(fromElement, toElement);
        } else if (elementType == metaTypes.getTransition()) {
            connection =
                getStateMachines().buildTransition(fromElement, toElement);
        }

        if (connection == null) {
            throw new IllegalModelElementConnectionException("Cannot make a "
                    + elementType.getClass().getName() + " between a "
                    + fromElement.getClass().getName() + " and a "
                    + toElement.getClass().getName());
        }

        return connection;
    }


    public Object buildNode(Object elementType) {
        if (elementType == metaTypes.getActor()) {
            return getUseCases().createActor();
        } else if (elementType == metaTypes.getUseCase()) {
            return getUseCases().createUseCase();
        } else if (elementType == metaTypes.getUMLClass()) {
            return getCore().buildClass();
        } else if (elementType == metaTypes.getInterface()) {
            return getCore().buildInterface();
        } else if (elementType == metaTypes.getDataType()) {
            return getCore().createDataType();
        } else if (elementType == metaTypes.getPackage()) {
            return getModelManagement().createPackage();
        } else if (elementType == metaTypes.getModel()) {
            return getModelManagement().createModel();
        } else if (elementType == metaTypes.getInstance()) {
            throw new IllegalArgumentException(
                    "Attempt to instantiate abstract type");
        } else if (elementType == metaTypes.getSubsystem()) {
            return getModelManagement().createSubsystem();
        } else if (elementType == metaTypes.getCallState()) {
            return getActivityGraphs().createCallState();
        } else if (elementType == metaTypes.getSimpleState()) {
            return getStateMachines().createSimpleState();
        } else if (elementType == metaTypes.getFinalState()) {
            return getStateMachines().createFinalState();
        } else if (elementType == metaTypes.getPseudostate()) {
            return getStateMachines().createPseudostate();
        } else if (elementType == metaTypes.getObjectFlowState()) {
            return getActivityGraphs().createObjectFlowState();
        } else if (elementType == metaTypes.getActionState()) {
            return getActivityGraphs().createActionState();
        } else if (elementType == metaTypes.getSubactivityState()) {
            return getActivityGraphs().createSubactivityState();
        } else if (elementType == metaTypes.getPartition()) {
            return getActivityGraphs().createPartition();
        } else if (elementType == metaTypes.getStubState()) {
            return getStateMachines().createStubState();
        } else if (elementType == metaTypes.getSubmachineState()) {
            return getStateMachines().createSubmachineState();
        } else if (elementType == metaTypes.getCompositeState()) {
            return getStateMachines().createCompositeState();
        } else if (elementType == metaTypes.getSynchState()) {
            return getStateMachines().createSynchState();
        } else if (elementType == metaTypes.getState()) {
            throw new IllegalArgumentException(
                    "Attempt to instantiate abstract type");
        } else if (elementType == modelImpl.getMetaTypes().getSimpleState()) {
            return getStateMachines().createSimpleState();
        } else if (elementType == metaTypes.getClassifierRole()) {
            return getCollaborations().createClassifierRole();
        } else if (elementType == metaTypes.getComponent()) {
            return getCore().createComponent();
        } else if (elementType == metaTypes.getComponentInstance()) {
            return getCommonBehavior().createComponentInstance();
        } else if (elementType == metaTypes.getNode()) {
            return getCore().createNode();
        } else if (elementType == metaTypes.getNodeInstance()) {
            return getCommonBehavior().createNodeInstance();
        } else if (elementType == metaTypes.getObject()) {
            return getCommonBehavior().createObject();
        } else if (elementType == metaTypes.getComment()) {
            return getCore().createComment();
        } else if (elementType == metaTypes.getNamespace()) {
            throw new IllegalArgumentException(
                    "Attempt to instantiate abstract type");
        } else if (elementType == metaTypes.getOperation()) {
            return getCore().createOperation();
        } else if (elementType == metaTypes.getEnumeration()) {
            return getCore().createEnumeration();
        } else if (elementType == metaTypes.getStereotype()) {
            return getExtensionMechanisms().createStereotype();
        } else if (elementType == metaTypes.getAttribute()) {
            return getCore().buildAttribute();
        } else if (elementType == metaTypes.getSignal()) {
            return getCommonBehavior().createSignal();
        } else if (elementType == metaTypes.getException()) {
            return getCommonBehavior().createException();
        } else if (elementType == metaTypes.getTransition()) {
            return getStateMachines().createTransition();
        }
            
        throw new IllegalArgumentException(
                "Attempted to create unsupported model element type: " 
                + elementType);
    }

    public Object buildNode(Object elementType, Object container) {
        
        Object element = null;
        
        // if this is a feature get the owner of that feature
        if (this.modelImpl.getFacade().isAFeature(container)) {
            container = this.modelImpl.getFacade().getOwner(container);
        }
        
        // supports implementation of some special elements not 
        // supported by buildNode
        if (elementType == this.metaTypes.getAttribute()) {
            element = getCore().buildAttribute2(container, null);   
        } else if (elementType == this.metaTypes.getOperation()) {
            element = getCore().buildOperation(container, null);
        } else if (elementType == this.metaTypes.getReception()) {
            element = this.modelImpl.getCommonBehaviorFactory().
                createReception();
            
            this.modelImpl.getCoreHelper().addOwnedElement(container, element);
        } else if (elementType == this.metaTypes.getEnumerationLiteral()) {
            element = getCore().buildEnumerationLiteral(null, container);
        } else if (elementType == this.metaTypes.getExtensionPoint()) {
            element = this.modelImpl.getUseCasesFactory().
                buildExtensionPoint(container);            
        } else {
            // build all other elements using existing buildNode
            element = buildNode(elementType);
            
            if (container instanceof Namespace && element instanceof Namespace)
                ((Namespace) element).setNamespace(
                        ((Namespace) container).getNamespace());
            
            this.modelImpl.getCoreHelper().addOwnedElement(container, element);
        }
        
        return element;
    }
    
    public boolean isConnectionType(Object connectionType) {
        // If our map has any entries for this type, it's a connection type
        return (validConnectionMap.get(connectionType) != null);
    }


    @SuppressWarnings("deprecation")
    public boolean isConnectionValid(Object connectionType, Object fromElement,
            Object toElement) {
        return isConnectionValid(connectionType, fromElement, toElement, true);
    }
    

    public boolean isConnectionValid(Object connectionType, Object fromElement,
            Object toElement, boolean checkWFR) {
        // Get the list of valid model item pairs for the given connection type
        List<Class<?>[]> validItems = validConnectionMap.get(connectionType);
        if (validItems == null) {
            return false;
        }
        // See if there's a pair in this list that match the given
        // model elements
        for (Class<?>[] modeElementPair : validItems) {
            if (modeElementPair[0].isInstance(fromElement)
                    && modeElementPair[1].isInstance(toElement)) {
                if (checkWFR) {
                    return isConnectionWellformed(
                            (Class<?>) connectionType,
                            (ModelElement) fromElement,
                            (ModelElement) toElement);
                } else {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isContainmentValid(Object metaType, Object container) {
        
        // find the passed in container in validContainmentMap
        for (Class<?> containerType : validContainmentMap.keySet()) {
            
            if (containerType.isInstance(container)) {
                // determine if metaType is a valid element for container
                Class<?>[] validElements = validContainmentMap.get(containerType);
                
                for (int eIter = 0; eIter < validElements.length; ++eIter) {
                    
                    if (metaType == validElements[eIter]) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * Run through any well formedness rules we wish to enforce for a
     * connection.
     * @param connectionType
     * @param fromElement
     * @param toElement
     * @return true if the connection satisfies the wellformedness rules
     */
    private boolean isConnectionWellformed(
            Class<?> connectionType,
            ModelElement fromElement,
            ModelElement toElement) {
        
        if (fromElement == null || toElement == null) {
            return false;
        }
            
        if (connectionType == Generalization.class) {
            /*
             * UML 1.4.2 Spec section 4.5.3.20 [5]
             * A GeneralizableElement may only be a child of
             * GeneralizableElement of the same kind.
             */
            if (fromElement.getClass() != toElement.getClass()) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Returns the package factory for the UML package
     * Foundation::ExtensionMechanisms.
     *
     * @return the ExtensionMechanisms factory instance.
     */
    private ExtensionMechanismsFactoryMDRImpl getExtensionMechanisms() {
        return (ExtensionMechanismsFactoryMDRImpl) modelImpl.
                getExtensionMechanismsFactory();
    }

    /**
     * Returns the package factory for the UML package Foundation::Core.
     *
     * @return the Core factory instance.
     */
    private CoreFactoryMDRImpl getCore() {
        return (CoreFactoryMDRImpl) modelImpl.getCoreFactory();
    }

    /**
     * Returns the package factory for the UML package
     * BehavioralElements::CommonBehavior.
     *
     * @return the CommonBehavior factory instance.
     */
    private CommonBehaviorFactoryMDRImpl getCommonBehavior() {
        return (CommonBehaviorFactoryMDRImpl) modelImpl.
                getCommonBehaviorFactory();
    }

    /**
     * Returns the package factory for the UML package
     * BehavioralElements::UseCases.
     *
     * @return the UseCases factory instance.
     */
    private UseCasesFactoryMDRImpl getUseCases() {
        return (UseCasesFactoryMDRImpl) modelImpl.getUseCasesFactory();
    }

    /**
     * Returns the package factory for the UML package
     * BehavioralElements::StateMachines.
     *
     * @return the StateMachines factory instance.
     */
    private StateMachinesFactoryMDRImpl getStateMachines() {
        return (StateMachinesFactoryMDRImpl) modelImpl
                .getStateMachinesFactory();
    }

    /**
     * Returns the package factory for the UML package
     * BehavioralElements::Collaborations.
     *
     * @return the Collaborations factory instance.
     */
    private CollaborationsFactoryMDRImpl getCollaborations() {
        return (CollaborationsFactoryMDRImpl) modelImpl.
                getCollaborationsFactory();
    }

    /**
     * Returns the package factory for the UML package
     * BehavioralElements::ActivityGraphs.
     *
     * @return the ActivityGraphs factory instance.
     */
    private ActivityGraphsFactoryMDRImpl getActivityGraphs() {
        return (ActivityGraphsFactoryMDRImpl) modelImpl.
                getActivityGraphsFactory();
    }

    /**
     * Returns the package factory for the UML package ModelManagement.
     *
     * @return the ModelManagement factory instance.
     */
    private ModelManagementFactoryMDRImpl getModelManagement() {
        return (ModelManagementFactoryMDRImpl) modelImpl.
                getModelManagementFactory();
    }

    /*
     * Delete a model element.  Implements 'cascading delete' to make sure
     * model is still valid after element has been deleted.<p>
     *
     * The actual deletion is delegated to delete methods in the rest of the
     * factories. For example: a method deleteClass exists on CoreHelper. Delete
     * methods as deleteClass should only do those extra actions that are
     * necessary for the deletion of the modelelement itself. I.e. deleteClass
     * should only take care of things specific to UmlClass.<p>
     *
     * The delete methods in the UML Factories should not be called directly
     * throughout the code! Calls should always refer to this method and never
     * call the deleteXXX method on XXXFactory directly. The reason that it is
     * possible to call the deleteXXX methods directly is a pure implementation
     * detail.<p>
     *
     * The implementation of this method uses a quite complicated if/then/else
     * tree. This is done to provide optimal performance and full compliance to
     * the UML 1.4 metamodel. The last remark refers to the fact that the
     * UML 1.4 model uses multiple inheritance in several places.
     * This has to be taken into account.<p>
     *
     * TODO: The requirements of the metamodel could probably be better
     * determined by reflection on the metamodel.  Then each association
     * that a deleted element participates in could be reviewed to make sure
     * that it meets the requirements and, if not, be deleted. - tfm<p>
     *
     * Extensions and its children are not taken into account here. They do not
     * require extra cleanup actions. Not in the form of a call to the remove
     * method as is normal for all children of MBase and not in the form of
     * other behaviour we want to implement via this operation.
     *
     * @param elem
     *            The element to be deleted
     *            
     * @see org.argouml.model.UmlFactory#delete(java.lang.Object)
     */
    public void delete(Object elem) {
        if (elem == null) {
            throw new IllegalArgumentException("Element may not be null "
                    + "in delete");
        }

        // TODO: Hold lock for entire recursive traversal?
        synchronized (lock) {
            if (elementsToBeDeleted.contains(elem)) {
                return;
            }
            if (top == null) {
                top = elem;
            }
            elementsToBeDeleted.add((RefObject) elem);
        }

        if (LOG.isDebugEnabled()) {
            if (top == elem) {
                LOG.debug("Set top for cascade delete to " + elem);
            }
            LOG.debug("Deleting " + elem);
        }

        // Begin a transaction - we'll do a bunch of reads first
        // to collect a set of elements to delete - then delete them all
        modelImpl.getRepository().beginTrans(false);
        try {
            // TODO: Encountering a deleted object during
            // any part of this traversal will
            // abort the rest of the traversal.
            // We probably should do the whole traversal
            // in a single MDR transaction.
            if (elem instanceof Element) {
                getCore().deleteElement(elem);
                if (elem instanceof ModelElement) {
                    getCore().deleteModelElement(elem);
                    // no else here to make sure Classifier with
                    // its double inheritance goes ok

                    if (elem instanceof GeneralizableElement) {
                        GeneralizableElement ge = (GeneralizableElement) elem;
                        getCore().deleteGeneralizableElement(ge);
                        if (elem instanceof Stereotype) {
                            Stereotype s = (Stereotype) elem;
                            getExtensionMechanisms().deleteStereotype(s);
                        }
                    } // no else here to make sure AssociationClass goes ok

                    if (elem instanceof Parameter) {
                        getCore().deleteParameter(elem);
                    } else if (elem instanceof Constraint) {
                        getCore().deleteConstraint(elem);
                    } else if (elem instanceof Relationship) {
                        deleteRelationship((Relationship) elem);
                    } else if (elem instanceof AssociationEnd) {
                        getCore().deleteAssociationEnd(elem);
                        if (elem instanceof AssociationEndRole) {
                            getCollaborations().deleteAssociationEndRole(elem);
                        }
                    } else if (elem instanceof Comment) {
                        getCore().deleteComment(elem);
                    } else if (elem instanceof Action) {
                        deleteAction(elem);
                    } else if (elem instanceof AttributeLink) {
                        getCommonBehavior().deleteAttributeLink(elem);
                    } else if (elem instanceof Instance) {
                        deleteInstance((Instance) elem);
                    } else if (elem instanceof Stimulus) {
                        getCommonBehavior().deleteStimulus(elem);
                    } // no else to handle multiple inheritance of linkobject

                    if (elem instanceof Link) {
                        getCommonBehavior().deleteLink(elem);
                    } else if (elem instanceof LinkEnd) {
                        getCommonBehavior().deleteLinkEnd(elem);
                    } else if (elem instanceof Interaction) {
                        getCollaborations().deleteInteraction(elem);
                    } else if (elem instanceof InteractionInstanceSet) {
                        getCollaborations().deleteInteractionInstanceSet(elem);
                    } else if (elem instanceof CollaborationInstanceSet) {
                        getCollaborations()
                                .deleteCollaborationInstanceSet(elem);
                    } else if (elem instanceof Message) {
                        getCollaborations().deleteMessage(elem);
                    } else if (elem instanceof ExtensionPoint) {
                        getUseCases().deleteExtensionPoint(elem);
                    } else if (elem instanceof StateVertex) {
                        deleteStateVertex((StateVertex) elem);
                    }

                    if (elem instanceof StateMachine) {
                        getStateMachines().deleteStateMachine(elem);
                        if (elem instanceof ActivityGraph) {
                            getActivityGraphs().deleteActivityGraph(elem);
                        }
                    } else if (elem instanceof Transition) {
                        getStateMachines().deleteTransition(elem);
                    } else if (elem instanceof Guard) {
                        getStateMachines().deleteGuard(elem);
                    } else if (elem instanceof TaggedValue) {
                        getExtensionMechanisms().deleteTaggedValue(elem);
                    } else if (elem instanceof TagDefinition) {
                        getExtensionMechanisms().deleteTagDefinition(elem);
                    }
                    // else if (elem instanceof MEvent) {
                    //
                    // }
                } else if (elem instanceof PresentationElement) {
                    getCore().deletePresentationElement(elem);
                }
            } else if (elem instanceof TemplateParameter) {
                getCore().deleteTemplateParameter(elem);
            } else if (elem instanceof TemplateArgument) {
                getCore().deleteTemplateArgument(elem);
            } else if (elem instanceof ElementImport) {
                getModelManagement().deleteElementImport(elem);
            } else if (elem instanceof ElementResidence) {
                getCore().deleteElementResidence(elem);
            } 

            if (elem instanceof Partition) {
                getActivityGraphs().deletePartition(elem);
            }

            if (elem instanceof Feature) {
                deleteFeature((Feature) elem);
            } else if (elem instanceof Namespace) {
                deleteNamespace((Namespace) elem);
            }
        } catch (InvalidObjectException e) {
            // If we get this with the repository locked, it means our root
            // model element was already deleted.  Nothing to do...
            LOG.error("Encountered deleted object during delete of " + elem);
        } catch (InvalidElementException e) {
            // Our wrapped version of the same error
            LOG.error("Encountered deleted object during delete of " + elem);
        } finally {
            // end our transaction
            modelImpl.getRepository().endTrans();
        }



        synchronized (lock) {
            // Elements which will be deleted when their container is deleted
            // don't get added to the list of elements to be deleted
            // (but we still want to traverse them looking for other elements
            //  to be deleted)
            try {
                Object container = ((RefObject) elem).refImmediateComposite();
                if (container == null
                        || !elementsToBeDeleted.contains(container)
                        // For some reason StateMachine.top doesn't get deleted
                        // even though it should because it's a composite
                        // see issue 4948
                        || (container instanceof StateMachine 
                                && elem instanceof StateVertex)) {
                    elementsInDeletionOrder.add((RefObject) elem);
                }
            } catch (InvalidObjectException e) {
                LOG.warn("Object already deleted " + elem);
            }

            if (elem == top) {
                for (RefObject o : elementsInDeletionOrder) {
                    try {
                        o.refDelete();
                    } catch (InvalidObjectException e) {
                        LOG.warn("Object already deleted " + o);
                    }
                    elementsToBeDeleted.remove(o);
                }
                top = null;
                elementsInDeletionOrder.clear();
                if (!elementsToBeDeleted.isEmpty()) {
                    LOG.debug("**Skipped deleting "
                            + elementsToBeDeleted.size()
                            + " elements (probably in a deleted container");
                    elementsToBeDeleted.clear();
                }
            }
        }
        
        Model.execute(new DummyModelCommand());
    }


    public boolean isRemoved(Object o) {
        if (!(o instanceof RefObject)) {
            throw new IllegalArgumentException(
                    "Expected JMI RefObject, received " + o);
        }
        try {
            // We don't care about the value - just want to see if it throws
            ((RefObject) o).refImmediateComposite();
            return false;
        } catch (InvalidObjectException e) {
            return true;
        }
    }

    /**
     * Delete a Feature.
     *
     * @param elem feature to be deleted
     */
    private void deleteFeature(Feature elem) {
        getCore().deleteFeature(elem);
        if (elem instanceof BehavioralFeature) {
            getCore().deleteBehavioralFeature(elem);
            if (elem instanceof Operation) {
                getCore().deleteOperation(elem);
            } else if (elem instanceof Method) {
                getCore().deleteMethod(elem);
            } else if (elem instanceof Reception) {
                getCommonBehavior().deleteReception(elem);
            }
        } else if (elem instanceof StructuralFeature) {
            getCore().deleteStructuralFeature(elem);
            if (elem instanceof Attribute) {
                getCore().deleteAttribute(elem);
            }
        }
    }

    /**
     * Delete a Namespace.
     *
     * @param elem namespace to be deleted
     */
    private void deleteNamespace(Namespace elem) {
        getCore().deleteNamespace(elem);
        if (elem instanceof Classifier) {
            getCore().deleteClassifier(elem);
            if (elem instanceof UmlClass) {
                getCore().deleteClass(elem);
                if (elem instanceof AssociationClass) {
                    getCore().deleteAssociationClass(elem);
                }
            } else if (elem instanceof Interface) {
                getCore().deleteInterface(elem);
            } else if (elem instanceof DataType) {
                getCore().deleteDataType(elem);
                if (elem instanceof Primitive) {
                    getCore().deletePrimitive(elem);
                } else if (elem instanceof Enumeration) {
                    // TODO: Add EnumerationLiteral someplace
                    getCore().deleteEnumeration(elem);
                } else if (elem instanceof ProgrammingLanguageDataType) {
                    getCore().deleteProgrammingLanguageDataType(elem);
                }
            } else if (elem instanceof Node) {
                getCore().deleteNode(elem);
            } else if (elem instanceof Component) {
                getCore().deleteComponent(elem);
            } else if (elem instanceof Artifact) {
                getCore().deleteArtifact(elem);
            } else if (elem instanceof Signal) {
                getCommonBehavior().deleteSignal(elem);
                if (elem instanceof Exception) {
                    getCommonBehavior().deleteException(elem);
                }
            } else if (elem instanceof ClassifierRole) {
                getCollaborations().deleteClassifierRole(elem);
            } else if (elem instanceof UseCase) {
                getUseCases().deleteUseCase(elem);
            } else if (elem instanceof Actor) {
                getUseCases().deleteActor(elem);
            } else if (elem instanceof ClassifierInState) {
                getActivityGraphs().deleteClassifierInState(elem);
            }
        } else if (elem instanceof Collaboration) {
            getCollaborations().deleteCollaboration(elem);
        } else if (elem instanceof UmlPackage) {
            getModelManagement().deletePackage(elem);
            if (elem instanceof org.omg.uml.modelmanagement.Model) {
                getModelManagement().deleteModel(elem);
            } else if (elem instanceof Subsystem) {
                getModelManagement().deleteSubsystem(elem);
            }
        }
    }

    /**
     * Delete a Relationship.
     *
     * @param elem Relationship to be deleted
     */
    private void deleteRelationship(Relationship elem) {
        getCore().deleteRelationship(elem);
        if (elem instanceof Flow) {
            getCore().deleteFlow(elem);
        } else if (elem instanceof Generalization) {
            getCore().deleteGeneralization(elem);
        } else if (elem instanceof UmlAssociation) {
            getCore().deleteAssociation(elem);
            if (elem instanceof AssociationRole) {
                getCollaborations().deleteAssociationRole(elem);
            }
        } else if (elem instanceof Dependency) {
            getCore().deleteDependency(elem);
            if (elem instanceof Abstraction) {
                getCore().deleteAbstraction(elem);
            } else if (elem instanceof Binding) {
                getCore().deleteBinding(elem);
            } else if (elem instanceof Usage) {
                getCore().deleteUsage(elem);
            } else if (elem instanceof Permission) {
                getCore().deletePermission(elem);
            }
        } else if (elem instanceof Include) {
            getUseCases().deleteInclude(elem);
        } else if (elem instanceof Extend) {
            getUseCases().deleteExtend(elem);
        }
    }

    /**
     * Delete an Action.
     *
     * @param elem the Action to be deleted
     */
    private void deleteAction(Object elem) {
        getCommonBehavior().deleteAction(elem);
        if (elem instanceof ActionSequence) {
            getCommonBehavior().deleteActionSequence(elem);
        } else if (elem instanceof CreateAction) {
            getCommonBehavior().deleteCreateAction(elem);
        } else if (elem instanceof CallAction) {
            getCommonBehavior().deleteCallAction(elem);
        } else if (elem instanceof ReturnAction) {
            getCommonBehavior().deleteReturnAction(elem);
        } else if (elem instanceof SendAction) {
            getCommonBehavior().deleteSendAction(elem);
        } else if (elem instanceof TerminateAction) {
            getCommonBehavior().deleteTerminateAction(elem);
        } else if (elem instanceof UninterpretedAction) {
            getCommonBehavior().deleteUninterpretedAction(elem);
        } else if (elem instanceof DestroyAction) {
            getCommonBehavior().deleteDestroyAction(elem);
        }
    }

    /**
     * Delete an Instance.
     *
     * @param elem the Instance to be deleted.
     */
    private void deleteInstance(Instance elem) {
        getCommonBehavior().deleteInstance(elem);
        if (elem instanceof DataValue) {
            getCommonBehavior().deleteDataValue(elem);
        } else if (elem instanceof ComponentInstance) {
            getCommonBehavior().deleteComponentInstance(elem);
        } else if (elem instanceof NodeInstance) {
            getCommonBehavior().deleteNodeInstance(elem);
        } else if (elem
                instanceof
                org.omg.uml.behavioralelements.commonbehavior.Object) {
            getCommonBehavior().deleteObject(elem);
            if (elem instanceof LinkObject) {
                getCommonBehavior().deleteLinkObject(elem);
            }
        } else if (elem instanceof SubsystemInstance) {
            getCommonBehavior().deleteSubsystemInstance(elem);
        }
        if (elem instanceof UseCaseInstance) {
            getUseCases().deleteUseCaseInstance(elem);
        }
    }

    /**
     * Delete a StateVertex.
     *
     * @param elem the StateVertex to be deleted
     */
    private void deleteStateVertex(StateVertex elem) {
        getStateMachines().deleteStateVertex(elem);
        if (elem instanceof Pseudostate) {
            getStateMachines().deletePseudostate(elem);
        } else if (elem instanceof SynchState) {
            getStateMachines().deleteSynchState(elem);
        } else if (elem instanceof StubState) {
            getStateMachines().deleteStubState(elem);
        } else if (elem instanceof State) {
            if (elem instanceof CompositeState) {
                getStateMachines().deleteCompositeState(elem);
                if (elem instanceof SubmachineState) {
                    getStateMachines().deleteSubmachineState(elem);
                    if (elem instanceof SubactivityState) {
                        getActivityGraphs().deleteSubactivityState(elem);
                    }
                }
            } else if (elem instanceof SimpleState) {
                getStateMachines().deleteSimpleState(elem);
                if (elem instanceof ActionState) {
                    getActivityGraphs().deleteActionState(elem);
                    if (elem instanceof CallState) {
                        getActivityGraphs().deleteCallState(elem);
                    }
                } else if (elem instanceof ObjectFlowState) {
                    getActivityGraphs().deleteObjectFlowState(elem);
                }
            } else if (elem instanceof FinalState) {
                getStateMachines().deleteFinalState(elem);
            }
        }
    }

}
