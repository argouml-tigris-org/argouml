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

	// Avoid doing this too early in the initialization process
	if (rules == null)
	    return;

	// Avoid recursively updating the same child
	if (updatingChildren.contains(node))
	    return;
	updatingChildren.add(node);

	Enumeration enChld = node.children();
	while (enChld.hasMoreElements()) {
	    Object child = enChld.nextElement();
	    if (child instanceof DefaultMutableTreeNode) {
		Object obj = ((DefaultMutableTreeNode) child).getUserObject();
		if (children.size() > 0) {
		    Object obj0 = children.get(children.size() - 1);
		    if (order.compare(obj0, obj) > 0)
			reordered.add(child);
		    else
			children.add(obj);
		} else {
		    children.add(obj);
		}
	    }
	}

        for (int x = 0; x < reordered.size(); x++) {
	    DefaultMutableTreeNode child = (DefaultMutableTreeNode) reordered.get(x);
	    Object obj = child.getUserObject();
	    int ip = Collections.binarySearch(children, obj, order);

	    if (ip < 0)
		ip = -(ip + 1);

	    int cidx = node.getIndex(child);

	    // Avoid our initialization/deinitialization here
	    super.removeNodeFromParent(child);
	    super.insertNodeInto(child, node, ip);
	    children.add(ip, obj);
	}

        for (int x = 0; x < rules.length; x++) {
            Collection c = ((PerspectiveRule) rules[x])
		    .getChildren(modelElement);
            Set c2 = ((PerspectiveRule) rules[x])
		    .getDependencies(modelElement);

	    if (c != null) {
		Iterator it = c.iterator();
		while (it.hasNext()) {
		    Object obj = it.next();
		    if (!newChildren.contains(obj))
			newChildren.add(obj);
		}
	    }
	    if (c2 != null) {
		deps.addAll(c2);
	    }
        }
	Collections.sort(newChildren, order);
	deps.addAll(newChildren);
	node.setModifySet(deps);

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
	    if (r == 0) {
		/* Objects cc and nc sorts arbitrary */
		cldIdx++;
		if (cc == nc)
		    nc = null;
		cc = null; /* postspone adding nc */
	    } else if (r < 0) {
		/* cc were smaller, so it is not in nChlds -> remove */
		removeNodeFromParent((MutableTreeNode) node.getChildAt(cldIdx));
		cc = null;
	    } else if (!children.contains(nc)) {
		/* cc were greater, so nc is not in cChlds -> add */
		ExplorerTreeNode newNode = new ExplorerTreeNode(nc, this);
		insertNodeInto(newNode, node, cldIdx);
		nc = null;
		cldIdx++;
	    } else {
		nc = null;
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
    public void insertNodeInto(MutableTreeNode newChild, MutableTreeNode parent, int index) {
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

