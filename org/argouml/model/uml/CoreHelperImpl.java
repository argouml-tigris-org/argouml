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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.model.CoreHelper;
import org.argouml.model.UmlException;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.behavior.activity_graphs.MActivityGraph;
import ru.novosoft.uml.behavior.activity_graphs.MClassifierInState;
import ru.novosoft.uml.behavior.activity_graphs.MObjectFlowState;
import ru.novosoft.uml.behavior.activity_graphs.MPartition;
import ru.novosoft.uml.behavior.collaborations.MAssociationEndRole;
import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.behavior.collaborations.MCollaboration;
import ru.novosoft.uml.behavior.collaborations.MInteraction;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MAction;
import ru.novosoft.uml.behavior.common_behavior.MAttributeLink;
import ru.novosoft.uml.behavior.common_behavior.MComponentInstance;
import ru.novosoft.uml.behavior.common_behavior.MInstance;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.common_behavior.MLinkEnd;
import ru.novosoft.uml.behavior.common_behavior.MNodeInstance;
import ru.novosoft.uml.behavior.common_behavior.MReception;
import ru.novosoft.uml.behavior.common_behavior.MSignal;
import ru.novosoft.uml.behavior.state_machines.MCompositeState;
import ru.novosoft.uml.behavior.state_machines.MEvent;
import ru.novosoft.uml.behavior.state_machines.MGuard;
import ru.novosoft.uml.behavior.state_machines.MPseudostate;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.behavior.use_cases.MActor;
import ru.novosoft.uml.behavior.use_cases.MExtend;
import ru.novosoft.uml.behavior.use_cases.MInclude;
import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.foundation.core.MAbstraction;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MBehavioralFeature;
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
import ru.novosoft.uml.foundation.core.MRelationship;
import ru.novosoft.uml.foundation.core.MStructuralFeature;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;
import ru.novosoft.uml.foundation.data_types.MBooleanExpression;
import ru.novosoft.uml.foundation.data_types.MCallConcurrencyKind;
import ru.novosoft.uml.foundation.data_types.MChangeableKind;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MExpressionEditor;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MOrderingKind;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;
import ru.novosoft.uml.foundation.data_types.MProcedureExpression;
import ru.novosoft.uml.foundation.data_types.MPseudostateKind;
import ru.novosoft.uml.foundation.data_types.MScopeKind;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.model_management.MElementImport;
import ru.novosoft.uml.model_management.MPackage;

/**
 * Helper class for UML Foundation::Core Package.<p>
 *
 * Current implementation is a placeholder.<p>
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 * @author Jaap Branderhorst
 */
class CoreHelperImpl implements CoreHelper {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(CoreHelperImpl.class);

    /**
     * The model implementation.
     */
    private NSUMLModelImplementation nsmodel;

    /**
     * Don't allow instantiation.
     *
     * @param implementation To get other helpers and factories.
     */
    CoreHelperImpl(NSUMLModelImplementation implementation) {
        nsmodel = implementation;
    }

    /**
     * Determine if a meta type is a subtype of another.
     * @param type The parent metatype.
     * @param subType The metatype to test for being a subtype.
     * @return true is subType is a sub-type of type.
     */
    public boolean isSubType(Object type, Object subType) {
        if (!(type instanceof Class) || !(subType instanceof Class)) {
            throw new IllegalArgumentException("Metatypes are expected");
        }
           return ((Class) type).isAssignableFrom((Class) subType);
    }

    /**
     * This method returns all Classifiers of which this class is a
     * direct or indirect subtype.
     *
     * @param cls1  the class you want to have the parents for
     * @return a collection of the parents, each of which is a
     *         {@link MGeneralizableElement MGeneralizableElement}
     */
    public Collection getAllSupertypes(Object cls1) {

        if (!(cls1 instanceof MClassifier)) {
            throw new IllegalArgumentException();
        }

        MClassifier cls = (MClassifier) cls1;

        Collection result = new HashSet();
        Collection add = getSupertypes(cls);
        do {
            Collection newAdd = new HashSet();
            Iterator addIter = add.iterator();
            while (addIter.hasNext()) {
                MGeneralizableElement next =
                    (MGeneralizableElement) addIter.next();
                if (next instanceof MClassifier) {
                    newAdd.addAll(getSupertypes(next));
                }
            }
            result.addAll(add);
            add = newAdd;
            add.removeAll(result);
        }
        while (!add.isEmpty());
        return result;
    }

    /**
     * This method returns all Classifiers of which this class is a
     * direct subtype.<p>
     *
     * @param ogeneralizableelement the class you want to have the parents for
     * @return a collection of the parents, each of which is a
     *         {@link MGeneralizableElement MGeneralizableElement}
     */
    public Collection getSupertypes(Object ogeneralizableelement) {
        Collection result = new HashSet();
        if (ogeneralizableelement instanceof MGeneralizableElement) {
            MGeneralizableElement cls =
                (MGeneralizableElement) ogeneralizableelement;
            Collection gens = cls.getGeneralizations();
            Iterator genIterator = gens.iterator();
            while (genIterator.hasNext()) {
                MGeneralization next = (MGeneralization) genIterator.next();
                result.add(next.getParent());
            }
        }
        return result;
    }

    /**
     * This method returns all opposite AssociationEnds of a given
     * Classifier.
     *
     * @param classifier the classifier you want to have the opposite
     * association ends for
     * @return a collection of the opposite associationends
     */
    public Collection getAssociateEnds(Object classifier) {
        if (!(classifier instanceof MClassifier)) {
            throw new IllegalArgumentException();
        }

        Collection result = new ArrayList();
        Iterator ascends =
            ((MClassifier) classifier).getAssociationEnds().iterator();
        while (ascends.hasNext()) {
            MAssociationEnd ascend = (MAssociationEnd) ascends.next();
            if ((ascend.getOppositeEnd() != null)) {
                result.add(ascend.getOppositeEnd());
            }
        }
        return result;
    }

    /**
     * This method returns all opposite AssociationEnds of a given
     * Classifier, including inherited.
     *
     * @param classifier1 the classifier you want to have the opposite
     * association ends for
     * @return a collection of the opposite associationends
     */
    public Collection getAssociateEndsInh(Object classifier1) {

        if (!(classifier1 instanceof MClassifier)) {
            throw new IllegalArgumentException();
        }

        MClassifier classifier = (MClassifier) classifier1;

        Collection result = new ArrayList();
        result.addAll(getAssociateEnds(classifier));
        Iterator parents = classifier.getParents().iterator();
        while (parents.hasNext()) {
            result.addAll(getAssociateEndsInh(parents.next()));
        }
        return result;
    }

    /**
     * This method removes a feature from a classifier.
     *
     * @param cls the classifier
     * @param feature the feature to be removed
     */
    public void removeFeature(Object cls, Object feature) {
        if (cls != null
            && feature != null
            && cls instanceof MClassifier
            && feature instanceof MFeature) {
            ((MClassifier) cls).removeFeature((MFeature) feature);
        }
    }

    /**
     * This method returns the name of a feature.
     *
     * @param o is the feature
     * @return name
     */
    public String getFeatureName(Object o) {
        if (o != null && o instanceof MFeature) {
            return ((MFeature) o).getName();
        }
        return null;
    }

    /**
     * This method returns if the object is a method.
     *
     * @param o object
     * @return true if it's a method, false if not
     */
    public boolean isMethod(Object o) {
        return (o instanceof MMethod);
    }

    /**
     * This method returns if the object is an operation.
     *
     * @param o object
     * @return true if it's an operation, false if not
     */
    public boolean isOperation(Object o) {
        return (o instanceof MOperation);
    }

    /**
     * This method returns all operations of a given Classifier.
     *
     * @param classifier the classifier you want to have the operations for
     * @return a collection of the operations
     */
    public Collection getOperations(Object classifier) {
        Collection result = new ArrayList();
        if (classifier instanceof MClassifier) {
            MClassifier mclassifier = (MClassifier) classifier;
            Iterator features = mclassifier.getFeatures().iterator();
            while (features.hasNext()) {
                MFeature feature = (MFeature) features.next();
                if (feature instanceof MOperation) {
                    result.add(feature);
                }
            }
        }
        return result;
    }

    /**
     * This method replaces all operations of the given classifier
     * by the given collection of operations.
     *
     * @param classifier the given classifier
     * @param operations the new operations
     */
    public void setOperations(Object classifier, Collection operations) {
        if (classifier instanceof MClassifier) {
            MClassifier mclassifier = (MClassifier) classifier;
            List result = new ArrayList(mclassifier.getFeatures());
            Iterator features = mclassifier.getFeatures().iterator();
            while (features.hasNext()) {
                MFeature feature = (MFeature) features.next();
                if (feature instanceof MOperation) {
                    result.remove(feature);
                }
            }
            result.addAll(operations);
            mclassifier.setFeatures(result);
        }
    }

    /**
     * This method returns all attributes of a given Classifier.
     *
     * @param classifier the classifier you want to have the attributes for
     * @return a collection of the attributes
     */
    public Collection getAttributes(Object classifier) {
        if (!(classifier instanceof MClassifier)) {
            throw new IllegalArgumentException();
        }

	Collection result = new ArrayList();
	Iterator features = ((MClassifier) classifier).getFeatures().iterator();
	while (features.hasNext()) {
	    MFeature feature = (MFeature) features.next();
	    if (feature instanceof MAttribute) {
	        result.add(feature);
	    }
	}
	return result;
    }

    /**
     * This method replaces all attributes of the given classifier
     * by the given collection of attributes.
     * @param classifier the classifier
     * @param attributes the new attributes
     */
    public void setAttributes(Object classifier, Collection attributes) {
        if (classifier instanceof MClassifier) {
            MClassifier mclassifier = (MClassifier) classifier;
            List result = new ArrayList(mclassifier.getFeatures());
            Iterator features = mclassifier.getFeatures().iterator();
            while (features.hasNext()) {
                MFeature feature = (MFeature) features.next();
                if (feature instanceof MAttribute) {
                    result.remove(feature);
                }
            }
            result.addAll(attributes);
            mclassifier.setFeatures(result);
        }
    }


    /**
     * This method returns all attributes of a given Classifier,
     * including inherited.
     *
     * @param classifier the classifier you want to have the attributes for
     * @return a collection of the attributes
     */
    public Collection getAttributesInh(Object classifier) {

        if (!(classifier instanceof MClassifier)) {
            throw new IllegalArgumentException();
        }

        Collection result = new ArrayList();
        result.addAll(nsmodel.getFacade().getStructuralFeatures(classifier));
        Iterator parents = ((MClassifier) classifier).getParents().iterator();
        while (parents.hasNext()) {
            MClassifier parent = (MClassifier) parents.next();
            LOG.debug("Adding attributes for: " + parent);
            result.addAll(getAttributesInh(parent));
        }
        return result;
    }

    /**
     * This method returns all operations of a given Classifier,
     * including inherited.
     *
     * @param classifier the classifier you want to have the operations for
     * @return a collection of the operations
     */
    public Collection getOperationsInh(Object classifier) {
        if (!(classifier instanceof MClassifier)) {
            throw new IllegalArgumentException();
        }

        Collection result = new ArrayList();
        result.addAll(nsmodel.getFacade().getOperations(classifier));
        Iterator parents = ((MClassifier) classifier).getParents().iterator();
        while (parents.hasNext()) {
            result.addAll(getOperationsInh(parents.next()));
        }
        return result;
    }

    /**
     * This method finds all paramters of the given operation which
     * have the MParamterDirectionType RETURN. If it is only one, it
     * is returned.  In case there are no return parameters, null is
     * returned. If there is more than one return paramter, first of
     * them is returned, but a message is logged.<p>
     *
     * @param operation1 the operation you want to find the return
     * parameter for.
     * @return If this operation has only one paramter with Kind: RETURN,
     *         this is it, otherwise null
     */
    public Object getReturnParameter(Object operation1) {

        if (!(operation1 instanceof MOperation)) {
            throw new IllegalArgumentException();
        }

        MOperation operation = (MOperation) operation1;

        Vector returnParams = new Vector();
        Iterator params = operation.getParameters().iterator();
        while (params.hasNext()) {
            MParameter parameter = (MParameter) params.next();
            if ((parameter.getKind()).equals(MParameterDirectionKind.RETURN)) {
                returnParams.add(parameter);
            }
        }
        switch (returnParams.size()) {
	case 1 :
	    return (MParameter) returnParams.elementAt(0);
	case 0 :
	    //Next line gives too many strings while debugging
	    // obscuring other errors.
	    //cat.debug("No ReturnParameter found!");
	    return null;
	default :
	    LOG.debug(
		      "More than one ReturnParameter found, returning first!");
	    return (MParameter) returnParams.elementAt(0);
        }
    }
    /**
     * Returns all return parameters for an operation.
     *
     * @param operation is the operation.
     * @return Collection
     */
    public Collection getReturnParameters(Object operation) {
        Vector returnParams = new Vector();
        Iterator params = ((MOperation) operation).getParameters().iterator();
        while (params.hasNext()) {
            MParameter parameter = (MParameter) params.next();
            if ((parameter.getKind()).equals(MParameterDirectionKind.RETURN)) {
                returnParams.add(parameter);
            }
        }
        return returnParams;
    }

    /**
     * Returns the operation that some method realized. Returns null if
     * object isn't a method or, possibly, if the method isn't properly
     * defined.
     *
     * @param object  the method you want the realized operation of.
     * @return an operation, or null.
     */
    public Object getSpecification(Object object) {
	if (!(object instanceof MMethod)) {
	    return null;
	}
	return ((MMethod) object).getSpecification();
    }

    /**
     * Returns all Interfaces of which this class is a realization.<p>
     *
     * @param classifier  the class you want to have the interfaces for
     * @return a collection of the Interfaces
     */
    public Collection getSpecifications(Object classifier) {
        Collection result = new Vector();
        Collection deps = ((MClassifier) classifier).getClientDependencies();
        Iterator depIterator = deps.iterator();
        while (depIterator.hasNext()) {
            Object dep = depIterator.next();
            Object stereo = null;
            if (nsmodel.getFacade().getStereotypes(dep).size() > 0) {
                stereo =
                    nsmodel.getFacade().getStereotypes(dep).iterator().next();
            }
            if ((dep instanceof MAbstraction)
                && stereo != null
                && nsmodel.getFacade().getName(stereo) != null
                && nsmodel.getFacade().getName(stereo).equals("realize")) {
		Object i = nsmodel.getFacade().getSuppliers(dep).toArray()[0];
                result.add(i);
            }
        }
        return result;
    }
    /**
     * This method returns all Classifiers of which this class is a
     * direct supertype.
     *
     * @param cls  the class you want to have the children for
     * @return a collection of the children, each of which is a
     *         {@link MGeneralizableElement MGeneralizableElement}
     */
    public Collection getSubtypes(Object cls) {
        if (!(cls instanceof MClassifier)) {
            throw new IllegalArgumentException();
        }

        Collection result = new Vector();
        Collection gens = ((MClassifier) cls).getSpecializations();
        Iterator genIterator = gens.iterator();
        while (genIterator.hasNext()) {
            MGeneralization next = (MGeneralization) genIterator.next();
            result.add(next.getChild());
        }
        return result;
    }

    /**
     * Returns all behavioralfeatures found in this element and its
     * children.<p>
     *
     * @param element is the element
     * @return Collection
     */
    public Collection getAllBehavioralFeatures(Object element) {
        if (!(element instanceof MModelElement)) {
            throw new IllegalArgumentException();
        }

        Iterator it =
            ((MModelElement) element).getModelElementContents().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof MClassifier) {
                MClassifier clazz = (MClassifier) o;
                if (!(clazz instanceof MDataType)) {
                    Iterator it1 = clazz.getFeatures().iterator();
                    while (it1.hasNext()) {
                        Object o1 = it1.next();
                        if (o1 instanceof MBehavioralFeature) {
                            list.add(o1);
                        }
                    }
                }
            } else {
                list.addAll(getAllBehavioralFeatures(it.next()));
            }
        }
        return list;
    }

    /**
     * Returns all behavioral features of some classifier.
     * @param clazz The classifier
     * @return the collection with all behavioral features of some classifier
     */
    public Collection getBehavioralFeatures(Object clazz) {
        if (clazz instanceof MClassifier) {
            List ret = new ArrayList();
            Iterator it = nsmodel.getFacade().getFeatures(clazz).iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof MBehavioralFeature) {
                    ret.add(o);
                }
            }
            return ret;
        } else {
            throw new IllegalArgumentException("Argument is not a classifier");
        }
    }

    /**
     * Returns all interfaces found in this namespace and in its children.
     *
     * @param ns the given namespace
     * @return Collection with all interfaces found
     */
    public Collection getAllInterfaces(Object ns) {
        if (ns == null) {
            return new ArrayList();
        }
        if (!(ns instanceof MNamespace)) {
            throw new IllegalArgumentException();
        }

        Iterator it = ((MNamespace) ns).getOwnedElements().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof MNamespace) {
                list.addAll(getAllInterfaces(o));
            }
            if (o instanceof MInterface) {
                list.add(o);
            }
        }
        return list;
    }

    /**
     * Returns all classes found in this namespace and in its children.<p>
     *
     * @param ns is the namespace.
     * @return Collection
     */
    public Collection getAllClasses(Object ns) {
        if (ns == null) {
            return new ArrayList();
        }
        if (!(ns instanceof MNamespace)) {
            throw new IllegalArgumentException();
        }

        Iterator it = ((MNamespace) ns).getOwnedElements().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof MNamespace) {
                list.addAll(getAllClasses(o));
            }
            if (o instanceof MClass) {
                list.add(o);
            }
        }
        return list;
    }

    /**
     * Return all interfaces the given class realizes.<p>
     *
     * @param cls the classifier
     * @return Collection
     */
    public Collection getRealizedInterfaces(Object cls) {
        MClassifier classifier = (MClassifier) cls;
        if (classifier == null) {
            return new ArrayList();
        }
        Iterator it = classifier.getClientDependencies().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object clientDependency = it.next();
            if (clientDependency instanceof MAbstraction) {
                Object stereo = null;
                if (nsmodel.getFacade().getStereotypes(clientDependency).size()
                        > 0) {
                    stereo =
			nsmodel.getFacade().getStereotypes(clientDependency)
				.iterator().next();
                }
                if (stereo != null
                        && nsmodel.getFacade().getBaseClass(stereo) != null
                        && nsmodel.getFacade().getName(stereo) != null
                        && nsmodel.getFacade().getBaseClass(stereo)
		                .equals("Abstraction")
                        && nsmodel.getFacade().getName(stereo)
                        	.equals("realize")) {
                    Iterator it2 =
                        nsmodel.getFacade().getSuppliers(clientDependency)
				.iterator();
                    while (it2.hasNext()) {
                        Object supplier = it2.next();
                        if (supplier instanceof MInterface) {
                            list.add(supplier);
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * Returns all classes some generalizable element extends.
     *
     * @param clazz is the generalizable element
     * @return Collection
     */
    public Collection getExtendedClassifiers(Object clazz) {
        if (clazz == null) {
            return new ArrayList();
        }
        Iterator it = nsmodel.getFacade().getGeneralizations(clazz).iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            MGeneralization gen = (MGeneralization) it.next();
            MGeneralizableElement parent = gen.getParent();
            if (parent != null) {
                list.add(parent);
            }
        }
        return list;
    }

    /**
     * Gets the generalization between two generalizable elements.
     * Returns null if there is none.<p>
     *
     * @param achild is the child generalizable element.
     * @param aparent is the parent generalizable element.
     * @return MGeneralization
     */
    public Object getGeneralization(Object achild,
					     Object aparent) {
        MGeneralizableElement child = (MGeneralizableElement) achild;
        MGeneralizableElement parent = (MGeneralizableElement) aparent;
        if (child == null || parent == null) {
            return null;
        }
        Iterator it = child.getGeneralizations().iterator();
        while (it.hasNext()) {
            MGeneralization gen = (MGeneralization) it.next();
            if (gen.getParent() == parent) {
                return gen;
            }
        }
        return null;
    }

    /**
     * Returns all flows from some source modelelement to a target
     * modelelement.<p>
     *
     * @param source is the source model element.
     * @param target is the target model element.
     * @return Collection
     */
    public Collection getFlows(Object source, Object target) {
        if (source == null || target == null) {
            return null;
        }
        if (!(source instanceof MModelElement)) {
            throw new IllegalArgumentException("source");
        }
        if (!(target instanceof MModelElement)) {
            throw new IllegalArgumentException("target");
        }

        List ret = new ArrayList();
        Collection targetFlows = ((MModelElement) target).getTargetFlows();
        Iterator it = ((MModelElement) source).getSourceFlows().iterator();
        while (it.hasNext()) {
            MFlow flow = (MFlow) it.next();
            if (targetFlows.contains(flow)) {
                ret.add(flow);
            }
        }
        return ret;
    }

    /**
     * Returns all elements that extend some class.
     *
     * @param clazz is the class (a generalizable element)
     * @return Collection
     */
    public Collection getExtendingElements(Object clazz) {
        if (clazz == null) {
            return new ArrayList();
        }
        Iterator it = nsmodel.getFacade().getSpecializations(clazz).iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            MGeneralization gen = (MGeneralization) it.next();
            MGeneralizableElement client =
                (MGeneralizableElement) nsmodel.getFacade().getChild(gen);
            if (client != null) {
                list.add(client);
            }
        }
        return list;
    }

    /**
     * Returns all classifiers that extend some classifier.
     *
     * @param clazz is the classifier.
     * @return Collection
     */
    public Collection getExtendingClassifiers(Object clazz) {
        if (clazz == null) {
            return new ArrayList();
        }
        if (!(clazz instanceof MClassifier)) {
            throw new IllegalArgumentException();
        }

        Iterator it =
            ((MGeneralizableElement) clazz).getSpecializations().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            MGeneralization gen = (MGeneralization) it.next();
            MGeneralizableElement client = gen.getChild();
            if (client instanceof MClassifier) {
                list.add(client);
            }
        }
        return list;
    }

    /**
     * Returns all components found in this namespace and in its children.
     *
     * @param ns is the namespace.
     * @return Collection
     */
    public Collection getAllComponents(Object ns) {
        if (ns == null) {
            return new ArrayList();
        }
        if (!(ns instanceof MNamespace)) {
            throw new IllegalArgumentException();
        }

        Iterator it = ((MNamespace) ns).getOwnedElements().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof MNamespace) {
                list.addAll(getAllComponents(o));
            }
            if (o instanceof MComponent) {
                list.add(o);
            }
        }
        return list;
    }

    /**
     * Returns all components found in this namespace and in its children.
     *
     * @param ns is the namespace
     * @return Collection
     */
    public Collection getAllDataTypes(Object ns) {
        if (ns == null) {
            return new ArrayList();
        }
        if (!(ns instanceof MNamespace)) {
            throw new IllegalArgumentException();
        }

        Iterator it = ((MNamespace) ns).getOwnedElements().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof MNamespace) {
                list.addAll(getAllDataTypes(o));
            }
            if (o instanceof MDataType) {
                list.add(o);
            }
        }
        return list;
    }

    /**
     * Returns all components found in this namespace and in its children.<p>
     *
     * @param ns is the namespace
     * @return Collection
     */
    public Collection getAllNodes(Object ns) {
        if (ns == null) {
            return new ArrayList();
        }
        if (!(ns instanceof MNamespace)) {
            throw new IllegalArgumentException();
        }

        Iterator it = ((MNamespace) ns).getOwnedElements().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof MNamespace) {
                list.addAll(getAllNodes(o));
            }
            if (o instanceof MNode) {
                list.add(o);
            }
        }
        return list;
    }
    /**
     * Gets all classifiers that are associated to the given
     * classifier (have an association relationship with the
     * classifier).<p>
     *
     * @param aclassifier an MClassifier
     * @return Collection
     */
    public Collection getAssociatedClassifiers(Object aclassifier) {
        MClassifier classifier = (MClassifier) aclassifier;
        if (classifier == null) {
            return new ArrayList();
        }
        List list = new ArrayList();
        Iterator it = classifier.getAssociationEnds().iterator();
        while (it.hasNext()) {
            MAssociationEnd end = (MAssociationEnd) it.next();
            MAssociation assoc = end.getAssociation();
            Iterator it2 = assoc.getConnections().iterator();
            while (it2.hasNext()) {
                MAssociationEnd end2 = (MAssociationEnd) it2.next();
                if (end2 != end) {
                    list.add(end2.getType());
                }
            }
        }
        return list;
    }

    /**
     * Gets the associations between the classifiers from and to. Returns null
     * if from or to is null or if there is no association between them.
     *
     * @param from a classifier
     * @param to a classifier
     * @return MAssociation
     */
    public Collection getAssociations(Object/*MClassifier*/ from,
				      Object/*MClassifier*/ to) {
        Set ret = new HashSet();
        if (from == null || to == null) {
            return ret;
        }
        Iterator it = nsmodel.getFacade().getAssociationEnds(from).iterator();
        while (it.hasNext()) {
            MAssociationEnd end = (MAssociationEnd) it.next();
            MAssociation assoc = end.getAssociation();
            Iterator it2 = assoc.getConnections().iterator();
            while (it2.hasNext()) {
                MAssociationEnd end2 = (MAssociationEnd) it2.next();
                if (end2.getType() == to) {
                    ret.add(assoc);
                }
            }
        }
        return ret;
    }

    /**
     * Returns all classifiers found in this namespace and in its children.
     *
     * @param namespace the given namespace
     * @return Collection the collection of all classifiers
     *                    found in the namespace
     */
    public Collection getAllClassifiers(Object namespace) {
        if (namespace == null) {
            return new ArrayList();
        }
        MNamespace ns = (MNamespace) namespace;
        Iterator it = ns.getOwnedElements().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof MNamespace) {
                list.addAll(getAllClassifiers(o));
            }
            if (o instanceof MClassifier) {
                list.add(o);
            }
        }
        return list;
    }

    /**
     * Returns all associations for some classifier.<p>
     *
     * @param oclassifier the given classifier
     * @return Collection all associations for the given classifier
     */
    public Collection getAssociations(Object oclassifier) {
        Collection col = new ArrayList();
        if (oclassifier instanceof MClassifier) {
            MClassifier classifier = (MClassifier) oclassifier;
            Iterator it = classifier.getAssociationEnds().iterator();
            while (it.hasNext()) {
                col.add(((MAssociationEnd) it.next()).getAssociation());
            }
        }
        return col;
    }
    /**
     * Returns the associationend between a classifier and
     * an associaton.<p>
     *
     * @param type is the classifier
     * @param assoc is the association
     * @return MAssociationEnd
     */
    public Object getAssociationEnd(Object type,
            Object assoc) {
        if (type == null || assoc == null) {
            return null;
        }
        if (!(type instanceof MClassifier)) {
            throw new IllegalArgumentException();
        }
        if (!(assoc instanceof MAssociation)) {
            throw new IllegalArgumentException();
        }

        Iterator it = ((MClassifier) type).getAssociationEnds().iterator();
        while (it.hasNext()) {
            MAssociationEnd end = (MAssociationEnd) it.next();
            if (((MAssociation) assoc).getConnections().contains(end)) {
                return end;
            }
        }
        return null;
    }
    /**
     * Returns the contents (owned elements) of this classifier and
     * all its parents as specified in section 2.5.3.8 of the UML 1.3
     * spec.<p>
     *
     * @param clazz is the classifier
     * @return Collection
     */
    public Collection getAllContents(Object clazz) {
        if (clazz == null) {
            return new ArrayList();
        }
        if (!(clazz instanceof MClassifier)) {
            throw new IllegalArgumentException();
        }

        List list = new ArrayList();
        Iterator it = ((MNamespace) clazz).getOwnedElements().iterator();
        while (it.hasNext()) {
            MModelElement element = (MModelElement) it.next();
            if (element.getVisibility().equals(MVisibilityKind.PUBLIC)
                || element.getVisibility().equals(MVisibilityKind.PROTECTED)) {
                list.add(element);
            }
        }
        it = ((MGeneralizableElement) clazz).getGeneralizations().iterator();
        while (it.hasNext()) {
            list.addAll(getAllContents(it.next()));
        }
        return list;
    }

    /**
     * Returns all attributes of some classifier and of its parents.
     *
     * @param clazz is the classifier
     * @return Collection
     */
    public Collection getAllAttributes(Object clazz) {
        if (clazz == null) {
            return new ArrayList();
        }
        if (!(clazz instanceof MClassifier)) {
            throw new IllegalArgumentException();
        }

        List list = new ArrayList();
        Iterator it = ((MClassifier) clazz).getFeatures().iterator();
        while (it.hasNext()) {
            MFeature element = (MFeature) it.next();
            if (element instanceof MAttribute) {
                list.add(element);
            }
        }
        it = ((MClassifier) clazz).getGeneralizations().iterator();
        while (it.hasNext()) {
            list.addAll(getAllAttributes(it.next()));
        }
        return list;
    }

    /**
     * Returns the source of a relation. The source of a relation is
     * defined as the modelelement that propagates this relation. If
     * there are more then 1 sources, only the first is returned. If
     * there is no source, null is returned. Examples of sources
     * include classifiers that are types to associationends, usecases
     * that are bases to extend and include relations and so on. A
     * source is allways the start from the arrow in the fig, the
     * destination the end.<p>
     *
     * This method also works to get the source of a Link.<p>
     *
     * TODO: move this method to a generic ModelHelper
     *
     * @param relationship is the relation
     * @return Object
     */
    public Object getSource(Object relationship) {
        if (!(relationship instanceof MRelationship)
	    && !(relationship instanceof MLink)
	    && !(relationship instanceof MAssociationEnd)) {


            throw new IllegalArgumentException("Argument "
                    			       + relationship.toString()
                    			       + " is not "
					       + "a relationship");

	}
        if (relationship instanceof MLink) {
	    Iterator it =
	        nsmodel.getFacade().getConnections(relationship).iterator();
	    if (it.hasNext()) {
		return nsmodel.getFacade().getInstance(it.next());
	    } else {
		return null;
	    }
        }
        if (relationship instanceof MAssociation) {
            MAssociation assoc = (MAssociation) relationship;
            List conns = assoc.getConnections();
            if (conns == null || conns.isEmpty()) {
                return null;
            }
            return ((MAssociationEnd) conns.get(0)).getType();
        }
        if (relationship instanceof MGeneralization) {
            MGeneralization gen = (MGeneralization) relationship;
            return gen.getChild();
        }
        if (relationship instanceof MDependency) {
            MDependency dep = (MDependency) relationship;
            Collection col = dep.getClients();
            if (col.isEmpty()) {
                return null;
            }
            return (MModelElement) (col.toArray())[0];
        }
        if (relationship instanceof MFlow) {
            MFlow flow = (MFlow) relationship;
            Collection col = flow.getSources();
            if (col.isEmpty()) {
                return null;
            }
            return (MModelElement) (col.toArray())[0];
        }
        if (relationship instanceof MExtend) {
            MExtend extend = (MExtend) relationship;
            return extend.getExtension(); // we have to follow the arrows..
        }
        if (relationship instanceof MInclude) {
            MInclude include = (MInclude) relationship;
            // we use modelfacade here to cover up for a messup in NSUML
            return nsmodel.getFacade().getBase(include);
        }
        if (relationship instanceof MAssociationEnd) {
            return ((MAssociationEnd) relationship).getAssociation();
        }
        return null;
    }

    /**
     * Returns the destination of a relation. The destination of a
     * relation is defined as the modelelement that receives this
     * relation.  If there are more then 1 destinations, only the
     * first is returned.  If there is no destination, null is
     * returned.  Examples of sources include classifiers that are
     * types to associationends, usecases that are bases to extend and
     * include relations and so on.  In the case of an association,
     * the destination is defined as the type of the second element in
     * the connections list.<p>
     *
     * This method also works for links.<p>
     *
     * TODO: move this method to a generic ModelHelper
     *
     * @param relationship is the relation
     * @return object
     */
    public Object getDestination(Object relationship) {

	if (!(relationship instanceof MRelationship)
	    && !(relationship instanceof MLink)
	    && !(relationship instanceof MAssociationEnd)) {

	    throw new IllegalArgumentException("Argument is not "
					       + "a relationship");
	}
	if (relationship instanceof MLink) {
	    Iterator it =
	        nsmodel.getFacade().getConnections(relationship).iterator();
	    if (it.hasNext()) {
		it.next();
		if (it.hasNext()) {
		    return nsmodel.getFacade().getInstance(it.next());
		} else {
		    return null;
		}
	    } else {
	        return null;
	    }
	}


        if (relationship instanceof MAssociation) {
            MAssociation assoc = (MAssociation) relationship;
            List conns = assoc.getConnections();
            if (conns.size() <= 1) {
                return null;
            }
            return ((MAssociationEnd) conns.get(1)).getType();
        }
        if (relationship instanceof MGeneralization) {
            MGeneralization gen = (MGeneralization) relationship;
            return gen.getParent();
        }
        if (relationship instanceof MDependency) {
            MDependency dep = (MDependency) relationship;
            Collection col = dep.getSuppliers();
            if (col.isEmpty()) {
                return null;
            }
            return (MModelElement) (col.toArray())[0];
        }
        if (relationship instanceof MFlow) {
            MFlow flow = (MFlow) relationship;
            Collection col = flow.getTargets();
            if (col.isEmpty()) {
                return null;
            }
            return (MModelElement) (col.toArray())[0];
        }
        if (relationship instanceof MExtend) {
            MExtend extend = (MExtend) relationship;
            return extend.getBase();
        }
        if (relationship instanceof MInclude) {
            MInclude include = (MInclude) relationship;
            return nsmodel.getFacade().getAddition(include);
        }
        if (relationship instanceof MAssociationEnd) {
            return ((MAssociationEnd) relationship).getType();
        }
        return null;
    }

    /**
     * Returns the dependencies between some supplier modelelement and
     * some client modelelement.  Does not return the vica versa
     * relationship (dependency 'from client to supplier').<p>
     *
     * @param supplierObj a MModelElement
     * @param clientObj a MModelElement
     * @return Collection
     */
    public Collection getDependencies(
				      Object supplierObj,
				      Object clientObj) {

        if (!(supplierObj instanceof MModelElement)
	    || !(clientObj instanceof MModelElement)) {

            return null;

	}

        MModelElement supplier = (MModelElement) supplierObj;
        MModelElement client = (MModelElement) clientObj;

        List ret = new ArrayList();
        Collection clientDependencies = client.getClientDependencies();
        Iterator it = supplier.getSupplierDependencies().iterator();
        while (it.hasNext()) {
            MDependency dep = (MDependency) it.next();
            if (clientDependencies.contains(dep)) {
                ret.add(dep);
            }
        }
        return ret;
    }

    /**
     * Returns all relationships between the source and dest
     * modelelement and vica versa.<p>
     *
     * @param source is the source model element
     * @param dest is the destination model element
     * @return Collection
     */
    public Collection getRelationships(Object source,
				       Object dest) {
        Set ret = new HashSet();
        if (source == null || dest == null) {
            return ret;
        }
        if (!(source instanceof MModelElement)) {
            throw new IllegalArgumentException("source");
        }
        if (!(dest instanceof MModelElement)) {
            throw new IllegalArgumentException("dest");
        }

        ret.addAll(getFlows(source, dest));
        ret.addAll(getFlows(dest, source));
        ret.addAll(getDependencies(source, dest));
        ret.addAll(getDependencies(dest, source));
        if (source instanceof MGeneralizableElement
            && dest instanceof MGeneralizableElement) {
            ret.add(getGeneralization(source, dest));
            ret.add(getGeneralization(dest, source));
            if (source instanceof MClassifier && dest instanceof MClassifier) {
                ret.addAll(getAssociations(source, dest));
            }
        }
        return ret;
    }

    /**
     * Returns true if some modelelement may be owned by the given
     * namespace.<p>
     *
     * @param mObj a MModelElement
     * @param nsObj a MNamespace
     * @return boolean
     */
    public boolean isValidNamespace(Object mObj, Object nsObj) {

        if (!(mObj instanceof MModelElement)
	    || !(nsObj instanceof MNamespace)) {

            return false;

	}

        MModelElement modelElement = (MModelElement) mObj;
        MNamespace ns = (MNamespace) nsObj;

        if (modelElement == null || ns == null) {
            return false;
        }
        if (ns.getModel() != modelElement.getModel()) {
            return false;
        }
        if (modelElement == ns) {
            return false;
        }
        if (modelElement instanceof MNamespace
                && modelElement == getFirstSharedNamespace(modelElement, ns)) {
            return false;
        }
        if (ns instanceof MInterface
            || ns instanceof MActor
            || ns instanceof MUseCase) {
            return false;
        } else if (ns instanceof MComponent) {
            return (modelElement instanceof MComponent && modelElement != ns);
        } else if (ns instanceof MCollaboration) {
            if (!(modelElement instanceof MClassifierRole
		  || modelElement instanceof MAssociationRole
		  || modelElement instanceof MGeneralization
		  || modelElement instanceof MConstraint)) {
                return false;
            }
        } else if (ns instanceof MPackage) {
            if (!(modelElement instanceof MPackage
		  || modelElement instanceof MClassifier
		  || modelElement instanceof MAssociation
		  || modelElement instanceof MGeneralization
		  || modelElement instanceof MDependency
		  || modelElement instanceof MConstraint
		  || modelElement instanceof MCollaboration
		  || modelElement instanceof MStateMachine
		  || modelElement instanceof MStereotype)) {
                return false;
            }
        } else if (ns instanceof MClass) {
            if (!(modelElement instanceof MClass
		  || modelElement instanceof MAssociation
		  || modelElement instanceof MGeneralization
		  || modelElement instanceof MUseCase
		  || modelElement instanceof MConstraint
		  || modelElement instanceof MDependency
		  || modelElement instanceof MCollaboration
		  || modelElement instanceof MDataType
		  || modelElement instanceof MInterface)) {
                return false;
            }
        } else if (ns instanceof MClassifierRole) {
            MClassifierRole cr = (MClassifierRole) ns;
            if (!(cr.getAvailableContentses().contains(modelElement)
		  || cr.getAvailableFeatures().contains(modelElement))) {
                return false;
            }
        }
        if (modelElement instanceof MStructuralFeature) {
            if (!isValidNamespace((MStructuralFeature) modelElement, ns)) {
                return false;
            }
        } else if (modelElement instanceof MGeneralizableElement) {
            if (!isValidNamespace((MGeneralizableElement) modelElement, ns)) {
                return false;
            }
        } else if (modelElement instanceof MGeneralization) {
            if (!isValidNamespace((MGeneralization) modelElement, ns)) {
                return false;
            }
        }
        if (modelElement instanceof MAssociation) {
            if (!isValidNamespace((MAssociation) modelElement, ns)) {
                return false;
            }
        } else if (modelElement instanceof MCollaboration) {
            if (!isValidNamespace((MCollaboration) modelElement, ns)) {
                return false;
            }
        }
        return true;
    }
    private boolean isValidNamespace(MCollaboration collab, MNamespace ns) {
        Iterator it = collab.getOwnedElements().iterator();
        while (it.hasNext()) {
            MModelElement m = (MModelElement) it.next();
            if (m instanceof MClassifierRole) {
                MClassifierRole role = (MClassifierRole) m;
                Iterator it2 = role.getBases().iterator();
                while (it2.hasNext()) {
                    if (!ns.getOwnedElements().contains(it2.next())) {
                        return false;
                    }
                }
            } else if (m instanceof MAssociationRole) {
                if (!ns.getOwnedElements()
		    .contains(((MAssociationRole) m).getBase())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValidNamespace(MGeneralization gen, MNamespace ns) {
        if (gen.getParent() == null || gen.getChild() == null) {
            return true;
        }
        MNamespace ns1 = gen.getParent().getNamespace();
        MNamespace ns2 = gen.getChild().getNamespace();
        if (ns == getFirstSharedNamespace(ns1, ns2)) {
            return true;
        }
        return false;
    }

    private boolean isValidNamespace(MStructuralFeature struc, MNamespace ns) {
        if (struc.getType() == null || struc.getOwner() == null) {
            return true;
        }
        return struc.getOwner().getNamespace()
	    .getOwnedElements().contains(struc.getType());
    }

    private boolean isValidNamespace(MAssociation assoc, MNamespace ns) {
        Iterator it = assoc.getConnections().iterator();
        List namespaces = new ArrayList();
        while (it.hasNext()) {
            MAssociationEnd end = (MAssociationEnd) it.next();
            namespaces.add(end.getType().getNamespace());
        }
        it = namespaces.iterator();
        while (it.hasNext()) {
            MNamespace ns1 = (MNamespace) it.next();
            if (it.hasNext()) {
                MNamespace ns2 = (MNamespace) it.next();
                // TODO: this contains a small error (ns can be part
                // of hierarchy of namespaces, that's not taken into
                // account)
                if (ns == getFirstSharedNamespace(ns1, ns2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isValidNamespace(
				     MGeneralizableElement gen,
				     MNamespace ns) {
        Iterator it = gen.getParents().iterator();
        while (it.hasNext()) {
            MGeneralizableElement gen2 = (MGeneralizableElement) it.next();
            if (!ns.getOwnedElements().contains(gen2)) {
                return false;
            }
        }
        return true;
    }
    /**
     * Gets the first namespace two namespaces share. That is: it
     * returns the first namespace that owns the given namespaces
     * itself or some owner of the given namespaces.<p>
     *
     * @param ns1 is the first name space
     * @param ns2 is the second name space
     * @return MNamespace
     */
    public Object getFirstSharedNamespace(Object ns1, Object ns2) {
        if (ns1 == null || ns2 == null) {
            return null;
        }
        if (ns1 == ns2) {
            return ns1;
        }
        if (!(ns1 instanceof MNamespace)) {
            throw new IllegalArgumentException();
        }
        if (!(ns2 instanceof MNamespace)) {
            throw new IllegalArgumentException();
        }

        boolean ns1Owner =
            nsmodel.getModelManagementHelper()
	        .getAllNamespaces(ns1).contains(ns2);
        boolean ns2Owner =
            nsmodel.getModelManagementHelper()
	        .getAllNamespaces(ns2).contains(ns1);
        if (ns1Owner) {
            return ns1;
        }
        if (ns2Owner) {
            return ns2;
        }
        return getFirstSharedNamespace(
                ((MModelElement) ns1).getNamespace(),
                ((MModelElement) ns2).getNamespace());
    }

    /**
     * Returns all possible namespaces that may be selected by some given
     * modelelement. Which namespaces are allowed, is decided in the method
     * isValidNamespace.<p>
     *
     * @param modelElement is the model element
     * @param model the model to search
     * @return Collection
     */
    public Collection getAllPossibleNamespaces(Object modelElement,
            Object model) {
        MModelElement m = (MModelElement) modelElement;
        List ret = new ArrayList();
        if (m == null) {
            return ret;
        }
        if (isValidNamespace(m, model)) {
            ret.add(model);
        }
        Iterator it =
            nsmodel.getModelManagementHelper()
	        .getAllModelElementsOfKind(model, MNamespace.class)
	            .iterator();
        while (it.hasNext()) {
            MNamespace ns = (MNamespace) it.next();
            if (isValidNamespace(m, ns)) {
                ret.add(ns);
            }
        }
        return ret;
    }

    /**
     * Returns the base classes (that are the classes that do not have any
     * generalizations) for some given namespace. Personally, this seems a
     * pointless operation to me but in GoModelToBaseElements this is done like
     * this for some reason.
     * TODO: find out if someone uses this.
     *
     * @param o is the given namespace.
     * @return Collection
     */
    public Collection getBaseClasses(Object o) {
        Collection col = new ArrayList();
        if (o instanceof MNamespace) {
            Iterator it =
                nsmodel.getModelManagementHelper()
		    .getAllModelElementsOfKind(o, MGeneralizableElement.class)
		        .iterator();
            while (it.hasNext()) {
                MGeneralizableElement gen = (MGeneralizableElement) it.next();
                if (gen.getGeneralizations().isEmpty()) {
                    col.add(gen);
                }
            }
        }
        return col;
    }

    /**
     * Returns all children from some given generalizableelement on
     * all levels (the complete tree excluding the generalizable
     * element itself).<p>
     *
     * @param o is the generalizable element
     * @return Collection
     * @throws IllegalStateException if there is a circular reference.
     */
    // TODO: Argument shall be typed to catch a lot of problems at
    //       compile time.
    // TODO: The exception shall be declared explicitly.
    // TODO: Use an exception that needs to be caugh so users of
    //       getChildren won't forget that they need to catch it.
    public Collection getChildren(Object o) {
        Collection col = new ArrayList();
        if (o instanceof MGeneralizableElement) {
            Iterator it =
                ((MGeneralizableElement) o).getSpecializations().iterator();
            while (it.hasNext()) {
                getChildren(col, (MGeneralization) it.next());
            }
        }
        return col;
    }

    /**
     * Adds all children recursively to the Collection in the first argument.
     *
     * @param currentChildren collection to collect them in.
     * @param gen element whose children are added.
     * @throws IllegalStateException if there is a circular reference.
     */
    private void getChildren(Collection currentChildren, MGeneralization gen) {

	MGeneralizableElement child = gen.getChild();
	if (currentChildren.contains(child)) {
	    throw new IllegalStateException("Circular inheritance occured.");
	}
	currentChildren.add(child);
	Iterator it = child.getSpecializations().iterator();
	while (it.hasNext()) {
	    getChildren(currentChildren, (MGeneralization) it.next());
	}
    }

    /**
     * Returns all interfaces that are realized by the given class or
     * by its superclasses. It's possible that interfaces occur twice
     * in the collection returned. In that case there is a double
     * reference to that interface.
     *
     * @param o is the given class
     * @return Collection
     */
    public Collection getAllRealizedInterfaces(Object o) {
        return internalGetAllRealizedInterfaces(
						o,
						new ArrayList(),
						new HashSet());
    }

    /**
     * Helper method for getAllRealizedInterfaces.
     * @param o
     * @param col
     * @param visited
     * @return Collection
     */
    private Collection internalGetAllRealizedInterfaces(
				Object o, Collection col, Set visited) {
        visited.add(o);
        if (o != null) {
            if (o instanceof MClass) {
                MClass clazz = (MClass) o;
                Collection supDependencies = clazz.getClientDependencies();
                Iterator it = supDependencies.iterator();
                while (it.hasNext()) {
                    MDependency dependency = (MDependency) it.next();
                    MStereotype stereo = dependency.getStereotype();
                    if (dependency instanceof MAbstraction
                        && stereo != null
			&& "realize".equals(stereo.getName())
			&& "Abstraction".equals(stereo.getBaseClass())) {

                        col.addAll(dependency.getSuppliers());

                    }
                }
                Collection superTypes = getSupertypes(o);
                it = superTypes.iterator();
                while (it.hasNext()) {
		    Object obj = it.next();
		    if (!visited.contains(obj)) {
			internalGetAllRealizedInterfaces(obj, col, visited);
		    }
                }
            }
        }
        return col;
    }

    /**
     * @param  association the association to be investigated
     * @return true if one of the association ends of the given association
     *         is of the composite kind
     */
    public final boolean hasCompositeEnd(Object association) {
        if (!(association instanceof MAssociation)) {
            throw new IllegalArgumentException();
	}

        MAssociation association1 = (MAssociation) association;

	List ends = association1.getConnections();
	for (Iterator iter = ends.iterator(); iter.hasNext();) {
	    MAssociationEnd end = (MAssociationEnd) iter.next();
	    if (end.getAggregation() == MAggregationKind.COMPOSITE) {
	        return true;
	    }
	}
	return false;
    }

    /**
     * @param associationEnd is the association end
     * @param kindType the MAggregationKind as a string in lower case letter,
     *                 eg: composite.
     * @return true if the aggregation kinds are the same.
     */
    public final boolean equalsAggregationKind(Object associationEnd,
                                               String kindType) {
        if (!(associationEnd instanceof MAssociationEnd)) {
            throw new IllegalArgumentException();
	}

        MAssociationEnd associationEnd1 = (MAssociationEnd) associationEnd;

        if (kindType.equals("composite")) {
            return MAggregationKind.COMPOSITE.equals(
                            associationEnd1.getAggregation());
        } else {
            throw new IllegalArgumentException("kindType: " + kindType
					       + " not supported");
        }
    }

    /**
     * Remove the given modelelement from a given comment.
     *
     * @param handle MComment
     * @param me MModelElement
     */
    public void removeAnnotatedElement(Object handle, Object me) {
        if (handle instanceof MComment && me instanceof MModelElement) {
            ((MComment) handle).removeAnnotatedElement((MModelElement) me);
            return;
        }
        LOG.error("Failed to remove comment from model element");
        throw new IllegalArgumentException();
    }

    /**
     * This method removes a dependency from a model element.
     *
     * @param handle is the model element
     * @param dep is the dependency
     */
    public void removeClientDependency(Object handle, Object dep) {
        if (handle instanceof MModelElement
                && dep instanceof MDependency) {
            ((MModelElement) handle).removeClientDependency((MDependency) dep);
            return;
        }
	throw new IllegalArgumentException();
    }

    /**
     * Remove the given constraint from a given ModelElement.
     *
     * @param handle ModelElement
     * @param cons Constraint
     */
    public void removeConstraint(Object handle, Object cons) {
        if (handle instanceof MModelElement && cons instanceof MConstraint) {
            ((MModelElement) handle).removeConstraint((MConstraint) cons);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or cons: " + cons);
    }

    /**
     * Removes a owned model element from a namespace.
     *
     * @param handle is the name space
     * @param value is the model element
     */
    public void removeOwnedElement(Object handle, Object value) {
        if (handle instanceof MNamespace
                && value instanceof MModelElement) {
            ((MNamespace) handle).removeOwnedElement((MModelElement) value);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or value: " + value);
    }

    /**
     * This method removes a parameter from an operation.
     *
     * @param handle The operation.
     * @param parameter The parameter.
     */
    public void removeParameter(Object handle, Object parameter) {
        if (parameter instanceof MParameter) {
            if (handle instanceof MObjectFlowState) {
                ((MObjectFlowState) handle).removeParameter(
                        (MParameter) parameter);
                return;
            }
            if (handle instanceof MEvent) {
                ((MEvent) handle).removeParameter((MParameter) parameter);
                return;
            }
            if (handle instanceof MBehavioralFeature) {
                ((MBehavioralFeature) handle).removeParameter(
                    (MParameter) parameter);
                return;
            }
            if (handle instanceof MClassifier) {
                ((MClassifier) handle).removeParameter((MParameter) parameter);
                return;
            }
        }

        throw new IllegalArgumentException("handle: " + handle
                + " or parameter: " + parameter);
    }

    /**
     * Remove a source flow from a model element.
     *
     * @param handle The model element.
     * @param flow The flow.
     */
    public void removeSourceFlow(Object handle, Object flow) {
        if (handle instanceof MModelElement
                && flow instanceof MFlow) {
            ((MModelElement) handle).removeSourceFlow((MFlow) flow);
            return;
        }

        throw new IllegalArgumentException("handle: " + handle
                + " or flow: " + flow);
    }

    /**
     * Adds a supplier dependency to some modelelement.
     *
     * @param supplier the supplier
     * @param dependency the dependency
     */
    public void removeSupplierDependency(
            Object supplier,
            Object dependency) {
        if (supplier instanceof MModelElement
                && dependency instanceof MDependency) {
            MModelElement me = (MModelElement) supplier;
            me.removeSupplierDependency((MDependency) dependency);
            return;
        }
        throw new IllegalArgumentException("supplier: " + supplier
                + " or dependency: " + dependency);
    }

    /**
     * Removes a named tagged value from a model element, ie subsequent calls
     * to getTaggedValue will return null for name, at least until a tagged
     * value with that name has been added again.
     *
     * @param handle the model element to remove the tagged value from
     * @param name the name of the tagged value
     * @throws IllegalArgumentException if handle isn't a model element
     */
    public void removeTaggedValue(Object handle, String name) {
        if (handle instanceof MModelElement) {
            MModelElement me = (MModelElement) handle;
            me.removeTaggedValue(name);
            return;
        }

        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Add a target flow to a model element.
     *
     * @param handle The model element.
     * @param flow The flow to add.
     */
    public void removeTargetFlow(Object handle, Object flow) {
        if (handle instanceof MModelElement
                && flow instanceof MFlow) {
            ((MModelElement) handle).removeTargetFlow((MFlow) flow);
            return;
        }

        throw new IllegalArgumentException("handle: " + handle
                + " or flow: " + flow);
    }

    /**
     * Adds an annotated element to a comment.
     *
     * @param comment The comment to which the element is annotated
     * @param annotatedElement The element to annotate
     */
    public void addAnnotatedElement(Object comment,
            Object annotatedElement) {
        if (comment instanceof MComment
                && annotatedElement instanceof MModelElement) {
            ((MComment) comment)
                .addAnnotatedElement(((MModelElement) annotatedElement));
            return;
        }
        LOG.error("Failed to link a comment to a model element");
        throw new IllegalArgumentException("comment: " + comment
                + " or annotatedElement: " + annotatedElement);
    }

    /**
     * Adds a client model element to some dependency.
     *
     * @param handle dependency.
     * @param element The model element.
     * @throws IllegalArgumentException if the handle is not a dependency
     * or the element is not a model element.
     */
    public void addClient(Object handle, Object element) {
        if (handle instanceof MDependency
                && element instanceof MModelElement) {
            ((MDependency) handle).addClient((MModelElement) element);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or element: " + element);
    }

    /**
     * Adds a client dependency to some modelelement.
     *
     * @param handle the modelelement
     * @param dependency the dependency
     */
    public void addClientDependency(Object handle, Object dependency) {
        if (handle instanceof MModelElement
                && dependency instanceof MDependency) {
            MModelElement me = (MModelElement) handle;
            me.addClientDependency((MDependency) dependency);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or dependency: " + dependency);
    }

    /**
     * Add a new comment to a model element.
     *
     * @param element the element to which the comment is to be added
     * @param comment the comment for the model element
     */
    public void addComment(Object element, Object comment) {
        if (element instanceof MModelElement && comment instanceof MComment) {
            ((MModelElement) element).addComment((MComment) comment);
            return;
        }
        throw new IllegalArgumentException("element: " + element);
    }

    /**
     * Add an End to a connection.
     *
     * @param handle Association or Link
     * @param connection AssociationEnd or LinkEnd
     */
    public void addConnection(Object handle, Object connection) {
        if (handle instanceof MAssociation
            && connection instanceof MAssociationEnd) {
            ((MAssociation) handle).addConnection((MAssociationEnd) connection);
            return;
        }
        if (handle instanceof MLink
            && connection instanceof MLinkEnd) {
            ((MLink) handle).addConnection((MLinkEnd) connection);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or connection: " + connection);
    }

    /**
     * Adds a constraint to some model element.
     *
     * @param handle model element
     * @param mc constraint
     */
    public void addConstraint(Object handle, Object mc) {
        if (handle instanceof MModelElement && mc instanceof MConstraint) {
            ((MModelElement) handle).addConstraint((MConstraint) mc);
            return;
        }
    throw new IllegalArgumentException("handle: " + handle + " or mc: " + mc);
    }

    /**
     * @param handle Component
     * @param node Node
     */
    public void addDeploymentLocation(Object handle, Object node) {
        if (handle instanceof MComponent && node instanceof MNode) {
            ((MComponent) handle).addDeploymentLocation((MNode) node);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or node: " + node);
    }

    /**
     * Adds a feature to some classifier.
     *
     * @param handle classifier
     * @param index position
     * @param f feature
     */
    public void addFeature(Object handle, int index, Object f) {
        if (handle instanceof MClassifier && f instanceof MFeature) {
            ((MClassifier) handle).addFeature(index, (MFeature) f);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or f: " + f);
    }

    /**
     * Adds a feature to some classifier.
     *
     * @param handle classifier
     * @param f feature
     */
    public void addFeature(Object handle, Object f) {
        if (handle instanceof MClassifier && f instanceof MFeature) {
            ((MClassifier) handle).addFeature((MFeature) f);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Add the given Link to the given Link or Association.
     *
     * @param handle the Link or Association
     * @param link Link
     */
    public void addLink(Object handle, Object link) {
        if (handle instanceof MAssociation && link instanceof MLink) {
            ((MAssociation) handle).addLink((MLink) link);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or link: " + link);
    }

    /**
     * Adds a method to some operation and copies the op's attributes
     * to the method.
     *
     * @param handle is the operation
     * @param m is the method
     */
    public void addMethod(Object handle, Object m) {
        if (handle instanceof MOperation
            && m instanceof MMethod) {
            ((MMethod) m).setVisibility(((MOperation) handle).getVisibility());
            ((MMethod) m).setOwnerScope(((MOperation) handle).getOwnerScope());
            ((MOperation) handle).addMethod((MMethod) m);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or m: " + m);
    }

    /**
     * Adds a model element to some namespace.
     *
     * @param handle namespace
     * @param me model element
     */
    public void addOwnedElement(Object handle, Object me) {
        if (handle instanceof MNamespace && me instanceof MModelElement) {
            ((MNamespace) handle).addOwnedElement((MModelElement) me);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or me: " + me);
    }

    /**
     * Add a Parameter to the given object at given location.
     *
     * @param handle The object that will get the Parameter:
     *               MEvent, MBehavioralFeature.
     * @param index the location
     * @param parameter Object that will be added
     */
    public void addParameter(
        Object handle,
        int index,
        Object parameter) {
        if (parameter instanceof MParameter) {
            if (handle instanceof MEvent) {
                ((MEvent) handle).addParameter(index, (MParameter) parameter);
                return;
            }
            if (handle instanceof MBehavioralFeature) {
                ((MBehavioralFeature) handle).addParameter(
                        index,
                        (MParameter) parameter);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or parameter: " + parameter);
    }

    /**
     * Add a Parameter to the given object.
     *
     * @param handle The object that will get the Parameter:
     *               MObjectFlowState, MEvent, MBehavioralFeature, MClassifier.
     * @param parameter Object that will be added
     */
    public void addParameter(Object handle, Object parameter) {
        if (parameter instanceof MParameter) {
            if (handle instanceof MObjectFlowState) {
                ((MObjectFlowState) handle).addParameter(
                        (MParameter) parameter);
                return;
            }
            if (handle instanceof MEvent) {
                ((MEvent) handle).addParameter((MParameter) parameter);
                return;
            }
            if (handle instanceof MBehavioralFeature) {
                ((MBehavioralFeature) handle).addParameter(
                    (MParameter) parameter);
                return;
            }
            if (handle instanceof MClassifier) {
                ((MClassifier) handle).addParameter((MParameter) parameter);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or parameter: " + parameter);
    }

    /**
     * Add a raised Signal to a Message or Operation.
     *
     * @param handle the Message or Operation
     * @param sig the Signal that is raised
     */
    public void addRaisedSignal(Object handle, Object sig) {
        if (sig instanceof MSignal) {
            if (handle instanceof MMessage) {
                ((MBehavioralFeature) handle).addRaisedSignal((MSignal) sig);
                return;
            }
            if (handle instanceof MOperation) {
                ((MOperation) handle).addRaisedSignal((MSignal) sig);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or sig: " + sig);
    }

    /**
     * Add a source flow to a model element.
     *
     * @param handle The model element.
     * @param flow The flow.
     */
    public void addSourceFlow(Object handle, Object flow) {
        if (handle instanceof MModelElement
                && flow instanceof MFlow) {
            ((MModelElement) handle).addSourceFlow((MFlow) flow);
            return;
        }

        throw new IllegalArgumentException("handle: " + handle
                + " or flow: " + flow);
    }

    /**
     * Adds a supplier classifier to some abstraction.
     *
     * @param handle abstraction
     * @param element supplier model element
     */
    public void addSupplier(Object handle, Object element) {
        if (handle instanceof MDependency && element instanceof MModelElement) {
            ((MDependency) handle).addSupplier((MModelElement) element);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or element: " + element);
    }

    /**
     * Adds a supplier dependency to some modelelement.
     *
     * @param supplier the supplier
     * @param dependency the dependency
     */
    public void addSupplierDependency(
            Object supplier,
            Object dependency) {
        if (supplier instanceof MModelElement
                && dependency instanceof MDependency) {
            MModelElement me = (MModelElement) supplier;
            me.addSupplierDependency((MDependency) dependency);
            return;
        }
        throw new IllegalArgumentException("supplier: " + supplier
                + " or dependency: " + dependency);
    }

    /**
     * Adds a TaggedValue to a ModelElement.
     *
     * @param handle ModelElement
     * @param taggedValue TaggedValue
     */
    public void addTaggedValue(Object handle, Object taggedValue) {
        if (handle instanceof MModelElement
                && taggedValue instanceof MTaggedValue) {
            ((MModelElement) handle).addTaggedValue((MTaggedValue) taggedValue);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or taggedValue: " + taggedValue);
    }

    /**
     * Add a target flow to a model element.
     *
     * @param handle The model element.
     * @param flow The flow to add.
     */
    public void addTargetFlow(Object handle, Object flow) {
        if (handle instanceof MModelElement
                && flow instanceof MFlow) {
            ((MModelElement) handle).addTargetFlow((MFlow) flow);
            return;
        }

        throw new IllegalArgumentException("handle: " + handle
                + " or flow: " + flow);
    }

    /**
     * Sets if of some model element is abstract.
     *
     * @param handle is the classifier
     * @param flag is true if it should be abstract
     */
    public void setAbstract(Object handle, boolean flag) {
        if (handle instanceof MGeneralizableElement) {
            ((MGeneralizableElement) handle).setAbstract(flag);
            return;
        }
        if (handle instanceof MOperation) {
            ((MOperation) handle).setAbstract(flag);
            return;
        }
        if (handle instanceof MReception) {
            ((MReception) handle).setAbstarct(flag);
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Makes a Class active.
     *
     * @param handle Class
     * @param active boolean
     */
    public void setActive(Object handle, boolean active) {
        if (handle instanceof MClass) {
            ((MClass) handle).setActive(active);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Sets the aggregation of some model element.
     *
     * @param handle the model element to set aggregation
     * @param aggregationKind the aggregation kind
     */
    public void setAggregation(Object handle, Object aggregationKind) {
        if (handle instanceof MAssociationEnd
            && aggregationKind instanceof MAggregationKind) {
            ((MAssociationEnd) handle).setAggregation(
                (MAggregationKind) aggregationKind);
                return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or aggregationKind: " + aggregationKind);
    }

    /**
     * Sets the list of annotated elements of the given comment.
     *
     * @param handle the given comment
     * @param elems the list of annotated modelelements
     */
    public void setAnnotatedElements(Object handle, Collection elems) {
        if (handle instanceof MComment
            && elems instanceof List) {
            ((MComment) handle).setAnnotatedElements(elems);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Sets the association of some model element.
     *
     * @param handle the model element to set association
     * @param association is the association
     */
    public void setAssociation(Object handle, Object association) {
        if (association instanceof MAssociation) {
            if (handle instanceof MAssociationEnd) {
                ((MAssociationEnd) handle).setAssociation(
                    (MAssociation) association);
                return;
            }
            if (handle instanceof MLink) {
                ((MLink) handle).setAssociation((MAssociation) association);
                return;
            }
        } else if (association instanceof MAssociationRole) {
            if (handle instanceof MAssociationEndRole) {
                ((MAssociationEndRole) handle).setAssociation(
                        (MAssociationRole) association);
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or association: " + association);
    }

    /**
     * Sets if some model element is a leaf.
     *
     * @param handle model element
     * @param flag is true if it is a leaf.
     */
    public void setLeaf(Object handle, boolean flag) {
        if (handle instanceof MReception) {
            ((MReception) handle).setLeaf(flag);
            return;
        }
        if (handle instanceof MOperation) {
            ((MOperation) handle).setLeaf(flag);
            return;
        }
        if (handle instanceof MGeneralizableElement) {
            ((MGeneralizableElement) handle).setLeaf(flag);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Sets the raised signals of some behavioural feature.
     *
     * @param handle the behavioural feature
     * @param raisedSignals the raised signals
     */
    public void setRaisedSignals(
        Object handle,
        Collection raisedSignals) {
        if (handle instanceof MBehavioralFeature) {
            ((MBehavioralFeature) handle).setRaisedSignals(raisedSignals);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Sets a body of a given Method, Constraint or Expression.
     *
     * @param handle is the method, expression
     * @param expr is the body string for the expression
     */
    public void setBody(Object handle, Object expr) {
        if (handle instanceof MMethod
            && (expr == null || expr instanceof MProcedureExpression)) {
            ((MMethod) handle).setBody((MProcedureExpression) expr);
            return;
        }

        if (handle instanceof MConstraint
            && (expr == null || expr instanceof MBooleanExpression)) {
            ((MConstraint) handle).setBody((MBooleanExpression) expr);
            return;
        }

        /*
         * TODO: MVW: The next part is fooling the user of setBody()
         * in thinking that the body of the object is changed.
         * Instead, a new object is created and as a side-effect
         * the language is lost.
         * Maybe we should just copy the language?
         */
        if (handle instanceof MExpression) {
            MExpressionEditor expressionEditor =
                (MExpressionEditor) nsmodel.getDataTypesFactory()
                	.createExpressionEditor(handle);
            expressionEditor.setBody((String) expr);
            expressionEditor.toExpression();
            // this last step creates a new MExpression
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or expr: " + expr);
    }

    /**
     * Set the Changeability of a StructuralFeature or AssociationEnd.
     *
     * @param handle StructuralFeature or AssociationEnd
     * @param ck ChangeableKind
     */
    public void setChangeability(Object handle, Object ck) {
        if (ck == null || ck instanceof MChangeableKind) {
            MChangeableKind changeableKind = (MChangeableKind) ck;

            if (handle instanceof MStructuralFeature) {
                ((MStructuralFeature) handle).setChangeability(changeableKind);
                return;
            }
            if (handle instanceof MAssociationEnd) {
                ((MAssociationEnd) handle).setChangeability(changeableKind);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or ck: " + ck);
    }

    /**
     * Set the changeability of some feature.
     *
     * @param handle is the feature
     * @param flag is the changeability flag
     */
    public void setChangeable(Object handle, boolean flag) {
        // TODO: the implementation is ugly, because I have no spec
        // at hand...
        if (handle instanceof MStructuralFeature) {
            if (flag) {
                ((MStructuralFeature) handle).setChangeability(
                    MChangeableKind.CHANGEABLE);
                    return;
            } else {
                ((MStructuralFeature) handle).setChangeability(
                    MChangeableKind.FROZEN);
            return;
            }
        } else if (handle instanceof MAssociationEnd) {
            MAssociationEnd ae = (MAssociationEnd) handle;
            if (flag) {
                ae.setChangeability(MChangeableKind.CHANGEABLE);
            } else {
                ae.setChangeability(MChangeableKind.FROZEN);
            }
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Set the child for a generalization.
     *
     * @param handle Generalization
     * @param child GeneralizableElement
     */
    public void setChild(Object handle, Object child) {
        if (handle instanceof MGeneralization) {
            ((MGeneralization) handle).setChild((MGeneralizableElement) child);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or child: " + child);
    }

    /**
     * Set the concurrency of some operation.
     *
     * @param handle is the operation
     * @param concurrencyKind is the concurrency
     */
    public void setConcurrency(
        Object handle,
        Object concurrencyKind) {
        if (handle instanceof MOperation
            && concurrencyKind instanceof MCallConcurrencyKind) {
            ((MOperation) handle).setConcurrency(
                (MCallConcurrencyKind) concurrencyKind);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or concurrencyKind: " + concurrencyKind);
    }

    /**
     * Sets the list of connections of the given association or link.
     *
     * @param handle the given association or link
     * @param elems the list of association-ends or link-ends
     */
    public void setConnections(Object handle, Collection elems) {
        if (handle instanceof MAssociation && elems instanceof List) {
            ((MAssociation) handle).setConnections((List) elems);
            return;
        }
        if (handle instanceof MLink && elems instanceof List) {
            ((MLink) handle).setConnections(elems);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Sets a default value of some parameter.
     *
     * @param handle is the parameter
     * @param expr is the expression
     */
    public void setDefaultValue(Object handle, Object expr) {
        if (handle instanceof MParameter && expr instanceof MExpression) {
            ((MParameter) handle).setDefaultValue((MExpression) expr);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or expr: " + expr);
    }

    /**
     * @param handle a generalization
     * @param discriminator the discriminator to set
     */
    public void setDiscriminator(Object handle, String discriminator) {
        if (handle instanceof MGeneralization) {
            ((MGeneralization) handle).setDiscriminator(discriminator);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Set the feature at the given position.
     *
     * @param elem The classifier to set.
     * @param i The position. Start with 0.
     * @param impl The feature to set.
     */
    public void setFeature(Object elem, int i, Object impl) {
        if (elem instanceof MClassifier
                && impl instanceof MFeature) {
            ((MClassifier) elem).setFeature(i, (MFeature) impl);
            return;
        }

        throw new IllegalArgumentException("elem: " + elem
                + " or impl: " + impl);
    }

    /**
     * Sets the features of some model element.
     *
     * @param handle the model element to set features to
     * @param features the list of features
     */
    public void setFeatures(Object handle, Collection features) {
        if (handle instanceof MClassifier
            && features instanceof List) {
            ((MClassifier) handle).setFeatures((List) features);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Set the ImplementationLocation of the given ElementResidence
     * to the given Component.
     *
     * @param handle the ElementResidence
     * @param component the Component
     */
    public void setImplementationLocation(
        Object handle,
        Object component) {
        if (handle instanceof MElementResidence
                && (component == null || component instanceof MComponent)) {
            ((MElementResidence) handle).setImplementationLocation(
                (MComponent) component);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or component: " + component);
    }

    /**
     * Sets an initial value.
     *
     * @param at attribute that we set the initial value of
     * @param expr that is the value to set. Can be <code>null</code>.
     */
    public void setInitialValue(Object at, Object expr) {
        if (at instanceof MAttribute
                && (expr == null || expr instanceof MExpression)) {
            ((MAttribute) at).setInitialValue((MExpression) expr);
            return;
        }
        throw new IllegalArgumentException("at: " + at + " or expr: " + expr);
    }

    /**
     * Set some parameters kind.
     *
     * @param handle is the parameter
     * @param kind is the directionkind
     */
    public void setKind(Object handle, Object kind) {
        if (handle instanceof MParameter
            && kind instanceof MParameterDirectionKind) {
            ((MParameter) handle).setKind((MParameterDirectionKind) kind);
            return;
        }
        if (handle instanceof MPseudostate
            && kind instanceof MPseudostateKind) {
            ((MPseudostate) handle).setKind((MPseudostateKind) kind);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or kind: " + kind);
    }

    /**
     * Set some parameters kind to 'in'.
     *
     * @param handle is the parameter
     */
    public void setKindToIn(Object handle) {
        if (handle instanceof MParameter) {
            ((MParameter) handle).setKind(MParameterDirectionKind.IN);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Set some parameters kind to 'in/out'.
     *
     * @param handle is the parameter
     */
    public void setKindToInOut(Object handle) {
        if (handle instanceof MParameter) {
            ((MParameter) handle).setKind(MParameterDirectionKind.INOUT);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Set some parameters kind to 'out'.
     *
     * @param handle is the parameter
     */
    public void setKindToOut(Object handle) {
        if (handle instanceof MParameter) {
            ((MParameter) handle).setKind(MParameterDirectionKind.OUT);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Set some parameters kind to 'return'.
     *
     * @param handle is the parameter
     */
    public void setKindToReturn(Object handle) {
        if (handle instanceof MParameter) {
            ((MParameter) handle).setKind(MParameterDirectionKind.RETURN);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Sets the container that owns the handle. This must be set
     * correctly so every modelelement except the root model does have
     * an owner. Otherwise the saving/loading will fail.<p>
     *
     * <em>Warning: when changing the implementation of this method
     * be warned that the sequence of the if then else tree DOES
     * matter.</em> Most notabely, do not move the setNamespace method
     * any level up in the tree.<p>
     *
     * <em>Warning: the implementation does not support setting the
     * owner of actions.</em> Use setState1 etc. on action for that
     * goal<p>
     *
     * @param handle The modelelement that must be added to the container
     * @param container The owning modelelement
     * @exception IllegalArgumentException when the handle or
     * container is null or if the handle cannot be added to the
     * container.
     */
    public void setModelElementContainer(
        Object handle,
        Object container) {
        if (handle instanceof MPartition
            && container instanceof MActivityGraph) {
            ((MPartition) handle).setActivityGraph((MActivityGraph) container);
        } else if (handle instanceof MModelElement
                && container instanceof MPartition) {
            ((MPartition) container).addContents((MModelElement) handle);
        } else if (
            handle instanceof MConstraint
                && container instanceof MStereotype) {
            MConstraint c = (MConstraint) handle;
            c.setConstrainedElement2((MStereotype) container);
        } else if (
            handle instanceof MInteraction
                && container instanceof MCollaboration) {
            ((MInteraction) handle).setContext((MCollaboration) container);
        } else if (
            handle instanceof MElementResidence
                && container instanceof MComponent) {
            MElementResidence er = (MElementResidence) handle;
            er.setImplementationLocation((MComponent) container);
        } else if (
            handle instanceof MAttributeLink
                && container instanceof MInstance) {
            ((MAttributeLink) handle).setInstance((MInstance) container);
        } else if (
            handle instanceof MMessage && container instanceof MInteraction) {
            ((MMessage) handle).setInteraction((MInteraction) container);
        } else if (handle instanceof MLinkEnd && container instanceof MLink) {
            ((MLinkEnd) handle).setLink((MLink) container);
        } else if (
            handle instanceof MAttributeLink
                && container instanceof MLinkEnd) {
            ((MAttributeLink) handle).setLinkEnd((MLinkEnd) container);
        } else if (
            handle instanceof MTaggedValue
                && container instanceof MStereotype) {
            ((MTaggedValue) handle).setStereotype((MStereotype) container);
        } else if (
            handle instanceof MTaggedValue
                && container instanceof MModelElement) {
            ((MTaggedValue) handle).setModelElement((MModelElement) container);
        } else if (
            handle instanceof MStateVertex
                && container instanceof MCompositeState) {
            ((MStateVertex) handle).setContainer((MCompositeState) container);
        } else if (
            handle instanceof MElementImport
                && container instanceof MPackage) {
            ((MElementImport) handle).setPackage((MPackage) container);
        } else if (
            handle instanceof MTransition && container instanceof MState) {
            ((MTransition) handle).setState((MState) container);
        } else if (
            handle instanceof MState && container instanceof MStateMachine) {
            ((MState) handle).setStateMachine((MStateMachine) container);
        } else if (
            handle instanceof MTransition
                && container instanceof MStateMachine) {
            ((MTransition) handle).setStateMachine((MStateMachine) container);
        } else if (
            handle instanceof MAction && container instanceof MTransition) {
            ((MAction) handle).setTransition((MTransition) container);
        } else if (
            handle instanceof MGuard && container instanceof MTransition) {
            ((MGuard) handle).setTransition((MTransition) container);
        } else if (
            handle instanceof MModelElement
                && container instanceof MNamespace) {
            ((MModelElement) handle).setNamespace((MNamespace) container);
        } else {
            throw new IllegalArgumentException("handle: " + handle
                    + " or container: " + container);
        }
    }

    /**
     * Sets a multiplicity of some model element.
     *
     * @param handle model element
     * @param arg multiplicity as string OR multiplicity object
     */
    public void setMultiplicity(Object handle, Object arg) {
        if (arg instanceof String) {
            arg =
                ("1_N".equals(arg)) ? MMultiplicity.M1_N : MMultiplicity.M1_1;
        }

        if (arg instanceof MMultiplicity) {
            MMultiplicity mult = (MMultiplicity) arg;

            if (handle instanceof MAssociationRole) {
                ((MAssociationRole) handle).setMultiplicity(mult);
                return;
            }
            if (handle instanceof MClassifierRole) {
                ((MClassifierRole) handle).setMultiplicity(mult);
                return;
            }
            if (handle instanceof MStructuralFeature) {
                ((MStructuralFeature) handle).setMultiplicity(mult);
                return;
            }
            if (handle instanceof MAssociationEnd) {
                ((MAssociationEnd) handle).setMultiplicity(mult);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or arg: " + arg);
    }

    /**
     * Sets a name of some modelelement.
     *
     * @param handle is the model element
     * @param name to set
     */
    public void setName(Object handle, String name) {
        if ((handle instanceof MModelElement) && (name != null)) {
            // The following code is a workaround for issue
            // http://argouml.tigris.org/issues/show_bug.cgi?id=2847.
            // The cause is
            // not known and the best fix available for the moment is to remove
            // the corruptions as they are found.
            int pos = 0;
            while ((pos = name.indexOf(0xffff)) >= 0) {
                name =
                    name.substring(0, pos)
                    + name.substring(pos + 1, name.length());
                try {
                    throw new UmlException(
                            "Illegal character stripped out of element name");
                } catch (UmlException e) {
                    LOG.warn("0xFFFF detected in element name", e);
                }
            }
            ((MModelElement) handle).setName(name);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or name: " + name);
    }

    /**
     * Sets a namespace of some modelelement.
     *
     * @param handle is the model element
     * @param ns is the namespace. Can be <code>null</code>.
     */
    public void setNamespace(Object handle, Object ns) {
        if (handle instanceof MModelElement
            && (ns == null || ns instanceof MNamespace)) {
            ((MModelElement) handle).setNamespace((MNamespace) ns);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or ns: " + ns);
    }

    /**
     * Sets the navigability of some association end.
     *
     * @param handle is the association end
     * @param flag is the navigability flag
     */
    public void setNavigable(Object handle, boolean flag) {
        if (handle instanceof MAssociationEnd) {
            ((MAssociationEnd) handle).setNavigable(flag);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Set the OrderingKind of a given AssociationEnd.
     *
     * @param handle AssociationEnd
     * @param ok OrderingKind
     */
    public void setOrdering(Object handle, Object ok) {
        if (handle instanceof MAssociationEnd && ok instanceof MOrderingKind) {
            ((MAssociationEnd) handle).setOrdering((MOrderingKind) ok);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or ok: " + ok);
    }

    /**
     * Set the owner of a Feature.
     *
     * @param handle Feature
     * @param owner Classifier or null
     */
    public void setOwner(Object handle, Object owner) {
        if (handle instanceof MFeature
            && (owner == null || owner instanceof MClassifier)) {
            ((MFeature) handle).setOwner((MClassifier) owner);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or owner: " + owner);
    }

    /**
     * @param handle Feature
     * @param os ScopeKind
     */
    public void setOwnerScope(Object handle, Object os) {
        if (handle instanceof MFeature
            && (os == null || os instanceof MScopeKind)) {
            ((MFeature) handle).setOwnerScope((MScopeKind) os);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or os: " + os);
    }

    /**
     * Sets the parameters of a classifier, event, objectflowstate or
     * behavioralfeature.
     *
     * @param handle the classifier, event, objectflowstate or
     * behavioralfeature
     * @param parameters is a Collection of parameters
     */
    public void setParameters(Object handle, Collection parameters) {
        if (handle instanceof MObjectFlowState) {
            ((MObjectFlowState) handle).setParameters(parameters);
            return;
        }
        if (handle instanceof MClassifier) {
            ((MClassifier) handle).setParameters(parameters);
            return;
        }
        if (handle instanceof MEvent && parameters instanceof List) {
            ((MEvent) handle).setParameters((List) parameters);
            return;
        }
        if (handle instanceof MBehavioralFeature
            && parameters instanceof List) {
            ((MBehavioralFeature) handle).setParameters((List) parameters);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or parameters: " + parameters);
    }

    /**
     * Sets the parent of a generalization.
     *
     * @param handle generalization
     * @param parent generalizable element (parent)
     */
    public void setParent(Object handle, Object parent) {
        if (handle instanceof MGeneralization
            && parent instanceof MGeneralizableElement) {
            ((MGeneralization) handle).setParent(
                (MGeneralizableElement) parent);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or parent: " + parent);
    }

    /**
     * Set the PowerType of a Generalization.
     * @param handle Generalization
     * @param pt Classifier
     */
    public void setPowertype(Object handle, Object pt) {
        if (handle instanceof MGeneralization
                && (pt == null
                        || pt instanceof MClassifier)) {
            ((MGeneralization) handle).setPowertype((MClassifier) pt);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or pt: " + pt);
    }

    /**
     * Sets the qualified attributes of an association end.
     *
     * @param handle the association end
     * @param elems is a Collection of qualifiers
     */
    public void setQualifiers(Object handle, Collection elems) {
        if (handle instanceof MAssociationEnd && elems instanceof List) {
            ((MAssociationEnd) handle).setQualifiers((List) elems);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Sets the query flag of a behavioral feature.
     *
     * @param handle is the behavioral feature
     * @param flag is the query flag
     */
    public void setQuery(Object handle, boolean flag) {
        if (handle instanceof MBehavioralFeature) {
            ((MBehavioralFeature) handle).setQuery(flag);
            return;
        }
    throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @param handle ElementResidence
     * @param resident ModelElement or null
     */
    public void setResident(Object handle, Object resident) {
        if (handle instanceof MElementResidence
            && (resident == null || resident instanceof MModelElement)) {
            ((MElementResidence) handle).setResident((MModelElement) resident);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or resident: " + resident);
    }

    /**
     * Sets the residents of some model element.
     *
     * @param handle the model element
     * @param residents collection
     */
    public void setResidents(Object handle, Collection residents) {
        if (handle instanceof MNodeInstance) {
            ((MNodeInstance) handle).setResidents(residents);
            return;
        }
        if (handle instanceof MComponentInstance) {
            ((MComponentInstance) handle).setResidents(residents);
            return;
        }
        if (handle instanceof MNode) {
            ((MNode) handle).setResidents(residents);
            return;
        }
    throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Sets if some model element is a root.
     *
     * @param handle model element
     * @param flag is true if it is a root
     */
    public void setRoot(Object handle, boolean flag) {
        if (handle instanceof MReception) {
            ((MReception) handle).setRoot(flag);
            return;
        }
        if (handle instanceof MOperation) {
            ((MOperation) handle).setRoot(flag);
            return;
        }
        if (handle instanceof MGeneralizableElement) {
            ((MGeneralizableElement) handle).setRoot(flag);
            return;
        }
    throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @param handle Flow
     * @param specifications the collection of ModelEvents (sourceFlow)
     */
    public void setSources(Object handle, Collection specifications) {
        if (handle instanceof MFlow) {
            ((MFlow) handle).setSources(specifications);
            return;
        }
    throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Set the Specification flag for a ModelElement.
     *
     * @param handle ModelElement
     * @param specification boolean
     */
    public void setSpecification(Object handle, boolean specification) {
        if (handle instanceof MModelElement) {
            ((MModelElement) handle).setSpecification(specification);
            return;
        }
    throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Sets the specifications of some association end.
     *
     * @param handle the association end
     * @param specifications collection
     */
    public void setSpecifications(
        Object handle,
        Collection specifications) {
        if (handle instanceof MAssociationEnd) {
            ((MAssociationEnd) handle).setSpecifications(specifications);
            return;
        }
    throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Sets the stereotype of some modelelement. The method also
     * copies a stereotype that is not a part of the current model to
     * the current model.<p>
     *
     * TODO: Currently does not copy the stereotype, but changes the
     * namespace to the new model (kidnapping it). That might possibly be
     * dangerous, especially if more complex profile models are developed.
     * This documentation should say what is supposed to be done. I think
     * it would have been better if the caller had been responsible for the
     * stereotype being in the right model and been adviced of
     * eg ModelManagementHelper.getCorrespondingElement(...). Or if that had
     * been used here. This function could possibly assert that the caller had
     * got it right.
     *
     * @param handle model element
     * @param stereo stereotype
     */
    public void setStereotype(Object handle, Object stereo) {
        if (handle instanceof MModelElement) {
            MModelElement me = (MModelElement) handle;
            if (stereo instanceof MStereotype
                && me.getModel() != ((MStereotype) stereo).getModel()) {
                ((MStereotype) stereo).setNamespace(me.getModel());
            }
            if (stereo == null || stereo instanceof MStereotype) {
                me.setStereotype((MStereotype) stereo);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or stereo: " + stereo);
    }

    /**
     * Sets a tagged value of some modelelement.
     *
     * @param handle is the model element
     * @param tag is the tag name (a string)
     * @param value is the value
     */
    public void setTaggedValue(
        Object handle,
        String tag,
        String value) {
        if (handle instanceof MModelElement) {
            ((MModelElement) handle).setTaggedValue(tag, value);
            return;
        }
    throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Set the TaggedValues of a ModelElement.
     *
     * @param handle ModelElement
     * @param taggedValues Collection of TaggedValues
     */
    public void setTaggedValues(
        Object handle,
        Collection taggedValues) {
        if (handle instanceof MModelElement) {
            ((MModelElement) handle).setTaggedValues(taggedValues);
            return;
        }
    throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Set the target scope of some association end or structural feature.
     *
     * @param handle the model element
     * @param scopeKind the target scope
     */
    public void setTargetScope(Object handle, Object scopeKind) {
        if (scopeKind instanceof MScopeKind) {
            if (handle instanceof MStructuralFeature) {
                ((MStructuralFeature) handle).setTargetScope(
                    (MScopeKind) scopeKind);
                return;
            }
            if (handle instanceof MAssociationEnd) {
                ((MAssociationEnd) handle)
                	.setTargetScope((MScopeKind) scopeKind);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or scopeKind: " + scopeKind);
    }

    /**
     * Sets the type of some parameter.
     *
     * @param handle is the model element
     * @param type is the type (a classifier)
     */
    public void setType(Object handle, Object type) {
        if (type == null || type instanceof MClassifier) {
            if (handle instanceof MObjectFlowState) {
                ((MObjectFlowState) handle).setType((MClassifier) type);
                return;
            }
            if (handle instanceof MClassifierInState) {
                ((MClassifierInState) handle).setType((MClassifier) type);
                return;
            }
            if (handle instanceof MParameter) {
                ((MParameter) handle).setType((MClassifier) type);
                return;
            }
            if (handle instanceof MAssociationEnd) {
                ((MAssociationEnd) handle).setType((MClassifier) type);
                return;
            }
            if (handle instanceof MStructuralFeature) {
                ((MStructuralFeature) handle).setType((MClassifier) type);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or type: " + type);
    }

    /**
     * Set the UUID of this element.
     *
     * @param handle base element (MBase type)
     * @param uuid is the UUID
     */
    public void setUUID(Object handle, String uuid) {
        if (handle instanceof MBase) {
            ((MBase) handle).setUUID(uuid);
            return;
        }
    throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Set the visibility of some modelelement.
     *
     * @param handle element
     * @param visibility is the visibility
     */
    public void setVisibility(Object handle, Object visibility) {
        if (visibility instanceof MVisibilityKind) {
            if (handle instanceof MModelElement) {
                ((MModelElement) handle).setVisibility(
                    (MVisibilityKind) visibility);
                return;
            }
            if (handle instanceof MElementResidence) {
                ((MElementResidence) handle).setVisibility(
                    (MVisibilityKind) visibility);
                    return;
            }
            if (handle instanceof MElementImport) {
                ((MElementImport) handle).setVisibility(
                    (MVisibilityKind) visibility);
                    return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or visibility: " + visibility);
    }
}
