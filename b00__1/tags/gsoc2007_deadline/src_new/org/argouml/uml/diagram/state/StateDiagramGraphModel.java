// $Id:StateDiagramGraphModel.java 12908 2007-06-24 18:22:05Z mvw $
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram.state;

import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.tigris.gef.presentation.Fig;

/**
 * This class defines a bridge between the UML meta-model representation of the
 * design and the GraphModel interface used by GEF. This class handles UML
 * Statemachine Diagrams, and is also used for Activity diagrams.
 */
public class StateDiagramGraphModel extends UMLMutableGraphSupport implements
        VetoableChangeListener {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(StateDiagramGraphModel.class);


    /**
     * The statemachine we are diagramming.
     */
    private Object machine;

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @return the statemachine of this diagram
     */
    public Object getMachine() {
        return machine;
    }

    /**
     * @param sm   the statemachine of this diagram
     */
    public void setMachine(Object sm) {

        if (!Model.getFacade().isAStateMachine(sm)) {
            throw new IllegalArgumentException();
        }

        if (sm != null) {
            machine = sm;
        }
    }

    ////////////////////////////////////////////////////////////////
    // GraphModel implementation

    
    /*
     * @see org.tigris.gef.graph.GraphModel#getPorts(java.lang.Object)
     */
    public List getPorts(Object nodeOrEdge) {
        List res = new ArrayList();
        if (Model.getFacade().isAState(nodeOrEdge)) {
	    res.add(nodeOrEdge);
	}
        if (Model.getFacade().isAPseudostate(nodeOrEdge)) {
	    res.add(nodeOrEdge);
	}
        return res;
    }

    /*
     * @see org.tigris.gef.graph.BaseGraphModel#getOwner(java.lang.Object)
     */
    public Object getOwner(Object port) {
        return port;
    }

    /*
     * @see org.tigris.gef.graph.GraphModel#getInEdges(java.lang.Object)
     */
    public List getInEdges(Object port) {
        if (Model.getFacade().isAStateVertex(port)) {
	    return new ArrayList(Model.getFacade().getIncomings(port));
	}
        LOG.debug("TODO: getInEdges of MState");
        return Collections.EMPTY_LIST;
    }

    /*
     * @see org.tigris.gef.graph.GraphModel#getOutEdges(java.lang.Object)
     */
    public List getOutEdges(Object port) {
        if (Model.getFacade().isAStateVertex(port)) {
	    return new ArrayList(Model.getFacade().getOutgoings(port));
	}
        LOG.debug("TODO: getOutEdges of MState");
        return Collections.EMPTY_LIST;
    }

    ////////////////////////////////////////////////////////////////
    // MutableGraphModel implementation

    /*
     * @see org.tigris.gef.graph.MutableGraphModel#canAddNode(java.lang.Object)
     */
    public boolean canAddNode(Object node) {
        if (node == null 
                || !Model.getFacade().isAModelElement(node)
                || containsNode(node)) {
            return false;
        }

        if (Model.getFacade().isAComment(node)) {
            return true;
        }
        
        if (Model.getFacade().isAStateVertex(node)
                || Model.getFacade().isAPartition(node)) {
            /*
             * The next solves issue 3665: Do not allow addition of an element
             * to a statemachine that is contained by a statemachine other than
             * the one represented by this diagram.
             */
            Object nodeMachine =
                Model.getStateMachinesHelper().getStateMachine(node);
            if (nodeMachine == null || nodeMachine == getMachine()) {
                return true;
            }
        }

        return false;
    }

    /*
     * @see org.tigris.gef.graph.MutableGraphModel#canAddEdge(java.lang.Object)
     */
    public boolean canAddEdge(Object edge) {
        if (super.canAddEdge(edge)) {
            return true;
        }
        if (edge == null) {
            return false;
        }
        if (containsEdge(edge)) {
            return false;
        }
        
        Object end0 = null;
        Object end1 = null;

        if (Model.getFacade().isATransition(edge)) {
            end0 = Model.getFacade().getSource(edge);
            end1 = Model.getFacade().getTarget(edge);
            // it's not allowed to directly draw a transition
            // from a composite state to one of it's substates.
            if (Model.getFacade().isACompositeState(end0)
                    && Model.getStateMachinesHelper().getAllSubStates(end0)
                                                        .contains(end1)) {
                return false;
            }
        } else if (edge instanceof CommentEdge) {
            end0 = ((CommentEdge) edge).getSource();
            end1 = ((CommentEdge) edge).getDestination();
        } else {
            return false;
        }

        // Both ends must be defined and nodes that are on the graph already.
        if (end0 == null || end1 == null) {
            LOG.error("Edge rejected. Its ends are not attached to anything");
            return false;
        }

        if (!containsNode(end0)
                && !containsEdge(end0)) {
            LOG.error("Edge rejected. Its source end is attached to "
                    + end0
                    + " but this is not in the graph model");
            return false;
        }
        if (!containsNode(end1)
                && !containsEdge(end1)) {
            LOG.error("Edge rejected. Its destination end is attached to "
                    + end1
                    + " but this is not in the graph model");
            return false;
        }

        return true;
    }

    /*
     * @see org.tigris.gef.graph.MutableGraphModel#addNode(java.lang.Object)
     */
    public void addNode(Object node) {
        LOG.debug("adding statechart/activity diagram node: " + node);
        if (!canAddNode(node)) {
            return;
        }
        if (containsNode(node)) {
            return;
        }

        getNodes().add(node);

        if (Model.getFacade().isAStateVertex(node)) {
            Object top = Model.getStateMachinesHelper().getTop(getMachine());
            Model.getStateMachinesHelper().addSubvertex(top, node);
        }

        fireNodeAdded(node);
    }

    /*
     * @see org.tigris.gef.graph.MutableGraphModel#addEdge(java.lang.Object)
     */
    public void addEdge(Object edge) {
        LOG.debug("adding statechart/activity diagram edge!!!!!!");

        if (!canAddEdge(edge)) {
            return;
        }
        getEdges().add(edge);
        fireEdgeAdded(edge);
    }

    /*
     * @see org.tigris.gef.graph.MutableGraphModel#addNodeRelatedEdges(java.lang.Object)
     */
    public void addNodeRelatedEdges(Object node) {
        super.addNodeRelatedEdges(node);

        if (Model.getFacade().isAStateVertex(node)) {
            Collection transen = 
                new ArrayList(Model.getFacade().getOutgoings(node));
            transen.addAll(Model.getFacade().getIncomings(node));
            Iterator iter = transen.iterator();
            while (iter.hasNext()) {
                Object dep = /* (MTransition) */iter.next();
                if (canAddEdge(dep)) {
                    addEdge(dep);
                }
            }
        }
    }

    /*
     * @see org.tigris.gef.graph.MutableGraphModel#canConnect(java.lang.Object,
     *      java.lang.Object)
     */
    public boolean canConnect(Object fromPort, Object toPort) {
        if (!(Model.getFacade().isAStateVertex(fromPort))) {
            LOG.error("internal error not from sv");
            return false;
        }
        if (!(Model.getFacade().isAStateVertex(toPort))) {
            LOG.error("internal error not to sv");
            return false;
        }

        if (Model.getFacade().isAFinalState(fromPort)) {
            return false;
        }
        if (Model.getFacade().isAPseudostate(toPort)) {
            if ((Model.getPseudostateKind().getInitial()).equals(
                    Model.getFacade().getKind(toPort))) {
                return false;
            }
        }
        return true;
    }

    /*
     * @see org.tigris.gef.graph.MutableGraphModel#connect(java.lang.Object,
     *      java.lang.Object, java.lang.Class)
     */
    public Object connect(Object fromPort, Object toPort,
			  Object edgeClass) {

        if (Model.getFacade().isAFinalState(fromPort)) {
	    return null;
	}

        if (Model.getFacade().isAPseudostate(toPort)
                && Model.getPseudostateKind().getInitial().equals(
			Model.getFacade().getKind(toPort))) {
            return null;
	}

        if (Model.getMetaTypes().getTransition().equals(edgeClass)) {
            Object tr = null;
            tr =
                Model.getStateMachinesFactory()
                    .buildTransition(fromPort, toPort);
            if (canAddEdge(tr)) {
                addEdge(tr);
            } else {
                ProjectManager.getManager().getCurrentProject().moveToTrash(tr);
                tr = null;
            }
            return tr;
        } else if (edgeClass == CommentEdge.class) {
            try {
                Object connection =
                    buildConnection(
                            edgeClass, fromPort, null, toPort, null, null,
                            ProjectManager.getManager().getCurrentProject()
                            .getModel());
                addEdge(connection);
                return connection;
            } catch (Exception ex) {
                LOG.error("buildConnection() failed", ex);
            }
            return null;
        } else {
            LOG.debug("wrong kind of edge in StateDiagram connect3 "
                    + edgeClass);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////
    // VetoableChangeListener implementation

    /*
     * @see java.beans.VetoableChangeListener#vetoableChange(java.beans.PropertyChangeEvent)
     */
    public void vetoableChange(PropertyChangeEvent pce) {
        //throws PropertyVetoException

        if ("ownedElement".equals(pce.getPropertyName())) {
            Collection oldOwned = (Collection) pce.getOldValue();
            Object eo = /* (MElementImport) */pce.getNewValue();
            Object me = Model.getFacade().getModelElement(eo);
            if (oldOwned.contains(eo)) {
                LOG.debug("model removed " + me);
                if (Model.getFacade().isAState(me)) {
                    removeNode(me);
                }
                if (Model.getFacade().isAPseudostate(me)) {
                    removeNode(me);
                }
                if (Model.getFacade().isATransition(me)) {
                    removeEdge(me);
                }
            } else {
                LOG.debug("model added " + me);
            }
        }
    }

    /**
     * The UID.
     */
    static final long serialVersionUID = -8056507319026044174L;

    /**
     * @param newNode
     *            this is the new node that one of the ends is dragged to.
     * @param oldNode
     *            this is the existing node that is already connected.
     * @param edge
     *            this is the edge that is being dragged/rerouted
     * @return true if a transition is being rerouted between two states.
     */
    public boolean canChangeConnectedNode(Object newNode, Object oldNode,
            Object edge) {
        // prevent no changes...
        if (newNode == oldNode) {
            return false;
        }

        // check parameter types:
        if (!(Model.getFacade().isAState(newNode)
	      || Model.getFacade().isAState(oldNode)
	      || Model.getFacade().isATransition(edge))) {
	    return false;
	}

        // it's not allowed to move a transition
        // so that it will go from a composite to its substate
        // nor vice versa. See issue 2865.
        Object otherSideNode = Model.getFacade().getSource(edge);
        if (otherSideNode == oldNode) {
            otherSideNode = Model.getFacade().getTarget(edge);
        }
        if (Model.getFacade().isACompositeState(newNode)
                && Model.getStateMachinesHelper().getAllSubStates(newNode)
                                                    .contains(otherSideNode)) {
            return false;
        }

        return true;
    }

    /**
     * Reroutes the connection to the old node to be connected to the new node.
     *
     * @param newNode
     *            this is the new node that one of the ends is dragged to.
     * @param oldNode
     *            this is the existing node that is already connected.
     * @param edge
     *            this is the edge that is being dragged/rerouted
     * @param isSource
     *            tells us which end is being rerouted.
     */
    public void changeConnectedNode(Object newNode, Object oldNode,
            Object edge, boolean isSource) {

        if (isSource) {
            Model.getStateMachinesHelper().setSource(edge, newNode);
        } else {
            Model.getCommonBehaviorHelper().setTarget(edge, newNode);
        }

    }

    /*
     * @see org.argouml.uml.diagram.UMLMutableGraphSupport#isRemoveFromDiagramAllowed(Collection)
     */
    public boolean isRemoveFromDiagramAllowed(Collection figs) {
        /* If nothing is selected, then not allowed to remove it. */
        if (figs.isEmpty()) {
            return false;
        }
        Iterator i = figs.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            if (!(obj instanceof Fig)) {
                return false;
            }
            Object uml = ((Fig) obj).getOwner();
            /* If a UML object is found, you can not remove selected elms. */
            if (uml != null) {
                return false;
            }
        }
        /* If only Figs without owner are selected, then you can remove them! */
        return true;
    }


} /* end class StateDiagramGraphModel */
