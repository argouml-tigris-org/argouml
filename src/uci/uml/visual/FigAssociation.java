// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.


package uci.uml.visual;

import java.awt.*;
import java.beans.*;
import com.sun.java.swing.*;

import uci.gef.*;
import uci.uml.ui.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.generate.*;

public class FigAssociation extends FigEdgeLine
implements VetoableChangeListener, DelayedVetoableChangeListener {

    FigText _name;
    FigText _source_end;
    FigText _dest_end;

  public FigAssociation(Object edge) {
    super();
    setOwner(edge);

    if (edge instanceof ElementImpl)
      ((ElementImpl)edge).addVetoableChangeListener(this);

    _name = new FigText(10,30,90,20, Color.black, "Dialog", 9);
    _name.setTextFilled(false);
    _name.setFilled(false);
    _name.setLineWidth(0);

    addPathItem(_name, new PathConvPercent(this, (float).50, 0));

    _source_end = new FigText(10,30,90,20, Color.black, "Dialog", 9);
    _source_end.setTextFilled(false);
    _source_end.setFilled(false);
    _source_end.setLineWidth(0);
    addPathItem(_source_end, new PathConvPercent(this, (float).20, 0));

    _dest_end = new FigText(10,30,90,20, Color.black, "dialog", 9);
    _dest_end.setTextFilled(false);
    _dest_end.setFilled(false);
    _dest_end.setLineWidth(0);
    addPathItem(_dest_end, new PathConvPercent(this, (float).80, 0));

    setBetweenNearestPoints(true);
  }


  public void dispose() {
    if (!(getOwner() instanceof Association)) return;
    Association asc = (Association) getOwner();
    Project p = ProjectBrowser.TheInstance.getProject();
    p.moveToTrash(asc);
    super.dispose();
  }
  

  public void vetoableChange(PropertyChangeEvent pce) {
    // throws PropertyVetoException 
    System.out.println("FigAssociation got a change notification!");
    Object src = pce.getSource();
    if (src == getOwner()) {
      DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
      SwingUtilities.invokeLater(delayedNotify);
    }
  }

  public void delayedVetoableChange(PropertyChangeEvent pce) {
    // throws PropertyVetoException 
    System.out.println("FigAssociation got a delayed change notification!");
    Object src = pce.getSource();
    if (src == getOwner()) updateText();
  }

  protected void updateText() {
    Association as = (Association) getOwner();
    String asNameStr = GeneratorDisplay.Generate(as.getName());

    startTrans();
    _name.setText(asNameStr);

    AssociationEnd _end_1 = ((AssociationEnd)(as.getConnection().elementAt(0)));
    AssociationEnd _end_2 = ((AssociationEnd)(as.getConnection().elementAt(1)));

    _source_end.setText(GeneratorDisplay.Generate(_end_1.getMultiplicity()));
    _dest_end.setText(GeneratorDisplay.Generate(_end_2.getMultiplicity()));

    AggregationKind agg_kind_1 = _end_1.getAggregation();
    AggregationKind agg_kind_2 = _end_2.getAggregation();

    updateArrowHead("source", agg_kind_1);
    updateArrowHead("dest", agg_kind_2);

    Rectangle bbox = getBounds();
    setBounds(bbox.x, bbox.y, bbox.width, bbox.height);
    endTrans();
  }

  protected void updateArrowHead(String which, AggregationKind agg_kind)
  {
    ArrowHead newArrowhead = new ArrowHeadNone();

    if (agg_kind.equals(AggregationKind.UNSPEC)) {
      newArrowhead = new ArrowHeadNone(); }
    if (agg_kind.equals(AggregationKind.NONE)) {
      newArrowhead = new ArrowHeadNone(); }
    if (agg_kind.equals(AggregationKind.AGG)) {
      ArrowHeadDiamond temp = new ArrowHeadDiamond();
      temp.setFillColor(Color.white);
      newArrowhead = temp; }
    if (agg_kind.equals(AggregationKind.COMPOSITE)) {
      ArrowHeadDiamond temp = new ArrowHeadDiamond();
      temp.setFillColor(Color.black);
      newArrowhead = temp; }

    if (which.equals("source")) {
      setSourceArrowHead(newArrowhead); }
    else if (which.equals("dest")) {
      setDestArrowHead(newArrowhead); }
  }

} /* end class FigAssociation */

