package uci.uml.visual;

import java.awt.*;
import java.beans.*;

import uci.gef.*;
import uci.uml.ui.*;
import uci.uml.Foundation.Core.*;
import uci.uml.generate.*;

public class FigAssociation extends FigEdgeLine
implements VetoableChangeListener {

  public FigAssociation(Object edge) {
    super();
    setOwner(edge);
    if (edge instanceof ElementImpl)
      ((ElementImpl)edge).addVetoableChangeListener(this);

    ArrowHeadDiamond endArrow = new ArrowHeadDiamond();
    endArrow.setFillColor(Color.white);

    FigText _dummy_text = new FigText(10,30,90,20, Color.blue, "Times", 10);
    _dummy_text.setText("Association Label");

    addPathItem(_dummy_text, new PathConvPercent(this, (float).50, 0));

    setDestArrowHead(endArrow);
    setBetweenNearestPoints(true);
  }


  public void dispose() {
    if (!(getOwner() instanceof Association)) return;
    Association asc = (Association) getOwner();
    Project p = ProjectBrowser.TheInstance.getProject();
    p.moveToTrash(asc);
    super.dispose();
  }
  

  public void vetoableChange(PropertyChangeEvent evt) {
    // throws PropertyVetoException 
    System.out.println("FigAssociation got a change notification!");
  }

  
} /* end class FigAssociation */

