package uci.uml.ui;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
//import com.sun.java.swing.border.*;

import uci.util.*;
import uci.argo.kernel.*;


public class ToDoPane extends JPanel
implements ItemListener, TreeSelectionListener, MouseListener {
  ////////////////////////////////////////////////////////////////
  // constants

  public static int WIDTH = 690;
  public static int HEIGHT = 520;
  public static int INITIAL_WIDTH = 400;
  public static int INITIAL_HEIGHT = 200;

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected ProjectBrowser _pb = null;

  // vector of TreeModels
  protected Vector _perspectives = new Vector();

  protected JComboBox _combo = new JComboBox();
  protected ToDoList _root = null;
  protected ToDoPerspective _curPerspective = null;
  protected JTree _tree = new JTree();

  ////////////////////////////////////////////////////////////////
  // constructors

  public ToDoPane() {
    setLayout(new BorderLayout());
    add(_combo, BorderLayout.NORTH);
    add(new JScrollPane(_tree), BorderLayout.CENTER);
    _combo.addItemListener(this);
    
    _tree.setRootVisible(false);
    _tree.addTreeSelectionListener(this);

    _tree.addMouseListener(this);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setRoot(ToDoList r) { _root = r; }
  public ToDoList getRoot() { return _root; }

  public Vector getPerspectives() { return _perspectives; }
  public void setPerspectives(Vector pers) {
    _perspectives = pers;
    if (pers.isEmpty()) _curPerspective = null;
    else _curPerspective = (ToDoPerspective) pers.elementAt(0);
    _combo.removeAllItems();
    Enumeration persEnum = _perspectives.elements();
    while (persEnum.hasMoreElements()) 
      _combo.addItem(persEnum.nextElement());
    updateTree();
  }

  public ToDoPerspective getCurPerspective() { return _curPerspective; }
  public void setCurPerspective(TreeModel per) {
    if (_perspectives == null || !_perspectives.contains(per)) return;
    _combo.setSelectedItem(per);
  }

  public Object getSelectedObject() {
    return _tree.getLastSelectedPathComponent();
  }

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
    System.out.println("ToDoPane valueChanged");
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    ProjectBrowser.TheInstance.setToDoItem(getSelectedObject());
  }


  public void mousePressed(MouseEvent e) { }
  public void mouseReleased(MouseEvent e) { }
  public void mouseEntered(MouseEvent e) { }
  public void mouseExited(MouseEvent e) { }

  public void mouseClicked(MouseEvent e) {
    int row = _tree.getRowForLocation(e.getX(), e.getY());
    TreePath path = _tree.getPathForLocation(e.getX(), e.getY());
    if (row != -1) {
      if (e.getClickCount() == 1) mySingleClick(row, path);
      else if (e.getClickCount() >= 2) myDoubleClick(row, path);
    }
  }



  // needs-more-work: what should the difference be between a single
  // and double click?

  /** called when the user clicks once on an item in the tree. */ 
  public void mySingleClick(int row, TreePath path) {
    if (getSelectedObject() == null) return;
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    //System.out.println("1: " + getSelectedObject().toString());
  }

  /** called when the user clicks once on an item in the tree. */ 
  public void myDoubleClick(int row, TreePath path) {
    if (getSelectedObject() == null) return;
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    //System.out.println("2: " + getSelectedObject().toString());
  }

  ////////////////////////////////////////////////////////////////
  // internal methods

  protected void updateTree() {
    ToDoPerspective tm = (ToDoPerspective) _combo.getSelectedItem();
    if (tm != _curPerspective) {
      _curPerspective = tm;
      if (_curPerspective == null) _tree.hide();
      else {
	System.out.println("ToDoPane setting tree model");
	_curPerspective.setRoot(_root);
	_tree.setModel(_curPerspective);
	_tree.show(); // blinks?
      }
    }
  }

} /* end class ToDoPane */
