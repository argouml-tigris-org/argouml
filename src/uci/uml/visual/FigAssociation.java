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
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.plaf.metal.MetalLookAndFeel;

import uci.gef.*;
import uci.uml.ui.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.generate.*;

public class FigAssociation extends FigEdgeLine
implements VetoableChangeListener, DelayedVetoableChangeListener,
  KeyListener, MouseListener {

  protected FigText _name;
  protected FigText _srcMult, _srcRole;
  protected FigText _destMult, _destRole;

  public FigAssociation(Object edge) {
    super();
    setOwner(edge);

    if (edge instanceof ElementImpl)
      ((ElementImpl)edge).addVetoableChangeListener(this);

    Font labelFont = MetalLookAndFeel.getSubTextFont();
    
    
    _name = new FigText(10, 30, 90, 20);
    _name.setFont(labelFont);
    _name.setTextColor(Color.black);
    _name.setTextFilled(false);
    _name.setFilled(false);
    _name.setLineWidth(0);

    addPathItem(_name, new PathConvPercent(this, 50, 10));

    _srcMult = new FigText(10, 30, 90, 20);
    _srcMult.setFont(labelFont);
    _srcMult.setTextColor(Color.black);
    _srcMult.setTextFilled(false);
    _srcMult.setFilled(false);
    _srcMult.setLineWidth(0);
    addPathItem(_srcMult,
		new PathConvPercentPlusConst(this, 0, 10, 10));

    _srcRole = new FigText(10, 30, 90, 20);
    _srcRole.setFont(labelFont);
    _srcRole.setTextColor(Color.black);
    _srcRole.setTextFilled(false);
    _srcRole.setFilled(false);
    _srcRole.setLineWidth(0);
    addPathItem(_srcRole,
		new PathConvPercentPlusConst(this, 0, 35, -15));

    _destMult = new FigText(10, 30, 90, 20);
    _destMult.setFont(labelFont);
    _destMult.setTextColor(Color.black);
    _destMult.setTextFilled(false);
    _destMult.setFilled(false);
    _destMult.setLineWidth(0);
    addPathItem(_destMult,
		new PathConvPercentPlusConst(this, 100, -10, 10));

    _destRole = new FigText(10, 30, 90, 20);
    _destRole.setFont(labelFont);
    _destRole.setTextColor(Color.black);
    _destRole.setTextFilled(false);
    _destRole.setFilled(false);
    _destRole.setLineWidth(0);
    addPathItem(_destRole,
		new PathConvPercentPlusConst(this, 100, -35, -15));
    setBetweenNearestPoints(true);
    updateText();

    _name.addPropertyChangeListener(this);
    _srcRole.addPropertyChangeListener(this);
    _destRole.addPropertyChangeListener(this);

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
    //System.out.println("FigAssociation got a change notification!");
    Object src = pce.getSource();
    if (src == getOwner()) {
      DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
      SwingUtilities.invokeLater(delayedNotify);
    }
  }

  public void delayedVetoableChange(PropertyChangeEvent pce) {
    // throws PropertyVetoException 
    //System.out.println("FigAssociation got a delayed change notification!");
    Object src = pce.getSource();
    updateText();
  }

  public void propertyChange(PropertyChangeEvent pve) {
    Object src = pve.getSource();
    String pName = pve.getPropertyName();
    if (pName.equals("editing") && Boolean.FALSE.equals(pve.getNewValue())) {
      //System.out.println("finished editing");
      IAssociation asc = (IAssociation) getOwner();
      try {
	if (src == _name) { asc.setName(new Name(_name.getText())); }
	Vector conn = asc.getConnection();
	if (conn == null || conn.size() == 0) return;

	if (src == _srcRole) {
	  AssociationEnd srcAE = (AssociationEnd) conn.elementAt(0);
	  srcAE.setName(new Name(_srcRole.getText()));
	}
	if (src == _destRole) {
	  AssociationEnd destAE = (AssociationEnd) conn.elementAt(1);
	  destAE.setName(new Name(_destRole.getText()));
	}
      }
      catch (PropertyVetoException ex) {
	System.out.println("could not parse and use the text you entered");
      }
    }
    else super.propertyChange(pve);
  }

    
  ////////////////////////////////////////////////////////////////
  // event handlers

  public void mousePressed(MouseEvent me) { }
  public void mouseReleased(MouseEvent me) { }
  public void mouseEntered(MouseEvent me) { }
  public void mouseExited(MouseEvent me) { }

  public void mouseClicked(MouseEvent me) {
    if (me.isConsumed()) return;
    if (me.getClickCount() >= 2) {
      if (_name.contains(me.getX(), me.getY())) _name.mouseClicked(me);
      if (_destRole.contains(me.getX(), me.getY())) _destRole.mouseClicked(me);
      if (_srcRole.contains(me.getX(), me.getY())) _srcRole.mouseClicked(me);
    }
    me.consume();
  }


  public void keyPressed(KeyEvent ke) { }

  public void keyReleased(KeyEvent ke) { }

  public void keyTyped(KeyEvent ke) {
    if (!ke.isConsumed()) {
      _name.keyTyped(ke);
      ke.consume();
      IAssociation asc = (IAssociation) getOwner();
      try {
	asc.setName(new Name(_name.getText()));
      }
      catch (PropertyVetoException pve) {
	System.out.println("could not set association name");
      }
    }
  }


  protected void updateText() {
    Association as = (Association) getOwner();
    String asNameStr = GeneratorDisplay.Generate(as.getName());

    startTrans();
    _name.setText(asNameStr);

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

    setSourceArrowHead(chooseArrowHead(ae0.getAggregation()));
    setDestArrowHead(chooseArrowHead(ae1.getAggregation()));
    
    Rectangle bbox = getBounds();
    setBounds(bbox.x, bbox.y, bbox.width, bbox.height);
    endTrans();
  }

  protected ArrowHead chooseArrowHead(AggregationKind ak) {
    ArrowHead res = new ArrowHeadNone();

    if (AggregationKind.UNSPEC.equals(ak))
      return ArrowHeadNone.TheInstance;
    if (AggregationKind.NONE.equals(ak))
      return ArrowHeadNone.TheInstance;
    if (AggregationKind.AGG.equals(ak))
      return ArrowHeadDiamond.WhiteDiamond;
    if (AggregationKind.COMPOSITE.equals(ak))
      return ArrowHeadDiamond.BlackDiamond;
    System.out.println("unknown case in drawing arrowhead");
    return ArrowHeadNone.TheInstance;
  }

} /* end class FigAssociation */

