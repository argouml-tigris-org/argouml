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

// File: CmdSaveGraphics.java
// Classes: CmdSaveGraphics
// Original Author: wienberg@informatik.uni-hamburg.de

package uci.gef;

import com.sun.java.util.collections.*;
import java.util.Enumeration;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

/** Abstract Cmd to save a diagram as Graphics in a supplied OutputStream. 
 *  Operates on the diagram in the current editor.
 *
 *  @author Frank Wienberg, wienberg@informatik.uni-hamburg.de
 */

public abstract class CmdSaveGraphics extends Cmd {

  protected abstract void saveGraphics(OutputStream s, Editor ce,
				       Rectangle drawingArea)
                          throws IOException;

  protected CmdSaveGraphics(String name, boolean useIcon) {
    super(name, useIcon);
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

    drawingArea.width -= xmin;
    drawingArea.height -= ymin;
    drawingArea.x = xmin;
    drawingArea.y = ymin;
    drawingArea.grow(4,4); // security border

    System.out.println("Bounding box: "+drawingArea);

    //	Tell the editor to hide the grid before exporting:

    boolean h = ce.getGridHidden();
    ce.setGridHidden( true );

    //  Now, do the real work:
    try {
	saveGraphics(s, ce, drawingArea);
    } catch (java.io.IOException e) {
        System.out.println("Error while exporting Graphics:");
	System.out.println(e.toString());
    }

    //  Restore old grid state:
    ce.setGridHidden( h );

  }

  /**
   *	Undo stub. No useful implementation.
   */

  public void undoIt() {
    System.out.println( "Undo does not make sense for CmdSavePS" );
  }

} /* end class CmdSaveGraphics */
