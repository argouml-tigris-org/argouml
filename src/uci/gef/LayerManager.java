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
    if (findLayerNamed(lay.getName()) == null) {
      _layers.addElement(lay);
      lay.addEditor(_editor);
      setActiveLayer(lay);
    }
  }

  public void removeAllLayers() {
    _layers.removeAllElements();
    _activeLayer = null;
  }

  public void replaceActiveLayer(Layer lay) {
    _activeLayer.removeEditor(_editor);
    int oldActiveIndex = _layers.indexOf(_activeLayer);
    _layers.setElementAt(lay, oldActiveIndex);
    lay.addEditor(_editor);
    setActiveLayer(lay);
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
   *  Figs that are contained in this layer, reply the
   *  contents of my active layer. Maybe this should really reply _all_
   *  the contents of all layers. */
  public Vector getContents() {
    return (_activeLayer == null) ?  null : _activeLayer.getContents();
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

  public void preSave() {
    for (int i = 0; i < _layers.size(); i++)
      ((Layer) _layers.elementAt(i)).preSave();
  }

  public void postSave() {
    for (int i = 0; i < _layers.size(); i++)
      ((Layer) _layers.elementAt(i)).postSave();
  }

  public void postLoad() {
    for (int i = 0; i < _layers.size(); i++)
      ((Layer) _layers.elementAt(i)).postLoad();
  }

  static final long serialVersionUID = 1910040372518652814L;
} /* end class LayerManager */
