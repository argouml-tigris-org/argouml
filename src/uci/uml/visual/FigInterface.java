// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
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
import java.util.*;
import java.beans.*;
import com.sun.java.swing.*;
import com.sun.java.swing.plaf.metal.MetalLookAndFeel;

import uci.gef.*;
import uci.graph.*;
import uci.argo.kernel.*;
import uci.uml.ui.*;
import uci.uml.generate.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

/** Class to display graphics for a UML Class in a diagram. */

public class FigInterface extends FigNodeModelElement {

  protected FigRect _bigPort;
  protected FigRect _outline; 
  protected FigText _stereo; 
  protected FigText _oper; 

  public FigInterface(GraphModel gm, Object node) {
    super(gm, node);

    if (node instanceof ElementImpl)
      ((ElementImpl)node).addVetoableChangeListener(this);

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
    _oper.setExpandOnly(true);
    _oper.setJustification(FigText.JUSTIFY_LEFT);

    addFig(_bigPort);
    addFig(_outline);
    addFig(_stereo);
    addFig(_name);
    addFig(_oper);

    Object onlyPort = node; //this kngl should be in the GraphModel
    bindPort(onlyPort, _bigPort);
    Rectangle r = getBounds();
    setBounds(r.x, r.y, r.width, r.height);
  }

  /* Override setBounds to keep shapes looking right */
  public void setBounds(int x, int y, int w, int h) {
    if (_name == null) return;
    int leftSide = x;
    int widthP = w;
    int topSide = y;
    int heightP = h;

    Rectangle _outline_pref = _outline.getBounds();
    Rectangle _stereo_pref = _stereo.getBounds();
    Rectangle _name_pref = _name.getBounds();
    Rectangle _oper_pref = _oper.getBounds();

    int total_height = _stereo_pref.height + _name_pref.height + _oper_pref.height;

    widthP = Math.max(widthP, Math.max(_stereo_pref.width, Math.max(_name_pref.width, _oper_pref.width)));
    heightP = Math.max(heightP, total_height);

    int extra_each = (heightP - total_height) / 3;

    _outline.setBounds(leftSide-1, topSide, widthP+2, _stereo_pref.height + _name.getBounds().height + extra_each);
    _stereo.setBounds(leftSide, topSide, widthP, _stereo_pref.height + extra_each);
    _name.setBounds(leftSide, topSide + _stereo.getBounds().height, widthP, _name_pref.height + extra_each);
    _oper.setBounds(leftSide, topSide + _stereo.getBounds().height + _name.getBounds().height, widthP, _oper_pref.height + extra_each);
    _bigPort.setBounds(leftSide+1, topSide+1, widthP-1, heightP-2);

    calcBounds(); //_x = x; _y = y; _w = w; _h = h;
  }

  ////////////////////////////////////////////////////////////////
  // internal methods

  protected void textEdited(FigText ft) throws PropertyVetoException {
    super.textEdited(ft);
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

    if (cls.getIsAbstract()) {
        _name.setFont(ITALIC_LABEL_FONT);
    }
    else {
        _name.setFont(LABEL_FONT);
    }

  }

} /* end class FigInterface */


