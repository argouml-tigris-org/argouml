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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import org.argouml.model.uml.foundation.extensionmechanisms.ExtensionMechanismsFactory;
import org.argouml.ui.ProjectBrowser;

import ru.novosoft.uml.foundation.core.MAbstraction;
import ru.novosoft.uml.foundation.core.MAbstractionImpl;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MComponent;
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MDependency;
import ru.novosoft.uml.foundation.core.MFeature;
import ru.novosoft.uml.foundation.core.MGeneralizableElement;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MNode;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.core.MStructuralFeature;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.model_management.MModel;
import sun.security.action.GetBooleanAction;

/**
 * Helper class for UML Foundation::Core Package.
 *
 * Current implementation is a placeholder.
 * 
 * @since ARGO0.11.2
 * @author Thierry Lach
 * @author Jaap Branderhorst
 */
public class CoreHelper {

    /** Don't allow instantiation.
     */
    private CoreHelper() {
    }
    
     /** Singleton instance.
     */
    private static CoreHelper SINGLETON =
                   new CoreHelper();

    
    /** Singleton instance access method.
     */
    public static CoreHelper getHelper() {
        return SINGLETON;
    }
    
    /** This method returns all Classifiers of which this class is a 
	 *	direct or indirect subtype.
	 *
	 * @param cls  the class you want to have the parents for
	 * @return a collection of the parents, each of which is a 
	 *					{@link MGeneralizableElement MGeneralizableElement}
	 */
	public Collection getAllSupertypes(MClassifier cls) {

		Collection result = new HashSet();

		Collection add = getSupertypes(cls);
		do 
		{
			Collection newAdd = new HashSet();
			Iterator addIter = add.iterator();
			while (addIter.hasNext())
			{
				MGeneralizableElement next = (MGeneralizableElement) addIter.next();
				if (next instanceof MClassifier) 
				{
					newAdd.addAll( getSupertypes((MClassifier) next) );
				}
			}
			result.addAll(add);
			add = newAdd;
			add.removeAll(result);
		}
		while (! add.isEmpty());
		
		return result;
	}
	
	/** This method returns all Classifiers of which this class is a 
	 *	direct subtype.
	 *
	 * @param cls  the class you want to have the parents for
	 * @return a collection of the parents, each of which is a 
	 *					{@link MGeneralizableElement MGeneralizableElement}
	 */
	public Collection getSupertypes(MClassifier cls) {

		Collection result = new HashSet();
		Collection gens = cls.getGeneralizations();
		Iterator genIterator = gens.iterator();

		while (genIterator.hasNext()) {
			MGeneralization next = (MGeneralization) genIterator.next();
			result.add(next.getParent());
		}
		return result;
	}
	
	/** This method returns all opposite AssociationEnds of a given Classifier
	 *
	 * @param classifier the classifier you want to have the opposite association ends for
	 * @return a collection of the opposite associationends
	 */
	public Collection getAssociateEnds(MClassifier classifier) {
	    Collection result = new ArrayList();
		Iterator ascends = classifier.getAssociationEnds().iterator();
		while (ascends.hasNext()) {
			MAssociationEnd ascend = (MAssociationEnd)ascends.next();
			if ((ascend.getOppositeEnd() != null))
				result.add(ascend.getOppositeEnd());
		}
		return result;
	}
	
	/** This method returns all opposite AssociationEnds of a given Classifier, including inherited
	 *
	 * @param classifier the classifier you want to have the opposite association ends for
	 * @return a collection of the opposite associationends
	 */
	public Collection getAssociateEndsInh(MClassifier classifier) {
	    Collection result = new ArrayList();
		result.addAll(getAssociateEnds(classifier));
		Iterator parents = classifier.getParents().iterator();
		while (parents.hasNext()) {
		    result.addAll(getAssociateEndsInh((MClassifier)parents.next()));
		}
		return result;
	}
	
	/** This method returns all attributes of a given Classifier.
	 *
	 * @param classifier the classifier you want to have the attributes for
	 * @return a collection of the attributes
	 */

	public Collection getAttributes(MClassifier classifier) {
	    Collection result = new ArrayList();
		Iterator features = classifier.getFeatures().iterator();
		while (features.hasNext()) {
			MFeature feature = (MFeature)features.next();
			if (feature instanceof MAttribute)
				result.add(feature);
		}
		return result;
	}
	
	/** This method returns all operations of a given Classifier
	 *
	 * @param classifier the classifier you want to have the operations for
	 * @return a collection of the operations
	 */
	public Collection getOperations(MClassifier classifier) {
	    Collection result = new ArrayList();
		Iterator features = classifier.getFeatures().iterator();
		while (features.hasNext()) {
			MFeature feature = (MFeature)features.next();
			if (feature instanceof MOperation)
				result.add(feature);
		}
		return result;
	}
	
	/** This method returns all attributes of a given Classifier, including inherited
	 *
	 * @param classifier the classifier you want to have the attributes for
	 * @return a collection of the attributes
	 */

	public Collection getAttributesInh(MClassifier classifier) {
	    Collection result = new ArrayList();
		result.addAll(getAttributes(classifier));
		Iterator parents = classifier.getParents().iterator();
		while (parents.hasNext()) {
			MClassifier parent = (MClassifier)parents.next();
  			System.out.println("Adding attributes for: "+parent);
			result.addAll(getAttributesInh(parent));
		}
		return result;
	}
	
	/** This method returns all operations of a given Classifier, including inherited
	 *
	 * @param classifier the classifier you want to have the operations for
	 * @return a collection of the operations
	 */
	public Collection getOperationsInh(MClassifier classifier) {
	    Collection result = new ArrayList();
		result.addAll(getOperations(classifier));
		Iterator parents = classifier.getParents().iterator();
		while (parents.hasNext()) {
			result.addAll(getOperationsInh((MClassifier)parents.next()));
		}
		return result;
	}
	
	/** this method finds all paramters of the given operation which have
	 * the MParamterDirectionType RETURN. If it is only one, it is returned.
	 * In case there are no return parameters, null is returned. If there
	 * is more than one return paramter, first of them is returned, but a
	 * message is written to System.out
	 *
	 * @param operation the operation you want to find the return parameter for
	 * @return If this operation has only one paramter with Kind: RETURN, this is it, otherwise null
	 */

	public MParameter getReturnParameter(MOperation operation) {
		Vector returnParams = new Vector();
		MParameter firstReturnParameter = null;
		Iterator params = operation.getParameters().iterator();
		while (params.hasNext()) {
			MParameter parameter = (MParameter)params.next();
			if ((parameter.getKind()).equals(MParameterDirectionKind.RETURN)) {
				returnParams.add(parameter);
			}
		}

		switch (returnParams.size()) {
		case 1:
			return (MParameter)returnParams.elementAt(0);
		case 0:
		    // System.out.println("No ReturnParameter found!");
			return null;
		default:
			System.out.println("More than one ReturnParameter found, returning first!");
			return (MParameter)returnParams.elementAt(0);
		}
	}
	
	/**
     * Returns all return parameters for an operation.
     * @param operation
     * @return Collection
     */
	public Collection getReturnParameters(MOperation operation) {
		Vector returnParams = new Vector();
		MParameter firstReturnParameter = null;
		Iterator params = operation.getParameters().iterator();
		while (params.hasNext()) {
			MParameter parameter = (MParameter)params.next();
			if ((parameter.getKind()).equals(MParameterDirectionKind.RETURN)) {
				returnParams.add(parameter);
			}
		}
		return (Collection)returnParams;
	}
	
	/** 
	 * Returns all Interfaces of which this class is a realization.
	 * @param cls  the class you want to have the interfaces for
	 * @return a collection of the Interfaces
	 */
	public Collection getSpecifications(MClassifier cls) {
		Collection result = new Vector();
		Collection deps = cls.getClientDependencies();
		Iterator depIterator = deps.iterator();

		while (depIterator.hasNext()) {
			MDependency dep = (MDependency)depIterator.next();
			if ((dep instanceof MAbstraction) &&
			    dep.getStereotype() != null &&
			    dep.getStereotype().getName() != null &&
			    dep.getStereotype().getName().equals("realize")) {
			    MInterface i = (MInterface)dep.getSuppliers().toArray()[0];
			    result.add(i);
			}
		}
		return result;
	}
	
	/** This method returns all Classifiers of which this class is a 
	 *	direct supertype.
	 *
	 * @param cls  the class you want to have the children for
	 * @return a collection of the children, each of which is a 
	 *					{@link MGeneralizableElement MGeneralizableElement}
	 */
	public Collection getSubtypes(MClassifier cls) {

		Collection result = new Vector();
		Collection gens = cls.getSpecializations();
		Iterator genIterator = gens.iterator();

		while (genIterator.hasNext()) {
			MGeneralization next = (MGeneralization) genIterator.next();
			result.add(next.getChild());
		}
		return result;
	}
	
	/**
     * Build a returnparameter. Removes all current return parameters from the
     * operation and adds the supplied parameter. The directionkind of the 
     * parameter will be return. The name will be equal to the name of the last
     * found return parameter or the default value "return" if no return
     * parameter was present in the operation.
     * @param operation
     * @param newReturnParameter
     */
	public void setReturnParameter(MOperation operation, MParameter newReturnParameter) {
		Iterator params = operation.getParameters().iterator();
        String name = "return";
		while (params.hasNext()) {
			MParameter parameter = (MParameter)params.next();
			if ((parameter.getKind()).equals(MParameterDirectionKind.RETURN)) {
				operation.removeParameter(parameter);
                if (parameter.getName() != null || parameter.getName() == "") {
                    name = parameter.getName();
                }
			}
		}
        newReturnParameter.setName(name);
		newReturnParameter.setKind(MParameterDirectionKind.RETURN);
		operation.addParameter(0, newReturnParameter);
	}
	
	/**
	 * Builds a dependency with stereotype support
	 */
	public MDependency buildSupportDependency(MModelElement from, MModelElement to) {
		MDependency dep = CoreFactory.getFactory().buildDependency(from, to);
		MNamespace model = ProjectBrowser.TheInstance.getProject().getModel();
		MStereotype stereo = ExtensionMechanismsFactory.getFactory().buildStereotype(dep, "support", ProjectBrowser.TheInstance.getProject().getModel());
		return dep;
	}
	
	/**
	 * Returns all behavioralfeatures found in this element and its children
	 * @return Collection
	 */
	public Collection getAllBehavioralFeatures(MModelElement element) {
		Iterator it = element.getModelElementContents().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof MClassifier) {
				list.addAll(getAllBehavioralFeatures((MClassifier)o));
			} else {
				list.addAll(getAllBehavioralFeatures((MModelElement)it.next()));
			}
			
		}
		return list;
	}
	
	/**
	 * Returns all behavioralfeatures found in this classifier and its children
	 * @return Collection
	 */
	public Collection getAllBehavioralFeatures(MClassifier clazz) {
		List features = new ArrayList();
		if (!(clazz instanceof MDataType)) {
			Iterator it = clazz.getFeatures().iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (!(o instanceof MStructuralFeature)) {
					features.add(o);
				}
			}
		}
		return features;
	}
	
	/**
	 * Returns all behavioralfeatures found in the projectbrowser model
	 * @return Collection
	 */
	public Collection getAllBehavioralFeatures() {
		MNamespace model = ProjectBrowser.TheInstance.getProject().getModel();
		return getAllBehavioralFeatures(model);
	}
	
	/**
	 * Returns all interfaces found in the projectbrowser model
	 * @return Collection
	 */
	public Collection getAllInterfaces() {
		MNamespace model = ProjectBrowser.TheInstance.getProject().getModel();
		return getAllInterfaces(model);
	}
	
	/**
	 * Returns all interfaces found in this namespace and in its children
	 * @return Collection
	 */
	public Collection getAllInterfaces(MNamespace ns) {
		if (ns == null) return new ArrayList();
		Iterator it = ns.getOwnedElements().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof MNamespace) {
				list.addAll(getAllInterfaces((MNamespace)o));
			} 
			if (o instanceof MInterface) {
				list.add(o);
			}
			
		}
		return list;
	}
	
	/**
	 * Returns all classes found in the projectbrowser model
	 * @return Collection
	 */
	public Collection getAllClasses() {
		MNamespace model = ProjectBrowser.TheInstance.getProject().getModel();
		return getAllClasses(model);
	}
	
	/**
	 * Returns all classes found in this namespace and in its children
	 * @return Collection
	 */
	public Collection getAllClasses(MNamespace ns) {
		if (ns == null) return new ArrayList();
		Iterator it = ns.getOwnedElements().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof MNamespace) {
				list.addAll(getAllClasses((MNamespace)o));
			} 
			if (o instanceof MClass) {
				list.add(o);
			}
			
		}
		return list;
	}
	
	/**
	 * Return all interfaces the given class realizes.
	 * @param clazz
	 * @return Collection
	 */
	public Collection getRealizedInterfaces(MClassifier clazz) {
		if (clazz == null) return new ArrayList();
		Iterator it = clazz.getClientDependencies().iterator();
		List list = new ArrayList();
		MNamespace model = ProjectBrowser.TheInstance.getProject().getModel();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof MAbstraction) {
				MAbstraction abstraction = (MAbstraction)o;
				MStereotype stereo = abstraction.getStereotype();
				if (stereo != null && stereo.getBaseClass().equals("Abstraction") && stereo.getName().equals("realize")) {					
					Iterator it2 = abstraction.getSuppliers().iterator();
					while (it2.hasNext()) {
						Object o2 = it2.next();
						if (o2 instanceof MInterface) {
							list.add(o2);
						}
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * Returns the realization (abstraction) between some class and some interface
	 *
	 * @param source
	 * @param clazz
	 * @return MAbstraction
	 */
	public MAbstraction getRealization(MInterface source, MClassifier clazz) {
		if (source == null || clazz == null) return null;
		Iterator it = clazz.getClientDependencies().iterator();
		MNamespace model = ProjectBrowser.TheInstance.getProject().getModel();
		MStereotype stereo = ExtensionMechanismsFactory.getFactory().buildStereotype(new MAbstractionImpl(), "realize", model);
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof MAbstraction && 
				((MAbstraction)o).getStereotype().equals(stereo)) {
				Iterator it2 = ((MAbstraction)o).getSuppliers().iterator();
				while (it2.hasNext()) {
					Object o2 = it2.next();
					if (o2.equals(source)) {
						return (MAbstraction)o;
					}
				}
			}
		}
		return null;
	}
				
	/**
	 * Returns all classes some generalizable element clazz extends.
	 * @param clazz
	 * @return Collection
	 */
	public Collection getExtendedClassifiers(MGeneralizableElement clazz) {
		if (clazz == null) return new ArrayList();
		Iterator it = clazz.getGeneralizations().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			MGeneralization gen = (MGeneralization)it.next();
			MGeneralizableElement parent = gen.getParent();
			if (parent != null) {
				list.add(parent);
			}
		}
		return list;
	}
	
	/**
	 * Gets the generalization between two generalizable elements. Returns null
	 * if there is none.
	 * @param child
	 * @param parent
	 * @return MGeneralization
	 */
	public MGeneralization getGeneralization(MGeneralizableElement child, MGeneralizableElement parent) {
		if (child == null || parent == null) return null;
		Iterator it = child.getGeneralizations().iterator();
		while (it.hasNext()) {
			MGeneralization gen = (MGeneralization)it.next();
			if (gen.getParent() == parent) {
				return gen;
			}
		}
		return null;
	}
		
	/**
	 * Returns all elements that extend some class clazz.
	 * @param clazz
	 * @return Collection
	 */
	public Collection getExtendingElements(MGeneralizableElement clazz) {
		if (clazz == null) return new ArrayList();
		Iterator it = clazz.getSpecializations().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			MGeneralization gen = (MGeneralization)it.next();
			MGeneralizableElement client = gen.getChild();
			if (client != null) {
				list.add(client);
			}
		}
		return list;
	}
	
	/**
	 * Returns all classifiers that extend some classifier clazz.
	 * @param clazz
	 * @return Collection
	 */
	public Collection getExtendingClassifiers(MClassifier clazz) {
		if (clazz == null) return new ArrayList();
		Iterator it = clazz.getSpecializations().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			MGeneralization gen = (MGeneralization)it.next();
			MGeneralizableElement client = gen.getChild();
			if (client instanceof MClassifier) {
				list.add(client);
			}
		}
		return list;
	}
	
	/**
	 * Returns all components found in the projectbrowser model
	 * @return Collection
	 */
	public Collection getAllComponents() {
		MNamespace model = ProjectBrowser.TheInstance.getProject().getModel();
		return getAllComponents(model);
	}
	
	/**
	 * Returns all components found in this namespace and in its children
	 * @return Collection
	 */
	public Collection getAllComponents(MNamespace ns) {
		if (ns == null) return new ArrayList();
		Iterator it = ns.getOwnedElements().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof MNamespace) {
				list.addAll(getAllComponents((MNamespace)o));
			} 
			if (o instanceof MComponent) {
				list.add(o);
			}
			
		}
		return list;
	}
	
	/**
	 * Returns all datatypes found in the projectbrowser model
	 * @return Collection
	 */
	public Collection getAllDataTypes() {
		MNamespace model = ProjectBrowser.TheInstance.getProject().getModel();
		return getAllDataTypes(model);
	}
	
	/**
	 * Returns all components found in this namespace and in its children
	 * @return Collection
	 */
	public Collection getAllDataTypes(MNamespace ns) {
		if (ns == null) return new ArrayList();
		Iterator it = ns.getOwnedElements().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof MNamespace) {
				list.addAll(getAllDataTypes((MNamespace)o));
			} 
			if (o instanceof MDataType) {
				list.add(o);
			}
			
		}
		return list;
	}
	
	/**
	 * Returns all nodes found in the projectbrowser model
	 * @return Collection
	 */
	public Collection getAllNodes() {
		MNamespace model = ProjectBrowser.TheInstance.getProject().getModel();
		return getAllNodes(model);
	}
	
	/**
	 * Returns all components found in this namespace and in its children
	 * @return Collection
	 */
	public Collection getAllNodes(MNamespace ns) {
		if (ns == null) return new ArrayList();
		Iterator it = ns.getOwnedElements().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof MNamespace) {
				list.addAll(getAllNodes((MNamespace)o));
			} 
			if (o instanceof MNode) {
				list.add(o);
			}
			
		}
		return list;
	}
	
	/**
	 * Gets all classifiers that are associated to the given classifier (have 
	 * an association relationship with the classifier).
	 * @param classifier
	 * @return Collection
	 */
	public Collection getAssociatedClassifiers(MClassifier classifier) {
		if (classifier == null) return new ArrayList();
		List list = new ArrayList();
		Iterator it = classifier.getAssociationEnds().iterator();
		while (it.hasNext()) {
			MAssociationEnd end = (MAssociationEnd)it.next();
			MAssociation assoc = end.getAssociation();
			Iterator it2 = assoc.getConnections().iterator();
			while (it2.hasNext()) {
				MAssociationEnd end2 = (MAssociationEnd)it2.next();
				if (end2 != end) {
					list.add(end2.getType());
				}
			}
		}
		return list;
	}
	
	/**
	 * Gets the association between the classifiers from and to. Returns null
	 * if from or to is null or if there is no association between them.
	 * @param from
	 * @param to
	 * @return MAssociation
	 */
	public MAssociation getAssociation(MClassifier from, MClassifier to) {
		if (from == null || to == null) return null;
		Iterator it = from.getAssociationEnds().iterator();
		while (it.hasNext()) {
			MAssociationEnd end = (MAssociationEnd)it.next();
			MAssociation assoc = end.getAssociation();
			Iterator it2 = assoc.getConnections().iterator();
			while (it2.hasNext()) {
				MAssociationEnd end2 = (MAssociationEnd)it2.next();
				if (end2.getType() == to) {
					return assoc;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns all classifiers found in the projectbrowser model
	 * @return Collection
	 */
	public Collection getAllClassifiers() {
		MNamespace model = ProjectBrowser.TheInstance.getProject().getModel();
		return getAllClassifiers(model);
	}
	
	/**
	 * Returns all classifiers found in this namespace and in its children
	 * @return Collection
	 */
	public Collection getAllClassifiers(MNamespace ns) {
		if (ns == null) return new ArrayList();
		Iterator it = ns.getOwnedElements().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof MNamespace) {
				list.addAll(getAllClasses((MNamespace)o));
			} 
			if (o instanceof MClassifier) {
				list.add(o);
			}
			
		}
		return list;
	}
	
	/**
	 * Returns all associations for some classifier
	 * @param classifier
	 * @return Collection
	 */
	public Collection getAssociations(MClassifier classifier) {
		if (classifier == null) return new ArrayList();
		Iterator it = classifier.getAssociationEnds().iterator();
		List associations = new ArrayList();
		while (it.hasNext()) {
			associations.add(((MAssociationEnd)it.next()).getAssociation());
		}
		return associations;
	}
	
	/**
	 * Returns the associationend between some classifier type and some associaton assoc.
	 * @param type
	 * @param assoc
	 * @return MAssociationEnd
	 */
	public MAssociationEnd getAssociationEnd(MClassifier type, MAssociation assoc) {
		if (type == null || assoc == null) return null;
		Iterator it = type.getAssociationEnds().iterator();
		while (it.hasNext()) {
			MAssociationEnd end = (MAssociationEnd)it.next();
			if (assoc.getConnections().contains(end)) return end;
		}
		return null;
	}
	
	/**
	 * Returns the contents (owned elements) of this classifier and all its parents
	 * as specified in section 2.5.3.8 of the UML 1.3 spec
	 * @param clazz
	 * @return Collection
	 */
	public Collection getAllContents(MClassifier clazz) {
		if (clazz == null) return new ArrayList();
		List list = new ArrayList();
		Iterator it = clazz.getOwnedElements().iterator();
		while (it.hasNext()) {
			MModelElement element = (MModelElement)it.next();
			if (element.getVisibility().equals(MVisibilityKind.PUBLIC) ||
				element.getVisibility().equals(MVisibilityKind.PROTECTED)) {
				list.add(element);
			}
		}
		it = clazz.getGeneralizations().iterator();
		while (it.hasNext()) {
			list.addAll(getAllContents((MClassifier)it.next()));
		}
		return list;
	}
	
	/**
	 * Returns all attributes of some classifier clazz and of its parents
	 * @param clazz
	 * @return Collection
	 */
	public Collection getAllAttributes(MClassifier clazz) {
		if (clazz == null) return new ArrayList();
		List list = new ArrayList();
		Iterator it = clazz.getFeatures().iterator();
		while (it.hasNext()) {
			MFeature element = (MFeature)it.next();
			if (element instanceof MAttribute) {
				list.add(element);
			}
		}
		it = clazz.getGeneralizations().iterator();
		while (it.hasNext()) {
			list.addAll(getAllAttributes((MClassifier)it.next()));
		}
		return list;
	}
		
		
		
	
}

