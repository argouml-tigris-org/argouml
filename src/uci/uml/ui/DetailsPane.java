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
  protected Object _target = null;

  // vector of TreeModels
  protected JTabbedPane _tabs = new JTabbedPane();
  protected Vector _tabPanels = new Vector();
  

  ////////////////////////////////////////////////////////////////
  // constructors

  public DetailsPane() {
    System.out.println("making DetailsPane");    
    _tabPanels.addElement(new TabToDo());
    _tabPanels.addElement(new TabDocs());
    _tabPanels.addElement(new TabProps());
    _tabPanels.addElement(new TabSrc());
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
    
    _tabs.addMouseListener(this);
  }
    


  ////////////////////////////////////////////////////////////////
  // accessors

  // needs-more-work: ToDoItem
  public void setToDoItem(Object item) {
    for (int i = 0; i < _tabPanels.size(); i++) {
      JPanel t = (JPanel) _tabPanels.elementAt(i);
      if (t instanceof TabToDoTarget) {
	((TabToDoTarget)t).setTarget(item);
	_tabs.setSelectedComponent(t);
      }
    }
  }


  public void setTarget(Object t) {
    if (_target == t) return;
    _target = t;
    for (int i = 0; i < _tabPanels.size(); i++) {
      t = (JPanel) _tabPanels.elementAt(i);      
      if (t instanceof TabModelTarget) 
	((TabModelTarget)t).setTarget(t);
    }
  }
  public Object getTarget() { return _target; }

  public Dimension getMinimumSize() { return new Dimension(100, 100); }
  public Dimension getPreferredSize() { return new Dimension(400, 150); }

  
  ////////////////////////////////////////////////////////////////
  // event handlers

  /** called when the user selects an item in the tree, by clicking or
   *  otherwise. */
  public void stateChanged(ChangeEvent e) {
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    //System.out.println("DetailsPane state changed");
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

  ////////////////////////////////////////////////////////////////
  // inner classes
  
  protected Icon _upArrowIcon = new Icon() {
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
  };

  protected Icon _leftArrowIcon = new Icon() {
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
  };

} /* end class DetailsPane */
