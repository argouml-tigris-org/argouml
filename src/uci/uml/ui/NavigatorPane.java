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


package uci.uml.ui;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
//import com.sun.java.swing.border.*;

import uci.ui.ToolBar;
import uci.util.*;

 
public class NavigatorPane extends JPanel
implements ItemListener, TreeSelectionListener {
  ////////////////////////////////////////////////////////////////
  // instance variables

  // vector of TreeModels
  protected Vector _perspectives = new Vector(); 

  protected ToolBar _toolbar = new ToolBar();
  protected JComboBox _combo = new JComboBox();
  protected Object _root = null;
  protected NavPerspective _curPerspective = null;
  protected JTree _tree = new DisplayTextTree();
  protected Action _navBack = Actions.NavBack;
  protected Action _navForw = Actions.NavForw;
  protected Action _navConfig = Actions.NavConfig;
  
  ////////////////////////////////////////////////////////////////
  // constructors

  public NavigatorPane() {
    setLayout(new BorderLayout());
    //_toolbar.add(new JLabel("Perspective "));
    _toolbar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    _toolbar.add(_combo);
    _toolbar.add(_navBack);
    _toolbar.add(_navForw);
    _toolbar.add(_navConfig);
    add(_toolbar, BorderLayout.NORTH);
    add(new JScrollPane(_tree), BorderLayout.CENTER);
    _combo.addItemListener(this);
    _tree.setRootVisible(false);
    _tree.setShowsRootHandles(true);
    _tree.addTreeSelectionListener(this);
    _tree.addMouseListener(new NavigatorMouseListener());
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setRoot(Object r) {
    _root = r;
    if (_curPerspective != null) {
      _curPerspective.setRoot(_root);
      _tree.setModel(_curPerspective); //forces a redraw
    }
    
  }
  public Object getRoot() { return _root; }

  public Vector getPerspectives() { return _perspectives; }
  public void setPerspectives(Vector pers) {
    _perspectives = pers;
    if (pers.isEmpty()) _curPerspective = null;
    else setCurPerspective((NavPerspective) pers.elementAt(0));
    
    _combo.removeAllItems();
    java.util.Enumeration persEnum = _perspectives.elements();
    while (persEnum.hasMoreElements()) 
      _combo.addItem(persEnum.nextElement());
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

  public Dimension getPreferredSize() { return new Dimension(200, 100); }
  public Dimension getMinimumSize() { return new Dimension(100, 100); }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** called when the user selects a perspective from the perspective
   *  combo. */
  public void itemStateChanged(ItemEvent e) {
    updateTree();
  }

  /** called when the user selects an item in the tree, by clicking or
   *  otherwise. */
  public void valueChanged(TreeSelectionEvent e) {
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    //ProjectBrowser.TheInstance.setTarget(getSelectedObject());
  }


  // needs-more-work: what should the difference be between a single
  // and double click?

  /** called when the user clicks once on an item in the tree. */ 
  public void mySingleClick(int row, TreePath path) {
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    if (getSelectedObject() == null) return;
    System.out.println("single: " + getSelectedObject().toString());
    ProjectBrowser.TheInstance.select(getSelectedObject());
  }

  /** called when the user clicks twice on an item in the tree. */ 
  public void myDoubleClick(int row, TreePath path) {
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    if (getSelectedObject() == null) return;
    System.out.println("double: " + getSelectedObject().toString());
    ProjectBrowser.TheInstance.setTarget(getSelectedObject());
  }

  ////////////////////////////////////////////////////////////////
  // internal methods

  protected void updateTree() {
    System.out.println("updateTree!");
    NavPerspective tm = (NavPerspective) _combo.getSelectedItem();
    //if (tm == _curPerspective) return;
      _curPerspective = tm;
      if (_curPerspective == null) {
	System.out.println("null perspective!");
	_tree.hide();
      }
      else {
      System.out.println("updateTree _curPerspective=" + _curPerspective.toString());
	_curPerspective.setRoot(_root);
	_tree.setModel(_curPerspective);
	_tree.show(); // blinks?
      }

  }


  ////////////////////////////////////////////////////////////////
  // inner classes

  class NavigatorMouseListener extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      int row = _tree.getRowForLocation(e.getX(), e.getY());
      TreePath path = _tree.getPathForLocation(e.getX(), e.getY());
      if (row != -1) {
	if (e.getClickCount() == 1) mySingleClick(row, path);
	  else if (e.getClickCount() >= 2) myDoubleClick(row, path);
      }
    }
  }

  
} /* end class NavigatorPane */
