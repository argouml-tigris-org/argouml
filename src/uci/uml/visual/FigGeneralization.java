package uci.uml.visual;

import java.awt.*;

import uci.gef.*;
import uci.uml.Foundation.Core.*;

public class FigGeneralization extends FigEdgeLine {

  public FigGeneralization(Object edge) {
    super();
    setOwner(edge);

    ArrowHeadTriangle endArrow = new ArrowHeadTriangle();
    endArrow.setFillColor(Color.white);

    setDestArrowHead(endArrow);
    setBetweenNearestPoints(true);
  }
  
} /* end class FigGeneralization */

