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

package uci.gef;

import java.awt.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.border.*;

import uci.ui.*;
import uci.graph.*;
import uci.gef.event.*;

/** A window that displays a toolbar, a connected graph editing pane,
 *  and a status bar. */

public class JGraphFrame extends JFrame
implements IStatusBar, Cloneable {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The toolbar (shown at top of window). */
  protected ToolBar _toolbar = new PaletteFig();
  /** The graph pane (shown in middle of window). */
  protected JGraph _graph;
  /** A statusbar (shown at bottom ow window). */
  protected JLabel _statusbar = new JLabel(" ");
  
  protected JPanel _mainPanel = new JPanel(new BorderLayout());
  protected JPanel _graphPanel = new JPanel(new BorderLayout());
  protected JMenuBar _menubar = new JMenuBar();

  ////////////////////////////////////////////////////////////////
  // constructor

  /** Contruct a new JGraphFrame with the title "untitled" and a new
   *  DefaultGraphModel. */
  public JGraphFrame() { this("untitled"); }

  /** Contruct a new JGraphFrame with the title "untitled" and the
   *  given GraphModel. */
  public JGraphFrame(GraphModel gm) {
    this("untitled");
    setGraphModel(gm);
  }
  
  /** Contruct a new JGraphFrame with the given title and a new
   *  DefaultGraphModel. */
  public JGraphFrame(String title) {
    this(title, new JGraph());
  }

  public JGraphFrame(String title, Editor ed) {
    this(title, new JGraph(ed));
  }
  
  /** Contruct a new JGraphFrame with the given title and given
   *  JGraph. All JGraphFrame contructors call this one. */
  public JGraphFrame(String title, JGraph jg) {
    super(title);
    _graph = jg;
    Container content = getContentPane();
    setUpMenus();
    content.setLayout(new BorderLayout());
    content.add(_menubar, BorderLayout.NORTH);    
    _graphPanel.add(_graph, BorderLayout.CENTER);
    _graphPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

    _mainPanel.add(_toolbar, BorderLayout.NORTH);
    _mainPanel.add(_graphPanel, BorderLayout.CENTER);
    content.add(_mainPanel, BorderLayout.CENTER);
    content.add(_statusbar, BorderLayout.SOUTH);
    resize(300, 250);
  }

  /** Set up the menus and keystrokes for menu items. Subclasses can
   *  override this, or you can use setMenuBar(). */
  protected void setUpMenus() {
    JMenuItem openItem, saveItem, printItem, prefsItem, exitItem;
    JMenuItem selectAllItem;
    JMenuItem deleteItem, cutItem, copyItem, pasteItem;
    JMenuItem editNodeItem;
    JMenuItem groupItem, ungroupItem;
    JMenuItem toBackItem, backwardItem, toFrontItem, forwardItem;
    JMenuItem nudgeUpItem, nudgeDownItem, nudgeLeftItem, nudgeRightItem;
    
    JMenu file = new JMenu("File");
    file.setMnemonic('F');
    _menubar.add(file);
    //file.add(new CmdNew());
    openItem = file.add(new CmdOpen());
    saveItem = file.add(new CmdSave());
    printItem = file.add(new CmdPrint());
    prefsItem = file.add(new CmdOpenWindow("uci.gef.PrefsEditor",
					   "Preferences..."));
    //file.add(new CmdClose());
    exitItem = file.add(new CmdExit());

    
    JMenu edit = new JMenu("Edit");
    edit.setMnemonic('E');
    _menubar.add(edit);
    
    JMenu select = new JMenu("Select");
    edit.add(select);
    selectAllItem = select.add(new CmdSelectAll());
    select.add(new CmdSelectNext(false));
    select.add(new CmdSelectNext(true));
    select.add(new CmdSelectInvert());

    //select.add(new CmdSelectSuchThat());

    //edit.add(new CmdUndo());
    //edit.add(new CmdRedo());
    //edit.addSeparator();
    
    //edit.add(new CmdCut());
    //edit.add(new CmdCopy());
    //edit.add(new CmdPaste());
    
    deleteItem = edit.add(new CmdDelete());
    edit.addSeparator();
    edit.add(new CmdUseReshape());
    edit.add(new CmdUseResize());
    edit.add(new CmdUseRotate());
    edit.addSeparator();

    JMenu view = new JMenu("View");
    _menubar.add(view);
    view.setMnemonic('V');
    view.add(new CmdSpawn());
    view.add(new CmdShowProperties());
    //view.addSeparator();
    //view.add(new CmdZoomIn());
    //view.add(new CmdZoomOut());
    //view.add(new CmdZoomNormal());
    view.addSeparator();
    view.add(new CmdAdjustGrid());
    view.add(new CmdAdjustGuide());
    view.add(new CmdAdjustPageBreaks());

    JMenu arrange = new JMenu("Arrange");
    _menubar.add(arrange);
    arrange.setMnemonic('A');
    groupItem = arrange.add(new CmdGroup());
    groupItem.setMnemonic('G');
    ungroupItem = arrange.add(new CmdUngroup());
    ungroupItem.setMnemonic('U');

    JMenu align = new JMenu("Align");
    arrange.add(align);
    align.add(new CmdAlign(CmdAlign.ALIGN_TOPS));
    align.add(new CmdAlign(CmdAlign.ALIGN_BOTTOMS));
    align.add(new CmdAlign(CmdAlign.ALIGN_LEFTS));
    align.add(new CmdAlign(CmdAlign.ALIGN_RIGHTS));
    align.add(new CmdAlign(CmdAlign.ALIGN_H_CENTERS));
    align.add(new CmdAlign(CmdAlign.ALIGN_V_CENTERS));
    align.add(new CmdAlign(CmdAlign.ALIGN_TO_GRID));

    JMenu reorder = new JMenu("Reorder");
    arrange.add(reorder);
    toBackItem = reorder.add(new CmdReorder(CmdReorder.SEND_TO_BACK));
    toFrontItem = reorder.add(new CmdReorder(CmdReorder.BRING_TO_FRONT));
    backwardItem = reorder.add(new CmdReorder(CmdReorder.SEND_BACKWARD));
    forwardItem = reorder.add(new CmdReorder(CmdReorder.BRING_FORWARD));

    JMenu nudge = new JMenu("Nudge");
    arrange.add(nudge);
    nudgeLeftItem = nudge.add(new CmdNudge(CmdNudge.LEFT));
    nudgeRightItem = nudge.add(new CmdNudge(CmdNudge.RIGHT));
    nudgeUpItem = nudge.add(new CmdNudge(CmdNudge.UP));
    nudgeDownItem = nudge.add(new CmdNudge(CmdNudge.DOWN));

    KeyStroke ctrlN = KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK);
    KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK);
    KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK);
    KeyStroke ctrlP = KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK);
    KeyStroke altF4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK);

    KeyStroke leftArrow, rightArrow, upArrow, downArrow;
    leftArrow = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
    rightArrow = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
    upArrow = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
    downArrow = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);

    KeyStroke sLeftArrow, sRightArrow, sUpArrow, sDownArrow;
    sLeftArrow = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.SHIFT_MASK);
    sRightArrow = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.SHIFT_MASK);
    sUpArrow = KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.SHIFT_MASK);
    sDownArrow = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.SHIFT_MASK);

    KeyStroke delKey, ctrlG, ctrlU, ctrlB, ctrlF, sCtrlB, sCtrlF;
    delKey = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
    ctrlG = KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_MASK);
    ctrlU = KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_MASK);
    ctrlB = KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_MASK);
    ctrlF = KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK);
    sCtrlB = KeyStroke.getKeyStroke(KeyEvent.VK_B,
				    KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK);
    sCtrlF = KeyStroke.getKeyStroke(KeyEvent.VK_F,
				    KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK);

    
    //newItem.setAccelerator(ctrlN);
    openItem.setAccelerator(ctrlO);
    saveItem.setAccelerator(ctrlS);
    printItem.setAccelerator(ctrlP);
    exitItem.setAccelerator(altF4);

    deleteItem.setAccelerator(delKey);

    groupItem.setAccelerator(ctrlG);
    ungroupItem.setAccelerator(ctrlU);

    toBackItem.setAccelerator(sCtrlB);
    toFrontItem.setAccelerator(sCtrlF);
    backwardItem.setAccelerator(ctrlB);
    forwardItem.setAccelerator(ctrlF);

  }


  ////////////////////////////////////////////////////////////////
  // accessors

  public JGraph getGraph() { return _graph; }
  public void setGraph(JGraph g) { _graph = g; }

  public void setGraphModel(GraphModel gm) { _graph.setGraphModel(gm); }
  public GraphModel getGraphModel() { return _graph.getGraphModel(); }
  public void setGraphNodeRenderer(GraphNodeRenderer rend) {
    _graph.getEditor().setGraphNodeRenderer(rend);
  }
  public GraphNodeRenderer getGraphNodeRenderer() {
    return _graph.getEditor().getGraphNodeRenderer();
  }
  public void setGraphEdgeRenderer(GraphEdgeRenderer rend) {
    _graph.getEditor().setGraphEdgeRenderer(rend);
  }
  public GraphEdgeRenderer getGraphEdgeRenderer() {
    return _graph.getEditor().getGraphEdgeRenderer();
  }
  
  public void setToolBar(ToolBar tb) {
    _toolbar = tb;
    _mainPanel.add(_toolbar, BorderLayout.NORTH);    
  }
  public ToolBar getToolBar() { return _toolbar; }

  public void setJMenuBar(JMenuBar mb) {
    _menubar = mb;
    getContentPane().add(_menubar, BorderLayout.NORTH);    
  }
  public JMenuBar getJMenuBar() { return _menubar; }

  
  ////////////////////////////////////////////////////////////////
  // display related methods

  public void show() { super.show(); Globals.setStatusBar(this); }
  
  ////////////////////////////////////////////////////////////////
  // Cloneable implementation

  public Object clone() {
    return null; //needs-more-work
  }

  ////////////////////////////////////////////////////////////////
  // IStatusListener implementation

  /** Show a message in the statusbar. */
  public void showStatus(String msg) {
    if (_statusbar != null) _statusbar.setText(msg);
  }

  
} /* end class JGraphFrame */
