package uci.uml.ui;


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.plaf.metal.MetalLookAndFeel;

//import uci.util.*;
import uci.argo.kernel.*;

public class TabHistory extends TabSpawnable
implements ListSelectionListener, ListCellRenderer, MouseMotionListener {

  ////////////////////////////////////////////////////////////////
  // class variables
  protected ImageIcon _CritiqueIcon = loadIconResource("PostIt0");
  protected ImageIcon _ResolveIcon = loadIconResource("PostIt100");
  protected ImageIcon _ManipIcon = loadIconResource("PostIt100");
  protected ImageIcon _HistoryItemIcon = loadIconResource("Rectangle");

  
  ////////////////////////////////////////////////////////////////
  // instance variables
  Object _target;
  Vector _data;
  JList _list = new JList();
  JLabel _label = new JLabel();
  DefaultCellEditor _editor = new DefaultCellEditor(new JTextField());
  EtchedBorder _border = new EtchedBorder(EtchedBorder.LOWERED);

  ////////////////////////////////////////////////////////////////
  // constructor
  public TabHistory() {
    super("History");
    setLayout(new BorderLayout());
    //setFont(new Font("Dialog", Font.PLAIN, 10));
    //_label.setFont(new Font("Dialog", Font.PLAIN, 10));
    _label.setOpaque(true);
    //setUpMockItems();
    add(new JScrollPane(_list), BorderLayout.CENTER);
    _list.addListSelectionListener(this);
    _list.setCellRenderer(this);
    _list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    _list.setEnabled(true);
    _list.addMouseMotionListener(this);
    _list.setModel(new HistoryListModel());
  }

  //needs-more-work: should be more than just a scrolling list
  // need to examine individual items, filter, sort,...


  protected void setUpMockItems() {
    _list.setListData(_data);
  }

  /** 
   * Called whenever the value of the selection changes.
   * @param e the event that characterizes the change.
   */
  public void valueChanged(ListSelectionEvent e) {
    // needs-more-work: called on each critique!
    //System.out.println("user selected " + _list.getSelectedValue());
  }


  // This is the only method defined by ListCellRenderer.  We just
  // reconfigure the Jlabel each time we're called.
  
  public Component getListCellRendererComponent(
         JList list,
         Object value,            // value to display
         int index,               // cell index
         boolean isSelected,      // is the cell selected
         boolean cellHasFocus)    // the list and the cell have the focus
  {
    String s = value.toString();
    _label.setText(s);
    _label.setBackground(isSelected ?
			 MetalLookAndFeel.getTextHighlightColor() :
    			 MetalLookAndFeel.getWindowBackground());
    _label.setForeground(isSelected ?
			 MetalLookAndFeel.getHighlightedTextColor() :
			 MetalLookAndFeel.getUserTextColor());
    if (value instanceof HistoryItemManipulation) {
      _label.setIcon(_ManipIcon);
    }
    else if (value instanceof HistoryItemCritique) {
      _label.setIcon(_CritiqueIcon);
    }
    else if (value instanceof HistoryItemResolve) {
      _label.setIcon(_ResolveIcon);
    }
    else if (value instanceof HistoryItem) {
      _label.setIcon(_HistoryItemIcon);
    }
    return _label;
  }

  ////////////////////////////////////////////////////////////////
  // MouseMotionListener implementation

  public void mouseMoved(MouseEvent me) {
    int index = _list.locationToIndex(me.getPoint());
    if (index == -1) return;
    String tip = _list.getModel().getElementAt(index).toString();
    System.out.println("tip=" + tip);
    _list.setToolTipText(tip);
  }

  public void mouseDragged(MouseEvent me) { }


  ////////////////////////////////////////////////////////////////
  // utility functions
  
  protected static ImageIcon loadIconResource(String name) {
    String imgName = imageName(name);
    ImageIcon res = null;
    try {
      java.net.URL imgURL = UMLTreeCellRenderer.class.getResource(imgName);
      return new ImageIcon(imgURL);
    }
    catch (Exception ex) {
      return new ImageIcon(name);
    }
  }

  protected static String imageName(String name) {
    return "/uci/Images/" + stripJunk(name) + ".gif";
  }
  
  protected static String stripJunk(String s) {
    String res = "";
    int len = s.length();
    for (int i = 0; i < len; i++) {
      char c = s.charAt(i);
      if (Character.isJavaLetterOrDigit(c)) res += c;
    }
    return res;
  }
  
  ////////////////////////////////////////////////////////////////
  // inner classes
  
//   protected Icon _manuipIcon = new ManipIcon();

//   protected Icon _criticFiredIcon = new FiredIcon();

//   protected Icon _criticResolvedIcon = new ResolvedIcon();

} /* end class TabHistory */


// class ManipIcon implements Icon {
//   public void paintIcon(Component c, Graphics g, int x, int y) {
//     int w = getIconWidth(), h = getIconHeight();
//     g.setColor(Color.black);
//     Polygon p = new Polygon();
//     p.addPoint(x, y + h);
//     p.addPoint(x + w/2+1, y);
//     p.addPoint(x + w, y + h);
//     g.fillPolygon(p);
//   }
//   public int getIconWidth() { return 9; }
//   public int getIconHeight() { return 9; }
// }


// class FiredIcon implements Icon {
//   public void paintIcon(Component c, Graphics g, int x, int y) {
//     int w = getIconWidth(), h = getIconHeight();
//     g.setColor(Color.black);
//     Polygon p = new Polygon();
//     p.addPoint(x+1, y + h/2+1);
//     p.addPoint(x + w, y);
//     p.addPoint(x + w, y + h);
//     g.fillPolygon(p);
//   }
//   public int getIconWidth() { return 9; }
//   public int getIconHeight() { return 9; }
// }


// class ResolvedIcon implements Icon {
//   public void paintIcon(Component c, Graphics g, int x, int y) {
//     int w = getIconWidth(), h = getIconHeight();
//     g.setColor(Color.black);
//     Polygon p = new Polygon();
//     p.addPoint(x+1, y + h/2+1);
//     p.addPoint(x + w, y);
//     p.addPoint(x + w, y + h);
//     g.fillPolygon(p);
//   }
//   public int getIconWidth() { return 9; }
//   public int getIconHeight() { return 9; }
// }

class HistoryListModel implements ListModel, HistoryListener {
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected EventListenerList listenerList = new EventListenerList();


  ////////////////////////////////////////////////////////////////
  // constructor
  public HistoryListModel() {
    History.TheHistory.addHistoryListener(this);
  }
			     

  ////////////////////////////////////////////////////////////////
  // HistoryListener implementation

  public void historyAdded(HistoryEvent he) {
    fireIntervalAdded(this, he.getIndex(), he.getIndex());
  }

  ////////////////////////////////////////////////////////////////
  // ListModel implementation

  /** 
   * Returns the length of the list.
   */
  public int getSize() {
    History h = uci.argo.kernel.History.TheHistory;
    return h.getItems().size();
  }

  /**
   * Returns the value at the specified index.  
   */
  public Object getElementAt(int index) {
    History h = uci.argo.kernel.History.TheHistory;
    return h.getItems().elementAt(index);
  }

  
  /**
   * Add a listener to the list that's notified each time a change
   * to the data model occurs.
   * @param l the ListDataListener
   */  
  public void addListDataListener(ListDataListener l) {
    listenerList.add(ListDataListener.class, l);
  }
  

  /**
   * Remove a listener from the list that's notified each time a 
   * change to the data model occurs.
   * @param l the ListDataListener
   */  
  public void removeListDataListener(ListDataListener l) {
    listenerList.remove(ListDataListener.class, l);
  }


  /*
   * AbstractListModel subclasses must call this method <b>after</b>
   * one or more elements are added to the model.  The new elements
   * are specified by a closed interval index0, index1, i.e. the
   * range that includes both index0 and index1.  Note that
   * index0 need not be less than or equal to index1.
   * 
   * @param source The ListModel that changed, typically "this".
   * @param index0 One end of the new interval.
   * @param index1 The other end of the new interval.
   * @see EventListenerList
   * @see DefaultListModel
   */
  protected void fireIntervalAdded(Object source, int index0, int index1)
  {
    Object[] listeners = listenerList.getListenerList();
    ListDataEvent e = null;
    
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == ListDataListener.class) {
	if (e == null) {
	  e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index0, index1);
	}
	((ListDataListener)listeners[i+1]).intervalAdded(e);
      }	       
    }
  }


  
} /* end class HistoryListModel */
