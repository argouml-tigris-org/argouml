package uci.uml.visual;

import java.awt.*;

import uci.gef.*;
import uci.uml.ui.*;
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

  public void dispose() {
    if (!(getOwner() instanceof Generalization)) return;
    Generalization gen = (Generalization) getOwner();
    Project p = ProjectBrowser.TheInstance.getProject();
    p.moveToTrash(gen);
    super.dispose();
  }
  

} /* end class FigGeneralization */

