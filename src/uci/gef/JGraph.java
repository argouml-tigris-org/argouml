// copyright

package uci.gef;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;

import uci.ui.*;
import uci.graph.*;
import uci.gef.event.*;

public class JGraph extends JPanel implements Cloneable {
  //implements MouseListener, MouseMotionListener, KeyListener {

  ////////////////////////////////////////////////////////////////
  // instance variables
  protected Editor _editor;

  
  ////////////////////////////////////////////////////////////////
  // constructor

  public JGraph() { this(new DefaultGraphModel()); }
  
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

//     setLayout(new BorderLayout());
//     add(_graphPanel, BorderLayout.CENTER);
//     add(_toolbar, BorderLayout.NORTH);

    _editor.setAwtComponent(this);
    addMouseListener(_editor);
    addMouseMotionListener(_editor);
    addKeyListener(_editor);
    setUpKeys();
  }

  public Object clone() {
    JGraph newJGraph = new JGraph((Editor) _editor.clone());
    return newJGraph;
  }

  public void setUpKeys() {
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

  public void setGraphModel(GraphModel gm) { _editor.setGraphModel(gm); }
  public GraphModel getGraphModel() { return _editor.getGraphModel(); }
  
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
  public void select(Fig f) { _editor.getSelectionManager().select(f); }

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
