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

  protected ToDoList _root;
  protected Vector _pseudoNodes = new Vector();

  protected EventListenerList _listenerList = new EventListenerList();

  ////////////////////////////////////////////////////////////////
  // constructor
  public ToDoPerspective() { }

  ////////////////////////////////////////////////////////////////
  // TreeModel implementation
  
  public Object getRoot() { return _root; }
  public void setRoot(ToDoList r) {
    _root = r;
    _root.addToDoListListener(this);
    toDoListChanged(null);
  }


  // needs-more-work: if there are absolutely no outstanding
  // ToDoItems, put a special pseudonode in the tree to say that.
  
  public Object getChild(Object parent, int index) {
    System.out.println(this + " getChild");
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
    System.out.println(this + " getChildCount");
    if (parent instanceof ToDoList) {
      System.out.println("case ToDoList, returning " + _pseudoNodes.size());
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

  public void toDoItemAdded(ToDoListEvent tde) {
    System.out.println("toDoItemAdded");
    ToDoItem item = tde.getToDoItem();
    Vector newPseudos = addNewPseudoNodes(item);
    Object path[] = new Object[1];
    path[0] = _root;
    int childIndices[] = new int[1];
    Object children[] = new Object[1];

    if (newPseudos != null) {
      java.util.Enumeration newEnum = newPseudos.elements();
      while (newEnum.hasMoreElements()) {
	ToDoPseudoNode node = (ToDoPseudoNode) newEnum.nextElement();;
	System.out.println("toDoItemAdded firing new pseudonode!");
	childIndices[0] = _pseudoNodes.indexOf(node);
	children[0] = node;
	fireTreeNodesInserted(this, path, childIndices, children);
      }
    }
    
    path = new Object[2];
    path[0] = _root;
    java.util.Enumeration enum = _pseudoNodes.elements();
    while (enum.hasMoreElements()) {
      ToDoPseudoNode node = (ToDoPseudoNode) enum.nextElement();
      node.computeItems();
      Vector pseudoNodeItems = node.getToDoItems();
      if (pseudoNodeItems.contains(item)) {
	path[1] = node;
	System.out.println("toDoItemAdded firing new item!");
	childIndices[0] = pseudoNodeItems.indexOf(item);
	children[0] = item;
	fireTreeNodesInserted(this, path, childIndices, children);
      }
    }
  }

  public void toDoItemRemoved(ToDoListEvent tde) {
    ToDoItem item = tde.getToDoItem();
  }

  public void toDoListChanged(ToDoListEvent tde) {
    Vector removes = new Vector();
    java.util.Enumeration enum = _pseudoNodes.elements();
    while (enum.hasMoreElements()) {
      ToDoPseudoNode node = (ToDoPseudoNode) enum.nextElement();
      if (!isNeeded(node)) removes.addElement(node);
    }
    enum = removes.elements();
    while (enum.hasMoreElements())
      _pseudoNodes.removeElement(enum.nextElement());
    enum = _root.elements();
    while (enum.hasMoreElements()) {
      ToDoItem item = (ToDoItem) enum.nextElement();
      addNewPseudoNodes(item);
    }
    
    Object path[] = new Object[2];
    path[0] = _root;
    enum = _pseudoNodes.elements();
    while (enum.hasMoreElements()) {
      ToDoPseudoNode node = (ToDoPseudoNode) enum.nextElement();
      path[1] = node;
      fireTreeStructureChanged(path);
    }

//     // notify JTreePane
//     path[] = new Object[1];
//     path[0] = _root;
//     //     Enumeration enum = _pseudoNodes.elements();
//     //     while (enum.hasMoreElements()) {
//     //       path[1] = enum.nextElement();
//     fireTreeStructureChanged(path);
//     //    }

//     //expand those nodes that were expanded before
  }

  protected boolean isNeeded(ToDoPseudoNode node) { return true; }
  protected Vector addNewPseudoNodes(ToDoItem item) { return null; }
  

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
  protected void fireTreeStructureChanged(Object[] path) {
    // Guaranteed to return a non-null array
    Object[] listeners = _listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==TreeModelListener.class) {
	// Lazily create the event: only the path matters
	if (e == null) e = new TreeModelEvent(this, path, null, null);
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
