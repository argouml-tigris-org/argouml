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

// File: ModeCreate.java
// Classes: ModeCreate
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;

/** Abstract superclass for all Mode's that create new
 *  Fig's. This class factors our shared code that would
 *  otherwise be duplicated in its subclasses. On a mouse down the new
 *  item is created in memory. On mouse drag the new item is resized
 *  via its createDrag() method. On mouse up the new item is
 *  officially added to the document being edited in the parent
 *  Editor, the item is selected, and the Editor is placed in the next
 *  Mode (usually ModeSelect). Subclasses override various of these
 *  event handlers to give specific behaviors, for example,
 *  ModeCreateArc handles dragging differently.
 *
 * @see ModeCreateArc
 * @see ModeCreateFigRect */

public abstract class ModeCreate extends Mode {
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
  // event handlers

  private Point snapPt = new Point(0, 0);

  /** On mouse down, make a new Fig in memory. */
  public boolean mouseDown(Event e, int x, int y) {
    start();
    synchronized (snapPt) {
      snapPt.move(x, y);
      _editor.snap(snapPt);
      anchorX = snapPt.x;
      anchorY = snapPt.y;
    }
    _newItem = createNewItem(e, anchorX, anchorY);
    return true;
  }

  /** On mouse drag, resize the new item as the user moves the
   *  mouse. Maybe the Fig createDrag() method should be removed
   *  and I should call dragHandle(). That would elimiate one method
   *  from each oif several classes, but dragging during creation is
   *  not really the same thing as dragging after creation... */
  public boolean mouseDrag(Event e, int x, int y) {
    _editor.damaged(_newItem);
    creationDrag(x, y);
    _editor.damaged(_newItem);
    return true;
  }

  /** On mouse up, officially add the new item to the parent Editor
   *  and select it. Then exit this mode. */
  public boolean mouseUp(Event e, int x, int y) {
    if (_newItem != null) {
      _editor.damaged(_newItem);
      creationDrag(x, y);
      _editor.add(_newItem);
      _editor.select(_newItem);
      _newItem = null;
    }
    done();
    return true;
  }

  /** The default size of a Fig if the user simply clicks instead of
   *   dragging.
   *  <A HREF="../features.html#default_initial_size">
   *  <TT>FEATURE: default_initial_size</TT></A>
   */
  protected static int _defaultWidth = 32, _defaultHeight = 32;

  /** Update the new item to reflect the new mouse position. By
   *  default let the new item set its size, subclasses may
   *  override. If the user simply clicks instead of dragging then use
   *  the default size. If the user actually drags out a Fig, then
   *  use its size as the new default size.
   *  <A HREF="../features.html#default_initial_size">
   *  <TT>FEATURE: default_initial_size</TT></A>
   *
   * @see ModeCreateFigLine#creationDrag */
  protected void creationDrag(int x, int y) {
    int snapX, snapY;
    synchronized (snapPt) {
      snapPt.move(x, y);
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

  /** Construct a new Fig to be added to the
   *  Editor. Typically, subclasses will make a new instance of some Fig
   *  based on the given mouse down event and the state of the parent
   *  Editor (specifically, its default graphical attributes). */
  public abstract Fig createNewItem(Event e, int snapX, int snapY);

} /* end class ModeCreate */

