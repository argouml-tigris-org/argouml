// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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

import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.foundation.core.MNamespace;

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
    Collection getAllClassifierRoles(MNamespace ns);

    /**
     * Returns all associations the bases of the classifierrole has,
     * thereby forming the set of associationroles the classifierrole
     * can use. UML Spec 1.3 section 2.10.3.3.
     *
     * @param role the classifierrole
     * @return Collection the set of associationroles the classifierrole
     * can use
     */
    Collection getAllPossibleAssociationRoles(
            MClassifierRole role);

    /**
     * Returns all classifierroles associated via associationroles to some
     * classifierrole role.
     *
     * @param role the classifierrole
     * @return Collection all classifierroles associated via associationroles
     * to the given classifierrole role
     */
    Collection getClassifierRoles(MClassifierRole role);

    /**
     * Returns the first found associationrole between two
     * classifierroles.<p>
     *
     * @param afrom the first classifierrole
     * @param ato the second classifierrole
     * @return MAssociationRole the association between them, or null if none
     */
    Object/*MAssociationRole*/getAssocationRole(Object afrom,
            Object ato);

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
     * @param activator the given activator
     * @return boolean true if the given message has the message activator
     * somewhere as it's activator
     */
    boolean hasAsActivator(MMessage message, MMessage activator);

    /**
     * Sets the activator of some given message mes. Checks the
     * wellformednessrules as defined in the UML 1.3 spec in section
     * 2.10.3.6, will throw an illegalargumentexception if the
     * wellformednessrules are not met.  Not only sets the activator
     * for the given message mes but also for the predecessors of
     * mes. This is done since it can not be the case that a list of
     * messages (defined by the predecessor) has a different
     * activator.<p>
     *
     * @param ames the given message
     * @param anactivator the given activator
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
     * @param role the given classifierrole or associationrole
     * @return all available bases
     */
    Collection getAllPossibleBases(Object role);

    /**
     * Returns all possible bases for some associationrole taking into
     * account the wellformednessrules as defined in section 2.10.3 of
     * the UML 1.3 spec.<p>
     *
     * @param role the given associationrole
     * @return Collection all possible bases
     */
    Collection getAllPossibleBases(MAssociationRole role);

    /**
     * Returns all possible bases for some classifierrole taking into
     * account the wellformednessrules as defined in section 2.10.3 of
     * the UML 1.3 spec.<p>
     *
     * @param role the given classifierrole
     * @return Collection all possible bases
     */
    Collection getAllPossibleBases(MClassifierRole role);

    /**
     * Sets the base of some associationrole, including the attached
     * assocationendroles.  Checks for wellformedness first.
     *
     * @param arole  the given associationrole
     * @param abase all possible bases
     */
    void setBase(Object arole, Object abase);

    /**
     * Returns true if a collaboration may be added to the given context. To
     * decouple ArgoUML as much as possible from the NSUML model, the parameter
     * of the method is of type Object.<p>
     *
     * @param context the given context
     * @return boolean true if a collaboration may be added
     */
    boolean isAddingCollaborationAllowed(Object context);
}
