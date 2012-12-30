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

package org.argouml.sequence2.diagram;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.Model;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Mode;
import org.tigris.gef.base.ModeManager;
import org.tigris.gef.graph.MutableGraphModel;

/**
 * The sequence graph model is the bridge between the UML meta-model
 * representation of the design and the graph model of GEF.
 * @author 5eichler@informatik.uni-hamburg.de
 * @author penyaskito
 */
class SequenceDiagramGraphModel extends UMLMutableGraphSupport implements
        VetoableChangeListener, PropertyChangeListener, MutableGraphModel {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(SequenceDiagramGraphModel.class.getName());

    /**
     * The collaboration this sequence diagram belongs too.
     */
    private Object collaboration;

    /**
     * The interaction that is shown on the sequence diagram.
     */
    private Object interaction;


    /**
     * Default constructor. Constructs a model and a collaboration in
     * the root of the current project.
     */
    public SequenceDiagramGraphModel() {
        super();
        // TODO: Auto-generated constructor stub
    }

    /**
     * @param element The Fig that we want his available ports.
     * @return The available ports for a given element
     * @see org.tigris.gef.graph.GraphModel#getPorts(java.lang.Object)
     */
    public List getPorts(Object element) {
        List ports = new LinkedList();
        // if is a classifier role, it must return all the related messages.
        if (Model.getFacade().isAClassifierRole(element)) {
            ports.addAll(Model.getFacade().getReceivedMessages(element));
            ports.addAll(Model.getFacade().getSentMessages(element));
        }
        // if is a message, it must return the sender
        // and the receiver of the message.
        else if (Model.getFacade().isAMessage(element)) {
            ports.add(Model.getFacade().getSender(element));
            ports.add(Model.getFacade().getReceiver(element));
        }
        return ports;
    }

    /**
     * @param element
     * @return The outcoming edges.
     * @see org.tigris.gef.graph.GraphModel#getInEdges(java.lang.Object)
     */
    public List getInEdges(Object element) {
        List ports = new LinkedList();
        // If is a classifier role, it must return the sent messages.
        // In other cases, returns an empty list.
        // TODO: Must be the incoming messages or the sent ones?
        if (Model.getFacade().isAClassifierRole(element)) {
            ports.addAll(Model.getFacade().getSentMessages(element));
        }
        return ports;
    }

    /**
     * @param element
     * @return The incoming edges.
     * @see org.tigris.gef.graph.GraphModel#getOutEdges(java.lang.Object)
     */
    public List getOutEdges(Object element) {
        List ports = new LinkedList();
        // If is a classifier role, it must return the received messages.
        // In other cases, returns an empty list.
        // TODO: Must be the outgoing messages or the received ones?
        if (Model.getFacade().isAClassifierRole(element)) {
            ports.addAll(Model.getFacade().getReceivedMessages(element));
        }
        return ports;
    }

    /**
     * @param port
     * @return
     * @see org.tigris.gef.graph.BaseGraphModel#getOwner(java.lang.Object)
     */
    public Object getOwner(Object port) {
        return port;
    }

    /**
     * Gets the collaboration that is shown on the sequence diagram.<p>
     *
     * @return the collaboration of the diagram.
     */
    public Object getCollaboration() {
        if (collaboration == null) {
            LOG.log(Level.FINE, "The collaboration is null so creating a new collaboration");
            collaboration =
                Model.getCollaborationsFactory().buildCollaboration(
                        ProjectManager.getManager().getCurrentProject()
                        .getRoot());
        }

        return collaboration;
    }

    /**
     * Sets the collaboration that is shown at the sequence diagram.
     * @param c the collaboration
     */
    public void setCollaboration(Object c) {
        LOG.log(Level.FINE, "Setting the collaboration of sequence diagram to {0}", c);
        collaboration = c;
        Collection interactions = Model.getFacade().getInteractions(c);
        if (!interactions.isEmpty()) {
            interaction = interactions.iterator().next();
        }
    }

    /**
     * Gets the interaction that is shown on the sequence diagram.
     * @return the interaction of the diagram.
     */
    private Object getInteraction() {
        if (interaction == null) {
            interaction =
                Model.getCollaborationsFactory().buildInteraction(
                    collaboration);
            LOG.log(Level.FINE, "Interaction built.");
            Model.getPump().addModelEventListener(this, interaction);
        }
        return interaction;
    }

    /**
     * In UML1.4 the sequence diagram is owned by a collaboration.
     * In UML2 it is owned by an Interaction (which might itself be owned by a
     * collaboration or some other namespace)
     * @return the owner of the sequence diagram
     */
    public Object getOwner() {
        if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
            return getCollaboration();
        } else {
            return getInteraction();
        }
    }

    /*
     * @see org.tigris.gef.graph.MutableGraphModel#canAddNode(java.lang.Object)
     */
    @Override
    public boolean canAddNode(Object node) {
        if (node == null) {
            return false;
        }
        if (getNodes().contains(node)) {
            return false;
        }
        if (Model.getFacade().isAComment(node)) {
            // Comments from anywhere in the model are allowed
            return true;
        }
        return Model.getFacade().isAModelElement(node)
                // All other types of elements must be in this namespace
                && Model.getFacade().getNamespace(node) == getCollaboration();
    }

    /*
     * @see org.tigris.gef.graph.MutableGraphModel#canAddEdge(java.lang.Object)
     */
    @Override
    public boolean canAddEdge(Object edge) {
        if (edge == null) {
            return false;
        }

        if (getEdges().contains(edge)) {
            return false;
        }

        Object end0 = null;
        Object end1 = null;

        if (Model.getFacade().isAMessage(edge)) {
            end0 = Model.getFacade().getSender(edge);
            end1 = Model.getFacade().getReceiver(edge);
        } else if (edge instanceof CommentEdge) {
            end0 = ((CommentEdge) edge).getSource();
            end1 = ((CommentEdge) edge).getDestination();
        } else {
            return false;
        }

        // Both ends must be defined and nodes that are on the graph already.
        if (end0 == null || end1 == null) {
            LOG.log(Level.SEVERE, "Edge rejected. Its ends are not attached to anything");
            return false;
        }

        if (!containsNode(end0) && !containsEdge(end0)) {
            LOG.log(Level.SEVERE, "Edge rejected. Its source end is attached to "
                    + end0
                    + " but this is not in the graph model");
            return false;
        }
        if (!containsNode(end1) && !containsEdge(end1)) {
            LOG.log(Level.SEVERE, "Edge rejected. Its destination end is attached to "
                    + end1
                    + " but this is not in the graph model");
            return false;
        }

        return true;
    }

    /*
     * @see org.tigris.gef.graph.MutableGraphModel#addNode(java.lang.Object)
     */
    @Override
    public void addNode(Object node) {
        if (canAddNode(node)) {
            getNodes().add(node);
            fireNodeAdded(node);
        }
    }


    public void vetoableChange(PropertyChangeEvent pce)
        throws PropertyVetoException {
        // TODO: Auto-generated method stub

    }

    /**
     * Look for delete events of the interaction that this diagram
     * represents. Null our interaction reference if detected.
     * @param evt the property change event
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt instanceof DeleteInstanceEvent
                && evt.getSource() == interaction) {
            Model.getPump().removeModelEventListener(this, interaction);
            interaction = null;
        }
    }

    /**
     * Creates a link based on the given from and toPort. The fromPort
     * should always point to a MessageCoordinates instance. The toPort
     * can point to a MessageCoordinates instance or to a Object
     * instance. On a sequence diagram you can only draw Messages. So
     * other edgeClasses then links are not supported.
     * {@inheritDoc}
     * @see org.tigris.gef.graph.MutableGraphModel#connect(
     *          Object, Object, Class)
     */
    @Override
    public Object connect(Object fromPort, Object toPort, Object edgeType) {
        if (!canConnect(fromPort, toPort, edgeType)) {
            return null;
        }
        if (edgeType == CommentEdge.class) {
            return super.connect(fromPort, toPort, edgeType);
        }
        Editor curEditor = Globals.curEditor();
        ModeManager modeManager = curEditor.getModeManager();
        Mode mode = modeManager.top();
        Hashtable args = mode.getArgs();
        Object actionType = args.get("action");
        return connectMessage(fromPort, toPort, actionType);
    }

    /**
     * Creates a link based on the given from and toPort. The fromPort
     * should always point to a MessageCoordinates instance. The toPort
     * can point to a MessageCoordinates instance or to a Object
     * instance. On a sequence diagram you can only draw Messages. So
     * other edgeClasses then links are not supported.
     *
     * @see org.tigris.gef.graph.MutableGraphModel#connect(
     *          Object, Object, Class)
     */
    public Object connectMessage(Object fromPort, Object toPort, Object messageSort) {
        // TODO: Lets move this behind the model interface
        if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
            return createMessage1(fromPort, toPort, messageSort);
        } else {
            return createMessage2(fromPort, toPort, messageSort);
        }
    }

    private Object createMessage1(Object fromPort, Object toPort, Object messageSort) {
        Object edge = null;
        Object action = null;
        if (Model.getMessageSort().getSynchCall().equals(messageSort)) {
            action = Model.getCommonBehaviorFactory().createCallAction();
            Model.getCommonBehaviorHelper().setAsynchronous(action, false);
        } else if (Model.getMessageSort().getASynchCall().equals(messageSort)) {
            action = Model.getCommonBehaviorFactory().createCallAction();
            Model.getCommonBehaviorHelper().setAsynchronous(action, true);
        } else if (Model.getMessageSort().getCreateMessage()
                .equals(messageSort)) {
            action = Model.getCommonBehaviorFactory().createCreateAction();
            Model.getCommonBehaviorHelper().setAsynchronous(action, false);
        } else if (Model.getMessageSort().getReply()
                .equals(messageSort)) {
            action = Model.getCommonBehaviorFactory().createReturnAction();
            Model.getCommonBehaviorHelper().setAsynchronous(action, true);
        } else if (Model.getMessageSort().getDeleteMessage()
                .equals(messageSort)) {
            action = Model.getCommonBehaviorFactory().createDestroyAction();
            Model.getCommonBehaviorHelper().setAsynchronous(action, false);
        } else if (Model.getMessageSort().getASynchSignal()
                .equals(messageSort)) {
            action = Model.getCommonBehaviorFactory().createSendAction();
            Model.getCommonBehaviorHelper().setAsynchronous(action, true);
        } else if (Model.getMetaTypes().getTerminateAction()
                .equals(messageSort)) {
            // not implemented yet
        }
        if (fromPort != null && toPort != null) {
            Object associationRole =
                Model.getCollaborationsHelper().getAssociationRole(
                    fromPort,
                    toPort);
            if (associationRole == null) {
                associationRole =
                    Model.getCollaborationsFactory().buildAssociationRole(
                            fromPort, toPort);
            }

            Object message =
                Model.getCollaborationsFactory().buildMessage(
                    getInteraction(),
                    associationRole);
            if (action != null) {
                Model.getCollaborationsHelper().setAction(message, action);
                Model.getCoreHelper().setNamespace(action, getCollaboration());
            }
            Model.getCollaborationsHelper()
                .setSender(message, fromPort);
            Model.getCommonBehaviorHelper()
                .setReceiver(message, toPort);

            addEdge(message);
            edge = message;
        }
        if (edge == null) {
            LOG.log(Level.FINE, "Incorrect edge");
        }
        return edge;

    }

    private Object createMessage2(Object fromPort, Object toPort, Object messageSort) {
        if (fromPort != null && toPort != null) {

            Object message =
                Model.getCollaborationsFactory().buildMessage(
                    fromPort,
                    toPort);
            Model.getCollaborationsHelper().setMessageSort(message, messageSort);

            addEdge(message);
            return message;
        }
        LOG.log(Level.FINE, "Incorrect edge");
        return null;

    }



    /*
     * @see org.tigris.gef.graph.MutableGraphModel#addEdge(java.lang.Object)
     */
    @Override
    public void addEdge(Object edge) {
        if (canAddEdge(edge)) {
            getEdges().add(edge);
            fireEdgeAdded(edge);
        }
    }
}
