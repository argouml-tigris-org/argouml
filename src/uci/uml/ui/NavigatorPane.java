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

package uci.uml.ui;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
//import javax.swing.border.*;

import uci.ui.*;
import uci.util.*;
import uci.gef.Diagram;
import uci.uml.ui.nav.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Behavioral_Elements.Use_Cases.*;

/** The upper-left pane of the main Argo/UML window.  This shows the
 *  contents of the current project in one of several ways that are
 *  determined by NavPerspectives. */


public class NavigatorPane extends JPanel
implements ItemListener, TreeSelectionListener {
  //, CellEditorListener

  ////////////////////////////////////////////////////////////////
  // constants
  public static int MAX_HISTORY = 10;

  ////////////////////////////////////////////////////////////////
  // instance variables

  // vector of TreeModels
  protected Vector _perspectives = new Vector();

  protected ToolBar _toolbar = new ToolBar();
  protected JComboBox _combo = new JComboBox();
  protected Object _root = null;
  protected Vector _navHistory = new Vector();
  protected int _historyIndex = 0;
  protected NavPerspective _curPerspective = null;
  protected DisplayTextTree _tree = new DisplayTextTree();
//   protected Action _navBack = Actions.NavBack;
//   protected Action _navForw = Actions.NavForw;
//   protected Action _navConfig = Actions.NavConfig;

  public static int _clicksInNavPane = 0;
  public static int _navPerspectivesChanged = 0;

  ////////////////////////////////////////////////////////////////
  // constructors

  public NavigatorPane() {
    setLayout(new BorderLayout());
    //_toolbar.add(new JLabel("Perspective "));
    _toolbar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    _toolbar.add(_combo);
    _toolbar.add(Actions.NavBack);
    _toolbar.add(Actions.NavForw);
    _toolbar.add(Actions.NavConfig);
    add(_toolbar, BorderLayout.NORTH);
    add(new JScrollPane(_tree), BorderLayout.CENTER);
    _combo.addItemListener(this);
    _tree.setRootVisible(false);
    _tree.setShowsRootHandles(true);
    _tree.addTreeSelectionListener(this);
    _tree.addMouseListener(new NavigatorMouseListener());
    _tree.addKeyListener(new NavigatorKeyListener());
    //_tree.addActionListener(new NavigatorActionListener());
    //_tree.setEditable(true);
    //_tree.getCellEditor().addCellEditorListener(this);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setRoot(Object r) {
    _root = r;
    if (_curPerspective != null) {
      _curPerspective.setRoot(_root);
      _tree.setModel(_curPerspective); //forces a redraw
    }
    clearHistory();
  }
  public Object getRoot() { return _root; }

  public Vector getPerspectives() { return _perspectives; }
  public void setPerspectives(Vector pers) {
    _perspectives = pers;
    NavPerspective oldCurPers = _curPerspective;
    if(_combo.getItemCount() > 0) _combo.removeAllItems();
    java.util.Enumeration persEnum = _perspectives.elements();
    while (persEnum.hasMoreElements())
      _combo.addItem(persEnum.nextElement());

    if (_perspectives == null || _perspectives.isEmpty())
      _curPerspective = null;
    else if (oldCurPers != null && _perspectives.contains(oldCurPers))
      setCurPerspective(oldCurPers);
    else
      setCurPerspective((NavPerspective) _perspectives.elementAt(0));
    updateTree();
  }

  public NavPerspective getCurPerspective() { return _curPerspective; }
  public void setCurPerspective(NavPerspective per) {
    _curPerspective = per;
    if (_perspectives == null || !_perspectives.contains(per)) return;
    _combo.setSelectedItem(_curPerspective);
  }

  public Object getSelectedObject() {
    return _tree.getLastSelectedPathComponent();
  }

  public void forceUpdate() { _tree.forceUpdate(); }

  /** This is pretty limited, it is really only useful for selecting
   *  the default diagram when the user does New.  A general function
   *  to select a given object would have to find the shortest path to
   *  it. */
  public void setSelection(Object level1, Object level2) {
    Object objs[] = new Object[3];
    objs[0] = _root;
    objs[1] = level1;
    objs[2] = level2;
    TreePath path = new TreePath(objs);
    _tree.setSelectionPath(path);
  }

  public Dimension getPreferredSize() { return new Dimension(220, 500); }
  public Dimension getMinimumSize() { return new Dimension(120, 100); }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** called when the user selects a perspective from the perspective
   *  combo. */
  public void itemStateChanged(ItemEvent e) {
    updateTree();
    _navPerspectivesChanged++;
  }

  /** called when the user selects an item in the tree, by clicking or
   *  otherwise. */
  public void valueChanged(TreeSelectionEvent e) {
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    //ProjectBrowser.TheInstance.setTarget(getSelectedObject());
    //ProjectBrowser.TheInstance.setTarget(getSelectedObject());
  }


  /** called when the user clicks once on an item in the tree. */ 
  public void mySingleClick(int row, TreePath path) {
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    Object sel = getSelectedObject();
    if (sel  == null) return;
    if (sel instanceof Diagram) {
      myDoubleClick(row, path);
      return;
    }
    ProjectBrowser.TheInstance.select(sel);
    _clicksInNavPane++;
  }

  /** called when the user clicks twice on an item in the tree. */ 
  public void myDoubleClick(int row, TreePath path) {
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    Object sel = getSelectedObject();
    if (sel == null) return;
    addToHistory(sel);
    ProjectBrowser.TheInstance.setTarget(sel);
    repaint();
  }

  public void navDown() {
    int row = _tree.getMinSelectionRow();
    if (row == _tree.getRowCount()) row = 0;
    else row = row + 1;
    _tree.setSelectionRow(row);
    ProjectBrowser.TheInstance.select(getSelectedObject());
  }

  public void navUp() {
    int row = _tree.getMinSelectionRow();
    if (row == 0) row = _tree.getRowCount();
    else row = row - 1;
    _tree.setSelectionRow(row);
    ProjectBrowser.TheInstance.select(getSelectedObject());
  }

  public void clearHistory() {
    _historyIndex = 0;
    _navHistory.removeAllElements();
  }

  public void addToHistory(Object sel) {
    if (_navHistory.size() == 0)
      _navHistory.addElement(ProjectBrowser.TheInstance.getTarget());
    while (_navHistory.size() -1 > _historyIndex) {
      _navHistory.removeElementAt(_navHistory.size() - 1);
    }
    if (_navHistory.size() > MAX_HISTORY) {
      _navHistory.removeElementAt(0);
    }
    _navHistory.addElement(sel);
    _historyIndex = _navHistory.size() - 1;
  }
  public boolean canNavBack() {
    return _navHistory.size() > 0 && _historyIndex > 0;
  }
  public void navBack() {
    _historyIndex = Math.max(0, _historyIndex - 1);
    if (_navHistory.size() <= _historyIndex) return;
    Object oldTarget = _navHistory.elementAt(_historyIndex);
    ProjectBrowser.TheInstance.setTarget(oldTarget);
  }

  public boolean canNavForw() {
    return _historyIndex < _navHistory.size() - 1;
  }
  public void navForw() {
    _historyIndex = Math.min(_navHistory.size() - 1, _historyIndex + 1);
    Object oldTarget = _navHistory.elementAt(_historyIndex);
    ProjectBrowser.TheInstance.setTarget(oldTarget);
  }

  ////////////////////////////////////////////////////////////////
  // internal methods

  protected void updateTree() {
    NavPerspective tm = (NavPerspective) _combo.getSelectedItem();
    //if (tm == _curPerspective) return;
    _curPerspective = tm;
    if (_curPerspective == null) {
	//System.out.println("null perspective!");
      _tree.setVisible(false);
    }
    else {
      _curPerspective.setRoot(_root);
      _tree.setModel(_curPerspective);
      _tree.setVisible(true); // blinks?
    }
  }


  ////////////////////////////////////////////////////////////////
  // inner classes

  class NavigatorMouseListener extends MouseAdapter {
    public void mouseClicked(MouseEvent me) {
      //if (me.isConsumed()) return;
      int row = _tree.getRowForLocation(me.getX(), me.getY());
      TreePath path = _tree.getPathForLocation(me.getX(), me.getY());
      if (row != -1) {
	if (me.getClickCount() == 1) mySingleClick(row, path);
	else if (me.getClickCount() >= 2) myDoubleClick(row, path);
	//me.consume();

      }
    }
    public void mousePressed(MouseEvent me) {
      Vector tailActions = new Vector();
      if (me.isPopupTrigger() ||
	  me.getModifiers() == InputEvent.BUTTON3_MASK) {
        //TreeCellEditor tce = _tree.getCellEditor();
        JPopupMenu popup = new JPopupMenu("test");
	Object obj = getSelectedObject();
	if (obj instanceof PopupGenerator) {
	  Vector actions = ((PopupGenerator)obj).getPopUpActions(me);
	  int size = actions.size();
	  for (int i = 0; i < size; ++i) {
	    AbstractAction a = (AbstractAction) actions.elementAt(i);
	    popup.add(a);
	  }
	}
	else if (obj instanceof MMClass) {
	  popup.add(new ActionGoToDetails("Properties"));
	  // goto state diagram
	  // goto collaboration diagram(s)
	  tailActions.addElement(Actions.RemoveFromModel);
	}
	else if (obj instanceof UseCase) {
	  popup.add(new ActionGoToDetails("Properties"));
	  // goto activity diagram
	  tailActions.addElement(Actions.RemoveFromModel);
	}
	else if (obj instanceof ModelElement || obj instanceof Diagram) {
	  popup.add(new ActionGoToDetails("Properties")); //was CmdUMLProperties
	  tailActions.addElement(Actions.RemoveFromModel);
	}
	int size = tailActions.size();
	for (int i = 0; i < size; ++i) {
	  AbstractAction a = (AbstractAction) tailActions.elementAt(i);
	  popup.add(a);
	}
	popup.show(NavigatorPane.this, me.getX(), me.getY()+25);
        me.consume();
      }
    }
  } /* end class NavigatorMouseListener */

  class NavigatorKeyListener extends KeyAdapter {
    // maybe use keyTyped?
    public void keyPressed(KeyEvent e) {
      //System.out.println("got key: " + e.getKeyCode());
      int code = e.getKeyCode();
      if (code == KeyEvent.VK_ENTER || code  == KeyEvent.VK_SPACE) {
	Object newTarget = getSelectedObject();
	if (newTarget != null)
	  ProjectBrowser.TheInstance.select(newTarget);
      }
    }
  }


} /* end class NavigatorPane */
