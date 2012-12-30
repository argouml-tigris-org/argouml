/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.model.mdr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jmi.reflect.InvalidObjectException;

import org.argouml.model.CollaborationsHelper;
import org.argouml.model.CoreHelper;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.ModelManagementHelper;
import org.omg.uml.behavioralelements.collaborations.AssociationEndRole;
import org.omg.uml.behavioralelements.collaborations.AssociationRole;
import org.omg.uml.behavioralelements.collaborations.ClassifierRole;
import org.omg.uml.behavioralelements.collaborations.Collaboration;
import org.omg.uml.behavioralelements.collaborations.Interaction;
import org.omg.uml.behavioralelements.collaborations.Message;
import org.omg.uml.behavioralelements.commonbehavior.Action;
import org.omg.uml.behavioralelements.commonbehavior.Instance;
import org.omg.uml.behavioralelements.commonbehavior.Stimulus;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Feature;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.UmlAssociation;
import org.omg.uml.modelmanagement.UmlPackage;

/**
 * Helper class for UML BehavioralElements::Collaborations Package.
 * <p>
 * @since ARGO0.19.5
 * @author Ludovic Ma&icirc;tre
 * @author Tom Morris
 * Derived from NSUML implementation by:
 * @author Thierry Lach
 */
class CollaborationsHelperMDRImpl implements CollaborationsHelper {

    /**
     * The model implementation.
     */
    private MDRModelImplementation modelImpl;

    private static final Logger LOG =
        Logger.getLogger(CollaborationsHelperMDRImpl.class.getName());

    /**
     * Constructor.
     *
     * @param implementation
     *            To get other helpers and factories.
     */
    CollaborationsHelperMDRImpl(MDRModelImplementation implementation) {
        modelImpl = implementation;
    }


    public Collection<ClassifierRole> getAllClassifierRoles(Object ns) {
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException();
        }

        try {
            List<ClassifierRole> list = new ArrayList<ClassifierRole>();
            for (Object o : ((Namespace) ns).getOwnedElement()) {
                if (o instanceof Namespace) {
                    list.addAll(getAllClassifierRoles(o));
                }
                if (o instanceof ClassifierRole) {
                    list.add((ClassifierRole) o);
                }
            }
            return list;
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
    }


    public Collection getAllPossibleAssociationRoles(Object roleArg) {
        if (!(roleArg instanceof ClassifierRole)) {
            throw new IllegalArgumentException();
        }

        ClassifierRole role = (ClassifierRole) roleArg;

        try {
            if (role.getBase().isEmpty()) {
                return Collections.emptyList();
            }
            Set associations = new HashSet();
            for (Classifier base : role.getBase()) {
                associations.addAll(
                        modelImpl.getCoreHelper().getAssociations(base));
            }
            return associations;
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
    }


    public Collection<Classifier> getClassifierRoles(Object role) {
        if (role == null) {
            return Collections.emptySet();
        }

        if (!(role instanceof ClassifierRole)) {
            throw new IllegalArgumentException();
        }

        List<Classifier> roles = new ArrayList<Classifier>();
        try {
            Collection<AssociationEnd> associationEnds =
                Model.getFacade().getAssociationEnds(role);
            if (!associationEnds.isEmpty()) {
                for (AssociationEnd end : associationEnds) {
                    if (end instanceof AssociationEndRole) {
                        UmlAssociation assoc = end.getAssociation();
                        for (AssociationEnd end2 : assoc.getConnection()) {
                            Classifier classifier = end2.getParticipant();
                            if (classifier != role
                                    && classifier instanceof ClassifierRole) {
                                roles.add(classifier);
                            }
                        }
                    }
                }
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        return roles;
    }


    public Object getAssociationRole(Object afrom, Object ato) {
        if (afrom == null || ato == null) {
            throw new IllegalArgumentException();
        }
        ClassifierRole from = (ClassifierRole) afrom;
        ClassifierRole to = (ClassifierRole) ato;

        try {
            Collection<AssociationEnd> ends =
                Model.getFacade().getAssociationEnds(from);
            for (AssociationEnd end : ends) {
                if (end instanceof AssociationEndRole) {
                    UmlAssociation assoc = end.getAssociation();
                    for (AssociationEnd end2 : assoc.getConnection()) {
                        Classifier classifier = end2.getParticipant();
                        if (classifier == to) {
                            return assoc;
                        }
                    }
                }
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        return null;
    }


    public Collection<Message> getAllPossibleActivators(Object ames) {
        Message mes = (Message) ames;
        if (mes == null || mes.getInteraction() == null) {
            return Collections.unmodifiableCollection(Collections.EMPTY_LIST);
        }

        try {
            Interaction inter = mes.getInteraction();
            Collection<Message> predecessors = mes.getPredecessor();
            Collection<Message> allMessages = inter.getMessage();
            List<Message> list = new ArrayList<Message>();
            for (Message m : allMessages) {
                if (!predecessors.contains(m) && mes != m
                        && !hasAsActivator(m, mes)
                        && !m.getPredecessor().contains(mes)) {
                    list.add(m);
                }
            }
            return list;
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
    }


    public boolean hasAsActivator(Object message, Object activator) {
        if (!(message instanceof Message)) {
            throw new IllegalArgumentException();
        }
        if (!(activator instanceof Message)) {
            throw new IllegalArgumentException();
        }

        try {
            Message messActivator = ((Message) message).getActivator();
            if (messActivator == null) {
                return false;
            }
            if (messActivator == activator
                    || messActivator.getPredecessor().contains(activator)) {
                return true;
            }
            return hasAsActivator(messActivator, activator);
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
    }


    public void setActivator(Object ames, Object anactivator) {
        if (ames == null) {
            throw new IllegalArgumentException("message is null");
        }
        if (!(ames instanceof Message)) {
            throw new IllegalArgumentException("message");
        }
        if (anactivator != null && !(anactivator instanceof Message)) {
            throw new IllegalArgumentException(
                    "An activator must be a message");
        }
        Message mes = (Message) ames;
        Message activator = (Message) anactivator;
        if (mes == activator) {
            throw new IllegalArgumentException("In setActivator: message may "
                    + "not be equal to activator");
        }

        if (activator != null) {
            if (mes.getInteraction() != activator.getInteraction()) {
                throw new IllegalArgumentException(
                        "In setActivator: interaction "
                                + "of message should equal "
                                + "interaction of activator");
            }
            // we must find out if the activator itself does not have
            // message as it's activator
            if (hasAsActivator(activator, mes)) {
                throw new IllegalArgumentException(
                        "In setActivator: message may "
                                + "not be the activator for "
                                + "the original activator");
            }
            // An activator can't also be a predecessor of a message
            if (mes.getPredecessor().contains(activator)) {
                mes.getPredecessor().remove(activator);
            }
        }
        List<Message> listToChange = new ArrayList<Message>();
        Collection<Message> predecessors = mes.getPredecessor();
        listToChange.addAll(predecessors);
        listToChange.add(mes);
        Interaction inter = mes.getInteraction();
        for (Message mes2 : inter.getMessage()) {
            if (mes2.getPredecessor().contains(mes)) {
                listToChange.add(mes2);
            }
        }
        // This causes problems. It can make multiple return messages
        // refer to the same activator even if not returning to the same
        // classifier role as the activator emenates from.
        // I'm not sure that changing the activator of one message
        // should amend any other messages but this is certainly changing
        // too many - Bob.
        for (Message mes2 : listToChange) {
            mes2.setActivator(activator);
        }

    }


    public Collection<Message> getAllPossiblePredecessors(Object amessage) {
        Message message = (Message) amessage;
        if (message == null) {
            throw new IllegalArgumentException(
                    "In getAllPossiblePredecessors: "
                            + "argument message is null");
        }

        try {
            Interaction inter = message.getInteraction();
            List<Message> list = new ArrayList<Message>();
            for (Message mes : inter.getMessage()) {
                if (mes.getActivator() == message.getActivator()
                        && message != mes
                        && !mes.getPredecessor().contains(message)
                        && !message.getPredecessor().contains(message)) {
                    list.add(mes);
                }
            }
            return list;
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
    }


    public void addBase(Object arole, Object abase) {
        ClassifierRole role = (ClassifierRole) arole;
        Classifier base = (Classifier) abase;
        if (role == null || base == null) {
            throw new IllegalArgumentException("In addBase: either the role "
                    + "or the base is null");
        }
        role.getBase().add(base);
        if (modelImpl.getFacade().getBases(role).size() == 1) {
            role.getAvailableContents().clear();
            role.getAvailableContents().addAll(base.getOwnedElement());
            role.getAvailableFeature().clear();
            role.getAvailableFeature().addAll(base.getFeature());
        } else {
            for (ModelElement elem : base.getOwnedElement()) {
                if (!role.getAvailableContents().contains(elem)) {
                    role.getAvailableContents().add(elem);
                }
            }
            for (Feature feature : base.getFeature()) {
                if (!role.getAvailableFeature().contains(feature)) {
                    role.getAvailableFeature().add(feature);
                }
            }
        }
    }

    public void setBases(Object role, Collection bases) {
        if (role == null || bases == null) {
            throw new IllegalArgumentException("In setBases: either the role "
                    + "or the collection bases is " + "null");
        }
        CollectionHelper.update(((ClassifierRole) role).getBase(), bases);
    }


    public Collection<Feature> allAvailableFeatures(Object arole) {
        LOG.log(Level.INFO, "allAvailableFeatures start");

        if (arole instanceof ClassifierRole) {
            try {
                List<Feature> returnList = new ArrayList<Feature>();
                ClassifierRole role = (ClassifierRole) arole;
                for (ModelElement genElem
                        : CoreHelperMDRImpl.getAllParents(role)) {
                    if (genElem instanceof ClassifierRole) {
                        returnList.addAll(allAvailableFeatures(genElem));
                    }
                }
                for (Classifier classifier : role.getBase()) {
                    returnList.addAll(classifier.getFeature());
                }
                LOG.log(Level.INFO, "allAvailableFeatures {0}", returnList.size());
                return returnList;
            } catch (InvalidObjectException e) {
                throw new InvalidElementException(e);
            }
        }
        throw new IllegalArgumentException("Cannot get available features on "
                + arole);
    }


    public Collection allAvailableContents(Object arole) {
        LOG.log(Level.INFO, "allAvailableContents start");
        try {
            if (arole instanceof ClassifierRole) {
                List returnList = new ArrayList();
                ClassifierRole role = (ClassifierRole) arole;
                for (ModelElement genElem
                        : CoreHelperMDRImpl.getAllParents(role)) {
                    if (genElem instanceof ClassifierRole) {
                        returnList.addAll(allAvailableContents(genElem));
                    }
                }
                for (Classifier baseClassifier : role.getBase()) {
                    returnList.addAll(baseClassifier.getOwnedElement());
                }
                LOG.log(Level.INFO, "allAvailableFeatures {0}", returnList.size());
                return returnList;
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        throw new IllegalArgumentException("Cannot get available contents on "
                + arole);
    }


    public Collection getAllPossibleBases(Object role) {
        try {
            if (role instanceof ClassifierRole) {
                return getAllPossibleBases((ClassifierRole) role);
            } else if (role instanceof AssociationRole) {
                return getAllPossibleBases((AssociationRole) role);
            } else {
                throw new IllegalArgumentException("Illegal type " + role);
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
    }

    /**
     * Returns all possible bases for some AssociationRole taking into account
     * the wellformednessrules as defined in section 2.10.3 of the UML 1.3 spec.
     * <p>
     * Beware: this function does not return the actual base!
     * Which is by design; there are easier ways to retrieve the actual base.
     *
     * @param aRole
     *            the given associationrole
     * @return Collection all possible bases
     */
    private Collection getAllPossibleBases(AssociationRole aRole) {
        Set<UmlAssociation> ret = new HashSet<UmlAssociation>();
        if (aRole == null || aRole.getNamespace() == null) {
            return ret;
        }

        // find the bases of the connected classifierroles so that we can see
        // what associations are between them. If there are bases then the
        // associations between those bases form the possible bases. Otherwise
        // the bases are formed by all associations in the namespace of the
        // collaboration
        Set<Classifier> bases = new HashSet<Classifier>();
        for (AssociationEnd end : aRole.getConnection()) {
            assert end instanceof AssociationEndRole;
            ClassifierRole type = (ClassifierRole) end.getParticipant();
            if (type != null) {
                bases.addAll(type.getBase());
            }
        }
        if (bases.isEmpty()) {
            ModelManagementHelper mmh = modelImpl.getModelManagementHelper();
            Namespace ns =
                ((Collaboration) aRole.getNamespace()).getNamespace();
            ret.addAll(
                    mmh.getAllModelElementsOfKind(ns, UmlAssociation.class));
            ret.removeAll(mmh.getAllModelElementsOfKind(ns,
                    AssociationRole.class));
        } else {
            CoreHelper ch = modelImpl.getCoreHelper();
            /* This double 'for' loop may be optimised as follows:
             * - Use arrays in stead of a Set for bases
             * - Have the second loop start from the element after base1
             * ... but I chose not to do this, since the gain is small,
             * and this is only used for a lazily filled combo.
             * BTW: This is also used by Notation.*/
            for (Classifier base1 : bases)  {
                for (Classifier base2 : bases) {
                    // include associations to self - see issue 5602
                    ret.addAll(ch.getAssociations(base1, base2));
                }
            }
        }
        // An Association can only have a single unnamed ClassifierRole, so
        Collection<UmlAssociation> listToRemove = new ArrayList<UmlAssociation>();
        for (UmlAssociation association : ret) {
            Collection<AssociationRole> associationRoles =
                ((org.omg.uml.UmlPackage) (association)
                    .refOutermostPackage()).getCollaborations()
                    .getABaseAssociationRole().getAssociationRole(association);
            if (associationRoles.isEmpty()) {
                continue;
            }
            // if we are unnamed eliminate all classifiers which are already
            // the base of some role
            if (aRole.getName() == null || aRole.getName().equals("")) {
                listToRemove.add(association);
            } else {
                // eliminate Classifiers which already have an unnamed role
                for (AssociationRole ar : associationRoles) {
                    if (ar.getName() == null || ar.getName().equals("")) {
                        listToRemove.add(association);
                    }
                }
            }
        }
        ret.removeAll(listToRemove);

        return ret;
    }

    /**
     * Returns all possible bases for some classifierrole taking into account
     * the wellformednessrules as defined in section 2.10.3 of the UML 1.4 spec.
     * <p>
     *
     * @param role
     *            the given classifierrole
     * @return Collection all possible bases
     */
    private Collection getAllPossibleBases(ClassifierRole role) {
        if (role == null || modelImpl.getFacade().getNamespace(role) == null) {
            return Collections.EMPTY_SET;
        }
        Collaboration collaboration = (Collaboration) role.getNamespace();
        Namespace ns = collaboration.getNamespace();
        ModelManagementHelper mmh = modelImpl.getModelManagementHelper();
        Collection<Classifier> returnList = mmh.getAllModelElementsOfKind(ns,
                Classifier.class);
        // WFR 2.10.3.3 #4
        returnList.removeAll(mmh.getAllModelElementsOfKind(ns,
                ClassifierRole.class));

        // A Classifier can only have a single unnamed ClassifierRole, so
        // TODO: This probably belongs in a critic instead of here
        Collection<Classifier> listToRemove = new ArrayList<Classifier>();
        for (Classifier classifier : returnList) {
            Collection<ClassifierRole> classifierRoles =
                ((org.omg.uml.UmlPackage) (classifier)
                    .refOutermostPackage()).getCollaborations()
                    .getAClassifierRoleBase().getClassifierRole(classifier);
            if (classifierRoles.isEmpty()) {
                continue;
            }
            // if we are unnamed eliminate all classifiers which are already
            // the base of some role
            if (role.getName() == null || role.getName().equals("")) {
                listToRemove.add(classifier);
            } else {
                // eliminate Classifiers which already have an unnamed role
                for (ClassifierRole cr : classifierRoles) {
                    if (cr.getName() == null || cr.getName().equals("")) {
                        listToRemove.add(classifier);
                    }
                }
            }
        }
        returnList.removeAll(listToRemove);

        /* We need to verify that ns is a Package,
         * if not - find its parent package!
         * Otherwise this causes an exception when creating
         * a sequence diagram for a ClassifierRole.*/
        if (!(ns instanceof UmlPackage)) {
            while (ns != null) {
                ns = ns.getNamespace();
                if (ns instanceof UmlPackage) {
                    break;
                }
            }
        }
        // now get all classifiers imported from other packages
        // TODO: This should probably happen automatically in
        // getAllModelElementsOfKind() - tfm
        if (modelImpl.getFacade().isAPackage(ns)) {
            returnList.addAll(getAllImportedClassifiers(ns));
        }

        return returnList;
    }

    /**
     * Return a collection of classifiers that are imported from other packages
     * into the given namespace.
     *
     * @param obj the given namespace
     * @return a collection of classifiers
     */
    private Collection<Classifier> getAllImportedClassifiers(Object obj) {
        Collection c = modelImpl.getModelManagementHelper()
                        .getAllImportedElements(obj);
        return filterClassifiers(c);
    }

    private Collection<Classifier> filterClassifiers(Collection in) {
        Collection<Classifier> out = new ArrayList<Classifier>();
        for (Object o : in) {
            if (o instanceof Classifier && !(o instanceof ClassifierRole))
                out.add((Classifier) o);
        }
        return out;
    }


    public void setBase(Object arole, Object abase) {
        if (arole == null) {
            throw new IllegalArgumentException("role is null");
        }
        if (arole instanceof AssociationRole) {
            AssociationRole role = (AssociationRole) arole;
            UmlAssociation base = (UmlAssociation) abase;

            // TODO: Must we calculate the whole list?
            if (base != null && !getAllPossibleBases(role).contains(base)) {
                throw new IllegalArgumentException("base is not allowed for "
                        + "this role");
            }
            role.setBase(base);
            ClassifierRole sender = (ClassifierRole) modelImpl.getCoreHelper()
                    .getSource(role);
            ClassifierRole receiver = (ClassifierRole) modelImpl
                    .getCoreHelper().getDestination(role);
            Collection<Classifier> senderBases = sender.getBase();
            Collection<Classifier> receiverBases = receiver.getBase();

            AssociationEndRole senderRole = (AssociationEndRole) modelImpl.
                getCoreHelper().getAssociationEnd(sender, role);
            AssociationEndRole receiverRole = (AssociationEndRole) modelImpl.
                getCoreHelper().getAssociationEnd(receiver, role);

            if (base != null) {
                for (AssociationEnd end : base.getConnection()) {
                    if (senderBases.contains(end.getParticipant())) {
                        senderRole.setBase(end);
                    } else if (receiverBases.contains(end.getParticipant())) {
                        receiverRole.setBase(end);
                    }
                }
            }

            return;
        } else if (arole instanceof AssociationEndRole) {
            AssociationEndRole role = (AssociationEndRole) arole;
            AssociationEnd base = (AssociationEnd) abase;

            role.setBase(base);

            return;
        }

        throw new IllegalArgumentException("role");
    }


    public boolean isAddingCollaborationAllowed(Object context) {
        return (
                context instanceof Classifier
                || context instanceof Operation
                //|| context instanceof Collaboration
                //|| context instanceof Model
                );
    }


    public void removeBase(Object handle, Object c) {
        try {
            if (handle instanceof ClassifierRole && c instanceof Classifier) {
                ((ClassifierRole) handle).getBase().remove(c);

                return;
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        throw new IllegalArgumentException(
                "There must be a ClassifierRole and a Classifier");
    }


    public void removeConstrainingElement(Object handle, Object constraint) {
        try {
            if (handle instanceof Collaboration
                    && constraint instanceof ModelElement) {
                Collaboration collab = (Collaboration) handle;
                collab.getConstrainingElement().remove(constraint);

                return;
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or constraint: " + constraint);
    }


    public void removeMessage(Object handle, Object message) {
        try {
            if (handle instanceof Interaction && message instanceof Message) {
                ((Interaction) handle).getMessage().remove(message);
                return;
            }
            if (handle instanceof AssociationRole
                    && message instanceof Message) {
                ((AssociationRole) handle).getMessage().remove(message);
                return;
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or message: " + message);
    }


    public void removeSuccessor(Object handle, Object mess) {
        try {
            if (handle instanceof Message && mess instanceof Message) {
                ((org.omg.uml.UmlPackage) ((Message) handle)
                        .refOutermostPackage()).getCollaborations()
                        .getAPredecessorSuccessor().remove((Message) handle,
                                (Message) mess);
                return;
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        throw new IllegalArgumentException("predecessor: " + handle
                + " or successor: " + mess);
    }


    public void removePredecessor(Object handle, Object message) {
        try {
            if (handle instanceof Message && message instanceof Message) {
                ((Message) handle).getPredecessor().remove(message);
                return;
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or message: " + message);
    }


    public void addConstrainingElement(Object handle, Object constraint) {
        if (handle instanceof Collaboration
                && constraint instanceof ModelElement) {
            ((Collaboration) handle).getConstrainingElement().add(
                    (ModelElement) constraint);
            return;
        }

        throw new IllegalArgumentException("handle: " + handle
                + " or constraint: " + constraint);
    }


    public void addInstance(Object classifierRole, Object instance) {
        if (classifierRole instanceof ClassifierRole
                && instance instanceof Instance) {
            ((ClassifierRole) classifierRole).getConformingInstance().add(
                    (Instance) instance);
        }
        throw new IllegalArgumentException("classifierRole: " + classifierRole
                + " or instance: " + instance);
    }


    public void addMessage(Object handle, Object elem) {
        if (handle instanceof Interaction && elem instanceof Message) {
            final Message message = (Message) elem;
            final Interaction interaction = (Interaction) handle;
            final Interaction oldInteraction = message.getInteraction();

            if (oldInteraction != null) {
                oldInteraction.getMessage().remove(message);
            }
            interaction.getMessage().add(message);
            return;
        }
        if (handle instanceof AssociationRole && elem instanceof Message) {
            ((AssociationRole) handle).getMessage().add((Message) elem);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or elem: "
                + elem);
    }


    public void addSuccessor(Object handle, Object mess) {
        if (handle instanceof Message && mess instanceof Message) {
            ((Message) mess).getPredecessor().add((Message) handle);
            return;
        }

        throw new IllegalArgumentException("predecessor: " + handle
                + " or successor: " + mess);
    }


    public void addPredecessor(Object handle, Object predecessor) {
        if (handle != null && handle instanceof Message && predecessor != null
                && predecessor instanceof Message) {
            ((Message) handle).getPredecessor().add((Message) predecessor);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or predecessor: " + predecessor);
    }


    public void setAction(Object handle, Object action) {
        if (handle instanceof Message
                && (action == null || action instanceof Action)) {
            ((Message) handle).setAction((Action) action);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or action: "
                + action);
    }


    public void setContext(Object handle, Object col) {
        if (handle instanceof Interaction
                && (col instanceof Collaboration || col == null)) {
            ((Interaction) handle).setContext((Collaboration) col);

            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or col: "
                + col);
    }


    public void setSuccessors(Object handle, Collection messages) {
        if (handle instanceof Message) {
            Collection currentMessages =
                Model.getFacade().getSuccessors(handle);
            if (!currentMessages.isEmpty()) {
                Collection successors = new ArrayList(currentMessages);
                for (Object msg : successors) {
                    removeSuccessor(handle, msg);
                }
            }
            for (Object msg : messages) {
                addSuccessor(handle, msg);
            }
            return;
        }

        throw new IllegalArgumentException("predecessor: " + handle
                + " or messages: " + messages);
    }

    public void setMessageSort(Object message, Object messageSort) {
        setAction(message, messageSort);
    }

    public void setPredecessors(Object handle, Collection predecessors) {
        if (handle instanceof Message) {
            CollectionHelper.update(
                    ((Message) handle).getPredecessor(), predecessors);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or predecessors: " + predecessors);
    }


    public void setRepresentedClassifier(Object handle, Object classifier) {
        if (handle instanceof Collaboration
                && ((classifier == null) || classifier instanceof Classifier)) {
            ((Collaboration) handle).
                setRepresentedClassifier((Classifier) classifier);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or classifier: " + classifier);
    }


    public void setRepresentedOperation(Object handle, Object operation) {
        if (handle instanceof Collaboration
                && ((operation == null) || operation instanceof Operation)) {
            ((Collaboration) handle).
                setRepresentedOperation((Operation) operation);

            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or operation: " + operation);
    }


    public void setSender(Object handle, Object sender) {
        if (handle instanceof Message
                && (sender instanceof ClassifierRole || sender == null)) {
            ((Message) handle).setSender((ClassifierRole) sender);
            return;
        }
        if (handle instanceof Stimulus && sender instanceof Instance) {
            ((Stimulus) handle).setSender((Instance) sender);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or sender: "
                + sender);
    }


    public void removeInteraction(Object collab, Object interaction) {
        try {
            if (collab instanceof Collaboration
                    && interaction instanceof Interaction) {
                ((Collaboration) collab).getInteraction().remove(interaction);
                return;
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        throw new IllegalArgumentException("collab: " + collab
                + " or interaction: " + interaction);
    }
}
