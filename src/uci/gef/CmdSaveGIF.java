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

import com.sun.java.util.collections.*;
import java.util.Enumeration;
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

public class CmdSaveGIF extends CmdSaveGraphics {

  /** Used as background color in image and set transparent. Chosen because
   *  it's unlikely to be selected by the user, and leaves the diagram readable
   *  if viewed without transparency. */

  public static final int TRANSPARENT_BG_COLOR = 0x00efefef;


  public CmdSaveGIF() {
    super("Save GIF...", NO_ICON);
  }

  /** Write the diagram contained by the current editor into an OutputStream
   *  as a GIF image. */
  protected void saveGraphics(OutputStream s, Editor ce,
			      Rectangle drawingArea)
                 throws IOException {

    //	Create an offscreen image and render the diagram into it.

    Image i = ce.createImage( drawingArea.width, drawingArea.height );
    Graphics g = i.getGraphics();
    g.setColor( new Color(TRANSPARENT_BG_COLOR) );
    g.fillRect( 0, 0, drawingArea.width, drawingArea.height );
    // a little extra won't hurt
    g.translate( -drawingArea.x, -drawingArea.y );
    ce.print( g );

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
