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



// File: Editor.java
// Classes: Editor
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.Serializable;
import javax.swing.*;

import uci.util.*;
import uci.graph.*;
import uci.ui.*;
import uci.gef.event.*;


/** This class provides an editor for manipulating graphical
 *  documents. The editor is the central class of the graph editing
 *  framework, but it does not contain very much code.  It can be this
 *  small because all the net-level models, graphical objects, layers,
 *  editor modes, editor commands, and supporting dialogs and frames are
 *  implemented in their own classes. <p>
 *
 *  An Editor's LayerManager has a stack of Layer's. Normally Layers
 *  contain Figs.  Some Figs are linked to NetPrimitives.  When Figs
 *  are selected the SelectionManager holds a Selection object.  The
 *  behavior of the Editor is determined by its current Mode.  The
 *  Editor's ModeManager keeps track of all the active Modes.  Modes
 *  interpert user input events and decide how to change the state of
 *  the diagram.  The Editor acts as a shell for executing Commands
 *  that modify the document or the Editor itself. <p>
 *
 *  When Figs change visible state (e.g., color, size, or postition)
 *  they tell their Layer that they are damaged and need to be
 *  repainted. The Layer tells all Editors that are editing that
 *  Layer.  Each Editor has a RedrawManager that keeps track of what
 *  parts of the screen need to be redrawn, and does the redraws
 *  asynchronously.
 *
 *  A major goal of GEF is to make it easy to extend the framework for
 *  application to a specific domain. It is very important that new
 *  functionality can be added without modifying what is already
 *  there. The fairly small size of the Editor is a good indicator
 *  that it is not a bottleneck for enhancing the framework. <p>
 *
 * @see Layer
 * @see Fig
 * @see NetPrimitive
 * @see Selection
 * @see Mode
 * @see Cmd */


public class Editor
implements Serializable, MouseListener, MouseMotionListener, KeyListener {

  ////////////////////////////////////////////////////////////////
  // constants

  /** Clicking exactly on a small shape is hard for users to
   *  do. GRIP_MARGIN gives them a chance to have the mouse outside a
   *  Fig by a few pixels and still hit it.  */
  public static final int GRIP_SIZE = 8;

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The user interface mode that the Editor is currently
   *  in.  Generally Modes that the user has to think about are a bad
   *  idea.  But even in a very easy to use editor there are plenty of
   *  "spring-loaded" modes that change the way the system interperts
   *  input.  For example, when placing a new node, the editor is in
   *  ModePlace, and when dragging a handle of an object the editor is
   *  in ModeModify.  In each case moving or dragging the mouse has a
   *  different effect.
   *
   * @see ModeModify
   * @see ModeSelect
   * @see ModePlace */
  protected ModeManager _modeManager = new ModeManager(this);

  /** This points to the document object that the user is working
   *  on. At this point the framework does not have a very strong
   *  concept of document and there is no class Document. For now the
   *  meaning of this pointer is in the hands of the person applying
   *  this framework to an application. */
  protected Object _document;

  /** _selections holds the SelectionMultiple object that describes
   *  all the selection objects for what the user currently has
   *  selected. */
  protected SelectionManager _selectionManager = new SelectionManager(this);

  /** The LayerManager for this Editor. */
  protected LayerManager _layerManager = new LayerManager(this);

  /** The grid to snap points to.   */
  protected Guide _guide = new GuideGrid(8);

  /** The Fig that the mouse is in. */
  protected Fig _curFig = null;

  /** The Selection object that the mouse is in. */
  protected Selection _curSel = null;

  /** The AWT window or panel that the Editor draws to. */
  private transient Component _awt_component;

  /** The ancestor of _awt_component that has a peer that can create
   *  an image. */
  private transient Component _peer_component = null;

  /** Each Editor has a RedrawManager that executes in a separate
   *  thread of control to update damaged regions of the display. */
  //protected transient RedrawManager _redrawer = null; //-new RedrawManager(this);


  protected transient FigTextEditor _activeTextEditor = null;


  ////////////////////////////////////////////////////////////////
  // constructors and related functions


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
    mode(new ModeDragScroll(this));
    Globals.curEditor(this);
  }

  protected void defineLayers(GraphModel gm, Layer lay) {
    _layerManager.addLayer(new LayerGrid());
    _layerManager.addLayer(new LayerPageBreaks());
    // the following line is an example of another "grid"
    //_layerManager.addLayer(new LayerPolar());
    if (lay != null) _layerManager.addLayer(lay);
    else if (gm == null) _layerManager.addLayer(new LayerDiagram("Example"));
    else _layerManager.addLayer(new LayerPerspective("untitled", gm));
  }

  /** Called before the Editor is saved to a file. */
  public void preSave() { _layerManager.preSave(); }

  /** Called after the Editor is saved to a file. */
  public void postSave() { _layerManager.postSave(); }

  /** Called after the Editor is loaded from a file. */
  public void postLoad() {
    //- if (_redrawer == null) _redrawer = new RedrawManager(this);
    _layerManager.postLoad();
  }

  /** Return true if the Grid layer is currently hidden. */
  public boolean getGridHidden() {
    boolean h = false;
    Layer l = _layerManager.findLayerNamed( "Grid" );
    if( l != null ) h = l.getHidden();
    return h;
  }

  /** Set the hidden state of the Grid layer. */
  public void setGridHidden( boolean b ) {
    Layer l = _layerManager.findLayerNamed( "Grid" );
    if( l != null ) l.setHidden( b );
  }

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
    catch (java.lang.IllegalAccessException ignore) {
      System.out.println("IllegalAccessException in spawn");
    }
    catch (java.lang.InstantiationException ignore) {
      System.out.println("InstantiationException in spawn");
    }
    return null;
  }

  ////////////////////////////////////////////////////////////////
  /// methods related to editor state: graphical attributes, modes, view

  public ModeManager getModeManager() { return _modeManager; }

  /** Set the current user interface mode to the given Mode instance. */
  public void mode(Mode m) {
    _modeManager.push(m);
    m.setEditor(this);
    Globals.showStatus(m.instructions());
  }

  /** Set this Editor's current Mode to the next global Mode. */
  public void finishMode() {
    _modeManager.pop();
    mode(Globals.mode());
    Globals.clearStatus();
  }


  /** Return the LayerComposite that holds the diagram being edited. */
  public LayerManager getLayerManager() { return _layerManager; }

  /** The size of the grid for grid snap. */
  //  public int gridSize() { return 16; } // Needs-More-Work: prefs

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

  /** Get the renderer object that decides how to display nodes */
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

  /** Get the renderer object that decides how to display edges */
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
  // methods related to adding, removing, and accessing Figs
  // shown in the editor

  /** Returns a collection of all Figs in the layer currently being edited. */
  public Enumeration figs() { return _layerManager.elements(); }

  /** Add a Fig to the diagram being edited. */
  public void add(Fig f) { getLayerManager().add(f); }

  /** Remove a Fig from the diagram being edited. */
  public void remove(Fig f) { getLayerManager().remove(f); }

  /** Temp var used to implement hit() without doing memory allocation. */
  protected static Rectangle _hitRect
  = new Rectangle(0, 0, GRIP_SIZE, GRIP_SIZE);

  /** Reply the top Fig in the current layer that contains
   *  the given point. This is used in determining what the user
   *  clicked on, among other uses. */
  public final Fig hit(Point p) {
    _hitRect.setLocation(p.x - GRIP_SIZE/2, p.y - GRIP_SIZE/2);
    return hit(_hitRect);
  }
  public final Fig hit(int x, int y) {
    _hitRect.setLocation(x - GRIP_SIZE/2, y - GRIP_SIZE/2);
    return hit(_hitRect);
  }
  public final Fig hit(int x, int y, int w, int h) {
    return hit(new Rectangle(x, y, w, h));
  }
  public Fig hit(Rectangle r) { return getLayerManager().hit(r); }

  /** Find the Fig under the mouse, and the node it represents, if any */
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

    Selection sel = _selectionManager.findSelectionAt(x, y);
    if (sel != _curSel) {
	if (_curSel != null) _curSel.mouseExited(me);
	if (sel != null) sel.mouseEntered(me);
    }
    _curSel = sel;
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
  // recording damage to the display for later repair

  /** Calling any one of the following damaged() methods adds a
   *  damaged region (rectangle) to this Editor's RedrawManager.   */
  public void damaged(Rectangle r) {
    //- _redrawer.add(r);
     ((JComponent) getAwtComponent()).repaint(1000, r.x-32, r.y-32,
     					     r.width+64, r.height+64);
  }

  public void damaged(Fig f) {
    //- if (_redrawer == null) _redrawer = new RedrawManager(this);
    // the line above should not be needed, but without it I get
    // NullPointerExceptions...
    //- if (f != null) _redrawer.add(f);
    ((JComponent) getAwtComponent()).repaint(1000, f.getX()-32, f.getY()-32,
					     f.getWidth()+64, f.getHeight()+64);
  }

  public void damaged(Selection sel) {
    //- if (sel != null) _redrawer.add(sel);
    damaged(sel.getBounds());
  }

  /** Mark the entire visible area of this Editor as
   *  damaged. Currently called when a LayerGrid is adjusted. This will
   *  be useful for ActionRefresh if I get around to it. Also some
   *  Actions may perfer to do this instead of keeping track of all
   *  modified objects, but only in cases where most of the visible
   *  area is expected to change anyway. */
  public void damageAll() {
    //- if (_redrawer == null) _redrawer = new RedrawManager(this);
    Dimension size = getAwtComponent().getSize();
    //- _redrawer.add(new Rectangle(0, 0, size.width, size.height));
    ((JComponent) getAwtComponent()).repaint();

  }

  /** Tell my RedrawManager to repair all outstanding damaged regions
   *  now. Generally, you should not call this method explicitly. It
   *  already gets called periodically from another thread of
   *  control. You should call it if you need a very tight user
   *  interface feedback loop... */
  public void repairDamage() {
    //- _redrawer.repairDamage();
  }

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
    if (_activeTextEditor != null) _activeTextEditor.repaint();
  }

  public synchronized void print(Graphics g) {
    getLayerManager().paint(g);
    //does this work?
  }


  /** Scroll the JGraph so that the given point is visible.  This is
   *  used when the user wants to drag an object a long distance. This
   *  is commented out right now because it causes too many out of
   *  memory errors and the size of the JGraphInternalPanel is not set
   *  properly. */
  public void scrollToShow(int x, int y) {  
    //   Component c = getAwtComponent();
    //   if (c != null && c.getParent() instanceof JViewport) {
    //     JViewport view = (JViewport) c.getParent();
    //     view.scrollRectToVisible(new Rectangle(x - 10, y - 10, 20, 20));
    //   }
  }


  /** Reply the current SelectionManager of this Editor. */
  public SelectionManager getSelectionManager() { return _selectionManager; }


  ////////////////////////////////////////////////////////////////
  // Frame and panel related methods

  public Component getAwtComponent() { return _awt_component; }
  public void setAwtComponent(Component c) {
    _awt_component = c;
    _peer_component = null;
  }

  public void setActiveTextEditor(FigTextEditor fte) {
    FigTextEditor oldTextEditor = _activeTextEditor;
    _activeTextEditor = fte;
    if (oldTextEditor != null)
      oldTextEditor.endEditing();
  }

  public void setCursor(Cursor c) {
    if (getAwtComponent() != null) {
      getAwtComponent().setCursor(c);
      java.awt.Toolkit.getDefaultToolkit().sync();
    }
  }

  //protected static Graphics _graphics = null;
  /** Get the graphics context object that this editor should draw on. */
  public Graphics getGraphics() {
//     if (_graphics != null) {
//       Rectangle bbox = _awt_component.getBounds();
//       _graphics.setClip(new Rectangle(0, 0, bbox.width, bbox.height));
//       return _graphics;
//     }
    if (_awt_component == null) return null;
//     if (_awt_component instanceof JGraphInternalPane)
//       return ((JGraphInternalPane) _awt_component).getGraphicsCounted();
    return _awt_component.getGraphics();
//     System.out.println("made a graphics bounds =" + _graphics.getClipBounds());
//     System.out.println("comp bounds=" + _awt_component.getBounds());
//     return _graphics;
  }

  /** Find the AWT Frame that this Editor is being displayed in. This
   *  is needed to open a dialog box. */
  public Frame findFrame() {
    Component c = _awt_component;
    while (c != null && !(c instanceof Frame)) 
      c = c.getParent();
    return (Frame) c;
  }

  /** Create an Image (an off-screen bit-map) to be used to reduce
   *  flicker in redrawing. */
  public Image createImage(int w, int h) {
    if (_awt_component == null) return null;
    if (_peer_component == null) {
      _peer_component = _awt_component;
      while (_peer_component.getPeer() instanceof
	     java.awt.peer.LightweightPeer)
	_peer_component = _peer_component.getParent();
    }
//     try { if (_awt_component.getPeer() == null) _awt_component.addNotify(); }
//     catch (java.lang.NullPointerException ignore) { }
    // This catch works around a bug:
    // Sometimes there is an exception in the AWT peer classes,
    // but the next line should still work, despite the exception
    return _peer_component.createImage(w, h);
  }

  /** Get the backgrund color of the Editor.  Often, none of the
   *  background will be visible because LayerGrid covers the entire
   *  drawing area. */
  public Color getBackground() {
    if (_awt_component == null) return Color.lightGray;
    return _awt_component.getBackground();
  }


  ////////////////////////////////////////////////////////////////
  // event handlers

  /** Remember to notify listener whenever the selection changes. */
  public void addGraphSelectionListener(GraphSelectionListener listener) {
    _selectionManager.addGraphSelectionListener(listener);
  }

  /** Stop notifing listener of selection changes. */
  public void removeGraphSelectionListener(GraphSelectionListener listener) {
    _selectionManager.removeGraphSelectionListener(listener);
  }

  /** Remember to notify listener whenever the mode changes. */
  public void addModeChangeListener(ModeChangeListener listener) {
    _modeManager.addModeChangeListener(listener);
  }

  /** Stop notifing listener of mode changes. */
  public void removeModeChangeListener(ModeChangeListener listener) {
    _modeManager.removeModeChangeListener(listener);
  }


  ////////////////////////////////////////////////////////////////
  // JDK 1.1 AWT event handlers


  /** Invoked after the mouse has been pressed and released.  All
   *  events are passed on the SelectionManager and then ModeManager. */
  public void mouseClicked(MouseEvent me) { 
    //- RedrawManager.lock();
    Globals.curEditor(this);
    //setUnderMouse(me);
    if (_curFig instanceof MouseListener)
      ((MouseListener)_curFig).mouseClicked(me);
    _selectionManager.mouseClicked(me);
    _modeManager.mouseClicked(me);
    //- RedrawManager.unlock();
    //- _redrawer.repairDamage();
  }

  /** Invoked when a mouse button has been pressed. */
  public void mousePressed(MouseEvent me) {
    if (me.isConsumed()) return;
    setActiveTextEditor(null);
    //if (getAwtComponent() != null) getAwtComponent().requestFocus();
    //- RedrawManager.lock();
    Globals.curEditor(this);
    //setUnderMouse(me);
    if (_curFig instanceof MouseListener)
      ((MouseListener)_curFig).mousePressed(me);
    _selectionManager.mousePressed(me);
    _modeManager.mousePressed(me);
    //- RedrawManager.unlock();
    //- _redrawer.repairDamage();
  }

  /** Invoked when a mouse button has been released. */
  public void mouseReleased(MouseEvent me) {
    //- RedrawManager.lock();
    Globals.curEditor(this);
    //setUnderMouse(me);
    if (_curFig instanceof MouseListener)
      ((MouseListener)_curFig).mouseReleased(me);
    _selectionManager.mouseReleased(me);
    _modeManager.mouseReleased(me);
    //- RedrawManager.unlock();
    //- _redrawer.repairDamage();
  }

  /** Invoked when the mouse enters the Editor. */
  public void mouseEntered(MouseEvent me) {
    if (_activeTextEditor != null) _activeTextEditor.requestFocus();
    else if (_awt_component != null) _awt_component.requestFocus();
    //- RedrawManager.lock();
    Globals.curEditor(this);
    mode(Globals.mode());
    setUnderMouse(me);
    _modeManager.mouseEntered(me);
    //- RedrawManager.unlock();
    //- _redrawer.repairDamage();
  }

  /** Invoked when the mouse exits the Editor. */
  public void mouseExited(MouseEvent me) {
    //- RedrawManager.lock();
    // Globals.curEditor(this);
    setUnderMouse(me);
    if (_curFig instanceof MouseListener)
      ((MouseListener)_curFig).mouseExited(me);
    //- RedrawManager.unlock();
    //- _redrawer.repairDamage();
  }

  /** Invoked when a mouse button is pressed in the Editor and then
   *  dragged.  Mouse drag events will continue to be delivered to the
   *  Editor where the first originated until the mouse button is
   *  released (regardless of whether the mouse position is within the
   *  bounds of the Editor).  BTW, this makes drag and drop editing
   *  almost impossible.  */
  public void mouseDragged(MouseEvent me) {
    //- RedrawManager.lock();
    Globals.curEditor(this);
    setUnderMouse(me);
    _selectionManager.mouseDragged(me);
    _modeManager.mouseDragged(me);
    //- RedrawManager.unlock();
    //- _redrawer.repairDamage();
  }

  /** Invoked when the mouse button has been moved (with no buttons no down). */
  public void mouseMoved(MouseEvent me) {
    //- RedrawManager.lock();
    Globals.curEditor(this);
    setUnderMouse(me);
    if (_curFig != null && Globals.getShowFigTips()) {
      String tip = _curFig.getTipString(me);
      if (tip != null && tip.length() > 0 && !tip.endsWith(" ")) tip += " ";
      if (tip != null && (_awt_component instanceof JComponent)) {
 	((JComponent)_awt_component).setToolTipText(tip);
      }
    }
    else if (_awt_component instanceof JComponent)
      ((JComponent)_awt_component).setToolTipText(null); //was ""

    _selectionManager.mouseMoved(me);
    _modeManager.mouseMoved(me);
    //- RedrawManager.unlock();
    //- _redrawer.repairDamage();
  }


  /** Invoked when a key has been pressed and released. The KeyEvent
   *  has its keyChar ivar set to something, keyCode ivar is junk. */
  public void keyTyped(KeyEvent ke) {
    //- RedrawManager.lock();
    Globals.curEditor(this);
    _selectionManager.keyTyped(ke);
    _modeManager.keyTyped(ke);
    //- RedrawManager.unlock();
    //- _redrawer.repairDamage();
  }

  /** Invoked when a key has been pressed. The KeyEvent
   *  has its keyCode ivar set to something, keyChar ivar is junk. */
  public void keyPressed(KeyEvent ke) {
    //- RedrawManager.lock();
    Globals.curEditor(this);
    _selectionManager.keyPressed(ke);
    _modeManager.keyPressed(ke);
    //- RedrawManager.unlock();
    //- _redrawer.repairDamage();
  }

  /** Invoked when a key has been released. This is not very useful,
   *  so I ignore it. */
  public void keyReleased(KeyEvent ke) { }



  ////////////////////////////////////////////////////////////////
  // Command-related methods

  /** The editor acts as a shell for Cmds.  This method executes the
   *  given Cmd in response to the given event (some Cmds look
   *  at the Event that invoke them, even though this is
   *  discouraged). The Editor executes the Cmd in a safe
   *  environment so that buggy actions cannot crash the whole
   *  Editor.  */
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

  /** The given Fig was removed from the diagram this Editor is
   *  showing. Now update the display. */ 
  public void removed(Fig f) {
    _selectionManager.deselect(f);
    remove(f);
  }

  static final long serialVersionUID = -3313673451324513650L;

} /* end class Editor */
