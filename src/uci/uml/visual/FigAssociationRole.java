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
import uci.uml.Behavioral_Elements.Collaborations.*;
import uci.uml.generate.*;

public class FigAssociationRole extends FigEdgeModelElement {

  // needs-more-work: should be part of some preferences object
  public static boolean SUPPRESS_BIDIRECTIONAL_ARROWS = true;

  protected FigText _srcMult, _srcRole;
  protected FigText _destMult, _destRole;

  public FigAssociationRole() {
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

  public FigAssociationRole(Object edge) {
    this();
    setOwner(edge);
  }



  public void dispose() {
    AssociationRole asc = (AssociationRole) getOwner();
    if (asc == null) return;
    Vector conns = asc.getAssociationEndRole();
    for (int i = 0; i < conns.size(); i++) {
      try { ((AssociationEndRole)conns.elementAt(i)).setType(null); }
      catch (PropertyVetoException pve) { }
    }
    super.dispose();
  }

  protected void textEdited(FigText ft) throws PropertyVetoException {
    AssociationRole asc = (AssociationRole) getOwner();
    super.textEdited(ft);

    Vector conn = asc.getAssociationEndRole();
    if (conn == null || conn.size() == 0) return;

    if (ft == _srcRole) {
      AssociationEndRole srcAE = (AssociationEndRole) conn.elementAt(0);
      srcAE.setName(new Name(_srcRole.getText()));
    }
    if (ft == _destRole) {
      AssociationEndRole destAE = (AssociationEndRole) conn.elementAt(1);
      destAE.setName(new Name(_destRole.getText()));
    }
  }

  protected void modelChanged() {
    AssociationRole as = (AssociationRole) getOwner();
    if (as == null) return;
    String asNameStr = GeneratorDisplay.Generate(as.getName());

    super.modelChanged();

    Vector endRoles = as.getAssociationEndRole();
    if (endRoles == null || endRoles.size() != 2) {
      System.out.println("endRoles=" + endRoles);
    }
    
    AssociationEndRole ae0 = (AssociationEndRole) endRoles.elementAt(0);
    AssociationEndRole ae1 = (AssociationEndRole) endRoles.elementAt(1);

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
// 	    return ArrowHeadGreater.TheInstance;
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

} /* end class FigAssociationRole */

