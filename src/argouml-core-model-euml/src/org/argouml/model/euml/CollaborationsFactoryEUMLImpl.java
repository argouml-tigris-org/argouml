// $Id$
/*******************************************************************************
 * Copyright (c) 2007,2010 Tom Morris and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial API and implementation
 *******************************************************************************/
package org.argouml.model.euml;

import org.argouml.model.AbstractModelFactory;
import org.argouml.model.CollaborationsFactory;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Collaboration;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * Eclipse UML2 implementation of CollaborationsFactory.
 */
class CollaborationsFactoryEUMLImpl implements CollaborationsFactory,
        AbstractModelFactory {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     *
     * @param implementation The ModelImplementation.
     */
    public CollaborationsFactoryEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public Object buildActivator(Object owner, Object interaction) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object buildAssociationEndRole(Object atype) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object buildAssociationRole(Object from, Object to) {
        // TODO: Auto-generated method stub
        return null;
    }

    @Deprecated
    public Object buildAssociationRole(Object from, Object agg1, Object to,
            Object agg2, Boolean unidirectional) {
        if (unidirectional == null) {
            return buildAssociationRole(from, agg1, to, agg2, false);
        } else {
            return buildAssociationRole(from, agg1, to, agg2, 
                    unidirectional.booleanValue());
        }
    }

    public Object buildAssociationRole(Object from, Object agg1, Object to,
            Object agg2, boolean unidirectional) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object buildAssociationRole(Object link) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object buildClassifierRole(Object collaboration) {
        // TODO: Auto-generated method stub
        return null;
    }

    // TODO: All build/create methods need Undo support - tfm

    public Collaboration buildCollaboration(Object handle) {
        Collaboration collab = createCollaboration();
        ((Element) handle).getOwnedElements().add(collab);
        return collab;
    }

    public Collaboration buildCollaboration(Object namespace, 
            Object representedElement) {
        Collaboration collab = buildCollaboration(namespace);
        Classifier cls = (Classifier) representedElement;

        // TODO: Auto-generated method stub
        return collab;
    }

    public Object buildInteraction(Object handle) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Message buildMessage(Object acollab, Object arole) {
        // TODO: Auto-generated method stub
        return createMessage();
    }

    public Object createAssociationEndRole() {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object createAssociationRole() {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object createClassifierRole() {
        // TODO: Auto-generated method stub
        return null;
    }

    public Collaboration createCollaboration() {
        return UMLFactory.eINSTANCE.createCollaboration();
    }

    public Object createCollaborationInstanceSet() {
        // TODO: Auto-generated method stub
        return null;
    }

    public Interaction createInteraction() {
        return UMLFactory.eINSTANCE.createInteraction();
    }

    public Object createInteractionInstanceSet() {
        // TODO: Auto-generated method stub
        return null;
    }

    public Message createMessage() {
        return UMLFactory.eINSTANCE.createMessage();
    }


}
