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

// copyright

package uci.gef;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;

import uci.ui.*;
import uci.graph.*;
import uci.gef.event.*;

public class JGraph extends JPanel implements Cloneable {
  //implements MouseListener, MouseMotionListener, KeyListener {

  ////////////////////////////////////////////////////////////////
  // instance variables
  protected Editor _editor;
  
//  protected JPanel _graphPanel = new JGraphInternalPane();  
//   protected  ToolBar _toolbar = new PaletteFig();
  
  ////////////////////////////////////////////////////////////////
  // constructor

  public JGraph() { this(new DefaultGraphModel()); }

  public JGraph(Diagram d) { this(new Editor(d)); }

  public JGraph(GraphModel gm) { this(new Editor(gm, null)); }
//     super(false); // not double buffered
//     _editor = new Editor(gm, this);
//     addMouseListener(_editor);
//     addMouseMotionListener(_editor);
//     addKeyListener(_editor);
//   }

  public JGraph(Editor ed) {
    super(false); // not double buffered
    _editor = ed;

//     _graphPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
//     setLayout(new BorderLayout());
//     _graphPanel.setPreferredSize(new Dimension(500, 500));
//     add(new JScrollPane(_graphPanel), BorderLayout.CENTER);
//     add(_toolbar, BorderLayout.NORTH);
//     _editor.setAwtComponent(_graphPanel);
//     _graphPanel.addMouseListener(_editor);
//     _graphPanel.addMouseMotionListener(_editor);
//     _graphPanel.addKeyListener(_editor);

    _editor.setAwtComponent(this);
    addMouseListener(_editor);
    addMouseMotionListener(_editor);
    addKeyListener(_editor);

    initKeys();
  }

  public Object clone() {
    JGraph newJGraph = new JGraph((Editor) _editor.clone());
    return newJGraph;
  }

  public void initKeys() {
    int shift = KeyEvent.SHIFT_MASK;
    int ctrl = KeyEvent.CTRL_MASK;
    int ctrlShift = KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK;
    int alt = KeyEvent.ALT_MASK;
    
    bindKey(new CmdSelectNext(true), KeyEvent.VK_TAB, 0);
    bindKey(new CmdSelectNext(false), KeyEvent.VK_TAB, shift);

    bindKey(new CmdDelete(), KeyEvent.VK_DELETE, 0);

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

  }

  public void bindKey(ActionListener action, int keyCode, int modifiers) {
    registerKeyboardAction(action,
			   KeyStroke.getKeyStroke(keyCode, modifiers),
			   WHEN_FOCUSED);
  }
  
  ////////////////////////////////////////////////////////////////
  // accessors

  public Editor getEditor() { return _editor; }

  public void setDiagram(Diagram d) {
    if (d == null) return;
    _editor.getLayerManager().replaceActiveLayer(d.getLayer());
    _editor.getSelectionManager().deselectAll();
    _editor.damageAll();
  }

  public void setGraphModel(GraphModel gm) { _editor.setGraphModel(gm); }
  public GraphModel getGraphModel() { return _editor.getGraphModel(); }

  public void setGraphNodeRenderer(GraphNodeRenderer r) {
    _editor.setGraphNodeRenderer(r); }
  public GraphNodeRenderer getGraphNodeRenderer() {
    return _editor.getGraphNodeRenderer();
  }

  public void setGraphEdgeRenderer(GraphEdgeRenderer r) {
    _editor.setGraphEdgeRenderer(r); }
  public GraphEdgeRenderer getGraphEdgeRenderer() {
    return _editor.getGraphEdgeRenderer();
  }

  public boolean isManagingFocus() { return true; }


  public boolean isFocusTraversable() { return true; }
  
//   public ToolBar getToolBar() { return _toolbar; }
//   public void setToolBar(ToolBar tb) {
//     _toolbar = tb;
//     if (_toolbar != null) add(_toolbar, BorderLayout.NORTH);
//   }


  
  ////////////////////////////////////////////////////////////////
  // events

  public void addGraphSelectionListener(GraphSelectionListener listener) {
    getEditor().addGraphSelectionListener(listener);
  }

  public void removeGraphSelectionListener(GraphSelectionListener listener) {
    getEditor().removeGraphSelectionListener(listener);
  }

  
  ////////////////////////////////////////////////////////////////
  // Editor facade

  public void layoutGraph() {
    // needs-more-work: ask the editor to preform an Action
  }

  public void paint(Graphics g) {
    _editor.paint(g);
  }


  ////////////////////////////////////////////////////////////////
  // selection methods

  /** Add the given item to this editors selections.
   *  <A HREF="../features.html#selections">
   *  <TT>FEATURE: selections</TT></A>
   */
  public void select(Fig f) {
    if (f == null) deselectAll();
    else _editor.getSelectionManager().select(f);
  }

  public void selectByOwner(Object owner) {
    Layer lay = _editor.getLayerManager().getActiveLayer();
    if (lay instanceof LayerDiagram)
      select(((LayerDiagram)lay).presentationFor(owner));
  }

  /** Remove the given item from this editors selections.
   *  <A HREF="../features.html#selections">
   *  <TT>FEATURE: selections</TT></A>
   */
  public void deselect(Fig f) { _editor.getSelectionManager().deselect(f); }

  /** Select the given item if it was not already selected, and
   *  vis-a-versa.
   *  <A HREF="../features.html#selections">
   *  <TT>FEATURE: selections</TT></A>
   */
  public void toggleItem(Fig f) { _editor.getSelectionManager().toggle(f); }

  /** Deslect everything that is currently selected.
   *  <A HREF="../features.html#selections">
   *  <TT>FEATURE: selections</TT></A>
   */
  public void deselectAll() { _editor.getSelectionManager().deselectAll(); }

  /** Select a collection of Fig's.
   *  <A HREF="../features.html#selections">
   *  <TT>FEATURE: selections</TT></A>
   */
  public void select(Vector items) {
    _editor.getSelectionManager().select(items);
  }

  /** Toggle the selection of a collection of Fig's.
   *  <A HREF="../features.html#selections">
   *  <TT>FEATURE: selections</TT></A>
   */
  public void toggleItems(Vector items) {
    _editor.getSelectionManager().toggle(items);
  }
  

  /** <A HREF="../features.html#selections">
   *  <TT>FEATURE: selections</TT></A>
   */
  public Vector selectedFigs() {
    return _editor.getSelectionManager().getFigs();
  }

} /* end class JGraph */


// class JGraphInternalPane extends JPanel {
//   public Dimension getPreferredSize() { return new Dimension(300, 400); }
//   public Dimension getMinimumSize() { return new Dimension(300, 400); }
//   public Dimension getSize() { return new Dimension(300, 400); }
// }
