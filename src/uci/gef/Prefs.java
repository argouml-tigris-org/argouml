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

// File: Prefs.java
// Classes: Prefs
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** This class contains preferences that control the behavior of the
 *  editor to make it the way that the user likes it. Needs-More-Work:
 *  more of the frame-rate could be self-adusting, not a
 *  preference. Needs-More-Work: more of this should move into
 *  RedrawManager.
 *  <A HREF="../features.html#preferences">
 *  <TT>FEATURE: preferences</TT></A>
 */

public class Prefs {

  /** The color, thickness, etc. of rubberband lines */
  private Hashtable _rubberbandAttrs;

  /** Construct a new Prefs instance */
  public Prefs() {
    initializeRubberBandAttrs();
  }

  /** Set the rubberband attributes to some default values */
  protected void initializeRubberBandAttrs() {
    _rubberbandAttrs = new Hashtable();
    _rubberbandAttrs.put("LineColor", new Color(0x33, 0x33, 0x99));
/*    _rubberbandAttrs.put("LineThickness", new Integer(2)); */
  }

  /** Reply the graphical attributes that should be used when the user
   *  is dragging out a rubberband for a new arc or line */
  public Hashtable rubberbandAttrs() { return _rubberbandAttrs; }

  /** The color of the handles used to manipulate Fig's */
  private Color _handleColor = new Color(0x55, 0x55, 0xaa);
  /** The color of the handles used to indicate locked Fig's */
  private Color _lockedHandleColor = new Color(0x55, 0x55, 0x55);
  /** The color of the handles used to manipulate Fig's */
  public Color handleColorFor(Fig f) {
    if (f.getLocked()) return _lockedHandleColor;
    else return _handleColor;
  }
  /** The color of the handles used to manipulate Fig's */
  public void setHandleColor(Color c) { _handleColor = c; }
  public Color getHandleColor() { return _handleColor; }
  /** The color of the handles used to manipulate Fig's */
  public void setLockedHandleColor(Color c) { _lockedHandleColor = c; }
  public Color getLockedHandleColor() { return _lockedHandleColor; }

  /** The color of the highlight shown to draw the users attention */
  Color _highlightColor = new Color(0x99, 0x33, 0x33);
  /** The color of the highlight shown to draw the users attention */
  public Color getHighlightColor() { return _highlightColor; }
  /** The color of the highlight shown to draw the users attention */
  public void setHighlightColor(Color c) { _highlightColor = c; }

  /**  <A HREF="../features.html#printing_config">
   *  <TT>FEATURE: printing_config</TT></A>
   */
  protected boolean _printGrid = false;
  public boolean getPrintGrid() { return _printGrid; }
  public void setPrintGrid(boolean b) { _printGrid = b; }

  /**  <A HREF="../features.html#printing_config">
   *  <TT>FEATURE: printing_config</TT></A>
   */
  protected boolean _printBackground = false;
  public boolean getPrintBackground() { return _printBackground; }
  public void setPrintBackground(boolean b) { _printBackground = b; }

  /** needs-more-work
   */
  protected boolean _reshapeFirst = false;
  public boolean getReshapeFirst() { return _reshapeFirst; }
  public void setReshapeFirst(boolean b) { _reshapeFirst = b; }

  /** Times used to fine-tune redrawing behavior */
  private long _redrawTimeThreshold = 500, _lastRedrawTime;

  /** Set the last redraw time. Called from a RedrawManager. Should
   *  this be inside RedrawManager? What if there are multiple
   *  RedrawManager's? */
  public void lastRedrawTime(long t) { _lastRedrawTime = t; }

  /** if the time between redraws gets longer than this threshold,
   *  then switch to a faster redrawing method, at the expense of
   *  quality and/or flicker */
  public void setRedrawTimeThreshold(long t) { _redrawTimeThreshold = t; }

  /** Defines a default about whether the slow, flicker-free redraw
   *  method should be used, or the fast, flicker-full one.  By
   *  defualt uses, flicker-free, except on JDK 1.0.2 on Sun's
   *  appletviewer for Windows.  Needs-More-Work: should be a
   *  PARAM.
   *  <A HREF="../features.html#redraw_off_screen">
   *  <TT>FEATURE: redraw_off_screen</TT></A>
   */
  private boolean _tryOffScreen =
    !(System.getProperty("java.vendor").equals("Sun Microsystems Inc.") &&
      System.getProperty("java.version").equals("102") &&
      System.getProperty("os.name").startsWith("Win"));

  /** Should off screen images be used to reduce flicker? This is not
   *  the default behavior because some (beta) versions of WWW
   *  browsers do not handle off screen images well.
   *  <A HREF="../features.html#redraw_off_screen">
   *  <TT>FEATURE: redraw_off_screen</TT></A>
   */
  public void setTryOffScreen(boolean b) { _tryOffScreen = b;  }

  /** Should off screen images be used to reduce flicker?
   *  <A HREF="../features.html#redraw_off_screen">
   *  <TT>FEATURE: redraw_off_screen</TT></A>
   */
  public boolean getTryOffScreen() { return _tryOffScreen; }

  /** Determine if the next redraw should be done on screen or
   *  offscreen.  If the last redraw was fast, then try this one with
   *  less flicker. Needs-More-Work: this code should be in
   *  RedrawManager.
   *  <A HREF="../features.html#redraw_off_screen">
   *  <TT>FEATURE: redraw_off_screen</TT></A>
   *  <A HREF="../features.html#adaptive_redraw">
   *  <TT>FEATURE: adaptive_redraw</TT></A>
   */
  public boolean shouldPaintOffScreen() {
    if (_tryOffScreen)
      return _lastRedrawTime < _redrawTimeThreshold;
    else return false;
  }

} /* end class Prefs */

