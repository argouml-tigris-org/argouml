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



// File: FigNode.java
// Classes: FigNode
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.Serializable;
import java.beans.*;
import com.sun.java.swing.*;

import uci.util.*;
import uci.ui.*;
import uci.graph.*;
import uci.argo.kernel.*;

/** Class to present a node (such as a NetNode) in a diagram. */

public class FigNode extends FigGroup
implements MouseListener, PropertyChangeListener, Serializable {
  ////////////////////////////////////////////////////////////////
  // constants
  
  /** Constants useful for determining what side (north, south, east,
   *  or west) a port is located on. Maybe this should really be in
   *  FigNode. */
  public static final double ang45 = Math.PI / 4;
  public static final double ang135 = 3*Math.PI / 4;
  public static final double ang225 = 5*Math.PI / 4;
  public static final double ang315 = 7*Math.PI / 4;

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** True if you want ports to show when the mouse moves in and
   *  be invisible otherwise. */
  protected boolean _blinkPorts = false;

  /** True when we want to draw the user's attention to this FigNode. */
  protected boolean _highlight = false;

  /** A Vector of FigEdges that need to be rerouted when this FigNode
   *  moves. */
  protected Vector _figEdges = new Vector(); 
  
  ////////////////////////////////////////////////////////////////
  // constructors

  /** Constructs a new FigNode on the given node with the given owner. */
  public FigNode(Object node) {
    setOwner(node);
    // if (node instanceof GraphNodeHooks)
    // ((GraphNodeHooks)node).addPropertyChangeListener(this);
  }

  /** Constructs a new FigNode on the given node with the given owner
   *  and Figs. */
  public FigNode(Object node, Vector figs) {
    this(node);
    setFigs(figs);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Set the property of highlighting ports when the user moves the
   *  mouse over this FigNode. */
  public void setBlinkPorts(boolean b) {
    _blinkPorts = b;
    hidePorts();
  }
  public boolean getBlinkPorts() { return _blinkPorts; }


  /** Adds a FigEdge to the list of them that need to be rerouted when
   *  this FigNode moves. */
  public void addFigEdge(FigEdge fe) { _figEdges.addElement(fe); }

  /** removes a FigEdge from the list of them that need to be rerouted when
   *  this FigNode moves. */
  public void removeFigEdge(FigEdge fe) { _figEdges.removeElement(fe); }

  /** Sets the owner (an node in some underlying model). If the given
   *  node implements GraphNodeHooks, then the FigNode will register
   *  itself as a listener on the node. */
  public void setOwner(Object node) {
    Object oldOwner = getOwner();
    if (oldOwner != null && oldOwner instanceof GraphNodeHooks)
      ((GraphNodeHooks)oldOwner).removePropertyChangeListener(this);
    else if (oldOwner != null && oldOwner instanceof Highlightable)
      ((Highlightable)oldOwner).removePropertyChangeListener(this);

    if (node instanceof GraphNodeHooks)
      ((GraphNodeHooks)node).addPropertyChangeListener(this);
    else if (node instanceof Highlightable)
      ((Highlightable)node).addPropertyChangeListener(this);

    super.setOwner(node);
  }

  /** Returns true if any Fig in the group hits the given rect. */
  public boolean hit(Rectangle r) {
      int cornersHit = countCornersContained(r.x, r.y, r.width, r.height);
    if (_filled) return cornersHit > 0;
    else return cornersHit > 0 && cornersHit < 4;
  }

  public boolean contains(int x, int y) {
    return (_x <= x) && (x <= _x + _w) && (_y <= y) && (y <= _y + _h);
  }


  ////////////////////////////////////////////////////////////////
  // Editor API

  /** When a FigNode is damaged, all of its edges may need repainting. */
  public void startTrans() {
    Enumeration arcPers = _figEdges.elements();
    while (arcPers.hasMoreElements()) {
      Fig f = (Fig) arcPers.nextElement();
      f.startTrans();
    }
    super.startTrans();
  }

  /** When a FigNode is damaged, all of its edges may need repainting. */
  public void endTrans() {
    Enumeration arcPers = _figEdges.elements();
    while (arcPers.hasMoreElements()) {
      Fig f = (Fig) arcPers.nextElement();
      f.endTrans();
    }
    super.endTrans();
  }

  /** When a FigNode is deleted, all of its edges are deleted. */
  public void delete() {
    Enumeration arcPers = _figEdges.elements();
    while (arcPers.hasMoreElements()) {
      Fig f = (Fig) arcPers.nextElement();
      f.delete();
    }
    super.delete();
  }

  /** When a FigNode is disposed, all of its edges are disposed. */
  public void dispose() {
    Enumeration arcPers = _figEdges.elements();
    while (arcPers.hasMoreElements()) {
      Fig f = (Fig) arcPers.nextElement();
      f.dispose();
    }
    super.dispose();
  }


  ////////////////////////////////////////////////////////////////
  // ports

  /** Sets the port (some object in an underlying model) for Fig f.  f
   *  must already be contained in the FigNode. f will now represent
   *  the given port. */
  public void bindPort(Object port, Fig f) {
    Fig oldPortFig = getPortFig(port);
    if (oldPortFig != null) oldPortFig.setOwner(null); //?
    f.setOwner(port);
  }

  /** Removes a port from the current FigNode. */
  public void removePort(Fig rep) {
    if (rep.getOwner() != null) rep.setOwner(null);
  }

  /** Reply the NetPort associated with the topmost Fig under the mouse, or
   *  null if there is none. */
  public final Object hitPort(Point p) { return hitPort(p.x, p.y); }

  /** Reply the port that "owns" the topmost Fig under the given point, or
   *  null if none. */
  public Object hitPort(int x, int y) {
    Fig f = hitFig(new Rectangle(x, y, 1, 1));
    if (f != null) return f.getOwner();
    else return null;
  }

  /** Reply a port for the topmost Fig that actually has a port. This
   *  allows users to drag edges to or from ports that are hidden by
   *  other Figs. */
  public Object deepHitPort(int x, int y) {
    Enumeration figs = elements();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      Object own = f.getOwner();
      // assumes ports are always filled
      if (f.contains(x, y) && own != null) return own;
    }

    Rectangle r = new Rectangle(x - 16, y - 16, 32, 32);
    figs = elements();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      Object own = f.getOwner();
      // assumes ports are always filled
      if (f.hit(r) && own != null) return own;
    }

    return null;
  }


  
  /** Reply the Fig that displays the given NetPort. */
  public Fig getPortFig(Object np) {
    Enumeration figs = elements();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      if (f.getOwner() == np) return f;
    }
    return null;
  }

  /** Reply a Vector of Fig's that have some port as their owner */
  public Vector getPortFigs() {
    Vector res = new Vector();
    Enumeration figs = elements();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      if (f.getOwner() != null) res.addElement(f);
    }
    return res;
  }


  ////////////////////////////////////////////////////////////////
  // diagram-level operations

  /** Reply the port's sector within the current view.  This version
   *  works precisely with square FigNodes the angxx constants
   *  should be removed and calculated by the port if non-square
   *  FigNodes will be used.
   *
   *  <pre>Sectors
   *		      \  1   /
   *		       \    /
   *		        \  /
   *		     2   \/   -2
   *			 /\
   *		        /  \
   *		       /    \
   *		      /  -1  \ </pre>
   **/

  public int getPortSector(Fig portFig) {
    Rectangle nodeBBox = getBounds();
    Rectangle portBBox = portFig.getBounds();
    int nbbCenterX = nodeBBox.x + nodeBBox.width / 2;
    int nbbCenterY = nodeBBox.y + nodeBBox.height / 2;
    int pbbCenterX = portBBox.x + portBBox.width / 2;
    int pbbCenterY = portBBox.y + portBBox.height / 2;

    if (portFig != null) {
      int dx = (pbbCenterX - nbbCenterX) * nodeBBox.height;
      int dy = (pbbCenterY - nbbCenterY) * nodeBBox.width;
      double dist = Math.sqrt(dx * dx + dy * dy);
      double ang;
      if (dy > 0) ang = Math.acos(dx / dist);
      else ang = Math.acos(dx / dist) + Math.PI;

      if (ang < ang45) return 2;
      else if (ang < ang135) return 1;
      else if (ang < ang225) return -2;
      else if (ang < ang315) return -1;
      else return 2;
    }
    return -1;
  }

  
  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Paints the FigNode to the given Graphics. Calls super.paint to
   *  paint all the Figs contained in the FigNode. Also can draw a
   *  highlighting rectangle around the FigNode. Needs-more-work:
   *  maybe I should implement LayerHighlight instead. */
  public void paint(Graphics g) {
    super.paint(g);
    if (_highlight) {
      g.setColor(Globals.getPrefs().getHighlightColor()); /* needs-more-work */
      g.drawRect(_x - 5, _y - 5, _w + 9, _h + 8);
      g.drawRect(_x - 4, _y - 4, _w + 7, _h + 6);
      g.drawRect(_x - 3, _y - 3, _w + 5, _h + 4);
    }
    //paintClarifiers(g);
  }

  public void paintClarifiers(Graphics g) {
    int iconX = _x;
    int iconY = _y - 15;
    ToDoList list = Designer.theDesigner().getToDoList();
    Vector items = list.elementsForOffender(getOwner());
    int size = items.size();
    for (int i = 0; i < size; i++) {
      ToDoItem item = (ToDoItem) items.elementAt(i);
      Icon icon = item.getClarifier();
      if (icon instanceof Clarifier) {
	((Clarifier)icon).setFig(this);
	((Clarifier)icon).setToDoItem(item);
      }
      icon.paintIcon(null, g, iconX, iconY);
      iconX += icon.getIconWidth();
    }
  }

  public ToDoItem hitClarifier(int x, int y) {
    int iconX = _x;
    ToDoList list = Designer.theDesigner().getToDoList();
    Vector items = list.elementsForOffender(getOwner());
    int size = items.size();
    for (int i = 0; i < size; i++) {
      ToDoItem item = (ToDoItem) items.elementAt(i);
      Icon icon = item.getClarifier();
      if (icon instanceof Clarifier) {
	((Clarifier)icon).setFig(this);
	((Clarifier)icon).setToDoItem(item);
	if (((Clarifier)icon).hit(x, y)) return item;
      }
      int width = icon.getIconWidth();
      if (y >= _y - 20 && y <= _y + 5 &&
	  x >= iconX && x <= iconX + width) return item;
      iconX += width;
    }
    return null;
  }

  public String getTipString(MouseEvent me) {
    ToDoItem item = hitClarifier(me.getX(), me.getY());
    if (item != null) return item.getHeadline();
    return super.getTipString(me);
  }

  ////////////////////////////////////////////////////////////////
  // notifications and updates

  /** The node object that this FigNode is presenting has changed
   *  state, or been disposed or highlighted. */
  public void propertyChange(PropertyChangeEvent pce) {
    //System.out.println("FigNode got a PropertyChangeEvent");
    String pName = pce.getPropertyName();
    Object src = pce.getSource();
    if (pName.equals("dispose") && src == getOwner()) { delete(); }
    if (pName.equals("highlight") && src == getOwner()) {
      _highlight = ((Boolean)pce.getNewValue()).booleanValue();
      damage();
    }
  }


  /** Make the port Figs visible. Used when blinkingPorts is true. */
  public void showPorts() {
    startTrans();
    Enumeration figs = elements();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      if (f.getOwner() != null) {
	f.setLineWidth(1);
	f.setFilled(true);
      }
    }
    endTrans();
  }

  /** Make the port Figs invisible. Used when blinkingPorts is true. */
  public void hidePorts() {
    startTrans();
    Enumeration figs = elements();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      if (f.getOwner() != null) {
	f.setLineWidth(0);
	f.setFilled(false);
      }
    }
    endTrans();
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** If the mouse enters this FigNode's bbox and the
   *  _blinkPorts flag is set, then show ports. */
  public void mouseEntered(MouseEvent me) {
    if (_blinkPorts) showPorts();
  }

  /** If the mouse exits this FigNode's bbox and the
   *  _blinkPorts flag is set, then hide ports. */
  public void mouseExited(MouseEvent me) {
    if (_blinkPorts) hidePorts();
  }

  /** Do nothing when mouse is pressed in FigNode. */
  public void mousePressed(MouseEvent me) { }

  /** Do nothing when mouse is released in FigNode. */
  public void mouseReleased(MouseEvent me) { }

  /** Do nothing when mouse is clicked in FigNode. */
  public void mouseClicked(MouseEvent me) { }


  public void translate(int dx, int dy) {
    super.translate(dx, dy);
    Enumeration arcPers = _figEdges.elements();
    while (arcPers.hasMoreElements()) {
      FigEdge fe = (FigEdge) arcPers.nextElement();
      fe.computeRoute();
    }
  }

} /* end class FigNode */

