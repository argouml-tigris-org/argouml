// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

package org.argouml.ui.explorer;

import java.awt.EventQueue;
import java.util.*;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeNode;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import org.apache.log4j.Logger;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.explorer.rules.PerspectiveRule;

/**
 * The model for the Explorer tree view of the uml model.
 *
 * provides:
 *  - receives events from the uml model and updates itself and the tree ui.
 *  - responds to changes in perspetive and ordering.
 *
 * @author  alexb
 * @since 0.15.2
 */
public class ExplorerTreeModel extends DefaultTreeModel
		implements TreeModelUMLEventListener, ItemListener {

    private static final Logger LOG =
	Logger.getLogger(ExplorerTreeModel.class);

    /**
     * an array of 
     * {@link org.argouml.ui.explorer.rules.PerspectiveRule PerspectiveRules},
     * that determine the tree view.
     */
    Object rules[];

    /**
     * a map used to resolve model elements to tree nodes when determining
     * what effect a model event will have on the tree.
     */
    Map modelElementMap;

    /**
     * the global order for siblings in the tree.
     */
    private Comparator order;

    /**
     * The children currently being updated.
     */
    private Vector updatingChildren = new Vector();

    /**
     * A Runnable object that when executed does update some
     * currently pending nodes.
     */
    ExplorerUpdater nodeUpdater = new ExplorerUpdater();

    /**
     * Help class to semi-lazily update nodes in the tree.
     * This class is thread safe.
     */
    class ExplorerUpdater implements Runnable {
	/**
	 * The set of nodes pending being updated.
	 */
	private LinkedList pendingUpdates = new LinkedList();

	/**
	 * Is this object currently waiting to be run.
	 */
	private boolean hot;

	/** The maximum number of nodes to update in one chunk */
	public static final int MAX_UPDATES_PER_RUN = 100;

	/**
	 * Schedule this object to run on AWT-EventQueue-0 at some later time.
	 */
	private synchronized void schedule() {
	    if (hot)
		return;
	    hot = true;
	    EventQueue.invokeLater(this);
	}

	/**
	 * Schedule updateChildren to be called on node at some later time.
	 * Does nothing if there already is a pending update of node.
	 *
	 * @param node The ExplorerTreeNode to be updated.
	 * @throws NullPointerException If node is null.
	 */
	public synchronized void schedule(ExplorerTreeNode node) {
	    if (node.getPending())
		return;

	    pendingUpdates.add(node);
	    node.setPending(true);
	    schedule();
	}

	/**
	 * Call updateChildren for some pending nodes. Will call at most
	 * MAX_UPDATES_PER_RUN each time. Should there still be pending
	 * updates after that then it will reschedule itself.
	 *
	 * <p>This method should not be called explicitly, instead schedule
	 * should be called and this method will be called automatically.
	 */
	public void run() {
	    boolean done = false;

	    for (int i = 0; i < MAX_UPDATES_PER_RUN; i++) {
		ExplorerTreeNode node = null;
		synchronized (this) {
		    if (!pendingUpdates.isEmpty()) {
			node = (ExplorerTreeNode) pendingUpdates.removeFirst();
			node.setPending(false);
		    } else {
			done = true;
			hot = false;
			break;
		    }
		}

		updateChildren(new TreePath(getPathToRoot(node)));
	    }

	    if (!done)
		schedule();
	}
    };

    /** Creates a new instance of ExplorerTreeModel */
    public ExplorerTreeModel(Object root) {
	super(new DefaultMutableTreeNode());

	setRoot(new ExplorerTreeNode(root, this));
	setAsksAllowsChildren(false);
	modelElementMap = new HashMap();

	ExplorerEventAdaptor.getInstance()
	    .setTreeModelUMLEventListener(this);

	order = new TypeThenNameOrder();
    }
    
    /**
     * a model element has changed in some way.
     */
    public void modelElementChanged(Object node) {
	traverseModified((TreeNode) getRoot(), node);
    }
    
    /**
     * a model element has been added to the model.
     */
    public void modelElementAdded(Object node) {
	traverseModified((TreeNode) getRoot(), node);
    }

    /**
     * Traverses the children, finds those affected by node, and notifies
     * them that they are modified.
     */
    private void traverseModified(TreeNode root, Object node) {
	Enumeration children = root.children();
	while (children.hasMoreElements()) {
	    TreeNode child = (TreeNode) children.nextElement();
	    traverseModified(child, node);
	}

	if (root instanceof ExplorerTreeNode) {
	    ((ExplorerTreeNode) root).nodeModified(node);
	}
    }

    /**
     * a model element has been removed from the model.
     */
    public void modelElementRemoved(Object node) {
        Collection nodes = this.findNodes(node);
        Object[] nodesArray = nodes.toArray();
        
        for (int x = 0; x < nodesArray.length; x++) {
            ExplorerTreeNode changeNode = (ExplorerTreeNode) nodesArray[x];
            
            if (changeNode.getParent() != null) {
                removeNodeFromParent(changeNode);
            }
        }

	traverseModified((TreeNode) getRoot(), node);
    }

    /**
     * the model structure has changed, eg a new project.
     */
    public void structureChanged() {
	// remove references for gc
	if (getRoot() instanceof ExplorerTreeNode)
	    ((ExplorerTreeNode) getRoot()).remove();

	// This should only be helpful for old garbage collectors.
	Collection values = modelElementMap.values();
	Iterator valuesIt = values.iterator();
	while (valuesIt.hasNext()) {
	    ((Collection) valuesIt.next()).clear();
	}
	modelElementMap.clear();

	// This is somewhat inconsistent with the design of the constructor
	// that receives the root object by argument. If this is okay
	// then there may be no need for a constructor with that argument.
	modelElementMap = new HashMap();
	Project proj = ProjectManager.getManager().getCurrentProject();
	ExplorerTreeNode rootNode = new ExplorerTreeNode(proj, this);

	addToMap(proj, rootNode);
	setRoot(rootNode);
    }

    /**
     * updates next level of the explorer tree for a given tree path.
     */
    public void updateChildren(TreePath path) {
	ExplorerTreeNode node = (ExplorerTreeNode) path.getLastPathComponent();
	Vector children = new Vector();
	Vector reordered = new Vector();
	Vector newChildren = new Vector();
        Object modelElement = node.getUserObject();
	Set deps = new HashSet();
	Vector weakNodes = new Vector();
	Vector pendingWeakNodes = new Vector();
	Vector pendingWeakObjects = new Vector();

	// Avoid doing this too early in the initialization process
	if (rules == null)
	    return;

	// Avoid recursively updating the same child
	if (updatingChildren.contains(node))
	    return;
	updatingChildren.add(node);

	// Enumerate the current children of node to find out which now sorts
	// in different order, since these must be moved
	Enumeration enChld = node.children();
	while (enChld.hasMoreElements()) {
	    Object child = enChld.nextElement();
	    if (child instanceof DefaultMutableTreeNode) {
		Object obj = ((DefaultMutableTreeNode) child).getUserObject();
		if (children.size() > 0) {
		    Object obj0 = children.get(children.size() - 1);
		    if (order.compare(obj0, obj) > 0) {
			reordered.add(child);
			// Avoid our deinitialization here
			// The node will be added back to the tree below
			super.removeNodeFromParent((MutableTreeNode) child);
		    } else
			children.add(obj);
		} else {
		    children.add(obj);
		}
	    }
	}

	// For each reordered node, find it's new position among the current
	// children and move it there
        for (int x = 0; x < reordered.size(); x++) {
	    DefaultMutableTreeNode child =
		(DefaultMutableTreeNode) reordered.get(x);
	    Object obj = child.getUserObject();
	    int ip = Collections.binarySearch(children, obj, order);

	    if (ip < 0)
		ip = -(ip + 1);

	    int cidx = node.getIndex(child);

	    // Avoid our initialization here
	    super.insertNodeInto(child, node, ip);
	    children.add(ip, obj);
	}

	// Collect the current set of objects that should be children to
	// this node
        for (int x = 0; x < rules.length; x++) {
            Collection c = ((PerspectiveRule) rules[x])
		    .getChildren(modelElement);
            Set c2 = ((PerspectiveRule) rules[x])
		    .getDependencies(modelElement);

	    if (c != null) {
		Iterator it = c.iterator();
		while (it.hasNext()) {
		    Object obj = it.next();
		    if (obj == null) {
			LOG.warn("PerspectiveRule " + rules[x] + " wanted to "
				 + "add null to the explorer tree!");
		    } else if (!newChildren.contains(obj)) {
			newChildren.add(obj);
		    }
		}
	    }

	    if (c2 != null) {
		deps.addAll(c2);
	    }
        }

	// Order the new children, the dependencies cannot and
	// need not be ordered
	Collections.sort(newChildren, order);
	deps.addAll(newChildren);
	node.setModifySet(deps);

	// Update the children to node by walking through the sorted list of
	// children and the sorted list of children to be (in order to avoid
	// unneccessarily touching tree nodes)
	// Method:
	//  Let cc be 'the first' object in the current list of children
	//  Let nc be 'the first' object in the new list of children
	//  Let r be -1 if cc sorts before nc, 0 if arbitrary order
	//        and 1 if cc sorts after nc
	//  If r < 0 then then cc should be removed
	//  If r = 0 then
	//    Advance both cc and nc through the arbitrary field,
	//    adding and removing as required but no more
	//    Weak node handling makes some other things easy, but here
	//    it becomes a true pain.
	//  If r > 0 then nc is new and we add it to children
	Iterator cChlds = children.iterator();
	Iterator nChlds = newChildren.iterator();
	Object cc = null, nc = null;
	int cldIdx = 0;
	while (true) {
	    int r;

	    if (cc == null && cChlds.hasNext())
		cc = cChlds.next();
	    if (nc == null && nChlds.hasNext())
		nc = nChlds.next();

	    if (cc == null) {
		if (nc == null)
		    break;
		r = 1;
	    } else if (nc == null) {
		r = -1;
	    } else {
		r = order.compare(cc, nc);
	    }

	    /* Always null at least one of cc and nc in every path */
	    if (r == 0 && cc == nc) {
		nc = null;
		cc = null;
		cldIdx++;
	    } else if (r == 0) {
		/* Objects cc and nc sorts arbitrary */
		Object cObj = cc;

		weakNodes.clear();
		pendingWeakObjects.clear();
		pendingWeakNodes.clear();

		do {
		    if (cc instanceof WeakExplorerNode) {
			weakNodes.add(cc);
			if (!newChildren.contains(cc)) {
			    // Note that these Vectors are parallell,
			    // one contains the object and the other the node
			    // at the same index (which is important)
			    pendingWeakNodes.add(getChild(node, cldIdx));
			    pendingWeakObjects.add(cc);
			}
			cldIdx++;
		    } else {
			if (!newChildren.contains(cc)) {
			    removeNodeFromParent(
				    (MutableTreeNode) node.getChildAt(cldIdx));
			} else {
			    cldIdx++;
			}
		    }

		    if (cChlds.hasNext())
			cc = cChlds.next();
		    else
			cc = null;
		} while (cc != null
			 && (nc == null || order.compare(cc, nc) == 0));

		do {
		    if (!children.contains(nc)) {
			boolean doAdd = true;
			if (weakNodes != null
			    && nc instanceof WeakExplorerNode) {
			    for (int x = 0; x < weakNodes.size(); x++) {
				if (((WeakExplorerNode) weakNodes.get(x)).subsumes(nc)) {
				    int i = pendingWeakObjects.indexOf(weakNodes.get(x));
				    doAdd = false;
				    pendingWeakNodes.remove(i);
				    pendingWeakObjects.remove(i);
				    break;
				}
			    }
			}

			if (doAdd) {
			    ExplorerTreeNode newNode =
				    new ExplorerTreeNode(nc, this);
			    insertNodeInto(newNode, node, cldIdx);
			    cldIdx++;
			}
		    }

		    if (nChlds.hasNext())
			nc = nChlds.next();
		    else
			nc = null;
		} while (nc != null
			 && (cObj == null || order.compare(cObj, nc) == 0));

		for (int x = 0; x < pendingWeakNodes.size(); x++) {
		    removeNodeFromParent(
			    (MutableTreeNode) pendingWeakNodes.get(x));
		    cldIdx--;
		}
	    } else if (r < 0) {
		/* cc were smaller, so it is not in nChlds -> remove */
		removeNodeFromParent((MutableTreeNode) node.getChildAt(cldIdx));
		cc = null;
	    } else {
		/* cc were greater, so nc is not in cChlds -> add */
		ExplorerTreeNode newNode = new ExplorerTreeNode(nc, this);
		insertNodeInto(newNode, node, cldIdx);
		nc = null;
		cldIdx++;
	    }
	}
	updatingChildren.remove(node);
    }

    /**
     * Invoked this to insert newChild at location index in parents children.
     * This will then message nodesWereInserted to create the appropriate
     * event. This is the preferred way to add children as it will create the
     * appropriate event.
     *
     * <p>Also performs subclass specific initialization.
     *
     * @param newChild The new child node.
     * @param parent The parent node.
     * @param index The index.
     */
    public void insertNodeInto(MutableTreeNode newChild,
			       MutableTreeNode parent, int index) {
	super.insertNodeInto(newChild, parent, index);

	addNodesToMap(newChild);
    }

    /**
     * Message this to remove node from its parent. This will message
     * nodesWereRemoved to create the appropriate event. This is the
     * preferred way to remove a node as it handles the event creation
     * for you.
     *
     * <p>Also performs subclass specific uninitialization.
     *
     * @param node The node to remove.
     */
    public void removeNodeFromParent(MutableTreeNode node) {
	removeNodesFromMap(node);

	if (node instanceof ExplorerTreeNode) {
	    ((ExplorerTreeNode) node).remove();
	}

	super.removeNodeFromParent(node);
    }

    /** Map all nodes in the subtree rooted at node */
    private void addNodesToMap(TreeNode node) {
	Enumeration children = node.children();
	while (children.hasMoreElements()) {
	    TreeNode child = (TreeNode) children.nextElement();
	    addNodesToMap(child);
	}

	if (node instanceof DefaultMutableTreeNode) {
	    DefaultMutableTreeNode mtn = (DefaultMutableTreeNode) node;
	    addToMap(mtn.getUserObject(), mtn);
	}
    }

    /** Unmap all nodes in the subtree rooted at node */
    private void removeNodesFromMap(TreeNode node) {
	Enumeration children = node.children();
	while (children.hasMoreElements()) {
	    TreeNode child = (TreeNode) children.nextElement();
	    removeNodesFromMap(child);
	}

	if (node instanceof DefaultMutableTreeNode) {
	    DefaultMutableTreeNode mtn = (DefaultMutableTreeNode) node;
	    removeFromMap(mtn.getUserObject(), mtn);
	}
    }

    /**
     * adds a new tree node and model element to the map.
     * nodes are removed from the map when a {@link #modelElementRemoved(Object)
     * modelElementRemoved} event is received.
     */
    private void addToMap(Object modelElement, TreeNode node) {
	Object value = modelElementMap.get(modelElement);

	if (value != null) {
	    ((Set) value).add(node);
	} else {
	    Set nodes = new HashSet();
	    nodes.add(node);
	    modelElementMap.put(modelElement, nodes);
	}
    }

    /**
     * removes a new tree node and model element from the map.
     */
    private void removeFromMap(Object modelElement, TreeNode node) {
	Object value = modelElementMap.get(modelElement);

	if (value != null) {
	    Set nodeset = (Set) value;
	    nodeset.remove(node);
	    if (nodeset.isEmpty())
		modelElementMap.remove(modelElement);
	}
    }

    /**
     * node lookup for a given model element.
     */
    private Collection findNodes(Object modelElement) {
	Collection nodes = (Collection) modelElementMap.get(modelElement);
        
	if (nodes == null) {
	    return Collections.EMPTY_LIST;
	} else {
	    return nodes;
	}
    }

    /**
     * Updates the explorer for new perspectives / orderings.
     */
    public void itemStateChanged(ItemEvent e) {
	if (e.getSource() instanceof PerspectiveComboBox) {
            rules = ((ExplorerPerspective) e.getItem()).getRulesArray();
	} else {
	    order = (Comparator) e.getItem();
	}

	structureChanged();
    }
}

