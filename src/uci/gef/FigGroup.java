// Copyright (c) 1995, 1996 Regents of the University of California.
// All rights reserved.
//
// This software was developed by the Arcadia project
// at the University of California, Irvine.
//
// Redistribution and use in source and binary forms are permitted
// provided that the above copyright notice and this paragraph are
// duplicated in all such forms and that any documentation,
// advertising materials, and other materials related to such
// distribution and use acknowledge that the software was developed
// by the University of California, Irvine.  The name of the
// University may not be used to endorse or promote products derived
// from this software without specific prior written permission.
// THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: FigGroup.java
// Classes: FigGroup
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** This class implements a basic composite figure that holds a list
 *  of Figs.
 *  <A HREF="../features.html#basic_shapes_group">
 *  <TT>FEATURE: basic_shapes_group</TT></A>
 */

public class FigGroup extends Fig {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The Fig's contained in this FigGroup */
  //? use array instead?
  protected Vector _figs;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new FigGroup that holds no Fig's. */
  public FigGroup() {
    super();
    _figs = new Vector();
  }

  /** Construct a new FigGroup that holds the given Fig's. */
  public FigGroup(Vector figs) {
    super();
    _figs = figs;
    calcBounds();
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Reply an Enumeration of the Fig's contained in this FigGroup. */
  public Enumeration elements() { return _figs.elements(); }

  /** Add a Fig to the list.  New Figs are added on the top. */
  public void addFig(Fig p) { _figs.addElement(p); calcBounds(); }

  /** Reply the list of Fig's. */
  public Vector getFigs() { return _figs; }

  public void setFigs(Vector figs) { _figs = figs; calcBounds(); }


  /** Delete a Fig from the list */
  public void deleteFig(Fig p) { _figs.removeElement(p); calcBounds(); }

  /** Delete all Fig's from the list. */
  public void removeAll() { _figs.removeAllElements(); calcBounds(); }


  public boolean isResizable() { return true; }
  public boolean isReshapable() { return false; }
  public boolean isRotatable() { return false; }


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

  public boolean contains(int x, int y) {
    return hitFig(new Rectangle(x, y, 0, 0)) != null;
  }

  public boolean hit(Rectangle r) { return hitFig(r) != null; }

  /** Translate all the Fig in the list by the given offset. */
  public void translate(int dx, int dy) {
    Enumeration figs = _figs.elements();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      f.translate(dx, dy);
    }
    _x += dx; _y += dy; // no need to call calcBounds();
  }

  public void setBounds(int x, int y, int w, int h) {
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
  }

  ////////////////////////////////////////////////////////////////
  // display methods

  /** Paint all the Fig's in this list. */
  public void paint(Graphics g) {
    Enumeration figs = elements();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      f.paint(g);
    }
  }

  /** Accumulate a bounding box for all the Fig's in the list. */
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

} /* end class FigGroup */

