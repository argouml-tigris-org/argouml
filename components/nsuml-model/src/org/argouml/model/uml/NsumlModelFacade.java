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

package org.argouml.model.uml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.model.uml.foundation.extensionmechanisms.ExtensionMechanismsHelper;
import org.argouml.api.Constants;
import org.argouml.api.model.uml.UmlModelFacade;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.behavior.collaborations.MInteraction;
import ru.novosoft.uml.behavior.common_behavior.MAction;
import ru.novosoft.uml.behavior.common_behavior.MCallAction;
import ru.novosoft.uml.behavior.common_behavior.MComponentInstance;
import ru.novosoft.uml.behavior.common_behavior.MInstance;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.common_behavior.MNodeInstance;
import ru.novosoft.uml.behavior.common_behavior.MObject;
import ru.novosoft.uml.behavior.common_behavior.MReception;
import ru.novosoft.uml.behavior.state_machines.MCompositeState;
import ru.novosoft.uml.behavior.state_machines.MPseudostate;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.behavior.use_cases.MActor;
import ru.novosoft.uml.behavior.use_cases.MExtend;
import ru.novosoft.uml.behavior.use_cases.MExtensionPoint;
import ru.novosoft.uml.behavior.use_cases.MInclude;
import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.MActionExpression;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;
import ru.novosoft.uml.foundation.data_types.MCallConcurrencyKind;
import ru.novosoft.uml.foundation.data_types.MChangeableKind;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;
import ru.novosoft.uml.foundation.data_types.MProcedureExpression;
import ru.novosoft.uml.foundation.data_types.MScopeKind;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.model_management.MPackage;

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
public class NsumlModelFacade implements UmlModelFacade {
    ////////////////////////////////////////////////////////////////
    // constants

    // Types of line
    public static final Object ABSTRACTION      = MAbstraction.class;
    public static final Object ASSOCIATION      = MAssociation.class;
    public static final Object ASSOCIATION_ROLE = MAssociationRole.class;
    public static final Object DEPENDENCY       = MDependency.class;
    public static final Object EXTEND           = MExtend.class;
    public static final Object GENERALIZATION   = MGeneralization.class;
    public static final Object INCLUDE          = MInclude.class;
    public static final Object LINK             = MLink.class;
    public static final Object PERMISSION       = MPermission.class;
    public static final Object USAGE            = MUsage.class;
    public static final Object TRANSITION       = MTransition.class;

    // Types of node
    public static final Object ACTOR              = MActor.class;
    public static final Object CLASS              = MClass.class;
    public static final Object CLASSIFIER         = MClassifier.class;
    public static final Object CLASSIFIER_ROLE    = MClassifierRole.class;
    public static final Object COMPONENT          = MComponent.class;
    public static final Object COMPONENT_INSTANCE = MComponentInstance.class;
    public static final Object INSTANCE           = MInstance.class;
    public static final Object INTERFACE          = MInterface.class;
    public static final Object NODE               = MNode.class;
    public static final Object NODE_INSTANCE      = MNodeInstance.class;
    public static final Object OBJECT             = MObject.class;
    public static final Object PACKAGE            = MPackage.class;
    public static final Object STATE              = MState.class;
    public static final Object COMPOSITESTATE     = MCompositeState.class;
    public static final Object STATEVERTEX        = MStateVertex.class;
    public static final Object PSEUDOSTATE        = MPseudostate.class;
    public static final Object USE_CASE           = MUseCase.class;
    
    // Invisible model elements
    public static final Object ACTION             = MAction.class;
    public static final Object ACTION_EXPRESSION  = MActionExpression.class;
    public static final Object ASSOCIATION_END    = MAssociationEnd.class;
    public static final Object CALL_ACTION        = MCallAction.class;
    public static final Object NAMESPACE          = MNamespace.class;
    public static final Object RECEPTION          = MReception.class;
    public static final Object STEREOTYPE         = MStereotype.class;


    public static final Object ATTRIBUTE          = MAttribute.class;
    public static final Object OPERATION          = MOperation.class;
    
    /** Standard constructor.
     */
    public NsumlModelFacade() {
    }

    ////////////////////////////////////////////////////////////////
    // Recognizer methods for the UML model (in alphabetic order)

    /** Recognizer for Abstraction.
     *
     * @param handle candidate
     * @returns true if handle is an Abstraction
     */
    public boolean isAAbstraction(Object handle) {
        return handle instanceof MAbstraction;
    }

    /** Recognizer for Association.
     *
     * @param handle candidate
     * @returns true if handle is an Association
     */
    public boolean isAAssociation(Object handle) {
        return handle instanceof MAssociation;
    }

    /** Recognizer for AssociationEnd.
     *
     * @param handle candidate
     * @returns true if handle is an AssociationEnd
     */
    public boolean isAAssociationEnd(Object handle) {
        return handle instanceof MAssociationEnd;
    }

    /** Recognizer for AssociationRole
     *
     * @param handle candidate
     * @returns true if handle is an AssociationRole
     */
    public boolean isAAssociationRole(Object handle) {
        return handle instanceof MAssociationRole;
    }

    /** Recognizer for abstract classes and operations.
     *
     * @param handle candidate
     * @returns true if handle is abstract.
     */
    public boolean isAbstract(Object handle) {
        if (handle instanceof MOperation)
            return ((MOperation)handle).isAbstract();
        if (handle instanceof MGeneralizableElement)
            return ((MGeneralizableElement)handle).isAbstract();
        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Recognizer for bases. A base is an object that is some form of an element
     *  in the model. MBase in Novosoft terms.
     *
     * @param handle candidate
     * @returns true if handle is abstract.
     */
    public boolean isABase(Object handle) {
        return handle instanceof MBase;
    }

    /** Recognizer for Class
     *
     * @param handle candidate
     * @returns true if handle is a Class
     */
    public boolean isAClass(Object handle) {
        return handle instanceof MClass;
    }

    /** Recognizer for Classifier
     *
     * @param handle candidate
     * @returns true if handle is a Classifier
     */
    public boolean isAClassifier(Object handle) {
        return handle instanceof MClassifier;
    }

    /** Recognizer for Comment
     *
     * @param handle candidate
     * @returns true if handle is a Comment
     */
    public boolean isAComment(Object handle) {
        return handle instanceof MComment;
    }

    

    /** Recognizer for Component
     *
     * @param handle candidate
     * @returns true if handle is a Component
     */
    public boolean isAComponent(Object handle) {
        return handle instanceof MComponent;
    }

     /** Recognizer for ComponentInstance
     *
     * @param handle candidate
     * @returns true if handle is a ComponentInstance
     */
    public boolean isAComponentInstance(Object handle) {
        return handle instanceof MComponentInstance;
    }

    /** Recognizer for DataType
     *
     * @param handle candidate
     * @returns true if handle is a DataType
     */
    public boolean isADataType(Object handle) {
        return handle instanceof MDataType;
    }

    /** Recognizer for Dependency
     *
     * @param handle candidate
     * @returns true if handle is a Dependency
     */
    public boolean isADependency(Object handle) {
        return handle instanceof MDependency;
    }

    /** Recognizer for CompositeState
     *
     * @param handle candidate
     * @returns true if handle is a CompositeState
     */
    public boolean isACompositeState(Object handle) {
        return handle instanceof MCompositeState;
    }
    
    /** Recognizer for Expression
     *
     * @param handle candidate
     * @returns true if handle is an Expression
     */
    public boolean isAElement(Object handle) {
        return handle instanceof MElement;
    }

    /** Recognizer for Expression
     *
     * @param handle candidate
     * @returns true if handle is an Expression
     */
    public boolean isAExpression(Object handle) {
        return handle instanceof MExpression;
    }

    /** Recognizer for ExtensionPoint
     *
     * @param handle candidate
     * @returns true if handle is an ExtensionPoint
     */
    public boolean isAExtensionPoint(Object handle) {
        return handle instanceof MExtensionPoint;
    }
    

    /** Recognizer for Feature
     *
     * @param handle candidate
     * @returns true if handle is a Feature
     */
    public boolean isAFeature(Object handle) {
        return handle instanceof MFeature;
    }

    /** Recognizer for GeneralizableElement
     *
     * @param handle candidate
     * @returns true if handle is a GeneralizableElement
     */
    public boolean isAGeneralizableElement(Object handle) {
        return handle instanceof MGeneralizableElement;
    }

    /** Recognizer for GeneralizableElement
     *
     * @param handle candidate
     * @returns true if handle is a GeneralizableElement
     */
    public boolean isAGeneralization(Object handle) {
        return handle instanceof MGeneralization;
    }

    /** Recognizer for Instance
     *
     * @param handle candidate
     * @returns true if handle is a Instance
     */
    public boolean isAInstance(Object handle) {
        return handle instanceof MInstance;
    }
    
    
    /** Recognizer for Interaction
     *
     * @param handle candidate
     * @returns true if handle is a Interaction
     */
    public boolean isAInteraction(Object handle) {
        return handle instanceof MInteraction;
    }

    /** Recognizer for Interface
     *
     * @param handle candidate
     * @returns true if handle is a Interface
     */
    public boolean isAInterface(Object handle) {
        return handle instanceof MInterface;
    }

    /** Recognizer for Link
     *
     * @param handle candidate
     * @returns true if handle is a Link
     */
    public boolean isALink(Object handle) {
        return handle instanceof MLink;
    }

    /** Recognizer for Method
     *
     * @param handle candidate
     * @returns true if handle is a Method
     */
    public boolean isAMethod(Object handle) {
        return handle instanceof MMethod;
    }

    /** Recognizer for Model
     *
     * @param handle candidate
     * @returns true if handle is a Model
     */
    public boolean isAModel(Object handle) {
        return handle instanceof MModel;
    }

    /** Recognizer for ModelElement
     *
     * @param handle candidate
     * @returns true if handle is a ModelElement
     */
    public boolean isAModelElement(Object handle) {
        return handle instanceof MModelElement;
    }

    /** Recognizer for Namespace
     *
     * @param handle candidate
     * @returns true if handle is a Namespace
     */
    public boolean isANamespace(Object handle) {
        return handle instanceof MNamespace;
    }

    /** Recognizer for a Node
     *
     * @param handle candidate
     * @returns true if handle is a Node
     */
    public boolean isANode(Object handle) {
        return handle instanceof MNode;
    }

    /** Recognizer for a NodeInstance
     *
     * @param handle candidate
     * @returns true if handle is a NodeInstance
     */
    public boolean isANodeInstance(Object handle) {
        return handle instanceof MNodeInstance;
    }

    /**
     * Recognizer for Operation
     *
     * @param handle candidate
     * @returns true if handle is an Operation
     */
    public boolean isAOperation(Object handle) {
        return handle instanceof MOperation;
    }

    /**
     * Recognizer for Object
     *
     * @param handle candidate
     * @returns true if handle is an Object
     */
    public boolean isAObject(Object handle) {
        return handle instanceof MObject;
    }

    /**
     * Recognizer for Permission
     *
     * @param handle candidate
     * @returns true if handle is an Permission
     */
    public boolean isAPermission(Object handle) {
        return handle instanceof MPermission;
    }
    
    /** Recognizer for Package
     *
     * @param handle candidate
     * @returns true if handle is a Package
     */
    public boolean isAPackage(Object handle) {
        return handle instanceof MPackage;
    }

    /** Recognizer for Reception
     *
     * @param handle candidate
     * @returns true if handle is a Reception
     */
    public boolean isAReception(Object handle) {
        return handle instanceof MReception;
    }
    
    /** Recognizer for Relationship
     *
     * @param handle candidate
     * @returns true if handle is a Relationship
     */
    public boolean isARelationship(Object handle) {
        return handle instanceof MRelationship;
    }
    

    /** Recognizer for StateMachine
     *
     * @param handle candidate
     * @returns true if handle is a StateMachine
     */
    public boolean isAStateMachine(Object handle) {
        return handle instanceof MStateMachine;
    }

    /** Recognizer for StateVertex
     *
     * @param handle candidate
     * @returns true if handle is a StateVertex
     */
    public boolean isAStateVertex(Object handle) {
        return handle instanceof MStateVertex;
    }

    /** Recognizer for Stereotype
     *
     * @param handle candidate
     * @returns true if handle is a Stereotype
     */
    public boolean isAStereotype(Object handle) {
        return handle instanceof MStereotype;
    }

    /** Recognizer for StructuralFeature
     *
     * @param handle candidate
     * @returns true if handle is a StructuralFeature
     */
    public boolean isAStructuralFeature(Object handle) {
        return handle instanceof MStructuralFeature;
    }


    /** Recognizer for TaggedValue
     *
     * @param handle candidate
     * @returns true if handle is a TaggedValue
     */
    public boolean isATaggedValue(Object handle) {
        return handle instanceof MTaggedValue;
    }
    

    /** Recognizer for Transition
     *
     * @param handle candidate
     * @returns true if handle is a Transition
     */
    public boolean isATransition(Object handle) {
        return handle instanceof MTransition;
    }

    /** Recognizer for a Use Case
     *
     * @param handle candidate
     * @returns true if handle is a Transition
     */
    public boolean isAUseCase(Object handle) {
        return handle instanceof MUseCase;
    }

    /** Recognizer for attributes that are changeable
     *
     * @param handle candidate
     * @returns true if handle is changeable
     */
    public boolean isChangeable(Object handle) {
        if (handle != null && handle instanceof MAttribute) {
            return MChangeableKind.CHANGEABLE.equals(
                ((MAttribute)handle).getChangeability());
        } else if (handle != null && handle instanceof MAssociationEnd) {
            return MChangeableKind.CHANGEABLE.equals(
                ((MAssociationEnd)handle).getChangeability());
        }
        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Recognizer for attributes with classifier scope.
     *
     * @param handle candidate
     * @returns true if handle has classifier scope.
     */
    public boolean isClassifierScope(Object handle) {
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
    public boolean isConstructor(Object handle) {
        return (
            CoreHelper.getHelper().isOperation(handle)
                && ExtensionMechanismsHelper.getHelper().isStereotypeInh(
                    getStereoType(handle),
                    "create",
                    "BehavioralFeature"))
            || (CoreHelper.getHelper().isMethod(handle)
                && ExtensionMechanismsHelper.getHelper().isStereotypeInh(
                    getStereoType(
                        CoreHelper.getHelper().getSpecification(handle)),
                    "create",
                    "BehavioralFeature"));
    }

    /**
     * Returns true if a given associationend is a composite.
     * @param handle
     * @return boolean
     */
    public boolean isComposite(Object handle) {
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
    public boolean isInitialized(Object handle) {
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
    public boolean isInstanceScope(Object handle) {
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
    public boolean isLeaf(Object handle) {

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
    public boolean isNavigable(Object handle) {
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
    public boolean isPrimaryObject(Object handle) {
        if (handle instanceof MModelElement) {
            MModelElement element = (MModelElement)handle;
            for (Iterator i = element.getTaggedValues().iterator();
                i.hasNext();
                ) {
                MTaggedValue tv = (MTaggedValue)i.next();
                if ((Constants.GENERATED_TAG).equals(tv.getTag())) {
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
    public boolean isPrivate(Object handle) {
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
    public boolean isRealize(Object handle) {
        return isStereotype(handle, "realize");
    }

    /** Recognizer for return
     *
     * @param handle candidate parameter
     * @returns true if handle is a return parameter.
     */
    public boolean isReturn(Object handle) {
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
    public boolean isSingleton(Object handle) {
        return isStereotype(handle, "singleton");
    }

    /** Recognizer for a specific stereotype.
     *
     * @param handle candidate
     * @param stereotype a string that is the stereotype name.
     * @return true if handle is a singleton.
     * @deprecated As of ArgoUml version 0.13.5, 
     *             {@link org.argouml.model.uml.foundation.extensionmechanisms.ExtensionMechanismsHelper#isStereotype(Object,String,String)} should be used
     *	instead. Since this should only ever be used together with predefined
     *	stereotypes the base class can be found in the UML 1.3 specification.
     */
    public boolean isStereotype(Object handle, String stereotypename) {
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

    public boolean isTop(Object handle) {
        return isACompositeState(handle)
            && ((MCompositeState)handle).getStateMachine() != null;
    }

    /** Recognizer for type.
     *
     * @param handle candidate
     * @returns true if handle is a type.
     */
    public boolean isType(Object handle) {
        return isStereotype(handle, "type");
    }

    /** Recognizer for utility.
     *
     * @param handle candidate
     * @returns true if handle is a utility.
     */
    public boolean isUtility(Object handle) {
        return isStereotype(handle, "utility");
    }

    ////////////////////////////////////////////////////////////////
    // Recognizer methods for the diagrams (in alphabetic order)

   ////////////////////////////////////////////////////////////////
    // Getters for the UML model (in alphabetic order)

    /**
     * Returns the association end between some classifier and some associaton.
     * @param type
     * @param assoc
     * @return association end
     */
    public Object getAssociationEnd(Object type, Object assoc) {
        if (type == null
            || assoc == null
            || !(type instanceof MClassifier)
            || !(assoc instanceof MAssociation))
            return null;
        Iterator it = ((MClassifier)type).getAssociationEnds().iterator();
        while (it.hasNext()) {
            MAssociationEnd end = (MAssociationEnd)it.next();
            if (((MAssociation)assoc).getConnections().contains(end))
                return end;
        }
        return null;
    }

    /** The list of Association Ends
     *
     * @param handle the object that we get the association ends from.
     * @return Iterator with association ends.
     */
    public Collection getAssociationEnds(Object handle) {
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
    public Collection getAttributes(Object handle) {
        if (handle instanceof MClassifier) {
            MClassifier c = (MClassifier)handle;
            // TODO: We are converting back and forth between collections and
            // iterators. I (Linus) prefer iterators.
            //return getStructuralFeatures(c).iterator();
            //...But I (thn) got CVS conflicts, so:
            return getStructuralFeatures(c);
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
    public Collection getBehaviors(Object handle) {
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
    public Object getBody(Object handle) {
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
    public Object getChild(Object handle) {
        if (handle instanceof MGeneralization) {
            return ((MGeneralization)handle).getChild();
        }
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /**
     * Get the children of some generalizable element
     *
     * @param handle to the generalizable element.
     * @return a collection with all children.
     */
    public Collection getChildren(Object handle) {
        if (isAGeneralizableElement(handle)) {
            return ((MGeneralizableElement)handle).getChildren();
        }
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /**
     * Get the client dependencies of some classifier
     *
     * @param handle to the classifier.
     * @return an iterator with all client dependencies.
     */
    public Iterator getClientDependencies(Object handle) {
        if (isAModelElement(handle)) {
            Collection c = ((MModelElement)handle).getClientDependencies();
            return (c != null) ? c.iterator() : null;
        }
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Get the concurrency of an operation.
     *
     * @param o operation.
     * @return the concurrency.
     */
    public short getConcurrency(Object o) {
        if (o != null && o instanceof MOperation) {
            return ((MOperation)o).getConcurrency()
                == MCallConcurrencyKind.GUARDED
                ? GUARDED
                : SEQUENTIAL;
        }
        throw new IllegalArgumentException("Unrecognized object " + o);
    }

    /** The list of Connections or AssociationEnds to an Association.
     *
     * @param handle to the association.
     * @return an Iterator with all connections.
     */
    public Iterator getConnections(Object handle) {
        if (handle instanceof MAssociation) {
            return ((MAssociation)handle).getConnections().iterator();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** The list of Features from a Classifier.
     *
     * @param handle Classifier to retrieve from.
     * @return Collection with Features
     */
    public Collection getFeatures(Object handle) {
        if (handle != null && handle instanceof MClassifier)
            return ((MClassifier)handle).getFeatures();
        return new ArrayList();
    }

    /**
     * Gets the generalization between two generalizable elements.
     * Returns null if there is none.
     * @param child
     * @param parent
     * @return The generalization
     */
    public Object getGeneralization(Object child, Object parent) {
        if (child == null
            || parent == null
            || !(child instanceof MGeneralizableElement)
            || !(parent instanceof MGeneralizableElement))
            return null;
        Iterator it = getGeneralizations(child);
        while (it.hasNext()) {
            MGeneralization gen = (MGeneralization)it.next();
            if (gen.getParent() == parent) {
                return gen;
            }
        }
        return null;
    }

    /** The list of Generalizations from a GeneralizableElement.
     *
     * @param handle GeneralizableElement to retrieve from.
     * @return Iterator with Generalizations
     */
    public Iterator getGeneralizations(Object handle) {
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
    public Collection getIncomings(Object stateVertex) {
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
    public Collection getMessages(Object handle) {
        if (isAInteraction(handle)) {
            return ((MInteraction)handle).getMessages();
        }
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }
    
    /**
     * Returns the container for the given modelelement. The container is the
     * owner of the modelelement. It will be null for elements that don't have
     * an owner. All elements except for the root element in a project should
     * have an owner. The root element is allways a model.
     * <p> In  the future, this function could return the container of Figs too.
     * </p>
     * @param handle
     * @return Object
     */
    public Object getContainer(Object handle) {
        if (handle instanceof MBase) {
            return ((MBase) handle).getModelElementContainer();
        }
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }
    
    /**
     * Returns the context of some given statemachine
     * @param handle the statemachine
     * @return the context of the statemachine or null if the statemachine doesn't 
     * have a context.
     */
    public Object getContext(Object handle) {
        if (handle instanceof MStateMachine) {
            return ((MStateMachine)handle).getContext();            
        }
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Get the namespace of an element.
     *
     * @param handle the model element that we are getting the namespace of
     * @returns the namespace (or null)
     */
    public Object getNamespace(Object handle) {
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
    public Collection getOperations(Object handle) {
        if (handle instanceof MClassifier) {
            MClassifier c = (MClassifier)handle;

            return getOperations(c);
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** The list of Operations of this classifier and all inherited.
     *
     * @param handle classifier to examine.
     * @return Iterator with operations.
     */
    public Iterator getOperationsInh(Object handle) {
        if (handle instanceof MClassifier) {
            MClassifier c = (MClassifier)handle;

            // TODO: We are converting back and forth between collections and
            // iterators. I (Linus) prefer iterators.
            return CoreHelper.getHelper().getOperationsInh(c).iterator();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }
    
    /**
     * Returns the opposite end of an association end.
     * @param handle
     * @return Object the opposite end.
     */
    public Object getOppositeEnd(Object handle) {
        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd)handle).getOppositeEnd();
        }
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Returns the list of Transitions outgoing from the given stateVertex.
     *
     * @param statevertex
     * @return Collection
     */
    public Collection getOutgoings(Object stateVertex) {
        if (isAStateVertex(stateVertex)) {
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
    public Collection getOtherAssociationEnds(Object handle) {
        if (handle instanceof MAssociationEnd) {
            MAssociation a = ((MAssociationEnd)handle).getAssociation();

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
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** The list of owned elements of the the package.
     *
     * @param handle package to retrieve from.
     * @return Iterator with operations
     */
    public Collection getOwnedElements(Object handle) {
        if (handle instanceof MNamespace) {
            return ((MNamespace)handle).getOwnedElements();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /** Get a parameter of an operation.
     *
     * @param op operation to retrieve from
     * @param n parameter number
     * @return parameter.
     */
    public Object getParameter(Object op, int n) {
        if (op == null || !(op instanceof MOperation))
            return null;
        return ((MOperation)op).getParameter(n);
    }

    /** Get the parameters of an operation.
     *
     * @param handle operation to retrieve from
     * @return Iterator with operations.
     */
    public Iterator getParameters(Object handle) {
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
    public Object getParent(Object handle) {
        if (handle instanceof MGeneralization) {
            return ((MGeneralization)handle).getParent();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /**
     * Returns a collection with all residents belonging to the given
     * node.
     * @param handle
     * @return Collection
     */
    public Collection getResidents(Object handle) {
        if (isANode(handle)) {
            return ((MNode)handle).getResidents();
        }
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /**
     * Gets the source for some given transitions.
     * @param handle
     * @return Object
     */
    public Object getSource(Object handle) {
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
    public Iterator getSpecializations(Object handle) {
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
    public Object getStereoType(Object handle) {
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
    public Collection getSubvertices(Object handle) {
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
    public Iterator getSupplierDependencies(Object handle) {
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
    public Object getType(Object handle) {
        if (handle instanceof MStructuralFeature) {
            return ((MAttribute)handle).getType();
        }
        if (handle instanceof MAssociationEnd) {
            return ((MAssociationEnd)handle).getType();
        }
        if (handle instanceof MParameter) {
            return ((MParameter)handle).getType();
        }

        // ...
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }

    /**
     * Returns the target of some transition
     * @param handle
     * @return Object
     */
    public Object getTarget(Object handle) {
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
    public int getUpper(Object handle) {
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
    public Collection getTransitions(Object handle) {
        if (isAStateMachine(handle)) {
            return ((MStateMachine)handle).getTransitions();
        } else if (isACompositeState(handle)) {
            return ((MCompositeState)handle).getInternalTransitions();
        }
        throw new IllegalArgumentException("Unrecognized object " + handle);
    }
    
    /** This method returns all attributes of a given Classifier.
     *
     * @param classifier the classifier you want to have the attributes for
     * @return a collection of the attributes
     */
    public Collection getStructuralFeatures(Object classifier) {
        Collection result = new ArrayList();
        if (isAClassifier(classifier)) {
            MClassifier mclassifier = (MClassifier)classifier;

            Iterator features = mclassifier.getFeatures().iterator();
            while (features.hasNext()) {
                MFeature feature = (MFeature)features.next();
                if (isAStructuralFeature(feature))
                    result.add(feature);
            }
        }
        return result;
    }

    /** This method returns all operations of a given Classifier
     *
     * @param classifier the classifier you want to have the operations for
     * @return a collection of the operations
     */
    public Collection getOperations(MClassifier mclassifier) {
        Collection result = new ArrayList();
        Iterator features = mclassifier.getFeatures().iterator();
        while (features.hasNext()) {
            MFeature feature = (MFeature)features.next();
            if (isAOperation(feature))
                result.add(feature);
        }
        return result;
    }

    /**
     * Returns all Interfaces of which this class is a realization.
     * @param cls  the class you want to have the interfaces for
     * @return a collection of the Interfaces
     */
    public Collection getSpecifications(Object cls) {
        Collection result = new Vector();
        if (cls instanceof MClassifier) {
            Collection deps = ((MClassifier)cls).getClientDependencies();
            Iterator depIterator = deps.iterator();
            while (depIterator.hasNext()) {
                MDependency dep = (MDependency)depIterator.next();
                if ((dep instanceof MAbstraction)
                    && dep.getStereotype() != null
                    && dep.getStereotype().getName() != null
                    && dep.getStereotype().getName().equals("realize")) {
                    MInterface i = (MInterface)dep.getSuppliers().toArray()[0];
                    result.add(i);
                }
            }
        }
        return result;
    }

    /**
     * Returns the suppliers of an abstraction.
     * @param abstraction
     * @return a collection of the suppliers
     */
    public Collection getSuppliers(Object handle) {
        if (handle == null || !(handle instanceof MAbstraction))
            return null;
        return ((MAbstraction)handle).getSuppliers();
    }

    /**
     * Returns all associated classes for some given classifier. Returns an
     * empty collection if the given argument o is not a classifier. The given
     * parameter is included in the returned collection if it has a self-
     * referencing association.
     * @param o
     * @return Collection
     */
    public Collection getAssociatedClasses(Object o) {
        Collection col = new ArrayList();
        if (o instanceof MClassifier) {
            MClassifier classifier = (MClassifier)o;
            Collection ends = classifier.getAssociationEnds();
            Iterator it = ends.iterator();
            Set associations = new HashSet();
            while (it.hasNext()) {
                associations.add(((MAssociationEnd)it.next()).getAssociation());
            }
            Collection otherEnds = new ArrayList();
            it = associations.iterator();
            while (it.hasNext()) {
                otherEnds.addAll(((MAssociation)it.next()).getConnections());
            }
            otherEnds.removeAll(ends);
            it = otherEnds.iterator();
            while (it.hasNext()) {
                col.add(((MAssociationEnd)it.next()).getType());
            }
        }
        return col;
    }

	////////////////////////////////////////////////////////////////
	// Common getters

	/** The name of a model element or some diagram part.
	 *
	 * @param handle that points out the object.
	 * @returns the name
	 */
	public String getName(Object handle) {
		if (handle instanceof MModelElement) {
			MModelElement me = (MModelElement)handle;

			return me.getName();
		}

		// ...
		throw new IllegalArgumentException("Unrecognized object " + handle);
	}

    /**
       Return the owner of a feature.
       @param feature
       @return classifier
     */
    public Object getOwner(Object f) {
        if (f != null && f instanceof MFeature) {
            return ((MFeature)f).getOwner();
        }
        throw new IllegalArgumentException("Unrecognized object " + f);
    }

    /**
       Return the tagged values iterator of a model element.
    
       @param element The tagged values belong to this.
       @return The tagged values iterator
     */
    public Iterator getTaggedValues(Object modelElement) {
        if (modelElement != null && modelElement instanceof MModelElement) {
            return ((MModelElement)modelElement).getTaggedValues().iterator();
        }
        return null;
    }

    /**
       Return the tagged value with a specific tag.
    
       @param element The tagged value belongs to this.
       @param name The tag.
       @return The found tag, null if not found
     */
    public Object getTaggedValue(Object modelElement, String name) {
        if (modelElement != null && modelElement instanceof MModelElement) {
            for (Iterator i =
                ((MModelElement)modelElement).getTaggedValues().iterator();
                i.hasNext();
                ) {
                MTaggedValue tv = (MTaggedValue)i.next();
                if (tv.getTag().equals(name))
                    return tv;
            }
        }
        return null;
    }


    /**
       Return the key (tag) of some tagged value.
    
       @param tv The tagged value.
       @return The found value, null if not found
     */
    public String getTagOfTag(Object tv) {
        if (tv != null && tv instanceof MTaggedValue) {
            return ((MTaggedValue)tv).getTag();
        }
        return null;
    }

    /**
       Return the value of some tagged value.
    
       @param tv The tagged value.
       @return The found value, null if not found
     */
    public String getValueOfTag(Object tv) {
        if (tv != null && tv instanceof MTaggedValue) {
            return ((MTaggedValue)tv).getValue();
        }
        return null;
    }


    /**
       Return the UUID of this element

       @param base base element (MBase type)
       @return UUID
    */
    public String getUUID(Object base) {
        if (isABase(base)) {
            return ((MBase) base).getUUID();
        }
        //
        throw new IllegalArgumentException("Unrecognized object " + base);
    }

    ////////////////////////////////////////////////////////////////
    // Other querying methods

    /**
     * Returns a named object in the given object by calling it's lookup method.
     * @param namespace
     * @param name of the model element
     * @return found object, null otherwise
     */
    public Object lookupIn(Object o, String name) {
        if (o instanceof MModel)
            return ((MModel)o).lookup(name);
        if (o instanceof MNamespace)
            return ((MNamespace)o).lookup(name);
        if (o instanceof MClassifier)
            return ((MClassifier)o).lookup(name);
        return null;
    }

    ////////////////////////////////////////////////////////////////
    // Model modifying methods

    /**
     * Adds a feature to some classifier.
     * @param classifier
     * @param feature
     */
    public void addFeature(Object cls, Object f) {
        if (cls != null
            && f != null
            && cls instanceof MClassifier
            && f instanceof MFeature) {
            ((MClassifier)cls).addFeature((MFeature)f);
        }
    }

    /**
     * Adds a method to some operation and copies the op's attributes to the method.
     * @param operation
     * @param method
     */
    public void addMethod(Object o, Object m) {
        if (o != null
            && m != null
            && o instanceof MOperation
            && m instanceof MMethod) {
            ((MMethod)m).setVisibility(((MOperation)o).getVisibility());
            ((MMethod)m).setOwnerScope(((MOperation)o).getOwnerScope());
            ((MOperation)o).addMethod((MMethod)m);
        }
    }

    /**
     * Adds a model element to some namespace.
     * @param ns namespace
     * @param me model element
     */
    public void addOwnedElement(Object ns, Object me) {
        if (ns != null
            && ns instanceof MNamespace
            && me != null
            && me instanceof MModelElement) {
            ((MNamespace)ns).addOwnedElement((MModelElement)me);
        }
    }

    /**
     * Adds a supplier classifier to some abstraction.
     * @param a abstraction
     * @param cls supplier classifier
     */
    public void addSupplier(Object a, Object cls) {
        if (a != null
            && cls != null
            && a instanceof MAbstraction
            && cls instanceof MClassifier) {
            ((MAbstraction)a).addSupplier((MClassifier)cls);
        }
    }

    /**
     * Adds a client classifier to some abstraction.
     * @param a abstraction
     * @param cls client classifier
     */
    public void addClient(Object a, Object cls) {
        if (a != null
            && cls != null
            && a instanceof MAbstraction
            && cls instanceof MClassifier) {
            ((MAbstraction)a).addClient((MClassifier)cls);
        }
    }

    /** This method removes a dependency from a model element.
     *
     * @param model element
     * @param dependency
     */
    public void removeClientDependency(Object o, Object dep) {
        if (o != null
            && dep != null
            && o instanceof MModelElement
            && dep instanceof MDependency) {
            ((MModelElement)o).removeClientDependency((MDependency)dep);
        }
    }

    /** This method removes a feature from a classifier.
     *
     * @param classifier
     * @param feature
     */
    public void removeFeature(Object cls, Object feature) {
        if (cls != null
            && feature != null
            && cls instanceof MClassifier
            && feature instanceof MFeature) {
            ((MClassifier)cls).removeFeature((MFeature)feature);
        }
    }

    /** This method removes a parameter from an operation.
     *
     * @param operation
     * @param parameter
     */
    public void removeParameter(Object o, Object p) {
        if (o != null
            && p != null
            && o instanceof MOperation
            && p instanceof MParameter) {
            ((MOperation)o).removeParameter((MParameter)p);
        }
    }

    /**
     * Sets a body of some method.
     * @param method
     * @param expression
     */
    public void setBody(Object m, Object expr) {
        if (m != null
            && expr != null
            && m instanceof MMethod
            && expr instanceof MProcedureExpression) {
            ((MMethod)m).setBody((MProcedureExpression)expr);
        }
    }

    /**
     * Sets an initial value of some attribute.
     * @param attribute
     * @param expression
     */
    public void setInitialValue(Object at, Object expr) {
        if (at != null
            && expr != null
            && at instanceof MAttribute
            && expr instanceof MExpression) {
            ((MAttribute)at).setInitialValue((MExpression)expr);
        }
    }

    /**
     * Sets a multiplicity of some attribute or association end.
     * @param attribute or association end
     * @param multiplicity as string
     */
    public void setMultiplicity(Object o, String mult) {
        // FIXME: the implementation is ugly, because I have no spec at hand...
        if (o == null)
            return;
        if (o instanceof MAttribute) {
            if ("1_N".equals(mult))
                 ((MAttribute)o).setMultiplicity(MMultiplicity.M1_N);
            else
                ((MAttribute)o).setMultiplicity(MMultiplicity.M1_1);
            // default
        } else if (o instanceof MAssociationEnd) {
            if ("1_N".equals(mult))
                 ((MAssociationEnd)o).setMultiplicity(MMultiplicity.M1_N);
            else
                ((MAssociationEnd)o).setMultiplicity(MMultiplicity.M1_1);
            // default
        }
    }

    /**
     * Sets a name of some modelelement.
     * @param model element
     * @param name
     */
    public void setName(Object o, String name) {
        if (o != null && o instanceof MModelElement) {
            ((MModelElement)o).setName(name);
        }
    }

    /**
     * Sets a namespace of some modelelement.
     * @param model element
     * @param namespace
     */
    public void setNamespace(Object o, Object ns) {
        if (o != null
            && o instanceof MModelElement
            && ns != null
            && ns instanceof MNamespace) {
            ((MModelElement)o).setNamespace((MNamespace)ns);
        }
    }

    /**
     * Sets the navigability of some association end.
     * @param association end
     * @param navigability flag
     */
    public void setNavigable(Object o, boolean flag) {
        if (o != null && o instanceof MAssociationEnd) {
            ((MAssociationEnd)o).setNavigable(flag);
        }
    }

    /**
     * Set the visibility of some modelelement.
     * @param model element
     * @param visibility
     */
    public void setVisibility(Object o, short v) {
        if (o != null && o instanceof MModelElement) {
            if (v == ACC_PRIVATE) {
                ((MModelElement)o).setVisibility(MVisibilityKind.PRIVATE);
            } else if (v == ACC_PROTECTED) {
                ((MModelElement)o).setVisibility(MVisibilityKind.PROTECTED);
            } else if (v == ACC_PUBLIC) {
                ((MModelElement)o).setVisibility(MVisibilityKind.PUBLIC);
            }
        }
    }

    /**
     * Set the owner scope of some feature.
     * @param feature
     * @param owner scope
     */
    public void setOwnerScope(Object f, short os) {
        if (f != null && f instanceof MFeature) {
            if (os == CLASSIFIER_SCOPE) {
                ((MFeature)f).setOwnerScope(MScopeKind.CLASSIFIER);
            } else if (os == INSTANCE_SCOPE) {
                ((MFeature)f).setOwnerScope(MScopeKind.INSTANCE);
            }
        }
    }

    /**
     * Set the target scope of some association end.
     * @param association end
     * @param target scope
     */
    public void setTargetScope(Object ae, short ts) {
        if (ae != null && ae instanceof MAssociationEnd) {
            if (ts == CLASSIFIER_SCOPE) {
                ((MAssociationEnd)ae).setTargetScope(MScopeKind.CLASSIFIER);
            } else if (ts == INSTANCE_SCOPE) {
                ((MAssociationEnd)ae).setTargetScope(MScopeKind.INSTANCE);
            }
        }
    }

    /**
     * Set the concurrency of some operation.
     * @param operation
     * @param concurrency
     */
    public void setConcurrency(Object o, short c) {
        if (o != null && o instanceof MOperation) {
            if (c == GUARDED) {
                ((MOperation)o).setConcurrency(MCallConcurrencyKind.GUARDED);
            } else if (c == SEQUENTIAL) {
                ((MOperation)o).setConcurrency(MCallConcurrencyKind.SEQUENTIAL);
            }
        }
    }

    /**
     * Set the changeability of some feature.
     * @param feature
     * @param changeability flag
     */
    public void setChangeable(Object o, boolean flag) {
        // FIXME: the implementation is ugly, because I have no spec at hand...
        if (o == null)
            return;
        if (o instanceof MAttribute) {
            if (flag)
                 ((MAttribute)o).setChangeability(MChangeableKind.CHANGEABLE);
            else
                 ((MAttribute)o).setChangeability(MChangeableKind.FROZEN);
        } else if (o instanceof MAssociationEnd) {
            if (flag)
                ((MAssociationEnd)o).setChangeability(
                    MChangeableKind.CHANGEABLE);
            else
                 ((MAssociationEnd)o).setChangeability(MChangeableKind.FROZEN);
        }
    }

    /**
     * Sets if of some classifier is abstract.
     * @param classifier
     * @param flag
     */
    public void setAbstract(Object o, boolean flag) {
        if (o != null) {
            if (o instanceof MClassifier)
                 ((MClassifier)o).setAbstract(flag);
            else if (o instanceof MOperation)
                 ((MOperation)o).setAbstract(flag);
        }
    }

    /**
     * Sets if of some classifier is a leaf.
     * @param classifier
     * @param flag
     */
    public void setLeaf(Object o, boolean flag) {
        if (o != null && o instanceof MClassifier) {
            ((MClassifier)o).setLeaf(flag);
        }
    }

    /**
     * Sets if of some classifier is a root.
     * @param classifier
     * @param flag
     */
    public void setRoot(Object o, boolean flag) {
        if (o != null && o instanceof MClassifier) {
            ((MClassifier)o).setRoot(flag);
        }
    }

    /**
     * Set some parameters kind to 'in'.
     * @param parameter
     */
    public void setKindToIn(Object p) {
        if (p != null && p instanceof MParameter) {
            ((MParameter)p).setKind(MParameterDirectionKind.IN);
        }
    }

    /**
     * Set some parameters kind to 'return'.
     * @param parameter
     */
    public void setKindToReturn(Object p) {
        if (p != null && p instanceof MParameter) {
            ((MParameter)p).setKind(MParameterDirectionKind.RETURN);
        }
    }

    /**
     * Sets the type of some parameter.
     * @param parameter
     * @param type
     */
    public void setType(Object p, Object cls) {
        if (p != null && cls != null && cls instanceof MClassifier) {
            if (p instanceof MParameter)
                 ((MParameter)p).setType((MClassifier)cls);
            else if (p instanceof MAssociationEnd)
                 ((MAssociationEnd)p).setType((MClassifier)cls);
            else if (p instanceof MAttribute)
                 ((MAttribute)p).setType((MClassifier)cls);
        }
    }

    /**
     * Sets a tagged value of some modelelement.
     * @param model element
     * @param tag
     * @param value
     */
    public void setTaggedValue(Object o, String tag, String value) {
        if (o != null && o instanceof MModelElement) {
            MTaggedValue tv = MFactory.getDefaultFactory().createTaggedValue();
            tv.setModelElement((MModelElement)o);
            tv.setTag(tag);
            tv.setValue(value);
        }
    }

    /**
     * Sets a value of some taggedValue.
     * @param taggedValue
     * @param value
     */
    public void setValueOfTag(Object tv, String value) {
        if (tv != null && tv instanceof MTaggedValue) {
            ((MTaggedValue)tv).setValue(value);
        }
    }

    /**
     * Sets the stereotype of some modelelement. The method also copies a
     * stereotype that is not a part of the current model to the current model.
     * @param m model element
     * @param stereo stereotype
     */
    public void setStereotype(Object m, Object stereo) {
        if (m != null && m instanceof MModelElement) {
            if (stereo != null
                && stereo instanceof MStereotype
                && ((MModelElement)m).getModel()
                    != ((MStereotype)stereo).getModel()) {
                ((MStereotype)stereo).setNamespace(
                    ((MModelElement)m).getModel());
            }
            ((MModelElement)m).setStereotype((MStereotype)stereo);
        }
    }

    /**
     * Adds a constraint to some model element.
     * @param me model element
     * @param mc constraint
     */
    public void addConstraint(Object me, Object mc) {
        if (me != null
            && me instanceof MModelElement
            && mc != null
            && mc instanceof MConstraint) {
            ((MModelElement)me).addConstraint((MConstraint)mc);
        }
    }

    /** getUMLClassName returns the name of the UML Model class, e.g. it 
     *  it will return Class for an object of type MClass.
     * @param handle Modelelement
     * @return classname of modelelement
     */
    public String getUMLClassName(Object handle) {
        if (handle instanceof MModelElement) {
            return ((MModelElement) handle).getUMLClassName();
        }
        return null;
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
    
    private static Collection emptyCollection() {
        return Collections.EMPTY_LIST;
    }
}
