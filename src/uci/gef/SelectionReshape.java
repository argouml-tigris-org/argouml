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

// File: SelectionReshape.java
// Classes: SelectionReshape
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

/** A Selection class to represent selections on Fig's that
 *  present handles. Needs-More-Work: in an early version of this graph
 *  editing framework the Fig's did their own painting of
 *  handles. I want to get away from that, but it has not happened
 *  yet.
 *  <A HREF="../features.html#selections_handles">
 *  <TT>FEATURE: selections_handles</TT></A>
 */

public class SelectionReshape extends Selection {

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected int _selectedHandle = -1; //@

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new SelectionReshape for the given Fig */
  public SelectionReshape(Fig f) { super(f); }

  /** Return a handle ID for the handle under the mouse, or -1 if none. 
   */
  public int hitHandle(Rectangle r) {
    int npoints = _content.getNumPoints();
    int[] xs = _content.getXs();
    int[] ys = _content.getYs();
    for (int i = 0; i < npoints; ++i)
      if (r.contains(xs[i], ys[i])) {
	_selectedHandle = i;
	return i;
      }
    _selectedHandle = -1;
    return -1;
  }

  /** Paint the handles at the four corners and midway along each edge
   * of the bounding box.  */
  public void paint(Graphics g) {
    int npoints = _content.getNumPoints();
    int[] xs = _content.getXs();
    int[] ys = _content.getYs();
    g.setColor(Globals.getPrefs().handleColorFor(_content));
    for (int i = 0; i < npoints; ++i)
      g.fillRect(xs[i] - HAND_SIZE/2, ys[i] - HAND_SIZE/2,
		 HAND_SIZE, HAND_SIZE);
    if (_selectedHandle != -1)
      g.drawRect(xs[_selectedHandle] - HAND_SIZE/2 - 2,
		 ys[_selectedHandle] - HAND_SIZE/2 - 2,
		 HAND_SIZE + 3, HAND_SIZE + 3);

  }

  /** Change some attribute of the selected Fig when the user drags one of its
   *  handles.  */
  public void dragHandle(int mX, int mY, int anX, int anY, Handle h) {
    // check assertions
    _content.setPoints(h, mX, mY);
  }


  ////////////////////////////////////////////////////////////////
  // event handlers

  /** If the user presses delete or backaspace while a handle is
   *  selected, remove that point from the polygon. The 'n' and 'p'
   *  keys select the next and previous points. The 'i' key inserts a
   *  new point. */
  public void keyTyped(KeyEvent ke) {
    Editor ce = Globals.curEditor();
    char key = ke.getKeyChar();
    int npoints = _content.getNumPoints();
    if (_selectedHandle != -1 && (key == 127 || key == 8)) {
      ce.executeCmd(new CmdRemovePoint(_selectedHandle), ke);
      ke.consume();
      return;
    }
    if (key == 'i') {
      if (_selectedHandle == -1) _selectedHandle = 0;
      ce.executeCmd(new CmdInsertPoint(_selectedHandle), ke);
      ke.consume();
      return;
    }
    if (key == 'n') {
      // Needs-More-Work: the following should be in an Cmd.
      // ce.executeCmd(new CmdSelectNextPoint(), e);
      startTrans();
      if (_selectedHandle == -1) _selectedHandle = 0;
      else _selectedHandle = (_selectedHandle + 1) % npoints;
      endTrans();
      ke.consume();
      return;
    }
    if (key == 'p') {
      // Needs-More-Work: the following should be in an Cmd.
      // ce.executeCmd(new CmdSelectPrevPoint(), e);
      startTrans();
      if (_selectedHandle == -1) _selectedHandle = npoints - 1;
      else _selectedHandle = (_selectedHandle + npoints - 1) % npoints;
      endTrans();
      ke.consume();
      return;
    }
    //super.keyTyped(ke);
  }

} /* end class SelectionReshape */

