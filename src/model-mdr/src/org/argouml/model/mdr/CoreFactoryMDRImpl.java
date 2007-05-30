// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.model.mdr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.argouml.model.CoreFactory;
import org.argouml.model.ModelManagementHelper;
import org.omg.uml.behavioralelements.commonbehavior.Reception;
import org.omg.uml.behavioralelements.commonbehavior.Signal;
import org.omg.uml.behavioralelements.statemachines.Event;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.Artifact;
import org.omg.uml.foundation.core.AssociationClass;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.BehavioralFeature;
import org.omg.uml.foundation.core.Binding;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Comment;
import org.omg.uml.foundation.core.Component;
import org.omg.uml.foundation.core.Constraint;
import org.omg.uml.foundation.core.CorePackage;
import org.omg.uml.foundation.core.DataType;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.Element;
import org.omg.uml.foundation.core.ElementResidence;
import org.omg.uml.foundation.core.Enumeration;
import org.omg.uml.foundation.core.EnumerationLiteral;
import org.omg.uml.foundation.core.Feature;
import org.omg.uml.foundation.core.Flow;
import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.Interface;
import org.omg.uml.foundation.core.Method;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.Node;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.core.Permission;
import org.omg.uml.foundation.core.PresentationElement;
import org.omg.uml.foundation.core.Primitive;
import org.omg.uml.foundation.core.ProgrammingLanguageDataType;
import org.omg.uml.foundation.core.Relationship;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.StructuralFeature;
import org.omg.uml.foundation.core.TemplateArgument;
import org.omg.uml.foundation.core.TemplateParameter;
import org.omg.uml.foundation.core.UmlAssociation;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.core.Usage;
import org.omg.uml.foundation.datatypes.AggregationKind;
import org.omg.uml.foundation.datatypes.AggregationKindEnum;
import org.omg.uml.foundation.datatypes.BooleanExpression;
import org.omg.uml.foundation.datatypes.CallConcurrencyKindEnum;
import org.omg.uml.foundation.datatypes.ChangeableKind;
import org.omg.uml.foundation.datatypes.ChangeableKindEnum;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.OrderingKind;
import org.omg.uml.foundation.datatypes.OrderingKindEnum;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;
import org.omg.uml.foundation.datatypes.ProcedureExpression;
import org.omg.uml.foundation.datatypes.ScopeKind;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;
import org.omg.uml.foundation.datatypes.VisibilityKind;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;
import org.omg.uml.modelmanagement.Model;

/**
 * Factory to create UML classes for the UML Foundation::Core package.
 * <p>
 * Feature, StructuralFeature, and PresentationElement do not have a create
 * method since they are called an "abstract metaclass" in the UML 
 * specifications.
 * <p>
 * @since ARGO0.19.5
 * @author Ludovic Ma&icirc;tre
 * @author Tom Morris
 * <p>
 * Derived from NSUML implementation by: 
 * @author Thierry Lach
 * @author Jaap Branderhorst
 */
class CoreFactoryMDRImpl extends AbstractUmlModelFactoryMDR implements
        CoreFactory {

    /**
     * The model implementation.
     */
    private MDRModelImplementation nsmodel;

    /**
     * The MDR core package for model element construction
     */
    private CorePackage corePackage;

    /**
     * Don't allow instantiation.
     * 
     * @param implementation
     *            To get other helpers and factories.
     */
    CoreFactoryMDRImpl(MDRModelImplementation implementation) {
        nsmodel = implementation;
        corePackage = nsmodel.getUmlPackage().getCore();
    }

    /*
     * @see org.argouml.model.CoreFactory#createAbstraction()
     */
    public Object createAbstraction() {
        Abstraction myAbstraction = corePackage.getAbstraction()
                .createAbstraction();
        super.initialize(myAbstraction);
        return myAbstraction;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildAbstraction(java.lang.String,
     *      java.lang.Object, java.lang.Object)
     */
    public Object buildAbstraction(String name, Object supplier, 
            Object client) {
        if (!(client instanceof Classifier)
                || !(supplier instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "The supplier and client of an abstraction"
                            + "should be classifiers");
        }
        Abstraction abstraction = (Abstraction) createAbstraction();
        abstraction.setName(name);
        abstraction.getClient().add(client);
        abstraction.getSupplier().add(supplier);
        return abstraction;
    }

    /*
     * @see org.argouml.model.CoreFactory#createArtifact()
     */
    public Object createArtifact() {
        Artifact artifact = corePackage.getArtifact().createArtifact();
        super.initialize(artifact);
        return artifact;
    }
    

    /*
     * @see org.argouml.model.CoreFactory#createAssociation()
     */
    public Object createAssociation() {
        UmlAssociation assoc = corePackage.getUmlAssociation()
                .createUmlAssociation();
        super.initialize(assoc);
        return assoc;
    }

    /*
     * @return an initialized UML AssociationClass instance.
     */
    public Object createAssociationClass() {
        AssociationClass assoc = corePackage.getAssociationClass()
                .createAssociationClass();
        super.initialize(assoc);
        return assoc;
    }

    /*
     * @see org.argouml.model.CoreFactory#createAssociationEnd()
     */
    public Object createAssociationEnd() {
        AssociationEnd assocEnd = corePackage.getAssociationEnd()
                .createAssociationEnd();
        super.initialize(assocEnd);
        return assocEnd;
    }

    /*
     * @see org.argouml.model.CoreFactory#createAttribute()
     */
    public Object createAttribute() {
        Attribute myAttribute = corePackage.getAttribute().createAttribute();
        super.initialize(myAttribute);
        return myAttribute;
    }

    /*
     * @see org.argouml.model.CoreFactory#createBinding()
     */
    public Object createBinding() {
        Binding myBinding = corePackage.getBinding().createBinding();
        super.initialize(myBinding);
        return myBinding;
    }

    /*
     * @see org.argouml.model.CoreFactory#createClass()
     */
    public Object createClass() {
        UmlClass myClass = corePackage.getUmlClass().createUmlClass();
        super.initialize(myClass);
        return myClass;
    }

    /*
     * @see org.argouml.model.CoreFactory#createComment()
     */
    public Object createComment() {
        Comment myComment = corePackage.getComment().createComment();
        super.initialize(myComment);
        return myComment;
    }

    /*
     * @see org.argouml.model.CoreFactory#createComponent()
     */
    public Object createComponent() {
        Component myComponent = corePackage.getComponent().createComponent();
        super.initialize(myComponent);
        return myComponent;
    }

    /*
     * @see org.argouml.model.CoreFactory#createConstraint()
     */
    public Object createConstraint() {
        Constraint myConstraint = corePackage.getConstraint()
                .createConstraint();
        super.initialize(myConstraint);
        return myConstraint;
    }

    /*
     * @see org.argouml.model.CoreFactory#createDataType()
     */
    public Object createDataType() {
        DataType dataType = corePackage.getDataType().createDataType();
        super.initialize(dataType);
        return dataType;
    }

    /* 
     * @see org.argouml.model.CoreFactory#createDependency()
     */
    public Object createDependency() {
        Dependency myDependency = corePackage.getDependency()
                .createDependency();
        super.initialize(myDependency);
        return myDependency;
    }

    /*
     * @see org.argouml.model.CoreFactory#createElementResidence()
     */
    public Object createElementResidence() {
        ElementResidence myElementResidence = corePackage.
                getElementResidence().createElementResidence();
        super.initialize(myElementResidence);
        return myElementResidence;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildElementResidence(java.lang.Object, java.lang.Object)
     */
    public Object buildElementResidence(Object me, Object component) {
        ElementResidence myElementResidence = corePackage.
        getElementResidence().createElementResidence();
        super.initialize(myElementResidence);
        myElementResidence.setContainer((Component) component);
        myElementResidence.setResident((ModelElement) me);
        return myElementResidence;
    }

    /*
     * @see org.argouml.model.CoreFactory#createEnumeration()
     */
    public Object createEnumeration() {
        Enumeration myEnumeration = corePackage.getEnumeration()
                .createEnumeration();
        super.initialize(myEnumeration);
        return myEnumeration;
    }
    

    /*
     * @see org.argouml.model.CoreFactory#createEnumerationLiteral()
     */
    public Object createEnumerationLiteral() {
        EnumerationLiteral myEnumerationLiteral = corePackage
                .getEnumerationLiteral().createEnumerationLiteral();
        super.initialize(myEnumerationLiteral);
        return myEnumerationLiteral;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildEnumerationLiteral(java.lang.String, java.lang.Object)
     */
    public Object buildEnumerationLiteral(String name, Object enumeration) {
        EnumerationLiteral el = 
            (EnumerationLiteral) createEnumerationLiteral();
        el.setName(name);
        el.setEnumeration((Enumeration) enumeration);
        return el;
    }
    /*
     * @see org.argouml.model.CoreFactory#createFlow()
     */
    public Object createFlow() {
        Flow myFlow = corePackage.getFlow().createFlow();
        super.initialize(myFlow);
        return myFlow;
    }

    /*
     * @see org.argouml.model.CoreFactory#createGeneralization()
     */
    public Object createGeneralization() {
        Generalization myGeneralization = corePackage.getGeneralization()
                .createGeneralization();
        super.initialize(myGeneralization);
        return myGeneralization;
    }

    /*
     * @see org.argouml.model.CoreFactory#createInterface()
     */
    public Object createInterface() {
        Interface myInterface = corePackage
                .getInterface().createInterface();
        super.initialize(myInterface);
        return myInterface;
    }

    /*
     * @see org.argouml.model.CoreFactory#createMethod()
     */
    public Object createMethod() {
        Method myMethod = corePackage.getMethod()
                .createMethod();
        super.initialize(myMethod);
        return myMethod;
    }

    /*
     * @see org.argouml.model.CoreFactory#createNode()
     */
    public Object createNode() {
        Node myNode = corePackage.getNode().createNode();
        super.initialize(myNode);
        return myNode;
    }

    /*
     * @see org.argouml.model.CoreFactory#createOperation()
     */
    public Object createOperation() {
        Operation myOperation = corePackage
                .getOperation().createOperation();
        super.initialize(myOperation);
        return myOperation;
    }

    /*
     * @see org.argouml.model.CoreFactory#createParameter()
     */
    public Object createParameter() {
        Parameter myParameter = corePackage
                .getParameter().createParameter();
        super.initialize(myParameter);
        return myParameter;
    }

    /*
     * @see org.argouml.model.CoreFactory#createPermission()
     */
    public Object createPermission() {
        Permission myPermission = corePackage
                .getPermission().createPermission();
        super.initialize(myPermission);
        return myPermission;
    }

    /*
     * @see org.argouml.model.CoreFactory#createPrimitive()
     */
    public Object createPrimitive() {
        Primitive obj = corePackage.getPrimitive().createPrimitive();
        super.initialize(obj);
        return obj;
    }

    /*
     * @see org.argouml.model.CoreFactory#createProgrammingLanguageDataType()
     */
    public Object createProgrammingLanguageDataType() {
        ProgrammingLanguageDataType obj = corePackage
                .getProgrammingLanguageDataType()
                .createProgrammingLanguageDataType();
        super.initialize(obj);
        return obj;
    }

    /*
     * @see org.argouml.model.CoreFactory#createTemplateArgument()
     */
    public Object createTemplateArgument() {
        TemplateArgument obj = corePackage.getTemplateArgument()
                .createTemplateArgument();
        super.initialize(obj);
        return obj;
    }
        
    /*
     * @see org.argouml.model.CoreFactory#createTemplateParameter()
     */
    public Object createTemplateParameter() {
        TemplateParameter myTemplateParameter = corePackage
                .getTemplateParameter().createTemplateParameter();
        super.initialize(myTemplateParameter);
        return myTemplateParameter;
    }


    /*
     * @see org.argouml.model.CoreFactory#createUsage()
     */
    public Object createUsage() {
        Usage myUsage = corePackage.getUsage().createUsage();
        super.initialize(myUsage);
        return myUsage;
    }

    /**
     * Builds a default binary association with two default association ends.
     * 
     * @param c1
     *            The first classifier to connect to
     * @param nav1
     *            The navigability of the Associaton end
     * @param agg1
     *            The aggregation type of the second Associaton end
     * @param c2
     *            The second classifier to connect to
     * @param nav2
     *            The navigability of the second Associaton end
     * @param agg2
     *            The aggregation type of the second Associaton end
     * @return MAssociation
     * @throws IllegalArgumentException
     *             if either Classifier is null
     */
    private UmlAssociation buildAssociation(Classifier c1, boolean nav1,
            AggregationKind agg1, Classifier c2, boolean nav2,
            AggregationKind agg2) {
        if (c1 == null || c2 == null) {
            throw new IllegalArgumentException("one of "
                    + "the classifiers to be " + "connected is null");
        }
        Namespace ns1 = c1.getNamespace();
        Namespace ns2 = c2.getNamespace();
        if (ns1 == null || ns2 == null) {
            throw new IllegalArgumentException("one of "
                    + "the classifiers does not " + "belong to a namespace");
        }
        UmlAssociation assoc = (UmlAssociation) createAssociation();
        assoc.setName("");
        assoc.setNamespace((Namespace) nsmodel.getCoreHelper().
                getFirstSharedNamespace(ns1, ns2));
        buildAssociationEnd(assoc, null, c1, null, null,
                nav1, null, agg1, null, null, null);
        buildAssociationEnd(assoc, null, c2, null, null, 
                nav2, null, agg2, null, null, null);
        return assoc;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildAssociation(java.lang.Object,
     *      java.lang.Object, java.lang.Object, java.lang.Object,
     *      java.lang.Boolean)
     */
    public Object buildAssociation(Object fromClassifier,
            Object aggregationKind1, Object toClassifier,
            Object aggregationKind2, Boolean unidirectional) {
        if (fromClassifier == null || toClassifier == null) {
            throw new IllegalArgumentException("one of "
                    + "the classifiers to be " + "connected is null");
        }
        Classifier from = (Classifier) fromClassifier;
        Classifier to = (Classifier) toClassifier;
        AggregationKind agg1 = (AggregationKind) aggregationKind1;
        AggregationKind agg2 = (AggregationKind) aggregationKind2;

        Namespace ns1 = from.getNamespace();
        Namespace ns2 = to.getNamespace();
        if (ns1 == null || ns2 == null) {
            throw new IllegalArgumentException("one of "
                    + "the classifiers does not " + "belong to a namespace");
        }
        UmlAssociation assoc = (UmlAssociation) createAssociation();
        assoc.setName("");
        assoc.setNamespace((Namespace) nsmodel.getCoreHelper().
                getFirstSharedNamespace(ns1, ns2));

        boolean nav1 = true;
        boolean nav2 = true;

        if (from instanceof Interface) {
            nav2 = false;
            agg2 = agg1;
            agg1 = null;
        } else if (to instanceof Interface) {
            nav1 = false;
        } else {
            nav1 = !Boolean.TRUE.equals(unidirectional);
            nav2 = true;
        }

        buildAssociationEnd(assoc, null, from, null, null, nav1, null, agg1,
                null, null, null);
        buildAssociationEnd(assoc, null, to, null, null, nav2, null, agg2,
                null, null, null);
        return assoc;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildAssociation(java.lang.Object,
     *      java.lang.Object)
     */
    public Object buildAssociation(Object classifier1, Object classifier2) {
        Classifier c1 = (Classifier) classifier1;
        Classifier c2 = (Classifier) classifier2;
        return buildAssociation(c1, true, AggregationKindEnum.AK_NONE, c2,
                true, AggregationKindEnum.AK_NONE);
    }

    /*
     * @see org.argouml.model.CoreFactory#buildAssociation(java.lang.Object,
     *      boolean, java.lang.Object, boolean, java.lang.String)
     */
    public Object buildAssociation(Object c1, boolean nav1, Object c2,
            boolean nav2, String name) {
        UmlAssociation assoc = buildAssociation((Classifier) c1, nav1,
                AggregationKindEnum.AK_NONE, (Classifier) c2, nav2,
                AggregationKindEnum.AK_NONE);
        if (assoc != null) {
            assoc.setName(name);
        }
        return assoc;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildAssociationClass(java.lang.Object,
     *      java.lang.Object)
     */
    public Object buildAssociationClass(Object end1, Object end2) {
        if (end1 == null || end2 == null || !(end1 instanceof Classifier)
                || !(end2 instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "either one of the arguments was null");
        }
        return buildAssociationClass(
                (UmlClass) buildClass(),
                (Classifier) end1,
                (Classifier) end2);
    }


    /*
     * @see org.argouml.model.CoreFactory#buildAssociationEnd(java.lang.Object,
     *      java.lang.String, java.lang.Object, java.lang.Object,
     *      java.lang.Object, boolean, java.lang.Object, java.lang.Object,
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object buildAssociationEnd(Object assoc, String name, Object type,
            Object multi, Object stereo, boolean navigable, Object order,
            Object aggregation, Object scope, Object changeable,
            Object visibility) {
        // wellformednessrules and preconditions
        if (assoc == null || !(assoc instanceof UmlAssociation) || type == null
                || !(type instanceof Classifier)) {
            throw new IllegalArgumentException("either type or association "
                    + "are null");
        }
        if (multi != null && !(multi instanceof Multiplicity)) {
            throw new IllegalArgumentException("Multiplicity");
        }
        if (stereo != null && !(stereo instanceof Stereotype)) {
            throw new IllegalArgumentException("Stereotype");
        }
        if (order != null && !(order instanceof OrderingKind)) {
            throw new IllegalArgumentException("OrderingKind");
        }
        if (aggregation != null && !(aggregation instanceof AggregationKind)) {
            throw new IllegalArgumentException("AggregationKind");
        }
        if (scope != null && !(scope instanceof ScopeKind)) {
            throw new IllegalArgumentException("ScopeKind");
        }
        if (changeable != null && !(changeable instanceof ChangeableKind)) {
            throw new IllegalArgumentException("ChangeableKind");
        }
        if (visibility != null && !(visibility instanceof VisibilityKind)) {
            throw new IllegalArgumentException("VisibilityKind");
        }

        if (type instanceof DataType || type instanceof Interface) {
            if (!navigable) {
                throw new IllegalArgumentException(
                        "Wellformedness rule 2.5.3.3 [1] is broken. "
                                + "The Classifier of an AssociationEnd cannot"
                                + "be an Interface or a DataType if the "
                                + "association is navigable away from "
                                + "that end.");
            }
            List ends = new ArrayList();
            ends.addAll(((UmlAssociation) assoc).getConnection());
            Iterator it = ends.iterator();
            while (it.hasNext()) {
                AssociationEnd end = (AssociationEnd) it.next();
                if (end.isNavigable()) {
                    throw new IllegalArgumentException("type is either "
                            + "datatype or " + "interface and is "
                            + "navigable to");
                }
            }
        }
        if (aggregation != null
                && aggregation.equals(AggregationKindEnum.AK_COMPOSITE)
                && multi != null && getMaxUpper((Multiplicity) multi) > 1) {
            throw new IllegalArgumentException("aggregation is composite "
                    + "and multiplicity > 1");
        }

        AssociationEnd end = (AssociationEnd) createAssociationEnd();
        end.setAssociation((UmlAssociation) assoc);
        end.setParticipant((Classifier) type);
        end.setName(name);
        if (multi != null) {
            end.setMultiplicity((Multiplicity) multi);
        } else {
            end.setMultiplicity(getMultiplicity11());
        }
        if (stereo != null) {
            end.getStereotype().clear();
            end.getStereotype().add(stereo);
        }
        end.setNavigable(navigable);
        if (order != null) {
            end.setOrdering((OrderingKind) order);
        } else {
            end.setOrdering(OrderingKindEnum.OK_UNORDERED);
        }
        if (aggregation != null) {
            end.setAggregation((AggregationKind) aggregation);
        } else {
            end.setAggregation(AggregationKindEnum.AK_NONE);
        }
        if (scope != null) {
            end.setTargetScope((ScopeKind) scope);
        } else {
            end.setTargetScope(ScopeKindEnum.SK_INSTANCE);
        }
        if (changeable != null) {
            end.setChangeability((ChangeableKind) changeable);
        } else {
            end.setChangeability(ChangeableKindEnum.CK_CHANGEABLE);
        }
        if (visibility != null) {
            end.setVisibility((VisibilityKind) visibility);
        } else {
            end.setVisibility(VisibilityKindEnum.VK_PUBLIC);
        }
        return end;
    }

    /**
     * Get the maximum value of a multiplicity
     * 
     * @param m
     *            the Multiplicity
     * @return upper range
     */
    private int getMaxUpper(Multiplicity m) {
        int max = 0;
        for (Iterator i = m.getRange().iterator(); i.hasNext();) {
            int value = ((MultiplicityRange) i.next()).getUpper();
            if (value > max) {
                max = value;
            }
        }
        return 0;
    }

    /**
     * Get a 1..1 multiplicity
     */
    private Multiplicity getMultiplicity11() {
        return (Multiplicity) nsmodel.getDataTypesFactory().createMultiplicity(
                1, 1);
    }

    /*
     * @see org.argouml.model.CoreFactory#buildAssociationEnd(java.lang.Object,
     *      java.lang.Object)
     */
    public Object buildAssociationEnd(Object type, Object assoc) {
        if (type == null || !(type instanceof Classifier) || assoc == null
                || !(assoc instanceof UmlAssociation)) {
            throw new IllegalArgumentException("one of the arguments is null");
        }
        return buildAssociationEnd(assoc, "", type, null, null, true, null,
                null, null, null, VisibilityKindEnum.VK_PUBLIC);
    }

    /**
     * Builds an association class from a class and two classifiers that should
     * be associated. Both ends of the associationclass are navigable.
     * <p>
     * 
     * @param cl
     *            the class
     * @param end1
     *            the first classifier
     * @param end2
     *            the second classifier
     * @return MAssociationClass
     */
    private AssociationClass buildAssociationClass(UmlClass cl,
            Classifier end1, Classifier end2) {
        if (end1 == null || end2 == null || cl == null) {
            throw new IllegalArgumentException(
                    "one of the arguments was null");
        }
        AssociationClass assoc = (AssociationClass) createAssociationClass();
        
        // Copy attributes from our template class
        assoc.setNamespace(cl.getNamespace());
        assoc.setName(cl.getName());
        assoc.setAbstract(cl.isAbstract());
        assoc.setActive(cl.isActive());
        assoc.setLeaf(cl.isLeaf());
        assoc.setRoot(cl.isRoot());
        assoc.setSpecification(cl.isSpecification());
        assoc.getStereotype().addAll(cl.getStereotype());
        assoc.setVisibility(cl.getVisibility());
        
        /*
         * Normally we will be called with a newly created default class as our
         * template so only the above attribute copying is needed. The rest of
         * this is just in case someone wants it to be more general in the
         * future.
         * TODO: This is untested and just copies what was done in the NSUML
         * implementation (which is also untested for the same reason as above).
         */
        assoc.getClientDependency().addAll(cl.getClientDependency());
        assoc.getComment().addAll(cl.getComment());
        assoc.getConstraint().addAll(cl.getConstraint());        
        assoc.getFeature().addAll(cl.getFeature());
        assoc.getGeneralization().addAll(cl.getGeneralization());
        assoc.getPowertypeRange().addAll(cl.getPowertypeRange());
        assoc.getSourceFlow().addAll(cl.getSourceFlow());
        assoc.getTaggedValue().addAll(cl.getTaggedValue());
        assoc.getTargetFlow().addAll(cl.getTargetFlow());
        assoc.getTemplateParameter().addAll(cl.getTemplateParameter());

        // Other things copied in the NSUML implementation 
        // which have no direct analog here
        
        //assoc.setAssociationEnds(facade.getAssociationEnds(cl));
        //assoc.setClassifierRoles(cl.getClassifierRole());
        //assoc.setClassifierRoles1(cl.getClassifierRoles1());
        //assoc.setClassifiersInState(cl.getClassifiersInState());
        //assoc.setCollaborations(cl.getCollaborations());
        //assoc.setCollaborations1(cl.getCollaborations1());
        //assoc.setCreateActions(cl.getCreateActions());
        //assoc.setExtensions(cl.getExtensions());
        //assoc.setInstances(cl.getInstances());
        //assoc.setObjectFlowStates(cl.getObjectFlowStates());
        //assoc.setParticipants(cl.getParticipants());
        //assoc.setPartitions1(cl.getPartitions1());
        //assoc.setPresentations(cl.getPresentations());
        //assoc.setStructuralFeatures(cl.getStructuralFeatures());

        buildAssociationEnd(assoc, null, end1, null, null, true, null, null,
                null, null, null);
        buildAssociationEnd(assoc, null, end2, null, null, true, null, null,
                null, null, null);
        return assoc;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildAttribute(java.lang.Object,
     *      java.lang.Object)
     */
    public Object buildAttribute(Object model, Object theType) {
        Classifier clsType = (Classifier) theType;
        // Force type element into given namespace if not already there
        // side effect!
        if (model != clsType.getNamespace()
                && !(nsmodel.getModelManagementHelper().getAllNamespaces(model).
                        contains(clsType.getNamespace()))) {
            clsType.setNamespace((Model) model);
        }
        return buildAttribute2(theType);
    }
    
    /*
     * @see org.argouml.model.CoreFactory#buildAttribute(java.lang.Object,
     *      java.lang.Object)
     */
    public Object buildAttribute2(Object theType) {
        Attribute attr = buildAttribute();
        attr.setType((Classifier) theType);
        return attr;
    }
    
    /**
     * Build a new attribute with no type
     * @return the new attribute
     */
    Attribute buildAttribute() {
        Attribute attr = (Attribute) createAttribute();
        attr.setName("newAttr");
        attr.setMultiplicity(getMultiplicity11());
        attr.setVisibility(VisibilityKindEnum.VK_PUBLIC);
        attr.setOwnerScope(ScopeKindEnum.SK_INSTANCE);
        attr.setChangeability(ChangeableKindEnum.CK_CHANGEABLE);
        attr.setTargetScope(ScopeKindEnum.SK_INSTANCE);
        return attr;
    }
    

    @SuppressWarnings("deprecation")
    public Object buildAttribute(Object handle, Object model, Object type) {
        Attribute attr = (Attribute) buildAttribute(model, type);
        if (handle instanceof Classifier) {
            Classifier cls = (Classifier) handle;
            cls.getFeature().add(attr);
        } else if (handle instanceof AssociationEnd) {
            AssociationEnd assend = (AssociationEnd) handle;
            assend.getQualifier().add(attr);
        } else {
            throw new IllegalArgumentException();            
        }
        return attr;
    }
    

    public Object buildAttribute2(Object handle, Object type) {
        Attribute attr = (Attribute) buildAttribute2(type);
        if (handle instanceof Classifier) {
            Classifier cls = (Classifier) handle;
            cls.getFeature().add(attr);
        } else if (handle instanceof AssociationEnd) {
            AssociationEnd assend = (AssociationEnd) handle;
            assend.getQualifier().add(attr);
        } else {
            throw new IllegalArgumentException();            
        }
        return attr;
    }
    
    /*
     * @see org.argouml.model.CoreFactory#buildClass()
     */
    public Object buildClass() {
        UmlClass cl = (UmlClass) createClass();
        cl.setName("");
        cl.setAbstract(false);
        cl.setActive(false);
        cl.setRoot(false);
        cl.setLeaf(false);
        cl.setSpecification(false);
        cl.setVisibility(VisibilityKindEnum.VK_PUBLIC);
        return cl;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildClass(java.lang.Object)
     */
    public Object buildClass(Object owner) {
        Object clazz = buildClass();
        if (owner instanceof Namespace) {
            nsmodel.getCoreHelper().setNamespace(clazz, owner);
        }
        return clazz;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildClass(java.lang.String)
     */
    public Object buildClass(String name) {
        Object clazz = buildClass();
        nsmodel.getCoreHelper().setName(clazz, name);
        return clazz;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildClass(java.lang.String,
     *      java.lang.Object)
     */
    public Object buildClass(String name, Object owner) {
        Object clazz = buildClass();
        nsmodel.getCoreHelper().setName(clazz, name);
        if (owner instanceof Namespace) {
            nsmodel.getCoreHelper().setNamespace(clazz, /* MNamespace */owner);
        }
        return clazz;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildInterface()
     */
    public Object buildInterface() {
        Interface cl = (Interface) createInterface();
        cl.setName("");
        cl.setAbstract(false);
        cl.setRoot(false);
        cl.setLeaf(false);
        cl.setSpecification(false);
        cl.setVisibility(VisibilityKindEnum.VK_PUBLIC);
        return cl;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildInterface(java.lang.Object)
     */
    public Object buildInterface(Object owner) {
        Interface cl = (Interface) buildInterface();
        if (owner instanceof Namespace) {
            cl.setNamespace((Namespace) owner);
        }
        return cl;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildInterface(java.lang.String)
     */
    public Object buildInterface(String name) {
        Interface cl = (Interface) buildInterface();
        cl.setName(name);
        return cl;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildInterface(java.lang.String,
     *      java.lang.Object)
     */
    public Object buildInterface(String name, Object owner) {
        Interface cl = (Interface) buildInterface();
        cl.setName(name);
        if (owner instanceof Namespace) {
            cl.setNamespace((Namespace) owner);
        }
        return cl;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildDataType(java.lang.String,
     *      java.lang.Object)
     */
    public Object buildDataType(String name, Object owner) {
        DataType dt = (DataType) createDataType();
        dt.setName(name);
        if (owner instanceof Namespace) {
            dt.setNamespace((Namespace) owner);
        }
        return dt;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildEnumeration(java.lang.String, java.lang.Object)
     */
    public Object buildEnumeration(String name, Object owner) {
        Enumeration e = (Enumeration) createEnumeration();
        e.setName(name);
        if (owner instanceof Namespace) {
            e.setNamespace((Namespace) owner);
        }
        return e;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildDependency(java.lang.Object,
     *      java.lang.Object)
     */
    public Object buildDependency(Object clientObj, Object supplierObj) {

        ModelElement client = (ModelElement) clientObj;
        ModelElement supplier = (ModelElement) supplierObj;
        if (client == null || supplier == null) {
            throw new IllegalArgumentException("client or supplier is null "
                    + "client = " + client + " supplier = " + supplier);
        }
        Dependency dep = (Dependency) createDependency();
        dep.getSupplier().add(supplier);
        dep.getClient().add(client);
        if (supplier.getNamespace() != null) {
            dep.setNamespace(supplier.getNamespace());
        } else if (client.getNamespace() != null) {
            dep.setNamespace(client.getNamespace());
        }
        return dep;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildPermission(java.lang.Object,
     *      java.lang.Object)
     */
    public Object buildPermission(Object clientObj, Object supplierObj) {

        ModelElement client = (ModelElement) clientObj;
        ModelElement supplier = (ModelElement) supplierObj;
        // TODO: Supplier must be a namespace per UML 1.4 spec - tfm
        if (client == null || supplier == null || client.getNamespace() == null
                || supplier.getNamespace() == null) {
            throw new IllegalArgumentException("client or supplier is null "
                    + "or their namespaces.");
        }
        Permission per = (Permission) createPermission();
        per.getSupplier().add(supplier);
        per.getClient().add(client);
        if (supplier.getNamespace() != null) {
            per.setNamespace(supplier.getNamespace());
        } else if (client.getNamespace() != null) {
            per.setNamespace(client.getNamespace());
        }
        // TODO: In addition to "import," a Permission can also take "friend"
        // and "access" stereotypes, each with its own semantics
        nsmodel.getExtensionMechanismsFactory().buildStereotype(per, 
                ModelManagementHelper.IMPORT_STEREOTYPE,
                per.getNamespace());
        return per;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildGeneralization(java.lang.Object,
     *      java.lang.Object, java.lang.String)
     */
    public Object buildGeneralization(Object child, Object parent, 
            String name) {
        if (child == null || parent == null
                || !(child instanceof GeneralizableElement)
                || !(parent instanceof GeneralizableElement)) {
            throw new IllegalArgumentException();
        }
        Object gen = buildGeneralization(child, parent);
        if (gen != null) {
            ((Generalization) gen).setName(name);
        }
        return gen;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildGeneralization(java.lang.Object,
     *      java.lang.Object)
     *      
     * The well-formedness rules should move to
     * UmlFactoryMDRImpl.isConnectionWellFormed and buildGeneralization should
     * become private.
     */
    public Object buildGeneralization(Object child1, Object parent1) {
        // TODO: This is a part implementation of well-formedness rule
        // UML1.4.2 - 4.5.3.20 [3] Circular inheritance is not allowed.
        // not self.allParents->includes(self)
        if ((!(child1 instanceof GeneralizableElement) 
                || !(parent1 instanceof GeneralizableElement))
                && child1 != parent1) {
            throw new IllegalArgumentException(
                    "Both items must be different generalizable elements");
        }

        GeneralizableElement child = (GeneralizableElement) child1;
        GeneralizableElement parent = (GeneralizableElement) parent1;

        // Check that the two elements aren't already linked the opposite way
        // TODO: This is a part implementation of well-formedness rule
        // UML1.4.2 - 4.5.3.20 [3] Circular inheritance is not allowed.
        // not self.allParents->includes(self)
        Iterator it = parent.getGeneralization().iterator();
        while (it.hasNext()) {
            Generalization gen = (Generalization) it.next();
            if (gen.getParent().equals(child)) {
                throw new IllegalArgumentException("Generalization exists" 
                        + " in opposite direction");
            }
        }

        // TODO: What well-formedness rule is this?
        if (parent.getNamespace() == null) {
            throw new IllegalArgumentException("parent has no namespace");
        }
        
        // TODO: This is well-formedness rule from UML1.4.2
        // 4.5.3.20 [2] No GeneralizableElement can have a parent
        // Generalization to an element that is a leaf.
        // self.parent->forAll(s | not s.isLeaf)
        if (parent.isLeaf()) {
            throw new IllegalArgumentException("parent is leaf");
        }
        
        // TODO: This is well-formedness rule from UML1.4.2
        // 4.5.3.20 [1] A root cannot have any Generalizations.
        // self.isRoot implies self.generalization->isEmpty        
        if (child.isRoot()) {
            throw new IllegalArgumentException("child is root");
        }

        Generalization gen = (Generalization) createGeneralization();
        gen.setParent(parent);
        gen.setChild(child);
        gen.setNamespace(parent.getNamespace());
        return gen;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildMethod(java.lang.String)
     */
    public Object buildMethod(String name) {
        Method method = (Method) createMethod();
        if (method != null) {
            method.setName(name);
        }
        return method;
    }


    @SuppressWarnings("deprecation")
    public Object buildOperation(Object classifier, Object model,
            Object returnType) {
        return buildOperation(classifier, returnType);
    }


    public Object buildOperation(Object classifier, Object returnType) {
        if (!(classifier instanceof Classifier)) {
            throw new IllegalArgumentException("Handle is not a classifier");
        }
        Classifier cls = (Classifier) classifier;
        Operation oper = (Operation) createOperation();
        oper.setName("newOperation");
        oper.setOwner(cls);
        oper.setVisibility(VisibilityKindEnum.VK_PUBLIC);
        oper.setAbstract(false);
        oper.setLeaf(false);
        oper.setRoot(false);
        oper.setQuery(false);
        oper.setOwnerScope(ScopeKindEnum.SK_INSTANCE);
        oper.setConcurrency(CallConcurrencyKindEnum.CCK_SEQUENTIAL);

        Parameter returnParameter = (Parameter) buildParameter(oper, 
                returnType);
        returnParameter.setKind(ParameterDirectionKindEnum.PDK_RETURN);
        returnParameter.setName("return");
        return oper;
    }


    @SuppressWarnings("deprecation")
    public Object buildOperation(Object cls, Object model, Object returnType,
            String name) {
        return buildOperation2(cls, returnType, name);
    }
    

    public Object buildOperation2(Object cls, Object returnType, String name) {
        Object oper = buildOperation(cls, returnType);
        if (oper != null) {
            ((Operation) oper).setName(name);
        }
        return oper;
    }

    /**
     * Constructs a default parameter.
     * 
     * @return The newly created parameter.
     */
    private Object buildParameter(Classifier type) {
        Parameter param = corePackage.getParameter().createParameter();
        param.setType(type);
        return param;
    }


    @SuppressWarnings("deprecation")
    public Object buildParameter(Object o, Object model, Object type) {
        return buildParameter(o, type);
    }
    

    public Object buildParameter(Object o, Object type) {
        if (o instanceof Event) {
            Event event = (Event) o;
            Parameter res = (Parameter) buildParameter((Classifier) type);
            res.setKind(ParameterDirectionKindEnum.PDK_IN);
            event.getParameter().add(res);
            return res;
        } else if (o instanceof BehavioralFeature) {
            BehavioralFeature oper = (BehavioralFeature) o;
            Parameter res = (Parameter) buildParameter((Classifier) type);
            oper.getParameter().add(res);
            res.setName("arg" + oper.getParameter().size());
            return res;
        } else {
            throw new IllegalArgumentException("Unsupported object type");
        }
    }


    public Object buildRealization(Object clnt, Object spplr, Object model) {
        ModelElement client = (ModelElement) clnt;
        ModelElement supplier = (ModelElement) spplr;
        if (client == null || supplier == null || client.getNamespace() == null
                || supplier.getNamespace() == null) {
            throw new IllegalArgumentException("faulty arguments.");
        }
        Abstraction realization = (Abstraction) createAbstraction();
        Namespace nsc = client.getNamespace();
        Namespace nss = supplier.getNamespace();
        // TODO: Shouldn't this use this computed nsc value below? - tfm
        realization.setNamespace(nsc);
        Namespace ns = null;
        if (nsc.equals(nss)) {
            ns = nsc;
        } else {
            ns = (Namespace) model;
        }
        nsmodel.getExtensionMechanismsFactory().buildStereotype(realization,
                CoreFactory.REALIZE_STEREOTYPE, ns);
        nsmodel.getCoreHelper().addClientDependency(client, realization);
        nsmodel.getCoreHelper().addSupplierDependency(supplier, realization);
        return realization;
    }


    public Object buildTemplateArgument(Object element) {
        TemplateArgument ta = (TemplateArgument) createTemplateArgument();
        ta.setModelElement((ModelElement) element);
        return ta;
    }
    

    public Object buildUsage(Object client, Object supplier) {
        if (client == null || supplier == null) {
            throw new IllegalArgumentException("In buildUsage null arguments.");
        }
        if (!(client instanceof ModelElement)) {
            throw new IllegalArgumentException("client ModelElement");
        }
        if (!(supplier instanceof ModelElement)) {
            throw new IllegalArgumentException("supplier ModelElement");
        }
        // TODO: UML 1.4 spec requires both client and supplier to be
        // in the same model - tfm
        Usage usage = (Usage) createUsage();
        usage.getSupplier().add(supplier);
        usage.getClient().add(client);
        if (((ModelElement) supplier).getNamespace() != null) {
            usage.setNamespace(((ModelElement) supplier).getNamespace());
        } else if (((ModelElement) client).getNamespace() != null) {
            usage.setNamespace(((ModelElement) client).getNamespace());
        }
        // TODO: Add standard stereotype?  Set is open ended, but 
        // predefined names include: call, create, instantiate, send
        return usage;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildComment(java.lang.Object,
     *      java.lang.Object)
     */
    public Object buildComment(Object element, Object model) {
        if (model == null) {
            throw new IllegalArgumentException("A namespace must be supplied.");
        }
        ModelElement elementToAnnotate = (ModelElement) element;
        Comment comment = (Comment) createComment();

        Namespace commentsModel = null;
        if (elementToAnnotate != null) {
            comment.getAnnotatedElement().add(elementToAnnotate);
            commentsModel = elementToAnnotate.getNamespace();
        } else {
            commentsModel = (Namespace) model;
        }

        comment.setNamespace(commentsModel);
        return comment;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildConstraint(java.lang.Object)
     */
    public Object buildConstraint(Object constrElement) {
        ModelElement constrainedElement = (ModelElement) constrElement;
        if (constrainedElement == null) {
            throw new IllegalArgumentException("the constrained element is "
                    + "mandatory and may not be " + "null.");
        }
        Constraint con = (Constraint) createConstraint();
        con.getConstrainedElement().add(constrainedElement);
        con.setNamespace(constrainedElement.getNamespace());
        return con;
    }

    /*
     * @see org.argouml.model.CoreFactory#buildConstraint(java.lang.String,
     *      java.lang.Object)
     */
    public Object buildConstraint(String name, Object bexpr) {
        if (bexpr == null || !(bexpr instanceof BooleanExpression)) {
            throw new IllegalArgumentException("invalid boolean expression.");
        }
        Constraint con = (Constraint) createConstraint();
        if (name != null) {
            con.setName(name);
        }
        con.setBody((BooleanExpression) bexpr);
        return con;
    }
    

    public Object buildBinding(Object client, Object supplier, List arguments) {
        Collection clientDeps = ((ModelElement) client).getClientDependency();
        for (Iterator it = clientDeps.iterator(); it.hasNext();) {
            Object dep = it.next();
            if (dep instanceof Binding) {
                throw new IllegalArgumentException(
                        "client is already client of another Binding");
            }
        }
        
        // Check arguments against parameters for type and number
        // TODO: Perhaps move this to a critic instead? - tfm - 20070326
        if (arguments != null) {
            Collection params = 
                ((ModelElement) supplier).getTemplateParameter();
            if (params.size() != arguments.size()) {
                throw new IllegalArgumentException(
                        "number of arguments doesn't match number of params");
            }
            Iterator ita = arguments.iterator();
            for (Iterator itp = params.iterator(); itp.hasNext();) {
                TemplateParameter param = (TemplateParameter) itp.next();
                TemplateArgument ta = (TemplateArgument) ita.next();
                // TODO: Before allowing this, we should really check that 
                // TemplateParameter.defaultElement is defined
                if (ta == null || ta.getModelElement() == null) {
                    continue;
                }
                if (!(param.getParameter().getClass().equals(
                        ta.getModelElement().getClass()))) {
                    throw new IllegalArgumentException(
                            "type of argument doesn't match type of parameter");
                }
            }
        }
        
        Binding binding = (Binding) createBinding();
        binding.getClient().add(client);
        binding.getSupplier().add(supplier);
        if (arguments != null) {
            binding.getArgument().addAll(arguments);
        }

        return binding;
    }


    /**
     * @param elem
     *            the abstraction to be deleted
     */
    void deleteAbstraction(Object elem) {
        if (!(elem instanceof Abstraction)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * @param elem
     *            the artifact to be deleted
     */
    void deleteArtifact(Object elem) {
        if (!(elem instanceof Artifact)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * @param elem
     *            the association to be deleted
     */
    void deleteAssociation(Object elem) {
        if (!(elem instanceof UmlAssociation)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * @param elem
     *            the a. to be deleted
     */
    void deleteAssociationClass(Object elem) {
        if (!(elem instanceof AssociationClass)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * Does a 'cascading delete' to all modelelements that are associated with
     * this element that would be in an illegal state after deletion of the
     * element. This method should not be called directly.
     * <p>
     * 
     * In the case of an AssociationEnd these are the following elements:
     * <ul>
     * <li>Binary Associations that lose one of the AssociationEnds by this
     * deletion.
     * <li>LinkEnds associated with this AssociationEnd.
     * </ul>
     * 
     * 
     * @param elem
     * @see UmlFactoryMDRImpl#delete(Object)
     */
    void deleteAssociationEnd(Object elem) {
        if (!(elem instanceof AssociationEnd)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
        AssociationEnd ae = (AssociationEnd) elem;
        UmlAssociation assoc = ae.getAssociation();
        if (assoc != null && assoc.getConnection() != null
                && assoc.getConnection().size() == 2) { // binary association
            nsmodel.getUmlFactory().delete(assoc);
        }
        // delete LinkEnds which have this as their associationEnd
        nsmodel.getUmlHelper().deleteCollection(
                nsmodel.getUmlPackage().getCommonBehavior()
                        .getAAssociationEndLinkEnd().getLinkEnd(ae));
    }

    /**
     * @param elem
     *            the attribute to be deleted
     */
    void deleteAttribute(Object elem) {
        if (!(elem instanceof Attribute)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
        // delete AttributeLinks where this is the Attribute
        nsmodel.getUmlHelper().deleteCollection(
                nsmodel.getUmlPackage().getCommonBehavior()
                        .getAAttributeLinkAttribute().getAttributeLink(
                                (Attribute) elem));
    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteBehavioralFeature(Object elem) {
        if (!(elem instanceof BehavioralFeature)) {
            throw new IllegalArgumentException("elem: " + elem);
        }

    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteBinding(Object elem) {
        if (!(elem instanceof Binding)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
        nsmodel.getUmlHelper().deleteCollection(
                corePackage.getABindingArgument()
                        .getArgument((Binding) elem));
    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteClass(Object elem) {
        if (!(elem instanceof UmlClass)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * Does a 'cascading delete' to all modelelements that are associated with
     * this element that would be in an illegal state after deletion of the
     * element. Does not do an cascading delete for elements that are deleted by
     * the MDR method remove. This method should not be called directly.
     * <p>
     * 
     * In the case of a classifier these are the following elements:
     * <ul>
     * <li>AssociationEnds that have this classifier as type
     * </ul>
     * 
     * @param elem
     * @see UmlFactoryMDRImpl#delete(Object)
     */
    void deleteClassifier(Object elem) {
        if (!(elem instanceof Classifier)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
        nsmodel.getUmlHelper().deleteCollection(
                nsmodel.getFacade().getAssociationEnds(elem));
        Classifier cls = (Classifier) elem;
        // delete CreateActions which have this as their instantiation
        nsmodel.getUmlHelper().deleteCollection(
                nsmodel.getUmlPackage().getCommonBehavior()
                        .getACreateActionInstantiation().getCreateAction(cls));
        // TODO: ?delete Instances which have this as their classifier?
        // or should we leave them since they contain so much state that the
        // user would have to recreate??
//        nsmodel.getUmlHelper().deleteCollection(
//                nsmodel.getUmlPackage().getCommonBehavior()
//                        .getAInstanceClassifier().getInstance(cls));
        // TODO: ?delete ObjectFlowStates which have this as their type?
//        nsmodel.getUmlHelper().deleteCollection(
//                nsmodel.getUmlPackage().getActivityGraphs()
//                        .getATypeObjectFlowState().getObjectFlowState(cls));
        // TODO: ?delete ClassifierInStates which have this as their type?
        nsmodel.getUmlHelper().deleteCollection(
                nsmodel.getUmlPackage().getActivityGraphs()
                        .getATypeClassifierInState().getClassifierInState(cls));
    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteComment(Object elem) {
        if (!(elem instanceof Comment)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteComponent(Object elem) {
        if (!(elem instanceof Component)) {
            throw new IllegalArgumentException("elem: " + elem);
        }

    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteConstraint(Object elem) {
        if (!(elem instanceof Constraint)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteDataType(Object elem) {
        if (!(elem instanceof DataType)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteDependency(Object elem) {
        if (!(elem instanceof Dependency)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteElement(Object elem) {
        if (!(elem instanceof Element)) {
            throw new IllegalArgumentException("elem: " + elem);
        }

    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteElementResidence(Object elem) {
        if (!(elem instanceof ElementResidence)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteFeature(Object elem) {
        if (!(elem instanceof Feature)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteFlow(Object elem) {
        if (!(elem instanceof Flow)) {
            throw new IllegalArgumentException("elem: " + elem);
        }

    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteGeneralizableElement(Object elem) {
        if (!(elem instanceof GeneralizableElement)) {
            throw new IllegalArgumentException("elem: " + elem);
        }

        GeneralizableElement generalizableElement = (GeneralizableElement) elem;
        nsmodel.getUmlHelper().deleteCollection(
                generalizableElement.getGeneralization());
        nsmodel.getUmlHelper().deleteCollection(
                corePackage.getAParentSpecialization().getSpecialization(
                        generalizableElement));

    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteGeneralization(Object elem) {
        if (!(elem instanceof Generalization)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteInterface(Object elem) {
        if (!(elem instanceof Interface)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteMethod(Object elem) {
        if (!(elem instanceof Method)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * Does a 'cascading delete' to all modelelements that are associated with
     * this element that would be in an illegal state after deletion of the
     * element. Does not do an cascading delete for elements that are deleted by
     * MDR automatically. This method should not be called directly.
     * <p>
     * 
     * In the case of a modelelement these are the following elements:
     * <ul>
     * <li>Dependencies that have the modelelement as supplier or as a client
     * and are binary. (that is, they only have one supplier and one client)
     * <li>Behaviors, TemplateArguments, and ElementImports which require
     * this ModelElement
     * </ul>
     * 
     * @param elem
     * @see UmlFactoryMDRImpl#delete(Object)
     */
    void deleteModelElement(Object elem) {
        if (!(elem instanceof ModelElement)) {
            throw new IllegalArgumentException("elem: " + elem);
        }

        // Delete dependencies where this is the only client
        Collection deps = org.argouml.model.Model.getFacade()
                .getClientDependencies(elem);
        Iterator it = deps.iterator();
        while (it.hasNext()) {
            Dependency dep = (Dependency) it.next();
            if (dep.getClient().size() < 2
                    && dep.getClient().contains(elem)) {
                nsmodel.getUmlFactory().delete(dep);
            }
        }

        // Delete dependencies where this is the only supplier
        deps = org.argouml.model.Model.getFacade()
                .getSupplierDependencies(elem);
        it = deps.iterator();
        while (it.hasNext()) {
            Dependency dep = (Dependency) it.next();
            if (dep.getSupplier().size() < 2
                    && dep.getSupplier().contains(elem)) {
                nsmodel.getUmlFactory().delete(dep);
            }
        }

        /* Do not delete behaviors here! 
         * The behavior-context relation in the UML model 
         * is an aggregate, not composition. See issue 4281. */

        nsmodel.getUmlHelper().deleteCollection(
                corePackage
                        .getAModelElementTemplateArgument()
                        .getTemplateArgument((ModelElement) elem));
        nsmodel.getUmlHelper().deleteCollection(
                nsmodel.getUmlPackage().getModelManagement()
                        .getAImportedElementElementImport()
                        .getElementImport((ModelElement) elem));
        

    }

    /**
     * A namespace deletes its owned elements.
     * 
     * @param elem
     *            is the namespace.
     */
    void deleteNamespace(Object elem) {
        if (!(elem instanceof Namespace)) {
            throw new IllegalArgumentException("elem: " + elem);
        }

        List ownedElements = new ArrayList();
        // TODO: This is a composite association, so these will get deleted
        // automatically.  The only thing we need to do is check for any
        // additional elements that need to be deleted as a result.
        ownedElements.addAll(((Namespace) elem).getOwnedElement());
        Iterator it = ownedElements.iterator();
        while (it.hasNext()) {
            nsmodel.getUmlFactory().delete(it.next());
        }
    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteNode(Object elem) {
        if (!(elem instanceof Node)) {
            throw new IllegalArgumentException("elem: " + elem);
        }

    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteOperation(Object elem) {
        if (!(elem instanceof Operation)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
        Operation oper = (Operation) elem;
        // delete CallActions which have this as their operation
        nsmodel.getUmlHelper().deleteCollection(
                nsmodel.getUmlPackage().getCommonBehavior()
                        .getACallActionOperation().getCallAction(oper));
        // delete CallEvents which have this as their operation
        nsmodel.getUmlHelper().deleteCollection(
                nsmodel.getUmlPackage().getStateMachines()
                        .getAOccurrenceOperation().getOccurrence(oper));
    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteParameter(Object elem) {
        if (!(elem instanceof Parameter)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deletePermission(Object elem) {
        if (!(elem instanceof Permission)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deletePresentationElement(Object elem) {
        if (!(elem instanceof PresentationElement)) {
            throw new IllegalArgumentException("elem: " + elem);
        }

    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteRelationship(Object elem) {
        if (!(elem instanceof Relationship)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteStructuralFeature(Object elem) {
        if (!(elem instanceof StructuralFeature)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteTemplateArgument(Object elem) {
        if (!(elem instanceof TemplateArgument)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }
    
    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteTemplateParameter(Object elem) {
        if (!(elem instanceof TemplateParameter)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * @param elem
     *            the element to be deleted
     */
    void deleteUsage(Object elem) {
        if (!(elem instanceof Usage)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * Delete an Enumeration.
     * @param elem
     *            the element to be deleted
     * @since UML 1.4
     */
    void deleteEnumeration(Object elem) {
        if (!(elem instanceof Enumeration)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
        // EnumerationLiterals should get deleted implicitly
        // since they are associated by composition
    }

    /**
     * Delete EnumerationLiteral.
     * @param elem
     *            the element to be deleted
     * @since UML 1.4
     */
    void deleteEnumerationLiteral(Object elem) {
        if (!(elem instanceof EnumerationLiteral)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * Delete the given UML Primitive.
     * 
     * @param elem the element to be deleted
     * @since UML 1.4
     */
    void deletePrimitive(Object elem) {
        if (!(elem instanceof Primitive)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }

    /**
     * Delete the given ProgrammingLanguageDataType.
     * 
     * @param elem the element to be deleted
     * @since UML 1.4
     */
    void deleteProgrammingLanguageDataType(Object elem) {
        if (!(elem instanceof ProgrammingLanguageDataType)) {
            throw new IllegalArgumentException("elem: " + elem);
        }
    }



    /**
     * Copies a class, and it's features. This may also require other
     * classifiers to be copied.
     * 
     * @param source
     *            is the class to copy.
     * @param ns
     *            is the namespace to put the copy in.
     * @return a newly created class.
     */
    public Object copyClass(Object source, Object ns) {
        if (!(source instanceof UmlClass && ns instanceof Namespace)) {
            throw new IllegalArgumentException("source: " + source + ",ns: "
                    + ns);
        }

        UmlClass c = (UmlClass) createClass();
        ((Namespace) ns).getOwnedElement().add(c);
        doCopyClass(source, c);
        return c;
    }

    /**
     * Copies a feature from one classifier to another.
     *
     * @param source is the feature to copy.
     * @param classifier is the classifier to put the copy in.
     * @return a newly created feature.
     */
    public Object copyFeature(Object source, Object classifier) {
        if (!(source instanceof Feature && classifier instanceof Classifier)) {
            throw new IllegalArgumentException("source: " + source 
                    + ",classifier: " + classifier);
        }

        Feature f = null;
        if (source instanceof Attribute) {
            Attribute attr = (Attribute) createAttribute();
            doCopyAttribute((Attribute) source, attr);
            f = attr;
        } else if (source instanceof Operation) {
            Operation oper = (Operation) createOperation();
            doCopyOperation((Operation) source, oper);
            // TODO: build a return parameter
            f = oper;
        } else if (source instanceof Method) {
            Method method = (Method) createMethod();
            doCopyMethod((Method) source, method);
            f = method;
        } else if (source instanceof Reception) {
            Reception reception = (Reception) 
                nsmodel.getCommonBehaviorFactory().createReception();
            doCopyReception((Reception) source, reception);
            f = reception;
        } else {
            throw new IllegalArgumentException("source: " + source);
        }

        f.setOwner((Classifier) classifier);
        ((Classifier) classifier).getFeature().add(f);
        return f;
    }

    /**
     * Copies a datatype, and it's features. This may also require other
     * classifiers to be copied.
     * 
     * @param source
     *            is the datatype to copy.
     * @param ns
     *            is the namespace to put the copy in.
     * @return a newly created data type.
     */
    public Object copyDataType(Object source, Object ns) {
        if (!(source instanceof DataType)) {
            throw new IllegalArgumentException();
        }

        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException();
        }

        DataType i = (DataType) createDataType();
        ((Namespace) ns).getOwnedElement().add(i);
        doCopyDataType(source, i);
        return i;
    }

    /**
     * Copies an interface, and it's features. This may also require other
     * classifiers to be copied.
     * 
     * @param source
     *            is the interface to copy.
     * @param ns
     *            is the namespace to put the copy in.
     * @return a newly created interface.
     */
    public Object copyInterface(Object source, Object ns) {
        if (!(source instanceof Interface)) {
            throw new IllegalArgumentException();
        }

        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException();
        }

        Interface i = (Interface) createInterface();
        ((Namespace) ns).getOwnedElement().add(i);
        doCopyInterface(source, i);
        return i;
    }

    /**
     * 
     * @param from
     *            The object which own the enumeration to copy
     * @param to
     *            The object to which copy the enumeration
     */
    public void copyEnumeration(Object from, Object to) {
        doCopyModelElement(from, to);
        List listFrom = ((Enumeration) from).getLiteral();
        List listTo = ((Enumeration) to).getLiteral();
        Object literalFrom;
        Object literalTo;
        for (int i = 0; i < listFrom.size(); i++) {
            literalFrom = listFrom.get(i);
            if (listTo.size() > i) {
                literalTo = listTo.get(i);
            } else {
                literalTo = createEnumerationLiteral();
                listTo.add(literalTo);
            }
            doCopyModelElement(literalFrom, literalTo);
            ((EnumerationLiteral) literalTo).setEnumeration((Enumeration) to);
        }
    }

    /**
     * Used by the copy functions. Do not call this function directly.
     */
    private void doCopyElement(Object source, Object target) {
        // Nothing more to do.
    }

    /**
     * Used by the copy functions. Do not call this function directly.
     * 
     * @param source
     *            the source class
     * @param target
     *            the target class
     */
    public void doCopyClass(Object source, Object target) {
        if (!(source instanceof UmlClass)) {
            throw new IllegalArgumentException();
        }

        if (!(target instanceof UmlClass)) {
            throw new IllegalArgumentException();
        }

        doCopyClassifier(source, target);

        ((UmlClass) target).setActive(((UmlClass) source).isActive());
    }

    /*
     * TODO: All the ToDos in the doCopyFoo methods below are inherited from the
     * NSUML implementation and do not reflect new issues. One additional thing
     * which does need to be dealt with is the copying of any attributes which
     * have been added since this code was implemented for UML 1.3.
     */
    /**
     * Used by the copy functions. Do not call this function directly. 
     * TODO: actions? instances? collaborations etc?
     * 
     * @param source
     *            the source classifier
     * @param target
     *            the target classifier
     */
    public void doCopyClassifier(Object source, Object target) {
        if (!(source instanceof Classifier)) {
            throw new IllegalArgumentException();
        }

        if (!(target instanceof Classifier)) {
            throw new IllegalArgumentException();
        }

        // TODO: how to merge multiple inheritance? Necessary?
        // This currently copies the common ancestors multiple times
        doCopyNamespace(source, target);
        doCopyGeneralizableElement(source, target);

        // Copy all the Features
        List features = ((Classifier) source).getFeature();
        Iterator i = features.iterator();
        while (i.hasNext()) {
            Feature f = (Feature) i.next();
            copyFeature(f, target);
        }
    }

    /**
     * Used by the copy functions. Do not call this function directly.
     * 
     * @param source
     *            the source datatype
     * @param target
     *            the target datatype
     */
    public void doCopyDataType(Object source, Object target) {
        if (!(source instanceof DataType)) {
            throw new IllegalArgumentException();
        }

        if (!(target instanceof DataType)) {
            throw new IllegalArgumentException();
        }

        doCopyClassifier(source, target);
    }

    /**
     * Used by the copy functions. Do not call this function directly. 
     * TODO: generalizations, specializations?
     * 
     * @param source
     *            the source generalizable element
     * @param target
     *            the target generalizable element
     */
    public void doCopyGeneralizableElement(Object source, Object target) {
        if (!(source instanceof GeneralizableElement
                && target instanceof GeneralizableElement)) {
            throw new IllegalArgumentException("source: " + source
                    + ",target: " + target);
        }

        doCopyModelElement(source, target);

        GeneralizableElement targetGE = ((GeneralizableElement) target);
        GeneralizableElement sourceGE = ((GeneralizableElement) source);
        targetGE.setAbstract(sourceGE.isAbstract());
        targetGE.setLeaf(sourceGE.isLeaf());
        targetGE.setRoot(sourceGE.isRoot());
    }

    /**
     * Used by the copy functions. Do not call this function directly.
     * 
     * @param source
     *            the source interface
     * @param target
     *            the target interface
     */
    public void doCopyInterface(Object source, Object target) {
        if (!(source instanceof Interface)) {
            throw new IllegalArgumentException();
        }

        if (!(target instanceof Interface)) {
            throw new IllegalArgumentException();
        }

        doCopyClassifier(source, target);
    }

    /**
     * Used by the copy functions. Do not call this function directly. 
     * TODO: template parameters, default type 
     * TODO: constraining elements 
     * TODO: flows, dependencies, comments, bindings, contexts ??? 
     * TODO: contents, residences ???
     * 
     * @param source
     *            the source me
     * @param target
     *            the target me
     */
    public void doCopyModelElement(Object source, Object target) {
        if (!(source instanceof ModelElement)) {
            throw new IllegalArgumentException();
        }

        if (!(target instanceof ModelElement)) {
            throw new IllegalArgumentException();
        }

        // Set the name so that superclasses can find the newly
        // created element in the model, if necessary.
        ModelElement targetME = ((ModelElement) target);
        ModelElement sourceME = ((ModelElement) source);
        targetME.setName(sourceME.getName());
        doCopyElement(source, target);

        targetME.setSpecification(sourceME.isSpecification());
        targetME.setVisibility(sourceME.getVisibility());
        nsmodel.getExtensionMechanismsFactory()
                .copyTaggedValues(source, target);

        if (!sourceME.getStereotype().isEmpty()) {
            // Note that if we're copying this element then we
            // must also be allowed to copy other necessary
            // objects.
            Model targetModel = (Model) org.argouml.model.Model.getFacade()
                    .getModel(targetME);
            Iterator it = sourceME.getStereotype().iterator();
            while (it.hasNext()) {
                Stereotype st = (Stereotype) nsmodel.getModelManagementHelper()
                        .getCorrespondingElement(it.next(), targetModel, true);
                targetME.getStereotype().add(st);
            }
        }
    }

    /**
     * Used by the copy functions. Do not call this function directly.
     * 
     * @param source
     *            the source namespace
     * @param target
     *            the target namespace
     */
    public void doCopyNamespace(Object source, Object target) {
        if (!(source instanceof Namespace)) {
            throw new IllegalArgumentException();
        }

        if (!(target instanceof Namespace)) {
            throw new IllegalArgumentException();
        }

        doCopyModelElement(source, target);
        // Nothing more to do, don't copy owned elements.
    }

    /**
     * Copy the meta-attributes of an Attribute to another.
     * 
     * @param source the source attribute
     * @param target the new attribute to be adapted
     */
    void doCopyAttribute(Attribute source, Attribute target) {
        target.setMultiplicity(getMultiplicity11());
        target.setChangeability(source.getChangeability());
        target.setTargetScope(source.getTargetScope());
        target.setType(source.getType());

        doCopyFeature(source, target);
    }

    /**
     * Copy the attributes of an Operation to another.
     * 
     * @param source the source operation
     * @param target the new operation to be modified
     */
    void doCopyOperation(Operation source, Operation target) {
        target.setAbstract(source.isAbstract());
        target.setLeaf(source.isLeaf());
        target.setRoot(source.isRoot());
        target.setConcurrency(source.getConcurrency());
        target.setSpecification(source.getSpecification());
    
        doCopyBehavioralFeature(source, target);
    }

    /**
     * Copy the attributes of one Method to another.
     * 
     * @param source the method to copy attributes from
     * @param target the method to be adapted
     */
    void doCopyMethod(Method source, Method target) {
        ProcedureExpression pe = source.getBody();
        if (pe != null) {
            target.setBody((ProcedureExpression) 
                    nsmodel.getDataTypesFactory().createProcedureExpression(
                            pe.getLanguage(), pe.getBody()));
        }

        doCopyBehavioralFeature(source, target);
    }



    
    /**
     * Copy the attributes of one Reception to another.
     * 
     * @param source the rception to copy attributes from
     * @param target the reception to be adapted
     */
    void doCopyReception(Reception source, Reception target) {
        target.setAbstract(source.isAbstract());
        target.setLeaf(source.isLeaf());
        target.setRoot(source.isRoot());
        target.setSpecification(source.getSpecification());
        target.setSignal(source.getSignal());

        doCopyBehavioralFeature(source, target);
    }
    
    
    /**
     * Copy the attributes of one BehavioralFeature to another.
     * 
     * @param source the BehavioralFeature to copy from
     * @param target the BehavioralFeature to b adapted
     */
    void doCopyBehavioralFeature(BehavioralFeature source, 
            BehavioralFeature target) {
        target.setQuery(source.isQuery());
        // copy raised signals:
        Collection c = nsmodel.getUmlPackage().getCommonBehavior()
            .getAContextRaisedSignal().getRaisedSignal(source);
        Iterator i = c.iterator();
        while (i.hasNext()) {
            nsmodel.getUmlPackage().getCommonBehavior()
                .getAContextRaisedSignal().add(target, (Signal) i.next());
        }

        doCopyFeature(source, target);
    }

    /**
     * Copy the attributes of one Feature to another.
     * 
     * @param source the Feature to copy from
     * @param target the Feature to copy to
     */
    void doCopyFeature(Feature source, Feature target) {
        target.setVisibility(source.getVisibility());
        target.setOwnerScope(source.getOwnerScope());

        doCopyModelElement(source, target);
    }

}
