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

// File: LayerManager.java
// Classes: LayerManager
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** This class implements a kind of Layer that contains other
 *  Layers. Layer's can be nested in an is-part-of tree. That tree can
 *  be walked to paint the contents of the view, find what the user
 *  clicked on, find a layer by name, save the contents to a file,
 *  etc. */

public class LayerManager implements java.io.Serializable  {
  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The Layer's contained within this LayerManager. */
  protected Vector _layers = new Vector();

  /** In most editors one Layer is the active layer and all mouse
   *  clicks go to the contents of that layer.  For now I assume this,
   *  but I would like to avoid this assumption in the future. */
  protected Layer _activeLayer;

  public Editor _editor = null;

  ////////////////////////////////////////////////////////////////
  // constructors and related methods

  /** Construct a new LayerManager with no sublayers. */
  public LayerManager(Editor editor) { _editor = editor; }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Add a sublayer to this layer. */
  public void addLayer(Layer lay) {
    if (findLayerNamed(lay.name()) == null) {
      _layers.addElement(lay);
      lay.addEditor(_editor);
      setActiveLayer(lay);
    }
  }

  /** Remove a sublayer to this layer. */
  public void removeLayer(Layer lay) {
    _layers.removeElement(lay);
    lay.removeEditor(_editor);
    if (_activeLayer == lay) {
      if (_layers.size() >= 1) _activeLayer = (Layer) _layers.elementAt(0);
      else _activeLayer = null;
    }
  }

  /** Find a layer with the given name somewhere in the layer tree. */
  public Layer findLayerNamed(String aName) {
    Enumeration layers = _layers.elements();
    while (layers.hasMoreElements()) {
      Layer curLayer = (Layer) layers.nextElement();
      Layer res = curLayer.findLayerNamed(aName);
      if (res != null) return res;
    }
    return null;
  }

  /** Make one of my layers the active one. */
  public void setActiveLayer(Layer lay) {
    if (_layers.contains(lay)) _activeLayer = lay;
    else System.out.println("That layer is not one of my layers");
  }

  /** Reply which layer is the active one. In case LayerManager's
   *  are nested, this works recursively. */
  public Layer getActiveLayer() { return _activeLayer; }

  /** When an editor or some tool wants to look at all the
   *  Fig's that are contained in this layer, reply the
   *  contents of my active layer. Maybe this should really reply _all_
   *  the contents of all layers. */
  public Vector contents() {
    return (_activeLayer == null) ?  null : _activeLayer.contents();
    /* needs-more-work: should accumulate???? */
    /* Vector v = new Vector();
    Enumerations layers = _layers.elements();
    while (layers.hasMoreElements()) {
      Layer lay = (Layer) layers.nextElement();
      Vector lay_contents = lay.contents();
      if (lay_contents != null) return lay_contents;
    }  */
  }

  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Paint the contents of this LayerManager by painting all layers. */
  public void paint(Graphics g) {
    Enumeration layers = _layers.elements();
    while (layers.hasMoreElements()) ((Layer) layers.nextElement()).paint(g);
  }

  ////////////////////////////////////////////////////////////////
  // Layer API

  /** When the user tries to add a new Fig to a
   *  LayerManager, pass that addition along to my active layer. */
  public void add(Fig f) {
    if (_activeLayer != null) _activeLayer.add(f);
  }

  /** When the user tries to remove a new Fig from a
   *  LayerManager, pass that removal along to my active layer. */
  public void remove(Fig f) {
    if (_activeLayer != null) _activeLayer.remove(f);
  }

  /** See comments above, this message is passed to my active layer. */
  public void removeAll() {
    if (_activeLayer != null) _activeLayer.removeAll();
  }

  /** See comments above, this message is passed to my active layer. */
  public Enumeration elements() {
    return (_activeLayer == null) ? null : _activeLayer.elements();
  }

  /** See comments above, this message is passed to my active layer. */
  public Fig hit(Rectangle r) {
    return (_activeLayer == null) ? null : _activeLayer.hit(r);
  }

  /** Try to find a FigNode instance that presents the given
   *  Net-level object. */
  public Fig presentationFor(Object obj) {
    Fig f = null;
    Enumeration lays = _layers.elements();
    while (lays.hasMoreElements()) {
        Layer sub = (Layer) lays.nextElement();
        f = sub.presentationFor(obj);
        if (f != null) return f;
    }
    return null;
  }

  /** See comments above, this message is passed to my active layer. */
  public void sendToBack(Fig f) {
    if (_activeLayer != null) _activeLayer.sendToBack(f);
  }

  /** See comments above, this message is passed to my active layer. */
  public void bringForward(Fig f) {
    if (_activeLayer != null) _activeLayer.bringForward(f);
  }

  /** See comments above, this message is passed to my active layer. */
  public void sendBackward(Fig f) {
    if (_activeLayer != null) _activeLayer.sendBackward(f);
  }

  /** See comments above, this message is passed to my active layer. */
  public void bringToFront(Fig f) {
    if (_activeLayer != null) _activeLayer.bringToFront(f);
  }

  /** See comments above, this message is passed to my active layer. */
  public void reorder(Fig f, int function) {
    if (_activeLayer != null) _activeLayer.reorder(f, function);
  }

  public void setEditor(Editor ed) {
    _editor = ed;
    Enumeration lays = _layers.elements();
    while (lays.hasMoreElements()) ((Layer) lays.nextElement()).addEditor(ed);
  }

  public Editor getEditor() { return _editor; }

} /* end class LayerManager */
