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





// File: FigImage.java
// Classes: FigImage
// Original Author: brw@tusc.com.au
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.net.*;

/** Primitive Fig to paint images (such as icons) on a LayerDiagram. */

public class FigImage extends Fig implements ImageObserver {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The Image being rendered */
  protected transient Image _image;

  /** The URL of the Image being rendered */
  protected URL _url;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new FigImage with the given position, size, and Image.  */
  public FigImage(int x, int y, int w, int h, Image img) {
    super(x, y, w, h);
    _image = img;
  }


  /** Construct a new FigImage w/ the given position and image. */
  public FigImage(int x, int y, Image i) {
    this(x, y, 0, 0, i);
    setWidth(i.getWidth(this));
    setHeight(i.getHeight(this));
  }

  /** Construct a new FigImage w/ the given position and URL. */
  public FigImage(int x, int y, URL imageUrl) {
    super(x, y, 0, 0);
    _url = imageUrl;
    _image = Globals.getImage(_url);
    Globals.waitForImages();
    _w = _image.getWidth(this);
    _h = _image.getHeight(this);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  // needs-more-work: add get and put for the url...

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

static final long serialVersionUID = 3355684374450331076L;

} /* end of FigImage class */

