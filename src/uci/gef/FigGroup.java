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





// File: FigGroup.java
// Classes: FigGroup
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** This class implements a group of Figs. */

public class FigGroup extends Fig {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The Fig's contained in this FigGroup */
  //? use array for speed?
  protected Vector _figs;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new FigGroup that holds no Figs. */
  public FigGroup() {
    super();
    _figs = new Vector();
  }

  /** Construct a new FigGroup that holds the given Figs. */
  public FigGroup(Vector figs) {
    super();
    _figs = figs;
    calcBounds();
  }

   public Object clone() {
     FigGroup figClone = (FigGroup) super.clone();
     Vector figsClone = new Vector(_figs.size());
     Enumeration fc = _figs.elements();
     while (fc.hasMoreElements()) {
       Fig tempFig = (Fig) fc.nextElement();
       Fig tempFigClone = (Fig) tempFig.clone();
       figsClone.addElement(tempFigClone);
       tempFigClone.setGroup(figClone);
     }
     figClone._figs = figsClone;
     return figClone;
   }


  ////////////////////////////////////////////////////////////////
  // accessors

  /** Reply an Enumeration of the Figs contained in this FigGroup. */
  public Enumeration elements() { return _figs.elements(); }

  /** Add a Fig to the group.  New Figs are added on the top. */
  public void addFig(Fig f) {
    _figs.addElement(f);
    f.setGroup(this);
    calcBounds();
  }

  /** Reply the Vector of Figs. */
  public Vector getFigs() { return _figs; }

  /** Set the Vector of Figs in this group. Fires PropertyChange with "bounds". */
  public void setFigs(Vector figs) {
    Rectangle oldBounds = getBounds();
    _figs = figs;
    calcBounds();
    firePropChange("bounds", oldBounds, getBounds());
  }


  /** Remove a Fig from the group. Fires PropertyChange with "bounds". */
  public void removeFig(Fig f) {
    if (!_figs.contains(f)) return;
    Rectangle oldBounds = getBounds();
    _figs.removeElement(f);
    f.setGroup(null);
    calcBounds();
    firePropChange("bounds", oldBounds, getBounds());
  }

  /** Delete all Fig's from the group. Fires PropertyChange with "bounds".*/
  public void removeAll() {
    Rectangle oldBounds = getBounds();
    for (int i = 0; i < _figs.size(); ++i) {
      Fig f = (Fig) _figs.elementAt(i);
      f.setGroup(null);
    }
    _figs.removeAllElements();
    calcBounds();
    firePropChange("bounds", oldBounds, getBounds());
  }


  /** Groups are resizable, but not reshapable, and not rotatable (for now). */
  public boolean isResizable() { return true; }
  public boolean isReshapable() { return false; }
  public boolean isRotatable() { return false; }

  ////////////////////////////////////////////////////////////////
  // Fig Accessors

  public void setLineColor(Color col) {
    int size = _figs.size();
    for (int i = 0; i < size; i++)
      ((Fig)_figs.elementAt(i)).setLineColor(col);
  }
  public Color getLineColor() {
    if (_figs.size() == 0) return super.getLineColor();
    return ((Fig)_figs.elementAt(_figs.size()-1)).getLineColor();
  }

  public void setFillColor(Color col) {
    int size = _figs.size();
    for (int i = 0; i < size; i++)
      ((Fig)_figs.elementAt(i)).setFillColor(col);
  }
  public Color getFillColor() {
    if (_figs.size() == 0) return super.getFillColor();
    return ((Fig)_figs.elementAt(_figs.size()-1)).getFillColor();
  }

  public void setFilled(boolean f) {
    int size = _figs.size();
    for (int i = 0; i < size; i++)
      ((Fig)_figs.elementAt(i)).setFilled(f);
  }
  public boolean getFilled() {
    if (_figs.size() == 0) return super.getFilled();
    return ((Fig)_figs.elementAt(_figs.size()-1)).getFilled();
  }

  public void setLineWidth(int w) {
    int size = _figs.size();
    for (int i = 0; i < size; i++)
      ((Fig)_figs.elementAt(i)).setLineWidth(w);
  }
  public int getLineWidth() {
    if (_figs.size() == 0) return super.getLineWidth();
    return ((Fig)_figs.elementAt(_figs.size()-1)).getLineWidth();
  }

  ////////////////////////////////////////////////////////////////
  // FigText Accessors

  public void setTextColor(Color c) {
    int size = _figs.size();
    for (int i = 0; i < size; i++) {
      Object ft = _figs.elementAt(i);
      if (ft instanceof FigText) ((FigText)ft).setTextColor(c);
    }
  }
  public Color getTextColor() {
    int size = _figs.size();
    for (int i = 0; i < size; i++) {
      Object ft = _figs.elementAt(i);
      if (ft instanceof FigText) return ((FigText)ft).getTextColor();
    }
    return null;
  }

  public void setTextFilled(boolean b) {
    int size = _figs.size();
    for (int i = 0; i < size; i++) {
      Object ft = _figs.elementAt(i);
      if (ft instanceof FigText) ((FigText)ft).setTextFilled(b);
    }
  }
  public boolean getTextFilled() {
    int size = _figs.size();
    for (int i = 0; i < size; i++) {
      Object ft = _figs.elementAt(i);
      if (ft instanceof FigText) return ((FigText)ft).getTextFilled();
    }
    return false;
  }

  public void setTextFillColor(Color c) {
    int size = _figs.size();
    for (int i = 0; i < size; i++) {
      Object ft = _figs.elementAt(i);
      if (ft instanceof FigText) ((FigText)ft).setTextFillColor(c);
    }
  }
  public Color getTextFillColor() {
    int size = _figs.size();
    for (int i = 0; i < size; i++) {
      Object ft = _figs.elementAt(i);
      if (ft instanceof FigText) return ((FigText)ft).getTextFillColor();
    }
    return null;
  }

  public void setFont(Font f) {
    int size = _figs.size();
    for (int i = 0; i < size; i++) {
      Object ft = _figs.elementAt(i);
      if (ft instanceof FigText) ((FigText)ft).setFont(f);
    }
  }
  public Font getFont() {
    int size = _figs.size();
    for (int i = 0; i < size; i++) {
      Object ft = _figs.elementAt(i);
      if (ft instanceof FigText) return ((FigText)ft).getFont();
    }
    return null;
  }

  public void setFontSize(int s) {
    int size = _figs.size();
    for (int i = 0; i < size; i++) {
      Object ft = _figs.elementAt(i);
      if (ft instanceof FigText) ((FigText)ft).setFontSize(s);
    }
  }
  public int getFontSize() {
    int size = _figs.size();
    for (int i = 0; i < size; i++) {
      Object ft = _figs.elementAt(i);
      if (ft instanceof FigText) return ((FigText)ft).getFontSize();
    }
    return 10;
  }

  public void setFontFamily(String s) {
    int size = _figs.size();
    for (int i = 0; i < size; i++) {
      Object ft = _figs.elementAt(i);
      if (ft instanceof FigText) ((FigText)ft).setFontFamily(s);
    }
  }
  public String getFontFamily() {
    int size = _figs.size();
    for (int i = 0; i < size; i++) {
      Object ft = _figs.elementAt(i);
      if (ft instanceof FigText) return ((FigText)ft).getFontFamily();
    }
    return "Serif";
  }

  ////////////////////////////////////////////////////////////////
  // Fig API

  /** Retrieve the top-most Fig containing the given point, or null.
   *  Needs-More-Work: just do a linear search.  Later, optimize this
   *  routine using Quad Trees (or other) techniques. */

  public Fig hitFig(Rectangle r) {
    Enumeration figs = elements();
    Fig res = null;
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      if (f.hit(r)) res = f;
    }
    return res;
  }

  /** Returns true if any Fig in the group contains the given point. */
  public boolean contains(int x, int y) {
    return hitFig(new Rectangle(x, y, 0, 0)) != null;
  }

  /** Returns true if any Fig in the group hits the given rect. */
  public boolean hit(Rectangle r) { return hitFig(r) != null; }

  /** Translate all the Fig in the list by the given offset. */
  public void translate(int dx, int dy) {
    Rectangle oldBounds = getBounds();
    Enumeration figs = _figs.elements();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      f.translate(dx, dy);
    }
    _x += dx; _y += dy; // no need to call calcBounds();
    firePropChange("bounds", oldBounds, getBounds());
  }

  /** Set the bounding box to the given rect. Figs in the group are
   *  scaled to fit. Fires PropertyChange with "bounds" */
  public void setBounds(int x, int y, int w, int h) {
    Rectangle oldBounds = getBounds();
    Enumeration figs = _figs.elements();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      int newX = (_w == 0) ? x : x + ((f.getX() - _x) * w) / _w;
      int newY = (_h == 0) ? y : y + ((f.getY() - _y) * h) / _h;
      int newW = (_w == 0) ? 0 : (f.getWidth() * w) / _w;
      int newH = (_h == 0) ? 0 : (f.getHeight() * h) / _h;
      f.setBounds(newX, newY, newW, newH);
    }
    calcBounds(); //_x = x; _y = y; _w = w; _h = h;
    firePropChange("bounds", oldBounds, getBounds());
  }

  ////////////////////////////////////////////////////////////////
  // display methods

  /** Paint all the Figs in this group. */
  public void paint(Graphics g) {
    Enumeration figs = elements();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      f.paint(g);
    }
  }

  /** Accumulate a bounding box for all the Figs in the group. */
  public void calcBounds() {
    Rectangle bbox; // could be blank final
    Enumeration figs = _figs.elements();
    if (figs.hasMoreElements()) bbox = ((Fig)figs.nextElement()).getBounds();
    else bbox = new Rectangle(0, 0, 0, 0);
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      bbox.add(f.getBounds());
    }
    _x = bbox.x;
    _y = bbox.y;
    _w = bbox.width;
    _h = bbox.height;
  }

  static final long serialVersionUID = 5329475606052749531L;

} /* end class FigGroup */

