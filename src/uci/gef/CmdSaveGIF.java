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

// File: CmdSaveGIF.java
// Classes: CmdSaveGIF
// Original Author: stevep@wrq.com

package uci.gef;

import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

import Acme.JPM.Encoders.GifEncoder;


/** Cmd to save a diagram as a GIF image in a supplied OutputStream. 
 *  Requires the Acme.JPM.Encoders.GifEncoder class. Operates on the
 *  diagram in the current editor.
 *
 *  Code loosely adapted from CmdPrint.
 *
 *  @author Steve Poole, stevep@wrq.com
 */

public class CmdSaveGIF extends Cmd {

  /** Used as background color in image and set transparent. Chosen because
   *  it's unlikely to be selected by the user, and leaves the diagram readable
   *  if viewed without transparency. */

  public static final int TRANSPARENT_BG_COLOR = 0x00efefef;


  public CmdSaveGIF() {
    super("Save GIF...", NO_ICON);
  }

  /** Set the outputStream argument. This must be done prior to saving
   *  the image.
   *
   *  @param s	the OutputStream into which the image will be saved */

  public void setStream( OutputStream s ) {
    setArg( "outputStream", s );
  }

  /** Write the diagram contained by the current editor into an OutputStream
   *  as a GIF image. The "outputStream" argument must have been previously
   *  set with setStream(). */

  public void doIt() {
    //	FIX - what's the global exception handling strategy?
    //	Should this method ensure that no exceptions are propagated?

    Editor ce = Globals.curEditor();
    OutputStream s = (OutputStream)getArg( "outputStream" );

    //	Determine the bounds of the diagram.
    //
    //	FIX - this is a little glitchy. It appears that some elements
    //	will underreport their size and others will overreport. Various
    //	line styles seem to have the problem. Haven't spent any time
    //	trying to figure it out.

    int xmin = 99999, ymin = 99999;
    Fig f = null;
    Rectangle rectSize = null;
    Rectangle drawingArea = new Rectangle( 0, 0 );
    Enumeration enum = ce.figs();
    while( enum.hasMoreElements() ) {
      f = (Fig) enum.nextElement();
      rectSize = f.getBounds();
      xmin = Math.min( xmin, rectSize.x );
      ymin = Math.min( ymin, rectSize.y );
      drawingArea.add( rectSize );
    }

    //	Create an offscreen image and render the diagram into it.
    //	Tell the editor to hide the grid before doing so.

    boolean h = ce.getGridHidden();
    ce.setGridHidden( true );

    int width = drawingArea.width - xmin;
    int height = drawingArea.height - ymin;
    Image i = ce.createImage( width, height );
    Graphics g = i.getGraphics();
    g.setColor( new Color(TRANSPARENT_BG_COLOR) );
    g.fillRect( 0, 0, drawingArea.width, drawingArea.height );
    // a little extra won't hurt
    g.translate( -xmin, -ymin );
    ce.print( g );
    ce.setGridHidden( h );

    //	Tell the Acme GIF encoder to save the image as a GIF into the
    //	output stream. Use the TransFilter to make the background
    //	color transparent.

    try {
      FilteredImageSource fis =
	new FilteredImageSource( i.getSource(),
				 new TransFilter( TRANSPARENT_BG_COLOR ) );
      GifEncoder ge = new GifEncoder( fis, s );
      //GifEncoder ge = new GifEncoder( i, s );
      ge.encode();
    }
    catch( IOException e ) {
      System.out.println( "GifEncoder failed: " + e );
    }

    g.dispose();
    g = null;
  }

  /**
   *	Undo stub. No useful implementation.
   */

  public void undoIt() {
    System.out.println( "Undo does not make sense for CmdSaveGIF" );
  }

} /* end class CmdSaveGIF */


/**
 * RGBImageFilter that turns on transparency for pixels of a specified color.
 */

class TransFilter extends RGBImageFilter {
  int _transBG;

  public TransFilter( int bg ) {
    _transBG = bg;
    canFilterIndexColorModel = true;
  }

  public int filterRGB( int x, int y, int rgb ) {
    // background color w/any alpha level? make it transparent
    if( (rgb & 0x00ffffff) == _transBG ) return _transBG;
    return 0xff000000 | rgb;  // make it 100% opaque
  }
} /* end class TransFilter */
