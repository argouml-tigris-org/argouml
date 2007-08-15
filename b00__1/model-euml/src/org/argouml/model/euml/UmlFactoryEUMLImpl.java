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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.argouml.model.AbstractModelFactory;
import org.argouml.model.IllegalModelElementConnectionException;
import org.argouml.model.MetaTypes;
import org.argouml.model.UmlFactory;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EcoreFactoryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Abstraction;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Extend;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Include;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.UseCase;


/**
 * The implementation of the UmlFactory for EUML2.
 */
class UmlFactoryEUMLImpl implements UmlFactory, AbstractModelFactory {

    private static final String IS_REMOVED_NAME = 
        "http://argouml.tigris.org/argouml-core-model-euml/isRemoved";
    
    private EAnnotation removedAnnotation;
    
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
    private Map validConnectionMap = new HashMap();
    
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
    private static final Object[][] VALID_CONNECTIONS = {
        {Generalization.class,   Classifier.class, },
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
//        {AssociationEnd.class, Classifier.class, Association.class, },
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

        removedAnnotation = EcoreFactoryImpl.eINSTANCE.createEAnnotation();
        removedAnnotation.setSource(IS_REMOVED_NAME);
    }

    public Object buildConnection(Object elementType, Object fromElement,
            Object fromStyle, Object toElement, Object toStyle,
            Object unidirectional, Object namespace)
        throws IllegalModelElementConnectionException {
        
        IllegalModelElementConnectionException exception = new IllegalModelElementConnectionException("Cannot make a "
                + elementType.getClass().getName() + " between a "
                + fromElement.getClass().getName() + " and a "
                + toElement.getClass().getName()); 
        
        if (!isConnectionValid(elementType, fromElement, toElement, true)) {
            throw exception;
        }

        Object connection = null;

        if (elementType == metaTypes.getAssociation()) {
            connection =
                modelImpl.getCoreFactory().buildAssociation((Classifier) fromElement,
                    (AggregationKind) fromStyle, (Classifier) toElement,
                    (AggregationKind) toStyle, (Boolean) unidirectional);
        } else if (elementType == metaTypes.getAssociationEnd()) {
            if (fromElement instanceof Association) {
                connection =
                    modelImpl.getCoreFactory().buildAssociationEnd(toElement, fromElement);
            } else if (fromElement instanceof Classifier) {
                connection =
                    modelImpl.getCoreFactory().buildAssociationEnd(fromElement, toElement);
            }
        } else if (elementType
                == metaTypes.getAssociationClass()) {
            connection =
                modelImpl.getCoreFactory().buildAssociationClass(fromElement, toElement);
        } else if (elementType == metaTypes.getAssociationRole()) {
            connection =
                modelImpl.getCollaborationsFactory().buildAssociationRole(fromElement,
                    fromStyle, toElement, toStyle, (Boolean) unidirectional);
        } else if (elementType == metaTypes.getGeneralization()) {
            connection = modelImpl.getCoreFactory().buildGeneralization(fromElement, toElement);
        } else if (elementType == metaTypes.getPackageImport()) {
            connection = modelImpl.getCoreFactory().buildPackageImport(fromElement, toElement);
        } else if (elementType == metaTypes.getUsage()) {
            connection = modelImpl.getCoreFactory().buildUsage(fromElement, toElement);
        } else if (elementType == metaTypes.getDependency()) {
            connection = modelImpl.getCoreFactory().buildDependency(fromElement, toElement);
        } else if (elementType == metaTypes.getAbstraction()) {
            connection =
                modelImpl.getCoreFactory().buildRealization(fromElement, toElement, namespace);
        } else if (elementType == metaTypes.getLink()) {
            connection = modelImpl.getCommonBehaviorFactory().buildLink(fromElement, toElement);
        } else if (elementType == metaTypes.getExtend()) {
            // Extend, but only between two use cases. Remember we draw from the
            // extension port to the base port.
            connection = modelImpl.getUseCasesFactory().buildExtend(toElement, fromElement);
        } else if (elementType == metaTypes.getInclude()) {
            connection = modelImpl.getUseCasesFactory().buildInclude(fromElement, toElement);
        } else if (elementType == metaTypes.getTransition()) {
            connection =
                modelImpl.getStateMachinesFactory().buildTransition(fromElement, toElement);
        }

        if (connection == null) {
            throw exception;
        }

        return connection;
    }

    public Object buildNode(Object elementType) {
        if (elementType == metaTypes.getActor()) {
            return modelImpl.getUseCasesFactory().createActor();
        } else if (elementType == metaTypes.getUseCase()) {
            return modelImpl.getUseCasesFactory().createUseCase();
        } else if (elementType == metaTypes.getUMLClass()) {
            return modelImpl.getCoreFactory().buildClass();
        } else if (elementType == metaTypes.getInterface()) {
            return modelImpl.getCoreFactory().buildInterface();
        } else if (elementType == metaTypes.getDataType()) {
            return modelImpl.getCoreFactory().createDataType();
        } else if (elementType == metaTypes.getPackage()) {
            return modelImpl.getModelManagementFactory().createPackage();
        } else if (elementType == metaTypes.getModel()) {
            return modelImpl.getModelManagementFactory().createModel();
        } else if (elementType == metaTypes.getInstance()) {
            throw new IllegalArgumentException(
                    "Attempt to instantiate abstract type");
        } else if (elementType == metaTypes.getSubsystem()) {
            return modelImpl.getModelManagementFactory().createSubsystem();
        } else if (elementType == metaTypes.getCallState()) {
            return modelImpl.getActivityGraphsFactory().createCallState();
        } else if (elementType == metaTypes.getSimpleState()) {
            return modelImpl.getStateMachinesFactory().createSimpleState();
        } else if (elementType == metaTypes.getFinalState()) {
            return modelImpl.getStateMachinesFactory().createFinalState();
        } else if (elementType == metaTypes.getPseudostate()) {
            return modelImpl.getStateMachinesFactory().createPseudostate();
        } else if (elementType == metaTypes.getObjectFlowState()) {
            return modelImpl.getActivityGraphsFactory().createObjectFlowState();
        } else if (elementType == metaTypes.getActionState()) {
            return modelImpl.getActivityGraphsFactory().createActionState();
        } else if (elementType == metaTypes.getSubactivityState()) {
            return modelImpl.getActivityGraphsFactory().createSubactivityState();
        } else if (elementType == metaTypes.getPartition()) {
            return modelImpl.getActivityGraphsFactory().createPartition();
        } else if (elementType == metaTypes.getStubState()) {
            return modelImpl.getStateMachinesFactory().createStubState();
        } else if (elementType == metaTypes.getSubmachineState()) {
            return modelImpl.getStateMachinesFactory().createSubmachineState();
        } else if (elementType == metaTypes.getCompositeState()) {
            return modelImpl.getStateMachinesFactory().createCompositeState();
        } else if (elementType == metaTypes.getSynchState()) {
            return modelImpl.getStateMachinesFactory().createSynchState();
        } else if (elementType == metaTypes.getState()) {
            throw new IllegalArgumentException(
                    "Attempt to instantiate abstract type");
        } else if (elementType == modelImpl.getMetaTypes().getSimpleState()) {
            return modelImpl.getStateMachinesFactory().createSimpleState();
        } else if (elementType == metaTypes.getClassifierRole()) {
            return modelImpl.getCollaborationsFactory().createClassifierRole();
        } else if (elementType == metaTypes.getComponent()) {
            return modelImpl.getCoreFactory().createComponent();
        } else if (elementType == metaTypes.getComponentInstance()) {
            return modelImpl.getCommonBehaviorFactory().createComponentInstance();
        } else if (elementType == metaTypes.getNode()) {
            return modelImpl.getCoreFactory().createNode();
        } else if (elementType == metaTypes.getNodeInstance()) {
            return modelImpl.getCommonBehaviorFactory().createNodeInstance();
        } else if (elementType == metaTypes.getObject()) {
            return modelImpl.getCommonBehaviorFactory().createObject();
        } else if (elementType == metaTypes.getComment()) {
            return modelImpl.getCoreFactory().createComment();
        } else if (elementType == metaTypes.getNamespace()) {
            throw new IllegalArgumentException(
                    "Attempt to instantiate abstract type");
        } else if (elementType == metaTypes.getOperation()) {
            return modelImpl.getCoreFactory().createOperation();
        } else if (elementType == metaTypes.getEnumeration()) {
            return modelImpl.getCoreFactory().createEnumeration();
        } else if (elementType == metaTypes.getStereotype()) {
            return modelImpl.getExtensionMechanismsFactory().createStereotype();
        } else if (elementType == metaTypes.getAttribute()) {
            return modelImpl.getCoreFactory().createAttribute();
        } else if (elementType == metaTypes.getSignal()) {
            return modelImpl.getCommonBehaviorFactory().createSignal();
        } else if (elementType == metaTypes.getException()) {
            return modelImpl.getCommonBehaviorFactory().createException();
        } else if (elementType == metaTypes.getTransition()) {
            return modelImpl.getStateMachinesFactory().createTransition();
        }
            
        throw new IllegalArgumentException(
                "Attempted to create unsupported model element type: " 
                + elementType);
    }

    public void delete(Object elem) {
        // TODO: We probably need a better way of doing this - tfm
        // Add an annotation saying that we've deleted the element
        if (!(elem instanceof EObject)) {
            throw new IllegalArgumentException("elem must be instance of EObject"); //$NON-NLS-1$
        }
        EcoreUtil.delete((EObject) elem);
//        ((EModelElement) elem).getEAnnotations().add(removedAnnotation);
//        ((Element) elem).destroy();
    }
    
    public boolean isRemoved(Object o) {
        if (o instanceof Element) {
            return ((Element) o).eResource() != null;
//            return ((EModelElement) o).getEAnnotation(IS_REMOVED_NAME) != null;
        }
        throw new IllegalArgumentException("Not an Element : " + o);
    }

    public boolean isConnectionType(Object connectionType) {
        // TODO Auto-generated method stub
        throw new NotYetImplementedException();
    }

    public boolean isConnectionValid(Object connectionType, Object fromElement,
            Object toElement) {
        return isConnectionValid(connectionType, fromElement, toElement, true);
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

        Object connection = null;
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

}
