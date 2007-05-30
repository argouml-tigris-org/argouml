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
import java.util.Collections;
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
        // TODO Auto-generated method stub
        
    }

    public void addAnnotatedElement(Object comment, Object annotatedElement) {
        // TODO Auto-generated method stub
        
    }

    public void addClient(Object dependency, Object element) {
        // TODO Auto-generated method stub
        
    }

    public void addClientDependency(Object handle, Object dependency) {
        // TODO Auto-generated method stub
        
    }

    public void addComment(Object element, Object comment) {
        // TODO Auto-generated method stub
        
    }

    public void addConnection(Object handle, Object connection) {
        // TODO Auto-generated method stub
        
    }

    public void addConnection(Object handle, int position, Object connection) {
        // TODO Auto-generated method stub
        
    }

    public void addConstraint(Object handle, Object mc) {
        // TODO Auto-generated method stub
        
    }

    public void addDeploymentLocation(Object handle, Object node) {
        // TODO Auto-generated method stub
        
    }

    public void addElementResidence(Object handle, Object residence) {
        // TODO Auto-generated method stub
        
    }

    public void addFeature(Object handle, int index, Object f) {
        // TODO Auto-generated method stub
        
    }

    public void addFeature(Object handle, Object f) {
        // TODO Auto-generated method stub
        
    }

    public void addLink(Object handle, Object link) {
        // TODO Auto-generated method stub
        
    }

    public void addLiteral(Object handle, int index, Object literal) {
        // TODO Auto-generated method stub
        
    }

    public void addMethod(Object handle, Object method) {
        // TODO Auto-generated method stub
        
    }

    public void addOwnedElement(Object handle, Object me) {
        // TODO Auto-generated method stub
        
    }

    public void addParameter(Object handle, int index, Object parameter) {
        // TODO Auto-generated method stub
        
    }

    public void addParameter(Object handle, Object parameter) {
        // TODO Auto-generated method stub
        
    }

    public void addQualifier(Object handle, int position, Object qualifier) {
        // TODO Auto-generated method stub
        
    }

    public void addRaisedSignal(Object handle, Object sig) {
        // TODO Auto-generated method stub
        
    }

    public void addSourceFlow(Object handle, Object flow) {
        // TODO Auto-generated method stub
        
    }

    public void addStereotype(Object modelElement, Object stereo) {
        // TODO Auto-generated method stub
        
    }

    public void addSupplier(Object handle, Object element) {
        // TODO Auto-generated method stub
        
    }

    public void addSupplierDependency(Object supplier, Object dependency) {
        // TODO Auto-generated method stub
        
    }

    public void addTaggedValue(Object handle, Object taggedValue) {
        // TODO Auto-generated method stub
        
    }

    public void addTargetFlow(Object handle, Object flow) {
        // TODO Auto-generated method stub
        
    }

    public void addTemplateArgument(Object handle, int index, Object argument) {
        // TODO Auto-generated method stub
        
    }

    public void addTemplateArgument(Object handle, Object argument) {
        // TODO Auto-generated method stub
        
    }

    public void addTemplateParameter(Object handle, int index, Object parameter) {
        // TODO Auto-generated method stub
        
    }

    public void addTemplateParameter(Object handle, Object parameter) {
        // TODO Auto-generated method stub
        
    }

    public void clearStereotypes(Object handle) {
        // TODO Auto-generated method stub
        
    }

    public boolean equalsAggregationKind(Object associationEnd, String kindType) {
        // TODO Auto-generated method stub
        return false;
    }

    public Collection getAllAttributes(Object classifier) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAllBehavioralFeatures(Object element) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAllClasses(Object ns) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAllClassifiers(Object namespace) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAllComponents(Object ns) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAllContents(Object namespace) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAllDataTypes(Object ns) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAllInterfaces(Object ns) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAllMetatypeNames() {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAllNodes(Object ns) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAllPossibleNamespaces(Object modelElement, Object model) {
        // TODO: Dummy implementation that just returns the current namespace
        Collection<Namespace> result = new ArrayList<Namespace>();
        result.add(((NamedElement) modelElement).getNamespace());
        return result;
    }

    public Collection getAllRealizedInterfaces(Object element) {
        // TODO Auto-generated method stub
        return Collections.EMPTY_SET;
    }

    public Collection getAllSupertypes(Object classifier) {
        // TODO Auto-generated method stub
        return Collections.EMPTY_SET;
    }

    public Collection getAllVisibleElements(Object ns) {
        // TODO Auto-generated method stub
        return Collections.EMPTY_SET;
    }

    public Collection getAssociateEnds(Object classifier) {
        // TODO Auto-generated method stub
        return Collections.EMPTY_SET;
    }

    public Collection getAssociateEndsInh(Object classifier) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAssociatedClassifiers(Object aclassifier) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getAssociationEnd(Object type, Object assoc) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAssociations(Object from, Object to) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAssociations(Object oclassifier) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAttributesInh(Object classifier) {
        // TODO Auto-generated method stub
        return null;
    }

    public List getBehavioralFeatures(Object classifier) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getBody(Object comment) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getChildren(Object element) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getDependencies(Object supplierObj, Object clientObj) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getExtendedClassifiers(Object element) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getExtendingClassifiers(Object classifier) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getExtendingElements(Object element) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getFirstSharedNamespace(Object ns1, Object ns2) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getFlows(Object source, Object target) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getGeneralization(Object achild, Object aparent) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getOperationsInh(Object classifier) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getRealizedInterfaces(Object cls) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getRelationships(Object source, Object dest) {
        // TODO Auto-generated method stub
        return null;
    }

    public List getReturnParameters(Object operation) {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getSpecifications(Object classifier) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getSubtypes(Object cls) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getSupertypes(Object generalizableElement) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasCompositeEnd(Object association) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isSubType(Object type, Object subType) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isValidNamespace(Object element, Object namespace) {
        // TODO Auto-generated method stub
        return false;
    }

    public void removeAnnotatedElement(Object handle, Object me) {
        // TODO Auto-generated method stub
        
    }

    public void removeClientDependency(Object handle, Object dep) {
        // TODO Auto-generated method stub
        
    }

    public void removeConnection(Object handle, Object connection) {
        // TODO Auto-generated method stub
        
    }

    public void removeConstraint(Object handle, Object cons) {
        // TODO Auto-generated method stub
        
    }

    public void removeDeploymentLocation(Object handle, Object node) {
        // TODO Auto-generated method stub
        
    }

    public void removeElementResidence(Object handle, Object residence) {
        // TODO Auto-generated method stub
        
    }

    public void removeFeature(Object cls, Object feature) {
        // TODO Auto-generated method stub
        
    }

    public void removeLiteral(Object enumeration, Object literal) {
        // TODO Auto-generated method stub
        
    }

    public void removeOwnedElement(Object handle, Object value) {
        // TODO Auto-generated method stub
        
    }

    public void removeParameter(Object handle, Object parameter) {
        // TODO Auto-generated method stub
        
    }

    public void removeQualifier(Object handle, Object qualifier) {
        // TODO Auto-generated method stub
        
    }

    public void removeSourceFlow(Object handle, Object flow) {
        // TODO Auto-generated method stub
        
    }

    public void removeStereotype(Object handle, Object stereo) {
        // TODO Auto-generated method stub
        
    }

    public void removeSupplierDependency(Object supplier, Object dependency) {
        // TODO Auto-generated method stub
        
    }

    public void removeTargetFlow(Object handle, Object flow) {
        // TODO Auto-generated method stub
        
    }

    public void removeTemplateArgument(Object binding, Object argument) {
        // TODO Auto-generated method stub
        
    }

    public void removeTemplateParameter(Object handle, Object parameter) {
        // TODO Auto-generated method stub
        
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
        // TODO Auto-generated method stub
    }

    public void setAnnotatedElements(Object handle, Collection elems) {
        // TODO Auto-generated method stub
        
    }

    public void setAssociation(Object handle, Object association) {
        // TODO Auto-generated method stub
        
    }

    public void setAttributes(Object classifier, List attributes) {
        // TODO Auto-generated method stub
        
    }

    public void setBody(Object handle, Object expr) {
        // TODO Auto-generated method stub
        
    }

    public void setBody(Object handle, String body) {
        // TODO Auto-generated method stub
        
    }

    public void setChangeability(Object handle, Object ck) {
        // TODO Auto-generated method stub
        
    }

    public void setChangeable(Object handle, boolean changeable) {
        // TODO Auto-generated method stub
        
    }

    public void setChild(Object handle, Object child) {
        // TODO Auto-generated method stub
        
    }

    public void setConcurrency(Object handle, Object concurrencyKind) {
        // TODO Auto-generated method stub
        
    }

    public void setConnections(Object handle, Collection ends) {
        // TODO Auto-generated method stub
        
    }

    public void setContainer(Object handle, Object component) {
        // TODO Auto-generated method stub
        
    }

    public void setDefaultElement(Object handle, Object element) {
        // TODO Auto-generated method stub
        
    }

    public void setDefaultValue(Object handle, Object expression) {
        // TODO Auto-generated method stub
        
    }

    public void setDiscriminator(Object handle, String discriminator) {
        // TODO Auto-generated method stub
        
    }

    public void setEnumerationLiterals(Object enumeration, List literals) {
        // TODO Auto-generated method stub
        
    }

    public void setFeature(Object classifier, int index, Object feature) {
        // TODO Auto-generated method stub
        
    }

    public void setFeatures(Object classifier, Collection features) {
        // TODO Auto-generated method stub
        
    }

    public void setInitialValue(Object attribute, Object expression) {
        // TODO Auto-generated method stub
        
    }

    public void setKind(Object handle, Object kind) {
        // TODO Auto-generated method stub
        
    }

    public void setLeaf(Object handle, boolean isLeaf) {
        ((RedefinableElement) handle).setIsLeaf(isLeaf);
    }

    public void setModelElementContainer(Object handle, Object container) {
        // TODO Auto-generated method stub
        
    }

    public void setMultiplicity(Object handle, Object arg) {
        // TODO Auto-generated method stub
        
    }

    public void setName(Object handle, String name) {
        ((NamedElement) handle).setName(name);
    }

    public void setNamespace(Object handle, Object ns) {
        ((Namespace) ns).getOwnedElements().add((NamedElement) handle);
    }

    public void setNavigable(Object handle, boolean flag) {
        ((Property) handle).setIsNavigable(flag);
    }

    public void setOperations(Object classifier, List operations) {
        // TODO Auto-generated method stub
        
    }

    public void setOrdering(Object handle, Object ordering) {
//        ((Property) handle).setIsOrdered(ordering);
        // TODO Auto-generated method stub
    }

    public void setOwner(Object handle, Object owner) {
        // TODO Auto-generated method stub
    }

    public void setOwnerScope(Object feature, Object scopeKind) {
        // Don't implement - deprecated method in interface.
        throw new NotImplementedException();
    }

    public void setParameter(Object handle, Object parameter) {
        // TODO Auto-generated method stub
        
    }

    public void setParameters(Object handle, Collection parameters) {
        // TODO Auto-generated method stub
        
    }

    public void setParent(Object handle, Object parent) {
        // TODO Auto-generated method stub
        
    }

    public void setPowertype(Object handle, Object powerType) {
        // TODO Auto-generated method stub
        
    }

    public void setQualifiers(Object handle, List qualifiers) {
        // TODO Auto-generated method stub
        
    }

    public void setQuery(Object handle, boolean isQuery) {
        ((Operation) handle).setIsQuery(isQuery);
    }

    public void setRaisedSignals(Object handle, Collection raisedSignals) {
        // TODO Auto-generated method stub
        
    }
    
    public void setReadOnly(Object handle, boolean isReadOnly) {
        ((StructuralFeature) handle).setIsReadOnly(isReadOnly);
    }

    public void setResident(Object handle, Object resident) {
        // TODO Auto-generated method stub
        
    }

    public void setResidents(Object handle, Collection residents) {
        // TODO Auto-generated method stub
        
    }

    public void setRoot(Object handle, boolean isRoot) {
        // TODO Auto-generated method stub
        
    }

    public void setSources(Object handle, Collection specifications) {
        // TODO Auto-generated method stub
        
    }

    public void setSpecification(Object handle, boolean isSpecification) {
        // TODO Auto-generated method stub
        
    }

    public void setSpecification(Object method, Object specification) {
        // TODO Auto-generated method stub
    }

    public void setSpecification(Object operation, String specification) {
        // TODO Auto-generated method stub
    }

    public void setSpecifications(Object handle, Collection specifications) {
        // TODO Auto-generated method stub

    }
    
    public void setStatic(Object feature, boolean isStatic) {
        ((Feature) feature).setIsStatic(isStatic);
    }

    public void setTaggedValue(Object handle, String tag, String value) {
        // TODO Auto-generated method stub
        
    }

    public void setTaggedValues(Object handle, Collection taggedValues) {
        // TODO Auto-generated method stub
        
    }

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
