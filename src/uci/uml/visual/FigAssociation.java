package uci.uml.visual;

import java.awt.*;

import uci.gef.*;
import uci.uml.Foundation.Core.*;

public class FigAssociation extends FigEdgeLine {

  public FigAssociation(Object edge) {
    super();
    setOwner(edge);

    ArrowHeadDiamond endArrow = new ArrowHeadDiamond();
    endArrow.setFillColor(Color.white);

    setDestArrowHead(endArrow);
    setBetweenNearestPoints(true);
  }

  
} /* end class FigAssociation */

