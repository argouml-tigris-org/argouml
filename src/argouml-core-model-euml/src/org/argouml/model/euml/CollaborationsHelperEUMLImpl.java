// $Id$
/***********************************************************************
 * Copyright (c) 2007,2010 Tom Morris and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial API and implementation
 ***********************************************************************/
package org.argouml.model.euml;

import java.util.Collection;

import org.argouml.model.CollaborationsHelper;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageSort;

/**
 * Eclipse UML2 implementation of CollaborationsHelper.
 */
class CollaborationsHelperEUMLImpl implements CollaborationsHelper {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     *
     * @param implementation The ModelImplementation.
     */
    public CollaborationsHelperEUMLImpl(
            EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public void addBase(Object arole, Object abase) {
        // TODO: Auto-generated method stub
        
    }

    public void addConstrainingElement(Object handle, Object constraint) {
        // TODO: Auto-generated method stub
        
    }

    public void addInstance(Object classifierRole, Object instance) {
        // TODO: Auto-generated method stub
        
    }

    public void addMessage(Object handle, Object elem) {
        // TODO: Auto-generated method stub
        
    }

    public void addPredecessor(Object handle, Object predecessor) {
        // TODO: Auto-generated method stub
        
    }

    public void addSuccessor(Object handle, Object mess) {
        // TODO: Auto-generated method stub
        
    }

    public Collection allAvailableContents(Object arole) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Collection allAvailableFeatures(Object arole) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Collection getAllClassifierRoles(Object ns) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Collection getAllPossibleActivators(Object ames) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Collection getAllPossibleAssociationRoles(Object role) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Collection getAllPossibleBases(Object role) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Collection getAllPossiblePredecessors(Object amessage) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object getAssociationRole(Object afrom, Object ato) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Collection getClassifierRoles(Object role) {
        // TODO: Auto-generated method stub
        return null;
    }

    public boolean hasAsActivator(Object message, Object activator) {
        // TODO: Auto-generated method stub
        return false;
    }

    public boolean isAddingCollaborationAllowed(Object context) {
        // TODO: Auto-generated method stub
        return false;
    }

    public void removeBase(Object handle, Object c) {
        // TODO: Auto-generated method stub
        
    }

    public void removeConstrainingElement(Object handle, Object constraint) {
        // TODO: Auto-generated method stub
        
    }

    public void removeInteraction(Object collab, Object interaction) {
        // TODO: Auto-generated method stub
        
    }

    public void removeMessage(Object handle, Object message) {
        // TODO: Auto-generated method stub
        
    }

    public void removePredecessor(Object handle, Object message) {
        // TODO: Auto-generated method stub
        
    }

    public void removeSuccessor(Object handle, Object mess) {
        // TODO: Auto-generated method stub
        
    }

    public void setAction(Object message, Object action) {
        setMessageSort(message, action);
    }

    public void setActivator(Object ames, Object anactivator) {
        // TODO: Auto-generated method stub
        
    }

    public void setBase(Object arole, Object abase) {
        // TODO: Auto-generated method stub
        
    }

    public void setBases(Object role, Collection bases) {
        // TODO: Auto-generated method stub
        
    }

    public void setContext(Object handle, Object col) {
        // TODO: Auto-generated method stub
        
    }
    
    public void setMessageSort(Object message, Object messageSort) {
        MessageSort ms = (MessageSort) messageSort;
        Message m = (Message) message;
        m.setMessageSort(ms);
    }

    public void setPredecessors(Object handle, Collection predecessors) {
        // TODO: Auto-generated method stub
        
    }

    public void setRepresentedClassifier(Object handle, Object classifier) {
        // TODO: Auto-generated method stub
        
    }

    public void setRepresentedOperation(Object handle, Object operation) {
        // TODO: Auto-generated method stub
        
    }

    public void setSender(Object handle, Object sender) {
        // TODO: Auto-generated method stub
        
    }

    public void setSuccessors(Object handle, Collection messages) {
        // TODO: Auto-generated method stub
        
    }


}
