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

import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.uml.AbstractUmlModelFactory;
import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.behavior.collaborations.MAssociationEndRole;
import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.behavior.collaborations.MCollaboration;
import ru.novosoft.uml.behavior.collaborations.MInteraction;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;

/**
 * Factory to create UML classes for the UML
 * BehaviorialElements::Collaborations package.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 * @stereotype singleton
 */
public class CollaborationsFactory extends AbstractUmlModelFactory {

    /** Singleton instance.
     */
    private static CollaborationsFactory SINGLETON =
                   new CollaborationsFactory();

    /** Singleton instance access method.
     */
    public static CollaborationsFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation
     */
    private CollaborationsFactory() {
    }

    /** Create an empty but initialized instance of a UML AssociationEndRole.
     *  
     *  @return an initialized UML AssociationEndRole instance.
     */
    public MAssociationEndRole createAssociationEndRole() {
        MAssociationEndRole modelElement = MFactory.getDefaultFactory().createAssociationEndRole();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML AssociationRole.
     *  
     *  @return an initialized UML AssociationRole instance.
     */
    public MAssociationRole createAssociationRole() {
        MAssociationRole modelElement = MFactory.getDefaultFactory().createAssociationRole();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML ClassifierRole.
     *  
     *  @return an initialized UML ClassifierRole instance.
     */
    public MClassifierRole createClassifierRole() {
        MClassifierRole modelElement = MFactory.getDefaultFactory().createClassifierRole();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Collaboration.
     *  
     *  @return an initialized UML Collaboration instance.
     */
    public MCollaboration createCollaboration() {
        MCollaboration modelElement = MFactory.getDefaultFactory().createCollaboration();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Interaction.
     *  
     *  @return an initialized UML Interaction instance.
     */
    public MInteraction createInteraction() {
        MInteraction modelElement = MFactory.getDefaultFactory().createInteraction();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Message.
     *  
     *  @return an initialized UML Message instance.
     */
    public MMessage createMessage() {
        MMessage modelElement = MFactory.getDefaultFactory().createMessage();
	super.initialize(modelElement);
	return modelElement;
    }
    
    /**
     * Builds a default collaboration not attached to a classifier
     */
    public MCollaboration buildCollaboration(MNamespace namespace) {
    	MCollaboration modelelement = createCollaboration();
    	modelelement.setNamespace(namespace);
    	modelelement.setName("newCollaboration");
    	modelelement.setAbstract(false);
        if (namespace instanceof MClassifier) {
            modelelement.setRepresentedClassifier((MClassifier)namespace);
        }
    	return modelelement;
    }
    
    /**
     * Builds an interaction belonging to some collaboration
     */
    public MInteraction buildInteraction(MCollaboration collab) {
    	MInteraction inter = createInteraction();
    	inter.setContext(collab);
    	inter.setName("newInteraction");
    	return inter;
    }
    
    /**
     * Builds an associationendrole based on some classifierrole
     */
    public MAssociationEndRole buildAssociationEndRole(MClassifierRole type) {
    	MAssociationEndRole end = createAssociationEndRole();
    	end.setType(type);
    	return end;
    }
    
    /**
     * Builds a binary associationrole on basis of two classifierroles
     */
    public MAssociationRole buildAssociationRole(MClassifierRole from, MClassifierRole to) {
    	MCollaboration colFrom = (MCollaboration)from.getNamespace();
    	MCollaboration colTo = (MCollaboration)to.getNamespace();
    	if (colFrom != null && colFrom.equals(colTo)) {
    		MAssociationRole role = createAssociationRole();
    		// we do not create on basis of associations between the bases of the classifierroles
    		role.addConnection(buildAssociationEndRole(from));
    		role.addConnection(buildAssociationEndRole(to));
                colFrom.addOwnedElement(role);
    		return role;
    	}
    	return null;
    }

    /**
     * Builds a binary associationrole on basis of two classifierroles,
     * navigation and aggregation
     */
    public MAssociationRole buildAssociationRole(
            MClassifierRole from,
            boolean nav1,
            MAggregationKind agg1,
            MClassifierRole to, 
            boolean nav2,
            MAggregationKind agg2) {
    	MCollaboration colFrom = (MCollaboration)from.getNamespace();
    	MCollaboration colTo = (MCollaboration)to.getNamespace();
    	if (colFrom != null && colFrom.equals(colTo)) {
    		MAssociationRole role = createAssociationRole();
    		// we do not create on basis of associations between the bases of the classifierroles
                MAssociationEndRole fromEnd = buildAssociationEndRole(from);
                fromEnd.setNavigable(nav1);
                fromEnd.setAggregation(agg1);
    		role.addConnection(fromEnd);
                
                MAssociationEndRole toEnd = buildAssociationEndRole(to);
                toEnd.setNavigable(nav2);
                toEnd.setAggregation(agg2);
    		role.addConnection(toEnd);
                
                colFrom.addOwnedElement(role);
    		return role;
    	}
    	return null;
    }

    /**
     * Builds a message within some interaction related to some assocationrole. The message
     * is added as the last in the interaction sequence. Furthermore, the message is added as the last
     * to the list of messages allready attached to the role. Effectively, the allready attached messages
     * become predecessors of this message.
     */
    public MMessage buildMessage(MInteraction inter, MAssociationRole role) {
	if (inter == null || role == null)
	   return null;

	MMessage message = createMessage();

	inter.addMessage(message);
        
	message.setCommunicationConnection(role);

	if (role.getConnections().size() == 2) {
	    message.setSender((MClassifierRole)role.getConnection(0).getType());
	    message.setReceiver((MClassifierRole)role.getConnection(1).getType());

	    Collection messages = message.getSender().getMessages1();
	    MMessage lastMsg = lastMessage(messages, message);

	    if (lastMsg != null) {
                message.setActivator(lastMsg);
                messages = lastMsg.getMessages4();
	    } else {
		messages = message.getSender().getMessages2();
	    }
            
	    lastMsg = lastMessage(messages, message);
	    if (lastMsg != null)
		message.addPredecessor(findEnd(lastMsg));
        
	}

	return message;
    }

    /**
     * Finds the last message in the collection not equal to null and not
     * equal to m.
     *
     * @param c A collection containing exclusively MMessages.
     * @param m A MMessage.
     * @return The last message in the collection, or null.
     */
    private MMessage lastMessage(Collection c, MMessage m) {
	MMessage last = null;
	Iterator it = c.iterator();
	while (it.hasNext()) {
	    MMessage msg = (MMessage) it.next();
	    if (msg != null && msg != m)
		last = msg;
	}
	return last;
    }

    /**
     * Walks the tree of successors to m rooted until a leaf is found. The
     * leaf is the returned. If m is itself a leaf, then m is returned.
     *
     * @param m A MMessage.
     * @return The last message in one branch of the tree rooted at m.
     */
    private MMessage findEnd(MMessage m) {
	while (true) {
	    Collection c = m.getMessages3();
	    Iterator it = c.iterator();
	    if (!it.hasNext())
		return m;
	    m = (MMessage) it.next();
	}
    }

    /**
     * Builds a message within some collaboration. The message is added to the first interaction inside
     * the collaboration. If there is no interaction yet, one is build.
     */
    public MMessage buildMessage(MCollaboration collab, MAssociationRole role) {
    	MInteraction inter = null;
    	if (collab.getInteractions().size() == 0) {
    		inter = buildInteraction(collab);
    	} else {
    		inter = (MInteraction)(collab.getInteractions().toArray())[0];
    	}
    	return buildMessage(inter, role);
    }
    
    /**
     * Builds an activator for some message
     */
    public MMessage buildActivator(MMessage owner, MInteraction interaction) {
    	if (owner == null) return null;
    	if (interaction == null) interaction = owner.getInteraction();
    	if (interaction == null) return null;
    	
    	MMessage activator = createMessage();
    	activator.setInteraction(interaction);
    	owner.setActivator(activator);
    	return activator;
    }
    
    public void deleteAssociationEndRole(MAssociationEndRole elem) {}
    
    public void deleteAssociationRole(MAssociationRole elem) {
        Iterator it = elem.getMessages().iterator();
        while (it.hasNext()) {
            UmlFactory.getFactory().delete((MMessage)it.next());
        }
    }
    
    public void deleteClassifierRole(MClassifierRole elem) {}
    
    public void deleteCollaboration(MCollaboration elem) {}
    
    public void deleteInteraction(MInteraction elem) {}
    
    public void deleteMessage(MMessage elem) {}
    

}

