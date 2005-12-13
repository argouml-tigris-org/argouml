// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.model.uml;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.argouml.model.CoreFactory;

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
import ru.novosoft.uml.foundation.core.MElement;
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
import ru.novosoft.uml.foundation.data_types.MBooleanExpression;
import ru.novosoft.uml.foundation.data_types.MCallConcurrencyKind;
import ru.novosoft.uml.foundation.data_types.MChangeableKind;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MOrderingKind;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;
import ru.novosoft.uml.foundation.data_types.MScopeKind;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.model_management.MModel;

/**
 * Factory to create UML classes for the UML
 * Foundation::Core package. <p>
 *
 * Feature, StructuralFeature, and PresentationElement do not have a
 * create method since it is called an "abstract metaclass" in the
 * UML specifications.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 * @author Jaap Branderhorst
 */
public class CoreFactoryImpl
	extends AbstractUmlModelFactory
	implements CoreFactory {

    /**
     * Logger.<p>
     */
    private static final Logger LOG = Logger.getLogger(CoreFactoryImpl.class);

    /**
     * The model implementation.
     */
    private NSUMLModelImplementation nsmodel;

    /**
     * Don't allow instantiation.
     *
     * @param implementation To get other helpers and factories.
     */
    CoreFactoryImpl(NSUMLModelImplementation implementation) {
        nsmodel = implementation;
    }

    /**
     * Create an empty but initialized instance of a UML Abstraction.
     *
     * @return an initialized UML Abstraction instance.
     */
    public Object createAbstraction() {
	Object modelElement =
	    MFactory.getDefaultFactory().createAbstraction();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Build an empty but initialized instance of a UML Abstraction
     * with a given name.
     *
     * @param name The name.
     * @return an initialized UML Abstraction instance.
     * @param supplier the supplier of the abstraction
     * @param client the client of the abstraction
     */
    public Object buildAbstraction(String name, Object supplier,
            Object client) {
        if (!(client instanceof MClassifier)
                || !(supplier instanceof MClassifier)) {
            throw new IllegalArgumentException(
                "The supplier and client of an abstraction"
                + "should be classifiers");
        }
        MAbstraction abstraction = (MAbstraction) createAbstraction();
        super.initialize(abstraction);
        nsmodel.getCoreHelper().setName(abstraction, name);
        abstraction.addClient((MClassifier) client);
        abstraction.addSupplier((MClassifier) supplier);
        return abstraction;
    }

    /**
     * Create an empty but initialized instance of a UML Association.
     *
     * @return an initialized UML Association instance.
     */
    public Object createAssociation() {
	MAssociation modelElement =
	    MFactory.getDefaultFactory().createAssociation();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML AssociationClass.
     *
     * @return an initialized UML AssociationClass instance.
     */
    public Object createAssociationClass() {
	MAssociationClass modelElement =
	    MFactory.getDefaultFactory().createAssociationClass();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML AssociationEnd.
     *
     * @return an initialized UML AssociationEnd instance.
     */
    public Object createAssociationEnd() {
	MAssociationEnd modelElement =
	    MFactory.getDefaultFactory().createAssociationEnd();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Attribute.
     *
     * @return an initialized UML Attribute instance.
     */
    public Object createAttribute() {
	MAttribute modelElement =
	    MFactory.getDefaultFactory().createAttribute();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Binding.
     *
     * @return an initialized UML Binding instance.
     */
    public Object createBinding() {
	MBinding modelElement = MFactory.getDefaultFactory().createBinding();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Class.
     *
     * @return an initialized UML Class instance.
     */
    public Object createClass() {
	MClass modelElement = MFactory.getDefaultFactory().createClass();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Comment.
     *
     * @return an initialized UML Comment instance.
     */
    public Object createComment() {
        MComment modelElement = MFactory.getDefaultFactory().createComment();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Component.
     *
     * @return an initialized UML Component instance.
     */
    public Object createComponent() {
	MComponent modelElement =
	    MFactory.getDefaultFactory().createComponent();
	super.initialize(modelElement);
	return modelElement;
    }



    /**
     * Create an empty but initialized instance of a UML Constraint.
     *
     * @return an initialized UML Constraint instance.
     */
    public Object createConstraint() {
	MConstraint modelElement =
	    MFactory.getDefaultFactory().createConstraint();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML DataType.
     *
     * @return an initialized UML DataType instance.
     */
    public Object createDataType() {
	MDataType modelElement = MFactory.getDefaultFactory().createDataType();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Dependency.
     *
     * @return an initialized UML Dependency instance.
     */
    public Object createDependency() {
	MDependency modelElement =
	    MFactory.getDefaultFactory().createDependency();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML ElementResidence.
     *
     * @return an initialized UML ElementResidence instance.
     */
    public Object createElementResidence() {
	MElementResidence modelElement =
	    MFactory.getDefaultFactory().createElementResidence();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Flow.
     *
     * @return an initialized UML Flow instance.
     */
    public Object createFlow() {
	MFlow modelElement = MFactory.getDefaultFactory().createFlow();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Generalization.
     *
     * @return an initialized UML Generalization instance.
     */
    public Object createGeneralization() {
	MGeneralization modelElement =
	    MFactory.getDefaultFactory().createGeneralization();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Interface.
     *
     * @return an initialized UML Interface instance.
     */
    public Object createInterface() {
	MInterface modelElement =
	    MFactory.getDefaultFactory().createInterface();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Method.
     *
     * @return an initialized UML Method instance.
     */
    public Object createMethod() {
	MMethod modelElement = MFactory.getDefaultFactory().createMethod();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Node.
     *
     * @return an initialized UML Node instance.
     */
    public Object createNode() {
	MNode modelElement = MFactory.getDefaultFactory().createNode();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Operation.
     *
     * @return an initialized UML Operation instance.
     */
    public Object createOperation() {
	MOperation modelElement =
	    MFactory.getDefaultFactory().createOperation();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Parameter.
     *
     * @return an initialized UML Parameter instance.
     */
    public Object createParameter() {
	MParameter modelElement =
	    MFactory.getDefaultFactory().createParameter();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Permission.
     *
     * @return an initialized UML Permission instance.
     */
    public Object createPermission() {
	MPermission modelElement =
	    MFactory.getDefaultFactory().createPermission();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML TemplateParameter.
     *
     * @return an initialized UML TemplateParameter instance.
     */
    public Object createTemplateParameter() {
	MTemplateParameter modelElement =
	    MFactory.getDefaultFactory().createTemplateParameter();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Usage.
     *
     * @return an initialized UML Usage instance.
     */
    public Object createUsage() {
	MUsage modelElement = MFactory.getDefaultFactory().createUsage();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Builds a default binary association with two default association ends.
     * @param c1 The first classifier to connect to
     * @param nav1 The navigability of the Associaton end
     * @param agg1 The aggregation type of the second Associaton end
     * @param c2 The second classifier to connect to
     * @param nav2 The navigability of the second Associaton end
     * @param agg2 The aggregation type of the second Associaton end
     * @return MAssociation
     * @throws IllegalArgumentException if either Classifier is null
     */
    private MAssociation buildAssociation(MClassifier c1,
					 boolean nav1,
					 MAggregationKind agg1,
					 MClassifier c2,
					 boolean nav2,
					 MAggregationKind agg2) {
        if (c1 == null || c2 == null) {
            throw new IllegalArgumentException("one of "
					       + "the classifiers to be "
					       + "connected is null");
        }
        MNamespace ns1 = c1.getNamespace();
        MNamespace ns2 = c2.getNamespace();
        if (ns1 == null || ns2 == null) {
            throw new IllegalArgumentException("one of "
					       + "the classifiers does not "
					       + "belong to a namespace");
        }
        MAssociation assoc = (MAssociation) createAssociation();
        assoc.setName("");
        assoc.setNamespace(
                (MNamespace) nsmodel.getCoreHelper()
                	.getFirstSharedNamespace(ns1, ns2));
        buildAssociationEnd(
			    assoc,
			    null,
			    c1,
			    null,
			    null,
			    nav1,
			    null,
			    agg1,
			    null,
			    null,
			    null);
        buildAssociationEnd(
			    assoc,
			    null,
			    c2,
			    null,
			    null,
			    nav2,
			    null,
			    agg2,
			    null,
			    null,
			    null);
        return assoc;
    }

    /**
     * Builds a binary associationrole on basis of two classifierroles,
     * navigation and aggregation.
     *
     * @param fromClassifier   the first given classifier
     * @param aggregationKind1 the first aggregationkind
     * @param toClassifier     the second given classifier
     * @param aggregationKind2 the second aggregationkind
     * @param unidirectional true if unidirectional
     * @return the newly build binary associationrole
     */
    public Object buildAssociation(
					 Object fromClassifier,
					 Object aggregationKind1,
					 Object toClassifier,
					 Object aggregationKind2,
					 Boolean unidirectional) {
        if (fromClassifier == null || toClassifier == null) {
                throw new IllegalArgumentException("one of "
        				       + "the classifiers to be "
        				       + "connected is null");
        }
        MClassifier from = (MClassifier) fromClassifier;
        MClassifier to = (MClassifier) toClassifier;
        MAggregationKind agg1 = (MAggregationKind) aggregationKind1;
        MAggregationKind agg2 = (MAggregationKind) aggregationKind2;

        MNamespace ns1 = from.getNamespace();
        MNamespace ns2 = to.getNamespace();
        if (ns1 == null || ns2 == null) {
                throw new IllegalArgumentException("one of "
        				       + "the classifiers does not "
        				       + "belong to a namespace");
        }
        MAssociation assoc = (MAssociation) createAssociation();
        assoc.setName("");
        assoc.setNamespace((MNamespace)
        		   nsmodel.getCoreHelper().getFirstSharedNamespace(ns1,
        								  ns2));

        boolean nav1 = true;
        boolean nav2 = true;

        if (from instanceof MInterface) {
            nav2 = false;
            agg2 = agg1;
            agg1 = null;
        } else if (to instanceof MInterface) {
            nav1 = false;
        } else {
            nav1 = !Boolean.TRUE.equals(unidirectional);
            nav2 = true;
        }

        buildAssociationEnd(
			    assoc,
			    null,
			    from,
			    null,
			    null,
			    nav1,
			    null,
			    agg1,
			    null,
			    null,
			    null);
        buildAssociationEnd(
			    assoc,
			    null,
			    to,
			    null,
			    null,
			    nav2,
			    null,
			    agg2,
			    null,
			    null,
			    null);

        return assoc;
    }


    /**
     * Builds a binary associations between two classifiers with
     * default values for the association ends and the association
     * itself.<p>
     *
     * @param classifier1 The first classifier to connect
     * @param classifier2 The second classifier to connect
     * @return MAssociation
     */
    public Object buildAssociation(
            Object classifier1,
			Object classifier2) {
        MClassifier c1 = (MClassifier) classifier1;
        MClassifier c2 = (MClassifier) classifier2;
        return buildAssociation(c1, true, MAggregationKind.NONE,
                                c2, true, MAggregationKind.NONE);
    }

    /**
     * Builds a binary association with a direction, aggregation
     * and a given name.
     *
     * @param c1 The first classifier to connect to
     * @param nav1 The navigability of the Associaton end
     * @param c2 The second classifier to connect to
     * @param nav2 The navigability of the second Associaton end
     * @param name the given name
     * @return association
     */
    public Object buildAssociation(Object c1, boolean nav1,
				   Object c2, boolean nav2, String name) {
        MAssociation assoc =
	    buildAssociation((MClassifier) c1, nav1, MAggregationKind.NONE,
			     (MClassifier) c2, nav2, MAggregationKind.NONE);
        if (assoc != null) {
            assoc.setName(name);
        }
        return assoc;
    }

    /**
     * Builds an associationClass between classifier end1 and end2 with a
     * default class.<p>
     *
     * @param end1 the first given classifier
     * @param end2 the second given classifier
     * @return MAssociationClass
     */
    public Object buildAssociationClass(Object end1, Object end2) {
        if (end1 == null
                || end2 == null
                || !(end1 instanceof MClassifier)
                || !(end2 instanceof MClassifier)) {
            throw new IllegalArgumentException(
                    "either one of the arguments was null");
        }
        return buildAssociationClass(
                (MClass) buildClass(),
                (MClassifier) end1, (MClassifier) end2);
    }

    /**
     * Builds a fully configurable association end. All variables for
     * an associationend can be given as parameter.
     *
     * @param assoc The associaton this end will be part of
     * @param name The name of the association end
     * @param type The type (classifier) the end will connect. The end
     * is a connection piece between an association and a classifier
     * @param multi The multiplicity
     * @param stereo The stereotype
     * @param navigable The navigability. True if this association end
     *                  can be 'passed' from the other classifier.
     * @param order Ordering of the association
     * @param aggregation the aggregationkind
     * @param scope the scope kind
     * @param changeable the changeablekind
     * @param visibility the visibilitykind
     * @return MAssociationEnd
     */
    public Object buildAssociationEnd(
					       Object assoc,
					       String name,
					       Object type,
					       Object multi,
					       Object stereo,
					       boolean navigable,
					       Object order,
					       Object aggregation,
					       Object scope,
					       Object changeable,
					       Object visibility) {
        // wellformednessrules and preconditions
        if (assoc == null
                || !(assoc instanceof MAssociation)
                || type == null
                || !(type instanceof MClassifier)) {
            throw new IllegalArgumentException("either type or association "
					       + "are null");
        }
        if (multi != null
                && !(multi instanceof MMultiplicity)) {
            throw new IllegalArgumentException("Multiplicity");
        }
        if (stereo != null
                && !(stereo instanceof MStereotype)) {
            throw new IllegalArgumentException("Stereotype");
        }
        if (order != null
                && !(order instanceof MOrderingKind)) {
            throw new IllegalArgumentException("OrderingKind");
        }
        if (aggregation != null
                && !(aggregation instanceof MAggregationKind)) {
            throw new IllegalArgumentException("AggregationKind");
        }
        if (scope != null
                && !(scope instanceof MScopeKind)) {
            throw new IllegalArgumentException("ScopeKind");
        }
        if (changeable != null
                && !(changeable instanceof MChangeableKind)) {
            throw new IllegalArgumentException("ChangeableKind");
        }
        if (visibility != null
                && !(visibility instanceof MVisibilityKind)) {
            throw new IllegalArgumentException("VisibilityKind");
        }

        if (type instanceof MDataType || type instanceof MInterface) {
            if (!navigable) {
                throw new IllegalArgumentException(
                        "Wellformedness rule 2.5.3.3 [1] is broken. "
                        + "The Classifier of an AssociationEnd cannot be an "
                        + "Interface or a DataType if the association is "
                        + "navigable away from that end.");
            }
            List ends = new ArrayList();
            ends.addAll(((MAssociation) assoc).getConnections());
            Iterator it = ends.iterator();
            while (it.hasNext()) {
                MAssociationEnd end = (MAssociationEnd) it.next();
                if (end.isNavigable()) {
                    throw new IllegalArgumentException("type is either "
						       + "datatype or "
						       + "interface and is "
						       + "navigable to");
                }
            }
        }
        if (aggregation != null
                && aggregation.equals(MAggregationKind.COMPOSITE)
                && multi != null
                && ((MMultiplicity) multi).getUpper() > 1) {
            throw new IllegalArgumentException("aggregation is composite "
					   + "and multiplicity > 1");
        }

        MAssociationEnd end =
            (MAssociationEnd)
            	nsmodel.getCoreFactory().createAssociationEnd();
        end.setAssociation((MAssociation) assoc);
        end.setType((MClassifier) type);
        end.setName(name);
        if (multi != null) {
            end.setMultiplicity((MMultiplicity) multi);
        } else {
            end.setMultiplicity(MMultiplicity.M1_1);
        }
        if (stereo != null) {
            end.setStereotype((MStereotype) stereo);
        }
        end.setNavigable(navigable);
        if (order != null) {
            end.setOrdering((MOrderingKind) order);
        } else {
            end.setOrdering(MOrderingKind.UNORDERED);
        }
        if (aggregation != null) {
            end.setAggregation((MAggregationKind) aggregation);
        } else {
            end.setAggregation(MAggregationKind.NONE);
        }
        if (scope != null) {
            end.setTargetScope((MScopeKind) scope);
        } else {
            end.setTargetScope(MScopeKind.INSTANCE);
        }
        if (changeable != null) {
            end.setChangeability((MChangeableKind) changeable);
        } else {
            end.setChangeability(MChangeableKind.CHANGEABLE);
        }
        if (visibility != null) {
            end.setVisibility((MVisibilityKind) visibility);
        } else {
            end.setVisibility(MVisibilityKind.PUBLIC);
        }
        return end;
    }

    /**
     * Builds a simply configured association end.
     *
     * @param type the given classifier
     * @param assoc the given association
     * @return the newly build associationend
     */
    public Object buildAssociationEnd(
            Object type,
            Object assoc) {
        if (type == null
                || !(type instanceof MClassifier)
                || assoc == null
                || !(assoc instanceof MAssociation)) {
            throw new IllegalArgumentException("one of the arguments is null");
        }
        return buildAssociationEnd(
                assoc,
                "",
                type,
                null,
                null,
                true,
                null,
                null,
                null,
                null,
                MVisibilityKind.PUBLIC);
    }

    /**
     * Builds an association class from a class and two classifiers
     * that should be associated. Both ends of the associationclass
     * are navigable.<p>
     *
     * @param cl the class
     * @param end1 the first classifier
     * @param end2 the second classifier
     * @return MAssociationClass
     */
    private MAssociationClass buildAssociationClass(MClass cl,
            MClassifier end1, MClassifier end2) {
        if (end1 == null
                || end2 == null
                || cl == null) {
            throw new IllegalArgumentException(
                    "either one of the arguments was null");
        }
        MAssociationClass assoc = (MAssociationClass) createAssociationClass();
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
        buildAssociationEnd(assoc,
        		    null,
        		    end1,
        		    null,
        		    null,
        		    true,
        		    null,
        		    null,
        		    null,
        		    null,
        		    null);
        buildAssociationEnd(assoc,
        		    null,
        		    end2,
        		    null,
        		    null,
        		    true,
        		    null,
        		    null,
        		    null,
        		    null,
        		    null);
        return assoc;
    }

    /**
     * Builds a default attribute.
     *
     * @see org.argouml.model.CoreFactory#buildAttribute(java.lang.Object,
     *         java.lang.Object)
     */
    public Object buildAttribute(Object model, Object theIntType) {
        //build the default attribute
        // this should not be here via the ProjectBrowser but the CoreHelper
        // should provide this functionality
        MClassifier intType = (MClassifier) theIntType;
//        Project p = ProjectManager.getManager().getCurrentProject();
//        MClassifier intType = (MClassifier) p.findType("int");
        if (model != intType.getNamespace()
                && !(nsmodel.getModelManagementHelper()
                    .getAllNamespaces(model)
        	        .contains(intType.getNamespace()))) {
            intType.setNamespace((MModel) model);
        }
        MAttribute attr = (MAttribute) createAttribute();
        attr.setName("newAttr");
        attr.setMultiplicity(
                (MMultiplicity) nsmodel.getDataTypesFactory()
                	.createMultiplicity(1, 1));
        attr.setStereotype(null);
        attr.setOwner(null);
        attr.setType(intType);
        attr.setInitialValue(null);
        attr.setVisibility(MVisibilityKind.PUBLIC);
        attr.setOwnerScope(MScopeKind.INSTANCE);
        attr.setChangeability(MChangeableKind.CHANGEABLE);
        attr.setTargetScope(MScopeKind.INSTANCE);

        return attr;
    }

    /**
     * Builds an attribute owned by some classifier cls. I don't know
     * if this is legal for an interface (purely UML speaking). In
     * this method it is.
     *
     * @see org.argouml.model.CoreFactory#buildAttribute(java.lang.Object,
     *         java.lang.Object, java.lang.Object, java.util.Collection)
     */
    public Object buildAttribute(Object handle, Object model, Object intType,
                                 Collection propertyChangeListeners) {
        if (!(handle instanceof MClassifier)
                && !(handle instanceof MAssociationEnd)) {
            return null;
        }
        MAttribute attr = null;
        if (handle instanceof MClassifier) {
            MClassifier cls = (MClassifier) handle;
            attr = (MAttribute) buildAttribute(model, intType);
            cls.addFeature(attr);
        }
        if (handle instanceof MAssociationEnd) {
            MAssociationEnd assend = (MAssociationEnd) handle;
            attr = (MAttribute) buildAttribute(model, intType);
            assend.addQualifier(attr);
            //attr.setAssociationEnd((MAssociationEnd) handle);
        }
        // we set the listeners to the figs here too
        // it would be better to do that in the figs themselves
        Iterator it = propertyChangeListeners.iterator();

        while (it.hasNext()) {
            PropertyChangeListener listener =
                    (PropertyChangeListener) it.next();
            // nsmodel.getPump().removeModelEventListener(listener, attr);
            nsmodel.getModelEventPump().addModelEventListener(listener, attr);
        }
        return attr;
    }

    /**
     * Builds a default implementation for a class. The class is not owned by
     * any model element by default. Users should not forget to add ownership
     * @return MClass
     */
    public Object buildClass() {
        MClass cl = (MClass) createClass();
        // cl.setNamespace(ProjectBrowser.getInstance().getProject()
        // .getModel());
        cl.setName("");
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
     * Builds a class with a given namespace.
     *
     * @param owner the namespace
     * @return MClass
     * @see #buildClass()
     */
    public Object buildClass(Object owner) {
        Object clazz = buildClass();
        if (owner instanceof MNamespace) {
            nsmodel.getCoreHelper().setNamespace(clazz, /*MNamespace*/ owner);
        }
        return clazz;
    }

    /**
     * Builds a class with a given name.
     *
     * @param name the given name
     * @return MClass
     * @see #buildClass()
     */
    public Object buildClass(String name) {
        Object clazz = buildClass();
        nsmodel.getCoreHelper().setName(clazz, name);
        return clazz;
    }

    /**
     * Builds a class with a given name and namespace.
     *
     * @param name the given name
     * @param owner the namespace
     * @return MClass
     * @see #buildClass()
     */
    public Object buildClass(String name, Object owner) {
        Object clazz = buildClass();
        nsmodel.getCoreHelper().setName(clazz, name);
        if (owner instanceof MNamespace) {
            nsmodel.getCoreHelper().setNamespace(clazz, /*MNamespace*/ owner);
        }
        return clazz;
    }

    /**
     * Builds a default implementation for an interface. The interface
     * is not owned by any model element by default. Users should not
     * forget to add ownership.
     *
     * @return MInterface
     */
    public Object buildInterface() {
	MInterface cl = (MInterface) createInterface();
	// cl.setNamespace(ProjectBrowser.getInstance().getProject()
	// .getModel());
	cl.setName("");
	cl.setStereotype(null);
	cl.setAbstract(false);
	cl.setRoot(false);
	cl.setLeaf(false);
	cl.setSpecification(false);
	cl.setVisibility(MVisibilityKind.PUBLIC);
	return cl;
    }

    /**
     * Builds an interface with a given namespace.
     *
     * @param owner is the owner
     * @return MInterface
     * @see #buildInterface()
     */
    public Object buildInterface(Object owner) {
	MInterface cl = (MInterface) buildInterface();
	if (owner instanceof MNamespace) {
	    cl.setNamespace((MNamespace) owner);
	}
	return cl;
    }

    /**
     * Builds an interface with a given name.
     *
     * @param name is the given name.
     * @return MInterface
     * @see #buildInterface()
     */
    public Object buildInterface(String name) {
	MInterface cl = (MInterface) buildInterface();
	cl.setName(name);
	return cl;
    }

    /**
     * Builds an interface with a given name and namespace.
     *
     * @param name is the given name
     * @param owner is the namespace
     * @return MInterface
     * @see #buildInterface()
     */
    public Object buildInterface(String name, Object owner) {
	MInterface cl = (MInterface) buildInterface();
	cl.setName(name);
	if (owner instanceof MNamespace) {
	    cl.setNamespace((MNamespace) owner);
	}
	return cl;
    }

    /**
     * Builds a datatype with a given name and namespace.
     *
     * @param name is the name
     * @param owner is the namespace
     * @return an initialized UML DataType instance.
     */
    public Object buildDataType(String name, Object owner) {
	MDataType dt = (MDataType) createDataType();
	dt.setName(name);
	if (owner instanceof MNamespace) {
	    dt.setNamespace((MNamespace) owner);
	}
	return dt;
    }

    /**
     * @see org.argouml.model.CoreFactory#buildEnumeration(java.lang.String, java.lang.Object)
     */
    public Object buildEnumeration(String name, Object owner) {
        // TODO: Auto-generated method stub
        return null;
    }

    /**
     * Builds a modelelement dependency between two modelelements.<p>
     *
     * @param clientObj is the client
     * @param supplierObj is the supplier
     * @return MDependency
     */
    public Object buildDependency(Object clientObj,
				       Object supplierObj) {

	MModelElement client = (MModelElement) clientObj;
	MModelElement supplier = (MModelElement) supplierObj;
	if (client == null
                || supplier == null
                || client.getNamespace() == null
                || supplier.getNamespace() == null) {
	    throw new IllegalArgumentException("client or supplier is null "
					       + "or their namespaces.");
	}
	MDependency dep = (MDependency) createDependency();
	dep.addSupplier(supplier);
	dep.addClient(client);
	if (supplier.getNamespace() != null) {
	    dep.setNamespace(supplier.getNamespace());
	} else if (client.getNamespace() != null) {
	    dep.setNamespace(client.getNamespace());
	}
	return dep;
    }

    /**
     * Builds a modelelement permission between two modelelements.
     *
     * @param clientObj is the client
     * @param supplierObj is the supplier
     * @return MPermission
     */
    public Object buildPermission(Object clientObj,
				       Object supplierObj) {

	MModelElement client = (MModelElement) clientObj;
	MModelElement supplier = (MModelElement) supplierObj;
	if (client == null
	    || supplier == null
	    || client.getNamespace() == null
	    || supplier.getNamespace() == null) {
	    throw new IllegalArgumentException("client or supplier is null "
					       + "or their namespaces.");
	}
	MPermission per = (MPermission) createPermission();
	per.addSupplier(supplier);
	per.addClient(client);
	if (supplier.getNamespace() != null) {
	    per.setNamespace(supplier.getNamespace());
	} else if (client.getNamespace() != null) {
	    per.setNamespace(client.getNamespace());
	}
	nsmodel.getExtensionMechanismsFactory()
	    .buildStereotype(per, "import", per.getNamespace());
	return per;
    }

    /**
     * Builds a generalization between a parent and a child with a given name.
     *
     * @param child is the child
     * @param parent is the parent
     * @param name is the given name
     * @return generalization
     */
    public Object buildGeneralization(Object child, Object parent,
				      String name) {
        if (child == null
	    || parent == null
	    || !(child instanceof MGeneralizableElement)
	    || !(parent instanceof MGeneralizableElement)) {
            return null;
        }
        Object gen = buildGeneralization(child, parent);
        if (gen != null) {
            ((MGeneralization) gen).setName(name);
        }
        return gen;
    }

    /**
     * Builds a generalization between a parent and a child. Does not check if
     * multiple inheritance is allowed for the current notation.
     *
     * @param child1 is the child
     * @param parent1 is the parent
     * @return MGeneralization
     */
    public Object buildGeneralization(Object child1, Object parent1) {
        if (!(child1 instanceof MGeneralizableElement)
	    || !(parent1 instanceof MGeneralizableElement)) {
            throw new IllegalArgumentException();
        }

        MGeneralizableElement child = (MGeneralizableElement) child1;
        MGeneralizableElement parent = (MGeneralizableElement) parent1;

        if (parent.getParents().contains(child)) {
            return null;
        }
        Iterator it = parent.getGeneralizations().iterator();
        while (it.hasNext()) {
            MGeneralization gen = (MGeneralization) it.next();
            if (gen.getParent().equals(child)) {
                return null;
            }
        }

        if (parent.getNamespace() == null) {
            throw new IllegalArgumentException("parent has no namespace");
        }
        if (parent.isLeaf()) {
            throw new IllegalArgumentException("parent is leaf");
        }
        if (child.isRoot()) {
            throw new IllegalArgumentException("child is root");
        }

        MGeneralization gen = (MGeneralization) createGeneralization();
        gen.setParent(parent);
        gen.setChild(child);
        if (parent.getNamespace() != null) {
            gen.setNamespace(parent.getNamespace());
        } else if (child.getNamespace() != null) {
            gen.setNamespace(child.getNamespace());
        }
        return gen;
    }


    /**
     * Builds a method with a given name.
     *
     * @param name is the given name
     * @return method
     */
    public Object buildMethod(String name) {
        MMethod method = (MMethod) createMethod();
        if (method != null) {
            method.setName(name);
        }
        return method;
    }

    /**
     * Builds an operation for a classifier.
     *
     * @param classifier is the given classifier
     * @param model is the model to which the class belongs
     * @param voidType the type of the return parameter
     * @param propertyChangeListeners the listeners
     * @return the operation
     */
    public Object buildOperation(Object classifier, Object model,
            Object voidType, Collection propertyChangeListeners) {
        if (!(classifier instanceof MClassifier)) {
            throw new IllegalArgumentException("Handle is not a classifier");
        }
        MClassifier cls = (MClassifier) classifier;
        MOperation oper = (MOperation) createOperation();
        oper.setName("newOperation");
        oper.setStereotype(null);
        oper.setOwner(cls);
        oper.setVisibility(MVisibilityKind.PUBLIC);
        oper.setAbstract(false);
        oper.setLeaf(false);
        oper.setRoot(false);
        oper.setQuery(false);
        oper.setOwnerScope(MScopeKind.INSTANCE);
        // Jaap Branderhorst 6-4-2003 commented out next line since an
        // operation cannot have two owners.  the owner must be the
        // owning classifier which must be set via the setOwner
        // method, not via the namespace.
        //
        // oper.setNamespace(cls);
        oper.setConcurrency(MCallConcurrencyKind.SEQUENTIAL);

        MParameter returnParameter =
            (MParameter)
            	buildParameter(oper, model, voidType, propertyChangeListeners);
        returnParameter.setKind(MParameterDirectionKind.RETURN);
        returnParameter.setName("return");
        // we set the listeners to the figs here too it would be
        // better to do that in the figs themselves the
        // elementlistener for the parameter is allready set in
        // buildparameter(oper)
        Iterator it = propertyChangeListeners.iterator();
        while (it.hasNext()) {
            PropertyChangeListener listener =
                (PropertyChangeListener) it.next();
            // UmlModelEventPump.getPump().removeModelEventListener(listener,
            // oper);
            nsmodel.getModelEventPump().addModelEventListener(listener, oper);
        }
        return oper;
    }

    /**
     * Builds an operation with a given name for classifier.
     *
     * @param cls is the classifier that shall own the operation
     * @param model is the model that contains the class
     * @param voidType the type of the return parameter
     * @param name the given name for the operation
     * @param propertyChangeListeners the listeners
     * @return the operation
     */
    public Object buildOperation(Object cls, Object model, Object voidType,
            String name, Collection propertyChangeListeners) {
        Object oper =
            buildOperation(cls, model, voidType, propertyChangeListeners);
        if (oper != null) {
            ((MOperation) oper).setName(name);
        }
        return oper;
    }

    /**
     * Constructs a default parameter.
     *
     * @return      The newly created parameter.
     */
    private Object buildParameter(MModel model, MClassifier voidType) {
        // this should not be here via the ProjectBrowser but the CoreHelper
        // should provide this functionality
        if (voidType.getModel() != model) {
            voidType.setNamespace(model);
        }
        MParameter res =
            (MParameter) nsmodel.getCoreFactory().createParameter();
        res.setName("");
        res.setStereotype(null);
        res.setType(voidType);
        res.setKind(MParameterDirectionKind.IN);
        res.setDefaultValue(null);

        return res;
    }

    /**
     * Adds a parameter initialized to default values to a given event
     * or behavioral feature.
     *
     * @param o an event or behavioral feature
     * @param model the model to which the event or behavioral feature belongs
     * @param voidType the type of the return parameter
     * @param propertyChangeListeners the listeners
     * @return the parameter
     */
    public Object buildParameter(Object o, Object model, Object voidType,
            Collection propertyChangeListeners) {
        if (o instanceof MEvent) {
            MEvent event = (MEvent) o;
            MParameter res =
                (MParameter) buildParameter((MModel) model,
                        (MClassifier) voidType);
            res.setKind(MParameterDirectionKind.IN);
            //    removing this next line solves issue 2209
            //res.setNamespace(event.getNamespace());
            event.addParameter(res);
            return res;
        } else if (o instanceof MBehavioralFeature) {
            MBehavioralFeature oper = (MBehavioralFeature) o;
            if (oper == null || oper.getOwner() == null) {
                throw new IllegalArgumentException(
                        "operation is null or does not have an owner");
            }
            MParameter res =
                (MParameter) buildParameter((MModel) model,
                        (MClassifier) voidType);
            String name = "arg";
            int counter = 1;

            oper.addParameter(res);
            Iterator it = oper.getParameters().iterator();
            while (it.hasNext()) {
                MParameter para = (MParameter) it.next();
                if ((name + counter).equals(para.getName())) {
                    counter++;
                }
            }

            res.setName(name + counter);

            // we set the listeners to the figs here too
            // it would be better to do that in the figs themselves
            it = propertyChangeListeners.iterator();
            while (it.hasNext()) {
                PropertyChangeListener listener =
                    (PropertyChangeListener) it.next();
                // nsmodel.getModelEventPump()
                //	.removeModelEventListener(listener, res);
                nsmodel.getModelEventPump()
                	.addModelEventListener(listener, res);
            }
            return res;
        } else {
            return null;
        }
    }

    /**
     * Builds a realization between some supplier (for example an
     * interface in Java) and a client who implements the realization.
     *
     * @param clnt is the client
     * @param spplr is the supplier
     * @param model the namespace to use if client and
     *              supplier are of different namespace
     * @return Object the created abstraction
     */
    public Object buildRealization(
            Object clnt,
            Object spplr,
            Object model) {
        MModelElement client = (MModelElement) clnt;
        MModelElement supplier = (MModelElement) spplr;
	if (client == null
	    || supplier == null
	    || client.getNamespace() == null
	    || supplier.getNamespace() == null) {
	    throw new IllegalArgumentException("faulty arguments.");
	}
	Object realization = createAbstraction();
	MNamespace nsc = client.getNamespace();
	MNamespace nss = supplier.getNamespace();
	MNamespace ns = null;
	if (nsc.equals(nss)) {
	    ns = nsc;
	} else {
	    ns = (MNamespace) model;
	}
	nsmodel.getExtensionMechanismsFactory().buildStereotype(realization,
								"realize", ns);
	nsmodel.getCoreHelper().addClientDependency(client, realization);
	nsmodel.getCoreHelper().addSupplierDependency(supplier, realization);
	return realization;
    }

    /**
     * Builds a usage between some client and a supplier. If client
     * and supplier do not have the same model, an
     * {@link IllegalArgumentException} is thrown.
     *
     * @param client is the client
     * @param supplier is the supplier
     * @return MUsage
     */
    public Object buildUsage(Object client, Object supplier) {
	if (client == null || supplier == null) {
	    throw new IllegalArgumentException("In buildUsage null arguments.");
	}
	if (!(client instanceof MModelElement)) {
	    throw new IllegalArgumentException("client ModelElement");
	}
	if (!(supplier instanceof MModelElement)) {
	    throw new IllegalArgumentException("supplier ModelElement");
	}

	if (((MModelElement) client).getModel()
	        != ((MModelElement) supplier).getModel()) {
	    throw new IllegalArgumentException("To construct a usage, the "
					       + "client and the supplier "
					       + "must be part of the same "
					       + "model.");
	}
	MUsage usage = (MUsage) nsmodel.getCoreFactory().createUsage();
	usage.addSupplier((MModelElement) supplier);
	usage.addClient((MModelElement) client);
	if (((MModelElement) supplier).getNamespace() != null) {
	    usage.setNamespace(((MModelElement) supplier).getNamespace());
	} else if (((MModelElement) client).getNamespace() != null) {
	    usage.setNamespace(((MModelElement) client).getNamespace());
	}
	return usage;
    }

    /**
     * Builds a comment inluding a reference to the given modelelement
     * to comment.  If the element is null, the comment is still build
     * since it is not mandatory to have an annotated element in the
     * comment.
     *
     * @param element is the model element
     * @param model the namespace for the comment
     * @return MComment
     */
    public Object buildComment(Object element, Object model) {
        if (model == null) {
            throw new IllegalArgumentException("A namespace must be supplied.");
        }
        MModelElement elementToAnnotate = (MModelElement) element;
        MComment comment = (MComment) createComment();

        MNamespace commentsModel = null;
        if (elementToAnnotate != null) {
            comment.addAnnotatedElement(elementToAnnotate);
            commentsModel = elementToAnnotate.getModel();
        } else {
            commentsModel = (MNamespace) model;
        }

        comment.setNamespace(commentsModel);

        return comment;
    }


    /**
     * Builds a constraint that constraints the given modelelement.
     * The namespace of the constraint will be the same as the
     * namespace of the given modelelement.<p>
     *
     * @param constrElement The constrained element.
     * @return MConstraint
     */
    public Object buildConstraint(Object constrElement) {
        MModelElement constrainedElement = (MModelElement) constrElement;
	if (constrainedElement == null) {
	    throw new IllegalArgumentException("the constrained element is "
					       + "mandatory and may not be "
					       + "null.");
	}
	MConstraint con = (MConstraint) createConstraint();
	con.addConstrainedElement(constrainedElement);
	con.setNamespace(constrainedElement.getNamespace());
	return con;
    }

    /**
     * Builds a constraint with a given name and boolean expression.<p>
     *
     * @param name is the given name
     * @param bexpr boolean expression
     * @return constraint
     */
    public Object buildConstraint(String name, Object bexpr) {
	if (bexpr == null || !(bexpr instanceof MBooleanExpression)) {
	    throw new IllegalArgumentException("invalid boolean expression.");
	}
	MConstraint con = (MConstraint) createConstraint();
	if (name != null) {
	    con.setName(name);
	}
	con.setBody((MBooleanExpression) bexpr);
	return con;
    }

    /**
     * @param elem the abstraction to be deleted
     */
    void deleteAbstraction(Object elem) {
    }

    /**
     * @param elem the association to be deleted
     */
    void deleteAssociation(Object elem) {
        if (!(elem instanceof MAssociation)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @param elem the a. to be deleted
     */
    void deleteAssociationClass(Object elem) {
        if (!(elem instanceof MAssociationClass)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Does a 'cascading delete' to all modelelements that are associated
     * with this element that would be in an illegal state after deletion
     * of the element. Does not do an cascading delete for elements that
     * are deleted by the NSUML method remove. This method should not be called
     * directly.<p>
     *
     * In the case of an associationend these are the following elements:<ul>
     * <li>Binary Associations that 'loose' one of the associationends by this
     * deletion.
     * </ul>
     *
     * @param elem
     * @see UmlFactoryImpl#delete(Object)
     */
    void deleteAssociationEnd(Object elem) {
        if (!(elem instanceof MAssociationEnd)) {
            throw new IllegalArgumentException();
        }
	MAssociation assoc = ((MAssociationEnd) elem).getAssociation();
	if (assoc != null
	    && assoc.getConnections() != null
	    && assoc.getConnections().size() == 2) { // binary association
	    nsmodel.getUmlFactory().delete(assoc);
	}
    }

    /**
     * @param elem the attribute to be deleted
     */
    void deleteAttribute(Object elem) {
        if (!(elem instanceof MAttribute)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    void deleteBehavioralFeature(Object elem) {
        if (!(elem instanceof MBehavioralFeature)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    void deleteBinding(Object elem) {
        if (!(elem instanceof MBinding)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    void deleteClass(Object elem) {
        if (!(elem instanceof MClass)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * Does a 'cascading delete' to all modelelements that are associated
     * with this element that would be in an illegal state after deletion
     * of the element. Does not do an cascading delete for elements that
     * are deleted by the NSUML method remove. This method should not be called
     * directly.<p>
     *
     * In the case of a classifier these are the following elements:<ul>
     * <li>AssociationEnds that have this classifier as type
     * </ul>
     *
     * @param elem
     * @see UmlFactoryImpl#delete(Object)
     */
    void deleteClassifier(Object elem) {
        if (elem != null && elem instanceof MClassifier) {
            Collection col = ((MClassifier) elem).getAssociationEnds();
            Iterator it = col.iterator();
            while (it.hasNext()) {
                nsmodel.getUmlFactory().delete(it.next());
            }
        }
    }

    /**
     * @param elem the element to be deleted
     */
    void deleteComment(Object elem) {
        if (!(elem instanceof MComment)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @param elem the element to be deleted
     */
    void deleteComponent(Object elem) {
        if (!(elem instanceof MComponent)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    void deleteConstraint(Object elem) {
        if (!(elem instanceof MConstraint)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    void deleteDataType(Object elem) {
        if (!(elem instanceof MDataType)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    void deleteDependency(Object elem) {
        if (!(elem instanceof MDependency)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    void deleteElement(Object elem) {
        if (!(elem instanceof MElement)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    void deleteElementResidence(Object elem) {
        if (!(elem instanceof MElementResidence)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    void deleteFeature(Object elem) {
        if (!(elem instanceof MFeature)) {
            throw new IllegalArgumentException();
        }
        LOG.warn("deleteFeature called but it is not implemented");
    }

    /**
     * @param elem the element to be deleted
     */
    void deleteFlow(Object elem) {
        if (!(elem instanceof MFlow)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    void deleteGeneralizableElement(Object elem) {
        if (!(elem instanceof MGeneralizableElement)) {
            throw new IllegalArgumentException();
        }

	MGeneralizableElement generalizableElement =
	    (MGeneralizableElement) elem;
	Iterator it = generalizableElement.getGeneralizations().iterator();
	while (it.hasNext()) {
	    nsmodel.getUmlFactory().delete(it.next());
	}
	it = generalizableElement.getSpecializations().iterator();
	while (it.hasNext()) {
	    nsmodel.getUmlFactory().delete(it.next());
	}
    }

    /**
     * @param elem the element to be deleted
     */
    void deleteGeneralization(Object elem) {
        if (!(elem instanceof MGeneralization)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    void deleteInterface(Object elem) {
        if (!(elem instanceof MInterface)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    void deleteMethod(Object elem) {
        if (!(elem instanceof MMethod)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * Does a 'cascading delete' to all modelelements that are associated
     * with this element that would be in an illegal state after deletion
     * of the element. Does not do an cascading delete for elements that
     * are deleted by the NSUML method remove. This method should not be called
     * directly.<p>
     *
     * In the case of a modelelement these are the following elements:<ul>
     * <li>Dependencies that have the modelelement as supplier or as a client
     * and are binary. (that is, they only have one supplier and one client)
     * </ul>
     *
     * @param elem
     * @see UmlFactoryImpl#delete(Object)
     */
    void deleteModelElement(Object elem) {
        if (!(elem instanceof MModelElement)) {
            throw new IllegalArgumentException();
        }

        // Delete dependencies where this is the only client
        Collection deps = org.argouml.model.Model.getFacade()
                .getClientDependencies(elem);
        Iterator it = deps.iterator();
        while (it.hasNext()) {
            MDependency dep = (MDependency) it.next();
            if (dep.getClients().size() < 2
                    && dep.getClients().contains(elem)) {
                nsmodel.getUmlFactory().delete(dep);
            }
        }

        // Delete dependencies where this is the only supplier
        deps = org.argouml.model.Model.getFacade()
                .getSupplierDependencies(elem);
        it = deps.iterator();
        while (it.hasNext()) {
            MDependency dep = (MDependency) it.next();
            if (dep.getSuppliers().size() < 2
                    && dep.getSuppliers().contains(elem)) {
                nsmodel.getUmlFactory().delete(dep);
            }
        }

        List ownedBehaviors = new ArrayList();
        ownedBehaviors.addAll(((MModelElement) elem).getBehaviors());
        it = ownedBehaviors.iterator();
        while (it.hasNext()) {
            nsmodel.getUmlFactory().delete(it.next());
        }
    }

    /**
     * A namespace deletes its owned elements.
     *
     * @param elem is the namespace.
     */
    void deleteNamespace(Object elem) {
        if (!(elem instanceof MNamespace)) {
            throw new IllegalArgumentException();
        }

	List ownedElements = new ArrayList();
	ownedElements.addAll(((MNamespace) elem).getOwnedElements());
	Iterator it = ownedElements.iterator();
	while (it.hasNext()) {
	    nsmodel.getUmlFactory().delete(it.next());
	}
    }

    /**
     * @param elem the element to be deleted
     */
    void deleteNode(Object elem) {
        if (!(elem instanceof MNode)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    void deleteOperation(Object elem) {
        if (!(elem instanceof MOperation)) {
            throw new IllegalArgumentException();
        }
        LOG.warn("deleteOperation called but it is not implemented");
    }

    /**
     * @param elem the element to be deleted
     */
    void deleteParameter(Object elem) {
        if (!(elem instanceof MParameter)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    void deletePermission(Object elem) {
        if (!(elem instanceof MPermission)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    void deletePresentationElement(Object elem) {
        if (!(elem instanceof MPresentationElement)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    void deleteRelationship(Object elem) {
        if (!(elem instanceof MRelationship)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    void deleteStructuralFeature(Object elem) {
        if (!(elem instanceof MStructuralFeature)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    void deleteTemplateParameter(Object elem) {
        if (!(elem instanceof MTemplateParameter)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    void deleteUsage(Object elem) {
        if (!(elem instanceof MUsage)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * Copies a class, and it's features. This may also require other
     * classifiers to be copied.
     *
     * @param source is the class to copy.
     * @param ns is the namespace to put the copy in.
     * @return a newly created class.
     */
    public Object copyClass(Object source, Object ns) {
        if (!(source instanceof MClass)) {
            throw new IllegalArgumentException();
        }

        if (!(ns instanceof MNamespace)) {
            throw new IllegalArgumentException();
        }

        MClass c = (MClass) createClass();
	((MNamespace) ns).addOwnedElement(c);
	doCopyClass(source, c);
	return c;
    }

    /**
     * Copies a datatype, and it's features. This may also require other
     * classifiers to be copied.
     *
     * @param source is the datatype to copy.
     * @param ns is the namespace to put the copy in.
     * @return a newly created data type.
     */
    public Object copyDataType(Object source, Object ns) {
        if (!(source instanceof MDataType)) {
            throw new IllegalArgumentException();
        }

        if (!(ns instanceof MNamespace)) {
            throw new IllegalArgumentException();
        }

	MDataType i = (MDataType) createDataType();
	((MNamespace) ns).addOwnedElement(i);
	doCopyDataType(source, i);
	return i;
    }

    /**
     * Copies an interface, and it's features. This may also require other
     * classifiers to be copied.
     *
     * @param source is the interface to copy.
     * @param ns is the namespace to put the copy in.
     * @return a newly created interface.
     */
    public Object copyInterface(Object source, Object ns) {
        if (!(source instanceof MInterface)) {
            throw new IllegalArgumentException();
        }

        if (!(ns instanceof MNamespace)) {
            throw new IllegalArgumentException();
        }

	MInterface i = (MInterface) createInterface();
	((MNamespace) ns).addOwnedElement(i);
	doCopyInterface(source, i);
	return i;
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
     * @param source the source class
     * @param target the target class
     */
    public void doCopyClass(Object source, Object target) {
        if (!(source instanceof MClass)) {
            throw new IllegalArgumentException();
        }

        if (!(target instanceof MClass)) {
            throw new IllegalArgumentException();
        }

	doCopyClassifier(source, target);

	((MClass) target).setActive(((MClass) source).isActive());
    }

    /**
     * Used by the copy functions. Do not call this function directly.
     * TODO: actions? instances? collaborations etc?
     *
     * @param source the source classifier
     * @param target the target classifier
     */
    public void doCopyClassifier(Object source, Object target) {
        if (!(source instanceof MClassifier)) {
            throw new IllegalArgumentException();
        }

        if (!(target instanceof MClassifier)) {
            throw new IllegalArgumentException();
        }

	// TODO: how to merge multiple inheritance? Necessary?
	doCopyNamespace(source, target);
	doCopyGeneralizableElement(source, target);

	// TODO: Features
    }

    /**
     * Used by the copy functions. Do not call this function directly.
     *
     * @param source the source datatype
     * @param target the target datatype
     */
    public void doCopyDataType(Object source, Object target) {
        if (!(source instanceof MDataType)) {
            throw new IllegalArgumentException();
        }

        if (!(target instanceof MDataType)) {
            throw new IllegalArgumentException();
        }

	doCopyClassifier(source, target);
    }

    /**
     * Used by the copy functions. Do not call this function directly.
     * TODO: generalizations, specializations?
     *
     * @param source the source generalizable element
     * @param target the target generalizable element
     */
    public void doCopyGeneralizableElement(Object source,
					   Object target) {
        if (!(source instanceof MGeneralizableElement)) {
            throw new IllegalArgumentException();
        }

        if (!(target instanceof MGeneralizableElement)) {
            throw new IllegalArgumentException();
        }

	doCopyModelElement(source, target);

	MGeneralizableElement targetGE = ((MGeneralizableElement) target);
	MGeneralizableElement sourceGE = ((MGeneralizableElement) source);
	targetGE.setAbstract(sourceGE.isAbstract());
	targetGE.setLeaf(sourceGE.isLeaf());
	targetGE.setRoot(sourceGE.isRoot());
    }

    /**
     * Used by the copy functions. Do not call this function directly.
     *
     * @param source the source interface
     * @param target the target interface
     */
    public void doCopyInterface(Object source, Object target) {
        if (!(source instanceof MInterface)) {
            throw new IllegalArgumentException();
        }

        if (!(target instanceof MInterface)) {
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
     * @param source the source me
     * @param target the target me
     */
    public void doCopyModelElement(Object source, Object target) {
        if (!(source instanceof MModelElement)) {
            throw new IllegalArgumentException();
        }

        if (!(target instanceof MModelElement)) {
            throw new IllegalArgumentException();
        }

	// Set the name so that superclasses can find the newly
	// created element in the model, if necessary.
	MModelElement targetME = ((MModelElement) target);
	MModelElement sourceME = ((MModelElement) source);
	targetME.setName(sourceME.getName());
	doCopyElement(source, target);

	targetME.setSpecification(sourceME.isSpecification());
	targetME.setVisibility(sourceME.getVisibility());
	nsmodel.getDataTypesHelper().copyTaggedValues(source, target);

	if (sourceME.getStereotype() != null) {
	    // Note that if we're copying this element then we
	    // must also be allowed to copy other necessary
	    // objects.
	    MStereotype st =
	        (MStereotype) nsmodel.getModelManagementHelper()
		    .getCorrespondingElement(
		            sourceME.getStereotype(),
		            targetME.getModel(),
		            true);
	    targetME.setStereotype(st);
	}
    }

    /**
     * Used by the copy functions. Do not call this function directly.
     *
     * @param source the source namespace
     * @param target the target namespace
     */
    public void doCopyNamespace(Object source, Object target) {
        if (!(source instanceof MNamespace)) {
            throw new IllegalArgumentException();
        }

        if (!(target instanceof MNamespace)) {
            throw new IllegalArgumentException();
        }

	doCopyModelElement(source, target);
	// Nothing more to do, don't copy owned elements.
    }
}
