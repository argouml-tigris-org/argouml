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




// File: ColorTilePanel.java
// Classes: ColorTilePanel
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.beans.editors;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.applet.*;
import java.util.*;

/** A Panel that shows an array of little colored squares to allow the
 *  user to pick a color. By default I use "netscape colors" which are
 *  colors that Netscape Navigator (TM) tries to allocate when it
 *  starts. */

public class ColorTilePanel extends JPanel
implements MouseListener, MouseMotionListener {
  ////////////////////////////////////////////////////////////////
  // constants
  public final static int TILESIZE = 6;

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The colors that will be shown */
  protected Vector _colors;

  /** The index of the selected color */
  protected int _selected = 0;

  /** The number of columns to use when displaying the array of
  /** colors. */
  protected int _nCols;

  /** True iff the user can select a color, false if this widget is
  /** disabled. */
  protected boolean _allowSelection = true;

  protected ActionListener _onlyListener = null;

  ////////////////////////////////////////////////////////////////
  // constructors

  public ColorTilePanel() { this(netscapeColors()); }
  public ColorTilePanel(int nCols) { this(netscapeColors(), nCols); }


  public ColorTilePanel(Vector cs) {
    _colors = cs;
    _nCols = (int) Math.sqrt(cs.size());
    addMouseListener(this);
    addMouseMotionListener(this);
  }

  public ColorTilePanel(Vector cs, int nCols) {
    _colors = cs;
    _nCols = nCols;
    addMouseListener(this);
    addMouseMotionListener(this);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public Color getColor() { return (Color)_colors.elementAt(_selected); }

  /** Select the little rectangle for the given color, ONLY if it is
   * one of the displayed colors. */
  public void setColor(Color c) {
    if (!_colors.contains(c)) return;
    setSelectionIndex(_colors.indexOf(c));
  }

  /** The user can be prevented from selecting a color if there would
   * be nothing to do the the selected color. E.g., the Color Picker
   * window is open but no DiagramElement is selected. */
  public void allowSelection(boolean as) {
    _allowSelection = as;
    repaint();
  }

  ////////////////////////////////////////////////////////////////
  // painting and related methods

  public void paint(Graphics g) {
    for (int i = 0; i < _colors.size(); ++i) paintTile(g, i);
    showSelection(g);
  }

  public void paintTile(Graphics g, int tileNum) {
    g.setColor((Color)_colors.elementAt(tileNum));
    int col = tileNum % _nCols;
    int row = tileNum / _nCols;
    g.fillRect(col*TILESIZE, row*TILESIZE, TILESIZE, TILESIZE);
  }

  /** Draw a black or white hollow rectangle to indicate which color
   * the user selected. */
  public void showSelection(Graphics g) {
    Color c, sc;
    c = (Color)_colors.elementAt(_selected);
    if (c.getRed() + c.getBlue() + c.getGreen() > 255 * 3 / 2)
      sc = Color.black; else sc = Color.white;
    g.setColor(sc);
    g.drawRect((_selected % _nCols)*TILESIZE,
	       (_selected / _nCols)*TILESIZE,
	       TILESIZE -1, TILESIZE-1);
    if (TILESIZE >= 8)
      g.drawRect((_selected % _nCols)*TILESIZE + 1,
		 (_selected / _nCols)*TILESIZE + 1,
		 TILESIZE -3, TILESIZE - 3);
  }


  public Dimension getMinimumSize() {
    int xSize = TILESIZE * _nCols;
    int ySize = TILESIZE * ( _colors.size() / _nCols + 2);
    return (new Dimension(xSize, ySize));
  }

  public Dimension getPreferredSize() { return getMinimumSize(); }

  public boolean setSelectionIndex(int s) {
    if (s < 0 || s > _colors.size()) return false;
    if (s == _selected) return false;
    _selected = s;
    repaint();
    return true;
  }

  ////////////////////////////////////////////////////////////////
  // event handling

  /** When the user releases the mouse button that signifies that
   * (s)he has made a selection, post an ACTION_EVENT that the
   * enclosing Container can handle. */
  public void mouseReleased(MouseEvent me) {
    int x = me.getX();
    int y = me.getY();
    if (x > _nCols * TILESIZE - 1 ||
        y > (_colors.size() / _nCols) * TILESIZE - 1)
      return;
    int col = x / TILESIZE;
    int row = y / TILESIZE;
    if (setSelectionIndex(col + row * _nCols)) {
      fireActionEvent(null);
    }
  }

  /** Mousedown is the same as mouse up: it selects the color under
   * the mouse */
  public void mousePressed(MouseEvent me) { mouseReleased(me); }

  public void mouseMoved(MouseEvent me) { }
  public void mouseDragged(MouseEvent me) { mouseReleased(me); }
  public void mouseClicked(MouseEvent me) { mouseReleased(me); }
  public void mouseEntered(MouseEvent me) { }
  public void mouseExited(MouseEvent me) { }

  ////////////////////////////////////////////////////////////////
  // color sets

  public static Vector _NetscapeColors = null;

  public static Vector netscapeColors() {
    if (_NetscapeColors == null) {
      int values[] = new int[6];
      int nValues = 0;
      values[nValues++] = 0;
      values[nValues++] = 3*16 + 3;
      values[nValues++] = 6*16 + 6;
      values[nValues++] = 9*16 + 9;
      values[nValues++] = 12*16 + 12;
      values[nValues++] = 15*16 + 15;
      _NetscapeColors = new Vector(nValues * nValues * nValues);
      for (int r = 0; r < nValues; ++r)
	for (int g = 0; g < nValues; ++g)
	  for (int b = 0; b < nValues; ++b) {
	    Color c = new Color(values[r], values[g], values[b]);
	    _NetscapeColors.addElement(c);
	  }
    }
    return _NetscapeColors;
  }


  ////////////////////////////////////////////////////////////////
  // events

  public void addActionListener(ActionListener list) {
    _onlyListener = list;
  }

  public void removeActionListener(ActionListener list) {
    if (_onlyListener == list) _onlyListener = null;
  }

  public void fireActionEvent(ActionEvent ae) {
    if (_onlyListener != null) _onlyListener.actionPerformed(ae);
  }

} /* end class ColorTilePanel */

