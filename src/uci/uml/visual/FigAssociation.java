package uci.uml.visual;

import java.awt.*;
import java.beans.*;

import uci.gef.*;
import uci.uml.ui.*;
import uci.uml.Foundation.Core.*;
import uci.uml.generate.*;

public class FigAssociation extends FigEdgeLine
implements VetoableChangeListener {
  FigText _name = new FigText(10,30,90,20, Color.blue, "Times", 10);

  public FigAssociation(Object edge) {
    super();
    setOwner(edge);
    if (edge instanceof ElementImpl)
      ((ElementImpl)edge).addVetoableChangeListener(this);

    ArrowHeadDiamond endArrow = new ArrowHeadDiamond();
    endArrow.setFillColor(Color.white);

    _name.setText("Association Label");

    addPathItem(_name, new PathConvPercent(this, (float).50, 0));

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

