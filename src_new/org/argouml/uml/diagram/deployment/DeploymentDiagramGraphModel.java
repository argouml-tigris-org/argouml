package org.argouml.uml.diagram.deployment;

import org.apache.log4j.Category;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.behavioralelements.commonbehavior.CommonBehaviorFactory;
import org.argouml.model.uml.behavioralelements.commonbehavior.CommonBehaviorHelper;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.uml.MMUtil;
import org.argouml.uml.diagram.UMLMutableGraphSupport;

import java.util.*;
import java.beans.*;

import org.tigris.gef.graph.*;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Mode;
import org.tigris.gef.base.ModeManager;


import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.model_management.*;

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

    /** get the homemodel. */
  public MNamespace getNamespace() { return _model; }

    /** set the homemodel. */
  public void setNamespace(MNamespace namespace) {
    _model = namespace;
  }

  ////////////////////////////////////////////////////////////////
  // GraphModel implementation


  /** Return all ports on node or edge */
  public Vector getPorts(Object nodeOrEdge) {
    Vector res = new Vector();  //wasteful!
    if (ModelFacade.isANode(nodeOrEdge)) res.addElement(nodeOrEdge);
    if (ModelFacade.isANodeInstance(nodeOrEdge)) res.addElement(nodeOrEdge);
    if (ModelFacade.isAComponent(nodeOrEdge)) res.addElement(nodeOrEdge);    
    if (ModelFacade.isAComponentInstance(nodeOrEdge)) 
        res.addElement(nodeOrEdge);
    if (ModelFacade.isAClass(nodeOrEdge)) res.addElement(nodeOrEdge);    
    if (ModelFacade.isAInterface(nodeOrEdge)) res.addElement(nodeOrEdge);    
    if (ModelFacade.isAObject(nodeOrEdge)) res.addElement(nodeOrEdge);
    return res;
  }

  /** Return the node or edge that owns the given port */
  public Object getOwner(Object port) {
    return port;
  }


  /** Return all edges going to given port */
  public Vector getInEdges(Object port) {
    Vector res = new Vector(); //wasteful!
    if (ModelFacade.isANode(port)) {
      Collection ends = ModelFacade.getAssociationEnds(port);
      if (ends == null) return res; // empty Vector
      Iterator iter = ends.iterator();
      while (iter.hasNext()) {
	    MAssociationEnd aec = (MAssociationEnd) iter.next();
	    res.add(aec.getAssociation());
      }
    }
    if (ModelFacade.isANodeInstance(port)) {
      MNodeInstance noi = (MNodeInstance) port;
      Collection ends = noi.getLinkEnds();
	    res.addAll(ends);
    }
    if (ModelFacade.isAComponent(port)) {
      Collection ends = ModelFacade.getAssociationEnds(port);
      if (ends == null) return res; // empty Vector
      Iterator endEnum = ends.iterator();
      while (endEnum.hasNext()) {
	    MAssociationEnd aec = (MAssociationEnd) endEnum.next();
	    res.addElement(aec.getAssociation());
      }
    }
    if (ModelFacade.isAComponentInstance(port)) {
      MComponentInstance coi = (MComponentInstance) port;
      Collection ends = coi.getLinkEnds();
	    res.addAll(ends);
    }
    if (ModelFacade.isAClass(port)) {
      Collection ends = ModelFacade.getAssociationEnds(port);
      if (ends == null) return res; // empty Vector 
      Iterator endEnum = ends.iterator();
      while (endEnum.hasNext()) {
	MAssociationEnd ae = (MAssociationEnd) endEnum.next();
	res.addElement(ae.getAssociation());
      }
    }
    if (ModelFacade.isAInterface(port)) {
      Collection ends = ModelFacade.getAssociationEnds(port);
      if (ends == null) return res; // empty Vector 
      Iterator endEnum = ends.iterator();
      while (endEnum.hasNext()) {
	    MAssociationEnd ae = (MAssociationEnd) endEnum.next();
	    res.addElement(ae.getAssociation());
	  }
    }
    if (ModelFacade.isAObject(port)) {
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
    if (ModelFacade.isARelationship(edge)) {
        return CoreHelper.getHelper().getSource((MRelationship)edge);
    } else
    if (ModelFacade.isALink(edge)) {
       return CommonBehaviorHelper.getHelper().getSource((MLink)edge);
    }
    
    cat.debug("TODO getSourcePort");

    return null;
  }


  /** Return  the other end of an edge */
  public Object getDestPort(Object edge) {
    if (ModelFacade.isARelationship(edge)) {
        return CoreHelper.getHelper().getDestination((MRelationship)edge);
    } else
    if (ModelFacade.isALink(edge)) {
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
    return (ModelFacade.isANode(node)) || 
        (ModelFacade.isAComponent(node)) || 
        (ModelFacade.isAClass(node)) || 
        (ModelFacade.isAInterface(node)) ||
        (ModelFacade.isAObject(node)) ||
        (ModelFacade.isANodeInstance(node)) || 
        (ModelFacade.isAComponentInstance(node));
  }

  /** Return true if the given object is a valid edge in this graph */
  public boolean canAddEdge(Object edge)  {
    if (edge == null) return false;
if(_edges.contains(edge)) return false;
    Object end0 = null, end1 = null;
    if (ModelFacade.isARelationship(edge)) {
        end0 = CoreHelper.getHelper().getSource((MRelationship)edge);
        end1 = CoreHelper.getHelper().getDestination((MRelationship)edge);
    }
    else if (ModelFacade.isALink(edge)) {
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
	if (ModelFacade.isAModelElement(node) &&
		(ModelFacade.getNamespace(node) == null)) {
		((MNamespace)_model).addOwnedElement((MModelElement) node);
	}
    fireNodeAdded(node);
  }

  /** Add the given edge to the graph, if valid. */
  public void addEdge(Object edge) {
    cat.debug("adding class edge!!!!!!");
    if (!canAddEdge(edge)) return;
    _edges.addElement(edge);
    // TODO: assumes public
      if (ModelFacade.isAModelElement(edge)) {
          ((MNamespace)_model).addOwnedElement((MModelElement) edge);
      }
    fireEdgeAdded(edge);
  }

  public void addNodeRelatedEdges(Object node) {
    if (ModelFacade.isAClassifier(node) ) {
      Collection ends = ModelFacade.getAssociationEnds(node);
      Iterator iter = ends.iterator();
      while (iter.hasNext()) {
         MAssociationEnd ae = (MAssociationEnd) iter.next();
         if(canAddEdge(ae.getAssociation()))
           addEdge(ae.getAssociation());
           return;
      }
    }
    if ( ModelFacade.isAInstance(node) ) {
      Collection ends = ((MInstance)node).getLinkEnds();
      Iterator iter = ends.iterator();
      while (iter.hasNext()) {
         MLinkEnd ae = (MLinkEnd) iter.next();
         if(canAddEdge(ae.getLink()))
           addEdge(ae.getLink());
           return;
      }
    }
    if ( ModelFacade.isAGeneralizableElement(node) ) {
      Iterator iter = ModelFacade.getGeneralizations(node);
      while (iter.hasNext()) {
          // g contains a Generalization
         Object g = iter.next();
         if(canAddEdge(g))
           addEdge(g);
           return;
      }
      iter = ModelFacade.getSpecializations(node);
      while (iter.hasNext()) {
          // s contains a specialization
         Object s = iter.next();
         if(canAddEdge(s))
           addEdge(s);
           return;
      }
    }
    if ( ModelFacade.isAModelElement(node) ) {
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


  public void vetoableChange(PropertyChangeEvent pce) {
    if ("ownedElement".equals(pce.getPropertyName())) {
      Vector oldOwned = (Vector) pce.getOldValue();
      MElementImport eo = (MElementImport) pce.getNewValue();
      MModelElement me = eo.getModelElement();
      if (oldOwned.contains(eo)) {
	    cat.debug("model removed " + me);
	    if (ModelFacade.isANode(me)) removeNode(me);
	    if (ModelFacade.isANodeInstance(me)) removeNode(me);
	    if (ModelFacade.isAComponent(me)) removeNode(me);
	    if (ModelFacade.isAComponentInstance(me)) removeNode(me);
	    if (ModelFacade.isAClass(me)) removeNode(me);
	    if (ModelFacade.isAInterface(me)) removeNode(me);
	    if (ModelFacade.isAObject(me)) removeNode(me);
	    if (ModelFacade.isAAssociation(me)) removeEdge(me);
	    if (ModelFacade.isADependency(me)) removeEdge(me);
        if (ModelFacade.isALink(me)) removeEdge(me);
      }
      else {
	    cat.debug("model added " + me);
      }
    }
  }

  static final long serialVersionUID = 1003748292917485298L;

} /* end class DeploymentDiagramGraphModel */
