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

// File: Globals.java
// Classes: Globals
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.applet.*;
import java.awt.*;
import java.beans.*;
import java.net.*;
import java.util.*;
import uci.ui.*;

/** This class stores global info that is needed by all Editors. For
 *  example, it aids in communication between the various Palette's and
 *  Editor's by holding the next global Mode.  */

public class Globals {

  ////////////////////////////////////////////////////////////////
  // constants

  public static final String REMOVE = "Remove";
  public static final String HIGHLIGHT = "Highlight";
  public static final String UNHIGHLIGHT = "Unhighlight";

  ////////////////////////////////////////////////////////////////
  // applet related methods

  protected static AppletContext _appletContext;
  protected static Applet _applet;
  protected static MediaTracker _tracker;

  protected static TabPropFrame _tabPropFrame;

  public static String defaultStatus = "";

  public static void setApplet(Applet a) {
    _applet = a;
    _appletContext = a.getAppletContext();
    clearStatus();
    _tracker = new MediaTracker(a);
  }

  public static AppletContext getAppletContext() { return _appletContext; }
  public static Applet getApplet() { return _applet; }

  public static IStatusBar _StatusBar = null;
  public static void setStatusBar(IStatusBar sb) { _StatusBar = sb; }

  /** Show a string on the browser's status bar, or print a message
   * on the console.  */
  public static void showStatus(String s) {
    if (_StatusBar != null) _StatusBar.showStatus(s);
    else if (_appletContext != null) _appletContext.showStatus(s);
    else if (curEditor() != null) curEditor().showStatus(s);
    //else System.out.println(s);
  }

  public static void clearStatus() { showStatus(defaultStatus); }

  /**  <A HREF="../features.html#show_document">
   *  <TT>FEATURE: show_document</TT></A>
   */
  public static void showDocument(URL url) {
    if (_appletContext != null) _appletContext.showDocument(url);
  }

  /**  <A HREF="../features.html#show_document">
   *  <TT>FEATURE: show_document</TT></A>
   */
  public static void showDocument(URL url, String target) {
    if (_appletContext != null) _appletContext.showDocument(url, target);
  }

  /**  <A HREF="../features.html#show_document">
   *  <TT>FEATURE: show_document</TT></A>
   */
  public static void showDocument(String urlString) {
    try { showDocument(new URL(urlString)); }
    catch (MalformedURLException ignore) { }
  }

  public static Image getImage(URL url) {
    Image img = null;
    if (_appletContext != null) img = _appletContext.getImage(url);
    if (_tracker !=null && img != null) _tracker.addImage(img, 1);
    return img;
  }

  public static Image getImage(String urlStr) {
    try {
      Image img = null;
      if(_appletContext !=null) img = _appletContext.getImage(new URL(urlStr));
      if (_tracker !=null && img != null) _tracker.addImage(img, 1);
      return img;
    }
    catch (java.net.MalformedURLException e) { return null; }
  }

  public static void waitForImages() {
    if (_tracker == null) return;
    try { _tracker.waitForAll(); }
    catch (InterruptedException e) { }
  }

  public static void quit() {
    showStatus("Quiting"); // Needs-More-Work: put up "are you sure?" dialog
    if (_appletContext != null) _applet.destroy();
    // Needs-More-Work: now it is up to the Applet to close all windows.
    System.exit(0);     // in any case, try to exit
  }

  ////////////////////////////////////////////////////////////////
  // user preferences

  /** The user's preferences for various editor features. */
  protected static Prefs _prefs = new Prefs();

  /** Reply the user's preferences. */
  public static Prefs getPrefs() { return _prefs; }

  ////////////////////////////////////////////////////////////////
  // properties sheet

  /** <A HREF="../features.html#property_sheet">
   *  <TT>FEATURE: property_sheet</TT></A>
   */
  public static void setPropertySheet(TabPropFrame tpf) {
    _tabPropFrame = tpf;
  }

  /** <A HREF="../features.html#property_sheet">
   *  <TT>FEATURE: property_sheet</TT></A>
   */
  public static void startPropertySheet() {
    if (_tabPropFrame == null) _tabPropFrame = new TabPropFrame();
    if (_tabPropFrame.isVisible()) _tabPropFrame.toFront();
    else _tabPropFrame.show();
    if (curEditor() == null) return;
    Vector selectedFigs = curEditor().selectedFigs();
    if (selectedFigs.size() == 1)
      propertySheetSubject((Fig)selectedFigs.elementAt(0));
  }

  /** <A HREF="../features.html#property_sheet">
   *  <TT>FEATURE: property_sheet</TT></A>
   */
  public static void propertySheetSubject(Fig f) {
    if (_tabPropFrame != null) _tabPropFrame.select(f);
  }

  static {
    if (_tabPropFrame == null) _tabPropFrame = new TabPropFrame();
    _tabPropFrame.show();
  }

  public static Frame someFrame() { return _tabPropFrame; }

  ////////////////////////////////////////////////////////////////
  // Editor Modes

  /** True if the next global Mode should remain as the next global
   *  mode even after it has been given to one editor. */
  protected static boolean _sticky = false;

  /** Set whether the next global mode should remain as the next
   *  global mode even after it has been given to one editor. */
  public static void setSticky(boolean b) { _sticky = b; }

  /** The next global mode. This is given to an editor on mouse entry */
  protected static Mode _mode;

  /** What mode should Editor's go into if the Palette is not
   *  requesting a specific mode? */
  protected static Mode defaultMode() { return new ModeSelect(); }

  /** Set the next global mode. */
  public static void mode(Mode m) { _mode = m; }

  /** Set the next global mode, and set it's stickiness. */
  public static void mode(Mode m, boolean s) { _mode = m; _sticky = s; }

  /** Reply the next global mode. */
  public static Mode mode() {
    if (_mode == null) _mode = defaultMode();
    return _mode;
  }

  /** Determine the next global mode. This is called when a mode
   *  finishes whatever it was meant to accomplish. For now, a sticky
   *  mode stays the next global mode, all other modes are "popped" off
   *  a single element stack and the defaultMode becomes the next
   *  global mode. In the future it may be useful to have a true stack
   *  of pending modes, I don't know.
   *
   * @see Mode#done */
  public static Mode nextMode() {
    if (!_sticky) _mode = defaultMode();
    return _mode;
  }

  ////////////////////////////////////////////////////////////////
  // Methods that keep track of the current editor

  /** The Editor that most recently contained the mouse. */
  protected static Editor _curEditor;

  /** Set the current Editor. */
  public static void curEditor(Editor ce) { _curEditor = ce; }

  /** Reply the Editor that most recently contained the mouse. */
  public static Editor curEditor() { return _curEditor; }


  ////////////////////////////////////////////////////////////////
  // a light-weight data-structure for tracking PropertyChangeListeners

  protected static Hashtable _pcListeners = new Hashtable();

  public static int MAX_LISTENERS = 4;

  public static void
  addPropertyChangeListener(Object src, PropertyChangeListener l) {
    PropertyChangeListener listeners[] =
      (PropertyChangeListener[]) _pcListeners.get(src);
    if (listeners == null) {
      listeners = new PropertyChangeListener[4];
      _pcListeners.put(src, listeners);
    }
    for (int i=0; i < MAX_LISTENERS; ++i)
      if (listeners[i] == null) { listeners[i] = l; return; }
    System.out.println("ran out of listeners!");
  }

  public static void
  removePropertyChangeListener(Object s, PropertyChangeListener l){
    PropertyChangeListener listeners[] =
      (PropertyChangeListener[]) _pcListeners.get(s);
    if (listeners == null) return;
    for (int i=0; i < MAX_LISTENERS; ++i)
      if (listeners[i] == l) { listeners[i] = null; return; }
    System.out.println("listener not found!");
  }

  public static void firePropChange(Object src, String propName,
			     boolean oldV, boolean newV) {
    firePropChange(src, propName, new Boolean(oldV), new Boolean(newV));
  }

  public static void firePropChange(Object src, String propName,
			     int oldV, int newV) {
    firePropChange(src, propName, new Integer(oldV), new Integer(newV));
  }

  public static void firePropChange(Object src, String propName,
			     Object oldValue, Object newValue) {
    if (oldValue != null && oldValue.equals(newValue)) return;
    PropertyChangeListener listeners[] =
      (PropertyChangeListener[]) _pcListeners.get(src);
    if (listeners == null) return;
    PropertyChangeEvent evt =
      new PropertyChangeEvent(src, propName, oldValue, newValue);
    // needs-more-work: should be thread safe, clone array?
    for (int i=0; i < MAX_LISTENERS; ++i)
      if (listeners[i] != null) listeners[i].propertyChange(evt);
  }

} /* end class Globals */
