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

package uci.gef;

import java.util.*;
import java.awt.*;
import java.io.*;

import uci.graph.*;

import com.ibm.xml.parser.*;
import org.w3c.dom.*;

public class PGMLParserIBM implements ElementHandler {

  ////////////////////////////////////////////////////////////////
  // static variables
  
  public static PGMLParserIBM SINGLETON = new PGMLParserIBM();

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected boolean _inGroup = false;
  protected Diagram _diagram = null;
  
  ////////////////////////////////////////////////////////////////
  // constructors
  
  protected PGMLParserIBM() { }

  ////////////////////////////////////////////////////////////////
  // main parsing methods
  
  protected Diagram readDiagram(String filename, InputStream is) {
    Parser pc = new Parser(filename);
    pc.addElementHandler(this);
    initDiagram();
    pc.readStream(is);
    return _diagram;
  }

  ////////////////////////////////////////////////////////////////
  // internal methods

  protected void initDiagram() {
    _diagram = new Diagram();
  }

  
  ////////////////////////////////////////////////////////////////
  // XML element handlers
  
  public TXElement handleElement(TXElement e) {
    String elementName = e.getName();
    if (elementName.equals("pgml")) handlePGML(e);
    else if (elementName.equals("path")) handlePolyLine(e);
    else if (elementName.equals("ellipse")) handleEllipse(e);
    else if (elementName.equals("rectangle")) handleRect(e);
    else if (elementName.equals("text")) handleText(e);
    else if (elementName.equals("group")) handleGroup(e);
    else if (elementName.equals("piewedge")) { }
    else if (elementName.equals("circle")) { }
    else if (elementName.equals("moveto")) { }
    else if (elementName.equals("lineto")) { }
    else if (elementName.equals("curveto")) { }
    else if (elementName.equals("arc")) { }
    else if (elementName.equals("closepath")) { }
    else System.out.println("unknown tag: " + elementName);

    return e; // needs-more-work: too much memory? should return null.    
  }


  protected void handlePGML(TXElement e) {
    String name = e.getAttribute("name");
    String clsName = e.getAttribute("description");
    try {
      if (name != null) _diagram.setName(name);
      if (clsName != null) _diagram.setGraphModel(getGraphModelFor(clsName));
    }
    catch (Exception ex) { System.out.println("Exception in handlePGML"); }
  }

  protected void handlePolyLine(TXElement e) {
    String clsName = e.getAttribute("description");
    if (clsName != null && clsName.indexOf("FigLine") != 0)
      handleLine(e);
    else
      handlePath(e);
  }

  protected void handleLine(TXElement e) {
    FigLine f = new FigLine(0, 0, 100, 100);
    setAttrs(f, e);
    TXElement moveto = e.getElementNamed("moveto");
    TXElement lineto = e.getElementNamed("lineto");
    if (moveto != null && lineto != null) {
      String x1 = moveto.getAttribute("x");
      String y1 = moveto.getAttribute("y");
      String x2 = lineto.getAttribute("x");
      String y2 = lineto.getAttribute("y");
      int x1Int = (x1 == null) ? 0 : Integer.parseInt(x1);
      int y1Int = (y1 == null) ? 0 : Integer.parseInt(y1);
      int x2Int = (x2 == null) ? x1Int : Integer.parseInt(x2);
      int y2Int = (y2 == null) ? y1Int : Integer.parseInt(y2);
      f.setX1(x1Int);
      f.setY1(y1Int);
      f.setX2(x2Int);
      f.setY2(y2Int);
    }
    _diagram.add(f);
  }

  protected void handleEllipse(TXElement e) {
    FigCircle f = new FigCircle(0, 0, 50, 50);
    setAttrs(f, e);
    String rx = e.getAttribute("rx");
    String ry = e.getAttribute("ry");
    int rxInt = (rx == null) ? 10 : Integer.parseInt(rx);
    int ryInt = (ry == null) ? 10 : Integer.parseInt(ry);
    f.setWidth(rxInt * 2);
    f.setHeight(ryInt * 2);
    _diagram.add(f);
  }

  protected void handleRect(TXElement e) {
    Fig f;
    String cornerRadius = e.getAttribute("rounding");
    if (cornerRadius == null) {
      f = new FigRect(0, 0, 80, 80);
    }
    else {
      f = new FigRRect(0, 0, 80, 80);
      int rInt = Integer.parseInt(cornerRadius);
      ((FigRRect)f).setCornerRadius(rInt);
    }
    setAttrs(f, e);
    _diagram.add(f);
  }

  protected void handleText(TXElement e) {
    FigText f = new FigText(100, 100, 90, 45);
    setAttrs(f, e);
    String text = e.getText();
    f.setText(text);
    String font = e.getAttribute("font");
    if (font != null) f.setFontFamily(font);
    String textsize = e.getAttribute("textsize");
    if (textsize != null) {
      int textsizeInt = Integer.parseInt(textsize);
      f.setFontSize(textsizeInt);
    }
    _diagram.add(f);
  }

  protected void handlePath(TXElement e) {
    FigPoly f = new FigPoly();
    setAttrs(f, e);
    if (e.hasChildNodes()) {
      NodeList nl = e.getChildNodes();
      int size = nl.getLength();
      for (int i = 0; i < size; i++) {
	Node n = nl.item(i);
	int xInt = 0;
	int yInt = 0;
	if (n instanceof TXElement) {
	  String x = ((TXElement)n).getAttribute("x");
	  if (x != null) xInt = Integer.parseInt(x);
	  String y = ((TXElement)n).getAttribute("y");
	  if (y != null) yInt = Integer.parseInt(y);
	  //needs-more-work: dx, dy
	  f.addPoint(xInt, yInt);
	}
      }
    }
    _diagram.add(f);
  }

  protected void handleGroup(TXElement e) {
    Fig f = null;
    String clsName = e.getAttribute("description");
    try {
      Class nodeClass = Class.forName(clsName);
      f = (Fig) nodeClass.newInstance();
      System.out.println("made instance " + f);
    }
    catch (Exception ex) {
      System.out.println("Exception in handleGroup");
      ex.printStackTrace();
    }
    setAttrs(f, e);
    _diagram.add(f);
  }

  ////////////////////////////////////////////////////////////////
  // internal parsing methods

  protected void setAttrs(Fig f, TXElement e) {
    String x = e.getAttribute("x");
    if (x != null) {
      String y = e.getAttribute("y");
      String w = e.getAttribute("width");
      String h = e.getAttribute("height");
      int xInt = Integer.parseInt(x);
      int yInt = (y == null) ? 0 : Integer.parseInt(y);
      int wInt = (w == null) ? 20 : Integer.parseInt(w);
      int hInt = (h == null) ? 20 : Integer.parseInt(h);
      f.setBounds(xInt, yInt, wInt, hInt);
    }
    String linewidth = e.getAttribute("linewidth");
    if (linewidth != null) f.setLineWidth(Integer.parseInt(linewidth));
    String strokecolor = e.getAttribute("strokecolor");
    if (strokecolor != null)
      f.setLineColor(colorByName(strokecolor, Color.blue));

    String fill = e.getAttribute("fill");
    if (fill != null)
      f.setFilled(fill.equals("1") || fill.startsWith("t"));
    String fillcolor = e.getAttribute("fillcolor");
    if (fillcolor != null)
      f.setFillColor(colorByName(fillcolor, Color.white));

    String dasharray = e.getAttribute("dasharray");
    if (dasharray != null && !dasharray.equals("solid"))
      f.setDashed(true);

    //needs-more-work: set owner
  }

  
  //needs-more-work: make an instance of the named class
  protected GraphModel getGraphModelFor(String desc) {
    return new DefaultGraphModel();
  }

  protected Color colorByName(String name, Color defaultColor) {
    if (name.equals("white")) return Color.white;
    if (name.equals("lightGray")) return Color.lightGray;
    if (name.equals("gray")) return Color.gray;
    if (name.equals("darkGray")) return Color.darkGray;
    if (name.equals("black")) return Color.black;
    if (name.equals("red")) return Color.red;
    if (name.equals("pink")) return Color.pink;
    if (name.equals("orange")) return Color.orange;
    if (name.equals("yellow")) return Color.yellow;
    if (name.equals("green")) return Color.green;
    if (name.equals("magenta")) return Color.magenta;
    if (name.equals("cyan")) return Color.cyan;
    if (name.equals("blue")) return Color.blue;
    int code = defaultColor.getRGB();
    try {
      Integer i = Integer.decode(name);
      code = i.intValue();
    }
    catch (Exception ex) { }
    return new Color(code);
  }

  
} /* end class PGMLParserIBM */

