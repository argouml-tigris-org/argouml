/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *    Thomas Neustupny
 *    Tom Morris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jmi.reflect.InvalidObjectException;
import javax.jmi.reflect.RefObject;

import org.argouml.model.CoreFactory;
import org.argouml.model.Model;
import org.argouml.model.ModelCommand;
import org.argouml.model.ModelManagementHelper;
import org.argouml.model.NotImplementedException;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;
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
import org.omg.uml.foundation.datatypes.Expression;
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
import org.omg.uml.modelmanagement.UmlPackage;

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

    private static final Logger LOG =
        Logger.getLogger(CoreFactoryMDRImpl.class.getName());

    /**
     * The model implementation.
     */
    private MDRModelImplementation modelImpl;

    /**
     * Constructor.
     *
     * @param implementation
     *            To get other helpers and factories.
     */
    CoreFactoryMDRImpl(MDRModelImplementation implementation) {
        modelImpl = implementation;
    }

    private CorePackage getCorePackage() {
        return modelImpl.getUmlPackage().getCore();
    }

    public Abstraction createAbstraction() {
        Abstraction myAbstraction = getCorePackage().getAbstraction()
                .createAbstraction();
        super.initialize(myAbstraction);
        return myAbstraction;
    }


    public Abstraction buildAbstraction(String name, Object supplier,
            Object client) {
        if (!(client instanceof Classifier)
                || !(supplier instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "The supplier and client of an abstraction"
                            + "should be classifiers");
        }
        if (client.equals(supplier)) {
            throw new IllegalArgumentException("The supplier and the client "
                    + "must be different elements");
        }
        Abstraction abstraction = createAbstraction();
        abstraction.setName(name);
        abstraction.getClient().add((Classifier) client);
        abstraction.getSupplier().add((Classifier) supplier);
        return abstraction;
    }


    public Artifact createArtifact() {
        Artifact artifact = getCorePackage().getArtifact().createArtifact();
        super.initialize(artifact);
        return artifact;
    }


    @Deprecated
    public UmlAssociation createAssociation() {
        return createAssociation(modelImpl.getUmlPackage());
    }


    public UmlAssociation createAssociation(Object extent) {
        UmlAssociation assoc = ((org.omg.uml.UmlPackage) extent).getCore()
                .getUmlAssociation().createUmlAssociation();
        super.initialize(assoc);
        return assoc;
    }

    public AssociationClass createAssociationClass() {
        AssociationClass assoc = getCorePackage().getAssociationClass()
                .createAssociationClass();
        super.initialize(assoc);
        return assoc;
    }


    public AssociationEnd createAssociationEnd() {
        AssociationEnd assocEnd = getCorePackage().getAssociationEnd()
                .createAssociationEnd();
        super.initialize(assocEnd);
        return assocEnd;
    }


    public Attribute createAttribute() {
        Attribute myAttribute = getCorePackage().getAttribute().createAttribute();
        super.initialize(myAttribute);
        return myAttribute;
    }


    public Binding createBinding() {
        Binding myBinding = getCorePackage().getBinding().createBinding();
        super.initialize(myBinding);
        return myBinding;
    }


    public UmlClass createClass() {
        return createClass(modelImpl.getUmlPackage());
    }

    public UmlClass createClass(org.omg.uml.UmlPackage extent) {
        UmlClass myClass = extent.getCore().getUmlClass().createUmlClass();
        super.initialize(myClass);
        return myClass;
    }

    public Comment createComment() {
        Comment myComment = getCorePackage().getComment().createComment();
        super.initialize(myComment);
        return myComment;
    }


    public Component createComponent() {
        Component myComponent = getCorePackage().getComponent().createComponent();
        super.initialize(myComponent);
        return myComponent;
    }


    public Constraint createConstraint() {
        Constraint myConstraint = getCorePackage().getConstraint()
                .createConstraint();
        super.initialize(myConstraint);
        return myConstraint;
    }


    public DataType createDataType() {
        DataType dataType = getCorePackage().getDataType().createDataType();
        super.initialize(dataType);
        return dataType;
    }


    public Dependency createDependency() {
        Dependency myDependency = getCorePackage().getDependency()
                .createDependency();
        super.initialize(myDependency);
        return myDependency;
    }


    public ElementResidence createElementResidence() {
        ElementResidence myElementResidence = getCorePackage().
                getElementResidence().createElementResidence();
        super.initialize(myElementResidence);
        return myElementResidence;
    }


    public ElementResidence buildElementResidence(Object me, Object component) {
        ElementResidence myElementResidence =
            ((org.omg.uml.UmlPackage) ((ModelElement) me)
                .refOutermostPackage()).getCore().getElementResidence()
                .createElementResidence();
        super.initialize(myElementResidence);
        myElementResidence.setContainer((Component) component);
        myElementResidence.setResident((ModelElement) me);
        return myElementResidence;
    }


    public Enumeration createEnumeration() {
        Enumeration myEnumeration = getCorePackage().getEnumeration()
                .createEnumeration();
        super.initialize(myEnumeration);
        return myEnumeration;
    }


    public EnumerationLiteral createEnumerationLiteral() {
        EnumerationLiteral myEnumerationLiteral = getCorePackage()
                .getEnumerationLiteral().createEnumerationLiteral();
        super.initialize(myEnumerationLiteral);
        return myEnumerationLiteral;
    }


    public EnumerationLiteral buildEnumerationLiteral(String name,
            Object enumeration) {
        EnumerationLiteral el = createEnumerationLiteral();
        el.setName(name);
        el.setEnumeration((Enumeration) enumeration);
        return el;
    }


    public Flow createFlow() {
        Flow myFlow = getCorePackage().getFlow().createFlow();
        super.initialize(myFlow);
        return myFlow;
    }

    @Deprecated
    public Generalization createGeneralization() {
        return createGeneralization(modelImpl.getUmlPackage());
    }

    public Generalization createGeneralization(Object extent) {
        Generalization myGeneralization = ((org.omg.uml.UmlPackage) extent)
                .getCore().getGeneralization().createGeneralization();
        super.initialize(myGeneralization);
        return myGeneralization;
    }

    public Interface createInterface() {
        Interface myInterface = getCorePackage()
                .getInterface().createInterface();
        super.initialize(myInterface);
        return myInterface;
    }


    public Method createMethod() {
        Method myMethod = getCorePackage().getMethod()
                .createMethod();
        super.initialize(myMethod);
        return myMethod;
    }


    public Node createNode() {
        Node myNode = getCorePackage().getNode().createNode();
        super.initialize(myNode);
        return myNode;
    }


    public Operation createOperation() {
        Operation myOperation = getCorePackage()
                .getOperation().createOperation();
        super.initialize(myOperation);
        return myOperation;
    }


    public Parameter createParameter() {
        Parameter myParameter = getCorePackage()
                .getParameter().createParameter();
        super.initialize(myParameter);
        return myParameter;
    }


    @Deprecated
    public Permission createPermission() {
        return createPackageImport();
    }

    public Permission createPackageImport() {
        Permission myPermission = getCorePackage()
                .getPermission().createPermission();
        super.initialize(myPermission);
        return myPermission;
    }

    public Primitive createPrimitiveType() {
        Primitive obj = getCorePackage().getPrimitive().createPrimitive();
        super.initialize(obj);
        return obj;
    }


    public TemplateArgument createTemplateArgument() {
        return createTemplateArgument(modelImpl.getUmlPackage());
    }

      private  TemplateArgument createTemplateArgument(
              org.omg.uml.UmlPackage extent) {
        TemplateArgument obj = extent.getCore().getTemplateArgument()
                .createTemplateArgument();
        super.initialize(obj);
        return obj;
    }


    public TemplateParameter createTemplateParameter() {
        return createTemplateParameter(modelImpl.getUmlPackage());
    }

    private TemplateParameter createTemplateParameter(
            org.omg.uml.UmlPackage extent) {
        TemplateParameter myTemplateParameter = extent.getCore()
                .getTemplateParameter().createTemplateParameter();
        super.initialize(myTemplateParameter);
        return myTemplateParameter;
    }


    public Usage createUsage() {
        Usage myUsage = getCorePackage().getUsage().createUsage();
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
     * @return a newly created Association
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

        // We'll put the association in the namespace of whichever end
        // is not navigable and is writeable.  If they both are, we'll use the
        // namepace of c1.
        Namespace ns = null;
        if (nav2 && !modelImpl.getModelManagementHelper().isReadOnly(ns1)) {
            ns = ns1;
        } else if (nav1
                && !modelImpl.getModelManagementHelper().isReadOnly(ns2)) {
            ns = ns2;
        } else {
            throw new IllegalArgumentException(
                    "At least one end must be navigable");
        }
        UmlAssociation assoc = createAssociation(ns.refOutermostPackage());
        assoc.setName("");
        assoc.setNamespace(ns);
        buildAssociationEnd(assoc, null, c1, null, null,
                nav1, null, agg1, null, null, null);
        buildAssociationEnd(assoc, null, c2, null, null,
                nav2, null, agg2, null, null, null);
        return assoc;
    }

    @Deprecated
    public UmlAssociation buildAssociation(Object fromClassifier,
            Object aggregationKind1, Object toClassifier,
            Object aggregationKind2, Boolean unidirectional) {

        if (unidirectional == null) {
            return buildAssociation(fromClassifier, aggregationKind1,
                    toClassifier, aggregationKind2, false);
        } else {
            return buildAssociation(fromClassifier, aggregationKind1,
                    toClassifier, aggregationKind2, unidirectional
                            .booleanValue());
        }
    }

    public UmlAssociation buildAssociation(Object fromClassifier,
            Object aggregationKind1, Object toClassifier,
            Object aggregationKind2, boolean unidirectional) {
        if (fromClassifier == null || toClassifier == null) {
            throw new IllegalArgumentException("one of "
                    + "the classifiers to be " + "connected is null");
        }
        Classifier from = (Classifier) fromClassifier;
        Classifier to = (Classifier) toClassifier;
        AggregationKind agg1 = (AggregationKind) aggregationKind1;
        AggregationKind agg2 = (AggregationKind) aggregationKind2;

        Namespace ns = from.getNamespace();
        if (ns == null || modelImpl.getModelManagementHelper().isReadOnly(ns)) {
            ns = to.getNamespace();
            if (ns == null
                    || modelImpl.getModelManagementHelper().isReadOnly(ns)) {
                throw new IllegalArgumentException(
                        "At least one namespace must be non-null and writeable");
            }
        }

        UmlAssociation assoc = createAssociation(ns.refOutermostPackage());
        assoc.setName("");
        assoc.setNamespace(ns);

        final boolean nav1 = !unidirectional;
        final boolean nav2 = true;

        buildAssociationEnd(assoc, null, from, null, null, nav1, null, agg1,
                null, null, null);
        buildAssociationEnd(assoc, null, to, null, null, nav2, null, agg2,
                null, null, null);
        return assoc;
    }


    public UmlAssociation buildAssociation(Object classifier1,
            Object classifier2) {
        Classifier c1 = (Classifier) classifier1;
        Classifier c2 = (Classifier) classifier2;
        return buildAssociation(c1, true, AggregationKindEnum.AK_NONE, c2,
                true, AggregationKindEnum.AK_NONE);
    }


    public UmlAssociation buildAssociation(Object c1, boolean nav1, Object c2,
            boolean nav2, String name) {
        UmlAssociation assoc = buildAssociation((Classifier) c1, nav1,
                AggregationKindEnum.AK_NONE, (Classifier) c2, nav2,
                AggregationKindEnum.AK_NONE);
        if (assoc != null) {
            assoc.setName(name);
        }
        return assoc;
    }


    public AssociationClass buildAssociationClass(Object end1, Object end2) {
        if (end1 == null || end2 == null || !(end1 instanceof Classifier)
                || !(end2 instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "either one of the arguments was null");
        }
        final Classifier classifier1 = (Classifier) end1;
        final Classifier classifier2 = (Classifier) end2;
        AssociationClass assocClass = createAssociationClass();

        assocClass.setNamespace(classifier1.getNamespace());
        assocClass.setName("");
        assocClass.setAbstract(false);
        assocClass.setActive(false);
        assocClass.setRoot(false);
        assocClass.setLeaf(false);
        assocClass.setSpecification(false);
        assocClass.setVisibility(VisibilityKindEnum.VK_PUBLIC);

        buildAssociationEnd(
                assocClass, null, classifier1, null, null, true, null, null,
                null, null, null);
        buildAssociationEnd(
                assocClass, null, classifier2, null, null, true, null, null,
                null, null, null);
        return assocClass;
    }


    public AssociationEnd buildAssociationEnd(Object assoc, String name,
            Object type, Integer[] multiplicity, Object stereo, boolean navigable,
            Object order, Object aggregation, Object scope, Object changeable,
            Object visibility) {
        if (aggregation != null
                && aggregation.equals(AggregationKindEnum.AK_COMPOSITE)
                && multiplicity != null
                && (multiplicity[1] > 1 || multiplicity[1] == -1) ) {
            throw new IllegalArgumentException("aggregation is composite "
                    + "and multiplicity > 1");
        }
        AssociationEnd ae = buildAssociationEndInternal(assoc, name, type,
                stereo, navigable, order, aggregation, scope, changeable,
                visibility);
        if (multiplicity != null) {
            Multiplicity m = modelImpl.getDataTypesFactoryInternal()
                    .createMultiplicityInternal(multiplicity[0],
                            multiplicity[1]);
            ae.setMultiplicity(m);
        }
        return ae;
    }

    @Deprecated
    public AssociationEnd buildAssociationEnd(Object assoc, String name,
            Object type, Object multi, Object stereo, boolean navigable,
            Object order, Object aggregation, Object scope, Object changeable,
            Object visibility) {
        if (multi != null && !(multi instanceof Multiplicity)) {
            throw new IllegalArgumentException("Multiplicity");
        }
        if (aggregation != null
                && aggregation.equals(AggregationKindEnum.AK_COMPOSITE)
                && multi != null
                && compareMultiplicity(getMaxUpper((Multiplicity) multi), 1)
                    > 0) {
            throw new IllegalArgumentException("aggregation is composite "
                    + "and multiplicity > 1");
        }
        AssociationEnd ae = buildAssociationEndInternal(assoc, name, type,
                stereo, navigable, order, aggregation, scope, changeable,
                visibility);
        if (multi == null) {
            ae.setMultiplicity(getMultiplicity11());
        } else if (multi instanceof Multiplicity) {
            ae.setMultiplicity((Multiplicity) multi);
        } else if (multi instanceof String) {
            Multiplicity m = modelImpl.getDataTypesFactoryInternal()
                    .createMultiplicityInternal((String) multi);
            ae.setMultiplicity(m);
        }
        return ae;
    }

    private AssociationEnd buildAssociationEndInternal (Object assoc, String name,
            Object type, Object stereo, boolean navigable,
            Object order, Object aggregation, Object scope, Object changeable,
            Object visibility) {
        // wellformednessrules and preconditions
        if (assoc == null || !(assoc instanceof UmlAssociation) || type == null
                || !(type instanceof Classifier)) {
            throw new IllegalArgumentException("either type or association "
                    + "are null");
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

        AssociationEnd end = createAssociationEnd();
        end.setAssociation((UmlAssociation) assoc);
        end.setParticipant((Classifier) type);
        end.setName(name);
        // UML 1.4 WFR 2.5.3.1 #3 - no aggregation for N-ary associations
        List<AssociationEnd> ends = ((UmlAssociation) assoc).getConnection();
        if (ends.size() >= 3) {
            for (AssociationEnd e : ends) {
                e.setAggregation(AggregationKindEnum.AK_NONE);
            }
        }
        if (stereo != null) {
            end.getStereotype().clear();
            end.getStereotype().add((Stereotype) stereo);
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

    private static final int MULT_UNLIMITED = -1;

    /**
     * Get the maximum value of a multiplicity
     *
     * @param m
     *            the Multiplicity
     * @return upper range
     */
    private int getMaxUpper(Multiplicity m) {
        int max = 0;
        for (MultiplicityRange mr : m.getRange()) {
            int value = mr.getUpper();
            if (value == MULT_UNLIMITED) {
                max = value;
            } else if (max != MULT_UNLIMITED && value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * Compare two multiplicities taking care of the value 'unlimited' (-1).
     *
     * @param mult1 first multiplicity
     * @param mult2 second multiplicity
     * @return 0 if equal, a positive integer (not necessarily 1) if mult1 is
     *         greater than mult2 and a negative integer if mult2 is greater..
     */
    private static int compareMultiplicity(int mult1, int mult2) {
        if (mult1 == MULT_UNLIMITED) {
            if (mult2 == MULT_UNLIMITED) {
                return 0; // equal
            }
            return 1; // greater
        } else if (mult2 == MULT_UNLIMITED) {
            return -1; // less than
        }
        return mult1 - mult2;
    }

    /**
     * Get a 1..1 multiplicity
     */
    private Multiplicity getMultiplicity11() {
        return modelImpl.getDataTypesFactoryInternal()
                .createMultiplicityInternal(1, 1);
    }


    public AssociationEnd buildAssociationEnd(Object type, Object assoc) {
        if (type == null || !(type instanceof Classifier) || assoc == null
                || !(assoc instanceof UmlAssociation)) {
            throw new IllegalArgumentException("one of the arguments is null");
        }
        return buildAssociationEnd(assoc, "", type, null, null, true, null,
                null, null, null, VisibilityKindEnum.VK_PUBLIC);
    }

    public Attribute buildAttribute(Object model, Object theType) {
        return buildAttribute2(theType);
    }

    public Attribute buildAttribute2(Object theType) {
        Attribute attr = buildAttribute();
        attr.setType((Classifier) theType);
        return attr;
    }

    /**
     * Build a new attribute with no type
     * @return the new attribute
     */
    Attribute buildAttribute() {
        Attribute attr = createAttribute();
        attr.setMultiplicity(getMultiplicity11());
        attr.setVisibility(VisibilityKindEnum.VK_PUBLIC);
        attr.setOwnerScope(ScopeKindEnum.SK_INSTANCE);
        attr.setChangeability(ChangeableKindEnum.CK_CHANGEABLE);
        attr.setTargetScope(ScopeKindEnum.SK_INSTANCE);
        return attr;
    }


    public Attribute buildAttribute2(Object handle, Object type) {
        Attribute attr = buildAttribute2(type);
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


    public UmlClass buildClass() {
        return buildClass((Object) null);
    }


    private static void initClass(UmlClass cl) {
        cl.setName("");
        cl.setAbstract(false);
        cl.setActive(false);
        cl.setRoot(false);
        cl.setLeaf(false);
        cl.setSpecification(false);
        cl.setVisibility(VisibilityKindEnum.VK_PUBLIC);
    }

    public UmlClass buildClass(final Object owner) {
        ModelCommand command = new ModelCommand() {
            private UmlClass cl;
            public UmlClass execute() {
                if (owner == null) {
                    cl = createClass();
                } else {
                    cl = createClass(getExtent(owner));
                }
                initClass(cl);
                return cl;
            }

            public void undo() {
                try {
                    cl.refDelete();
                } catch (InvalidObjectException e) {
                    LOG.log(Level.WARNING, "Object already deleted " + cl);
                }
            }

            public boolean isUndoable() {
                return true;
            }

            public boolean isRedoable() {
                return false;
            }
        };
        UmlClass clazz = (UmlClass) org.argouml.model.Model.execute(command);
        if (owner instanceof Namespace) {
            modelImpl.getCoreHelper().setNamespace(clazz, owner);
        }
        return clazz;
    }


    private org.omg.uml.UmlPackage getExtent(Object element) {
        return (org.omg.uml.UmlPackage) ((RefObject) element)
                .refOutermostPackage();
    }

    public UmlClass buildClass(String name) {
        UmlClass clazz = buildClass();
        clazz.setName(name);
        return clazz;
    }


    public UmlClass buildClass(String name, Object owner) {
        UmlClass clazz = buildClass();
        clazz.setName(name);
        if (owner instanceof Namespace) {
            modelImpl.getCoreHelper().setNamespace(clazz, owner);
        }
        return clazz;
    }


    public Interface buildInterface() {
        Interface cl = createInterface();
        cl.setName("");
        cl.setAbstract(false);
        cl.setRoot(false);
        cl.setLeaf(false);
        cl.setSpecification(false);
        cl.setVisibility(VisibilityKindEnum.VK_PUBLIC);
        return cl;
    }


    public Interface buildInterface(Object owner) {
        Interface cl = buildInterface();
        if (owner instanceof Namespace) {
            cl.setNamespace((Namespace) owner);
        }
        return cl;
    }


    public Interface buildInterface(String name) {
        Interface cl = buildInterface();
        cl.setName(name);
        return cl;
    }


    public Interface buildInterface(String name, Object owner) {
        Interface cl = buildInterface();
        cl.setName(name);
        if (owner instanceof Namespace) {
            cl.setNamespace((Namespace) owner);
        }
        return cl;
    }


    public DataType buildDataType(String name, Object owner) {
        DataType dt = createDataType();
        dt.setName(name);
        if (owner instanceof Namespace) {
            dt.setNamespace((Namespace) owner);
        }
        return dt;
    }


    public Enumeration buildEnumeration(String name, Object owner) {
        Enumeration e = createEnumeration();
        e.setName(name);
        if (owner instanceof Namespace) {
            e.setNamespace((Namespace) owner);
        }
        return e;
    }


    public Dependency buildDependency(Object clientObj, Object supplierObj) {

        ModelElement client = (ModelElement) clientObj;
        ModelElement supplier = (ModelElement) supplierObj;
        if (client == null || supplier == null) {
            throw new IllegalArgumentException("client or supplier is null "
                    + "client = " + client + " supplier = " + supplier);
        }
        Dependency dep = createDependency();
        dep.getSupplier().add(supplier);
        dep.getClient().add(client);
        if (client instanceof Namespace) {
            dep.setNamespace((Namespace) client);
        } else if (client.getNamespace() != null) {
            dep.setNamespace(client.getNamespace());
        }
        return dep;
    }


    public Permission buildPackageImport(Object client, Object supplier) {
        if (!(client instanceof Namespace)
                || !(supplier instanceof UmlPackage)) {
            throw new IllegalArgumentException("client is not a Namespace"
                    + " or supplier is not a Package");
        }
        Permission per = buildPermissionInternal((ModelElement) client,
                (UmlPackage) supplier);

        // TODO: This should fetch the stereotype from our profile
        modelImpl.getExtensionMechanismsFactory().buildStereotype(per,
                ModelManagementHelper.IMPORT_STEREOTYPE,
                per.getNamespace());
        return per;
    }


    private Permission buildPermissionInternal(ModelElement client,
            ModelElement supplier) {
        Permission permission = createPackageImport();
        permission.getSupplier().add(supplier);
        permission.getClient().add(client);
        if (client instanceof Namespace) {
            permission.setNamespace((Namespace) client);
        } else if (client.getNamespace() != null) {
            permission.setNamespace(client.getNamespace());
        }
        return permission;
    }


    public Permission buildPackageAccess(Object client, Object supplier) {
        if (!(client instanceof Namespace)
                || !(supplier instanceof UmlPackage)) {
            throw new IllegalArgumentException("client or "
                    + "supplier is not a Namespace");
        }
        Permission per = buildPermissionInternal((ModelElement) client,
                (UmlPackage) supplier);

        // TODO: This should fetch the stereotype from our profile
        modelImpl.getExtensionMechanismsFactory().buildStereotype(per,
                ModelManagementHelper.ACCESS_STEREOTYPE,
                per.getNamespace());
        return per;
    }


    public Generalization buildGeneralization(Object child1, Object parent1) {
        // TODO: This is a part implementation of well-formedness rule
        // UML1.4.2 - 4.5.3.20 [3] Circular inheritance is not allowed.
        // not self.allParents->includes(self)
        if (!(child1 instanceof GeneralizableElement
                && parent1 instanceof GeneralizableElement
                && child1 != parent1)) {
            throw new IllegalArgumentException(
                    "Both items must be different generalizable elements");
        }

        GeneralizableElement child = (GeneralizableElement) child1;
        GeneralizableElement parent = (GeneralizableElement) parent1;

        // Check that the two elements aren't already linked the opposite way
        // TODO: This is a part implementation of well-formedness rule
        // UML1.4.2 - 4.5.3.20 [3] Circular inheritance is not allowed.
        // not self.allParents->includes(self)
        for (Generalization gen : parent.getGeneralization()) {
            if (gen.getParent().equals(child)) {
                throw new IllegalArgumentException("Generalization exists"
                        + " in opposite direction");
            }
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

        Namespace ns = child.getNamespace();
        if ((ns == null || modelImpl.getModelManagementHelper().isReadOnly(ns))
                && child instanceof Namespace) {
            ns = (Namespace) child;
        }
        if (ns == null || modelImpl.getModelManagementHelper().isReadOnly(ns)) {
            throw new IllegalArgumentException("No valid writeable namespace");
        }
        Generalization gen = createGeneralization(ns.refOutermostPackage());
        gen.setParent(parent);
        gen.setChild(child);
        gen.setNamespace(ns);
        return gen;
    }

    public Object buildManifestation(Object utilizedElement) {
        throw new NotImplementedException( "UML 1.4 has no manifestations");
    }

    public Method buildMethod(String name) {
        Method method = createMethod();
        if (method != null) {
            method.setName(name);
        }
        return method;
    }


    public Operation buildOperation(Object classifier, Object returnType) {
        if (!(classifier instanceof Classifier)) {
            throw new IllegalArgumentException("Handle is not a classifier");
        }
        Classifier cls = (Classifier) classifier;
        Operation oper = createOperation();
        oper.setOwner(cls);
        oper.setVisibility(VisibilityKindEnum.VK_PUBLIC);
        oper.setAbstract(false);
        oper.setLeaf(false);
        oper.setRoot(false);
        oper.setQuery(false);
        oper.setOwnerScope(ScopeKindEnum.SK_INSTANCE);
        oper.setConcurrency(CallConcurrencyKindEnum.CCK_SEQUENTIAL);

        Parameter returnParameter = buildParameter(oper, returnType);
        returnParameter.setKind(ParameterDirectionKindEnum.PDK_RETURN);
        returnParameter.setName("return");
        return oper;
    }


    public Operation buildOperation2(Object cls, Object returnType,
            String name) {
        Operation oper = buildOperation(cls, returnType);
        if (oper != null) {
            oper.setName(name);
        }
        return oper;
    }

    /**
     * Constructs a default parameter.
     *
     * @return The newly created parameter.
     */
    private Parameter buildParameter(Classifier type,
            javax.jmi.reflect.RefObject ref) {
        Parameter param = ((org.omg.uml.UmlPackage) ref.refOutermostPackage())
                .getCore().getParameter().createParameter();
        param.setType(type);
        return param;
    }


    public Parameter buildParameter(Object o, Object type) {
        if (o instanceof Event) {
            Event event = (Event) o;
            Parameter res = buildParameter((Classifier) type, event);
            res.setKind(ParameterDirectionKindEnum.PDK_IN);
            event.getParameter().add(res);
            res.setName("arg" + event.getParameter().size());
            return res;
        } else if (o instanceof ObjectFlowState) {
            ObjectFlowState ofs = (ObjectFlowState) o;
            Parameter res = buildParameter((Classifier) type, ofs);
            res.setKind(ParameterDirectionKindEnum.PDK_IN);
            ofs.getParameter().add(res);
            res.setName("arg" + ofs.getParameter().size());
            return res;
        } else if (o instanceof BehavioralFeature) {
            BehavioralFeature oper = (BehavioralFeature) o;
            Parameter res = buildParameter((Classifier) type, oper);
            res.setKind(ParameterDirectionKindEnum.PDK_IN);
            oper.getParameter().add(res);
            res.setName("arg" + oper.getParameter().size());
            return res;
        } else if (o == null) {
            throw new IllegalArgumentException(
                    "A containing element must be supplied for the parameter");
        } else {
            throw new IllegalArgumentException(
                    "Unsupported contining element for parameter "
                    + o.getClass().getName());
        }
    }


    public Abstraction buildRealization(Object clnt, Object spplr,
            Object model) {
        ModelElement client = (ModelElement) clnt;
        ModelElement supplier = (ModelElement) spplr;
        if (client == null || supplier == null || client.getNamespace() == null
                || supplier.getNamespace() == null || client.equals(supplier)) {
            throw new IllegalArgumentException("faulty arguments.");
        }
        Abstraction realization = createAbstraction();
        Namespace nsc = client.getNamespace();
        Namespace nss = supplier.getNamespace();
        Namespace ns = null;
        if (nsc.equals(nss)) {
            ns = nsc;
        } else {
            ns = (Namespace) model;
        }
        realization.setNamespace(nsc);
        modelImpl.getExtensionMechanismsFactory().buildStereotype(realization,
                CoreFactory.REALIZE_STEREOTYPE, ns);
        realization.getClient().add(client);
        realization.getSupplier().add(supplier);
        return realization;
    }


    public TemplateArgument buildTemplateArgument(Object element) {
        TemplateArgument ta = createTemplateArgument();
        ta.setModelElement((ModelElement) element);
        return ta;
    }

    public TemplateArgument buildTemplateArgument(Object binding,
            Object element) {
        if (!(binding instanceof Binding && element instanceof ModelElement)) {
            throw new IllegalArgumentException();
        }
        TemplateArgument ta = createTemplateArgument(getExtent(binding));
        ta.setModelElement((ModelElement) element);
        ta.setBinding((Binding) binding);
        return ta;
    }

    public Object buildTemplateParameter(Object template, Object parameter,
            Object defaultElement) {
        if (!(template instanceof ModelElement)) {
            throw new IllegalArgumentException(
                    "Template must be a model element");
        }
        if (!(parameter instanceof ModelElement)) {
            if (parameter == null) {
                parameter = createClass(getExtent(template));
            } else {
            throw new IllegalArgumentException(
                    "Parameter must be a model element");
            }
        }
        if (defaultElement != null
                && !(defaultElement instanceof ModelElement)) {
            throw new IllegalArgumentException(
                    "Default element must be a model element");
        }

        TemplateParameter templateParam =
            createTemplateParameter(getExtent(template));
        templateParam.setParameter((ModelElement) parameter);
        if (defaultElement != null) {
            templateParam.setDefaultElement((ModelElement) defaultElement);
        }
        templateParam.setTemplate((ModelElement) template);
        return templateParam;
    }


    public Usage buildUsage(Object client, Object supplier) {
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
        Usage usage = createUsage();
        usage.getSupplier().add((ModelElement) supplier);
        usage.getClient().add((ModelElement) client);
        if (((ModelElement) supplier).getNamespace() != null) {
            usage.setNamespace(((ModelElement) supplier).getNamespace());
        } else if (((ModelElement) client).getNamespace() != null) {
            usage.setNamespace(((ModelElement) client).getNamespace());
        }
        // TODO: Add standard stereotype?  Set is open ended, but
        // predefined names include: call, create, instantiate, send
        return usage;
    }


    public Comment buildComment(Object element, Object model) {
        if (model == null) {
            throw new IllegalArgumentException("A namespace must be supplied.");
        }
        ModelElement elementToAnnotate = (ModelElement) element;
        Comment comment = createComment();

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


    public Constraint buildConstraint(Object constrElement) {
        ModelElement constrainedElement = (ModelElement) constrElement;
        if (constrainedElement == null) {
            throw new IllegalArgumentException("the constrained element is "
                    + "mandatory and may not be " + "null.");
        }
        Constraint con = createConstraint();
        con.getConstrainedElement().add(constrainedElement);
        con.setNamespace(constrainedElement.getNamespace());
        return con;
    }


    public Constraint buildConstraint(String name, Object bexpr) {
        if (bexpr == null || !(bexpr instanceof BooleanExpression)) {
            throw new IllegalArgumentException("invalid boolean expression.");
        }
        Constraint con = createConstraint();
        if (name != null) {
            con.setName(name);
        }
        con.setBody((BooleanExpression) bexpr);
        return con;
    }


    public Binding buildBinding(Object client, Object supplier,
            List arguments) {
        Collection<Dependency> clientDeps = ((ModelElement) client)
                .getClientDependency();
        for (Dependency dep : clientDeps) {
            if (dep instanceof Binding) {
                throw new IllegalArgumentException(
                        "client is already client of another Binding");
            }
        }

        // Check arguments against parameters for type and number
        // TODO: Perhaps move this to a critic instead? - tfm - 20070326
        if (arguments != null) {
            List<TemplateParameter> params =
                ((ModelElement) supplier).getTemplateParameter();
            if (params.size() != arguments.size()) {
                throw new IllegalArgumentException(
                        "number of arguments doesn't match number of params");
            }
            Iterator<TemplateArgument> ita = arguments.iterator();
            for (TemplateParameter param : params) {
                TemplateArgument ta = ita.next();
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

        Binding binding = createBinding();
        binding.getClient().add((ModelElement) client);
        binding.getSupplier().add((ModelElement) supplier);
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
            modelImpl.getUmlFactory().delete(assoc);
        }
        // delete LinkEnds which have this as their associationEnd
        modelImpl.getUmlHelper().deleteCollection(
                ((org.omg.uml.UmlPackage) ae.refOutermostPackage())
                        .getCommonBehavior().getAAssociationEndLinkEnd()
                        .getLinkEnd(ae));
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
        Attribute attr = (Attribute) elem;
        modelImpl.getUmlHelper().deleteCollection(
                ((org.omg.uml.UmlPackage) attr.refOutermostPackage())
                        .getCommonBehavior().getAAttributeLinkAttribute()
                        .getAttributeLink(attr));
    }

    /**
     * @param elem the element to be deleted
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
        Binding binding = (Binding) elem;
        modelImpl.getUmlHelper().deleteCollection(
                ((org.omg.uml.UmlPackage) binding.refOutermostPackage())
                        .getCore().getABindingArgument().getArgument(binding));
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
        modelImpl.getUmlHelper().deleteCollection(
                modelImpl.getFacade().getAssociationEnds(elem));
        Classifier cls = (Classifier) elem;
        // delete CreateActions which have this as their instantiation
        modelImpl.getUmlHelper().deleteCollection(
                ((org.omg.uml.UmlPackage) cls.refOutermostPackage())
                        .getCommonBehavior().getACreateActionInstantiation()
                        .getCreateAction(cls));
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
        modelImpl.getUmlHelper().deleteCollection(
                ((org.omg.uml.UmlPackage) cls.refOutermostPackage())
                        .getActivityGraphs().getATypeClassifierInState()
                        .getClassifierInState(cls));
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

        GeneralizableElement ge = (GeneralizableElement) elem;
        modelImpl.getUmlHelper().deleteCollection(ge.getGeneralization());
        modelImpl.getUmlHelper().deleteCollection(
                ((org.omg.uml.UmlPackage) ge.refOutermostPackage()).getCore()
                        .getAParentSpecialization().getSpecialization(ge));

    }

    /**
     * @param elem the element to be deleted
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
        Collection<Dependency> deps = org.argouml.model.Model.getFacade()
                .getClientDependencies(elem);
        for (Dependency dep : deps) {
            if (dep.getClient().size() < 2
                    && dep.getClient().contains(elem)) {
                modelImpl.getUmlFactory().delete(dep);
            }
        }

        // Delete dependencies where this is the only supplier
        deps = org.argouml.model.Model.getFacade()
                .getSupplierDependencies(elem);
        for (Dependency dep : deps) {
            if (dep.getSupplier().size() < 2
                    && dep.getSupplier().contains(elem)) {
                modelImpl.getUmlFactory().delete(dep);
            }
        }

        /* Do not delete behaviors here!
         * The behavior-context relation in the UML model
         * is an aggregate, not composition. See issue 4281. */

        ModelElement me = (ModelElement) elem;
        modelImpl.getUmlHelper().deleteCollection(
                ((org.omg.uml.UmlPackage) me.refOutermostPackage()).getCore()
                        .getAModelElementTemplateArgument()
                        .getTemplateArgument(me));
        modelImpl.getUmlHelper().deleteCollection(
                ((org.omg.uml.UmlPackage) me.refOutermostPackage())
                        .getModelManagement()
                        .getAImportedElementElementImport()
                        .getElementImport(me));


    }

    /**
     * A namespace deletes its owned elements.
     *
     * @param elem
     *            is the namespace.
     */
    void deleteNamespace(Object elem) {
        LOG.log(Level.FINE, "Deleting namespace {0}", elem);

        if (!(elem instanceof Namespace)) {
            throw new IllegalArgumentException("elem: " + elem);
        }

        List<ModelElement> ownedElements = new ArrayList<ModelElement>();
        // TODO: This is a composite association, so these will get deleted
        // automatically.  The only thing we need to do is check for any
        // additional elements that need to be deleted as a result.
        ownedElements.addAll(((Namespace) elem).getOwnedElement());
        for (ModelElement element : ownedElements) {

            LOG.log(Level.FINE, "Deleting ownedElement {0}", element);

            modelImpl.getUmlFactory().delete(element);
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
        modelImpl.getUmlHelper().deleteCollection(
                ((org.omg.uml.UmlPackage) oper.refOutermostPackage())
                        .getCommonBehavior().getACallActionOperation()
                        .getCallAction(oper));
        // delete CallEvents which have this as their operation
        modelImpl.getUmlHelper().deleteCollection(
                ((org.omg.uml.UmlPackage) oper.refOutermostPackage())
                        .getStateMachines().getAOccurrenceOperation()
                        .getOccurrence(oper));
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
    public UmlClass copyClass(Object source, Object ns) {
        if (!(source instanceof UmlClass && ns instanceof Namespace)) {
            throw new IllegalArgumentException("source: " + source + ",ns: "
                    + ns);
        }

        UmlClass c = createClass();
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
    public Feature copyFeature(Object source, Object classifier) {
        if (!(source instanceof Feature && classifier instanceof Classifier)) {
            throw new IllegalArgumentException("source: " + source
                    + ",classifier: " + classifier);
        }

        Feature f = null;
        if (source instanceof Attribute) {
            Attribute attr = createAttribute();
            doCopyAttribute((Attribute) source, attr);
            f = attr;
        } else if (source instanceof Operation) {
            Operation oper = createOperation();
            doCopyOperation((Operation) source, oper);
            // TODO: build a return parameter
            f = oper;
        } else if (source instanceof Method) {
            Method method = createMethod();
            doCopyMethod((Method) source, method);
            f = method;
        } else if (source instanceof Reception) {
            Reception reception = (Reception)
                modelImpl.getCommonBehaviorFactory().createReception();
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
    public DataType copyDataType(Object source, Object ns) {
        if (!(source instanceof DataType)) {
            throw new IllegalArgumentException();
        }

        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException();
        }

        DataType i = createDataType();
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
    public Interface copyInterface(Object source, Object ns) {
        if (!(source instanceof Interface)) {
            throw new IllegalArgumentException();
        }

        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException();
        }

        Interface i = createInterface();
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
        for (Feature f : ((Classifier) source).getFeature()) {
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
        modelImpl.getExtensionMechanismsFactory()
                .copyTaggedValues(source, target);

        if (!sourceME.getStereotype().isEmpty()) {
            // Note that if we're copying this element then we
            // must also be allowed to copy other necessary
            // objects.
            for (Stereotype s : sourceME.getStereotype()) {
                targetME.getStereotype().add(s);
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
        // TODO: Delete old multiplicity? Why is "copy" using hard coded value? - tfm
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
        ProcedureExpression oldPe = target.getBody();
        if (!equal(oldPe,pe)) {
            target.setBody((ProcedureExpression)
                    modelImpl.getDataTypesFactory().createProcedureExpression(
                            pe.getLanguage(), pe.getBody()));
            if (oldPe != null) {
                Model.getUmlFactory().delete(oldPe);
            }
        }

        doCopyBehavioralFeature(source, target);
    }


    private boolean equal(Expression expr1, Expression expr2) {
        if (expr1 == null) {
            if (expr2 == null) {
                return true;
            } else {
                return false;
            }
        } else {
            return expr1.equals(expr2);
        }
    }

    /**
     * Copy the attributes of one Reception to another.
     *
     * @param source the reception to copy attributes from
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
        Collection<Signal> raisedSignals = ((org.omg.uml.UmlPackage) source
                .refOutermostPackage()).getCommonBehavior()
                .getAContextRaisedSignal().getRaisedSignal(source);
        for (Signal signal : raisedSignals) {
            ((org.omg.uml.UmlPackage) source.refOutermostPackage())
                    .getCommonBehavior().getAContextRaisedSignal().add(target,
                            signal);
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
