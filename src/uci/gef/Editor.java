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

// File: Editor.java
// Classes: Editor
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;
import uci.util.*;
import uci.graph.*;
import uci.ui.*;

/** This class provides an editor frame for manipulating graphical
 *  documents. The editor is the central class of the graph editing
 *  framework, but it does not contain very much code. It can be this
 *  small because all the net-level models, graphical objects, layers,
 *  editor modes, editor actions, and supporting dialogs and frames are
 *  implemented in their own classes. The framework defines abstract
 *  and example classes for each of those class categories.<p>
 *
 *  An editor presents a tree of Layer's. Normally layers contain
 *  Fig's. Some Fig's are linked to
 *  NetPrimitive's. When Fig's are selected the Editor holds
 *  a Selection object. The behavior of the editor is determined by its
 *  current Mode. The Editor acts as a shell for executing Action's
 *  that modify the document or the Editor itself. <p>
 *
 *  A major goal of the graph editing framework is to make it easy to
 *  extend the framework for application to a specific domain. It is
 *  very important that new functionality can be added without
 *  modifying what is already there. The fairly small size of the
 *  editor is a good indicator that it is not a bottleneck for
 *  enhancing the framework. <p>
 *
 * @see Layer
 * @see Fig
 * @see NetPrimitive
 * @see Selection
 * @see Mode
 * @see Action */

public class Editor extends EventHandler
implements Observer, Runnable, IStatusBar, java.io.Serializable {
  //, GEF { //, IProps

  ////////////////////////////////////////////////////////////////
  // constants

  public static final int GRIP_SIZE = 8;

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The user interface mode that the editor is currently
   *  in. Generally modes that the user has to think about are a bad
   *  idea. But even in a very easy to use editor there are plenty of
   *  "spring-loaded" modes that change the way the system interperts
   *  input. For example, when placing a new node, the editor is in
   *  ModePlace, and when dragging a handle of an object the editor is
   *  in ModeModify. In each case moving or dragging the mouse has a
   *  different effect from what happens when the editor is in its
   *  default mode (usually ModeSelect). <p>
   *  <A HREF="../features.html#editing_modes">
   *  <TT>FEATURE: editing_modes</TT></A>
   *
   * @see ModeModify
   * @see ModeSelect
   * @see ModePlace */
  protected Mode _curMode;
  protected ModeManager _modeManager = new ModeManager(this);

  /** This points to the document object that the user is working
   *  on. At this point the framework does not have a very strong
   *  concept of document and there is no class Document. For now the
   *  meaning of this pointer is in the hands of the person applying
   *  this framework to an application.
   *  <A HREF="../features.html#integration_with_exising_code">
   *  <TT>FEATURE: integration_with_exising_code</TT></A>
   */
  protected Object _document;
  protected String _title;
  protected transient Label _statusLabel;

  /** _selections holds the SelectionMultiple object that describes
   *  all the selection objects for what the user currently has
   *  selected.
   *  <A HREF="../features.html#selections">
   *  <TT>FEATURE: selections</TT></A>
   */
  protected SelectionManager _selectionManager = new SelectionManager(this);

  /** This holds the LayerComposite being displayed in the editor.
   *  <A HREF="../features.html#layers">
   *  <TT>FEATURE: layers </TT></A>
   */
  protected LayerManager _layerManager = new LayerManager(this);

  /** The grid to snap points to if any.
   *  <A HREF="../features.html#snap_to_guide">
   *  <TT>FEATURE: snap_to_guide</TT></A>
   */
  protected Guide _guide = new GuideGrid(8);

  /** Default graphical attributes for newly created objects. */
  protected Hashtable _graphAttrs;

  /** Refers to an object that defines the editors menus and maps menu
   *  events into Actions. */
  protected transient MenuManager menu = null;

  /** <A HREF="../features.html#event_skipping">
   *  <TT>FEATURE: event_skipping</TT></A>
   */
  private transient uci.util.EventQueue _eventQueue = null;

  private transient  Thread _eventHandler = null;

  protected Object _curNode = null;

  protected Fig _curFig = null;     // _focusFig //?

  protected transient Frame _frame;
  private transient  Component _awt_component;

  /** Each Editor has a RedrawManager that executes in a separate
   *  thread of control to update damaged regions of the display.
   *  <A HREF="../features.html#visual_updates">
   *  <TT>FEATURE: visual_updates</TT></A>
   *  <A HREF="../features.html#redraw_minimal">
   *  <TT>FEATURE: redraw_minimal</TT></A>
   */
  protected transient RedrawManager _redrawer = new RedrawManager(this);

  ////////////////////////////////////////////////////////////////
  /// methods related to editor state: graphical attributes, modes, view

  /** Editors have a set of graphical attributes just like individual
   *  Fig' do. The editor uses these attributes to define
   *  the defaults for newly created Fig's.
   *
   * @see Fig */
  public Hashtable graphAttrs() { return _graphAttrs; }

  public Object get(String key) { return _graphAttrs.get(key); }
  public Object get(String key, Object def) {
    Object res = get(key);
    if (null != res) return res;
    return def;
  }

  public boolean put(String key, Object value) {
    _graphAttrs.put(key, value);
    return true;
  }

  public void put(Hashtable newAttrs) {
    Enumeration cur = newAttrs.keys();
    while (cur.hasMoreElements()) {
      String key = (String) cur.nextElement();
      Object val = newAttrs.get(key);
      put(key, val);
    }
  }

  public Enumeration keysIn(String category) {
    return (new Vector()).elements();
  }

  public boolean canPut(String key) { return true; }

  /** Set the current user interface mode to the given Mode
   *  instance.
   *  <A HREF="../features.html#editing_modes">
   *  <TT>FEATURE: editing_modes</TT></A>
   */
  public void mode(Mode m) {
    _modeManager.push(m);
    m.setEditor(this);
    Globals.showStatus(m.instructions());
  }

  // no mode getter?

  /** <A HREF="../features.html#editing_modes">
   *  <TT>FEATURE: editing_modes</TT></A>
   */
  public void finishMode() {
    _modeManager.pop();
    mode(Globals.mode());
    Globals.clearStatus();
  }

  public void showStatus(String msg) {
    if (_statusLabel != null) _statusLabel.setText(msg);
  }

  /** Return the LayerComposite that holds the diagram being edited. */
  public LayerManager getLayerManager() { return _layerManager; }

  /** Set the LayerManager that holds the diagram being edited. */
// //   public void setLayerManager(LayerManager lm) {
// //     if (_layerManager != null) _layerManager.removeEditor(this);
// //     _layerManager = lm;
// //     _layerManager.addEditor(this);
// //     if (getGraphics() != null) paint(getGraphics());
// //   }

  public int gridSize() { return 16; } // Needs-More-Work: prefs

  /** Return the net under the diagram being edited. */
  public GraphModel getGraphModel() {
    Layer active = _layerManager.getActiveLayer();
    if (active instanceof LayerPerspective)
      return ((LayerPerspective)active).getGraphModel();
    else return null;
  }

  ////////////////////////////////////////////////////////////////
  // methods related to adding, removing, and accessing DiagramElemnts
  // shown in the editor

  /** Returns a collection of all Figs in the layer
   *  currently being edited. Not used very much now, but could be
   *  useful for many Actions. Now used only by FigEdge. */
  public Enumeration figs() { return _layerManager.elements(); }

  /** Add a Fig to the diagram being edited. */
  public void add(Fig f) { getLayerManager().add(f); }

  /** Remove a Fig from the diagram being edited. */
  public void remove(Fig f) { getLayerManager().remove(f); }

  /** Reply the top Fig in the current layer that contains
   *  the given point. This is used in determining what the user
   *  clicked on, among other uses. */

  protected static Rectangle _hitRect
  = new Rectangle(0, 0, GRIP_SIZE, GRIP_SIZE);

  public final Fig hit(Point p) {
    _hitRect.move(p.x - GRIP_SIZE/2, p.y - GRIP_SIZE/2);
    return hit(_hitRect);
  }
  public final Fig hit(int x, int y) {
    _hitRect.move(x - GRIP_SIZE/2, y - GRIP_SIZE/2);
    return hit(_hitRect);
  }
  public final Fig hit(int x, int y, int w, int h) {
    return hit(new Rectangle(x, y, w, h));
  }
  public Fig hit(Rectangle r) { return getLayerManager().hit(r); }

  /** Find the NetNode represented by the FigNode under the mouse,
   *  if any */
  protected void setUnderMouse(Event e) {
    if (e.id != Event.MOUSE_MOVE && e.id != Event.MOUSE_DRAG) return;
    Object node = null;
    Fig f = hit(e.x, e.y);
    if (f != _curFig) {
      if (_curFig != null) _curFig.mouseExit(e, e.x, e.y);
      if (f != null) f.mouseEnter(e, e.x, e.y);
    }
    _curFig = f;
    if (f instanceof FigNode) node = f.getOwner();
    if (node != _curNode) {
      if (_curNode != null && _curNode instanceof EventHandler)
	((EventHandler)_curNode).mouseExit(e, e.x, e.y);
      if (node != null && node instanceof EventHandler)
	((EventHandler)node).mouseEnter(e, e.x, e.y);
    }
    _curNode = node;
  }

  ////////////////////////////////////////////////////////////////
  // document related methods

  /** Get and set document being edited. There are no deep semantics
   *  here yet, a "document" is up to you to define. */
  public Object document() { return _document; }
  public void document(Object d) { _document = d; }

  ////////////////////////////////////////////////////////////////
  // Guide and layout related commands

  /** Modify the given point to be on the guideline (In this case, a
   *  gridline).
   *  <A HREF="../features.html#snap_to_guide">
   *  <TT>FEATURE: snap_to_guide</TT></A>
   */
  public void snap(Point p) { if (_guide != null) _guide.snap(p); }

  /** <A HREF="../features.html#snap_to_guide">
   *  <TT>FEATURE: snap_to_guide</TT></A>
   */
  public Guide getGuide() { return _guide; }
  public void setGuide(Guide g) { _guide = g; }

  ////////////////////////////////////////////////////////////////
  // constructors and related functions

  /** Clone the receiving editor. Called from ActionSpawn. Subclasses
   *  of Editor should override this method. */
  public Object clone() {
    try {
      Editor ed = (Editor) this.getClass().newInstance();
      ed.getLayerManager().addLayer(_layerManager.getActiveLayer());
      //needs-more-work: does not duplicate layer stack!
      ed.document(document());
      ed.setTitle("Clone of " + _title);
      return ed;
    }
    catch (java.lang.IllegalAccessException ignore) { }
    catch (java.lang.InstantiationException ignore) { }
    return null;
  }

  /** Construct a new Editor to edit the given NetList */
  //public Editor(NetList net, Component awt_comp) { this(net, awt_comp, null); }

  public Editor(GraphModel gm, Component awt_comp) {
    this(gm, awt_comp, null);
    // needs-more-work: set graph model
  }

  //public Editor(NetList net) { this(net, null, null); }
  public Editor(GraphModel gm) { this(gm, null, null); }

  public Editor() { this(null, null, null); }

  //public Editor(NetList net, Component awt_comp, Layer lay) {
  public Editor(GraphModel gm, Component awt_comp, Layer lay) {
    _awt_component = awt_comp;
    defineLayers(gm, lay);
    _graphAttrs = new Hashtable();

    /* Now set up the menu items */
    setMenuBar(menuBar());
    // //_modeManager.setEditor(this);
    mode(new ModeExampleKeys(this));
    mode(new ModeSelect(this));
    Globals.curEditor(this);
  }

  //protected void defineLayers(NetList net, Layer lay) {
  protected void defineLayers(GraphModel gm, Layer lay) {
    _layerManager.addLayer(new LayerGrid());
    _layerManager.addLayer(new LayerPageBreaks());
    // the following line is an example of another "grid"
    // _layerManager.addLayer(new LayerPolar());
    if (lay != null) _layerManager.addLayer(lay);
    else if (gm == null) _layerManager.addLayer(new LayerDiagram("Example"));
    else _layerManager.addLayer(new LayerPerspective("untitled", gm));
  }

  ////////////////////////////////////////////////////////////////
  // recording damage to the display for later repair

  /** Calling any one of the following damaged() methods adds a
   *  damaged region (one or more rectangles) to this editors
   *  RedrawManager.
   *  <A HREF="../features.html#visual_updates">
   *  <TT>FEATURE: visual_updates</TT></A>
   *  <A HREF="../features.html#redraw_minimal">
   *  <TT>FEATURE: redraw_minimal</TT></A>
   */
  public void damaged(Rectangle r) { _redrawer.add(r); }

  /** <A HREF="../features.html#visual_updates">
   *  <TT>FEATURE: visual_updates</TT></A>
   *  <A HREF="../features.html#redraw_minimal">
   *  <TT>FEATURE: redraw_minimal</TT></A>
   */

  // //private Rectangle _damageBounds = new Rectangle(0, 0, 0, 0);

  public void damaged(Fig f) {
    if (_redrawer == null) _redrawer = new RedrawManager(this);
    // the line above should not be needed, but without it I get
    // NullPointerExceptions...
    // // f.stuffBounds(_damageBounds);
    if (f != null) _redrawer.add(f);
  }

  /** <A HREF="../features.html#visual_updates">
   *  <TT>FEATURE: visual_updates</TT></A>
   *  <A HREF="../features.html#redraw_minimal">
   *  <TT>FEATURE: redraw_minimal</TT></A>
   */

  public void damaged(Selection sel) {
    if (sel != null) {
      // //sel.stuffBounds(_damageBounds);
      //Rectangle r = sel.getBounds();
      _redrawer.add(sel);
    }
  }

  /** Mark the entire visible area of this Editor as
   *  damaged. Currently called when a LayerGrid is adjusted. This will
   *  be useful for ActionRefresh if I get around to it. Also some
   *  Actions may perfer to do this instead of keeping track of all
   *  modified objects, but only in cases where most of the visible
   *  area is expected to change anyway.
   *  <A HREF="../features.html#visual_updates">
   *  <TT>FEATURE: visual_updates</TT></A>
   */
  public void damageAll() {
    if (_redrawer == null) _redrawer = new RedrawManager(this);
    Dimension size = size();
    _redrawer.add(new Rectangle(0, 0, size.width, size.height));
  }

  /** Tell my RedrawManager to repair all outstanding damaged regions
   *  now. Generally, you should not call this method explicitly. It
   *  already gets called periodically from another thread of
   *  control. You should call it if you need a very tight user
   *  interface feedback loop...
   *  <A HREF="../features.html#visual_updates">
   *  <TT>FEATURE: visual_updates</TT></A>
   */
  public void repairDamage() { _redrawer.repairDamage(); }

  ////////////////////////////////////////////////////////////////
  // display methods

  /** Paint anything that can be seen in the given Rectangle. */
  public void paintRect(Rectangle r, Graphics g) {
    // useful for debugging/learning redraw logic
    //if (Boolean.getBoolean("DebugRedrawArea"))
    //g.drawRect(r.x-1, r.y-1, r.width+2, r.height+2);
    paint(g);
  }

  /** Paints the graphs nodes by calling paint() on layers, selections,
   *  and mode.  Whenever their is a change to the screen, this method
   *  will eventually be called from the RedrawManager.
   *
   * @see RedrawManager */
  public synchronized void paint(Graphics g) {
    getLayerManager().paint(g);
    if (!(g instanceof PrintGraphics)) {
      _selectionManager.paint(g);
      _modeManager.paint(g);
    }
  }

  public synchronized void print(Graphics g) { paint(g); }

  ////////////////////////////////////////////////////////////////
  // selection methods

  /** Add the given item to this editors selections.
   *  <A HREF="../features.html#selections">
   *  <TT>FEATURE: selections</TT></A>
   */
  public void select(Fig f) { _selectionManager.select(f); }

  /** Remove the given item from this editors selections.
   *  <A HREF="../features.html#selections">
   *  <TT>FEATURE: selections</TT></A>
   */
  public void deselect(Fig f) { _selectionManager.deselect(f); }

  /** Select the given item if it was not already selected, and
   *  vis-a-versa.
   *  <A HREF="../features.html#selections">
   *  <TT>FEATURE: selections</TT></A>
   */
  public void toggleItem(Fig f) { _selectionManager.toggle(f); }

  /** Deslect everything that is currently selected.
   *  <A HREF="../features.html#selections">
   *  <TT>FEATURE: selections</TT></A>
   */
  public void deselectAll() { _selectionManager.deselectAll(); }

  /** Select a collection of Fig's.
   *  <A HREF="../features.html#selections">
   *  <TT>FEATURE: selections</TT></A>
   */
  public void select(Vector items) {_selectionManager.select(items);}

  /** Toggle the selection of a collection of Fig's.
   *  <A HREF="../features.html#selections">
   *  <TT>FEATURE: selections</TT></A>
   */
  public void toggleItems(Vector items) {_selectionManager.toggle(items);}

  /** Reply the current selection objects of this editor.
   *  <A HREF="../features.html#selections">
   *  <TT>FEATURE: selections</TT></A>
   */
  public SelectionManager getSelectionManager() { return _selectionManager; }

  /** <A HREF="../features.html#selections">
   *  <TT>FEATURE: selections</TT></A>
   */
  public Vector selectedFigs() { return _selectionManager.getFigs(); }

  ////////////////////////////////////////////////////////////////
  // property sheet methods

  public void updatePropertySheet() {
    if (Globals.curEditor() != this) return;
    if (_selectionManager.size() != 1) Globals.propertySheetSubject(null);
    else {
      Fig f = (Fig) selectedFigs().elementAt(0);
      Globals.propertySheetSubject(f);
    }
  }

  ////////////////////////////////////////////////////////////////
  // Frame and panel related methods

  /** Reply the Frame for the editor. */
  public Frame frame() { return _frame; }
  public void frame(Frame f) {
    _frame = f;
    f.setMenuBar(menuBar());
  }

  public Component awt_comp() { return _awt_component; }
  public void awt_comp(Component c) {
    _awt_component = c;
  }

  public Graphics getGraphics() {
    if (_awt_component == null) return null;
    return _awt_component.getGraphics();
  }

  public void setMenuBar(MenuBar mb) {
    if (_frame != null) _frame.setMenuBar(mb);
  }

  public MenuBar menuBar() {
    if (menu == null) menu = new MenuManager(this);
    return menu.menuBar;
  }

  public void addMenu(Menu m) {
    menu.add(m);
    setMenuBar(menuBar());
  }

  public void setTitleOnly(String title) { _title = title; }
  public void setTitle(String title) {
    setTitleOnly(title);
    if (_frame != null) _frame.setTitle(title);
  }

  public Dimension size() { return _awt_component.size(); }

  public void resize(int w, int h) { _awt_component.resize(w, h); }

  public void hide() {
    if (_frame != null) _frame.hide();
  }

  /** Show the editor. Use an existing frame and awt component if
   *  they were supplied, otherwise make a new Panel and Frame.
   *  <A HREF="../features.html#editor_frame">
   *  <TT>FEATURE: editor_frame</TT></A>.
   *  <A HREF="../features.html#editor_in_browser">
   *  <TT>FEATURE: editor_in_browser</TT></A>.
   */
  public void show() {
    if (_awt_component == null) {
      ForwardingPanel fp = new ForwardingPanel(this, new Dimension(400, 300));
      ForwardingFrame ff  = new ForwardingFrame(this, new Dimension(400, 350));
      ff.setLayout(new BorderLayout());
      ff.add("Center", fp);
      if (Globals.getAppletContext() != null) {
	_statusLabel = new Label("");
	_statusLabel.setFont(new Font("Dialog", Font.PLAIN, 10));
	ff.add("South", _statusLabel);
      }
      ff.pack();
      frame(ff);
      awt_comp(fp);
      setTitle(_title);
    }
    if (_frame != null) _frame.show();
  }

  public void move(int x, int y) {
    _awt_component.move(x, y);
  }

  public Image createImage(int w, int h) {
    if (_awt_component == null) return null;
    try { if (_awt_component.getPeer() == null) _awt_component.addNotify(); }
    catch (java.lang.NullPointerException ignore) { }
    // This catch works around a bug:
    // Sometimes there is an exception in the AWT peer classes,
    // but the next line should still work, despite the exception
    return _awt_component.createImage(w, h);
  }

  public Color getBackground() { return _awt_component.getBackground(); }

  /** close this editor window */
  public void close() {
    // needs-more-work
    // if (!saved) { ... }
    if (_frame != null) { _frame.hide(); _frame.dispose(); }
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** Pass any Menu events to my MenuManager object.
   *
   * @see MenuManager */
  public boolean handleMenuEvent(Event e) {
    if (menu == null) menu = new MenuManager(this);
    return menu.handleMenuEvent(e) || super.handleMenuEvent(e);
  }

  /** The next several methods process user interface events.  There
   *  are some events that the editor handles itself, but most useful
   *  work is done by delegating events to the current mode, the
   *  Fig under the mouse or the node under the mouse. Menu
   *  events are always delegated to my MenuManager object. This scheme
   *  could be improved by using another object that defines the
   *  bindings of arbitrary UI events to Actions. <p>
   *
   *  Please always keep the number of built-in command and actions
   *  to a minimum.
   */

  /** Before an event is handled by dispatchEvent(), lock the
   *  redrawManager to avoid dirt. If the mouse entered this Editor,
   *  set this Editor's mode to the next global mode. */
  public void preHandleEvent(Event e) {
    RedrawManager.lock();
    if (e.id == Event.MOUSE_ENTER) mode(Globals.mode());
  }

  /** After an event has been handled by dispatchEvent(), unlock the
   *  RedrawManager so that damage can be repaired. */
  public void postHandleEvent(Event e) {
    RedrawManager.unlock();
    //repairDamage();
  }

  /** Rather than handling user events immediately, store them in a
   *  uci.util.EventQueue for later processing by dispatchEvent(). This
   *  allows me to skip some events which is VERY important to
   *  performance.
   *  <A HREF="../features.html#event_skipping">
   *  <TT>FEATURE: event_skipping</TT></A>
   */
  public boolean handleEvent(Event e) {
    if (_eventQueue == null) {
      _eventQueue = new uci.util.EventQueue();
      _eventHandler = new Thread(this);
      _eventHandler.start();
    }
    if (_redrawer == null) _redrawer = new RedrawManager(this);
    synchronized (_eventQueue) {
      _eventQueue.enqueue(e);
      _eventQueue.notify();
    }
    return true; // assume every event gets handled
  }

  /** Each Editor has its own event processing thread that takes ALL
   *  events that have been queued in handleEvent and processes them as
   *  defined in uci.util.EventHandler (i.e. it passes them to
   *  preHandleEvent, dispatchEvent, and postHandleEvent), then the
   *  Editor repairs damaged regions of the screen. Note that events
   *  are processed in batches: several are queued up, then they are
   *  all processed at once, and only one (at most) repair is done and
   *  the property sheet is updated once.
   *  <A HREF="../features.html#event_skipping">
   *  <TT>FEATURE: event_skipping</TT></A>
   */
  public void run()  {
    while (true) {
      // process all events that have been enqueued by handleEvent
      while (!_eventQueue.isEmpty()) super.handleEvent(_eventQueue.dequeue());
      if (_redrawer != null && _redrawer.pendingDamage()) {
	repairDamage();
	updatePropertySheet();
      }
      synchronized (_eventQueue) {
      	try { _eventQueue.wait(); } // wait for one event to be enqueued
      	catch (InterruptedException ignore) { }
      }
      // allow a few more events to build up, if they do it quickly
      try { _eventHandler.sleep(10); }
      catch (InterruptedException ignore) { }
    }
  }

  /** Actually process events (i.e., take action to modify the
   *  Figs presented by this Editor) */
  public boolean dispatchEvent(Event e) {
    if (e.x == -1 && e.y == -1) return false; // work-around a weird bug

    //  Useful if you are customizing the redraw logic
    //if (Boolean.getBoolean("MetaForcesRepair"))
    //if (e.metaDown()) damaged(new Rectangle(e.x, e.y, 100, 100));

    //if (Dbg.on)
    //Dbg.log("DebugDispatch", "really handing an event: " + e.toString());

    if (e.id == Event.MOUSE_MOVE || e.id == Event.MOUSE_DRAG)
      if (_eventQueue.nextIsSameAs(e) && _eventQueue.size() > 2)
	return false; // drop some useless events: this REALLY helps performance

    Globals.curEditor(this);
    setUnderMouse(e);
    try {
      if (_selectionManager.handleEvent(e)) return true;
      if (_modeManager.handleEvent(e)) return true;
      if (_curNode != null && _curNode instanceof EventHandler)
	if (((EventHandler)_curNode).handleEvent(e)) return true;
    }
    catch (java.lang.Throwable ex) {
      System.out.println("While processing event " + e.toString() +
			 " the following error occured:");
      ex.printStackTrace();
    }

    switch(e.id) {
    case Event.WINDOW_DESTROY:
      if (e.target == _frame) {
	close();
	return true;
      }
      break;
    }
    return super.dispatchEvent(e);
  }

  ////////////////////////////////////////////////////////////////
  // Action methods

  /** The editor acts as a shell for Actions. This method executes the
   *  given action in response to the given event (some Action's look
   *  at the Event's that invoke them, even though this is
   *  discouraged). The Editor executes the Action in a safe
   *  environment so that buggy actions cannot crash the whole
   *  Editor. <p>
   *
   *  Needs-more-work: Action debugging facilities are needed.
   *  Needs-more-work: support for dynamic loading of actions... here?
   *  <A HREF="../features.html#editing_actions">
   *  <TT>FEATURE: editing_actions</TT></A>
   *  <A HREF="../features.html#undo_and_redo">
   *  <TT>FEATURE: undo_and_redo</TT></A>
   *  <A HREF="../features.html#macros">
   *  <TT>FEATURE: macros</TT></A>
   *  <A HREF="../features.html#integrated_debugging_support">
   *  <TT>FEATURE: integrated_debugging_support</TT></A>
   * @see Action */
  public void executeAction(Action act, Event e) {
    if (act == null) return;
    try { act.doIt(e); }
    catch (java.lang.Throwable ex) {
      System.out.println("While executing " + act.toString() +
			 " on event " + e.toString() +
			 " the following error occured:");
      ex.printStackTrace();
    }
  }

  ////////////////////////////////////////////////////////////////
  // notifications and updates

  /** Process change notifications from objects that the Editor
   *  observes. An editor observes its Layer's, which intern observe
   *  their Fig's which inturn observe their net-level
   *  models. When something changes state the notification is
   *  propagated up and eventually cause a damaged region and a
   *  redraw.  Needs-more-work: This is too complex, indirect, and
   *  slow. But the concept is simple.  */
  public void update(Observable o, Object arg) {
    if (arg instanceof Vector) {
      String s = (String) ((Vector)arg).elementAt(0);
      Object obj = ((Vector)arg).elementAt(1);
    }
  }

  public void notifyRemoved(Fig f) {
    _selectionManager.deselect(f);
    remove(f);
  }

  public void notifyRemoved(Object np) {
    Fig f = getLayerManager().presentationFor(np);
    if (f != null) remove(f);
  }

  public void notifyChanged(Fig f) { f.damagedIn(this); }

} /* end class Editor */
