// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

package org.argouml.model;

import java.util.Collection;
import java.util.List;



/**
 * The interface for the helper for Core.<p>
 *
 * Created from the old CoreHelper.
 */
public interface CoreHelper {
    /**
     * Determine if a meta type is a subtype of another.
     * @param type The parent metatype.
     * @param subType The metatype to test for being a subtype.
     * @return true is subType is a sub-type of type.
     */
    boolean isSubType(Object type, Object subType);

    /**
     * This method returns all Classifiers of which this class is a
     * direct or indirect subtype.
     *
     * @param cls1  the class you want to have the parents for
     * @return a collection of the parents, each of which is a
     *         GeneralizableElement.
     */
    Collection getAllSupertypes(Object cls1);

    /**
     * This method returns all Classifiers of which this class is a
     * direct subtype.<p>
     *
     * @param ogeneralizableelement the class you want to have the parents for
     * @return a collection of the parents, each of which is a
     *         GeneralizableElement.
     */
    Collection getSupertypes(Object ogeneralizableelement);

    /**
     * This method returns all opposite AssociationEnds of a given
     * Classifier.
     *
     * @param classifier the classifier you want to have the opposite
     * association ends for
     * @return a collection of the opposite associationends
     */
    Collection getAssociateEnds(Object classifier);

    /**
     * This method returns all opposite AssociationEnds of a given
     * Classifier, including inherited.
     *
     * @param classifier1 the classifier you want to have the opposite
     * association ends for
     * @return a collection of the opposite associationends
     */
    Collection getAssociateEndsInh(Object classifier1);

    /**
     * This method removes a feature from a classifier.
     *
     * @param cls the classifier
     * @param feature the feature to be removed
     */
    void removeFeature(Object cls, Object feature);

    /**
     * Remove an EnumerationLiteral from an Enumeration.
     *
     * @param enumeration the enumeration
     * @param literal the literal to be removed
     */
    void removeLiteral(Object enumeration, Object literal);

    /**
     * This method returns all operations of a given Classifier.
     * 
     * @param classifier
     *            the classifier you want to have the operations for
     * @return a collection of the operations
     * @deprecated by tfmorris for 0.23.4 use
     *             {@link Facade#getOperations(Object)}
     */
    List getOperations(Object classifier);

    /**
     * This method replaces all operations of the given classifier by the given
     * collection of operations.
     * 
     * @param classifier
     *            the given classifier
     * @param operations
     *            the new operations
     * @deprecated by tfmorris for 0.23.4, use variant which takes a List of
     *             operations as a parameter
     *             {@link #setOperations(Object, List)}
     */
    void setOperations(Object classifier, Collection operations);

    /**
     * This method replaces all operations of the given classifier
     * by the given list of operations.
     *
     * @param classifier the given classifier
     * @param operations the new operations
     */
    void setOperations(Object classifier, List operations);

    /**
     * This method returns all attributes of a given Classifier.
     * 
     * @param classifier
     *            the classifier you want to have the attributes for
     * @return a collection of the attributes
     * @deprecated by tfmorris for 0.23.4 use
     *             {@link Facade#getAttributes(Object)}
     */
    List getAttributes(Object classifier);

    /**
     * This method replaces all attributes of the given classifier
     * by the given collection of attributes.
     * @param classifier the classifier
     * @param attributes the new attributes
     * @deprecated by tfmorris for 0.23.4, use the variant that takes a List 
     * of attributes {@link #setAttributes(Object, List)}
     */
    void setAttributes(Object classifier, Collection attributes);

    /**
     * This method replaces all attributes of the given classifier
     * by the given collection of attributes.
     * @param classifier the classifier
     * @param attributes an ordered list of new attributes
     */
    void setAttributes(Object classifier, List attributes);
    
    /**
     * This method returns all attributes of a given Classifier,
     * including inherited.
     *
     * @param classifier the classifier you want to have the attributes for
     * @return a collection of the attributes
     */
    Collection getAttributesInh(Object classifier);

    /**
     * This method returns all operations of a given Classifier,
     * including inherited.
     *
     * @param classifier the classifier you want to have the operations for
     * @return a collection of the operations
     */
    Collection getOperationsInh(Object classifier);

    /**
     * Returns all return parameters for an operation.
     *
     * @param operation is the operation.
     * @return List of parameters of with direction kind of Return
     */
    List getReturnParameters(Object operation);

    /**
     * Returns the operation that some method realized. Returns null if
     * object isn't a method or, possibly, if the method isn't properly
     * defined.<p>
     * 
     * There is also a method in the Facade interface with the same name
     * which may be what you want if you are looking for the specification
     * of something other than a Method.
     * @see Facade#getSpecification(Object)
     *
     * @param object  the method you want the realized operation of.
     * @return an operation, or null.
     */
    Object getSpecification(Object object);

    /**
     * Returns all Interfaces of which this class is a realization.<p>
     * Duplicate of method by same name in Facade.
     * @see Facade#getSpecifications(Object)
     *
     * @param classifier  the class you want to have the interfaces for
     * @return a collection of the Interfaces
     */
    Collection getSpecifications(Object classifier);

    /**
     * This method returns all Classifiers of which this class is a
     * direct supertype.
     *
     * @param cls  the class you want to have the children for
     * @return a collection of the children, each of which is a
     *         GeneralizableElement.
     */
    Collection getSubtypes(Object cls);

    /**
     * Returns all behavioralfeatures found in this element and its
     * children.<p>
     *
     * @param element is the element
     * @return Collection
     */
    Collection getAllBehavioralFeatures(Object element);

    /**
     * Returns all behavioral features of some classifier.
     * @param clazz The classifier
     * @return the list with all behavioral features of some classifier
     */
    List getBehavioralFeatures(Object clazz);

    /**
     * Returns all interfaces found in this namespace and in its children.
     *
     * @param ns the given namespace
     * @return Collection with all interfaces found
     */
    Collection getAllInterfaces(Object ns);

    /**
     * Returns all classes found in this namespace and in its children.<p>
     *
     * @param ns is the namespace.
     * @return Collection
     */
    Collection getAllClasses(Object ns);

    /**
     * Return all interfaces the given class realizes.<p>
     *
     * @param cls the classifier
     * @return Collection
     */
    Collection getRealizedInterfaces(Object cls);

    /**
     * Returns all classes some generalizable element extends.
     *
     * @param clazz is the generalizable element
     * @return Collection
     */
    Collection getExtendedClassifiers(Object clazz);

    /**
     * Gets the generalization between two generalizable elements.
     * Returns null if there is none.<p>
     *
     * @param achild is the child generalizable element.
     * @param aparent is the parent generalizable element.
     * @return MGeneralization
     */
    Object getGeneralization(Object achild, Object aparent);

    /**
     * Gets the body of a comment.
     *
     * @param comment the comment
     * @return the body of the comment
     */
    String getBody(Object comment);

    /**
     * Returns all flows from some source modelelement to a target
     * modelelement.<p>
     *
     * @param source is the source model element.
     * @param target is the target model element.
     * @return Collection
     */
    Collection getFlows(Object source, Object target);

    /**
     * Returns all elements that extend some class.
     *
     * @param clazz is the class (a generalizable element)
     * @return Collection
     */
    Collection getExtendingElements(Object clazz);

    /**
     * Returns all classifiers that extend some classifier.
     *
     * @param clazz is the classifier.
     * @return Collection
     */
    Collection getExtendingClassifiers(Object clazz);

    /**
     * Returns all components found in this namespace and in its children.
     *
     * @param ns is the namespace.
     * @return Collection
     */
    Collection getAllComponents(Object ns);

    /**
     * Returns all components found in this namespace and in its children.
     *
     * @param ns is the namespace
     * @return Collection
     */
    Collection getAllDataTypes(Object ns);

    /**
     * Returns all components found in this namespace and in its children.<p>
     *
     * @param ns is the namespace
     * @return Collection
     */
    Collection getAllNodes(Object ns);

    /**
     * Gets all classifiers that are associated to the given
     * classifier (have an association relationship with the
     * classifier).<p>
     *
     * @param aclassifier an MClassifier
     * @return Collection
     */
    Collection getAssociatedClassifiers(Object aclassifier);

    /**
     * Gets the associations between the classifiers from and to. Returns null
     * if from or to is null or if there is no association between them.
     *
     * @param from a classifier
     * @param to a classifier
     * @return a Collection with Associations
     */
    Collection getAssociations(Object from, Object to);

    /**
     * Returns all classifiers found in this namespace and in its children.
     *
     * @param namespace the given namespace
     * @return Collection the collection of all classifiers
     *                    found in the namespace
     */
    Collection getAllClassifiers(Object namespace);

    /**
     * Returns all associations for some classifier.<p>
     *
     * @param oclassifier the given classifier
     * @return Collection all associations for the given classifier
     */
    Collection getAssociations(Object oclassifier);

    /**
     * Returns the associationend between a classifier and
     * an associaton.<p>
     *
     * @param type is the classifier
     * @param assoc is the association
     * @return An AssociationEnd.
     */
    Object getAssociationEnd(Object type, Object assoc);

    /**
     * Returns the contents (owned elements) of this classifier and
     * all its parents as specified in section 2.5.3.8 of the UML 1.3
     * spec.<p>
     *
     * @param clazz is the classifier
     * @return Collection
     */
    Collection getAllContents(Object clazz);

    /**
     * Returns all attributes of some classifier and of its parents.
     *
     * @param clazz is the classifier
     * @return Collection
     */
    Collection getAllAttributes(Object clazz);
    
    /**
     * Returns a Set containing all ModelElements visible 
     * outside of the Namespace. 
     * This is an "Additional Operation" from the UML spec.
     * 
     * @param ns the given namespace
     * @return the collection with modelelements
     */
    Collection getAllVisibleElements(Object ns);

    /**
     * Returns the source of a relation. The source of a relation is
     * defined as the modelelement that propagates this relation. If
     * there are more then 1 sources, only the first is returned. If
     * there is no source, null is returned. Examples of sources
     * include classifiers that are types to associationends, usecases
     * that are bases to extend and include relations and so on. A
     * source is allways the start from the arrow in the fig, the
     * destination the end.<p>
     *
     * This method also works to get the source of a Link.<p>
     *
     * TODO: move this method to a generic ModelHelper
     *
     * @param relationship is the relation
     * @return Object
     */
    Object getSource(Object relationship);

    /**
     * Returns the destination of a relation. The destination of a
     * relation is defined as the modelelement that receives this
     * relation.  If there are more then 1 destinations, only the
     * first is returned.  If there is no destination, null is
     * returned.  Examples of sources include classifiers that are
     * types to associationends, usecases that are bases to extend and
     * include relations and so on.  In the case of an association,
     * the destination is defined as the type of the second element in
     * the connections list.<p>
     *
     * This method also works for links.<p>
     *
     * TODO: move this method to a generic ModelHelper
     *
     * @param relationship is the relation
     * @return object
     */
    Object getDestination(Object relationship);

    /**
     * Returns the dependencies between some supplier modelelement and
     * some client modelelement.  Does not return the vica versa
     * relationship (dependency 'from client to supplier').<p>
     *
     * @param supplierObj a MModelElement
     * @param clientObj a MModelElement
     * @return Collection
     */
    Collection getDependencies(Object supplierObj, Object clientObj);

    /**
     * Returns all relationships between the source and dest
     * modelelement and vica versa.<p>
     *
     * @param source is the source model element
     * @param dest is the destination model element
     * @return Collection
     */
    Collection getRelationships(Object source, Object dest);

    /**
     * Returns true if some modelelement may be owned by the given
     * namespace.<p>
     *
     * @param mObj a MModelElement
     * @param nsObj a MNamespace
     * @return boolean
     */
    boolean isValidNamespace(Object mObj, Object nsObj);

    /**
     * Gets the first namespace two namespaces share. That is: it
     * returns the first namespace that owns the given namespaces
     * itself or some owner of the given namespaces.<p>
     *
     * @param ns1 is the first name space
     * @param ns2 is the second name space
     * @return The Namespace.
     */
    Object getFirstSharedNamespace(Object ns1, Object ns2);

    /**
     * Returns all possible namespaces that may be selected by some given
     * modelelement. Which namespaces are allowed, is decided in the method
     * isValidNamespace.<p>
     *
     * @param modelElement is the model element
     * @param model the model to search
     * @return Collection
     */
    Collection getAllPossibleNamespaces(Object modelElement, Object model);

    /**
     * Returns all children from some given generalizableelement on
     * all levels (the complete tree excluding the generalizable
     * element itself).<p>
     *
     * @param o is the generalizable element
     * @return Collection
     * @throws IllegalStateException if there is a circular reference.
     */
    Collection getChildren(Object o);

    /**
     * Returns all interfaces that are realized by the given class or
     * by its superclasses. It's possible that interfaces occur twice
     * in the collection returned. In that case there is a double
     * reference to that interface.
     *
     * @param o is the given class
     * @return Collection
     */
    Collection getAllRealizedInterfaces(Object o);

    /**
     * @param  association the association to be investigated
     * @return true if one of the association ends of the given association
     *         is of the composite kind
     */
    boolean hasCompositeEnd(Object association);

    /**
     * @param associationEnd is the association end
     * @param kindType the MAggregationKind as a string in lower case letter,
     *                 eg: composite.
     * @return true if the aggregation kinds are the same.
     */
    boolean equalsAggregationKind(Object associationEnd, String kindType);

    /**
     * Remove the given modelelement from a given comment.
     *
     * @param handle MComment
     * @param me MModelElement
     */
    void removeAnnotatedElement(Object handle, Object me);

    /**
     * This method removes a dependency from a model element.
     *
     * @param handle is the model element
     * @param dep is the dependency
     */
    void removeClientDependency(Object handle, Object dep);

    /**
     * Remove the given constraint from a given ModelElement.
     *
     * @param handle ModelElement
     * @param cons Constraint
     */
    void removeConstraint(Object handle, Object cons);

    /**
     * Removes a owned model element from a namespace.
     *
     * @param handle is the name space
     * @param value is the model element
     */
    void removeOwnedElement(Object handle, Object value);

    /**
     * This method removes a parameter from an operation.
     *
     * @param handle The operation.
     * @param parameter The parameter.
     */
    void removeParameter(Object handle, Object parameter);

    /**
     * Remove a source flow from a model element.
     *
     * @param handle The model element.
     * @param flow The flow.
     */
    void removeSourceFlow(Object handle, Object flow);

    /**
     * Adds a supplier dependency to some modelelement.
     *
     * @param supplier the supplier
     * @param dependency the dependency
     */
    void removeSupplierDependency(Object supplier, Object dependency);

    /**
     * Removes a named tagged value from a model element, ie subsequent calls
     * to getTaggedValue will return null for name, at least until a tagged
     * value with that name has been added again.
     *
     * @param handle the model element to remove the tagged value from
     * @param name the name of the tagged value
     * @throws IllegalArgumentException if handle isn't a model element
     * 
     * @deprecated by tfmorris for 0.23.3 - 
     * use {@link ExtensionMechanismsHelper#removeTaggedValue(Object, Object)}
     */
    void removeTaggedValue(Object handle, String name);

    /**
     * Add a target flow to a model element.
     *
     * @param handle The model element.
     * @param flow The flow to add.
     */
    void removeTargetFlow(Object handle, Object flow);

    /**
     * Adds an annotated element to a comment.
     *
     * @param comment The comment to which the element is annotated
     * @param annotatedElement The element to annotate
     */
    void addAnnotatedElement(Object comment, Object annotatedElement);

    /**
     * Adds a client model element to some dependency.
     *
     * @param handle dependency.
     * @param element The model element.
     * @throws IllegalArgumentException if the handle is not a dependency
     * or the element is not a model element.
     */
    void addClient(Object handle, Object element);

    /**
     * Adds a client dependency to some modelelement.
     *
     * @param handle the modelelement
     * @param dependency the dependency
     */
    void addClientDependency(Object handle, Object dependency);

    /**
     * Add a new comment to a model element.
     *
     * @param element the element to which the comment is to be added
     * @param comment the comment for the model element
     */
    void addComment(Object element, Object comment);

    /**
     * Add an End to a connection.
     *
     * @param handle Association or Link
     * @param connection AssociationEnd or LinkEnd
     */
    void addConnection(Object handle, Object connection);

    /**
     * Add an End to a connection.
     *
     * @param handle Association or Link
     * @param position the 0-based position at which
     *          to insert the AssociationEnd or LinkEnd
     * @param connection AssociationEnd or LinkEnd
     */
    void addConnection(Object handle, int position, Object connection);

    /**
     * Adds a constraint to some model element.
     *
     * @param handle model element
     * @param mc constraint
     */
    void addConstraint(Object handle, Object mc);

    /**
     * @param handle Component
     * @param node Node
     */
    void addDeploymentLocation(Object handle, Object node);

    /**
     * Adds a feature to some classifier.
     *
     * @param handle classifier
     * @param index position
     * @param f feature
     */
    void addFeature(Object handle, int index, Object f);

    /**
     * Add an EnumerationLiteral to an Enumeration at the specified position.
     * 
     * @param handle the enumeration
     * @param index the position
     * @param literal the EnumerationLiteral to be added
     */
    void addLiteral(Object handle, int index, Object literal);

    /**
     * Adds a feature to some classifier.
     *
     * @param handle classifier
     * @param f feature
     */
    void addFeature(Object handle, Object f);

    /**
     * Add the given Link to the given Link or Association.
     *
     * @param handle the Link or Association
     * @param link Link
     */
    void addLink(Object handle, Object link);

    /**
     * Adds a method to some operation and copies the op's attributes
     * to the method.
     *
     * @param handle is the operation
     * @param m is the method
     */
    void addMethod(Object handle, Object m);

    /**
     * Adds a model element to some namespace.
     *
     * @param handle namespace
     * @param me model element
     */
    void addOwnedElement(Object handle, Object me);

    /**
     * Add a Parameter to the given object at given location.
     *
     * @param handle The object that will get the Parameter:
     *               MEvent, MBehavioralFeature.
     * @param index the location
     * @param parameter Object that will be added
     */
    void addParameter(Object handle, int index, Object parameter);

    /**
     * Add a Parameter to the given object.
     *
     * @param handle The object that will get the Parameter:
     *               MObjectFlowState, MEvent, MBehavioralFeature, MClassifier.
     * @param parameter Object that will be added
     */
    void addParameter(Object handle, Object parameter);

    /**
     * Add a raised Signal to a Message or Operation.
     *
     * @param handle the Message or Operation
     * @param sig the Signal that is raised
     */
    void addRaisedSignal(Object handle, Object sig);

    /**
     * Add a source flow to a model element.
     *
     * @param handle The model element.
     * @param flow The flow.
     */
    void addSourceFlow(Object handle, Object flow);

    /**
     * Adds a supplier classifier to some abstraction.
     *
     * @param handle abstraction
     * @param element supplier model element
     */
    void addSupplier(Object handle, Object element);

    /**
     * Adds a supplier dependency to some modelelement.
     *
     * @param supplier the supplier
     * @param dependency the dependency
     */
    void addSupplierDependency(Object supplier, Object dependency);

    /**
     * Adds a TaggedValue to a ModelElement.
     *
     * @param handle ModelElement
     * @param taggedValue TaggedValue
     */
    void addTaggedValue(Object handle, Object taggedValue);

    /**
     * Add a target flow to a model element.
     *
     * @param handle The model element.
     * @param flow The flow to add.
     */
    void addTargetFlow(Object handle, Object flow);

    /**
     * Sets if of some model element is abstract.
     *
     * @param handle is the classifier
     * @param flag is true if it should be abstract
     */
    void setAbstract(Object handle, boolean flag);

    /**
     * Makes a Class active.
     *
     * @param handle Class
     * @param active boolean
     */
    void setActive(Object handle, boolean active);

    /**
     * Sets the aggregation of some model element.
     *
     * @param handle the model element to set aggregation
     * @param aggregationKind the aggregation kind
     */
    void setAggregation(Object handle, Object aggregationKind);

    /**
     * Sets the list of annotated elements of the given comment.
     *
     * @param handle the given comment
     * @param elems the list of annotated modelelements
     */
    void setAnnotatedElements(Object handle, Collection elems);

    /**
     * Sets the association of some model element.
     *
     * @param handle the model element to set association
     * @param association is the association
     */
    void setAssociation(Object handle, Object association);

    /**
     * Sets if some model element is a leaf.
     *
     * @param handle model element
     * @param flag is true if it is a leaf.
     */
    void setLeaf(Object handle, boolean flag);

    /**
     * Sets the raised signals of some behavioural feature.
     *
     * @param handle the behavioural feature
     * @param raisedSignals the raised signals
     */
    void setRaisedSignals(Object handle, Collection raisedSignals);

    /**
     * Sets a body of a given Method or Constraint.
     *
     * @param handle The method or constraint.
     * @param expr The body of the expression.
     *             If it is a method, this must be a ProcedureExpression.
     *             If it is a Constraint, this must be a BooleanExpression.
     */
    void setBody(Object handle, Object expr);

    /**
     * Set the Changeability of a StructuralFeature or AssociationEnd.
     *
     * @param handle StructuralFeature or AssociationEnd
     * @param ck ChangeableKind
     */
    void setChangeability(Object handle, Object ck);

    /**
     * Set the changeability of some feature.
     *
     * @param handle is the feature
     * @param flag is the changeability flag
     */
    void setChangeable(Object handle, boolean flag);

    /**
     * Set the child for a generalization.
     *
     * @param handle Generalization
     * @param child GeneralizableElement
     */
    void setChild(Object handle, Object child);

    /**
     * Set the concurrency of some operation.
     *
     * @param handle is the operation
     * @param concurrencyKind is the concurrency
     */
    void setConcurrency(Object handle, Object concurrencyKind);

    /**
     * Sets the list of connections of the given association or link.
     *
     * @param handle the given association or link
     * @param elems the list of association-ends or link-ends
     */
    void setConnections(Object handle, Collection elems);

    /**
     * Sets a default value of some parameter.
     *
     * @param handle is the parameter
     * @param expr is the expression
     */
    void setDefaultValue(Object handle, Object expr);

    /**
     * @param handle a generalization
     * @param discriminator the discriminator to set
     */
    void setDiscriminator(Object handle, String discriminator);

    /**
     * Set the feature at the given position.
     *
     * @param elem The classifier to set.
     * @param i The position. Start with 0.
     * @param impl The feature to set.
     */
    void setFeature(Object elem, int i, Object impl);

    /**
     * Sets the features of some model element.
     *
     * @param handle the model element to set features to
     * @param features the list of features
     */
    void setFeatures(Object handle, Collection features);

    /**
     * Set the Container of the given ElementResidence
     * to the given Component.
     *
     * @param handle the ElementResidence
     * @param component the Component
     */
    void setContainer(Object handle, Object component);

    /**
     * Sets an initial value.
     *
     * @param at attribute that we set the initial value of
     * @param expr that is the value to set. Can be <code>null</code>.
     */
    void setInitialValue(Object at, Object expr);

    /**
     * Set some parameters kind.
     *
     * @param handle is the parameter
     * @param kind is the directionkind
     * @see org.argouml.model.Model#getDirectionKind
     */
    void setKind(Object handle, Object kind);

    /**
     * Sets the container that owns the handle.<p>
     * 
     * <em>Warning: the implementation does not support setting the owner
     * of actions.</em>
     * Use setState1 etc. on action for that goal.<p>
     * Use a more specific method such as setOwner, setContainer, etc if
     * at all possible, rather than this method.
     *
     * @param handle
     *            The modelelement that must be added to the container
     * @param container
     *            The owning modelelement
     * @exception IllegalArgumentException
     *                when the handle or container is null or if the handle
     *                cannot be added to the container.
     */
    void setModelElementContainer(Object handle, Object container);

    /**
     * Sets a multiplicity of some model element.
     *
     * @param handle model element
     * @param arg multiplicity as string OR multiplicity object
     */
    void setMultiplicity(Object handle, Object arg);

    /**
     * Sets a name of some modelelement.
     *
     * @param handle is the model element
     * @param name to set
     */
    void setName(Object handle, String name);

    /**
     * Sets the body of a comment.<p>
     *
     * <em>NOTE:</em> For UML 1.3, this actually set Comment.name, but for UML
     * 1.4 it sets Comment.body.
     * <em>This is a behavior change in the API.</em><p>
     *
     * @param handle
     *            the Comment element
     * @param body
     *            the string
     */
    void setBody(Object handle, String body);

    /**
     * Sets a namespace of some modelelement.
     *
     * @param handle is the model element
     * @param ns is the namespace. Can be <code>null</code>.
     */
    void setNamespace(Object handle, Object ns);

    /**
     * Sets the navigability of some association end.
     *
     * @param handle is the association end
     * @param flag is the navigability flag
     */
    void setNavigable(Object handle, boolean flag);

    /**
     * Set the OrderingKind of a given AssociationEnd.
     *
     * @param handle AssociationEnd
     * @param ok OrderingKind
     */
    void setOrdering(Object handle, Object ok);

    /**
     * Set the owner of a Feature.
     *
     * @param handle Feature
     * @param owner Classifier or null
     */
    void setOwner(Object handle, Object owner);

    /**
     * @param handle Feature
     * @param os ScopeKind
     */
    void setOwnerScope(Object handle, Object os);

    /**
     * Sets the parameters of a classifier, event, objectflowstate or
     * behavioralfeature.
     *
     * @param handle the classifier, event, objectflowstate or
     * behavioralfeature
     * @param parameters is a Collection of parameters
     */
    void setParameters(Object handle, Collection parameters);

    /**
     * Sets the parent of a generalization.
     *
     * @param handle generalization
     * @param parent generalizable element (parent)
     */
    void setParent(Object handle, Object parent);

    /**
     * Set the PowerType of a Generalization.
     * @param handle Generalization
     * @param pt Classifier
     */
    void setPowertype(Object handle, Object pt);

    /**
     * Sets the qualified attributes of an association end.
     *
     * @param handle the association end
     * @param elems is a Collection of qualifiers
     * @deprecated by tfmorris for 0.23.4,
     * use {@link #setQualifiers(Object, List)}
     */
    void setQualifiers(Object handle, Collection elems);

    /**
     * Sets the qualified attributes of an association end.
     *
     * @param handle the association end
     * @param elems List of qualifiers
     */
    void setQualifiers(Object handle, List elems);

    /**
     * Sets the query flag of a behavioral feature.
     *
     * @param handle is the behavioral feature
     * @param flag is the query flag
     */
    void setQuery(Object handle, boolean flag);

    /**
     * @param handle ElementResidence
     * @param resident ModelElement or null
     */
    void setResident(Object handle, Object resident);

    /**
     * Sets the residents of some model element.
     *
     * @param handle the model element
     * @param residents collection
     */
    void setResidents(Object handle, Collection residents);

    /**
     * Sets if some model element is a root.
     *
     * @param handle model element
     * @param flag is true if it is a root
     */
    void setRoot(Object handle, boolean flag);

    /**
     * @param handle Flow
     * @param specifications the collection of ModelEvents (sourceFlow)
     */
    void setSources(Object handle, Collection specifications);

    /**
     * Set the Specification flag for a ModelElement.
     *
     * @param handle ModelElement
     * @param specification boolean
     */
    void setSpecification(Object handle, boolean specification);

    /**
     * Set the specification (i.e. the operation) of a Method.
     * 
     * @param method the method
     * @param specification the operation
     */
    void setSpecification(Object method, Object specification);

    /**
     * Set the specification of an Operation.
     * 
     * @param operation the operation
     * @param specification the operation
     */
    void setSpecification(Object operation, String specification);
    
    /**
     * Sets the specifications of some association end.
     *
     * @param handle the association end
     * @param specifications collection
     */
    void setSpecifications(Object handle, Collection specifications);

    /**
     * Adds a stereotype to some modelelement.
     *
     * @param modelElement model element
     * @param stereo stereotype
     */
    void addStereotype(Object modelElement, Object stereo);

    /**
     * Adds a collection of stereotype to some modelelement.
     *
     * @param modelElement model element
     * @param stereos stereotype
     */
    void addAllStereotypes(Object modelElement, Collection stereos);

    /**
     * Removes a stereotype from some modelelement.
     *
     * @param handle model element
     * @param stereo stereotype
     */
    void removeStereotype(Object handle, Object stereo);

    /**
     * Removes all stereotypes from a given modelelement.
     *
     * @param handle model element
     */
    void clearStereotypes(Object handle);

    /**
     * Sets a tagged value of some modelelement.
     *
     * @param handle is the model element
     * @param tag is the tag name (a string)
     * @param value is the value
     * @deprecated by tfmorris for 0.23.3 - 
     * use {@link ExtensionMechanismsHelper#setType(Object, Object)}
     * and {@link ExtensionMechanismsHelper#setValueOfTag(Object, String)}
     */
    void setTaggedValue(Object handle, String tag, String value);

    /**
     * Set the TaggedValues of a ModelElement.
     *
     * @param handle ModelElement
     * @param taggedValues Collection of TaggedValues
     */
    void setTaggedValues(Object handle, Collection taggedValues);

    /**
     * Set the target scope of some association end or structural feature.
     *
     * @param handle the model element
     * @param scopeKind the target scope
     */
    void setTargetScope(Object handle, Object scopeKind);

    /**
     * Sets the type of some parameter.
     *
     * @param handle is the model element
     * @param type is the type (a classifier)
     */
    void setType(Object handle, Object type);

    /**
     * Set the visibility of some modelelement.
     *
     * @param handle element
     * @param visibility is the visibility
     */
    void setVisibility(Object handle, Object visibility);

    /**
     * Remove a deployment location.
     *
     * @param handle Component from which to remove deploymentLocation
     * @param node Node to be removed
     */
    void removeDeploymentLocation(Object handle, Object node);

    /**
     * Remove a connection between an Association & AssociationEnd
     * or between a Link & LinkEnd.
     *
     * @param handle Association or Link
     * @param connection AssociationEnd or LinkEnd
     */
    void removeConnection(Object handle, Object connection);

    /**
     * Add an ElementResidence.
     *
     * @param handle ModelElement
     * @param residence ElementResidence to add
     */
    void addElementResidence(Object handle, Object residence);

    /**
     * Remove an ElementResidence.
     *
     * @param handle ModelElement
     * @param residence ElementResidence to remove
     */
    void removeElementResidence(Object handle, Object residence);

    /**
     * Set the (ordered) list of literals of an Enumeration.
     * @param enumeration Enumeration to add EnumerationLiterals too
     * @param literals A list of EnumerationLiterals
     */
    void setEnumerationLiterals(Object enumeration, List literals);
    
    /**
     * Returns names of all metatypes in metamodel
     * 
     * TODO: This method rightly belongs in a separate interface dealing
     * with instances of MOF types as opposed to UML types like all the
     * rest of the methods here do.
     * 
     * @return Collection containing Strings with names of all metatypes
     */
    Collection getAllMetatypeNames();

}
