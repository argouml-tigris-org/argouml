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

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.Notation;
import org.argouml.application.api.QuadrantPanel;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
import org.argouml.uml.diagram.ui.ActionAddExistingEdge;
import org.argouml.uml.diagram.ui.ActionAddExistingNode;
import org.argouml.uml.ui.ActionRemoveFromModel;
import org.argouml.uml.ui.UMLAction;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.ui.PopupGenerator;
import ru.novosoft.uml.behavior.common_behavior.MDataValue;
import ru.novosoft.uml.behavior.common_behavior.MInstance;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MFlow;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MRelationship;
import ru.novosoft.uml.model_management.MPackage;


/** The upper-left pane of the main Argo/UML window.  This shows the
 * contents of the current project in one of several ways that are
 * determined by NavPerspectives.
 *
 * This does not allow drag and drop as yet. This is considered
 * a must have for as yet undetermined future.
 *
 * other feature enhancements include showing the presence of
 * note connected to individual UML artifacts and to diagrams
 * themselves.
 */


public class NavigatorPane extends JPanel
implements ItemListener, TreeSelectionListener, PropertyChangeListener, QuadrantPanel {
    protected static Category cat = Category.getInstance(NavigatorPane.class);
    
    /**
     * The singleton implementation
     */
    
    private static NavigatorPane _navPane;
    
    public static NavigatorPane getNavigatorPane() {
        if (_navPane == null) {
            _navPane = new NavigatorPane();
        }
        return _navPane;
    }
    
  //, CellEditorListener

  ////////////////////////////////////////////////////////////////
  // constants
  public static int MAX_HISTORY = 10;

  ////////////////////////////////////////////////////////////////
  // instance variables

  // vector of TreeModels
  protected Vector _perspectives = new Vector();

  protected Toolbar _toolbar = new Toolbar();
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
    JPanel toolbarPanel = new JPanel(new BorderLayout());
    toolbarPanel.add(_toolbar, BorderLayout.WEST);
    _toolbar.add(_combo);
    _toolbar.add(Actions.NavBack);
    _toolbar.add(Actions.NavForw);
    _toolbar.add(Actions.NavConfig);
    add(toolbarPanel, BorderLayout.NORTH);
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
    Configuration.addListener(Notation.KEY_USE_GUILLEMOTS, this);
    Configuration.addListener(Notation.KEY_SHOW_STEREOTYPES, this);
    setPreferredSize(new Dimension(
                                  Configuration.getInteger(Argo.KEY_SCREEN_WEST_WIDTH, ProjectBrowser.DEFAULT_COMPONENTWIDTH),0
                                  ));
    ProjectManager.getManager().addPropertyChangeListener(this);                                  
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
    //TODO: should fire its own event and ProjectBrowser
    //should register a listener
    //ProjectBrowser.TheInstance.setTarget(getSelectedObject());
    //ProjectBrowser.TheInstance.setTarget(getSelectedObject());
  }


  /** called when the user clicks once on an item in the tree. */
  public void mySingleClick(int row, TreePath path) {
    //TODO: should fire its own event and ProjectBrowser
    //should register a listener
    /*
    Object sel = getSelectedObject();
    if (sel  == null) return;
    //if (sel instanceof Diagram) {
      myDoubleClick(row, path);
      return;
    
    }
    ProjectBrowser.TheInstance.select(sel);
    */
    mouseClick(row, path);
    _clicksInNavPane++;
  }
  
  protected void mouseClick(int row, TreePath path) {
  	 Object sel = getSelectedObject();
     if (sel == null) return;
  	 addToHistory(sel);
     ProjectBrowser.TheInstance.setTarget(sel);
     // ProjectBrowser.TheInstance.setDetailsTarget(sel);
     repaint();
  }

  /** called when the user clicks twice on an item in the tree. */
  public void myDoubleClick(int row, TreePath path) {
    //TODO: should fire its own event and ProjectBrowser
    //should register a listener
    mouseClick(row, path);
    _clicksInNavPane += 2;
    repaint();
  }

  public void navDown() {
    int row = _tree.getMinSelectionRow();
    if (row == _tree.getRowCount()) row = 0;
    else row = row + 1;
    _tree.setSelectionRow(row);
    ProjectBrowser.TheInstance.setTarget(getSelectedObject());
  }

  public void navUp() {
    int row = _tree.getMinSelectionRow();
    if (row == 0) row = _tree.getRowCount();
    else row = row - 1;
    _tree.setSelectionRow(row);
    ProjectBrowser.TheInstance.setTarget(getSelectedObject());
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
	cat.warn("null perspective!");
      _tree.setVisible(false);
    }
    else {
      _curPerspective.setRoot(_root);
      _tree.setModel(_curPerspective);
      _tree.setVisible(true); // blinks?
    }
  }

    /**
     * Locale a popup menu item in the navigator pane.
     *
     * @param key The key for the string to localize.
     * @return The localized string.
     */
    final protected String menuLocalize(String key) {
	return Argo.localize("Tree",key);
    }

  ////////////////////////////////////////////////////////////////
  // inner classes

    class NavigatorMouseListener extends MouseAdapter {
    public void mouseClicked(MouseEvent me) {
		//
		//   if this platform's popup trigger is occurs on a click
		//       then do the popup
		if(me.isPopupTrigger()) {
			me.consume();
			showPopupMenu(me);
		}
		else {
			//
			//    otherwise expand the tree?
			//?      //if (me.isConsumed()) return;
			int row = _tree.getRowForLocation(me.getX(), me.getY());
			TreePath path = _tree.getPathForLocation(me.getX(), me.getY());
			if (row != -1) {
				if (me.getClickCount() == 1) mySingleClick(row, path);
				else if (me.getClickCount() >= 2) myDoubleClick(row, path);
				//me.consume();
			}
		}
    }

    public void showPopupMenu(MouseEvent me) {
        //TreeCellEditor tce = _tree.getCellEditor();
        JPopupMenu popup = new JPopupMenu("test");
        Object obj = getSelectedObject();
	if (obj instanceof PopupGenerator) {
	    Vector actions = ((PopupGenerator)obj).getPopUpActions(me);
	    for(Enumeration e = actions.elements(); e.hasMoreElements(); ) {
		popup.add((AbstractAction) e.nextElement());
	    }
	}
        else {
            if ((obj instanceof MClassifier && !(obj instanceof MDataType))
                || ((obj instanceof MPackage) && (obj != ProjectManager.getManager().getCurrentProject().getModel())) 
	        || obj instanceof MStateVertex 
                || (obj instanceof MInstance && !(obj instanceof MDataValue) && !(ProjectBrowser.TheInstance.getActiveDiagram() instanceof UMLSequenceDiagram))) {
                    UMLAction action = new ActionAddExistingNode(menuLocalize("menu.popup.add-to-diagram"),obj);
                    action.setEnabled(action.shouldBeEnabled());
	       popup.add(action);
            }
            if ((obj instanceof MRelationship && !(obj instanceof MFlow)) || 
                ((obj instanceof MLink) && !(ProjectBrowser.TheInstance.getActiveDiagram() instanceof UMLSequenceDiagram)) ||
                (obj instanceof MTransition)) {
                    UMLAction action = new ActionAddExistingEdge(menuLocalize("menu.popup.add-to-diagram"),obj);
                    action.setEnabled(action.shouldBeEnabled());  
                popup.add(action);
            }
                
            if ((obj instanceof MModelElement && (obj != ProjectManager.getManager().getCurrentProject().getModel())) || obj instanceof Diagram ) {
	        popup.add(ActionRemoveFromModel.SINGLETON);
            }
            popup.add(new ActionGoToDetails(menuLocalize("Properties")));
        }
	popup.show(_tree,me.getX(),me.getY());
    }

    public void mousePressed(MouseEvent me) {
		if (me.isPopupTrigger()) {
			me.consume();
			showPopupMenu(me);
		}
    }

    public void mouseReleased(MouseEvent me) {
		if (me.isPopupTrigger()) {
			me.consume();
			showPopupMenu(me);
		}
    }

  } /* end class NavigatorMouseListener */

  class NavigatorKeyListener extends KeyAdapter {
    // maybe use keyTyped?
    public void keyPressed(KeyEvent e) {
      cat.debug("got key: " + e.getKeyCode());
      int code = e.getKeyCode();
      if (code == KeyEvent.VK_ENTER || code  == KeyEvent.VK_SPACE) {
	Object newTarget = getSelectedObject();
	if (newTarget != null)
	  ProjectBrowser.TheInstance.setTarget(newTarget);
      }
    }
  }

  public int getQuadrant() { return Q_TOP_LEFT; }
  

  
  public TreePath getParentPath() {
  	TreePath path = _tree.getSelectionPath();
  	if (path != null) return path.getParentPath();
  	return null;
  }
  	
  /** Listen for configuration changes that could require repaint
   *  of the navigator pane.
   * Listens for changes of the project fired by projectmanager.
   *
   *  @since ARGO0.11.2
   */
  public void propertyChange(PropertyChangeEvent pce) {
    if (pce.getPropertyName().equals(ProjectManager.CURRENT_PROJECT_PROPERTY_NAME)) {
        setRoot(pce.getNewValue());
        forceUpdate();
        return;
    }
      if (Notation.KEY_USE_GUILLEMOTS.isChangedProperty(pce) ||
          Notation.KEY_SHOW_STEREOTYPES.isChangedProperty(pce)) {
          _tree.forceUpdate();
      }
  } 
  
  

} /* end class NavigatorPane */
