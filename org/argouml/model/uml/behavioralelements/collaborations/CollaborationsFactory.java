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
     * Builds a message with an empty string as sequence number.
     * @param ar
     * @return MMessage
     */
    public MMessage buildMessage(MAssociationRole ar){
    return buildMessage(ar, "");
    }
    
    /**
     * Builds a message with a given sequencenumber for a given associationrole.
     * @param ar
     * @param sequenceNumber
     * @return MMessage
     */
    public MMessage buildMessage(MAssociationRole ar,String sequenceNumber){
	
	    MMessage msg = UmlFactory.getFactory().getCollaborations().createMessage();
	    msg.setName(sequenceNumber);
	    Collection ascEnds = ar.getConnections();
	
	    // next line poses a problem. N-array associations are not supported in this
	    // way
	    // TODO implement this for N-array associations.
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
	
	    // TODO: correct the creation of the CallAction. This is probably the wrong 
	    // element.
	    MCallAction action = UmlFactory.getFactory().getCommonBehavior().createCallAction();
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
     * Builds a default collaborationa representing some classifier
     */
    public MCollaboration buildCollaboration(MClassifier classifier) {
    	MCollaboration modelelement = buildCollaboration(classifier.getNamespace());
    	modelelement.setRepresentedClassifier(classifier);
    	return modelelement;
    }



}

