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
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import uci.gef.*;
import uci.graph.*;
import uci.argo.kernel.*;
import uci.uml.ui.*;
import uci.uml.generate.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

/** Class to display graphics for a UML Class in a diagram. */

public class FigInterface extends FigNodeModelElement {

  ////////////////////////////////////////////////////////////////
  // constants
  public static int OVERLAP = 4;

  ////////////////////////////////////////////////////////////////
  // instance variables
  protected FigRect _bigPort;
  protected FigRect _outline;
  protected FigText _stereo;
  protected FigText _oper;

  ////////////////////////////////////////////////////////////////
  // constructors

  public FigInterface() {

//     if (node instanceof ElementImpl)
//       ((ElementImpl)node).addVetoableChangeListener(this);

    _bigPort = new FigRect(8, 8, 92, 64, Color.cyan, Color.cyan);

    _outline = new FigRect(8,8,92,30, Color.black, Color.white);

    _stereo = new FigText(10,10,92,15,Color.black, "Times", 10);
    _stereo.setExpandOnly(true);
    _stereo.setFilled(false);
    _stereo.setLineWidth(0);
    _stereo.setEditable(false);
    _stereo.setText("<<Interface>>");
    _stereo.setHeight(15);

    _name.setHeight(18);
    _name.setY(23);
    _name.setWidth(92);
    _name.setLineWidth(0);
    _name.setFilled(false);

    _oper = new FigText(10,40,92,33,Color.black, "Times", 10);
    _oper.setFont(LABEL_FONT);
    _oper.setExpandOnly(true);
    _oper.setJustification(FigText.JUSTIFY_LEFT);

    addFig(_bigPort);
    addFig(_outline);
    addFig(_stereo);
    addFig(_name);
    addFig(_oper);
    Rectangle r = getBounds();
    setBounds(r.x, r.y, r.width, r.height);
  }

  public FigInterface(GraphModel gm, Object node) {
    this();
    setOwner(node);
  }

  public String placeString() { return "new Interface"; }

  public Object clone() {
    FigInterface figClone = (FigInterface) super.clone();
    Vector v = figClone.getFigs();
    figClone._bigPort = (FigRect) v.elementAt(0);
    figClone._outline = (FigRect) v.elementAt(1);
    figClone._stereo = (FigText) v.elementAt(2);
    figClone._name = (FigText) v.elementAt(3);
    figClone._oper = (FigText) v.elementAt(4);
    return figClone;
  }

  ////////////////////////////////////////////////////////////////
  // Fig implemetation

  public Selection makeSelection() {
    return new SelectionInterface(this);
  }

  public void setOwner(Object node) {
    super.setOwner(node);
    bindPort(node, _bigPort);
  }

  public Dimension getMinimumSize() {
    Dimension stereoMin = _stereo.getMinimumSize();
    Dimension nameMin = _name.getMinimumSize();
    Dimension operMin = _oper.getMinimumSize();

    int h = stereoMin.height + nameMin.height - OVERLAP + operMin.height;
    int w = Math.max(stereoMin.width, Math.max(nameMin.width, operMin.width));
    return new Dimension(w, h);
  }

  public void setLineColor(Color c) {
    super.setLineColor(c);
    _stereo.setLineWidth(0);
    _name.setLineWidth(0);
  }

  public void setLineWidth(int w) {
    super.setLineWidth(w);
    _stereo.setLineWidth(0);
    _name.setLineWidth(0);
  }

  /* Override setBounds to keep shapes looking right */
  public void setBounds(int x, int y, int w, int h) {
    if (_name == null) return;

    Rectangle oldBounds = getBounds();
    Dimension stereoMin = _stereo.getMinimumSize();
    Dimension nameMin = _name.getMinimumSize();
    Dimension operMin = _oper.getMinimumSize();

    _outline.setBounds(x, y, w,
		       stereoMin.height + nameMin.height - OVERLAP);
    _stereo.setBounds(x+1, y+1, w-2, stereoMin.height);
    _name.setBounds(x+1, y + stereoMin.height - OVERLAP + 1, w-2, nameMin.height);
    _oper.setBounds(x, y + _outline.getBounds().height-1,
		    w, h - _outline.getBounds().height+1);
    _bigPort.setBounds(x+1, y+1, w-2, h-2);

    calcBounds(); //_x = x; _y = y; _w = w; _h = h;
    updateEdges();
    firePropChange("bounds", oldBounds, getBounds());
  }

  ////////////////////////////////////////////////////////////////
  // internal methods

  protected void textEdited(FigText ft) throws PropertyVetoException {
    super.textEdited(ft);
    Classifier cls = (Classifier) getOwner();
    if (cls == null) return;
    if (ft == _oper) {
      String s = ft.getText();
      ParserDisplay.SINGLETON.parseOperationCompartment(cls, s);
    }
  }

  protected void modelChanged() {
    super.modelChanged();
    Classifier cls = (Classifier) getOwner();
    if (cls == null) return;
    Vector behs = cls.getBehavioralFeature();
    String operStr = "";
    if (behs != null) {
      java.util.Enumeration enum = behs.elements();
      while (enum.hasMoreElements()) {
	BehavioralFeature bf = (BehavioralFeature) enum.nextElement();
	operStr += GeneratorDisplay.Generate(bf);
	if (enum.hasMoreElements())
	  operStr += "\n";
      }
    }

    _oper.setText(operStr);
  }

  static final long serialVersionUID = 4928213949795787107L;

} /* end class FigInterface */


