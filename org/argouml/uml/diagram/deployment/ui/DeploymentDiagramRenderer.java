package org.argouml.uml.diagram.deployment.ui;

import java.util.*;
import java.util.Enumeration;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.behavior.common_behavior.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;

import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.static_structure.ui.*;

public class DeploymentDiagramRenderer
implements GraphNodeRenderer, GraphEdgeRenderer {

  /** Return a Fig that can be used to represent the given node */

  public FigNode getFigNodeFor(GraphModel gm, Layer lay, Object node) {
    if (node instanceof MNode) return new FigMNode(gm, node);
    else if (node instanceof MNodeInstance) return new FigMNodeInstance(gm, node);
    else if (node instanceof MComponent) return new FigComponent(gm, node); 
    else if (node instanceof MComponentInstance) return new FigComponentInstance(gm, node);
    else if (node instanceof MClass) return new FigClass(gm, node); 
    else if (node instanceof MInterface) return new FigInterface(gm, node); 
    else if (node instanceof MObject) return new FigObject(gm, node);
    System.out.println("needs-more-work DeploymentDiagramRenderer getFigNodeFor");
    return null;
  }

  /** Return a Fig that can be used to represent the given edge */
  public FigEdge getFigEdgeFor(GraphModel gm, Layer lay, Object edge) {

    if (edge instanceof MAssociation) {
      MAssociation asc = (MAssociation) edge;
      FigAssociation ascFig = new FigAssociation(asc, lay);
      /*
      Collection connections = asc.getConnections();
      if (connections == null) System.out.println("null connections....");
      
	  Object[] connArray = connections.toArray();
      MAssociationEnd fromEnd = (MAssociationEnd) connArray[0];
      MClassifier fromCls = (MClassifier) fromEnd.getType();
      MAssociationEnd toEnd = (MAssociationEnd) connArray[1];
      MClassifier toCls = (MClassifier) toEnd.getType();
      FigNode fromFN = (FigNode) lay.presentationFor(fromCls);
      FigNode toFN = (FigNode) lay.presentationFor(toCls);
      ascFig.setSourcePortFig(fromFN);
      ascFig.setSourceFigNode(fromFN);
      ascFig.setDestPortFig(toFN);
      ascFig.setDestFigNode(toFN);
      */
      return ascFig;
    }
    if (edge instanceof MLink) {
      MLink lnk = (MLink) edge;
      FigLink lnkFig = new FigLink(lnk);
      Collection linkEnds = lnk.getConnections();
      if (linkEnds == null) System.out.println("null linkRoles....");
	  Object[] leArray = linkEnds.toArray();
      MLinkEnd fromEnd = (MLinkEnd) leArray[0];
      MInstance fromInst = fromEnd.getInstance();
      MLinkEnd toEnd = (MLinkEnd) leArray[1];
      MInstance toInst = toEnd.getInstance();
      FigNode fromFN = (FigNode) lay.presentationFor(fromInst);
      FigNode toFN = (FigNode) lay.presentationFor(toInst);
      lnkFig.setSourcePortFig(fromFN);
      lnkFig.setSourceFigNode(fromFN);
      lnkFig.setDestPortFig(toFN);
      lnkFig.setDestFigNode(toFN);
      return lnkFig;
    }
    if (edge instanceof MDependency) {
      MDependency dep = (MDependency) edge;
      FigDependency depFig = new FigDependency(dep);

      MModelElement supplier = (MModelElement)(((dep.getSuppliers().toArray())[0]));
      MModelElement client = (MModelElement)(((dep.getClients().toArray())[0]));

      FigNode supFN = (FigNode) lay.presentationFor(supplier);
      FigNode cliFN = (FigNode) lay.presentationFor(client);

      depFig.setSourcePortFig(cliFN);
      depFig.setSourceFigNode(cliFN);
      depFig.setDestPortFig(supFN);
      depFig.setDestFigNode(supFN);
      depFig.getFig().setDashed(true);
      return depFig;
    }

    return null;
  }

  static final long serialVersionUID = 8002278834226522224L;

}
