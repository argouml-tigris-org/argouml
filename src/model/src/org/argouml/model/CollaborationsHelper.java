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
 * The interface for the helper to Collaborations.<p>
 *
 * Created from the old CollaborationsHelper.
 */
public interface CollaborationsHelper {
    /**
     * Returns all classifierroles found in this namespace and in its children.
     *
     * @return Collection collection of all classifierroles
     * @param ns the namespace
     */
    Collection getAllClassifierRoles(Object ns);

    /**
     * Returns all associations the bases of the classifierrole has,
     * thereby forming the set of associationroles the classifierrole
     * can use. UML Spec 1.3 section 2.10.3.3.
     *
     * @param role the classifierrole
     * @return Collection the set of associationroles the classifierrole
     * can use
     */
    Collection getAllPossibleAssociationRoles(Object role);

    /**
     * Returns all classifierroles associated via associationroles to some
     * classifierrole role.
     *
     * @param role the classifierrole
     * @return Collection all classifierroles associated via associationroles
     * to the given classifierrole role
     */
    Collection getClassifierRoles(Object role);

    /**
     * Returns the first found associationrole between two
     * classifierroles.<p>
     *
     * @param afrom the first classifierrole
     * @param ato the second classifierrole
     * @return MAssociationRole the association between them, or null if none
     */
    Object getAssociationRole(Object afrom, Object ato);

    /**
     * @param afrom the first classifierrole
     * @param ato the second classifierrole
     * @return associationrole
     * @deprecated for 0.21.2 by tfmorris, use getAssociationRole
     * @see #getAssociationRole(Object, Object)
     */
    Object getAssocationRole(Object afrom, Object ato);

    /**
     * Returns all possible activators for some message mes. The
     * possible activators are all messages in the same interaction as
     * the given message that are not part of the predecessors of the
     * message and that are not equal to the given message.<p>
     *
     * @param ames the message
     * @return Collection all possible activators for the given message
     */
    Collection getAllPossibleActivators(Object ames);

    /**
     * Returns true if the given message has the message activator
     * somewhere as it's activator. This is defined as that the
     * message activator can be the activator itself of the given
     * message OR that the given activator can be the activator of the
     * activator of the given message (recursive) OR that the given
     * activator is part of the predecessors of the activator of the
     * given message (recursive too).
     *
     * @param message the given message
     * @param activator the given activator (a message).
     * @return boolean true if the given message has the message activator
     * somewhere as it's activator
     */
    boolean hasAsActivator(Object message, Object activator);

    /**
     * Sets the activator of some given message mes. Checks the
     * wellformednessrules as defined in the UML 1.3 spec in section
     * 2.10.3.6, will throw an IllegalArgumentException if the
     * wellformednessrules are not met.  Not only sets the activator
     * for the given message mes but also for the predecessors of
     * mes. This is done since it can not be the case that a list of
     * messages (defined by the predecessor) has a different
     * activator.<p>
     *
     * @param ames the given message
     * @param anactivator the given activator or null.
     */
    void setActivator(Object ames, Object anactivator);

    /**
     * Returns all possible predecessors for some message, taking into account
     * the wellformednessrules as defined in section 2.10 of the UML spec.<p>
     *
     * @param amessage the given message
     * @return Collection  all possible predecessors
     */
    Collection getAllPossiblePredecessors(Object amessage);

    /**
     * Adds a base to the given classifierrole. If the
     * classifierrole does not have a name yet and there is only one base,
     * the name of the classifierrole is set to the name of the given base
     * according to the wellformednessrules of section 2.10.3 of the UML 1.3
     * spec.
     *
     * @param arole the given classifierrole
     * @param abase the base to be added
     */
    void addBase(Object arole, Object abase);

    /**
     * Sets the bases of the given classifierrole to the given
     * collection bases.<p>
     *
     * @param role the given classifierrole
     * @param bases the given collection of bases
     */
    void setBases(Object role, Collection bases);

    /**
     * Returns all available features for a given classifierrole as
     * defined in section 2.10.3.3 of the UML 1.3 spec. Does not use
     * the standard getAvailableFeatures method on ClassifierRole
     * since this is derived information.<p>
     *
     * @param arole the given classifierrole
     * @return Collection all available features
     */
    Collection allAvailableFeatures(Object arole);

    /**
     * Returns all available contents for a given classifierrole as
     * defined in section 2.10.3.3 of the UML 1.3 spec. Does not use
     * the standard getAvailableContents method on ClassifierRole
     * since this is derived information.
     *
     * @param arole the given classifierrole
     * @return Collection all available contents
     */
    Collection allAvailableContents(Object arole);

    /**
     * Returns all possible bases for the given element. <p>
     * TODO: Beware: this function does not return the actual base!
     * Is that by design or a bug?
     *
     * @param role the given classifierrole or associationrole
     * @return all available bases
     */
    Collection getAllPossibleBases(Object role);

    /**
     * Sets the base checking for wellformedness according to
     * 2.10.3 in the UML 1.3 specification.<p>
     *
     * Either the role can be an AssociationRole in which case the given
     * base must be an Association.
     * Or the role can be an AssocationEndRole in which case the given base
     * must be an AssociationEnd.<p>
     *
     * To set the base(s) of a ClassifierRole use
     * {@link #setBases(Object, Collection)}.
     *
     * @param arole The given role.
     * @param abase The base to set (or <code>null</code> to clear).
     */
    void setBase(Object arole, Object abase);

    /**
     * Returns true if a collaboration may be added to the given context. To
     * decouple ArgoUML as much as possible from the model implementation,
     * the parameter of the method is of type Object.<p>
     *
     * @param context the given context
     * @return boolean true if a collaboration may be added
     */
    boolean isAddingCollaborationAllowed(Object context);

    /**
     * This method removes a classifier from a classifier role.
     *
     * @param handle is the classifier role
     * @param c is the classifier
     */
    void removeBase(Object handle, Object c);

    /**
     * Remove a constraining element.
     *
     * @param handle The collaboration to remove a constraint to.
     * @param constraint The constraint to remove.
     */
    void removeConstrainingElement(Object handle, Object constraint);

    /**
     * Removes a message from the interaction or association role.
     *
     * @param handle The interaction or association role to remove the
     *               message from.
     * @param message The message to remove.
     */
    void removeMessage(Object handle, Object message);

    /**
     * Removes a successor message.
     *
     * @param handle the Message that needs to loose a successor
     * @param mess the Message that is removed
     */
    void removeMessage3(Object handle, Object mess);

    /**
     * Removes a predecessor message.
     *
     * @param handle the Message that needs to loose a predecessor
     * @param message the Message that is removed
     */
    void removePredecessor(Object handle, Object message);

    /**
     * Add a constraining element.
     *
     * @param handle The collaboration to add a constraint to.
     * @param constraint The constraint to add.
     */
    void addConstrainingElement(Object handle, Object constraint);

    /**
     * Adds an instance to a classifier role.
     *
     * @param classifierRole is the classifier role
     * @param instance is the instance to add
     */
    void addInstance(Object classifierRole, Object instance);

    /**
     * Add a message to an interaction or association role.
     *
     * @param handle The interaction or association role.
     * @param elem The message.
     */
    void addMessage(Object handle, Object elem);

    /**
     * Add Message to a predecessor Message.
     *
     * @param handle predecessor Message
     * @param mess Message to be added
     */
    void addMessage3(Object handle, Object mess);

    /**
     * Adds a predecessor to a message.
     *
     * @param handle the message
     * @param predecessor is the predecessor
     */
    void addPredecessor(Object handle, Object predecessor);

    /**
     * Sets the action to a message.
     *
     * @param handle is the message
     * @param action is the action
     */
    void setAction(Object handle, Object action);

    /**
     * Set the context of an interaction.
     *
     * @param handle The element.
     * @param col The context to set.
     */
    void setContext(Object handle, Object col);

    /**
     * Set the successors of a message
     * (named messsage3 in UML 1.3 & successor in UML 1.4).
     *
     * @param handle Message
     * @param messages Collection of successor messages
     */
    void setMessages3(Object handle, Collection messages);

    /**
     * Set the collection of predecessing messages.
     *
     * @param handle Message
     * @param predecessors Collection of Messages
     */
    void setPredecessors(Object handle, Collection predecessors);

    /**
     * Sets the represented classifier of some collaboration.
     *
     * @param handle the collaboration
     * @param classifier is the classifier or null
     */
    void setRepresentedClassifier(Object handle, Object classifier);

    /**
     * Sets the represented operation of some collaboration.
     *
     * @param handle the collaboration
     * @param operation is the operation or null
     */
    void setRepresentedOperation(Object handle, Object operation);

    /**
     * Sets the sender of some model element.<p>
     *
     * @param handle model element
     * @param sender the sender
     */
    void setSender(Object handle, Object sender);

    /**
     * Additionnal support for interactions.
     *
     * @param collab The collaboration.
     * @param interaction The interaction.
     */
    void removeInteraction(Object collab, Object interaction);
}
