package uci.uml.util;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.state_machines.*;

import com.sun.java.util.collections.*;

public class MMUtil {

	public static MMUtil SINGLETON = new MMUtil();

	public static MModel STANDARDS;

	static {
		STANDARDS = new MModelImpl();
		MStereotype realization = new MStereotypeImpl();
		realization.setName("realize");
		realization.setUUID(UUIDManager.SINGLETON.getNewUUID());
		STANDARDS.addOwnedElement(realization);
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

    // should be moved to better standards repository!
    public MStereotype getRealizationStereotype() {
	return (MStereotype)STANDARDS.lookup("realize");
    }

	public MAssociation buildAssociation(MClassifier c1, MClassifier c2) {
		return this.buildAssociation(c1, true, c2, true);
	}
		
	public MAssociation buildAssociation(MClassifier c1, boolean nav1, MClassifier c2, boolean nav2) {
		// try to put AssociationEnds into the same namespace as the corresponding 
		// Classifier. If it has none, use the Classifier (which is a namespace itself)
		MNamespace ns = c1.getNamespace();
		if (ns == null) ns = c1;
		MAssociation asc = new MAssociationImpl();
		MAssociationEnd ae1 = new MAssociationEndImpl();
		ae1.setType(c1);
		ae1.setNavigable(nav1);
		ae1.setNamespace(ns);

		ns = c2.getNamespace();
		if (ns == null) ns = c2;
		MAssociationEnd ae2 = new MAssociationEndImpl();
		ae2.setType(c2);
		ae2.setNavigable(nav2);
		ae2.setNamespace(ns);

		asc.addConnection(ae1);
		asc.addConnection(ae2);

		// asc.setUUID(UUIDManager.SINGLETON.getNewUUID());

		return asc;
	}

	public MGeneralization buildGeneralization(MGeneralizableElement child, MGeneralizableElement parent) {

		MGeneralization gen = new MGeneralizationImpl();
		gen.setParent(parent);
		gen.setChild(child);
		if (parent.getNamespace() != null) gen.setNamespace(parent.getNamespace());
		else if (child.getNamespace() != null) gen.setNamespace(child.getNamespace());
		return gen;
	}

	public MAbstraction buildRealization(MModelElement client, MModelElement supplier) {
		MAbstraction realization = new MAbstractionImpl();
		realization.setStereotype((MStereotype)STANDARDS.lookup("realize"));
		realization.addSupplier(supplier);
		realization.addClient(client);
		if (supplier.getNamespace() != null) realization.setNamespace(supplier.getNamespace());
		else if (client.getNamespace() != null) realization.setNamespace(client.getNamespace());
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

	// this method finds all paramters of the given operation which have
	// the MParamterDirectionType RETURN. If it is only one, it is returned.
	// In case there are no return parameters, null is returned. If there
	// is more than one return paramter, first of them is returned, but a 
	// message is written to System.out

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
			System.out.println("No ReturnParameter found!");
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
