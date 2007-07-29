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
import org.eclipse.uml2.common.edit.command.ChangeCommand;
import org.eclipse.uml2.uml.Abstraction;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Artifact;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.BehavioralFeature;
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
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.TemplateParameter;
import org.eclipse.uml2.uml.TemplateParameterSubstitution;
import org.eclipse.uml2.uml.TemplateSignature;
import org.eclipse.uml2.uml.TemplateableElement;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.ValueSpecification;


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
     * @param implementation The ModelImplementation.
     */
    public CoreFactoryEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
        editingDomain = implementation.getEditingDomain();
    }

    public Abstraction buildAbstraction(String name, Object supplier,
            Object client) {
        Abstraction abstraction = createAbstraction();
        abstraction.setName(name);
        abstraction.getSuppliers().add((NamedElement) supplier);
        abstraction.getClients().add((NamedElement) client);
        return abstraction;
    }

    Association buildAssociation(final Object type1,
            final Boolean navigability1, final Object aggregationKind1,
            final String name1, final Object type2,
            final Boolean navigability2, final Object aggregationKind2,
            final String name2, final String associationName) {

        RunnableClass run = new RunnableClass() {
            public void run() {
                Association assoc = ((Type) type1).createAssociation(
                        navigability1,
                        aggregationKind1 == null ? AggregationKind.NONE_LITERAL
                                : (AggregationKind) aggregationKind1, name1, 0,
                        1, (Type) type2, navigability2,
                        aggregationKind2 == null ? AggregationKind.NONE_LITERAL
                                : (AggregationKind) aggregationKind2, name2, 0,
                        1);
                for (Property p : assoc.getMemberEnds()) {
                    p.setLowerValue(null);
                    p.setUpperValue(null);
                }
                if (associationName != null) {
                    assoc.setName(associationName);
                }
                getParams().add(assoc);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));

        return (Association) run.getParams().get(0);
    }

    public Association buildAssociation(Object fromClassifier,
            Object aggregationKind1, Object toClassifier,
            Object aggregationKind2, Boolean unidirectional) {

        return buildAssociation(fromClassifier, true, aggregationKind1, null,
                toClassifier, !unidirectional, aggregationKind2, null, null);
    }

    public Association buildAssociation(Object classifier1, 
            Object classifier2) {
        return buildAssociation(classifier1, true,
                AggregationKind.NONE_LITERAL, null, classifier2, true,
                AggregationKind.NONE_LITERAL, null, null);
    }

    public Association buildAssociation(Object c1, boolean nav1, Object c2,
            boolean nav2, String name) {
        return buildAssociation(c1, nav1, AggregationKind.NONE_LITERAL, null,
                c2, nav2, AggregationKind.NONE_LITERAL, null, name);
    }

    public Association buildAssociationClass(Object end1, Object end2) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildAssociationEnd(Object assoc, String name, Object type,
            Object multi, Object stereo, boolean navigable, Object order,
            Object aggregation, Object scope, Object changeable,
            Object visibility) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildAssociationEnd(Object type, Object assoc) {
        // TODO Auto-generated method stub
        return null;
    }

    public Property buildAttribute(Object model, Object type) {
        return buildAttribute2(type);
    }

    public Property buildAttribute2(Object type) {
        if (!(type instanceof Type) || type == null) {
            throw new IllegalArgumentException(
            "The type of the attribute must be instance of Type."); //$NON-NLS-1$
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
        if (!(handle instanceof org.eclipse.uml2.uml.Class) || handle == null) {
            throw new IllegalArgumentException(
                    "The handle must be instance of UML2 Class."); //$NON-NLS-1$
        }
        if (!(type instanceof Type) || type == null) {
            throw new IllegalArgumentException(
                    "The type of the attribute must be instance of Type."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Property property = createAttribute();
                property.setType((Type) type);
                ((org.eclipse.uml2.uml.Class) handle).getOwnedAttributes().add(
                        property);
                getParams().add(property);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));

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
        if (!(client instanceof TemplateableElement) || client == null) {
            throw new IllegalArgumentException(
                    "The supplier must be instance of TemplateableElement."); //$NON-NLS-1$
        }
        if (!(supplier instanceof TemplateSignature) || supplier == null) {
            throw new IllegalArgumentException(
                    "The supplier must be instance of TemplateSignature."); //$NON-NLS-1$
        }
        if (arguments != null) {
            for (Object o : arguments) {
                if (!(o instanceof TemplateParameterSubstitution) || o == null) {
                    throw new IllegalArgumentException(
                            "The list of arguments must be instances of TemplateParameterSubstitutions."); //$NON-NLS-1$
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
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));

        return (TemplateBinding) run.getParams().get(0);
    }

    public org.eclipse.uml2.uml.Class buildClass() {
        return createClass();
    }

    public org.eclipse.uml2.uml.Class buildClass(Object owner) {
        return buildClass(null, owner);
    }

    public org.eclipse.uml2.uml.Class buildClass(String name) {
        org.eclipse.uml2.uml.Class class_ = createClass();
        if (name != null) {
            class_.setName(name);
        }
        return class_;
    }

    public org.eclipse.uml2.uml.Class buildClass(final String name,
            final Object owner) {
        if (!(owner instanceof org.eclipse.uml2.uml.Package) || owner == null) {
            throw new IllegalArgumentException("The owner must be instance of Package."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                org.eclipse.uml2.uml.Class class_ = createClass();
                if (name != null) {
                    class_.setName(name);
                }
                class_.setPackage((org.eclipse.uml2.uml.Package) owner);
                getParams().add(class_);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));

        return (org.eclipse.uml2.uml.Class) run.getParams().get(0);
    }

    public Comment buildComment(final Object element, final Object model) {
        if (!(model instanceof Namespace) || model == null) {
            throw new IllegalArgumentException("A namespace must be supplied."); //$NON-NLS-1$
        }
        if (!(element instanceof Element)) {
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
                getParams().add(element);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));

        return (Comment) run.getParams().get(0);
    }

    public Constraint buildConstraint(final Object constrElement) {
        if (!(constrElement instanceof Element) || constrElement == null) {
            throw new IllegalArgumentException(
                    "The constrained element must be instance of Element."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Constraint constraint = createConstraint();
                constraint.getConstrainedElements().add((Element) constrElement);
                ((Element) constrElement).getNearestPackage().getPackagedElements().add(
                        constraint);
                getParams().add(constraint);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));

        return (Constraint) run.getParams().get(0);
    }

    public Constraint buildConstraint(String name, Object bexpr) {
        // TODO: BooleanExpresion is removed from UML2.x, is it OK to use
        // ValueSpecification?
        if (bexpr == null || !(bexpr instanceof ValueSpecification)) {
            throw new IllegalArgumentException(
                    "The 'bexpr' value specification must be instance of ValueSpecification"); //$NON-NLS-1$
        }
        Constraint constraint = createConstraint();
        if (name != null) {
            constraint.setName(name);
        }
        constraint.setSpecification((ValueSpecification) bexpr);
        return constraint;
    }

    public DataType buildDataType(final String name, final Object owner) {
        if (!(owner instanceof org.eclipse.uml2.uml.Package) || owner == null) {
            throw new IllegalArgumentException(
                    "The owner must be instance of Package."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                DataType dataType = createDataType();
                if (name != null) {
                    dataType.setName(name);
                }
                dataType.setPackage((org.eclipse.uml2.uml.Package) owner);
                getParams().add(dataType);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));

        return (DataType) run.getParams().get(0);
    }

    public Dependency buildDependency(final Object clientObj,
            final Object supplierObj) {
        if (!(clientObj instanceof NamedElement)
                || !(supplierObj instanceof NamedElement) || clientObj == null
                || supplierObj == null) {
            throw new IllegalArgumentException(
                    "The client and the supplier must be instances of NamedElement."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Dependency dependency = createDependency();
                dependency.getClients().add((NamedElement) clientObj);
                dependency.getSuppliers().add((NamedElement) supplierObj);
                ((NamedElement) clientObj).getNearestPackage().getPackagedElements().add(
                        dependency);
                getParams().add(dependency);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));

        return (Dependency) run.getParams().get(0);
    }

    public Object buildElementResidence(Object me, Object component) {
        // TODO Is it removed from UML2 ?
        throw new NotImplementedException();
    }

    public Enumeration buildEnumeration(final String name, final Object owner) {
        if (!(owner instanceof org.eclipse.uml2.uml.Package) || owner == null) {
            throw new IllegalArgumentException(
                    "The owner must be instance of Package."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Enumeration enumeration = createEnumeration();
                if (name != null) {
                    enumeration.setName(name);
                }
                enumeration.setPackage((org.eclipse.uml2.uml.Package) owner);
                getParams().add(enumeration);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));

        return (Enumeration) run.getParams().get(0);
    }

    public EnumerationLiteral buildEnumerationLiteral(final String name,
            final Object enumeration) {
        if (!(enumeration instanceof Enumeration) || enumeration == null) {
            throw new IllegalArgumentException(
                    "The enumeration must be instance of Enumeration."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                EnumerationLiteral enumerationLiteral = createEnumerationLiteral();
                if (name != null) {
                    enumerationLiteral.setName(name);
                }
                enumerationLiteral.setEnumeration((Enumeration) enumeration);
                getParams().add(enumerationLiteral);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));

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
        if (!(child instanceof Classifier) || !(parent instanceof Classifier)
                || child == null || parent == null) {
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
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));

        return (Generalization) run.getParams().get(0);
    }

    public Interface buildInterface() {
        return createInterface();
    }

    public Interface buildInterface(Object owner) {
        return buildInterface(null, owner);
    }

    public Interface buildInterface(String name) {
        Interface interface_ = createInterface();
        if (name != null) {
            interface_.setName(name);
        }
        return interface_;
    }

    public Interface buildInterface(final String name, final Object owner) {
        if (!(owner instanceof org.eclipse.uml2.uml.Package) || owner == null) {
            throw new IllegalArgumentException(
                    "The owner must be instance of Package."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Interface interface_ = createInterface();
                interface_.setPackage((org.eclipse.uml2.uml.Package) owner);
                if (name != null) {
                    interface_.setName(name);
                }
                getParams().add(interface_);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));

        return (Interface) run.getParams().get(0);
    }

    public Object buildMethod(String name) {
        // TODO Is it removed from UML2 ?
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
        if (!(cls instanceof org.eclipse.uml2.uml.Class) || cls == null) {
            throw new IllegalArgumentException(
                    "The operation must be affiliated with an instance of Class."); //$NON-NLS-1$
        }
        if (!(returnType instanceof Type) || returnType == null) {
            throw new IllegalArgumentException(
                    "The type of the return parameter must be instance of Type."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Operation operation = createOperation();
                operation.setClass_((org.eclipse.uml2.uml.Class) cls);
                operation.createReturnResult(null, (Type) returnType);
                if (name != null) {
                    operation.setName(name);
                }
                getParams().add(operation);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));

        return (Operation) run.getParams().get(0);
    }

    @SuppressWarnings("deprecation")
    public Parameter buildParameter(Object o, Object model, Object type) {
        return buildParameter(o, type);
    }

    public Parameter buildParameter(final Object o, final Object type) {
        // TODO: In UML2.x Event has no parameters. The Event metaclass in UML1.x
        // corresponds to the Trigger metaclass in UML2.x (see UML Superstructure
        // page 456).
        if (!(o instanceof BehavioralFeature) || o == null) {
            throw new IllegalArgumentException(
                    "The parameter must be attached to a BehavioralFeature."); //$NON-NLS-1$
        }
        if (!(type instanceof Type) || type == null) {
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
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));

        return (Parameter) run.getParams().get(0);
    }

    /**
     * Removed from UML2.x, use buildPackageImport instead.
     */
    @Deprecated
    public PackageImport buildPermission(Object clientObj, Object supplierObj) {
        return buildPackageImport(clientObj, supplierObj);
    }
    
    public PackageImport buildPackageImport(final Object clientObj,
            final Object supplierObj) {
        if (!(clientObj instanceof Namespace) || clientObj == null) {
            throw new IllegalArgumentException(
                    "The client must be instance of Namespace."); //$NON-NLS-1$
        }
        if (!(supplierObj instanceof org.eclipse.uml2.uml.Package)
                || supplierObj == null) {
            throw new IllegalArgumentException(
                    "The supplier must be instance of Package."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                PackageImport packageImport = createPackageImport();
                packageImport.setImportedPackage((org.eclipse.uml2.uml.Package) supplierObj);
                packageImport.setImportingNamespace((Namespace) clientObj);
                getParams().add(packageImport);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));

        return (PackageImport) run.getParams().get(0);
    }

    public Realization buildRealization(final Object client,
            final Object supplier, final Object namespace) {
        if (!(client instanceof NamedElement)
                || !(supplier instanceof NamedElement) || client == null
                || supplier == null) {
            throw new IllegalArgumentException(
                    "The client and the supplier must be NamedElements."); //$NON-NLS-1$
        }
        if (!(namespace instanceof Namespace)
                || (((NamedElement) client).getNearestPackage() != ((NamedElement) supplier).getNearestPackage() && namespace == null)) {
            throw new IllegalArgumentException(
                    "The namespace must be instance of Namespace."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Realization realization = UMLFactory.eINSTANCE.createRealization();
                realization.getClients().add((NamedElement) client);
                realization.getSuppliers().add((NamedElement) supplier);
                if (((NamedElement) client).getNearestPackage() == ((NamedElement) supplier).getNearestPackage()) {
                    ((NamedElement) client).getNearestPackage().getPackagedElements().add(
                            realization);
                } else {
                    ((Namespace) namespace).getNearestPackage().getPackagedElements().add(
                            realization);
                }
                getParams().add(realization);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));

        return (Realization) run.getParams().get(0);
    }

    public Object buildTemplateArgument(Object element) {
        // TODO Is it removed from UML2 ?
        throw new NotImplementedException();
    }

    public Usage buildUsage(final Object client, final Object supplier) {
        if (!(client instanceof NamedElement)
                || !(supplier instanceof NamedElement) || client == null
                || supplier == null) {
            throw new IllegalArgumentException(
                    "The client and the supplier must be NamedElements."); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Usage usage = createUsage();
                usage.getClients().add((NamedElement) client);
                usage.getSuppliers().add((NamedElement) supplier);
                ((NamedElement) client).getNearestPackage().getPackagedElements().add(
                        usage);
                getParams().add(usage);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));

        return (Usage) run.getParams().get(0);
    }

    public Object copyClass(Object source, Object ns) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object copyDataType(Object source, Object ns) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object copyFeature(Object source, Object classifier) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object copyInterface(Object source, Object ns) {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Is it removed from UML2 ?
        throw new NotImplementedException();
    }

    public Enumeration createEnumeration() {
        return UMLFactory.eINSTANCE.createEnumeration();
    }

    public EnumerationLiteral createEnumerationLiteral() {
        return UMLFactory.eINSTANCE.createEnumerationLiteral();
    }

    public Object createFlow() {
        // TODO Is it removed from UML2 ?
        throw new NotImplementedException();
    }

    public Generalization createGeneralization() {
        return UMLFactory.eINSTANCE.createGeneralization();
    }

    public Interface createInterface() {
        return UMLFactory.eINSTANCE.createInterface();
    }

    public Object createMethod() {
        // TODO Is it removed from UML2 ?
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
        // TODO Is it removed from UML2 ?
        throw new NotImplementedException();
    }

    public TemplateParameter createTemplateParameter() {
        return UMLFactory.eINSTANCE.createTemplateParameter();
    }

    public Usage createUsage() {
        return UMLFactory.eINSTANCE.createUsage();
    }

}
