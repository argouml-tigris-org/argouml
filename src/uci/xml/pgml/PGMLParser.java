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

package uci.xml.pgml;

import java.util.*;
import java.awt.*;
import java.io.*;
import java.net.URL;

import uci.graph.*;
import uci.gef.*;
import uci.xml.*;

import com.ibm.xml.parser.*;
import org.w3c.dom.*;

public class PGMLParser implements ElementHandler, TagHandler {

  ////////////////////////////////////////////////////////////////
  // static variables

  public static PGMLParser SINGLETON = new PGMLParser();

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected Diagram _diagram = null;
  protected int _nestedGroups = 0;
  protected Hashtable _figRegistry;
  protected Hashtable _ownerRegistry;

  ////////////////////////////////////////////////////////////////
  // constructors

  protected PGMLParser() { }

  ////////////////////////////////////////////////////////////////
  // main parsing methods

  public synchronized Diagram readDiagram(URL url) {
    try {
      InputStream is = url.openStream();
      String filename = url.getFile();
      System.out.println("=======================================");
      System.out.println("== READING DIAGRAM: " + url);
      Parser pc = new Parser(filename);
      pc.addElementHandler(this);
      pc.setTagHandler(this);
      pc.getEntityHandler().setEntityResolver(DTDEntityResolver.SINGLETON);
      //pc.setProcessExternalDTD(false);
      initDiagram("uci.gef.Diagram");
      _figRegistry = new Hashtable();
      pc.readStream(is);
      is.close();
      return _diagram;
    }
    catch (Exception ex) {
      System.out.println("Exception in readDiagram");
      ex.printStackTrace();
    }
    return null;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setOwnerRegistery(Hashtable owners) {
    _ownerRegistry = owners;
  }

  ////////////////////////////////////////////////////////////////
  // internal methods

  protected void initDiagram(String diagDescr) {
    String clsName = diagDescr;
    String initStr = null;
    int bar = diagDescr.indexOf("|");
    if (bar != -1) {
      clsName = diagDescr.substring(0, bar);
      initStr = diagDescr.substring(bar + 1);
    }
    try {
      Class cls = Class.forName(clsName);
      _diagram = (Diagram) cls.newInstance();
      if (initStr != null && !initStr.equals(""))
	_diagram.initialize(findOwner(initStr));
    }
    catch (Exception ex) {
      System.out.println("could not set diagram type to " + clsName);
      ex.printStackTrace();
    }
  }


  ////////////////////////////////////////////////////////////////
  // XML element handlers

  public void handleStartTag(TXElement e, boolean empty) {
    String elementName = e.getName();
    if ("group".equals(elementName)) _nestedGroups++;
    else if (elementName.equals("pgml")) handlePGML(e);
  }

  public void handleEndTag(TXElement e, boolean empty) {
    String elementName = e.getName();
    if ("group".equals(elementName)) _nestedGroups--;
  }

  public TXElement handleElement(TXElement e) {
    try {
      String elementName = e.getName();
      if (elementName.equals("pgml")) { /* do nothing */ }
      else if (elementName.equals("group"))
	_diagram.add(handleGroup(e));
      else if (_nestedGroups == 0) {
	if (elementName.equals("path"))
	  _diagram.add(handlePolyLine(e));
	else if (elementName.equals("ellipse"))
	  _diagram.add(handleEllipse(e));
	else if (elementName.equals("rectangle"))
	  _diagram.add(handleRect(e));
	else if (elementName.equals("text"))
	  _diagram.add(handleText(e));
	else if (elementName.equals("piewedge")) { }
	else if (elementName.equals("circle")) { }
	else if (elementName.equals("moveto")) { }
	else if (elementName.equals("lineto")) { }
	else if (elementName.equals("curveto")) { }
	else if (elementName.equals("arc")) { }
	else if (elementName.equals("closepath")) { }
	else System.out.println("unknown top-level tag: " + elementName);
      }
      else if (_nestedGroups > 0) {
	//System.out.println("skipping nested " + elementName);
      }
    }
    catch (Exception ex) {
      System.out.println("Exception in PGMLParser handleElement");
      ex.printStackTrace();
    }
    return e; // needs-more-work: too much memory? should return null.
  }


  protected void handlePGML(TXElement e) {
    String name = e.getAttribute("name");
    String clsName = e.getAttribute("description");
    try {
      if (clsName != null && !clsName.equals("")) initDiagram(clsName);
      if (name != null && !name.equals("")) _diagram.setName(name);
    }
    catch (Exception ex) { System.out.println("Exception in handlePGML"); }
  }

  protected Fig handlePolyLine(TXElement e) {
    String clsName = e.getAttribute("description");
    if (clsName != null && clsName.indexOf("FigLine") != -1)
      return handleLine(e);
    else
      return handlePath(e);
  }

  protected FigLine handleLine(TXElement e) {
    FigLine f = new FigLine(0, 0, 100, 100);
    setAttrs(f, e);
    TXElement moveto = e.getElementNamed("moveto");
    TXElement lineto = e.getElementNamed("lineto");
    if (moveto != null && lineto != null) {
      String x1 = moveto.getAttribute("x");
      String y1 = moveto.getAttribute("y");
      String x2 = lineto.getAttribute("x");
      String y2 = lineto.getAttribute("y");
      int x1Int = (x1 == null || x1.equals("")) ? 0 : Integer.parseInt(x1);
      int y1Int = (y1 == null || y1.equals("")) ? 0 : Integer.parseInt(y1);
      int x2Int = (x2 == null || x2.equals("")) ? x1Int : Integer.parseInt(x2);
      int y2Int = (y2 == null || y2.equals("")) ? y1Int : Integer.parseInt(y2);
      f.setX1(x1Int);
      f.setY1(y1Int);
      f.setX2(x2Int);
      f.setY2(y2Int);
    }
    return f;
  }

  protected FigCircle handleEllipse(TXElement e) {
    FigCircle f = new FigCircle(0, 0, 50, 50);
    setAttrs(f, e);
    String rx = e.getAttribute("rx");
    String ry = e.getAttribute("ry");
    int rxInt = (rx == null || rx.equals("")) ? 10 : Integer.parseInt(rx);
    int ryInt = (ry == null || ry.equals("")) ? 10 : Integer.parseInt(ry);
    f.setX(f.getX() - rxInt);
    f.setY(f.getY() - ryInt);
    f.setWidth(rxInt * 2);
    f.setHeight(ryInt * 2);
    return f;
  }

  protected FigRect handleRect(TXElement e) {
    FigRect f;
    String cornerRadius = e.getAttribute("rounding");
    if (cornerRadius == null || cornerRadius.equals("")) {
      f = new FigRect(0, 0, 80, 80);
    }
    else {
      f = new FigRRect(0, 0, 80, 80);
      int rInt = Integer.parseInt(cornerRadius);
      ((FigRRect)f).setCornerRadius(rInt);
    }
    setAttrs(f, e);
    return f;
  }

  protected FigText handleText(TXElement e) {
    FigText f = new FigText(100, 100, 90, 45);
    setAttrs(f, e);
    String text = e.getText();
    f.setText(text);
    String font = e.getAttribute("font");
    if (font != null && !font.equals("")) f.setFontFamily(font);
    String textsize = e.getAttribute("textsize");
    if (textsize != null && !textsize.equals("")) {
      int textsizeInt = Integer.parseInt(textsize);
      f.setFontSize(textsizeInt);
    }
    return f;
  }

  protected FigPoly handlePath(TXElement e) {
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
	  if (x != null && !x.equals("")) xInt = Integer.parseInt(x);
	  String y = ((TXElement)n).getAttribute("y");
	  if (y != null && !y.equals("")) yInt = Integer.parseInt(y);
	  //needs-more-work: dx, dy
	  f.addPoint(xInt, yInt);
	}
      }
    }
    return f;
  }

  /* Returns Fig rather than FigGroups because this is also
     used for FigEdges. */
  protected Fig handleGroup(TXElement e) {
    Fig f = null;
    String clsNameBounds = e.getAttribute("description");
    StringTokenizer st = new StringTokenizer(clsNameBounds, ",;[] ");
    String clsName = st.nextToken();
    String xStr = null, yStr = null, wStr = null, hStr = null;
    if (st.hasMoreElements()) {
      xStr = st.nextToken();
      yStr = st.nextToken();
      wStr = st.nextToken();
      hStr = st.nextToken();
    }
    try {
      Class nodeClass = Class.forName(clsName);
      f = (Fig) nodeClass.newInstance();
      if (xStr != null && !xStr.equals("")) {
	int x = Integer.parseInt(xStr);
	int y = Integer.parseInt(yStr);
	int w = Integer.parseInt(wStr);
	int h = Integer.parseInt(hStr);
	f.setBounds(x, y, w, h);
      }
      if (f instanceof FigNode) {
	FigNode fn = (FigNode) f;
	if (e.hasChildNodes()) {
	  NodeList nl = e.getChildNodes();
	  int size = nl.getLength();
	  for (int i = 0; i < size; i++) {
	    Node n = nl.item(i);
	    if (n instanceof TXElement) {
	      TXElement pe = (TXElement) n;
	      String peName = pe.getName();
	      if ("private".equals(peName)) {
		Fig encloser = null;
		String body = pe.getText();
		StringTokenizer st2 = new StringTokenizer(body, "=\"' \t\n");
		while (st2.hasMoreElements()) {
		  String t = st2.nextToken();
		  String v = "no such fig";
		  if (st2.hasMoreElements()) v = st2.nextToken();
		  if (t.equals("enclosingFig")) encloser = findFig(v);
		}
		fn.setEnclosingFig(encloser);
	      }
	    }
	  }
	}
      }
      if (f instanceof FigEdge) {
	FigEdge fe = (FigEdge) f;
	if (e.hasChildNodes()) {
	  NodeList nl = e.getChildNodes();
	  int size = nl.getLength();
	  for (int i = 0; i < size; i++) {
	    Node n = nl.item(i);
	    if (n instanceof TXElement) {
	      TXElement pe = (TXElement) n;
	      String peName = pe.getName();
	      if ("path".equals(peName)) {
		Fig p = handlePath(pe);
		fe.setFig(p);
		((FigPoly)p)._isComplete = true;
		fe.calcBounds();
		if (fe instanceof FigEdgePoly)
		  ((FigEdgePoly)fe).setInitiallyLaidOut(true);
	      }
	      else if ("private".equals(peName)) {
		Fig spf = null;
		Fig dpf = null;
		FigNode sfn = null;
		FigNode dfn = null;
		String body = pe.getText();
		StringTokenizer st2 = new StringTokenizer(body, "=\"' \t\n");
		while (st2.hasMoreElements()) {
		  String t = st2.nextToken();
		  String v = st2.nextToken();
		  if (t.equals("sourcePortFig")) spf = findFig(v);
		  if (t.equals("destPortFig")) dpf = findFig(v);
		  if (t.equals("sourceFigNode")) sfn = (FigNode) findFig(v);
		  if (t.equals("destFigNode")) dfn = (FigNode) findFig(v);
		}
		fe.setSourcePortFig(spf);
		fe.setDestPortFig(dpf);
		fe.setSourceFigNode(sfn);
		fe.setDestFigNode(dfn);
	      }
	    }
	  }
	}
      }
    }
    catch (Exception ex) {
      System.out.println("Exception in handleGroup");
      ex.printStackTrace();
    }
    catch (NoSuchMethodError ex) {
      System.out.println("No constructor() in class " + clsName);
      ex.printStackTrace();
    }
    setAttrs(f, e);
    return f;
  }

  ////////////////////////////////////////////////////////////////
  // internal parsing methods

  protected void setAttrs(Fig f, TXElement e) {
    String name = e.getAttribute("name");
    if (name != null && !name.equals("")) _figRegistry.put(name, f);
    String x = e.getAttribute("x");
    if (x != null && !x.equals("")) {
      String y = e.getAttribute("y");
      String w = e.getAttribute("width");
      String h = e.getAttribute("height");
      int xInt = Integer.parseInt(x);
      int yInt = (y == null || y.equals("")) ? 0 : Integer.parseInt(y);
      int wInt = (w == null || w.equals("")) ? 20 : Integer.parseInt(w);
      int hInt = (h == null || h.equals("")) ? 20 : Integer.parseInt(h);
      f.setBounds(xInt, yInt, wInt, hInt);
    }
    String linewidth = e.getAttribute("stroke");
    if (linewidth != null && !linewidth.equals("")) {
      f.setLineWidth(Integer.parseInt(linewidth));
    }
    String strokecolor = e.getAttribute("strokecolor");
    if (strokecolor != null && !strokecolor.equals(""))
      f.setLineColor(colorByName(strokecolor, Color.blue));

    String fill = e.getAttribute("fill");
    if (fill != null && !fill.equals(""))
      f.setFilled(fill.equals("1") || fill.startsWith("t"));
    String fillcolor = e.getAttribute("fillcolor");
    if (fillcolor != null && !fillcolor.equals(""))
      f.setFillColor(colorByName(fillcolor, Color.white));

    String dasharray = e.getAttribute("dasharray");
    if (dasharray != null && !dasharray.equals("") &&
	!dasharray.equals("solid"))
      f.setDashed(true);

    try {
      String owner = e.getAttribute("href");
      if (owner != null && !owner.equals("")) f.setOwner(findOwner(owner));
    }
    catch (Exception ex) {
      System.out.println("could not set owner");
    }
  }


  //needs-more-work: find object in model
  protected Object findOwner(String uri) {
    Object own = _ownerRegistry.get(uri);
    return own;
  }

  protected Fig findFig(String uri) {
    Fig f = null;
    if (uri.indexOf(".") == -1) {
      f = (Fig) _figRegistry.get(uri);
    }
    else {
      StringTokenizer st = new StringTokenizer(uri, ".");
      String figNum = st.nextToken();
      f = (Fig) _figRegistry.get(figNum);
      if (f instanceof FigEdge) return ((FigEdge)f).getFig();
      while (st.hasMoreElements()) {
	if (f instanceof FigGroup) {
	  String subIndex = st.nextToken();
	  int i = Integer.parseInt(subIndex);
	  f = (Fig)((FigGroup)f).getFigs().elementAt(i);
	}
      }
    }
    return f;
  }

  //needs-more-work: make an instance of the named class
  protected GraphModel getGraphModelFor(String desc) {
    return new DefaultGraphModel();
  }

  protected Color colorByName(String name, Color defaultColor) {
    if (name.equalsIgnoreCase("white")) return Color.white;
    if (name.equalsIgnoreCase("lightGray")) return Color.lightGray;
    if (name.equalsIgnoreCase("gray")) return Color.gray;
    if (name.equalsIgnoreCase("darkGray")) return Color.darkGray;
    if (name.equalsIgnoreCase("black")) return Color.black;
    if (name.equalsIgnoreCase("red")) return Color.red;
    if (name.equalsIgnoreCase("pink")) return Color.pink;
    if (name.equalsIgnoreCase("orange")) return Color.orange;
    if (name.equalsIgnoreCase("yellow")) return Color.yellow;
    if (name.equalsIgnoreCase("green")) return Color.green;
    if (name.equalsIgnoreCase("magenta")) return Color.magenta;
    if (name.equalsIgnoreCase("cyan")) return Color.cyan;
    if (name.equalsIgnoreCase("blue")) return Color.blue;
    try { return Color.decode(name); }
    catch (Exception ex) {
      System.out.println("invalid color code string: " + name);
    }
    return defaultColor;
  }


} /* end class PGMLParser */

