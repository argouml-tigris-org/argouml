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




// File: ModeCreate.java
// Classes: ModeCreate
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.*;

/** Abstract superclass for all Mode's that create new
 *  Figs.  This class factors our shared code that would
 *  otherwise be duplicated in its subclasses.  On a mouse down the new
 *  item is created in memory.  On mouse drag the new item is resized
 *  via its createDrag() method.  On mouse up the new item is
 *  officially added to the Layer being edited in the parent
 *  Editor, the item is selected, and the Editor is placed in the next
 *  Mode (usually ModeSelect).  Subclasses override various of these
 *  event handlers to give specific behaviors, for example,
 *  ModeCreateEdge handles dragging differently.
 *
 * @see ModeCreateEdge
 * @see ModeCreateFigRect
 * @see ModeCreateFigRRect
 * @see ModeCreateFigLine
 * @see ModeCreateFigCircle
 * @see ModeCreateFigPoly
 * @see ModeCreateFigInk
 * @see ModeCreateFigText
 * @see ModeCreateFigImage
 */

public abstract class ModeCreate extends Mode {
  ////////////////////////////////////////////////////////////////
  // static variables

  /** The default size of a Fig if the user simply clicks instead of
   *  dragging out a size. */
  protected static int _defaultWidth = 32, _defaultHeight = 32;



  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Original mouse down event coordinates */
  protected int anchorX, anchorY;

  /** This holds the Fig to be added to the parent Editor. */
  protected Fig _newItem;

  ////////////////////////////////////////////////////////////////
  // constructors

  public ModeCreate(Editor par) { super(par); }

  public ModeCreate() { super(); }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** By default all creation modes use CROSSHAIR_CURSOR. */
  public Cursor getInitialCursor() {
    return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
  }


  ////////////////////////////////////////////////////////////////
  // event handlers

  private Point snapPt = new Point(0, 0);

  /** On mouse down, make a new Fig in memory. */
  public void mousePressed(MouseEvent me) {
    if (me.isConsumed()) return;
    start();
    synchronized (snapPt) {
      snapPt.setLocation(me.getX(), me.getY());
      _editor.snap(snapPt);
      anchorX = snapPt.x;
      anchorY = snapPt.y;
    }
    _newItem = createNewItem(me, anchorX, anchorY);
    me.consume();
    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
  }

  /** On mouse drag, resize the new item as the user moves the
   *  mouse.  Maybe the Fig createDrag() method should be removed
   *  and I should call dragHandle(). That would elimiate one method
   *  from each oif several classes, but dragging during creation is
   *  not really the same thing as dragging after creation....<p>
   *
   *  Note: _newItem has not been added to any Layer yet. So you cannot
   *  use _newItem.damage(), instead use _editor.damaged(_newItem). */
  public void mouseDragged(MouseEvent me) {
    if (me.isConsumed()) return;
    if (_newItem != null) {
      _editor.damaged(_newItem);
      creationDrag(me.getX(), me.getY());
      _editor.damaged(_newItem);
    }
    _editor.scrollToShow(me.getX(), me.getY());
    me.consume();
  }

  /** On mouse up, officially add the new item to the parent Editor
   *  and select it. Then exit this mode. */
  public void mouseReleased(MouseEvent me) {
    if (me.isConsumed()) return;
    if (_newItem != null) {
      _editor.damaged(_newItem);
      creationDrag(me.getX(), me.getY());
      _editor.add(_newItem);
      _editor.getSelectionManager().select(_newItem);
      _newItem = null;
    }
    done();
    me.consume();
  }

  /** Update the new item to reflect the new mouse position. By
   *  default let the new item set its size, subclasses may
   *  override. If the user simply clicks instead of dragging then use
   *  the default size. If the user actually drags out a Fig, then
   *  use its size as the new default size.
   *
   * @see ModeCreateFigLine#creationDrag */
  protected void creationDrag(int x, int y) {
    if (_newItem == null) return;
    int snapX, snapY;
    synchronized (snapPt) {
      snapPt.setLocation(x, y);
      _editor.snap(snapPt);
      snapX = snapPt.x;
      snapY = snapPt.y;
    }
    if (anchorX == snapX && anchorY == snapY)
      ((Fig)_newItem).createDrag(anchorX, anchorY,
				x + _defaultWidth,
				y + _defaultHeight,
				snapX + _defaultWidth,
				snapY + _defaultHeight);
    else {
      ((Fig)_newItem).createDrag(anchorX, anchorY, x, y, snapX, snapY);
      _defaultWidth = snapX - anchorX;
      _defaultHeight = snapY - anchorY;
    }
  }

  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Paint this mode by painting the new item. This is the only
   *  feedback that the user will get since the new item is not
   *  officially added to the Editor's document yet. */
  public void paint(Graphics g) {
    if (null != _newItem) _newItem.paint(g);
  }

  ////////////////////////////////////////////////////////////////
  // ModeCreate API

  /** Abstact method to construct a new Fig to be added to the Editor.
   *  Typically, subclasses will make a new instance of some Fig based
   *  on the given mouse down event and the state of the parent Editor
   *  (specifically, its default graphical attributes). */
  public abstract Fig createNewItem(MouseEvent me, int snapX, int snapY);

  static final long serialVersionUID = -997186474502969920L;
} /* end class ModeCreate */

