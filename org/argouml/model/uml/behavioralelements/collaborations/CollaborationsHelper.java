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

package org.argouml.model.uml.behavioralelements.collaborations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import ru.novosoft.uml.behavior.collaborations.MAssociationEndRole;
import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.behavior.collaborations.MCollaboration;
import ru.novosoft.uml.behavior.collaborations.MInteraction;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MFeature;
import ru.novosoft.uml.foundation.core.MGeneralizableElement;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;

/**
 * Helper class for UML BehavioralElements::Collaborations Package.
 *
 * Current implementation is a placeholder.
 * 
 * @since ARGO0.11.2
 * @author Thierry Lach
 * @stereotype singleton
 */
public class CollaborationsHelper {

    /** Don't allow instantiation.
     */
    private CollaborationsHelper() {
    }
    
     /** Singleton instance.
     */
    private static CollaborationsHelper SINGLETON =
                   new CollaborationsHelper();

    
    /** Singleton instance access method.
     */
    public static CollaborationsHelper getHelper() {
        return SINGLETON;
    }
    
    /**
	 * Returns all classifierroles found in the projectbrowser model
	 * @return Collection
	 */
	public Collection getAllClassifierRoles() {
		MNamespace model = ProjectManager.getManager().getCurrentProject().getModel();
		return getAllClassifierRoles(model);
	}
	
	/**
	 * Returns all classifierroles found in this namespace and in its children
	 * @return Collection
	 */
	public Collection getAllClassifierRoles(MNamespace ns) {
		Iterator it = ns.getOwnedElements().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof MNamespace) {
				list.addAll(getAllClassifierRoles((MNamespace)o));
			} 
			if (o instanceof MClassifierRole) {
				list.add(o);
			}
			
		}
		return list;
	}
	
	/**
	 * Returns all associations the bases of the classifierrole has, thereby forming
	 * the set of associationroles the classifierrole can use. UML Spec 1.3
	 * section 2.10.3.3
	 * @param role
	 * @return Collection
	 */
	public Collection getAllPossibleAssociationRoles(MClassifierRole role) {
		if (role == null || role.getBases().isEmpty()) return new ArrayList();
		Iterator it = role.getBases().iterator();
		Set associations = new HashSet();
		while (it.hasNext()) {
			MClassifier base = (MClassifier)it.next();
			associations.addAll(CoreHelper.getHelper().getAssociations(base));
		}
		return associations;
	}
	
	/**
	 * Returns all classifierroles associated via associationroles to some 
	 * classifierrole role
	 * @param role
	 * @return Collection
	 */
	public Collection getClassifierRoles(MClassifierRole role) {
		if (role == null) return new ArrayList();
		List roles = new ArrayList();
		Iterator it = role.getAssociationEnds().iterator();
		while (it.hasNext()) {
			MAssociationEnd end = (MAssociationEnd)it.next();
			if (end instanceof MAssociationEndRole) {
				MAssociation assoc = end.getAssociation();
				Iterator it2 = assoc.getConnections().iterator();
				while (it2.hasNext()) {
					MAssociationEnd end2 = (MAssociationEnd)it2.next();
					MClassifier classifier = end2.getType();
					if (classifier != role && classifier instanceof MClassifierRole) {
						roles.add(classifier);
					}
				}
			}
		}
		return roles;
	}
	
	/**
	 * Returns the first found associationrole between two classifierroles.
	 * @param from
	 * @param to
	 * @return MAssociationRole
	 */
	public MAssociationRole getAssocationRole(MClassifierRole from, MClassifierRole to) {
		if (from == null || to == null) return null;
		Iterator it = from.getAssociationEnds().iterator();
		while (it.hasNext()) {
			MAssociationEnd end = (MAssociationEnd)it.next();
			if (end instanceof MAssociationEndRole) {
				MAssociation assoc = end.getAssociation();
				Iterator it2 = assoc.getConnections().iterator();
				while (it2.hasNext()) {
					MAssociationEnd end2 = (MAssociationEnd)it2.next();
					MClassifier classifier = end2.getType();
					if (classifier == to) {
						return (MAssociationRole)assoc;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns all possible activators for some message mes. The possible
	 * activators are all messages in the same interaction as the given message
	 * that are not part of the predecessors of the message and that are
	 * not equal to the given message.
	 * @param mes
	 * @return Collection
	 */
	public Collection getAllPossibleActivators(MMessage mes) {
		if (mes == null || mes.getInteraction() == null) return new ArrayList();
		MInteraction inter = mes.getInteraction();                              
		Collection predecessors = mes.getPredecessors();
		Collection allMessages = inter.getMessages();                
		Iterator it = allMessages.iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			Object o = it.next();
			if (!predecessors.contains(o) && mes!=o && !hasAsActivator((MMessage)o, mes) && !((MMessage)o).getPredecessors().contains(mes)) {
				list.add(o);
			}
		}
		return list;
	}
	
    /**
     * Returns true if the given message has the message activator somewhere as it's activator. This is defined
     * as that the message activator can be the activator itself of the given message OR that the given activator
     * can be the activator of the activator of the given message (recursive) OR that the given activator is part
     * of the predecessors of the activator of the given message (recursive too).
     * @param message
     * @param activator
     * @return boolean
     */
	public boolean hasAsActivator(MMessage message, MMessage activator) {
		if (message.getActivator() == null) return false;
		if (message.getActivator() == activator || message.getActivator().getPredecessors().contains(activator)) return true;
		else 
			return hasAsActivator(message.getActivator(), activator);
	}
	
    /**
     * Sets the activator of some given message mes. Checks the wellformednessrules as defined in the UML 1.3 spec
     * in section 2.10.3.6, will throw an illegalargumentexception if the wellformednessrules are not met.
     * Not only sets the activator for the given message mes but also for the predecessors of mes. This is done
     * since it can not be the case that a list of messages (defined by the predecessor) has a different activator.
     * @param mes
     * @param activator
     */
	public void setActivator(MMessage mes, MMessage activator) {
		if (mes == null || activator == null) throw new IllegalArgumentException("In setActivator: either message or activator is null");
		if (mes == activator) throw new IllegalArgumentException("In setActivator: message may not be equal to activator");
		if (mes.getInteraction() != activator.getInteraction()) throw new IllegalArgumentException("In setActivator: interaction of message should equal interaction of activator");
		if (mes.getPredecessors().contains(activator)) throw new IllegalArgumentException("In setActivator: the predecessors of the message may not contain the activator"); 
		// we must find out if the activator itself does not have message as it's activator
		if (hasAsActivator(activator, mes)) throw new IllegalArgumentException("In setActivator: message may not be the activator for the original activator");
		List listToChange = new ArrayList();
		Collection predecessors = mes.getPredecessors();
		listToChange.addAll(predecessors);
		listToChange.add(mes);
		MInteraction inter = mes.getInteraction();
		Collection allMessages = inter.getMessages();
		Iterator it = allMessages.iterator();
		while (it.hasNext()) {
			MMessage mes2 = (MMessage)it.next();
			if (mes2.getPredecessors().contains(mes)) {
				listToChange.add(mes2);
			}
		}
		it = listToChange.iterator();
		while (it.hasNext()) {
			MMessage mes2 = (MMessage)it.next();
			mes2.setActivator(activator);
		}
	}
    
    /**
     * Returns all possible predecessors for some message, taking into account
     * the wellformednessrules as defined in section 2.10 of the UML spec.
     * @param message
     * @return Collection
     */
    public Collection getAllPossiblePredecessors(MMessage message) {
        if (message == null) throw new IllegalArgumentException("In getAllPossiblePredecessors: argument message is null");
        MInteraction inter = message.getInteraction();
        Iterator it = inter.getMessages().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            MMessage mes = (MMessage)it.next();
            if (mes.getActivator() == message.getActivator() 
                && message != mes && !mes.getPredecessors().contains(message) 
                && !message.getPredecessors().contains(message)) {
                    list.add(mes);
            }
        }
        return list;
    }
    
    /**
     * Returns all possible bases for some classifierrole taking into account the 
     * wellformednessrules as defined in section 2.10.3 of the UML 1.3 spec.
     * @param role
     * @return Collection
     */
    public Collection getAllPossibleBases(MClassifierRole role) {
        if (role == null || role.getNamespace() == null) return new ArrayList();
        MCollaboration coll = (MCollaboration)role.getNamespace();
        MNamespace ns = coll.getNamespace();
        Collection returnList = ModelManagementHelper.getHelper().getAllModelElementsOfKind(ns, MClassifier.class);
        returnList.removeAll(ModelManagementHelper.getHelper().getAllModelElementsOfKind(ns, MClassifierRole.class));
        if (role.getName() == null || role.getName().equals("")) {
            List listToRemove = new ArrayList();
            Iterator it = returnList.iterator();
            while (it.hasNext()) {
                MClassifier clazz = (MClassifier)it.next();
                if (!clazz.getClassifierRoles().isEmpty()) {
                    Iterator it2 = clazz.getClassifierRoles().iterator();
                    while (it2.hasNext()) {
                        MClassifierRole role2 = (MClassifierRole)it2.next();
                        if (role2.getNamespace() == coll) {
                            listToRemove.add(clazz);
                        }
                    }
                }
            }
            returnList.removeAll(listToRemove);
        }                        
        return returnList;
    }
    
    /**
     * Adds a base to the given classifierrole. If the 
     * classifierrole does not have a name yet and there is only one base,
     * the name of the classifierrole is set to the name of the given base
     * according to the wellformednessrules of section 2.10.3 of the UML 1.3 
     * spec.
     * @param role
     * @param base
     */
    public void addBase(MClassifierRole role, MClassifier base) {
        if (role == null || base == null) throw new IllegalArgumentException("In addBase: either the role or the base is null");
        // wellformednessrule: if the role does not have a name, the role shall
        // be the only one with the particular base
        if (role.getName() == null || role.getName().equals("")) {
            MCollaboration collab = (MCollaboration)role.getNamespace();
            Collection roles = ModelManagementHelper.getHelper().getAllModelElementsOfKind(collab, MClassifierRole.class);
            Iterator it = roles.iterator();
            while (it.hasNext()) {
                if (((MClassifierRole)it.next()).getBases().contains(base)) 
                    throw new IllegalArgumentException("In addBase: base is allready part of another role and role does not have a name");
            }
        }
        role.addBase(base);
        if (role.getBases().size() == 1) {
            role.setAvailableContentses(base.getOwnedElements());
            role.setAvailableFeatures(base.getFeatures());
        } else {
            Iterator it = base.getOwnedElements().iterator();
            while (it.hasNext()) {
                MModelElement elem = (MModelElement)it.next();
                if (!role.getAvailableContentses().contains(elem)) {
                    role.addAvailableContents((MModelElement)it.next());
                }
            }
            it = base.getFeatures().iterator();
            while (it.hasNext()) {
                MFeature feature = (MFeature)it.next();
                if (!role.getAvailableFeatures().contains(feature)) {
                    role.addAvailableFeature((MFeature)it.next());
                }
            }
        }
       
    }
    
    /**
     * Sets the bases of the given classifierrole to the given collection bases.
     * @param role
     * @param bases
     */
    public void setBases(MClassifierRole role, Collection bases) {
        if (role == null || bases == null) throw new IllegalArgumentException("In setBases: either the role or the collection bases is null");
        Iterator it = role.getBases().iterator();
        while(it.hasNext()) {
            role.removeBase((MClassifier)it.next());
        }
        it = bases.iterator();
        while (it.hasNext()) {
            addBase(role, (MClassifier)it.next());
        }
    }
    
    /**
     * Returns all available features for a given classifierrole as defined in 
     * section 2.10.3.3 of the UML 1.3 spec. Does not use the standard getAvailableFeatures
     * method on ClassifierRole since this is derived information.
     * @param role
     * @return Collection
     */
    public Collection allAvailableFeatures(MClassifierRole role) {
        if (role == null) return new ArrayList();
        List returnList = new ArrayList();
        Iterator it = role.getParents().iterator();
        while (it.hasNext()) {
            MGeneralizableElement genElem = (MGeneralizableElement)it.next();
            if (genElem instanceof MClassifierRole) {
                returnList.addAll(allAvailableFeatures((MClassifierRole)genElem));
            }
        }
        it = role.getBases().iterator();
        while(it.hasNext()) {
            returnList.addAll(((MClassifier)it.next()).getFeatures());
        }
        return returnList;
    }
    
    /**
     * Returns all available contents for a given classifierrole as defined in 
     * section 2.10.3.3 of the UML 1.3 spec. Does not use the standard getAvailableContents
     * method on ClassifierRole since this is derived information.
     * @param role
     * @return Collection
     */
    public Collection allAvailableContents(MClassifierRole role) {
        if (role == null) return new ArrayList();
        List returnList = new ArrayList();
        Iterator it = role.getParents().iterator();
        while (it.hasNext()) {
            MGeneralizableElement genElem = (MGeneralizableElement)it.next();
            if (genElem instanceof MClassifierRole) {
                returnList.addAll(allAvailableContents((MClassifierRole)genElem));
            }
        }
        it = role.getBases().iterator();
        while(it.hasNext()) {
            returnList.addAll(((MClassifier)it.next()).getOwnedElements());
        }
        return returnList;
    }
    
    /**
     * Returns all possible bases for some associationrole taking into account the 
     * wellformednessrules as defined in section 2.10.3 of the UML 1.3 spec.
     * @param role
     * @return Collection
     */
    public Collection getAllPossibleBases(MAssociationRole role) {
        Set ret = new HashSet();
        if (role == null || role.getNamespace() == null) return ret;
         MCollaboration coll = (MCollaboration)role.getNamespace();
        
        // find the bases of the connected classifierroles so that we can see
        // what associations are between them. If there are bases then the
        // assocations between those bases form the possible bases. Otherwise
        // the bases are formed by all associations in the namespace of the 
        // collaboration
        Iterator it = role.getConnections().iterator();
        Set bases = new HashSet();
        while (it.hasNext()) {
            MAssociationEndRole end = (MAssociationEndRole)it.next();
            MClassifierRole type = (MClassifierRole)end.getType();
            if (type != null) {
                bases.addAll(type.getBases());
            }
        }
        if (bases.isEmpty()) {
            MNamespace ns = coll.getNamespace();
            ret.addAll(ModelManagementHelper.getHelper().getAllModelElementsOfKind(ns, MAssociation.class));
            ret.removeAll(ModelManagementHelper.getHelper().getAllModelElementsOfKind(ns, MAssociationRole.class));
        } else {  
            it = bases.iterator();
            while (it.hasNext()) {
                MClassifier base1 = (MClassifier)it.next();
                if (it.hasNext()) {
                    MClassifier base2 = (MClassifier)it.next();
                    ret.addAll(CoreHelper.getHelper().getAssociations(base1, base2));
                }
            }
        }        
        // if there is no name, the base may not be base for another associationrole
        if (role.getName() == null || role.getName().equals("")) {
            List listToRemove = new ArrayList();
            it = ret.iterator();
            while (it.hasNext()) {
                MAssociation assoc = (MAssociation)it.next();
                if (!assoc.getAssociationRoles().isEmpty()) {
                    Iterator it2 = assoc.getAssociationRoles().iterator();
                    while (it2.hasNext()) {
                        MAssociationRole role2 = (MAssociationRole)it2.next();
                        if (role2.getNamespace() == coll) {
                            listToRemove.add(assoc);
                        }
                    }
                }
            }
            ret.removeAll(listToRemove);
        }                 
        return ret;   
    } 
    
    /**
     * Sets the base of some associationrole, including the attached assocationendroles.
     * Checks for wellformedness first.
     * @param role
     * @param base
     */
    public void setBase(MAssociationRole role, MAssociation base) {
        if (role == null) throw  new IllegalArgumentException("role is null");
        if (base != null && !getAllPossibleBases(role).contains(base)) { 
            throw new IllegalArgumentException("base is not allowed for this role");
        }
        role.setBase(base);
        MClassifierRole sender = (MClassifierRole)CoreHelper.getHelper().getSource(role);
        MClassifierRole receiver = (MClassifierRole)CoreHelper.getHelper().getDestination(role);
        Collection senderBases = sender.getBases();
        Collection receiverBases = receiver.getBases();
        Collection baseConnections = base.getConnections();
        Iterator it = baseConnections.iterator();
        while (it.hasNext()) {
            MAssociationEnd end = (MAssociationEnd)it.next();
            if (senderBases.contains(end.getType())) {
                ((MAssociationEndRole)CoreHelper.getHelper().getAssociationEnd(sender, role)).setBase(end);
            } else
            if (receiverBases.contains(end.getType())) {
                ((MAssociationEndRole)CoreHelper.getHelper().getAssociationEnd(receiver, role)).setBase(end);
            }
        }
    }
            
		
}

