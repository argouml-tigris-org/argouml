package uci.uml.ui;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import uci.gef.*;
import uci.graph.*;
import uci.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
//import com.sun.java.swing.border.*;


public class MultiEditorPane extends JPanel
implements ChangeListener, MouseListener {

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected Object _target;
  protected JTabbedPane _tabs = new JTabbedPane(JTabbedPane.BOTTOM);
  protected Editor _ed;
 // protected ForwardingPanel _awt_comp;
  protected Vector _tabPanels = new Vector();

  ////////////////////////////////////////////////////////////////
  // constructors

  public MultiEditorPane() {
    System.out.println("mainkng MultiEditorPane");
    _tabPanels.addElement(new TabUMLDisplay());
    _tabPanels.addElement(new TabSrc());
    _tabPanels.addElement(new TabDiagram());
    _tabPanels.addElement(new TabTable());
    _tabPanels.addElement(new TabHash());
    
    setLayout(new BorderLayout());
    add(_tabs, BorderLayout.CENTER);

    _tabs.addChangeListener(this);
    for (int i = 0; i < _tabPanels.size(); i++) {
      String title = "tab";
      JPanel t = (JPanel) _tabPanels.elementAt(i);
      if (t instanceof TabSpawnable)
	title = ((TabSpawnable)t).getTitle();
      _tabs.addTab("As " + title, t);
    } /* end for */


    _tabs.addChangeListener(this);
    _tabs.addMouseListener(this);
    setTarget(null);
  }



  ////////////////////////////////////////////////////////////////
  // accessors

  public Dimension getMinimumSize() { return new Dimension(100, 100); }
  public Dimension getPreferredSize() { return new Dimension(400, 400); }

  public void setTarget(Object target) {
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
    if (jumpToFirstEnabledTab && firstEnabled != -1 )
      _tabs.setSelectedIndex(firstEnabled);
  }
    

  public Object getTarget() { return _target; }

  
  ////////////////////////////////////////////////////////////////
  // actions

  public void select(Object o) {
    Component curTab = _tabs.getSelectedComponent();
    System.out.println("select?" + curTab);
    if (curTab instanceof TabDiagram) {
      System.out.println("selectByOwner");
      ((TabDiagram)curTab).getJGraph().selectByOwner(o);
    }
    //needs-more-work: handle tables
    
  }
  
  ////////////////////////////////////////////////////////////////
  // event handlers

  /** called when the user selects an item in the tree, by clicking or
   *  otherwise. */
  public void stateChanged(ChangeEvent e) {
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    System.out.println("MultiEditorPane state changed");
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

  

} /* end class MultiEditorPane */
