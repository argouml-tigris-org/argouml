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

// File: SelectionWButtons.java
// Classes: SelectionWButtons
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.diagram.ui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Icon;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;

public abstract class SelectionWButtons extends SelectionNodeClarifiers {
  ////////////////////////////////////////////////////////////////
  // constants
  public static final int IMAGE_SIZE = 22;
  public static final int MARGIN = 2;
  public static final Color PRESSED_COLOR = Color.gray.brighter();

  ////////////////////////////////////////////////////////////////
  // static variables
  public static int Num_Button_Clicks = 0;
  public static boolean _showRapidButtons = true;

  ////////////////////////////////////////////////////////////////
  // instance variables
  protected boolean _paintButtons = true;
  protected int _pressedButton = -1;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new SelectionWButtons for the given Fig */
  public SelectionWButtons(Fig f) {
    super(f);
    _paintButtons = _showRapidButtons;
  }

  ////////////////////////////////////////////////////////////////
  // static accessors

  public static void toggleShowRapidButtons() {
    _showRapidButtons = ! _showRapidButtons;
  }

  ////////////////////////////////////////////////////////////////
  // interaction utility methods

  public boolean hitAbove(int x, int y, int w, int h, Rectangle r) {
    return intersectsRect(r, x - w/2, y - h - MARGIN, w, h + MARGIN);
  }

  public boolean hitBelow(int x, int y, int w, int h, Rectangle r) {
    return intersectsRect(r, x - w/2, y, w, h + MARGIN);
  }

  public boolean hitLeft(int x, int y, int w, int h, Rectangle r) {
    return intersectsRect(r, x, y - h/2, w + MARGIN, h);
  }

  public boolean hitRight(int x, int y, int w, int h, Rectangle r) {
    return intersectsRect(r, x - w - MARGIN, y - h/2, w + MARGIN, h);
  }

  public boolean intersectsRect(Rectangle r, int x, int y, int w, int h) {
    return !((r.x + r.width <= x) ||
	     (r.y + r.height <= y) ||
	     (r.x >= x + w) ||
	     (r.y >= y + h));
  }

  ////////////////////////////////////////////////////////////////
  // display methods

  /** Paint the handles at the four corners and midway along each edge
   * of the bounding box.  */
  public void paint(Graphics g) {
    super.paint(g);
    if (!_paintButtons) return;
    Editor ce = Globals.curEditor();
    SelectionManager sm = ce.getSelectionManager();
    if (sm.size() != 1) return;
    ModeManager mm = ce.getModeManager();
    if (mm.includes(ModeModify.class) && _pressedButton == -1) return;
    paintButtons(g);
  }

  public abstract void paintButtons(Graphics g);

  public void paintButtonAbove(Icon i, Graphics g, int x, int y, int hi) {
    paintButton(i, g, x - i.getIconWidth()/2, y - i.getIconHeight() -
		MARGIN, hi);
  }

  public void paintButtonBelow(Icon i, Graphics g, int x, int y, int hi) {
    paintButton(i, g, x - i.getIconWidth()/2, y + MARGIN, hi);
  }

  public void paintButtonLeft(Icon i, Graphics g, int x, int y, int hi) {
    paintButton(i, g, x + MARGIN, y - i.getIconHeight()/2, hi);
  }

  public void paintButtonRight(Icon i, Graphics g, int x, int y, int hi) {
    paintButton(i, g, x - i.getIconWidth() - MARGIN, y -
		i.getIconHeight()/2, hi);
  }

  public void paintButton(Icon i, Graphics g, int x, int y, int hi) {
    int w = i.getIconWidth() + 4;
    int h = i.getIconHeight() + 4;

    if (hi == _pressedButton) {
      g.setColor(PRESSED_COLOR);
      g.fillRect(x-2, y-2, w, h);
    }
    i.paintIcon(null, g, x, y);

    g.translate(x-2, y-2);

    Color handleColor = Globals.getPrefs().handleColorFor(_content);
    g.setColor(handleColor.darker());
    g.drawRect(0, 0, w-2, h-2);

    g.setColor(handleColor.brighter().brighter().brighter());
    if (hi != _pressedButton) {
      g.drawLine(1, h-3, 1, 1);
      g.drawLine(1, 1, w-3, 1);
    }

    g.drawLine(0, h-1, w-1, h-1);
    g.drawLine(w-1, h-1, w-1, 0);

    g.translate(-x+2, -y+2);
  }

  public Rectangle getBounds() {
    return new Rectangle(_content.getX() - IMAGE_SIZE * 2,
			 _content.getY() - IMAGE_SIZE * 2,
			 _content.getWidth() + IMAGE_SIZE * 4,
			 _content.getHeight() + IMAGE_SIZE * 4);
  }

  /** Dont show buttons while the user is moving the Class.  Called
   *  from FigClass when it is translated. */
  public void hideButtons() { _paintButtons = false; }

  public void buttonClicked(int buttonInt) {
    if (buttonInt >= 10) Num_Button_Clicks++;
  }


  ////////////////////////////////////////////////////////////////
  // event handlers

  public void mousePressed(MouseEvent me) {
    Handle h = new Handle(-1);
    hitHandle(me.getX(), me.getY(), 0, 0, h);
    _pressedButton = h.index;
    Editor ce = Globals.curEditor();
    ce.damaged(this);
  }

  public void mouseReleased(MouseEvent me) {
    if (_pressedButton < 10) return;
    Handle h = new Handle(-1);
    hitHandle(me.getX(), me.getY(), 0, 0, h);
    if (_pressedButton == h.index) {
      buttonClicked(_pressedButton);
    }
    _pressedButton = -1;
    Editor ce = Globals.curEditor();
    ce.damaged(this);
  }

  public void mouseEntered(MouseEvent me) {
    super.mouseEntered(me);
    if (_showRapidButtons) _paintButtons = true;
    Editor ce = Globals.curEditor();
    ce.damaged(this);
  }
  public void mouseExited(MouseEvent me) {
    super.mouseExited(me);
    _paintButtons = false;
    Editor ce = Globals.curEditor();
    ce.damaged(this);
  }

} /* end class SelectionWButtons */

