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

  public static final String REMOVE = "remove";
//   public static final String HIGHLIGHT = "highlight";
//   public static final String UNHIGHLIGHT = "unhighlight";

  ////////////////////////////////////////////////////////////////
  // applet related methods

  protected static AppletContext _appletContext;
  protected static Applet _applet;
  protected static MediaTracker _tracker;

  //protected static TabPropFrame _tabPropFrame;

  /** String to display when nothing important is being displayed.  By
   *  default the string " " is used. Some applications may want to
   *  change this to "Ready." It should not be set to "", because that
   *  can mess up window layouts, causing the status line to
   *  disappear. */
  public static String defaultStatus = "  ";

  /** If we are running as an applet, Store the Applet and
   *  AppletContext in a well known place. */
  public static void setApplet(Applet a) {
    _applet = a;
    _appletContext = a.getAppletContext();
    clearStatus();
    _tracker = new MediaTracker(a);
  }

  public static AppletContext getAppletContext() { return _appletContext; }
  public static Applet getApplet() { return _applet; }

  /** Until jdk 1.2, this will be our clipboard */
  public static Vector clipBoard;

  /** The directory most recently used in an open or save dialog */
  public static String LastDirectory = ".";

  public static String getLastDirectory() { return LastDirectory;}
  public static void setLastDirectory(String s) { LastDirectory = s;}

  /** Determines if tool tips be are shown on Figs in diagrams,
   *  including ToDoItem headlines. */
  public static boolean ShowFigTips = true;

  public static boolean getShowFigTips() { return ShowFigTips;}
  public static void setShowFigTips(boolean b) { ShowFigTips = b; }

  /** The place where status messages will be written. */
  public static IStatusBar _StatusBar = null;

  /** Sets the place where status messages will be written. */
  public static void setStatusBar(IStatusBar sb) { _StatusBar = sb; }

  /** Show a string on the some status bar, or print a message
   * on the console.  */
  public static void showStatus(String s) {
    if (_StatusBar != null) _StatusBar.showStatus(s);
    else if (_appletContext != null) _appletContext.showStatus(s);
    //else System.out.println(s);
  }

  /** Sets the status to its default (blank) string. */
  public static void clearStatus() { showStatus(defaultStatus); }

  /** If we are running as an applet, ask the browser to display the
   *  given URL. */
  public static void showDocument(URL url) {
    if (_appletContext != null) _appletContext.showDocument(url);
  }

  /** If we are running as an applet, ask the browser to display the
   *  given URL. */
  public static void showDocument(URL url, String target) {
    if (_appletContext != null) _appletContext.showDocument(url, target);
  }

  /** If we are running as an applet, ask the browser to display the
   *  given URL. */
  public static void showDocument(String urlString) {
    try { showDocument(new URL(urlString)); }
    catch (MalformedURLException ignore) { }
  }

  /** Get an image from the given URL (usually a gif file). */
  public static Image getImage(URL url) {
    Image img = null;
    if (_appletContext != null) img = _appletContext.getImage(url);
    if (_tracker !=null && img != null) _tracker.addImage(img, 1);
    return img;
  }

  /** Get an image from the given URL (usually a gif file). */
  public static Image getImage(String urlStr) {
    try {
      Image img = null;
      if(_appletContext !=null) img = _appletContext.getImage(new URL(urlStr));
      if (_tracker !=null && img != null) _tracker.addImage(img, 1);
      return img;
    }
    catch (java.net.MalformedURLException e) { return null; }
  }

  /** Wait for all images to download. */
  public static void waitForImages() {
    if (_tracker == null) return;
    try { _tracker.waitForAll(); }
    catch (InterruptedException e) { }
  }

  /** Try to exit the applet or application.  Needs-more-work: in the
   *  case of an applet, not all windows are closed. */
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

  /** There is one global property sheet that shows details of the
   *  selected Fig. */
  //public static void setPropertySheet(TabPropFrame tpf) {
  //_tabPropFrame = tpf;
  //}

  /** Open the property sheet. */
   public static void startPropertySheet() {
//     if (_tabPropFrame == null) _tabPropFrame = new TabPropFrame();
//     if (_tabPropFrame.isVisible()) _tabPropFrame.toFront();
//     else _tabPropFrame.show();
//     if (curEditor() == null) return;
//     Vector selectedFigs = curEditor().getSelectionManager().getFigs();
//     if (selectedFigs.size() == 1)
//       propertySheetSubject((Fig)selectedFigs.elementAt(0));
   }

  /** Tell the property sheet to display details of the given Fig. */
//   public static void propertySheetSubject(Fig f) {
//     if (_tabPropFrame != null) _tabPropFrame.select(f);
//   }

   /** Static initialization: set up the prop sheet. */
  //static {
    //if (_tabPropFrame == null) _tabPropFrame = new TabPropFrame();
    //_tabPropFrame.show();
  // }

  /** Return an exisiting instance of class Frame.  This is needed to
   *  create off-screen bit-maps. Needs-more-work: I think Swing keeps
   *  its own. */
  public static Frame someFrame() {
    Editor ce = curEditor();
    if (ce == null) return null;
    Component c = ce.getAwtComponent();
    while (c != null && !(c instanceof Frame))
      c = c.getParent();
    return (Frame) c;
  }

  ////////////////////////////////////////////////////////////////
  // Editor Modes

  /** True if the next global Mode should remain as the next global
   *  mode even after it has been given to one editor. */
  protected static boolean _sticky = false;

  /** Set whether the next global mode should remain as the next
   *  global mode even after it has been given to one editor. */
  public static void setSticky(boolean b) { _sticky = b; }

  public static boolean getSticky() { return _sticky; }

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
   *  finishes whatever it was meant to accomplish. If the sticky flag
   *  is set, the current mode stays the next global mode. Otherwise
   *  the defaultMode becomes the next global mode.
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

  /** A global dictionary of PropertyChangeListeners for Figs.  Most
   *  Figs will not have any listeners at any given moment, so I did
   *  not want to allocate an instance variable to hold
   *  listeners. Instead I use this global Hashtable with Figs ans
   *  keys and arrays of up to 4 listeners as values. <p>
   *
   *  Note: It is important that all listeners eventually remove
   *  themselves by calling removePropertyChangeListener.  Otherwise
   *  this table will keep pointers that can reduce grabage
   *  collection. */

  protected static Hashtable _pcListeners = new Hashtable();

  /** The most listeners a Fig can have, 4. */
  public static int MAX_LISTENERS = 4;

  /** Add a listener to a Fig.  Now the listener will get
   *  notifications of all property change events from that Fig. */
  public static void
  addPropertyChangeListener(Object src, PropertyChangeListener l) {
    PropertyChangeListener listeners[] =
      (PropertyChangeListener[]) _pcListeners.get(src);
    if (listeners == null) {
      listeners = new PropertyChangeListener[MAX_LISTENERS];
      _pcListeners.put(src, listeners);
    }
    // debugging warning
    if (_pcListeners.size() > 100)
      System.out.println("_pcListeners size = " + _pcListeners.size());
    for (int i=0; i < MAX_LISTENERS; ++i)
      if (listeners[i] == null) { listeners[i] = l; return; }
    System.out.println("ran out of listeners!");
  }

  public static void
  removePropertyChangeListener(Object s, PropertyChangeListener l){
    PropertyChangeListener listeners[] =
      (PropertyChangeListener[]) _pcListeners.get(s);
    boolean found = false;
    if (listeners == null) return;
    for (int i=0; i < MAX_LISTENERS; ++i)
      if (listeners[i] == l) { listeners[i] = null; found = true; }
    if (!found) System.out.println("listener not found!");
    for (int i=0; i < MAX_LISTENERS; ++i)
      if (listeners[i] != null) return;
    // s has no listeners, keep Hashtable size reasonable
    _pcListeners.remove(s);
  }

  /** Send a property change event to listeners of the src Fig. */
  public static void firePropChange(Object src, String propName,
			     boolean oldV, boolean newV) {
    firePropChange(src, propName, new Boolean(oldV), new Boolean(newV));
  }

  /** Send a property change event to listeners of the src Fig. */
  public static void firePropChange(Object src, String propName,
			     int oldV, int newV) {
    firePropChange(src, propName, new Integer(oldV), new Integer(newV));
  }

  /** Send a property change event to listeners of the src Fig. */
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
