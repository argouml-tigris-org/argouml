// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.




package uci.uml.visual;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;

import uci.gef.*;
import uci.uml.ui.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.generate.*;

public class FigAssociation extends FigEdgeModelElement {

  // needs-more-work: should be part of some preferences object
  public static boolean SUPPRESS_BIDIRECTIONAL_ARROWS = true;

  protected FigText _srcMult, _srcRole;
  protected FigText _destMult, _destRole;

  ////////////////////////////////////////////////////////////////
  // constructors

  public FigAssociation() {
    addPathItem(_name, new PathConvPercent(this, 50, 10));

    _srcMult = new FigText(10, 30, 90, 20);
    _srcMult.setFont(LABEL_FONT);
    _srcMult.setTextColor(Color.black);
    _srcMult.setTextFilled(false);
    _srcMult.setFilled(false);
    _srcMult.setLineWidth(0);
    addPathItem(_srcMult, new PathConvPercentPlusConst(this, 0, 15, 15));

    _srcRole = new FigText(10, 30, 90, 20);
    _srcRole.setFont(LABEL_FONT);
    _srcRole.setTextColor(Color.black);
    _srcRole.setTextFilled(false);
    _srcRole.setFilled(false);
    _srcRole.setLineWidth(0);
    addPathItem(_srcRole, new PathConvPercentPlusConst(this, 0, 35, -15));

    _destMult = new FigText(10, 30, 90, 20);
    _destMult.setFont(LABEL_FONT);
    _destMult.setTextColor(Color.black);
    _destMult.setTextFilled(false);
    _destMult.setFilled(false);
    _destMult.setLineWidth(0);
    addPathItem(_destMult, new PathConvPercentPlusConst(this, 100, -15, 15));

    _destRole = new FigText(10, 30, 90, 20);
    _destRole.setFont(LABEL_FONT);
    _destRole.setTextColor(Color.black);
    _destRole.setTextFilled(false);
    _destRole.setFilled(false);
    _destRole.setLineWidth(0);
    addPathItem(_destRole, new PathConvPercentPlusConst(this, 100, -35, -15));
    setBetweenNearestPoints(true);
  }

  public FigAssociation(Object edge) {
    this();
    setOwner(edge);
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  protected void textEdited(FigText ft) throws PropertyVetoException {
    IAssociation asc = (IAssociation) getOwner();
    if (asc == null) return;
    super.textEdited(ft);

    Vector conn = asc.getConnection();
    if (conn == null || conn.size() == 0) return;

    if (ft == _srcRole) {
      AssociationEnd srcAE = (AssociationEnd) conn.elementAt(0);
      srcAE.setName(new Name(_srcRole.getText()));
    }
    if (ft == _destRole) {
      AssociationEnd destAE = (AssociationEnd) conn.elementAt(1);
      destAE.setName(new Name(_destRole.getText()));
    }
    // needs-more-work: parse multiplicities
  }


  protected void modelChanged() {
    Association as = (Association) getOwner();
    if (as == null) return;
    String asNameStr = GeneratorDisplay.Generate(as.getName());

    super.modelChanged();

    AssociationEnd ae0 = ((AssociationEnd)(as.getConnection().elementAt(0)));
    AssociationEnd ae1 = ((AssociationEnd)(as.getConnection().elementAt(1)));

    Multiplicity mult0 = ae0.getMultiplicity();
    Multiplicity mult1 = ae1.getMultiplicity();
    _srcMult.setText(GeneratorDisplay.Generate(mult0));
    if (Multiplicity.ONE.equals(mult0)) _srcMult.setText("");
    _destMult.setText(GeneratorDisplay.Generate(mult1));
    if (Multiplicity.ONE.equals(mult1)) _destMult.setText("");

    _srcRole.setText(GeneratorDisplay.Generate(ae0.getName()));
    _destRole.setText(GeneratorDisplay.Generate(ae1.getName()));

    boolean srcNav = ae0.getIsNavigable();
    boolean destNav = ae1.getIsNavigable();
    if (srcNav && destNav && SUPPRESS_BIDIRECTIONAL_ARROWS)
      srcNav = destNav = false;
    setSourceArrowHead(chooseArrowHead(ae0.getAggregation(), srcNav));
    setDestArrowHead(chooseArrowHead(ae1.getAggregation(), destNav));
  }


  static ArrowHead _NAV_AGG =
  new ArrowHeadComposite(ArrowHeadDiamond.WhiteDiamond,
			 ArrowHeadGreater.TheInstance);

  static ArrowHead _NAV_COMP =
  new ArrowHeadComposite(ArrowHeadDiamond.BlackDiamond,
			 ArrowHeadGreater.TheInstance);

  protected ArrowHead chooseArrowHead(AggregationKind ak, boolean nav) {
    if (nav) {
//       if (AggregationKind.UNSPEC.equals(ak))
// 	return ArrowHeadGreater.TheInstance;
      if (AggregationKind.NONE.equals(ak))
	return ArrowHeadGreater.TheInstance;
      else if (AggregationKind.AGG.equals(ak))
	return _NAV_AGG;
      else if (AggregationKind.COMPOSITE.equals(ak))
	return _NAV_COMP;
    }
//     if (AggregationKind.UNSPEC.equals(ak))
//       return ArrowHeadNone.TheInstance;
    if (AggregationKind.NONE.equals(ak))
      return ArrowHeadNone.TheInstance;
    else if (AggregationKind.AGG.equals(ak))
      return ArrowHeadDiamond.WhiteDiamond;
    else if (AggregationKind.COMPOSITE.equals(ak))
      return ArrowHeadDiamond.BlackDiamond;
    System.out.println("unknown case in drawing assoc arrowhead");
    return ArrowHeadNone.TheInstance;
  }

  public Vector getPopUpActions(MouseEvent me) {
    Vector popUpActions = super.getPopUpActions(me);
    // x^2 + y^2 = r^2  (equation of a circle)
    Point firstPoint = this.getFirstPoint();
    Point lastPoint = this.getLastPoint();
    int length = getPerimeterLength();

    int rSquared = (int) (.3 * length);

    // max distance is set at 100 pixels, (rSquared = 100^2)
    if( rSquared > 100 )
      rSquared = 10000;
    else
      rSquared *= rSquared;

    int srcDeterminingFactor = getSquaredDistance(me.getPoint(), firstPoint);
    int destDeterminingFactor = getSquaredDistance(me.getPoint(), lastPoint);

    if (srcDeterminingFactor < rSquared &&
	srcDeterminingFactor < destDeterminingFactor) {
      JMenu multMenu = new JMenu("Multiplicity");
      multMenu.add(Actions.SrcMultOne);
      multMenu.add(Actions.SrcMultZeroToOne);
      multMenu.add(Actions.SrcMultOneToMany);
      multMenu.add(Actions.SrcMultZeroToMany);
      popUpActions.insertElementAt(multMenu, popUpActions.size() - 1);

      JMenu aggMenu = new JMenu("Aggregation");
      aggMenu.add(Actions.SrcAggNone);
      aggMenu.add(Actions.SrcAgg);
      aggMenu.add(Actions.SrcAggComposite);
      popUpActions.insertElementAt(aggMenu, popUpActions.size() - 1);
    }
    else if (destDeterminingFactor < rSquared) {
      JMenu multMenu = new JMenu("Multiplicity");
      multMenu.add(Actions.DestMultOne);
      multMenu.add(Actions.DestMultZeroToOne);
      multMenu.add(Actions.DestMultOneToMany);
      multMenu.add(Actions.DestMultZeroToMany);
      popUpActions.insertElementAt(multMenu, popUpActions.size() - 1);

      JMenu aggMenu = new JMenu("Aggregation");
      aggMenu.add(Actions.DestAggNone);
      aggMenu.add(Actions.DestAgg);
      aggMenu.add(Actions.DestAggComposite);
      popUpActions.insertElementAt(aggMenu, popUpActions.size() - 1);
    }
    else { }

    return popUpActions;
  }

  static final long serialVersionUID = 9100125695919853919L;

} /* end class FigAssociation */

