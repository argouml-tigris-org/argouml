// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
                return Collections.EMPTY_LIST;
            }
            Iterator it = role.getBase().iterator();
            Set associations = new HashSet();
            while (it.hasNext()) {
                Classifier base = (Classifier) it.next();
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
            return Collections.EMPTY_SET;
        }

        if (!(role instanceof ClassifierRole)) {
            throw new IllegalArgumentException();
        }

        List<Classifier> roles = new ArrayList<Classifier>();
        try {
            Collection associationEnds = 
                Model.getFacade().getAssociationEnds(role);
            if (!associationEnds.isEmpty()) {
                Iterator it = associationEnds.iterator();
                while (it.hasNext()) {
                    AssociationEnd end = (AssociationEnd) it.next();
                    if (end instanceof AssociationEndRole) {
                        UmlAssociation assoc = end.getAssociation();
                        Iterator it2 = assoc.getConnection().iterator();
                        while (it2.hasNext()) {
                            AssociationEnd end2 = (AssociationEnd) it2.next();
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
            Iterator it = Model.getFacade().getAssociationEnds(from).iterator();
            while (it.hasNext()) {
                AssociationEnd end = (AssociationEnd) it.next();
                if (end instanceof AssociationEndRole) {
                    UmlAssociation assoc = end.getAssociation();
                    Iterator it2 = assoc.getConnection().iterator();
                    while (it2.hasNext()) {
                        AssociationEnd end2 = (AssociationEnd) it2.next();
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
            throw new IllegalArgumentException("activator");
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
            if (mes.getPredecessor().contains(activator)) {
                throw new IllegalArgumentException("In setActivator: the "
                        + "predecessors of the message "
                        + "may not contain the " + "activator");
            }
            // we must find out if the activator itself does not have
            // message as it's activator
            if (hasAsActivator(activator, mes)) {
                throw new IllegalArgumentException(
                        "In setActivator: message may "
                                + "not be the activator for "
                                + "the original activator");
            }
        }
        List<Message> listToChange = new ArrayList<Message>();
        Collection<Message> predecessors = mes.getPredecessor();
        listToChange.addAll(predecessors);
        listToChange.add(mes);
        Interaction inter = mes.getInteraction();
        Collection<Message> allMessages = inter.getMessage();
        Iterator<Message> it = allMessages.iterator();
        while (it.hasNext()) {
            Message mes2 = it.next();
            if (mes2.getPredecessor().contains(mes)) {
                listToChange.add(mes2);
            }
        }
        it = listToChange.iterator();
        while (it.hasNext()) {
            Message mes2 = it.next();
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
        // wellformednessrule: if the role does not have a name, the role shall
        // be the only one with the particular base
        if (modelImpl.getFacade().getName(role) == null
                || modelImpl.getFacade().getName(role).equals("")) {

            Collaboration collab = (Collaboration) role.getNamespace();
            ModelManagementHelper mmHelper = 
                modelImpl.getModelManagementHelper();
            Collection roles = mmHelper.getAllModelElementsOfKind(collab,
                    ClassifierRole.class);
            Iterator it = roles.iterator();
            while (it.hasNext()) {
                if (((ClassifierRole) it.next()).getBase().contains(base)) {
                    throw new IllegalArgumentException("In addBase: base is "
                            + "already part of " + "another role and "
                            + "role does not have " + "a name");
                }
            }
        }
        role.getBase().add(base);
        if (modelImpl.getFacade().getBases(role).size() == 1) {
            role.getAvailableContents().clear();
            role.getAvailableContents().addAll(base.getOwnedElement());
            role.getAvailableFeature().clear();
            role.getAvailableFeature().addAll(base.getFeature());
        } else {
            Iterator it = base.getOwnedElement().iterator();
            while (it.hasNext()) {
                ModelElement elem = (ModelElement) it.next();
                if (!role.getAvailableContents().contains(elem)) {
                    // TODO: I'm not sure what this is supposed to be doing,
                    // but it looks suspicious - tfm - 20070806
                    role.getAvailableContents().add((ModelElement) it.next());
                }
            }
            it = base.getFeature().iterator();
            while (it.hasNext()) {
                Feature feature = (Feature) it.next();
                if (!role.getAvailableFeature().contains(feature)) {
                    // TODO: I'm not sure what this is supposed to be doing,
                    // but it looks suspicious - tfm - 20070806
                    role.getAvailableFeature().add((Feature) it.next());
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
        
        if (arole instanceof ClassifierRole) {
            try {
                List<Feature> returnList = new ArrayList<Feature>();
                ClassifierRole role = (ClassifierRole) arole;
                Iterator it = 
                    Model.getFacade().getGeneralizations(arole).iterator();
                while (it.hasNext()) {
                    Object genElem = it.next();
                    if (genElem instanceof ClassifierRole) {
                        returnList.addAll(allAvailableFeatures(genElem));
                    }
                }
                for (Classifier classifier : role.getBase()) {
                    returnList.addAll(classifier.getFeature());
                }
                return returnList;
            } catch (InvalidObjectException e) {
                throw new InvalidElementException(e);
            }
        }
        throw new IllegalArgumentException("Cannot get available features on "
                + arole);
    }


    public Collection allAvailableContents(Object arole) {
        try {
            if (arole instanceof ClassifierRole) {
                List returnList = new ArrayList();
                ClassifierRole role = (ClassifierRole) arole;
                Iterator it = 
                    Model.getFacade().getGeneralizations(role).iterator();
                while (it.hasNext()) {
                    Object genElem = it.next();
                    if (genElem instanceof ClassifierRole) {
                        returnList.addAll(allAvailableContents(genElem));
                    }
                }
                it = role.getBase().iterator();
                while (it.hasNext()) {
                    returnList.addAll(
                            ((Classifier) it.next()).getOwnedElement());
                }
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
     * TODO: Beware: this function does not return the actual base! 
     * Is that by design or a bug?
     * 
     * @param role
     *            the given associationrole
     * @return Collection all possible bases
     */
    private Collection getAllPossibleBases(AssociationRole role) {
        Set ret = new HashSet();
        if (role == null || role.getNamespace() == null) {
            return ret;
        }

        // find the bases of the connected classifierroles so that we can see
        // what associations are between them. If there are bases then the
        // assocations between those bases form the possible bases. Otherwise
        // the bases are formed by all associations in the namespace of the
        // collaboration
        Iterator it = role.getConnection().iterator();
        Set bases = new HashSet();
        while (it.hasNext()) {
            AssociationEndRole end = (AssociationEndRole) it.next();
            ClassifierRole type = (ClassifierRole) end.getParticipant();
            if (type != null) {
                bases.addAll(type.getBase());
            }
        }
        if (bases.isEmpty()) {
            ModelManagementHelper mmh = modelImpl.getModelManagementHelper();
            Namespace ns = ((Collaboration) role.getNamespace()).getNamespace();
            ret.addAll(
                    mmh.getAllModelElementsOfKind(ns, UmlAssociation.class));
            ret.removeAll(mmh.getAllModelElementsOfKind(ns,
                    AssociationRole.class));
        } else {
            it = bases.iterator();
            while (it.hasNext()) {
                Classifier base1 = (Classifier) it.next();
                if (it.hasNext()) {
                    Classifier base2 = (Classifier) it.next();
                    CoreHelper ch = modelImpl.getCoreHelper();
                    Collection assocs = ch.getAssociations(base1, base2);
                    ret.addAll(assocs);
                }
            }
        }
        // if there is no name, the base may not be base for another
        // associationrole
        if (role.getName() == null || role.getName().equals("")) {
            List listToRemove = new ArrayList();
            it = ret.iterator();
            while (it.hasNext()) {
                UmlAssociation assoc = (UmlAssociation) it.next();
                Collection associationRoles = Model.getFacade().
                    getAssociationRoles(assoc);
                if (!associationRoles.isEmpty()) {
                    Iterator it2 = associationRoles.iterator();
                    while (it2.hasNext()) {
                        AssociationRole role2 = (AssociationRole) it2.next();
                        if (role2.getNamespace() == role.getNamespace()) {
                            listToRemove.add(assoc);
                        }
                    }
                }
            }
            ret.removeAll(listToRemove);
        }
        return ret;
    }

    /**
     * Returns all possible bases for some classifierrole taking into account
     * the wellformednessrules as defined in section 2.10.3 of the UML 1.3 spec.
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
        Collaboration coll = (Collaboration) role.getNamespace();
        Namespace ns = coll.getNamespace();
        ModelManagementHelper mmh = modelImpl.getModelManagementHelper();
        Collection returnList = mmh.getAllModelElementsOfKind(ns,
                Classifier.class);
        returnList.removeAll(mmh.getAllModelElementsOfKind(ns,
                ClassifierRole.class));

        if (modelImpl.getFacade().getName(role) == null
                || modelImpl.getFacade().getName(role).equals("")) {

            List listToRemove = new ArrayList();
            Iterator it = returnList.iterator();
            while (it.hasNext()) {
                Classifier clazz = (Classifier) it.next();
                Collection classifierRoles = modelImpl.getUmlPackage().
                    getCollaborations().getAClassifierRoleBase().
                        getClassifierRole(clazz);
                if (!classifierRoles.isEmpty()) {
                    Iterator it2 = classifierRoles.iterator();
                    while (it2.hasNext()) {
                        ClassifierRole role2 = (ClassifierRole) it2.next();
                        if (role2.getNamespace() == coll) {
                            listToRemove.add(clazz);
                        }
                    }
                }
            }
            returnList.removeAll(listToRemove);
        }

        /* TODO: We need to verify that ns is a Package!
         * If not - find its parent package! 
         * Currently this causes an exception when creating 
         * a sequence diagram for a ClassifierRole.*/
        if (!modelImpl.getFacade().isAPackage(ns)) {
            while (modelImpl.getFacade().isANamespace(ns)) {
                ns = ns.getNamespace();
                if (modelImpl.getFacade().isAPackage(ns)) break;
            }
        }
        // now get all classifiers imported from other packages
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
            if (o instanceof Classifier) 
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
                modelImpl.getUmlPackage().getCollaborations()
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
            ((Interaction) handle).getMessage().add((Message) elem);
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
            modelImpl.getUmlPackage().getCollaborations().
                getAPredecessorSuccessor().add((Message) handle,
                            (Message) mess);
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
                Iterator toRemove = successors.iterator();
                while (toRemove.hasNext()) {
                    removeSuccessor(handle, toRemove.next());
                }
            }
            Iterator toAdd = messages.iterator();
            while (toAdd.hasNext())
                addSuccessor(handle, toAdd.next());
    
            return;
        }

        throw new IllegalArgumentException("predecessor: " + handle
                + " or messages: " + messages);
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
                modelImpl.getUmlPackage().getCollaborations()
                        .getAContextInteraction().remove(
                                (Collaboration) collab,
                                (Interaction) interaction);
                return;
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        throw new IllegalArgumentException("collab: " + collab
                + " or interaction: " + interaction);
    }
}
