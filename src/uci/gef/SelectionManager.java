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

// File: SelectionManager.java
// Classes: SelectionManager
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import uci.util.*;
import java.awt.*;

/** This class handles Manager selections. It is basically a
 *  collection of Selection instances. Most of its operations
 *  just dispatch the same operation to each of the Selection
 *  instances in turn.
 *
 * @see Selection
 */

public class SelectionManager extends EventHandler
implements java.io.Serializable {

  /** The collection of Selection instances */
  protected Vector _selections = new Vector();
  protected Editor _editor;

  public SelectionManager(Editor ed) { _editor = ed; }

  /** Add a new selection to the collection of selections */
  public void addSelection(Selection s) { _selections.addElement(s); }
  public void addFig(Fig f) { _selections.addElement(makeSelectionFor(f)); }
  public void addAllFigs(Vector v) {
    Enumeration eles = v.elements();
    while(eles.hasMoreElements()) addFig((Fig) eles.nextElement());
  }

  public void removeAllElements() { _selections.removeAllElements(); }
  public void removeSelection(Selection s) {
    if (s != null) _selections.removeElement(s);
  }
  public void removeFig(Fig f) { removeSelection(findSelectionFor(f)); }

  public void allDamaged() { _editor.damaged(this.getBounds()); }

  public void select(Fig f) {
    allDamaged();
    removeAllElements();
    addFig(f);
    _editor.damaged(f);
  }

  /** Deselect the given Fig */
  public void deselect(Fig f) {
    if (containsFig(f)) {
      removeFig(f);
      _editor.damaged(f);
    }
  }

  public void toggle(Fig f) {
    _editor.damaged(f);
    if (containsFig(f)) removeFig(f);
    else addFig(f);
    _editor.damaged(f);
  }


  public void deselectAll() {
    Rectangle damagedArea = this.getBounds(); // too much area
    removeAllElements();
    _editor.damaged(damagedArea);
  }

  public void select(Vector items) {
    allDamaged();
    removeAllElements();
    addAllFigs(items);
    allDamaged();
  }


  public void toggle(Vector items) {
    allDamaged();
    Enumeration figs = ((Vector)items.clone()).elements();
    while(figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      if (containsFig(f)) removeFig(f);
      else addFig(f);
    }
    allDamaged();
  }

  public Selection findSelectionFor(Fig f) {
    Enumeration sels = ((Vector)_selections.clone()).elements();
    while(sels.hasMoreElements()) {
      Selection sel = (Selection) sels.nextElement();
      if (sel.contains(f)) return sel;
    }
    return null;
  }



  /** Reply true if the given selection instance is part of my
   * collection */
  public boolean contains(Selection s) { return _selections.contains(s); }

  /** Reply true if the given Fig is selected by any of my
   * selection objects */
  public boolean containsFig(Fig f) { return findSelectionFor(f) != null; }

  public boolean getLocked() {
    Enumeration sels = _selections.elements();
    while (sels.hasMoreElements())
      if (((Selection) sels.nextElement()).getLocked()) return true;
    return false;
  }

  /** Reply the number of selected Fig's. This assumes that
   * this collection holds only Selection instances and each of
   * those holds one Fig */
  public int size() { return _selections.size(); }

  public Vector selections() { return _selections; }

  /** Reply the collection of all selected Fig's */
  public Vector getFigs() {
    Vector figs = new Vector(_selections.size());
    Enumeration sels = _selections.elements();
    while (sels.hasMoreElements())
      figs.addElement(((Selection) sels.nextElement()).getContent());
    return figs;
  }

  /** Start a transaction that damages all selected Fig's */
  public void startTrans() {
    Enumeration ss = _selections.elements();
    while (ss.hasMoreElements()) ((Selection)ss.nextElement()).startTrans();
  }

  /** End a transaction that damages all selected Fig's */
  public void endTrans() {
    Enumeration sels = _selections.elements();
    while (sels.hasMoreElements()) ((Selection) sels.nextElement()).endTrans();
  }

  /** Paint all selection objects */
  public void paint(Graphics g) {
    Enumeration sels = _selections.elements();
    while (sels.hasMoreElements()) ((Selection) sels.nextElement()).paint(g);
  }


  /** When the SelectionManager is damaged, that implies that each
  * Selection should be damaged. */
  public void damagedIn(Editor ed) {
    Enumeration ss = _selections.elements();
    while (ss.hasMoreElements()) ((Selection)ss.nextElement()).damagedIn(ed);
  }

  /** Reply true iff the given point is inside one of the selected Fig's */
  public boolean contains(int x, int y) {
    Enumeration sels = _selections.elements();
    while (sels.hasMoreElements())
      if (((Selection) sels.nextElement()).contains(x, y)) return true;
    return false;
  }

  /** Reply true iff the given point is inside one of the selected
   * Fig's */
  public boolean hit(Rectangle r) {
    Enumeration sels = _selections.elements();
    while (sels.hasMoreElements())
      if (((Selection) sels.nextElement()).hit(r)) return true;
    return false;
  }

  public Rectangle getBounds() {
    Rectangle r = null;
    Enumeration sels = _selections.elements();
    if (sels.hasMoreElements())
      r = ((Selection) sels.nextElement()).getBounds();
    else return new Rectangle(0, 0, 0, 0);

    while(sels.hasMoreElements()) {
      Selection sel = (Selection) sels.nextElement();
      r.add(sel.getBounds());
    }
    return r;
  }

  public Rectangle getContentBounds() {
    Rectangle r = null;
    Enumeration sels = _selections.elements();
    if (sels.hasMoreElements())
      r = ((Selection) sels.nextElement()).getContentBounds();
    else return new Rectangle(0, 0, 0, 0);

    while(sels.hasMoreElements()) {
      Selection sel = (Selection) sels.nextElement();
      r.add(sel.getContentBounds());
    }
    return r;
  }

  /** Align the selected Fig's relative to each other */
  /* needs-more-work: more of this logic should be in ActionAlign */
  public void align(int dir) {
    Editor ed = Globals.curEditor();
    Rectangle bbox = getContentBounds();
    Enumeration ss = _selections.elements();
    while (ss.hasMoreElements())
      ((Selection) ss.nextElement()).align(bbox, dir, ed);
  }

  public void align(Rectangle r, int dir, Editor ed) {
    Enumeration ss = _selections.elements();
    while(ss.hasMoreElements()) ((Selection)ss.nextElement()).align(r,dir,ed);
  }

  /** When Manager selections are sent to back, each of them is sent
   * to back. */
  public void reorder(int func, Layer lay) {
    Enumeration ss = _selections.elements();
    while(ss.hasMoreElements())((Selection)ss.nextElement()).reorder(func,lay);
  }

  /** When Manager selections are moved, each of them is moved */
  public void translate(int dx, int dy) {
    Enumeration ss = _selections.elements();
    while(ss.hasMoreElements())((Selection)ss.nextElement()).translate(dx, dy);
  }

  /** If only one thing is selected, then it is possible to mouse on
   * one of its handles, but if Manager things are selected, users
   * can only drag the objects around */
  /* needs-more-work: should take on more of this responsibility */
  public int hitHandle(Rectangle r) {
    if (size() != 1) return -1;
    int hp = ((Selection) _selections.firstElement()).hitHandle(r);
    return hp;
  }

  /** If only one thing is selected, then it is possible to mouse on
   * one of its handles, but if Manager things are selected, users
   * can only drag the objects around */
  public void dragHandle(int mx, int my, int an_x,int an_y, Handle h) {
    if (size() != 1) return;
    Selection sel =  ((Selection) _selections.firstElement());
    sel.dragHandle(mx, my, an_x, an_y, h);
  }

  /** When a multiple selection is removed, each selection is removed */
  public void removeFrom(Editor ed) {
    Enumeration ss = ((Vector)_selections.clone()).elements();
    while (ss.hasMoreElements()) ((Selection)ss.nextElement()).removeFrom(ed);
  }

  /** When a multiple selection is disposed, each selection is disposed */
  public void dispose(Editor ed) {
    Enumeration sels = ((Vector)_selections.clone()).elements();
    while (sels.hasMoreElements()) ((Selection)sels.nextElement()).dispose(ed);
  }

  /** When an event is passed to a multiple selection, try to pass it
   * off to the first selection that will handle it. */
  /* needs-more-work: maybe it should pass it to _all_ selections that
     will handle it */
  public boolean keyDown(Event e,int key) {
    Enumeration sels = ((Vector)_selections.clone()).elements();
    while(sels.hasMoreElements())
      if (((Selection) sels.nextElement()).keyDown(e, key)) return true;
    return false;
  }

  public boolean mouseMove(Event e,int x,int y) {
    Enumeration sels = _selections.elements();
    while(sels.hasMoreElements())
      if (((Selection) sels.nextElement()).mouseMove(e, x, y)) return true;
    return false;
  }

  public boolean mouseDrag(Event e,int x,int y) {
    Enumeration sels = _selections.elements();
    while(sels.hasMoreElements())
      if (((Selection) sels.nextElement()).mouseDrag(e, x, y)) return true;
    return false;
  }

  public boolean mouseDown(Event e,int x,int y) {
    Enumeration sels = ((Vector)_selections.clone()).elements();
    while(sels.hasMoreElements())
      if (((Selection) sels.nextElement()).mouseDown(e, x, y)) return true;
    return false;
  }

  public boolean mouseUp(Event e,int x,int y) {
    Enumeration sels = ((Vector)_selections.clone()).elements();
    while(sels.hasMoreElements())
      if (((Selection) sels.nextElement()).mouseUp(e, x, y)) return true;
    return false;
  }


  ////////////////////////////////////////////////////////////////
  // static methods

  protected static Hashtable _SelectionRegistry = new Hashtable();

  public static Selection makeSelectionFor(Fig f) {
//     Class sc = (Class) _SelectionRegistry.get(f.classClass());
//     if (sc.isAssignableFrom(Selection.class)) {
//       Selection s = (Selection) sc.newInstance();
//       s.setContent(f);
//       return s;
//     }
    // needs-more-work: how do I choose if a fig can do multiple of
    // the following?
    //if (f.isRotatable()) return new SelectionResize(f);
    if (f.isReshapable()) return new SelectionReshape(f);
    else if (f.isResizable()) return new SelectionResize(f);
    else if (f.isMovable()) return new SelectionMove(f);
    else return new SelectionNoop(f);
  }

} /* end class SelectionManager */
