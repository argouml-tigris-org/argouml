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

// File: LayerPerspective.java
// Classes: LayerPerspective
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;
import uci.graph.*;

/** A Layer like found in many drawing applications. It contains a
 *  collection of Fig's, ordered from back to front. Each
 *  LayerPerspective contains part of the overall picture that the user is
 *  drawing.
 *  <A HREF="../features.html#graph_visualization">
 *  <TT>FEATURE: graph_visualization</TT></A>
 *  <A HREF="../features.html#multiple_perspectives">
 *  <TT>FEATURE: multiple_perspectives</TT></A>
 */

public class LayerPerspective extends LayerDiagram implements GraphListener {
  ////////////////////////////////////////////////////////////////
  // constants

  /** The space between node FigNodes that are automatically places. */
  public static final int GAP = 16;

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The underlying connected graph to be visualized. */
  //protected NetList _net;
  protected GraphModel _gm;

  /** Classes of NetNodes and NetEdges that are to be visualized in
   *  this perspective.
   *  <A HREF="../features.html#multiple_perspectives">
   *  <TT>FEATURE: multiple_perspectives</TT></A>
   */
  protected Vector _allowedNetClasses = new Vector();

  /** Rectangles of where to place nodes that are automatically added. */
  protected Hashtable _nodeTypeRegions = new Hashtable();

  /** True if the second column of automattically placed node
   *  FigNodes should be moved down by half the a node height. */
  protected boolean _stagger = true;

  ////////////////////////////////////////////////////////////////
  // constructors

// //   /** Construct a new LayerPerspective with a default name and do not put
// //    *  it on the Layer's menu. */
// //   public LayerPerspective(NetList net) {
// //     this("LayerPerspective" + numberWordFor(_nextLayerNumbered++), net);
// //   }

  /** Construct a new LayerPerspective with the given name, and add it to
   *  the menu of layers. Needs-More-Work: I have not implemented a
   *  menu of layers yet. I don't know if that is really the right user
   *  interface */
  //public LayerPerspective(String name, NetList net) {
  public LayerPerspective(String name, GraphModel gm) {
    super(name);
    _gm = gm;
    _gm.addGraphEventListener(this);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Reply the NetList of the underlying connected graph. */
  //public NetList net() { return _net; }
  public GraphModel getGraphModel() { return _gm; }

  /** Add a node class of NetNodes or NetEdges to what will be shown in
   *  this perspective.
   *  <A HREF="../features.html#multiple_perspectives">
   *  <TT>FEATURE: multiple_perspectives</TT></A>
   */
  public void allowNetClass(Class c) { _allowedNetClasses.addElement(c); }

  ////////////////////////////////////////////////////////////////
  // Layer API

  /** Add a Fig to the contents of this layer. Items are added on top
   *  of all other items. If a node is explicitly added then accept it
   *  regardless of the predicate, and add it to the net. */
  // needs-more-work: what about Objects that are not NetPrimitives
  public void add(Fig f) {
    super.add(f);
    if (f instanceof FigNode && _gm instanceof MutableGraphModel)
      ((MutableGraphModel)_gm).addNode((NetNode)f.getOwner());
    else if (f instanceof FigEdge && _gm instanceof MutableGraphModel)
      ((MutableGraphModel)_gm).addEdge((NetEdge)f.getOwner());
  }

  /** Remove the given Fig from this layer. */
  public void remove(Fig f) {
    super.remove(f);
    if (f instanceof FigNode && _gm instanceof MutableGraphModel)
      ((MutableGraphModel)_gm).removeNode((NetNode)f.getOwner());
    else if (f instanceof FigEdge && _gm instanceof MutableGraphModel)
      ((MutableGraphModel)_gm).removeEdge((NetEdge)f.getOwner());
  }

  ////////////////////////////////////////////////////////////////
  // node placement

  public void addNodeTypeRegion(Class nodeClass, Rectangle region) {
    _nodeTypeRegions.put(nodeClass, region);
  }

  public void putInPosition(Fig f) {
    Class nodeClass = f.getOwner().getClass();
    Rectangle placementRegion =
      (Rectangle) _nodeTypeRegions.get(nodeClass);
    if (placementRegion != null) {
      f.setLocation(placementRegion.x, placementRegion.y);
      bumpOffOtherNodesIn(f, placementRegion);
    }
  }

  public void bumpOffOtherNodesIn(Fig newFig, Rectangle bounds) {
    Rectangle bbox = newFig.getBounds();
    int col = 0;
    while (bounds.intersects(bbox)) {
      Enumeration overlappers = elementsIn(bbox);
      if (!overlappers.hasMoreElements()) return;
      newFig.translate(0, bbox.height + GAP);
      bbox.y += bbox.height + GAP;
      if (!(bounds.intersects(bbox))) {
	col++;
	int x = bbox.x + bbox.width + GAP;
	int y = bounds.y;
	if (_stagger) y += (col%2)*(bbox.height+GAP)/2;
	newFig.setLocation(x, y);
	bbox.move(x, y);
      }
    }
  }

  ////////////////////////////////////////////////////////////////
  // nofitications and updates

  public void update(Observable o, Object arg) {
    if (arg instanceof Vector) {
      Vector varg = (Vector)arg;
      String s = (String) varg.elementAt(0);
      Object obj = varg.elementAt(1);
      if (obj instanceof NetNode) {
	NetNode node = (NetNode) obj;
	  if (s.equals("add") && shouldShow(node)) {
	  //System.out.println("NetNode added to net: " + node);
	  Fig oldDE = presentationFor(obj);
	  if (null == oldDE) {
	    Fig newFig = node.presentationFor(this);
	    if (newFig != null) {
	      putInPosition(newFig);
	      add(newFig);
	    }
	    else System.out.println("added node de is null");
	  }
	}
      }
      if (obj instanceof NetEdge) {
	NetEdge arc = (NetEdge) obj;
	  if (s.equals("add") && shouldShow(arc)) {
	  Fig oldDE = presentationFor(obj);
	  if (null == oldDE) {
	    Fig newFig = arc.presentationFor(this);
	    if (newFig != null) {
	      add(newFig);
	      ((FigEdge)newFig).computeRoute();
	      newFig.reorder(ActionReorder.SEND_TO_BACK, this);
	      newFig.endTrans();
	    }
	    else System.out.println("added arc fig is null!!!!!!!!!!!!!!!!");
	  }
	}
      }
    }
    super.update(o, arg);
  } /* end update */

  /** Test to determine if a given NetNode should have a FigNode
   *  in this layer.  Normally checks NetNode class against a list of
   *  allowable classes.  For more sophisticated rules, override this
   *  method.
   *  <A HREF="../features.html#multiple_perspectives">
   *  <TT>FEATURE: multiple_perspectives</TT></A>
   */
  public boolean shouldShow(Object obj) {
    if (_allowedNetClasses.size() > 0 &&
	!_allowedNetClasses.contains(obj.getClass()))
      return false;
    if (obj instanceof NetEdge) {
      if (getPortFig(((NetEdge)obj).getSourcePort()) == null ||
	  getPortFig(((NetEdge)obj).getDestPort()) == null)
	return false;
    }
    return true;
  }

  /** Test to determine if a given NetEdge should have an FigEdge
   *  in this layer.  Normally checks NetNode class against a list of
   *  allowable classes.  For more sophisticated rules, override this
   *  method.
   *  <A HREF="../features.html#multiple_perspectives">
   *  <TT>FEATURE: multiple_perspectives</TT></A>
   */
//   public boolean shouldShow(NetEdge a) {
//     if (_allowedNetClasses.size() > 0 &&
// 	!_allowedNetClasses.contains(a.getClass()))
//       return false;
//     if (getPortFig(a.getSourcePort()) == null ||
// 	getPortFig(a.getDestPort()) == null)
//       return false;
//     return true;
//   }

  ////////////////////////////////////////////////////////////////
  // GraphListener implementation

  public void nodeAdded(GraphEvent e) { }
  public void edgeAdded(GraphEvent e) { }
  public void nodeRemoved(GraphEvent e) { }
  public void edgeRemoved(GraphEvent e) { }
  public void graphChanged(GraphEvent e) { }

  
} /* end class LayerPerspective */

