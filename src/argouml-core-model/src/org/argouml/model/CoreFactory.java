/* $Id$
 *******************************************************************************
 * Copyright (c) 2009,2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris
 *    Thomas Neustupny
 *******************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2005-2007 The Regents of the University of California. All
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

import java.util.List;


/**
 * The interface to the factory for the Core.
 */
public interface CoreFactory extends Factory {
    
    /**
     * The name of the stereotype for a Realization.
     */
    public String REALIZE_STEREOTYPE = "realize"; //$NON-NLS-1$
    
    /**
     * Create an empty but initialized instance of a UML Abstraction.
     * 
     * @return an initialized UML Abstraction instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    public Object createAbstraction();
    
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
     * Create an empty but initialized instance of an Artifact
     * 
     * @since UML 1.4
     * @return an initialized Artifact instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    public Object createArtifact();
    
    /**
     * Create an empty but initialized instance of a UML Association.
     * 
     * @return an initialized UML Association instance.
     * @deprecated for 0.26 by tfmorris. Use {@link #createAssociation(Object)}.
     */
    public Object createAssociation();
    
    /**
     * Create an empty but initialized instance of a UML Association.
     * 
     * @param extent outer package (extent) in which to create new Association.
     * @return an initialized UML Association instance.
     */
    public Object createAssociation(Object extent);
    
    /**
     * Create an empty but initialized instance of a UML AssociationClass.
     * 
     * @return an initialized UML AssociationClass instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    public Object createAssociationClass();
    
    /**
     * Create an empty but initialized instance of a UML AssociationEnd.
     *
     * @return an initialized UML AssociationEnd instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object createAssociationEnd();

    /**
     * Create an empty but initialized instance of a UML Attribute.
     *
     * @return an initialized UML Attribute instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object createAttribute();

    /**
     * Create an empty but initialized instance of a UML Binding.
     *
     * @return an initialized UML Binding instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object createBinding();

    /**
     * Create an empty but initialized instance of a UML Class.
     *
     * @return an initialized UML Class instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object createClass();

    /**
     * Create an empty but initialized instance of a UML Comment.
     *
     * @return an initialized UML Comment instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object createComment();

    /**
     * Create an empty but initialized instance of a UML Component.
     *
     * @return an initialized UML Component instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object createComponent();

    /**
     * Create an empty but initialized instance of a UML Constraint.
     *
     * @return an initialized UML Constraint instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object createConstraint();

    /**
     * Create an empty but initialized instance of a UML DataType.
     *
     * @return an initialized UML DataType instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object createDataType();
    
    /**
    * Create an empty but initialized instance of a UML Dependency.
    *
    * @return an initialized UML Dependency instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
    */
    public Object createDependency();

    /**
     * Create an empty but initialized instance of a UML ElementResidence.
     *
     * @return an initialized UML ElementResidence instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object createElementResidence();
    
    /**
     * @param me the modelelement
     * @param component the component
     * @return an initialized UML ElementResidence instance.
     */
    public Object buildElementResidence(Object me, Object component);
    
    /**
     * Create an Enumeration.
     * @return Object new Enumeration
     * @since UML 1.4
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object createEnumeration();

    /**
     * Create an EnumerationLiteral.
     * @return Object new Enumeration
     * @since UML 1.4
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object createEnumerationLiteral();

    /**
     * Create an empty but initialized instance of a UML Flow.
     *
     * @return an initialized UML Flow instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object createFlow();

    /**
     * Create an empty but initialized instance of a UML Interface.
     *
     * @return an initialized UML Interface instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object createInterface();

    /**
     * Create an empty but initialized instance of a UML Method.
     *
     * @return an initialized UML Method instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object createMethod();

    /**
     * Create an empty but initialized instance of a UML Node.
     *
     * @return an initialized UML Node instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object createNode();

    /**
     * Create an empty but initialized instance of a UML Operation.
     *
     * @return an initialized UML Operation instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object createOperation();

    /**
     * Create an empty but initialized instance of a UML Parameter.
     *
     * @return an initialized UML Parameter instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object createParameter();

    /**
     * Create an empty but initialized instance of a UML Permission.
     *
     * @return an initialized UML Permission instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     * @deprecated for 0.27.3 by tfmorris.  Renamed in UML 2.x to PackageImport.
     * Use {@link #createPackageImport()}.
     */
    @Deprecated
    Object createPermission();
    
    /**
     * Create an empty but initialized instance of a UML Permission.
     *
     * @return an initialized UML Permission instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object createPackageImport();
    
    /**
     * Create a UML PrimitiveType.
     * 
     * @since UML 2.1.1
     * @return an initialized UML Primitive instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    public Object createPrimitiveType();
    

    /**
     * Create a UML TemplateArgument.
     * 
     * @since UML 1.4
     * @return an initialized TemplateArgument instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    public Object createTemplateArgument();

    /**
     * Create an empty but initialized instance of a UML TemplateParameter.
     *
     * @return an initialized UML TemplateParameter instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object createTemplateParameter();

    /**
     * Create an empty but initialized instance of a UML Usage.
     *
     * @return an initialized UML Usage instance.
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object createUsage();

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
     * @deprecated for 0.27.3 by tfmorris.  Use 
     * {@link #buildAssociation(Object, Object, Object, Object, boolean)}.
     */
    @Deprecated
    Object buildAssociation(Object fromClassifier, Object aggregationKind1,
            Object toClassifier, Object aggregationKind2,
            Boolean unidirectional);


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
            boolean unidirectional);

    /**
     * Builds a binary associations between two classifiers with
     * default values for the association ends and the association
     * itself.<p>
     *
     * @param classifier1 The first classifier to connect
     * @param classifier2 The second classifier to connect
     * @return Association
     */
    Object buildAssociation(Object classifier1, Object classifier2);

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
     * @return AssociationClass
     */
    Object buildAssociationClass(Object end1, Object end2);

    /**
     * Builds a fully configurable association end. All variables for
     * an associationend can be given as parameter.
     * @param assoc The associaton this end will be part of
     * @param name The name of the association end
     * @param type The type (classifier) the end will connect. The end
     * is a connection piece between an association and a classifier
     * @param multi The multiplicity
     * @param stereo The stereotype
     * @param navigable The navigability. True if this association end
     *                  can be 'passed' from the other classifier.
     * @param order Ordering of the association
     * @param aggregation the aggregationkind
     * @param scope the scope kind
     * @param changeable the changeablekind
     * @param visibility the visibilitykind
     * @return AssociationEnd
     */
    @Deprecated
    Object buildAssociationEnd(
        Object assoc,
        String name,
        Object type,
        Object multi,
        Object stereo,
        boolean navigable,
        Object order,
        Object aggregation,
        Object scope,
        Object changeable,
        Object visibility);

    /**
     * Builds a fully configurable association end. All variables for
     * an associationend can be given as parameter.
     * @param assoc The associaton this end will be part of
     * @param name The name of the association end
     * @param type The type (classifier) the end will connect. The end
     * is a connection piece between an association and a classifier
     * @param multiplicity array of two integers containing lower and upper bound
     * @param stereo The stereotype
     * @param navigable The navigability. True if this association end
     *                  can be 'passed' from the other classifier.
     * @param order Ordering of the association
     * @param aggregation the aggregationkind
     * @param scope the scope kind
     * @param changeable the changeablekind
     * @param visibility the visibilitykind
     * @return AssociationEnd
     */
    Object buildAssociationEnd(
        Object assoc,
        String name,
        Object type,
        Integer[] multiplicity,
        Object stereo,
        boolean navigable,
        Object order,
        Object aggregation,
        Object scope,
        Object changeable,
        Object visibility);
    
    /**
     * Builds a simply configured association end.
     *
     * @param type the given classifier
     * @param assoc the given association
     * @return the newly build associationend
     */
    Object buildAssociationEnd(Object type, Object assoc);

    /**
     * Builds a default attribute.
     *
     * @param model The model the attribute belongs to.
     * @param type The type of the attribute.
     * @return The newly built attribute.
     */
    Object buildAttribute(Object model, Object type);

    /**
     * Builds a default attribute.
     *
     * @param type The type of the attribute.
     * @return The newly built attribute.
     */
    Object buildAttribute2(Object type);
    

    /**
     * Builds an attribute of the given type owned by a classifier.
     *
     * @param classifier the classifier which will own the new attribute
     * @param type the type
     * @return the newly built attribute
     */
    Object buildAttribute2(Object classifier, Object type);
    
    /**
     * Builds a default implementation for a class. The class is not owned by
     * any model element by default. Users should not forget to add ownership.
     *
     * @return newly created Class
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object buildClass();

    /**
     * Builds a class with a given namespace.
     *
     * @param owner the namespace
     * @return Class
     * @see #buildClass()
     */
    Object buildClass(Object owner);

    /**
     * Builds a class with a given name.
     *
     * @param name the given name
     * @return Class
     * @see #buildClass()
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object buildClass(String name);

    /**
     * Builds a class with a given name and namespace.
     *
     * @param name the given name
     * @param owner the namespace
     * @return Class
     * @see #buildClass()
     */
    Object buildClass(String name, Object owner);

    /**
     * Builds a default implementation for an interface. The interface
     * is not owned by any model element by default. Users should not
     * forget to add ownership.
     *
     * @return Interface
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object buildInterface();

    /**
     * Builds an interface with a given namespace.
     *
     * @param owner is the owner
     * @return Interface
     */
    Object buildInterface(Object owner);

    /**
     * Builds an interface with a given name.
     *
     * @param name is the given name.
     * @return Interface
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object buildInterface(String name);

    /**
     * Builds an interface with a given name and namespace.
     *
     * @param name is the given name
     * @param owner is the namespace
     * @return Interface
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
     * @param name is the name
     * @param owner is the namespace
     * @return an initialized UML Enumeration instance
     * @since UML 1.4
     */
    Object buildEnumeration(String name, Object owner);
    
    /**
     * @param name is the name
     * @param enumeration is the enumeration of the literal
     * @return an initialized UML EnumerationLiteral instance
     * @since UML 1.4
     */
    Object buildEnumerationLiteral(String name, Object enumeration);

    /**
     * Builds a modelelement dependency between two modelelements.<p>
     *
     * @param clientObj is the client
     * @param supplierObj is the supplier
     * @return Dependency
     */
    Object buildDependency(Object clientObj, Object supplierObj);


    /**
     * Build an import Permission between a Namespace and a Package. All model
     * elements in the supplier namespace will be added to the client namespace.
     * The Permission will be placed in the client namespace.
     * <p>
     * UML 1.4 spec - "Import is a stereotyped permission dependency between two
     * namespaces, denoting that the public contents of the target package are
     * added to the namespace of the source package."
     * <p>
     * NOTE: For compatibility with UML 2.x, we adopt the slightly stricter
     * requirement that the target namespace must be a Package.
     * 
     * @param client
     *                is the client Namespace
     * @param supplier
     *                is the supplier Package
     * @return Permission
     */
    Object buildPackageImport(Object client, Object supplier);


    /**
     * Build an access Permission between a Namespace and a Package. All model
     * elements in the supplier namespace will be accessible from the client
     * namespace, but they are not added to the client namespace. The Permission
     * will be placed in the client namespace.
     * <p>
     * UML 1.4 spec - "Access is a stereotyped permission dependency between two
     * namespaces, denoting that the public contents of the target namespace are
     * accessible to the namespace of the source package."
     * <p>
     * NOTE: For compatibility with UML 2.x, we adopt the slightly stricter
     * requirement that the target namespace must be a Package. In UML 2.x, this
     * translates to a packageImport with non-public visibility.
     * 
     * @param client
     *                is the client Namespace
     * @param supplier
     *                is the supplier Package
     * @return Permission
     */
    Object buildPackageAccess(Object client, Object supplier);

    
    /**
     * Builds a generalization between a parent and a child. Does not check if
     * multiple inheritance is allowed for the current notation.
     *
     * @param child is the child
     * @param parent is the parent
     * @return Generalization
     */
    Object buildGeneralization(Object child, Object parent);


    /**
     * Builds a manifestation of an element for an artifact.
     *
     * @param utilizedElement is the packageable element
     * @return manifestation
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object buildManifestation(Object utilizedElement);


    /**
     * Builds a method with a given name.
     *
     * @param name is the given name
     * @return method
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object buildMethod(String name);


    /**
     * Builds an operation for a classifier.
     *
     * @param classifier is the given classifier
     * @param returnType the type of the return parameter
     * @return the operation
     */
    Object buildOperation(Object classifier, Object returnType);


    /**
     * Builds an operation with a given name for classifier.
     * <p>
     * NOTE: This is given a new method name so there won't be a name clash for
     * the case where the old 3-param method was called with null arguments.
     * <p>
     * 
     * @param cls
     *            is the classifier that shall own the operation
     * @param returnType
     *            the type of the return parameter
     * @param name
     *            the given name for the operation
     * @return the operation
     */
    Object buildOperation2(Object cls, Object returnType, String name);


    /**
     * Adds a parameter initialized to default values to a given event
     * or behavioral feature or objectFlowState.
     *
     * @param o an event or behavioral feature or objectFlowState
     * @param type the type of the parameter or null
     * @return the parameter
     */
    Object buildParameter(Object o, Object type);
    
    /**
     * Builds a realization between some supplier (for example an
     * interface in Java) and a client who implements the realization.
     *
     * @param client is the client
     * @param supplier is the supplier
     * @param namespace the namespace to use if client and
     * supplier are of different namespace
     * @return Object the created abstraction
     */
    Object buildRealization(Object client, Object supplier, Object namespace);

    /**
     * Build a TemplateArgument which has the given element as its modelElement.
     * <p>
     * <em>NOTE:</em> Although it's not shown in the UML 1.4 spec, a
     * TemplateArgument is a value (like MultiplicityRange), not a ModelElement
     * and can not be reused in multiple instances. The UML diagrams don't show
     * it as a composition, but it effectively is.
     * 
     * @param element
     *            ModelElement to be used as argument
     * @return newly created TemplateArgument
     */
    Object buildTemplateArgument(Object element);
    
    /**
     * Build a TemplateArgument which has the given element as its modelElement.
     * <p>
     * <em>NOTE:</em> Although it's not shown in the UML 1.4 spec, a
     * TemplateArgument is a value (like MultiplicityRange), not a ModelElement
     * and can not be reused in multiple instances. The UML diagrams don't show
     * it as a composition, but it effectively is.
     * 
     * @param binding Binding which will own this Template Argument
     * @param argument
     *            ModelElement to be used as argument
     * @return newly created TemplateArgument
     */
    Object buildTemplateArgument(Object binding, Object argument);
    
    /**
     * Build a TemplateParameter which has the given template and parameter and
     * 
     * @param template ModelElement which will be template
     * @param parameter ModelElement which will be parameter. The only two two
     *            things of significance are its type and name
     * @param defaultElement an optional default Model Element which will be
     *            used if not value is given in a Binding. Null if not used.
     * 
     * @param element ModelElement to be used as argument
     * @return newly created TemplateParameter
     */
    Object buildTemplateParameter(Object template, Object parameter, 
            Object defaultElement);
    
    /**
     * Builds a usage between some client and a supplier. If client
     * and supplier do not have the same model, an
     * {@link IllegalArgumentException} is thrown.
     *
     * @param client is the client
     * @param supplier is the supplier
     * @return Usage
     */
    Object buildUsage(Object client, Object supplier);

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
     * Builds a constraint that constraints the given modelelement.
     * The namespace of the constraint will be the same as the
     * namespace of the given modelelement.<p>
     *
     * @param constrElement The constrained element.
     * @return Constraint
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
     * Build binding between a supplier template element and its parameterized
     * client using the given list or arguments to fill the template.
     * 
     * @param client
     *            ModelElement to become client. A given ModelElement may only
     *            participate as a client in a single Binding.
     * @param supplier
     *            template ModelElement to be parameterized
     * @param arguments
     *            list of argument used to fill the parameters. Number, order,
     *            and type must match those of the TemplateParameters for the
     *            supplier. A null may be passed to not create arguments at
     *            build time.
     * @return the newly created Binding
     */
    Object buildBinding(Object client, Object supplier, List arguments);

    /**
     * Copies a class, and it's features. This may also require other
     * classifiers to be copied.
     *
     * @param source is the class to copy.
     * @param ns is the namespace to put the copy in.
     * @return a newly created class.
     */
    Object copyClass(Object source, Object ns);

    /**
     * Copies a feature from one classifier to another.
     *
     * @param source is the feature to copy.
     * @param classifier is the classifier to put the copy in.
     * @return a newly created feature.
     */
    Object copyFeature(Object source, Object classifier);

    /**
     * Copies a datatype, and it's features. This may also require other
     * classifiers to be copied.
     *
     * @param source is the datatype to copy.
     * @param ns is the namespace to put the copy in.
     * @return a newly created data type.
     */
    Object copyDataType(Object source, Object ns);

    /**
     * Copies an interface, and it's features. This may also require other
     * classifiers to be copied.
     *
     * @param source is the interface to copy.
     * @param ns is the namespace to put the copy in.
     * @return a newly created interface.
     */
    Object copyInterface(Object source, Object ns);

    /**
     * Additional support for generalization.
     *
     * @return A generalization.
     * @deprecated for 0.26 by tfmorris.  Use 
     * {@link #createGeneralization(Object)}.
     */
    Object createGeneralization();

    /**
     * Create a generalization in the given extent.
     * 
     * @param extent the extent in which the generalization should be created
     * @return A generalization.
     */
    Object createGeneralization(Object extent);
}
