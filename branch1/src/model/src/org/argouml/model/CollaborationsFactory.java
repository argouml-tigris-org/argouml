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

/**
 * The interface for the factory of Collaboration.
 */
public interface CollaborationsFactory extends Factory {
    /**
     * Create an empty but initialized instance of a UML AssociationEndRole.
     *
     * @return an initialized UML AssociationEndRole instance.
     */
    Object createAssociationEndRole();

    /**
     * Create an empty but initialized instance of a UML AssociationRole.
     *
     * @return an initialized UML AssociationRole instance.
     */
    Object createAssociationRole();

    /**
     * Create an empty but initialized instance of a UML ClassifierRole.
     *
     * @return an initialized UML ClassifierRole instance.
     */
    Object createClassifierRole();

    /**
     * Create an empty but initialized instance of a UML Collaboration.
     *
     * @return an initialized UML Collaboration instance.
     */
    Object createCollaboration();

    /**
     * Create an empty but initialized instance of a CollaborationInstanceSet.
     *
     * @since UML 1.4
     * @return an initialized CollaborationInstanceSet instance.
     */
    Object createCollaborationInstanceSet();

    /**
     * Create an empty but initialized instance of a UML Interaction.
     *
     * @return an initialized UML Interaction instance.
     */
    Object createInteraction();

    /**
     * Create an empty but initialized instance of an InteractionInstanceSet.
     * 
     * @since UML 1.4
     * @return an initialized InteractionInstanceSet instance.
     */
    Object createInteractionInstanceSet();

    /**
     * Create an empty but initialized instance of a UML Message.
     *
     * @return an initialized UML Message instance.
     */
    Object createMessage();

    /**
     * Creates a classifierrole and adds it to the given collaboration.
     *
     * @param collaboration the given collaboration
     * @return the created classifier role
     */
    Object buildClassifierRole(Object collaboration);

    /**
     * Builds a default collaboration not attached to a classifier.
     *
     * @param handle the namespace for the collaboration
     * @return the created collaboration
     */
    Object buildCollaboration(Object handle);

    /**
     * Builds a collaboration that is owned by a certain namespace and
     * represents the given represented element.
     *
     * @param namespace the namespace for the collaboration
     * @param representedElement the represented element
     * @return the created collaboration
     */
    Object buildCollaboration(Object namespace,
            Object representedElement);

    /**
     * Builds an interaction belonging to some collaboration.
     *
     * @param handle the collaboration that will be the context
     * for the new interaction
     * @return the newly build interaction
     */
    Object buildInteraction(Object handle);

    /**
     * Builds an associationendrole based on some classifierrole.
     *
     * @param atype the classifierrole
     * @return the associationendrole
     */
    Object buildAssociationEndRole(Object atype);

    /**
     * Builds a binary associationrole on basis of two classifierroles.
     *
     * @param from the first classifierrole
     * @param to the second classifierrole
     * @return the newly build associationrole
     */
    Object buildAssociationRole(Object from, Object to);

    /**
     * Builds a binary associationrole on basis of two classifierroles,
     * navigation and aggregation.
     *
     * @param from   the first classifierrole
     * @param agg1   the first aggregationkind
     * @param to     the second classifierrole
     * @param agg2   the second aggregationkind
     * @param unidirectional true if unidirectional
     * @return the newly build assoc. role
     */
    Object buildAssociationRole(Object from,
            Object agg1, Object to, Object agg2,
            Boolean unidirectional);

    /**
     * Builds an associationrole based on a given link. The link must
     * have a source and a destination instance that both have a
     * classifierrole as classifier.  The classifierroles must have
     * the same collaboration as owner. This collaboration will be the
     * new owner of the associationrole.
     *
     * @param link a UML Link
     * @return the newly created association role (an Object)
     */
    Object buildAssociationRole(Object link);

    /**
     * Builds a message within some collaboration or interaction.
     *
     * @param acollab a collaboration or interaction
     * @param arole an associationrole
     * @return the newly build message
     */
    Object buildMessage(Object acollab, Object arole);

    /**
     * Builds an activator for some message.
     *
     * @param owner the owner
     * @param interaction the interaction
     * @return the newly build message
     */
    Object buildActivator(Object owner, Object interaction);
}
