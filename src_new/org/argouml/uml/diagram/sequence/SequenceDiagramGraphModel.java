// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

package org.argouml.uml.diagram.sequence;

import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.behavioralelements.collaborations.CollaborationsFactory;
import org.argouml.model.uml.behavioralelements.collaborations.CollaborationsHelper;
import org.argouml.model.uml.behavioralelements.commonbehavior.CommonBehaviorFactory;
import org.argouml.model.uml.behavioralelements.commonbehavior.CommonBehaviorHelper;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.sequence.ui.FigLink;
import org.argouml.uml.diagram.sequence.ui.FigLinkPort;
import org.argouml.uml.diagram.sequence.ui.FigObject;
import org.argouml.uml.diagram.sequence.ui.FigReturnActionLink;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Mode;
import org.tigris.gef.base.ModeManager;
import org.tigris.gef.presentation.Fig;

/** This class defines a bridge between the UML meta-model
 *  representation of the design and the GraphModel interface used by
 *  GEF.  This class handles only UML Sequence Digrams.
 *
 *  @author 5eichler@informatik.uni-hamburg.de
 */
public class SequenceDiagramGraphModel
    extends UMLMutableGraphSupport
    implements VetoableChangeListener {

    private static final Logger LOG = 
        Logger.getLogger(SequenceDiagramGraphModel.class);
    
    private abstract class CanConnectCmd {
        private Object _srcPort;
        private Object _destPort;
        private Object _srcObject;
        private Object _destObject;
        private FigObject _srcFigObject;
        private FigObject _destFigObject;
        private Fig _srcFigPort;
        private Fig _destFigPort;       

        public CanConnectCmd(Object fromPort, Object toPort) {
            Editor curEditor = Globals.curEditor();
            _srcPort = fromPort;
            _destPort = toPort;
            if (_srcPort instanceof LinkPort) {
                _srcObject = ((LinkPort) _srcPort).getObject();
            } 
            if (_destPort instanceof LinkPort) {
                _destObject = ((LinkPort) _destPort).getObject();
            } 

            _srcFigObject =
                (FigObject) curEditor.getLayerManager().getActiveLayer()
		    .presentationFor(_srcObject);
            _destFigObject =
                (FigObject) curEditor.getLayerManager().getActiveLayer()
                    .presentationFor(_destObject);
            _srcFigPort = _srcFigObject.getPortFig(_srcPort);
            _destFigPort = _destFigObject.getPortFig(_destPort);            
        }
       
        /**
         * @return
         */
        public FigObject getDestFigObject() {
            return _destFigObject;
        }

        /**
         * @return
         */
        public Fig getDestFigPort() {
            return _destFigPort;
        }

        /**
         * @return
         */
        public Object getDestObject() {
            return _destObject;
        }

        /**
         * @return
         */
        public Object getDestPort() {
            return _destPort;
        }

       

        /**
         * @return
         */
        public FigObject getSrcFigObject() {
            return _srcFigObject;
        }

        /**
         * @return
         */
        public Fig getSrcFigPort() {
            return _srcFigPort;
        }

        /**
         * @return
         */
        public Object getSrcObject() {
            return _srcObject;
        }

        /**
         * @return
         */
        public Object getSrcPort() {
            return _srcPort;
        }

       

        /**
         * @param object
         */
        public void setDestFigObject(FigObject object) {
            _destFigObject = object;
        }

        /**
         * @param fig
         */
        public void setDestFigPort(Fig fig) {
            _destFigPort = fig;
        }

        /**
         * @param object
         */
        public void setDestObject(Object object) {
            _destObject = object;
        }

        /**
         * @param object
         */
        public void setDestPort(Object object) {
            _destPort = object;
        }
       
        /**
         * @param object
         */
        public void setSrcFigObject(FigObject object) {
            _srcFigObject = object;
        }

        /**
         * @param fig
         */
        public void setSrcFigPort(Fig fig) {
            _srcFigPort = fig;
        }

        /**
         * @param object
         */
        public void setSrcObject(Object object) {
            _srcObject = object;
        }

        /**
         * @param object
         */
        public void setSrcPort(Object object) {
            _srcPort = object;
        }

        /**
         * Checks if a certain combination of ports and edgeclass can
         * be connected.
         */
        public abstract boolean doit();

    }

    private class CanConnectCallActionCmd extends CanConnectCmd {

        public CanConnectCallActionCmd(Object fromPort, Object toPort) {
            super(fromPort, toPort);
        }

        /**
         * @see SequenceDiagramGraphModel.CanConnectCmd#doit()
         */
        public boolean doit() {
            if (!(this.getDestPort() instanceof LinkPort)
                || !(getSrcPort() instanceof LinkPort)) {
                return false;
            }
            if (this.getDestPort() instanceof ActivationNode
		&& !(getDestFigObject().getActivationNodes(
			 (Node) this.getDestPort()).isEmpty())) {
                // cannot connect a callaction to a destactivation
                // that already exists
                return false;
            }
           //  if (getSrcFigObject().getPreviousActivation((Node)getSrcPort()).)
            if (getSrcPort() instanceof LinkNode
		&& ((LinkNode) getSrcPort()).isDestroyed()) {
                // cannot connect a callaction to a sourceActivation
                // that is allready destroyed.
                return false;
            }
            if (getSrcPort() instanceof LinkNode) {
            	int index =
		    getSrcFigObject().getIndexOf((LinkNode) getSrcPort());
            	Iterator it =
		    getSrcFigObject().getActivationNodes(
		        (Node) getSrcPort()).iterator();
            	while (it.hasNext()) {
		    Node node = (Node) it.next();
		    if (node instanceof LinkPort
			&& (getSrcFigObject().getFigLink(
				((LinkPort) node).getFigLinkPort())
			    instanceof FigReturnActionLink)
			&& index > getSrcFigObject().getIndexOf(node)) {

			// cannot connect a callaction to a
			// sourceActivation that is allready returned.
			return false;

		    }
            	}
            }                    
            return true;
        }

    }

    private class CanConnectDestroyActionCmd extends CanConnectCmd {

        public CanConnectDestroyActionCmd(Object fromPort, Object toPort) {
            super(fromPort, toPort);
        }

        /**
         * @see SequenceDiagramGraphModel.CanConnectCmd#doit()
         */
        public boolean doit() {
            if (!(this.getDestPort() instanceof LinkPort)
                || !(getSrcPort() instanceof LinkPort)) {
                return false;
            }
            if (getDestFigObject().getActivationNodes(
		    (Node) this.getDestPort()).isEmpty()) {
                // there must be a destination activation.
                return false;
            }
            if (this.getDestPort() instanceof ActivationNode
		&& !((ActivationNode) this.getDestPort()).isEnd()) {
                // cannot destroy an object in the middle of an activation.
                return false;
            }          
            return true;

        }
    }

    private class CanConnectReturnActionCmd extends CanConnectCmd {

        public CanConnectReturnActionCmd(Object fromPort, Object toPort) {
            super(fromPort, toPort);
        }

        /**
         * @see SequenceDiagramGraphModel.CanConnectCmd#doit()
         */
        public boolean doit() {
            if (!(this.getDestPort() instanceof LinkPort)
                || !(getSrcPort() instanceof LinkPort)) {
                return false;
            }
	    if (getDestFigObject().getActivationNodes(
		    (Node) this.getDestPort()).isEmpty()) {
		// there must be a destination activation.
		return false;
	    }
	    if (this.getSrcPort() instanceof ActivationNode
		&& !((ActivationNode) this.getSrcPort()).isEnd()) {

		// cannot return in the middle of an activation.
		return false;
	    } 
	    Node startNode =
		(Node) getSrcFigObject().getActivationNodes(
		    (Node) getSrcPort()).get(0);
	    if (startNode instanceof LinkPort) {
		LinkPort linkNode = (LinkPort) startNode;
		FigLink figLink =
		    getSrcFigObject().getFigLink(linkNode.getFigLinkPort());
		   	 
		if (!(getDestFigObject().getActivationNodes(
			  (Node) ((FigLinkPort) figLink.getSourcePortFig())
			             .getOwner())
		      .contains(this.getDestPort()))) {
		    // cannot let a activation return to an activation
		    // that did not start the source activation
		    return false;
		}
	    } else {
		return false;
	    }		
            return true;

        }
    }

    private class CanConnectCreateActionCmd extends CanConnectCmd {

        public CanConnectCreateActionCmd(Object fromPort, Object toPort) {
            super(fromPort, toPort);
        }

        /**
        * @see SequenceDiagramGraphModel.CanConnectCmd#doit()
        */
        public boolean doit() {
            if (!(ModelFacade.isAObject(this.getDestPort()))
                || !(getSrcPort() instanceof LinkPort)) {
                return false;
            }
            return true;
        }
    }

    ////////////////////////////////////////////////////////////////
    // instance variables

    /**
     * The collaboration this sequence diagram belongs too.
     */
    private Object _collaboration;

    /**
     * The interaction that is shown on the sequence diagram
     */
    private Object _interaction;

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
     * Default constructor. Constructs a model and a collaboration in
     * the root of the current project.
     */
    public SequenceDiagramGraphModel(Object collaboration) {
        _collaboration = collaboration;
        _interaction =
            CollaborationsFactory.getFactory().buildInteraction(collaboration);
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
            end0 =
		CommonBehaviorHelper.getHelper().getSource(/*(MLink)*/
							   edge);
            end1 =
		CommonBehaviorHelper.getHelper().getDestination(/*(MLink)*/
								edge);
        }
        if (end0 == null || end1 == null)
            return false;
        if (!getNodes().contains(end0))
            return false;
        if (!getNodes().contains(end1))
            return false;
        if (getEdges().contains(edge))
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

    /**
     * Adds an edge to the model if this is allowed. If the edge is a link, 
     * an associationrole and a stimulus to accompany this link are build. 
     *
     * @see org.tigris.gef.graph.MutableGraphModel#addEdge(java.lang.Object)
     */
    public void addEdge(Object edge) {
        // if (canAddEdge(edge)) {            
        fireEdgeAdded(edge);
        // }
    }

    public void addNodeRelatedEdges(Object node) {
        if (ModelFacade.isAInstance(node)) {
            Collection ends = ModelFacade.getLinkEnds(node);
            Iterator iter = ends.iterator();
            while (iter.hasNext()) {
                Object /*MLinkEnd*/ le = iter.next();
                if (canAddEdge(ModelFacade.getLink(le)))
                    addEdge(ModelFacade.getLink(le));
                return;
            }
        }
    }

    /** Return true if the two given ports can be connected by a
     * kind of edge to be determined by the ports. 
     */
    public boolean canConnect(Object fromP, Object toP) {
        Editor curEditor = Globals.curEditor();
        ModeManager modeManager = curEditor.getModeManager();
        Mode mode = (Mode) modeManager.top();
        Hashtable args = mode.getArgs();
        Class actionClass = (Class) args.get("action");
        CanConnectCmd cmd = null;
        if (actionClass == ModelFacade.CALL_ACTION) {
            cmd = new CanConnectCallActionCmd(fromP, toP);
        } else if (actionClass == ModelFacade.RETURN_ACTION) {
            cmd = new CanConnectReturnActionCmd(fromP, toP);
        } else if (actionClass == ModelFacade.CREATE_ACTION) {
            cmd = new CanConnectCreateActionCmd(fromP, toP);
        } else if (actionClass == ModelFacade.DESTROY_ACTION) {
            cmd = new CanConnectDestroyActionCmd(fromP, toP);
        } else {
            // not supported action
            return false;
        }
        return cmd.doit();
    }

    /**
     * Creates a link based on the given from and toPort. The fromPort
     * should allways point to a LinkCoordinates instance. The toPort
     * can point to a LinkCoordinates instance or to a Object
     * instance. On a sequence diagram you can only draw Links. So
     * other edgeClasses then links are not supported.
     *
     * @see org.tigris.gef.graph.MutableGraphModel#connect(
     *          Object, Object, Class)
     */
    public Object connect(Object fromPort, Object toPort, Class edgeClass) {
        if (!canConnect(fromPort, toPort)) {
            return null;
        }
        Object edge = null;
        Object fromObject = null;
        Object toObject = null;
        Object action = null;
        if (edgeClass == ModelFacade.LINK) {
            Editor curEditor = Globals.curEditor();
            ModeManager modeManager = curEditor.getModeManager();
            Mode mode = (Mode) modeManager.top();
            Hashtable args = mode.getArgs();
            Class actionClass = (Class) args.get("action");
            if (actionClass == ModelFacade.CALL_ACTION) {
                if (fromPort instanceof LinkPort
                    && toPort instanceof LinkPort) {
                    fromObject = ((LinkPort) fromPort).getObject();
                    toObject = ((LinkPort) toPort).getObject();

                    action =
                        UmlFactory.getFactory().getCommonBehavior()
                            .createCallAction();
                }
            } else if (actionClass == ModelFacade.CREATE_ACTION) {
                if (fromPort instanceof LinkPort
                    && ModelFacade.isAObject(toPort)) {
                    fromObject = ((LinkPort) fromPort).getObject();
                    toObject = toPort;
                    action =
                        UmlFactory.getFactory().getCommonBehavior()
                            .createCreateAction();
                }
            } else if (actionClass == ModelFacade.RETURN_ACTION) {
                if (fromPort instanceof LinkPort
                    && toPort instanceof LinkPort) {
                    fromObject = ((LinkPort) fromPort).getObject();
                    toObject = ((LinkPort) fromPort).getObject();
                    action =
                        UmlFactory.getFactory().getCommonBehavior()
                            .createReturnAction();

                }
            } else if (actionClass == ModelFacade.DESTROY_ACTION) {
                if (fromPort instanceof LinkPort
                    && toPort instanceof LinkPort) {
                    fromObject = ((LinkPort) fromPort).getObject();
                    toObject = ((LinkPort) fromPort).getObject();
                    action =
                        UmlFactory.getFactory().getCommonBehavior()
                            .createDestroyAction();
                }
            } else if (actionClass == ModelFacade.SEND_ACTION) {
                // no implementation, not of importance to sequence diagrams
            } else if (actionClass == ModelFacade.TERMINATE_ACTION) {
                // not implemented yet
            }
        }
        if (fromObject != null && toObject != null && action != null) {
            Object link =
                CommonBehaviorFactory.getFactory().buildLink(
                    fromObject,
                    toObject);
            Object classifierRoleFrom =
                ModelFacade.getClassifiers(fromObject).iterator().next();
            Object classifierRoleTo =
                ModelFacade.getClassifiers(toObject).iterator().next();
            Object associationRole =
                CollaborationsHelper.getHelper().getAssocationRole(
                    classifierRoleFrom,
                    classifierRoleTo);
            if (associationRole == null) {
                associationRole =
                    CollaborationsFactory.getFactory().buildAssociationRole(
                        link);
            }
            Object stimulus = null;
            if (ModelFacade.getStimuli(link) == null
                || ModelFacade.getStimuli(link).isEmpty()) {
                stimulus =
                    CommonBehaviorFactory.getFactory().buildStimulus(link);
            } else {
                // we need to find the right stimulus
                Iterator it = ModelFacade.getStimuli(link).iterator();
                while (it.hasNext()) {
                    Object o = it.next();
                    if (ModelFacade.getSender(o) != null
                        && ModelFacade.getSender(o).equals(fromObject)
                        && ModelFacade.getReceiver(o) != null
                        && ModelFacade.getReceiver(o).equals(toObject)) {
                        stimulus = o;
                        break;
                    }
                }
                if (stimulus == null) {
                    stimulus =
                        CommonBehaviorFactory.getFactory().buildStimulus(link);
                }
            }
            ModelFacade.setDispatchAction(stimulus, action);

            Object message =
                CollaborationsFactory.getFactory().buildMessage(
                    getInteraction(),
                    associationRole);
            ModelFacade.setAction(message, action);
            ModelFacade.setSender(message, classifierRoleFrom);
            ModelFacade.setReceiver(message, classifierRoleTo);
            addEdge(link);
            edge = link;
        }
        if (edge == null) {
            LOG.debug("Incorrect edge");
        }
        return edge;

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
                LOG.debug("model removed " + me);
                if (ModelFacade.isAObject(me))
                    removeNode(me);
                if (ModelFacade.isAAssociation(me))
                    removeEdge(me);
            } else {
                LOG.debug("model added " + me);
            }
        }
    }

    /**
     * Gets the collaboration that is shown on the sequence diagram.<p>
     *
     * @return the collaboration of the diagram.
     */
    public Object getCollaboration() {
        return _collaboration;
    }

    /**
     * Sets the collaboration that is shown at the sequence diagram.
     *
     * @param collaboration
     */
    public void setCollaboration(Object collaboration) {
        // TODO: when the collaboration is set, the whole sequence diagram
        // should be reset (all figs removed) and figs that are a view onto
        // the modelelements in the new collaboration must be shown
        _collaboration = collaboration;
    }

    private Object getInteraction() {
        if (_interaction == null) {
            _interaction =
                CollaborationsFactory.getFactory().buildInteraction(
                    _collaboration);
        }
        return _interaction;
    }
    
    

    /**
     * @see org.argouml.uml.diagram.UMLMutableGraphSupport#getNamespace()
     */
    public Object getNamespace() {
        return _collaboration;
    }
}
