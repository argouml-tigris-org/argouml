/* $Id$
 *******************************************************************************
 * Copyright (c) 2007-2012 Tom Morris and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial framework
 *    Bogdan Pistol - initial implementation
 *    Thomas Neustupny
 *    Bob Tarling
 *****************************************************************************/

package org.argouml.model.euml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.model.AbstractModelFactory;
import org.argouml.model.Defaults;
import org.argouml.model.IllegalModelElementConnectionException;
import org.argouml.model.InvalidElementException;
import org.argouml.model.MetaTypes;
import org.argouml.model.Model;
import org.argouml.model.UmlFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Abstraction;
import org.eclipse.uml2.uml.Action;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.Actor;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.ComponentRealization;
import org.eclipse.uml2.uml.Connector;
import org.eclipse.uml2.uml.ControlFlow;
import org.eclipse.uml2.uml.ControlNode;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ElementImport;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.ExecutableNode;
import org.eclipse.uml2.uml.Extend;
import org.eclipse.uml2.uml.ExtensionPoint;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Include;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Lifeline;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.ObjectFlow;
import org.eclipse.uml2.uml.ObjectNode;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Pin;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Reception;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.TemplateParameter;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.UseCase;

/**
 * The implementation of the UmlFactory for EUML2.
 */
class UmlFactoryEUMLImpl implements UmlFactory, AbstractModelFactory {

    private static final Logger LOG =
        Logger.getLogger(UmlFactoryEUMLImpl.class.getName());

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * The meta types factory.
     */
    private MetaTypes metaTypes;

    /**
     * A map of valid connections keyed by the connection type. The constructor
     * builds this from the data in the VALID_CONNECTIONS array
     */
    private Map<Class<? extends Element>, List> validConnectionMap =
        new HashMap<Class<? extends Element>, List>();

    /**
     * A map of the valid model elements that are valid to be contained
     * by other model elements.
     */
    private HashMap<Class<? extends Element>, Class<?>[]> validContainmentMap =
        new HashMap<Class<? extends Element>, Class<?>[]>();

    /**
     * An array of valid connections, the combination of connecting class and
     * node classes must exist as a row in this list to be considered valid.
     * <ul>
     * <li>The 1st column is the connecting element.
     * <li>The 2nd column is the "from" element type.
     * <li>The 3rd column is the "to" element type.
     * <li>The 3rd column is optional, if not given then it is assumed to be
     * the same as the "to" element.
     * <li>The existence of a 4th column indicates that the connection is valid
     * in one direction only.
     * </ul>
     * TODO: This encodes not only what is legal in UML, but also what ArgoUML
     * knows how to create, so not all legal connections are included. Probably
     * should be split into two pieces: 1) legal UML (here) and 2) supported (in
     * ArgoUML application someplace) - tfm - 20060325<p>
     *
     * Most of these are subtypes of Relationship which includes Association,
     * Dependency, Flow, Generalization, Extend, and Include. Dependency
     * includes Binding, Abstraction, Usage, and Permission. AssociationRole and
     * AssociationClass are Associations. The remaining items (Link, Transition,
     * AssociationEnd, Message) are non-Relationship types which ArgoUML treats
     * as connections/edges.
     */
    // TODO: This should be built by reflection from the metamodel - tfm
    //       Update for UML 2.x metamodel if not replaced by reflection
    private static final Class[][] VALID_CONNECTIONS = {
        {Connector.class,  Lifeline.class, },
        {Generalization.class,   Classifier.class, },
        {ComponentRealization.class, Element.class, Component.class},
        {Dependency.class,       Element.class, },
        // Although Usage & Permission are Dependencies, they need to
        // be include separately because of the way lookup works
        {Usage.class,            NamedElement.class, },
        {PackageImport.class,       NamedElement.class, },
        // The following is specifically for Realizations
        // TODO: correction in GEF, it should use InterfaceRealization, not Abstraction
        {Abstraction.class, org.eclipse.uml2.uml.Class.class, Interface.class, null, },
        // The next 3 restrictions for Abstraction seem to be Argo specific
        // not something the UML spec requires - tfm - 20070215
        // There is no need for these because they arn't used by buildConnection() - b00__1
//        {Abstraction.class, org.eclipse.uml2.uml.Class.class, org.eclipse.uml2.uml.Class.class, null, },
//        {Abstraction.class, org.eclipse.uml2.uml.Package.class,org.eclipse.uml2.uml.Package.class, null, },
//        {Abstraction.class, Component.class, Interface.class, null, },
        {Association.class,     Type.class, },
//        {AssociationRole.class,  ClassifierRole.class, },
        {Extend.class,           UseCase.class, },
        {Include.class,          UseCase.class, },
//        {Link.class, Instance.class, },
//        {Transition.class,       StateVertex.class, },
        {Transition.class,       State.class, },
        {AssociationClass.class, Type.class, },
        {Property.class, Classifier.class, Association.class, },
        {ControlFlow.class, ControlNode.class, },
        {ControlFlow.class, ExecutableNode.class, },
        {ControlFlow.class, ControlNode.class, ExecutableNode.class, },
        {ObjectFlow.class, ObjectNode.class, },
        {ObjectFlow.class, Action.class, ObjectNode.class, },
//        {Message.class, ClassifierRole.class },
    };

    /**
     * Constructor.
     *
     * @param implementation
     *            The ModelImplementation.
     */
    public UmlFactoryEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
        metaTypes = modelImpl.getMetaTypes();
        buildValidConnectionMap();
        buildValidContainmentMap();
    }

    public Object buildConnection(Object elementType, Object fromElement,
            Object fromStyle, Object toElement, Object toStyle,
            Object unidirectional, Object namespace)
            throws IllegalModelElementConnectionException {

        if (!isConnectionValid(elementType, fromElement, toElement, true)) {
            throw new IllegalModelElementConnectionException(
                    "Cannot make a " + elementType.getClass().getName() //$NON-NLS-1$
                            + " between a " + fromElement.getClass().getName() //$NON-NLS-1$
                            + " and a " + toElement.getClass().getName()); //$NON-NLS-1$
        }

        Object connection = null;

        if (elementType == metaTypes.getAssociationRole()) {
            connection =
                Model.getCollaborationsFactory().buildAssociationRole(fromElement, toElement);
        } else if (elementType == metaTypes.getAssociation()) {
            // Note for UML2 the aggregation ends are swapped
            connection = modelImpl.getCoreFactory().buildAssociation(
                    (Classifier) fromElement, (AggregationKind) toStyle,
                    (Classifier) toElement, (AggregationKind) fromStyle,
                    (Boolean) unidirectional);
        } else if (elementType == metaTypes.getAssociationEnd()) {
            if (fromElement instanceof Association) {
                connection = modelImpl.getCoreFactory().buildAssociationEnd(
                        toElement, fromElement);
            } else if (fromElement instanceof Classifier) {
                connection = modelImpl.getCoreFactory().buildAssociationEnd(
                        fromElement, toElement);
            }
        } else if (elementType == metaTypes.getAssociationClass()) {
            connection = modelImpl.getCoreFactory().buildAssociationClass(
                    fromElement, toElement);
        } else if (elementType == metaTypes.getAssociationRole()) {
            connection = modelImpl.getCollaborationsFactory().buildAssociationRole(
                    fromElement, fromStyle, toElement, toStyle,
                    (Boolean) unidirectional);
        } else if (elementType == metaTypes.getGeneralization()) {
            connection = modelImpl.getCoreFactory().buildGeneralization(
                    fromElement, toElement);
        } else if (elementType == metaTypes.getPackageImport()) {
            connection = modelImpl.getCoreFactory().buildPackageImport(
                    fromElement, toElement);
        } else if (elementType == metaTypes.getUsage()) {
            connection = modelImpl.getCoreFactory().buildUsage(
                    fromElement, toElement);
        } else if (elementType == metaTypes.getComponentRealization()) {
            connection = modelImpl.getCoreFactory().buildComponentRealization(
                    fromElement, toElement);
        } else if (elementType == metaTypes.getDependency()) {
            connection = modelImpl.getCoreFactory().buildDependency(
                    fromElement, toElement);
        } else if (elementType == metaTypes.getAbstraction()) {
            connection = modelImpl.getCoreFactory().buildRealization(
                    fromElement, toElement, namespace);
        } else if (elementType == metaTypes.getLink()) {
            connection = modelImpl.getCommonBehaviorFactory().buildLink(
                    fromElement, toElement);
        } else if (elementType == metaTypes.getExtend()) {
            // Extend, but only between two use cases. Remember we draw from the
            // extension port to the base port.
            connection = modelImpl.getUseCasesFactory().buildExtend(
                    toElement, fromElement);
        } else if (elementType == metaTypes.getInclude()) {
            connection = modelImpl.getUseCasesFactory().buildInclude(
                    fromElement, toElement);
        } else if (elementType == metaTypes.getControlFlow()) {
            ActivityNode fromActivity = (ActivityNode) fromElement;
            ActivityNode toActivity = (ActivityNode) fromElement;
            ControlFlow cf = UMLFactory.eINSTANCE.createControlFlow();
            cf.setActivity(fromActivity.getActivity());
            cf.setSource(fromActivity);
            cf.setTarget(toActivity);
            connection = cf;
        } else if (elementType == metaTypes.getObjectFlow()) {
            ActivityNode fromObject = (ActivityNode) fromElement;
            ActivityNode toObject = (ActivityNode) fromElement;
            ControlFlow of = UMLFactory.eINSTANCE.createControlFlow();
            of.setActivity(fromObject.getActivity());
            of.setSource(fromObject);
            of.setTarget(toObject);
            connection = of;
        } else if (elementType == metaTypes.getTransition()) {
            connection = modelImpl.getStateMachinesFactory().buildTransition(
                    fromElement, toElement);
        }

        if (connection == null) {
            throw new IllegalModelElementConnectionException(
                    "Cannot make a " + elementType.getClass().getName() //$NON-NLS-1$
                            + " between a " + fromElement.getClass().getName() //$NON-NLS-1$
                            + " and a " + toElement.getClass().getName()); //$NON-NLS-1$
        }

        return connection;
    }

    public Object buildNode(
            final Object elementType,
            final Object container,
            final String propertyName) {
        Object element = buildNode(elementType);
        modelImpl.getCoreHelper().addOwnedElement(container, element);
        return element;
    }

    public Object buildNode(Object elementType, Object container) {
        return buildNode(elementType, container, (String) null);
    }

    public Object buildNode(Object elementType, Object container, String property, Defaults defaults) {
        Object element = buildNode(elementType, container, property);
        if (defaults != null) {
            final Object type = defaults.getDefaultType(elementType);
            final String name = defaults.getDefaultName(elementType);
            if (type != null) {
                modelImpl.getCoreHelper().setType(element, type);
            }
            if (name != null) {
                modelImpl.getCoreHelper().setName(element, name);
            }
        }
        return element;
    }

    public Object buildNode(Object elementType) {
        Object o = null;
        if (elementType == metaTypes.getActor()) {
            o = modelImpl.getUseCasesFactory().createActor();
        } else if (elementType == metaTypes.getTagDefinition()) {
            o = modelImpl.getExtensionMechanismsFactory().createTagDefinition();
        } else if (elementType == metaTypes.getUseCase()) {
            o = modelImpl.getUseCasesFactory().createUseCase();
        } else if (elementType == metaTypes.getUMLClass()) {
            o = modelImpl.getCoreFactory().buildClass();
        } else if (elementType == metaTypes.getInterface()) {
            o = modelImpl.getCoreFactory().buildInterface();
        } else if (elementType == metaTypes.getDataType()) {
            o = modelImpl.getCoreFactory().createDataType();
        } else if (elementType == metaTypes.getPackage()) {
            o = modelImpl.getModelManagementFactory().createPackage();
        } else if (elementType == metaTypes.getProfile()) {
            o = modelImpl.getModelManagementFactory().createProfile();
        } else if (elementType == metaTypes.getModel()) {
            o = modelImpl.getModelManagementFactory().createModel();
        } else if (elementType == metaTypes.getActivity()) {
            o = modelImpl.getActivityGraphsFactory().createActivityGraph();
        } else if (elementType == metaTypes.getCallBehaviorAction()) {
            o = UMLFactory.eINSTANCE.createCallBehaviorAction();
        } else if (elementType == metaTypes.getCreateObjectAction()) {
            o = UMLFactory.eINSTANCE.createCreateObjectAction();
        } else if (elementType == metaTypes.getDestroyObjectAction()) {
            o = UMLFactory.eINSTANCE.createDestroyObjectAction();
        } else if (elementType == metaTypes.getActivityParameterNode()) {
            o = UMLFactory.eINSTANCE.createActivityParameterNode();
        } else if (elementType == metaTypes.getCentralBufferNode()) {
            o = UMLFactory.eINSTANCE.createCentralBufferNode();
        } else if (elementType == metaTypes.getDataStoreNode()) {
            o = UMLFactory.eINSTANCE.createDataStoreNode();
        } else if (elementType == metaTypes.getExpansionNode()) {
            o = UMLFactory.eINSTANCE.createExpansionNode();
        } else if (elementType == metaTypes.getAcceptEventAction()) {
            o = UMLFactory.eINSTANCE.createAcceptEventAction();
        } else if (elementType == metaTypes.getSendSignalAction()) {
            o = UMLFactory.eINSTANCE.createSendSignalAction();
        } else if (elementType == metaTypes.getInputPin()) {
            o = UMLFactory.eINSTANCE.createInputPin();
        } else if (elementType == metaTypes.getOutputPin()) {
            o = UMLFactory.eINSTANCE.createOutputPin();
        } else if (elementType == metaTypes.getState()) {
            o = UMLFactory.eINSTANCE.createState();
        } else if (elementType == metaTypes.getFinalState()) {
            o = UMLFactory.eINSTANCE.createFinalState();
        } else if (elementType == metaTypes.getPort()) {
            o = UMLFactory.eINSTANCE.createPort();
        } else if (elementType == metaTypes.getRegion()) {
            o = UMLFactory.eINSTANCE.createRegion();
        } else if (elementType == metaTypes.getPseudostate()) {
            o = modelImpl.getStateMachinesFactory().createPseudostate();
        } else if (elementType == metaTypes.getActionState()) {
            o = modelImpl.getActivityGraphsFactory().createActionState();
        } else if (elementType == metaTypes.getSubactivityState()) {
            o = modelImpl.getActivityGraphsFactory().createSubactivityState();
        } else if (elementType == metaTypes.getPartition()) {
            o = modelImpl.getActivityGraphsFactory().createPartition();
        } else if (elementType == metaTypes.getStubState()) {
            o = modelImpl.getStateMachinesFactory().createStubState();
        } else if (elementType == metaTypes.getSubmachineState()) {
            o = modelImpl.getStateMachinesFactory().createSubmachineState();
        } else if (elementType == metaTypes.getCompositeState()) {
            o = modelImpl.getStateMachinesFactory().createCompositeState();
        } else if (elementType == metaTypes.getSynchState()) {
            o = modelImpl.getStateMachinesFactory().createSynchState();
        } else if (elementType == metaTypes.getInstanceSpecification()) {
            o = UMLFactory.eINSTANCE.createInstanceSpecification();
        } else if (elementType == metaTypes.getState()) {
            throw new IllegalArgumentException(
                    "Attempt to instantiate abstract type"); //$NON-NLS-1$
        } else if (elementType == metaTypes.getClassifierRole()) {
            o = modelImpl.getCollaborationsFactory().createClassifierRole();
        } else if (elementType == metaTypes.getLifeline()) {
            o = modelImpl.getCollaborationsFactory().createLifeline();
        } else if (elementType == metaTypes.getComponent()) {
            o = modelImpl.getCoreFactory().createComponent();
        } else if (elementType == metaTypes.getNode()) {
            o = modelImpl.getCoreFactory().createNode();
        } else if (elementType == metaTypes.getNodeInstance()) {
            o = modelImpl.getCommonBehaviorFactory().createNodeInstance();
        } else if (elementType == metaTypes.getObject()) {
            o = modelImpl.getCommonBehaviorFactory().createObject();
        } else if (elementType == metaTypes.getComment()) {
            o = modelImpl.getCoreFactory().createComment();
        } else if (elementType == metaTypes.getNamespace()) {
            throw new IllegalArgumentException(
                    "Attempt to instantiate abstract type"); //$NON-NLS-1$
        } else if (elementType == metaTypes.getOperation()) {
            o = modelImpl.getCoreFactory().createOperation();
        } else if (elementType == metaTypes.getEnumeration()) {
            o = modelImpl.getCoreFactory().createEnumeration();
        } else if (elementType == metaTypes.getEnumerationLiteral()) {
            o = modelImpl.getCoreFactory().createEnumerationLiteral();
        } else if (elementType == metaTypes.getStereotype()) {
            o = modelImpl.getExtensionMechanismsFactory().createStereotype();
        } else if (elementType == metaTypes.getAttribute()) {
            o = modelImpl.getCoreFactory().createAttribute();
        } else if (elementType == metaTypes.getSignal()) {
            o = modelImpl.getCommonBehaviorFactory().createSignal();
        } else if (elementType == metaTypes.getException()) {
            o = modelImpl.getCommonBehaviorFactory().createException();
        } else if (elementType == metaTypes.getTransition()) {
            o = modelImpl.getStateMachinesFactory().createTransition();
        } else if (elementType == metaTypes.getParameter()) {
            o = modelImpl.getCoreFactory().createParameter();
        } else if (elementType == metaTypes.getExtensionPoint()) {
            o = modelImpl.getUseCasesFactory().createExtensionPoint();
        } else if (elementType == metaTypes.getReception()) {
            o = modelImpl.getCommonBehaviorFactory().createReception();
        } else if (elementType == metaTypes.getProperty()) {
            o = modelImpl.getCoreFactory().createAttribute();
        } else if (elementType == metaTypes.getSubsystem()) {
            // in UML2 subsystem is a Component with <<subsystem>> stereotype
            // so this must occur after the metaTypes.getComponent() case
            o = modelImpl.getModelManagementFactory().createSubsystem();
        }
        if (!(o instanceof EObject)) {
            throw new IllegalArgumentException(
                    "Attempted to create unsupported element type: " //$NON-NLS-1$
                            + elementType);
        }
        return o;
    }

    public void delete(final Object elem) {
        if (!(elem instanceof EObject)) {
            throw new IllegalArgumentException(
                    "elem must be instance of EObject"); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                EcoreUtil.delete((EObject) elem);
            }
        };
        modelImpl.getEditingDomain().getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run, "Remove from the model the element #", //$NON-NLS-1$
                        elem));
    }

    public boolean isRemoved(Object o) {
        // This triggers some warnings (in logs) because some elements are
        // created without an owner (and eResource is null)
        // TODO: fix this
        // The warning log (if we would not add the EObject to a resource) would
        // looks like this: "...WARN [AWT-EventQueue-0] Encountered deleted
        // object during delete of..."
        if (o instanceof Element) {
            return ((Element) o).eResource() == null;
        }
        throw new IllegalArgumentException("Not an Element : " + o); //$NON-NLS-1$
    }

    public boolean isConnectionType(Object connectionType) {
        // If our map has any entries for this type, it's a connection type
        return (validConnectionMap.get(connectionType) != null);
    }


    public boolean isConnectionValid(Object connectionType, Object fromElement,
            Object toElement, boolean checkWFR) {
        // Get the list of valid model item pairs for the given connection type
        List validItems = (ArrayList) validConnectionMap.get(connectionType);
        if (validItems == null) {
            return false;
        }
        // See if there's a pair in this list that match the given
        // model elements
        Iterator it = validItems.iterator();
        while (it.hasNext()) {
            Class[] modeElementPair = (Class[]) it.next();
            if (modeElementPair[0].isInstance(fromElement)
                && modeElementPair[1].isInstance(toElement)) {
                if (checkWFR) {
                    return isConnectionWellformed(
                            (Class) connectionType,
                            (Element) fromElement,
                            (Element) toElement);
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    // TODO: Can we get this info from UML2 plugin?
    // Perhaps collect all References in the metamodel, filter for those
    // which represent containments and find the types on either end - tfm
    public boolean isContainmentValid(Object metaType, Object container) {

        // find the passed in container in validContainmentMap
        for (Class<?> containerType : validContainmentMap.keySet()) {

            if (containerType.isInstance(container)) {
                // determine if metaType is a valid element for container
                Class<?>[] validElements =
                    validContainmentMap.get(containerType);

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
            Class connectionType,
            Element fromElement,
            Element toElement) {

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

    private void buildValidConnectionMap() {
        // A list of valid connections between elements, the
        // connection type first and then the elements to be connected

        Class<Element> connection = null;
        for (int i = 0; i < VALID_CONNECTIONS.length; ++i) {
            connection = VALID_CONNECTIONS[i][0];
            List validItems = (ArrayList) validConnectionMap.get(connection);
            if (validItems == null) {
                validItems = new ArrayList();
                validConnectionMap.put(connection, validItems);
            }
            if (VALID_CONNECTIONS[i].length < 3) {
                // If there isn't a 3rd column then this represents a connection
                // of elements of the same type.
                Object[] modeElementPair = new Class[2];
                modeElementPair[0] = VALID_CONNECTIONS[i][1];
                modeElementPair[1] = VALID_CONNECTIONS[i][1];
                validItems.add(modeElementPair);
            } else {
                // If there is a 3rd column then this represents a connection
                // of between 2 different types of element.
                Object[] modeElementPair = new Class[2];
                modeElementPair[0] = VALID_CONNECTIONS[i][1];
                modeElementPair[1] = VALID_CONNECTIONS[i][2];
                validItems.add(modeElementPair);
                // If the array hasn't been flagged to indicate otherwise
                // swap elements the elemnts and add again.
                if (VALID_CONNECTIONS[i].length < 4
                        && VALID_CONNECTIONS[i][1] != VALID_CONNECTIONS[i][2]) {
                    Object[] reversedModeElementPair = new Class[2];
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

        validContainmentMap.put(Element.class,
                new Class<?>[] {
                });

        // specifies valid elements for a Region to contain
        validContainmentMap.put(Region.class,
            new Class<?>[] {
                State.class, Pseudostate.class
            });

        // specifies valid elements for a Region to contain
        validContainmentMap.put(State.class,
            new Class<?>[] {
                Region.class
            });

        // specifies valid elements for a Region to contain
        validContainmentMap.put(StateMachine.class,
            new Class<?>[] {
                Region.class
            });

        // specifies valid elements for a Package to contain
        validContainmentMap.put(Package.class,
            new Class<?>[] {
                Package.class, Actor.class,
                UseCase.class, org.eclipse.uml2.uml.Class.class,
                Interface.class, Component.class,
                Node.class, Enumeration.class, DataType.class,
                Signal.class
            });


        // specifies valid elements for a Package to contain
        validContainmentMap.put(Package.class,
            new Class<?>[] {
                Package.class, Actor.class,
                UseCase.class, org.eclipse.uml2.uml.Class.class,
                Interface.class, Component.class,
                Node.class, Enumeration.class, DataType.class,
                Signal.class
            });

        // specifies valid elements for a Package to contain
        validContainmentMap.put(Stereotype.class,
            new Class<?>[] {
                Property.class
            });

        // valid elements for a Profile to contain
        validContainmentMap.put(Profile.class,
            new Class<?>[] {
                Stereotype.class, ElementImport.class, PackageImport.class
            });

        // specifies valid elements for a class to contain
        validContainmentMap.put(org.eclipse.uml2.uml.Class.class,
            new Class<?>[] {
                Property.class, Operation.class,
                org.eclipse.uml2.uml.Class.class, Reception.class
            });

        // specifies valid elements for a classifier to contain
        validContainmentMap.put(Classifier.class,
            new Class<?>[] {
                TemplateParameter.class
            });

        // specifies valid elements for an Interface to contain
        validContainmentMap.put(Interface.class,
                new Class<?>[] {
                    Property.class, Operation.class,
                    Reception.class
                });

        // specifies valid elements for a Signal to contain
        validContainmentMap.put(Signal.class,
                new Class<?>[] {
                    Operation.class, Property.class
                });

        // specifies valid elements for an Actor to contain
        validContainmentMap.put(Actor.class,
                new Class<?>[] {
                    Operation.class,
                    Reception.class
                });

        // specifies valid elements for a Use Case to contain
        validContainmentMap.put(UseCase.class,
                new Class<?>[] {
                    ExtensionPoint.class, Property.class,
                    Operation.class, Reception.class
                });

        // specifies valid elements for a Use Case to contain
        validContainmentMap.put(Extend.class,
                new Class<?>[] {
                    ExtensionPoint.class
                });

        // specifies valid elements for a Component to contain
        validContainmentMap.put(Component.class,
                new Class<?>[] {
                    Operation.class,
                    Reception.class
                });

        // specifies valid elements for a Node to contain
        validContainmentMap.put(Node.class,
                new Class<?>[] {
                    Operation.class,
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
                    Operation.class,
                    Reception.class
                });

        // specifies valid elements for an Operation to contain
        validContainmentMap.put(Operation.class,
                new Class<?>[] {
                    Parameter.class
                });

        // valid elements for a Stereotype to contain
        validContainmentMap.put(Stereotype.class,
                new Class<?>[] {
                    Property.class
                });

        // valid elements for an Action to contain
        validContainmentMap.put(Action.class,
                new Class<?>[] {
                    Pin.class
                });
    }

    public void deleteExtent(Object element) {
        Resource resource = ((EObject) element).eResource();
        if (resource != null) {
            modelImpl.unloadResource(resource);
        } else {
            LOG.log(Level.WARNING, "Tried to delete null resource"); //$NON-NLS-1$
            throw new InvalidElementException(
                    element != null ? element.toString() : "Null" ); //$NON-NLS-1$
        }
    }

    public Collection getExtentElements(String extentName) {
        if (extentName != null && extentName.startsWith("pathmap://UML_")) {
            // trying to get a built-in standard profile from eclipse UML2
            try {
                URI uri = URI.createURI(extentName);
                Resource r = modelImpl.getEditingDomain().getResourceSet().getResource(uri, true);
                modelImpl.getReadOnlyMap().put(r, Boolean.TRUE);
                return r.getContents();
            } catch (Exception ex) {
                LOG.log(Level.WARNING, "failed to get resource: " + extentName); //$NON-NLS-1$
            }
        }
        return null;
    }

    public Collection getExtentPackages(String extentName) {
        Collection elements = getExtentElements(extentName);
        if (elements != null) {
            Collection<Object> result = new ArrayList<Object>();
            for (Object element : elements) {
                if (element instanceof Package) {
                    result.add(element);
                }
            }
        }
        return null;
    }

}
