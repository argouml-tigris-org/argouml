// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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
     * This method returns the name of a feature.
     *
     * @param o is the feature
     * @return name
     */
    String getFeatureName(Object o);

    /**
     * This method returns if the object is a method.
     *
     * @param o object
     * @return true if it's a method, false if not
     */
    boolean isMethod(Object o);

    /**
     * This method returns if the object is an operation.
     *
     * @param o object
     * @return true if it's an operation, false if not
     */
    boolean isOperation(Object o);

    /**
     * This method returns all operations of a given Classifier.
     *
     * @param classifier the classifier you want to have the operations for
     * @return a collection of the operations
     */
    Collection getOperations(Object classifier);

    /**
     * This method replaces all operations of the given classifier
     * by the given collection of operations.
     *
     * @param classifier the given classifier
     * @param operations the new operations
     */
    void setOperations(Object classifier, Collection operations);

    /**
     * This method returns all attributes of a given Classifier.
     *
     * @param classifier the classifier you want to have the attributes for
     * @return a collection of the attributes
     */
    Collection getAttributes(Object classifier);

    /**
     * This method replaces all attributes of the given classifier
     * by the given collection of attributes.
     * @param classifier the classifier
     * @param attributes the new attributes
     */
    void setAttributes(Object classifier, Collection attributes);

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
     * This method finds all paramters of the given operation which
     * have the MParamterDirectionType RETURN. If it is only one, it
     * is returned.  In case there are no return parameters, null is
     * returned. If there is more than one return paramter, first of
     * them is returned, but a message is logged.<p>
     *
     * @param operation1 the operation you want to find the return
     * parameter for.
     * @return If this operation has only one paramter with Kind: RETURN,
     *         this is it, otherwise null
     */
    Object getReturnParameter(Object operation1);

    /**
     * Returns all return parameters for an operation.
     *
     * @param operation is the operation.
     * @return Collection
     */
    Collection getReturnParameters(Object operation);

    /**
     * Returns the operation that some method realized. Returns null if
     * object isn't a method or, possibly, if the method isn't properly
     * defined.
     *
     * @param object  the method you want the realized operation of.
     * @return an operation, or null.
     */
    Object getSpecification(Object object);

    /**
     * Returns all Interfaces of which this class is a realization.<p>
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
     * @return the collection with all behavioral features of some classifier
     */
    Collection getBehavioralFeatures(Object clazz);

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
     * This method also works for CommentEdge.<p>
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
     * This method also works for CommentEdge<p>
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
     * Returns the base classes (that are the classes that do not have any
     * generalizations) for some given namespace. Personally, this seems a
     * pointless operation to me but in GoModelToBaseElements this is done like
     * this for some reason.
     * TODO: find out if someone uses this.
     *
     * @param o is the given namespace.
     * @return Collection
     */
    Collection getBaseClasses(Object o);

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
}
