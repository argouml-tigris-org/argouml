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

package org.argouml.cognitive.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.apache.log4j.Category;
import org.argouml.application.api.*;

import org.tigris.gef.ui.*;
import org.tigris.gef.util.*;

import org.argouml.cognitive.*;
import org.argouml.ui.*;

/** The lower-left pane of the main Argo/UML window. This ane shows
 *  a list or tree of all the "to do" items that the designer should
 *  condsider. */

public class ToDoPane extends JPanel
implements ItemListener, TreeSelectionListener, MouseListener, ToDoListListener, QuadrantPanel {
    protected static Category cat = 
        Category.getInstance(ToDoPane.class);
    
  ////////////////////////////////////////////////////////////////
  // constants

  public static int WIDTH = 690;
  public static int HEIGHT = 520;
  public static int INITIAL_WIDTH = 400;
  public static int INITIAL_HEIGHT = 200;
  public static int WARN_THRESHOLD = 50;
  public static int ALARM_THRESHOLD = 100;
  public static Color WARN_COLOR = Color.yellow;
  public static Color ALARM_COLOR = Color.pink;

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected ProjectBrowser _pb = null;

  // vector of TreeModels
  protected Vector _perspectives = new Vector();

  protected ToolBar _toolbar = new ToolBar();
  protected JComboBox _combo = new JComboBox();

  protected ToDoList _root = null;
  protected Action _flatView = Actions.FlatToDo;
  protected JToggleButton _flatButton;
  protected JLabel _countLabel = new JLabel(formatCountLabel(999));
  protected ToDoPerspective _curPerspective = null;
  protected JTree _tree = new DisplayTextTree();
  protected boolean _flat = false;
  protected Object _lastSel;
  protected int _oldSize = 0;
  protected char _dir = ' ';

  public static int _clicksInToDoPane = 0;
  public static int _dblClicksInToDoPane = 0;
  public static int _toDoPerspectivesChanged = 0;

  ////////////////////////////////////////////////////////////////
  // constructors

  public ToDoPane() {
    setLayout(new BorderLayout());
    _toolbar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    //_toolbar.add(new JLabel("Group by "));
    _toolbar.add(_combo);
    _flatButton = _toolbar.addToggle(_flatView, "Flat", "Hierarchical", "Flat");
    _toolbar.add(_countLabel);
    ImageIcon hierarchicalIcon =
		ResourceLoader.lookupIconResource("Hierarchical", "Hierarchical");
    ImageIcon flatIcon = ResourceLoader.lookupIconResource("Flat", "Flat");
    _flatButton.setIcon(hierarchicalIcon);
    _flatButton.setSelectedIcon(flatIcon);
    add(_toolbar, BorderLayout.NORTH);
    add(new JScrollPane(_tree), BorderLayout.CENTER);
    _combo.addItemListener(this);

    _tree.setRootVisible(false);
    _tree.setShowsRootHandles(true);
    _tree.addTreeSelectionListener(this);
    _tree.setCellRenderer(new ToDoTreeRenderer());

    _tree.addMouseListener(this);

    Designer.TheDesigner.getToDoList().addToDoListListener(this);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setRoot(ToDoList r) {
    _root = r;
    updateTree();
  }
  public ToDoList getRoot() { return _root; }

  public Vector getPerspectives() { return _perspectives; }
  public void setPerspectives(Vector pers) {
    _perspectives = pers;
    if (pers.isEmpty()) _curPerspective = null;
    else _curPerspective = (ToDoPerspective) pers.elementAt(0);

    //_combo.removeAllItems(); // broken in Swing-1.0.3?
    java.util.Enumeration persEnum = _perspectives.elements();
    while (persEnum.hasMoreElements())
      _combo.addItem(persEnum.nextElement());

    if (pers.isEmpty()) _curPerspective = null;
    else if (pers.contains(_curPerspective))
      setCurPerspective(_curPerspective);
    else
      setCurPerspective((ToDoPerspective)_perspectives.elementAt(0));
    updateTree();
  }

  public ToDoPerspective getCurPerspective() { return _curPerspective; }
  public void setCurPerspective(TreeModel per) {
    if (_perspectives == null || !_perspectives.contains(per)) return;
    _combo.setSelectedItem(per);
    _toDoPerspectivesChanged++;
  }

  public Object getSelectedObject() {
    return _tree.getLastSelectedPathComponent();
  }

  public void selectItem(ToDoItem item) {
    TreeModel tm = _tree.getModel();
    Object path[] = new Object[3];
    Object category = null;
    int size = _curPerspective.getChildCount(_root);
    for (int i = 0; i < size; i++) {
      category = _curPerspective.getChild(_root, i);
      if (_curPerspective.getIndexOfChild(category, item) != -1)
	break;
    }
    if (category == null) return;
    path[0] = _root;
    path[1] = category;
    path[2] = item;
    TreePath trPath = new TreePath(path);
    _tree.expandPath(trPath);
    _tree.scrollPathToVisible(trPath);
    _tree.setSelectionPath(trPath);
  }

  public boolean isFlat() { return _flat; }
  public void setFlat(boolean b) { _flat = b; }
  public void toggleFlat() {
    _flat = !_flat;
    _flatButton.getModel().setPressed(_flat);
    if (_flat) _tree.setShowsRootHandles(false);
    else _tree.setShowsRootHandles(true);
    updateTree();
  }

  public Dimension getMinimumSize() { return new Dimension(120, 100); }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** called when the user selects a perspective from the perspective
   *  combo. */
  public void itemStateChanged(ItemEvent e) {
    if (e.getSource() == _combo) updateTree();
  }

  /** called when the user selects an item in the tree, by clicking or
   *  otherwise. */
  public void valueChanged(TreeSelectionEvent e) {
    cat.debug("ToDoPane valueChanged");
    //TODO: should fire its own event and ProjectBrowser
    //should register a listener
    Object sel = getSelectedObject();
    ProjectBrowser.TheInstance.setToDoItem(sel);

    if (_lastSel instanceof ToDoItem) ((ToDoItem)_lastSel).deselect();
    if (sel instanceof ToDoItem) ((ToDoItem)sel).select();
    _lastSel = sel;
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



  // TODO: what should the difference be between a single
  // and double click?

  /** called when the user clicks once on an item in the tree. */
  public void mySingleClick(int row, TreePath path) {
    _clicksInToDoPane++;
    if (getSelectedObject() == null) return;
    //TODO: should fire its own event and ProjectBrowser
    //should register a listener
    cat.debug("1: " + getSelectedObject().toString());
  }

  /** called when the user clicks once on an item in the tree. */
  public void myDoubleClick(int row, TreePath path) {
    _dblClicksInToDoPane++;
    if (getSelectedObject() == null) return;
    Object sel = getSelectedObject();
    if (sel instanceof ToDoItem) {
      ((ToDoItem)sel).action();
      VectorSet offs = ((ToDoItem)sel).getOffenders();
      ProjectBrowser.TheInstance.jumpToDiagramShowing(offs);
    }

    //TODO: should fire its own event and ProjectBrowser
    //should register a listener
    cat.debug("2: " + getSelectedObject().toString());
  }

  ////////////////////////////////////////////////////////////////
  // DecisionModelListener implementation


  ////////////////////////////////////////////////////////////////
  // GoalListener implementation


  ////////////////////////////////////////////////////////////////
  // ToDoListListener implementation

  public void toDoItemsChanged(ToDoListEvent tde) {
    if (_curPerspective instanceof ToDoListListener)
      ((ToDoListListener)_curPerspective).toDoItemsChanged(tde);
    //updateCountLabel();
    //paintImmediately(getBounds());
  }

  public void toDoItemsAdded(ToDoListEvent tde) {
    if (_curPerspective instanceof ToDoListListener)
      ((ToDoListListener)_curPerspective).toDoItemsAdded(tde);
    updateCountLabel();
    //paintImmediately(getBounds());
  }

  public void toDoItemsRemoved(ToDoListEvent tde) {
    if (_curPerspective instanceof ToDoListListener)
      ((ToDoListListener)_curPerspective).toDoItemsRemoved(tde);
    updateCountLabel();
    //paintImmediately(getBounds());
  }

  public void toDoListChanged(ToDoListEvent tde) {
    if (_curPerspective instanceof ToDoListListener)
      ((ToDoListListener)_curPerspective).toDoListChanged(tde);
    updateCountLabel();
  }

    private static String formatCountLabel(int size) {
	switch (size) {
	case 0:
	    return Argo.localize("Cognitive", "todopane.label.no-items");
	case 1:
	    return MessageFormat.
		format(Argo.localize("Cognitive", "todopane.label.item"),
		       new Object[] { new Integer(size) }
		       );
	default:
	    return MessageFormat.
		format(Argo.localize("Cognitive", "todopane.label.items"),
		       new Object[] { new Integer(size) }
		       );
	}
    }

  public void updateCountLabel() {
    int size = Designer.TheDesigner.getToDoList().size();
    if (size > _oldSize) _dir = '+';
    if (size < _oldSize) _dir = '-';
    _oldSize = size;
    _countLabel.setText(formatCountLabel(size));
    _countLabel.setOpaque(size > WARN_THRESHOLD);
    _countLabel.setBackground((size >= ALARM_THRESHOLD) ? ALARM_COLOR
			                                : WARN_COLOR);
  }

  ////////////////////////////////////////////////////////////////
  // internal methods

  protected void updateTree() {
    ToDoPerspective tm = (ToDoPerspective) _combo.getSelectedItem();
    _curPerspective = tm;
    if (_curPerspective == null) _tree.setVisible(false);
    else {
      cat.debug("ToDoPane setting tree model");
      _curPerspective.setRoot(_root);
      _curPerspective.setFlat(_flat);
      _tree.setModel(_curPerspective);
      _tree.setVisible(true); // blinks?
    }
  }

  public int getQuadrant() { return Q_BOTTOM_LEFT; }


} /* end class ToDoPane */
