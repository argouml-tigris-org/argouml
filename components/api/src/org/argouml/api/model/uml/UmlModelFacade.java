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

package org.argouml.api.model.uml;

import java.util.Collection;
import java.util.Iterator;

/**
 * Interface definition for the Facade object for the Model component in ArgoUML.<p>
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
 * public boolean isA<TYPE>(Object handle)
 * public boolean is<PROPERTY>(Object handle)
 * <p>
 *
 * Signature for all getters in this Facade:
 * public Object get<TYPE>(Object handle) - 1..1
 * public Iterator get<TYPES>(Object handle) - 0..*
 * public String getName(Object handle) - Name
 * <p>
 *
 * @stereotype utility
 * @author Linus Tolke
 */
public interface UmlModelFacade {
    ////////////////////////////////////////////////////////////////
    // constants
    // TODO Move these into static class Uml.
    

    public final short ACC_PUBLIC = 1;
    public final short ACC_PRIVATE = 2;
    public final short ACC_PROTECTED = 3;

    public final short CLASSIFIER_SCOPE = 1;
    public final short INSTANCE_SCOPE = 2;

    public final short GUARDED = 1;
    public final short SEQUENTIAL = 2;
    
    ////////////////////////////////////////////////////////////////
    // Recognizer methods for the UML model (in alphabetic order)

    /** Recognizer for Abstraction.
     *
     * @param handle candidate
     * @returns true if handle is an Abstraction
     */
    public boolean isAAbstraction(Object handle);

    /** Recognizer for Association.
     *
     * @param handle candidate
     * @returns true if handle is an Association
     */
    public boolean isAAssociation(Object handle);

    /** Recognizer for AssociationEnd.
     *
     * @param handle candidate
     * @returns true if handle is an AssociationEnd
     */
    public boolean isAAssociationEnd(Object handle);

    /** Recognizer for AssociationRole
     *
     * @param handle candidate
     * @returns true if handle is an AssociationRole
     */
    public boolean isAAssociationRole(Object handle);

    /** Recognizer for abstract classes and operations.
     *
     * @param handle candidate
     * @returns true if handle is abstract.
     */
    public boolean isAbstract(Object handle);

    /** Recognizer for bases. A base is an object that is some form of an element
     *  in the model. MBase in Novosoft terms. RefBaseObject in JMI terms.
     *
     * @param handle candidate
     * @returns true if handle is abstract.
     */
    public boolean isABase(Object handle);

    /** Recognizer for Class
     *
     * @param handle candidate
     * @returns true if handle is a Class
     */
    public boolean isAClass(Object handle);

    /** Recognizer for Classifier
     *
     * @param handle candidate
     * @returns true if handle is a Classifier
     */
    public boolean isAClassifier(Object handle);

    /** Recognizer for Comment
     *
     * @param handle candidate
     * @returns true if handle is a Comment
     */
    public boolean isAComment(Object handle);

    

    /** Recognizer for Component
     *
     * @param handle candidate
     * @returns true if handle is a Component
     */
    public boolean isAComponent(Object handle);

     /** Recognizer for ComponentInstance
     *
     * @param handle candidate
     * @returns true if handle is a ComponentInstance
     */
    public boolean isAComponentInstance(Object handle);

    /** Recognizer for DataType
     *
     * @param handle candidate
     * @returns true if handle is a DataType
     */
    public boolean isADataType(Object handle);

    /** Recognizer for Dependency
     *
     * @param handle candidate
     * @returns true if handle is a Dependency
     */
    public boolean isADependency(Object handle);

    /** Recognizer for CompositeState
     *
     * @param handle candidate
     * @returns true if handle is a CompositeState
     */
    public boolean isACompositeState(Object handle);
    
    /** Recognizer for Expression
     *
     * @param handle candidate
     * @returns true if handle is an Expression
     */
    public boolean isAElement(Object handle);

    /** Recognizer for Expression
     *
     * @param handle candidate
     * @returns true if handle is an Expression
     */
    public boolean isAExpression(Object handle);

    /** Recognizer for ExtensionPoint
     *
     * @param handle candidate
     * @returns true if handle is an ExtensionPoint
     */
    public boolean isAExtensionPoint(Object handle);
    

    /** Recognizer for Feature
     *
     * @param handle candidate
     * @returns true if handle is a Feature
     */
    public boolean isAFeature(Object handle);

    /** Recognizer for GeneralizableElement
     *
     * @param handle candidate
     * @returns true if handle is a GeneralizableElement
     */
    public boolean isAGeneralizableElement(Object handle);

    /** Recognizer for GeneralizableElement
     *
     * @param handle candidate
     * @returns true if handle is a GeneralizableElement
     */
    public boolean isAGeneralization(Object handle);

    /** Recognizer for Instance
     *
     * @param handle candidate
     * @returns true if handle is a Instance
     */
    public boolean isAInstance(Object handle);
    
    
    /** Recognizer for Interaction
     *
     * @param handle candidate
     * @returns true if handle is a Interaction
     */
    public boolean isAInteraction(Object handle);

    /** Recognizer for Interface
     *
     * @param handle candidate
     * @returns true if handle is a Interface
     */
    public boolean isAInterface(Object handle);

    /** Recognizer for Link
     *
     * @param handle candidate
     * @returns true if handle is a Link
     */
    public boolean isALink(Object handle);

    /** Recognizer for Method
     *
     * @param handle candidate
     * @returns true if handle is a Method
     */
    public boolean isAMethod(Object handle);

    /** Recognizer for Model
     *
     * @param handle candidate
     * @returns true if handle is a Model
     */
    public boolean isAModel(Object handle);

    /** Recognizer for ModelElement
     *
     * @param handle candidate
     * @returns true if handle is a ModelElement
     */
    public boolean isAModelElement(Object handle);

    /** Recognizer for Namespace
     *
     * @param handle candidate
     * @returns true if handle is a Namespace
     */
    public boolean isANamespace(Object handle);

    /** Recognizer for a Node
     *
     * @param handle candidate
     * @returns true if handle is a Node
     */
    public boolean isANode(Object handle);

    /** Recognizer for a NodeInstance
     *
     * @param handle candidate
     * @returns true if handle is a NodeInstance
     */
    public boolean isANodeInstance(Object handle);

    /**
     * Recognizer for Operation
     *
     * @param handle candidate
     * @returns true if handle is an Operation
     */
    public boolean isAOperation(Object handle);

    /**
     * Recognizer for Object
     *
     * @param handle candidate
     * @returns true if handle is an Object
     */
    public boolean isAObject(Object handle);

    /**
     * Recognizer for Permission
     *
     * @param handle candidate
     * @returns true if handle is an Permission
     */
    public boolean isAPermission(Object handle);
    
    /** Recognizer for Package
     *
     * @param handle candidate
     * @returns true if handle is a Package
     */
    public boolean isAPackage(Object handle);

    /** Recognizer for Reception
     *
     * @param handle candidate
     * @returns true if handle is a Reception
     */
    public boolean isAReception(Object handle);
    
    /** Recognizer for Relationship
     *
     * @param handle candidate
     * @returns true if handle is a Relationship
     */
    public boolean isARelationship(Object handle);
    

    /** Recognizer for StateMachine
     *
     * @param handle candidate
     * @returns true if handle is a StateMachine
     */
    public boolean isAStateMachine(Object handle);

    /** Recognizer for StateVertex
     *
     * @param handle candidate
     * @returns true if handle is a StateVertex
     */
    public boolean isAStateVertex(Object handle);

    /** Recognizer for Stereotype
     *
     * @param handle candidate
     * @returns true if handle is a Stereotype
     */
    public boolean isAStereotype(Object handle);

    /** Recognizer for StructuralFeature
     *
     * @param handle candidate
     * @returns true if handle is a StructuralFeature
     */
    public boolean isAStructuralFeature(Object handle);


    /** Recognizer for TaggedValue
     *
     * @param handle candidate
     * @returns true if handle is a TaggedValue
     */
    public boolean isATaggedValue(Object handle);
    

    /** Recognizer for Transition
     *
     * @param handle candidate
     * @returns true if handle is a Transition
     */
    public boolean isATransition(Object handle);

    /** Recognizer for a Use Case
     *
     * @param handle candidate
     * @returns true if handle is a Transition
     */
    public boolean isAUseCase(Object handle);
    /** Recognizer for attributes that are changeable
     *
     * @param handle candidate
     * @returns true if handle is changeable
     */
    public boolean isChangeable(Object handle);

    /** Recognizer for attributes with classifier scope.
     *
     * @param handle candidate
     * @returns true if handle has classifier scope.
     */
    public boolean isClassifierScope(Object handle);

    /** Recognizer for constructor.
     *
     * @param handle candidate
     * @returns true if handle is a constructor.
     */
    public boolean isConstructor(Object handle);
    
    /**
     * Returns true if a given associationend is a composite.
     * @param handle
     * @return boolean
     */
    public boolean isComposite(Object handle);

    /** Recognizer for attributes that are initialized.
     *
     * @param handle candidate
     * @param true if the attribute is initialized.
     */
    public boolean isInitialized(Object handle);

    /** Recognizer for attributes with instance scope.
     *
     * @param handle candidate
     * @returns true if handle has instance scope.
     */
    public boolean isInstanceScope(Object handle);

    /** Recognizer for leafs
     *
     * @param handle candidate GeneralizableElement
     * @returns true if handle is a leaf
     */
    public boolean isLeaf(Object handle);

    /** Recognizer for Navigable elements
     *
     * @param handle candidate
     * @returns true if handle is navigable
     */
    public boolean isNavigable(Object handle);

    /** Recognizer for primary objects.
     * A primary object is an object that is created by the parser or
     * by a user.
     * Object that are created when importing some other object are not.
     *
     * @param handle candidate
     * @returns true if primary object.
     */
    public boolean isPrimaryObject(Object handle);

    /** Recognizer for attributes with private
     *
     * @param handle candidate
     * @returns true if handle has private
     */
    public boolean isPrivate(Object handle);

    /** Recognizer for realize
     *
     * @param handle candidate
     * @returns true if handle has a realize stereotype
     */
    public boolean isRealize(Object handle);

    /** Recognizer for return
     *
     * @param handle candidate parameter
     * @returns true if handle is a return parameter.
     */
    public boolean isReturn(Object handle);

    /** Recognizer for singleton.
     *
     * @param handle candidate
     * @returns true if handle is a singleton.
     */
    public boolean isSingleton(Object handle);

    public boolean isTop(Object handle);

    /** Recognizer for type.
     *
     * @param handle candidate
     * @returns true if handle is a type.
     */
    public boolean isType(Object handle);

    /** Recognizer for utility.
     *
     * @param handle candidate
     * @returns true if handle is a utility.
     */
    public boolean isUtility(Object handle);

    ////////////////////////////////////////////////////////////////
    // Getters for the UML model (in alphabetic order)

    /**
     * Returns the association end between some classifier and some associaton.
     * @param type
     * @param assoc
     * @return association end
     */
    public Object getAssociationEnd(Object type, Object assoc);

    /** The list of Association Ends
     *
     * @param handle the object that we get the association ends from.
     * @return Iterator with association ends.
     */
    public Collection getAssociationEnds(Object handle);

    /** The list of Attributes.
     *
     * @param handle classifier to examine.
     * @return iterator with attributes.
     */
    public Collection getAttributes(Object handle);

    /** Get the behaviors of a Modelelement.
     *
     *
     * @param handle modelelement to examine.
     * @return the behaviors.
     */
    public Collection getBehaviors(Object handle);

    /** Get the body of an Expression.
     *
     *
     * @param handle expression.
     * @return the body.
     */
    public Object getBody(Object handle);

    /** Get the child of a generalization.
     *
     * @param handle generalization.
     * @return the child.
     */
    public Object getChild(Object handle);
    /**
     * Get the children of some generalizable element
     *
     * @param handle to the generalizable element.
     * @return a collection with all children.
     */
    public Collection getChildren(Object handle);

    /**
     * Get the client dependencies of some classifier
     *
     * @param handle to the classifier.
     * @return an iterator with all client dependencies.
     */
    public Iterator getClientDependencies(Object handle);

    /** Get the concurrency of an operation.
     *
     * @param o operation.
     * @return the concurrency.
     */
    public short getConcurrency(Object o);

    /** The list of Connections or AssociationEnds to an Association.
     *
     * @param handle to the association.
     * @return an Iterator with all connections.
     */
    public Iterator getConnections(Object handle);

    /** The list of Features from a Classifier.
     *
     * @param handle Classifier to retrieve from.
     * @return Collection with Features
     */
    public Collection getFeatures(Object handle);
    
    /**
     * Gets the generalization between two generalizable elements.
     * Returns null if there is none.
     * @param child
     * @param parent
     * @return The generalization
     */
    public Object getGeneralization(Object child, Object parent);

    /** The list of Generalizations from a GeneralizableElement.
     *
     * @param handle GeneralizableElement to retrieve from.
     * @return Iterator with Generalizations
     */
    public Iterator getGeneralizations(Object handle);

    /**
     * Returns the incoming transitions for some statevertex
     * @param handle
     * @return Collection
     */
    public Collection getIncomings(Object stateVertex);

    /**
     * Returns the messages belonging to some interaction
     * @param handle
     * @return Collection
     */
    public Collection getMessages(Object handle);
    
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
    public Object getContainer(Object handle);
    
    /**
     * Returns the context of some given statemachine
     * @param handle the statemachine
     * @return the context of the statemachine or null if the statemachine doesn't 
     * have a context.
     */
    public Object getContext(Object handle);

	/** Get the namespace of an element.
	 *
	 * @param handle the model element that we are getting the namespace of
	 * @returns the namespace (or null)
	 */
	public Object getNamespace(Object handle);

    /** The list of operations
     *
     * @param handle classifier to examine.
     * @return Collection with operations.
     */
    public Collection getOperations(Object handle);

    /** The list of Operations of this classifier and all inherited.
     *
     * @param handle classifier to examine.
     * @return Iterator with operations.
     */
    public Iterator getOperationsInh(Object handle);
    
    /**
     * Returns the opposite end of an association end.
     * @param handle
     * @return Object the opposite end.
     */
    public Object getOppositeEnd(Object handle);
    /** Returns the list of Transitions outgoing from the given stateVertex.
     *
     * @param statevertex
     * @return Collection
     */
    public Collection getOutgoings(Object stateVertex);

    /** The list of Associations Ends connected to this association end
     *
     * @param handle association end to start from
     * @returns Iterator with all connected association ends.
     */
    public Collection getOtherAssociationEnds(Object handle);

    /** The list of owned elements of the the package.
     *
     * @param handle package to retrieve from.
     * @return Iterator with operations
     */
    public Collection getOwnedElements(Object handle);

    /** Get a parameter of an operation.
     *
     * @param op operation to retrieve from
     * @param n parameter number
     * @return parameter.
     */
    public Object getParameter(Object op, int n);

    /** Get the parameters of an operation.
     *
     * @param handle operation to retrieve from
     * @return Iterator with operations.
     */
    public Iterator getParameters(Object handle);
    
    /** Get the parent of a generalization.
     *
     * @param handle generalization.
     * @return the parent.
     */
    public Object getParent(Object handle);
    /**
     * Returns a collection with all residents belonging to the given
     * node.
     * @param handle
     * @return Collection
     */
    public Collection getResidents(Object handle);

    /**
     * Gets the source for some given transitions.
     * @param handle
     * @return Object
     */
    public Object getSource(Object handle);

    /** The list of Specializations from a GeneralizableElement.
     *
     * @param handle GeneralizableElement to retrieve from.
     * @return Iterator with Specializations.
     */
    public Iterator getSpecializations(Object handle);
    /**
     * Returns the stereotype belonging to some given modelelement
     * @param handle
     * @return Object
     */
    public Object getStereoType(Object handle);
    
    /**
     * Returns a collection with all subvertices belonging to the given
     * composite state.
     * @param handle
     * @return Collection
     */
    public Collection getSubvertices(Object handle);

    /** The list of SupplierDependencies from a ModelElement.
     *
     * @param handle model element.
     * @returns Iterator with the supplier dependencies.
     */
    public Iterator getSupplierDependencies(Object handle);
    
    /** The type of an attribute
     *
     * @param handle the attribute
     * @returns the type
     */
    public Object getType(Object handle);

    /**
     * Returns the target of some transition
     * @param handle
     * @return Object
     */
    public Object getTarget(Object handle);

    /**
     * Returns the upper bound of the multiplicity of the given handle (an
     * associationend).
     * @param handle
     * @return int
     */
    public int getUpper(Object handle);

    /**
     * Returns the transitions belonging to the given handle. The handle can be
     * a statemachine or a composite state. If it's a statemachine the
     * transitions will be given back belonging to that statemachine. If it's a
     * compositestate the internal transitions of that compositestate will be
     * given back.
     * @param handle
     * @return Collection
     */
    public Collection getTransitions(Object handle);
    
    /** This method returns all attributes of a given Classifier.
     *
     * @param classifier the classifier you want to have the attributes for
     * @return a collection of the attributes
     */
    public Collection getStructuralFeatures(Object classifier);

    /**
     * Returns all Interfaces of which this class is a realization.
     * @param cls  the class you want to have the interfaces for
     * @return a collection of the Interfaces
     */
    public Collection getSpecifications(Object cls);

    /**
     * Returns the suppliers of an abstraction.
     * @param abstraction
     * @return a collection of the suppliers
     */
    public Collection getSuppliers(Object handle);

    /**
     * Returns all associated classes for some given classifier. Returns an
     * empty collection if the given argument o is not a classifier. The given
     * parameter is included in the returned collection if it has a self-
     * referencing association.
     * @param o
     * @return Collection
     */
    public Collection getAssociatedClasses(Object o);

    ////////////////////////////////////////////////////////////////
    // Common getters

	/** The name of a model element.
	 *
	 * @param handle that points out the object.
	 * @returns the name
	 */
	public String getName(Object handle);

    /**
       Return the owner of a feature.
       @param feature
       @return classifier
     */
    public Object getOwner(Object f);
    /**
       Return the tagged values iterator of a model element.
    
       @param element The tagged values belong to this.
       @return The tagged values iterator
     */
    public Iterator getTaggedValues(Object modelElement);

    /**
       Return the tagged value with a specific tag.
    
       @param element The tagged value belongs to this.
       @param name The tag.
       @return The found tag, null if not found
     */
    public Object getTaggedValue(Object modelElement, String name);


    /**
       Return the key (tag) of some tagged value.
    
       @param tv The tagged value.
       @return The found value, null if not found
     */
    public String getTagOfTag(Object tv);

    /**
       Return the value of some tagged value.
    
       @param tv The tagged value.
       @return The found value, null if not found
     */
    public String getValueOfTag(Object tv);


    /**
       Return the UUID of this element

       @param base base element (MBase type)
       @return UUID
    */
    public String getUUID(Object base);

    ////////////////////////////////////////////////////////////////
    // Other querying methods

    /**
     * Returns a named object in the given object by calling it's lookup method.
     * @param namespace
     * @param name of the model element
     * @return found object, null otherwise
     */
    public Object lookupIn(Object o, String name);

    ////////////////////////////////////////////////////////////////
    // Model modifying methods

    /**
     * Adds a feature to some classifier.
     * @param classifier
     * @param feature
     */
    public void addFeature(Object cls, Object f);

    /**
     * Adds a method to some operation and copies the op's attributes to the method.
     * @param operation
     * @param method
     */
    public void addMethod(Object o, Object m);

    /**
     * Adds a model element to some namespace.
     * @param ns namespace
     * @param me model element
     */
    public void addOwnedElement(Object ns, Object me);

    /**
     * Adds a supplier classifier to some abstraction.
     * @param a abstraction
     * @param cls supplier classifier
     */
    public void addSupplier(Object a, Object cls);

    /**
     * Adds a client classifier to some abstraction.
     * @param a abstraction
     * @param cls client classifier
     */
    public void addClient(Object a, Object cls);

    /** This method removes a dependency from a model element.
     *
     * @param model element
     * @param dependency
     */
    public void removeClientDependency(Object o, Object dep);

    /** This method removes a feature from a classifier.
     *
     * @param classifier
     * @param feature
     */
    public void removeFeature(Object cls, Object feature);

    /** This method removes a parameter from an operation.
     *
     * @param operation
     * @param parameter
     */
    public void removeParameter(Object o, Object p);

    /**
     * Sets a body of some method.
     * @param method
     * @param expression
     */
    public void setBody(Object m, Object expr);

    /**
     * Sets an initial value of some attribute.
     * @param attribute
     * @param expression
     */
    public void setInitialValue(Object at, Object expr);

    /**
     * Sets a multiplicity of some attribute or association end.
     * @param attribute or association end
     * @param multiplicity as string
     */
    public void setMultiplicity(Object o, String mult);

    /**
     * Sets a name of some modelelement.
     * @param model element
     * @param name
     */
    public void setName(Object o, String name);
    /**
     * Sets a namespace of some modelelement.
     * @param model element
     * @param namespace
     */
    public void setNamespace(Object o, Object ns);

    /**
     * Sets the navigability of some association end.
     * @param association end
     * @param navigability flag
     */
    public void setNavigable(Object o, boolean flag);

    /**
     * Set the visibility of some modelelement.
     * @param model element
     * @param visibility
     */
    public void setVisibility(Object o, short v);

    /**
     * Set the owner scope of some feature.
     * @param feature
     * @param owner scope
     */
    public void setOwnerScope(Object f, short os);

    /**
     * Set the target scope of some association end.
     * @param association end
     * @param target scope
     */
    public void setTargetScope(Object ae, short ts);
    
    /**
     * Set the concurrency of some operation.
     * @param operation
     * @param concurrency
     */
    public void setConcurrency(Object o, short c);

    /**
     * Set the changeability of some feature.
     * @param feature
     * @param changeability flag
     */
    public void setChangeable(Object o, boolean flag);

    /**
     * Sets if of some classifier is abstract.
     * @param classifier
     * @param flag
     */
    public void setAbstract(Object o, boolean flag);

    /**
     * Sets if of some classifier is a leaf.
     * @param classifier
     * @param flag
     */
    public void setLeaf(Object o, boolean flag);

    /**
     * Sets if of some classifier is a root.
     * @param classifier
     * @param flag
     */
    public void setRoot(Object o, boolean flag);

    /**
     * Set some parameters kind to 'in'.
     * @param parameter
     */
    public void setKindToIn(Object p);

    /**
     * Set some parameters kind to 'return'.
     * @param parameter
     */
    public void setKindToReturn(Object p);

    /**
     * Sets the type of some parameter.
     * @param parameter
     * @param type
     */
    public void setType(Object p, Object cls);

    /**
     * Sets a tagged value of some modelelement.
     * @param model element
     * @param tag
     * @param value
     */
    public void setTaggedValue(Object o, String tag, String value);
    /**
     * Sets a value of some taggedValue.
     * @param taggedValue
     * @param value
     */
    public void setValueOfTag(Object tv, String value);

    /**
     * Sets the stereotype of some modelelement. The method also copies a
     * stereotype that is not a part of the current model to the current model.
     * @param m model element
     * @param stereo stereotype
     */
    public void setStereotype(Object m, Object stereo);

    /**
     * Adds a constraint to some model element.
     * @param me model element
     * @param mc constraint
     */
    public void addConstraint(Object me, Object mc);

    /** getUMLClassName returns the name of the UML Model class, e.g. it 
     *  it will return Class for an object of type MClass.
     * @param handle Modelelement
     * @return classname of modelelement
     */
    public String getUMLClassName(Object handle);

}
