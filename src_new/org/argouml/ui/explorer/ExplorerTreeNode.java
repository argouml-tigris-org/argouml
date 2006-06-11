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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.argouml.uml.diagram.ui.UMLDiagram;

/**
 * Ensures that explorer tree nodes have a default ordering.
 *
 * @author  alexb
 * @since 0.15.2, Created on 27 September 2003, 17:40
 */
public class ExplorerTreeNode extends DefaultMutableTreeNode implements
        PropertyChangeListener {

    private static final long serialVersionUID = -6766504350537675845L;
    private ExplorerTreeModel model;
    private boolean expanded;
    private boolean pending;
    private Set modifySet = Collections.EMPTY_SET;

    /**
     * Creates a new instance of ExplorerTreeNode.
     *
     * @param userObj the object in the tree
     * @param m the tree model
     */
    public ExplorerTreeNode(Object userObj, ExplorerTreeModel m) {
        super(userObj);
        this.model = m;
        if (userObj instanceof UMLDiagram)
            ((UMLDiagram) userObj).addPropertyChangeListener(this);
    }

    /**
     * @see javax.swing.tree.TreeNode#isLeaf()
     */
    public boolean isLeaf() {
	if (!expanded) {
	    model.updateChildren(new TreePath(model.getPathToRoot(this)));
	    expanded = true;
	}
	return super.isLeaf();
    }

    boolean getPending() {
	return pending;
    }

    void setPending(boolean value) {
	pending = value;
    }

    /**
     * @param set the given set
     */
    public void setModifySet(Set set) {
	if (set == null || set.size() == 0)
	    modifySet = Collections.EMPTY_SET;
	else
	    modifySet = set;
    }

    /**
     * @param node the modified node in the tree
     */
    public void nodeModified(Object node) {
	if (modifySet.contains(node))
	    model.getNodeUpdater().schedule(this);
	if (node == getUserObject())
	    model.nodeChanged(this);
    }

    /**
     * cleans up for gc.
     */
    public void remove() {
	this.userObject = null;

	if (children != null) {
	    Iterator childrenIt = children.iterator();
	    while (childrenIt.hasNext()) {
		((ExplorerTreeNode) childrenIt.next()).remove();
	    }

	    children.clear();
	    children = null;
	}
    }
    
    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        // Name of the UMLDiagram represented by this node has changed.
        if (evt.getSource() instanceof UMLDiagram
                && "name".equals(evt.getPropertyName())) {
            model.nodeChanged(this);
        }
    }
}
