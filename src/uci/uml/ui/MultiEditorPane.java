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
  protected ForwardingPanel _awt_comp;
  protected Vector _tabPanels = new Vector();

  ////////////////////////////////////////////////////////////////
  // constructors

  public MultiEditorPane() {
    setLayout(new BorderLayout());
    add(_tabs, BorderLayout.CENTER);
    _tabs.addChangeListener(this);
    _tabs.addMouseListener(this);
  }



  ////////////////////////////////////////////////////////////////
  // accessors

  public Dimension getMinimumSize() { return new Dimension(100, 100); }
  public Dimension getPreferredSize() { return new Dimension(600, 400); }

  public void setTarget(Object t) {
    String prevSelectedTabTitle = "no editor";
    
    if (t == _target) return;
    // remove old tabs
    int oldSelection = _tabs.getSelectedIndex();
    if (oldSelection >= 0) 
      prevSelectedTabTitle = _tabs.getTitleAt(oldSelection);
    for (int i = _tabs.getTabCount() - 1; i >= 0; --i) _tabs.removeTabAt(i);
    _tabPanels.removeAllElements();
    
    _target = t;

    // add new tabs
    // uci.beans.EditorManager?
    _tabPanels.addElement(new TabUMLDisplay());
    _tabPanels.addElement(new TabText());
    _tabPanels.addElement(new TabHash());
    _tabPanels.addElement(new ClassDiagramEditor());

    Enumeration tabPanelEnum = _tabPanels.elements();
    while (tabPanelEnum.hasMoreElements()) {
      JPanel p = (JPanel) tabPanelEnum.nextElement();
      String title = "tab";
      if (p instanceof TabSpawnable) title = ((TabSpawnable)p).getTitle();
      // needs-more-work: tab icons
      _tabs.addTab(title, p);
      if (p instanceof TabModelTarget)
	((TabModelTarget)p).setTarget(_target);
    }
      
    
    int correspondingTab = _tabs.indexOfTab(prevSelectedTabTitle);
    if (correspondingTab >= 0)
      _tabs.setSelectedIndex(correspondingTab);
  }

  public Object getTarget() { return _target; }

  
  ////////////////////////////////////////////////////////////////
  // actions


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
