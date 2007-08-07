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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.argouml.model.CoreHelper;
import org.argouml.model.NotImplementedException;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Behavior;
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
import org.eclipse.uml2.uml.Extend;
import org.eclipse.uml2.uml.Feature;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Include;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.RedefinableElement;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.StructuralFeature;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.TypedElement;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.VisibilityKind;

/**
 * Eclipse UML2 implementation of CoreHelper.
 */
class CoreHelperEUMLImpl implements CoreHelper {

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
    public CoreHelperEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
        editingDomain = implementation.getEditingDomain();
    }

    public void addAllStereotypes(final Object modelElement,
            final Collection stereos) {
        if (!(modelElement instanceof Element)) {
            throw new IllegalArgumentException(
                    "modelElement must be instance of Element"); //$NON-NLS-1$
        }
        if (stereos == null) {
            throw new NullPointerException("stereos must be non-null"); //$NON-NLS-1$
        }
        for (Object o : stereos) {
            if (!(o instanceof Stereotype)) {
                throw new IllegalArgumentException(
                        "The stereotypes from stereo collection must be instances of Stereotype"); //$NON-NLS-1$
            }
            if (!((Element) modelElement).isStereotypeApplicable((Stereotype) o)) {
                throw new UnsupportedOperationException(
                        "The stereotype " + o + " cannot be applied to " + modelElement); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                for (Object o : stereos) {
                    ((Element) modelElement).applyStereotype((Stereotype) o);
                }
            }
        };
        ChangeCommand cmd;
        if (stereos.size() == 1) {
            cmd = new ChangeCommand(
                    modelImpl, run, "Apply the stereotype # to the element #",
                    stereos.iterator().next(), modelElement);
        } else {
            cmd = new ChangeCommand(
                    modelImpl, run, "Apply stereotypes to the element #",
                    modelElement);
        }
        editingDomain.getCommandStack().execute(cmd);
    }

    public void addAnnotatedElement(final Object comment,
            final Object annotatedElement) {
        if (!(annotatedElement instanceof Element)) {
            throw new IllegalArgumentException(
                    "annotatedElement must be instance of Element"); //$NON-NLS-1$
        }
        if (!(comment instanceof Comment)) {
            throw new IllegalArgumentException(
                    "comment must be instance of Comment"); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                ((Comment) comment).getAnnotatedElements().add(
                        (Element) annotatedElement);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run, "Add the comment # to the element #",
                        comment, annotatedElement));
    }

    public void addClient(final Object dependency, final Object element) {
        if (!(dependency instanceof Dependency)) {
            throw new IllegalArgumentException(
                    "The dependency must be instance of Dependency"); //$NON-NLS-1$
        }
        if (!(element instanceof NamedElement)) {
            throw new IllegalArgumentException(
                    "The element must be instance of NamedElement"); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                ((Dependency) dependency).getClients().add(
                        (NamedElement) element);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run, "Add the client # to the dependency #",
                        element, dependency));
    }

    public void addClientDependency(Object handle, Object dependency) {
        addClient(dependency, handle);
    }

    public void addComment(Object element, Object comment) {
        addAnnotatedElement(comment, element);
    }

    public void addConnection(Object handle, Object connection) {
        addConnection(handle, CommandParameter.NO_INDEX, connection);
    }

    public void addConnection(Object handle, int position, Object connection) {
        if (!(handle instanceof Association)) {
            throw new IllegalArgumentException(
                    "The handle must be instance of Association"); //$NON-NLS-1$
        }
        if (!(connection instanceof Property)) {
            throw new IllegalArgumentException(
                    "The connection must be instance of Property"); //$NON-NLS-1$
        }
        RunnableClass run = getRunnableClassForAddCommand(
                (Association) handle, position, (Property) connection);
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl,
                        run,
                        "Add the AssociationEnd (Property) # to the Association #",
                        connection, handle));
    }

    public void addConstraint(final Object handle, final Object mc) {
        if (!(handle instanceof Element)) {
            throw new IllegalArgumentException(
                    "The handle must be instance of Element"); //$NON-NLS-1$
        }
        if (!(mc instanceof Constraint)) {
            throw new IllegalArgumentException(
                    "mc must be instance of Constraint"); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                ((Constraint) mc).getConstrainedElements().add((Element) handle);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run,
                        "Add the constraint # to the element #", mc, handle));
    }

    public void addDeploymentLocation(Object handle, Object node) {
        // TODO: Implement
        throw new NotYetImplementedException();
    }

    public void addElementResidence(Object handle, Object residence) {
        // TODO Is it removed from UML2 ?
        throw new NotYetImplementedException();
    }

    private RunnableClass getRunnableClassForAddCommand(Element owner,
            Element element) {
        return getRunnableClassForAddCommand(
                owner, CommandParameter.NO_INDEX, element);
    }

    private RunnableClass getRunnableClassForAddCommand(Element owner,
            int index, Element element) {
        final Command cmd = AddCommand.create(
                editingDomain, owner, null, element, index);
        if (cmd == null || !cmd.canExecute()) {
            throw new UnsupportedOperationException(
                    "The element " + element + " cannot be added to the element " + owner); //$NON-NLS-1$//$NON-NLS-2$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                cmd.execute();
            }
        };
        return run;
    }

    public void addFeature(Object handle, int index, Object f) {
        if (!(handle instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "The handle must be instance of Classifier"); //$NON-NLS-1$
        }
        if (!(f instanceof Feature)) {
            throw new IllegalArgumentException("f must be instance of Feature"); //$NON-NLS-1$
        }
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, getRunnableClassForAddCommand(
                                (Classifier) handle, index, (Feature) f),
                        "Add the feature # to the classifier #", f, handle));
    }

    public void addFeature(Object handle, Object f) {
        addFeature(handle, CommandParameter.NO_INDEX, f);
    }

    public void addLink(Object handle, Object link) {
        // A Link is an Assocation in UML2.x
        throw new NotYetImplementedException();
    }

    public void addLiteral(Object handle, int index, Object literal) {
        if (!(handle instanceof Enumeration)) {
            throw new IllegalArgumentException(
                    "The handle must be instance of Enumeration"); //$NON-NLS-1$
        }
        if (!(literal instanceof EnumerationLiteral)) {
            throw new IllegalArgumentException(
                    "literal must be instance of EnumerationLiteral"); //$NON-NLS-1$
        }
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, getRunnableClassForAddCommand(
                                (Enumeration) handle, index,
                                (EnumerationLiteral) literal),
                        "Add the EnumerationLiteral # to the Enumeration #",
                        literal, handle));
    }

    public void addMethod(final Object handle, final Object method) {
        // In UML2.x there is no metaclass named Method, but we could use the
        // 'method' association of BehavioralFeature
        // TODO: It's OK like this?
        if (!(handle instanceof BehavioralFeature)) {
            throw new IllegalArgumentException(
                    "The handle must be instance of BehavioralFeature"); //$NON-NLS-1$
        }
        if (!(method instanceof Behavior)) {
            throw new IllegalArgumentException(
                    "method must be instance of Behavior"); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                ((BehavioralFeature) handle).getMethods().add((Behavior) method);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl,
                        run,
                        "Add the Behavior (method) # to the BehavioralFeature (operation) #",
                        method, handle));
    }

    public void addOwnedElement(Object handle, Object me) {
        if (!(handle instanceof Namespace)) {
            throw new IllegalArgumentException(
                    "The handle must be instance of Namespace"); //$NON-NLS-1$
        }
        if (!(me instanceof Element)) {
            throw new IllegalArgumentException(
                    "'me' must be instance of Element"); //$NON-NLS-1$
        }
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, getRunnableClassForAddCommand(
                                (Namespace) handle, (Element) me),
                        "Add the owned element # to the owner #", me, handle));
    }

    public void addParameter(Object handle, int index, Object parameter) {
        // TODO: In UML2.x Event has no parameters.
        if (!(handle instanceof BehavioralFeature)) {
            throw new IllegalArgumentException(
                    "handle must be instance of BehavioralFeature"); //$NON-NLS-1$
        }
        if (!(parameter instanceof Parameter)) {
            throw new IllegalArgumentException(
                    "parameter must be instance of Parameter"); //$NON-NLS-1$
        }
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, getRunnableClassForAddCommand(
                                (BehavioralFeature) handle, index,
                                (Parameter) parameter),
                        "Add the owned element # to the owner #", parameter,
                        handle));
    }

    public void addParameter(Object handle, Object parameter) {
        addParameter(handle, CommandParameter.NO_INDEX, parameter);
    }

    public void addQualifier(Object handle, int position, Object qualifier) {
        if (!(handle instanceof Property) || !(qualifier instanceof Property)) {
            throw new IllegalArgumentException(
                    "handle and qualifier must be instances of Property"); //$NON-NLS-1$
        }
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, getRunnableClassForAddCommand(
                                (Property) handle, position,
                                (Property) qualifier),
                        "Add the qualifier # to the property #", qualifier,
                        handle));
    }

    public void addRaisedSignal(Object handle, Object sig) {
        // TODO: implement
        throw new NotYetImplementedException();
    }

    public void addSourceFlow(Object handle, Object flow) {
        // TODO: implement
        throw new NotYetImplementedException();
    }

    public void addStereotype(Object modelElement, Object stereo) {
        addAllStereotypes(modelElement, Collections.singleton(stereo));
    }

    public void addSupplier(final Object dependency, final Object element) {
        if (!(dependency instanceof Dependency)) {
            throw new IllegalArgumentException(
                    "The dependency must be instance of Dependency"); //$NON-NLS-1$
        }
        if (!(element instanceof NamedElement)) {
            throw new IllegalArgumentException(
                    "The element must be instance of NamedElement"); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                ((Dependency) dependency).getSuppliers().add(
                        (NamedElement) element);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run,
                        "Add the supplier # to the dependency #", element,
                        dependency));
    }

    public void addSupplierDependency(Object supplier, Object dependency) {
        addSupplier(dependency, supplier);
    }

    public void addTaggedValue(Object handle, Object taggedValue) {
        // TODO: implement
        throw new NotYetImplementedException();
    }

    public void addTargetFlow(Object handle, Object flow) {
        // TODO: implement
        throw new NotYetImplementedException();
    }

    public void addTemplateArgument(Object handle, int index, Object argument) {
        // TODO Is it removed from UML2 ?
        throw new NotYetImplementedException();
    }

    public void addTemplateArgument(Object handle, Object argument) {
        // TODO Is it removed from UML2 ?
        throw new NotYetImplementedException();
    }

    public void addTemplateParameter(Object handle, int index, Object parameter) {
        // TODO: implement
        throw new NotYetImplementedException();
    }

    public void addTemplateParameter(Object handle, Object parameter) {
        // TODO: implement
        throw new NotYetImplementedException();
    }

    public void clearStereotypes(Object handle) {
        // TODO: implement
        throw new NotYetImplementedException();
    }

    public boolean equalsAggregationKind(Object associationEnd, String kindType) {
        if (!(associationEnd instanceof Property)) {
            throw new IllegalArgumentException(
                    "associationEnd must be instance of Property"); //$NON-NLS-1$
        }
        return ((Property) associationEnd).getAggregation().getLiteral().equals(
                kindType);
    }

    public Collection<Property> getAllAttributes(Object classifier) {
        if (!(classifier instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "classifier must be instance of Classifier"); //$NON-NLS-1$
        }
        return ((Classifier) classifier).getAllAttributes();
    }

    private Collection getAllOwnedElementsOfKind(Object owner, Class kind) {
        if (!(owner instanceof Element)) {
            throw new IllegalArgumentException(
                    "owner must be instance of Element"); //$NON-NLS-1$
        }
        Collection<Element> result = new ArrayList<Element>();
        for (Element e : ((Element) owner).allOwnedElements()) {
            if (kind.isInstance(e)) {
                result.add(e);
            }
        }
        return result;
    }

    public Collection getAllBehavioralFeatures(Object element) {
        return getAllOwnedElementsOfKind(element, BehavioralFeature.class);
    }

    public Collection getAllClasses(Object ns) {
        return getAllOwnedElementsOfKind(ns, org.eclipse.uml2.uml.Class.class);
    }

    public Collection getAllClassifiers(Object namespace) {
        return getAllOwnedElementsOfKind(namespace, Classifier.class);
    }

    public Collection getAllComponents(Object ns) {
        return getAllOwnedElementsOfKind(ns, Component.class);
    }

    public Collection getAllContents(Object namespace) {
        return modelImpl.getModelManagementHelper().getAllContents(namespace);
    }

    public Collection getAllDataTypes(Object ns) {
        return getAllOwnedElementsOfKind(ns, DataType.class);
    }

    public Collection getAllInterfaces(Object ns) {
        return getAllOwnedElementsOfKind(ns, Interface.class);
    }

    public Collection getAllMetatypeNames() {
        Collection result = new ArrayList();
        for (Field f : UMLPackage.Literals.class.getDeclaredFields()) {
            Object o;
            try {
                o = f.get(null);
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (o instanceof EClass) {
                result.add(((EClass) o).getName());
            }
        }
        return result;
    }

    public Collection getAllNodes(Object ns) {
        return getAllOwnedElementsOfKind(ns, Node.class);
    }

    public Collection getAllPossibleNamespaces(Object modelElement, Object model) {
        if (!(model instanceof Element) || !(modelElement instanceof Element)) {
            throw new IllegalArgumentException(
                    "modelElement and model must be instances of Element"); //$NON-NLS-1$
        }
        Collection result = new ArrayList();
        if (isValidNamespace(modelElement, model)) {
            result.add((Namespace) model);
        }
        for (Object o : getAllOwnedElementsOfKind(model, Namespace.class)) {
            if (isValidNamespace(modelElement, o)) {
                result.add((Namespace) o);
            }
        }
        return result;
    }

    public Collection getAllRealizedInterfaces(Object element) {
        if (!(element instanceof org.eclipse.uml2.uml.Class)) {
            throw new IllegalArgumentException(
                    "element must be instance of UML2 Class"); //$NON-NLS-1$
        }
        return ((org.eclipse.uml2.uml.Class) element).getAllImplementedInterfaces();
    }

    public Collection getAllSupertypes(Object classifier) {
        if (!(classifier instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "classifier must be instance of Classifier"); //$NON-NLS-1$
        }
        return ((Classifier) classifier).allParents();
    }

    public Collection getAllVisibleElements(Object ns) {
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException(
                    "ns must be instance of Namespace"); //$NON-NLS-1$
        }
        Collection result = new ArrayList();
        for (NamedElement e : ((Namespace) ns).getOwnedMembers()) {
            if (e.getVisibility() == VisibilityKind.PUBLIC_LITERAL) {
                result.add(e);
            }
        }
        return result;
    }

    public Collection getAssociateEnds(Object classifier) {
        return modelImpl.getFacade().getAssociationEnds(classifier);
    }

    public Collection getAssociateEndsInh(Object classifier) {
        if (!(classifier instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "classifier must be instance of Classifier"); //$NON-NLS-1$
        }
        Collection result = new ArrayList();
        result.addAll(getAssociateEnds(classifier));
        for (Classifier o : ((Classifier) classifier).allParents()) {
            result.addAll(getAssociateEnds(o));
        }
        return result;
    }

    public Collection getAssociatedClassifiers(Object aclassifier) {
        if (!(aclassifier instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "aclassifier must be instance of Classifier"); //$NON-NLS-1$
        }
        Collection result = new ArrayList();
        for (Association a : ((Classifier) aclassifier).getAssociations()) {
            for (Type t : a.getEndTypes()) {
                if (t != aclassifier && t instanceof Classifier) {
                    result.add((Classifier) t);
                }
            }
        }
        return result;
    }

    public Object getAssociationEnd(Object type, Object assoc) {
        throw new NotYetImplementedException();
    }

    public Collection getAssociations(Object from, Object to) {
        throw new NotYetImplementedException();

    }

    public Collection getAssociations(Object oclassifier) {
        throw new NotYetImplementedException();

    }

    public Collection getAttributesInh(Object classifier) {
        throw new NotYetImplementedException();

    }

    public List<BehavioralFeature> getBehavioralFeatures(Object classifier) {
        List<BehavioralFeature> result = new ArrayList<BehavioralFeature>();
        for (Feature feature : ((Classifier) classifier).getFeatures()) {
            if (feature instanceof BehavioralFeature) {
                result.add((BehavioralFeature) feature);
            }
        }
        return result;
    }

    public String getBody(Object comment) {
        throw new NotYetImplementedException();

    }

    public Collection<Classifier> getChildren(Object element) {
        Collection<Classifier> results = new HashSet<Classifier>();
        for (Generalization g : ((Classifier) element).getGeneralizations()) {
            results.addAll(getChildren(g.getSpecific()));
        }
        results.remove(element);
        return results;
    }

    public Collection getDependencies(Object supplierObj, Object clientObj) {
        throw new NotYetImplementedException();

    }

    public Collection getExtendedClassifiers(Object element) {
        throw new NotYetImplementedException();

    }

    public Collection getExtendingClassifiers(Object classifier) {
        throw new NotYetImplementedException();

    }

    public Collection getExtendingElements(Object element) {
        throw new NotYetImplementedException();

    }

    public Object getFirstSharedNamespace(Object ns1, Object ns2) {
        throw new NotYetImplementedException();

    }

    public Collection getFlows(Object source, Object target) {
        throw new NotYetImplementedException();

    }

    public Object getGeneralization(Object achild, Object aparent) {
        throw new NotYetImplementedException();

    }

    public Collection getOperationsInh(Object classifier) {
        throw new NotYetImplementedException();

    }

    public Collection getRealizedInterfaces(Object cls) {
        throw new NotYetImplementedException();

    }

    public Collection getRelationships(Object source, Object dest) {
        throw new NotYetImplementedException();

    }

    public List getReturnParameters(Object operation) {
        throw new NotYetImplementedException();

    }

    public Object getSource(Object relationship) {

        // TODO: not implemented for UML 2 - tfm
        // if (relationship instanceof Link) {
        // Iterator it =
        // modelImpl.getFacade()
        // .getConnections(relationship).iterator();
        // if (it.hasNext()) {
        // return modelImpl.getFacade().getInstance(it.next());
        // } else {
        // return null;
        // }
        // }
        if (relationship instanceof Association) {
            Association assoc = (Association) relationship;
            List conns = assoc.getMemberEnds();
            if (conns == null || conns.isEmpty()) {
                return null;
            }
            // TODO: double check this - tfm
            return ((Property) conns.get(0)).getType();
        } else if (relationship instanceof Generalization) {
            Generalization gen = (Generalization) relationship;
            return gen.getSpecific();
        } else if (relationship instanceof Dependency) {
            Dependency dep = (Dependency) relationship;
            List col = dep.getClients();
            if (col.isEmpty()) {
                return null;
            }
            return col.get(0);
            // TODO: not implemented for UML 2 - tfm
            // } else if (relationship instanceof Flow) {
            // Flow flow = (Flow) relationship;
            // Collection col = flow.getSource();
            // if (col.isEmpty()) {
            // return null;
            // }
            // return (col.toArray())[0];
        } else if (relationship instanceof Extend) {
            Extend extend = (Extend) relationship;
            return extend.getExtension(); // we have to follow the
            // arrows..
        } else if (relationship instanceof Include) {
            Include include = (Include) relationship;
            return modelImpl.getFacade().getBase(include);
        } else if (relationship instanceof Property) {
            return ((Property) relationship).getAssociation();
        } else {
            throw new IllegalArgumentException();
        }

    }

    public Object getDestination(Object relationship) {

        // if (relationship instanceof Link) {
        // Iterator it = modelImpl.getFacade()
        // .getConnections(relationship).iterator();
        // if (it.hasNext()) {
        // it.next();
        // if (it.hasNext()) {
        // return modelImpl.getFacade().getInstance(it.next());
        // } else {
        // return null;
        // }
        // } else {
        // return null;
        // }
        // }

        if (relationship instanceof Association) {
            Association assoc = (Association) relationship;
            List conns = assoc.getMemberEnds();
            if (conns.size() <= 1) {
                return null;
            }
            return ((Property) conns.get(1)).getType();
        } else if (relationship instanceof Generalization) {
            Generalization gen = (Generalization) relationship;
            return gen.getGeneral();
        } else if (relationship instanceof Dependency) {
            Dependency dep = (Dependency) relationship;
            List col = dep.getSuppliers();
            if (col.isEmpty()) {
                return null;
            }
            return col.get(0);
            // } else if (relationship instanceof Flow) {
            // Flow flow = (Flow) relationship;
            // Collection col = flow.getTarget();
            // if (col.isEmpty()) {
            // return null;
            // }
            // return getFirstItemOrNull(col);
        } else if (relationship instanceof Extend) {
            Extend extend = (Extend) relationship;
            return extend.getExtendedCase();
        } else if (relationship instanceof Include) {
            Include include = (Include) relationship;
            return modelImpl.getFacade().getAddition(include);
        } else if (relationship instanceof Property) {
            return ((Property) relationship).getType();
        } else {
            throw new IllegalArgumentException();
        }

    }

    public Object getSpecification(Object object) {
        throw new NotYetImplementedException();

    }

    public Collection getSpecifications(Object classifier) {
        throw new NotYetImplementedException();

    }

    public Collection getSubtypes(Object cls) {
        throw new NotYetImplementedException();

    }

    public Collection getSupertypes(Object generalizableElement) {
        throw new NotYetImplementedException();

    }

    public boolean hasCompositeEnd(Object association) {
        throw new NotYetImplementedException();

    }

    public boolean isSubType(Object type, Object subType) {
        throw new NotYetImplementedException();

    }

    public boolean isValidNamespace(Object element, Object namespace) {
        if (!(element instanceof NamedElement)
                || !(namespace instanceof Namespace)) {
            return false;
        }
        if (((NamedElement) element).getNamespace() == namespace) {
            return true;
        }
        try {
            RunnableClass run = getRunnableClassForAddCommand(
                    (Namespace) namespace, (NamedElement) element);
        } catch (UnsupportedOperationException e) {
            return false;
        }
        return true;
    }

    public void removeAnnotatedElement(Object handle, Object me) {
        throw new NotYetImplementedException();

    }

    public void removeClientDependency(Object handle, Object dep) {
        throw new NotYetImplementedException();

    }

    public void removeConnection(Object handle, Object connection) {
        throw new NotYetImplementedException();

    }

    public void removeConstraint(Object handle, Object cons) {
        throw new NotYetImplementedException();

    }

    public void removeDeploymentLocation(Object handle, Object node) {
        throw new NotYetImplementedException();

    }

    public void removeElementResidence(Object handle, Object residence) {
        throw new NotYetImplementedException();

    }

    public void removeFeature(Object cls, Object feature) {
        throw new NotYetImplementedException();

    }

    public void removeLiteral(Object enumeration, Object literal) {
        throw new NotYetImplementedException();

    }

    public void removeOwnedElement(Object handle, Object value) {
        throw new NotYetImplementedException();

    }

    public void removeParameter(Object handle, Object parameter) {
        throw new NotYetImplementedException();

    }

    public void removeQualifier(Object handle, Object qualifier) {
        throw new NotYetImplementedException();

    }

    public void removeSourceFlow(Object handle, Object flow) {
        throw new NotYetImplementedException();

    }

    public void removeStereotype(Object handle, Object stereo) {
        throw new NotYetImplementedException();

    }

    public void removeSupplierDependency(Object supplier, Object dependency) {
        throw new NotYetImplementedException();

    }

    public void removeTargetFlow(Object handle, Object flow) {
        throw new NotYetImplementedException();

    }

    public void removeTemplateArgument(Object binding, Object argument) {
        throw new NotYetImplementedException();

    }

    public void removeTemplateParameter(Object handle, Object parameter) {
        throw new NotYetImplementedException();

    }

    public void setAbstract(Object handle, boolean isAbstract) {
        if (handle instanceof Classifier) {
            ((Classifier) handle).setIsAbstract(isAbstract);
        } else if (handle instanceof BehavioralFeature) {
            ((BehavioralFeature) handle).setIsAbstract(isAbstract);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setActive(Object handle, boolean isActive) {
        ((org.eclipse.uml2.uml.Class) handle).setIsActive(isActive);
    }

    public void setAggregation(Object handle, Object aggregationKind) {
        throw new NotYetImplementedException();
    }

    public void setAnnotatedElements(Object handle, Collection elems) {
        throw new NotYetImplementedException();

    }

    public void setAssociation(Object handle, Object association) {
        throw new NotYetImplementedException();

    }

    public void setAttributes(Object classifier, List attributes) {
        throw new NotYetImplementedException();

    }

    public void setBody(Object handle, Object expr) {
        throw new NotYetImplementedException();

    }

    public void setBody(Object handle, String body) {
        throw new NotYetImplementedException();

    }

    @SuppressWarnings("deprecation")
    public void setChangeability(Object handle, Object ck) {
        throw new NotImplementedException();
    }

    @SuppressWarnings("deprecation")
    public void setChangeable(Object handle, boolean changeable) {
        setReadOnly(handle, !changeable);
    }

    public void setChild(Object handle, Object child) {
        throw new NotYetImplementedException();

    }

    public void setConcurrency(Object handle, Object concurrencyKind) {
        throw new NotYetImplementedException();

    }

    public void setConnections(Object handle, Collection ends) {
        throw new NotYetImplementedException();

    }

    public void setContainer(Object handle, Object component) {
        throw new NotYetImplementedException();

    }

    public void setDefaultElement(Object handle, Object element) {
        throw new NotYetImplementedException();

    }

    public void setDefaultValue(Object handle, Object expression) {
        throw new NotYetImplementedException();

    }

    public void setDiscriminator(Object handle, String discriminator) {
        // Removed from UML 2.x
        throw new NotImplementedException();
    }

    public void setEnumerationLiterals(Object enumeration, List literals) {
        throw new NotYetImplementedException();

    }

    public void setFeature(Object classifier, int index, Object feature) {
        throw new NotYetImplementedException();

    }

    public void setFeatures(Object classifier, Collection features) {
        throw new NotYetImplementedException();

    }

    public void setInitialValue(Object attribute, Object expression) {
        throw new NotYetImplementedException();

    }

    public void setKind(Object handle, Object kind) {
        throw new NotYetImplementedException();

    }

    public void setLeaf(Object handle, boolean isLeaf) {
        ((RedefinableElement) handle).setIsLeaf(isLeaf);
    }

    public void setModelElementContainer(Object handle, Object container) {
        // TODO: This method is mostly (entirely?) redundant - tfm
        addOwnedElement(container, handle);
    }

    public void setMultiplicity(Object handle, Object arg) {
        throw new NotYetImplementedException();

    }

    public void setName(Object handle, String name) {
        ((NamedElement) handle).setName(name);
    }

    public void setNamespace(Object handle, Object ns) {
        addOwnedElement(ns, handle);
    }

    public void setNavigable(Object handle, boolean flag) {
        ((Property) handle).setIsNavigable(flag);
    }

    public void setOperations(Object classifier, List operations) {
        throw new NotYetImplementedException();

    }

    public void setOrdering(Object handle, Object ordering) {
        // ((Property) handle).setIsOrdered(ordering);
        throw new NotYetImplementedException();
    }

    public void setOwner(Object handle, Object owner) {
        throw new NotYetImplementedException();
    }

    @SuppressWarnings("deprecation")
    public void setOwnerScope(Object feature, Object scopeKind) {
        // Don't implement - deprecated method in interface.
        throw new NotImplementedException();
    }

    public void setParameter(Object handle, Object parameter) {
        throw new NotYetImplementedException();

    }

    public void setParameters(Object handle, Collection parameters) {
        throw new NotYetImplementedException();

    }

    public void setParent(Object handle, Object parent) {
        throw new NotYetImplementedException();

    }

    public void setPowertype(Object handle, Object powerType) {
        throw new NotYetImplementedException();

    }

    public void setQualifiers(Object handle, List qualifiers) {
        throw new NotYetImplementedException();

    }

    public void setQuery(Object handle, boolean isQuery) {
        ((Operation) handle).setIsQuery(isQuery);
    }

    public void setRaisedSignals(Object handle, Collection raisedSignals) {
        throw new NotYetImplementedException();

    }

    public void setReadOnly(Object handle, boolean isReadOnly) {
        ((StructuralFeature) handle).setIsReadOnly(isReadOnly);
    }

    public void setResident(Object handle, Object resident) {
        throw new NotYetImplementedException();

    }

    public void setResidents(Object handle, Collection residents) {
        throw new NotYetImplementedException();

    }

    public void setRoot(Object handle, boolean isRoot) {
        // Appears to be gone from UML 2.x
        throw new NotImplementedException();
    }

    public void setSources(Object handle, Collection specifications) {
        throw new NotYetImplementedException();

    }

    public void setSpecification(Object handle, boolean isSpecification) {
        throw new NotYetImplementedException();

    }

    public void setSpecification(Object method, Object specification) {
        throw new NotYetImplementedException();
    }

    public void setSpecification(Object operation, String specification) {
        throw new NotYetImplementedException();
    }

    public void setSpecifications(Object handle, Collection specifications) {
        throw new NotYetImplementedException();

    }

    public void setStatic(Object feature, boolean isStatic) {
        ((Feature) feature).setIsStatic(isStatic);
    }

    public void setTaggedValue(Object handle, String tag, String value) {
        throw new NotYetImplementedException();

    }

    public void setTaggedValues(Object handle, Collection taggedValues) {
        throw new NotYetImplementedException();

    }

    @SuppressWarnings("deprecation")
    public void setTargetScope(Object handle, Object targetScope) {
        // Don't implement - deprecated method in interface.
        throw new NotImplementedException();
    }

    public void setType(Object handle, Object type) {
        ((TypedElement) handle).setType((Type) type);
    }

    public void setVisibility(Object handle, Object visibility) {
        ((NamedElement) handle).setVisibility((VisibilityKind) visibility);
    }

    public Collection getParents(Object generalizableElement) {
        throw new NotYetImplementedException();
    }

    public Object getPackageImport(Object supplier, Object client) {
        throw new NotYetImplementedException();
    }

    public Collection getPackageImports(Object client) {
        throw new NotYetImplementedException();
    }

}
