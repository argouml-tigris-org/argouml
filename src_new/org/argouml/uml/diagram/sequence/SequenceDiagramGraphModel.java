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

import org.apache.log4j.Category;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.behavioralelements.commonbehavior.CommonBehaviorHelper;

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

public class SequenceDiagramGraphModel extends UMLMutableGraphSupport
implements VetoableChangeListener {
    protected static Category cat = 
        Category.getInstance(SequenceDiagramGraphModel.class);
  ////////////////////////////////////////////////////////////////
  // instance variables
  
  /** The "home" UML model of this diagram, not all ModelElements in this
   *  graph are in the home model, but if they are added and don't
   *  already have a model, they are placed in the "home model".
   *  Also, elements from other models will have their FigNodes add a
   *  line to say what their model is. */

  /** The Sequence / interaction we are diagramming */
  protected MNamespace _Sequence;
  //protected MInteraction _interaction;

  ////////////////////////////////////////////////////////////////
  // accessors

  public MNamespace getNamespace() { return _Sequence; }
  public void setNamespace(MNamespace m) {
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
    if (nodeOrEdge instanceof MObject) res.addElement(nodeOrEdge);
    return res;
  }

  /** Return the node or edge that owns the given port */
  public Object getOwner(Object port) {
    return port;
  }

  /** Return all edges going to given port */
  public Vector getInEdges(Object port) {
    Vector res = new Vector(); //wasteful!
    if (port instanceof MObject) {
      MObject mo = (MObject) port;
      Collection ends = mo.getLinkEnds();
      if (ends == null) return res; // empty Vector
	  Iterator iter = ends.iterator();
      while (iter.hasNext()) {
	    MLinkEnd aer = (MLinkEnd) iter.next();
	    res.addElement(aer.getLink());
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
    if (edge instanceof MLink) {
      return CommonBehaviorHelper.getHelper().getSource((MLink)edge);
    }
    cat.debug("TODO getSourcePort");
    return null;
  }

  /** Return  the other end of an edge */
  public Object getDestPort(Object edge) {
    if (edge instanceof MLink) {
      return CommonBehaviorHelper.getHelper().getDestination((MLink)edge);
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
    return (node instanceof MObject || node instanceof MStimulus);
  }

  /** Return true if the given object is a valid edge in this graph */
  public boolean canAddEdge(Object edge)  {
    if (edge == null) return false;
    Object end0 = null;
    Object end1 = null;
    if (edge instanceof MLink) {
        end0 = CommonBehaviorHelper.getHelper().getSource((MLink)edge);
        end1 = CommonBehaviorHelper.getHelper().getDestination((MLink)edge);
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

  /** Add the given edge to the graph, if valid. */
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
    if ( node instanceof MInstance ) {
      Collection ends = ((MInstance)node).getLinkEnds();
      Iterator iter = ends.iterator();
      while (iter.hasNext()) {
         MLinkEnd ae = (MLinkEnd) iter.next();
         if(canAddEdge(ae.getLink()))
           addEdge(ae.getLink());
           return;
      }
    }
  }


  /** Return true if the two given ports can be connected by a
   * kind of edge to be determined by the ports. */
  public boolean canConnect(Object fromP, Object toP) {
    if ((fromP instanceof MObject) && (toP instanceof MObject)) return true;
    return false;
  }

  
    /** Contruct and add a new edge of the given kind */
    public Object connect(Object fromPort, Object toPort, java.lang.Class edgeClass) {
        if (edgeClass == MLink.class && (fromPort instanceof MObject && toPort instanceof MObject)) {
            MLink ml = UmlFactory.getFactory().getCommonBehavior().createLink();
            MLinkEnd le0 = UmlFactory.getFactory().getCommonBehavior().createLinkEnd();
            le0.setInstance((MObject) fromPort);
            MLinkEnd le1 = UmlFactory.getFactory().getCommonBehavior().createLinkEnd();
            le1.setInstance((MObject) toPort);
            ml.addConnection(le0);
            ml.addConnection(le1);
            addEdge(ml);
            // add stimulus with given action, taken from global mode
            Editor curEditor = Globals.curEditor();
            if (ml.getStimuli() == null || ml.getStimuli().size() == 0) {
                ModeManager modeManager = curEditor.getModeManager();
                Mode mode = (Mode)modeManager.top();
                Hashtable args = mode.getArgs();
                if ( args != null ) {
                    Object action=null;
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

                        if (action != null)  {
                            // determine action type of arguments in mode
                            ModelFacade.setName(action, "new action");

                            if (ModelFacade.isASendAction(action) || ModelFacade.isAReturnAction(action)) {
                                ModelFacade.setAsynchronous(action, true);
                            } else {
                                ModelFacade.setAsynchronous(action, false);
                            }
                            // create stimulus
                            MStimulus stimulus = UmlFactory.getFactory().getCommonBehavior().createStimulus();
                            //if we want to allow the sequence number to appear
                            /*UMLSequenceDiagram sd=(UMLSequenceDiagram) ProjectBrowser.TheInstance.getActiveDiagram();
                            int num=sd.getNumStimuluss()+1;
                            stimulus.setName(""+num);*/
                            //otherwise: no sequence number
                            stimulus.setName("");

                            //set sender and receiver
                            stimulus.setSender((MObject)fromPort);
                            stimulus.setReceiver((MObject)toPort);
                            // set action type
                            ModelFacade.setDispatchAction(stimulus, action);
                            // add stimulus to link
                            ml.addStimulus(stimulus);
                            // add new modelelements: stimulus and action to namesapce
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
      MElementImport eo = (MElementImport) pce.getNewValue();
      MModelElement me = eo.getModelElement();
      if (oldOwned.contains(eo)) {
	    cat.debug("model removed " + me);
	    if (me instanceof MObject) removeNode(me);
	    if (me instanceof MStimulus) removeNode(me);
	    if (me instanceof MAssociation) removeEdge(me);
      }
      else {
	    cat.debug("model added " + me);
      }
    }
  }

} /* end class SequenceDiagramGraphModel */

