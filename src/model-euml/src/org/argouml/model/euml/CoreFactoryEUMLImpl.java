// $Id$
// Copyright (c) 2007, The ArgoUML Project
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above copyright
//       notice, this list of conditions and the following disclaimer in the
//       documentation and/or other materials provided with the distribution.
//     * Neither the name of the ArgoUML Project nor the
//       names of its contributors may be used to endorse or promote products
//       derived from this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE ArgoUML PROJECT ``AS IS'' AND ANY
// EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE ArgoUML PROJECT BE LIABLE FOR ANY
// DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.argouml.model.euml;

import java.util.List;

import org.argouml.model.AbstractModelFactory;
import org.argouml.model.CoreFactory;
import org.argouml.model.NotImplementedException;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.uml2.uml.Abstraction;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Artifact;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.BehavioralFeature;
import org.eclipse.uml2.uml.BehavioredClassifier;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.TemplateParameter;
import org.eclipse.uml2.uml.TemplateParameterSubstitution;
import org.eclipse.uml2.uml.TemplateSignature;
import org.eclipse.uml2.uml.TemplateableElement;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.VisibilityKind;

/**
 * The implementation of the CoreFactory for EUML2.
 */
class CoreFactoryEUMLImpl implements CoreFactory, AbstractModelFactory {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    private EditingDomain editingDomain;

    /**
     * Constructor.
     * 
     * @param implementation
     *                The ModelImplementation.
     */
    public CoreFactoryEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
        editingDomain = implementation.getEditingDomain();
    }

    public Abstraction buildAbstraction(final String name,
            final Object supplier, final Object client) {
        if (!(client instanceof NamedElement)
                || !(supplier instanceof NamedElement)) {
            throw new IllegalArgumentException(
                    "The client and the supplier " + //$NON-NLS-1$
                    "must be NamedElements."); //$NON-NLS-1$
        }
        if (((NamedElement) client).getNearestPackage() == null) {
            throw new NullPointerException(
                    "The containing package of the client " + //$NON-NLS-1$
                    "must be non-null."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Abstraction abstraction = createAbstraction();
                if (name != null) {
                    abstraction.setName(name);
                }
                abstraction.getSuppliers().add((NamedElement) supplier);
                abstraction.getClients().add((NamedElement) client);
                ((NamedElement) client).getNearestPackage()
                        .getPackagedElements().add(abstraction);
                getParams().add(abstraction);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run,
                "Create the abstraction # between " + //$NON-NLS-1$
                "the client # and the supplier #"); //$NON-NLS-1$
        editingDomain.getCommandStack().execute(cmd);
        cmd.setObjects(run.getParams().get(0), client, supplier);

        return (Abstraction) run.getParams().get(0);
    }

    private Association buildAssociation(final Object type1,
            final Boolean navigability1, final Object aggregationKind1,
            final Object type2, final Boolean navigability2,
            final Object aggregationKind2, final String associationName) {
        if (!(type1 instanceof Type) || !(type2 instanceof Type)) {
            throw new IllegalArgumentException(
                    "The types must be instances of Type."); //$NON-NLS-1$
        }
        if ((aggregationKind1 != null 
                    && !(aggregationKind1 instanceof AggregationKind))
                || (aggregationKind2 != null 
                        && !(aggregationKind2 instanceof AggregationKind))) {
            throw new IllegalArgumentException(
                    "The aggregations of the association ends" + //$NON-NLS-1$
                    " must be instances of AggregationKind."); //$NON-NLS-1$
        }
        if (((Type) type1).getNearestPackage() == null) {
            throw new NullPointerException(
                    "The containing package of the type1" + //$NON-NLS-1$
                    " must be non-null."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Association association = createAssociation();
                Property property1 = createAssociationEnd();
                Property property2 = createAssociationEnd();
                property1.setType((Type) type2);
                property2.setType((Type) type1);
                property1.setAssociation(association);
                property2.setAssociation(association);
                if (aggregationKind1 != null) {
                    property1.setAggregation(
                            (AggregationKind) aggregationKind1);
                }
                if (aggregationKind2 != null) {
                    property2.setAggregation(
                            (AggregationKind) aggregationKind2);
                }
                if (associationName != null) {
                    association.setName(associationName);
                }
                if (UMLUtil.getOwnedAttributes((Type) type1) == null) {
                    association.getOwnedEnds().add(property1);
                } else {
                    UMLUtil.getOwnedAttributes((Type) type1).add(property1);
                }
                if (UMLUtil.getOwnedAttributes((Type) type2) == null) {
                    association.getOwnedEnds().add(property2);
                } else {
                    UMLUtil.getOwnedAttributes((Type) type2).add(property2);
                }
                if (navigability1 != null) {
                    property1.setIsNavigable(navigability1);
                }
                if (navigability2 != null) {
                    property2.setIsNavigable(navigability2);
                }
                ((Type) type1).getNearestPackage().getPackagedElements().add(
                        association);
                getParams().add(association);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run, "Create the association # between # and #");
        editingDomain.getCommandStack().execute(cmd);
        cmd.setObjects(run.getParams().get(0), type1, type2);

        return (Association) run.getParams().get(0);
    }

    public Association buildAssociation(Object fromClassifier,
            Object aggregationKind1, Object toClassifier,
            Object aggregationKind2, Boolean unidirectional) {
        if (unidirectional != null) {
            return buildAssociation(
                    fromClassifier, true, aggregationKind1, toClassifier,
                    !unidirectional, aggregationKind2, null);
        } else {
            return buildAssociation(
                    fromClassifier, null, aggregationKind1, toClassifier, null,
                    aggregationKind2, null);
        }
    }

    public Association buildAssociation(Object classifier1, 
            Object classifier2) {
        return buildAssociation(
                classifier1, null, null, classifier2, null, null, null);
    }

    public Association buildAssociation(Object c1, boolean nav1, Object c2,
            boolean nav2, String name) {
        return buildAssociation(c1, nav1, null, c2, nav2, null, name);
    }

    public AssociationClass buildAssociationClass(final Object end1,
            final Object end2) {
        if (!(end1 instanceof Type) || !(end2 instanceof Type)) {
            throw new IllegalArgumentException(
                    "end1 and end2 must be instances of Type"); //$NON-NLS-1$
        }
        if (((Type) end1).getNearestPackage() == null) {
            throw new NullPointerException(
                    "The containing package of " + //$NON-NLS-1$
                    "the end1 must be non-null."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                AssociationClass associationClass = createAssociationClass();
                Property property1 = createAssociationEnd();
                Property property2 = createAssociationEnd();
                property1.setType((Type) end2);
                property2.setType((Type) end1);
                property1.setAssociation(associationClass);
                property2.setAssociation(associationClass);
                ((Type) end1).getNearestPackage().getPackagedElements().add(
                        associationClass);
                if (UMLUtil.getOwnedAttributes((Type) end1) == null) {
                    associationClass.getOwnedAttributes().add(property1);
                } else {
                    UMLUtil.getOwnedAttributes((Type) end1).add(property1);
                }
                if (UMLUtil.getOwnedAttributes((Type) end2) == null) {
                    associationClass.getOwnedAttributes().add(property2);
                } else {
                    UMLUtil.getOwnedAttributes((Type) end2).add(property2);
                }
                getParams().add(associationClass);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run,
                "Create the association class # between # and #");
        editingDomain.getCommandStack().execute(cmd);
        cmd.setObjects(run.getParams().get(0), end1, end2);

        return (AssociationClass) run.getParams().get(0);
    }

    private Property buildAssociationEnd(final Object assoc, final String name,
            final Object type, final Object multi, final Object stereo,
            final Boolean navigable, final Object order,
            final Object aggregation, final Object scope,
            final Object changeable, final Object visibility) {
        // The attribute 'targetScope' of an AssociationEnd in UML1.x is no
        // longer supported in UML2.x
        if (!(assoc instanceof Association)) {
            throw new IllegalArgumentException(
                    "The assoc must be instance of Association."); //$NON-NLS-1$
        }
        if (!(type instanceof Type)) {
            throw new IllegalArgumentException(
                    "The type of the property " + //$NON-NLS-1$
                    "must be instance of Type."); //$NON-NLS-1$
        }
        if (aggregation != null && !(aggregation instanceof AggregationKind)) {
            throw new IllegalArgumentException(
                    "The aggregation of the property " + //$NON-NLS-1$
                    "must be instance of AggregationKind."); //$NON-NLS-1$
        }
        if (visibility != null && !(visibility instanceof VisibilityKind)) {
            throw new IllegalArgumentException(
                    "The visibility of the property must" + //$NON-NLS-1$
                    " be instance of VisibilityKind."); //$NON-NLS-1$
        }
        if (multi != null && !(multi instanceof MultiplicityElement)) {
            throw new IllegalArgumentException(
                    "The multilicity of the property must" + //$NON-NLS-1$
                    " be instance of MultiplicityElement."); //$NON-NLS-1$
        }
        if ((order != null && !(order instanceof Boolean))
                || (changeable != null && !(changeable instanceof Boolean))) {
            throw new IllegalArgumentException(
                    "The isOrdered, isReadOnly attributes of " + //$NON-NLS-1$
                    "the property must be instances of Boolean."); //$NON-NLS-1$
        }
        if (stereo != null && !(stereo instanceof Stereotype)) {
            throw new IllegalArgumentException(
                    "stereo must be instance of Stereotype."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Property property = createAssociationEnd();
                property.setType((Type) type);
                property.setAssociation((Association) assoc);
                if (name != null) {
                    property.setName(name);
                }
                if (navigable != null) {
                    property.setIsNavigable(navigable);
                    if (!(Boolean) navigable) {
                        ((Association) assoc).getOwnedEnds().add(property);
                    }
                }
                if (aggregation != null) {
                    property.setAggregation((AggregationKind) aggregation);
                }
                if (visibility != null) {
                    property.setVisibility((VisibilityKind) visibility);
                }
                if (multi != null) {
                    if (((MultiplicityElement) multi).getLowerValue() != null) {
                        property.setLowerValue(
                                ((MultiplicityElement) multi).getLowerValue());
                    }
                    if (((MultiplicityElement) multi).getUpperValue() != null) {
                        property.setLowerValue(
                                ((MultiplicityElement) multi).getUpperValue());
                    }
                }
                if (order != null) {
                    property.setIsOrdered((Boolean) order);
                }
                if (changeable != null) {
                    property.setIsReadOnly((Boolean) changeable);
                }
                if (stereo != null) {
                    if (property.isStereotypeApplicable((Stereotype) stereo)) {
                        property.applyStereotype((Stereotype) stereo);
                    } else {
                        return;
                    }
                }
                getParams().add(property);
            }
        };
        modelImpl.getModelEventPump().getRootContainer().setHoldEvents(true);
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run,
                "Create the association end # of the association #");
        editingDomain.getCommandStack().execute(cmd);
        if (run.getParams().isEmpty()) {
            editingDomain.getCommandStack().undo();
            editingDomain.getCommandStack().flush();
            modelImpl.getModelEventPump().getRootContainer().clearHeldEvents();
            modelImpl.getModelEventPump().getRootContainer().setHoldEvents(
                    false);
            throw new UnsupportedOperationException(
                    "This stereotype cannot be applied " + //$NON-NLS-1$
                    "to the association end."); //$NON-NLS-1$
        }
        cmd.setObjects(run.getParams().get(0), assoc);
        modelImpl.getModelEventPump().getRootContainer().setHoldEvents(false);

        return (Property) run.getParams().get(0);
    }

    public Property buildAssociationEnd(Object assoc, String name, Object type,
            Object multi, Object stereo, boolean navigable, Object order,
            Object aggregation, Object scope, Object changeable,
            Object visibility) {
        return buildAssociationEnd(
                assoc, name, type, multi, stereo, navigable, order,
                aggregation, scope, changeable, visibility);
    }

    public Property buildAssociationEnd(Object type, Object assoc) {
        return buildAssociationEnd(
                assoc, null, type, null, null, null, null, null, null, null,
                null);
    }

    public Property buildAttribute(Object model, Object type) {
        return buildAttribute2(type);
    }

    public Property buildAttribute2(Object type) {
        if (!(type instanceof Type)) {
            throw new IllegalArgumentException(
                    "The type of the attribute must" + //$NON-NLS-1$
                    " be instance of Type."); //$NON-NLS-1$
        }
        Property property = createAttribute();
        property.setType((Type) type);
        return property;
    }

    @SuppressWarnings("deprecation")
    public Property buildAttribute(Object handle, Object model, Object type) {
        return buildAttribute2(handle, type);
    }

    public Property buildAttribute2(final Object handle, final Object type) {
        if (!(handle instanceof Type) || !(type instanceof Type)) {
            throw new IllegalArgumentException(
                    "handle and type must be instances of Type."); //$NON-NLS-1$
        }
        if (UMLUtil.getOwnedAttributes((Type) handle) == null) {
            throw new UnsupportedOperationException(
                    "The type " + handle.getClass()  //$NON-NLS-1$
                    + " does not support owning attributes."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Property property = createAttribute();
                UMLUtil.getOwnedAttributes((Type) handle).add(property);
                property.setType((Type) type);
                property.setName("newAttr");
                getParams().add(property);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run, "Create the attribute # of the type #");
        editingDomain.getCommandStack().execute(cmd);
        cmd.setObjects(run.getParams().get(0), handle);

        return (Property) run.getParams().get(0);
    }

    /**
     * Removed from UML2.x, use buildTemplateBinding instead.
     */
    @Deprecated
    public Object buildBinding(Object client, Object supplier, List arguments) {
        return buildTemplateBinding(client, supplier, arguments);
    }

    public TemplateBinding buildTemplateBinding(final Object client,
            final Object supplier, final List arguments) {
        // TODO: Is it appropriate the TemplateableElement as the client and a
        // list of TemplateParameterSubstitution as the list of parameters?
        if (!(client instanceof TemplateableElement)) {
            throw new IllegalArgumentException(
                    "The supplier must be instance of " + //$NON-NLS-1$
                    "TemplateableElement."); //$NON-NLS-1$
        }
        if (!(supplier instanceof TemplateSignature)) {
            throw new IllegalArgumentException(
                    "The supplier must be instance of " + //$NON-NLS-1$
                    "TemplateSignature."); //$NON-NLS-1$
        }
        if (arguments != null) {
            for (Object o : arguments) {
                if (!(o instanceof TemplateParameterSubstitution)) {
                    throw new IllegalArgumentException(
                            "The list of arguments must be instances" + //$NON-NLS-1$
                            " of TemplateParameterSubstitutions."); //$NON-NLS-1$
                }
            }
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                TemplateBinding templateBinding = createTemplateBinding();
                templateBinding.setBoundElement((TemplateableElement) client);
                templateBinding.setSignature((TemplateSignature) supplier);
                if (arguments != null) {
                    for (Object o : arguments) {
                        templateBinding.getParameterSubstitutions().add(
                                (TemplateParameterSubstitution) o);
                    }
                }
                getParams().add(templateBinding);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run,
                "Create the template binding # between "
                        + "the client # and the supplier #");
        editingDomain.getCommandStack().execute(cmd);
        cmd.setObjects(run.getParams().get(0), client, supplier);

        return (TemplateBinding) run.getParams().get(0);
    }

    public org.eclipse.uml2.uml.Class buildClass() {
        return createClass();
    }

    public org.eclipse.uml2.uml.Class buildClass(Object owner) {
        return buildClass(null, owner);
    }

    public org.eclipse.uml2.uml.Class buildClass(String name) {
        org.eclipse.uml2.uml.Class clazz = createClass();
        if (name != null) {
            clazz.setName(name);
        }
        return clazz;
    }

    public org.eclipse.uml2.uml.Class buildClass(final String name,
            final Object owner) {
        if (!(owner instanceof org.eclipse.uml2.uml.Package)
                && !(owner instanceof org.eclipse.uml2.uml.Class)
                && !(owner instanceof Interface)) {
            throw new IllegalArgumentException(
                    "The owner must be instance of Package" + //$NON-NLS-1$
                    " or UML2 Class or Interface."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                org.eclipse.uml2.uml.Class clazz = createClass();
                if (name != null) {
                    clazz.setName(name);
                }
                if (owner instanceof org.eclipse.uml2.uml.Package) {
                    clazz.setPackage((org.eclipse.uml2.uml.Package) owner);
                } else if (owner instanceof org.eclipse.uml2.uml.Class) {
                    ((org.eclipse.uml2.uml.Class) owner).getNestedClassifiers()
                            .add(clazz);
                } else if (owner instanceof Interface) {
                    ((Interface) owner).getNestedClassifiers().add(clazz);
                }
                getParams().add(clazz);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run, "Create the class # of the owner #");
        editingDomain.getCommandStack().execute(cmd);
        cmd.setObjects(run.getParams().get(0), owner);

        return (org.eclipse.uml2.uml.Class) run.getParams().get(0);
    }

    public Comment buildComment(final Object element, final Object model) {
        if (!(model instanceof Namespace)) {
            throw new IllegalArgumentException(
                    "A namespace must be supplied."); //$NON-NLS-1$
        }
        if (element != null && !(element instanceof Element)) {
            throw new IllegalArgumentException(
                    "The annotated element must be instance of Element."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Comment comment = createComment();
                if (element != null) {
                    comment.getAnnotatedElements().add((Element) element);
                }
                ((Namespace) model).getOwnedComments().add(comment);
                getParams().add(comment);
            }
        };
        ChangeCommand cmd;
        if (element == null) {
            cmd = new ChangeCommand(modelImpl, run, "Create the comment #");
        } else {
            cmd = new ChangeCommand(
                    modelImpl, run,
                    "Create the comment # attached to the element #");
        }
        editingDomain.getCommandStack().execute(cmd);
        if (element == null) {
            cmd.setObjects(run.getParams().get(0));
        } else {
            cmd.setObjects(run.getParams().get(0), element);
        }

        return (Comment) run.getParams().get(0);
    }

    public Constraint buildConstraint(final Object constrElement) {
        if (!(constrElement instanceof Element)) {
            throw new IllegalArgumentException(
                    "The constrained element must be instance of Element."); //$NON-NLS-1$
        }
        if (((Element) constrElement).getNearestPackage() == null) {
            throw new NullPointerException(
                    "The containing package of the constrained" //$NON-NLS-1$
                            + " element must be non-null."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Constraint constraint = createConstraint();
                constraint.getConstrainedElements()
                        .add((Element) constrElement);
                getParams().add(constraint);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run,
                "Create the constraint # that constrains the element #");
        editingDomain.getCommandStack().execute(cmd);
        cmd.setObjects(run.getParams().get(0), constrElement);

        return (Constraint) run.getParams().get(0);
    }

    public Constraint buildConstraint(String name, Object bexpr) {
        // TODO: BooleanExpresion is removed from UML2.x, is it OK to use
        // ValueSpecification?
        if (!(bexpr instanceof ValueSpecification)) {
            throw new IllegalArgumentException(
                    "The 'bexpr' value specification must be " //$NON-NLS-1$
                            + "instance of ValueSpecification"); //$NON-NLS-1$
        }
        Constraint constraint = createConstraint();
        if (name != null) {
            constraint.setName(name);
        }
        constraint.setSpecification((ValueSpecification) bexpr);
        return constraint;
    }

    public DataType buildDataType(final String name, final Object owner) {
        if (!(owner instanceof org.eclipse.uml2.uml.Package)
                && !(owner instanceof org.eclipse.uml2.uml.Class)
                && !(owner instanceof Interface)) {
            throw new IllegalArgumentException(
                    "The owner must be instance of Package" //$NON-NLS-1$
                            + " or UML2 Class or Interface."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                DataType dataType = createDataType();
                if (name != null) {
                    dataType.setName(name);
                }
                if (owner instanceof org.eclipse.uml2.uml.Package) {
                    dataType.setPackage((org.eclipse.uml2.uml.Package) owner);
                } else if (owner instanceof org.eclipse.uml2.uml.Class) {
                    ((org.eclipse.uml2.uml.Class) owner).getNestedClassifiers()
                            .add(dataType);
                } else if (owner instanceof Interface) {
                    ((Interface) owner).getNestedClassifiers().add(dataType);
                }
                getParams().add(dataType);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run, "Create the data type # owned by #");
        editingDomain.getCommandStack().execute(cmd);
        cmd.setObjects(run.getParams().get(0), owner);

        return (DataType) run.getParams().get(0);
    }

    public Dependency buildDependency(final Object clientObj,
            final Object supplierObj) {
        if (!(clientObj instanceof NamedElement)
                || !(supplierObj instanceof NamedElement)) {
            throw new IllegalArgumentException(
                    "The client and the supplier must be" //$NON-NLS-1$
                            + " instances of NamedElement."); //$NON-NLS-1$
        }
        if (((NamedElement) clientObj).getNearestPackage() == null) {
            throw new NullPointerException(
                    "The containing package of the client must be non-null."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Dependency dependency = createDependency();
                dependency.getClients().add((NamedElement) clientObj);
                dependency.getSuppliers().add((NamedElement) supplierObj);
                ((NamedElement) clientObj).getNearestPackage()
                        .getPackagedElements().add(dependency);
                getParams().add(dependency);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run, "Create the dependency # between the"
                        + " client # and the supplier #");
        editingDomain.getCommandStack().execute(cmd);
        cmd.setObjects(run.getParams().get(0), clientObj, supplierObj);

        return (Dependency) run.getParams().get(0);
    }

    public Object buildElementResidence(Object me, Object component) {
        // TODO: Is this removed from UML2 ?
        throw new NotImplementedException();
    }

    public Enumeration buildEnumeration(final String name, final Object owner) {
        if (!(owner instanceof org.eclipse.uml2.uml.Package)
                && !(owner instanceof org.eclipse.uml2.uml.Class)
                && !(owner instanceof Interface)) {
            throw new IllegalArgumentException(
                    "The owner must be instance of Package or UML2 Class or Interface."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Enumeration enumeration = createEnumeration();
                if (name != null) {
                    enumeration.setName(name);
                }
                if (owner instanceof org.eclipse.uml2.uml.Package) {
                    enumeration.setPackage(
                            (org.eclipse.uml2.uml.Package) owner);
                } else if (owner instanceof org.eclipse.uml2.uml.Class) {
                    ((org.eclipse.uml2.uml.Class) owner).getNestedClassifiers()
                            .add(enumeration);
                } else if (owner instanceof Interface) {
                    ((Interface) owner).getNestedClassifiers().add(enumeration);
                }
                getParams().add(enumeration);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run, "Create the enumeration # owned by #");
        editingDomain.getCommandStack().execute(cmd);
        cmd.setObjects(run.getParams().get(0), owner);

        return (Enumeration) run.getParams().get(0);
    }

    public EnumerationLiteral buildEnumerationLiteral(final String name,
            final Object enumeration) {
        if (!(enumeration instanceof Enumeration)) {
            throw new IllegalArgumentException(
                    "The enumeration must be instance of Enumeration."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                EnumerationLiteral enumerationLiteral = 
                    createEnumerationLiteral();
                if (name != null) {
                    enumerationLiteral.setName(name);
                }
                enumerationLiteral.setEnumeration((Enumeration) enumeration);
                getParams().add(enumerationLiteral);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run, "Create the enumeration literal # owned by #");
        editingDomain.getCommandStack().execute(cmd);
        cmd.setObjects(run.getParams().get(0), enumeration);

        return (EnumerationLiteral) run.getParams().get(0);
    }

    @SuppressWarnings("deprecation")
    public Generalization buildGeneralization(Object child, Object parent,
            String name) {
        // Generalizations are unnamed in UML 2.x
        return buildGeneralization(parent, child);
    }

    public Generalization buildGeneralization(final Object child,
            final Object parent) {
        if (!(child instanceof Classifier) || !(parent instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "The general (the parent) and the specific (the child) must be instances of Classifier."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Generalization generalization = createGeneralization();
                generalization.setGeneral((Classifier) parent);
                generalization.setSpecific((Classifier) child);
                getParams().add(generalization);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run,
                "Create the generalization # between # (general) and # (specific)");
        editingDomain.getCommandStack().execute(cmd);
        cmd.setObjects(run.getParams().get(0), parent, child);

        return (Generalization) run.getParams().get(0);
    }

    public Interface buildInterface() {
        return createInterface();
    }

    public Interface buildInterface(Object owner) {
        return buildInterface(null, owner);
    }

    public Interface buildInterface(String name) {
        Interface interfaze = createInterface();
        if (name != null) {
            interfaze.setName(name);
        }
        return interfaze;
    }

    public Interface buildInterface(final String name, final Object owner) {
        if (!(owner instanceof org.eclipse.uml2.uml.Package)
                && !(owner instanceof org.eclipse.uml2.uml.Class)
                && !(owner instanceof Interface)) {
            throw new IllegalArgumentException(
                    "The owner must be instance of Package" + //$NON-NLS-1$
                    " or UML2 Class or Interface."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Interface interfaze = createInterface();
                if (owner instanceof org.eclipse.uml2.uml.Package) {
                    interfaze.setPackage((org.eclipse.uml2.uml.Package) owner);
                } else if (owner instanceof org.eclipse.uml2.uml.Class) {
                    ((org.eclipse.uml2.uml.Class) owner).getNestedClassifiers()
                            .add(interfaze);
                } else if (owner instanceof Interface) {
                    ((Interface) owner).getNestedClassifiers().add(interfaze);
                }
                if (name != null) {
                    interfaze.setName(name);
                }
                getParams().add(interfaze);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run, "Create the interface # owned by #");
        editingDomain.getCommandStack().execute(cmd);
        cmd.setObjects(run.getParams().get(0), owner);

        return (Interface) run.getParams().get(0);
    }

    public Object buildMethod(String name) {
        // TODO: Is this removed from UML2 ?
        throw new NotImplementedException();
    }

    @SuppressWarnings("deprecation")
    public Operation buildOperation(Object classifier, Object model,
            Object returnType) {
        return buildOperation2(classifier, returnType, null);
    }

    public Operation buildOperation(Object classifier, Object returnType) {
        return buildOperation2(classifier, returnType, null);
    }

    @SuppressWarnings("deprecation")
    public Operation buildOperation(Object cls, Object model,
            Object returnType, String name) {
        return buildOperation2(cls, returnType, name);
    }

    public Operation buildOperation2(final Object cls, final Object returnType,
            final String name) {
        if ((returnType != null && !(returnType instanceof Type))
                || !(cls instanceof Type)) {
            throw new IllegalArgumentException(
                    "cls and returnType must be instances of Type."); //$NON-NLS-1$
        }
        if (UMLUtil.getOwnedOperations((Type) cls) == null) {
            throw new UnsupportedOperationException(
                    "The type " + cls.getClass()  //$NON-NLS-1$
                    + " does not support owning operations."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Operation operation = createOperation();
                UMLUtil.getOwnedOperations((Type) cls).add(operation);
                operation.createReturnResult(null, (Type) returnType);
                if (name != null) {
                    operation.setName(name);
                }
                getParams().add(operation);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run, "Create the operation # owned by #");
        editingDomain.getCommandStack().execute(cmd);
        cmd.setObjects(run.getParams().get(0), cls);

        return (Operation) run.getParams().get(0);
    }

    @SuppressWarnings("deprecation")
    public Parameter buildParameter(Object o, Object model, Object type) {
        return buildParameter(o, type);
    }

    public Parameter buildParameter(final Object o, final Object type) {
        // TODO: In UML2.x Event has no parameters. The Event metaclass in
        // UML1.x corresponds to the Trigger metaclass in UML2.x (see UML
        // Superstructure page 456).
        if (!(o instanceof BehavioralFeature)) {
            throw new IllegalArgumentException(
                    "The parameter must be attached to a BehavioralFeature."); //$NON-NLS-1$
        }
        if (!(type instanceof Type)) {
            throw new IllegalArgumentException(
                    "The type of the parameter must be instance of Type."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Parameter param = createParameter();
                param.setType((Type) type);
                ((BehavioralFeature) o).getOwnedParameters().add(param);
                getParams().add(param);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run, "Create the parameter # owned by #");
        editingDomain.getCommandStack().execute(cmd);
        cmd.setObjects(run.getParams().get(0), o);

        return (Parameter) run.getParams().get(0);
    }

    /**
     * Removed from UML2.x, use buildPackageImport instead.
     */
    @Deprecated
    public PackageImport buildPermission(Object client, Object supplier) {
        return buildPackageImport(client, supplier);
    }

    public PackageImport buildPackageAccess(Object client, Object supplier) {
        return buildPackageImport(
                client, supplier, VisibilityKind.PRIVATE_LITERAL);
    }

    public PackageImport buildPackageImport(Object client, Object supplier) {
        return buildPackageImport(client, supplier, null);
    }

    private PackageImport buildPackageImport(final Object client,
            final Object supplier, final VisibilityKind visibility) {
        if (!(client instanceof Namespace)) {
            throw new IllegalArgumentException(
                    "The client must be instance of Namespace."); //$NON-NLS-1$
        }
        if (!(supplier instanceof org.eclipse.uml2.uml.Package)) {
            throw new IllegalArgumentException(
                    "The supplier must be instance of Package."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                PackageImport packageImport = createPackageImport();
                packageImport.setImportedPackage((org.eclipse.uml2.uml.Package) supplier);
                packageImport.setImportingNamespace((Namespace) client);
                if (visibility != null) {
                    packageImport.setVisibility(visibility);
                }
                getParams().add(packageImport);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run,
                "Create the package import # between the client # and the supplier #");
        editingDomain.getCommandStack().execute(cmd);
        cmd.setObjects(run.getParams().get(0), client, supplier);

        return (PackageImport) run.getParams().get(0);
    }

    public InterfaceRealization buildRealization(final Object client,
            final Object supplier, Object namespace) {
        // The interface realization will be created in the client namespace
        // (client is a namespace)
        if (!(client instanceof BehavioredClassifier)) {
            throw new IllegalArgumentException(
                    "The client must be instance of BehavioredClassifier"); //$NON-NLS-1$
        }
        if (!(supplier instanceof Interface)) {
            throw new IllegalArgumentException(
                    "The supplier must be an Interface"); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                InterfaceRealization realization = 
                    UMLFactory.eINSTANCE.createInterfaceRealization();
                realization.setImplementingClassifier(
                        (BehavioredClassifier) client);
                realization.setContract((Interface) supplier);
                ((BehavioredClassifier) client).getInterfaceRealizations()
                        .add(realization);
                getParams().add(realization);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run,
                "Create the interface realization # between" //$NON-NLS-1$
                        + " the client # and the supplier #"); //$NON-NLS-1$
        editingDomain.getCommandStack().execute(cmd);
        cmd.setObjects(run.getParams().get(0), client, supplier);

        return (InterfaceRealization) run.getParams().get(0);
    }

    public Object buildTemplateArgument(Object element) {
        // TODO: Is this removed from UML2 ?
        throw new NotImplementedException();
    }

    public Usage buildUsage(final Object client, final Object supplier) {
        if (!(client instanceof NamedElement)
                || !(supplier instanceof NamedElement)) {
            throw new IllegalArgumentException(
                    "The client and the supplier must be NamedElements."); //$NON-NLS-1$
        }
        if (((NamedElement) client).getNearestPackage() == null) {
            throw new NullPointerException(
                    "The client is not contained in a package."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Usage usage = createUsage();
                usage.getClients().add((NamedElement) client);
                usage.getSuppliers().add((NamedElement) supplier);
                ((NamedElement) client).getNearestPackage()
                        .getPackagedElements().add(usage);
                getParams().add(usage);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run,
                "Create the usage # between the client # and the supplier #");
        editingDomain.getCommandStack().execute(cmd);
        cmd.setObjects(run.getParams().get(0), client, supplier);

        return (Usage) run.getParams().get(0);
    }

    public Object copyClass(Object source, Object ns) {
        return modelImpl.getCopyHelper().copy(source, ns);
    }

    public Object copyDataType(Object source, Object ns) {
        return modelImpl.getCopyHelper().copy(source, ns);
    }

    public Object copyFeature(Object source, Object classifier) {
        return modelImpl.getCopyHelper().copy(source, classifier);
    }

    public Object copyInterface(Object source, Object ns) {
        return modelImpl.getCopyHelper().copy(source, ns);
    }

    public Abstraction createAbstraction() {
        return UMLFactory.eINSTANCE.createAbstraction();
    }

    public Artifact createArtifact() {
        return UMLFactory.eINSTANCE.createArtifact();
    }

    public Association createAssociation() {
        return UMLFactory.eINSTANCE.createAssociation();
    }

    public AssociationClass createAssociationClass() {
        return UMLFactory.eINSTANCE.createAssociationClass();
    }

    public Property createAssociationEnd() {
        return UMLFactory.eINSTANCE.createProperty();
    }

    public Property createAttribute() {
        return UMLFactory.eINSTANCE.createProperty();
    }

    /**
     * Removed from UML2.x, use createTemplateBinding instead.
     */
    @Deprecated
    public TemplateBinding createBinding() {
        return createTemplateBinding();
    }

    public TemplateBinding createTemplateBinding() {
        return UMLFactory.eINSTANCE.createTemplateBinding();
    }

    public org.eclipse.uml2.uml.Class createClass() {
        return UMLFactory.eINSTANCE.createClass();
    }

    public Comment createComment() {
        return UMLFactory.eINSTANCE.createComment();
    }

    public Component createComponent() {
        return UMLFactory.eINSTANCE.createComponent();
    }

    public Constraint createConstraint() {
        return UMLFactory.eINSTANCE.createConstraint();
    }

    public DataType createDataType() {
        return UMLFactory.eINSTANCE.createDataType();
    }

    public Dependency createDependency() {
        return UMLFactory.eINSTANCE.createDependency();
    }

    public Object createElementResidence() {
        // TODO: Is this removed from UML2 ?
        throw new NotImplementedException();
    }

    public Enumeration createEnumeration() {
        return UMLFactory.eINSTANCE.createEnumeration();
    }

    public EnumerationLiteral createEnumerationLiteral() {
        return UMLFactory.eINSTANCE.createEnumerationLiteral();
    }

    public Object createFlow() {
        // TODO: Is this removed from UML2 ?
        throw new NotImplementedException();
    }

    public Generalization createGeneralization() {
        return UMLFactory.eINSTANCE.createGeneralization();
    }

    public Interface createInterface() {
        return UMLFactory.eINSTANCE.createInterface();
    }

    public Object createMethod() {
        // TODO: Is this removed from UML2 ?
        throw new NotImplementedException();
    }

    public Node createNode() {
        return UMLFactory.eINSTANCE.createNode();
    }

    public Operation createOperation() {
        return UMLFactory.eINSTANCE.createOperation();
    }

    public Parameter createParameter() {
        return UMLFactory.eINSTANCE.createParameter();
    }

    /**
     * Removed from UML2.x, use createPackageImport instead.
     */
    @Deprecated
    public PackageImport createPermission() {
        return createPackageImport();
    }

    public PackageImport createPackageImport() {
        return UMLFactory.eINSTANCE.createPackageImport();
    }

    @SuppressWarnings("deprecation")
    public Object createPrimitive() {
        return createPrimitiveType();
    }

    public PrimitiveType createPrimitiveType() {
        return UMLFactory.eINSTANCE.createPrimitiveType();
    }

    @SuppressWarnings("deprecation")
    public Object createProgrammingLanguageDataType() {
        // Removed from UML 2.x & unused by ArgoUML.
        throw new NotImplementedException();
    }

    public Object createTemplateArgument() {
        // TODO: Is this removed from UML2 ?
        throw new NotImplementedException();
    }

    public TemplateParameter createTemplateParameter() {
        return UMLFactory.eINSTANCE.createTemplateParameter();
    }

    public Usage createUsage() {
        return UMLFactory.eINSTANCE.createUsage();
    }

}
