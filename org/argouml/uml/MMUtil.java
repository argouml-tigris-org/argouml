// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: MMUtil.java
// Classes: MMUtil
// Original Author: not known
// $Id$

// 3 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to support
// the Extend and Include relationships.

// 8 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to add
// buildExtensionPoint and getExtensionPoints methods for use cases.

// 16 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to remove
// include and extend relationships when deleting a use case.


package org.argouml.uml;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.common_behavior.*;

import java.util.*;

import org.argouml.ui.ProjectBrowser;
import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Notation;
import org.argouml.application.api.NotationName;
import org.argouml.application.notation.NotationProviderFactory;
import org.argouml.kernel.Project;

public class MMUtil {
	
	Category cat = Category.getInstance(org.argouml.uml.MMUtil.class);

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

    /**
     * <p>Remove a use case and its associated connections from the model.<p>
     *
     * <p>We remove the include and extend here, then use the classifier
     *   version of this method to remove everything else. Note that we must
     *   get rid of both ends.</p>
     *
     * @param useCase  The use case to remove form the model
     */

    public void remove(MUseCase useCase) {

        // Get rid of extends

        Iterator extendIterator = (useCase.getExtends()).iterator();

        while (extendIterator.hasNext()) {
            ((MExtend) extendIterator.next()).remove();
        }

        extendIterator = (useCase.getExtends2()).iterator();

        while (extendIterator.hasNext()) {
            ((MExtend) extendIterator.next()).remove();
        }

        // Get rid of includes

        Iterator includeIterator = (useCase.getIncludes()).iterator();

        while (includeIterator.hasNext()) {
            ((MInclude) includeIterator.next()).remove();
        }

        includeIterator = (useCase.getIncludes2()).iterator();

        while (includeIterator.hasNext()) {
            ((MInclude) includeIterator.next()).remove();
        }

        // Use the classifier version to get rid of everything else (including
        // our very own good selves).

        remove((MClassifier) useCase);
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
    
    /**
     * Removes a modelelement. Delegates the actual removal to more 
     * specific methods if specific elements must be removed.
     * @param me
     * @see org.argouml.kernel.Project#trashInternal(Object) for the use of the method
     * @author jaap.branderhorst@xs4all.nl
     */
    public void remove(MModelElement me) {
    	if (me instanceof MClassifier) {
    		remove((MClassifier)me); return;
    	}
    	if (me instanceof MStateVertex) {
    		remove((MStateVertex)me); return;
    	}
    	if (me instanceof MLink) {
    		remove((MLink)me); return;
    	}
    	if (me instanceof MObject) {
    		remove((MObject)me); return;
    	}
    	if (me instanceof MStimulus) {
    		remove((MStimulus)me); return;
    	}
    	if (me instanceof MUseCase) {
    		remove((MUseCase)me); return;
    	}
    	me.remove();
    }

    /**
     * Removes a classifier including all depending Modelelements.
     * @param cls The classifier to be removed
     */
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


		cls.remove(); //takes also care of removing the elementlisteners
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

    /**
     * Builds a binary associations between two classifiers with default values for the
     * association ends and the association itself.
     * @param c1 The first classifier to connect
     * @param c2 The second classifier to connect
     * @return MAssociation
     */
    public MAssociation buildAssociation(MClassifier c1, MClassifier c2) {
	return this.buildAssociation(c1, true, c2, true);
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
    	MAssociationEnd end = new MAssociationEndImpl();
    	if (assoc != null && type != null) {
    		end.setAssociation(assoc);
    		end.setNamespace(assoc.getNamespace());
    		assoc.addConnection(end);
    		end.setType(type);
    	} else {
    		// this should never happen
    		cat.fatal("Tried to create associationend without association");
    		
    	}
    	if (name != null && name.length() > 0) {
    		end.setName(name);
    	} else {
    		end.setName("");
    	}
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

    /**
     * Builds a default binary association with two default association ends. 
     * @param c1 The first classifier to connect to
     * @param nav1 The navigability of the Associaton end
     * @param c2 The second classifier to connect to
     * @param nav2 The navigability of the second Associaton end
     * @return MAssociation
     */
    public MAssociation buildAssociation(MClassifier c1, boolean nav1, MClassifier c2, boolean nav2) {
    	MAssociation assoc = new MAssociationImpl();
    	assoc.setName("");
    	buildAssociationEnd(assoc, null, c1,null, null, nav1, null, null, null, null, null);
    	buildAssociationEnd(assoc, null, c2,null, null, nav2, null, null, null, null, null);
        assoc.setUUID(UUIDManager.SINGLETON.getNewUUID());
		return assoc;
	}

	public MGeneralization buildGeneralization(MGeneralizableElement child, MGeneralizableElement parent) {
	    if (parent.getParents().contains(child)) return null;

		MGeneralization gen = new MGeneralizationImpl();
		gen.setParent(parent);
		gen.setChild(child);
        gen.setUUID(UUIDManager.SINGLETON.getNewUUID());
		if (parent.getNamespace() != null) gen.setNamespace(parent.getNamespace());
		else if (child.getNamespace() != null) gen.setNamespace(child.getNamespace());
		return gen;
	}

    /**
     * <p>Build an extend relationship.</p>
     *
     * <p>Set the namespace to the base (preferred) or else extension's
     *   namespace. We don't do any checking on base and extension. They should
     *   be different, but that is someone else's problem.</p>
     *
     * @param base       The base use case for the relationship
     *
     * @param extension  The extension use case for the relationship
     *
     * @return           The new extend relationship or <code>null</code> if it
     *                   can't be created.
     */

     public MExtend buildExtend(MUseCase base, MUseCase extension) {

         MExtend extend = new MExtendImpl();
         extend.setUUID(UUIDManager.SINGLETON.getNewUUID());

         // Set the ends

         extend.setBase(base);
         extend.setExtension(extension);

         // Set the namespace to that of the base as first choice, or that of
         // the extension as second choice.

         if (base.getNamespace() != null) {
             extend.setNamespace(base.getNamespace());
         }
         else if (extension.getNamespace() != null) {
             extend.setNamespace(extension.getNamespace());
         }

         return extend;
     }


    /**
     * <p>Build an extension point for a use case.</p>
     *
     * <p>Set the namespace to that of the use case if possible.</p>
     *
     * @param useCase  The owning use case for the extension point. May be
     *                 <code>null</code>.
     *
     * @return         The new extension point or <code>null</code> if it
     *                 can't be created.
     */

     public MExtensionPoint buildExtensionPoint(MUseCase useCase) {

         MExtensionPoint extensionPoint = new MExtensionPointImpl();
         extensionPoint.setUUID(UUIDManager.SINGLETON.getNewUUID());

         // Set the owning use case if there is one given.

         if (useCase != null) {

             extensionPoint.setUseCase(useCase);

             // Set the namespace to that of the useCase if possible.

             if (useCase.getNamespace() != null) {
                 extensionPoint.setNamespace(useCase.getNamespace());
             }
         }

         // For consistency with attribute and operation, give it a default
         // name and location

         extensionPoint.setName("newEP");
         extensionPoint.setLocation("loc");

         return extensionPoint;
     }


    /**
     * <p>Build an include relationship.</p>
     *
     * <p>Set the namespace to the base (preferred) or else extension's
     *   namespace. We don't do any checking on base and extension. They should
     *   be different, but that is someone else's problem.</p>
     *
     * <p><em>Note</em>. There is a bug in NSUML that gets the base and
     *   addition associations back to front. We reverse the use of their
     *   accessors in the code to correct this.</p>
     *
     * @param base       The base use case for the relationship
     *
     * @param extension  The extension use case for the relationship
     *
     * @return           The new include relationship or <code>null</code> if
     *                   it can't be created.
     */

     public MInclude buildInclude(MUseCase base, MUseCase addition) {

         MInclude include = new MIncludeImpl();
         include.setUUID(UUIDManager.SINGLETON.getNewUUID());

         // Set the ends. Because of the NSUML bug we reverse the accessors
         // here.

         include.setAddition(base);
         include.setBase(addition);

         // Set the namespace to that of the base as first choice, or that of
         // the addition as second choice.

         if (base.getNamespace() != null) {
             include.setNamespace(base.getNamespace());
         }
         else if (addition.getNamespace() != null) {
             include.setNamespace(addition.getNamespace());
         }

         return include;
     }


	public MDependency buildDependency(MModelElement client, MModelElement supplier) {
		MDependency dep = new MDependencyImpl();
        dep.setUUID(UUIDManager.SINGLETON.getNewUUID());
		dep.addSupplier(supplier);
		dep.addClient(client);
		if (supplier.getNamespace() != null) dep.setNamespace(supplier.getNamespace());
		else if (client.getNamespace() != null) dep.setNamespace(client.getNamespace());
		return dep;
	}

	public MAbstraction buildRealization(MModelElement client, MModelElement supplier) {
		MAbstraction realization = new MAbstractionImpl();
        realization.setUUID(UUIDManager.SINGLETON.getNewUUID());
		// 2002-07-13
		// Jaap Branderhorst
		// need a singleton for the stereotype.
		// if the stereotype is allready on the model we should use this
		// otherwise create a new one
		// lets get the manager of the stereotypes (the namespace)
		// we presume that client and supplier live in the same namespace
		MStereotype realStereo = null;
		
		MNamespace namespace = supplier.getNamespace();
		if (namespace == null) {
			namespace = client.getNamespace();
		}
		
		if (namespace != null) {
			realStereo = (MStereotype)namespace.lookup("realize");
			
		}	
		if (realStereo == null) { // no stereotype yet
			realStereo = new MStereotypeImpl();
			realStereo.setName("realize");
			realStereo.setUUID(UUIDManager.SINGLETON.getNewUUID());
		}
		
		if (namespace != null) {
			realStereo.setNamespace(namespace);
			realization.setNamespace(namespace);
		}
		
		
		// next two lines were commented out earlier
		// MStereotype realStereo = (MStereotype)STANDARDS.lookup("realize");
    	// System.out.println("real ist: "+realStereo);
    	// commented next two lines out at change 2002-07-13 (Jaap Branderhorst)
		// MStereotype realStereo = new MStereotypeImpl();
		// realStereo.setName("realize");
		// 2002-07-12
		// Jaap Branderhorst
		// added next line to keep GUI and model in sync and to keep a complete model.
		realStereo.addExtendedElement(realization);
		// 2002-07-13
		// Jaap Branderhorst
		// next piece of code was replaced because i needed the namespace earlier on, commented out
		/*
		if (supplier.getNamespace() != null) {
		    MNamespace ns = supplier.getNamespace();
		    realization.setNamespace(ns);
		    // realStereo.setNamespace(ns);
		    
		    //		    ns.addOwnedElement(STANDARDS);
		}
		else if (client.getNamespace() != null) {
		    MNamespace ns = client.getNamespace();
		    realization.setNamespace(ns);
		    realStereo.setNamespace(ns);
		    //		    ns.addOwnedElement(STANDARDS);
		}
		*/
		realization.setStereotype(realStereo);
		realization.addSupplier(supplier);
		realization.addClient(client);
		return realization;
	}

	public MBinding buildBinding(MModelElement client, MModelElement supplier) {
		MBinding binding = new MBindingImpl();
        binding.setUUID(UUIDManager.SINGLETON.getNewUUID());
		binding.addSupplier(supplier);
		binding.addClient(client);
		if (supplier.getNamespace() != null) binding.setNamespace(supplier.getNamespace());
		else if (client.getNamespace() != null) binding.setNamespace(client.getNamespace());
		return binding;
	}

	public MUsage buildUsage(MModelElement client, MModelElement supplier) {
	    MUsage usage = new MUsageImpl();
        usage.setUUID(UUIDManager.SINGLETON.getNewUUID());
		usage.addSupplier(supplier);
		usage.addClient(client);
		if (supplier.getNamespace() != null) usage.setNamespace(supplier.getNamespace());
		else if (client.getNamespace() != null) usage.setNamespace(client.getNamespace());
		return usage;
	}

    /**
     * Constructs a default parameter and adds it to oper.
     *
     * @param oper 	The operation where it is added to. 
     *			If null, it is not added.
     * @return 		The newly created parameter.
     */
    public MParameter buildParameter() {
	ProjectBrowser pb = ProjectBrowser.TheInstance;
	Project p = pb.getProject();
	MClassifier voidType = p.findType("void");

	MParameter res = new MParameterImpl();
    res.setUUID(UUIDManager.SINGLETON.getNewUUID());
	res.setName(null);
	res.setStereotype(null);
	res.setType(voidType);
	res.setKind(MParameterDirectionKind.IN);
	res.setDefaultValue(null);

	return res;
    }

    public MParameter buildParameter(MBehavioralFeature oper) {
	MParameter res = buildParameter();

	if (oper != null)
	    oper.addParameter(res);
	return res;
    }

    public MParameter buildParameter(MEvent oper) {
	MParameter res = buildParameter();

	if (oper != null)
	    oper.addParameter(res);
	return res;
    }

    public MOperation buildOperation() {
	//build the default operation
	MOperation oper = new MOperationImpl();
    oper.setUUID(UUIDManager.SINGLETON.getNewUUID());
	oper.setName("newOperation");
	oper.setStereotype(null);
	oper.setOwner(null);
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

    public MOperation buildOperation(MClassifier cls) {
	MOperation oper = buildOperation();

	cls.addFeature(oper);
	oper.setOwner(cls);
	return oper;
    }


    /**
     * Builds the default attribute.
     * @return MAttribute
     */
    public MAttribute buildAttribute() {
	//build the default attribute
	ProjectBrowser pb = ProjectBrowser.TheInstance;
	Project p = pb.getProject();
	MClassifier intType = p.findType("int");

	MAttribute attr = new MAttributeImpl();
    attr.setUUID(UUIDManager.SINGLETON.getNewUUID());
	attr.setName("newAttr");
	attr.setMultiplicity(new MMultiplicity(1, 1));
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

    public MAttribute buildAttribute(MClassifier cls) {
	MAttribute attr = buildAttribute();
	cls.addFeature(attr);
	return attr;
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
		MMethod method = new MMethodImpl();
        method.setUUID(UUIDManager.SINGLETON.getNewUUID());
		if (op != null) {
			method.setSpecification(op);
			MClassifier owner = op.getOwner();
			if (owner != null) {
				method.setOwner(owner);
			}
		}
		if (notation != null && notation.getName() != null) {
			method.setBody(new MProcedureExpression(notation.getName(), body));
		}
		return method;
	}	

    public MMessage buildMessage(MAssociationRole ar){
	return buildMessage(ar, "");
    }

    public MMessage buildMessage(MAssociationRole ar,String sequenceNumber){

	MMessage msg = new MMessageImpl();
    msg.setUUID(UUIDManager.SINGLETON.getNewUUID());
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
    action.setUUID(UUIDManager.SINGLETON.getNewUUID());
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


    /** 
     * <p>This method returns all extension points of a given use case.
     *
     * <p>Here for completeness, but actually just a wrapper for the NSUML
     *   function.</p>
     *
     * @param useCase  The use case for which we want the extension points.
     *
     * @return         A collection of the extension points.
     */

    public Collection getExtensionPoints(MUseCase useCase) {

        return useCase.getExtensionPoints();
    }
    
    
}
