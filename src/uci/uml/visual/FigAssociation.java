package uci.uml.visual;

import java.awt.*;

import uci.gef.*;
import uci.uml.Foundation.Core.*;
import uci.uml.generate.*;

public class FigAssociation extends FigEdgeLine {
  FigText _name = new FigText(10,30,90,20, Color.blue, "Times", 10);

  public FigAssociation(Object edge) {
    super();
    setOwner(edge);

    ArrowHeadDiamond endArrow = new ArrowHeadDiamond();
    endArrow.setFillColor(Color.white);

    _name.setText("Association Label");

    addPathItem(_name, new PathConvPercent(this, (float).50, 0));

    setDestArrowHead(endArrow);
    setBetweenNearestPoints(true);
  }

  
} /* end class FigAssociation */

