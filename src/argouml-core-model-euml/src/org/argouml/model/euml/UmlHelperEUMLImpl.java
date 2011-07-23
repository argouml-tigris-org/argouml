// $Id$
/***************************************************************************
 * Copyright (c) 2007,2010 Tom Morris and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial implementation 
 ***************************************************************************/
package org.argouml.model.euml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.argouml.model.Model;
import org.argouml.model.UmlHelper;
import org.eclipse.uml2.uml.Action;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Extend;
import org.eclipse.uml2.uml.ExtensionPoint;
import org.eclipse.uml2.uml.Feature;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterSet;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.UseCase;

/**
 * The implementation of the UmlHelper for EUML2.
 */
class UmlHelperEUMLImpl implements UmlHelper {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     * 
     * @param implementation
     *            The ModelImplementation.
     */
    public UmlHelperEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public void addListenersToModel(Object model) {
        // Nothing to do

    }

    // TODO: Model implementation independent
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
        if (relationship instanceof Relationship) {
            // handles all children of relationship including extend and
            // include which are not members of core
            return modelImpl.getCoreHelper().getSource(relationship);
        } else if (relationship instanceof Transition) {
            return modelImpl.getStateMachinesHelper().getSource(relationship);
        } else if (relationship instanceof ActivityEdge) {
            return ((ActivityEdge) relationship).getSource();
        } else if (relationship instanceof Property) {
            // TODO: We expect an association end here - 
            // check more carefully? - tfm
            return modelImpl.getCoreHelper().getSource(relationship);
        } else if (relationship instanceof Message) {
            return modelImpl.getFacade().getSender(relationship);
        }
        throw new IllegalArgumentException(
                "Getting source of this object is not implemented" //$NON-NLS-1$
                + relationship);
    }

    /*
     * @see org.argouml.model.UmlHelper#getDestination(java.lang.Object)
     */
    public Object getDestination(Object relationship) {
        if (relationship instanceof Relationship) {
            // handles all children of relationship including extend and
            // include which are not members of core
            return modelImpl.getCoreHelper().getDestination(relationship);
        } else if (relationship instanceof ActivityEdge) {
            return ((ActivityEdge) relationship).getTarget();
        } else if (relationship instanceof Transition) {
            return modelImpl.getStateMachinesHelper().
                    getDestination(relationship);
        } else if (relationship instanceof Property) {
            // TODO: We expect an association end here - 
            // check more carefully? - tfm
            return modelImpl.getCoreHelper().getDestination(relationship);
        } else if (relationship instanceof Message) {
            return modelImpl.getFacade().getReceiver(relationship);
        }
        throw new IllegalArgumentException(
                "Getting destination of this object is not implemented" //$NON-NLS-1$
                + relationship);
    }

   /*
     * @see org.argouml.model.UmlHelper#move(java.lang.Object, org.argouml.model.UmlHelper.Direction)
     */
    public void move(Object parent, Object element, Direction direction) {
        //TODO: More work require - code below is from MDR implementation
//        if (false) {
//        if (element instanceof Argument) {
//            final Argument arg = (Argument) element;
//            final Action action = arg.getAction();
//            final List<Argument> f = action.getActualArgument();
//            final int oldIndex = f.indexOf(arg);
//            final int newIndex = newPosition(oldIndex, f.size(), direction);
//            f.remove(arg);
//            f.add(newIndex, arg);
//        } else if (element instanceof Action) {
//            final Action act = (Action) element;
//            final ActionSequence actionSequence = act.getInGroups();
//            final List<Action> f = actionSequence.getAction();
//            final int oldIndex = f.indexOf(act);
//            final int newIndex = newPosition(oldIndex, f.size(), direction);
//            f.remove(actionSequence);
//            f.add(newIndex, actionSequence);
//        } else if (element instanceof AssociationEnd) {
//            final AssociationEnd assEnd = (AssociationEnd) element;
//            final UmlAssociation assoc = assEnd.getAssociation();
//            final List<AssociationEnd> f =  assoc.getConnection();
//            final int oldIndex = f.indexOf(assEnd);
//            final int newIndex = newPosition(oldIndex, f.size(), direction);
//            f.remove(assEnd);
//            f.add(newIndex, assEnd);
//        } else if (element instanceof Property
//                   && parent instanceof AssociationEnd) {
//            final Attribute attr = (Attribute) element;
//            final AssociationEnd assocEnd = attr.getAssociationEnd();
//            final List<Attribute> f = assocEnd.getQualifier();
//            final int oldIndex = f.indexOf(assocEnd);
//            final int newIndex = newPosition(oldIndex, f.size(), direction);
//            f.remove(attr);
//            f.add(newIndex, attr);
//        } else 
        if (element instanceof Feature) {
            final Feature att = (Feature) element;
            final Element cls = att.getOwner();
            final List f = Model.getFacade().getFeatures(cls);
            final int oldIndex = f.indexOf(att);
            final int newIndex = newPosition(oldIndex, f.size(), direction);
            Model.getCoreHelper().removeFeature(cls, att);
            Model.getCoreHelper().addFeature(cls, newIndex, att);

        } else if (element instanceof Parameter && parent instanceof Behavior) {
            final Parameter param = (Parameter) element;
            final Behavior behavior = (Behavior) parent;
            final List<Parameter> f = behavior.getOwnedParameters();
            final int oldIndex = f.indexOf(param);
            final int newIndex = newPosition(oldIndex, f.size(), direction);
            f.remove(param);
            f.add(newIndex, param);
        } else if (element instanceof Parameter
                   && parent instanceof ParameterSet) {
            final Parameter param = (Parameter) element;
            final ParameterSet set = (ParameterSet) parent;
            final List<Parameter> f = set.getParameters();
            final int oldIndex = f.indexOf(param);
            final int newIndex = newPosition(oldIndex, f.size(), direction);
            f.remove(param);
            f.add(newIndex, param);
            // UML 1.4 case
//        } else if (element instanceof Parameter && parent instanceof Event) {
//            final Parameter param = (Parameter) element;
//            final Event event = (Event) parent;
//            final List<Parameter> f = event.getParameter();
//            final int oldIndex = f.indexOf(param);
//            final int newIndex = newPosition(oldIndex, f.size(), direction);
//            f.remove(param);
//            f.add(newIndex, param);
        } else if (element instanceof Parameter) {
            final Parameter param = (Parameter) element;
            final Operation operation = param.getOperation();
            final List<Parameter> f = operation.getOwnedParameters();
            final int oldIndex = f.indexOf(param);
            final int newIndex = newPosition(oldIndex, f.size(), direction);
            f.remove(param);
            f.add(newIndex, param);
        } else if (element instanceof EnumerationLiteral) {
            final EnumerationLiteral lit = (EnumerationLiteral) element;
            final Enumeration enumeration = lit.getEnumeration();
            final List<EnumerationLiteral> f = enumeration.getOwnedLiterals();
            final int oldIndex = f.indexOf(lit);
            final int newIndex = newPosition(oldIndex, f.size(), direction);
            f.remove(lit);
            f.add(newIndex, lit);
        } else if (element instanceof ExtensionPoint
                   && parent instanceof Extend) {
            final ExtensionPoint ep = (ExtensionPoint) element;
            final Extend extend = (Extend) parent;
            final List<ExtensionPoint> f = extend.getExtensionLocations();
            final int oldIndex = f.indexOf(ep);
            final int newIndex = newPosition(oldIndex, f.size(), direction);
            f.remove(ep);
            f.add(newIndex, ep);
//        } else if (element instanceof LinkEnd) {
//            final LinkEnd le = (LinkEnd) element;
//            final Link link = le.getLink();
//            final List f =
//                    new ArrayList(Model.getFacade().getConnections(link));
//            final int oldIndex = f.indexOf(le);
//            final int newIndex = newPosition(oldIndex, f.size(), direction);
//            f.remove(le);
//            f.add(newIndex, le);
//            Model.getCoreHelper().setConnections(link, f);
        } else if (element instanceof ExtensionPoint
                   && parent instanceof UseCase) {
            final ExtensionPoint ep = (ExtensionPoint) element;
            final UseCase extend = ep.getUseCase();
            final List f =
                new ArrayList(Model.getFacade().getExtensionPoints(extend));
            final int oldIndex = f.indexOf(ep);
            final int newIndex = newPosition(oldIndex, f.size(), direction);
            f.remove(ep);
            f.add(newIndex, ep);
            Model.getUseCasesHelper().setExtensionPoints(ep, f);
        }
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
            posn = size;
        } else {
            posn = 0;
        }
        return posn;
    }

    /*
     * @see org.argouml.model.UmlHelper#move(java.lang.Object, org.argouml.model.UmlHelper.Direction)
     */
    public boolean isMovable(Object metaType) {
        final Class<?>[] movableMetaType = {
            Action.class, 
            EnumerationLiteral.class,
            Extend.class,
            ExtensionPoint.class,
            Operation.class,
            Parameter.class,
            Property.class};
        return Arrays.asList(movableMetaType).contains(metaType);
    }


}
