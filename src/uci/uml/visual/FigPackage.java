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



// File: FigPackage.java
// Classes: FigPackage
// Original Author: your email address here
// $Id$

package uci.uml.visual;

import java.awt.*;
import java.util.*;
import java.beans.*;
import com.sun.java.swing.*;

import uci.gef.*;
import uci.graph.*;
import uci.uml.ui.*;
import uci.uml.generate.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Model_Management.*;

/** Class to display graphics for a UML State in a diagram. */

public class FigPackage extends FigNode
implements VetoableChangeListener, DelayedVetoableChangeListener {

  ////////////////////////////////////////////////////////////////
  // constants
  
  public final int MARGIN = 2;
  public int x = 10;
  public int y = 10;
  public int width = 150;
  public int height = 60;
  public int indentX = 20;
  public int indentY = 20;
  public int textH =15;
  public Point pos;
  public Dimension dim;
  protected int _radius = 20;

  ////////////////////////////////////////////////////////////////
  // instance variables
  
  FigText _name;
  FigText _dashRect;
  
  /** UML does not really use ports, so just define one big one so
   *  that users can drag edges to or from any point in the icon. */
  
  FigRect _bigPort;
  
  // add other Figs here aes needed

  FigRRect _rRect;
  FigRect _firstRect;

  ////////////////////////////////////////////////////////////////
  // constructors
  
  public FigPackage(GraphModel gm, Object node) {
    super(node);
    // if it is a UML meta-model object, register interest in any change events
    if (node instanceof ElementImpl)
      ((ElementImpl)node).addVetoableChangeListener(this);

    Color handleColor = Globals.getPrefs().getHandleColor();
    _bigPort = new FigRect(x,y,width,height, handleColor, Color.lightGray);
    _rRect = new FigRRect(x,y,width,height,null, null);
    _dashRect = new FigText(x , y, width -indentX,textH);
    _name = new FigText(x,y+textH,width,height-textH);
    //_name = new FigText(10,10,90,20, Color.blue, "Times", 10);
    _name.setExpandOnly(true);
    _name.setTextFilled(true);
    _name.setText("FigPackage");
    
    // initialize any other Figs here
    //_clss.setExpandOnly(true);
    //_clss.setTextFilled(true);
    //_clss.setText("FigPackage");

    // add Figs to the FigNode in back-to-front order
    addFig(_bigPort);
    addFig(_name);
    //addFig(_clss);
    addFig(_dashRect);


    Object onlyPort = node;
    bindPort(onlyPort, _bigPort);
    setBlinkPorts(false); //make port invisble unless mouse enters
    Rectangle r = getBounds();
  }


  /** If the UML meta-model object changes state. Update the Fig.  But
   *  we need to do it as a "DelayedVetoableChangeListener", so that
   *  model changes complete before we update the screen. */
  public void vetoableChange(PropertyChangeEvent pce) {
    // throws PropertyVetoException 
    System.out.println("FigPackage got a change notification!");
    Object src = pce.getSource();
    if (src == getOwner()) {
      DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
      SwingUtilities.invokeLater(delayedNotify);
    }
  }

  /** The UML meta-model object changed. Now update the Fig to show
   *  its current  state. */
  public void delayedVetoableChange(PropertyChangeEvent pce) {
    // throws PropertyVetoException 
    System.out.println("FigPackage got a delayed change notification!");
    Object src = pce.getSource();
    if (src == getOwner()) {
      updateText();
      // you may have to update more than just the text
    }
  }


  /** Update the text labels */
  protected void updateText() {
    ModelElement elmt = (ModelElement) getOwner();
    //Vector strs = elmt.getStructuralFeature();

    String nameStr = GeneratorDisplay.Generate(elmt.getName());

    startTrans();
    _name.setText(nameStr);
    Rectangle bbox = getBounds();
    setBounds(bbox.x, bbox.y, bbox.width, bbox.height);
    endTrans();
    
    /*String parStr = GeneratorDisplay.Generate(elmt.getAttributes());
    if (strs != null) {
      java.util.Enumeration enum = strs.elements();
      while (enum.hasMoreElements()) {
	    StructuralFeature sf = (StructuralFeature) enum.nextElement();
	    parStr += GeneratorDisplay.Generate(sf);
	    if (enum.hasMoreElements())
	      parStr += "\n";
      }
    }

    _dashRect.setText(parStr);*/

  }

  

  public void dispose() {
    if (!(getOwner() instanceof Element)) return;
    Element elmt = (Element) getOwner();
    Project p = ProjectBrowser.TheInstance.getProject();
    p.moveToTrash(elmt);
    super.dispose();
  }

  public void paint(Graphics g) {
    pos = getLocation();
    dim = getSize() ;
    Point p = _dashRect.getLocation();
    Dimension d = _dashRect.getSize() ;
    int minW = Math.min(d.width, dim.width);
    int minH = Math.max(textH, Math.min(d.height, 30));
    int minW2 = Math.max((int)(0.75*dim.width),dim.width-minW/2);
    _rRect.setBounds(pos.x, pos.y, dim.width, dim.height);
    _name.setTopMargin((dim.height-minH/2)/2-1);
    _name.setBounds(pos.x, pos.y+minH, dim.width, dim.height-minH);
    _dashRect.setBounds(pos.x, pos.y, minW, minH);

    super.paint(g);
 }


} /* end class FigPackage */
