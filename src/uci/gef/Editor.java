// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.

// File: Editor.java
// Classes: Editor
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.Serializable;
import com.sun.java.swing.*;

import uci.util.*;
import uci.graph.*;
import uci.ui.*;
import uci.gef.event.*;


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


public class Editor
implements Serializable, MouseListener, MouseMotionListener, KeyListener {
  //, GEF { //, IProps //Runnable, IStatusBar

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

  /** <A HREF="../features.html#event_skipping">
   *  <TT>FEATURE: event_skipping</TT></A>
   */
  //private transient uci.util.EventQueue _eventQueue = null;

  //private transient  Thread _eventHandler = null;

  protected Object _curNode = null;

  protected Fig _curFig = null;     // _focusFig //?

  private transient Component _awt_component;

  /** Each Editor has a RedrawManager that executes in a separate
   *  thread of control to update damaged regions of the display.
   *  <A HREF="../features.html#visual_updates">
   *  <TT>FEATURE: visual_updates</TT></A>
   *  <A HREF="../features.html#redraw_minimal">
   *  <TT>FEATURE: redraw_minimal</TT></A>
   */
  protected transient RedrawManager _redrawer = new RedrawManager(this);


  //protected boolean canPeekEvents = false;
  
  ////////////////////////////////////////////////////////////////
  /// methods related to editor state: graphical attributes, modes, view


  /** Set the current user interface mode to the given Mode
   *  instance. */
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


  /** Return the LayerComposite that holds the diagram being edited. */
  public LayerManager getLayerManager() { return _layerManager; }

  public int gridSize() { return 16; } // Needs-More-Work: prefs

  /** Return the net under the diagram being edited. */
  public GraphModel getGraphModel() {
    Layer active = _layerManager.getActiveLayer();
    if (active instanceof LayerPerspective)
      return ((LayerPerspective)active).getGraphModel();
    else return null;
  }

  public void setGraphModel(GraphModel gm) {
    Layer active = _layerManager.getActiveLayer();
    if (active instanceof LayerPerspective)
      ((LayerPerspective)active).setGraphModel(gm);
  }
    
  public GraphNodeRenderer getGraphNodeRenderer() {
    Layer active = _layerManager.getActiveLayer();
    if (active instanceof LayerPerspective)
      return ((LayerPerspective)active).getGraphNodeRenderer();
    else return null;
  }

  public void setGraphNodeRenderer(GraphNodeRenderer rend) {
    Layer active = _layerManager.getActiveLayer();
    if (active instanceof LayerPerspective)
      ((LayerPerspective)active).setGraphNodeRenderer(rend);
  }

  public GraphEdgeRenderer getGraphEdgeRenderer() {
    Layer active = _layerManager.getActiveLayer();
    if (active instanceof LayerPerspective)
      return ((LayerPerspective)active).getGraphEdgeRenderer();
    else return null;
  }

  public void setGraphEdgeRenderer(GraphEdgeRenderer rend) {
    Layer active = _layerManager.getActiveLayer();
    if (active instanceof LayerPerspective)
      ((LayerPerspective)active).setGraphEdgeRenderer(rend);
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

  protected void setUnderMouse(MouseEvent me) {
    Object node = null;
    int x = me.getX(), y = me.getY();
    Fig f = hit(x, y);
    if (f != _curFig) {
      if (_curFig instanceof MouseListener)
	((MouseListener)_curFig).mouseExited(me);
      if (f instanceof MouseListener)
	((MouseListener)f).mouseEntered(me);
    }
    _curFig = f;
    if (f instanceof FigNode) node = f.getOwner();
    if (node != _curNode) {
      if (_curNode instanceof MouseListener)
	((MouseListener)_curNode).mouseExited(me);
      if (node instanceof MouseListener)
	((MouseListener)node).mouseEntered(me);
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
   *  gridline). */
  public void snap(Point p) { if (_guide != null) _guide.snap(p); }

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
      return ed;
    }
    catch (java.lang.IllegalAccessException ignore) { }
    catch (java.lang.InstantiationException ignore) { }
    return null;
  }

  /** Construct a new Editor to edit the given NetList */
  public Editor(GraphModel gm, Component awt_comp) {
    this(gm, awt_comp, null);
  }

  public Editor(GraphModel gm) { this(gm, null, null); }

  public Editor() { this(null, null, null); }

  public Editor(Diagram d) { this(d.getGraphModel(), null, d.getLayer()); }

  public Editor(GraphModel gm, Component awt_comp, Layer lay) {
    _awt_component = awt_comp;
    defineLayers(gm, lay);

    mode(new ModeSelect(this));
    mode(new ModePopup(this));
    Globals.curEditor(this);
  }

  protected void defineLayers(GraphModel gm, Layer lay) {
    _layerManager.addLayer(new LayerGrid());
    //_layerManager.addLayer(new LayerPageBreaks());
    // the following line is an example of another "grid"
    //_layerManager.addLayer(new LayerPolar());
    if (lay != null) _layerManager.addLayer(lay);
    else if (gm == null) _layerManager.addLayer(new LayerDiagram("Example"));
    else _layerManager.addLayer(new LayerPerspective("untitled", gm));
  }


  public void preSave() { }
  public void postSave() { }
  public void postLoad() {
    if (_redrawer == null) _redrawer = new RedrawManager(this);;
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
    Dimension size = getAwtComponent().size();
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

  /** Paints the graphs nodes by calling paint() on layers, selections,
   *  and mode.  Whenever their is a change to the screen, this method
   *  will eventually be called from the RedrawManager.
   *
   * @see RedrawManager */
  public synchronized void paint(Graphics g) {
    getLayerManager().paint(g);
    _selectionManager.paint(g);
    _modeManager.paint(g);
  }

  public synchronized void print(Graphics g) {
    getLayerManager().paint(g);
    //does this work?
  }


  /** Reply the current selection objects of this editor.
   *  <A HREF="../features.html#selections">
   *  <TT>FEATURE: selections</TT></A>
   */
  public SelectionManager getSelectionManager() { return _selectionManager; }


  ////////////////////////////////////////////////////////////////
  // Frame and panel related methods

  public Component getAwtComponent() { return _awt_component; }
  public void setAwtComponent(Component c) {
    _awt_component = c;
  }

  public void setCursor(Cursor c) {
    if (getAwtComponent() != null) getAwtComponent().setCursor(c);
  }
  
  public Graphics getGraphics() {
    if (_awt_component == null) return null;
    return _awt_component.getGraphics();

//     Graphics g = _awt_component.getGraphics();
//     if ((g != null) && (_awt_component instanceof JComponent)) {
// 	JComponent parent = (JComponent) _awt_component;
// 	Rectangle bounds = parent.getBounds();
// 	if (parent instanceof JViewport) {
// 	  Point position = ((JViewport) parent).getViewPosition();
// 	  g.clipRect(bounds.x + position.x, bounds.y + position.y ,
// 		     bounds.width-1, bounds.height-1);
// 	}
// 	else {
// 	  g.clipRect(bounds.x, bounds.y ,
// 		     bounds.width-1, bounds.height-1);
// 	}
//       }
//     return g;
  }
    
  public Frame findFrame() {
    Component c = _awt_component;
    while (c != null && !(c instanceof Frame)) 
      c = c.getParent();
    return (Frame) c;
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

  public Color getBackground() {
    if (_awt_component == null) return Color.lightGray;
    return _awt_component.getBackground();
  }


  ////////////////////////////////////////////////////////////////
  // event handlers

  public void addGraphSelectionListener(GraphSelectionListener listener) {
    _selectionManager.addGraphSelectionListener(listener);
  }

  public void removeGraphSelectionListener(GraphSelectionListener listener) {
    _selectionManager.removeGraphSelectionListener(listener);
  }
  


    


  ////////////////////////////////////////////////////////////////
  // JDK 1.1 event handlers


  /**
   * Invoked when the mouse has been clicked on a component.
   */
  public void mouseClicked(MouseEvent me) { 
    RedrawManager.lock();
    Globals.curEditor(this);
    //setUnderMouse(me);
    _selectionManager.mouseClicked(me);
    _modeManager.mouseClicked(me);  
    RedrawManager.unlock();
    _redrawer.repairDamage();
  }

  /**
   * Invoked when a mouse button has been pressed on a component.
   */
  public void mousePressed(MouseEvent me) {
    RedrawManager.lock();
    Globals.curEditor(this);
    //setUnderMouse(me);
    _selectionManager.mousePressed(me);
    _modeManager.mousePressed(me);  
    RedrawManager.unlock();
    _redrawer.repairDamage();    
  }

  /**
     * Invoked when a mouse button has been released on a component.
     */
  public void mouseReleased(MouseEvent me) {
    RedrawManager.lock();
    Globals.curEditor(this);
    //setUnderMouse(me);
    _selectionManager.mouseReleased(me);
    _modeManager.mouseReleased(me);  
    RedrawManager.unlock();
    _redrawer.repairDamage();    
  }

  /**
     * Invoked when the mouse enters a component.
     */
  public void mouseEntered(MouseEvent me) {
    if (_awt_component != null) _awt_component.requestFocus();
    RedrawManager.lock();
    Globals.curEditor(this);
    mode(Globals.mode());
    setUnderMouse(me);
    _modeManager.mouseEntered(me);  
    RedrawManager.unlock();
    _redrawer.repairDamage();
  }

  /**
     * Invoked when the mouse exits a component.
     */
  public void mouseExited(MouseEvent me) {
    RedrawManager.lock();
//     Globals.curEditor(this);
    setUnderMouse(me);
    if (_curFig instanceof MouseListener)
      ((MouseListener)_curFig).mouseExited(me);
//     _selectionManager.mouseExited(me);
//     _modeManager.mouseExited(me);  
    RedrawManager.unlock();
    _redrawer.repairDamage();
  }

  /**
   * Invoked when a mouse button is pressed on a component and then 
   * dragged.  Mouse drag events will continue to be delivered to
   * the component where the first originated until the mouse button is
   * released (regardless of whether the mouse position is within the
   * bounds of the component).
   */
  public void mouseDragged(MouseEvent me) {
    AWTEvent nextEvent = null;
//     if (canPeekEvents) {
//       nextEvent = _awt_component.getToolkit().getSystemEventQueue().peekEvent();
//       if (nextEvent != null && nextEvent.getID() == me.getID()) return;
//     }
    RedrawManager.lock();
    Globals.curEditor(this);
    setUnderMouse(me);
    _selectionManager.mouseDragged(me);
    _modeManager.mouseDragged(me);  
    RedrawManager.unlock();
    if (nextEvent == null) _redrawer.repairDamage();
  }

  /**
     * Invoked when the mouse button has been moved on a component
     * (with no buttons no down).
     */
  public void mouseMoved(MouseEvent me) {
    AWTEvent nextEvent = null;
//     if (canPeekEvents) {
//       nextEvent = _awt_component.getToolkit().getSystemEventQueue().peekEvent();
//       if (nextEvent != null && nextEvent.getID() == me.getID()) return;
//     }
    RedrawManager.lock();
    Globals.curEditor(this);
    setUnderMouse(me);
    _selectionManager.mouseMoved(me);
    _modeManager.mouseMoved(me);  
    RedrawManager.unlock();
    if (nextEvent == null) _redrawer.repairDamage();
  }
  

  /**
   * Invoked when a key has been typed.
   * This event occurs when a key press is followed by a key release.
   */
  public void keyTyped(KeyEvent ke) {
    RedrawManager.lock();
    Globals.curEditor(this);
    _selectionManager.keyTyped(ke);
    _modeManager.keyTyped(ke);  
    RedrawManager.unlock();
    _redrawer.repairDamage();    
  }
  
  /**
   * Invoked when a key has been pressed.
     */
  public void keyPressed(KeyEvent ke) {
    RedrawManager.lock();
    Globals.curEditor(this);
    _selectionManager.keyPressed(ke);
    _modeManager.keyPressed(ke);  
    RedrawManager.unlock();
    _redrawer.repairDamage();    
  }
  
  /**
   * Invoked when a key has been released.
   */
  public void keyReleased(KeyEvent ke) {
//     RedrawManager.lock();
//     Globals.curEditor(this);
//     _selectionManager.keyReleased(ke);
//     _modeManager.keyReleased(ke);  
//     RedrawManager.unlock();
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
  public void executeCmd(Cmd c, InputEvent ie) {
    if (c == null) return;
    try { c.doIt(); }
    catch (java.lang.Throwable ex) {
      System.out.println("While executing " + c +
			 " on event " + ie +
			 " the following error occured:");
      ex.printStackTrace();
    }
  }

  ////////////////////////////////////////////////////////////////
  // notifications and updates


  public void removed(Fig f) {
    _selectionManager.deselect(f);
    remove(f);
  }


} /* end class Editor */
