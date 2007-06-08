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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.argouml.model.CoreHelper;
import org.argouml.model.NotImplementedException;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.BehavioralFeature;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Extend;
import org.eclipse.uml2.uml.Feature;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Include;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.RedefinableElement;
import org.eclipse.uml2.uml.StructuralFeature;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.TypedElement;
import org.eclipse.uml2.uml.VisibilityKind;


/**
 * Eclipse UML2 implementation of CoreHelper.
 */
class CoreHelperEUMLImpl implements CoreHelper {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     *
     * @param implementation The ModelImplementation.
     */
    public CoreHelperEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public void addAllStereotypes(Object modelElement, Collection stereos) {
        throw new NotYetImplementedException();
        
    }

    public void addAnnotatedElement(Object comment, Object annotatedElement) {
        throw new NotYetImplementedException();
        
    }

    public void addClient(Object dependency, Object element) {
        throw new NotYetImplementedException();
    }

    public void addClientDependency(Object handle, Object dependency) {
        throw new NotYetImplementedException();
        
    }

    public void addComment(Object element, Object comment) {
        throw new NotYetImplementedException();
        
    }

    public void addConnection(Object handle, Object connection) {
        throw new NotYetImplementedException();
        
    }

    public void addConnection(Object handle, int position, Object connection) {
        throw new NotYetImplementedException();
        
    }

    public void addConstraint(Object handle, Object mc) {
        throw new NotYetImplementedException();
        
    }

    public void addDeploymentLocation(Object handle, Object node) {
        throw new NotYetImplementedException();
        
    }

    public void addElementResidence(Object handle, Object residence) {
        throw new NotYetImplementedException();
        
    }

    public void addFeature(Object handle, int index, Object f) {
        throw new NotYetImplementedException();
        
    }

    public void addFeature(Object handle, Object f) {
        throw new NotYetImplementedException();
        
    }

    public void addLink(Object handle, Object link) {
        throw new NotYetImplementedException();
        
    }

    public void addLiteral(Object handle, int index, Object literal) {
        throw new NotYetImplementedException();
        
    }

    public void addMethod(Object handle, Object method) {
        throw new NotYetImplementedException();
    }

    public void addOwnedElement(Object handle, Object me) {
        if (handle instanceof org.eclipse.uml2.uml.Package
                && me instanceof Type) {
            ((org.eclipse.uml2.uml.Package) handle).getOwnedTypes().add(
                    (Type) me);
        } else if (handle instanceof org.eclipse.uml2.uml.Package
                && me instanceof PackageableElement) {
            ((org.eclipse.uml2.uml.Package) handle).getPackagedElements().add(
                    (PackageableElement) me);
        } else {
            throw new NotYetImplementedException();
        }
    }

    public void addParameter(Object handle, int index, Object parameter) {
        throw new NotYetImplementedException();
    }

    public void addParameter(Object handle, Object parameter) {
        throw new NotYetImplementedException();
        
    }

    public void addQualifier(Object handle, int position, Object qualifier) {
        throw new NotYetImplementedException();
        
    }

    public void addRaisedSignal(Object handle, Object sig) {
        throw new NotYetImplementedException();
        
    }

    public void addSourceFlow(Object handle, Object flow) {
        throw new NotYetImplementedException();
        
    }

    public void addStereotype(Object modelElement, Object stereo) {
        throw new NotYetImplementedException();
        
    }

    public void addSupplier(Object handle, Object element) {
        throw new NotYetImplementedException();
        
    }

    public void addSupplierDependency(Object supplier, Object dependency) {
        throw new NotYetImplementedException();
        
    }

    public void addTaggedValue(Object handle, Object taggedValue) {
        throw new NotYetImplementedException();
        
    }

    public void addTargetFlow(Object handle, Object flow) {
        throw new NotYetImplementedException();
        
    }

    public void addTemplateArgument(Object handle, int index, Object argument) {
        throw new NotYetImplementedException();
        
    }

    public void addTemplateArgument(Object handle, Object argument) {
        throw new NotYetImplementedException();
        
    }

    public void addTemplateParameter(Object handle, int index, Object parameter) {
        throw new NotYetImplementedException();
        
    }

    public void addTemplateParameter(Object handle, Object parameter) {
        throw new NotYetImplementedException();
        
    }

    public void clearStereotypes(Object handle) {
        throw new NotYetImplementedException();
        
    }

    public boolean equalsAggregationKind(Object associationEnd, String kindType) {
        throw new NotYetImplementedException();

    }

    public Collection getAllAttributes(Object classifier) {
        throw new NotYetImplementedException();

    }

    public Collection getAllBehavioralFeatures(Object element) {
        throw new NotYetImplementedException();

    }

    public Collection getAllClasses(Object ns) {
        throw new NotYetImplementedException();

    }

    public Collection getAllClassifiers(Object namespace) {
        throw new NotYetImplementedException();

    }

    public Collection getAllComponents(Object ns) {
        throw new NotYetImplementedException();

    }

    public Collection getAllContents(Object namespace) {
        throw new NotYetImplementedException();

    }

    public Collection getAllDataTypes(Object ns) {
        throw new NotYetImplementedException();

    }

    public Collection getAllInterfaces(Object ns) {
        throw new NotYetImplementedException();

    }

    public Collection getAllMetatypeNames() {
        throw new NotYetImplementedException();

    }

    public Collection getAllNodes(Object ns) {
        throw new NotYetImplementedException();

    }

    public Collection getAllPossibleNamespaces(Object modelElement, Object model) {
        // TODO: Dummy implementation that just returns the current namespace hierarchy
        Collection<Namespace> result = new ArrayList<Namespace>();
        result.addAll(((NamedElement) modelElement).allNamespaces());
        return result;
    }

    public Collection getAllRealizedInterfaces(Object element) {
        return ((Classifier) element).getAllUsedInterfaces();
    }

    public Collection getAllSupertypes(Object classifier) {
        return ((Classifier) classifier).allParents();
    }

    public Collection getAllVisibleElements(Object ns) {
        throw new NotYetImplementedException();
    }

    public Collection getAssociateEnds(Object classifier) {
        throw new NotYetImplementedException();
    }

    public Collection getAssociateEndsInh(Object classifier) {
        throw new NotYetImplementedException();
    }

    public Collection getAssociatedClassifiers(Object aclassifier) {
        throw new NotYetImplementedException();

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
//      if (relationship instanceof Link) {
//      Iterator it =
//      modelImpl.getFacade()
//      .getConnections(relationship).iterator();
//      if (it.hasNext()) {
//      return modelImpl.getFacade().getInstance(it.next());
//      } else {
//      return null;
//      }
//      }
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
//        } else if (relationship instanceof Flow) {
//            Flow flow = (Flow) relationship;
//            Collection col = flow.getSource();
//            if (col.isEmpty()) {
//                return null;
//            }
//            return (col.toArray())[0];
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

//        if (relationship instanceof Link) {
//            Iterator it = modelImpl.getFacade()
//            .getConnections(relationship).iterator();
//            if (it.hasNext()) {
//                it.next();
//                if (it.hasNext()) {
//                    return modelImpl.getFacade().getInstance(it.next());
//                } else {
//                    return null;
//                }
//            } else {
//                return null;
//            }
//        }

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
//        } else if (relationship instanceof Flow) {
//            Flow flow = (Flow) relationship;
//            Collection col = flow.getTarget();
//            if (col.isEmpty()) {
//                return null;
//            }
//            return getFirstItemOrNull(col);
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

        if (!(element instanceof Element) 
                || !(namespace instanceof Namespace)) {
            return false;
        }

        Element e = (Element) element;
        Namespace ns = (Namespace) namespace;
        
        // TODO: Needs implementation - for now anything is valid
        
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
//        ((Property) handle).setIsOrdered(ordering);
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


}
