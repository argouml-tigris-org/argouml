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


// File: SequenceDiagramGraphModel.java
// Classes: SequenceDiagramGraphModel
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$

package org.argouml.uml.diagram.sequence;

import org.apache.log4j.Logger;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.behavioralelements.commonbehavior.CommonBehaviorHelper;

import java.util.*;
import java.beans.*;

import org.tigris.gef.base.Mode;
import org.tigris.gef.base.ModeManager;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;

import org.argouml.ui.*;
import org.argouml.uml.diagram.UMLMutableGraphSupport;

/** This class defines a bridge between the UML meta-model
 *  representation of the design and the GraphModel interface used by
 *  GEF.  This class handles only UML Sequence Digrams.  */

public class SequenceDiagramGraphModel extends UMLMutableGraphSupport
    implements VetoableChangeListener 
{
    protected static Logger cat = 
        Logger.getLogger(SequenceDiagramGraphModel.class);
    ////////////////////////////////////////////////////////////////
    // instance variables
  
    /** The "home" UML model of this diagram, not all ModelElements in this
     *  graph are in the home model, but if they are added and don't
     *  already have a model, they are placed in the "home model".
     *  Also, elements from other models will have their FigNodes add a
     *  line to say what their model is. */

    /** The Sequence / interaction we are diagramming */
    protected Object _Sequence;
    //protected MInteraction _interaction;

    ////////////////////////////////////////////////////////////////
    // accessors

    public Object getNamespace() { return _Sequence; }
    public void setNamespace(Object/*MNamespace*/ m) {
	_Sequence = m;
    }

    ////////////////////////////////////////////////////////////////
    // GraphModel implementation

    /** Return all nodes in the graph */
    public Vector getNodes() { return _nodes; }

    /** Return all nodes in the graph */
    public Vector getEdges() { return _edges; }

    /** Return all ports on node or edge */
    public Vector getPorts(Object nodeOrEdge) {
	Vector res = new Vector();  //wasteful!
	if (org.argouml.model.ModelFacade.isAObject(nodeOrEdge)) res.addElement(nodeOrEdge);
	return res;
    }

    /** Return the node or edge that owns the given port */
    public Object getOwner(Object port) {
	return port;
    }

    /** Return all edges going to given port */
    public Vector getInEdges(Object port) {
	Vector res = new Vector(); //wasteful!
	if (ModelFacade.isAObject(port)) {
	    Object mo = /*(MObject)*/ port;
	    Collection ends = ModelFacade.getLinkEnds(mo);
	    if (ends == null) return res; // empty Vector
	    Iterator iter = ends.iterator();
	    while (iter.hasNext()) {
		Object aer = /*(MLinkEnd)*/ iter.next();
		res.addElement(ModelFacade.getLink(aer));
	    }
	}
	return res;
    }

    /** Return all edges going from given port */
    public Vector getOutEdges(Object port) {
	return new Vector(); // TODO?
    }

    /** Return one end of an edge */
    public Object getSourcePort(Object edge) {
	if (org.argouml.model.ModelFacade.isALink(edge)) {
	    return CommonBehaviorHelper.getHelper().getSource(/*(MLink)*/ edge);
	}
	cat.debug("TODO getSourcePort");
	return null;
    }

    /** Return  the other end of an edge */
    public Object getDestPort(Object edge) {
	if (org.argouml.model.ModelFacade.isALink(edge)) {
	    return CommonBehaviorHelper.getHelper()
		.getDestination(/*(MLink)*/ edge);
	}
	cat.debug("TODO getDestPort");
	return null;
    }


    ////////////////////////////////////////////////////////////////
    // MutableGraphModel implementation

    /** Return true if the given object is a valid node in this graph */
    public boolean canAddNode(Object node) {
	if (node == null) return false;
	if (_nodes.contains(node)) return false;
	return (org.argouml.model.ModelFacade.isAObject(node) || org.argouml.model.ModelFacade.isAStimulus(node));
    }

    /** Return true if the given object is a valid edge in this graph */
    public boolean canAddEdge(Object edge)  {
	if (edge == null) return false;
	Object end0 = null;
	Object end1 = null;
	if (org.argouml.model.ModelFacade.isALink(edge)) {
	    end0 = CommonBehaviorHelper.getHelper().getSource(/*(MLink)*/ edge);
	    end1 =
		CommonBehaviorHelper.getHelper().getDestination(/*(MLink)*/ edge);
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
	if (org.argouml.model.ModelFacade.isAModelElement(node)) {
	    ModelFacade.addOwnedElement(_Sequence, /*(MModelElement)*/ node);
	    // ((MClassifier)node).setNamespace(_Sequence.getNamespace());
	}

	fireNodeAdded(node);
    }

    /** Add the given edge to the graph, if valid. */
    public void addEdge(Object edge) {
	if (!canAddEdge(edge)) return;
	_edges.addElement(edge);
	// TODO: assumes public
	if (ModelFacade.isAModelElement(edge)) {
	    ModelFacade.addOwnedElement(_Sequence, /*(MModelElement)*/ edge);
	}
	fireEdgeAdded(edge);

    }

    public void addNodeRelatedEdges(Object node) {
	if (ModelFacade.isAInstance(node) ) {
	    Collection ends = ModelFacade.getLinkEnds(node);
	    Iterator iter = ends.iterator();
	    while (iter.hasNext()) {
		Object/*MLinkEnd*/ le = iter.next();
		if (canAddEdge(ModelFacade.getLink(le)))
		    addEdge(ModelFacade.getLink(le));
		return;
	    }
	}
    }


    /** Return true if the two given ports can be connected by a
     * kind of edge to be determined by the ports. */
    public boolean canConnect(Object fromP, Object toP) {
	if ((ModelFacade.isAObject(fromP)) && (ModelFacade.isAObject(toP))) return true;
	return false;
    }

  
    /** Contruct and add a new edge of the given kind */
    public Object connect(Object fromPort, Object toPort,
			  Class edgeClass) {
        if (edgeClass == ModelFacade.LINK &&
	        (ModelFacade.isAObject(fromPort) && ModelFacade.isAObject(toPort))) {
            Object ml = UmlFactory.getFactory().getCommonBehavior().createLink();
            Object le0 =
		UmlFactory.getFactory().getCommonBehavior().createLinkEnd();
            ModelFacade.setInstance(le0, /*(MObject)*/ fromPort);
            Object le1 =
		UmlFactory.getFactory().getCommonBehavior().createLinkEnd();
            ModelFacade.setInstance(le1, /*(MObject)*/ toPort);
            ModelFacade.addConnection(ml, le0);
            ModelFacade.addConnection(ml, le1);
            addEdge(ml);
            // add stimulus with given action, taken from global mode
            Editor curEditor = Globals.curEditor();
            if (ModelFacade.getStimuli(ml) == null || ModelFacade.getStimuli(ml).size() == 0) {
                ModeManager modeManager = curEditor.getModeManager();
                Mode mode = (Mode) modeManager.top();
                Hashtable args = mode.getArgs();
                if ( args != null ) {
                    Object action = null;
                    // get "action"-Class taken from global mode
                    Class actionClass = (Class) args.get("action");
                    if (actionClass != null) {
                        //create the action
                        if (actionClass == ModelFacade.CALL_ACTION)
                            action =
				UmlFactory.getFactory().getCommonBehavior()
				.createCallAction();
                        else if (actionClass == ModelFacade.CREATE_ACTION)
                            action =
				UmlFactory.getFactory().getCommonBehavior()
				.createCreateAction();
                        else if (actionClass == ModelFacade.DESTROY_ACTION)
                            action =
				UmlFactory.getFactory().getCommonBehavior()
				.createDestroyAction();
                        else if (actionClass == ModelFacade.SEND_ACTION)
                            action =
				UmlFactory.getFactory().getCommonBehavior()
				.createSendAction();
                        else if (actionClass == ModelFacade.RETURN_ACTION)
                            action =
				UmlFactory.getFactory().getCommonBehavior()
				.createReturnAction();

                        if (action != null)  {
                            // determine action type of arguments in mode
                            ModelFacade.setName(action, "new action");

                            if (ModelFacade.isASendAction(action)
                                    || ModelFacade.isAReturnAction(action)) {
                                ModelFacade.setAsynchronous(action, true);
                            } else {
                                ModelFacade.setAsynchronous(action, false);
                            }
                            // create stimulus
                            Object stimulus =
				UmlFactory.getFactory().getCommonBehavior()
				.createStimulus();
                            //if we want to allow the sequence number to appear
                            /*UMLSequenceDiagram
			      sd=(UMLSequenceDiagram)
			      ProjectBrowser.getInstance().getActiveDiagram();
			      int num=sd.getNumStimuluss()+1;
			      stimulus.setName(""+num);*/
                            //otherwise: no sequence number
                            ModelFacade.setName(stimulus, "");

                            //set sender and receiver
                            ModelFacade.setSender(stimulus, /*(MObject)*/ fromPort);
                            ModelFacade.setReceiver(stimulus,/*(MObject)*/ toPort);
                            // set action type
                            ModelFacade.setDispatchAction(stimulus, action);
                            // add stimulus to link
                            ModelFacade.addStimulus(ml, stimulus);
                            // add new modelelements: stimulus and
                            // action to namesapce
                            ModelFacade.addOwnedElement(_Sequence, stimulus);
                            ModelFacade.addOwnedElement(_Sequence, action);
                        }
                    }
                }
            }
            return ml;
        } else {
            cat.debug("Incorrect edge");
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
		if (ModelFacade.isAObject(me)) removeNode(me);
		if (ModelFacade.isAStimulus(me)) removeNode(me);
		if (ModelFacade.isAAssociation(me)) removeEdge(me);
	    }
	    else {
		cat.debug("model added " + me);
	    }
	}
    }

} /* end class SequenceDiagramGraphModel */