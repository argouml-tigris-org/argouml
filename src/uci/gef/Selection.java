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


// File: Selection.java
// Classes: Selection
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import uci.util.*;

/** This class represents the "selection" object that is used when you
 *  select one or more Figures in the drawing window. Selection's
 *  (should) handle the display of handles or whatever graphics
 *  indicate that something is selected, and they process events to
 *  manipulate the selected Fig. Needs-More-Work: in a
 *  earlier version of this design Fig's did all of that
 *  work themselves which led to much code duplication. Unfortuanately
 *  some of that code is still left over. Specificially,
 *  Fig's still have methods to draw their own handles.<p>
 *  <A HREF="../features.html#selections">
 *  <TT>FEATURE: selections</TT></A>
 *
 * @see Fig
 * @see Fig#drawSelected */

public abstract class Selection extends Observable
implements Serializable, MouseListener, MouseMotionListener, KeyListener {

  ////////////////////////////////////////////////////////////////
  // constants

  public static final int HAND_SIZE = 6;

  /** The margin between the contents bbox and the frame */
  public static final int BORDER_WIDTH = 4;

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** _content refers to the DiagramElement that is selected.
   * Needs-more-work:  this could be a blank-final. */ 
  protected Fig _content; // could be blank final? no...

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new selection. Empty, subclases can override */
  public Selection(Fig f) {
    if (null == f) throw new java.lang.NullPointerException();
    _content = f;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Reply the Fig that was selected */
  public Fig getContent() { return _content; }
  public void setcontent(Fig f) {  _content = f; }

  public boolean getLocked() { return getContent().getLocked(); }

  /** Reply true if this selection contains the given Fig */
  public boolean contains(Fig f) { return f == _content; }


  ////////////////////////////////////////////////////////////////
  // display methods

  /** Paint something that indicates a selection to the user */
  public void print(Graphics g) { paint(g); }
  public abstract void paint(Graphics g);

  /** Tell the content to start a transaction that causes damage */
  public void startTrans() { getContent().startTrans(); }

  /** Tell the content to end a transaction that causes damage */
  public void endTrans() { getContent().endTrans(); }

  /** Reply the position of the Selection. That is defined to be the
   *  upper left corner of my bounding box. */
  public Point getLocation() {
    return _content.getLocation();
  }

  /** This selection object needs to be redrawn, register its damaged
   * area within the given Editor */
  public void damage() { _content.damage(); }

  /** reply true if the given point is inside this selection */
  public boolean contains(int x, int y) {
    return _content.contains(x, y) || hitHandle(x, y, 0, 0) != -1;
  }
  public boolean hit(Rectangle r) {
    return _content.hit(r) || hitHandle(r) != -1;
  }

  public final boolean contains(Point pnt) { return contains(pnt.x, pnt.y); }

  /** Find which handle the user clicked on, or return -1 if none. */
  public abstract int hitHandle(Rectangle r);
  public final int hitHandle(int x, int y, int w, int h) {
    return hitHandle(new Rectangle(x, y, w, h));
  }

    /** Tell the selected Fig to move to front or back, etc. */
  public void reorder(int func, Layer lay) { lay.reorder(_content, func); }

  /** Do nothing because alignment only makes sense for multiple
   * selections */
  public void align(int direction) { /* do nothing */ }

  public void align(Rectangle r, int direction, Editor ed) {
    _content.align(r, direction, ed);
  }

  /** When the selection is told to move, move the selected
   * Fig */
  public void translate(int dx,int dy) { _content.translate(dx, dy); }

  /** The bounding box of the selection is the bbox of the contained
   * Fig. */
  public Rectangle getBounds() {
    return new Rectangle(_content.getX() - HAND_SIZE/2,
			 _content.getY() - HAND_SIZE/2,
			 _content.getWidth() + HAND_SIZE,
			 _content.getHeight() + HAND_SIZE);
  }

  public void stuffBounds(Rectangle r) {
    r.reshape(_content.getX() - HAND_SIZE/2,
	      _content.getY() - HAND_SIZE/2,
	      _content.getWidth() + HAND_SIZE,
	      _content.getHeight() + HAND_SIZE);
  }

  /** If the selection is being deleted, the selected object is
   * deleted also. This is different from just deselecting the
   * selected Fig, to do that use one of the deselect
   * operations in Editor
   * @see Editor#deselect
   */
  public void delete() { _content.delete(); }
  public void dispose() { _content.dispose(); }

  /** Move one of the handles of a selected Fig. */
  public abstract void dragHandle(int mx, int my, int an_x,int an_y, Handle h);

  /** Reply the bounding box of the selected Fig's, does not
   *  include space used by handles. */
  public Rectangle getContentBounds() { return _content.getBounds(); }

  ////////////////////////////////////////////////////////////////
  // event handlers

    /** Pass any events along to the selected Fig.
   * Subclasses of Selection may reimplement this to add
   * functionality.
   */
  public void keyTyped(KeyEvent ke) {
    if (_content instanceof KeyListener)
      ((KeyListener)_content).keyTyped(ke);
  }

  public void keyPressed(KeyEvent ke) {
    if (_content instanceof KeyListener)
      ((KeyListener)_content).keyPressed(ke);
  }
  
  public void keyReleased(KeyEvent ke) { }


  public void mouseMoved(MouseEvent me) {
    if (_content instanceof MouseMotionListener)
      ((MouseMotionListener)_content).mouseMoved(me);
  }

  public void mouseDragged(MouseEvent me) {
    if (_content instanceof MouseMotionListener)
      ((MouseMotionListener)_content).mouseDragged(me);
  }

  public void mousePressed(MouseEvent me) {
    if (_content instanceof MouseListener)
      ((MouseListener)_content).mousePressed(me);
  }

  public void mouseReleased(MouseEvent me) {
    if (_content instanceof MouseListener)
      ((MouseListener)_content).mouseReleased(me);
  }

  public void mouseClicked(MouseEvent me) {
    if (_content instanceof MouseListener)
      ((MouseListener)_content).mouseClicked(me);
  }

  public void mouseEntered(MouseEvent me) {
    if (_content instanceof MouseListener)
      ((MouseListener)_content).mouseEntered(me);
  }

  public void mouseExited(MouseEvent me) {
    if (_content instanceof MouseListener)
      ((MouseListener)_content).mouseExited(me);
  }

} /* end class Selection */
