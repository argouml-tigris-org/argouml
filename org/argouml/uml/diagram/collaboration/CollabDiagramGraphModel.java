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


// File: CollabDiagramGraphModel.java
// Classes: CollabDiagramGraphModel
// Original Author: agauthie@ics.uci.edu
// $Id$


package org.argouml.uml.diagram.collaboration;

import org.apache.log4j.Category;

import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.model.uml.foundation.core.CoreHelper;

import java.util.*;
import java.beans.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Mode;
import org.tigris.gef.base.ModeManager;


/** This class defines a bridge between the UML meta-model
 *  representation of the design and the GraphModel interface used by
 *  GEF.  This class handles only UML Use Case Digrams.  */

public class CollabDiagramGraphModel extends UMLMutableGraphSupport
implements VetoableChangeListener {
    protected static Category cat = Category.getInstance(CollabDiagramGraphModel.class);

  /** The "home" UML model of this diagram, not all ModelElements in this
   *  graph are in the home model, but if they are added and don't
   *  already have a model, they are placed in the "home model".
   *  Also, elements from other models will have their FigNodes add a
   *  line to say what their model is. */

  /** The collaboration / interaction we are diagramming */
	protected MCollaboration _collab;
	protected MInteraction _interaction;

  ////////////////////////////////////////////////////////////////
  // accessors

    public MNamespace getNamespace() { return _collab; }
    public void setNamespace(MNamespace m) {
        if (!(m instanceof MCollaboration)) {
            throw new IllegalArgumentException("invalid namespace for CollabDiagramGraphModel");
        }
        _collab = (MCollaboration) m;
    }


  ////////////////////////////////////////////////////////////////
  // GraphModel implementation

 
  /** Return all ports on node or edge */
  public Vector getPorts(Object nodeOrEdge) {
    Vector res = new Vector();  //wasteful!
    if (nodeOrEdge instanceof MClassifierRole) res.addElement(nodeOrEdge);
    return res;
  }

  /** Return the node or edge that owns the given port */
  public Object getOwner(Object port) {
    return port;
  }

  /** Return all edges going to given port */
  public Vector getInEdges(Object port) {
    Vector res = new Vector(); //wasteful!
    if (port instanceof MClassifierRole) {
      MClassifierRole cr = (MClassifierRole) port;
      Collection ends = cr.getAssociationEnds();
      if (ends == null) return res; // empty Vector
	  Iterator iter = ends.iterator();
      while (iter.hasNext()) {
	    MAssociationEndRole aer = (MAssociationEndRole) iter.next();
	    res.addElement(aer.getAssociation());
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
    if (edge instanceof MRelationship) {
        return CoreHelper.getHelper().getSource((MRelationship)edge);
    }
    cat.debug("TODO getSourcePort");
    return null;
  }

  /** Return  the other end of an edge */
  public Object getDestPort(Object edge) {
    if (edge instanceof MRelationship) {
        return CoreHelper.getHelper().getDestination((MRelationship)edge);
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
    return (node instanceof MClassifierRole || node instanceof MMessage);
  }

  /** Return true if the given object is a valid edge in this graph */
  public boolean canAddEdge(Object edge)  {
    if (edge == null) return false;
    if(_edges.contains(edge)) return false;
    Object end0 = null, end1 = null;
    if (edge instanceof MAssociationRole) {
      List conns = ((MAssociationRole)edge).getConnections();
      if (conns.size() < 2) return false;
      MAssociationEndRole ae0 = (MAssociationEndRole) conns.get(0);
      MAssociationEndRole ae1 = (MAssociationEndRole) conns.get(1);
      if (ae0 == null || ae1 == null) return false;
      end0 = ae0.getType();
      end1 = ae1.getType();
    }
    if (edge instanceof MGeneralization) {
        MGeneralization gen = (MGeneralization)edge;
        end0 = gen.getParent();
        end1 = gen.getChild();
    }
    if (end0 == null || end1 == null) return false;
    if (!_nodes.contains(end0)) return false;
    if (!_nodes.contains(end1)) return false;
    return true;
  }


  /** Add the given node to the graph, if valid. */
  public void addNode(Object node) {
    cat.debug("adding MClassifierRole node!!");
    if (!canAddNode(node)) return;
    _nodes.addElement(node);
    // TODO: assumes public, user pref for default visibility?
      if (node instanceof MClassifier) {
		  _collab.addOwnedElement((MClassifier) node);
		  // ((MClassifier)node).setNamespace(_collab.getNamespace());
      }
    
    fireNodeAdded(node);
  }

    /** Add the given edge to the graph, if valid. */
    public void addEdge(Object edge) {
        cat.debug("adding class edge!!!!!!");
        if (!canAddEdge(edge)) return;
        _edges.addElement(edge);
        // TODO: assumes public
        if (edge instanceof MModelElement && ((MModelElement)edge).getNamespace() == null) {
            _collab.addOwnedElement((MModelElement) edge);
        }
        fireEdgeAdded(edge);
    }

  public void addNodeRelatedEdges(Object node) {
    if ( node instanceof MClassifier ) {
      Collection ends = ((MClassifier)node).getAssociationEnds();
      Iterator iter = ends.iterator();
      while (iter.hasNext()) {
         MAssociationEndRole ae = (MAssociationEndRole) iter.next();
         if(canAddEdge(ae.getAssociation()))
           addEdge(ae.getAssociation());
      }
    }
    if ( node instanceof MGeneralizableElement ) {
      Collection gn = ((MGeneralizableElement)node).getGeneralizations();
      Iterator iter = gn.iterator();
      while (iter.hasNext()) {
         MGeneralization g = (MGeneralization) iter.next();
         if(canAddEdge(g)) {
           addEdge(g);
           return;
         }
      }
      Collection sp = ((MGeneralizableElement)node).getSpecializations();
      iter = sp.iterator();
      while (iter.hasNext()) {
         MGeneralization s = (MGeneralization) iter.next();
         if(canAddEdge(s)) {
           addEdge(s);
           return;
         }
      }
    }
    if ( node instanceof MModelElement ) {
      Vector specs = new Vector(((MModelElement)node).getClientDependencies());
      specs.addAll(((MModelElement)node).getSupplierDependencies());
      Iterator iter = specs.iterator();
      while (iter.hasNext()) {
         MDependency dep = (MDependency) iter.next();
         if(canAddEdge(dep)) {
           addEdge(dep);
           return;
         }
      }
    }
 }


  /** Return true if the two given ports can be connected by a
   * kind of edge to be determined by the ports. */
  public boolean canConnect(Object fromP, Object toP) {
    if ((fromP instanceof MClassifierRole) && (toP instanceof MClassifierRole)) return true;
    return false;
  }


    /** Contruct and add a new edge of a kind determined by the ports */
    public Object connect(Object fromPort, Object toPort) {
        throw new UnsupportedOperationException("should not enter here! connect2");
    }

    /** Contruct and add a new edge of the given kind */
    public Object connect(Object fromPort, Object toPort,
                          java.lang.Class edgeClass) {
        Object connection = null;
        try {
            Editor curEditor = Globals.curEditor();
            ModeManager modeManager = curEditor.getModeManager();
            Mode mode = (Mode)modeManager.top();
            Hashtable args = mode.getArgs();
            MAggregationKind aggregation = (MAggregationKind)args.get("aggregation");
            Boolean unidirectional = (Boolean)args.get("unidirectional");
            connection = UmlFactory.getFactory().buildConnection(
                (MClassifierRole)fromPort,
                aggregation,
                (MClassifierRole)toPort,
                null,
                edgeClass,
                unidirectional
            );
        } catch (org.argouml.model.uml.UmlException ex) {
            // fail silently as we expect users to accidentally drop on to wrong component
        }
        
        if (connection == null) {
            cat.debug("Cannot make a "+ edgeClass.getName() +
                         " between a " + fromPort.getClass().getName() +
                         " and a " + toPort.getClass().getName());
            return null;
        }
        
        addEdge(connection);
        return connection;
        
    }

    /** Contruct and add a new association and connect to
     * the given ports.
     */
    private Object connectAssociationRole(MClassifierRole fromPort, MClassifierRole toPort) {
        if (fromPort instanceof MClassifier && toPort instanceof MClassifier) {
            Editor curEditor = Globals.curEditor();
            ModeManager modeManager = curEditor.getModeManager();
            Mode mode = (Mode)modeManager.top();
            Hashtable args = mode.getArgs();
            MAggregationKind aggregation = (MAggregationKind)args.get("aggregation");
            Boolean unidirectional = (Boolean)args.get("unidirectional");
            return UmlFactory.getFactory().getCollaborations().buildAssociationRole(fromPort, aggregation, toPort, MAggregationKind.NONE, unidirectional);
        }
        
        return null;
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
	    if (me instanceof MClassifier) removeNode(me);
	    if (me instanceof MMessage) removeNode(me);
	    if (me instanceof MAssociation) removeEdge(me);
      }
      else {
	    cat.debug("model added " + me);
      }
    }
  }

} /* end class CollabDiagramGraphModel */

