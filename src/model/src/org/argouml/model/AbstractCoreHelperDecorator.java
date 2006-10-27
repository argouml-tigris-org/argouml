// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An abstract Decorator for the {@link CoreHelper}.
 *
 * @author Bob Tarling
 */
public abstract class AbstractCoreHelperDecorator implements CoreHelper {

    /**
     * The delegate.
     */
    private CoreHelper impl;

    /**
     * Construct a new AbstractCoreHelperDecorator.
     *
     * @param component The component to decorate.
     */
    AbstractCoreHelperDecorator(CoreHelper component) {
        impl = component;
    }

    /**
     * The component we are decorating.
     *
     * @return Returns the component.
     */
    protected CoreHelper getComponent() {
        return impl;
    }

    /**
     * @see org.argouml.model.CoreHelper#clearStereotypes(
     *         java.lang.Object, java.lang.Object)
     */
    public void clearStereotypes(Object modelElement) {
        impl.clearStereotypes(modelElement);
    }

    /**
     * @see org.argouml.model.CoreHelper#isSubType(
     *         java.lang.Object, java.lang.Object)
     */
    public boolean isSubType(Object type, Object subType) {
        return impl.isSubType(type, subType);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllSupertypes(java.lang.Object)
     */
    public Collection getAllSupertypes(Object cls1) {
        return impl.getAllSupertypes(cls1);
    }

    /**
     * @see org.argouml.model.CoreHelper#getSupertypes(java.lang.Object)
     */
    public Collection getSupertypes(Object generalizableElement) {
        return impl.getSupertypes(generalizableElement);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAssociateEnds(java.lang.Object)
     */
    public Collection getAssociateEnds(Object classifier) {
        return impl.getAssociateEnds(classifier);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAssociateEndsInh(java.lang.Object)
     */
    public Collection getAssociateEndsInh(Object classifier1) {
        return impl.getAssociateEndsInh(classifier1);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeFeature(
     *         java.lang.Object, java.lang.Object)
     */
    public void removeFeature(Object cls, Object feature) {
        impl.removeFeature(cls, feature);
    }

    /**
     * @see org.argouml.model.CoreHelper#getOperations(java.lang.Object)
     */
    public List getOperations(Object classifier) {
        return impl.getOperations(classifier);
    }

    /**
     * @see org.argouml.model.CoreHelper#setOperations(
     *         java.lang.Object, java.util.Collection)
     */
    public void setOperations(Object classifier, Collection operations) {
        List operList = new ArrayList();
        operList.addAll(operations);
        impl.setOperations(classifier, operList);
    }

    /**
     * @see org.argouml.model.CoreHelper#setOperations(
     *         java.lang.Object, java.util.List)
     */
    public void setOperations(Object classifier, List operations) {
        impl.setOperations(classifier, operations);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAttributes(java.lang.Object)
     */
    public List getAttributes(Object classifier) {
        return impl.getAttributes(classifier);
    }

    /**
     * @see org.argouml.model.CoreHelper#setAttributes(
     *         java.lang.Object, java.util.Collection)
     */
    public void setAttributes(Object classifier, Collection attributes) {
        List attrList = new ArrayList();
        attrList.addAll(attributes);
        impl.setAttributes(classifier, attrList);
    }

    /**
     * @see org.argouml.model.CoreHelper#setAttributes(
     *         java.lang.Object, java.util.List)
     */
    public void setAttributes(Object classifier, List attributes) {
        impl.setAttributes(classifier, attributes);
    }
    
    /**
     * @see org.argouml.model.CoreHelper#getAttributesInh(java.lang.Object)
     */
    public Collection getAttributesInh(Object classifier) {
        return impl.getAttributesInh(classifier);
    }

    /**
     * @see org.argouml.model.CoreHelper#getOperationsInh(java.lang.Object)
     */
    public Collection getOperationsInh(Object classifier) {
        return impl.getOperationsInh(classifier);
    }

    /**
     * @see org.argouml.model.CoreHelper#getReturnParameters(java.lang.Object)
     */
    public List getReturnParameters(Object operation) {
        return impl.getReturnParameters(operation);
    }

    /**
     * @see org.argouml.model.CoreHelper#getSpecification(java.lang.Object)
     */
    public Object getSpecification(Object object) {
        return impl.getSpecification(object);
    }

    /**
     * @see org.argouml.model.CoreHelper#getSpecifications(java.lang.Object)
     */
    public Collection getSpecifications(Object classifier) {
        return impl.getSpecifications(classifier);
    }

    /**
     * @see org.argouml.model.CoreHelper#getSubtypes(java.lang.Object)
     */
    public Collection getSubtypes(Object cls) {
        return impl.getSubtypes(cls);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllBehavioralFeatures(
     *         java.lang.Object)
     */
    public Collection getAllBehavioralFeatures(Object element) {
        return impl.getAllBehavioralFeatures(element);
    }

    /**
     * @see org.argouml.model.CoreHelper#getBehavioralFeatures(
     *         java.lang.Object)
     */
    public List getBehavioralFeatures(Object clazz) {
        return impl.getBehavioralFeatures(clazz);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllInterfaces(java.lang.Object)
     */
    public Collection getAllInterfaces(Object ns) {
        return impl.getAllInterfaces(ns);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllClasses(java.lang.Object)
     */
    public Collection getAllClasses(Object ns) {
        return impl.getAllClasses(ns);
    }

    /**
     * @see org.argouml.model.CoreHelper#getRealizedInterfaces(
     *         java.lang.Object)
     */
    public Collection getRealizedInterfaces(Object cls) {
        return impl.getRealizedInterfaces(cls);
    }

    /**
     * @see org.argouml.model.CoreHelper#getExtendedClassifiers(
     *         java.lang.Object)
     */
    public Collection getExtendedClassifiers(Object clazz) {
        return impl.getExtendedClassifiers(clazz);
    }

    /**
     * @see org.argouml.model.CoreHelper#getGeneralization(
     *         java.lang.Object, java.lang.Object)
     */
    public Object getGeneralization(Object child, Object parent) {
        return impl.getGeneralization(child, parent);
    }

    /**
     * @see org.argouml.model.CoreHelper#getBody(java.lang.Object)
     */
    public String getBody(Object comment) {
        return impl.getBody(comment);
    }

    /**
     * @see org.argouml.model.CoreHelper#getFlows(
     *         java.lang.Object, java.lang.Object)
     */
    public Collection getFlows(Object source, Object target) {
        return impl.getFlows(source, target);
    }

    /**
     * @see org.argouml.model.CoreHelper#getExtendingElements(java.lang.Object)
     */
    public Collection getExtendingElements(Object clazz) {
        return impl.getExtendingElements(clazz);
    }

    /**
     * @see org.argouml.model.CoreHelper#getExtendingClassifiers(
     *         java.lang.Object)
     */
    public Collection getExtendingClassifiers(Object clazz) {
        return impl.getExtendingClassifiers(clazz);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllComponents(java.lang.Object)
     */
    public Collection getAllComponents(Object ns) {
        return impl.getAllComponents(ns);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllDataTypes(java.lang.Object)
     */
    public Collection getAllDataTypes(Object ns) {
        return impl.getAllDataTypes(ns);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllNodes(java.lang.Object)
     */
    public Collection getAllNodes(Object ns) {
        return impl.getAllNodes(ns);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAssociatedClassifiers(
     *         java.lang.Object)
     */
    public Collection getAssociatedClassifiers(Object classifier) {
        return impl.getAssociatedClassifiers(classifier);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAssociations(
     *         java.lang.Object, java.lang.Object)
     */
    public Collection getAssociations(Object from, Object to) {
        return impl.getAssociations(from, to);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllClassifiers(java.lang.Object)
     */
    public Collection getAllClassifiers(Object namespace) {
        return impl.getAllClassifiers(namespace);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAssociations(java.lang.Object)
     */
    public Collection getAssociations(Object classifier) {
        return impl.getAssociations(classifier);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAssociationEnd(
     *         java.lang.Object, java.lang.Object)
     */
    public Object getAssociationEnd(Object type, Object assoc) {
        return impl.getAssociationEnd(type, assoc);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllContents(java.lang.Object)
     */
    public Collection getAllContents(Object clazz) {
        return impl.getAllContents(clazz);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllAttributes(java.lang.Object)
     */
    public Collection getAllAttributes(Object clazz) {
        return impl.getAllAttributes(clazz);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllVisibleElements(java.lang.Object)
     */
    public Collection getAllVisibleElements(Object ns) {
        return impl.getAllVisibleElements(ns);
    }

    /**
     * @see org.argouml.model.CoreHelper#getSource(java.lang.Object)
     */
    public Object getSource(Object relationship) {
        return impl.getSource(relationship);
    }

    /**
     * @see org.argouml.model.CoreHelper#getDestination(java.lang.Object)
     */
    public Object getDestination(Object relationship) {
        return impl.getDestination(relationship);
    }

    /**
     * @see org.argouml.model.CoreHelper#getDependencies(
     *         java.lang.Object, java.lang.Object)
     */
    public Collection getDependencies(Object supplierObj, Object clientObj) {
        return impl.getDependencies(supplierObj, clientObj);
    }

    /**
     * @see org.argouml.model.CoreHelper#getRelationships(
     *         java.lang.Object, java.lang.Object)
     */
    public Collection getRelationships(Object source, Object dest) {
        return impl.getRelationships(source, dest);
    }

    /**
     * @see org.argouml.model.CoreHelper#isValidNamespace(
     *         java.lang.Object, java.lang.Object)
     */
    public boolean isValidNamespace(Object mObj, Object nsObj) {
        return impl.isValidNamespace(mObj, nsObj);
    }

    /**
     * @see org.argouml.model.CoreHelper#getFirstSharedNamespace(
     *         java.lang.Object, java.lang.Object)
     */
    public Object getFirstSharedNamespace(Object ns1, Object ns2) {
        return impl.getFirstSharedNamespace(ns1, ns2);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllPossibleNamespaces(
     *         java.lang.Object, java.lang.Object)
     */
    public Collection getAllPossibleNamespaces(Object modelElement,
					       Object model) {
        return impl.getAllPossibleNamespaces(modelElement, model);
    }

    /**
     * @see org.argouml.model.CoreHelper#getChildren(java.lang.Object)
     */
    public Collection getChildren(Object o) {
        return impl.getChildren(o);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllRealizedInterfaces(
     *         java.lang.Object)
     */
    public Collection getAllRealizedInterfaces(Object o) {
        return impl.getAllRealizedInterfaces(o);
    }

    /**
     * @see org.argouml.model.CoreHelper#hasCompositeEnd(java.lang.Object)
     */
    public boolean hasCompositeEnd(Object association) {
        return impl.hasCompositeEnd(association);
    }

    /**
     * @see org.argouml.model.CoreHelper#equalsAggregationKind(
     *         java.lang.Object, java.lang.String)
     */
    public boolean equalsAggregationKind(Object associationEnd,
					 String kindType) {
        return impl.equalsAggregationKind(associationEnd, kindType);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeAnnotatedElement(
     *         java.lang.Object, java.lang.Object)
     */
    public void removeAnnotatedElement(Object handle, Object me) {
        impl.removeAnnotatedElement(handle, me);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeClientDependency(
     *         java.lang.Object, java.lang.Object)
     */
    public void removeClientDependency(Object handle, Object dep) {
        impl.removeClientDependency(handle, dep);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeConstraint(
     *         java.lang.Object, java.lang.Object)
     */
    public void removeConstraint(Object handle, Object cons) {
        impl.removeConstraint(handle, cons);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeOwnedElement(
     *         java.lang.Object, java.lang.Object)
     */
    public void removeOwnedElement(Object handle, Object value) {
        impl.removeOwnedElement(handle, value);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeParameter(
     *         java.lang.Object, java.lang.Object)
     */
    public void removeParameter(Object handle, Object parameter) {
        impl.removeParameter(handle, parameter);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeSourceFlow(
     *         java.lang.Object, java.lang.Object)
     */
    public void removeSourceFlow(Object handle, Object flow) {
        impl.removeSourceFlow(handle, flow);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeSupplierDependency(
     *         java.lang.Object, java.lang.Object)
     */
    public void removeSupplierDependency(Object supplier, Object dependency) {
        impl.removeSupplierDependency(supplier, dependency);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeStereotype(
     *         java.lang.Object, java.lang.Object)
     */
    public void removeStereotype(Object modelElement, Object stereotype) {
        impl.removeStereotype(modelElement, stereotype);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeTaggedValue(
     *         java.lang.Object, java.lang.String)
     */
    public void removeTaggedValue(Object handle, String name) {
        impl.removeTaggedValue(handle, name);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeTargetFlow(
     *         java.lang.Object, java.lang.Object)
     */
    public void removeTargetFlow(Object handle, Object flow) {
        impl.removeTargetFlow(handle, flow);
    }

    /**
     * @see org.argouml.model.CoreHelper#addAnnotatedElement(
     *         java.lang.Object, java.lang.Object)
     */
    public void addAnnotatedElement(Object comment, Object annotatedElement) {
        impl.addAnnotatedElement(comment, annotatedElement);
    }

    /**
     * @see org.argouml.model.CoreHelper#addClient(
     *         java.lang.Object, java.lang.Object)
     */
    public void addClient(Object handle, Object element) {
        impl.addClient(handle, element);
    }

    /**
     * @see org.argouml.model.CoreHelper#addClientDependency(
     *         java.lang.Object, java.lang.Object)
     */
    public void addClientDependency(Object handle, Object dependency) {
        impl.addClientDependency(handle, dependency);
    }

    /**
     * @see org.argouml.model.CoreHelper#addComment(
     *         java.lang.Object, java.lang.Object)
     */
    public void addComment(Object element, Object comment) {
        impl.addComment(element, comment);
    }

    /**
     * @see org.argouml.model.CoreHelper#addConnection(
     *         java.lang.Object, java.lang.Object)
     */
    public void addConnection(Object handle, Object connection) {
        impl.addConnection(handle, connection);
    }

    /**
     * @see org.argouml.model.CoreHelper#addConstraint(
     *         java.lang.Object, java.lang.Object)
     */
    public void addConstraint(Object handle, Object mc) {
        impl.addConstraint(handle, mc);
    }

    /**
     * @see org.argouml.model.CoreHelper#addDeploymentLocation(
     *         java.lang.Object, java.lang.Object)
     */
    public void addDeploymentLocation(Object handle, Object node) {
        impl.addDeploymentLocation(handle, node);
    }

    /**
     * @see org.argouml.model.CoreHelper#addFeature(
     *         java.lang.Object, int, java.lang.Object)
     */
    public void addFeature(Object handle, int index, Object f) {
        impl.addFeature(handle, index, f);
    }

    /**
     * @see org.argouml.model.CoreHelper#addFeature(
     *         java.lang.Object, java.lang.Object)
     */
    public void addFeature(Object handle, Object f) {
        impl.addFeature(handle, f);
    }

    /**
     * @see org.argouml.model.CoreHelper#addLink(
     *         java.lang.Object, java.lang.Object)
     */
    public void addLink(Object handle, Object link) {
        impl.addLink(handle, link);
    }

    /**
     * @see org.argouml.model.CoreHelper#addMethod(
     *         java.lang.Object, java.lang.Object)
     */
    public void addMethod(Object handle, Object m) {
        impl.addMethod(handle, m);
    }

    /**
     * @see org.argouml.model.CoreHelper#addOwnedElement(
     *         java.lang.Object, java.lang.Object)
     */
    public void addOwnedElement(Object handle, Object me) {
        impl.addOwnedElement(handle, me);
    }

    /**
     * @see org.argouml.model.CoreHelper#addParameter(
     *         java.lang.Object, int, java.lang.Object)
     */
    public void addParameter(Object handle, int index, Object parameter) {
        impl.addParameter(handle, index, parameter);
    }

    /**
     * @see org.argouml.model.CoreHelper#addParameter(
     *         java.lang.Object, java.lang.Object)
     */
    public void addParameter(Object handle, Object parameter) {
        impl.addParameter(handle, parameter);
    }

    /**
     * @see org.argouml.model.CoreHelper#addRaisedSignal(
     *         java.lang.Object, java.lang.Object)
     */
    public void addRaisedSignal(Object handle, Object sig) {
        impl.addRaisedSignal(handle, sig);
    }

    /**
     * @see org.argouml.model.CoreHelper#addSourceFlow(
     *         java.lang.Object, java.lang.Object)
     */
    public void addSourceFlow(Object handle, Object flow) {
        impl.addSourceFlow(handle, flow);
    }

    /**
     * @see org.argouml.model.CoreHelper#addAllStereotypes(java.lang.Object, java.util.Collection)
     */
    public void addAllStereotypes(Object modelElement, Collection stereotypes) {
        impl.addStereotype(modelElement, stereotypes);
    }

    /**
     * @see org.argouml.model.CoreHelper#addStereotype(
     *         java.lang.Object, java.lang.Object)
     */
    public void addStereotype(Object modelElement, Object stereotype) {
        impl.addStereotype(modelElement, stereotype);
    }

    /**
     * @see org.argouml.model.CoreHelper#addSupplier(
     *         java.lang.Object, java.lang.Object)
     */
    public void addSupplier(Object handle, Object element) {
        impl.addSupplier(handle, element);
    }

    /**
     * @see org.argouml.model.CoreHelper#addSupplierDependency(
     *         java.lang.Object, java.lang.Object)
     */
    public void addSupplierDependency(Object supplier, Object dependency) {
        impl.addSupplierDependency(supplier, dependency);
    }

    /**
     * @see org.argouml.model.CoreHelper#addTaggedValue(
     *         java.lang.Object, java.lang.Object)
     */
    public void addTaggedValue(Object handle, Object taggedValue) {
        impl.addTaggedValue(handle, taggedValue);
    }

    /**
     * @see org.argouml.model.CoreHelper#addTargetFlow(
     *         java.lang.Object, java.lang.Object)
     */
    public void addTargetFlow(Object handle, Object flow) {
        impl.addTargetFlow(handle, flow);
    }

    /**
     * @see org.argouml.model.CoreHelper#setAnnotatedElements(
     *         java.lang.Object, java.util.Collection)
     */
    public void setAnnotatedElements(Object handle, Collection elems) {
        impl.setAnnotatedElements(handle, elems);
    }

    /**
     * @see org.argouml.model.CoreHelper#setAssociation(
     *         java.lang.Object, java.lang.Object)
     */
    public void setAssociation(Object handle, Object association) {
        impl.setAssociation(handle, association);
    }

    /**
     * @see org.argouml.model.CoreHelper#setRaisedSignals(
     *         java.lang.Object, java.util.Collection)
     */
    public void setRaisedSignals(Object handle, Collection raisedSignals) {
        impl.setRaisedSignals(handle, raisedSignals);
    }

    /**
     * @see org.argouml.model.CoreHelper#setBody(
     *         java.lang.Object, java.lang.Object)
     */
    public void setBody(Object handle, Object expr) {
        impl.setBody(handle, expr);
    }

    /**
     * @see org.argouml.model.CoreHelper#setChild(
     *         java.lang.Object, java.lang.Object)
     */
    public void setChild(Object handle, Object child) {
        impl.setChild(handle, child);
    }

    /**
     * @see org.argouml.model.CoreHelper#setConnections(
     *         java.lang.Object, java.util.Collection)
     */
    public void setConnections(Object handle, Collection elems) {
        impl.setConnections(handle, elems);
    }

    /**
     * @see org.argouml.model.CoreHelper#setDefaultValue(
     *         java.lang.Object, java.lang.Object)
     */
    public void setDefaultValue(Object handle, Object expr) {
        impl.setDefaultValue(handle, expr);
    }

    /**
     * @see org.argouml.model.CoreHelper#setDiscriminator(
     *         java.lang.Object, java.lang.String)
     */
    public void setDiscriminator(Object handle, String discriminator) {
        impl.setDiscriminator(handle, discriminator);
    }

    /**
     * @see org.argouml.model.CoreHelper#setFeature(
     *         java.lang.Object, int, java.lang.Object)
     */
    public void setFeature(Object elem, int i, Object feature) {
        impl.setFeature(elem, i, feature);
    }

    /**
     * @see org.argouml.model.CoreHelper#setFeatures(
     *         java.lang.Object, java.util.Collection)
     */
    public void setFeatures(Object handle, Collection features) {
        impl.setFeatures(handle, features);
    }

    /**
     * @see org.argouml.model.CoreHelper#setContainer(
     *         java.lang.Object, java.lang.Object)
     */
    public void setContainer(Object handle, Object component) {
        impl.setContainer(handle, component);
    }

    /**
     * @see org.argouml.model.CoreHelper#setInitialValue(
     *         java.lang.Object, java.lang.Object)
     */
    public void setInitialValue(Object at, Object expr) {
        impl.setInitialValue(at, expr);
    }

    /**
     * @see org.argouml.model.CoreHelper#setModelElementContainer(
     *         java.lang.Object, java.lang.Object)
     */
    public void setModelElementContainer(Object handle, Object container) {
        impl.setModelElementContainer(handle, container);
    }

    /**
     * @see org.argouml.model.CoreHelper#setNamespace(
     *         java.lang.Object, java.lang.Object)
     */
    public void setNamespace(Object handle, Object ns) {
        impl.setNamespace(handle, ns);
    }

    /**
     * @see org.argouml.model.CoreHelper#setOwner(
     *         java.lang.Object, java.lang.Object)
     */
    public void setOwner(Object handle, Object owner) {
        impl.setOwner(handle, owner);
    }

    /**
     * @see org.argouml.model.CoreHelper#setOwnerScope(
     *         java.lang.Object, java.lang.Object)
     */
    public void setOwnerScope(Object handle, Object os) {
        impl.setOwnerScope(handle, os);
    }

    /**
     * @see org.argouml.model.CoreHelper#setParameters(
     *         java.lang.Object, java.util.Collection)
     */
    public void setParameters(Object handle, Collection parameters) {
        impl.setParameters(handle, parameters);
    }

    /**
     * @see org.argouml.model.CoreHelper#setParent(
     *         java.lang.Object, java.lang.Object)
     */
    public void setParent(Object handle, Object parent) {
        impl.setParent(handle, parent);
    }

    /**
     * @see org.argouml.model.CoreHelper#setQualifiers(
     *         java.lang.Object, java.util.Collection)
     */
    public void setQualifiers(Object handle, Collection elems) {
        List elemList = new ArrayList();
        elemList.addAll(elems);
        impl.setQualifiers(handle, elemList);
    }

    /**
     * @see org.argouml.model.CoreHelper#setQualifiers(
     *         java.lang.Object, java.util.List)
     */
    public void setQualifiers(Object handle, List elems) {
        impl.setQualifiers(handle, elems);
    }

    /**
     * @see org.argouml.model.CoreHelper#setResident(
     *         java.lang.Object, java.lang.Object)
     */
    public void setResident(Object handle, Object resident) {
        impl.setResident(handle, resident);
    }

    /**
     * @see org.argouml.model.CoreHelper#setResidents(
     *         java.lang.Object, java.util.Collection)
     */
    public void setResidents(Object handle, Collection residents) {
        impl.setResidents(handle, residents);
    }

    /**
     * @see org.argouml.model.CoreHelper#setSources(
     *         java.lang.Object, java.util.Collection)
     */
    public void setSources(Object handle, Collection specifications) {
        impl.setSources(handle, specifications);
    }

    /**
     * @see org.argouml.model.CoreHelper#setSpecifications(
     *         java.lang.Object, java.util.Collection)
     */
    public void setSpecifications(Object handle, Collection specifications) {
        impl.setSpecifications(handle, specifications);
    }

    /**
     * @see org.argouml.model.CoreHelper#setTaggedValue(
     *         java.lang.Object, java.lang.String, java.lang.String)
     */
    public void setTaggedValue(Object handle, String tag, String value) {
        impl.setTaggedValue(handle, tag, value);
    }

    /**
     * @see org.argouml.model.CoreHelper#setTaggedValues(
     *         java.lang.Object, java.util.Collection)
     */
    public void setTaggedValues(Object handle, Collection taggedValues) {
        impl.setTaggedValues(handle, taggedValues);
    }

    /**
     * @see org.argouml.model.CoreHelper#setType(
     *         java.lang.Object, java.lang.Object)
     */
    public void setType(Object handle, Object type) {
        impl.setType(handle, type);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeDeploymentLocation(
     *         java.lang.Object, java.lang.Object)
     */
    public void removeDeploymentLocation(Object handle, Object node) {
        impl.removeDeploymentLocation(handle, node);
    }

    /**
     * @see org.argouml.model.CoreHelper#setAbstract(java.lang.Object, boolean)
     */
    public void setAbstract(Object handle, boolean flag) {
        impl.setAbstract(handle, flag);
    }

    /**
     * @see org.argouml.model.CoreHelper#setActive(java.lang.Object, boolean)
     */
    public void setActive(Object handle, boolean active) {
        impl.setActive(handle, active);
    }

    /**
     * @see org.argouml.model.CoreHelper#setAggregation(
     *         java.lang.Object, java.lang.Object)
     */
    public void setAggregation(Object handle, Object aggregationKind) {
        impl.setAggregation(handle, aggregationKind);
    }

    /**
     * @see org.argouml.model.CoreHelper#setLeaf(java.lang.Object, boolean)
     */
    public void setLeaf(Object handle, boolean flag) {
        impl.setLeaf(handle, flag);
    }

    /**
     * @see org.argouml.model.CoreHelper#setChangeability(
     *         java.lang.Object, java.lang.Object)
     */
    public void setChangeability(Object handle, Object ck) {
        impl.setChangeability(handle, ck);
    }

    /**
     * @see org.argouml.model.CoreHelper#setChangeable(
     *         java.lang.Object, boolean)
     */
    public void setChangeable(Object handle, boolean flag) {
        impl.setChangeable(handle, flag);
    }

    /**
     * @see org.argouml.model.CoreHelper#setConcurrency(
     *         java.lang.Object, java.lang.Object)
     */
    public void setConcurrency(Object handle, Object concurrencyKind) {
        impl.setConcurrency(handle, concurrencyKind);
    }

    /**
     * @see org.argouml.model.CoreHelper#setKind(
     *         java.lang.Object, java.lang.Object)
     */
    public void setKind(Object handle, Object kind) {
        impl.setKind(handle, kind);
    }

    /**
     * @see org.argouml.model.CoreHelper#setMultiplicity(
     *         java.lang.Object, java.lang.Object)
     */
    public void setMultiplicity(Object handle, Object arg) {
        impl.setMultiplicity(handle, arg);
    }

    /**
     * @see org.argouml.model.CoreHelper#setName(
     *         java.lang.Object, java.lang.String)
     */
    public void setName(Object handle, String name) {
        impl.setName(handle, name);
    }

    /**
     * @see org.argouml.model.CoreHelper#setBody(
     *         java.lang.Object, java.lang.String)
     */
    public void setBody(Object handle, String body) {
        impl.setBody(handle, body);
    }

    /**
     * @see org.argouml.model.CoreHelper#setNavigable(java.lang.Object, boolean)
     */
    public void setNavigable(Object handle, boolean flag) {
        impl.setNavigable(handle, flag);
    }

    /**
     * @see org.argouml.model.CoreHelper#setOrdering(
     *         java.lang.Object, java.lang.Object)
     */
    public void setOrdering(Object handle, Object ok) {
        impl.setOrdering(handle, ok);
    }

    /**
     * @see org.argouml.model.CoreHelper#setPowertype(
     *         java.lang.Object, java.lang.Object)
     */
    public void setPowertype(Object handle, Object pt) {
        impl.setPowertype(handle, pt);
    }

    /**
     * @see org.argouml.model.CoreHelper#setQuery(java.lang.Object, boolean)
     */
    public void setQuery(Object handle, boolean flag) {
        impl.setQuery(handle, flag);
    }

    /**
     * @see org.argouml.model.CoreHelper#setRoot(java.lang.Object, boolean)
     */
    public void setRoot(Object handle, boolean flag) {
        impl.setRoot(handle, flag);
    }

    /**
     * @see org.argouml.model.CoreHelper#setSpecification(
     *         java.lang.Object, boolean)
     */
    public void setSpecification(Object handle, boolean specification) {
        impl.setSpecification(handle, specification);
    }

    /**
     * @see org.argouml.model.CoreHelper#setTargetScope(
     *         java.lang.Object, java.lang.Object)
     */
    public void setTargetScope(Object handle, Object scopeKind) {
        impl.setTargetScope(handle, scopeKind);
    }

    /**
     * @see org.argouml.model.CoreHelper#setVisibility(
     *         java.lang.Object, java.lang.Object)
     */
    public void setVisibility(Object handle, Object visibility) {
        impl.setVisibility(handle, visibility);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeConnection(
     *         java.lang.Object, java.lang.Object)
     */
    public void removeConnection(Object handle, Object connection) {
        impl.removeConnection(handle, connection);
    }

    /**
     * @see org.argouml.model.CoreHelper#addElementResidence(
     *         java.lang.Object, java.lang.Object)
     */
    public void addElementResidence(Object handle, Object residence) {
        impl.addElementResidence(handle, residence);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeElementResidence(
     *         java.lang.Object, java.lang.Object)
     */
    public void removeElementResidence(Object handle, Object residence) {
        impl.removeElementResidence(handle, residence);
    }

    /**
     * @see org.argouml.model.CoreHelper#setEnumerationLiterals(java.lang.Object, java.util.List)
     */
    public void setEnumerationLiterals(Object enumeration, List literals) {
        impl.setEnumerationLiterals(enumeration, literals);
    }
    
    /**
     * @see org.argouml.model.CoreHelper#getAllMetatypeNames()
     */
    public Collection getAllMetatypeNames() {
        return impl.getAllMetatypeNames();
    }
}
