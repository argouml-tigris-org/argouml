/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.argouml.model.Model;
import org.argouml.model.UmlHelper;
import org.omg.uml.behavioralelements.collaborations.Message;
import org.omg.uml.behavioralelements.commonbehavior.Action;
import org.omg.uml.behavioralelements.commonbehavior.ActionSequence;
import org.omg.uml.behavioralelements.commonbehavior.Argument;
import org.omg.uml.behavioralelements.commonbehavior.Link;
import org.omg.uml.behavioralelements.commonbehavior.LinkEnd;
import org.omg.uml.behavioralelements.statemachines.Event;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.behavioralelements.usecases.Extend;
import org.omg.uml.behavioralelements.usecases.ExtensionPoint;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.BehavioralFeature;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Enumeration;
import org.omg.uml.foundation.core.EnumerationLiteral;
import org.omg.uml.foundation.core.Feature;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.core.Relationship;
import org.omg.uml.foundation.core.UmlAssociation;

/**
 * Helper class for UML metamodel.
 * 
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
class UmlHelperMDRImpl implements UmlHelper {

    /**
     * The model implementation.
     */
    private MDRModelImplementation modelImpl;

    /**
     * Don't allow instantiation.
     * 
     * @param implementation
     *            To get other helpers and factories.
     */
    UmlHelperMDRImpl(MDRModelImplementation implementation) {
        modelImpl = implementation;
    }

    public void addListenersToModel(Object model) {
        // Nothing to do - we get all events automatically
    }

    /*
     * @see org.argouml.model.UmlHelper#deleteCollection(java.util.Collection)
     */
    public void deleteCollection(Collection col) {
        Iterator it = col.iterator();
        while (it.hasNext()) {
            modelImpl.getUmlFactory().delete(it.next());
        }
    }

    /*
     * @see org.argouml.model.UmlHelper#getSource(java.lang.Object)
     */
    public Object getSource(Object relationship) {
        if (relationship instanceof Message) {
            Message message = (Message) relationship;
            return message.getSender();
        }
        if (relationship instanceof Relationship) {
            // handles all children of relationship including extend and
            // include which are not members of core
            return modelImpl.getCoreHelper().getSource(relationship);
        }
        if (relationship instanceof Transition) {
            return modelImpl.getStateMachinesHelper().getSource(relationship);
        }
        if (relationship instanceof AssociationEnd) {
            return modelImpl.getCoreHelper().getSource(relationship);
        }
        throw new IllegalArgumentException();
    }

    /*
     * @see org.argouml.model.UmlHelper#getDestination(java.lang.Object)
     */
    public Object getDestination(Object relationship) {
        if (relationship instanceof Message) {
            Message message = (Message) relationship;
            return message.getSender();
        }
        if (relationship instanceof Relationship) {
            // handles all children of relationship including extend and
            // include which are not members of core
            return modelImpl.getCoreHelper().getDestination(relationship);
        }
        if (relationship instanceof Transition) {
            return modelImpl.getStateMachinesHelper().
                    getDestination(relationship);
        }
        if (relationship instanceof AssociationEnd) {
            return modelImpl.getCoreHelper().getDestination(relationship);
        }
        throw new IllegalArgumentException();
    }
    

    /*
     * @see org.argouml.model.UmlHelper#move(java.lang.Object, org.argouml.model.UmlHelper.Direction)
     */
    public void move(Object parent, Object element, Direction direction) {
        if (element instanceof Argument) {
            final Argument arg = (Argument) element;
            final Action action = arg.getAction();
            final List<Argument> f = action.getActualArgument();
            final int oldIndex = f.indexOf(arg);
            final int newIndex = newPosition(oldIndex, f.size(), direction);
            f.remove(arg);
            f.add(newIndex, arg);
        } else if (element instanceof Action) {
            final Action act = (Action) element;
            final ActionSequence actionSequence = act.getActionSequence();
            final List<Action> f = actionSequence.getAction();
            final int oldIndex = f.indexOf(act);
            final int newIndex = newPosition(oldIndex, f.size(), direction);
            f.remove(actionSequence);
            f.add(newIndex, actionSequence);
        } else if (element instanceof AssociationEnd) {
            final AssociationEnd assEnd = (AssociationEnd) element;
            final UmlAssociation assoc = assEnd.getAssociation();
            final List<AssociationEnd> f =  assoc.getConnection();
            final int oldIndex = f.indexOf(assEnd);
            final int newIndex = newPosition(oldIndex, f.size(), direction);
            f.remove(assEnd);
            f.add(newIndex, assEnd);
        } else if (element instanceof Attribute && parent instanceof AssociationEnd) {
            final Attribute attr = (Attribute) element;
            final AssociationEnd assocEnd = attr.getAssociationEnd();
            final List<Attribute> f = assocEnd.getQualifier();
            final int oldIndex = f.indexOf(assocEnd);
            final int newIndex = newPosition(oldIndex, f.size(), direction);
            f.remove(attr);
            f.add(newIndex, attr);
        } else if (element instanceof Feature) {
            final Feature att = (Feature) element;
            final Classifier cls = att.getOwner();
            final List f = Model.getFacade().getFeatures(cls);
            final int oldIndex = f.indexOf(att);
            final int newIndex = newPosition(oldIndex, f.size(), direction);
            Model.getCoreHelper().removeFeature(cls, att);
            Model.getCoreHelper().addFeature(cls, newIndex, att);
        } else if (element instanceof Parameter && parent instanceof Event) {
            final Parameter param = (Parameter) element;
            final Event event = (Event) parent;
            final List<Parameter> f = event.getParameter();
            final int oldIndex = f.indexOf(param);
            final int newIndex = newPosition(oldIndex, f.size(), direction);
            f.remove(param);
            f.add(newIndex, param);
        } else if (element instanceof Parameter) {
            final Parameter param = (Parameter) element;
            final BehavioralFeature bf = param.getBehavioralFeature();
            final List<Parameter> f = bf.getParameter();
            final int oldIndex = f.indexOf(param);
            final int newIndex = newPosition(oldIndex, f.size(), direction);
            f.remove(param);
            f.add(newIndex, param);
        } else if (element instanceof EnumerationLiteral) {
            final EnumerationLiteral lit = (EnumerationLiteral) element;
            final Enumeration enumeration = lit.getEnumeration();
            final List<EnumerationLiteral> f = enumeration.getLiteral();
            final int oldIndex = f.indexOf(lit);
            final int newIndex = newPosition(oldIndex, f.size(), direction);
            f.remove(lit);
            f.add(newIndex, lit);
        } else if (element instanceof ExtensionPoint && parent instanceof Extend) {
            final ExtensionPoint ep = (ExtensionPoint) element;
            final Extend extend = (Extend) parent;
            final List<ExtensionPoint> f = extend.getExtensionPoint();
            final int oldIndex = f.indexOf(ep);
            final int newIndex = newPosition(oldIndex, f.size(), direction);
            f.remove(ep);
            f.add(newIndex, ep);
        } else if (element instanceof LinkEnd) {
            final LinkEnd le = (LinkEnd) element;
            final Link link = le.getLink();
            final List f = new ArrayList(Model.getFacade().getConnections(link));
            final int oldIndex = f.indexOf(le);
            final int newIndex = newPosition(oldIndex, f.size(), direction);
            f.remove(le);
            f.add(newIndex, le);
            Model.getCoreHelper().setConnections(link, f);
        } else if (element instanceof ExtensionPoint && parent instanceof UseCase) {
            final ExtensionPoint ep = (ExtensionPoint) element;
            final UseCase extend = ep.getUseCase();
            final List f = new ArrayList(Model.getFacade().getExtensionPoints(extend));
            final int oldIndex = f.indexOf(ep);
            final int newIndex = newPosition(oldIndex, f.size(), direction);
            f.remove(ep);
            f.add(newIndex, ep);
            Model.getUseCasesHelper().setExtensionPoints(extend, f);
        }
    }
    
    /*
     * @see org.argouml.model.UmlHelper#move(java.lang.Object, org.argouml.model.UmlHelper.Direction)
     */
    public boolean isMovable(Object metaType) {
        final Class<?>[] movableMetaType = new Class<?> [] {
            Action.class, 
            Argument.class, 
            AssociationEnd.class, 
            Attribute.class, 
            EnumerationLiteral.class,
            Extend.class,
            ExtensionPoint.class,
            Feature.class, 
            LinkEnd.class,
            Operation.class, 
            Parameter.class};
        return Arrays.asList(movableMetaType).contains(metaType);
    }
    
    
    private int newPosition(int index, int size, Direction direction) {
        final int posn;
        if (direction == Direction.DOWN) {
            posn = index + 1;
        } else if (direction == Direction.UP) {
            posn = index - 1;
        } else if (direction == Direction.TOP) {
            posn = 0;
        } else if (direction == Direction.BOTTOM) {
            posn = size - 1;
        } else {
            posn = 0;
        }
        return posn;
    }
}
