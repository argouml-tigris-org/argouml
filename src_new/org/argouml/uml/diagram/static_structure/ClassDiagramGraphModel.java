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

// File: ClassDiagramGraphModel.java
// Classes: ClassDiagramGraphModel
// Original Author: jrobbins@ics.uci.edu
// $Id$


package org.argouml.uml.diagram.static_structure;

import java.util.*;
import java.beans.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

import org.apache.log4j.Category;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.uml.MMUtil;

/** This class defines a bridge between the UML meta-model
 *  representation of the design and the GraphModel interface used by
 *  GEF.  This class handles only UML Class digrams.  */

public class ClassDiagramGraphModel extends UMLMutableGraphSupport
implements VetoableChangeListener  {
    protected static Category cat = Category.getInstance(ClassDiagramGraphModel.class);
  ////////////////////////////////////////////////////////////////
  // instance variables
 
  /** The "home" UML model of this diagram, not all ModelElements in this
   *  graph are in the home model, but if they are added and don't
   *  already have a model, they are placed in the "home model".
   *  Also, elements from other models will have their FigNodes add a
   *  line to say what their model is. */

  protected MNamespace _model;

  ////////////////////////////////////////////////////////////////
  // accessors

  public MNamespace getNamespace() { return _model; }
  public void setNamespace(MNamespace m) {
    _model = m;
  }

  ////////////////////////////////////////////////////////////////
  // GraphModel implementation


  /** Return all ports on node or edge */
  public Vector getPorts(Object nodeOrEdge) {
    Vector res = new Vector();  //wasteful!
    if (nodeOrEdge instanceof MClass) res.addElement(nodeOrEdge);
    if (nodeOrEdge instanceof MInterface) res.addElement(nodeOrEdge);
    if (nodeOrEdge instanceof MInstance) res.addElement(nodeOrEdge);
    if (nodeOrEdge instanceof MModel) res.addElement(nodeOrEdge);
    return res;
  }

  /** Return the node or edge that owns the given port */
  public Object getOwner(Object port) {
    return port;
  }

  /** Return all edges going to given port */
  public Vector getInEdges(Object port) {
    Vector res = new Vector(); //wasteful!
    if (port instanceof MClass) {
      MClass cls = (MClass) port;
      Collection ends = cls.getAssociationEnds();
      if (ends == null) return res; // empty Vector 
      //java.util.Enumeration endEnum = ends.elements();
	  Iterator iter = ends.iterator();
      while (iter.hasNext()) {
		  MAssociationEnd ae = (MAssociationEnd) iter.next();
		  res.add(ae.getAssociation());
      }
    }
    if (port instanceof MInterface) {
      MInterface Intf = (MInterface) port;
      Collection ends = Intf.getAssociationEnds();
      if (ends == null) return res; // empty Vector 
      Iterator endEnum = ends.iterator();
      while (endEnum.hasNext()) {
	    MAssociationEnd ae = (MAssociationEnd) endEnum.next();
	    res.addElement(ae.getAssociation());
	  }
    }
    /*if (port instanceof MPackage) {
      MPackage cls = (MPackage) port;
      Vector ends = cls.getAssociationEnd();
      if (ends == null) return res; // empty Vector
      java.util.Enumeration endEnum = ends.elements();
      while (endEnum.hasMoreElements()) {
	    MAssociationEnd ae = (MAssociationEnd) endEnum.nextElement();
	    res.addElement(ae.getAssociation());
      }
    }*/
    if (port instanceof MInstance) {
      MInstance inst = (MInstance) port;
      Collection ends = inst.getLinkEnds();
	  res.addAll(ends);
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
    cat.debug("TODO getSourcePort");
    return null;
  }


  ////////////////////////////////////////////////////////////////
  // MutableGraphModel implementation

  /** Return true if the given object is a valid node in this graph */
  public boolean canAddNode(Object node) {
    if (_nodes.contains(node)) return false;
    return (node instanceof MClass) || 
        (node instanceof MInterface)|| 
        (node instanceof MModel)    || 
        (node instanceof MPackage)  || 
        (node instanceof MInstance);
  }

  /** Return true if the given object is a valid edge in this graph */
  public boolean canAddEdge(Object edge)  {
    if (edge == null) return false;
    if(_edges.contains(edge)) return false;
    Object end0 = null, end1 = null;
    if (edge instanceof MAssociation) {
      List conns = ((MAssociation)edge).getConnections();
      if (conns.size() < 2) return false;
      MAssociationEnd ae0 = (MAssociationEnd) conns.get(0);
      MAssociationEnd ae1 = (MAssociationEnd) conns.get(1);
      if (ae0 == null || ae1 == null) return false;
      end0 = ae0.getType();
      end1 = ae1.getType();
    }
    else if (edge instanceof MGeneralization) {
      end0 = ((MGeneralization)edge).getChild();
      end1 = ((MGeneralization)edge).getParent();
    }
    else if (edge instanceof MDependency) {
      Collection clients = ((MDependency)edge).getClients();
      Collection suppliers = ((MDependency)edge).getSuppliers();
      if (clients == null || suppliers == null) return false;
      end0 = ((Object[])clients.toArray())[0];
      end1 = ((Object[])suppliers.toArray())[0];
    }
    else if (edge instanceof MLink) {
      Collection roles = ((MLink)edge).getConnections();
      MLinkEnd le0 = (MLinkEnd)((Object[]) roles.toArray())[0];
      MLinkEnd le1 = (MLinkEnd)((Object[]) roles.toArray())[0];
      if (le0 == null || le1 == null) return false;
      end0 = le0.getInstance();
      end1 = le1.getInstance();
    }
    if (end0 == null || end1 == null) return false;
    if (!_nodes.contains(end0)) return false;
    if (!_nodes.contains(end1)) return false;
    return true;
  }


  /** Add the given node to the graph, if valid. */
  public void addNode(Object node) {
    cat.debug("adding class node!!");
    if (!canAddNode(node)) return;
    _nodes.addElement(node);
    if (node instanceof MModelElement &&
	((MModelElement)node).getNamespace() == null) {
	_model.addOwnedElement((MModelElement) node);
    }
    if (node instanceof MInterface){
	cat.debug("Interface stereo: "+MMUtil.STANDARDS.lookup("interface"));

	((MInterface)node).setStereotype((MStereotype)MMUtil.STANDARDS.lookup("interface"));
    }
    
    fireNodeAdded(node);
    cat.debug("adding "+node+" OK");
  }

  /** Add the given edge to the graph, if valid. */
  public void addEdge(Object edge) {
    cat.debug("adding class edge!!!!!!");
    if (!canAddEdge(edge)) return;
    _edges.addElement(edge);
    // TODO: assumes public
    if (edge instanceof MModelElement &&
       ((MModelElement)edge).getNamespace() == null) {
      _model.addOwnedElement((MModelElement) edge);
    }
    fireEdgeAdded(edge);
  }


/**
 * Adds the edges from the given node. For example, this method lets you add
 * an allready existing massociation between two figclassifiers.
 * @see org.tigris.gef.graph.MutableGraphModel#addNodeRelatedEdges(Object)
 */
  public void addNodeRelatedEdges(Object node) {
    if ( node instanceof MClassifier ) {
      Collection ends = ((MClassifier)node).getAssociationEnds();
      Iterator iter = ends.iterator();
      while (iter.hasNext()) {
         MAssociationEnd ae = (MAssociationEnd) iter.next();
         if(canAddEdge(ae.getAssociation())) {
           addEdge(ae.getAssociation());
           // return;
         }
      }
    }
    if ( node instanceof MGeneralizableElement ) {
      Collection gn = ((MGeneralizableElement)node).getGeneralizations();
      Iterator iter = gn.iterator();
      while (iter.hasNext()) {
         MGeneralization g = (MGeneralization) iter.next();
         if(canAddEdge(g)) {
           addEdge(g);
           // return;
         }
      }
      Collection sp = ((MGeneralizableElement)node).getSpecializations();
      iter = sp.iterator();
      while (iter.hasNext()) {
         MGeneralization s = (MGeneralization) iter.next();
         if(canAddEdge(s)) {
           addEdge(s);
           // return;
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
           // return;
         }
      }
    }
  }


  /** Return true if the two given ports can be connected by a 
   * kind of edge to be determined by the ports. */
  public boolean canConnect(Object fromP, Object toP) { return true; }


  /** Contruct and add a new edge of a kind determined by the ports */
  public Object connect(Object fromPort, Object toPort) {
      throw new UnsupportedOperationException("should not enter here!");
  }

  /** Contruct and add a new edge of the given kind */
  public Object connect(Object fromPort, Object toPort,
                        java.lang.Class edgeClass) {
	  cat.debug("connecting: "+fromPort+toPort+edgeClass);

      if ((fromPort instanceof MModelElement) && 
               (toPort instanceof MModelElement)) {
          if (edgeClass == MPermission.class) {
              MModelElement fromMMElem = (MModelElement) fromPort;
              MModelElement toMMElem = (MModelElement) toPort;
              MPermission per = 
                  UmlFactory.getFactory().getCore().
                  buildPermission(fromMMElem, toMMElem);
              addEdge(per);
              return per;
          }
          else if (edgeClass == MUsage.class) {
              MModelElement fromMMElem = (MModelElement) fromPort;
              MModelElement toMMElem = (MModelElement) toPort;
              MUsage usa = 
                  UmlFactory.getFactory().getCore().
                  buildUsage(fromMMElem, toMMElem);
              addEdge(usa);
              return usa;
          }
      }

      if ((fromPort instanceof MClass) && (toPort instanceof MClass)) {
          MClass fromCls = (MClass) fromPort;
          MClass toCls = (MClass) toPort;

          if (edgeClass == MGeneralization.class) {
              MGeneralization gen = 
                  UmlFactory.getFactory().getCore().buildGeneralization(fromCls, toCls);
              if (gen != null) addEdge(gen);
              return gen;
          }
          else if (edgeClass == MAssociation.class) {
              MAssociation asc = 
                  UmlFactory.getFactory().getCore().buildAssociation(fromCls, toCls);
              addEdge(asc);
              return asc;
              //return asc;
          }
          else if (edgeClass == MDependency.class) {
              // nsuml: using Binding as default
              MDependency dep = 
                  UmlFactory.getFactory().getCore().buildDependency(fromCls, toCls);
              addEdge(dep);
              return dep;
          }
          else {
              cat.debug("connect: Cannot make a "+ edgeClass.getName() +
                                 " between a " + fromPort.getClass().getName() +
                                 " and a " + toPort.getClass().getName());
              return null;
          }
      }
      else if ((fromPort instanceof MPackage) && (toPort instanceof MPackage))
          {
              MPackage fromPack = (MPackage) fromPort;
              MPackage toPack = (MPackage) toPort;
              // TODO: assumes public, user pref for default visibility?
              //do I have to check the namespace here? (Toby)
              if (edgeClass == MDependency.class) {
                  // nsuml: using Usage as default
                  MDependency dep = UmlFactory.getFactory().getCore().buildDependency(fromPack, toPack);
                  addEdge(dep);
                  return dep;
              }
          }

      // break
      else if ((fromPort instanceof MClass) && (toPort instanceof MInterface)) {
          MClass fromCls = (MClass) fromPort;
          MInterface toIntf = (MInterface) toPort;
	 
          if (edgeClass == MAbstraction.class) {
              MAbstraction real = 
                  UmlFactory.getFactory().getCore().buildRealization(fromCls, toIntf);
              addEdge(real);
              return real;
          }
	
          else  if (edgeClass == MAssociation.class) {
              MAssociation asc = 
                  UmlFactory.getFactory().getCore().buildAssociation(fromCls, false, toIntf, true);
              addEdge(asc);
              return asc;
          }
          else if (edgeClass == MDependency.class) {
                  // nsuml: using Binding as default
                  MDependency dep = 
                      UmlFactory.getFactory().getCore().buildDependency(fromCls, toIntf);
                  addEdge(dep);
                  return dep;
              }
          else {
              cat.debug("Cannot make a "+ edgeClass.getName() +
                                 " between a " + fromPort.getClass().getName() +
                                 " and a " + toPort.getClass().getName());
              return null;
          }
      }

      // break
      else if ((fromPort instanceof MInterface) && (toPort instanceof MClass)) 
          {
              MInterface fromIntf = (MInterface) fromPort;
              MClass toCls = (MClass) toPort;


              if (edgeClass == MAssociation.class) {
                  MAssociation asc = 
                      UmlFactory.getFactory().getCore().buildAssociation(fromIntf, true, 
                                                        toCls, false);
                  addEdge(asc);
                  return asc;
              }
              else if (edgeClass == MDependency.class) {
                  // nsuml: using Binding as default
                  MDependency dep = 
                      UmlFactory.getFactory().getCore().buildDependency(fromIntf, toCls);
                  addEdge(dep);
                  return dep;
              }

              // 	else if (edgeClass == MAbstractionImpl.class) {
              // 		MAbstraction real = 
              //            MMUtil.SINGLETON.buildRealization(toCls, fromIntf);
              // 		addEdge(real);
              // 		return real;
              // 	}

              else {
                  cat.debug("Cannot make a "+ edgeClass.getName() +
                                     " between a " + fromPort.getClass().getName() +
                                     " and a " + toPort.getClass().getName());
                  return null;
              }
          }

      // break
      else if ((fromPort instanceof MInterface) && 
               (toPort instanceof MInterface)) {
          MInterface fromIntf = (MInterface) fromPort;
          MInterface toIntf = (MInterface) toPort;

          if (edgeClass == MGeneralization.class) {
              MGeneralization gen = CoreFactory.getFactory().buildGeneralization(fromIntf, toIntf);
              addEdge(gen);
              return gen;
          }
          else if (edgeClass == MDependency.class) {
              //nsuml: using Binding
              MDependency dep = UmlFactory.getFactory().getCore().buildDependency(fromIntf, toIntf);
              addEdge(dep);
              return dep;
          }
          else {
              cat.debug("Cannot make a "+ edgeClass.getName() +
                                 " between a " + fromPort.getClass().getName() +
                                 " and a " + toPort.getClass().getName());
              return null;
          }
      }

      // break
      else if ((fromPort instanceof MInstance) && 
               (toPort instanceof MInstance)) {
          MInstance fromInst = (MInstance) fromPort;
          MInstance toInst = (MInstance) toPort;
          if (edgeClass == MLink.class) {
              MLink link = UmlFactory.getFactory().getCommonBehavior().buildLink(fromInst, toInst);
              addEdge(link);
              return link;
          }
      }
      throw new UnsupportedOperationException("should not enter here!");
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
	if (me instanceof MPackage) removeNode(me);
	if (me instanceof MAssociation) removeEdge(me);
	if (me instanceof MDependency) removeEdge(me);
	if (me instanceof MGeneralization) removeEdge(me);
      }
      else {
	cat.debug("model added " + me);
      }
    }
  }


  static final long serialVersionUID = -2638688086415040146L;

} /* end class ClassDiagramGraphModel */

