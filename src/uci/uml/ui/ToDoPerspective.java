package uci.uml.ui;


import java.util.*;
import java.awt.*;
import java.io.Serializable;
import com.sun.java.swing.tree.*;
import com.sun.java.swing.event.*;
import uci.argo.kernel.*;

public abstract class ToDoPerspective
implements Serializable, TreeModel, ToDoListListener {

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected Object _root;
  protected Vector _pseudoNodes = new Vector();

  protected EventListenerList _listenerList = new EventListenerList();

  ////////////////////////////////////////////////////////////////
  // constructor
  public ToDoPerspective() { }

  ////////////////////////////////////////////////////////////////
  // TreeModel implementation
  
  public Object getRoot() { return _root; }
  public void setRoot(ToDoList r) { _root = r; }


  // needs-more-work: if there are absolutely no outstanding
  // ToDoItems, put a special pseudonode in the tree to say that.
  
  public Object getChild(Object parent, int index) {
    System.out.println("ToDoPerspective getChild");
    if (parent instanceof ToDoList) {
      return _pseudoNodes.elementAt(index);
    }
    else if (parent instanceof ToDoPseudoNode) {
      Vector items = ((ToDoPseudoNode)parent).getToDoItems();
      return items.elementAt(index);
    }    
    return null;
  }
  
  public int getChildCount(Object parent) {
    System.out.println("ToDoPerspective getChildCount");
    if (parent instanceof ToDoList) {
      return _pseudoNodes.size();
    }
    else if (parent instanceof ToDoPseudoNode) {
      Vector items = ((ToDoPseudoNode)parent).getToDoItems();
      return items.size();
    }    
    return 0;
  }
  
  public int getIndexOfChild(Object parent, Object child) {
    System.out.println("ToDoPerspective getChild");
    if (parent instanceof ToDoList) {
      return _pseudoNodes.indexOf(child);
    }
    else if (parent instanceof ToDoPseudoNode) {
      Vector items = ((ToDoPseudoNode)parent).getToDoItems();
      return items.indexOf(child);
    }    
    return 0;
  }


  /**
   * Returns true if <I>node</I> is a leaf.  It is possible for this method
   * to return false even if <I>node</I> has no children.  A directory in a
   * filesystem, for example, may contain no files; the node representing
   * the directory is not a leaf, but it also has no children.
   *
   * @param   node    a node in the tree, obtained from this data source
   * @return  true if <I>node</I> is a leaf
   */
  public boolean isLeaf(Object node) {
    if (node instanceof ToDoList) return false;
    if (node instanceof ToDoPseudoNode) return false;
    if (node instanceof ToDoItem) return true;
    System.out.println("unsecpected case in ToDoPerspective isLeaf" +
		       node.toString());
    return true;
  }

  ////////////////////////////////////////////////////////////////
  // ToDoListListener implementation

  public void toDoListChanged(ToDoListEvent tde) {
    computePseudoNodes();
    // notify JTreePane
  }

  protected void computePseudoNodes() {
    if (_pseudoNodes == null) _pseudoNodes = new Vector();
    else _pseudoNodes.removeAllElements();
  }


  /**
   * Messaged when the user has altered the value for the item identified
   * by <I>path</I> to <I>newValue</I>.  If <I>newValue</I> signifies
   * a truly new value the model should post a treeNodesChanged
   * event.
   *
   * @param path path to the node that the user has altered.
   * @param newValue the new value from the TreeCellEditor.
   */
  public void valueForPathChanged(TreePath path, Object newValue) {
    // needs-more-work 
  }


  //
  //  Change Events
  //

  /*
   * Notify all listeners that have registered interest for
   * notification on this event type.  The event instance 
   * is lazily created using the parameters passed into 
   * the fire method.
   * @see EventListenerList
   */
  protected void fireTreeNodesChanged(Object source, Object[] path, 
				      int[] childIndices, 
				      Object[] children) {
    // Guaranteed to return a non-null array
    Object[] listeners = _listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==TreeModelListener.class) {
	// Lazily create the event:
	if (e == null)
	  e = new TreeModelEvent(source, path, 
				 childIndices, children);
	((TreeModelListener)listeners[i+1]).treeNodesChanged(e);
      }          
    }
  }

  /*
   * Notify all listeners that have registered interest for
   * notification on this event type.  The event instance 
   * is lazily created using the parameters passed into 
   * the fire method.
   * @see EventListenerList
   */
  protected void fireTreeNodesInserted(Object source, Object[] path, 
				       int[] childIndices, 
				       Object[] children) {
    // Guaranteed to return a non-null array
    Object[] listeners = _listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==TreeModelListener.class) {
	// Lazily create the event:
	if (e == null)
	  e = new TreeModelEvent(source, path, 
				 childIndices, children);
	((TreeModelListener)listeners[i+1]).treeNodesInserted(e);
      }          
    }
  }

  /*
   * Notify all listeners that have registered interest for
   * notification on this event type.  The event instance 
   * is lazily created using the parameters passed into 
   * the fire method.
   * @see EventListenerList
   */
  protected void fireTreeNodesRemoved(Object source, Object[] path, 
				      int[] childIndices, 
				      Object[] children) {
    // Guaranteed to return a non-null array
    Object[] listeners = _listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==TreeModelListener.class) {
	// Lazily create the event:
	if (e == null)
	  e = new TreeModelEvent(source, path, 
				 childIndices, children);
	((TreeModelListener)listeners[i+1]).treeNodesRemoved(e);
      }          
    }
  }

  /*
   * Notify all listeners that have registered interest for
   * notification on this event type.  The event instance 
   * is lazily created using the parameters passed into 
   * the fire method.
   * @see EventListenerList
   */
  protected void fireTreeStructureChanged(Object source, Object[] path, 
					  int[] childIndices, 
					  Object[] children) {
    // Guaranteed to return a non-null array
    Object[] listeners = _listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==TreeModelListener.class) {
	// Lazily create the event:
	if (e == null)
	  e = new TreeModelEvent(source, path, 
				 childIndices, children);
	((TreeModelListener)listeners[i+1]).treeStructureChanged(e);
      }          
    }
  }


  public void addTreeModelListener(TreeModelListener l) {
    _listenerList.add(TreeModelListener.class, l);
  }

  public void removeTreeModelListener(TreeModelListener l) {
    _listenerList.remove(TreeModelListener.class, l);
  }
  

} /* end class ToDoPerspective */
