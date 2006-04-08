// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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
import java.util.Vector;

import javax.jmi.reflect.InvalidObjectException;

import org.argouml.model.CollaborationsHelper;
import org.argouml.model.CoreHelper;
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
    private MDRModelImplementation nsmodel;

    /**
     * Don't allow instantiation.
     * 
     * @param implementation
     *            To get other helpers and factories.
     */
    CollaborationsHelperMDRImpl(MDRModelImplementation implementation) {
        nsmodel = implementation;
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#getAllClassifierRoles(java.lang.Object)
     */
    public Collection getAllClassifierRoles(Object ns) {
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException();
        }

        List list = new ArrayList();
        Iterator it = ((Namespace) ns).getOwnedElement().iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof Namespace) {
                list.addAll(getAllClassifierRoles(o));
            }
            if (o instanceof ClassifierRole) {
                list.add(o);
            }

        }
        return list;
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#getAllPossibleAssociationRoles(java.lang.Object)
     */
    public Collection getAllPossibleAssociationRoles(Object roleArg) {
        if (!(roleArg instanceof ClassifierRole)) {
            throw new IllegalArgumentException();
        }

        ClassifierRole role = (ClassifierRole) roleArg;

        if (role == null || role.getBase().isEmpty()) {
            return new ArrayList();
        }
        Iterator it = role.getBase().iterator();
        Set associations = new HashSet();
        while (it.hasNext()) {
            Classifier base = (Classifier) it.next();
            associations.addAll(nsmodel.getCoreHelper().getAssociations(base));
        }
        return associations;
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#getClassifierRoles(java.lang.Object)
     */
    public Collection getClassifierRoles(Object role) {
        if (role == null) {
            return new ArrayList();
        }

        if (!(role instanceof ClassifierRole)) {
            throw new IllegalArgumentException();
        }

        List roles = new ArrayList();
        Collection associationEnds = Model.getFacade().getAssociationEnds(role);
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
        return roles;
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#getAssociationRole(java.lang.Object,
     *      java.lang.Object)
     */
    public Object getAssociationRole(Object afrom, Object ato) {
        if (afrom == null || ato == null) {
            throw new IllegalArgumentException();
        }
        ClassifierRole from = (ClassifierRole) afrom;
        ClassifierRole to = (ClassifierRole) ato;

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
                        return (AssociationRole) assoc;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * @see org.argouml.model.CollaborationsHelper#getAssocationRole(java.lang.Object, java.lang.Object)
     */
    public Object getAssocationRole(Object afrom, Object ato) {
        return getAssociationRole(afrom, ato);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#getAllPossibleActivators(java.lang.Object)
     */
    public Collection getAllPossibleActivators(Object ames) {
        Message mes = (Message) ames;
        if (mes == null || mes.getInteraction() == null) {
            return Collections.unmodifiableCollection(Collections.EMPTY_LIST);
        }
        Interaction inter = mes.getInteraction();
        Collection predecessors = mes.getPredecessor();
        Collection allMessages = inter.getMessage();
        Iterator it = allMessages.iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (!predecessors.contains(o) && mes != o
                    && !hasAsActivator(o, mes)
                    && !((Message) o).getPredecessor().contains(mes)) {
                list.add(o);
            }
        }
        return list;
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#hasAsActivator(java.lang.Object,
     *      java.lang.Object)
     */
    public boolean hasAsActivator(Object message, Object activator) {
        if (!(message instanceof Message)) {
            throw new IllegalArgumentException();
        }
        if (!(activator instanceof Message)) {
            throw new IllegalArgumentException();
        }

        Message messActivator = ((Message) message).getActivator();
        if (messActivator == null) {
            return false;
        }
        if (messActivator == activator
                || messActivator.getPredecessor().contains(activator)) {
            return true;
        }
        return hasAsActivator(messActivator, activator);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#setActivator(java.lang.Object,
     *      java.lang.Object)
     */
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
        List listToChange = new ArrayList();
        Collection predecessors = mes.getPredecessor();
        listToChange.addAll(predecessors);
        listToChange.add(mes);
        Interaction inter = mes.getInteraction();
        Collection allMessages = inter.getMessage();
        Iterator it = allMessages.iterator();
        while (it.hasNext()) {
            Message mes2 = (Message) it.next();
            if (mes2.getPredecessor().contains(mes)) {
                listToChange.add(mes2);
            }
        }
        it = listToChange.iterator();
        while (it.hasNext()) {
            Message mes2 = (Message) it.next();
            mes2.setActivator(activator);
        }

    }

    /**
     * @see org.argouml.model.CollaborationsHelper#getAllPossiblePredecessors(java.lang.Object)
     */
    public Collection getAllPossiblePredecessors(Object amessage) {
        Message message = (Message) amessage;
        if (message == null) {
            throw new IllegalArgumentException(
                    "In getAllPossiblePredecessors: "
                            + "argument message is null");
        }
        Interaction inter = message.getInteraction();
        Iterator it = inter.getMessage().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Message mes = (Message) it.next();
            if (mes.getActivator() == message.getActivator() && message != mes
                    && !mes.getPredecessor().contains(message)
                    && !message.getPredecessor().contains(message)) {
                list.add(mes);
            }
        }
        return list;
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#addBase(java.lang.Object,
     *      java.lang.Object)
     */
    public void addBase(Object arole, Object abase) {
        ClassifierRole role = (ClassifierRole) arole;
        Classifier base = (Classifier) abase;
        if (role == null || base == null) {
            throw new IllegalArgumentException("In addBase: either the role "
                    + "or the base is null");
        }
        // wellformednessrule: if the role does not have a name, the role shall
        // be the only one with the particular base
        if (nsmodel.getFacade().getName(role) == null
                || nsmodel.getFacade().getName(role).equals("")) {

            Collaboration collab = (Collaboration) role.getNamespace();
            ModelManagementHelper mmHelper = nsmodel.getModelManagementHelper();
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
        if (nsmodel.getFacade().getBases(role).size() == 1) {
            role.getAvailableContents().clear();
            role.getAvailableContents().addAll(base.getOwnedElement());
            role.getAvailableFeature().clear();
            role.getAvailableFeature().addAll(base.getFeature());
        } else {
            Iterator it = base.getOwnedElement().iterator();
            while (it.hasNext()) {
                ModelElement elem = (ModelElement) it.next();
                if (!role.getAvailableContents().contains(elem)) {
                    role.getAvailableContents().add(it.next());
                }
            }
            it = base.getFeature().iterator();
            while (it.hasNext()) {
                Feature feature = (Feature) it.next();
                if (!role.getAvailableFeature().contains(feature)) {
                    role.getAvailableFeature().add(it.next());
                }
            }
        }
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#setBases(java.lang.Object,
     *      java.util.Collection)
     */
    public void setBases(Object role, Collection bases) {
        if (role == null || bases == null) {
            throw new IllegalArgumentException("In setBases: either the role "
                    + "or the collection bases is " + "null");
        }
        ((ClassifierRole) role).getBase().clear();
        // The next code gives NoSuchElementException:
//        Iterator it = nsmodel.getFacade().getBases(role).iterator();
//        while (it.hasNext()) {
//            removeBase(role, it.next());
//        }
        Iterator i = bases.iterator();
        while (i.hasNext()) {
            addBase(role, i.next());
        }

    }

    /**
     * @see org.argouml.model.CollaborationsHelper#allAvailableFeatures(java.lang.Object)
     */
    public Collection allAvailableFeatures(Object arole) {
        if (arole instanceof ClassifierRole) {
            List returnList = new ArrayList();
            ClassifierRole role = (ClassifierRole) arole;
            Iterator it = Model.getFacade().getGeneralizations(arole)
                    .iterator();
            while (it.hasNext()) {
                Object genElem = it.next();
                if (genElem instanceof ClassifierRole) {
                    returnList.addAll(allAvailableFeatures(genElem));
                }
            }
            it = role.getBase().iterator();
            while (it.hasNext()) {
                returnList.addAll(((Classifier) it.next()).getFeature());
            }
            return returnList;
        }
        throw new IllegalArgumentException("Cannot get available features on "
                + arole);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#allAvailableContents(java.lang.Object)
     */
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
            return Collections.EMPTY_LIST;
        }
        throw new IllegalArgumentException("Cannot get available contents on "
                + arole);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#getAllPossibleBases(java.lang.Object)
     */
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
            return Collections.unmodifiableCollection(Collections.EMPTY_LIST);
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
            ModelManagementHelper mmh = nsmodel.getModelManagementHelper();
            Namespace ns = ((Collaboration) role.getNamespace()).getNamespace();
            ret.addAll(mmh.getAllModelElementsOfKind(ns, UmlAssociation.class));
            ret.removeAll(mmh.getAllModelElementsOfKind(ns,
                    AssociationRole.class));
        } else {
            it = bases.iterator();
            while (it.hasNext()) {
                Classifier base1 = (Classifier) it.next();
                if (it.hasNext()) {
                    Classifier base2 = (Classifier) it.next();
                    CoreHelper ch = nsmodel.getCoreHelper();
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
        if (role == null || nsmodel.getFacade().getNamespace(role) == null) {
            return new ArrayList();
        }
        Collaboration coll = (Collaboration) role.getNamespace();
        Namespace ns = coll.getNamespace();
        ModelManagementHelper mmh = nsmodel.getModelManagementHelper();
        Collection returnList = mmh.getAllModelElementsOfKind(ns,
                Classifier.class);
        returnList.removeAll(mmh.getAllModelElementsOfKind(ns,
                ClassifierRole.class));

        if (nsmodel.getFacade().getName(role) == null
                || nsmodel.getFacade().getName(role).equals("")) {

            List listToRemove = new ArrayList();
            Iterator it = returnList.iterator();
            while (it.hasNext()) {
                Classifier clazz = (Classifier) it.next();
                Collection classifierRoles = nsmodel.getUmlPackage().
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

        // now get all classifiers imported from other packages
        returnList.addAll(getAllImportedClassifiers(ns));
        
        return returnList;
    }
    
    /**
     * Return a collection of classifiers that are imported from other packages
     * into the given namespace.
     *  
     * @param obj the given namespace
     * @return a collection of classifiers
     */
    private Collection getAllImportedClassifiers(Object obj) {
        Collection c = nsmodel.getModelManagementHelper()
                        .getAllImportedElements(obj);
        return filterClassifiers(c);
    }
    
    private Collection filterClassifiers(Collection in) {
        Collection out = new ArrayList();
        Iterator i = in.iterator();
        while (i.hasNext()) {
            Object o = i.next();
            if (nsmodel.getFacade().isAClassifier(o)) 
                out.add(o);
        }
        return out;
    }
    
    /**
     * @see org.argouml.model.CollaborationsHelper#setBase( java.lang.Object,
     *      java.lang.Object)
     */
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
            ClassifierRole sender = (ClassifierRole) nsmodel.getCoreHelper().
                getSource(role);
            ClassifierRole receiver = (ClassifierRole) nsmodel.getCoreHelper().
                getDestination(role);
            Collection senderBases = sender.getBase();
            Collection receiverBases = receiver.getBase();

            AssociationEndRole senderRole = (AssociationEndRole) nsmodel.
                getCoreHelper().getAssociationEnd(sender, role);
            AssociationEndRole receiverRole = (AssociationEndRole) nsmodel.
                getCoreHelper().getAssociationEnd(receiver, role);

            if (base != null) {
                Collection baseConnections = base.getConnection();
                Iterator it = baseConnections.iterator();
                while (it.hasNext()) {
                    AssociationEnd end = (AssociationEnd) it.next();
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

    /**
     * @see org.argouml.model.CollaborationsHelper#isAddingCollaborationAllowed(java.lang.Object)
     */
    public boolean isAddingCollaborationAllowed(Object context) {
        return (/* context instanceof Collaboration || */
                context instanceof Classifier || context instanceof Operation
        /* || context instanceof Model */);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#removeBase(java.lang.Object,
     *      java.lang.Object)
     */
    public void removeBase(Object handle, Object c) {
        if (handle instanceof ClassifierRole && c instanceof Classifier) {
            ((ClassifierRole) handle).getBase().remove(c);
    
            return;
        }
        throw new IllegalArgumentException(
                "There must be a ClassifierRole and a Classifier");
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#removeConstrainingElement(java.lang.Object,
     *      java.lang.Object)
     */
    public void removeConstrainingElement(Object handle, Object constraint) {
        if (handle instanceof Collaboration
                && constraint instanceof ModelElement) {
            Collaboration collab = (Collaboration) handle;
            collab.getConstrainingElement().remove(constraint);
    
            return;
        }

        throw new IllegalArgumentException("handle: " + handle
                + " or constraint: " + constraint);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#removeMessage(java.lang.Object,
     *      java.lang.Object)
     */
    public void removeMessage(Object handle, Object message) {
        if (handle instanceof Interaction && message instanceof Message) {
            ((Interaction) handle).getMessage().remove(message);
    
            return;
        }
        if (handle instanceof AssociationRole && message instanceof Message) {
            ((AssociationRole) handle).getMessage().remove(message);
    
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or message: " + message);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#removeMessage3(java.lang.Object,
     *      java.lang.Object)
     */
    public void removeMessage3(Object handle, Object mess) {
        if (handle instanceof Message && mess instanceof Message) {
            nsmodel.getUmlPackage().getCollaborations().
                getAPredecessorSuccessor().remove((Message) handle,
                            (Message) mess);
            return;
        }
        throw new IllegalArgumentException("predecessor: " + handle
                + " or successor: " + mess);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#removePredecessor(java.lang.Object,
     *      java.lang.Object)
     */
    public void removePredecessor(Object handle, Object message) {
        if (handle instanceof Message && message instanceof Message) {
            ((Message) handle).getPredecessor().remove(message);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or message: " + message);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#addConstrainingElement(java.lang.Object,
     *      java.lang.Object)
     */
    public void addConstrainingElement(Object handle, Object constraint) {
        if (handle instanceof Collaboration
                && constraint instanceof ModelElement) {
            ((Collaboration) handle).getConstrainingElement().add(constraint);
    
            return;
        }

        throw new IllegalArgumentException("handle: " + handle
                + " or constraint: " + constraint);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#addInstance(java.lang.Object,
     *      java.lang.Object)
     */
    public void addInstance(Object classifierRole, Object instance) {
        if (classifierRole instanceof ClassifierRole
                && instance instanceof Instance) {
            ((ClassifierRole) classifierRole).getConformingInstance()
                .add(instance);
        }
        throw new IllegalArgumentException("classifierRole: " + classifierRole
                + " or instance: " + instance);
    }


    /**
     * @see org.argouml.model.CollaborationsHelper#addMessage(java.lang.Object,
     *      java.lang.Object)
     */
    public void addMessage(Object handle, Object elem) {
        if (handle instanceof Interaction && elem instanceof Message) {
            ((Interaction) handle).getMessage().add(elem);
    
            return;
        }
        if (handle instanceof AssociationRole && elem instanceof Message) {
            ((AssociationRole) handle).getMessage().add(elem);
    
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or elem: "
                + elem);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#addMessage3(java.lang.Object,
     *      java.lang.Object)
     */
    public void addMessage3(Object handle, Object mess) {
        if (handle instanceof Message && mess instanceof Message) {
            nsmodel.getUmlPackage().getCollaborations().
                getAPredecessorSuccessor().add((Message) handle,
                            (Message) mess);
            return;
        }

        throw new IllegalArgumentException("predecessor: " + handle
                + " or successor: " + mess);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#addPredecessor(java.lang.Object,
     *      java.lang.Object)
     */
    public void addPredecessor(Object handle, Object predecessor) {
        if (handle != null && handle instanceof Message && predecessor != null
                && predecessor instanceof Message) {
            ((Message) handle).getPredecessor().add(predecessor);
    
            return;
        }

        throw new IllegalArgumentException("handle: " + handle
                + " or predecessor: " + predecessor);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#setAction(java.lang.Object,
     *      java.lang.Object)
     */
    public void setAction(Object handle, Object action) {
        if (handle instanceof Message
                && (action == null || action instanceof Action)) {
            ((Message) handle).setAction((Action) action);
            return;
        }

        throw new IllegalArgumentException("handle: " + handle + " or action: "
                + action);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#setContext(java.lang.Object,
     *      java.lang.Object)
     */
    public void setContext(Object handle, Object col) {
        if (handle instanceof Interaction
                && (col instanceof Collaboration || col == null)) {
            ((Interaction) handle).setContext((Collaboration) col);
    
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or col: "
                + col);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#setMessages3(java.lang.Object,
     *      java.util.Collection)
     */
    public void setMessages3(Object handle, Collection messages) {
        if (handle instanceof Message) {
            Collection actualMessages = Model.getFacade().getMessages3(handle);
            if (!actualMessages.isEmpty()) {
                Vector messages3 = new Vector();
                messages3.addAll(actualMessages);
                Iterator toRemove = messages3.iterator();
                while (toRemove.hasNext())
                    removeMessage3(handle, toRemove.next());
            }
            Iterator toAdd = messages.iterator();
            while (toAdd.hasNext())
                addMessage3(handle, toAdd.next());
    
            return;
        }

        throw new IllegalArgumentException("predecessor: " + handle
                + " or messages: " + messages);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#setPredecessors(java.lang.Object,
     *      java.util.Collection)
     */
    public void setPredecessors(Object handle, Collection predecessors) {
        if (handle instanceof Message) {
            ((Message) handle).getPredecessor().clear();
            ((Message) handle).getPredecessor().addAll(predecessors);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or predecessors: " + predecessors);
    }

    /**
     * @see org.argouml.model.CollaborationsHelper#setRepresentedClassifier(java.lang.Object,
     *      java.lang.Object)
     */
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

    /**
     * @see org.argouml.model.CollaborationsHelper#setRepresentedOperation(java.lang.Object,
     *      java.lang.Object)
     */
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

    /**
     * @see org.argouml.model.CollaborationsHelper#setSender(java.lang.Object,
     *      java.lang.Object)
     */
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

    /**
     * 
     * @see org.argouml.model.CollaborationsHelper#removeInteraction(java.lang.Object,
     *      java.lang.Object)
     */
    public void removeInteraction(Object collab, Object interaction) {
        if (collab instanceof Collaboration
                && interaction instanceof Interaction) {
            nsmodel.getUmlPackage().getCollaborations().
                 getAContextInteraction().remove((Collaboration) collab,
                            (Interaction) interaction);
            return;
        }
        throw new IllegalArgumentException("collab: " + collab
                + " or interaction: " + interaction);
    }
}
