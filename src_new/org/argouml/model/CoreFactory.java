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

import org.argouml.application.api.NotationName;
import org.argouml.uml.diagram.static_structure.ui.CommentEdge;

import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationClass;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MBehavioralFeature;
import ru.novosoft.uml.foundation.core.MBinding;
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
import ru.novosoft.uml.foundation.core.MPresentationElement;
import ru.novosoft.uml.foundation.core.MRelationship;
import ru.novosoft.uml.foundation.core.MStructuralFeature;
import ru.novosoft.uml.foundation.core.MTemplateParameter;
import ru.novosoft.uml.foundation.core.MUsage;

/**
 * The interface to the factory for the Core.<p>
 *
 * Created from the old CoreFactory.
 */
public interface CoreFactory {
    /**
     * Create an empty but initialized instance of a UML Abstraction.
     *
     * @return an initialized UML Abstraction instance.
     */
    Object createAbstraction();

    /**
     * Build an empty but initialized instance of a UML Abstraction
     * with a given name.
     *
     * @param name The name.
     * @return an initialized UML Abstraction instance.
     * @param supplier the supplier of the abstraction
     * @param client the client of the abstraction
     */
    Object buildAbstraction(String name, Object supplier, Object client);

    /**
     * Create an empty but initialized instance of a UML Association.
     *
     * @return an initialized UML Association instance.
     */
    MAssociation createAssociation();

    /**
     * Create an empty but initialized instance of a UML AssociationClass.
     *
     * @return an initialized UML AssociationClass instance.
     */
    MAssociationClass createAssociationClass();

    /**
     * Create an empty but initialized instance of a UML AssociationEnd.
     *
     * @return an initialized UML AssociationEnd instance.
     */
    MAssociationEnd createAssociationEnd();

    /**
     * Create an empty but initialized instance of a UML Attribute.
     *
     * @return an initialized UML Attribute instance.
     */
    MAttribute createAttribute();

    /**
     * Create an empty but initialized instance of a UML Binding.
     *
     * @return an initialized UML Binding instance.
     */
    MBinding createBinding();

    /**
     * Create an empty but initialized instance of a UML Class.
     *
     * @return an initialized UML Class instance.
     */
    MClass createClass();

    /**
     * Create an empty but initialized instance of a UML Classifier.
     *
     * @return an initialized UML Classifier instance.
     */
    MClassifier createClassifier();

    /**
     * Create an empty but initialized instance of a UML Comment.
     *
     * @return an initialized UML Comment instance.
     */
    MComment createComment();

    /**
     * Create an empty but initialized instance of a UML Component.
     *
     * @return an initialized UML Component instance.
     */
    MComponent createComponent();

    /**
     * Create an empty but initialized instance of a UML Constraint.
     *
     * @return an initialized UML Constraint instance.
     */
    MConstraint createConstraint();

    /**
     * Create an empty but initialized instance of a UML DataType.
     *
     * @return an initialized UML DataType instance.
     */
    MDataType createDataType();

    /**
     * Create an empty but initialized instance of a UML Dependency.
     *
     * @return an initialized UML Dependency instance.
     */
    MDependency createDependency();

    /**
     * Create an empty but initialized instance of a UML ElementResidence.
     *
     * @return an initialized UML ElementResidence instance.
     */
    MElementResidence createElementResidence();

    /**
     * Create an empty but initialized instance of a UML Flow.
     *
     * @return an initialized UML Flow instance.
     */
    MFlow createFlow();

    /**
     * Create an empty but initialized instance of a UML Generalization.
     *
     * @return an initialized UML Generalization instance.
     */
    MGeneralization createGeneralization();

    /**
     * Create an empty but initialized instance of a UML Interface.
     *
     * @return an initialized UML Interface instance.
     */
    MInterface createInterface();

    /**
     * Create an empty but initialized instance of a UML Method.
     *
     * @return an initialized UML Method instance.
     */
    MMethod createMethod();

    /**
     * Create an empty but initialized instance of a UML Namespace.
     *
     * @return an initialized UML Namespace instance.
     */
    MNamespace createNamespace();

    /**
     * Create an empty but initialized instance of a UML Node.
     *
     * @return an initialized UML Node instance.
     */
    MNode createNode();

    /**
     * Create an empty but initialized instance of a UML Operation.
     *
     * @return an initialized UML Operation instance.
     */
    MOperation createOperation();

    /**
     * Create an empty but initialized instance of a UML Parameter.
     *
     * @return an initialized UML Parameter instance.
     */
    MParameter createParameter();

    /**
     * Create an empty but initialized instance of a UML Permission.
     *
     * @return an initialized UML Permission instance.
     */
    MPermission createPermission();

    /**
     * Create an empty but initialized instance of a UML Relationship.
     *
     * @return an initialized UML Relationship instance.
     */
    MRelationship createRelationship();

    /**
     * Create an empty but initialized instance of a UML TemplateParameter.
     *
     * @return an initialized UML TemplateParameter instance.
     */
    MTemplateParameter createTemplateParameter();

    /**
     * Create an empty but initialized instance of a UML Usage.
     *
     * @return an initialized UML Usage instance.
     */
    MUsage createUsage();

    /**
     * Builds a binary associationrole on basis of two classifierroles,
     * navigation and aggregation.
     *
     * @param fromClassifier   the first given classifier
     * @param aggregationKind1 the first aggregationkind
     * @param toClassifier     the second given classifier
     * @param aggregationKind2 the second aggregationkind
     * @param unidirectional true if unidirectional
     * @return the newly build binary associationrole
     */
    Object buildAssociation(Object fromClassifier, Object aggregationKind1,
            Object toClassifier, Object aggregationKind2,
            Boolean unidirectional);

    /**
     * Builds a binary associations between two classifiers with
     * default values for the association ends and the association
     * itself.<p>
     *
     * @param classifier1 The first classifier to connect
     * @param classifier2 The second classifier to connect
     * @return MAssociation
     */
    MAssociation buildAssociation(Object classifier1, Object classifier2);

    /**
     * Builds a binary association with a direction, aggregation
     * and a given name.
     *
     * @param c1 The first classifier to connect to
     * @param nav1 The navigability of the Associaton end
     * @param c2 The second classifier to connect to
     * @param nav2 The navigability of the second Associaton end
     * @param name the given name
     * @return association
     */
    Object buildAssociation(Object c1, boolean nav1, Object c2, boolean nav2,
            String name);

    /**
     * Builds an associationClass between classifier end1 and end2 with a
     * default class.<p>
     *
     * @param end1 the first given classifier
     * @param end2 the second given classifier
     * @return MAssociationClass
     */
    MAssociationClass buildAssociationClass(MClassifier end1, MClassifier end2);

    /**
     * Builds a default attribute.
     *
     * @param model The model the attribute belongs to.
     * @param theIntType The type of the attribute.
     * @return The newly built attribute.
     */
    Object buildAttribute(Object model, Object theIntType);

    /**
     * Builds an attribute owned by some classifier cls. I don't know
     * if this is legal for an interface (purely UML speaking). In
     * this method it is.<p>
     *
     * @param handle the given classifier
     * @return the newly build attribute
     */
    Object buildAttribute(Object handle, Object model, Object intType,
            Collection propertyChangeListeners);

    /**
     * Builds a default implementation for a class. The class is not owned by
     * any model element by default. Users should not forget to add ownership.
     *
     * @return MClass
     */
    Object buildClass();

    /**
     * Builds a class with a given namespace.
     *
     * @param owner the namespace
     * @return MClass
     * @see #buildClass()
     */
    Object buildClass(Object owner);

    /**
     * Builds a class with a given name.
     *
     * @param name the given name
     * @return MClass
     * @see #buildClass()
     */
    Object buildClass(String name);

    /**
     * Builds a class with a given name and namespace.
     *
     * @param name the given name
     * @param owner the namespace
     * @return MClass
     * @see #buildClass()
     */
    Object buildClass(String name, Object owner);

    /**
     * Builds a default implementation for an interface. The interface
     * is not owned by any model element by default. Users should not
     * forget to add ownership.
     *
     * @return MInterface
     */
    Object buildInterface();

    /**
     * Builds an interface with a given namespace.
     *
     * @param owner is the owner
     * @return MInterface
     * @see #buildInterface()
     */
    Object buildInterface(Object owner);

    /**
     * Builds an interface with a given name.
     *
     * @param name is the given name.
     * @return MInterface
     * @see #buildInterface()
     */
    Object buildInterface(String name);

    /**
     * Builds an interface with a given name and namespace.
     *
     * @param name is the given name
     * @param owner is the namespace
     * @return MInterface
     * @see #buildInterface()
     */
    Object buildInterface(String name, Object owner);

    /**
     * Builds a datatype with a given name and namespace.
     *
     * @param name is the name
     * @param owner is the namespace
     * @return an initialized UML DataType instance.
     */
    Object buildDataType(String name, Object owner);

    /**
     * Builds a modelelement dependency between two modelelements.<p>
     *
     * @param clientObj is the client
     * @param supplierObj is the supplier
     * @return MDependency
     */
    Object buildDependency(Object clientObj, Object supplierObj);

    /**
     * Builds a modelelement permission between two modelelements.
     *
     * @param clientObj is the client
     * @param supplierObj is the supplier
     * @return MPermission
     */
    MPermission buildPermission(Object clientObj, Object supplierObj);

    /**
     * Builds a generalization between a parent and a child with a given name.
     *
     * @param child is the child
     * @param parent is the parent
     * @param name is the given name
     * @return generalization
     */
    Object buildGeneralization(Object child, Object parent, String name);

    /**
     * Builds a generalization between a parent and a child. Does not check if
     * multiple inheritance is allowed for the current notation.
     *
     * @param child1 is the child
     * @param parent1 is the parent
     * @return MGeneralization
     */
    MGeneralization buildGeneralization(Object child1, Object parent1);

    /**
     * Builds a default method belonging to a certain operation. The
     * language of the body is set to the selected Notation
     * language. The body of the method is set to an emtpy string.
     *
     * @param op is the operation
     * @return MMethod
     */
    MMethod buildMethod(MOperation op);

    /**
     * Builds a method belonging to a certain operation.
     * @param op The operation this method belongs to
     * @param notation The notationname (language name) of the body
     * @param body The actual body of the method
     * @return MMethod
     */
    MMethod buildMethod(MOperation op, NotationName notation, String body);

    /**
     * Builds a method with a given name.
     *
     * @param name is the given name
     * @return method
     */
    MMethod buildMethod(String name);

    /**
     * Builds an operation for a classifier.
     *
     * @param classifier is the given classifier
     * @param model is the model to which the class belongs
     * @param voidType the type of the return parameter
     * @param propertyChangeListeners
     * @return the operation
     */
    Object buildOperation(Object classifier, Object model, Object voidType,
            Collection propertyChangeListeners);

    /**
     * Builds an operation with a given name for classifier.
     *
     * @param cls is the classifier that shall own the operation
     * @param model is the model that contains the class
     * @param voidType the type of the return parameter
     * @param name the given name for the operation
     * @param propertyChangeListeners
     * @return the operation
     */
    Object buildOperation(Object cls, Object model, Object voidType,
            String name, Collection propertyChangeListeners);

    /**
     * Adds a parameter initialized to default values to a given event
     * or behavioral feature.
     *
     * @param o an event or behavioral feature
     * @param model the model to which the event or behavioral feature belongs
     * @param voidType the type of the return parameter
     * @param propertyChangeListeners
     * @return the parameter
     */
    MParameter buildParameter(Object o, Object model, Object voidType,
            Collection propertyChangeListeners);

    /**
     * Builds a realization between some supplier (for example an
     * interface in Java) and a client who implements the realization.
     *
     * @param clnt is the client
     * @param spplr is the supplier
     * @param model the namespace to use if client and
     * supplier are of different namespace
     * @return Object the created abstraction
     */
    Object buildRealization(Object clnt, Object spplr, Object model);

    /**
     * Builds a usage between some client and a supplier. If client
     * and supplier do not have the same model, an
     * {@link IllegalArgumentException} is thrown.
     *
     * @param client is the client
     * @param supplier is the supplier
     * @return MUsage
     */
    MUsage buildUsage(MModelElement client, MModelElement supplier);

    /**
     * Builds a comment inluding a reference to the given modelelement
     * to comment.  If the element is null, the comment is still build
     * since it is not mandatory to have an annotated element in the
     * comment.<p>
     *
     * @param element is the model element
     * @param model the namespace for the comment
     * @return MComment
     */
    Object buildComment(Object element, Object model);

    /**
     * Builds the model behind a connection between a comment and
     * the annotated modelelement.
     *
     * @param from The comment or annotated element.
     * @param to The comment or annotated element.
     * @return A commentEdge representing the model behind the connection
     *         between a comment and an annotated modelelement.
     */
    CommentEdge buildCommentConnection(Object from, Object to);

    /**
     * Builds a constraint that constraints the given modelelement.
     * The namespace of the constraint will be the same as the
     * namespace of the given modelelement.<p>
     *
     * @param constrElement The constrained element.
     * @return MConstraint
     */
    Object buildConstraint(Object constrElement);

    /**
     * Builds a constraint with a given name and boolean expression.<p>
     *
     * @param name is the given name
     * @param bexpr boolean expression
     * @return constraint
     */
    Object buildConstraint(String name, Object bexpr);

    /**
     * @param elem the abstraction to be deleted
     */
    void deleteAbstraction(Object elem);

    /**
     * @param elem the association to be deleted
     */
    void deleteAssociation(MAssociation elem);

    /**
     * @param elem the a. to be deleted
     */
    void deleteAssociationClass(MAssociationClass elem);

    /**
     * Does a 'cascading delete' to all modelelements that are associated
     * with this element that would be in an illegal state after deletion
     * of the element. Does not do an cascading delete for elements that
     * are deleted by the NSUML method remove. This method should not be called
     * directly.<p>
     *
     * In the case of an associationend these are the following elements:<ul>
     * <li>Binary Associations that 'loose' one of the associationends by this
     *     deletion.
     * </ul>
     *
     * @param elem
     * @see UmlFactory#delete(Object)
     */
    void deleteAssociationEnd(MAssociationEnd elem);

    /**
     * @param elem the attribute to be deleted
     */
    void deleteAttribute(MAttribute elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteBehavioralFeature(MBehavioralFeature elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteBinding(MBinding elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteClass(MClass elem);

    /**
     * Does a 'cascading delete' to all modelelements that are associated
     * with this element that would be in an illegal state after deletion
     * of the element. Does not do an cascading delete for elements that
     * are deleted by the NSUML method remove. This method should not be called
     * directly.<p>
     *
     * In the case of a classifier these are the following elements:<ul>
     * <li>AssociationEnds that have this classifier as type
     * </ul>
     *
     * @param elem
     * @see UmlFactory#delete(Object)
     */
    void deleteClassifier(Object elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteComment(MComment elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteComponent(MComponent elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteConstraint(MConstraint elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteDataType(MDataType elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteDependency(MDependency elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteElement(MElement elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteElementResidence(MElementResidence elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteFeature(MFeature elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteFlow(MFlow elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteGeneralizableElement(MGeneralizableElement elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteGeneralization(MGeneralization elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteInterface(MInterface elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteMethod(MMethod elem);

    /**
     * Does a 'cascading delete' to all modelelements that are associated
     * with this element that would be in an illegal state after deletion
     * of the element. Does not do an cascading delete for elements that
     * are deleted by the NSUML method remove. This method should not be called
     * directly.<p>
     *
     * In the case of a modelelement these are the following elements:<ul>
     * <li>Dependencies that have the modelelement as supplier or as a client
     * and are binary. (that is, they only have one supplier and one client)
     * </ul>
     *
     * @param elem
     * @see UmlFactory#delete(Object)
     */
    void deleteModelElement(MModelElement elem);

    /**
     * A namespace deletes its owned elements.
     *
     * @param elem is the namespace.
     */
    void deleteNamespace(MNamespace elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteNode(MNode elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteOperation(MOperation elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteParameter(MParameter elem);

    /**
     * @param elem the element to be deleted
     */
    void deletePermission(MPermission elem);

    /**
     * @param elem the element to be deleted
     */
    void deletePresentationElement(MPresentationElement elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteRelationship(MRelationship elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteStructuralFeature(MStructuralFeature elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteTemplateParameter(MTemplateParameter elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteUsage(MUsage elem);

    /**
     * Copies a class, and it's features. This may also require other
     * classifiers to be copied.
     *
     * @param source is the class to copy.
     * @param ns is the namespace to put the copy in.
     * @return a newly created class.
     */
    MClass copyClass(MClass source, MNamespace ns);

    /**
     * Copies a datatype, and it's features. This may also require other
     * classifiers to be copied.
     *
     * @param source is the datatype to copy.
     * @param ns is the namespace to put the copy in.
     * @return a newly created data type.
     */
    MDataType copyDataType(MDataType source, MNamespace ns);

    /**
     * Copies an interface, and it's features. This may also require other
     * classifiers to be copied.
     *
     * @param source is the interface to copy.
     * @param ns is the namespace to put the copy in.
     * @return a newly created interface.
     */
    MInterface copyInterface(MInterface source, MNamespace ns);

    /**
     * Used by the copy functions. Do not call this function directly.
     *
     * @param source the source class
     * @param target the target class
     */
    void doCopyClass(MClass source, MClass target);

    /**
     * Used by the copy functions. Do not call this function directly.
     * TODO: actions? instances? collaborations etc?
     *
     * @param source the source classifier
     * @param target the target classifier
     */
    void doCopyClassifier(MClassifier source, MClassifier target);

    /**
     * Used by the copy functions. Do not call this function directly.
     *
     * @param source the source datatype
     * @param target the target datatype
     */
    void doCopyDataType(MDataType source, MDataType target);

    /**
     * Used by the copy functions. Do not call this function directly.
     * TODO: generalizations, specializations?
     *
     * @param source the source generalizable element
     * @param target the target generalizable element
     */
    void doCopyGeneralizableElement(MGeneralizableElement source,
            MGeneralizableElement target);

    /**
     * Used by the copy functions. Do not call this function directly.
     *
     * @param source the source interface
     * @param target the target interface
     */
    void doCopyInterface(MInterface source, MInterface target);

    /**
     * Used by the copy functions. Do not call this function directly.
     * TODO: template parameters, default type
     * TODO: constraining elements
     * TODO: flows, dependencies, comments, bindings, contexts ???
     * TODO: contents, residences ???
     *
     * @param source the source me
     * @param target the target me
     */
    void doCopyModelElement(MModelElement source, MModelElement target);

    /**
     * Used by the copy functions. Do not call this function directly.
     *
     * @param source the source namespace
     * @param target the target namespace
     */
    void doCopyNamespace(MNamespace source, MNamespace target);
}
