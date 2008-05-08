// $Id$
// Copyright (c) 2005-2008 The Regents of the University of California. All
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
     * Return all elements of which this GeneralizableElement is a direct or
     * indirect subtype.
     * <p>
     * Note: This follows the Generalizations in UML, so the results do
     * <em>not</em> include Interfaces which are connected to Classes by
     * Realizations.
     * 
     * @param element
     *                the GeneralizableElement you want to have the parents for
     * @return a collection of the parents, each of which is a
     *         GeneralizableElement.
     */
    Collection getAllSupertypes(Object element);

    /**
     * Return the immediate supertypes of a GeneralizableElement.
     * 
     * @param generalizableElement
     *            the element you want to have the parents for
     * @return a collection of the parents, each of which is a
     *         GeneralizableElement.
     */
    Collection getSupertypes(Object generalizableElement);

    /**
     * Return all AssociationEnds of a given Classifier. Same as
     * {@link Facade#getAssociationEnds(Object)}.
     * 
     * @param classifier
     *            the classifier for which to get the association ends
     * 
     * @return a collection of the associationends
     * @see Facade#getAssociationEnds(Object)
     * @deprecated for 0.25.4 by tfmorris.  Use 
     *          {@link Facade#getAssociationEnds(Object)}.
     */
    @Deprecated
    Collection getAssociateEnds(Object classifier);

    /**
     * Return all AssociationEnds of a given Classifier plus all 
     * AssociationEnds of all of the Classifier's supertypes.
     * <p>
     * Note: Until 0.24 this was documented as returning the <em>opposite</em>
     * ends, but the implementation has returned the near ends since at least
     * beginning of the MDR implementation (0.18).
     * 
     * @param classifier
     *                the classifier for which to get the association ends
     * @return a collection of the AssociationEnds
     */
    Collection getAssociateEndsInh(Object classifier);

    /**
     * Remove a Feature from a Classifier.
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
     * Replace all operations of the given classifier
     * by the given list of operations.
     *
     * @param classifier the given classifier
     * @param operations the new operations
     */
    void setOperations(Object classifier, List operations);

    /**
     * Replace all attributes of the given classifier
     * by the given collection of attributes.
     * @param classifier the classifier
     * @param attributes an ordered list of new attributes
     */
    void setAttributes(Object classifier, List attributes);
    
    /**
     * Return all attributes of a given Classifier,
     * including inherited.
     *
     * @param classifier the classifier you want to have the attributes for
     * @return a collection of the attributes
     */
    Collection getAttributesInh(Object classifier);

    /**
     * Return all operations of a given Classifier,
     * including inherited.
     *
     * @param classifier the classifier you want to have the operations for
     * @return a collection of the operations
     */
    Collection getOperationsInh(Object classifier);
    
    /**
     * Return the collection of all direct parents. <p>
     * 
     * This function is additional operation number 1 for 
     * a GeneralizableElement from the UML1.4 standard.
     * 
     * @param generalizableElement the given element
     * @return a collection of GeneralizableElement
     */
    Collection getParents(Object generalizableElement);

    /**
     * Return all return parameters for an Operation.
     *
     * @param operation is the operation.
     * @return List of parameters of with direction kind of Return.
     */
    List getReturnParameters(Object operation);

    /**
     * Return the Operation which is the specification for a Method.<p>
     * 
     * There is also a method in the Facade interface with the same name which
     * may be what you want if you are looking for the specification of
     * something other than a Method.
     * 
     * @see Facade#getSpecification(Object)
     * 
     * @param object
     *            the method you want the realized operation of.
     * @return an operation, or null.
     */
    Object getSpecification(Object object);

    /**
     * Return all Interfaces of which this class is a realization.
     * 
     * @param classifier
     *            the class you want to have the interfaces for
     * @return a collection of the Interfaces
     * @deprecated for 0.25.2 by tfmorris. Use
     *             {@link Facade#getSpecifications(Object)}.
     */
    @Deprecated
    Collection getSpecifications(Object classifier);

    /**
     * Return all Classifiers of which this class is a
     * direct supertype.
     *
     * @param cls  the class you want to have the children for
     * @return a collection of the children, each of which is a
     *         GeneralizableElement.
     */
    Collection getSubtypes(Object cls);

    /**
     * Return all behavioralfeatures found in this element and its
     * children.<p>
     *
     * @param element is the element
     * @return Collection
     */
    Collection getAllBehavioralFeatures(Object element);

    /**
     * Return all behavioral features of a Classifier.
     * @param classifier The classifier
     * @return the list with all behavioral features of the classifier
     */
    List getBehavioralFeatures(Object classifier);

    /**
     * Return all interfaces found in this namespace and in its children.
     *
     * @param ns the given namespace
     * @return Collection with all interfaces found
     */
    Collection getAllInterfaces(Object ns);

    /**
     * Return all classes found in this namespace and in its children.<p>
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
     * Return all classes that the given GeneralizableElement extends.
     *
     * @param element is the GeneralizableElement
     * @return Collection
     */
    Collection getExtendedClassifiers(Object element);

    /**
     * Gets the generalization between two GeneralizableElements.
     * Returns null if there is none.<p>
     *
     * @param achild is the child GeneralizableElement.
     * @param aparent is the parent GeneralizableElement.
     * @return Generalization
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
     * Return all flows connecting from a source ModelElement to a target
     * ModelElement.<p>
     *
     * @param source is the source model element.
     * @param target is the target model element.
     * @return Collection
     */
    Collection getFlows(Object source, Object target);

    /**
     * Return all elements that extend a Class.
     *
     * @param element is the class (a GeneralizableElement)
     * @return Collection
     */
    Collection getExtendingElements(Object element);

    /**
     * Return all classifiers that extend a Classifier.
     *
     * @param classifier is the classifier.
     * @return Collection
     */
    Collection getExtendingClassifiers(Object classifier);

    /**
     * Return all components found in this namespace and in its children.
     *
     * @param ns is the namespace.
     * @return Collection
     */
    Collection getAllComponents(Object ns);

    /**
     * Return all components found in this namespace and in its children.
     *
     * @param ns is the namespace
     * @return Collection
     */
    Collection getAllDataTypes(Object ns);

    /**
     * Return all components found in this namespace and in its children.<p>
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
     * @param aclassifier an Classifier
     * @return Collection
     */
    Collection getAssociatedClassifiers(Object aclassifier);

    /**
     * Gets the associations between the classifiers from and to. Returns an
     * empty collection if no Associations are found.
     *
     * @param from a classifier
     * @param to a classifier
     * @return a Collection with Associations
     */
    Collection getAssociations(Object from, Object to);

    /**
     * Return all classifiers found in this namespace and in its children.
     *
     * @param namespace the given namespace
     * @return Collection the collection of all classifiers
     *                    found in the namespace
     */
    Collection getAllClassifiers(Object namespace);

    /**
     * Return all associations for a Classifier.<p>
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
     * Returns the contents (owned elements) of this Namespace and all its
     * parents as specified in section 2.5.3.8 of the UML 1.3 spec.
     * 
     * @param namespace is the classifier
     * @return Collection
     * @deprecated for 0.25.1 by tfmorris. Use
     *             {@link ModelManagementHelper#getAllContents(Object)}.
     */
    @Deprecated
    Collection getAllContents(Object namespace);

    /**
     * Return all Attributes of a Classifier and of its parents.
     *
     * @param classifier is the classifier
     * @return Collection
     */
    Collection getAllAttributes(Object classifier);
    
    /**
     * Return a Set containing all ModelElements visible 
     * outside of the Namespace. 
     * This is an "Additional Operation" from the UML spec.
     * 
     * @param ns the given namespace
     * @return the collection with ModelElements
     */
    Collection getAllVisibleElements(Object ns);

    /**
     * Return the source of a relation or Link. The source of a relation is
     * defined as the ModelElement that propagates this relation. If
     * there are more then 1 sources, only the first is returned. If
     * there is no source, null is returned. Examples of sources
     * include classifiers that are types to associationends, usecases
     * that are bases to extend and include relations and so on. A
     * source is always the start from the arrow in the fig, the
     * destination the end.<p>
     * 
     * TODO: move this method to a generic ModelHelper
     *
     * @param relationship is the relation
     * @return Object
     */
    Object getSource(Object relationship);

    /**
     * Returns the destination of a relation or link. The destination of a
     * relation is defined as the ModelElement that receives this
     * relation.  If there are more then 1 destinations, only the
     * first is returned.  If there is no destination, null is
     * returned.  Examples of sources include classifiers that are
     * types to associationends, usecases that are bases to extend and
     * include relations and so on.  In the case of an association,
     * the destination is defined as the type of the second element in
     * the connections list.<p>
     * 
     * TODO: move this method to a generic ModelHelper
     *
     * @param relationship is the relation
     * @return object
     */
    Object getDestination(Object relationship);

    /**
     * Return the dependencies between a supplier ModelElement and
     * a client ModelElement.  Does not return the inverse
     * relationship (dependency 'from client to supplier').<p>
     *
     * @param supplier a ModelElement
     * @param client a ModelElement
     * @return Collection
     */
    Collection getDependencies(Object supplier, Object client);


    /**
     * Return the packageImport (Permission with
     * <code>&lt;&lt;import&gt;&gt;</code> or
     * <code>&lt;&lt;access&gt;&gt;</code> stereotype in UML 1.4) between a
     * supplier Package and a client Namespace, if any.
     * 
     * @param supplier a Package
     * @param client a Namespace
     * @return the import or null
     */
    Object getPackageImport(Object supplier, Object client);


    /**
     * Return all packageImports (Permissions with
     * <code>&lt;&lt;import&gt;&gt;</code> or
     * <code>&lt;&lt;access&gt;&gt;</code> stereotype in UML 1.4) that this
     * Namespace is a client of.
     * 
     * @param client a Namespace
     * @return Collection of imports
     */
    Collection getPackageImports(Object client);
    
    /**
     * Return all relationships between the source and destination
     * ModelElement and vice versa.<p>
     *
     * @param source is the source model element
     * @param dest is the destination model element
     * @return Collection
     */
    Collection getRelationships(Object source, Object dest);

    /**
     * Return true if the given ModelElement may be owned by the given
     * namespace.<p>
     *
     * @param element a ModelElement
     * @param namespace a Namespace
     * @return boolean
     */
    boolean isValidNamespace(Object element, Object namespace);

    /**
     * Return the first namespace which two namespaces share. That is: it
     * returns the first namespace that owns the given namespaces
     * themselves or an owner of the given namespaces.<p>
     *
     * @param ns1 is the first name space
     * @param ns2 is the second name space
     * @return The Namespace.
     */
    Object getFirstSharedNamespace(Object ns1, Object ns2);

    /**
     * Return all possible namespaces that are valid owners of the given
     * ModelElement as determined by the method isValidNamespace.
     * 
     * @param modelElement
     *            is the model element
     * @param model
     *            the model to search
     * @return Collection
     */
    Collection getAllPossibleNamespaces(Object modelElement, Object model);

    /**
     * Return all children of a given GeneralizableElement on all levels (the
     * complete subtree excluding the GeneralizableElement itself).
     * 
     * @param element
     *            is the GeneralizableElement
     * @return Collection
     * @throws IllegalStateException
     *             if there is a circular reference.
     */
    Collection getChildren(Object element);

    /**
     * Return all interfaces that are realized by the given class or
     * by its superclasses. It's possible that interfaces occur twice
     * in the collection returned. In that case there is a double
     * reference to that interface.
     *
     * @param element is the given class
     * @return Collection
     */
    Collection getAllRealizedInterfaces(Object element);

    /**
     * Determine whether an Association has at least one AssociationEnd
     * of AggregationKind Composite.
     * @param  association the association to be investigated
     * @return true if one of the association ends of the given association
     *         is of the composite kind
     */
    boolean hasCompositeEnd(Object association);

    /**
     * @param associationEnd is the association end
     * @param kindType the AggregationKind as a string in lower case,
     *                 eg: composite.
     * @return true if the aggregation kinds are the same.
     */
    boolean equalsAggregationKind(Object associationEnd, String kindType);

    /**
     * Remove the given ModelElement from a given comment.
     *
     * @param handle Comment
     * @param me ModelElement
     */
    void removeAnnotatedElement(Object handle, Object me);

    /**
     * Remove a dependency from a ModelElement.
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
     * Remove an owned model element from a namespace.
     *
     * @param handle is the name space
     * @param value is the model element
     */
    void removeOwnedElement(Object handle, Object value);

    /**
     * Remove a Parameter from an Operation.
     *
     * @param handle The operation.
     * @param parameter The parameter.
     */
    void removeParameter(Object handle, Object parameter);

    /**
     * Remove a Qualifier from a AssociationEnd.
     * 
     * @param handle the AssociationEnd
     * @param qualifier the Qualifier attribute to be removed
     */
    void removeQualifier(Object handle, Object qualifier);

    /**
     * Remove a source flow from a ModelElement.
     *
     * @param handle The model element.
     * @param flow The flow.
     */
    void removeSourceFlow(Object handle, Object flow);

    /**
     * Add a supplier dependency to a ModelElement.
     *
     * @param supplier the supplier
     * @param dependency the dependency
     */
    void removeSupplierDependency(Object supplier, Object dependency);

    /**
     * Add a target flow to a ModelElement.
     *
     * @param handle The model element.
     * @param flow The flow to add.
     */
    void removeTargetFlow(Object handle, Object flow);

    /**
     * Remove a TemplateArgument from a Binding.
     *
     * @param binding The Binding.
     * @param argument The argument.
     */
    void removeTemplateArgument(Object binding, Object argument);
    
    /**
     * Remove a TemplateParameter from a ModelElement.
     *
     * @param handle The element.
     * @param parameter The parameter.
     */
    void removeTemplateParameter(Object handle, Object parameter);
    
    /**
     * Add an annotated element to a comment.
     *
     * @param comment The comment to which the element is annotated
     * @param annotatedElement The element to annotate
     */
    void addAnnotatedElement(Object comment, Object annotatedElement);

    /**
     * Add a client model element to a Dependency.
     *
     * @param dependency the Dependency.
     * @param element The model element.
     */
    void addClient(Object dependency, Object element);

    /**
     * Add a client Dependency to a ModelElement.
     *
     * @param handle the ModelElement
     * @param dependency the dependency
     */
    void addClientDependency(Object handle, Object dependency);

    /**
     * Add a new comment to a ModelElement.
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
     * Add a constraint to a ModelElement.
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
     * Add a feature to a Classifier.
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
     * Add a feature to a Classifier.
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
     * Add a method to an Operation and copy the values of the Operation's
     * visibility and scope attributes to the Method.
     *
     * @param handle is the operation
     * @param method is the method
     */
    void addMethod(Object handle, Object method);

    /**
     * Add a ModelElement to a Namespace.
     *
     * @param handle namespace
     * @param me model element
     */
    void addOwnedElement(Object handle, Object me);

    /**
     * Add a Parameter to the given object at given location.
     *
     * @param handle The object that will get the Parameter:
     *               Event, BehavioralFeature.
     * @param index the location
     * @param parameter Object that will be added
     */
    void addParameter(Object handle, int index, Object parameter);

    /**
     * Add a Parameter to the given object.
     *
     * @param handle The object that will get the Parameter:
     *               ObjectFlowState, Event, BehavioralFeature, Classifier.
     * @param parameter Object that will be added
     */
    void addParameter(Object handle, Object parameter);

    /**
     * @param handle the Association End
     * @param position the 0-based position at which
     *          to insert the Qualifier
     * @param qualifier the Qualifier attribute
     */
    void addQualifier(Object handle, int position, Object qualifier);

    /**
     * Add a raised Signal to a Message or Operation.
     *
     * @param handle the Message or Operation
     * @param sig the Signal that is raised
     */
    void addRaisedSignal(Object handle, Object sig);

    /**
     * Add a source flow to a ModelElement.
     *
     * @param handle The model element.
     * @param flow The flow.
     */
    void addSourceFlow(Object handle, Object flow);

    /**
     * Add a supplier to a Dependency.
     *
     * @param handle the Dependency to which to add the supplier
     * @param element supplier model element
     */
    void addSupplier(Object handle, Object element);

    /**
     * Add a supplier dependency to a ModelElement.
     *
     * @param supplier the supplier
     * @param dependency the dependency
     */
    void addSupplierDependency(Object supplier, Object dependency);

    /**
     * Add a TaggedValue to a ModelElement.
     * 
     * @param handle ModelElement
     * @param taggedValue TaggedValue
     * @deprecated for 0.25.1 by tfmorris. Use
     *         {@link ExtensionMechanismsHelper#addTaggedValue(Object, Object)}.
     */
    @Deprecated
    void addTaggedValue(Object handle, Object taggedValue);

    /**
     * Add a target flow to a ModelElement.
     *
     * @param handle The model element.
     * @param flow The flow to add.
     */
    void addTargetFlow(Object handle, Object flow);

    /**
     * Add a TemplateArgument to the given object at given location.
     *
     * @param handle The object that will get the TemplateParameter
     * @param index the location
     * @param argument Object that will be added
     */
    void addTemplateArgument(Object handle, int index, Object argument);

    /**
     * Add a TemplateArgument to the given object.
     *
     * @param handle The object that will get the TemplateArgument
     * @param argument Object that will be added
     */
    void addTemplateArgument(Object handle, Object argument);
    
    /**
     * Add a TemplateParameter to the given object at given location.
     *
     * @param handle The object that will get the TemplateParameter
     * @param index the location
     * @param parameter Object that will be added
     */
    void addTemplateParameter(Object handle, int index, Object parameter);

    /**
     * Add a TemplateTParameter to the given object.
     *
     * @param handle The object that will get the TemplateParameter
     * @param parameter Object that will be added
     */
    void addTemplateParameter(Object handle, Object parameter);
    
    /**
     * Set the isAbstract attribute of a GeneralizableElement.
     *
     * @param handle the GeneralizableElement
     * @param isAbstract is true if it should be abstract
     */
    void setAbstract(Object handle, boolean isAbstract);

    /**
     * Set the isActive attribute of a UML Class.
     *
     * @param handle Class
     * @param isActive boolean
     */
    void setActive(Object handle, boolean isActive);

    /**
     * Set the aggregation attribute of an AssociationEnd.
     * 
     * @param handle
     *            the AssociationEnd
     * @param aggregationKind
     *            an {@link AggregationKind} of Aggregate, Composite, or None
     *            returned from {@link Model#getAggregationKind()}.
     */
    void setAggregation(Object handle, Object aggregationKind);

    /**
     * Set the list of annotated elements for the given comment.
     *
     * @param handle the given comment
     * @param elems the collection of annotated ModelElements
     */
    void setAnnotatedElements(Object handle, Collection elems);

    /**
     * Set the association of a ModelElement.
     *
     * @param handle the model element to set association
     * @param association is the association
     */
    void setAssociation(Object handle, Object association);

    /**
     * Set the isLeaf attribute of a GeneralizableElement.
     *
     * @param handle the GeneralizableElement
     * @param isLeaf is true if it is a leaf, ie it has no subtypes.
     */
    void setLeaf(Object handle, boolean isLeaf);

    /**
     * Set the raised signals of a BehavioralFeature.
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
     * @param handle
     *            StructuralFeature or AssociationEnd
     * @param ck
     *            a {@link ChangeableKind} of Changeable, Frozen or AddOnly
     *            returned from {@link Model#getChangeableKind()}.
     * @deprecated for 0.25.4 by tfmorris. Use
     *             {@link #setReadOnly(Object, boolean)}. NOTE: The UML 1.x
     *             enum of AddOnly has no equivalent in UML 2.x.
     */
    @Deprecated
    void setChangeability(Object handle, Object ck);

    /**
     * Set the changeability of a StructuralFeature or AssociationEnd. A
     * convenience method which is equivalent to invoking 
     * {{@link #setChangeability(Object, Object)} with values of Changeable
     * or Frozen.
     * 
     * @param handle
     *            is the feature
     * @param changeable true for Changeable and false for Frozen.
     * @deprecated for 0.25.4 by tfmorris. Use
     *             {@link #setReadOnly(Object, boolean)} with a negated value.
     */
    @Deprecated
    void setChangeable(Object handle, boolean changeable);

    /**
     * Set the isReadOnly (UML 2.x) or changeable (UML 1.x) attribute of a
     * StructuralFeature or AssociationEnd.
     * 
     * @param handle
     *            is the feature
     * @param isReadOnly
     *            true for ReadOnly (Frozen in UML 1.x).
     * 
     */
    void setReadOnly(Object handle, boolean isReadOnly);
    
    /**
     * Set the child for a generalization.
     *
     * @param handle Generalization
     * @param child GeneralizableElement
     */
    void setChild(Object handle, Object child);

    /**
     * Set the concurrency of an Operation.
     * 
     * @param handle
     *            the Operation
     * @param concurrencyKind
     *            a {@link ConcurrencyKind} of Concurrent, Guarded, or
     *            Sequential returned from {@link Model#getConcurrencyKind()}.
     */
    void setConcurrency(Object handle, Object concurrencyKind);

    /**
     * Sets the list of connections of the given association or link.
     *
     * @param handle the given association or link
     * @param ends the list of association-ends or link-ends
     */
    void setConnections(Object handle, Collection ends);

    /**
     * Sets the default ModelEelement of a TemplateParameter.
     *
     * @param handle is the TemplateParameter
     * @param element is the default ModelElement or null
     */
    void setDefaultElement(Object handle, Object element);
    
    /**
     * Set the defaultValue of a Parameter.
     *
     * @param handle is the Parameter
     * @param expression the Expression to be used as the default value
     */
    void setDefaultValue(Object handle, Object expression);

    /**
     * Set the discriminator of a Generalization.
     * 
     * @param handle a Generalization
     * @param discriminator the String representing the discriminator
     */
    void setDiscriminator(Object handle, String discriminator);

    /**
     * Set the Feature of a Classifier at the given position.
     *
     * @param classifier The classifier to set.
     * @param index The position. Start with 0.
     * @param feature The feature to set.
     */
    void setFeature(Object classifier, int index, Object feature);

    /**
     * Sets the features of a Classifier.
     * 
     * @param classifier
     *            the Classifier to set features to
     * @param features
     *            the list of features. <em>NOTE:</em> although the API allows
     *            any type of Collection, the list of features is ordered, so
     *            only a List should be passed here. Other types of collections
     *            will be converted to a List in their natural order (which may
     *            not be the desired order.
     */
    void setFeatures(Object classifier, Collection features);

    /**
     * Set the Container of the given ElementResidence
     * to the given Component.
     *
     * @param handle the ElementResidence
     * @param component the Component
     */
    void setContainer(Object handle, Object component);

    /**
     * Set the initialValue of an Attribute.
     *
     * @param attribute attribute that we set the initial value of
     * @param expression that is the value to set. Can be <code>null</code>.
     */
    void setInitialValue(Object attribute, Object expression);

    /**
     * Set the kind of a Parameter or the Pseudostate.
     * 
     * @param handle
     *            is the Parameter or Pseudostate
     * @param kind
     *            a direction kind returned from
     *            {@link Model#getDirectionKind()} or a pseudostate kind
     *            returned from {@link Model#getPseudostateKind()}.
     */
    void setKind(Object handle, Object kind);

    /**
     * Set the container that owns the handle.<p>
     * 
     * <em>Warning: the implementation does not support setting the owner
     * of actions.</em>
     * Use setState1 etc. on action for that goal.<p>
     * Use a more specific method such as setOwner, setContainer, etc if
     * at all possible, rather than this method.
     *
     * @param handle
     *            The ModelElement that must be added to the container
     * @param container
     *            The owning ModelElement
     * @exception IllegalArgumentException
     *                when the handle or container is null or if the handle
     *                cannot be added to the container.
     */
    void setModelElementContainer(Object handle, Object container);

    /**
     * Set the multiplicity of a ModelElement.
     *
     * @param handle model element
     * @param arg multiplicity as string OR multiplicity object,
     *            null is a valid value (unspecified)
     */
    void setMultiplicity(Object handle, Object arg);

    /**
     * Set the name of a ModelElement.
     *
     * @param handle is the model element
     * @param name to set
     */
    void setName(Object handle, String name);

    /**
     * Set the body of a comment.<p>
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
     * Set the namespace of a ModelElement.
     *
     * @param handle is the model element
     * @param ns is the namespace. Can be <code>null</code>.
     */
    void setNamespace(Object handle, Object ns);

    /**
     * Set the navigability of an AssociationEnd.
     *
     * @param handle is the association end
     * @param flag is the navigability flag
     */
    void setNavigable(Object handle, boolean flag);

    /**
     * Set the OrderingKind of a given AssociationEnd.
     * 
     * @param handle
     *            AssociationEnd
     * @param ordering
     *            an {@link OrderingKind} returned from
     *            {@link Model#getOrderingKind()}.
     */
    void setOrdering(Object handle, Object ordering);

    /**
     * Set the owner of a Feature.
     *
     * @param handle Feature
     * @param owner Classifier or null
     */
    void setOwner(Object handle, Object owner);

    /**
     * Set the ownerScope of a Feature.
     * 
     * @param feature
     *            Feature
     * @param scopeKind
     *            a {@link ScopeKind} of Instance or Classifier
     *            {@link Model#getScopeKind()}.
     * @deprecated for 0.25.4 by tfmorris.  Use {@link #setStatic}.
     */
    @Deprecated
    void setOwnerScope(Object feature, Object scopeKind);

    /**
     * Set the isStatic (UML 2.x) or ownerScope (UML 1.x) attribute of a
     * Feature.
     * 
     * @param feature
     *            Feature
     * @param isStatic
     *            true if static (Classifier scope in UML 1.x). False is
     *            equivalent to the UML 1.x ScopeKind of 'Instance'.
     * @since 0.25.4
     */
    void setStatic(Object feature, boolean isStatic);

    /**
     * Set the parameter of a TemplateParameter.
     * 
     * @param handle the TemplateParameter
     * @param parameter the dummy ModelElement to be used as the parameter
     */
    void setParameter(Object handle, Object parameter);

    /**
     * Set the parameters of a classifier, event, objectflowstate or
     * behavioralfeature.
     * 
     * @param handle
     *            the classifier, event, objectflowstate or behavioralfeature
     * @param parameters
     *            is a Collection of parameters
     */
    void setParameters(Object handle, Collection parameters);

    /**
     * Set the parent of a generalization.
     *
     * @param handle generalization
     * @param parent GeneralizableElement (parent)
     */
    void setParent(Object handle, Object parent);

    /**
     * Set the powerType of a Generalization.
     * 
     * @param handle
     *            Generalization
     * @param powerType
     *            the Classifier to set as the powerType or null to clear the
     *            powerType.
     */
    void setPowertype(Object handle, Object powerType);

    /**
     * Set the qualifier attributes of an AssociationEnd.
     *
     * @param handle the association end
     * @param qualifiers List of Attributes to be set as qualifiers
     */
    void setQualifiers(Object handle, List qualifiers);

    /**
     * Sets the isQuery attribute of a BehavioralFeature.
     *
     * @param handle is the behavioral feature
     * @param isQuery new value for the isQuery attribute
     */
    void setQuery(Object handle, boolean isQuery);

    /**
     * Sets the ModelElement of a ElementResidence.
     * 
     * @param handle ElementResidence
     * @param resident ModelElement or null
     */
    void setResident(Object handle, Object resident);

    /**
     * Sets the residents of a NodeInstance or ComponentInstance.
     *
     * @param handle the NodeInstance or ComponentInstance
     * @param residents collection of 
     *          ComponentInstances (in case the handle is a NodeInstance)
     *          or Instances (in case the handle is a ComponentInstance)
     */
    void setResidents(Object handle, Collection residents);

    /**
     * Set the isRoot attribute of a GeneralizableElement.
     *
     * @param handle model element
     * @param isRoot is true if it is a root ie it has no supertypes
     */
    void setRoot(Object handle, boolean isRoot);

    /**
     * @param handle Flow
     * @param specifications the collection of ModelEvents (sourceFlow)
     */
    void setSources(Object handle, Collection specifications);

    /**
     * Set the isSpecification attribute for the ElementOwnership of a
     * ModelElement.
     * 
     * @param handle
     *            ModelElement
     * @param isSpecification
     *            true of the element is specification
     */
    void setSpecification(Object handle, boolean isSpecification);

    /**
     * Set the specification (i.e. the Operation) of a Method.
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
     * Set the specifications of an AssociationEnd.
     *
     * @param handle the association end
     * @param specifications collection
     */
    void setSpecifications(Object handle, Collection specifications);

    /**
     * Add a stereotype to a ModelElement.
     *
     * @param modelElement model element
     * @param stereo stereotype
     */
    void addStereotype(Object modelElement, Object stereo);

    /**
     * Add a collection of stereotype to a ModelElement.
     *
     * @param modelElement model element
     * @param stereos stereotype
     */
    void addAllStereotypes(Object modelElement, Collection stereos);

    /**
     * Remove a stereotype from a ModelElement.
     *
     * @param handle model element
     * @param stereo stereotype
     */
    void removeStereotype(Object handle, Object stereo);

    /**
     * Remove all stereotypes from a given ModelElement.
     *
     * @param handle model element
     */
    void clearStereotypes(Object handle);

    /**
     * Set a tagged value of a ModelElement.
     * 
     * @param handle is the model element
     * @param tag is the tag name (a string)
     * @param value is the value
     * @deprecated by tfmorris for 0.23.3. Use
     *             {@link ExtensionMechanismsHelper#setType(Object, Object)} and
     *          {@link ExtensionMechanismsHelper#setValueOfTag(Object, String)}.
     */
    @Deprecated
    void setTaggedValue(Object handle, String tag, String value);

    /**
     * Set the TaggedValues of a ModelElement.
     *
     * @param handle ModelElement
     * @param taggedValues Collection of TaggedValues
     * @deprecated for 0.25.1 by tfmorris. Use 
     *     {@link ExtensionMechanismsHelper#setTaggedValue(Object, Collection)}.
     */
    @Deprecated
    void setTaggedValues(Object handle, Collection taggedValues);

    /**
     * Set the targetScope of an AssociationEnd or StructuralFeature.
     * 
     * @param handle the model element
     * @param targetScope a {@link ScopeKind} of Instance or Classifier returned
     *                from {@link Model#getScopeKind()}.
     * @deprecated for 0.25.4 by tfmorris. Target Scope has been removed from
     *             the UML 2.x spec so this should not be used.
     */
    @Deprecated
    void setTargetScope(Object handle, Object targetScope);

    /**
     * Set the type of a ModelElement.
     * 
     * @param handle
     *            a ModelElement which is one of: Parameter, AssociationEnd,
     *            StructuralFeature, ClassifierInState, or ObjectFlowState.
     * @param type
     *            a Classifier representing the type
     */
    void setType(Object handle, Object type);

    /**
     * Set the visibility of a ModelElement, 
     * ElementResidence, ElementImport.
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
     * Returns names of all metatypes in metamodel.
     * 
     * TODO: This method rightly belongs in a separate interface dealing
     * with instances of MOF types as opposed to UML types like all the
     * rest of the methods here do.
     * 
     * @return Collection containing Strings with names of all metatypes
     */
    Collection<String> getAllMetatypeNames();

    /**
     * Returns names of all metamodel value elements including datatypes,
     * enumerations, and primitive types.
     * 
     * TODO: This method rightly belongs in a separate interface dealing
     * with instances of MOF types as opposed to UML types like all the
     * rest of the methods here do.
     * 
     * @return Collection containing Strings with names of all metatypes
     */
    Collection<String> getAllMetaDatatypeNames();
}
