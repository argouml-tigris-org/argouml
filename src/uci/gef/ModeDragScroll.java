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


// File: ModeDragScroll.java
// Classes: ModeDragScroll
// Original Author: Sean Chen, schen@webex.net

package uci.gef;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

/** A Mode that allows the user to scroll the Editor by clicking and dragging
 * with the middle mouse button.
 * 
 * @see Mode
 * @see Editor
 * @author Sean Chen, schen@webex.net
 */
public class ModeDragScroll extends Mode implements ActionListener {

  /** Pause time between each scroll during autoscroll (in milliseconds)
   */
  private final static int AUTOSCROLL_DELAY = 200;

  /** Minimum pause time between each scroll during autoscroll
   */
  private final static int MIN_AUTOSCROLL_DELAY = 50;

  /** For each pixel farther away from component, shave off this factor
   *  (in milliseconds) down to MIN_AUTOSCROLL_DELAY
   */
  private final static int AUTOSCROLL_FACTOR = 10;

  ////////////////////////////////////////////////////////////////
  // constructors and related methods

  /** Construct a new ModeDragScroll with the given parent.
   * 
   * @param par The Editor this Mode will drag
   */
  public ModeDragScroll(Editor par) {
    super(par);
    autoTimer = new Timer(AUTOSCROLL_DELAY,this);
  }

  /** Construct a new ModeDragScroll instance. Its parent must be set
   * before this instance can be used.
   * 
   */
  public ModeDragScroll() { this(null); }

  /** Always false since this mode can never be exited.
   */
  public boolean canExit() { return false; }

  /** Instructions for the user.
   */
  public String instructions() {
    return "Drag with mouse to scroll";
  }

  private boolean autoscroll = false;
  private Rectangle currentView, newView = new Rectangle();
  private Point scrollOrigin = new Point();
  private int xcorrection = 0, ycorrection = 0;
  private JComponent component = null;
  private JViewport view = null;
  private Cursor oldCursor = null;
  private Dimension componentSize = null;
  private Timer autoTimer;
  private boolean dragScrolling = false;
  private int recentX, recentY;

  /** Grabs component to begin scrolling.  Will turn cursor into a hand.
   * 
   * @param me 
   */
  public void mousePressed(MouseEvent me) {
    dragScrolling = (me.isAltDown());

    // get the component and the view
    component = (JComponent) _editor.getAwtComponent();
    if (component == null) return;
    if (component != null && component.getParent() instanceof JViewport)
      view = (JViewport) component.getParent();
    componentSize = component.getSize();

    int x = me.getX(), y = me.getY();
    currentView = view.getViewRect();    // assume it's a new Rectangle
    newView.width = currentView.width;
    newView.height = currentView.height;
    newView.x = currentView.x;
    newView.y = currentView.y;
    scrollOrigin.x = x; scrollOrigin.y = y;
    xcorrection = 0;
    ycorrection = 0;
    // System.out.println("Reset to "+newView+" with origin at "+scrollOrigin);
    if (dragScrolling) {
      oldCursor = component.getCursor();
      component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    //if (autoscroll) {
    autoTimer.stop();
    autoscroll = false;
    //}
    if (dragScrolling) me.consume();
  }

  /** If mouse is outside the component, begins autoscrolling or speeds it up.
   * Otherwise will just scroll.
   * 
   * @param me 
   */
  public void mouseDragged(MouseEvent me) {
    int x = me.getX(), y = me.getY();
    recentX = x; recentY = y;
    // are we out of this component? if so, don't do anything
    // let autoscroll take over

    // jer: check if we are outside of the current view rect, not the
    // component, keep mouse x,y in view coordinates
    if (x < currentView.x || y < currentView.y ||
	x > currentView.x + currentView.width ||
	y > currentView.y + currentView.height) {
      if (x < currentView.x) {
	newView.x = x - currentView.x;
	newView.y = 0;
	//System.out.println("left");
      }
      else if (x > currentView.x + currentView.width) {
	newView.x = x - (currentView.x + currentView.width);
	newView.y = 0;
	//System.out.println("right");
      }
      if (y < currentView.y) {
	newView.x = 0;
	newView.y = y - currentView.y;
	//System.out.println("top");
      }
      else if (y > currentView.y + currentView.height) {
	newView.x = 0;
	newView.y = y - (currentView.y + currentView.height);
	//System.out.println("bot");
      }
      if (dragScrolling) {
	newView.x = - newView.x;
	newView.y = - newView.y;
      }
      if (!autoscroll) {
	autoscroll = true;
	autoTimer.start();
      }
      if (!doScroll()) {
	autoscroll = false; // can't scroll anymore
	autoTimer.stop();
      }

      //else {
	//jer: keep speed constant and make steps larger

	// speed up the farther away we are from the component
	//int distance = Math.max(Math.abs(newView.x), Math.abs(newView.y));

	// for each pixel, shave off AUTOSCROLL_FACTOR milliseconds
	// down to a MIN_AUTOSCROLL_DELAY
	// autoTimer.setDelay(Math.max(AUTOSCROLL_DELAY - 
	// distance * AUTOSCROLL_FACTOR, 
	// MIN_AUTOSCROLL_DELAY));
	//autoTimer.setDelay(AUTOSCROLL_DELAY);
      //}
      if (dragScrolling) {
	//System.out.println("consumed in ModeDragScroll");
	me.consume();
      }
      return;
    }

    //mouse is within current view
    autoscroll = false;
    autoTimer.stop();
    if (dragScrolling) {
      x -= xcorrection;
      y -= ycorrection;
      newView.x = (scrollOrigin.x - x);
      newView.y = (scrollOrigin.y - y);

      if (doScroll()) {
	scrollOrigin.x = x;
	scrollOrigin.y = y;
      }
      me.consume();
    }
  }

  /** Perform scrolling using the delta found in newView.
   * 
   * @return scroll succeeded or not
   */
  private boolean doScroll() {
    // check to see if we're scrolling the component off the view
    currentView.x += newView.x;
    currentView.y += newView.y;
    if (currentView.x < 0 || currentView.x > (componentSize.width - currentView.width)) {
      currentView.x -= newView.x;
      newView.x = 0;
    }
    if (currentView.y < 0 || currentView.y > (componentSize.height - currentView.height)) {
      currentView.y -= newView.y;
      newView.y = 0;
    }
    if (newView.x == 0 && newView.y == 0) {
      //System.out.println("stop!!");
      return false;
    }
    //view.scrollRectToVisible(newView);
    view.setViewPosition(new Point(currentView.x, currentView.y));
    //System.out.println("scrolled to "+newView+" / "+_editor.getCurrentView());
    xcorrection += newView.x;
    ycorrection += newView.y;
    currentView = view.getViewRect();  //jer
    return true;
  }

  /** Stops autoscrolling.
   * @param me 
   */
  public void mouseReleased(MouseEvent me) {
    currentView = null;
    if (dragScrolling) component.setCursor(oldCursor);
    component = null;
    componentSize = null;
    view = null;
    oldCursor = null;
    if (autoscroll) autoTimer.stop();
    autoscroll = false;
    if (dragScrolling) me.consume();
  }

  /** If mouse re-enters component, stops autoscrolling.
   * @param me 
   */
  public void mouseEntered(MouseEvent me) {
    if (oldCursor != null && component != null && dragScrolling) {
      component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      //me.consume();
    }
    if (autoscroll) autoTimer.stop();
    autoscroll = false;
  }


  /** Called by the autoTimer to scroll once during autoscrolling.
   *  Multithreaded note: No synchronization needed because Timers will
   *  fire this ActionEvent on the event dispatching thread
   * 
   * @param e 
   */
  // private long freeMemory = 0;
  public void actionPerformed(ActionEvent e) {
    /* uncomment to view memory usage 
    if (freeMemory != 0)
      System.out.println(freeMemory - Runtime.getRuntime().freeMemory());
    freeMemory = Runtime.getRuntime().freeMemory();
    */
//     if (!doScroll()) {
//       autoscroll = false; // can't scroll anymore
//       autoTimer.stop();
//     }
    MouseEvent me = new MouseEvent(getEditor().getAwtComponent(),
				   Event.MOUSE_DRAG, 0,
				   InputEvent.BUTTON1_MASK,
				   recentX, recentY, 0, false);
    getEditor().mouseDragged(me);
  }

}
