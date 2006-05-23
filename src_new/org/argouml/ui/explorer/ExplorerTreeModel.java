// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.InvalidElementException;
import org.argouml.ui.explorer.rules.PerspectiveRule;

/**
 * The model for the Explorer tree view of the uml model.
 *
 * provides:
 *  - receives events from the uml model and updates itself and the tree ui.
 *  - responds to changes in perspective and ordering.
 *
 * @author  alexb
 * @since 0.15.2
 */
public class ExplorerTreeModel extends DefaultTreeModel
		implements TreeModelUMLEventListener, ItemListener {
    /**
     * Logger.
     */
    private static final Logger LOG =
	Logger.getLogger(ExplorerTreeModel.class);

    /**
     * an array of
     * {@link org.argouml.ui.explorer.rules.PerspectiveRule PerspectiveRules},
     * that determine the tree view.
     */
    private Object[] rules;

    /**
     * a map used to resolve model elements to tree nodes when determining
     * what effect a model event will have on the tree.
     */
    private Map modelElementMap;

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
    private ExplorerUpdater nodeUpdater = new ExplorerUpdater();

    private ExplorerTree tree;

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

	/**
         * The maximum number of nodes to update in one chunk.
         */
	public static final int MAX_UPDATES_PER_RUN = 100;

	/**
	 * Schedule this object to run on AWT-EventQueue-0 at some later time.
	 */
	private synchronized void schedule() {
	    if (hot) {
                return;
            }
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
	    if (node.getPending()) {
                return;
            }

	    pendingUpdates.add(node);
	    node.setPending(true);
	    schedule();
	}

	/**
	 * Call updateChildren for some pending nodes. Will call at most
	 * MAX_UPDATES_PER_RUN each time. Should there still be pending
	 * updates after that then it will reschedule itself.<p>
	 *
	 * This method should not be called explicitly, instead schedule
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

	    if (!done) {
		schedule();
            } else {
                /* This solves issue 2287. */
                tree.refreshSelection();
            }
	}
    }

    /**
     * The constructor of ExplorerTreeModel.
     *
     * @param root an object to place at the root
     * @param myTree the tree
     */
    public ExplorerTreeModel(Object root, ExplorerTree myTree) {
	super(new DefaultMutableTreeNode());

        tree = myTree;
	setRoot(new ExplorerTreeNode(root, this));
	setAsksAllowsChildren(false);
	modelElementMap = new HashMap();

	ExplorerEventAdaptor.getInstance()
	    .setTreeModelUMLEventListener(this);

	order = new TypeThenNameOrder();
    }

    /**
     * a model element has changed in some way.
     *
     * @see org.argouml.ui.explorer.TreeModelUMLEventListener#modelElementChanged(java.lang.Object)
     */
    public void modelElementChanged(Object node) {
        traverseModified((TreeNode) getRoot(), node);
    }

    /**
     * a model element has been added to the model.
     *
     * @see org.argouml.ui.explorer.TreeModelUMLEventListener#modelElementAdded(java.lang.Object)
     */
    public void modelElementAdded(Object node) {
        traverseModified((TreeNode) getRoot(), node);
    }

    /**
     * Traverses the children, finds those affected by node, and notifies
     * them that they are modified.
     */
    private void traverseModified(TreeNode start, Object node) {
	Enumeration children = start.children();
	while (children.hasMoreElements()) {
	    TreeNode child = (TreeNode) children.nextElement();
	    traverseModified(child, node);
	}

	if (start instanceof ExplorerTreeNode) {
	    ((ExplorerTreeNode) start).nodeModified(node);
	}
    }

    /**
     * a model element has been removed from the model.
     *
     * @see org.argouml.ui.explorer.TreeModelUMLEventListener#modelElementRemoved(java.lang.Object)
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
	if (getRoot() instanceof ExplorerTreeNode) {
            ((ExplorerTreeNode) getRoot()).remove();
        }

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
     *
     * @param path the path to the node whose children to update.
     * @throws IllegalArgumentException if node has a child that is not a
     *         (descendant of) DefaultMutableTreeNode.
     */
    public void updateChildren(TreePath path) {
	ExplorerTreeNode node = (ExplorerTreeNode) path.getLastPathComponent();
        Object modelElement = node.getUserObject();

	// Avoid doing this too early in the initialization process
	if (rules == null) {
	    return;
	}

	// Avoid recursively updating the same child
	if (updatingChildren.contains(node)) {
	    return;
	}
	updatingChildren.add(node);

	Vector children = reorderChildren(node);

	Vector newChildren = new Vector();
	Set deps = new HashSet();
	collectChildren(modelElement, newChildren, deps);

	node.setModifySet(deps);

	mergeChildren(node, children, newChildren);

	updatingChildren.remove(node);
    }

    /**
     * Sorts the child nodes of node using the current ordering.<p>
     *
     * Note: UserObject is only available from descendants of
     * DefaultMutableTreeNode, so any other children couldn't be sorted.
     * Thus these are currently forbidden. But currently no such node is
     * ever inserted into the tree.
     *
     * @param node the node whose children to sort
     * @return the UserObjects of the children, in the same order as the
     *         children.
     * @throws IllegalArgumentException if node has a child that is not a
     *         (descendant of) DefaultMutableTreeNode.
     */
    private Vector reorderChildren(ExplorerTreeNode node) {
	Vector children = new Vector();
	Vector reordered = new Vector();

	// Enumerate the current children of node to find out which now sorts
	// in different order, since these must be moved
	Enumeration enChld = node.children();
	Object lastObj = null;
	while (enChld.hasMoreElements()) {
	    Object child = enChld.nextElement();
	    if (child instanceof DefaultMutableTreeNode) {
		Object obj = ((DefaultMutableTreeNode) child).getUserObject();
		if (lastObj != null && order.compare(lastObj, obj) > 0) {
		    /*
		     * If a node to be moved is currently selected,
		     * move its predecessors instead so don't lose selection.
		     * This fixes issue 3249.
		     * NOTE: this does not deal with the case where
                     * multiple nodes are selected and they are out
                     * of order with respect to each other, but I
                     * don't think more than one node is ever reordered
		     * at a time - tfm
		     */
		    if (!tree.isPathSelected(new TreePath(
                            getPathToRoot((DefaultMutableTreeNode) child)))) {
			reordered.add(child);
		    } else {
			DefaultMutableTreeNode prev =
                                ((DefaultMutableTreeNode) child)
                                        .getPreviousSibling();
			while (prev != null
                                && (order.compare(prev.getUserObject(), obj)
                                        >= 0)) {
			    reordered.add(prev);
			    children.removeElementAt(children.size() - 1);
			    prev = prev.getPreviousSibling();
			}
			children.add(obj);
			lastObj = obj;
		    }
		} else {
		    children.add(obj);
		    lastObj = obj;
		}
	    } else {
		throw new IllegalArgumentException(
			"Incomprehencible child node " + child.toString());
	    }
	}

        for (int x = 0; x < reordered.size(); x++) {
	    DefaultMutableTreeNode child =
		(DefaultMutableTreeNode) reordered.get(x);

	    // Avoid our deinitialization here
	    // The node will be added back to the tree again
	    super.removeNodeFromParent(child);
	}

	// For each reordered node, find it's new position among the current
	// children and move it there
        for (int x = 0; x < reordered.size(); x++) {
	    DefaultMutableTreeNode child =
		(DefaultMutableTreeNode) reordered.get(x);
	    Object obj = child.getUserObject();
	    int ip = Collections.binarySearch(children, obj, order);

	    if (ip < 0) {
		ip = -(ip + 1);
	    }

	    // Avoid our initialization here
	    super.insertNodeInto(child, node, ip);
	    children.add(ip, obj);
	}

	return children;
    }

    /**
     * Collects the set of children modelElement should have at this point in
     * time. The children are added to newChildren.<p>
     *
     * Note: Both newChildren and deps are modified by this function, it
     * is in fact it's primary purpose to modify these collections. It is your
     * responsibility to make sure that they are empty when it is called, or
     * to know what you are doing if they are not.
     *
     * @param modelElement the element to collect children for.
     * @param newChildren the new children of modelElement.
     * @param deps the set of objects that should be monitored for changes
     *        since these could affect this list.
     * @throws UnsupportedOperationException if add is not supported by
     *         newChildren or addAll isn't supported by deps.
     * @throws NullPointerException if newChildren or deps is null.
     * @throws ClassCastException if newChildren or deps rejects some element.
     * @throws IllegalArgumentException if newChildren or deps rejects some
     *         element.
     */
    private void collectChildren(Object modelElement, List newChildren,
				 Set deps) {
	if (modelElement == null) {
	    return;
	}

	// Collect the current set of objects that should be children to
	// this node
        for (int x = 0; x < rules.length; x++) {
            Collection c = null;
            Set c2 = null;
            
            // TODO: A better implementation would be to batch events into
            // logical groups and update the tree one time for the entire
            // group, synchronizing access to the model repository so that
            // it stays consistent during the query.  This would likely
            // require doing the updates in a different thread than the
            // event delivery thread to prevent deadlocks, so for right now
            // we protect ourselves with try/catch blocks.

            try {
                c =
                    ((PerspectiveRule) rules[x])
                        .getChildren(modelElement);
            } catch (InvalidElementException e) {
                LOG.debug("InvalidElementException in ExplorerTree : " 
                        + e.getStackTrace());
            }
            try {
                c2 =
                    ((PerspectiveRule) rules[x])
                        .getDependencies(modelElement);
            } catch (InvalidElementException e) {
                LOG.debug("InvalidElementException in ExplorerTree : " 
                        + e.getStackTrace());
            }
            
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
    }

    /**
     * Returns a Set of current children to remove and modifies newChildren
     * to only contain the children not already in children and not subsumed
     * by any WeakExplorerNode in children.<p>
     *
     * Note: newChildren will be modified by this call.<p>
     *
     * Note: It is expected that a WeakExplorerNode will not be reused and
     * thus they will always initially be slated for removal, and only those
     * nodes are in fact used to check subsumption of new nodes. New nodes
     * are not checked among themselves for subsumtion.
     *
     * @param children is the list of current children.
     * @param newChildren is the list of expected children.
     * @return the Set of current children to remove.
     * @throws UnsupportedOperationException if newChildren doesn't support
     *         remove or removeAll.
     * @throws NullPointerException if either argument is null.
     */
    private Set prepareAddRemoveSets(List children, List newChildren) {
	Set removeSet = new HashSet();
	Set commonObjects = new HashSet();
	if (children.size() < newChildren.size()) {
	    commonObjects.addAll(children);
	    commonObjects.retainAll(newChildren);
	} else {
	    commonObjects.addAll(newChildren);
	    commonObjects.retainAll(children);
	}
	newChildren.removeAll(commonObjects);
	removeSet.addAll(children);
	removeSet.removeAll(commonObjects);

	// Handle WeakExplorerNodes
	Iterator it = removeSet.iterator();
	List weakNodes = null;
	while (it.hasNext()) {
	    Object obj = it.next();
	    if (!(obj instanceof WeakExplorerNode)) {
		continue;
	    }
	    WeakExplorerNode node = (WeakExplorerNode) obj;

	    if (weakNodes == null) {
		weakNodes = new LinkedList();
		Iterator it2 = newChildren.iterator();
		while (it2.hasNext()) {
		    Object obj2 = it2.next();
		    if (obj2 instanceof WeakExplorerNode) {
			weakNodes.add(obj2);
		    }
		}
	    }

	    Iterator it3 = weakNodes.iterator();
	    while (it3.hasNext()) {
		Object obj3 = it3.next();
		if (node.subsumes(obj3)) {
		    // Remove the node from removeSet
		    it.remove();
		    // Remove obj3 from weakNodes and newChildren
		    newChildren.remove(obj3);
		    it3.remove();
		    break;
		}
	    }
	}

	return removeSet;
    }

    /**
     * Merges the current children with the new children removing children no
     * longer present and adding new children in the right place.
     *
     * @param node the TreeNode were merging lists for.
     * @param children the current child UserObjects, in order.
     * @param newChildren the expected child UserObjects, in order.
     * @throws UnsupportedOperationException if the Iterator returned by
     *         newChildren doesn't support the remove operation, or if
     *         newChildren itself doesn't support remove or removeAll.
     * @throws NullPointerException if node, children or newChildren are null.
     */
    private void mergeChildren(ExplorerTreeNode node, List children,
			       List newChildren) {
	Set removeObjects = prepareAddRemoveSets(children, newChildren);
	// Remember that children are not TreeNodes but UserObjects
	Vector actualNodes = new Vector();
	Enumeration childrenEnum = node.children();
	while (childrenEnum.hasMoreElements()) {
	    actualNodes.add(childrenEnum.nextElement());
	}

	int position = 0;
	Iterator childNodes = actualNodes.iterator();
	Iterator newNodes = newChildren.iterator();
	Object firstNew = newNodes.hasNext() ? newNodes.next() : null;
	while (childNodes.hasNext()) {
	    Object childObj = childNodes.next();
	    if (!(childObj instanceof DefaultMutableTreeNode)) {
		continue;
	    }

	    DefaultMutableTreeNode child = (DefaultMutableTreeNode) childObj;
	    Object userObject = child.getUserObject();

	    if (removeObjects.contains(userObject)) {
		removeNodeFromParent(child);
	    } else {
		while (firstNew != null
		       && order.compare(firstNew, userObject) < 0) {
		    insertNodeInto(new ExplorerTreeNode(firstNew, this),
				   node,
				   position);
		    position++;
		    firstNew = newNodes.hasNext() ? newNodes.next() : null;
		}
		position++;
	    }
	}

	// Add any remaining nodes
	while (firstNew != null) {
	    insertNodeInto(new ExplorerTreeNode(firstNew, this),
			   node,
			   position);
	    position++;
	    firstNew = newNodes.hasNext() ? newNodes.next() : null;
	}
    }

    /**
     * Invoked this to insert newChild at location index in parents children.
     * This will then message nodesWereInserted to create the appropriate
     * event. This is the preferred way to add children as it will create the
     * appropriate event.<p>
     *
     * Also performs subclass specific initialization.
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
     * for you.<p>
     *
     * Also performs subclass specific uninitialization.
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

    /**
     * Map all nodes in the subtree rooted at node.
     */
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

    /**
     * Unmap all nodes in the subtree rooted at node.
     */
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
	    if (nodeset.isEmpty()) {
                modelElementMap.remove(modelElement);
            }
	}
    }

    /**
     * node lookup for a given model element.
     */
    private Collection findNodes(Object modelElement) {
	Collection nodes = (Collection) modelElementMap.get(modelElement);

	if (nodes == null) {
	    return Collections.EMPTY_LIST;
	}
        return nodes;
    }

    /**
     * Updates the explorer for new perspectives / orderings.
     *
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent e) {
	if (e.getSource() instanceof PerspectiveComboBox) {
            rules = ((ExplorerPerspective) e.getItem()).getRulesArray();
	} else { // it is the combo for "order"
	    order = (Comparator) e.getItem();
	}
	structureChanged();
	tree.expandPath(tree.getPathForRow(0));
    }

    /**
     * @return Returns the nodeUpdater.
     */
    ExplorerUpdater getNodeUpdater() {
        return nodeUpdater;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 3132732494386565870L;
}

