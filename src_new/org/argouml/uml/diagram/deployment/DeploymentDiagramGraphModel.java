package org.argouml.uml.diagram.deployment;

import org.apache.log4j.Category;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.behavioralelements.commonbehavior.CommonBehaviorFactory;
import org.argouml.model.uml.behavioralelements.commonbehavior.CommonBehaviorHelper;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.uml.MMUtil;
import org.argouml.uml.diagram.UMLMutableGraphSupport;

import java.util.*;
import java.beans.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;

import org.tigris.gef.graph.*;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Mode;
import org.tigris.gef.base.ModeManager;

public class DeploymentDiagramGraphModel extends UMLMutableGraphSupport
implements VetoableChangeListener  {
    protected static Category cat = Category.getInstance(DeploymentDiagramGraphModel.class);

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
    if (nodeOrEdge instanceof MNode) res.addElement(nodeOrEdge);
    if (nodeOrEdge instanceof MNodeInstance) res.addElement(nodeOrEdge);
    if (nodeOrEdge instanceof MComponent) res.addElement(nodeOrEdge);    
    if (nodeOrEdge instanceof MComponentInstance) res.addElement(nodeOrEdge);
    if (nodeOrEdge instanceof MClass) res.addElement(nodeOrEdge);    
    if (nodeOrEdge instanceof MInterface) res.addElement(nodeOrEdge);    
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
    if (port instanceof MNode) {
      MNode no = (MNode) port;
      Collection ends = no.getAssociationEnds();
      if (ends == null) return res; // empty Vector
      Iterator iter = ends.iterator();
      while (iter.hasNext()) {
	    MAssociationEnd aec = (MAssociationEnd) iter.next();
	    res.add(aec.getAssociation());
      }
    }
    if (port instanceof MNodeInstance) {
      MNodeInstance noi = (MNodeInstance) port;
      Collection ends = noi.getLinkEnds();
	    res.addAll(ends);
    }
    if (port instanceof MComponent) {
      MComponent co = (MComponent) port;
      Collection ends = co.getAssociationEnds();
      if (ends == null) return res; // empty Vector
      Iterator endEnum = ends.iterator();
      while (endEnum.hasNext()) {
	    MAssociationEnd aec = (MAssociationEnd) endEnum.next();
	    res.addElement(aec.getAssociation());
      }
    }
    if (port instanceof MComponentInstance) {
      MComponentInstance coi = (MComponentInstance) port;
      Collection ends = coi.getLinkEnds();
	    res.addAll(ends);
    }
    if (port instanceof MClass) {
      MClass cls = (MClass) port;
      Collection ends = cls.getAssociationEnds();
      if (ends == null) return res; // empty Vector 
      Iterator endEnum = ends.iterator();
      while (endEnum.hasNext()) {
	MAssociationEnd ae = (MAssociationEnd) endEnum.next();
	res.addElement(ae.getAssociation());
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
    if (port instanceof MObject) {
      MInstance clo = (MInstance) port;
      Collection ends = clo.getLinkEnds();
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
    } else
    if (edge instanceof MLink) {
       return CommonBehaviorHelper.getHelper().getSource((MLink)edge);
    }
    
    cat.debug("TODO getSourcePort");

    return null;
  }


  /** Return  the other end of an edge */
  public Object getDestPort(Object edge) {
    if (edge instanceof MRelationship) {
        return CoreHelper.getHelper().getDestination((MRelationship)edge);
    } else
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
    return (node instanceof MNode) || (node instanceof MComponent) || 
           (node instanceof MClass) || (node instanceof MInterface) ||
	   (node instanceof MObject) ||(node instanceof MNodeInstance) || 
           (node instanceof MComponentInstance);
  }

  /** Return true if the given object is a valid edge in this graph */
  public boolean canAddEdge(Object edge)  {
    if (edge == null) return false;
if(_edges.contains(edge)) return false;
    Object end0 = null, end1 = null;
    if (edge instanceof MRelationship) {
        end0 = CoreHelper.getHelper().getSource((MRelationship)edge);
        end1 = CoreHelper.getHelper().getDestination((MRelationship)edge);
    }
    else if (edge instanceof MLink) {
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
    cat.debug("adding class node!!");
    if (!canAddNode(node)) return;
    _nodes.addElement(node);
    // TODO: assumes public, user pref for default visibility?
	//do I have to check the namespace here? (Toby)
	if (node instanceof MModelElement &&
		((MModelElement)node).getNamespace() == null) {
		_model.addOwnedElement((MModelElement) node);
	}
    fireNodeAdded(node);
  }

  /** Add the given edge to the graph, if valid. */
  public void addEdge(Object edge) {
    cat.debug("adding class edge!!!!!!");
    if (!canAddEdge(edge)) return;
    _edges.addElement(edge);
    // TODO: assumes public
      if (edge instanceof MModelElement) {
	_model.addOwnedElement((MModelElement) edge);
      }
    fireEdgeAdded(edge);
  }

    /** Add the given edge to the graph, if valid. */
    protected Object addEdge(MModelElement edge) {
        addEdge((Object)edge);
        return edge;
    }

  public void addNodeRelatedEdges(Object node) {
    if ( node instanceof MClassifier ) {
      Collection ends = ((MClassifier)node).getAssociationEnds();
      Iterator iter = ends.iterator();
      while (iter.hasNext()) {
         MAssociationEnd ae = (MAssociationEnd) iter.next();
         if(canAddEdge(ae.getAssociation()))
           addEdge(ae.getAssociation());
           return;
      }
    }
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
    if ( node instanceof MGeneralizableElement ) {
      Collection gn = ((MGeneralizableElement)node).getGeneralizations();
      Iterator iter = gn.iterator();
      while (iter.hasNext()) {
         MGeneralization g = (MGeneralization) iter.next();
         if(canAddEdge(g))
           addEdge(g);
           return;
      }
      Collection sp = ((MGeneralizableElement)node).getSpecializations();
      iter = sp.iterator();
      while (iter.hasNext()) {
         MGeneralization s = (MGeneralization) iter.next();
         if(canAddEdge(s))
           addEdge(s);
           return;
      }
    }
    if ( node instanceof MModelElement ) {
      Vector specs = new Vector(((MModelElement)node).getClientDependencies());
      specs.addAll(((MModelElement)node).getSupplierDependencies());
      Iterator iter = specs.iterator();
      while (iter.hasNext()) {
         MDependency dep = (MDependency) iter.next();
         if(canAddEdge(dep))
           addEdge(dep);
           return;
      }
    }
 }


  /** Return true if the two given ports can be connected by a
   * kind of edge to be determined by the ports. */
  public boolean canConnect(Object fromP, Object toP) {
    return true;
  }


  /** Contruct and add a new edge of a kind determined by the ports */
  public Object connect(Object fromPort, Object toPort) {
      throw new UnsupportedOperationException("should not enter here!");
  }

    /** Contruct and add a new edge of the given kind */
    public Object connect(Object fromPort, Object toPort, java.lang.Class edgeClass) {
        
        Object connection = null;
        
        try {
            if (edgeClass == MAssociation.class) {
                connection = connectAssociation((MClassifier)fromPort, (MClassifier)toPort);
            } else if (edgeClass == MLink.class) {
                connection = connectLink((MInstance)fromPort, (MInstance)toPort);
            } else if (edgeClass == MDependency.class) {
                if ((fromPort instanceof MComponent) && (toPort instanceof MComponent)) {
                    MComponent fromCom = (MComponent) fromPort;
                    MComponent toCom = (MComponent) toPort; 
                    MDependency dep = UmlFactory.getFactory().getCore().buildDependency(fromCom, toCom);
                    addEdge(dep);
                    return dep;
                } else if ((fromPort instanceof MComponentInstance) && (toPort instanceof MComponentInstance)) {
                    MComponentInstance fromComI = (MComponentInstance) fromPort;
                    MComponentInstance toComI = (MComponentInstance) toPort; 
                    MDependency dep = UmlFactory.getFactory().getCore().buildDependency(fromComI, toComI);
                    addEdge(dep);
                    return dep;
	  	} else if ((fromPort instanceof MClass) && (toPort instanceof MClass)) {
                    MClass fromCls = (MClass) fromPort;
                    MClass toCls = (MClass) toPort;
                    MDependency dep = UmlFactory.getFactory().getCore().buildDependency(fromCls, toCls);
                    addEdge(dep);
                    return dep;
                } else if ((fromPort instanceof MClass) && (toPort instanceof MInterface)) {
                    MClass fromCls = (MClass) fromPort;
                    MInterface toIntf = (MInterface) toPort;        
                    MDependency dep = UmlFactory.getFactory().getCore().buildDependency(fromCls, toIntf);
                    addEdge(dep);
                    return dep;
                } else if ((fromPort instanceof MInterface) && (toPort instanceof MClass)) {
                    MInterface fromIntf = (MInterface) fromPort;
                    MClass toCls = (MClass) toPort;
                    MDependency dep = UmlFactory.getFactory().getCore().buildDependency(fromIntf, toCls);
                    addEdge(dep);
                    return dep;
                } else if ((fromPort instanceof MInterface) && (toPort instanceof MInterface)) {
                    MInterface fromIntf = (MInterface) fromPort;
                    MInterface toIntf = (MInterface) toPort;
                    MDependency dep = UmlFactory.getFactory().getCore().buildDependency(fromIntf, toIntf);
                    addEdge(dep);
                    return dep;
                } else if ((fromPort instanceof MObject) && (toPort instanceof MObject)) {
                    MObject fromObj = (MObject) fromPort;
                    MObject toObj = (MObject) toPort; 
                    // nsuml: using Binding as default
                    MDependency dep = UmlFactory.getFactory().getCore().buildDependency(fromObj, toObj);
                    addEdge(dep);
                    return dep;
		} else if ((fromPort instanceof MComponent) && (toPort instanceof MNode)) {
                    MComponent fromObj = (MComponent)fromPort;
                    MNode toObj = (MNode)toPort;
                    MDependency dep = CoreHelper.getHelper().buildSupportDependency(fromObj, toObj);
                    addEdge(dep);
                    return dep;
                } else if ((fromPort instanceof MObject) && (toPort instanceof MComponent)) {
                    MObject fromObj = (MObject)fromPort;
                    MComponent toObj = (MComponent)toPort;
                    MDependency dep = CoreHelper.getHelper().buildSupportDependency(fromObj, toObj);
                    addEdge(dep);
                    return dep;
                } else if ((fromPort instanceof MComponentInstance) && (toPort instanceof MNodeInstance)) {
                    MComponentInstance fromObj = (MComponentInstance)fromPort;
                    MNodeInstance toObj = (MNodeInstance)toPort;
                    MDependency dep = CoreHelper.getHelper().buildSupportDependency(fromObj, toObj);
                    addEdge(dep);
                    return dep;
                } else if ((fromPort instanceof MObject) && (toPort instanceof MComponentInstance)) {
                    MObject fromObj = (MObject)fromPort;
                    MComponentInstance toObj = (MComponentInstance)toPort;
                    MDependency dep = CoreHelper.getHelper().buildSupportDependency(fromObj, toObj);
                    addEdge(dep);
                    return dep;
                }
            }
        } catch (ClassCastException ex) {
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
    private Object connectDependency(MModelElement fromPort, MModelElement toPort) {
        if ((fromPort instanceof MComponent && toPort instanceof MComponent) ||
                (fromPort instanceof MComponentInstance && toPort instanceof MComponentInstance) ||
                (fromPort instanceof MClass && toPort instanceof MClass) ||
                (fromPort instanceof MClass && toPort instanceof MInterface) ||
                (fromPort instanceof MInterface && toPort instanceof MClass) ||
                (fromPort instanceof MInterface && toPort instanceof MInterface) ||
                (fromPort instanceof MObject && toPort instanceof MObject) ||
                (fromPort instanceof MComponent && toPort instanceof MNode) ||
                (fromPort instanceof MObject && toPort instanceof MComponent) ||
                (fromPort instanceof MComponentInstance && toPort instanceof MNodeInstance) ||
                (fromPort instanceof MObject && toPort instanceof MComponentInstance)) {
            return CoreHelper.getHelper().buildSupportDependency(fromPort, toPort);
        }
        return null;
    }
    /** Contruct and add a new association and connect to
     * the given ports.
     */
    private Object connectLink(MInstance fromPort, MInstance toPort) {
        if ((fromPort instanceof MNodeInstance && toPort instanceof MNodeInstance) ||
                (fromPort instanceof MObject && toPort instanceof MObject)) {
            return CommonBehaviorFactory.getFactory().buildLink(fromPort, toPort);
        }
        return null;
    }
    /** Contruct and add a new association and connect to
     * the given ports.
     */
    private Object connectAssociation(MModelElement fromPort, MModelElement toPort) {
        if ((fromPort instanceof MClass && toPort instanceof MClass) ||
                (fromPort instanceof MNode && toPort instanceof MNode) ||
                (fromPort instanceof MInterface && toPort instanceof MClass) ||
                (fromPort instanceof MClass && toPort instanceof MInterface)) {
            Editor curEditor = Globals.curEditor();
            ModeManager modeManager = curEditor.getModeManager();
            Mode mode = (Mode)modeManager.top();
            Hashtable args = mode.getArgs();
            boolean unidirectional = false;
            MAggregationKind aggregation = (MAggregationKind)args.get("aggregation");
            if (aggregation != null) {
                unidirectional = ((Boolean)args.get("unidirectional")).booleanValue();
            } else {
                aggregation = MAggregationKind.NONE;
            }
            return UmlFactory.getFactory().getCore().buildAssociation((MClassifier)fromPort, !unidirectional, aggregation, (MClassifier)toPort, true, MAggregationKind.NONE);
        }
        
        return null;
    }

  public void vetoableChange(PropertyChangeEvent pce) {
    //throws PropertyVetoException

    if ("ownedElement".equals(pce.getPropertyName())) {
      Vector oldOwned = (Vector) pce.getOldValue();
      MElementImport eo = (MElementImport) pce.getNewValue();
      MModelElement me = eo.getModelElement();
      if (oldOwned.contains(eo)) {
	    cat.debug("model removed " + me);
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
	    cat.debug("model added " + me);
      }
    }
  }

  static final long serialVersionUID = 1003748292917485298L;

} /* end class DeploymentDiagramGraphModel */

