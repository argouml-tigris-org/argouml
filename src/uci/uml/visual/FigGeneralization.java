package uci.uml.visual;

import java.awt.*;

import uci.gef.*;
import uci.uml.Foundation.Core.*;

public class FigGeneralization extends FigEdgeRectiline {

  public FigGeneralization(Object edge) {
    super();
    setOwner(edge);
    setDestArrowHead(new ArrowHeadTriangle());
    setBetweenNearestPoints(true);
  }

  
} /* end class FigGeneralization */

