// $Id$
/***************************************************************************
 * Copyright (c) 2007,2010 Tom Morris and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial API and implementation
 ***************************************************************************/
package org.argouml.model.euml;

import org.argouml.model.AbstractModelFactory;
import org.argouml.model.CollaborationsFactory;
import org.eclipse.uml2.uml.Collaboration;
import org.eclipse.uml2.uml.ConnectableElement;
import org.eclipse.uml2.uml.Connector;
import org.eclipse.uml2.uml.ConnectorEnd;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.Lifeline;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageOccurrenceSpecification;
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
    public CollaborationsFactoryEUMLImpl(
            EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public Object buildActivator(Object owner, Object interaction) {
        throw new NotYetImplementedException();
    }

    public ConnectorEnd buildAssociationEndRole(Object atype) {
        Lifeline lifeline = (Lifeline) atype;
        ConnectorEnd end = UMLFactory.eINSTANCE.createConnectorEnd();
        ConnectableElement prop = lifeline.getRepresents();
        if (prop == null) {
            prop = UMLFactory.eINSTANCE.createProperty();
            lifeline.setRepresents(prop);
        }

        end.setRole(prop);
//        lifeline.setRepresents(prop);
        return end;
    }

    public Connector buildAssociationRole(Object from, Object to) {
        return buildAssociationRole((Lifeline) from, (Lifeline) to);
    }
    
    /**
     * Internal type-checked version of buildAssociationRole.
     */
    private Connector buildAssociationRole(Lifeline from,
            Lifeline to) {
        Connector connector = UMLFactory.eINSTANCE.createConnector();
        from.getInteraction().getOwnedConnectors().add(connector);

        connector.getEnds().add(buildAssociationEndRole(from));
        connector.getEnds().add(buildAssociationEndRole(to));
        return connector;
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
        throw new IllegalArgumentException(
                "No such thing as ClassifierRole in UML2"); //$NON-NLS-1$
    }
    
    public Object buildLifeline(Object interaction) {
        Lifeline lifeline = createLifeline();
        lifeline.setInteraction((Interaction) interaction);
        return lifeline;
    }
    
    // TODO: All build/create methods need Undo support - tfm

    public Collaboration buildCollaboration(Object handle) {
        Collaboration collab = createCollaboration();
        ((Element) handle).getOwnedElements().add(collab);
        return collab;
    }

    public Collaboration buildCollaboration(Object namespace, 
            Object representedElement) {
        throw new IllegalArgumentException(
                "A collaboration is only attached to a namespace in UML2");
    }

    public Object buildInteraction(Object collaboration) {
        Interaction interaction = createInteraction();
        Collaboration collab = (Collaboration) collaboration;
        modelImpl.getCoreHelper().addOwnedElement(collab, interaction);
        return interaction;
    }

    public Message buildMessage(Object from, Object to) {
        
        Lifeline l1 = (Lifeline) from;
        Lifeline l2 = (Lifeline) to;

        Message message = UMLFactory.eINSTANCE.createMessage();
        
        MessageOccurrenceSpecification receive = 
            UMLFactory.eINSTANCE.createMessageOccurrenceSpecification();
        MessageOccurrenceSpecification send = 
            UMLFactory.eINSTANCE.createMessageOccurrenceSpecification();
        
        message.setReceiveEvent(receive);
        message.setSendEvent(send);
        
        l1.getCoveredBys().add(send);
        l2.getCoveredBys().add(receive);
        return message;
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
        return UMLFactory.eINSTANCE.createLifeline();
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
    
    public Lifeline createLifeline() {
        return UMLFactory.eINSTANCE.createLifeline();
    }

    public Message createMessage() {
        return UMLFactory.eINSTANCE.createMessage();
    }


}
