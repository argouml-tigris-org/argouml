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




// File: LayerGrid.java
// Classes: LayerGrid
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** Paint a background drawing guide consisting of horizontal and
 *  vertical lines in a neutral color. This feature is common to many
 *  drawing applications (e.g., MacDraw).  LayerGrid is in concept a
 *  Layer, just like any other so it can be composed, locked, grayed,
 *  hidden, and reordered. <p>
 *
 *  Needs-More-Work: Since I am using image stamping and AWT does not
 *  support transparent pixels, any Layer underneath an instance of
 *  LayerGrid will not be seen. Image stamping is nice because it is
 *  fast, but I need to think of some other fast way to do it... */


public class LayerGrid extends Layer {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The spacing between the lines. */
  private int _spacing = 16;

  /** True means paint grid lines, false means paint only dots where the
   *  lines would intersect. Painting dots is about as useful as painting
   *  lines and it looks less cluttered. But lines are more familiar to
   *  some people. */
  private boolean _paintLines = false;

  /** The image stamp is used to paint the grid quickly. Basically I
   *  make a pretty big off screen image and paint my grid on it, then I
   *  repeatedly bitblt that image to the screen (or some other off
   *  screen image). More pixels are being drawn this way, but since
   *  bitblt is a fast operation it works much faster. */
  private transient Image _stamp = null;

  /** The size of the image stamp. */
  private int _stampWidth = 128, _stampHeight = 128;

  /** The color of the grid lines or dots. */
  protected Color _color = new Color(180, 180, 180); //Color.gray;

  /** The color of the space between the lines or dots.  */
  protected Color _bgColor = Color.lightGray;

  /** True means to fill in the image stamp or drawing area with the
   *  background color. False means to just paint the lines or dots. */
  protected boolean _paintBackground = true;

  /** The size of the dots.  Dots are actually small rectangles. */
  protected int _dotSize = 2;

  /** As an example of different grid styles 5 are
   *  defined. Needs-More-Work: I should use the property sheet to
   *  adjust the grid.
   *
   * @see LayerGrid#adjust */
  private int _style = 2;
  private final int NUM_STYLES = 5;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new LayerGrid and name it 'Grid'. */
  public LayerGrid() { super("Grid"); }

  /** Construct a new LayerGrid with the given foreground color,
   * background color, line spacing, and lines/dots flag. */
  public LayerGrid(Color fore, Color back, int spacing, boolean lines) {
    super("Grid");
    _color = fore; _bgColor = back; _spacing = spacing; _paintLines = lines;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public Vector getContents() { return null; }
  public Fig presentationFor(Object obj) { return null; }

  
  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Paint the grid lines or dots by repeatedly bitblting a
   *  precomputed 'stamp' onto the given Graphics */
  public synchronized void paintContents(Graphics g) {
    // This line is for printing under Java 1.1
    if (g instanceof PrintGraphics) {
      if (!Globals.getPrefs().getPrintGrid()) return;
      if (_paintLines) paintLines(g, Globals.getPrefs().getPrintBackground());
      else paintDots(g, Globals.getPrefs().getPrintBackground());
      return;
    }
    if (_stamp == null) {
      if (_spacing > _stampHeight) _stampHeight = _stampWidth = _spacing;
      if (Globals.curEditor() == null) {
	// this is a bad idea, but it works around a very awkward AWT
	// requirement: that only frames can make Image instances
	System.out.println("no editor");
	Frame frame = new Frame();
	frame.show();
	_stamp = frame.createImage(_stampWidth, _stampHeight);
	frame.dispose();
      }
      else{ _stamp =Globals.curEditor().createImage(_stampWidth,_stampHeight);}
      if (_stamp != null) {
	if (_paintLines) paintLines(_stamp, _paintBackground);
	else paintDots(_stamp, _paintBackground);
      }
    }

    Rectangle clip = g.getClipBounds();
    int x = clip.x / _spacing * _spacing;
    int y = clip.y / _spacing * _spacing;
    int bot = clip.y + clip.height;
    int right = clip.x + clip.width;

    if (_stamp != null)
      while (x <= right) {
	y = clip.y / _spacing * _spacing;
	while (y <= bot) {
	  g.drawImage(_stamp, x, y, null);
	  y += _stampHeight;
	}
	x += _stampWidth;
      }
  }

  /** Paint lines on the given stamp Image. */
  private void paintLines(Image i, boolean paintBackground) {
    Graphics g = i.getGraphics();
    g.clipRect(0, 0, i.getWidth(null), i.getHeight(null));
    paintLines(g, paintBackground);
  }

  /** Paint dots on the given stamp Image. */
  private void paintDots(Image i, boolean paintBackground) {
    Graphics g = i.getGraphics();
    g.clipRect(0, 0, i.getWidth(null), i.getHeight(null));
    paintDots(g, paintBackground);
  }

  /** Paint lines on the given Graphics. */
  private void paintLines(Graphics g, boolean paintBackground) {
    Rectangle clip = g.getClipBounds();
    if (paintBackground) {
      g.setColor(_bgColor);
      g.fillRect(clip.x, clip.y, clip.width, clip.height);
    }
    int x = clip.x / _spacing * _spacing - _spacing;
    int y = clip.y / _spacing * _spacing - _spacing;
    int stepsX = clip.width / _spacing + 2;
    int stepsY = clip.height / _spacing + 2;
    int right = clip.x + clip.width;
    int bot = clip.y + clip.height;
    g.setColor(_color);

    while (stepsX > 0) {
      g.drawLine(x, 0, x, bot);
      x += _spacing;
      --stepsX;
    }
    while (stepsY > 0) {
      g.drawLine(0, y, right, y);
      y += _spacing;
      --stepsY;
    }
  }

  /** Paint dots on the given Graphics. */
  protected void paintDots(Graphics g, boolean paintBackground) {
    Rectangle clip = g.getClipBounds();
    if (paintBackground) {
      g.setColor(_bgColor);
      g.fillRect(clip.x, clip.y, clip.width, clip.height);
    }
    int x = clip.x / _spacing * _spacing - _spacing;
    int y = clip.y / _spacing * _spacing - _spacing;
    int right = clip.x + clip.width;
    int bot = clip.y + clip.height;

    g.setColor(_color);
    while (x <= right) {
      y = 0;
      while (y <= bot) {
	g.fillRect(x, y, _dotSize, _dotSize);
	y += _spacing;
      }
      x += _spacing;
    }
  }

  ////////////////////////////////////////////////////////////////
  // user interface

  /** Eventually this will open a dialog box to let the user adjust
   *  the grid line spacing, colors, and whether liens or dots are
   *  shown. For now it just cycles among 5 predefined styles. */
  public void adjust() {
    _style = (_style + 1) % NUM_STYLES;
    _stamp = null;
    switch (_style) {
    case 0: setHidden(false); _paintLines = true; _spacing = 16; break;
    case 1: setHidden(false); _paintLines = true; _spacing = 8; break;
    case 2: setHidden(false); _paintLines = false; _spacing = 16; break;
    case 3: setHidden(false); _paintLines = false; _spacing = 32; break;
    case 4: setHidden(true); break;
    }
    refreshEditors();
  }

  static final long serialVersionUID = -6881857653751712834L;
} /* end class LayerGrid */

