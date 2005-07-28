// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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
 * An abstract Decorator for the {@link UmlHelper}.
 *
 * @author Bob Tarling
 */
public abstract class AbstractUmlHelperDecorator implements UmlHelper {

    /**
     * The component.
     */
    private UmlHelper impl;


    /**
     * @param component The component to decorate.
     */
    public AbstractUmlHelperDecorator(UmlHelper component) {
        impl = component;
    }

    /**
     * @see org.argouml.model.UmlHelper#addListenersToModel(java.lang.Object)
     */
    public void addListenersToModel(Object model) {
        impl.addListenersToModel(model);
    }

    /**
     * @see org.argouml.model.UmlHelper#getExtensionMechanisms()
     */
    public ExtensionMechanismsHelper getExtensionMechanisms() {
        return impl.getExtensionMechanisms();
    }

    /**
     * @see org.argouml.model.UmlHelper#getDataTypes()
     */
    public DataTypesHelper getDataTypes() {
        return impl.getDataTypes();
    }

    /**
     * @see org.argouml.model.UmlHelper#getCore()
     */
    public CoreHelper getCore() {
        return impl.getCore();
    }

    /**
     * @see org.argouml.model.UmlHelper#getCommonBehavior()
     */
    public CommonBehaviorHelper getCommonBehavior() {
        return impl.getCommonBehavior();
    }

    /**
     * @see org.argouml.model.UmlHelper#getUseCases()
     */
    public UseCasesHelper getUseCases() {
        return impl.getUseCases();
    }

    /**
     * @see org.argouml.model.UmlHelper#getStateMachines()
     */
    public StateMachinesHelper getStateMachines() {
        return impl.getStateMachines();
    }

    /**
     * @see org.argouml.model.UmlHelper#getCollaborations()
     */
    public CollaborationsHelper getCollaborations() {
        return impl.getCollaborations();
    }

    /**
     * @see org.argouml.model.UmlHelper#getActivityGraphs()
     */
    public ActivityGraphsHelper getActivityGraphs() {
        return impl.getActivityGraphs();
    }

    /**
     * @see org.argouml.model.UmlHelper#getModelManagement()
     */
    public ModelManagementHelper getModelManagement() {
        return impl.getModelManagement();
    }

    /**
     * @see org.argouml.model.UmlHelper#getHelper(java.lang.Object)
     */
    public Object getHelper(Object base) {
        throw new UnsupportedOperationException(
                "getHelper is not implemented and deprecated");
    }

    /**
     * @see org.argouml.model.UmlHelper#getOwner(java.lang.Object)
     */
    public Object getOwner(Object handle) {
        return impl.getOwner(handle);
    }

    /**
     * @see org.argouml.model.UmlHelper#deleteCollection(java.util.Collection)
     */
    public void deleteCollection(Collection col) {
        impl.deleteCollection(col);
    }

    /**
     * @see org.argouml.model.UmlHelper#getSource(java.lang.Object)
     */
    public Object getSource(Object relationship) {
        return impl.getSource(relationship);
    }

    /**
     * @see org.argouml.model.UmlHelper#getDestination(java.lang.Object)
     */
    public Object getDestination(Object relationship) {
        return impl.getDestination(relationship);
    }

}
