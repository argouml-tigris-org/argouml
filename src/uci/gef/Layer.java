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



// File: Layer.java
// Classes: Layer
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;
import uci.util.*;

/** A Layer is like a drawing layer in high-end drawing applications
 *  (e.g., MacDraw Pro).  A Layer is like a sheet of clear plastic that
 *  can contain part of the picture being drawn and multiple layers
 *  are put on top of each other to make the overall
 *  picture.  Different layers can be hidden, locked, or grayed out
 *  independently.  In GEF the Layer class is more abstract than
 *  described above.  LayerDiagram is a subclass of Layer that does
 *  what is described above.  Other subclasses of Layer can provide
 *  functionality. For example the background drawing grid is a
 *  subclass of Layer that computes its display rather than displaying
 *  what is stored in a data structure.  Generalizing the concept of a
 *  layer to handle grids and other computed display features gives
 *  more power and allows the framework to be extended in building
 *  various applications. For example an application that needs polar
 *  coordinates might use LayerPolar, and an application that used a
 *  world map might implement LayerMap. But since layers can be
 *  composed, the user could put a grid in front of or behind the
 *  map. <p>
 *
 *  This approach to implementing drawing editors is similar to that
 *  described in a published paper: "Using the Multi-Layer Model for
 *  Building Interactive Graphical Applications" Fekete, et al.
 *  UIST'96. pp. 109-117.  GEF might be improved by making it more
 *  like the system described in that paper: basically by moving some
 *  of the XXXManage functionality into Layers, or merging Layers and
 *  Modes.
 *
 * @see LayerDiagram
 * @see LayerPerspective
 * @see LayerGrid
 * @see LayerPolar */

public abstract class Layer implements java.io.Serializable {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The name of the layer as it should appear in a menu. */
  private String _name = "aLayer";

  /** The type of FigNodes that should appear in this layer. */
  private String _type = "aLayer";

  /** Does the user not want to see this Layer right now?
   *  Needs-More-Work. */
  private boolean _hidden = false;

  /** Is this Layer demphasized by making everything in it gray?
   *  Needs-More-Work. */
  private boolean _grayed = false;

  /** Is this Layer locked so that the user can not modify it?
   *  Needs-More-Work. */
  private boolean _locked = false;

  /** Should the user be able to hide, lock, or gray this layer?
   *  Needs-More-Work. */
  protected boolean _onMenu = false;

  /** A Vector of the Editors that are displaying this Layer. */
  public transient Vector _editors = new Vector();

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new Layer.  This abstract class really does nothing
   *  in its constructor, but subclasses may have meaningful
   *  constructors. */
  public Layer() { }

  /** Construct a new layer with the given name. */
  public Layer(String name) { _name = name; }

  /** Construct a new layer with the given name and type. */
  public Layer(String name, String type) { _name = name; _type = type; }

  // needed?
//   public Layer shallowCopy() {
//     Layer lay;
//     try { lay = (Layer) (this.getClass().newInstance()); }
//     catch (java.lang.IllegalAccessException ignore) { return null; }
//     catch (java.lang.InstantiationException ignore) { return null; }
//     lay._name = _name;
//     lay._type = _type;
//     lay._onMenu = _onMenu;
//     lay._grayed = _grayed;
//     return lay;
//   }

  ////////////////////////////////////////////////////////////////
  // accessors


  /** Reply a string useful for debugging */
  public String toString() { return super.toString() + "[" + _name + "]"; }

  /** If this layer has the given name then return 'this', else null */
  public Layer findLayerNamed(String aName) {
    return (aName.equals(_name)) ? this : null;
  }

  /** Get and set methods */
  public String getName() { return _name; }
  public void setName(String n) { _name = n; }

  public void setHidden(boolean b) { _hidden = b; }
  public boolean getHidden() { return _hidden; }

  public void setGrayed(boolean b) { _grayed = b; }
  public boolean getGrayed() { return _grayed; }

  public void setLocked(boolean b) { _locked = b; }
  public boolean getLocked() { return _locked; }

  public void setOnMenu(boolean b) { _onMenu = b; }
  public boolean getOnMenu() { return _onMenu; }

  public abstract Vector getContents();

  public Vector getContentsNoEdges() {
    Vector contents = getContents();
    int size = contents.size();
    Vector res = new Vector(size);
    for (int i = 0; i < size; i++) {
      Object o = contents.elementAt(i);
      if (!(o instanceof FigEdge))
	res.addElement(o);
    }
    return res;
  }

  public Vector getContentsEdgesOnly() {
    Vector contents = getContents();
    int size = contents.size();
    Vector res = new Vector(size);
    for (int i = 0; i < size; i++) {
      Object o = contents.elementAt(i);
      if (o instanceof FigEdge)
	res.addElement(o);
    }
    return res;
  }

  /** Return the Vector of Editors that are showing this Layer. */
  public Vector getEditors() { return _editors; }

  /** Most Layers contain Fig, so I have empty implementations of
   * add, remove, removeAll, elements, and hit.
   *
   * @see LayerDiagram */
  public void add(Fig f) { }
  public void remove(Fig f) { }
  public void removeAll() { }
  public Enumeration elements() { return EnumerationEmpty.theInstance(); }
  public Fig hit(Rectangle r) { return null; }

  /** Reply an enumeration of all the Figs in this Layer that 
   *  intersect given Rectangle. */
  public Enumeration elementsIn(Rectangle r) {
    return new EnumerationPredicate(elements(), new PredFigInRect(r));
  }

  /** Reply an enumeration of all the FigNodes in this Layer that 
   *  intersect given Rectangle. */
  public Enumeration nodesIn(Rectangle r) {
    return new EnumerationPredicate(elements(), new PredFigNodeInRect(r));
  }

  /** Given an object from the net-level model (e.g., NetNode or
   * NetPort), reply the graphical depiction of that object in this
   * layer, if there is one. Otherwise reply null. */
  public abstract Fig presentationFor(Object obj);

  /** Return a string that can be used to make some Layers show nodes
   * in one way and other Layers show the same nodes in a different
   * way.  By default just use the name of the layer, but in general
   * names are for users to specify as reminders to themselves and the
   * perspectiveType controls what kinds of node FigNodes will be
   * added to that view. */
  public String getPerspectiveType() { return _type; }
  public void setPerspectiveType(String t) { _type = t; }

  /** Most Layers will contain things in back to front order, so I
   * define empty reordering functions here.  Subclasses can implement
   * these if appropriate. */
  public void sendToBack(Fig f) { }
  public void bringForward(Fig f) { }
  public void sendBackward(Fig f) { }
  public void bringToFront(Fig f) { }
  public void bringInFrontOf(Fig f1, Fig f2) { }
  public void reorder(Fig f, int function) { }

  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Draw the Layer on a PrintGraphics.  By default, just calls paint(g). */
  public void print(Graphics g) { paint(g); }

  /** Paint this Layer on the given Graphics. Sublasses should define
   *  methods for paintContents, which is called from here if the Layer
   *  is not hidden. */
  public void paint(Graphics g) {
    if (_hidden) return;
    if (! _grayed) paintContents(g); else paintGrayContents(g);
  }

  /** Abactract method to paint the contents of this layer, subclasses
   *  must define this.  For example, LayerDiagram paints itself by
   *  painting a list of Figs and LayerGrid paints itself by painting
   *  a lot lines. */
  public abstract void paintContents(Graphics g);

  /** Paint the contents in a dimmed, demphasized way.  Calls
   *  paintContents. Needs-More-Work: really needs a new kind of
   *  Graphics to work right. */
  public void paintGrayContents(Graphics g) {
    g.setColor(Color.lightGray);
    // g.lockColor(); // Needs-More-Work: not implemented
    paintContents(g);
    // g.unlockColor(); // Needs-More-Work: not implemented
  }

  ////////////////////////////////////////////////////////////////
  // notifications and updates

  /** A Fig in this Layer has changed state and needs to be
   *  redrawn. Notify all Editors showing this Layer that they should
   *  record the damage. */ 
  public void damaged(Fig f) {
    if (_editors == null) return;
    Enumeration eds = _editors.elements();
    while (eds.hasMoreElements())
      ((Editor)eds.nextElement()).damaged(f);
  }

  /** A Fig in this Layer has been deleted. Notify all Editors so that
   *  they can deselect the Fig. */
  public void deleted(Fig f) {
    if (_editors == null) return;
    Enumeration eds = _editors.elements();
    while (eds.hasMoreElements())
      ((Editor)eds.nextElement()).removed(f);
  }


  /** Ask all Editors to completely redraw their display. */
  public void refreshEditors() {
    if (_editors == null) return;
    Enumeration eds = _editors.elements();
    while (eds.hasMoreElements()) ((Editor) eds.nextElement()).damageAll();
  }

  /** Add an Editor to the list of Editors showing this Layer. */ 
  public void addEditor(Editor ed) {
    if (_editors == null) _editors = new Vector();
    _editors.addElement(ed);
  }

  public void removeEditor(Editor ed) {
    if (_editors == null) return;
    _editors.removeElement(ed);
  }

  public void preSave() { }
  public void postSave() { }
  public void postLoad() { }

  ////////////////////////////////////////////////////////////////
  // user interface

  /** Allow the user to edit the properties of this layer (not the
   * properties of the contents of this layer). For example, in
   * LayerGrid this could set the grid size. By default, does nothing.
   *
   * @see LayerGrid */
  public void adjust() { }

  static final long serialVersionUID = 8654800923889173867L;
} /* end class Layer */
