// $Id$
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

// File: StateDiagramGraphModel.java
// Classes: StateDiagramGraphModel
// Original Author: your email address here
// $Id$
package org.argouml.uml.diagram.state;

import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.behavioralelements.statemachines.StateMachinesFactory;
import org.argouml.model.uml.behavioralelements.statemachines.StateMachinesHelper;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.foundation.data_types.MPseudostateKind;
/** This class defines a bridge between the UML meta-model
 *  representation of the design and the GraphModel interface used by
 *  GEF.  This class handles only UML MState Digrams.  */

public class StateDiagramGraphModel extends UMLMutableGraphSupport
    implements VetoableChangeListener 
{
    protected static Logger cat = 
        Logger.getLogger(StateDiagramGraphModel.class);


    /** The "home" UML model of this diagram, not all ModelElements in this
     *  graph are in the home model, but if they are added and don't
     *  already have a model, they are placed in the "home model".
     *  Also, elements from other models will have their FigNodes add a
     *  line to say what their model is. */

    protected Object _namespace;

    /** The statemachine we are diagramming */
    protected Object _machine;

    ////////////////////////////////////////////////////////////////
    // accessors

    public Object getNamespace() { return _namespace; }
    public void setNamespace(Object namespace) {
	_namespace = namespace;
    }

    public Object getMachine() { return _machine; }
    public void setMachine(Object sm) {
        
        if(!ModelFacade.isAStateMachine(sm))
            throw new IllegalArgumentException();
        
	if (sm != null) {
	    _machine = sm;
	}
    }

    ////////////////////////////////////////////////////////////////
    // GraphModel implementation

  

    /** Return all ports on node or edge */
    public Vector getPorts(Object nodeOrEdge) {
	Vector res = new Vector();  //wasteful!
	if (org.argouml.model.ModelFacade.isAState(nodeOrEdge)) res.addElement(nodeOrEdge);
	if (org.argouml.model.ModelFacade.isAPseudostate(nodeOrEdge)) res.addElement(nodeOrEdge);
	return res;
    }

    /** Return the node or edge that owns the given port */
    public Object getOwner(Object port) {
	return port;
    }

    /** Return all edges going to given port */
    public Vector getInEdges(Object port) {
	if (org.argouml.model.ModelFacade.isAStateVertex(port)) {
	    return new Vector(ModelFacade.getIncomings(port));
	}
	cat.debug("TODO getInEdges of MState");
	return new Vector(); //wasteful!
    }

    /** Return all edges going from given port */
    public Vector getOutEdges(Object port) {
	if (org.argouml.model.ModelFacade.isAStateVertex(port)) {
	    return new Vector(ModelFacade.getOutgoings(port));
	}
	cat.debug("TODO getOutEdges of MState");
	return new Vector(); //wasteful!
    }

    /** Return one end of an edge */
    public Object getSourcePort(Object edge) {
	if (org.argouml.model.ModelFacade.isATransition(edge)) {
	    return StateMachinesHelper.getHelper()
		.getSource(/*(MTransition)*/ edge);
	}
	cat.debug("TODO getSourcePort of MTransition");
	return null;
    }

    /** Return  the other end of an edge */
    public Object getDestPort(Object edge) {
	if (ModelFacade.isATransition(edge)) {
	    return StateMachinesHelper.getHelper()
		.getDestination(/*(MTransition)*/ edge);
	}
	cat.debug("TODO getDestPort of MTransition");
	return null;
    }


    ////////////////////////////////////////////////////////////////
    // MutableGraphModel implementation

    /** Return true if the given object is a valid node in this graph */
    public boolean canAddNode(Object node) {
	if (node == null) return false;
	if (_nodes.contains(node)) return false;
	return (org.argouml.model.ModelFacade.isAStateVertex(node));
    }

    /** Return true if the given object is a valid edge in this graph */
    public boolean canAddEdge(Object edge)  {
	if (edge == null) return false;
	if (_edges.contains(edge)) return false;
	Object end0 = null, end1 = null;
	if (org.argouml.model.ModelFacade.isATransition(edge)) {
	    Object tr = /*(MTransition)*/ edge;
	    end0 = ModelFacade.getSource(tr);
	    end1 = ModelFacade.getTarget(tr);
	}

	if (end0 == null || end1 == null) return false;
	if (!_nodes.contains(end0)) return false;
	if (!_nodes.contains(end1)) return false;
	return true;
    }



    /** Add the given node to the graph, if valid. */
    public void addNode(Object node) {
	cat.debug("adding state diagram node: " + node);
	if (!canAddNode(node)) return;
	if (!(org.argouml.model.ModelFacade.isAStateVertex(node))) {
	    cat.error("internal error: got past canAddNode");
	    return;
	}
	Object sv = /*(MStateVertex)*/ node;

	if (_nodes.contains(sv)) return;
	_nodes.addElement(sv);
	// TODO: assumes public, user pref for default visibility?
	//if (sv.getNamespace() == null)
	//_namespace.addOwnedElement(sv);
	// TODO: assumes not nested in another composite state
	Object top = /*(MCompositeState)*/ StateMachinesHelper.getHelper().getTop(getMachine());

	ModelFacade.addSubvertex(top, sv);
	//       sv.setParent(top); this is done in setEnclosingFig!!
	//      if ((sv instanceof MState) &&
	//      (sv.getNamespace()==null))
	//      ((MState)sv).setStateMachine(_machine);
	fireNodeAdded(node);
    }

    /** Add the given edge to the graph, if valid. */
    public void addEdge(Object edge) {
	cat.debug("adding state diagram edge!!!!!!");
   
	if (!canAddEdge(edge)) return;
	Object tr = /*(MTransition)*/ edge;
	_edges.addElement(tr);
	fireEdgeAdded(edge);
    }

    public void addNodeRelatedEdges(Object node) {
	if ( org.argouml.model.ModelFacade.isAStateVertex(node) ) {
	    Vector transen = new Vector(ModelFacade.getOutgoings(node));
	    transen.addAll(ModelFacade.getIncomings(node));
	    Iterator iter = transen.iterator();
	    while (iter.hasNext()) {
		Object dep = /*(MTransition)*/ iter.next();
		if (canAddEdge(dep))
		    addEdge(dep);
	    }
	}
    }



    /** Return true if the two given ports can be connected by a
     * kind of edge to be determined by the ports. */
    public boolean canConnect(Object fromPort, Object toPort) {
	if (!(org.argouml.model.ModelFacade.isAStateVertex(fromPort))) {
	    cat.error("internal error not from sv");
	    return false;
	}
	if (!(org.argouml.model.ModelFacade.isAStateVertex(toPort))) {
	    cat.error("internal error not to sv");
	    return false;
	}
	Object fromSV = /*(MStateVertex)*/ fromPort;
	Object toSV = /*(MStateVertex)*/ toPort;

	if (org.argouml.model.ModelFacade.isAFinalState(fromSV)) {
            return false;
        }
	if (org.argouml.model.ModelFacade.isAPseudostate(toSV)) {
	    if (MPseudostateKind.INITIAL.equals(ModelFacade.getKind(toSV))) {
		return false;
            }
        }

	return true;
    }

    /** Contruct and add a new edge of the given kind */
    public Object connect(Object fromPort, Object toPort,
			  java.lang.Class edgeClass)
    {
	//    try {
	if (!(org.argouml.model.ModelFacade.isAStateVertex(fromPort))) {
	    cat.error("internal error not from sv");
	    return null;
	}
	if (!(org.argouml.model.ModelFacade.isAStateVertex(toPort))) {
	    cat.error("internal error not to sv");
	    return null;
	}
	Object fromSV = /*(MStateVertex)*/ fromPort;
	Object toSV = /*(MStateVertex)*/ toPort;

	if (org.argouml.model.ModelFacade.isAFinalState(fromSV))
	    return null;
	if (org.argouml.model.ModelFacade.isAPseudostate(toSV))
	    if (MPseudostateKind.INITIAL
		.equals(ModelFacade.getKind(toSV)))
		return null;

	if (edgeClass == MTransition.class) {
	    Object tr = null;
	    Object comp = ModelFacade.getContainer(fromSV);
	    tr = StateMachinesFactory.getFactory().buildTransition(fromSV,
								   toSV);
	    addEdge(tr);
	    return tr;
	}
	else {
	    cat.debug("wrong kind of edge in StateDiagram connect3 "
		      + edgeClass);
	    return null;
	}
    }


    ////////////////////////////////////////////////////////////////
    // VetoableChangeListener implementation

    public void vetoableChange(PropertyChangeEvent pce) {
	//throws PropertyVetoException

	if ("ownedElement".equals(pce.getPropertyName())) {
	    Vector oldOwned = (Vector) pce.getOldValue();
	    Object eo = /*(MElementImport)*/ pce.getNewValue();
	    Object me = ModelFacade.getModelElement(eo);
	    if (oldOwned.contains(eo)) {
		cat.debug("model removed " + me);
		if (org.argouml.model.ModelFacade.isAState(me)) removeNode(me);
		if (org.argouml.model.ModelFacade.isAPseudostate(me)) removeNode(me);
		if (org.argouml.model.ModelFacade.isATransition(me)) removeEdge(me);
	    }
	    else {
		cat.debug("model added " + me);
	    }
	}
    }


    static final long serialVersionUID = -8056507319026044174L;

    /**
     * @param newNode this is the new node that one of the ends is dragged to.
     * @param oldNode this is the existing node that is already connected.
     * @param edge this is the edge that is being dragged/rerouted
     * @return true if a transition is being rerouted between two states.
     */
    public boolean canChangeConnectedNode(Object newNode, Object oldNode,
					  Object edge)
    {
    	// prevent no changes... 
	if ( newNode == oldNode)
	    return false;
            
	// check parameter types:
	if ( !(org.argouml.model.ModelFacade.isAState(newNode) ||
	       org.argouml.model.ModelFacade.isAState(oldNode) ||
	       org.argouml.model.ModelFacade.isATransition(edge) ) )
	    return false;
            
	return true;
    }

    /**
     * Reroutes the connection to the old node to be connected to
     * the new node.
     *
     * @param newNode this is the new node that one of the ends is dragged to.
     * @param oldNode this is the existing node that is already connected.
     * @param edge this is the edge that is being dragged/rerouted
     * @param isSource tells us which end is being rerouted.
     */
    public void changeConnectedNode(Object newNode, Object oldNode,
				    Object edge, boolean isSource)
    {
      
	if (isSource)
	    ModelFacade.setSource(edge, newNode);
	else
	    ModelFacade.setTarget(edge, newNode);
          
    }
  
} /* end class StateDiagramGraphModel */