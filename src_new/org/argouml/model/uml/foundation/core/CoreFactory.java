// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.model.uml.foundation.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.argouml.application.api.Notation;
import org.argouml.application.api.NotationName;
import org.argouml.kernel.Project;
import org.argouml.model.ModelHelper;
import org.argouml.model.uml.AbstractUmlModelFactory;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.UmlHelper;
import org.argouml.model.uml.foundation.extensionmechanisms.ExtensionMechanismsFactory;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.argouml.ui.ProjectBrowser;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.behavior.state_machines.MEvent;
import ru.novosoft.uml.foundation.core.MAbstraction;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationClass;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MBehavioralFeature;
import ru.novosoft.uml.foundation.core.MBinding;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MComment;
import ru.novosoft.uml.foundation.core.MComponent;
import ru.novosoft.uml.foundation.core.MConstraint;
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MDependency;
import ru.novosoft.uml.foundation.core.MElementResidence;
import ru.novosoft.uml.foundation.core.MFeature;
import ru.novosoft.uml.foundation.core.MFlow;
import ru.novosoft.uml.foundation.core.MGeneralizableElement;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MMethod;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MNode;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.core.MPermission;
import ru.novosoft.uml.foundation.core.MPresentationElement;
import ru.novosoft.uml.foundation.core.MRelationship;
import ru.novosoft.uml.foundation.core.MStructuralFeature;
import ru.novosoft.uml.foundation.core.MTemplateParameter;
import ru.novosoft.uml.foundation.core.MUsage;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;
import ru.novosoft.uml.foundation.data_types.MCallConcurrencyKind;
import ru.novosoft.uml.foundation.data_types.MChangeableKind;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MOrderingKind;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;
import ru.novosoft.uml.foundation.data_types.MScopeKind;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

/**
 * Factory to create UML classes for the UML
 * Foundation::Core package.
 *
 * Feature, StructuralFeature, and PresentationElement
 * do not have a create methods since
 * it is called an "abstract metaclass" in the
 * UML specifications.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */

public class CoreFactory extends AbstractUmlModelFactory {

    /** Singleton instance.
     */
    private static CoreFactory SINGLETON =
                   new CoreFactory();

    /** Singleton instance access method.
     */
    public static CoreFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation
     */
    private CoreFactory() {
    }

    /** Create an empty but initialized instance of a UML Abstraction.
     *  
     *  @return an initialized UML Abstraction instance.
     */
    public MAbstraction createAbstraction() {
        MAbstraction modelElement = MFactory.getDefaultFactory().createAbstraction();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Association.
     *  
     *  @return an initialized UML Association instance.
     */
    public MAssociation createAssociation() {
        MAssociation modelElement = MFactory.getDefaultFactory().createAssociation();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML AssociationClass.
     *  
     *  @return an initialized UML AssociationClass instance.
     */
    public MAssociationClass createAssociationClass() {
        MAssociationClass modelElement = MFactory.getDefaultFactory().createAssociationClass();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML AssociationEnd.
     *  
     *  @return an initialized UML AssociationEnd instance.
     */
    public MAssociationEnd createAssociationEnd() {
        MAssociationEnd modelElement = MFactory.getDefaultFactory().createAssociationEnd();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Attribute.
     *  
     *  @return an initialized UML Attribute instance.
     */
    public MAttribute createAttribute() {
        MAttribute modelElement = MFactory.getDefaultFactory().createAttribute();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Binding.
     *  
     *  @return an initialized UML Binding instance.
     */
    public MBinding createBinding() {
        MBinding modelElement = MFactory.getDefaultFactory().createBinding();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Class.
     *  
     *  @return an initialized UML Class instance.
     */
    public MClass createClass() {
        MClass modelElement = MFactory.getDefaultFactory().createClass();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Classifier.
     *  
     *  @return an initialized UML Classifier instance.
     */
    public MClassifier createClassifier() {
        MClassifier modelElement = MFactory.getDefaultFactory().createClassifier();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Comment.
     *  
     *  @return an initialized UML Comment instance.
     */
    public MComment createComment() {
        MComment modelElement = MFactory.getDefaultFactory().createComment();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Component.
     *  
     *  @return an initialized UML Component instance.
     */
    public MComponent createComponent() {
        MComponent modelElement = MFactory.getDefaultFactory().createComponent();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Constraint.
     *  
     *  @return an initialized UML Constraint instance.
     */
    public MConstraint createConstraint() {
        MConstraint modelElement = MFactory.getDefaultFactory().createConstraint();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML DataType.
     *  
     *  @return an initialized UML DataType instance.
     */
    public MDataType createDataType() {
        MDataType modelElement = MFactory.getDefaultFactory().createDataType();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Dependency.
     *  
     *  @return an initialized UML Dependency instance.
     */
    public MDependency createDependency() {
        MDependency modelElement = MFactory.getDefaultFactory().createDependency();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML ElementResidence.
     *  
     *  @return an initialized UML ElementResidence instance.
     */
    public MElementResidence createElementResidence() {
        MElementResidence modelElement = MFactory.getDefaultFactory().createElementResidence();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Flow.
     *  
     *  @return an initialized UML Flow instance.
     */
    public MFlow createFlow() {
        MFlow modelElement = MFactory.getDefaultFactory().createFlow();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Generalization.
     *  
     *  @return an initialized UML Generalization instance.
     */
    public MGeneralization createGeneralization() {
        MGeneralization modelElement = MFactory.getDefaultFactory().createGeneralization();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Interface.
     *  
     *  @return an initialized UML Interface instance.
     */
    public MInterface createInterface() {
        MInterface modelElement = MFactory.getDefaultFactory().createInterface();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Method.
     *  
     *  @return an initialized UML Method instance.
     */
    public MMethod createMethod() {
        MMethod modelElement = MFactory.getDefaultFactory().createMethod();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Namespace.
     *  
     *  @return an initialized UML Namespace instance.
     */
    public MNamespace createNamespace() {
        MNamespace modelElement = MFactory.getDefaultFactory().createNamespace();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Node.
     *  
     *  @return an initialized UML Node instance.
     */
    public MNode createNode() {
        MNode modelElement = MFactory.getDefaultFactory().createNode();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Operation.
     *  
     *  @return an initialized UML Operation instance.
     */
    public MOperation createOperation() {
        MOperation modelElement = MFactory.getDefaultFactory().createOperation();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Parameter.
     *  
     *  @return an initialized UML Parameter instance.
     */
    public MParameter createParameter() {
        MParameter modelElement = MFactory.getDefaultFactory().createParameter();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Permission.
     *  
     *  @return an initialized UML Permission instance.
     */
    public MPermission createPermission() {
        MPermission modelElement = MFactory.getDefaultFactory().createPermission();
	super.initialize(modelElement);
	return modelElement;
    }


    /** Create an empty but initialized instance of a UML Relationship.
     *  
     *  @return an initialized UML Relationship instance.
     */
    public MRelationship createRelationship() {
        MRelationship modelElement = MFactory.getDefaultFactory().createRelationship();
	super.initialize(modelElement);
	return modelElement;
    }


    /** Create an empty but initialized instance of a UML TemplateParameter.
     *  
     *  @return an initialized UML TemplateParameter instance.
     */
    public MTemplateParameter createTemplateParameter() {
        MTemplateParameter modelElement = MFactory.getDefaultFactory().createTemplateParameter();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Usage.
     *  
     *  @return an initialized UML Usage instance.
     */
    public MUsage createUsage() {
        MUsage modelElement = MFactory.getDefaultFactory().createUsage();
	super.initialize(modelElement);
	return modelElement;
    }
    
    /**
     * Builds a default binary association with two default association ends. 
     * @param c1 The first classifier to connect to
     * @param nav1 The navigability of the Associaton end
     * @param c2 The second classifier to connect to
     * @param nav2 The navigability of the second Associaton end
     * @return MAssociation
     */
    public MAssociation buildAssociation(MClassifier c1, boolean nav1, MClassifier c2, boolean nav2) {
        if (c1 == null || c2 == null) throw new IllegalArgumentException("In buildAssociation: one of the classifiers to be connected is null");
        MNamespace ns1 = c1.getNamespace();
        MNamespace ns2 = c2.getNamespace();
        if (ns1 == null || ns2 == null) throw new IllegalArgumentException("In buildAssociation: one of the classifiers does not belong to a namespace");
        MAssociation assoc = UmlFactory.getFactory().getCore().createAssociation();
        assoc.setName("");     
        assoc.setNamespace(getFirstSharedNamespace(ns1, ns2));
        buildAssociationEnd(assoc, null, c1,null, null, nav1, null, null, null, null, null);
        buildAssociationEnd(assoc, null, c2,null, null, nav2, null, null, null, null, null);
        return assoc;       
    }
    
    private MNamespace getFirstSharedNamespace(MNamespace ns1, MNamespace ns2) {
    	if (ns1 == null || ns2 == null) return null;
    	if (ns1 == ns2) return ns1;
    	boolean ns1Owner = ModelManagementHelper.getHelper().getAllNamespaces(ns1).contains(ns2);
        boolean ns2Owner = ModelManagementHelper.getHelper().getAllNamespaces(ns2).contains(ns1);
        if (ns1Owner) return ns1;
        if (ns2Owner) return ns2;
        return getFirstSharedNamespace(ns1.getNamespace(), ns2.getNamespace());
    }
    
    /**
     * Builds a binary associations between two classifiers with default values for the
     * association ends and the association itself.
     * @param c1 The first classifier to connect
     * @param c2 The second classifier to connect
     * @return MAssociation
     */
    public MAssociation buildAssociation(MClassifier c1, MClassifier c2) {
        return buildAssociation(c1, true, c2, true);
    }
    
    /**
     * Builds an associationClass between classifier end1 and end2 with a 
     * default class.
     * @param end1
     * @param end2
     * @return MAssociationClass
     */
    public MAssociationClass buildAssociationClass(MClassifier end1, MClassifier end2) {
    	if (end1 == null || end2 == null || end1 instanceof MAssociationClass || end2 instanceof MAssociationClass) 
    		throw new IllegalArgumentException("In buildAssociationClass: either one of the arguments was null or " +
    			"was instanceof MAssociationClass");
        return buildAssociatonClass(buildClass(), end1, end2);
    } 
    
    /**
     * Builds a fully configurable association end. All variables for an associationend can
     * be given as parameter.
     * @param assoc The associaton this end will be part of
     * @param name The name of the association end
     * @param type The type (classifier) the end will connect. The end
     * is a connection piece between an association and a classifier
     * @param multi The multiplicity
     * @param stereo The stereotype
     * @param navigable The navigability. True if this association end can be 'passed' from the other
     * classifier.
     * @param order Ordering of the association
     * @param aggregation 
     * @param scope
     * @param changeable
     * @param visibility
     * @return MAssociationEnd
     */
    public MAssociationEnd buildAssociationEnd(MAssociation assoc, 
        String name,
        MClassifier type,
        MMultiplicity multi,
        MStereotype stereo,
        boolean navigable, 
        MOrderingKind order,
        MAggregationKind aggregation,
        MScopeKind scope,
        MChangeableKind changeable,
        MVisibilityKind visibility) 
    {
        // wellformednessrules and preconditions
        if (assoc == null || type == null) throw new IllegalArgumentException("In buildAssociationend: either type or association are null");
        if (type instanceof MDataType || type instanceof MInterface) {
        	if (!navigable) throw new IllegalArgumentException("In buildAssocationend: type is either datatype or interface and is navigable to");
        	List ends = new ArrayList();
        	ends.addAll(assoc.getConnections());
        	Iterator it = ends.iterator();
        	while (it.hasNext()) {
        		MAssociationEnd end = (MAssociationEnd)it.next();
        		if (end.isNavigable()) throw new IllegalArgumentException("In buildAssocationend: type is either datatype or interface and is navigable to");
        	}
        }
        if (aggregation != null && aggregation.equals(MAggregationKind.COMPOSITE)) {
        	if (multi != null && multi.getUpper() > 1) {
        		throw new IllegalArgumentException("In buildAssociationend: aggregation is composite and multiplicity > 1");
        	}
        }
        
        MAssociationEnd end = UmlFactory.getFactory().getCore().createAssociationEnd();		
        end.setAssociation(assoc);
        end.setType(type);	
        end.setName(name);
        if (multi != null) {
            end.setMultiplicity(multi);
        } else {
            end.setMultiplicity(MMultiplicity.M1_1);
        }
        if (stereo != null) {
            end.setStereotype(stereo);
        }
        end.setNavigable(navigable);
        if (order != null) {
            end.setOrdering(order);
        } else {
            end.setOrdering(MOrderingKind.UNORDERED);
        }
        if (aggregation != null) {
        	end.setAggregation(aggregation);
        } else { 
            end.setAggregation(MAggregationKind.NONE);
        }
        if (scope != null) {
            end.setTargetScope(scope);
        } else {
            end.setTargetScope(MScopeKind.INSTANCE);
        }
        if (changeable != null) {
            end.setChangeability(changeable);
        } else {
            end.setChangeability(MChangeableKind.CHANGEABLE);
        }
        if (visibility != null) {
            end.setVisibility(visibility);
        } else {
            end.setVisibility(MVisibilityKind.PUBLIC);
        }
        return end;
    }  
    
    public MAssociationEnd buildAssociationEnd(MClassifier type, MAssociation assoc) {
    	if (type == null || assoc == null) throw new IllegalArgumentException("In buildAssocationEnd: one of the arguments is null");
    	return buildAssociationEnd(assoc, "", type, null, null, true, null, null, null, null, MVisibilityKind.PUBLIC);
    }
    
    /**
     * Builds an association class from a class and two classifiers that should
     * be associated. Both ends of the associationclass are navigable.
     * @param cl
     * @param assoc
     * @return MAssociationClass
     */
    public MAssociationClass buildAssociatonClass(MClass cl, MClassifier end1, MClassifier end2) { 
    	if (end1 == null || end2 == null || cl == null || end1 instanceof MAssociationClass || end2 instanceof MAssociationClass) 
    		throw new IllegalArgumentException("In buildAssociationClass: either one of the arguments was null or " +
    			"was instanceof MAssociationClass");
       MAssociationClass assoc = createAssociationClass();
       assoc.setName(cl.getName());
       assoc.setAbstract(cl.isAbstract());
       assoc.setActive(cl.isActive());
       assoc.setAssociationEnds(cl.getAssociationEnds());
       assoc.setClassifierRoles(cl.getClassifierRoles());
       assoc.setClassifierRoles1(cl.getClassifierRoles1());
       assoc.setClassifiersInState(cl.getClassifiersInState());
       assoc.setClientDependencies(cl.getClientDependencies());
       assoc.setCollaborations(cl.getCollaborations());
       assoc.setCollaborations1(cl.getCollaborations1());
       assoc.setComments(cl.getComments());
       assoc.setConstraints(cl.getConstraints());
       assoc.setCreateActions(cl.getCreateActions());
       assoc.setFeatures(cl.getFeatures());
       assoc.setExtensions(cl.getExtensions());
       assoc.setGeneralizations(cl.getGeneralizations());
       assoc.setInstances(cl.getInstances());
       assoc.setLeaf(cl.isLeaf());
       assoc.setNamespace(cl.getNamespace());
       assoc.setObjectFlowStates(cl.getObjectFlowStates());
       assoc.setParameters(cl.getParameters());
       assoc.setParticipants(cl.getParticipants());
       assoc.setPartitions1(cl.getPartitions1());
       assoc.setPowertypeRanges(cl.getPowertypeRanges());
       assoc.setPresentations(cl.getPresentations());
       assoc.setRoot(cl.isRoot());
       assoc.setSourceFlows(cl.getSourceFlows());
       assoc.setSpecification(cl.isSpecification());
       assoc.setStereotype(cl.getStereotype());
       assoc.setStructuralFeatures(cl.getStructuralFeatures());
       assoc.setTaggedValues(cl.getTaggedValues());
       assoc.setVisibility(cl.getVisibility());
       buildAssociationEnd(assoc, null, end1,null, null, true, null, null, null, null, null);
       buildAssociationEnd(assoc, null, end2,null, null, true, null, null, null, null, null);
       return assoc;
    }      
    
    /**
     * Builds a default attribute.
     * @return MAttribute
     */
    public MAttribute buildAttribute() {
        //build the default attribute
        // this should not be here via the ProjectBrowser but the CoreHelper 
        // should provide this functionality
        ProjectBrowser pb = ProjectBrowser.TheInstance;
        Project p = pb.getProject();
        MClassifier intType = p.findType("int");
        if (p.getModel() != intType.getNamespace() && !ModelManagementHelper.getHelper().getAllNamespaces(p.getModel()).contains(intType.getNamespace())) {
        	intType.setNamespace(p.getModel());
        }
        MAttribute attr = createAttribute();
        attr.setName("newAttr");
        attr.setMultiplicity(UmlFactory.getFactory().getDataTypes().createMultiplicity(1, 1));
        attr.setStereotype(null);
        attr.setOwner(null);
        attr.setType(intType);
        attr.setInitialValue(null);
        attr.setVisibility(MVisibilityKind.PUBLIC);
        attr.setOwnerScope(MScopeKind.INSTANCE);
        attr.setChangeability(MChangeableKind.CHANGEABLE);
        attr.setTaggedValue("transient", "false");
        attr.setTaggedValue("volatile", "false");

       return attr;
    } 
    
    /**
     * Builds an attribute owned by some classifier cls. I don't know if this is
     * legal for an interface (purely UML speaking). In this method it is.
     * @param cls
     * @return MAttribute
     */
    public MAttribute buildAttribute(MClassifier cls) {
       MAttribute attr = buildAttribute();
       cls.addFeature(attr);
       return attr;
    }
    
    /**
     * Builds a binding between a client modelelement and a supplier 
     * modelelement
     * @param client
     * @param supplier
     * @return MBinding
     */
    public MBinding buildBinding(MModelElement client, MModelElement supplier) {
        // 2002-07-08
        // Jaap Branderhorst
        // checked for existence of client
        Collection clientDependencies = supplier.getClientDependencies();
        if (!clientDependencies.isEmpty()) {
            if (clientDependencies.contains(client)) {
                throw new IllegalArgumentException("Supplier has allready " +
                 "client " + client.getName() + " as Client");
            }
        }
        // end new code
        MBinding binding = createBinding();
        binding.addSupplier(supplier);
        binding.addClient(client);
        if (supplier.getNamespace() != null) 
            binding.setNamespace(supplier.getNamespace());
        else 
            if (client.getNamespace() != null) 
                binding.setNamespace(client.getNamespace());
        return binding;
    }
    
    /**
     * Builds a default implementation for a class. The class is not owned by 
     * any model element by default. Users should not forget to add ownership
     * @return MClass
     */
    public MClass buildClass() {
        MClass cl = createClass();
        // cl.setNamespace(ProjectBrowser.TheInstance.getProject().getModel());
        cl.setName("annon");
        cl.setStereotype(null);
        cl.setAbstract(false);
        cl.setActive(false);
        cl.setRoot(false);
        cl.setLeaf(false);
        cl.setSpecification(false);
        cl.setVisibility(MVisibilityKind.PUBLIC);
        return cl;
    }
    
    /**
     * Builds a class with a given name. 
     * @param name
     * @return MClass
     * @see #buildClass()
     */
    public MClass buildClass(String name) {
        MClass cl = buildClass();
        cl.setName(name);
        return cl;
    }
    
     /**
     * Builds a modelelement dependency between two modelelements. 
     * @param client
     * @param supplier
     * @return MDependency
     */
    public MDependency buildDependency(MModelElement client, MModelElement supplier) {
        MDependency dep = createDependency();
        dep.addSupplier(supplier);
        dep.addClient(client);
        if (supplier.getNamespace() != null) 
            dep.setNamespace(supplier.getNamespace());
        else 
            if (client.getNamespace() != null) 
                dep.setNamespace(client.getNamespace());
        return dep;
    }
    
    /**
     * Builds a generalization between a parent and a child. Does not check if 
     * multiple inheritance is allowed for the current notation.
     * @param child
     * @param parent
     * @return MGeneralization
     */
    public MGeneralization buildGeneralization(MGeneralizableElement child, MGeneralizableElement parent) {
        if (parent.getParents().contains(child)) return null;
        if (!child.getClass().equals(parent.getClass())) return null;
        Iterator it = parent.getGeneralizations().iterator();
        while (it.hasNext()) {
        	MGeneralization gen = (MGeneralization)it.next();
        	if (gen.getParent().equals(child)) return null;
        }
        if (parent.getNamespace() == null) return null;
        if (parent.isLeaf()) return null;
        if (child.isRoot()) return null;

        MGeneralization gen = createGeneralization();
        gen.setParent(parent);
        gen.setChild(child);
        if (parent.getNamespace() != null) gen.setNamespace(parent.getNamespace());
        else if (child.getNamespace() != null) gen.setNamespace(child.getNamespace());
        return gen;
    }
    
    /**
     * Builds a default method belonging to a certain operation. The language of the body is set to the 
     * selected Notation language. The body of the method is set to an emtpy string.
     * @param op
     * @return MMethod
     */
    public MMethod buildMethod(MOperation op) {
        return buildMethod(op, Notation.getDefaultNotation(), "");
    }
    
    /**
     * Builds a method belonging to a certain operation.
     * @param op The operation this method belongs to
     * @param notation The notationname (language name) of the body
     * @param body The actual body of the method
     * @return MMethod
     */
    public MMethod buildMethod(MOperation op, NotationName notation, String body) {
        MMethod method = createMethod();
        if (op != null) {
            method.setSpecification(op);
            MClassifier owner = op.getOwner();
            if (owner != null) {
                method.setOwner(owner);
            }
        }
        if (notation != null && notation.getName() != null) {
            method.setBody(UmlFactory.getFactory().getDataTypes().createProcedureExpression(notation.getName(), body));
        }
        return method;
    }   
    
   
    
    /**
     * Builds an operation for classifier cls.
     * @param cls
     * @return MOperation
     */
    public MOperation buildOperation(MClassifier cls) {
    	MOperation oper = createOperation();
        oper.setName("newOperation");
        oper.setStereotype(null);
        oper.setOwner(cls);
        oper.setVisibility(MVisibilityKind.PUBLIC);
        oper.setAbstract(false);
        oper.setLeaf(false);
        oper.setRoot(false);
        oper.setQuery(false);
        oper.setOwnerScope(MScopeKind.INSTANCE);
        oper.setConcurrency(MCallConcurrencyKind.SEQUENTIAL);
    
        MParameter returnParameter = buildParameter(oper);
        returnParameter.setKind(MParameterDirectionKind.RETURN);
        returnParameter.setName("return");
        return oper;
    }
    
    /**
     * Constructs a default parameter.
     *
     * @param oper  The operation where it is added to. 
     *          If null, it is not added.
     * @return      The newly created parameter.
     */
    public MParameter buildParameter() {
        // this should not be here via the ProjectBrowser but the CoreHelper 
        // should provide this functionality
        ProjectBrowser pb = ProjectBrowser.TheInstance;
        Project p = pb.getProject();
        MClassifier voidType = p.findType("void");
        if (voidType.getModel() != p.getModel()) {
        	voidType.setNamespace(p.getModel());
        }
        MParameter res = UmlFactory.getFactory().getCore().createParameter();
        res.setName(null);
        res.setStereotype(null);
        res.setType(voidType);
        res.setKind(MParameterDirectionKind.IN);
        res.setDefaultValue(null);
    
        return res;
    }
    
    /**
     * Constructs a default parameter and adds it to oper. The name is unique in the operation so 
     * no code generation problems will exist.
     *
     * @param oper  The operation where it is added to. 
     *          If null, it is not added.
     * @return      The newly created parameter.
     */
    public MParameter buildParameter(MBehavioralFeature oper) {
    	if (oper == null || oper.getOwner() == null) throw new IllegalArgumentException("In buildParameter: operation is null or does not have an owner");
        MParameter res = buildParameter();
    	String name = "arg";
    	int counter = 1;
       
        	Iterator it = oper.getParameters().iterator();
        	while (it.hasNext()) {
        		MParameter para = (MParameter)it.next();
        		if ((name + counter).equals(para.getName())) {
        			counter++;
        		} 
        	}
            oper.addParameter(res);
       
        res.setName(name + counter);
        return res;
    }
    
    /**
     * Constructs a default parameter, adds it to oper and sets its type 
     * (return etc.).
     *
     * @param oper  The operation where it is added to. 
     *          If null, it is not added.
     * @param directionKind The directionkind. If null it is not set.
     * @return      The newly created parameter.
     */
    public MParameter buildParameter(MBehavioralFeature oper, MParameterDirectionKind directionKind) {
        MParameter res = buildParameter(oper);
        if (directionKind != null) {
        	res.setKind(directionKind);
        }
        return res;
    }
    
    
    /**
     * Adds a parameter initialized to default values to a given event 
     * @param oper
     * @return MParameter
     */
    public MParameter buildParameter(MEvent event) {
        MParameter res = buildParameter();
    
        if (event != null)
            event.addParameter(res);
        return res;
    }
    
    /**
     * Builds a realization between some supplier (for example an interface in 
     * Java) and a client who implements the realization.
     * @param client
     * @param supplier
     * @return MAbstraction
     */
    public MAbstraction buildRealization(MModelElement client, MModelElement supplier) {
    	if (client == null || supplier == null || client.getNamespace() == null || supplier.getNamespace() == null) {
    		throw new IllegalArgumentException("In buildrealization faulty arguments.");
    	}
        MAbstraction realization = UmlFactory.getFactory().getCore().createAbstraction();
        MNamespace nsc = client.getNamespace();
        MNamespace nss = supplier.getNamespace();
        MNamespace ns = null;
        if (nsc != null && nsc.equals(nss)) {
        	ns = nsc;
        } else
        	ns = ProjectBrowser.TheInstance.getProject().getModel();
        ExtensionMechanismsFactory.getFactory().buildStereotype(realization, "realize", ns);
        client.addClientDependency(realization);
        supplier.addSupplierDependency(realization);
       
        return realization;
    }
    
    /**
     * Builds a usage between some client and a supplier. If client and supplier
     * do not have the same model, an illegalargumentexception is thrown.
     * @param client
     * @param supplier
     * @return MUsage
     */
    public MUsage buildUsage(MModelElement client, MModelElement supplier) {
        if (client.getModel() != supplier.getModel()) {
            throw new IllegalArgumentException("To construct a usage, the " +
            "client and the supplier must be part of the same model.");
        }
        MUsage usage = UmlFactory.getFactory().getCore().createUsage();
        usage.addSupplier(supplier);
        usage.addClient(client);
        if (supplier.getNamespace() != null) usage.setNamespace(supplier.getNamespace());
        else if (client.getNamespace() != null) usage.setNamespace(client.getNamespace());
        return usage;
    }
    
    /**
     * Builds a comment inluding a reference to the given modelelement to comment. 
     * If the element is null, the comment is still build since it is not 
     * mandatory to have an annotated element in the comment.
     * @param elementToComment
     * @return MComment
     */
    public MComment buildComment(MModelElement elementToComment) {
        MComment comment = createComment();
        if (elementToComment != null) {
            comment.addAnnotatedElement(elementToComment);
            comment.setNamespace(elementToComment.getModel());
        } else
            comment.setNamespace(ProjectBrowser.TheInstance.getProject().getModel());
        
        return comment;
    }
}

