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



package uci.gef;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import uci.ui.*;
import uci.graph.*;
import uci.gef.event.*;

/** JGraph is a Swing component that displays a connected graph and
 *  allows interactive editing. In many ways this class serves as a
 *  simple front-end to class Editor, and other classes which do the
 *  real work. */

public class JGraph extends JPanel implements Cloneable {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The Editor object that is being shown in this panel */
  protected Editor _editor;
  protected JGraphInternalPane _drawingPane;
  protected JScrollPane _scroll;

  ////////////////////////////////////////////////////////////////
  // constructor

  /** Make a new JGraph with a new DefaultGraphModel.
   * @see uci.graph.DefaultGraphModel */
  public JGraph() { this(new DefaultGraphModel()); }

  /** Make a new JGraph with a the GraphModel and Layer from the given
   *  Diagram. */
  public JGraph(Diagram d) { this(new Editor(d)); }

  /** Make a new JGraph with the given GraphModel */
  public JGraph(GraphModel gm) { this(new Editor(gm, null)); }

  /** Make a new JGraph with the given Editor.  All JGraph contructors
   *  eventually call this contructor. */
  public JGraph(Editor ed) {
    super(false); // not double buffered. I do my own flicker-free redraw.
    _editor = ed;
    _drawingPane = new JGraphInternalPane(_editor);
    setDrawingSize(1500, 1500);

    _scroll = new JScrollPane(_drawingPane);
    _scroll.setBorder(null);
    _scroll.getHorizontalScrollBar().setUnitIncrement(25);
    _scroll.getVerticalScrollBar().setUnitIncrement(25);

    _editor.setAwtComponent(_drawingPane);
    setLayout(new BorderLayout());
    add(_scroll, BorderLayout.CENTER);
    addMouseListener(_editor);
    addMouseMotionListener(_editor);
    addKeyListener(_editor);

    initKeys();

    //invalidate();
    validate();
    //revalidate();
  }

  public void addMouseListener(MouseListener listener) {
    _drawingPane.addMouseListener(listener);
  }

  public void addMouseMotionListener(MouseMotionListener listener) {
    _drawingPane.addMouseMotionListener(listener);
  }

  public void addKeyListener(KeyListener listener) {
    _drawingPane.addKeyListener(listener);
  }

  /** Make a copy of this JGraph so that it can be shown in another window.*/
  public Object clone() {
    JGraph newJGraph = new JGraph((Editor) _editor.clone());
    return newJGraph;
  }

  /* Set up some standard keystrokes and the Cmds that they invoke. */
  public void initKeys() {
    int shift = KeyEvent.SHIFT_MASK;
    int ctrl = KeyEvent.CTRL_MASK;
    int ctrlShift = KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK;
    int alt = KeyEvent.ALT_MASK;
    int meta = KeyEvent.META_MASK;

    bindKey(new CmdSelectNext(true), KeyEvent.VK_TAB, 0);
    bindKey(new CmdSelectNext(false), KeyEvent.VK_TAB, shift);

    //bindKey(new CmdDelete(), KeyEvent.VK_DELETE, 0);
    //bindKey(new CmdDispose(), KeyEvent.VK_D, ctrl);

    bindKey(new CmdGroup(), KeyEvent.VK_G, ctrl);
    bindKey(new CmdUngroup(), KeyEvent.VK_U, ctrl);

    bindKey(new CmdReorder(CmdReorder.SEND_BACKWARD), KeyEvent.VK_B, ctrl);
    bindKey(new CmdReorder(CmdReorder.BRING_FORWARD), KeyEvent.VK_F, ctrl);
    bindKey(new CmdReorder(CmdReorder.SEND_TO_BACK), KeyEvent.VK_B, ctrlShift);
    bindKey(new CmdReorder(CmdReorder.BRING_TO_FRONT), KeyEvent.VK_F, ctrlShift);

    bindKey(new CmdNudge(CmdNudge.LEFT), KeyEvent.VK_LEFT, 0);
    bindKey(new CmdNudge(CmdNudge.RIGHT), KeyEvent.VK_RIGHT, 0);
    bindKey(new CmdNudge(CmdNudge.UP), KeyEvent.VK_UP, 0);
    bindKey(new CmdNudge(CmdNudge.DOWN), KeyEvent.VK_DOWN, 0);

    bindKey(new CmdNudge(CmdNudge.LEFT, 8), KeyEvent.VK_LEFT, shift);
    bindKey(new CmdNudge(CmdNudge.RIGHT, 8), KeyEvent.VK_RIGHT, shift);
    bindKey(new CmdNudge(CmdNudge.UP, 8), KeyEvent.VK_UP, shift);
    bindKey(new CmdNudge(CmdNudge.DOWN, 8), KeyEvent.VK_DOWN, shift);

    bindKey(new CmdNudge(CmdNudge.LEFT, 18), KeyEvent.VK_LEFT, alt);
    bindKey(new CmdNudge(CmdNudge.RIGHT, 18), KeyEvent.VK_RIGHT, alt);
    bindKey(new CmdNudge(CmdNudge.UP, 18), KeyEvent.VK_UP, alt);
    bindKey(new CmdNudge(CmdNudge.DOWN, 18), KeyEvent.VK_DOWN, alt);

    bindKey(new CmdSelectNear(CmdSelectNear.LEFT), KeyEvent.VK_LEFT, meta);
    bindKey(new CmdSelectNear(CmdSelectNear.RIGHT), KeyEvent.VK_RIGHT, meta);
    bindKey(new CmdSelectNear(CmdSelectNear.UP), KeyEvent.VK_UP, meta);
    bindKey(new CmdSelectNear(CmdSelectNear.DOWN), KeyEvent.VK_DOWN, meta);

  }

  /** Utility function to bind a keystroke to a Swing Action.  Note
   *  that GEF Cmds are subclasses of Swing's Actions. */
  public void bindKey(ActionListener action, int keyCode, int modifiers) {
    _drawingPane.registerKeyboardAction(action,
					KeyStroke.getKeyStroke(keyCode, modifiers),
					WHEN_FOCUSED);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Get the Editor that is being displayed */
  public Editor getEditor() { return _editor; }

  /** Set the Diagram that should be displayed by setting the
   *  GraphModel and Layer that the Editor is using. */
  public void setDiagram(Diagram d) {
    if (d == null) return;
    _editor.getLayerManager().replaceActiveLayer(d.getLayer());
    _editor.setGraphModel(d.getGraphModel());
    _editor.getSelectionManager().deselectAll();
    _editor.damageAll();
  }

  public void setDrawingSize(int width, int height) {
    _drawingPane.setPreferredSize(new Dimension(width, height));
  }

  /** Get and set the GraphModel the Editor is using. */
  public void setGraphModel(GraphModel gm) { _editor.setGraphModel(gm); }
  public GraphModel getGraphModel() { return _editor.getGraphModel(); }

  /** Get and set the Renderer used to make FigNodes for nodes in the
   *  GraphModel. */ 
  public void setGraphNodeRenderer(GraphNodeRenderer r) {
    _editor.setGraphNodeRenderer(r);
  }
  public GraphNodeRenderer getGraphNodeRenderer() {
    return _editor.getGraphNodeRenderer();
  }

  /** Get and set the Renderer used to make FigEdges for edges in the
   *  GraphModel. */ 
  public void setGraphEdgeRenderer(GraphEdgeRenderer r) {
    _editor.setGraphEdgeRenderer(r); }
  public GraphEdgeRenderer getGraphEdgeRenderer() {
    return _editor.getGraphEdgeRenderer();
  }

  /** When the JGraph is hidden, hide its internal pane */
  public void setVisible(boolean b) {
    super.setVisible(b);
    _drawingPane.setVisible(b);
    _editor.setActiveTextEditor(null);
  }

  /** Tell Swing/AWT that JGraph handles tab-order itself. */
  public boolean isManagingFocus() { return true; }

  /** Tell Swing/AWT that JGraph can be tabbed into. */
  public boolean isFocusTraversable() { return true; }


  ////////////////////////////////////////////////////////////////
  // events

  /** Add listener to the objects to notify whenever the Editor
   *  changes its current selection. */
  public void addGraphSelectionListener(GraphSelectionListener listener) {
    getEditor().addGraphSelectionListener(listener);
  }

  public void removeGraphSelectionListener(GraphSelectionListener listener) {
    getEditor().removeGraphSelectionListener(listener);
  }

  public void addModeChangeListener(ModeChangeListener listener) {
    getEditor().addModeChangeListener(listener);
  }

  public void removeModeChangeListener(ModeChangeListener listener) {
    getEditor().removeModeChangeListener(listener);
  }

  ////////////////////////////////////////////////////////////////
  // Editor facade

  public void layoutGraph() {
    // needs-more-work: ask the editor to preform automatic layout
  }

  /** The JGraph is painted by simply painting its Editor. */
  //public void paint(Graphics g) { _editor.paint(getGraphics()); }


  ////////////////////////////////////////////////////////////////
  // selection methods

  /** Add the given item to this Editor's selections. */
  public void select(Fig f) {
    if (f == null) deselectAll();
    else _editor.getSelectionManager().select(f);
  }

  /** Find the Fig that owns the given item and select it. */
  public void selectByOwner(Object owner) {
    Layer lay = _editor.getLayerManager().getActiveLayer();
    if (lay instanceof LayerDiagram)
      select(((LayerDiagram)lay).presentationFor(owner));
  }

  /** Find Fig that owns the given item, or the item if it is a
   *  Fig, and select it. */
  public void selectByOwnerOrFig(Object owner) {
    if (owner instanceof Fig) select((Fig) owner);
    else selectByOwner(owner);
  }

  /** Add the Fig that owns the given item to this Editor's selections. */
  public void selectByOwnerOrNoChange(Object owner) {
    Layer lay = _editor.getLayerManager().getActiveLayer();
    if (lay instanceof LayerDiagram) {
      Fig f = ((LayerDiagram)lay).presentationFor(owner);
      if (f != null) select(f);
    }
  }

  /** Remove the given item from this editors selections.   */
  public void deselect(Fig f) { _editor.getSelectionManager().deselect(f); }

  /** Select the given item if it was not already selected, and
   *  vis-a-versa. */
  public void toggleItem(Fig f) { _editor.getSelectionManager().toggle(f); }

  /** Deslect everything that is currently selected. */
  public void deselectAll() { _editor.getSelectionManager().deselectAll(); }

  /** Select a collection of Figs. */
  public void select(Vector items) {
    _editor.getSelectionManager().select(items);
  }

  /** Toggle the selection of a collection of Figs. */
  public void toggleItems(Vector items) {
    _editor.getSelectionManager().toggle(items);
  }

  /** reply a Vector of all selected Figs. Used in many Cmds.*/
  public Vector selectedFigs() {
    return _editor.getSelectionManager().getFigs();
  }

//   public Dimension getPreferredSize() { return new Dimension(1000, 1000); }

//   public Dimension getMinimumSize() { return new Dimension(1000, 1000); }

//   public Dimension getSize() { return new Dimension(1000, 1000); }

  static final long serialVersionUID = -5459241816919316496L;

} /* end class JGraph */



class JGraphInternalPane extends JPanel {
//implements FocusListener 
  protected Editor _editor;

  public JGraphInternalPane(Editor e) {
    _editor = e;
    setLayout(null);
    setDoubleBuffered(false);
    // setAutoscrolls(true); // needs-more-work: no effect...
    //addFocusListener(this);
  }

  public void paintComponent(Graphics g) { _editor.paint(g); }

//   static int getGraphicsCount = 0;

//   public Graphics getGraphicsCounted() {
//     getGraphicsCount = 0;
//     Graphics g = getGraphics();
//     System.out.println("getGraphics recurred " + getGraphicsCount + "times");
//     return g;
//   }

  public Graphics getGraphics() {
    //getGraphicsCount++;
    Graphics res = super.getGraphics();
    if (res == null) { return res; }
    Component parent = getParent();

    if (parent instanceof JViewport) {
      JViewport view = (JViewport) parent;
      Rectangle bounds = view.getBounds();
      Point pos = view.getViewPosition();
      res.clipRect(bounds.x + pos.x - 1, bounds.y + pos.y - 1,
		   bounds.width + 1, bounds.height + 1);
    }
    return res;
  }

  public void setToolTipText(String text) {
    if ("".equals(text)) text = null;
    putClientProperty(TOOL_TIP_TEXT_KEY, text);
    ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
    //if (text != null) {
    toolTipManager.registerComponent(this);
    //} else {
    //    toolTipManager.unregisterComponent(this);
    //}
  }

  /** Tell Swing/AWT that JGraph handles tab-order itself. */
  public boolean isManagingFocus() { return true; }

  /** Tell Swing/AWT that JGraph can be tabbed into. */
  public boolean isFocusTraversable() { return true; }  

  static final long serialVersionUID = -5067026168452437942L;

} /* end class JGraphInternalPane */

