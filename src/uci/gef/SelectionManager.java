// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
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


// File: SelectionManager.java
// Classes: SelectionManager
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import com.sun.java.swing.event.EventListenerList;

import uci.util.*;
import uci.gef.event.*;


/** This class handles Manager selections. It is basically a
 *  collection of Selection instances. Most of its operations
 *  just dispatch the same operation to each of the Selection
 *  instances in turn.<p>
 *
 *  The SelectionManager is also responsible for sending out
 *  GraphSelectionEvents to any GraphSelectionListeners that are
 *  registered.
 *
 * @see Selection */

public class SelectionManager
implements Serializable, KeyListener, MouseListener, MouseMotionListener {

  /** The collection of Selection instances */
  protected Vector _selections = new Vector();
  protected Editor _editor;

  protected EventListenerList _listeners = new EventListenerList();

  
  public SelectionManager(Editor ed) { _editor = ed; }

  /** Add a new selection to the collection of selections */
  protected void addSelection(Selection s) {
    _selections.addElement(s);
  }
  protected void addFig(Fig f) {
    _selections.addElement(makeSelectionFor(f));
  }
  protected void addAllFigs(Vector v) {
    Enumeration eles = v.elements();
    while(eles.hasMoreElements()) addFig((Fig) eles.nextElement());
  }

  protected void removeAllElements() {
    _selections.removeAllElements();
  }
  protected void removeSelection(Selection s) {
    if (s != null) _selections.removeElement(s);
  }
  protected void removeFig(Fig f) {
    Selection s = findSelectionFor(f);
    if (s != null) _selections.removeElement(s);
  }

  protected void allDamaged() { _editor.damaged(this.getBounds()); }

  public void select(Fig f) {
    allDamaged();
    removeAllElements();
    addFig(f);
    _editor.damaged(f);
    fireSelectionChanged();
  }

  /** Deselect the given Fig */
  public void deselect(Fig f) {
    if (containsFig(f)) {
      removeFig(f);
      _editor.damaged(f);
      fireSelectionChanged();
    }
  }

  public void toggle(Fig f) {
    _editor.damaged(f);
    if (containsFig(f)) removeFig(f);
    else addFig(f);
    _editor.damaged(f);
    fireSelectionChanged();
  }


  public void deselectAll() {
    Rectangle damagedArea = this.getBounds(); // too much area
    removeAllElements();
    _editor.damaged(damagedArea);
    fireSelectionChanged();
  }

  public void select(Vector items) {
    allDamaged();
    removeAllElements();
    addAllFigs(items);
    allDamaged();
    fireSelectionChanged();
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
    fireSelectionChanged();
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
  public void damage() {
    Enumeration ss = _selections.elements();
    while (ss.hasMoreElements()) ((Selection)ss.nextElement()).damage();
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

  /** When a multiple selection are deleted, each selection is deleted */
  public void delete() {
    Enumeration ss = ((Vector)_selections.clone()).elements();
    while (ss.hasMoreElements()) ((Selection)ss.nextElement()).delete();
  }


  /** When a multiple selection are deleted, each selection is deleted */
  public void dispose() {
    Enumeration ss = ((Vector)_selections.clone()).elements();
    while (ss.hasMoreElements()) ((Selection)ss.nextElement()).dispose();
  }


  ////////////////////////////////////////////////////////////////
  // input events
  
  /** When an event is passed to a multiple selection, try to pass it
   * off to the first selection that will handle it. */
  public void keyTyped(KeyEvent ke) {
    Enumeration sels = ((Vector)_selections.clone()).elements();
    while (sels.hasMoreElements() && !ke.isConsumed())
      ((Selection) sels.nextElement()).keyTyped(ke);
  }

  public void keyReleased(KeyEvent ke) { }

  public void keyPressed(KeyEvent ke) {
    Enumeration sels = ((Vector)_selections.clone()).elements();
    while (sels.hasMoreElements() && !ke.isConsumed())
      ((Selection) sels.nextElement()).keyPressed(ke);
  }
  
  public void mouseMoved(MouseEvent me) {
    Enumeration sels = _selections.elements();
    while (sels.hasMoreElements() && !me.isConsumed())
      ((Selection) sels.nextElement()).mouseMoved(me);
  }

  public void mouseDragged(MouseEvent me) {
    Enumeration sels = _selections.elements();
    while (sels.hasMoreElements() && !me.isConsumed())
      ((Selection) sels.nextElement()).mouseDragged(me);
  }

  public void mouseClicked(MouseEvent me) {
    Enumeration sels = ((Vector)_selections.clone()).elements();
    while (sels.hasMoreElements() && !me.isConsumed())
      ((Selection) sels.nextElement()).mouseClicked(me);
  }

  public void mousePressed(MouseEvent me) {
    Enumeration sels = ((Vector)_selections.clone()).elements();
    while (sels.hasMoreElements() && !me.isConsumed())
      ((Selection) sels.nextElement()).mousePressed(me);
  }

  public void mouseReleased(MouseEvent me) {
    Enumeration sels = ((Vector)_selections.clone()).elements();
    while (sels.hasMoreElements() && !me.isConsumed())
      ((Selection) sels.nextElement()).mouseReleased(me);
  }

  public void mouseExited(MouseEvent me) {
    Enumeration sels = ((Vector)_selections.clone()).elements();
    while (sels.hasMoreElements() && !me.isConsumed())
      ((Selection) sels.nextElement()).mouseExited(me);
  }

  public void mouseEntered(MouseEvent me) {
    Enumeration sels = ((Vector)_selections.clone()).elements();
    while (sels.hasMoreElements() && !me.isConsumed())
      ((Selection) sels.nextElement()).mouseEntered(me);
  }

  ////////////////////////////////////////////////////////////////
  // graph events
  
  public void addGraphSelectionListener(GraphSelectionListener listener) {
    _listeners.add(GraphSelectionListener.class, listener);
  }

  public void removeGraphSelectionListener(GraphSelectionListener listener) {
    _listeners.remove(GraphSelectionListener.class, listener);
  }

  protected void fireSelectionChanged() {
    Object[] listeners = _listeners.getListenerList();
    GraphSelectionEvent e = null;
    
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == GraphSelectionListener.class) {
	if (e == null) e = new GraphSelectionEvent(_editor, getFigs());
	//needs-more-work: should copy vector, use JGraph as src?
	((GraphSelectionListener)listeners[i+1]).selectionChanged(e);
      }
    }
    updatePropertySheet();
  }
  

  ////////////////////////////////////////////////////////////////
  // property sheet methods

  public void updatePropertySheet() {
    if (_selections.size() != 1) Globals.propertySheetSubject(null);
    else {
      Fig f = (Fig) getFigs().elementAt(0);
      Globals.propertySheetSubject(f);
    }
  }
  
  ////////////////////////////////////////////////////////////////
  // static methods

  protected static Hashtable _SelectionRegistry = new Hashtable();

  public static Selection makeSelectionFor(Fig f) {
    //if (f.isRotatable()) return new SelectionRotate(f);
    if (f.isReshapable()) return new SelectionReshape(f);
    else if (f.isResizable()) return new SelectionResize(f);
    else if (f.isMovable()) return new SelectionMove(f);
    else return new SelectionNoop(f);
  }

} /* end class SelectionManager */
