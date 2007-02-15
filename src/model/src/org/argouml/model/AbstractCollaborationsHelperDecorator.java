// $Id$
// Copyright (c) 2005-2007 The Regents of the University of California. All
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

package org.argouml.model;

import java.util.Collection;

/**
 * An abstract Decorator for the {@link CollaborationsHelper}.
 *
 * @author Bob Tarling
 */
public abstract class AbstractCollaborationsHelperDecorator
	implements CollaborationsHelper {

    /**
     * The component.
     */
    private CollaborationsHelper impl;


    /**
     * @param component The component to decorate.
     */
    AbstractCollaborationsHelperDecorator(CollaborationsHelper component) {
        impl = component;
    }

    /**
     * The component we are decorating.
     *
     * @return Returns the component.
     */
    protected CollaborationsHelper getComponent() {
        return impl;
    }

    /*
     * @see org.argouml.model.CollaborationsHelper#getAllClassifierRoles(java.lang.Object)
     */
    public Collection getAllClassifierRoles(Object ns) {
        return impl.getAllClassifierRoles(ns);
    }

    public Collection getAllPossibleAssociationRoles(Object role) {
        return impl.getAllPossibleAssociationRoles(role);
    }

    public Collection getClassifierRoles(Object role) {
        return impl.getClassifierRoles(role);
    }

    public Object getAssociationRole(Object afrom, Object ato) {
        return impl.getAssociationRole(afrom, ato);
    }

    public Collection getAllPossibleActivators(Object ames) {
        return impl.getAllPossibleActivators(ames);
    }

    public boolean hasAsActivator(Object message, Object activator) {
        return impl.hasAsActivator(message, activator);
    }

    public void setActivator(Object ames, Object anactivator) {
        impl.setActivator(ames, anactivator);
    }

    public Collection getAllPossiblePredecessors(Object amessage) {
        return impl.getAllPossiblePredecessors(amessage);
    }

    public void addBase(Object arole, Object abase) {
        impl.addBase(arole, abase);
    }

    public void setBases(Object role, Collection bases) {
        impl.setBases(role, bases);
    }

    public Collection allAvailableFeatures(Object arole) {
        return impl.allAvailableFeatures(arole);
    }

    public Collection allAvailableContents(Object arole) {
        return impl.allAvailableContents(arole);
    }

    public Collection getAllPossibleBases(Object role) {
        return impl.getAllPossibleBases(role);
    }

    public void setBase(Object arole, Object abase) {
        impl.setBase(arole, abase);
    }

    public boolean isAddingCollaborationAllowed(Object context) {
        return impl.isAddingCollaborationAllowed(context);
    }

    public void removeBase(Object handle, Object c) {
        impl.removeBase(handle, c);
    }

    public void removeConstrainingElement(Object handle, Object constraint) {
        impl.removeConstrainingElement(handle, constraint);
    }

    public void removeMessage(Object handle, Object message) {
        impl.removeMessage(handle, message);
    }

    public void removeMessage3(Object handle, Object mess) {
        impl.removeSuccessor(handle, mess);
    }

    public void removeSuccessor(Object handle, Object mess) {
        impl.removeSuccessor(handle, mess);
    }

    public void removePredecessor(Object handle, Object message) {
        impl.removePredecessor(handle, message);
    }

    public void addConstrainingElement(Object handle, Object constraint) {
        impl.addConstrainingElement(handle, constraint);
    }

    public void addInstance(Object classifierRole, Object instance) {
        impl.addInstance(classifierRole, instance);
    }

    public void addMessage(Object handle, Object elem) {
        impl.addMessage(handle, elem);
    }

    public void addSuccessor(Object handle, Object mess) {
        impl.addSuccessor(handle, mess);
    }

    public void addPredecessor(Object handle, Object predecessor) {
        impl.addPredecessor(handle, predecessor);
    }

    public void setAction(Object handle, Object action) {
        impl.setAction(handle, action);
    }

    public void setContext(Object handle, Object col) {
        impl.setContext(handle, col);
    }

    public void setSuccessors(Object handle, Collection messages) {
        impl.setSuccessors(handle, messages);
    }

    public void setPredecessors(Object handle, Collection predecessors) {
        impl.setPredecessors(handle, predecessors);
    }

    public void setRepresentedClassifier(Object handle, Object classifier) {
        impl.setRepresentedClassifier(handle, classifier);
    }

    public void setRepresentedOperation(Object handle, Object operation) {
        impl.setRepresentedOperation(handle, operation);
    }

    public void setSender(Object handle, Object sender) {
        impl.setSender(handle, sender);
    }

    public void removeInteraction(Object collab, Object interaction) {
        impl.removeInteraction(collab, interaction);
    }

}
