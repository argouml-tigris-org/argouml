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




// File: SelectionReshape.java
// Classes: SelectionReshape
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/** A Selection that allows the user to reshape the selected Fig.
 *  This is used with FigPoly, FigLine, and FigInk.  One handle is
 *  drawn over each point on the Fig.
 *
 * @see FigLine
 * @see FigPoly
 * @see FigInk
 */

public class SelectionReshape extends Selection
  implements KeyListener {

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected int _selectedHandle = -1; //@

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new SelectionReshape for the given Fig */
  public SelectionReshape(Fig f) { super(f); }

  /** Return a handle ID for the handle under the mouse, or -1 if none. 
   */
  public void hitHandle(Rectangle r, Handle h) {
    int npoints = _content.getNumPoints();
    int[] xs = _content.getXs();
    int[] ys = _content.getYs();
    for (int i = 0; i < npoints; ++i)
      if (r.contains(xs[i], ys[i])) {
	_selectedHandle = i;
	h.index = i;
	h.instructions = "Move point";
	return;
      }
    if (_content instanceof FigEdgePoly) {
      for (int i = 0; i < npoints-1; ++i) {
	if(Geometry.intersects(r,xs[i], ys[i],xs[i+1], ys[i+1])) {
	  FigEdgePoly p = ((FigEdgePoly)_content);
	  //System.out.println("hit segment");
	  h.index = p.getNumPoints();
	  h.instructions = "Add a point";
	  return;
	}
      }
    }
    _selectedHandle = -1;
    h.index = -1;
    h.instructions = "Move object(s)";
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
    super.paint(g);
  }

  /** Change some attribute of the selected Fig when the user drags one of its
   *  handles.  */
  public void dragHandle(int mX, int mY, int anX, int anY, Handle h) {
    // check assertions
    if ( _content instanceof FigEdgePoly) {
    FigEdgePoly p = ((FigEdgePoly)_content);
    int npoints = _content.getNumPoints();
    int[] xs = _content.getXs();
    int[] ys = _content.getYs();
    Rectangle r = new Rectangle(anX-4,anY-4,8,8);
    if(h.index == p.getNumPoints()) {
      for (int i = 0; i < npoints-1; ++i) {
        if(Geometry.intersects(r,xs[i], ys[i],xs[i+1], ys[i+1])) {
          p.insertPoint(i,r.x,r.y);
          h.index = i+1;
          break;
        }
      }
    }
    if (h.index < 0 || h.index >= p.getNumPoints())
      System.out.println("mistake " + h.index);
    }
    _content.setPoints(h, mX, mY);
  }


  ////////////////////////////////////////////////////////////////
  // event handlers

  public void keyPressed(KeyEvent ke) {
    if (ke.isConsumed()) return;
    if (_content instanceof KeyListener)
      ((KeyListener)_content).keyPressed(ke);
  }

  public void keyReleased(KeyEvent ke) {
    if (ke.isConsumed()) return;
    if (_content instanceof KeyListener)
      ((KeyListener)_content).keyReleased(ke);
  }

  /** If the user presses delete or backaspace while a handle is
   *  selected, remove that point from the polygon. The 'n' and 'p'
   *  keys select the next and previous points. The 'i' key inserts a
   *  new point. */
  public void keyTyped(KeyEvent ke) {
    Editor ce = Globals.curEditor();
    char key = ke.getKeyChar();
    int npoints = _content.getNumPoints();
    if (ke.isConsumed()) return;
    if (_content instanceof KeyListener)
      ((KeyListener)_content).keyTyped(ke);
    if (ke.isConsumed()) return;

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

    static final long serialVersionUID = -7030659500756989233L;
} /* end class SelectionReshape */

