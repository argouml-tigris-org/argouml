package uci.uml.visual;

import java.util.*;

import uci.graph.*;
import uci.gef.*;
import uci.uml.Foundation.Core.*;

// could be singleton

public class ClassDiagramRenderer
implements GraphNodeRenderer, GraphEdgeRenderer {

  /** Return a Fig that can be used to represent the given node */
  public FigNode getFigNodeFor(GraphModel gm, Layer lay, Object node) {
    if (isUMLClass(node)) return new FigClass(gm, node);
    else if (isUMLInterface(node)) return new FigClass(gm, node);
    System.out.println("needs-more-work ClassDiagramRenderer getFigNodeFor");
    return null;
  }

  /** Return a Fig that can be used to represent the given edge */
  public FigEdge getFigEdgeFor(GraphModel gm, Layer lay, Object edge) {
    System.out.println("making figedge for " + edge);
    if (isUMLAssoc(edge)) {
      Association asc = (Association) edge;
      FigAssociation ascFig = new FigAssociation(asc);
      Vector connections = asc.getConnection();
      if (connections == null) System.out.println("oh boy....");
      AssociationEnd fromEnd = (AssociationEnd) connections.elementAt(0);
      Classifier fromCls = (Classifier) fromEnd.getType();
      AssociationEnd toEnd = (AssociationEnd) connections.elementAt(1);
      Classifier toCls = (Classifier) toEnd.getType();
      FigNode fromFN = (FigNode) lay.presentationFor(fromCls);
      FigNode toFN = (FigNode) lay.presentationFor(toCls);
      ascFig.sourcePortFig(fromFN);
      ascFig.sourceFigNode(fromFN);
      ascFig.destPortFig(toFN);
      ascFig.destFigNode(toFN);      
      return ascFig;
    }
    if (isUMLGeneralization(edge)) {
      Generalization gen = (Generalization) edge;
      FigGeneralization genFig = new FigGeneralization(gen);
      GeneralizableElement subType = gen.getSubtype();
      GeneralizableElement superType = gen.getSupertype();
      FigNode subTypeFN = (FigNode) lay.presentationFor(subType);
      FigNode superTypeFN = (FigNode) lay.presentationFor(superType);
      genFig.sourcePortFig(subTypeFN);
      genFig.sourceFigNode(subTypeFN);
      genFig.destPortFig(superTypeFN);
      genFig.destFigNode(superTypeFN);
      return genFig;
    }
    // what about realizations? They are not distince objects in my UML model
    // maybe they should be, just as an implementation issue, dont
    // remove any of the methods that are there now.
    
    System.out.println("needs-more-work ClassDiagramRenderer getFigEdgeFor");
    return null;
  }

  protected boolean isUMLClass(Object o) {
    return (o instanceof uci.uml.Foundation.Core.Class);
  }

  protected boolean isUMLInterface(Object o) {
    return (o instanceof uci.uml.Foundation.Core.Interface);
  }

  protected boolean isUMLAssoc(Object o) {
    return (o instanceof uci.uml.Foundation.Core.Association);
  }

  protected boolean isUMLGeneralization(Object o) {
    return (o instanceof uci.uml.Foundation.Core.Generalization);
  }

  
  
} /* end class ClassDiagramRenderer */
