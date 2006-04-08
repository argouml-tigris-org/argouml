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

package org.argouml.model.mdr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.jmi.reflect.InvalidObjectException;

import org.apache.log4j.Logger;
import org.argouml.model.CoreHelper;
import org.argouml.model.Model;
import org.argouml.model.ModelManagementHelper;
import org.argouml.model.NotImplementedException;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.activitygraphs.ClassifierInState;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;
import org.omg.uml.behavioralelements.activitygraphs.Partition;
import org.omg.uml.behavioralelements.collaborations.AssociationEndRole;
import org.omg.uml.behavioralelements.collaborations.AssociationRole;
import org.omg.uml.behavioralelements.collaborations.ClassifierRole;
import org.omg.uml.behavioralelements.collaborations.Collaboration;
import org.omg.uml.behavioralelements.collaborations.Interaction;
import org.omg.uml.behavioralelements.collaborations.Message;
import org.omg.uml.behavioralelements.commonbehavior.Action;
import org.omg.uml.behavioralelements.commonbehavior.AttributeLink;
import org.omg.uml.behavioralelements.commonbehavior.ComponentInstance;
import org.omg.uml.behavioralelements.commonbehavior.Instance;
import org.omg.uml.behavioralelements.commonbehavior.Link;
import org.omg.uml.behavioralelements.commonbehavior.LinkEnd;
import org.omg.uml.behavioralelements.commonbehavior.NodeInstance;
import org.omg.uml.behavioralelements.commonbehavior.Reception;
import org.omg.uml.behavioralelements.commonbehavior.Signal;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.Event;
import org.omg.uml.behavioralelements.statemachines.Guard;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.statemachines.StateVertex;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.behavioralelements.usecases.Actor;
import org.omg.uml.behavioralelements.usecases.Extend;
import org.omg.uml.behavioralelements.usecases.Include;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.BehavioralFeature;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Comment;
import org.omg.uml.foundation.core.Component;
import org.omg.uml.foundation.core.Constraint;
import org.omg.uml.foundation.core.DataType;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.ElementResidence;
import org.omg.uml.foundation.core.Enumeration;
import org.omg.uml.foundation.core.Feature;
import org.omg.uml.foundation.core.Flow;
import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.Interface;
import org.omg.uml.foundation.core.Method;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.Node;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.core.Relationship;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.StructuralFeature;
import org.omg.uml.foundation.core.TagDefinition;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.core.UmlAssociation;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.datatypes.AggregationKind;
import org.omg.uml.foundation.datatypes.AggregationKindEnum;
import org.omg.uml.foundation.datatypes.BooleanExpression;
import org.omg.uml.foundation.datatypes.CallConcurrencyKind;
import org.omg.uml.foundation.datatypes.ChangeableKind;
import org.omg.uml.foundation.datatypes.ChangeableKindEnum;
import org.omg.uml.foundation.datatypes.Expression;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.OrderingKind;
import org.omg.uml.foundation.datatypes.ParameterDirectionKind;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;
import org.omg.uml.foundation.datatypes.ProcedureExpression;
import org.omg.uml.foundation.datatypes.PseudostateKind;
import org.omg.uml.foundation.datatypes.ScopeKind;
import org.omg.uml.foundation.datatypes.VisibilityKind;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;
import org.omg.uml.modelmanagement.ElementImport;
import org.omg.uml.modelmanagement.UmlPackage;


/**
 * The Core Helper implementation for MDR.<p>
 *
 * @since ARGO0.19.5
 * @author Ludovic Ma&icirc;tre
 * @author Tom Morris
 * derived from NSUML implementation by:
 * @author Linus Tolke
 */
public class CoreHelperMDRImpl implements CoreHelper {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(CoreHelperMDRImpl.class);

    /**
     * The model implementation.
     */
    private MDRModelImplementation modelImpl;

    /**
     * Constructor.
     *
     * @param theModelImpl
     *            The MDRModelImplementation.
     */
    public CoreHelperMDRImpl(MDRModelImplementation theModelImpl) {
        super();
        modelImpl = theModelImpl;
    }

    /**
     * Return the first item from a collection using the most efficient method
     * possible. Returns null for an empty collection.
     *
     * @param c
     *            The Collection.
     * @return the first element of a Collection.
     */
    private static Object getFirstItemOrNull(Collection c) {
        if (c.size() == 0) {
            return null;
        }
        if (c instanceof List) {
            return ((List) c).get(0);
        }
        return c.iterator().next();
    }

    /**
     * @see org.argouml.model.CoreHelper#isSubType(java.lang.Object,
     *      java.lang.Object)
     */
    public boolean isSubType(Object type, Object subType) {
        if (!(type instanceof Class) || !(subType instanceof Class)) {
            throw new IllegalArgumentException("Metatypes are expected");
        }
        return ((Class) type).isAssignableFrom((Class) subType);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllSupertypes(java.lang.Object)
     */
    public Collection getAllSupertypes(Object cls1) {

        if (!(cls1 instanceof Classifier)) {
            throw new IllegalArgumentException();
        }

        Classifier cls = (Classifier) cls1;

        Collection result = new HashSet();
        Collection add = getSupertypes(cls);
        do {
            Collection newAdd = new HashSet();
            Iterator addIter = add.iterator();
            while (addIter.hasNext()) {
                GeneralizableElement next =
                    (GeneralizableElement) addIter.next();
                if (next instanceof Classifier) {
                    newAdd.addAll(getSupertypes(next));
                }
            }
            result.addAll(add);
            add = newAdd;
            add.removeAll(result);
        } while (!add.isEmpty());
        return result;
    }

    /**
     * @see org.argouml.model.CoreHelper#getSupertypes(java.lang.Object)
     */
    public Collection getSupertypes(Object ogeneralizableelement) {
        Collection result = new HashSet();
        if (ogeneralizableelement instanceof GeneralizableElement) {
            Iterator genIterator =
                modelImpl.getFacade().getGeneralizations(
                    ogeneralizableelement).iterator();
            while (genIterator.hasNext()) {
                Generalization next = (Generalization) genIterator.next();
                result.add(next.getParent());
            }
        }
        return result;
    }

    /**
     * @see org.argouml.model.CoreHelper#getAssociateEnds(java.lang.Object)
     */
    public Collection getAssociateEnds(Object classifier) {
        if (!(classifier instanceof Classifier)) {
            throw new IllegalArgumentException();
        }
        return Model.getFacade().getAssociationEnds(classifier);
    }

    /**
     * @see org.argouml.model.CoreHelper#getAssociateEndsInh(java.lang.Object)
     */
    public Collection getAssociateEndsInh(Object classifier1) {
        if (!(classifier1 instanceof Classifier)) {
            throw new IllegalArgumentException();
        }

        Classifier classifier = (Classifier) classifier1;

        Collection result = new ArrayList();
        result.addAll(getAssociateEnds(classifier));
        Collection generalizations =
            Model.getFacade().getGeneralizations(classifier);
        Iterator genIter = generalizations.iterator();
        while (genIter.hasNext()) {
            Object parent = Model.getFacade().getParent(genIter.next());
            result.addAll(getAssociateEndsInh(parent));
        }
        return result;
    }

    /**
     * @see org.argouml.model.CoreHelper#removeFeature( java.lang.Object,
     *      java.lang.Object)
     */
    public void removeFeature(Object cls, Object feature) {
        if (cls instanceof Classifier && feature instanceof Feature) {
            ((Classifier) cls).getFeature().remove(feature);
        }
    }

    /**
     * @see org.argouml.model.CoreHelper#getFeatureName(java.lang.Object)
     */
    public String getFeatureName(Object o) {
        if (o != null && o instanceof Feature) {
            return ((Feature) o).getName();
        }
        throw new IllegalArgumentException("Not a feature");
    }

    /**
     * @see org.argouml.model.CoreHelper#isMethod(java.lang.Object)
     * There is a duplicate method Model.getFacade().isAMethod(Object)
     */
    public boolean isMethod(Object o) {
        return (o instanceof Method);
    }

    /**
     * @see org.argouml.model.CoreHelper#isOperation(java.lang.Object)
     * There is a duplicate method Model.getFacade().isAOperation(Object)
     */
    public boolean isOperation(Object o) {
        return (o instanceof Operation);
    }

    /**
     * @see org.argouml.model.CoreHelper#getOperations(java.lang.Object)
     */
    public Collection getOperations(Object classifier) {
        Collection result = new ArrayList();
        if (classifier instanceof Classifier) {
            Classifier mclassifier = (Classifier) classifier;
            Iterator features = mclassifier.getFeature().iterator();
            while (features.hasNext()) {
                Feature feature = (Feature) features.next();
                if (feature instanceof Operation) {
                    result.add(feature);
                }
            }
        }
        return result;
    }

    /**
     * @see org.argouml.model.CoreHelper#setOperations( java.lang.Object,
     *      java.util.Collection)
     */
    public void setOperations(Object classifier, Collection operations) {
        if (classifier instanceof Classifier) {
            Classifier mclassifier = (Classifier) classifier;
            List result = new ArrayList(mclassifier.getFeature());
            Iterator features = mclassifier.getFeature().iterator();
            while (features.hasNext()) {
                Feature feature = (Feature) features.next();
                if (feature instanceof Operation) {
                    result.remove(feature);
                }
            }
            result.addAll(operations);
            mclassifier.getFeature().clear();
            mclassifier.getFeature().addAll(result);
        }
    }

    /**
     * @see org.argouml.model.CoreHelper#getAttributes(java.lang.Object)
     */
    public Collection getAttributes(Object classifier) {
        if (!(classifier instanceof Classifier)) {
            throw new IllegalArgumentException();
        }

        Collection result = new ArrayList();
        Iterator features = ((Classifier) classifier).getFeature().iterator();
        while (features.hasNext()) {
            Feature feature = (Feature) features.next();
            if (feature instanceof Attribute) {
                result.add(feature);
            }
        }
        return result;
    }

    /**
     * @see org.argouml.model.CoreHelper#setAttributes( java.lang.Object,
     *      java.util.Collection)
     */
    public void setAttributes(Object classifier, Collection attributes) {
        if (classifier instanceof Classifier) {
            Classifier mclassifier = (Classifier) classifier;
            List result = new ArrayList(mclassifier.getFeature());
            Iterator features = mclassifier.getFeature().iterator();
            while (features.hasNext()) {
                Feature feature = (Feature) features.next();
                if (feature instanceof Attribute) {
                    result.remove(feature);
                }
            }
            result.addAll(attributes);
            mclassifier.getFeature().clear();
            mclassifier.getFeature().addAll(result);
        }
    }

    /**
     * @see org.argouml.model.CoreHelper#getAttributesInh(java.lang.Object)
     */
    public Collection getAttributesInh(Object classifier) {

        if (!(classifier instanceof Classifier)) {
            throw new IllegalArgumentException();
        }

        Collection result = new ArrayList();
        result.addAll(modelImpl.getFacade().getStructuralFeatures(classifier));

        Collection generalizations = Model.getFacade().getGeneralizations(
                classifier);
        Iterator genIter = generalizations.iterator();
        while (genIter.hasNext()) {
            Object parent = Model.getFacade().getParent(genIter.next());
            result.addAll(getAttributesInh(parent));
        }
        return result;
    }

    /**
     * @see org.argouml.model.CoreHelper#getOperationsInh(java.lang.Object)
     */
    public Collection getOperationsInh(Object classifier) {
        if (!(classifier instanceof Classifier)) {
            throw new IllegalArgumentException();
        }

        Collection result = new ArrayList();
        result.addAll(modelImpl.getFacade().getOperations(classifier));

        Collection generalizations = Model.getFacade().getGeneralizations(
                classifier);
        Iterator genIter = generalizations.iterator();
        while (genIter.hasNext()) {
            Object parent = Model.getFacade().getParent(genIter.next());
            result.addAll(getOperationsInh(parent));
        }
        return result;
    }

    /**
     * @see org.argouml.model.CoreHelper#getReturnParameter(java.lang.Object)
     */
    public Object getReturnParameter(Object operation1) {
        if (!(operation1 instanceof Operation)) {
            throw new IllegalArgumentException();
        }

        Collection returnParams = getReturnParameters(operation1);
        switch (returnParams.size()) {
        case 1:
            return (Parameter) returnParams.iterator().next();
        case 0:
            return null;
        default:
            LOG.debug("More than one ReturnParameter found, returning first!");
            return (Parameter) returnParams.iterator().next();
        }
    }

    /**
     * @see org.argouml.model.CoreHelper#getReturnParameters(java.lang.Object)
     */
    public Collection getReturnParameters(Object operation) {
        Vector returnParams = new Vector();
        Iterator params = ((Operation) operation).getParameter().iterator();
        while (params.hasNext()) {
            Parameter parameter = (Parameter) params.next();
            if (ParameterDirectionKindEnum.PDK_RETURN.equals(parameter.
                    getKind())) {
                returnParams.add(parameter);
            }
        }
        return returnParams;
    }

    /**
     * @see org.argouml.model.CoreHelper#getSpecification(java.lang.Object)
     */
    public Object getSpecification(Object object) {
        if (!(object instanceof Method)) {
            throw new IllegalArgumentException("Not a method : " + object);
        }
        return ((Method) object).getSpecification();
    }

    /**
     * @see org.argouml.model.CoreHelper#getSpecifications(java.lang.Object)
     * same as Facade.getSpecifications()
     */
    public Collection getSpecifications(Object classifier) {
        // TODO: not sure whether this belongs here or in the Facade
        // but we definitely don't need three independent implementations!
        // - tfm 20060224
        return getRealizedInterfaces(classifier);
    }

    /**
     * @see org.argouml.model.CoreHelper#getSubtypes(java.lang.Object)
     */
    public Collection getSubtypes(Object cls) {
        if (!(cls instanceof Classifier)) {
            throw new IllegalArgumentException();
        }

        Collection result = new Vector();
        Collection gens = Model.getFacade().getSpecializations(cls);
        Iterator genIterator = gens.iterator();
        while (genIterator.hasNext()) {
            Generalization next = (Generalization) genIterator.next();
            result.add(next.getChild());
        }
        return result;
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllBehavioralFeatures(
     *      java.lang.Object)
     */
    public Collection getAllBehavioralFeatures(Object element) {
        if (!(element instanceof ModelElement)) {
            throw new IllegalArgumentException();
        }
        List contents = new ArrayList();
        contents.addAll(Model.getFacade().getTaggedValuesCollection(element));
        contents.addAll(((ModelElement) element).getTemplateParameter());
        Iterator it = contents.iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof Classifier) {
                Classifier clazz = (Classifier) o;
                if (!(clazz instanceof DataType)) {
                    Iterator it1 = clazz.getFeature().iterator();
                    while (it1.hasNext()) {
                        Object o1 = it1.next();
                        if (o1 instanceof BehavioralFeature) {
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
     * @see org.argouml.model.CoreHelper#getBehavioralFeatures(java.lang.Object)
     */
    public Collection getBehavioralFeatures(Object clazz) {
        if (clazz instanceof Classifier) {
            List ret = new ArrayList();
            Iterator it = modelImpl.getFacade().getFeatures(clazz).iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof BehavioralFeature) {
                    ret.add(o);
                }
            }
            return ret;
        } else {
            throw new IllegalArgumentException("Argument is not a classifier");
        }
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllInterfaces(java.lang.Object)
     */
    public Collection getAllInterfaces(Object ns) {
        if (ns == null) {
            return new ArrayList();
        }
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException();
        }

        Iterator it = ((Namespace) ns).getOwnedElement().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof Namespace) {
                list.addAll(getAllInterfaces(o));
            }
            if (o instanceof Interface) {
                list.add(o);
            }
        }
        return list;
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllClasses(java.lang.Object)
     */
    public Collection getAllClasses(Object ns) {
        if (ns == null) {
            return new ArrayList();
        }
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException();
        }

        Iterator it = ((Namespace) ns).getOwnedElement().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof Namespace) {
                list.addAll(getAllClasses(o));
            }
            if (o instanceof UmlClass) {
                list.add(o);
            }
        }
        return list;
    }

    /**
     * @see org.argouml.model.CoreHelper#getRealizedInterfaces(java.lang.Object)
     */
    public Collection getRealizedInterfaces(Object cls) {
        Classifier classifier = (Classifier) cls;
        if (classifier == null) {
            return new ArrayList();
        }
        Iterator it = classifier.getClientDependency().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object clientDependency = it.next();
            if (clientDependency instanceof Abstraction) {
                Abstraction abstraction = (Abstraction) clientDependency;
                Collection stereos = abstraction.getStereotype();
                for (Iterator s = stereos.iterator(); s.hasNext();) {
                    Stereotype stereo = (Stereotype) s.next();
                    if (stereo != null
                            && "realize".equals(stereo.getName())
                            // the following should always be true
                            && stereo.getBaseClass().contains("Abstraction")) {
                        Iterator it2 = abstraction.getSupplier().iterator();
                        while (it2.hasNext()) {
                            Object supplier = it2.next();
                            if (supplier instanceof Interface) {
                                list.add(supplier);
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * @see org.argouml.model.CoreHelper#getExtendedClassifiers(
     *      java.lang.Object)
     */
    public Collection getExtendedClassifiers(Object clazz) {
        if (clazz == null) {
            return new ArrayList();
        }
        Iterator it =
            modelImpl.getFacade().getGeneralizations(clazz).iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Generalization gen = (Generalization) it.next();
            GeneralizableElement parent = gen.getParent();
            if (parent != null) {
                list.add(parent);
            }
        }
        return list;
    }

    /**
     * @see org.argouml.model.CoreHelper#getGeneralization( java.lang.Object,
     *      java.lang.Object)
     */
    public Object getGeneralization(Object achild, Object aparent) {
        if (!(achild instanceof GeneralizableElement) 
                || !(aparent instanceof GeneralizableElement)) {
            throw new IllegalArgumentException();
        }
        GeneralizableElement child = (GeneralizableElement) achild;
        GeneralizableElement parent = (GeneralizableElement) aparent;
        Iterator it =
            modelImpl.getFacade().getGeneralizations(child).iterator();
        while (it.hasNext()) {
            Generalization gen = (Generalization) it.next();
            if (gen.getParent() == parent) {
                return gen;
            }
        }
        return null;
    }

    /**
     * @see org.argouml.model.CoreHelper#getBody(java.lang.Object)
     */
    public String getBody(Object comment) {
        if (comment instanceof Comment) {
            /*
             * In UML 1.3, the name is the only place to store text.
             * In UML 1.4, there is a body as well, but some tools
             * still seem to use name.
             */
            return ((Comment) comment).getBody();
        }
        throw new IllegalArgumentException();
    }

    /**
     * @see org.argouml.model.CoreHelper#getFlows( java.lang.Object,
     *      java.lang.Object)
     */
    public Collection getFlows(Object source, Object target) {
        if (!(source instanceof ModelElement)) {
            throw new IllegalArgumentException("source");
        }
        if (!(target instanceof ModelElement)) {
            throw new IllegalArgumentException("target");
        }

        List ret = new ArrayList();
        Collection targetFlows = ((ModelElement) target).getTargetFlow();
        Iterator it = ((ModelElement) source).getSourceFlow().iterator();
        while (it.hasNext()) {
            Flow flow = (Flow) it.next();
            if (targetFlows.contains(flow)) {
                ret.add(flow);
            }
        }
        return ret;
    }

    /**
     * @see org.argouml.model.CoreHelper#getExtendingElements(java.lang.Object)
     */
    public Collection getExtendingElements(Object clazz) {
        if (clazz == null) {
            return new ArrayList();
        }
        Iterator it =
            modelImpl.getFacade().getSpecializations(clazz).iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Generalization gen = (Generalization) it.next();
            GeneralizableElement client =
                (GeneralizableElement) modelImpl.getFacade().getChild(gen);
            if (client != null) {
                list.add(client);
            }
        }
        return list;
    }

    /**
     * @see org.argouml.model.CoreHelper#getExtendingClassifiers(
     *      java.lang.Object)
     */
    public Collection getExtendingClassifiers(Object clazz) {
        if (clazz == null) {
            return new ArrayList();
        }
        if (!(clazz instanceof Classifier)) {
            throw new IllegalArgumentException();
        }
        Collection specializations =
            Model.getFacade().getSpecializations(clazz);
        Iterator it = specializations.iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Generalization gen = (Generalization) it.next();
            GeneralizableElement client = gen.getChild();
            if (client instanceof Classifier) {
                list.add(client);
            }
        }
        return list;
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllComponents(java.lang.Object)
     */
    public Collection getAllComponents(Object ns) {
        if (ns == null) {
            return new ArrayList();
        }
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException();
        }

        Iterator it = ((Namespace) ns).getOwnedElement().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof Namespace) {
                list.addAll(getAllComponents(o));
            }
            if (o instanceof Component) {
                list.add(o);
            }
        }
        return list;
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllDataTypes(java.lang.Object)
     */
    public Collection getAllDataTypes(Object ns) {
        if (ns == null) {
            return new ArrayList();
        }
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException();
        }

        Iterator it = ((Namespace) ns).getOwnedElement().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof Namespace) {
                list.addAll(getAllDataTypes(o));
            }
            if (o instanceof DataType) {
                list.add(o);
            }
        }
        return list;
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllNodes(java.lang.Object)
     */
    public Collection getAllNodes(Object ns) {
        if (ns == null) {
            return new ArrayList();
        }
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException();
        }

        Iterator it = ((Namespace) ns).getOwnedElement().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof Namespace) {
                list.addAll(getAllNodes(o));
            }
            if (o instanceof Node) {
                list.add(o);
            }
        }
        return list;
    }

    /**
     * @see org.argouml.model.CoreHelper#getAssociatedClassifiers(
     *      java.lang.Object)
     */
    public Collection getAssociatedClassifiers(Object aclassifier) {
        Classifier classifier = (Classifier) aclassifier;
        if (classifier == null) {
            return new ArrayList();
        }
        List list = new ArrayList();
        Iterator it =
            Model.getFacade().getAssociationEnds(classifier).iterator();
        while (it.hasNext()) {
            AssociationEnd end = (AssociationEnd) it.next();
            UmlAssociation assoc = end.getAssociation();
            Iterator it2 = assoc.getConnection().iterator();
            while (it2.hasNext()) {
                AssociationEnd end2 = (AssociationEnd) it2.next();
                if (end2 != end) {
                    list.add(end2.getParticipant());
                }
            }
        }
        return list;
    }

    /**
     * @see org.argouml.model.CoreHelper#getAssociations( java.lang.Object,
     *      java.lang.Object)
     */
    public Collection getAssociations(Object/* Classifier */from,
            Object/* Classifier */to) {
        if (!(from instanceof Classifier) || !(to instanceof Classifier)) {
            throw new IllegalArgumentException();
        }
        Set ret = new HashSet();
        Iterator it = modelImpl.getFacade().getAssociationEnds(from).iterator();
        while (it.hasNext()) {
            AssociationEnd end = (AssociationEnd) it.next();
            UmlAssociation assoc = end.getAssociation();
            Iterator it2 = assoc.getConnection().iterator();
            while (it2.hasNext()) {
                AssociationEnd end2 = (AssociationEnd) it2.next();
                if (end2.getParticipant() == to) {
                    ret.add(assoc);
                }
            }
        }
        return ret;
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllClassifiers(java.lang.Object)
     */
    public Collection getAllClassifiers(Object namespace) {
        if (namespace == null) {
            throw new IllegalArgumentException();
        }
        Namespace ns = (Namespace) namespace;
        Iterator it = ns.getOwnedElement().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof Namespace) {
                list.addAll(getAllClassifiers(o));
            }
            if (o instanceof Classifier) {
                list.add(o);
            }
        }
        return list;
    }

    /**
     * @see org.argouml.model.CoreHelper#getAssociations(java.lang.Object)
     */
    public Collection getAssociations(Object oclassifier) {
        Collection col = new ArrayList();
        if (oclassifier instanceof Classifier) {
            Classifier classifier = (Classifier) oclassifier;
            Iterator it =
                Model.getFacade().getAssociationEnds(classifier).iterator();
            while (it.hasNext()) {
                col.add(((AssociationEnd) it.next()).getAssociation());
            }
        }
        return col;
    }

    /**
     * @see org.argouml.model.CoreHelper#getAssociationEnd( java.lang.Object,
     *      java.lang.Object)
     */
    public Object getAssociationEnd(Object type, Object assoc) {
        if (!(type instanceof Classifier)) {
            throw new IllegalArgumentException();
        }
        if (!(assoc instanceof UmlAssociation)) {
            throw new IllegalArgumentException();
        }
        Iterator it = Model.getFacade().getAssociationEnds(type).iterator();
        while (it.hasNext()) {
            AssociationEnd end = (AssociationEnd) it.next();
            if (((UmlAssociation) assoc).getConnection().contains(end)) {
                return end;
            }
        }
        return null;
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllContents(java.lang.Object)
     */
    public Collection getAllContents(Object clazz) {
        if (clazz == null) {
            return new ArrayList();
        }
        if (!(clazz instanceof Classifier)) {
            throw new IllegalArgumentException();
        }

        List list = new ArrayList();
        Iterator it = ((Namespace) clazz).getOwnedElement().iterator();
        while (it.hasNext()) {
            ModelElement element = (ModelElement) it.next();
            if (element.getVisibility().equals(VisibilityKindEnum.VK_PUBLIC)
                    || element.getVisibility().equals(
                            VisibilityKindEnum.VK_PROTECTED)) {
                list.add(element);
            }
        }
        it = modelImpl.getFacade().getGeneralizations(clazz).iterator();
        while (it.hasNext()) {
            list.addAll(getAllContents(it.next()));
        }
        return list;
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllAttributes(java.lang.Object)
     */
    public Collection getAllAttributes(Object clazz) {
        if (clazz == null) {
            return new ArrayList();
        }
        if (!(clazz instanceof Classifier)) {
            throw new IllegalArgumentException();
        }

        List list = new ArrayList();
        Iterator it = ((Classifier) clazz).getFeature().iterator();
        while (it.hasNext()) {
            Feature element = (Feature) it.next();
            if (element instanceof Attribute) {
                list.add(element);
            }
        }
        it = modelImpl.getFacade().getGeneralizations(clazz).iterator();
        while (it.hasNext()) {
            list.addAll(getAllAttributes(it.next()));
        }
        return list;
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllVisibleElements(java.lang.Object)
     */
    public Collection getAllVisibleElements(Object ns) {
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException();
        }

        List list = new ArrayList();
        Iterator it = ((Namespace) ns).getOwnedElement().iterator();
        while (it.hasNext()) {
            ModelElement element = (ModelElement) it.next();
            if (element.getVisibility().equals(VisibilityKindEnum.VK_PUBLIC)) {
                list.add(element);
            }
        }
        return list;
    }

    /**
     * @see org.argouml.model.CoreHelper#getSource(java.lang.Object)
     */
    public Object getSource(Object relationship) {
        if (!(relationship instanceof Relationship)
                && !(relationship instanceof Link)
                && !(relationship instanceof AssociationEnd)) {

            throw new IllegalArgumentException("Argument "
                    + relationship.toString() + " is not " + "a relationship");

        }
        try {
            if (relationship instanceof Link) {
                Iterator it = modelImpl.getFacade()
                        .getConnections(relationship).iterator();
                if (it.hasNext()) {
                    return modelImpl.getFacade().getInstance(it.next());
                } else {
                    return null;
                }
            }
            if (relationship instanceof UmlAssociation) {
                UmlAssociation assoc = (UmlAssociation) relationship;
                List conns = assoc.getConnection();
                if (conns == null || conns.isEmpty()) {
                    return null;
                }
                return ((AssociationEnd) conns.get(0)).getParticipant();
            }
            if (relationship instanceof Generalization) {
                Generalization gen = (Generalization) relationship;
                return gen.getChild();
            }
            if (relationship instanceof Dependency) {
                Dependency dep = (Dependency) relationship;
                Collection col = dep.getClient();
                if (col.isEmpty()) {
                    return null;
                }
                return (ModelElement) (col.toArray())[0];
            }
            if (relationship instanceof Flow) {
                Flow flow = (Flow) relationship;
                Collection col = flow.getSource();
                if (col.isEmpty()) {
                    return null;
                }
                return (ModelElement) (col.toArray())[0];
            }
            if (relationship instanceof Extend) {
                Extend extend = (Extend) relationship;
                return extend.getExtension(); // we have to follow the
                                                // arrows..
            }
            if (relationship instanceof Include) {
                Include include = (Include) relationship;
                return modelImpl.getFacade().getBase(include);
            }
            if (relationship instanceof AssociationEnd) {
                return ((AssociationEnd) relationship).getAssociation();
            }
        } catch (InvalidObjectException e) {
            LOG.error("Queried a removed model element", e);
            return null;
        }
        return null;
    }

    /**
     * @see org.argouml.model.CoreHelper#getDestination(java.lang.Object)
     */
    public Object getDestination(Object relationship) {
        if (!(relationship instanceof Relationship)
                && !(relationship instanceof Link)
                && !(relationship instanceof AssociationEnd)) {

            throw new IllegalArgumentException("Argument is not "
                    + "a relationship");
        }
        if (relationship instanceof Link) {
            Iterator it =
                modelImpl.getFacade().getConnections(relationship).iterator();
            if (it.hasNext()) {
                it.next();
                if (it.hasNext()) {
                    return modelImpl.getFacade().getInstance(it.next());
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        if (relationship instanceof UmlAssociation) {
            UmlAssociation assoc = (UmlAssociation) relationship;
            List conns = assoc.getConnection();
            if (conns.size() <= 1) {
                return null;
            }
            return ((AssociationEnd) conns.get(1)).getParticipant();
        }
        if (relationship instanceof Generalization) {
            Generalization gen = (Generalization) relationship;
            return gen.getParent();
        }
        if (relationship instanceof Dependency) {
            Dependency dep = (Dependency) relationship;
            Collection col = dep.getSupplier();
            if (col.isEmpty()) {
                return null;
            }
            return (ModelElement) getFirstItemOrNull(col);
        }
        if (relationship instanceof Flow) {
            Flow flow = (Flow) relationship;
            Collection col = flow.getTarget();
            if (col.isEmpty()) {
                return null;
            }
            return (ModelElement) getFirstItemOrNull(col);
        }
        if (relationship instanceof Extend) {
            Extend extend = (Extend) relationship;
            return extend.getBase();
        }
        if (relationship instanceof Include) {
            Include include = (Include) relationship;
            return modelImpl.getFacade().getAddition(include);
        }
        if (relationship instanceof AssociationEnd) {
            return ((AssociationEnd) relationship).getParticipant();
        }
        return null;
    }

    /**
     * @see org.argouml.model.CoreHelper#getDependencies( java.lang.Object,
     *      java.lang.Object)
     */
    public Collection getDependencies(Object supplierObj, Object clientObj) {

        if (!(supplierObj instanceof ModelElement)
                || !(clientObj instanceof ModelElement)) {
            throw new IllegalArgumentException("null argument");
        }

        ModelElement supplier = (ModelElement) supplierObj;
        ModelElement client = (ModelElement) clientObj;

        List ret = new ArrayList();
        Collection clientDependencies = client.getClientDependency();
        Iterator it =
            Model.getFacade().getSupplierDependencies(supplier).iterator();
        while (it.hasNext()) {
            Dependency dep = (Dependency) it.next();
            if (clientDependencies.contains(dep)) {
                ret.add(dep);
            }
        }
        return ret;
    }

    /**
     * @see org.argouml.model.CoreHelper#getRelationships( java.lang.Object,
     *      java.lang.Object)
     */
    public Collection getRelationships(Object source, Object dest) {
        Set ret = new HashSet();
        if (!(source instanceof ModelElement)) {
            throw new IllegalArgumentException("source");
        }
        if (!(dest instanceof ModelElement)) {
            throw new IllegalArgumentException("dest");
        }

        ret.addAll(getFlows(source, dest));
        ret.addAll(getFlows(dest, source));
        ret.addAll(getDependencies(source, dest));
        ret.addAll(getDependencies(dest, source));
        if (source instanceof GeneralizableElement
                && dest instanceof GeneralizableElement) {
            ret.add(getGeneralization(source, dest));
            ret.add(getGeneralization(dest, source));
            if (source instanceof Classifier && dest instanceof Classifier) {
                ret.addAll(getAssociations(source, dest));
            }
        }
        return ret;
    }

    /**
     * @see org.argouml.model.CoreHelper#isValidNamespace( java.lang.Object,
     *      java.lang.Object)
     */
    // TODO: This hasn't been updated for UML 1.4
    public boolean isValidNamespace(Object mObj, Object nsObj) {

        if (!(mObj instanceof ModelElement) || !(nsObj instanceof Namespace)) {
            return false;
        }

        ModelElement modelElement = (ModelElement) mObj;
        Namespace ns = (Namespace) nsObj;

        if (modelElement == null || ns == null) {
            return false;
        }

        try {
            if (Model.getFacade().getModel(ns) != Model.getFacade().getModel(
                    modelElement)) {
                return false;
            }

            if (modelElement == ns) {
                return false;
            }
            if (modelElement instanceof Namespace
                    && modelElement
                            == getFirstSharedNamespace(modelElement, ns)) {
                return false;
            }
            if (ns instanceof Interface || ns instanceof Actor) {
                return false;
            } else if (ns instanceof UseCase 
                    && modelElement instanceof Classifier) {
                return false;
            } else if (ns instanceof Component) {
                return (modelElement instanceof Component
                        && modelElement != ns);
            } else if (ns instanceof Collaboration) {
                if (!(modelElement instanceof ClassifierRole
                        || modelElement instanceof AssociationRole
                        || modelElement instanceof Generalization
                        || modelElement instanceof Constraint)) {
                    return false;
                }
            } else if (ns instanceof UmlPackage) {
                if (!(modelElement instanceof UmlPackage
                        || modelElement instanceof Classifier
                        || modelElement instanceof UmlAssociation
                        || modelElement instanceof Generalization
                        || modelElement instanceof Dependency
                        || modelElement instanceof Constraint
                        || modelElement instanceof Collaboration
                        || modelElement instanceof StateMachine
                        || modelElement instanceof Stereotype)) {
                    return false;
                }
            } else if (ns instanceof UmlClass) {
                if (!(modelElement instanceof UmlClass
                        || modelElement instanceof UmlAssociation
                        || modelElement instanceof Generalization
                        || modelElement instanceof UseCase
                        || modelElement instanceof Constraint
                        || modelElement instanceof Dependency
                        || modelElement instanceof Collaboration
                        || modelElement instanceof DataType
                        || modelElement instanceof Interface)) {
                    return false;
                }
            } else if (ns instanceof ClassifierRole) {
                ClassifierRole cr = (ClassifierRole) ns;
                if (!(cr.getAvailableContents().contains(modelElement) || cr.
                        getAvailableFeature().contains(modelElement))) {
                    return false;
                }
            }
            if (modelElement instanceof StructuralFeature) {
                if (!isValidNamespace((StructuralFeature) modelElement, ns)) {
                    return false;
                }
            } else if (modelElement instanceof GeneralizableElement) {
                GeneralizableElement ge = (GeneralizableElement) modelElement;
                if (!isValidNamespace(ge, ns)) {
                    return false;
                }
            } else if (modelElement instanceof Generalization) {
                if (!isValidNamespace((Generalization) modelElement, ns)) {
                    return false;
                }
            }
            if (modelElement instanceof UmlAssociation) {
                if (!isValidNamespace((UmlAssociation) modelElement, ns)) {
                    return false;
                }
            } else if (modelElement instanceof Collaboration) {
                if (!isValidNamespace((Collaboration) modelElement, ns)) {
                    return false;
                }
            }
            return true;
        } catch (InvalidObjectException e) {
            return false;
        }
    }

    /**
     * The base of a AssociationRole or ClassifierRole should be contained in
     * the given Namespace. If no base is set (yet), then allow any namespace.
     *  
     * @param collab the given collaboration
     * @param ns the given candidate namespace
     * @return true if the given namespace may contain the collaboration
     */
    private boolean isValidNamespace(Collaboration collab, Namespace ns) {
        Iterator it = collab.getOwnedElement().iterator();
        while (it.hasNext()) {
            ModelElement m = (ModelElement) it.next();
            if (m instanceof ClassifierRole) {
                ClassifierRole role = (ClassifierRole) m;
                Iterator it2 = role.getBase().iterator();
                while (it2.hasNext()) {
                    if (!ns.getOwnedElement().contains(it2.next())) {
                        return false;
                    }
                }
            } else if (m instanceof AssociationRole) {
                AssociationRole ar = (AssociationRole) m;
                UmlAssociation a = ar.getBase();
                if (a != null && !ns.getOwnedElement().contains(a)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValidNamespace(Generalization gen, Namespace ns) {
        if (gen.getParent() == null || gen.getChild() == null) {
            return true;
        }
        Namespace ns1 = gen.getParent().getNamespace();
        Namespace ns2 = gen.getChild().getNamespace();
        if (ns == getFirstSharedNamespace(ns1, ns2)) {
            return true;
        }
        return false;
    }

    private boolean isValidNamespace(StructuralFeature struc, Namespace ns) {
        if (struc.getType() == null || struc.getOwner() == null) {
            return true;
        }
        // TODO: This is a tautology.  Won't it always be true? - tfm
        // why aren't we checking the Namespace parameter?
        // Note: this matches the NSUML implementation
        return struc.getOwner().getNamespace().getOwnedElement().contains(
                struc.getType());
    }

    private boolean isValidNamespace(UmlAssociation assoc, Namespace ns) {
        Iterator it = assoc.getConnection().iterator();
        List namespaces = new ArrayList();
        while (it.hasNext()) {
            AssociationEnd end = (AssociationEnd) it.next();
            namespaces.add(end.getParticipant().getNamespace());
        }
        it = namespaces.iterator();
        while (it.hasNext()) {
            Namespace ns1 = (Namespace) it.next();
            if (it.hasNext()) {
                Namespace ns2 = (Namespace) it.next();
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

    private boolean isValidNamespace(GeneralizableElement gen, Namespace ns) {
        Iterator it =
            modelImpl.getUmlPackage().getCore()
                .getAChildGeneralization().getGeneralization(gen).iterator();
        while (it.hasNext()) {
            Generalization gen2 = (Generalization) it.next();
            /* TODO: Fix the following problem, as described in issue 3772:
             * Both implementations for valid namespace check whether 
             * the parents are owned by the namespace. This is invalid. 
             * The constraint 
             * [4] The parent must be included in the Namespace 
             * of the GeneralizableElement.self.generalization->forAll(g |
             * self.namespace.allContents->includes(g.parent) )
             * only asks that they are included, 
             * that is there can also be an elementimport 
             * at work somewhere. (same as in java - you can also use 
             * an import and then generalize, without the classes being 
             * required to be located in the same package). 
             * Symptom of this problem:
             * Load the project attached to issue 3772. Select the "class1".
             * The UMLModelElementNamespaceComboBoxModel gives 
             * a warning.*/
            if (!ns.getOwnedElement().contains(gen2.getParent())) {
                return false;
            }
        }
        return true;
    }

    /**
     * @see org.argouml.model.CoreHelper#getFirstSharedNamespace(
     *      java.lang.Object, java.lang.Object)
     */
    public Object getFirstSharedNamespace(Object ns1, Object ns2) {
        if (ns1 == null || ns2 == null) {
            throw new IllegalArgumentException("null argument");
        }
        if (ns1 == ns2) {
            return ns1;
        }
        if (!(ns1 instanceof Namespace)) {
            throw new IllegalArgumentException(
                    "Expecting a Namespace argument. Got a "
                            + ns1.getClass().getName());
        }
        if (!(ns2 instanceof Namespace)) {
            throw new IllegalArgumentException(
                    "Expecting a Namespace argument. Got a "
                            + ns2.getClass().getName());
        }

        ModelManagementHelper mmh = modelImpl.getModelManagementHelper();
        boolean ns1Owner = mmh.getAllNamespaces(ns1).contains(ns2);
        boolean ns2Owner = mmh.getAllNamespaces(ns2).contains(ns1);
        if (ns1Owner) {
            return ns1;
        }
        if (ns2Owner) {
            return ns2;
        }
        return getFirstSharedNamespace(((ModelElement) ns1).getNamespace(),
                ((ModelElement) ns2).getNamespace());
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllPossibleNamespaces(
     *      java.lang.Object, java.lang.Object)
     */
    public Collection getAllPossibleNamespaces(Object modelElement,
            Object model) {
        ModelElement m = (ModelElement) modelElement;
        List ret = new ArrayList();
        if (m == null) {
            return ret;
        }
        if (isValidNamespace(m, model)) {
            ret.add(model);
        }
        Iterator it =
            modelImpl.getModelManagementHelper()
                .getAllModelElementsOfKind(model, Namespace.class).iterator();
        while (it.hasNext()) {
            Namespace ns = (Namespace) it.next();
            if (isValidNamespace(m, ns)) {
                ret.add(ns);
            }
        }
        return ret;
    }

    /**
     * @see org.argouml.model.CoreHelper#getChildren(java.lang.Object)
     */
    public Collection getChildren(Object o) {
        if (o instanceof GeneralizableElement) {
            Collection col = new ArrayList();
            Collection generalizations = new ArrayList();
            if (o instanceof GeneralizableElement) {
                Iterator it =
                    Model.getFacade().getSpecializations(o).iterator();
                while (it.hasNext()) {
                    getChildren(col, (Generalization) it.next(),
                            generalizations);
                }
            }
            return col;
        }
        throw new IllegalArgumentException("Illegal arguments: " + o);
    }

    /**
     * Adds all children recursively to the Collection in the first argument.
     * The algorithm assumes that there is a cycle when a node has been visited
     * twice using already known generalizations.
     *
     * @param currentChildren
     *            collection to collect them in.
     * @param gen
     *            element whose children are added.
     * @param generalizations
     *            the list of already traversed generalizations.
     * @throws IllegalStateException
     *             if there is a circular reference.
     */
    private void getChildren(Collection currentChildren, Generalization gen,
            Collection generalizations) {
        GeneralizableElement child = gen.getChild();
        if (currentChildren.contains(child) && generalizations.contains(gen)) {
            throw new IllegalStateException("Circular inheritance occured.");
        } else {
            currentChildren.add(child);
            generalizations.add(gen);
        }
        Iterator it = Model.getFacade().getSpecializations(child).iterator();
        while (it.hasNext()) {
            getChildren(currentChildren, (Generalization) it.next(),
                    generalizations);
        }
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllRealizedInterfaces(
     *      java.lang.Object)
     */
    public Collection getAllRealizedInterfaces(Object o) {
        return internalGetAllRealizedInterfaces(o, new ArrayList(),
                new HashSet());
    }

    /**
     * Helper method for getAllRealizedInterfaces.
     *
     * @param o
     * @param col
     * @param visited
     * @return Collection
     */
    private Collection internalGetAllRealizedInterfaces(Object o,
            Collection col, Set visited) {
        visited.add(o);
        if (o != null) {
            if (o instanceof UmlClass) {
                UmlClass clazz = (UmlClass) o;
                Collection supDependencies = clazz.getClientDependency();
                Iterator it = supDependencies.iterator();
                while (it.hasNext()) {
                    Dependency dependency = (Dependency) it.next();
                    Stereotype stereo =
                        (Stereotype) getFirstItemOrNull(
                                dependency.getStereotype());
                    if (dependency instanceof Abstraction && stereo != null
                            && "realize".equals(stereo.getName())
                            && "Abstraction".equals(stereo.getBaseClass())) {

                        col.addAll(dependency.getSupplier());

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
     * @see org.argouml.model.CoreHelper#hasCompositeEnd(java.lang.Object)
     */
    public final boolean hasCompositeEnd(Object association) {
        if (!(association instanceof UmlAssociation)) {
            throw new IllegalArgumentException();
        }

        UmlAssociation association1 = (UmlAssociation) association;

        List ends = association1.getConnection();
        for (Iterator iter = ends.iterator(); iter.hasNext();) {
            AssociationEnd end = (AssociationEnd) iter.next();
            if (end.getAggregation() == AggregationKindEnum.AK_COMPOSITE) {
                return true;
            }
        }
        return false;
    }

    /**
     * @see org.argouml.model.CoreHelper#equalsAggregationKind(
     *      java.lang.Object, java.lang.String)
     */
    public final boolean equalsAggregationKind(Object associationEnd,
            String kindType) {
        if (!(associationEnd instanceof AssociationEnd)) {
            throw new IllegalArgumentException();
        }

        AssociationEnd associationEnd1 = (AssociationEnd) associationEnd;

        if (kindType.equals("composite")) {
            return AggregationKindEnum.AK_COMPOSITE.equals(associationEnd1.
                    getAggregation());
        } else {
            throw new IllegalArgumentException("kindType: " + kindType
                    + " not supported");
        }
    }

    /**
     * @see org.argouml.model.CoreHelper#removeAnnotatedElement(
     *      java.lang.Object, java.lang.Object)
     */
    public void removeAnnotatedElement(Object handle, Object me) {
        if (handle instanceof Comment && me instanceof ModelElement) {
            ((Comment) handle).getAnnotatedElement().remove(me);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or model element: " + me);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeClientDependency(
     *      java.lang.Object, java.lang.Object)
     */
    public void removeClientDependency(Object handle, Object dep) {
        if (handle instanceof ModelElement && dep instanceof Dependency) {
            ModelElement me = (ModelElement) handle;
            Collection deps = me.getClientDependency();
            if (deps != null && deps.contains(dep)) {
                deps.remove(dep);
            }
            return;
        }
        throw new IllegalArgumentException();
    }

    /**
     * @see org.argouml.model.CoreHelper#removeConnection( java.lang.Object,
     *      java.lang.Object)
     */
    public void removeConnection(Object handle, Object connection) {
        if (handle instanceof UmlAssociation
                && connection instanceof AssociationEnd) {
            ((UmlAssociation) handle).getConnection().remove(connection);
            return;
        }
        if (handle instanceof Link && connection instanceof LinkEnd) {
            ((Link) handle).getConnection().remove(connection);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or connection: " + connection);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeConstraint( java.lang.Object,
     *      java.lang.Object)
     */
    public void removeConstraint(Object handle, Object cons) {
        if (handle instanceof ModelElement && cons instanceof Constraint) {
            ((ModelElement) handle).getConstraint().remove(cons);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or cons: "
                + cons);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeOwnedElement( java.lang.Object,
     *      java.lang.Object)
     */
    public void removeOwnedElement(Object handle, Object value) {
        if (handle instanceof Namespace && value instanceof ModelElement) {
            ((Namespace) handle).getOwnedElement().remove(value);
            ((ModelElement) value).setNamespace(null);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or value: "
                + value);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeParameter( java.lang.Object,
     *      java.lang.Object)
     */
    public void removeParameter(Object handle, Object parameter) {
        if (parameter instanceof Parameter) {
            if (handle instanceof ObjectFlowState) {
                ((ObjectFlowState) handle).getParameter().remove(parameter);
                return;
            }
            if (handle instanceof Event) {
                ((Event) handle).getParameter().remove(parameter);
                return;
            }
            if (handle instanceof BehavioralFeature) {
                ((BehavioralFeature) handle).getParameter().remove(parameter);
                return;
            }
            if (handle instanceof Classifier) {
                ((Parameter) parameter).setType(null);
            }
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or parameter: " + parameter);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeSourceFlow( java.lang.Object,
     *      java.lang.Object)
     */
    public void removeSourceFlow(Object handle, Object flow) {
        if (handle instanceof ModelElement && flow instanceof Flow) {
            ((ModelElement) handle).getSourceFlow().remove(flow);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or flow: "
                + flow);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeSupplierDependency(
     *      java.lang.Object, java.lang.Object)
     */
    public void removeSupplierDependency(Object supplier, Object dependency) {
        if (supplier instanceof ModelElement
                && dependency instanceof Dependency) {
            ((Dependency) dependency).getSupplier().remove(supplier);
            return;
        }
        throw new IllegalArgumentException("supplier: " + supplier
                + " or dependency: " + dependency);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeTaggedValue( java.lang.Object,
     *      java.lang.String)
     */
    public void removeTaggedValue(Object handle, String name) {
        Object taggedValue = Model.getFacade().getTaggedValue(handle, name);
        if (taggedValue != null) {
            modelImpl.getExtensionMechanismsHelper().removeTaggedValue(handle,
                    taggedValue);
        }
    }

    /**
     * @see org.argouml.model.CoreHelper#removeTargetFlow( java.lang.Object,
     *      java.lang.Object)
     */
    public void removeTargetFlow(Object handle, Object flow) {
        if (handle instanceof ModelElement && flow instanceof Flow) {
            ((ModelElement) handle).getTargetFlow().remove(flow);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or flow: "
                + flow);
    }

    /**
     * @see org.argouml.model.CoreHelper#addAnnotatedElement( java.lang.Object,
     *      java.lang.Object)
     */
    public void addAnnotatedElement(Object comment, Object annotatedElement) {
        if (comment instanceof Comment
                && annotatedElement instanceof ModelElement) {
            ((Comment) comment).getAnnotatedElement().add(annotatedElement);
            return;
        }
        throw new IllegalArgumentException("comment: " + comment
                + " or annotatedElement: " + annotatedElement);
    }

    /**
     * @see org.argouml.model.CoreHelper#addClient( java.lang.Object,
     *      java.lang.Object)
     */
    public void addClient(Object handle, Object element) {
        if (handle instanceof Dependency && element instanceof ModelElement) {
            ((Dependency) handle).getClient().add(element);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or element: " + element);
    }

    /**
     * @see org.argouml.model.CoreHelper#addClientDependency( java.lang.Object,
     *      java.lang.Object)
     */
    public void addClientDependency(Object handle, Object dependency) {
        if (handle instanceof ModelElement
                && dependency instanceof Dependency) {
            ((ModelElement) handle).getClientDependency().add(dependency);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or dependency: " + dependency);
    }

    /**
     * @see org.argouml.model.CoreHelper#addComment( java.lang.Object,
     *      java.lang.Object)
     */
    public void addComment(Object element, Object comment) {
        if (element instanceof ModelElement && comment instanceof Comment) {
            ((ModelElement) element).getComment().add(comment);
            return;
        }
        throw new IllegalArgumentException("element: " + element);
    }

    /**
     * @see org.argouml.model.CoreHelper#addConnection( java.lang.Object,
     *      java.lang.Object)
     */
    public void addConnection(Object handle, Object connection) {
        if (handle instanceof UmlAssociation
                && connection instanceof AssociationEnd) {
            ((UmlAssociation) handle).getConnection().add(connection);
            return;
        }
        if (handle instanceof Link && connection instanceof LinkEnd) {
            ((Link) handle).getConnection().add(connection);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or connection: " + connection);
    }

    /**
     * @see org.argouml.model.CoreHelper#addConstraint( java.lang.Object,
     *      java.lang.Object)
     */
    public void addConstraint(Object handle, Object mc) {
        if (handle instanceof ModelElement && mc instanceof Constraint) {
            ((ModelElement) handle).getConstraint().add(mc);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or mc: "
                + mc);
    }

    /**
     * @see org.argouml.model.CoreHelper#addDeploymentLocation(
     *      java.lang.Object, java.lang.Object)
     */
    public void addDeploymentLocation(Object handle, Object node) {
        if (handle instanceof Component && node instanceof Node) {
            ((Component) handle).getDeploymentLocation().add(node);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or node: "
                + node);
    }

    /**
     * @see org.argouml.model.CoreHelper#addElementResidence(java.lang.Object,
     *      java.lang.Object)
     */
    public void addElementResidence(Object handle, Object node) {
        // TODO: This is ambiguous as to whether it should be adding a container
        // or resident.
        if (handle instanceof ModelElement
                && node instanceof ElementResidence) {
            ((ElementResidence) node).setResident((ModelElement) handle);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or node: "
                + node);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeElementResidence(java.lang.Object,
     *      java.lang.Object)
     */
    public void removeElementResidence(Object handle, Object node) {
        if (handle instanceof ModelElement
                && node instanceof ElementResidence) {
            ((ElementResidence) node).setResident(null);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or node: "
                + node);
    }

    /**
     * Get ElementResidences.
     *
     * TODO: Unused.  Should be added to Facade? - tfm
     *
     * @param handle A modelElement
     * @return Collection The ElementResidence for this model element
     */
    public Collection getElementResidence(Object handle) {
        if (handle instanceof ModelElement) {
            return modelImpl.getUmlPackage().getCore().
                    getAResidentElementResidence().getElementResidence(
                            (ModelElement) handle);
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#addFeature( java.lang.Object, int,
     *      java.lang.Object)
     */
    public void addFeature(Object handle, int index, Object f) {
        if (handle instanceof Classifier && f instanceof Feature) {
            ((Classifier) handle).getFeature().add(index, f);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or f: " + f);
    }

    /**
     * @see org.argouml.model.CoreHelper#addFeature( java.lang.Object,
     *      java.lang.Object)
     */
    public void addFeature(Object handle, Object f) {
        if (handle instanceof Classifier && f instanceof Feature) {
            ((Classifier) handle).getFeature().add(f);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#addLink( java.lang.Object,
     *      java.lang.Object)
     */
    public void addLink(Object handle, Object link) {
        if (handle instanceof UmlAssociation && link instanceof Link) {
            ((Link) link).setAssociation((UmlAssociation) handle);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or link: "
                + link);
    }

    /**
     * @see org.argouml.model.CoreHelper#addMethod( java.lang.Object,
     *      java.lang.Object)
     */
    public void addMethod(Object handle, Object m) {
        if (handle instanceof Operation && m instanceof Method) {
            ((Method) m).setVisibility(((Operation) handle).getVisibility());
            ((Method) m).setOwnerScope(((Operation) handle).getOwnerScope());
            ((Method) m).setSpecification((Operation) handle);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or m: " + m);
    }

    /**
     * @see org.argouml.model.CoreHelper#addOwnedElement( java.lang.Object,
     *      java.lang.Object)
     */
    public void addOwnedElement(Object handle, Object me) {
        if (handle instanceof Namespace && me instanceof ModelElement) {
            Namespace ns = (Namespace) handle;
            ModelElement elem = (ModelElement) me;
            elem.setNamespace(ns);
            ns.getOwnedElement().add(elem);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or me: "
                + me);
    }

    /**
     * @see org.argouml.model.CoreHelper#addParameter( java.lang.Object, int,
     *      java.lang.Object)
     */
    public void addParameter(Object handle, int index, Object parameter) {
        if (parameter instanceof Parameter) {
            if (handle instanceof Event) {
                ((Event) handle).getParameter().add(index, parameter);
                return;
            }
            if (handle instanceof BehavioralFeature) {
                ((BehavioralFeature) handle).getParameter().add(index,
                        parameter);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or parameter: " + parameter);
    }

    /**
     * @see org.argouml.model.CoreHelper#addParameter( java.lang.Object,
     *      java.lang.Object)
     */
    public void addParameter(Object handle, Object parameter) {
        if (parameter instanceof Parameter) {
            if (handle instanceof ObjectFlowState) {
                ((ObjectFlowState) handle).getParameter().add(parameter);
                return;
            }
            if (handle instanceof Event) {
                ((Event) handle).getParameter().add(parameter);
                return;
            }
            if (handle instanceof BehavioralFeature) {
                ((BehavioralFeature) handle).getParameter().add(parameter);
                return;
            }
            if (handle instanceof Classifier) {
                modelImpl.getUmlPackage().getCore().getATypedParameterType().
                        add((Parameter) parameter, (Classifier) handle);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or parameter: " + parameter);
    }

    /**
     * @see org.argouml.model.CoreHelper#addRaisedSignal( java.lang.Object,
     *      java.lang.Object)
     */
    public void addRaisedSignal(Object handle, Object sig) {
        if (sig instanceof Signal) {
            if (handle instanceof Message) {
                modelImpl.getUmlPackage().getCommonBehavior().
                        getAContextRaisedSignal().add(
                                (BehavioralFeature) handle, (Signal) sig);
                return;
            }
            if (handle instanceof Operation) {
                modelImpl.getUmlPackage().getCommonBehavior().
                        getAContextRaisedSignal().add(
                                (BehavioralFeature) handle, (Signal) sig);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle + " or sig: "
                + sig);
    }

    /**
     * @see org.argouml.model.CoreHelper#addSourceFlow( java.lang.Object,
     *      java.lang.Object)
     */
    public void addSourceFlow(Object handle, Object flow) {
        if (handle instanceof ModelElement && flow instanceof Flow) {
            ((ModelElement) handle).getSourceFlow().add(flow);
            return;
        }

        throw new IllegalArgumentException("handle: " + handle + " or flow: "
                + flow);
    }

    /**
     * @see org.argouml.model.CoreHelper#addSupplier( java.lang.Object,
     *      java.lang.Object)
     */
    public void addSupplier(Object handle, Object element) {
        if (handle instanceof Dependency && element instanceof ModelElement) {
            ((Dependency) handle).getSupplier().add(element);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or element: " + element);
    }

    /**
     * @see org.argouml.model.CoreHelper#addSupplierDependency(
     *      java.lang.Object, java.lang.Object)
     */
    public void addSupplierDependency(Object supplier, Object dependency) {
        if (supplier instanceof ModelElement
                && dependency instanceof Dependency) {
            ((Dependency) dependency).getSupplier().add(supplier);
            return;
        }
        throw new IllegalArgumentException("supplier: " + supplier
                + " or dependency: " + dependency);
    }

    /**
     * @see org.argouml.model.CoreHelper#addTaggedValue( java.lang.Object,
     *      java.lang.Object)
     */
    public void addTaggedValue(Object handle, Object taggedValue) {
        modelImpl.getExtensionMechanismsHelper().addTaggedValue(handle,
                taggedValue);
    }

    /**
     * @see org.argouml.model.CoreHelper#addTargetFlow( java.lang.Object,
     *      java.lang.Object)
     */
    public void addTargetFlow(Object handle, Object flow) {
        if (handle instanceof ModelElement && flow instanceof Flow) {
            ((ModelElement) handle).getTargetFlow().add(flow);
            return;
        }

        throw new IllegalArgumentException("handle: " + handle + " or flow: "
                + flow);
    }

    /**
     * @see org.argouml.model.CoreHelper#setAbstract(java.lang.Object, boolean)
     */
    public void setAbstract(Object handle, boolean flag) {
        if (handle instanceof GeneralizableElement) {
            ((GeneralizableElement) handle).setAbstract(flag);
            return;
        }
        if (handle instanceof Operation) {
            ((Operation) handle).setAbstract(flag);
            return;
        }
        if (handle instanceof Reception) {
            ((Reception) handle).setAbstract(flag);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#setActive(java.lang.Object, boolean)
     */
    public void setActive(Object handle, boolean active) {
        if (handle instanceof UmlClass) {
            ((UmlClass) handle).setActive(active);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#setAggregation( java.lang.Object,
     *      java.lang.Object)
     */
    public void setAggregation(Object handle, Object aggregationKind) {
        if (handle instanceof AssociationEnd
                && aggregationKind instanceof AggregationKind) {
            ((AssociationEnd) handle).
                    setAggregation((AggregationKind) aggregationKind);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or aggregationKind: " + aggregationKind);
    }

    /**
     * @see org.argouml.model.CoreHelper#setAnnotatedElements( java.lang.Object,
     *      java.util.Collection)
     */
    public void setAnnotatedElements(Object handle, Collection elems) {
        if (handle instanceof Comment && elems instanceof List) {
            Comment comment = (Comment) handle;
            comment.getAnnotatedElement().clear();
            comment.getAnnotatedElement().addAll(elems);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#setAssociation( java.lang.Object,
     *      java.lang.Object)
     */
    public void setAssociation(Object handle, Object association) {
        if (association instanceof UmlAssociation) {
            if (handle instanceof AssociationEnd) {
                ((AssociationEnd) handle).
                        setAssociation((UmlAssociation) association);
                return;
            }
            if (handle instanceof Link) {
                ((Link) handle).setAssociation((UmlAssociation) association);
                return;
            }
        } else if (association instanceof AssociationRole) {
            if (handle instanceof AssociationEndRole) {
                ((AssociationEndRole) handle).
                        setAssociation((AssociationRole) association);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or association: " + association);
    }

    /**
     * @see org.argouml.model.CoreHelper#setLeaf(java.lang.Object, boolean)
     */
    public void setLeaf(Object handle, boolean flag) {
        if (handle instanceof Reception) {
            ((Reception) handle).setLeaf(flag);
            return;
        }
        if (handle instanceof Operation) {
            ((Operation) handle).setLeaf(flag);
            return;
        }
        if (handle instanceof GeneralizableElement) {
            ((GeneralizableElement) handle).setLeaf(flag);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#setRaisedSignals( java.lang.Object,
     *      java.util.Collection)
     */
    public void setRaisedSignals(Object handle, Collection raisedSignals) {
        throw new NotImplementedException();
    }

    /**
     * @see org.argouml.model.CoreHelper#setBody( java.lang.Object,
     *      java.lang.Object)
     */
    public void setBody(Object handle, Object expr) {
        if (handle instanceof Method
                && (expr == null || expr instanceof ProcedureExpression)) {
            ((Method) handle).setBody((ProcedureExpression) expr);
            return;
        }

        if (handle instanceof Constraint
                && (expr == null || expr instanceof BooleanExpression)) {
            ((Constraint) handle).setBody((BooleanExpression) expr);
            return;
        }

        throw new IllegalArgumentException("handle: " + handle + " or expr: "
                + expr);
    }

    /**
     * @see org.argouml.model.CoreHelper#setChangeability( java.lang.Object,
     *      java.lang.Object)
     */
    public void setChangeability(Object handle, Object ck) {
        if (ck == null || ck instanceof ChangeableKind) {
            ChangeableKind changeableKind = (ChangeableKind) ck;

            if (handle instanceof StructuralFeature) {
                ((StructuralFeature) handle).setChangeability(changeableKind);
                return;
            }
            if (handle instanceof AssociationEnd) {
                ((AssociationEnd) handle).setChangeability(changeableKind);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle + " or ck: "
                + ck);
    }

    /**
     * @see org.argouml.model.CoreHelper#setChangeable( java.lang.Object,
     *      boolean)
     */
    public void setChangeable(Object handle, boolean flag) {
        ChangeableKindEnum changeable =
            flag ? ChangeableKindEnum.CK_CHANGEABLE
                : ChangeableKindEnum.CK_FROZEN;
        if (handle instanceof StructuralFeature) {
            ((StructuralFeature) handle).setChangeability(changeable);
            return;
        } else if (handle instanceof AssociationEnd) {
            ((AssociationEnd) handle).setChangeability(changeable);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#setChild( java.lang.Object,
     *      java.lang.Object)
     */
    public void setChild(Object handle, Object child) {
        if (handle instanceof Generalization) {
            ((Generalization) handle).setChild((GeneralizableElement) child);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or child: "
                + child);
    }

    /**
     * @see org.argouml.model.CoreHelper#setConcurrency( java.lang.Object,
     *      java.lang.Object)
     */
    public void setConcurrency(Object handle, Object concurrencyKind) {
        if (handle instanceof Operation
                && concurrencyKind instanceof CallConcurrencyKind) {
            ((Operation) handle).
                    setConcurrency((CallConcurrencyKind) concurrencyKind);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or concurrencyKind: " + concurrencyKind);
    }

    /**
     * @see org.argouml.model.CoreHelper#setConnections( java.lang.Object,
     *      java.util.Collection)
     */
    public void setConnections(Object handle, Collection elems) {
        if (handle instanceof UmlAssociation && elems instanceof List) {
            ((UmlAssociation) handle).getConnection().clear();
            ((UmlAssociation) handle).getConnection().addAll(elems);
            return;
        }
        if (handle instanceof Link && elems instanceof List) {
            ((Link) handle).getConnection().clear();
            ((Link) handle).getConnection().addAll(elems);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#setDefaultValue( java.lang.Object,
     *      java.lang.Object)
     */
    public void setDefaultValue(Object handle, Object expr) {
        if (handle instanceof Parameter && expr instanceof Expression) {
            ((Parameter) handle).setDefaultValue((Expression) expr);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or expr: "
                + expr);
    }

    /**
     * @see org.argouml.model.CoreHelper#setDiscriminator( java.lang.Object,
     *      java.lang.String)
     */
    public void setDiscriminator(Object handle, String discriminator) {
        if (handle instanceof Generalization) {
            ((Generalization) handle).setDiscriminator(discriminator);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#setFeature( java.lang.Object, int,
     *      java.lang.Object)
     */
    public void setFeature(Object elem, int i, Object impl) {
        if (elem instanceof Classifier && impl instanceof Feature) {
            ((Classifier) elem).getFeature().add(i, impl);
            return;
        }

        throw new IllegalArgumentException("elem: " + elem + " or impl: "
                + impl);
    }

    /**
     * @see org.argouml.model.CoreHelper#setFeatures( java.lang.Object,
     *      java.util.Collection)
     */
    public void setFeatures(Object handle, Collection features) {
        if (handle instanceof Classifier) {
            List featuresList = null;
            if (features instanceof List) {
                featuresList = (List) features;
            } else {
                featuresList = new ArrayList(features);
            }
            ((Classifier) handle).getFeature().clear();
            ((Classifier) handle).getFeature().addAll(featuresList);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#setImplementationLocation(java.lang.Object, java.lang.Object)
     */
    public void setImplementationLocation(Object handle, Object component) {
        setContainer(handle, component);
    }

    /**
     * @see org.argouml.model.CoreHelper#setContainer(java.lang.Object, java.lang.Object)
     */
    public void setContainer(Object handle, Object container) {
        if (handle instanceof ElementResidence
                && (container instanceof Component || container == null)) {
            ElementResidence er = (ElementResidence) handle;
            er.setContainer((Component) container);
        } else {
            throw new IllegalArgumentException("handle: " + handle
                    + " or container: " + container);
        }
    }

    /**
     * @see org.argouml.model.CoreHelper#setInitialValue( java.lang.Object,
     *      java.lang.Object)
     */
    public void setInitialValue(Object at, Object expr) {
        if (at instanceof Attribute
                && (expr == null || expr instanceof Expression)) {
            ((Attribute) at).setInitialValue((Expression) expr);
            return;
        }
        throw new IllegalArgumentException("at: " + at + " or expr: " + expr);
    }

    /**
     * @see org.argouml.model.CoreHelper#setKind( java.lang.Object,
     *      java.lang.Object)
     */
    public void setKind(Object handle, Object kind) {
        if (handle instanceof Parameter
                && kind instanceof ParameterDirectionKind) {
            ((Parameter) handle).setKind((ParameterDirectionKind) kind);
            return;
        }
        if (handle instanceof Pseudostate && kind instanceof PseudostateKind) {
            ((Pseudostate) handle).setKind((PseudostateKind) kind);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or kind: "
                + kind);
    }

    /**
     * @see org.argouml.model.CoreHelper#setKindToIn(java.lang.Object)
     */
    public void setKindToIn(Object handle) {
        if (handle instanceof Parameter) {
            ((Parameter) handle).setKind(ParameterDirectionKindEnum.PDK_IN);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#setKindToInOut(java.lang.Object)
     */
    public void setKindToInOut(Object handle) {
        if (handle instanceof Parameter) {
            ((Parameter) handle).setKind(ParameterDirectionKindEnum.PDK_INOUT);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#setKindToOut(java.lang.Object)
     */
    public void setKindToOut(Object handle) {
        if (handle instanceof Parameter) {
            ((Parameter) handle).setKind(ParameterDirectionKindEnum.PDK_OUT);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#setKindToReturn(java.lang.Object)
     */
    public void setKindToReturn(Object handle) {
        if (handle instanceof Parameter) {
            ((Parameter) handle).setKind(ParameterDirectionKindEnum.PDK_RETURN);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#setModelElementContainer(
     *      java.lang.Object, java.lang.Object)
     */
    public void setModelElementContainer(Object handle, Object container) {
        /*
         * <em>Warning: when changing the implementation of this method be
         * warned that the sequence of the if then else tree DOES matter.</em>
         * Most notabely, do not move the setNamespace method any level up in
         * the tree.<p>
         */
        if (handle instanceof Partition && container instanceof ActivityGraph) {
            ((Partition) handle).setActivityGraph((ActivityGraph) container);
        } else if (handle instanceof ModelElement
                && container instanceof Partition) {
            ((Partition) container).getContents().add(handle);
        } else if (handle instanceof Constraint
                && container instanceof Stereotype) {
            ((Stereotype) container).getStereotypeConstraint().add(handle);
        } else if (handle instanceof Interaction
                && container instanceof Collaboration) {
            ((Interaction) handle).setContext((Collaboration) container);
        } else if (handle instanceof ElementResidence
                && (container instanceof Component || container == null)) {
            ElementResidence er = (ElementResidence) handle;
            er.setContainer((Component) container);
        } else if (handle instanceof AttributeLink
                && container instanceof Instance) {
            ((AttributeLink) handle).setInstance((Instance) container);
        } else if (handle instanceof Message
                && container instanceof Interaction) {
            ((Message) handle).setInteraction((Interaction) container);
        } else if (handle instanceof LinkEnd && container instanceof Link) {
            ((LinkEnd) handle).setLink((Link) container);
        } else if (handle instanceof AttributeLink
                && container instanceof LinkEnd) {
            ((AttributeLink) handle).setLinkEnd((LinkEnd) container);
        } else if (handle instanceof TaggedValue
                && container instanceof Stereotype) {
            ((TaggedValue) handle).getStereotype().clear();
            if (container != null) {
                ((TaggedValue) handle).getStereotype().add(container);
            }
        } else if (handle instanceof TaggedValue
                && container instanceof ModelElement) {
            ((TaggedValue) handle).setModelElement((ModelElement) container);
        } else if (handle instanceof StateVertex
                && container instanceof CompositeState) {
            ((StateVertex) handle).setContainer((CompositeState) container);
        } else if (handle instanceof ElementImport
                && container instanceof UmlPackage) {
            ((ElementImport) handle).setUmlPackage((UmlPackage) container);
        } else if (handle instanceof Transition && container instanceof State) {
            ((State) container).getInternalTransition().add(handle);
        } else if (handle instanceof State
                && container instanceof StateMachine) {
            ((State) handle).setStateMachine((StateMachine) container);
        } else if (handle instanceof Transition
                && container instanceof StateMachine) {
            ((Transition) handle).setStateMachine((StateMachine) container);
        } else if (handle instanceof Action
                && container instanceof Transition) {
            ((Transition) container).setEffect((Action) handle);
        } else if (handle instanceof Guard && container instanceof Transition) {
            ((Guard) handle).setTransition((Transition) container);
        } else if (handle instanceof ModelElement
                && container instanceof Namespace) {
            ((ModelElement) handle).setNamespace((Namespace) container);
        } else {
            throw new IllegalArgumentException("handle: " + handle
                    + " or container: " + container);
        }
    }

    /**
     * @see org.argouml.model.CoreHelper#setMultiplicity( java.lang.Object,
     *      java.lang.Object)
     */
    public void setMultiplicity(Object handle, Object arg) {
        if (arg instanceof String) {
            // TODO: We have multiple string representations for multiplicities
            // these should be consolidated. This form is used by
            // org.argouml.uml.reveng
            if ("1_N".equals(arg)) {
                arg =
                    modelImpl.getDataTypesFactory().createMultiplicity(1, -1);
            } else {
                arg = modelImpl.getDataTypesFactory().createMultiplicity(1, 1);
            }
        }

        if (arg instanceof Multiplicity) {
            Multiplicity mult = (Multiplicity) arg;

            if (handle instanceof AssociationRole) {
                ((AssociationRole) handle).setMultiplicity(mult);
                return;
            }
            if (handle instanceof ClassifierRole) {
                ((ClassifierRole) handle).setMultiplicity(mult);
                return;
            }
            if (handle instanceof StructuralFeature) {
                ((StructuralFeature) handle).setMultiplicity(mult);
                return;
            }
            if (handle instanceof AssociationEnd) {
                ((AssociationEnd) handle).setMultiplicity(mult);
                return;
            }
            if (handle instanceof TagDefinition) {
                ((TagDefinition) handle).setMultiplicity(mult);
                return;
            }
        }

        throw new IllegalArgumentException("handle: " + handle + " or arg: "
                + arg);
    }

    /**
     * @see org.argouml.model.CoreHelper#setName(java.lang.Object,
     *      java.lang.String)
     */
    public void setName(Object handle, String name) {
        if ((handle instanceof ModelElement) && (name != null)) {
            ((ModelElement) handle).setName(name);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or name: "
                + name);
    }

    /**
     * @see org.argouml.model.CoreHelper#setBody(java.lang.Object,
     *      java.lang.String)
     */
    public void setBody(Object handle, String body) {
        if ((handle instanceof Comment) && (body != null)) {
            // All text was stored in name field in UML 1.3
            ((Comment) handle).setBody(body);
        }
    }

    /**
     * @see org.argouml.model.CoreHelper#setNamespace( java.lang.Object,
     *      java.lang.Object)
     */
    public void setNamespace(Object handle, Object ns) {
        if (handle instanceof ModelElement
                && (ns == null || ns instanceof Namespace)) {
            Namespace oldNs = ((ModelElement) handle).getNamespace();
            if (((ns == null) && (oldNs == null))
                    || ((oldNs != null) && oldNs.equals(ns))) {
                return;
            }
            ((ModelElement) handle).setNamespace((Namespace) ns);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or ns: "
                + ns);
    }

    /**
     * @see org.argouml.model.CoreHelper#setNavigable(java.lang.Object, boolean)
     */
    public void setNavigable(Object handle, boolean flag) {
        if (handle instanceof AssociationEnd) {
            ((AssociationEnd) handle).setNavigable(flag);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#setOrdering( java.lang.Object,
     *      java.lang.Object)
     */
    public void setOrdering(Object handle, Object ok) {
        if (handle instanceof AssociationEnd && ok instanceof OrderingKind) {
            ((AssociationEnd) handle).setOrdering((OrderingKind) ok);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or ok: "
                + ok);
    }

    /**
     * @see org.argouml.model.CoreHelper#setOwner( java.lang.Object,
     *      java.lang.Object)
     *
     * only one of owner & namespace may be specified, so we clear namespace
     */
    public void setOwner(Object handle, Object owner) {
        if (handle instanceof Feature
                && (owner == null || owner instanceof Classifier)) {
            setNamespace(handle, null);
            ((Feature) handle).setOwner((Classifier) owner);
            return;
        }
        if (handle instanceof TagDefinition
                && (owner == null || owner instanceof Stereotype)) {
            setNamespace(handle, null);
            ((TagDefinition) handle).setOwner((Stereotype) owner);
            if (owner != null) {
                ((Stereotype) owner).getDefinedTag().add(handle);
            }
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or owner: "
                + owner);
    }

    /**
     * @see org.argouml.model.CoreHelper#setOwnerScope( java.lang.Object,
     *      java.lang.Object)
     */
    public void setOwnerScope(Object handle, Object os) {
        if (handle instanceof Feature
                && (os == null || os instanceof ScopeKind)) {
            ((Feature) handle).setOwnerScope((ScopeKind) os);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or os: "
                + os);
    }

    /**
     * @see org.argouml.model.CoreHelper#setParameters( java.lang.Object,
     *      java.util.Collection)
     */
    public void setParameters(Object handle, Collection parameters) {
        if (handle instanceof ObjectFlowState || handle instanceof Classifier
                || handle instanceof Event
                || handle instanceof BehavioralFeature) {
            Collection params = Model.getFacade().getParameters(handle);
            if (!params.isEmpty()) {
                Vector actualParams = new Vector();
                actualParams.addAll(params);
                Iterator toRemove = actualParams.iterator();
                while (toRemove.hasNext()) {
                    removeParameter(handle, toRemove.next());
                }
            }
            if (!parameters.isEmpty()) {
                Iterator toAdd = parameters.iterator();
                while (toAdd.hasNext()) {
                    addParameter(handle, toAdd.next());
                }
            }
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or parameters: " + parameters);
    }

    /**
     * @see org.argouml.model.CoreHelper#setParent( java.lang.Object,
     *      java.lang.Object)
     */
    public void setParent(Object handle, Object parent) {
        if (handle instanceof Generalization
                && parent instanceof GeneralizableElement) {
            ((Generalization) handle).setParent((GeneralizableElement) parent);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or parent: "
                + parent);
    }

    /**
     * @see org.argouml.model.CoreHelper#setPowertype( java.lang.Object,
     *      java.lang.Object)
     */
    public void setPowertype(Object handle, Object pt) {
        if (handle instanceof Generalization
                && (pt == null || pt instanceof Classifier)) {
            ((Generalization) handle).setPowertype((Classifier) pt);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or pt: "
                + pt);
    }

    /**
     * @see org.argouml.model.CoreHelper#setQualifiers( java.lang.Object,
     *      java.util.Collection)
     */
    public void setQualifiers(Object handle, Collection elems) {
        if (handle instanceof AssociationEnd && elems instanceof List) {
            ((AssociationEnd) handle).getQualifier().clear();
            ((AssociationEnd) handle).getQualifier().addAll(elems);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#setQuery(java.lang.Object, boolean)
     */
    public void setQuery(Object handle, boolean flag) {
        if (handle instanceof BehavioralFeature) {
            ((BehavioralFeature) handle).setQuery(flag);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#setResident( java.lang.Object,
     *      java.lang.Object)
     */
    public void setResident(Object handle, Object resident) {
        if (handle instanceof ElementResidence
                && (resident == null || resident instanceof ModelElement)) {
            ((ElementResidence) handle).setResident((ModelElement) resident);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or resident: " + resident);
    }

    /**
     * @see org.argouml.model.CoreHelper#setResidents( java.lang.Object,
     *      java.util.Collection)
     */
    public void setResidents(Object handle, Collection residents) {
        if (handle instanceof NodeInstance) {
            ((NodeInstance) handle).getResident().clear();
            ((NodeInstance) handle).getResident().addAll(residents);
            return;
        }
        if (handle instanceof ComponentInstance) {
            ((ComponentInstance) handle).getResident().clear();
            ((ComponentInstance) handle).getResident().addAll(residents);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#setRoot(java.lang.Object, boolean)
     */
    public void setRoot(Object handle, boolean flag) {
        if (handle instanceof Reception) {
            ((Reception) handle).setRoot(flag);
            return;
        }
        if (handle instanceof Operation) {
            ((Operation) handle).setRoot(flag);
            return;
        }
        if (handle instanceof GeneralizableElement) {
            ((GeneralizableElement) handle).setRoot(flag);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#setSources( java.lang.Object,
     *      java.util.Collection)
     */
    public void setSources(Object handle, Collection specifications) {
        if (handle instanceof Flow) {
            ((Flow) handle).getSource().clear();
            ((Flow) handle).getSource().addAll(specifications);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#setSpecification( java.lang.Object,
     *      boolean)
     */
    public void setSpecification(Object handle, boolean specification) {
        if (handle instanceof ModelElement) {
            ((ModelElement) handle).setSpecification(specification);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }


    /**
     * @see org.argouml.model.CoreHelper#setSpecification(java.lang.Object, java.lang.String)
     */
    public void setSpecification(Object operation, String specification) {
        if (operation instanceof Operation) {
            ((Operation) operation).setSpecification( specification);
            return;
        }
        throw new IllegalArgumentException("operation: " + operation);
    }
    
    /**
     * @see org.argouml.model.CoreHelper#setSpecification(java.lang.Object, java.lang.Object)
     */
    public void setSpecification(Object method, Object specification) {
        if (method instanceof Method && specification instanceof Operation) {
            ((Method) method).setSpecification((Operation) specification);
            return;
        }
        throw new IllegalArgumentException("method: " + method
                + " or operation: " + specification);
    }

    /**
     * @see org.argouml.model.CoreHelper#setSpecifications( java.lang.Object,
     *      java.util.Collection)
     */
    public void setSpecifications(Object handle, Collection specifications) {
        if (handle instanceof AssociationEnd) {
            ((AssociationEnd) handle).getSpecification().addAll(specifications);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#setTaggedValue( java.lang.Object,
     *      java.lang.String, java.lang.String)
     */
    public void setTaggedValue(Object handle, String tag, String value) {
        if (handle instanceof ModelElement) {
            TaggedValue tv =
                (TaggedValue) modelImpl.getFacade().
                    getTaggedValue(handle, tag);
            if (tv == null) {
                tv =
                    (TaggedValue) modelImpl.getExtensionMechanismsFactory().
                        buildTaggedValue(tag, value);
                ((ModelElement) handle).getTaggedValue().add(tv);
            } else {
                modelImpl.getExtensionMechanismsHelper().setValueOfTag(tv,
                        value);
            }
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.CoreHelper#setTaggedValues( java.lang.Object,
     *      java.util.Collection)
     */
    public void setTaggedValues(Object handle, Collection taggedValues) {
        Model.getExtensionMechanismsHelper().setTaggedValue(handle,
                taggedValues);
    }

    /**
     * @see org.argouml.model.CoreHelper#setTargetScope( java.lang.Object,
     *      java.lang.Object)
     */
    public void setTargetScope(Object handle, Object scopeKind) {
        if (scopeKind instanceof ScopeKind) {
            if (handle instanceof StructuralFeature) {
                ((StructuralFeature) handle).
                        setTargetScope((ScopeKind) scopeKind);
                return;
            }
            if (handle instanceof AssociationEnd) {
                ((AssociationEnd) handle).setTargetScope((ScopeKind) scopeKind);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or scopeKind: " + scopeKind);
    }

    /**
     * @see org.argouml.model.CoreHelper#setType( java.lang.Object,
     *      java.lang.Object)
     */
    public void setType(Object handle, Object type) {
        if (type == null || type instanceof Classifier) {
            if (handle instanceof ObjectFlowState) {
                ((ObjectFlowState) handle).setType((Classifier) type);
                return;
            }
            if (handle instanceof ClassifierInState) {
                ((ClassifierInState) handle).setType((Classifier) type);
                return;
            }
            if (handle instanceof Parameter) {
                ((Parameter) handle).setType((Classifier) type);
                return;
            }
            if (handle instanceof AssociationEnd) {
                ((AssociationEnd) handle).setParticipant((Classifier) type);
                return;
            }
            if (handle instanceof StructuralFeature) {
                ((StructuralFeature) handle).setType((Classifier) type);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle + " or type: "
                + type);
    }

    /**
     * @see org.argouml.model.CoreHelper#setUUID( java.lang.Object,
     *      java.lang.String)
     */
    public void setUUID(Object handle, String uuid) {
        throw new NotImplementedException();
    }

    /**
     * @see org.argouml.model.CoreHelper#setVisibility( java.lang.Object,
     *      java.lang.Object)
     */
    public void setVisibility(Object handle, Object visibility) {
        if (visibility instanceof VisibilityKind) {
            if (handle instanceof ModelElement) {
                ((ModelElement) handle).
                        setVisibility((VisibilityKind) visibility);
                return;
            }
            if (handle instanceof ElementResidence) {
                ((ElementResidence) handle).
                        setVisibility((VisibilityKind) visibility);
                return;
            }
            if (handle instanceof ElementImport) {
                ((ElementImport) handle).
                        setVisibility((VisibilityKind) visibility);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or visibility: " + visibility);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeDeploymentLocation(
     *      java.lang.Object, java.lang.Object)
     */
    public void removeDeploymentLocation(Object handle, Object node) {
        if (handle instanceof Component && node instanceof Node) {
            ((Component) handle).getDeploymentLocation().remove(node);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or node: "
                + node);
    }

    /**
     * @see org.argouml.model.CoreHelper#addStereotype(java.lang.Object,
     * java.lang.Object)
     */
    public void addStereotype(Object modelElement, Object stereo) {
        if (modelElement instanceof ModelElement
                && stereo instanceof Stereotype) {
            ModelElement me = (ModelElement) modelElement;
            Stereotype stereotype = (Stereotype) stereo;
            if (!(me.getStereotype().contains(stereo))) {
                me.getStereotype().add(stereotype);
            }
            return;
        }
        throw new IllegalArgumentException("handle: " + modelElement
                + " or stereo: " + stereo);
    }


    /**
     * @see org.argouml.model.CoreHelper#addAllStereotypes(java.lang.Object, java.util.Collection)
     */
    public void addAllStereotypes(Object modelElement, Collection stereos) {
        if (modelElement instanceof ModelElement) {
            ModelElement me = (ModelElement) modelElement;
            me.getStereotype().addAll(stereos);
            return;
        }
        throw new IllegalArgumentException("handle: " + modelElement
                + " or stereos: " + stereos);
    }

    /**
     * @see org.argouml.model.CoreHelper#removeStereotype(java.lang.Object,
     *      java.lang.Object)
     */
    public void removeStereotype(Object modelElement, Object stereo) {
        if (modelElement instanceof ModelElement
                && stereo instanceof Stereotype) {

            ModelElement me = (ModelElement) modelElement;
            Stereotype stereotype = (Stereotype) stereo;

            if (me.getStereotype().contains(stereo)) {
                me.getStereotype().remove(stereotype);
            }
            return;
        }
        throw new IllegalArgumentException("handle: " + modelElement
                + " or stereo: " + stereo);
    }


    /**
     * @see org.argouml.model.CoreHelper#clearStereotypes(java.lang.Object)
     */
    public void clearStereotypes(Object modelElement) {
        if (modelElement instanceof ModelElement) {
            ((ModelElement) modelElement).getStereotype().clear();
            return;
        }
        throw new IllegalArgumentException("handle: " + modelElement);
    }

    /**
     * @see org.argouml.model.CoreHelper#setEnumerationLiterals(java.lang.Object,
     *      java.util.List)
     */
    public void setEnumerationLiterals(Object enumeration, List literals) {
        if (enumeration instanceof Enumeration) {
            ((Enumeration) enumeration).getLiteral().clear();
            ((Enumeration) enumeration).getLiteral().addAll(literals);
        }
    }

    /**
     * @see org.argouml.model.CoreHelper#getAllMetatypeNames()
     */
    public Collection getAllMetatypeNames() {
        List names = new ArrayList();
        for (Iterator iter = modelImpl.getModelPackage().getMofClass()
                .refAllOfClass().iterator(); iter.hasNext();) {
            String name =
                ((javax.jmi.model.ModelElement) iter.next()).getName();
            if (names.contains(name)) {
                LOG.error("Found duplicate class " + name + " in metamodel");
            } else {
                names.add(name);
                LOG.debug(" Class " + name);
            }
        }
        return names;
    }

}
