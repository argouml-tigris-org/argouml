package uci.ui;

import java.awt.*;
import java.util.*;
import uci.util.*;
import symantec.itools.awt.*;

public class TreePerspective extends TreeView implements Observer {

  ///////
  // instance variables
  protected ChildGenerator _cg = null;
  protected Predicate _filter = null; //new ChildGenContainment();
  protected Object _rootObj = null;
  protected Hashtable _nodesToItems = new Hashtable();
  protected Hashtable _itemsToNodes = new Hashtable();

  ///////
  // constructors
  public TreePerspective() { }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setChildGenerator(ChildGenerator cg) { _cg = cg; }
  public ChildGenerator getChildGenerator() { return _cg; }

  public void setFilter(Predicate f) { _filter = f; }
  public Predicate getFilter() { return _filter; }

  public void setRootObject(Object obj) {
    // remove all old observers!
    //     if (obj instanceof Observable)
    //       ((Observable)_rootObj).removeObserver(this);
    _rootObj = obj;
    if (obj instanceof Observable)
      ((Observable)_rootObj).addObserver(this);
  }
  public Object getRootObject() { return _rootObj; }

  public void insert(TreeNode tn, Object obj, TreeNode parent) {
    insert(tn, obj, parent, TreeView.CHILD);
  }

  public void insert(TreeNode tn, Object obj, TreeNode parent, int pos) {
    if (parent == null && pos == TreeView.CHILD) append(tn, obj);
    _nodesToItems.put(tn, obj);
    _itemsToNodes.put(obj, tn);
    if (obj instanceof Observable)
      ((Observable)obj).addObserver(this);
    super.insert(tn, parent, pos);
  }

  public void append(TreeNode tn, Object obj) {
    _nodesToItems.put(tn, obj);
    _itemsToNodes.put(obj, tn);
    if (obj instanceof Observable)
      ((Observable)obj).addObserver(this);
    super.append(tn);
  }

  public TreeNode nodeFor(Object obj) {
    return (TreeNode) _itemsToNodes.get(obj);
  }

  public Object itemFor(TreeNode tn) {
    return  _nodesToItems.get(tn);
  }

  public Object selectedObject() {
    TreeNode tn = getSelectedNode();
    if (tn == null) return null;
    return itemFor(tn);
  }

  public boolean contains(Object obj) {
    return null != nodeFor(obj);
  }

  ////////////////////////////////////////////////////////////////
  // helper functions

  protected TreeNode makeNodeFor(Object obj) {
    String name;
//     if (obj instanceof INamedObject)
//       name = ((INamedObject)obj).getName();
//     else
      name = obj.toString();
    return new TreeNode(name);
    // colors and icons?
  }

  ////////////////////////////////////////////////////////////////
  // notifications and updates

  public void update(Observable obj, Object arg) {
    TreeNode tn = nodeFor(obj);
//     if (obj instanceof INamedObject)
//       name = ((INamedObject)obj).getName();
//     else
    String name = obj.toString();
    if (tn != null) tn.setText(name);
    addChildren(obj);
    repaint();
  }

  public void addChildren(Object changedObj) {
    TreeNode parentNode = nodeFor(changedObj);
    TreeNode prevSib = null;
    if (_cg == null) return;
    Enumeration kids = _cg.gen(changedObj);
    while (kids.hasMoreElements()) {
      Object kid = kids.nextElement();
      if (!this.contains(kid)) {
	TreeNode newKidTreeNode = makeNodeFor(kid);
	if (prevSib == null)
	  insert(newKidTreeNode, kid, parentNode, TreeView.CHILD);
	else
	  insert(newKidTreeNode, kid, prevSib, TreeView.NEXT);
	addChildren(kid);
      }
      prevSib = nodeFor(kid);
    }
    redraw();
  }

} /* end class TreePerspective */
