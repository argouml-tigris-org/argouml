// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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

import java.util.*;

// NS-UML imports:
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.state_machines.MCompositeState;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.model_management.*;

// GEF imports:
import org.tigris.gef.base.*;
// import org.tigris.gef.presentation.*;
// import org.tigris.gef.util.*;

// Diagram model imports:
import org.argouml.model.uml.foundation.core.*;
import org
    .argouml
    .model
    .uml
    .foundation
    .extensionmechanisms
    .ExtensionMechanismsFactory;
import org
    .argouml
    .model
    .uml
    .foundation
    .extensionmechanisms
    .ExtensionMechanismsHelper;
import org.argouml.uml.*;
// import org.argouml.uml.diagram.ui.*;
// import org.argouml.uml.diagram.deployment.ui.*;
// import org.argouml.uml.diagram.static_structure.ui.*;

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
 * public static boolean isA<TYPE>(Object handle)
 * public static boolean is<PROPERTY>(Object handle)
 * <p>
 *
 * Signature for all getters in this Facade:
 * public static Object get<TYPE>(Object handle) - 1..1
 * public static Iterator get<TYPES>(Object handle) - 0..*
 * public static String getName(Object handle) - Name
 * <p>
 * 
 * @stereotype utility
 * @author Linus Tolke
 */
public class ModelFacade {
    /** Constructor that forbids instantiation.
     */
    private ModelFacade() {
    }

    ////////////////////////////////////////////////////////////////
    // Recognizer methods for the UML model (in alphabetic order)

    /** Recognizer for Association.
     *
     * @param handle candidate
     * @returns true if handle is an Association
     */
    public static boolean isAAssociation(Object handle) {
        return handle instanceof MAssociation;
    }

    /** Recognizer for AssociationEnd.
     *
     * @param handle candidate
     * @returns true if handle is an AssociationEnd
     */
    public static boolean isAAssociationEnd(Object handle) {
        return handle instanceof MAssociationEnd;
    }

    /** Recognizer for AssociationRole
     *
     * @param handle candidate
     * @returns true if handle is an AssociationRole
     */
    public static boolean isAAssociationRole(Object handle) {
        return handle instanceof MAssociationRole;
    }

    /** Recognizer for abstract classes and operations.
     *
     * @param handle candidate
     * @returns true if handle is abstract.
     */
    public static boolean isAbstract(Object handle) {
        if (handle instanceof MOperation)
            return ((MOperation)handle).isAbstract();
        if (handle instanceof MGeneralizableElement)
            return ((MGeneralizableElement)handle).isAbstract();
        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Recognizer for Class
     *
     * @param handle candidate
     * @returns true if handle is a Class
     */
    public static boolean isAClass(Object handle) {
        return handle instanceof MClass;
    }

    /** Recognizer for Classifier
     *
     * @param handle candidate
     * @returns true if handle is a Classifier
     */
    public static boolean isAClassifier(Object handle) {
        return handle instanceof MClassifier;
    }

    /** Recognizer for Classifier
     *
     * @param handle candidate
     * @returns true if handle is a Classifier
     */
    public static boolean isACompositeState(Object handle) {
        return handle instanceof MCompositeState;
    }

    /** Recognizer for Expression
     *
     * @param handle candidate
     * @returns true if handle is an Expression
     */
    public static boolean isAExpression(Object handle) {
        return handle instanceof MExpression;
    }

    /** Recognizer for Feature
     *
     * @param handle candidate
     * @returns true if handle is a Feature
     */
    public static boolean isAFeature(Object handle) {
        return handle instanceof MFeature;
    }

    /** Recognizer for GeneralizableElement
     *
     * @param handle candidate
     * @returns true if handle is a GeneralizableElement
     */
    public static boolean isAGeneralizableElement(Object handle) {
        return handle instanceof MGeneralizableElement;
    }

    /** Recognizer for GeneralizableElement
     *
     * @param handle candidate
     * @returns true if handle is a GeneralizableElement
     */
    public static boolean isAGeneralization(Object handle) {
        return handle instanceof MGeneralization;
    }

    /** Recognizer for Interaction
     *
     * @param handle candidate
     * @returns true if handle is a Interaction
     */
    public static boolean isAInteraction(Object handle) {
        return handle instanceof MInteraction;
    }

    /** Recognizer for Interface
     *
     * @param handle candidate
     * @returns true if handle is a Interface
     */
    public static boolean isAInterface(Object handle) {
        return handle instanceof MInterface;
    }

    /** Recognizer for ModelElement
     *
     * @param handle candidate
     * @returns true if handle is a ModelElement
     */
    public static boolean isAModelElement(Object handle) {
        return handle instanceof MModelElement;
    }

    /** Recognizer for Namespace
     *
     * @param handle candidate
     * @returns true if handle is a Namespace
     */
    public static boolean isANamespace(Object handle) {
        return handle instanceof MNamespace;
    }

    /** 
     * Recognizer for Operation
     *
     * @param handle candidate
     * @returns true if handle is an Operation
     */
    public static boolean isAOperation(Object handle) {
        return handle instanceof MOperation;
    }

    /** Recognizer for Package
     *
     * @param handle candidate
     * @returns true if handle is a Package
     */
    public static boolean isAPackage(Object handle) {
        return handle instanceof MPackage;
    }

    /** Recognizer for StateMachine
     *
     * @param handle candidate
     * @returns true if handle is a StateMachine
     */
    public static boolean isAStateMachine(Object handle) {
        return handle instanceof MStateMachine;
    }

    /** Recognizer for StateVertex
     *
     * @param handle candidate
     * @returns true if handle is a StateVertex
     */
    public static boolean isAStateVertex(Object handle) {
        return handle instanceof MStateVertex;
    }

    /** Recognizer for StructuralFeature
     *
     * @param handle candidate
     * @returns true if handle is a StructuralFeature
     */
    public static boolean isAStructuralFeature(Object handle) {
        return handle instanceof MStructuralFeature;
    }

    /** Recognizer for Transition
     *
     * @param handle candidate
     * @returns true if handle is a Transition
     */
    public static boolean isATransition(Object handle) {
        return handle instanceof MTransition;
    }

    /** Recognizer for attributes that are changeable
     *
     * @param handle candidate
     * @returns true if handle is changeable
     */
    public static boolean isChangeable(Object handle) {
        if (handle instanceof MAttribute) {
            MAttribute a = (MAttribute)handle;
            return MChangeableKind.CHANGEABLE.equals(a.getChangeability());
        }
        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Recognizer for attributes with classifier scope.
     *
     * @param handle candidate
     * @returns true if handle has classifier scope.
     */
    public static boolean isClassifierScope(Object handle) {
        if (handle instanceof MAttribute) {
            MAttribute a = (MAttribute)handle;
            return MScopeKind.CLASSIFIER.equals(a.getOwnerScope());
        }
        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Recognizer for constructor.
     *
     * @param handle candidate
     * @returns true if handle is a constructor.
     */
    public static boolean isConstructor(Object handle) {
        MStereotype createStereoType =
            ExtensionMechanismsFactory.getFactory().buildStereotype(
                new MOperationImpl(),
                "create");
        return ExtensionMechanismsHelper.getHelper().isValidStereoType(
            handle,
            createStereoType);
    }

    /**
     * Returns true if a given associationend is a composite.
     * @param handle
     * @return boolean
     */
    public static boolean isComposite(Object handle) {
        if (isAAssociationEnd(handle)) {
            boolean composite = false;
            MAssociationEnd end = (MAssociationEnd)handle;
            if (end.getAggregation() != null
                && end.getAggregation().equals(MAggregationKind.COMPOSITE))
                composite = true;
            return composite;
        }
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Recognizer for attributes that are initialized.
     *
     * @param handle candidate
     * @param true if the attribute is initialized.
     */
    public static boolean isInitialized(Object handle) {
        if (handle instanceof MAttribute) {
            MExpression init = ((MAttribute)handle).getInitialValue();

            if (init != null
                && init.getBody() != null
                && init.getBody().trim().length() > 0)
                return true;
            return false;
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Recognizer for attributes with instance scope.
     *
     * @param handle candidate
     * @returns true if handle has instance scope.
     */
    public static boolean isInstanceScope(Object handle) {
        if (handle instanceof MFeature) {
            MFeature a = (MFeature)handle;
            return MScopeKind.INSTANCE.equals(a.getOwnerScope());
        }
        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Recognizer for leafs
     *
     * @param handle candidate GeneralizableElement
     * @returns true if handle is a leaf
     */
    public static boolean isLeaf(Object handle) {

        if (handle instanceof MGeneralizableElement) {
            return ((MGeneralizableElement)handle).isLeaf();
        }
        if (handle instanceof MOperation) {
            return ((MOperation)handle).isLeaf();
        }
        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Recognizer for Navigable elements
     *
     * @param handle candidate
     * @returns true if handle is navigable
     */
    public static boolean isNavigable(Object handle) {
        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd)handle).isNavigable();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Recognizer for primary objects.
     * A primary object is an object that is created by the parser or 
     * by a user.
     * Object that are created when importing some other object are not.
     *
     * @param handle candidate
     * @returns true if primary object.
     */
    public static boolean isPrimaryObject(Object handle) {
        if (handle instanceof MModelElement) {
            MModelElement element = (MModelElement)handle;
            for (Iterator i = element.getTaggedValues().iterator();
                i.hasNext();
                ) {
                MTaggedValue tv = (MTaggedValue)i.next();
                if ((MMUtil.GENERATED_TAG).equals(tv.getTag())) {
                    return false;
                }
            }
            return true;
        }
        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Recognizer for attributes with private
     *
     * @param handle candidate
     * @returns true if handle has private
     */
    public static boolean isPrivate(Object handle) {
        if (handle instanceof MBehavioralFeature) {
            MBehavioralFeature bf = (MBehavioralFeature)handle;
            return MVisibilityKind.PRIVATE.equals(bf.getVisibility());
        }
        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Recognizer for realize
     *
     * @param handle candidate
     * @returns true if handle has a realize stereotype
     */
    public static boolean isRealize(Object handle) {
        return isStereotype(handle, "realize");
    }

    /** Recognizer for return
     *
     * @param handle candidate parameter
     * @returns true if handle is a return parameter.
     */
    public static boolean isReturn(Object handle) {
        if (handle instanceof MParameter) {
            MParameter p = (MParameter)handle;
            return MParameterDirectionKind.RETURN.equals(p.getKind());
        }
        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Recognizer for singleton.
     *
     * @param handle candidate
     * @returns true if handle is a singleton.
     */
    public static boolean isSingleton(Object handle) {
        return isStereotype(handle, "singleton");
    }

    /** Recognizer for a specific stereotype.
     *
     * @param handle candidate
     * @param stereotype a string that is the stereotype name.
     * @return true if handle is a singleton.
     */
    public static boolean isStereotype(Object handle, String stereotypename) {
        if (handle instanceof MModelElement) {
            MModelElement element = (MModelElement)handle;
            MStereotype meSt = element.getStereotype();

            if (meSt == null)
                return false;

            String name = meSt.getName();
            if (name == null)
                return false;

            return name.equalsIgnoreCase(stereotypename);
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }
    
    public static boolean isTop(Object handle) {
        return isACompositeState(handle) && ((MCompositeState)handle).getStateMachine() != null;
    }

    /** Recognizer for type.
     *
     * @param handle candidate
     * @returns true if handle is a type.
     */
    public static boolean isType(Object handle) {
        return isStereotype(handle, "type");
    }

    /** Recognizer for utility.
     *
     * @param handle candidate
     * @returns true if handle is a utility.
     */
    public static boolean isUtility(Object handle) {
        return isStereotype(handle, "utility");
    }

    ////////////////////////////////////////////////////////////////
    // Recognizer methods for the diagrams (in alphabetic order)

    /** Recognizer for Diagram.
     *
     * @param handle candidate
     * @returns true if handle is a diagram.
     */
    public static boolean isADiagram(Object handle) {
        return handle instanceof Diagram;
    }

    ////////////////////////////////////////////////////////////////
    // Getters for the UML model (in alphabetic order)

    /** The list of Association Ends
     *
     * @param handle the object that we get the association ends from.
     * @return Iterator with association ends.
     */
    public static Collection getAssociationEnds(Object handle) {
        if (handle instanceof MClassifier) {
            Collection endc = ((MClassifier)handle).getAssociationEnds();
            return endc;
        }

        //...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** The list of Attributes.
     *
     * @param handle classifier to examine.
     * @return iterator with attributes.
     */
    public static Collection getAttributes(Object handle) {
        if (handle instanceof MClassifier) {
            MClassifier c = (MClassifier)handle;
            
            return CoreHelper.getHelper().getStructuralFeatures(c);
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Get the behaviors of a Modelelement.
     *
     *
     * @param handle modelelement to examine.
     * @return the behaviors.
     */
    public static Collection getBehaviors(Object handle) {
        if (isAModelElement(handle))
            return ((MModelElement)handle).getBehaviors();
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Get the body of an Expression.
     *
     *
     * @param handle expression.
     * @return the body.
     */
    public static Object getBody(Object handle) {
        if (handle instanceof MExpression)
            return ((MExpression)handle).getBody();
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Get the child of a generalization.
     *
     * TODO: Check that the concepts parent and child exist in the UML model.
     *
     * @param handle generalization.
     * @return the child.
     */
    public static Object getChild(Object handle) {
        if (handle instanceof MGeneralization) {
            return ((MGeneralization)handle).getChild();
        }
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /**
     * Get the children of some generalizable element
     */
    public static Collection getChildren(Object handle) {
        if (isAGeneralizableElement(handle)) {
            return ((MGeneralizableElement)handle).getChildren();
        }
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** The list of Connections or AssociationEnds to an Association.
     *
     * @param handle to the association.
     * @return an Iterator with all connections.
     */
    public static Iterator getConnections(Object handle) {
        if (handle instanceof MAssociation) {
            return ((MAssociation)handle).getConnections().iterator();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** The list of Generalizations from a GeneralizableElement.
     *
     * @param handle GeneralizableElement to retrieve from.
     * @return Iterator with Generalizations
     */
    public static Iterator getGeneralizations(Object handle) {
        if (handle instanceof MGeneralizableElement) {
            MGeneralizableElement ge = (MGeneralizableElement)handle;
            return ge.getGeneralizations().iterator();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /**
     * Returns the incoming transitions for some statevertex
     * @param handle
     * @return Collection
     */
    public static Collection getIncomings(Object stateVertex) {
        if (isAStateVertex(stateVertex)) {
            return ((MStateVertex)stateVertex).getIncomings();
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + stateVertex);
    }

    /**
     * Returns the messages belonging to some interaction
     * @param handle
     * @return Collection
     */
    public static Collection getMessages(Object handle) {
        if (isAInteraction(handle)) {
            return ((MInteraction)handle).getMessages();
        }
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Get the namespace of an element.
     *
     * @param handle the model element that we are getting the namespace of
     * @returns the namespace (or null)
     */
    public static Object getNamespace(Object handle) {
        if (handle instanceof MModelElement)
            return ((MModelElement)handle).getNamespace();
        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** The list of operations
     *
     * @param handle classifier to examine.
     * @return Collection with operations.
     */
    public static Collection getOperations(Object handle) {
        if (handle instanceof MClassifier) {
            MClassifier c = (MClassifier)handle;

            return CoreHelper.getHelper().getOperations(c);
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** The list of Operations of this classifier and all inherited.
     *
     * @param handle classifier to examine.
     * @return Iterator with operations.
     */
    public static Iterator getOperationsInh(Object handle) {
        if (handle instanceof MClassifier) {
            MClassifier c = (MClassifier)handle;

            // TODO: We are converting back and forth between collections and
            // iterators. I (Linus) prefer iterators.
            return CoreHelper.getHelper().getOperationsInh(c).iterator();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Returns the list of Transitions outgoing from the given stateVertex.
     *
     * @param statevertex 
     * @return Collection
     */
    public static Collection getOutgoings(Object stateVertex) {
        if (ModelFacade.isAStateVertex(stateVertex)) {
            return ((MStateVertex)stateVertex).getOutgoings();
        }
        throw new IllegalArgumentException(
            "Unrecognized object " + stateVertex);
    }

    /** The list of Associations Ends connected to this association end
     *
     * @param handle association end to start from
     * @returns Iterator with all connected association ends.
     */
    public static Iterator getOtherAssociationEnds(Object handle) {
        if (handle instanceof MAssociationEnd) {
            MAssociation a = ((MAssociationEnd)handle).getAssociation();

            if (a == null)
                return emptyIterator();

            Collection allEnds = a.getConnections();
            if (allEnds == null)
                return emptyIterator();

            // TODO: An Iterator filter would be nice here instead of the 
            // mucking around with the Collection.
            allEnds = new ArrayList(allEnds);
            allEnds.remove(handle);
            return allEnds.iterator();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** The list of owned elements of the the package.
     *
     * @param handle package to retrieve from.
     * @return Iterator with operations
     */
    public static Collection getOwnedElements(Object handle) {
        if (handle instanceof MNamespace) {
            return ((MNamespace)handle).getOwnedElements();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Get the parameters of an operation.
     *
     * @param handle operation to retrieve from
     * @return Iterator with operations.
     */
    public static Iterator getParameters(Object handle) {
        if (handle instanceof MOperation) {
            return ((MOperation)handle).getParameters().iterator();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Get the parent of a generalization.
     *
     * TODO: Check that the concepts parent and child exist in the UML model.
     *
     * @param handle generalization.
     * @return the parent.
     */
    public static Object getParent(Object handle) {
        if (handle instanceof MGeneralization) {
            return ((MGeneralization)handle).getParent();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /**
     * Gets the source for some given transitions.
     * @param handle
     * @return Object
     */
    public static Object getSource(Object handle) {
        if (isATransition(handle)) {
            return ((MTransition)handle).getSource();
        }
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** The list of Specializations from a GeneralizableElement.
     *
     * @param handle GeneralizableElement to retrieve from.
     * @return Iterator with Specializations.
     */
    public static Iterator getSpecializations(Object handle) {
        if (handle instanceof MGeneralizableElement) {
            MGeneralizableElement ge = (MGeneralizableElement)handle;
            return ge.getSpecializations().iterator();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /**
     * Returns the stereotype belonging to some given modelelement
     * @param handle
     * @return Object
     */
    public static Object getStereoType(Object handle) {
        if (isAModelElement(handle)) {
            return ((MModelElement)handle).getStereotype();
        }
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }
    /**
     * Returns a collection with all subvertices belonging to the given
     * composite state.
     * @param handle
     * @return Collection
     */
    public static Collection getSubvertices(Object handle) {
        if (isACompositeState(handle)) {
            return ((MCompositeState)handle).getSubvertices();
        }
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** The list of SupplierDependencies from a ModelElement.
     *
     * @param handle model element.
     * @returns Iterator with the supplier dependencies.
     */
    public static Iterator getSupplierDependencies(Object handle) {
        if (handle instanceof MModelElement) {
            MModelElement me = (MModelElement)handle;
            return me.getSupplierDependencies().iterator();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** The type of an attribute
     *
     * @param handle the attribute
     * @returns the type
     */
    public static Object getType(Object handle) {
        if (handle instanceof MAttribute) {
            return ((MAttribute)handle).getType();
        }
        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd)handle).getType();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /**
     * Returns the target of some transition
     * @param handle
     * @return Object
     */
    public static Object getTarget(Object handle) {
        if (isATransition(handle)) {
            return ((MTransition)handle).getTarget();
        }
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /**
     * Returns the upper bound of the multiplicity of the given handle (an
     * associationend).
     * @param handle
     * @return int
     */
    public static int getUpper(Object handle) {
        if (isAAssociationEnd(handle)) {
            int upper = 0;
            MAssociationEnd end = (MAssociationEnd)handle;
            if (end.getMultiplicity() != null)
                upper = end.getMultiplicity().getUpper();
            return upper;
        }
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /**
     * Returns the transitions belonging to the given handle. The handle can be
     * a statemachine or a composite state. If it's a statemachine the
     * transitions will be given back belonging to that statemachine. If it's a
     * compositestate the internal transitions of that compositestate will be
     * given back.
     * @param handle
     * @return Collection
     */
    public static Collection getTransitions(Object handle) {
        if (isAStateMachine(handle)) {
            return ((MStateMachine)handle).getTransitions();
        } else if (isACompositeState(handle)) {
            return ((MCompositeState)handle).getInternalTransitions();
        }
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    ////////////////////////////////////////////////////////////////
    // Common getters

    /** The name of a model element or some diagram part.
     *
     * @param handle that points out the object.
     * @returns the name
     */
    public static String getName(Object handle) {
        if (handle instanceof MModelElement) {
            MModelElement me = (MModelElement)handle;

            return me.getName();
        }

        if (handle instanceof Diagram) {
            Diagram d = (Diagram)handle;

            return d.getName();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    ////////////////////////////////////////////////////////////////
    // Convenience methods

    /** The empty set.
     *
     * @returns an empty iterator.
     */
    private static Iterator emptyIterator() {
        return Collections.EMPTY_SET.iterator();
    }
}
