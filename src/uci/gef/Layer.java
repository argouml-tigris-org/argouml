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

// File: Layer.java
// Classes: Layer
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;
import uci.util.*;

/** A Layer is like a drawing layer in high-end drawing applications
 *  (e.g., MacDraw Pro). A Layer is like a sheet of clear plastic that
 *  can contain part of the picture being drawn and multiple layers are
 *  put on top of each other to make the overall picture. Different
 *  layers can be hidden, locked, or grayed out independently. In The
 *  UCI Graph Editing Framework the Layer class is more abstract than
 *  described above. LayerDiagram is a Subclass of Layer that does what
 *  is described above. Other subclasses of Layer can provide
 *  functionality. For example the background drawing grid is a
 *  subclass of Layer that computes its display rather than displaying
 *  what is stored in a data structure. Generalizing the concept of a
 *  layer to handle grids and other computed display features gives
 *  more power and allows the framework to be extended in building
 *  various applications. For example an application that needs polar
 *  coordinates might use LayerPolar, and an application that used a
 *  world map might implement LayerMap. But since layers can be
 *  composed, the user could put a grid in front of or behind the map. <p>
 *
 *  This approach to implementing drawing editors is described in one
 *  or more published papers.. UIST?<p>
 *
 *  <A HREF="../features.html#layers">
 *  <TT>FEATURE: layers</TT></A>
 *
 * @see LayerComposite
 * @see LayerDiagram
 * @see LayerGrid
 * @see LayerPolar */

public abstract class Layer extends Observable
implements java.io.Serializable  { 
  //, GEF {
  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The name of the layer as it should appear in a menu. */
  private String _name = "aLayer";

  /** The type of FigNodes that should appear in this layer. */
  private String _type = "aLayer";

  /** Does the user not want to see this layer right now?
   *  Needs-More-Work.
   *  <A HREF="../features.html#layers_hidden">
   *  <TT>FEATURE: layers_hidden</TT></A>
   */
  private boolean _hidden = false;

  /** Is this layer demphasized by making everything in it gray?
   *  Needs-More-Work.
   *  <A HREF="../features.html#layers_dimmed">
   *  <TT>FEATURE: layers_dimmed</TT></A>
   */
  private boolean _grayed = false;

  /** Is this layer locked so that the user can not modify it?
   *  Needs-More-Work.
   *  <A HREF="../features.html#layers_locked">
   *  <TT>FEATURE: layers_locked</TT></A>
   */
  private boolean _locked = false;

  /** Should the user be able to hide, lock, or gray this layer?
   *  Needs-More-Work.
   *  <A HREF="../features.html#layers_menu">
   *  <TT>FEATURE: layers_menu</TT></A>
   */
  protected boolean _onMenu = false;

  /** <A HREF="../features.html#multiple_views">
   *  <TT>FEATURE: multiple_views</TT></A>
   */
  public Vector _editors = new Vector();

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new layer. This abstract class really does nothing
   *  in its constructor, but subclasses may have meaningful
   *  constructors. */
  public Layer() { }

  /** Set the name of the layer. */
  public Layer(String name, String type) { _name = name; _type = type; }

  public Layer(String name) { _name = name; }

  // needed?
  public Layer shallowCopy() {
    Layer lay;
    try { lay = (Layer) (this.getClass().newInstance()); }
    catch (java.lang.IllegalAccessException ignore) { return null; }
    catch (java.lang.InstantiationException ignore) { return null; }
    lay._name = _name;
    lay._type = _type;
    lay._onMenu = _onMenu;
    lay._grayed = _grayed;
    return lay;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public String name() { return _name; }

  /** Reply a string useful for debugging */
  public String toString() { return super.toString() + "[" + _name + "]"; }

  /** If this layer has the given name then return 'this', else null */
  public Layer findLayerNamed(String aName) {
    return (aName.equals(_name)) ? this : null;
  }

    /** get and set methods */
  public void hidden(boolean b) { _hidden = b; }
  public boolean hidden() { return _hidden; }

  public void grayed(boolean b) { _grayed = b; }
  public boolean grayed() { return _grayed; }

  public void setLocked(boolean b) { _locked = b; }
  public boolean getLocked() { return _locked; }

  public void onMenu(boolean b) { _onMenu = b; }
  public boolean onMenu() { return _onMenu; }

  public Vector contents() { return null; }

  /** <A HREF="../features.html#multiple_views">
   *  <TT>FEATURE: multiple_views</TT></A>
   */
  public Vector editors() { return _editors; }

  /** Most layers contain things, so I have empty implementations of
   * add, remove, removeAll, elements, and hit.
   *
   * @see LayerDiagram */
  public void add(Fig f) { }
  public void remove(Fig f) { }
  public void removeAll() { }
  public Enumeration elements() { return EnumerationEmpty.theInstance(); }

  /** <A HREF="../features.html#hit_objects">
   *  <TT>FEATURE: hit_objects</TT></A>
   */
  public Fig hit(Rectangle r) { return null; }

  public Enumeration elementsIn(Rectangle r) {
    return new EnumerationPredicate(elements(), new PredFigInRect(r));
  }

  /** Given an object from the net-level model (e.g., NetNode or
   * NetPort), reply the graphical depiction of that object in this
   * layer, if there is one. Otherwise reply null. */
  public Fig presentationFor(Object obj) { return null; }

// //   /** By default layers do not delegate operations to
// //    * sublayers. LayerComposite overrides this operation to delegate to
// //    * one of its layers that is considered active. */
// //   public Layer getActiveLayer() { return this; }

  /** Return a string that can be used to make some Layers show nodes
  * in one way and some layers show the same nodes in a different
  * way. By default just use the name of the layer, but in general
  * names are for users to specify as reminders to themselves and the
  * perspectiveType controls what kinds of node FigNodes will be
  * added to that view. */
  public String getPerspectiveType() { return _type; }
  public void setPerspectiveType(String t) { _type = t; }

  /** Most layers will contain things in back to front order, so I
   * define empty reordering functions here. Subclasses can implement
   * these if appropriate. */
  public void sendToBack(Fig f) { }
  public void bringForward(Fig f) { }
  public void sendBackward(Fig f) { }
  public void bringToFront(Fig f) { }
  public void reorder(Fig f, int function) { }

  ////////////////////////////////////////////////////////////////
  // painting methods

  public void print(Graphics g) { paint(g); }

  /** Paint this Layer on the given Graphics. Sublasses should define
   * methods for paintContents, which is called from here if the layer
   * is not hidden. */
  public void paint(Graphics g) {
    if (_hidden) return;
    if (! _grayed) paintContents(g); else paintGrayContents(g);
  }

  /** Paint the contents of this layer, subclasses must define
   * this. For example, LayerDiagram paints itself by painting a list of
   * Fig's and LayerGrid paints itself by painting a lot
   * lines. */
  public abstract void paintContents(Graphics g);

  /** Paint the contents in a dimmed, demphasized way. Calls
   * paintContents. Needs-More-Work:
   * really needs a new kind of Graphics to work right. */
  public void paintGrayContents(Graphics g) {
    g.setColor(Color.lightGray);
    // g.lockColor(); // Needs-More-Work: not implemented
    paintContents(g);
    // g.unlockColor(); // Needs-More-Work: not implemented
  }

  ////////////////////////////////////////////////////////////////
  // notifications and updates

  public void damaged(Fig f) {
    Enumeration eds = _editors.elements();
    while (eds.hasMoreElements())
      ((Editor)eds.nextElement()).damaged(f);
  }

  public void deleted(Fig f) {
    Enumeration eds = _editors.elements();
    while (eds.hasMoreElements())
      ((Editor)eds.nextElement()).removed(f);
  }


  /** <A HREF="../features.html#multiple_views">
   *  <TT>FEATURE: multiple_views</TT></A> */
  public void refreshEditors() {
    Enumeration eds = _editors.elements();
    while (eds.hasMoreElements()) ((Editor) eds.nextElement()).damageAll();
  }

  /** <A HREF="../features.html#multiple_views">
   *  <TT>FEATURE: multiple_views</TT></A> */
  public void addEditor(Editor ed) { _editors.addElement(ed); }

  /** <A HREF="../features.html#multiple_views">
   *  <TT>FEATURE: multiple_views</TT></A> */
  public void removeEditor(Editor ed) { _editors.removeElement(ed); }

  ////////////////////////////////////////////////////////////////
  // user interface

  /** Allow the user to edit the properties of this layer (not the
   * properties of the contents of this layer). For example, in
   * LayerGrid this could set the grid size.
   *
   * @see LayerGrid */
  public void adjust() { }

} /* end class Layer */
