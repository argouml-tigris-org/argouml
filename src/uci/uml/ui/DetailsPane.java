// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
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
import uci.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
//import com.sun.java.swing.border.*;

import uci.gef.*;


/** The lower-right pane of the main Argo/UML window.  This panel has
 *  several tabs that show details of the selected ToDoItem, or the
 *  selected model element in the NavigationPane, or the
 *  MultiEditorPane. */

public class DetailsPane extends JPanel
implements ChangeListener, MouseListener {
  ////////////////////////////////////////////////////////////////
  // constants

  public static int WIDTH = 690;
  public static int HEIGHT = 520;
  public static int INITIAL_WIDTH = 400;
  public static int INITIAL_HEIGHT = 200;

  //public static String TAB_LABELS[] = { "ToDoItem", "Docs", "Props", "Src" };
  
  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Target is the currently selected object from the UML Model,
   *  usually selected from a Fig in the diagram or from the
   *  navigation panel. */
  protected Object _target = "this is set to null in contructor";
  protected Object _item = "this is set to null in contructor";

  // vector of TreeModels
  protected JTabbedPane _tabs = new JTabbedPane();
  protected Vector _tabPanels = new Vector();
  

  ////////////////////////////////////////////////////////////////
  // constructors

  public DetailsPane() {
    System.out.println("making DetailsPane");    
    _tabPanels.addElement(new TabToDo());
    _tabPanels.addElement(new TabProps());
    _tabPanels.addElement(new TabDocs());
    _tabPanels.addElement(new TabSrc());
    _tabPanels.addElement(new TabConstraints());
    _tabPanels.addElement(new TabTaggedValues());
    _tabPanels.addElement(new TabChecklist());
    _tabPanels.addElement(new TabHistory());

    setLayout(new BorderLayout());
    setFont(new Font("Dialog", Font.PLAIN, 10));
    add(_tabs, BorderLayout.CENTER);

    _tabs.addChangeListener(this);
    for (int i = 0; i < _tabPanels.size(); i++) {
      String title = "tab";
      JPanel t = (JPanel) _tabPanels.elementAt(i);
      if (t instanceof TabSpawnable)
	title = ((TabSpawnable)t).getTitle();
      if (t instanceof TabToDoTarget) {
	_tabs.addTab(title, _leftArrowIcon, t);
      }
      else if (t instanceof TabModelTarget) {
	_tabs.addTab(title, _upArrowIcon, t);
      }
      else {
	_tabs.addTab(title, t);
      }
    } /* end for */

    setTarget(null);
    _item = null;
    _tabs.addMouseListener(this);
  }
    


  ////////////////////////////////////////////////////////////////
  // accessors

  public JTabbedPane getTabs() { return _tabs; }
  
  // needs-more-work: ToDoItem
  public void setToDoItem(Object item) {
    _item = item;
    for (int i = 0; i < _tabPanels.size(); i++) {
      JPanel t = (JPanel) _tabPanels.elementAt(i);
      if (t instanceof TabToDoTarget) {
	((TabToDoTarget)t).setTarget(_item);
	_tabs.setSelectedComponent(t);
      }
    }
  }


  public void setTarget(Object target) {
    if (target instanceof Fig && ((Fig)target).getOwner() != null)
      target = ((Fig)target).getOwner();
    int firstEnabled = -1;
    boolean jumpToFirstEnabledTab = false;
    int currentTab = _tabs.getSelectedIndex();
    if (_target == target) return;
    _target = target;
    for (int i = 0; i < _tabPanels.size(); i++) {
      JPanel tab = (JPanel) _tabPanels.elementAt(i);      
      if (tab instanceof TabModelTarget) {
	TabModelTarget tabMT = (TabModelTarget) tab;
	tabMT.setTarget(_target);
	boolean shouldEnable = tabMT.shouldBeEnabled();
	_tabs.setEnabledAt(i, shouldEnable);
	if (shouldEnable && firstEnabled == -1) firstEnabled = i;
	if (currentTab == i && !shouldEnable) {
	  jumpToFirstEnabledTab = true;
	}
      }
    }
    if (jumpToFirstEnabledTab && firstEnabled != -1)
      _tabs.setSelectedIndex(firstEnabled);

    if (jumpToFirstEnabledTab && firstEnabled == -1)
      _tabs.setSelectedIndex(0);
  }
  
  public Object getTarget() { return _target; }

  public Dimension getMinimumSize() { return new Dimension(100, 100); }
  public Dimension getPreferredSize() { return new Dimension(400, 150); }

  ////////////////////////////////////////////////////////////////
  // actions

  public void selectTabNamed(String tabName) {
    for (int i = 0; i < _tabPanels.size(); i++) {
      String title = _tabs.getTitleAt(i);
      if (title != null && title.equals(tabName)) {
	_tabs.setSelectedIndex(i);
	return;
      }
    }        
  }

  public void selectNextTab() {
    int size = _tabPanels.size();
    int currentTab = _tabs.getSelectedIndex();
    
    for (int i = 1; i < _tabPanels.size(); i++) {
      int newTab = (currentTab + i) % size;
      if (_tabs.isEnabledAt(newTab)) {
	_tabs.setSelectedIndex(newTab);
	return;
      }
    }    
  }
  
  ////////////////////////////////////////////////////////////////
  // event handlers

  /** called when the user selects an item in the tree, by clicking or
   *  otherwise. */
  public void stateChanged(ChangeEvent e) {
    //System.out.println("DetailsPane state changed");
    Component sel = _tabs.getSelectedComponent();
    if (sel instanceof TabToDoTarget) {
      ((TabToDoTarget)sel).setTarget(null);
      ((TabToDoTarget)sel).setTarget(_item);
    }
    else if (sel instanceof TabToDoTarget) {
      ((TabModelTarget)sel).setTarget(null);
      ((TabModelTarget)sel).setTarget(_target);
    }
  }

  /** called when the user clicks once on a tab. */ 
  public void mySingleClick(int tab) {
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    //System.out.println("single: " + _tabs.getComponentAt(tab).toString());
  }

  /** called when the user clicks twice on a tab. */ 
  public void myDoubleClick(int tab) {
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    //System.out.println("double: " + _tabs.getComponentAt(tab).toString());
    JPanel t = (JPanel) _tabPanels.elementAt(tab);
    if (t instanceof TabSpawnable) ((TabSpawnable)t).spawn();
  }

  public void mousePressed(MouseEvent me) { }
  public void mouseReleased(MouseEvent me) { }
  public void mouseEntered(MouseEvent me) { }
  public void mouseExited(MouseEvent me) { }
  public void mouseClicked(MouseEvent me) {
    int tab = _tabs.getSelectedIndex();
    if (tab != -1) {
      Rectangle tabBounds = _tabs.getBoundsAt(tab);
      if (!tabBounds.contains(me.getX(), me.getY())) return;
      if (me.getClickCount() == 1) mySingleClick(tab);
      else if (me.getClickCount() >= 2) myDoubleClick(tab);
    }
  }

  
  protected Icon _upArrowIcon = new UpArrowIcon();

  protected Icon _leftArrowIcon = new LeftArrowIcon();
  
} /* end class DetailsPane */


////////////////////////////////////////////////////////////////
// related classes


class UpArrowIcon implements Icon {
  public void paintIcon(Component c, Graphics g, int x, int y) {
    int w = getIconWidth(), h = getIconHeight();
    g.setColor(Color.black);
    Polygon p = new Polygon();
    p.addPoint(x, y + h);
    p.addPoint(x + w/2+1, y);
    p.addPoint(x + w, y + h);
    g.fillPolygon(p);
    }
  public int getIconWidth() { return 9; }
  public int getIconHeight() { return 9; }
}

class LeftArrowIcon implements Icon {
  public void paintIcon(Component c, Graphics g, int x, int y) {
    int w = getIconWidth(), h = getIconHeight();
    g.setColor(Color.black);
    Polygon p = new Polygon();
    p.addPoint(x+1, y + h/2+1);
    p.addPoint(x + w, y);
    p.addPoint(x + w, y + h);
    g.fillPolygon(p);
  }
  public int getIconWidth() { return 9; }
  public int getIconHeight() { return 9; }
}
