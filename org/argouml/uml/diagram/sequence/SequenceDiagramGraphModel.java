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

import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.behavioralelements.collaborations.CollaborationsFactory;
import org.argouml.model.uml.behavioralelements.commonbehavior.CommonBehaviorFactory;
import org.argouml.model.uml.behavioralelements.commonbehavior.CommonBehaviorHelper;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Mode;
import org.tigris.gef.base.ModeManager;

/** This class defines a bridge between the UML meta-model
 *  representation of the design and the GraphModel interface used by
 *  GEF.  This class handles only UML Sequence Digrams.  */

public class SequenceDiagramGraphModel
	extends UMLMutableGraphSupport
	implements VetoableChangeListener {
	protected static Logger cat =
		Logger.getLogger(SequenceDiagramGraphModel.class);
	////////////////////////////////////////////////////////////////
	// instance variables

	/**
	 * The collaboration this sequence diagram belongs too.
	 */
	private Object _collaboration;

	////////////////////////////////////////////////////////////////
	// GraphModel implementation

	/** Return all nodes in the graph */
	public Vector getNodes() {
		Vector allNodes = new Vector();
		Collection elements = ModelFacade.getOwnedElements(_collaboration);
		Iterator it = elements.iterator();
		Collection classifierRoles = new ArrayList();
		while (it.hasNext()) {
			Object o = it.next();
			if (ModelFacade.isAClassifierRole(o)) {
				classifierRoles.add(o);
			}
		}
        it = classifierRoles.iterator();
		while (it.hasNext()) {
			Iterator it2 = ModelFacade.getInstances(it.next()).iterator();
			while (it2.hasNext()) {
				Object instance = it2.next();
				if (ModelFacade.isAObject(instance)) {
					allNodes.add(instance);
				}
			}
		}
		return allNodes;
	}

	/**
	 * Default constructor. Constructs a model and a collaboration in the root of the current
	 * project.
	 *
	 */
	public SequenceDiagramGraphModel() {
		_collaboration =
			CollaborationsFactory.getFactory().buildCollaboration(
				ProjectManager.getManager().getCurrentProject().getRoot());
	}

	/** Return all edges in the graph */
	public Vector getEdges() {
		Vector allEdges = new Vector();
		Iterator it = ModelFacade.getOwnedElements(_collaboration).iterator();
		while (it.hasNext()) {
			Object o = it.next();
			if (ModelFacade.isAAssociationRole(o)) {
				Iterator it2 = ModelFacade.getLinks(o).iterator();
				while (it2.hasNext()) {
					Object link = it2.next();
					if (ModelFacade.isALink(link)) {
						allEdges.add(link);
					}
				}
			}
		}
		return allEdges;
	}

	/** Return all ports on node or edge */
	public Vector getPorts(Object nodeOrEdge) {
		Vector ports = new Vector();
		if (ModelFacade.isAObject(nodeOrEdge)) {
			ports.addAll(ModelFacade.getLinkEnds(nodeOrEdge));
		} else if (ModelFacade.isALink(nodeOrEdge)) {
			ports.addAll(ModelFacade.getLinkEnds(nodeOrEdge));
		}
		return ports;
	}

	/** Return the node or edge that owns the given port */
	public Object getOwner(Object port) {
		Object owner = null;
		if (ModelFacade.isALinkEnd(port)) {
			Iterator it = getNodes().iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (ModelFacade.isAInstance(o)) {
					Collection linkEnds = ModelFacade.getLinkEnds(o);
					if (linkEnds.contains(port)) {
						owner = o;
						break;
					}
				}
			}
		}
		return owner;
	}

	/** Return all edges going to given port */
	public Vector getInEdges(Object port) {
		Vector res = new Vector();
		if (ModelFacade.isAObject(port)) {
			Iterator it = ModelFacade.getLinkEnds(port).iterator();
			while (it.hasNext()) {
				Object link = ModelFacade.getLink(it.next());
				Iterator it2 = ModelFacade.getStimuli(link).iterator();
				while (it2.hasNext()) {
					Object stimulus = it2.next();
					Object instance = ModelFacade.getReceiver(stimulus);
					if (instance == port) {
						res.add(link);
					}
				}
			}
		}
		return res;
	}

	/** Return all edges going from given port */
	public Vector getOutEdges(Object port) {
		Vector res = new Vector();
		if (ModelFacade.isAObject(port)) {
			Iterator it = ModelFacade.getLinkEnds(port).iterator();
			while (it.hasNext()) {
				Object link = ModelFacade.getLink(it.next());
				Iterator it2 = ModelFacade.getStimuli(link).iterator();
				while (it2.hasNext()) {
					Object stimulus = it2.next();
					Object instance = ModelFacade.getSender(stimulus);
					if (instance == port) {
						res.add(link);
					}
				}
			}
		}
		return res;
	}

	/** Return one end of an edge */
	public Object getSourcePort(Object edge) {
		Object res = null;
		if (ModelFacade.isALink(edge)) {
			res = CommonBehaviorHelper.getHelper().getSource(edge);
		}
		return res;
	}

	/** Return  the other end of an edge */
	public Object getDestPort(Object edge) {
		Object res = null;
		if (ModelFacade.isALink(edge)) {
			res = CommonBehaviorHelper.getHelper().getDestination(edge);
		}
		return res;
	}

	////////////////////////////////////////////////////////////////
	// MutableGraphModel implementation

	/** Return true if the given object is a valid node in this graph */
	public boolean canAddNode(Object node) {
		if (node == null)
			return false;
		if (getNodes().contains(node))
			return false;
		return ModelFacade.isAObject(node);
	}

	/** Return true if the given object is a valid edge in this graph */
	public boolean canAddEdge(Object edge) {
		if (edge == null)
			return false;
		Object end0 = null;
		Object end1 = null;
		if (org.argouml.model.ModelFacade.isALink(edge)) {
			end0 = CommonBehaviorHelper.getHelper().getSource(/*(MLink)*/
			edge);
			end1 = CommonBehaviorHelper.getHelper().getDestination(
			/*(MLink)*/
			edge);
		}
		if (end0 == null || end1 == null)
			return false;
		if (!getNodes().contains(end0))
			return false;
		if (!getNodes().contains(end1))
			return false;
		return true;

	}

	/** Add the given node to the graph, if valid. */
	public void addNode(Object node) {
		if (canAddNode(node)) {
			Object clasrole =
				CollaborationsFactory.getFactory().buildClassifierRole(
					_collaboration);
			ModelFacade.addInstance(clasrole, node);
			fireNodeAdded(node);
		}

	}

	/** Add the given edge to the graph, if valid. */
	public void addEdge(Object edge) {
		if (canAddEdge(edge)) {
			Object source = CommonBehaviorHelper.getHelper().getSource(edge);
			Object dest = CommonBehaviorHelper.getHelper().getDestination(edge);
			if (!getNodes().contains(source)) {
				addNode(source);
			}
			if (!getNodes().contains(dest)) {
				addNode(dest);
			}
			fireEdgeAdded(edge);
		}
	}

	public void addNodeRelatedEdges(Object node) {
		if (ModelFacade.isAInstance(node)) {
			Collection ends = ModelFacade.getLinkEnds(node);
			Iterator iter = ends.iterator();
			while (iter.hasNext()) {
				Object /*MLinkEnd*/
				le = iter.next();
				if (canAddEdge(ModelFacade.getLink(le)))
					addEdge(ModelFacade.getLink(le));
				return;
			}
		}
	}

	/** Return true if the two given ports can be connected by a
	 * kind of edge to be determined by the ports. */
	public boolean canConnect(Object fromP, Object toP) {
		if ((ModelFacade.isAObject(fromP)) && (ModelFacade.isAObject(toP)))
			return true;
		return false;
	}

	/** Contruct and add a new edge of the given kind */
	public Object connect(Object fromPort, Object toPort, Class edgeClass) {
		if (edgeClass == ModelFacade.LINK
			&& (ModelFacade.isAObject(fromPort)
				&& ModelFacade.isAObject(toPort))) {
			Object link =
				CommonBehaviorFactory.getFactory().buildLink(fromPort, toPort);
			addEdge(link);
			// add stimulus with given action, taken from global mode
			Editor curEditor = Globals.curEditor();
			if (ModelFacade.getStimuli(link) == null
				|| ModelFacade.getStimuli(link).isEmpty()) {
				ModeManager modeManager = curEditor.getModeManager();
				Mode mode = (Mode)modeManager.top();
				Hashtable args = mode.getArgs();
				if (args != null) {
					Object action = null;
					// get "action"-Class taken from global mode
					Class actionClass = (Class)args.get("action");
					if (actionClass != null) {
						//create the action
						if (actionClass == ModelFacade.CALL_ACTION)
							action =
								UmlFactory
									.getFactory()
									.getCommonBehavior()
									.createCallAction();
						else if (actionClass == ModelFacade.CREATE_ACTION)
							action =
								UmlFactory
									.getFactory()
									.getCommonBehavior()
									.createCreateAction();
						else if (actionClass == ModelFacade.DESTROY_ACTION)
							action =
								UmlFactory
									.getFactory()
									.getCommonBehavior()
									.createDestroyAction();
						else if (actionClass == ModelFacade.SEND_ACTION)
							action =
								UmlFactory
									.getFactory()
									.getCommonBehavior()
									.createSendAction();
						else if (actionClass == ModelFacade.RETURN_ACTION)
							action =
								UmlFactory
									.getFactory()
									.getCommonBehavior()
									.createReturnAction();

						if (action != null) {
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
								UmlFactory
									.getFactory()
									.getCommonBehavior()
									.createStimulus();
							ModelFacade.setName(stimulus, "");

							//set sender and receiver
							ModelFacade.setSender(stimulus, /*(MObject)*/
							fromPort);
							ModelFacade.setReceiver(stimulus, /*(MObject)*/
							toPort);
							// set action type
							ModelFacade.setDispatchAction(stimulus, action);
							// add stimulus to link
							ModelFacade.addStimulus(link, stimulus);
							// add new modelelements: stimulus and
							// action to namesapce						
						}
					}
				}
			}
			return link;
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
			Vector oldOwned = (Vector)pce.getOldValue();
			Object eo = /*(MElementImport)*/
			pce.getNewValue();
			Object me = ModelFacade.getModelElement(eo);
			if (oldOwned.contains(eo)) {
				cat.debug("model removed " + me);
				if (ModelFacade.isAObject(me))
					removeNode(me);
				if (ModelFacade.isAStimulus(me))
					removeNode(me);
				if (ModelFacade.isAAssociation(me))
					removeEdge(me);
			} else {
				cat.debug("model added " + me);
			}
		}
	}


	/**
	 * Gets the collaboration that is shown on the sequence diagram
	 * @return 
	 */
	public Object getCollaboration() {
		return _collaboration;
	}


	/**
	 * Sets the collaboration that is shown at the sequence diagram
	 * @param collaboration
	 */
	public void setCollaboration(Object collaboration) {
		// TODO: when the collaboration is set, the whole sequence diagram
		// should be reset (all figs removed) and figs that are a view onto
		// the modelelements in the new collaboration must be shown
		_collaboration = collaboration;
	}
}
