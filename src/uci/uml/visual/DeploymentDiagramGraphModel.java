
package uci.uml.visual;

//import java.util.*;

import com.sun.java.util.collections.*;
import java.util.Enumeration;
import java.beans.*;

import uci.graph.*;
import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.behavior.common_behavior.*;

public class DeploymentDiagramGraphModel extends MutableGraphSupport
implements MutableGraphModel, VetoableChangeListener, MElementListener {
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected Vector _nodes = new Vector();
  protected Vector _edges = new Vector();

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
    if (_model != null) _model.removeMElementListener(this);
    _model = m;
    if (_model != null) _model.addMElementListener(this);
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
    if (nodeOrEdge instanceof MNodeImpl) res.addElement(nodeOrEdge);
    if (nodeOrEdge instanceof MNodeInstanceImpl) res.addElement(nodeOrEdge);
    if (nodeOrEdge instanceof MComponentImpl) res.addElement(nodeOrEdge);    
    if (nodeOrEdge instanceof MComponentInstanceImpl) res.addElement(nodeOrEdge);
    if (nodeOrEdge instanceof MClassImpl) res.addElement(nodeOrEdge);    
    if (nodeOrEdge instanceof MInterfaceImpl) res.addElement(nodeOrEdge);    
    if (nodeOrEdge instanceof MObjectImpl) res.addElement(nodeOrEdge);
    return res;
  }

  /** Return the node or edge that owns the given port */
  public Object getOwner(Object port) {
    return port;
  }


  /** Return all edges going to given port */
  public Vector getInEdges(Object port) {
    Vector res = new Vector(); //wasteful!
    if (port instanceof MNodeImpl) {
      MNode no = (MNode) port;
      Collection ends = no.getAssociationEnds();
      if (ends == null) return res; // empty Vector
      Iterator iter = ends.iterator();
      while (iter.hasNext()) {
	    MAssociationEnd aec = (MAssociationEnd) iter.next();
	    res.add(aec.getAssociation());
      }
    }
    if (port instanceof MNodeInstanceImpl) {
      MNodeInstance noi = (MNodeInstance) port;
      Collection ends = noi.getLinkEnds();
	    res.addAll(ends);
    }
    if (port instanceof MComponentImpl) {
      MComponent co = (MComponent) port;
      Collection ends = co.getAssociationEnds();
      if (ends == null) return res; // empty Vector
      Iterator endEnum = ends.iterator();
      while (endEnum.hasNext()) {
	    MAssociationEnd aec = (MAssociationEnd) endEnum.next();
	    res.addElement(aec.getAssociation());
      }
    }
    if (port instanceof MComponentInstanceImpl) {
      MComponentInstance coi = (MComponentInstance) port;
      Collection ends = coi.getLinkEnds();
	    res.addAll(ends);
    }
    if (port instanceof MClassImpl) {
      MClass cls = (MClass) port;
      Collection ends = cls.getAssociationEnds();
      if (ends == null) return res; // empty Vector 
      Iterator endEnum = ends.iterator();
      while (endEnum.hasNext()) {
	MAssociationEnd ae = (MAssociationEnd) endEnum.next();
	res.addElement(ae.getAssociation());
      }
    }
    if (port instanceof MInterfaceImpl) {
      MInterface Intf = (MInterface) port;
      Collection ends = Intf.getAssociationEnds();
      if (ends == null) return res; // empty Vector 
      Iterator endEnum = ends.iterator();
      while (endEnum.hasNext()) {
	    MAssociationEnd ae = (MAssociationEnd) endEnum.next();
	    res.addElement(ae.getAssociation());
	  }
    }
    if (port instanceof MObjectImpl) {
      MInstance clo = (MInstance) port;
      Collection ends = clo.getLinkEnds();
	    res.addAll(ends);
    }


    return res;
  }

  /** Return all edges going from given port */
  public Vector getOutEdges(Object port) {
    return new Vector(); // needs-more-work?
  }


  /** Return one end of an edge */
  public Object getSourcePort(Object edge) {
    if (edge instanceof MAssociation) {
      MAssociation assoc = (MAssociation) edge;
      List conns = assoc.getConnections();
      return conns.get(0);
    }
    System.out.println("needs-more-work getSourcePort");

    return null;
  }


  /** Return  the other end of an edge */
  public Object getDestPort(Object edge) {
    if (edge instanceof MAssociation) {
      MAssociation assoc = (MAssociation) edge;
      List conns = assoc.getConnections();
      return conns.get(1);
    }
    System.out.println("needs-more-work getDestPort");

    return null;
  }



  ////////////////////////////////////////////////////////////////
  // MutableGraphModel implementation

  /** Return true if the given object is a valid node in this graph */
  public boolean canAddNode(Object node) {
    return (node instanceof MNode) || (node instanceof MComponent) || 
           (node instanceof MClass) || (node instanceof MInterface) ||
	   (node instanceof MObject) ||(node instanceof MNodeInstance) || 
           (node instanceof MComponentInstance);
  }

  /** Return true if the given object is a valid edge in this graph */
  public boolean canAddEdge(Object edge)  {

    Object end0 = null, end1 = null;
    if (edge instanceof MAssociation) {
      List conns = ((MAssociation)edge).getConnections();
      MAssociationEnd ae0 = (MAssociationEnd) conns.get(0);
      MAssociationEnd ae1 = (MAssociationEnd) conns.get(1);
      if (ae0 == null || ae1 == null) return false;
      end0 = ae0.getType();
      end1 = ae1.getType();
    }
    else if (edge instanceof MDependency) {
      Collection clients = ((MDependency)edge).getClients();
      Collection suppliers = ((MDependency)edge).getSuppliers();
      if (clients == null || suppliers == null) return false;
      end0 = ((Object[])clients.toArray())[0];
      end1 = ((Object[])suppliers.toArray())[1];
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

  /** Remove the given node from the graph. */
  public void removeNode(Object node) {
    if (!_nodes.contains(node)) return;
    _nodes.removeElement(node);
    fireNodeRemoved(node);
  }

  /** Add the given node to the graph, if valid. */
  public void addNode(Object node) {
    //System.out.println("adding class node!!");
    if (_nodes.contains(node)) return;
    _nodes.addElement(node);
    // needs-more-work: assumes public, user pref for default visibility?
	//do I have to check the namespace here? (Toby)
	if (node instanceof MModelElement &&
		((MModelElement)node).getNamespace() == null) {
		_model.addOwnedElement((MModelElement) node);
	}
    fireNodeAdded(node);
  }

  /** Add the given edge to the graph, if valid. */
  public void addEdge(Object edge) {
    //System.out.println("adding class edge!!!!!!");
    if (_edges.contains(edge)) return;
    _edges.addElement(edge);
    // needs-more-work: assumes public
      if (edge instanceof MModelElement) {
	_model.addOwnedElement((MModelElement) edge);
      }
    fireEdgeAdded(edge);
  }

  /** Remove the given edge from the graph. */
  public void removeEdge(Object edge) {
    if (!_edges.contains(edge)) return;
    _edges.removeElement(edge);
    fireEdgeRemoved(edge);
  }

  /** Return true if the two given ports can be connected by a
   * kind of edge to be determined by the ports. */
  public boolean canConnect(Object fromP, Object toP) {
    return true;
  }


  /** Contruct and add a new edge of a kind determined by the ports */
  public Object connect(Object fromPort, Object toPort) {
    System.out.println("should not enter here! connect2");
    return null;
  }

  /** Contruct and add a new edge of the given kind */
  public Object connect(Object fromPort, Object toPort,
			java.lang.Class edgeClass) {

    try {
      if ((fromPort instanceof MNodeImpl) && (toPort instanceof MNodeImpl)) {
	    MNode fromNo = (MNode) fromPort;
	    MNode toNo = (MNode) toPort;

	    if (edgeClass == MAssociationImpl.class) {
	      MAssociation asc = new MAssociationImpl();
		  MAssociationEnd ae0 = new MAssociationEndImpl();
		  ae0.setType(fromNo);
		  MAssociationEnd ae1 = new MAssociationEndImpl();
		  ae1.setType(toNo);
		  asc.addConnection(ae0);
		  asc.addConnection(ae1);
	      addEdge(asc);
	      return asc;
	    }
      }
      else if ((fromPort instanceof MNodeInstanceImpl) && (toPort instanceof MNodeInstanceImpl)) {
	MNodeInstance fromNoI = (MNodeInstance) fromPort;
	MNodeInstance toNoI = (MNodeInstance) toPort; 
   	
    	if (edgeClass == MLinkImpl.class) {
    	  MLink link = new MLinkImpl();
		  MLinkEnd le0 = new MLinkEndImpl();
		  le0.setInstance(fromNoI);
		  MLinkEnd le1 = new MLinkEndImpl();
		  le1.setInstance(toNoI);
		  link.addConnection(le0);
		  link.addConnection(le1);
    	  addEdge(link);
    	  return link;
    	}

      }
      else if ((fromPort instanceof MComponentImpl) && (toPort instanceof MComponentImpl)) {
	MComponent fromCom = (MComponent) fromPort;
	MComponent toCom = (MComponent) toPort; 
   	
	if (edgeClass == MDependencyImpl.class) {
		MDependency dep = new MUsageImpl();
		dep.addSupplier(fromCom);
		dep.addClient(toCom);
		addEdge(dep);
		return dep;
	}

      }
      else if ((fromPort instanceof MComponentInstanceImpl) && (toPort instanceof MComponentInstanceImpl)) {
	MComponentInstance fromComI = (MComponentInstance) fromPort;
	MComponentInstance toComI = (MComponentInstance) toPort; 
   	
	if (edgeClass == MDependencyImpl.class) {
		MDependency dep = new MUsageImpl();
		dep.addSupplier(fromComI);
		dep.addClient(toComI);
		addEdge(dep);
		return dep;
	}

      }
 
      else if ((fromPort instanceof MClassImpl) && (toPort instanceof MClassImpl)) {
	    MClass fromCls = (MClass) fromPort;
	    MClass toCls = (MClass) toPort;

	    if (edgeClass == MAssociationImpl.class) {
	      MAssociation asc = new MAssociationImpl();
		  MAssociationEnd ae0 = new MAssociationEndImpl();
		  ae0.setType(fromCls);
		  MAssociationEnd ae1 = new MAssociationEndImpl();
		  ae1.setType(toCls);
		  asc.addConnection(ae0);
		  asc.addConnection(ae1);
	      addEdge(asc);
	      return asc;
	    }
	    else if (edgeClass == MDependencyImpl.class) {
			// nsuml: using Binding as default
	      MDependency dep = new MBindingImpl();
		  dep.addSupplier(fromCls);
		  dep.addClient(toCls);
	      addEdge(dep);
	      return dep;
	    }
	    else {
	      System.out.println("Cannot make a "+ edgeClass.getName() +
			     " between a " + fromPort.getClass().getName() +
			     " and a " + toPort.getClass().getName());
	      return null;
	    }
      }

      else if ((fromPort instanceof MClassImpl) && (toPort instanceof MInterfaceImpl)) {
	MClass fromCls = (MClass) fromPort;
	MInterface toIntf = (MInterface) toPort;
        
        if (edgeClass == MAssociationImpl.class) {
	  MAssociation asc = new MAssociationImpl();
	  MAssociationEnd ae0 = new MAssociationEndImpl();
	  ae0.setType(fromCls);
	  MAssociationEnd ae1 = new MAssociationEndImpl();
	  ae1.setType(toIntf);
	  asc.addConnection(ae0);
	  asc.addConnection(ae1);
	  ae0.setNavigable(false); 
	  addEdge(asc);
	  return asc;
	}
	else if (edgeClass == MDependencyImpl.class) {
		//nsuml: using Abstraction, maybe realization is meant?
		MDependency dep = new MAbstractionImpl();
		dep.addSupplier(fromCls);
		dep.addClient(toIntf);
		addEdge(dep);
		return dep;
	}
	else {
	  System.out.println("Cannot make a "+ edgeClass.getName() +
			     " between a " + fromPort.getClass().getName() +
			     " and a " + toPort.getClass().getName());
	  return null;
	}
      }

      else if ((fromPort instanceof MInterfaceImpl) && (toPort instanceof MClassImpl)) {
	MInterface fromIntf = (MInterface) fromPort;
	MClass toCls = (MClass) toPort;


	if (edgeClass == MAssociationImpl.class) {
	  MAssociation asc = new MAssociationImpl();
	  MAssociationEnd ae0 = new MAssociationEndImpl();
	  ae0.setType(fromIntf);
	  MAssociationEnd ae1 = new MAssociationEndImpl();
	  ae1.setType(toCls);
	  ae0.setNavigable(false); 
	  asc.addConnection(ae0);
	  asc.addConnection(ae1);
	  addEdge(asc);
	  return asc;
	}
	else if (edgeClass == MDependencyImpl.class) {
		//nsuml: using Abstraction, maybe realization is meant?
		MDependency dep = new MAbstractionImpl();
		dep.addSupplier(fromIntf);
		dep.addClient(toCls);
		addEdge(dep);
		return dep;
	}

	else {
	  System.out.println("Cannot make a "+ edgeClass.getName() +
			     " between a " + fromPort.getClass().getName() +
			     " and a " + toPort.getClass().getName());
	  return null;
	}
      }

      else if ((fromPort instanceof MInterfaceImpl) && (toPort instanceof MInterfaceImpl)) {
	MInterface fromIntf = (MInterface) fromPort;
	MInterface toIntf = (MInterface) toPort;

	if (edgeClass == MDependencyImpl.class) {
		//nsuml: using Binding
	        MDependency dep = new MBindingImpl();
		dep.addSupplier(fromIntf);
		dep.addClient(toIntf);
		addEdge(dep);
	  addEdge(dep);
	  return dep;
	}
	else {
	  System.out.println("Cannot make a "+ edgeClass.getName() +
			     " between a " + fromPort.getClass().getName() +
			     " and a " + toPort.getClass().getName());
	  return null;
	}
      }

      else if ((fromPort instanceof MObjectImpl) && (toPort instanceof MObjectImpl)) {
	MObject fromObj = (MObject) fromPort;
	MObject toObj = (MObject) toPort; 

    	if (edgeClass == MLinkImpl.class) {
    	  MLink link = new MLinkImpl();
		  MLinkEnd le0 = new MLinkEndImpl();
		  le0.setInstance(fromObj);
		  MLinkEnd le1 = new MLinkEndImpl();
		  le1.setInstance(toObj);
		  link.addConnection(le0);
		  link.addConnection(le1);
    	  addEdge(link);
    	  return link;
    	}
      }

      else if ((fromPort instanceof MObjectImpl) && (toPort instanceof MObjectImpl)) {
	MObject fromObj = (MObject) fromPort;
	MObject toObj = (MObject) toPort; 
   	
	if (edgeClass == MDependencyImpl.class) {
		MDependency dep = new MAbstractionImpl();
		dep.addSupplier(fromObj);
		dep.addClient(toObj);
		addEdge(dep);
		return dep;
	}

      }

      else {
	  System.out.println("Incorrect edge");
	  return null;
      }
    }
    catch (Exception ex) { }

    System.out.println("should not enter here! connect3");
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
	    //System.out.println("model removed " + me);
	    if (me instanceof MNode) removeNode(me);
	    if (me instanceof MNodeInstance) removeNode(me);
	    if (me instanceof MComponent) removeNode(me);
	    if (me instanceof MComponentInstance) removeNode(me);
	    if (me instanceof MClass) removeNode(me);
	    if (me instanceof MInterface) removeNode(me);
	    if (me instanceof MObject) removeNode(me);
	    if (me instanceof MAssociation) removeEdge(me);
	    if (me instanceof MDependency) removeEdge(me);
            if (me instanceof MLink) removeEdge(me);
      }
      else {
	    //System.out.println("model added " + me);
      }
    }
  }

  public void propertySet(MElementEvent mee) {
  }
  public void listRoleItemSet(MElementEvent mee) {
  }
  public void recovered(MElementEvent mee) {
  }
  public void removed(MElementEvent mee) {
  }
  public void roleAdded(MElementEvent mee) {
  }
  public void roleRemoved(MElementEvent mee) {
  }

  static final long serialVersionUID = 1003748292917485298L;

} /* end class DeploymentDiagramGraphModel */

