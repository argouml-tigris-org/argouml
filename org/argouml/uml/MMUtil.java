package org.argouml.uml;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.common_behavior.*;

import java.util.*;

import org.argouml.ui.ProjectBrowser;
import org.argouml.kernel.Project;

public class MMUtil {

	public static MMUtil SINGLETON = new MMUtil();

	public static MModel STANDARDS;

	static {
		STANDARDS = new MModelImpl();
		STANDARDS.setName("standard Elements");
		MStereotype realizationStereo = new MStereotypeImpl();
		realizationStereo.setName("realize");
		realizationStereo.setUUID(UUIDManager.SINGLETON.getNewUUID());
		STANDARDS.addOwnedElement(realizationStereo);

		MStereotype interfaceStereo = new MStereotypeImpl();
		interfaceStereo.setName("interface");
		interfaceStereo.setUUID(UUIDManager.SINGLETON.getNewUUID());
		STANDARDS.addOwnedElement(interfaceStereo);
	}

    // This method takes care about removing all unneeded transitions
    // while removing a StateVertex (like a State or ActionState, also Fork et.al.)
    public void remove(MStateVertex sv) {
	Collection transitions = sv.getIncomings();
	Iterator transitionIterator = transitions.iterator();
	while (transitionIterator.hasNext()) {
	    MTransition transition = (MTransition)transitionIterator.next();
	    transition.remove();
	}
	transitions = sv.getOutgoings();
	transitionIterator = transitions.iterator();
	while (transitionIterator.hasNext()) {
	    MTransition transition = (MTransition)transitionIterator.next();
	    transition.remove();
	}
	sv.remove();
    }

	// This method takes care about removing all unneeded associations,
	// generalizations, ClassifierRoles and dependencies when removing
	// a classifier.
    public void remove(MClassifier cls) {
		Iterator ascEndIterator = (cls.getAssociationEnds()).iterator();
		while (ascEndIterator.hasNext()) {
			MAssociationEnd ae = (MAssociationEnd)ascEndIterator.next();
			MAssociation assoc = ae.getAssociation();
			if ((assoc.getConnections()).size() < 3)
				assoc.remove();
			else
				ae.remove();
		}

		Iterator roleIterator = (cls.getClassifierRoles()).iterator();
		while (roleIterator.hasNext()) {
			MClassifierRole r = (MClassifierRole)roleIterator.next();
			r.remove();
		}

		Iterator generalizationIterator = (cls.getGeneralizations()).iterator();
		while (generalizationIterator.hasNext()) {
			MGeneralization gen = (MGeneralization)generalizationIterator.next();
			gen.remove();
		}

		Iterator specializationIterator = (cls.getSpecializations()).iterator();
		while (specializationIterator.hasNext()) {
			MGeneralization spec = (MGeneralization)specializationIterator.next();
			spec.remove();
		}

		Iterator clientDependencyIterator = cls.getClientDependencies().iterator();
		while (clientDependencyIterator.hasNext()) {
			MDependency dep = (MDependency)clientDependencyIterator.next();
			if (dep.getClients().size() < 2)
				dep.remove();
		}

		Iterator supplierDependencyIterator = cls.getSupplierDependencies().iterator();
		while (supplierDependencyIterator.hasNext()) {
			MDependency dep = (MDependency)supplierDependencyIterator.next();
			if (dep.getSuppliers().size() < 2)
				dep.remove();
		}


		cls.remove();
    }

 public void remove (MObject obj) {
	Iterator linkEndIterator = (obj.getLinkEnds()).iterator();
	while (linkEndIterator.hasNext()) {
	    MLinkEnd le = (MLinkEnd)linkEndIterator.next();
	    MLink link = le.getLink();
	    if ((link.getConnections()).size() < 3)
		link.remove();
	    else
		le.remove();
	}
	obj.remove();
    }

    public void remove (MStimulus stimulus) {
	MLink link = stimulus.getCommunicationLink();
	link.remove();
	stimulus.remove();
    }

     public void remove (MLink link) {
	link.remove();
    }

    // should be moved to better standards repository!
    public MStereotype getRealizationStereotype() {
	return (MStereotype)STANDARDS.lookup("realize");
    }

    public MAssociation buildAssociation(MClassifier c1, MClassifier c2) {
	return this.buildAssociation(c1, true, c2, true);
    }

    public MAssociation buildAssociation(MClassifier c1, boolean nav1, MClassifier c2, boolean nav2) {
		MAssociationEnd ae1 = new MAssociationEndImpl();
		ae1.setType(c1);
		ae1.setNavigable(nav1);
		ae1.setMultiplicity(MMultiplicity.M1_1);
	
		MAssociationEnd ae2 = new MAssociationEndImpl();
		ae2.setType(c2);
		ae2.setNavigable(nav2);
		ae2.setMultiplicity(MMultiplicity.M1_1);

		MAssociation asc = new MAssociationImpl();
		asc.addConnection(ae1);
		asc.addConnection(ae2);


		// asc.setUUID(UUIDManager.SINGLETON.getNewUUID());

		return asc;
	}

	public MGeneralization buildGeneralization(MGeneralizableElement child, MGeneralizableElement parent) {
	    if (parent.getParents().contains(child)) return null;

		MGeneralization gen = new MGeneralizationImpl();
		gen.setParent(parent);
		gen.setChild(child);
		if (parent.getNamespace() != null) gen.setNamespace(parent.getNamespace());
		else if (child.getNamespace() != null) gen.setNamespace(child.getNamespace());
		return gen;
	}

	public MDependency buildDependency(MModelElement client, MModelElement supplier) {
		MDependency dep = new MDependencyImpl();
		dep.addSupplier(supplier);
		dep.addClient(client);
		if (supplier.getNamespace() != null) dep.setNamespace(supplier.getNamespace());
		else if (client.getNamespace() != null) dep.setNamespace(client.getNamespace());
		return dep;
	}

	public MAbstraction buildRealization(MModelElement client, MModelElement supplier) {
		MAbstraction realization = new MAbstractionImpl();
// 		MStereotype realStereo = (MStereotype)STANDARDS.lookup("realize");
// 		System.out.println("real ist: "+realStereo);
		MStereotype realStereo = new MStereotypeImpl();
		realStereo.setName("realize");
		if (supplier.getNamespace() != null) {
		    MNamespace ns = supplier.getNamespace();
		    realization.setNamespace(ns);
		    realStereo.setNamespace(ns);
		    //		    ns.addOwnedElement(STANDARDS);
		}
		else if (client.getNamespace() != null) {
		    MNamespace ns = client.getNamespace();
		    realization.setNamespace(ns);
		    realStereo.setNamespace(ns);
		    //		    ns.addOwnedElement(STANDARDS);
		}
		realization.setStereotype(realStereo);
		realization.addSupplier(supplier);
		realization.addClient(client);
		return realization;
	}

	public MBinding buildBinding(MModelElement client, MModelElement supplier) {
		MBinding binding = new MBindingImpl();
		binding.addSupplier(supplier);
		binding.addClient(client);
		if (supplier.getNamespace() != null) binding.setNamespace(supplier.getNamespace());
		else if (client.getNamespace() != null) binding.setNamespace(client.getNamespace());
		return binding;
	}

	public MUsage buildUsage(MModelElement client, MModelElement supplier) {
	    MUsage usage = new MUsageImpl();
		usage.addSupplier(supplier);
		usage.addClient(client);
		if (supplier.getNamespace() != null) usage.setNamespace(supplier.getNamespace());
		else if (client.getNamespace() != null) usage.setNamespace(client.getNamespace());
		return usage;
	}

    public MOperation buildOperation(MClassifier cls){
	//build the default operation
	ProjectBrowser pb = ProjectBrowser.TheInstance;
	Project p = pb.getProject();
	MClassifier voidType = p.findType("void");

	MParameter returnParameter = new MParameterImpl();
	returnParameter.setKind(MParameterDirectionKind.RETURN);
	returnParameter.setType(voidType);
	returnParameter.setName("return");

	MOperation oper = new MOperationImpl();
	oper.addParameter(returnParameter);
	oper.setName("newOperation");
	oper.setVisibility(MVisibilityKind.PUBLIC);

	cls.addFeature(oper);
	return oper;
    }


    public MAttribute buildAttribute(MClassifier cls){
	//build the default operation
	ProjectBrowser pb = ProjectBrowser.TheInstance;
	Project p = pb.getProject();
	MClassifier intType = p.findType("int");
	MAttribute attr = cls.getFactory().createAttribute();
	attr.setName("newAttr");
	attr.setType(intType);
	attr.setVisibility(MVisibilityKind.PUBLIC);

	cls.addFeature(attr);
	return attr;
    }

    public MMessage buildMessage(MAssociationRole ar){
	return buildMessage(ar, "");
    }

    public MMessage buildMessage(MAssociationRole ar,String sequenceNumber){

	MMessage msg = new MMessageImpl();
	msg.setName(sequenceNumber);
	Collection ascEnds = ar.getConnections();

	if (ascEnds.size() != 2 ) return null;
	Iterator iter = ascEnds.iterator();
	MAssociationEndRole aer1 = (MAssociationEndRole)iter.next();
	MAssociationEndRole aer2 = (MAssociationEndRole)iter.next();
	
	// by default the "first" Classifierrole is the Sender,
	// should be configurable in PropPanelMessage!
	MClassifierRole crSrc = (MClassifierRole)aer1.getType();
	MClassifierRole crDst = (MClassifierRole)aer2.getType();
	msg.setSender(crSrc);
	msg.setReceiver(crDst);

	MCallAction action = new MCallActionImpl();
	action.setNamespace(ProjectBrowser.TheInstance.getProject().getModel());
	action.setName("action"+sequenceNumber);
	msg.setAction(action);

	ar.addMessage(msg);
	MCollaboration collab = (MCollaboration) ar.getNamespace();
	// collab.addOwnedElement(msg);
	Collection interactions = collab.getInteractions();
	// at the moment there can be only one Interaction per Collaboration
	Iterator iter2 = interactions.iterator();
	((MInteraction)iter2.next()).addMessage(msg);
	
	return msg;
    }

	/** This method returns all Interfaces of which this class is a realization.
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

	// this method removes ALL paramters of the given operation which have
	// the MParamterDirectionType RETURN and adds the new parameter, which
	// gets RETURN by default

	public void setReturnParameter(MOperation operation, MParameter newReturnParameter) {

		Iterator params = operation.getParameters().iterator();
		while (params.hasNext()) {
			MParameter parameter = (MParameter)params.next();
			if ((parameter.getKind()).equals(MParameterDirectionKind.RETURN)) {
				operation.removeParameter(parameter);
			}
		}
		newReturnParameter.setKind(MParameterDirectionKind.RETURN);
		operation.addParameter(0, newReturnParameter);
	}
}
