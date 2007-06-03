// $Id:CoreFactoryEUMLImpl.java 12721 2007-05-30 18:14:55Z tfmorris $
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

import org.argouml.model.CoreFactory;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;


/**
 * The implementation of the CoreFactory for EUML2.
 */
class CoreFactoryEUMLImpl implements CoreFactory {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     *
     * @param implementation The ModelImplementation.
     */
    public CoreFactoryEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public Object buildAbstraction(String name, Object supplier, Object client) {
        // TODO Auto-generated method stub
        return null;
    }

    // TODO: A few different ways of building Associations.  Pick one that
    // works and is efficient - tfm
    public Object buildAssociation(Object fromClassifier,
            Object aggregationKind1, Object toClassifier,
            Object aggregationKind2, Boolean unidirectional) {
        
//        return ((Type) fromClassifier).createAssociation(
//                true, (AggregationKind) aggregationKind1, null, 1, 1, 
//                (Type) toClassifier, 
//                true, (AggregationKind) aggregationKind2, null, 1, 1); 

        Association assoc = (Association) createAssociation();
        Property end1 = assoc.createNavigableOwnedEnd(null, (Type) fromClassifier);
        Property end2 = assoc.createOwnedEnd(null, (Type) toClassifier);
        boolean uni = false;
        if (unidirectional != null && unidirectional.booleanValue()) {
            uni = true;
        }
        end2.setIsNavigable(uni);
        return assoc;
        
    }

    public Object buildAssociation(Object classifier1, Object classifier2) {
        return buildAssociation(
                classifier1, AggregationKind.NONE_LITERAL, classifier2,
                AggregationKind.NONE_LITERAL, false);
    }

    public Object buildAssociation(Object c1, boolean nav1, Object c2,
            boolean nav2, String name) {
        Association assoc = (Association) createAssociation();
        Property end1 = assoc.createOwnedEnd(null, (Type) c1);
        end1.setIsNavigable(nav1);
        Property end2 = assoc.createOwnedEnd(null, (Type) c2);
        end2.setIsNavigable(nav1);
        assoc.setName(name);
        return assoc;
    }

    public Object buildAssociationClass(Object end1, Object end2) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildAssociationEnd(Object assoc, String name, Object type, Object multi, Object stereo, boolean navigable, Object order, Object aggregation, Object scope, Object changeable, Object visibility) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildAssociationEnd(Object type, Object assoc) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public Object buildAttribute() {
        // TODO: Required for UmlFactory.buildNode()
        throw new NotYetImplementedException();
//        Attribute attr = (Attribute) createAttribute();
//        attr.setName("newAttr");
//        attr.setMultiplicity(getMultiplicity11());
//        attr.setVisibility(VisibilityKindEnum.VK_PUBLIC);
//        attr.setOwnerScope(ScopeKindEnum.SK_INSTANCE);
//        attr.setChangeability(ChangeableKindEnum.CK_CHANGEABLE);
//        attr.setTargetScope(ScopeKindEnum.SK_INSTANCE);
//        return attr;
    }

    public Object buildAttribute(Object model, Object type) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildAttribute(Object handle, Object model, Object type) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildAttribute2(Object type) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildAttribute2(Object handle, Object type) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildBinding(Object client, Object supplier, List arguments) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildClass() {
        return createClass();
    }

    public Object buildClass(Object owner) {
        return buildClass(null, owner);
    }

    public Object buildClass(String name) {
        return buildClass(name, null);
    }

    public Object buildClass(String name, Object owner) {
        org.eclipse.uml2.uml.Class cls =
                (org.eclipse.uml2.uml.Class) createClass();
        if (owner != null) {
            ((Namespace) owner).getOwnedElements().add(cls);
        }
        if (name != null) {
            cls.setName(name);
        }
        return cls;
    }

    public Object buildComment(Object element, Object model) {
        if (model == null) {
            throw new IllegalArgumentException("A namespace must be supplied.");
        }
        Element elementToAnnotate = (Element) element;
        Comment comment = (Comment) createComment();

        Element owner = null;
        if (elementToAnnotate != null) {
            comment.getAnnotatedElements().add(elementToAnnotate);
            owner = elementToAnnotate.getOwner();
        } else {
            owner = (Namespace) model;
        }

        owner.getOwnedElements().add(comment);
        return comment;
    }

    public Object buildConstraint(Object constrElement) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildConstraint(String name, Object bexpr) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildDataType(String name, Object owner) {
        DataType dt = (DataType) createDataType();
        dt.setName(name);
        ((Element) owner).getOwnedElements().add(dt);
        return dt;
    }

    public Object buildDependency(Object clientObj, Object supplierObj) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildElementResidence(Object me, Object component) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildEnumeration(String name, Object owner) {
        Enumeration enumer = (Enumeration) createEnumeration();
        enumer.setName(name);
        if (owner != null) {
            ((Element) owner).getOwnedElements().add(enumer);
        }
        return enumer;
    }

    public Object buildEnumerationLiteral(String name, Object enumeration) {
        return ((Enumeration) enumeration).createOwnedLiteral(name);
    }

    public Object buildGeneralization(Object child, Object parent, String name) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildGeneralization(Object child, Object parent) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildInterface() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildInterface(Object owner) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildInterface(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildInterface(String name, Object owner) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildMethod(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildOperation(Object classifier, Object model, Object returnType) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildOperation(Object classifier, Object returnType) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildOperation(Object cls, Object model, Object returnType, String name) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildOperation2(Object cls, Object returnType, String name) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildParameter(Object o, Object model, Object type) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildParameter(Object o, Object type) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildPermission(Object clientObj, Object supplierObj) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildRealization(Object clnt, Object spplr, Object model) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildTemplateArgument(Object element) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildUsage(Object client, Object supplier) {
        // TODO Auto-generated method stub
        return null;
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

    public Object createAbstraction() {
        return UMLFactory.eINSTANCE.createAbstraction();
    }

    public Object createArtifact() {
        return UMLFactory.eINSTANCE.createArtifact();
    }

    public Object createAssociation() {
        return UMLFactory.eINSTANCE.createAssociation();
    }

    public Object createAssociationClass() {
        return UMLFactory.eINSTANCE.createAssociationClass();
    }

    public Object createAssociationEnd() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createAttribute() {
        // TODO: Double check - tfm
        return UMLFactory.eINSTANCE.createProperty();
    }

    public Object createBinding() {
        return UMLFactory.eINSTANCE.createTemplateBinding();
    }

    public Object createClass() {
        return UMLFactory.eINSTANCE.createClass();
    }

    public Object createComment() {
        return UMLFactory.eINSTANCE.createComment();
    }

    public Object createComponent() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createConstraint() {
        return UMLFactory.eINSTANCE.createConstraint();
    }

    public Object createDataType() {
        return UMLFactory.eINSTANCE.createDataType();
    }

    public Object createDependency() {
        return UMLFactory.eINSTANCE.createDependency();
    }

    public Object createElementResidence() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createEnumeration() {
        return UMLFactory.eINSTANCE.createEnumeration();
    }

    public Object createEnumerationLiteral() {
        return UMLFactory.eINSTANCE.createEnumerationLiteral();
    }

    public Object createFlow() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createGeneralization() {
        return UMLFactory.eINSTANCE.createGeneralization();
    }

    public Object createInterface() {
        return UMLFactory.eINSTANCE.createInterface();
    }

    public Object createMethod() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createNode() {
        return UMLFactory.eINSTANCE.createNode();
    }

    public Object createOperation() {
        return UMLFactory.eINSTANCE.createOperation();
    }

    public Object createParameter() {
        return UMLFactory.eINSTANCE.createParameter();
    }

    public Object createPermission() {
        return UMLFactory.eINSTANCE.createPackageImport();
    }

    public Object createPrimitive() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createProgrammingLanguageDataType() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createTemplateArgument() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createTemplateParameter() {
        return UMLFactory.eINSTANCE.createTemplateParameter();
    }

    public Object createUsage() {
        return UMLFactory.eINSTANCE.createUsage();
    }


}
