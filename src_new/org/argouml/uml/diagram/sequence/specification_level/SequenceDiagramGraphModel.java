// Copyright (c) 1996-2002 The Regents of the University of California. All
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


// File: SequenceDiagramGraphModel.java
// Classes: SequenceDiagramGraphModel
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$

package org.argouml.uml.diagram.sequence.specification_level;

import org.apache.log4j.Category;
import org.argouml.model.uml.UmlFactory;
// import org.argouml.model.uml.behavioralelements.commonbehavior.CommonBehaviorHelper;
import org.argouml.model.uml.behavioralelements.collaborations.CollaborationsHelper;

import java.util.*;
import java.beans.*;
import java.awt.Point;
import java.awt.event.MouseEvent;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.model_management.*;


import org.tigris.gef.base.Mode;
import org.tigris.gef.base.ModeManager;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;

import org.argouml.ui.*;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;

/** This class defines a bridge between the UML meta-model
 *  representation of the design and the GraphModel interface used by
 *  GEF.  This class handles only UML Sequence Digrams.  */

public class SequenceDiagramGraphModel extends UMLMutableGraphSupport implements VetoableChangeListener {

    protected static Category cat = Category.getInstance(SequenceDiagramGraphModel.class);

    ////////////////////////////////////////////////////////////////
    // instance variables
  
    /** The "home" UML model of this diagram, not all ModelElements in this
     *  graph are in the home model, but if they are added and don't
     *  already have a model, they are placed in the "home model".
     *  Also, elements from other models will have their FigNodes add a
     *  line to say what their model is. */

    /** The Sequence/interaction we are diagramming */
    protected MNamespace _Sequence;
    //protected MInteraction _interaction;

    ////////////////////////////////////////////////////////////////
    // accessors

    public MNamespace getNamespace() {
        return _Sequence;
    }

    public void setNamespace(MNamespace m) {
        _Sequence = m;
    }

    ////////////////////////////////////////////////////////////////
    // GraphModel implementation

    /**
     * Return all nodes in the graph
     */
    public Vector getNodes() {
        return _nodes;
    }

    /**
     * Return all nodes in the graph
     */
    public Vector getEdges() {
        return _edges;
    }

    /**
     * Return all ports on node or edge
     */
    public Vector getPorts(Object nodeOrEdge) {
        Vector res = new Vector();  //wasteful!
        if (nodeOrEdge instanceof MClassifierRole) res.addElement(nodeOrEdge);
        return res;
    }

    /**
     * Return the node or edge that owns the given port
     */
    public Object getOwner(Object port) {
        return port;
    }

    /**
     * Return all edges going to given port
     */
    public Vector getInEdges(Object port) {
        Vector res = new Vector();
        if (port instanceof MClassifierRole) {
            MClassifierRole mc = (MClassifierRole) port;
            Collection ends = mc.getMessages2();
            if (ends == null) return res; // empty Vector
            Iterator iter = ends.iterator();
            while (iter.hasNext()) {
                MMessage m = (MMessage)iter.next();
                res.addElement(m.getCommunicationConnection());
            }
        }
        return res;
    }

    /**
     * Return all edges going from given port
     */
    public Vector getOutEdges(Object port) {
        Vector res = new Vector();
        if (port instanceof MClassifierRole) {
            MClassifierRole mc = (MClassifierRole) port;
            Collection ends = mc.getMessages1();
            if (ends == null) return res; // empty Vector
            Iterator iter = ends.iterator();
            while (iter.hasNext()) {
                MMessage m = (MMessage)iter.next();
                res.addElement(m.getCommunicationConnection());
            }
        }
        return res;
    }

    /**
     * Return one end of an edge
     */
    public Object getSourcePort(Object edge) {
        if (edge instanceof MAssociationRole) {
            return CollaborationsHelper.getHelper().getSource((MAssociationRole)edge);
        }
        cat.debug("TODO getSourcePort");
        return null;
    }

    /**
     * Return  the other end of an edge
     */
    public Object getDestPort(Object edge) {
        if (edge instanceof MAssociationRole) {
            return CollaborationsHelper.getHelper().getDestination((MAssociationRole)edge);
        }
        cat.debug("TODO getDestPort");
        return null;
    }

    ////////////////////////////////////////////////////////////////
    // MutableGraphModel implementation

    /**
     * Return true if the given object is a valid node in this graph
     */
    public boolean canAddNode(Object node) {
        if (node == null) return false;
        if (_nodes.contains(node)) return false;
        return (node instanceof MClassifierRole || node instanceof MMessage);
    }

    /**
     * Return true if the given object is a valid edge in this graph
     */
    public boolean canAddEdge(Object edge)  {
        if (edge == null) return false;
        Object end0 = null;
        Object end1 = null;
        if (edge instanceof MAssociationRole) {
            end0 = CollaborationsHelper.getHelper().getSource((MAssociationRole)edge);
            end1 = CollaborationsHelper.getHelper().getDestination((MAssociationRole)edge);
        }
        if (end0 == null || end1 == null) return false;
        if (!_nodes.contains(end0)) return false;
        if (!_nodes.contains(end1)) return false;
        return true;
    }

    /** Add the given node to the graph, if valid. */
    public void addNode(Object node) {
        if (!canAddNode(node)) return;
        _nodes.addElement(node);
        // TODO: assumes public, user pref for default visibility?
        if (node instanceof MModelElement) {
            _Sequence.addOwnedElement((MModelElement) node);
            // ((MClassifier)node).setNamespace(_Sequence.getNamespace());
        }
        fireNodeAdded(node);
    }

    /**
     * Add the given edge to the graph, if valid.
     */
    public void addEdge(Object edge) {
        if (!canAddEdge(edge)) return;
        _edges.addElement(edge);
        // TODO: assumes public
        if (edge instanceof MModelElement) {
            _Sequence.addOwnedElement((MModelElement) edge);
        }
        fireEdgeAdded(edge);
    }

    public void addNodeRelatedEdges(Object node) {
        if ( node instanceof MClassifierRole ) {
            Collection ends = ((MClassifierRole)node).getMessages1();
            Iterator iter = ends.iterator();
            while (iter.hasNext()) {
                MMessage m = (MMessage)iter.next();
                if(canAddEdge(m.getCommunicationConnection()))
                        addEdge(m.getCommunicationConnection());
                return;
            }
        }
    }

    /**
     * Return true if the two given ports can be connected by a
     * kind of edge to be determined by the ports.
     */
    public boolean canConnect(Object fromP, Object toP) {
        if ((fromP instanceof MClassifierRole) && (toP instanceof MClassifierRole)) return true;
        return false;
    }


    /**
     * Contruct and add a new edge of a kind determined by the ports
     */
    public Object connect(Object fromPort, Object toPort) {
        throw new UnsupportedOperationException("should not enter here!");
    }

    /** Contruct and add a new edge of the given kind */
    public Object connect(Object fromPort, Object toPort, java.lang.Class edgeClass) {
System.out.println("*** SequenceDiagramGraphModel.connect ***");
        if (edgeClass == MAssociationRole.class && fromPort instanceof MClassifierRole && toPort instanceof MClassifierRole) {
            MAssociationRole    ma  = UmlFactory.getFactory().getCollaborations().createAssociationRole();
            MAssociationEndRole ae0 = UmlFactory.getFactory().getCollaborations().createAssociationEndRole();
            ae0.setType((MClassifierRole) fromPort);
            MAssociationEndRole ae1 = UmlFactory.getFactory().getCollaborations().createAssociationEndRole();
            ae1.setType((MClassifierRole) toPort);
            ma.addConnection(ae0);
            ma.addConnection(ae1);
            addEdge(ma);
            // add stimulus with given action, taken from global mode
            Editor curEditor = Globals.curEditor();
            if (ma.getMessages() == null || ma.getMessages().size() == 0) {
                ModeManager modeManager = curEditor.getModeManager();
                Mode mode = (Mode)modeManager.top();
                Hashtable args = mode.getArgs();
                if ( args != null ) {
                    MAction action=null;
                    // get "action"-Class taken from global mode
                    Class actionClass = (Class) args.get("action");
                    if (actionClass != null) {
                        //create the action
                        if(actionClass==MCallAction.class)
                                action=UmlFactory.getFactory().getCommonBehavior().createCallAction();
                        else if(actionClass==MCreateAction.class)
                                action=UmlFactory.getFactory().getCommonBehavior().createCreateAction();
                        else if(actionClass==MDestroyAction.class)
                                action=UmlFactory.getFactory().getCommonBehavior().createDestroyAction();
                        else if(actionClass==MSendAction.class)
                                action=UmlFactory.getFactory().getCommonBehavior().createSendAction();
                        else if(actionClass==MReturnAction.class)
                                action=UmlFactory.getFactory().getCommonBehavior().createReturnAction();
                        if (action != null) {
                            // determine action type of arguments in mode
                            action.setName("new action");
                            if (action instanceof MSendAction || action instanceof MReturnAction) {
                                action.setAsynchronous(true);
                            } else {
                                action.setAsynchronous(false);
                            }
                            // create stimulus
                            MMessage message = UmlFactory.getFactory().getCollaborations().createMessage();
                            //if we want to allow the sequence number to appear
                            /*UMLSequenceDiagram sd=(UMLSequenceDiagram) ProjectBrowser.TheInstance.getActiveDiagram();
                            int num=sd.getNumStimuluss()+1;
                            stimulus.setName(""+num);*/
                            //otherwise: no sequence number
                            message.setName("");
                            //set sender and receiver
                            message.setSender((MClassifierRole)fromPort);
                            message.setReceiver((MClassifierRole)toPort);
                            // set action type
                            message.setAction(action);
                            // add stimulus to link
                            ma.addMessage(message);
                            // add new modelelements: stimulus and action to namesapce
                            _Sequence.addOwnedElement(message);
                            _Sequence.addOwnedElement(action);
                        }
                    }
                }
            }
            return ma;
        } else {
            cat.debug("Incorrect edge");
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////
    // VetoableChangeListener implementation

    public void vetoableChange(PropertyChangeEvent pce) {
        if ("ownedElement".equals(pce.getPropertyName())) {
            Vector oldOwned = (Vector) pce.getOldValue();
            MElementImport eo = (MElementImport) pce.getNewValue();
            MModelElement me = eo.getModelElement();
            if (oldOwned.contains(eo)) {
                cat.debug("model removed " + me);
                if (me instanceof MClassifierRole) removeNode(me);
                if (me instanceof MMessage) removeNode(me);
                if (me instanceof MAssociation) removeEdge(me);
            } else {
                cat.debug("model added " + me);
            }
        }
    }

} /* end class SequenceDiagramGraphModel */

