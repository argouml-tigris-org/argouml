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

// File: ModeCreateFigImage.java
// Classes: ModeCreateFigImage
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.MouseEvent;

/** A Mode to interpert user input while creating a FigImage. All of
 *  the actual event handling is inherited from ModeCreate. This class
 *  just implements the differences needed to make it specific to
 *  rectangles.
 *  <A HREF="../features.html#basic_shapes_image">
 *  <TT>FEATURE: basic_shapes_image</TT></A>
 */

public class ModeCreateFigImage extends ModeCreate {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The image to be placed. */
  protected Image _image;

  ////////////////////////////////////////////////////////////////
  // accessors

  /** get and set the image to be used for the new FigImage. */
  public Image image() { return _image; }
  public void image(Image i) { _image = i; }

  ////////////////////////////////////////////////////////////////
  // Mode API

  public String instructions() {
    return "Click to place an image";
  }

  ////////////////////////////////////////////////////////////////
  // ModeCreate API

  /** Create a new FigImage instance based on the given mouse down
   *  event and the state of the parent Editor. */
  public Fig createNewItem(MouseEvent me, int snapX, int snapY) {
    if (null == _image) {
      String dURL = "http://www.ics.uci.edu/~jrobbins/banners/gef_banner.gif";
      String urlString = (String) _args.get("imageURL");
      if (urlString == null) urlString = dURL;
      _image = Globals.getImage(urlString);
      Globals.waitForImages();
    }
    return new FigImage(snapX, snapY, _image, _editor.graphAttrs());
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** When the mouse enters an Editor, create the FigImage and place
   *  it at the mouse position. */
  public void mouseEntered(MouseEvent me) {
    int x = me.getX(), y = me.getY();    
    start();
    anchorX = x;
    anchorY = y;
    Point snapPt = new Point(x, y);
    _editor.snap(snapPt);
    if (null == _newItem) _newItem = createNewItem(me, snapPt.x, snapPt.y);
    me.consume();
  }

  /** When the mouse exits the editor, clean up the display a little. */
  public void mouseExited(MouseEvent me) {
    _editor.damaged(_newItem);
    me.consume();
  }

  /** On mouse down, do nothing. */
  public void mousePressed(MouseEvent me) {
    me.consume();
  }

  /** Whem the user drags or moves the mouse, move the FigImage to the
   *  current mouse position. */
  public void mouseMoved(MouseEvent me) {
    int x = me.getX(), y = me.getY();
    if (_newItem == null) {
      System.out.println("null _newItem");
      me.consume();
      return;
    }
    _editor.damaged(_newItem);
    Point snapPt = new Point(x, y);
    _editor.snap(snapPt);
    _newItem.setLocation(snapPt.x, snapPt.y);
    _editor.damaged(_newItem); /* needed? */
    me.consume();
  }

  /** Exactly the same as mouseMove. */
  public void mouseDragged(MouseEvent me) {
    mouseMoved(me);
  }

} /* end class ModeCreateFigImage */

