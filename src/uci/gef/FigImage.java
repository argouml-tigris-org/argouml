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

// File: FigImage.java
// Classes: FigImage
// Original Author: brw@tusc.com.au
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.net.*;

/** Primitive Fig to paint icons on a LayerDiagram.
 *  <A HREF="../features.html#basic_shapes_image">
 *  <TT>FEATURE: basic_shapes_image</TT></A>
 */

public class FigImage extends Fig implements ImageObserver {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The Image being rendered */
  protected transient Image _image;
  protected URL _url;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new FigImage with the given position, size, and
   *  highlight color. The highlight color is used in inverting the
   *  image for highlighting when the icon is selected.  */
  public FigImage(int x, int y, int w, int h, Image img, Color xColor) {
    super(x, y, w, h);
    setLineColor(xColor);
    _image = img;
  }

  /** Construct a new FigImage w/ the given position, size, image, and
   *  attributes */
  public FigImage(int x, int y, int w, int h, Image img, Hashtable gAttrs) {
    super(x, y, w, h);
    setLineColor(Color.white);
    _image = img;
    //put(gAttrs);
  }

  /** Construct a new FigImage w/ the given position, image, and attributes. */
  public FigImage(int x, int y, Image i, Hashtable gAttrs) {
    this(x, y, 0, 0, i, gAttrs);
    setWidth(i.getWidth(this));
    setHeight(i.getHeight(this));
  }

  /** Construct a new FigImage w/ the given position, URL, and attributes. */
  public FigImage(int x, int y, URL imageUrl, Hashtable gAttrs) {
    super(x, y, 0, 0);
    setLineColor(Color.white);
    _url = imageUrl;
    _image = Globals.getImage(_url);
    Globals.waitForImages();
    //put(gAttrs);
    _w = _image.getWidth(this);
    _h = _image.getHeight(this);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  // add get and put for the url...

  ////////////////////////////////////////////////////////////////
  // ImageObserver API

  public boolean imageUpdate(Image img, int infoflags,
			     int x, int y, int w, int h) {
    boolean done=((infoflags&(ERROR | FRAMEBITS | ALLBITS)) != 0);
    return !done;
  }

  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Paint this FigImage on the given Graphics. */
  public void paint(Graphics g) {
    if (_image == null) {
      System.out.println("reloading image");
      if (_url != null) {
	_image = Globals.getImage(_url);
	Globals.waitForImages();
      }
    }

    if (_image != null)
      g.drawImage(_image, _x, _y, _w, _h, this);
    else {
      g.setColor(_fillColor);
      g.fillRect(_x, _y, _w, _h);
    }
  }

  ////////////////////////////////////////////////////////////////
  // Editor API

  public void createDrag(int anchorX, int anchorY, int x, int y,
			 int snapX, int snapY) {
    setLocation(snapX, snapY);
  }


} /* end of FigImage class */

