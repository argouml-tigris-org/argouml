// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

    /**
     * @see org.argouml.model.CollaborationsHelper#getAllClassifierRoles(
     *         java.lang.Object)
     */
    public Collection getAllClassifierRoles(Object ns) {
        return impl.getAllClassifierRoles(ns);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#getAllPossibleAssociationRoles(
     *         java.lang.Object)
     */
    public Collection getAllPossibleAssociationRoles(Object role) {
        return impl.getAllPossibleAssociationRoles(role);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#getClassifierRoles(
     *         java.lang.Object)
     */
    public Collection getClassifierRoles(Object role) {
        return impl.getClassifierRoles(role);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#getAssociationRole(
     *         java.lang.Object, java.lang.Object)
     */
    public Object getAssociationRole(Object afrom, Object ato) {
        return impl.getAssociationRole(afrom, ato);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#getAllPossibleActivators(
     *         java.lang.Object)
     */
    public Collection getAllPossibleActivators(Object ames) {
        return impl.getAllPossibleActivators(ames);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#hasAsActivator(
     *         java.lang.Object, java.lang.Object)
     */
    public boolean hasAsActivator(Object message, Object activator) {
        return impl.hasAsActivator(message, activator);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#setActivator(
     *         java.lang.Object, java.lang.Object)
     */
    public void setActivator(Object ames, Object anactivator) {
        impl.setActivator(ames, anactivator);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#getAllPossiblePredecessors(
     *         java.lang.Object)
     */
    public Collection getAllPossiblePredecessors(Object amessage) {
        return impl.getAllPossiblePredecessors(amessage);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#addBase(
     *         java.lang.Object, java.lang.Object)
     */
    public void addBase(Object arole, Object abase) {
        impl.addBase(arole, abase);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#setBases(
     *         java.lang.Object, java.util.Collection)
     */
    public void setBases(Object role, Collection bases) {
        impl.setBases(role, bases);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#allAvailableFeatures(
     *         java.lang.Object)
     */
    public Collection allAvailableFeatures(Object arole) {
        return impl.allAvailableFeatures(arole);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#allAvailableContents(
     *         java.lang.Object)
     */
    public Collection allAvailableContents(Object arole) {
        return impl.allAvailableContents(arole);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#getAllPossibleBases(
     *         java.lang.Object)
     */
    public Collection getAllPossibleBases(Object role) {
        return impl.getAllPossibleBases(role);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#setBase(
     *         java.lang.Object, java.lang.Object)
     */
    public void setBase(Object arole, Object abase) {
        impl.setBase(arole, abase);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#isAddingCollaborationAllowed(
     *         java.lang.Object)
     */
    public boolean isAddingCollaborationAllowed(Object context) {
        return impl.isAddingCollaborationAllowed(context);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#removeBase(
     *         java.lang.Object, java.lang.Object)
     */
    public void removeBase(Object handle, Object c) {
        impl.removeBase(handle, c);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#removeConstrainingElement(
     *         java.lang.Object, java.lang.Object)
     */
    public void removeConstrainingElement(Object handle, Object constraint) {
        impl.removeConstrainingElement(handle, constraint);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#removeMessage(
     *         java.lang.Object, java.lang.Object)
     */
    public void removeMessage(Object handle, Object message) {
        impl.removeMessage(handle, message);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#removeMessage3(
     *         java.lang.Object, java.lang.Object)
     */
    public void removeMessage3(Object handle, Object mess) {
        impl.removeMessage3(handle, mess);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#removePredecessor(
     *         java.lang.Object, java.lang.Object)
     */
    public void removePredecessor(Object handle, Object message) {
        impl.removePredecessor(handle, message);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#addConstrainingElement(
     *         java.lang.Object, java.lang.Object)
     */
    public void addConstrainingElement(Object handle, Object constraint) {
        impl.addConstrainingElement(handle, constraint);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#addInstance(
     *         java.lang.Object, java.lang.Object)
     */
    public void addInstance(Object classifierRole, Object instance) {
        impl.addInstance(classifierRole, instance);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#addMessage(
     *         java.lang.Object, java.lang.Object)
     */
    public void addMessage(Object handle, Object elem) {
        impl.addMessage(handle, elem);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#addMessage3(
     *         java.lang.Object, java.lang.Object)
     */
    public void addMessage3(Object handle, Object mess) {
        impl.addMessage3(handle, mess);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#addPredecessor(
     *         java.lang.Object, java.lang.Object)
     */
    public void addPredecessor(Object handle, Object predecessor) {
        impl.addPredecessor(handle, predecessor);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#setAction(
     *         java.lang.Object, java.lang.Object)
     */
    public void setAction(Object handle, Object action) {
        impl.setAction(handle, action);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#setContext(
     *         java.lang.Object, java.lang.Object)
     */
    public void setContext(Object handle, Object col) {
        impl.setContext(handle, col);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#setMessages3(
     *         java.lang.Object, java.util.Collection)
     */
    public void setMessages3(Object handle, Collection messages) {
        impl.setMessages3(handle, messages);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#setPredecessors(
     *         java.lang.Object, java.util.Collection)
     */
    public void setPredecessors(Object handle, Collection predecessors) {
        impl.setPredecessors(handle, predecessors);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#setRepresentedClassifier(
     *         java.lang.Object, java.lang.Object)
     */
    public void setRepresentedClassifier(Object handle, Object classifier) {
        impl.setRepresentedClassifier(handle, classifier);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#setRepresentedOperation(
     *         java.lang.Object, java.lang.Object)
     */
    public void setRepresentedOperation(Object handle, Object operation) {
        impl.setRepresentedOperation(handle, operation);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#setSender(
     *         java.lang.Object, java.lang.Object)
     */
    public void setSender(Object handle, Object sender) {
        impl.setSender(handle, sender);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#removeInteraction(
     *         java.lang.Object, java.lang.Object)
     */
    public void removeInteraction(Object collab, Object interaction) {
        impl.removeInteraction(collab, interaction);
    }

}
