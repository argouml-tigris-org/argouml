// $Id$
/*******************************************************************************
 * Copyright (c) 2007-2012 Tom Morris and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial framework & prototype implementation
 *    Bogdan Pistol - initial implementation
 *    Thomas Neustupny
 *    Michiel van der Wulp
 *    Bob Tarling
 *****************************************************************************/

package org.argouml.model.euml;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.model.CoreHelper;
import org.argouml.model.Model;
import org.argouml.model.NotImplementedException;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Artifact;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.BehavioralFeature;
import org.eclipse.uml2.uml.CallConcurrencyKind;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.DirectedRelationship;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Feature;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Manifestation;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.RedefinableElement;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.StructuralFeature;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.TypedElement;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.VisibilityKind;

/**
 * Eclipse UML2 implementation of CoreHelper.
 */
class CoreHelperEUMLImpl implements CoreHelper {

    private static final Logger LOG =
        Logger.getLogger(CoreHelperEUMLImpl.class.getName());

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

    public void addAllStereotypes(
            final Object modelElement,
            final Collection stereos) {
        if (!(modelElement instanceof Element)) {
            throw new IllegalArgumentException(
                    "modelElement must be instance of Element");
        }
        if (stereos == null) {
            throw new NullPointerException("stereos must be non-null");
        }

        final Element element = (Element) modelElement;

        for (Object o : stereos) {
            if (!(o instanceof Stereotype)) {
                throw new IllegalArgumentException(
                        "The stereotypes from stereo collection"
                            + " must be instances of Stereotype");
            }
            if (!element.isStereotypeApplicable((Stereotype) o)) {
                throw new UnsupportedOperationException(
                        "The stereotype " + o
                        + " cannot be applied to " + modelElement);
            }
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                for (Object o : stereos) {
                    Stereotype stereotype = (Stereotype) o;
                    EObject eo = element.applyStereotype(stereotype);
                    if (element.isStereotypeApplied(stereotype)) {
                        fireApplyStereotypeEvent(modelElement, stereotype);
                    } else {
                        EcoreUtil.remove(eo);
                    }
                }
            }

            /**
             * Call the model event pump and ask it to fire an event indicating
             * a stereotype has been added. This is a stop-gap until we have
             * determined how the event pump can detect itself that a stereotype
             * has been added.
             *
             * @param modelElement
             * @param stereotype
             */
            private void fireApplyStereotypeEvent(
                    Object modelElement,
                    Object stereotype) {
                final ModelEventPumpEUMLImpl pump =
                    (ModelEventPumpEUMLImpl) Model.getPump();
                pump.fireEvent(
                        modelElement,
                        null,
                        stereotype,
                        Notification.ADD,
                        "stereotype",
                        null);
            }

        };
        ChangeCommand cmd;
        if (stereos.size() == 1) {
            cmd = new ChangeCommand(
                    modelImpl, run, "Apply the stereotype # to the element #",
                    stereos.iterator().next(), modelElement);
        } else {
            cmd = new ChangeCommand(
                    modelImpl, run, "Apply # stereotypes to the element #",
                    stereos.size(), modelElement);
        }
        editingDomain.getCommandStack().execute(cmd);
    }

    public void addAnnotatedElement(final Object comment,
            final Object annotatedElement) {
        if (!(annotatedElement instanceof Element)) {
            throw new IllegalArgumentException(
                    "annotatedElement must be instance of Element");
        }
        if (!(comment instanceof Comment)) {
            throw new IllegalArgumentException(
                    "comment must be instance of Comment");
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
                    "The dependency must be instance of Dependency");
        }
        if (!(element instanceof NamedElement)) {
            throw new IllegalArgumentException(
                    "The element must be instance of NamedElement");
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
                    "The handle must be instance of Association");
        }
        if (!(connection instanceof Property)) {
            throw new IllegalArgumentException(
                    "The connection must be instance of Property");
        }
        RunnableClass run = getRunnableClassForAddCommand(
                (Association) handle, position, (Property) connection);
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl,
                        run,
                        "Add the AssociationEnd (Property) # "
                        + "to the Association #",
                        connection, handle));
    }

    public void addConstraint(final Object handle, final Object mc) {
        if (!(handle instanceof Element)) {
            throw new IllegalArgumentException(
                    "The handle must be instance of Element");
        }
        if (!(mc instanceof Constraint)) {
            throw new IllegalArgumentException(
                    "mc must be instance of Constraint");
        }
        final Element element = (Element) handle;
        RunnableClass run = new RunnableClass() {
            public void run() {
                ((Constraint) mc).getConstrainedElements().add(element);
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
        // TODO: Is it removed from UML2 ?
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
                    "The element " + element
                    + " cannot be added to the element " + owner);
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                cmd.execute();
            }
        };
        return run;
    }

    private RunnableClass getRunnableClassForRemoveCommand(Element element) {
        final Command cmd = RemoveCommand.create(editingDomain, element);
        if (cmd == null || !cmd.canExecute()) {
            String s = "The element " + element;
            if (element.getOwner() != null) {
                s += ", owned by " + element.getOwner() + ", ";
            }
            s += " cannot be removed";
            throw new UnsupportedOperationException(s);
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
                    "The handle must be instance of Classifier");
        }
        if (!(f instanceof Feature)) {
            throw new IllegalArgumentException("f must be instance of Feature");
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
                    "The handle must be instance of Enumeration");
        }
        if (!(literal instanceof EnumerationLiteral)) {
            throw new IllegalArgumentException(
                    "literal must be instance of EnumerationLiteral");
        }
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, getRunnableClassForAddCommand(
                                (Enumeration) handle, index,
                                (EnumerationLiteral) literal),
                        "Add the EnumerationLiteral # to the Enumeration #",
                        literal, handle));
    }

    public void addManifestation(Object handle, Object manifestation) {
        if (!(handle instanceof Artifact)) {
            throw new IllegalArgumentException(
                    "The handle must be instance of Artifact");
        }
        if (!(manifestation instanceof Manifestation)) {
            throw new IllegalArgumentException(
                    "The manifestation must be instance of Manifestation");
        }
        ((Artifact) handle).getManifestations()
                .add((Manifestation) manifestation);
    }

    public void addMethod(final Object handle, final Object method) {
        // In UML2.x there is no metaclass named Method, but we use the
        // 'method' association of BehavioralFeature
        if (!(handle instanceof BehavioralFeature)) {
            throw new IllegalArgumentException(
                    "The handle must be instance of BehavioralFeature");
        }
        if (!(method instanceof Behavior)) {
            throw new IllegalArgumentException(
                    "method must be instance of Behavior");
        }

        final Behavior behavior = (Behavior) method;
        RunnableClass run = new RunnableClass() {
            public void run() {
                ((BehavioralFeature) handle).getMethods().add(behavior);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl,
                        run,
                        "Add the Behavior (method) # "
                        + "to the BehavioralFeature (operation) #",
                        method, handle));
    }

    public void addOwnedElement(Object handle, Object me, String msg,
            Object... objects) {
        if (!(handle instanceof Namespace)) {
            throw new IllegalArgumentException(
                    "The handle must be instance of Namespace");
        }
        if (!(me instanceof Element)) {
            throw new IllegalArgumentException(
                    "'me' must be instance of Element, we got a " + me);
        }
        Element element = (Element) me;
        if (element.getOwner() != null) {
            LOG.log(Level.INFO, "Setting ignore delete for {0}", element);

            ModelEventPumpEUMLImpl pump =
                (ModelEventPumpEUMLImpl) Model.getPump();
            pump.addElementForDeleteEventIgnore(element);
        }
        editingDomain.getCommandStack().execute(
                new ChangeCommand(modelImpl, getRunnableClassForAddCommand(
                        (Namespace) handle, (Element) me), msg, objects));
    }

    public void addOwnedElement(Object handle, Object me) {
        addOwnedElement(handle, me, "Add the owned element # to the owner #",
                me, handle);
    }

    public void addParameter(Object handle, int index, Object parameter) {
        // TODO: In UML2.x Event has no parameters.
        // TODO: Treat ObjectFlowState (this doesn't exist anymore in UML2)
        // and Classifier
        if (!(handle instanceof BehavioralFeature)) {
            throw new IllegalArgumentException(
                    "handle must be instance of BehavioralFeature");
        }
        if (!(parameter instanceof Parameter)) {
            throw new IllegalArgumentException(
                    "parameter must be instance of Parameter");
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
                    "handle and qualifier must be instances of Property");
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

    public void addRaisedException(Object handle, Object exception) {
        UMLUtil.checkArgs(new Object[] {handle, exception},
                new Class[] {Operation.class, Type.class});
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, getRunnableClassForAddCommand(
                                (Operation) handle,
                                (Type) exception),
                        "Add the Exception # to the Operation #", exception,
                        handle));
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
                    "The dependency must be instance of Dependency");
        }
        if (!(element instanceof NamedElement)) {
            throw new IllegalArgumentException(
                    "The element must be instance of NamedElement");
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

    public void addTargetFlow(Object handle, Object flow) {
        // TODO: implement
        throw new NotYetImplementedException();
    }

    public void addTemplateArgument(Object handle, int index, Object argument) {
        // TODO: Is it removed from UML2 ?
        throw new NotYetImplementedException();
    }

    public void addTemplateArgument(Object handle, Object argument) {
        // TODO: Is it removed from UML2 ?
        throw new NotYetImplementedException();
    }

    public void addTemplateParameter(
            Object handle,
            int index,
            Object parameter) {
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

    public boolean equalsAggregationKind(
            Object associationEnd,
            String kindType) {
        if (!(associationEnd instanceof Property)) {
            throw new IllegalArgumentException(
                    "associationEnd must be instance of Property");
        }
        return ((Property) associationEnd).getAggregation().getLiteral().equals(
                kindType);
    }

    public Collection getAllAttributes(Object classifier) {
        if (!(classifier instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "classifier must be instance of Classifier");
        }
        Collection result = new HashSet();
        result.addAll(((Classifier) classifier).getAttributes());
        for (Classifier c : ((Classifier) classifier).allParents()) {
            result.addAll(c.getAttributes());
        }
        return result;
    }

    public Collection getAllBehavioralFeatures(Object element) {
        return modelImpl.getModelManagementHelper().getAllModelElementsOfKind(
                element, BehavioralFeature.class);
    }

    public Collection getAllClasses(Object ns) {
        return modelImpl.getModelManagementHelper().getAllModelElementsOfKind(
                ns, org.eclipse.uml2.uml.Class.class);
    }

    public Collection getAllClassifiers(Object namespace) {
        return modelImpl.getModelManagementHelper().getAllModelElementsOfKind(
                namespace, Classifier.class);
    }

    public Collection getAllComponents(Object ns) {
        return modelImpl.getModelManagementHelper().getAllModelElementsOfKind(
                ns, org.eclipse.uml2.uml.Component.class);
    }

    public Collection getAllDataTypes(Object ns) {
        return modelImpl.getModelManagementHelper().getAllModelElementsOfKind(
                ns, DataType.class);
    }

    public Collection getAllInterfaces(Object ns) {
        return modelImpl.getModelManagementHelper().getAllModelElementsOfKind(
                ns, Interface.class);
    }

    public Collection<String> getAllMetaDatatypeNames() {
        // TODO: not implemented
        return Collections.emptySet();
    }

    public Collection<String> getAllMetatypeNames() {
        Collection<String> result = new ArrayList<String>();
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
        final ModelManagementHelperEUMLImpl helper =
            modelImpl.getModelManagementHelper();
        return helper.getAllModelElementsOfKind(ns, Node.class);
    }

    public Collection getAllPossibleNamespaces(
            Object modelElement,
            Object model) {
        if (!(model instanceof Element) || !(modelElement instanceof Element)) {
            throw new IllegalArgumentException(
                    "modelElement and model must be instances of Element");
        }
        Collection result = new ArrayList();
        if (isValidNamespace(modelElement, model)) {
            result.add((Namespace) model);
        }
        final ModelManagementHelperEUMLImpl helper =
            modelImpl.getModelManagementHelper();
        for (Object o
                : helper.getAllModelElementsOfKind(model, Namespace.class)) {
            if (isValidNamespace(modelElement, o)) {
                result.add((Namespace) o);
            }
        }
        return result;
    }

    public Collection getAllRealizedInterfaces(Object element) {
        if (!(element instanceof org.eclipse.uml2.uml.Class)) {
            throw new IllegalArgumentException(
                    "element must be instance of UML2 Class");
        }
        final org.eclipse.uml2.uml.Class theClass =
            (org.eclipse.uml2.uml.Class) element;
        return theClass.getAllImplementedInterfaces();
    }

    public Collection getAllSupertypes(Object classifier) {
        if (!(classifier instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "classifier must be instance of Classifier");
        }
        return ((Classifier) classifier).allParents();
    }

    public Collection getAllVisibleElements(Object ns) {
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException(
                    "ns must be instance of Namespace");
        }
        Collection result = new ArrayList();
        for (NamedElement e : ((Namespace) ns).getOwnedMembers()) {
            if (e.getVisibility() == VisibilityKind.PUBLIC_LITERAL) {
                result.add(e);
            }
        }
        return result;
    }


    public Collection getAssociateEndsInh(Object classifier) {
        if (!(classifier instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "classifier must be instance of Classifier");
        }
        Collection result = new ArrayList();
        result.addAll(modelImpl.getFacade().getAssociationEnds(classifier));
        for (Classifier o : ((Classifier) classifier).allParents()) {
            result.addAll(modelImpl.getFacade().getAssociationEnds(o));
        }
        return result;
    }

    public Collection<Classifier> getAssociatedClassifiers(Object aclassifier) {
        if (!(aclassifier instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "aclassifier must be instance of Classifier");
        }
        Collection<Classifier> result = new ArrayList<Classifier>();
        for (Association a : ((Classifier) aclassifier).getAssociations()) {
            for (Type t : a.getEndTypes()) {
                if (t != aclassifier && t instanceof Classifier) {
                    result.add((Classifier) t);
                }
            }
        }
        return result;
    }

    public Property getAssociationEnd(Object type, Object assoc) {
        if (!(type instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "type must be instance of Classifier");
        }
        if (!(assoc instanceof Association)) {
            throw new IllegalArgumentException(
                    "assoc must be instance of Association");
        }
        return ((Association) assoc).getMemberEnd(null, (Classifier) type);
    }

    public Collection<Association> getAssociations(Object from, Object to) {
        // TODO: The javadoc specifies that null should be returned if 'from' or
        // 'to' are null or if there are no associations between them. We should
        // return an empty collection instead and the javadoc should be changed.
        if (from == null || to == null) {
            return Collections.emptyList();
        }
        if (!(from instanceof Classifier) || !(to instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "'from' and 'to' must be instances of Classifier");
        }
        Collection<Association> result = new ArrayList<Association>();
        for (Association a : ((Classifier) from).getAssociations()) {
            if (((Classifier) to).getAssociations().contains(a)) {
                result.add(a);
            }
        }
        return result;
    }

    public Collection getAssociations(Object classifier) {
        if (!(classifier instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "'classifier' must be instance of Classifier");
        }
        return ((Classifier) classifier).getAssociations();
    }

    public Collection getAttributesInh(Object classifier) {
        if (!(classifier instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "'classifier' must be instance of Classifier");
        }
        return ((Classifier) classifier).getAllAttributes();
    }

    public List<BehavioralFeature> getBehavioralFeatures(Object classifier) {
        if (!(classifier instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "'classifier' must be instance of Classifier");
        }
        List<BehavioralFeature> result = new ArrayList<BehavioralFeature>();
        for (Feature feature : ((Classifier) classifier).getFeatures()) {
            if (feature instanceof BehavioralFeature) {
                result.add((BehavioralFeature) feature);
            }
        }
        return result;
    }

    public String getBody(Object comment) {
        if (!(comment instanceof Comment)) {
            throw new IllegalArgumentException(
                    "'comment' must be instance of Comment");
        }
        return ((Comment) comment).getBody();
    }

    public Collection<Classifier> getChildren(Object element) {
        if (!(element instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "'element' must be instance of Classifier");
        }
        Collection<Classifier> results = new HashSet<Classifier>();
        LinkedList<Classifier> classifiers = new LinkedList<Classifier>();
        classifiers.add((Classifier) element);
        while (!classifiers.isEmpty()) {
            Classifier c = classifiers.removeFirst();
            if (results.contains(c)) {
                break;
            }
            results.add(c);
            for (DirectedRelationship d
                    : c.getTargetDirectedRelationships(
                            UMLPackage.Literals.GENERALIZATION)) {
                for (Element e : d.getSources()) {
                    if (e instanceof Classifier && !results.contains(e)) {
                        classifiers.add((Classifier) e);
                    }
                }
            }
        }
        results.remove(element);
        return results;
    }

    public Collection<Dependency> getDependencies(
            Object supplierObj,
            Object clientObj) {
        if (!(supplierObj instanceof NamedElement)
                || !(clientObj instanceof NamedElement)) {
            throw new IllegalArgumentException(
                    "supplierObj and clientObj must be "
                    + "instances of NamedElement");
        }
        Collection<Dependency>  result = new ArrayList<Dependency> ();
        for (Dependency d
                : ((NamedElement) clientObj).getClientDependencies()) {
            if (d.getSuppliers().contains(supplierObj)) {
                result.add(d);
            }
        }
        return result;
    }

    public Collection<Classifier> getExtendedClassifiers(Object element) {
        // TODO: Does CoreHelper#getExtendedClassifiers(Object element) means
        // all parents (direct and indirect) or only the direct parents?
        if (!(element instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "'element' must be instance of Classifier");
        }
        return ((Classifier) element).getGenerals();
    }

    public Collection<Element> getExtendingClassifiers(Object classifier) {
        // TODO: Does CoreHelper#getExtendingClassifiers(Object element) means
        // all direct and indirect extending classifiers or only the direct
        // extending classifiers?
        if (!(classifier instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "'classifier' must be instance of Classifier");
        }
        Collection<Element> result = new HashSet<Element>();
        for (Element e : getExtendingElements(classifier)) {
            if (e instanceof Classifier) {
                result.add(e);
            }
        }
        return result;
    }

    public Collection<Element> getExtendingElements(Object element) {
        if (!(element instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "'element' must be instance of Classifier");
        }
        Collection<Element> result = new HashSet<Element>();
        for (DirectedRelationship d
                : ((Classifier) element).getTargetDirectedRelationships(
                        UMLPackage.Literals.GENERALIZATION)) {
            for (Element e : d.getSources()) {
                result.add(e);
            }
        }
        return result;
    }

    public Namespace getFirstSharedNamespace(Object ns1, Object ns2) {
        if (!(ns1 instanceof Namespace) || !(ns2 instanceof Namespace)) {
            throw new IllegalArgumentException(
                    "ns1 and ns2 must be instances of Namespace");
        }
        Namespace result = null;
        List<Namespace> l1 = new ArrayList<Namespace>();
        l1.add((Namespace) ns1);
        l1.addAll(((Namespace) ns1).allNamespaces());
        List<Namespace> l2 = new ArrayList<Namespace>();
        l2.add((Namespace) ns2);
        l2.addAll(((Namespace) ns2).allNamespaces());
        int i = l1.size() - 1;
        int j = l2.size() - 1;
        while (i >= 0 && j >= 0) {
            if (l1.get(i) == l2.get(j)) {
                result = l1.get(i);
                i--;
                j--;
            } else {
                break;
            }
        }
        return result;
    }

    public Collection getFlows(Object source, Object target) {
        // TODO: implement
        throw new NotYetImplementedException();
    }

    public Generalization getGeneralization(Object achild, Object aparent) {
        if (!(achild instanceof Classifier)
                || !(aparent instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "'achild' and 'aparent' must "
                    + "be instances of Classifier");
        }
        return ((Classifier) achild).getGeneralization((Classifier) aparent);
    }

    public Collection<PackageableElement> getUtilizedElements(Object artifact) {
        if (!(artifact instanceof Artifact)) {
            throw new IllegalArgumentException(
                    "'artifact' must be instance of Artifact");
        }
        Collection<PackageableElement> c = new ArrayList<PackageableElement>();
        for (Manifestation m : ((Artifact) artifact).getManifestations()) {
            PackageableElement pe = m.getUtilizedElement();
            if (pe != null) {
                c.add(pe);
            }
        }
        return c;
    }

    public Collection<Operation> getOperationsInh(Object classifier) {
        if (!(classifier instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "'classifier' must be instance of Classifier");
        }
        return ((Classifier) classifier).getAllOperations();
    }

    public Collection<Interface> getRealizedInterfaces(Object cls) {
        if (!(cls instanceof org.eclipse.uml2.uml.Class)) {
            throw new IllegalArgumentException(
                    "'cls' must be instance of UML2 Class");
        }
        return ((org.eclipse.uml2.uml.Class) cls).getImplementedInterfaces();
    }

    public Collection<DirectedRelationship> getRelationships(
            Object source,
            Object dest) {
        if (!(source instanceof Element) || !(dest instanceof Element)) {
            throw new IllegalArgumentException(
                    "'source' and 'dest' must be instances of Element");
        }
        Collection<DirectedRelationship> result =
            new ArrayList<DirectedRelationship>();
        for (DirectedRelationship d : ((Element) source)
                .getSourceDirectedRelationships()) {
            if (d.getTargets().contains(dest)) {
                result.add(d);
            }
        }
        for (DirectedRelationship d : ((Element) source)
                .getTargetDirectedRelationships()) {
            if (d.getSources().contains(dest)) {
                result.add(d);
            }
        }
        return result;
    }

    public List<Parameter> getReturnParameters(Object bf) {
        if (!(bf instanceof BehavioralFeature)) {
            throw new IllegalArgumentException(
                    "'bf' must be instance of BehavioralFeature");
        }
        List<Parameter> result = new ArrayList<Parameter>();
        for (Parameter p : ((Operation) bf).getOwnedParameters()) {
            if (p.getDirection() == ParameterDirectionKind.RETURN_LITERAL) {
                result.add(p);
            }
        }
        return result;
    }

    public Object getSource(Object relationship) {
        // Link does not exist in UML2, a link is represented just as an
        // association

        // TODO: treat Message

        // TODO: not implemented for UML 2 - tfm
        // } else if (relationship instanceof Flow) {
        // Flow flow = (Flow) relationship;
        // Collection col = flow.getSource();
        // if (col.isEmpty()) {
        // return null;
        // }
        // return (col.toArray())[0];

        if (!(relationship instanceof Relationship)
                && !(relationship instanceof Property)) {
            throw new IllegalArgumentException(
                    "'relationship' must be instance "
                    + "of Relationship or Property");
        }

        if (relationship instanceof Association) {
            List<Property> conns = ((Association) relationship).getMemberEnds();
            if (conns.size() < 2) {
                return null;
            }
            return conns.get(1).getType();
        }
        if (relationship instanceof DirectedRelationship) {
            List<Element> sources = ((DirectedRelationship) relationship)
                    .getSources();
            if (sources.isEmpty()) {
                return null;
            }
            return sources.get(0);
        }
        if (relationship instanceof Property) {
            return ((Property) relationship).getAssociation();
        }
        return null;
    }

    public Object getDestination(Object relationship) {
        // Link does not exist in UML2, a link is represented just as an
        // association

        // TODO: treat Message

        // TODO: not implemented for UML 2 - tfm
        // } else if (relationship instanceof Flow) {
        // Flow flow = (Flow) relationship;
        // Collection col = flow.getTarget();
        // if (col.isEmpty()) {
        // return null;
        // }
        // return getFirstItemOrNull(col);

        if (!(relationship instanceof Relationship)
                && !(relationship instanceof Property)) {
            throw new IllegalArgumentException(
                    "'relationship' must be instance "
                    + "of Relationship or Property");
        }

        if (relationship instanceof Association) {
            List<Property> conns = ((Association) relationship).getMemberEnds();
            if (conns.isEmpty()) {
                return null;
            }
            return conns.get(0).getType();
        }
        if (relationship instanceof DirectedRelationship) {
            List<Element> targets =
                ((DirectedRelationship) relationship).getTargets();
            if (targets.isEmpty()) {
                return null;
            }
            return targets.get(0);
        }
        if (relationship instanceof Property) {
            return ((Property) relationship).getAssociation();
        }
        return null;
    }

    public Object getSpecification(Object object) {
        if (!(object instanceof Behavior)) {
            throw new IllegalArgumentException(
                    "'object' must be instance of Behavior");
        }
        return ((Behavior) object).getSpecification();
    }

    public Collection<Element> getSubtypes(Object cls) {
        if (!(cls instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "'cls' must be instance of Classifier");
        }
        Collection<Element> results = new HashSet<Element>();
        for (DirectedRelationship d
                : ((Classifier) cls).getTargetDirectedRelationships(
                        UMLPackage.Literals.GENERALIZATION)) {
            results.addAll(d.getSources());
        }
        return results;
    }

    public Collection<Classifier> getSupertypes(Object generalizableElement) {
        if (!(generalizableElement instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "'generalizableElement' must be instance of Classifier");
        }
        return ((Classifier) generalizableElement).getGenerals();
    }

    public boolean hasCompositeEnd(Object association) {
        if (!(association instanceof Association)) {
            throw new IllegalArgumentException(
                    "'association' must be instance of Association");
        }
        for (Property p : ((Association) association).getMemberEnds()) {
            if (p.getAggregation() == AggregationKind.COMPOSITE_LITERAL) {
                return true;
            }
        }
        return false;
    }

    public boolean isSubType(Object type, Object subType) {
        if (!(type instanceof Class) || !(subType instanceof Class)) {
            throw new IllegalArgumentException(
                    "type and subType must be instances of java.lang.Class");
        }
        return ((Class) type).isAssignableFrom((Class) subType);
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
            getRunnableClassForAddCommand(
                    (Namespace) namespace, (NamedElement) element);
        } catch (UnsupportedOperationException e) {
            return false;
        }
        return true;
    }

    public void removeAnnotatedElement(final Object comment,
            final Object annotatedElement) {
        if (!(annotatedElement instanceof Element)) {
            throw new IllegalArgumentException(
                    "annotatedElement must be instance of Element");
        }
        if (!(comment instanceof Comment)) {
            throw new IllegalArgumentException(
                    "comment must be instance of Comment");
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                ((Comment) comment).getAnnotatedElements().remove(
                        (Element) annotatedElement);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl,
                        run,
                        "Remove the link between the comment # "
                        + "and the element #",
                        comment, annotatedElement));
    }

    public void removeClientDependency(final Object handle, final Object dep) {
        if (!(handle instanceof NamedElement)) {
            throw new IllegalArgumentException(
                    "handle must be instance of NamedElement");
        }
        if (!(dep instanceof Dependency)) {
            throw new IllegalArgumentException(
                    "dep must be instance of Dependency");
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                ((NamedElement) handle).getClientDependencies().remove(dep);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run,
                        "Remove the client dependency # from the element #",
                        handle, dep));
    }

    public void removeConnection(final Object handle, final Object connection) {
        if (!(handle instanceof Association)) {
            throw new IllegalArgumentException(
                    "handle must be instance of Association");
        }
        if (!(connection instanceof Property)) {
            throw new IllegalArgumentException(
                    "connection must be instance of Property");
        }

        final Association association = (Association) handle;
        RunnableClass run = new RunnableClass() {
            public void run() {
                if (association.getOwnedEnds().contains(connection)) {
                    association.getOwnedEnds().remove(connection);
                }
                if (((Property) connection).getAssociation() == handle) {
                    ((Property) connection).setAssociation(null);
                }
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run,
                        "Remove the association end # from the association #",
                        connection, handle));
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
        removeOwnedElement(cls, feature);
    }

    public void removeLiteral(Object enumeration, Object literal) {
        removeOwnedElement(enumeration, literal);
    }

    public void removeOwnedElement(Object handle, Object value) {
        if (!(handle instanceof Element)) {
            throw new IllegalArgumentException(
                    "handle must be instance of Element");
        }
        if (!(value instanceof Element)) {
            throw new IllegalArgumentException(
                    "value must be instance of Element");
        }
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl,
                        getRunnableClassForRemoveCommand(
                                (Element) value),
                                "Remove the element # from the owner #",
                                value,
                                handle));
    }

    public void removeParameter(Object handle, Object parameter) {
        removeOwnedElement(handle, parameter);
    }

    public void removeQualifier(Object handle, Object qualifier) {
        removeOwnedElement(handle, qualifier);
    }

    public void removeSourceFlow(Object handle, Object flow) {
        throw new NotYetImplementedException();
    }


    public void removeStereotype(
            final Object modelElement,
            final Object stereo) {
        UMLUtil.checkArgs(new Object[] {modelElement, stereo},
                new Class[] {Element.class, Stereotype.class});
        RunnableClass run = new RunnableClass() {
            public void run() {
                Stereotype stereotype = (Stereotype) stereo;
                ((Element) modelElement).unapplyStereotype(stereotype);
                fireUnapplyStereotypeEvent(modelElement, stereotype);
            }
            /**
             * Call the model event pump and ask it to fire an event indicating
             * a stereotype has been removed. This is a stop-gap until we have
             * determined how the event pump can detect itself that a stereotype
             * has been removed.
             *
             * @param modelElement
             * @param stereotype
             */
            private void fireUnapplyStereotypeEvent(
                    Object modelElement,
                    Object stereotype) {
                final ModelEventPumpEUMLImpl pump =
                    (ModelEventPumpEUMLImpl) Model.getPump();
                pump.fireEvent(
                        modelElement,
                        stereotype,
                        null,
                        Notification.REMOVE,
                        "stereotype",
                        null);
            }

        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run,
                        "Remove the stereotype # from the element #",
                        stereo, modelElement));
    }

    public void removeSupplierDependency(final Object supplier,
            final Object dependency) {
        if (!(supplier instanceof NamedElement)) {
            throw new IllegalArgumentException(
                    "supplier must be instance of NamedElement");
        }
        if (!(dependency instanceof Dependency)) {
            throw new IllegalArgumentException(
                    "dependency must be instance of Dependency");
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                ((Dependency) dependency).getSuppliers().remove(supplier);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run,
                        "Remove the supplier # from the dependency #",
                        supplier, dependency));
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

    public void setAbstract(final Object handle, final boolean isAbstract) {
        if (!(handle instanceof Classifier)
                && !(handle instanceof BehavioralFeature)) {
            throw new IllegalArgumentException(
                    "handle must be instance "
                    + "of Classifier or BehavioralFeature");
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                if (handle instanceof Classifier) {
                    ((Classifier) handle).setIsAbstract(isAbstract);
                } else if (handle instanceof BehavioralFeature) {
                    ((BehavioralFeature) handle).setIsAbstract(isAbstract);
                }
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run, "Set isAbstract to # for #",
                        isAbstract, handle));
    }

    public void setActive(final Object handle, final boolean isActive) {
        if (!(handle instanceof org.eclipse.uml2.uml.Class)) {
            throw new IllegalArgumentException(
                    "handle must be instance of UML2 Class");
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                ((org.eclipse.uml2.uml.Class) handle).setIsActive(isActive);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run, "Set isActive to # for #", isActive,
                        handle));
    }

    public void setAggregation(
            final Object handle,
            final Object aggregationKind) {
        setAggregation2(handle, aggregationKind);
    }

    public void setAggregation1(
            final Object handle,
            final Object aggregationKind) {
        Property p = (Property) handle;
        Association ass = p.getAssociation();
        Collection assEnds = modelImpl.getFacade().getConnections(ass);
        Iterator it = assEnds.iterator();
        Object other = it.next();
        if (other == handle) {
            other = it.next();
        }

        setAggregation2(handle, aggregationKind);
    }

    public void setAggregation2(
            final Object handle,
            final Object aggregationKind) {
        if (!(handle instanceof Property)) {
            throw new IllegalArgumentException(
                    "handle must be instance of Property");
        }
        if (!(aggregationKind instanceof AggregationKind)) {
            throw new IllegalArgumentException(
                    "aggregationKind must be instance of AggregationKind "
                    + aggregationKind + " recieved");
        }
        final Property property = (Property) handle;
        final AggregationKind aggregation = (AggregationKind) aggregationKind;
        RunnableClass run = new RunnableClass() {
            public void run() {
                property.setAggregation(aggregation);
                if (aggregation == AggregationKind.COMPOSITE_LITERAL
                        || aggregation == AggregationKind.SHARED_LITERAL) {
                    for (Property end
                            : property.getAssociation().getMemberEnds()) {
                        if (!end.equals(property)) {
                            end.setAggregation(AggregationKind.NONE_LITERAL);
                        }
                    }
                }
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run,
                        "Set the aggregation # to the association end #",
                        aggregationKind, handle));
    }


    public void setAnnotatedElements(
            final Object handle,
            final Collection elems) {
        if (!(handle instanceof Comment)) {
            throw new IllegalArgumentException(
                    "handle must be instance of Comment");
        }
        if (elems == null) {
            throw new NullPointerException("elems must be non-null");
        }
        for (Object o : elems) {
            if (!(o instanceof Element)) {
                throw new IllegalArgumentException(
                        "the collection must contain "
                        + "only instances of Element");
            }
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                ((Comment) handle).getAnnotatedElements().clear();
                for (Object o : elems) {
                    ((Comment) handle).getAnnotatedElements().add((Element) o);
                }
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run,
                        "Set # annotated alements for the comment #",
                        elems.size(), handle));
    }

    public void setAssociation(Object handle, Object association) {
        throw new NotYetImplementedException();
    }

    public void setAttributes(Object classifier, List attributes) {
        throw new NotYetImplementedException();
    }

    public void setBody(Object handle, Object expr) {
        // must not be used in UML2; we model a method as a
        // OpaqueBehavior object that already has a body, so there is no need
        // to have an intermediate expression object
        throw new NotImplementedException();
    }

    public void setBody(Object handle, String body) {

	if (handle instanceof Comment) {
	    ((Comment) handle).setBody(body);
	    return;
	}

	if (handle instanceof Operation) {

	    // We need a method (operation implementation)
	    // to store the method body.
	    OpaqueBehavior methodImpl = null;

	    // Maybe this operation already has a method, that fits our purpose?
	    // In this case, try to reuse it, instead of creating a new
	    // implementation.
	    for (Behavior impl : ((Operation) handle).getMethods()) {
		if (impl instanceof OpaqueBehavior) {
		    methodImpl = (OpaqueBehavior) impl;
		    break;
		}
	    }

	    // Check, if we have to create a new implementation.
	    if (methodImpl == null) {
	        // Create a new implementation.
		methodImpl = UMLFactory.eINSTANCE.createOpaqueBehavior();

		// And set the specification to the current operation.
		methodImpl.setSpecification((Operation) handle);

	        // Add it to the operation's methods.
		((Operation) handle).getMethods().add(methodImpl);
	    }

	    // Look, if there's already a java implementation
	    if (methodImpl.isSetLanguages()) {
		int bodyIndex = 0;

		// Search for our current target language.
                for (String language : methodImpl.getLanguages()) {
                    if ("java".equals(language)) {

                        // Try to get the corresponding body and set it
                        // to the current body
                        // This _should_ work, if all the bodies
                        // were stored with their corresponding languages.
                        methodImpl.getBodies().set(bodyIndex, body);
			return;		// Job done.
		    }
		    bodyIndex++;
		}
	    }

            // It seems, there was no implementation of
            // our current target language, so we just add one.
            methodImpl.getLanguages().add("java");
            methodImpl.getBodies().add(body);
	    return;
	}

	// We cannot set the body of this model element type.
	throw new IllegalArgumentException();
    }

    @Deprecated
    public void setChangeability(Object handle, Object ck) {
        throw new NotImplementedException();
    }


    public void setChild(final Object handle, final Object child) {
        if (!(handle instanceof Generalization)) {
            throw new IllegalArgumentException(
                    "handle must be instance of Generalization");
        }
        if (!(child instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "child must be instance of Classifier");
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                ((Generalization) handle).setSpecific((Classifier) child);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl,
                        run,
                        "Set the # as the specific classifier "
                        + "of the generalization #",
                        child, handle));
    }

    public void setConcurrency(
            final Object handle,
            final Object concurrencyKind) {
        UMLUtil.checkArgs(new Object[] {handle, concurrencyKind},
                new Class[] {Element.class, CallConcurrencyKind.class});
        RunnableClass run = new RunnableClass() {
            public void run() {
                ((Operation) handle).setConcurrency(
                        (CallConcurrencyKind) concurrencyKind);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl,
                        run,
                        "Set the concurrencyKind # of the element #",
                        concurrencyKind, handle));
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
        ((Property) attribute).setDefaultValue((ValueSpecification) expression);
    }

    public void setKind(Object handle, Object kind) {
        // TODO: Needs undo support
        if (handle instanceof Parameter
                && kind instanceof ParameterDirectionKind) {
            ((Parameter) handle).setDirection((ParameterDirectionKind) kind);
            return;
	}
        if (handle instanceof Pseudostate && kind instanceof PseudostateKind) {
            ((Pseudostate) handle).setKind((PseudostateKind) kind);
            return;
	}
        throw new IllegalArgumentException( "handle: " + handle
                + " or kind: " + kind);
    }

    public void setLeaf(final Object handle, final boolean isLeaf) {
        if (!(handle instanceof RedefinableElement)) {
            throw new IllegalArgumentException(
                    "handle must be instance of RedefinableElement");
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                ((RedefinableElement) handle).setIsLeaf(isLeaf);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl,
                        run,
                        "Set isLeaf to # for #",
                        isLeaf,
                        handle));
    }

    public void setModelElementContainer(Object handle, Object container) {
        // TODO: This method is mostly (entirely?) redundant - tfm
        addOwnedElement(container, handle);
    }

    @Deprecated
    public void setMultiplicity(final Object handle, Object arg) {
        throw new NotImplementedException();
    }

    public void setMultiplicity(final Object handle, String arg) {
        if (!(handle instanceof MultiplicityElement)) {
            throw new IllegalArgumentException(
                    "A MultiplicityElement was expected");
        }
        if (arg == null || arg.equals("")) {
            RunnableClass run = new RunnableClass() {
                public void run() {
                    ((MultiplicityElement) handle).setLowerValue(null);
                    ((MultiplicityElement) handle).setUpperValue(null);
                }
            };
            editingDomain.getCommandStack().execute(
                    new ChangeCommand(
                            modelImpl, run,
                            "Removing the multiplicity from element #",
                            handle));
            return;
        }

        int[] range = parseMultiplicity(arg);
        setMultiplicity(handle, range[0], range[1]);

    }

    private int[] parseMultiplicity(String arg) {
        int lower = 1, upper = 1;

        if ("*".equals(arg.trim())) {
            lower = 0;
            upper = -1;
        } else if (arg.contains("..")) {
            String[] pieces = arg.trim().split("\\.\\.");
            if (pieces.length > 2) {
                throw new IllegalArgumentException((String) arg);
            }
            lower = Integer.parseInt(pieces[0]);
            if ("*".equals(pieces[1])) {
                upper = -1;
            } else {
                upper = Integer.parseInt(pieces[1]);
            }
        } else if (arg.contains("_")) {
            // also parse 1_* or 0_N etc.
            String[] pieces = arg.trim().split("_");
            if (pieces.length > 2) {
                throw new IllegalArgumentException((String) arg);
            }
            lower = Integer.parseInt(pieces[0]);
            if ("*".equals(pieces[1])
                    || "N".equals(pieces[1])) {
                upper = -1;
            } else {
                upper = Integer.parseInt(pieces[1]);
            }
        } else {
            lower = Integer.parseInt(arg);
            upper = lower;
        }
        return new int[] {lower, upper};
    }

    public void setMultiplicity(
            final Object handle,
            final int lower,
            final int upper) {

        RunnableClass run = new RunnableClass() {
            public void run() {
                // TODO: We currently delete the old values before setting
                // to something new. This is a workaround to issue 6056.
                // We should consider giving an API to get the lower and
                // upper values so that controls can listen directly to
                // those rather than the element containing those values.
                ((MultiplicityElement) handle).setLowerValue(null);
                ((MultiplicityElement) handle).setUpperValue(null);
                //
                ((MultiplicityElement) handle).setLower(lower);
                ((MultiplicityElement) handle).setUpper(upper);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run,
                        "Set the multiplicity #..# to the element #",
                        lower, upper, handle));
    }


    public void setName(final Object handle, final String name) {
        if (!(handle instanceof NamedElement)) {
            if (handle instanceof Generalization) {
                LOG.log(Level.WARNING, "Attempting to set the name of a generalization "
                        + "which is no longer a NamedElement in UML 2" + name
                        + handle.toString());
                return;
            }
            throw new IllegalArgumentException(
                    "handle must be instance of NamedElement");
        }
        if (name == null) {
            throw new NullPointerException("name must be non-null");
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                ((NamedElement) handle).setName(name);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run,
                        "Set the name \"#\" to the named element #",
                        name, handle));
    }

    public void setNamespace(Object handle, Object ns) {
        addOwnedElement(ns, handle);
    }

    public void setNavigable(final Object handle, final boolean flag) {
        if (!(handle instanceof Property)) {
            throw new IllegalArgumentException(
                    "handle must be instance of Property");
        }
        final Property prop = (Property) handle;
        if (flag == prop.isNavigable()) {
            return;
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                // WARNING - This has containment side effects!
                // Eclipse UML2 will move the Property from the Classifier to
                // the Association when the navigability is changed.
                if (!flag) {
                    // Because of this side effect we add the element to
                    // a special list of elements that we do not create
                    // a delete event for. See issue 5853.
                    ModelEventPumpEUMLImpl pump =
                        (ModelEventPumpEUMLImpl) Model.getPump();
                    pump.addElementForDeleteEventIgnore(prop);
                }
                prop.setIsNavigable(flag);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run,
                        "Set isNavigable to # for the association end #", flag,
                        handle));
    }

    public void setOperations(Object classifier, List operations) {
        throw new NotYetImplementedException();
    }

    public void setOrdering(Object handle, Object ordering) {
        ((MultiplicityElement) handle).setIsOrdered(
                OrderingKindEUMLImpl.ORDERED.equals(ordering));
    }

    public void setOwner(Object handle, Object owner) {
        throw new NotYetImplementedException();
    }

    public void setParameter(Object handle, Object parameter) {
        throw new NotYetImplementedException();
    }

    public void setParameters(Object handle, Collection parameters) {
        throw new NotYetImplementedException();
    }

    public void setParent(final Object handle, final Object parent) {
        if (!(handle instanceof Generalization)) {
            throw new IllegalArgumentException(
                    "handle must be instance of Generalization");
        }
        if (!(parent instanceof Classifier)) {
            throw new IllegalArgumentException(
                    "parent must be instance of Classifier");
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                ((Generalization) handle).setGeneral((Classifier) parent);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl,
                        run,
                        "Set the # as the general classifier "
                        + "of the generalization #",
                        parent, handle));
    }

    public void setPowertype(Object handle, Object powerType) {
        throw new NotYetImplementedException();
    }

    public void setQualifiers(Object handle, List qualifiers) {
        throw new NotYetImplementedException();
    }

    public void setQuery(final Object handle, final boolean isQuery) {
        if (!(handle instanceof Operation)) {
            throw new IllegalArgumentException(
                    "handle must be instance of Operation");
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                ((Operation) handle).setIsQuery(isQuery);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run, "Set isQuery to # for the operation #",
                        isQuery, handle));
    }

    public void setRaisedSignals(Object handle, Collection raisedSignals) {
        throw new NotYetImplementedException();
    }

    public void setReadOnly(final Object handle, final boolean isReadOnly) {
        if (!(handle instanceof StructuralFeature)) {
            throw new IllegalArgumentException(
                    "handle must be instance of StructuralFeature");
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                ((StructuralFeature) handle).setIsReadOnly(isReadOnly);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run,
                        "Set isReadOnly to # for the structural feature #",
                        isReadOnly, handle));
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

    public void setStatic(final Object feature, final boolean isStatic) {
        if (!(feature instanceof Feature)) {
            throw new IllegalArgumentException(
                    "feature must be instance of Feature");
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                ((Feature) feature).setIsStatic(isStatic);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run, "Set isStatic to # for the feature #",
                        isStatic, feature));
    }

    @Deprecated
    public void setTaggedValue(Object handle, String tag, String value) {
        throw new NotYetImplementedException();
    }

    @Deprecated
    public void setTargetScope(Object handle, Object targetScope) {
        // Don't implement - deprecated method in interface.
        throw new NotImplementedException();
    }

    public void setType(final Object handle, final Object type) {
        if (!(handle instanceof TypedElement)
                && !(handle instanceof Operation)) {
            throw new IllegalArgumentException(
                    "handle must be instance of TypedElement");
        }
        if (type != null && !(type instanceof Type)) {
            throw new IllegalArgumentException("type must be instance of Type");
        }
        final TypedElement typedElement;
        if (handle instanceof Operation) {
            typedElement = (TypedElement) getReturnParameters(handle).get(0);
        } else {
            typedElement = (TypedElement) handle;
        }

        RunnableClass run = new RunnableClass() {
            public void run() {
                typedElement.setType((Type) type);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run,
                        "Set the type # for the typed element #",
                        type,
                        handle));
    }

    public void setVisibility(final Object handle, final Object visibility) {
        if (!(handle instanceof NamedElement)) {
            throw new IllegalArgumentException(
                    "handle must be instance of NamedElement");
        }
        if (!(visibility instanceof VisibilityKind)) {
            throw new IllegalArgumentException(
                    "visibility must be instance of VisibilityKind");
        }

        final NamedElement namedElement = (NamedElement) handle;
        RunnableClass run = new RunnableClass() {
            public void run() {
                namedElement.setVisibility((VisibilityKind) visibility);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(
                        modelImpl, run,
                        "Set the visibility # to the named element #",
                        visibility, handle));
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
