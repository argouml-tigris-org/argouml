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
import org.argouml.ui.ProjectBrowser;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.behavior.collaborations.MAssociationEndRole;
import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.behavior.collaborations.MCollaboration;
import ru.novosoft.uml.behavior.collaborations.MInteraction;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MCallAction;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MNamespace;

/**
 * Factory to create UML classes for the UML
 * BehaviorialElements::Collaborations package.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
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
    	return modelelement;
    }
    
    /** 
     * Builds a default collaboration representing some classifier
     */
    public MCollaboration buildCollaboration(MClassifier classifier) {
    	MCollaboration modelelement = buildCollaboration(classifier.getNamespace());
    	modelelement.setRepresentedClassifier(classifier);
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
     * Builds an associationrole with some association as base and with some collaboration as owner
     */
    public MAssociationRole buildAssociationRole(MAssociation base, MCollaboration owner) {
    	MAssociationRole role = createAssociationRole();
    	Iterator it = base.getConnections().iterator();
    	while (it.hasNext()) {
    		role.addConnection(buildAssociationEndRole((MAssociationEnd)it.next()));
    	}
    	role.setBase(base);
    	return role;
    }
    
    /**
     * Builds an associationendrole based on some associationend
     */
    public MAssociationEndRole buildAssociationEndRole(MAssociationEnd base) {
    	MAssociationEndRole end = createAssociationEndRole();
    	end.setBase(base);
    	return end;
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
    		return role;
    	}
    	return null;
    }
    
    /**
     * Builds a message within some interaction related to some assocationrole. The message
     * is added as the last in the interaction sequence.
     */
    public MMessage buildMessage(MInteraction inter, MAssociationRole role) {
    	MMessage message = createMessage();
    	if (inter != null && role != null) {
    		Collection messages = inter.getMessages();
    		inter.addMessage(message);
    		message.setPredecessors(messages);
    		message.setCommunicationConnection(role);
    		if (role.getConnections().size() == 2) {
    			message.setSender((MClassifierRole)role.getConnection(0).getType());
    			message.setReceiver((MClassifierRole)role.getConnection(1).getType());
    		}
    		return message;
    	}
    	return null;
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



}

