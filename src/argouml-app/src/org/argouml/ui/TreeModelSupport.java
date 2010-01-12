/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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

package org.argouml.ui;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

/**
 * Helper class for tree models that provides tree event handling.<p>
 *
 * @author  alexb
 * @since 0.13.5, Created on 15 April 2003
 */
public class TreeModelSupport extends PerspectiveSupport {

    /** tree model listener list. */
    private EventListenerList listenerList = new EventListenerList();

    /**
     * The constructor.
     *
     * @param name the name that will be localized
     */
    public TreeModelSupport(String name) {
        super(name);
    }

    // ---------------- listener management ----------------

    /**
     * Add a TreeModelListener to the list of listeners.
     *
     * @param l the listener to be added
     */
    public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(TreeModelListener.class, l);
    }

    /**
     * Remove a TreeModelListener from the list of listeners..
     *
     * @param l the listener to be removed
     */
    public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(TreeModelListener.class, l);
    }

    // --------------- tree nodes -------------------------

    /**
     * Notify all listeners that a node (or a set of siblings) has changed in
     * some way. The node(s) have not changed locations in the tree or altered
     * their children arrays, but other attributes have changed and may affect
     * presentation. 
     * <p>
     * To indicate the root has changed, childIndices and children will be null.
     * <p>
     * <em>NOTE:</em> This is a Swing method which must be invoked on the
     * Swing/AWT event thread.
     * 
     * @param source the Object responsible for generating the event (typically
     *                the creator of the event object passes this for its value)
     * @param path an array of Object identifying the path to the parent of the
     *                modified item(s), where the first element of the array is
     *                the Object stored at the root node and the last element is
     *                the Object stored at the parent node
     * @param childIndices an array of int that specifies the index values of
     *                the removed items. The indices must be in sorted order,
     *                from lowest to highest
     * @param children an array of Object containing the inserted, removed, or
     *                changed objects
     * @see TreeModelListener#treeNodesChanged(TreeModelEvent)
     */
    protected void fireTreeNodesChanged(
					final Object source,
					final Object[] path,
					final int[] childIndices,
					final Object[] children) {

        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e =
                        new TreeModelEvent(
					   source,
					   path,
					   childIndices,
					   children);
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
            }
        }
    }

    /**
     * Notify all listeners a node has been inserted.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * <p>
     * <em>NOTE:</em> This is a Swing method which must be invoked on the
     * Swing/AWT event thread.
     * 
     * @param source the Object responsible for generating the event (typically
     *                the creator of the event object passes this for its value)
     * @param path an array of Object identifying the path to the parent of the
     *                modified item(s), where the first element of the array is
     *                the Object stored at the root node and the last element is
     *                the Object stored at the parent node
     * @param childIndices an array of int that specifies the index values of
     *                the removed items. The indices must be in sorted order,
     *                from lowest to highest
     * @param children an array of Object containing the inserted, removed, or
     *                changed objects
     * @see TreeModelListener#treeNodesChanged(TreeModelEvent)
     */
    protected void fireTreeNodesInserted(
					 Object source,
					 Object[] path,
					 int[] childIndices,
					 Object[] children) {

        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e =
                        new TreeModelEvent(
					   source,
					   path,
					   childIndices,
					   children);
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
            }
        }
    }

    /**
     * Notify all listeners that nodes have been removed from the tree. Note
     * that if a subtree is removed from the tree, this method may only be
     * invoked once for the root of the removed subtree, not once for each
     * individual set of siblings removed.
     * <p>
     * <em>NOTE:</em> This is a Swing method which must be invoked on the
     * Swing/AWT event thread.
     * 
     * @param source the Object responsible for generating the event (typically
     *                the creator of the event object passes this for its value)
     * @param path an array of Object identifying the path to the parent of the
     *                modified item(s), where the first element of the array is
     *                the Object stored at the root node and the last element is
     *                the Object stored at the parent node
     * @param childIndices an array of int that specifies the index values of
     *                the removed items. The indices must be in sorted order,
     *                from lowest to highest
     * @param children an array of Object containing the inserted, removed, or
     *                changed objects
     * @see TreeModelListener#treeNodesChanged(TreeModelEvent)
     * @see EventListenerList
     */
    protected void fireTreeNodesRemoved(
					Object source,
					Object[] path,
					int[] childIndices,
					Object[] children) {

        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e =
                        new TreeModelEvent(
					   source,
					   path,
					   childIndices,
					   children);
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
            }
        }
    }

    // ------------- tree structure -----------------

    /**
     * Notify all listeners that the tree has drastically changed
     * structure from a given node down. If the path returned by e.getPath() is
     * of length one and the first element does not identify the current root
     * node the first element should become the new root of the tree.
     * <p>
     * <em>NOTE:</em> This is a Swing method which must be invoked on the 
     * Swing/AWT event thread.
     * 
     * @param path an array of Object identifying the path to the parent of the
     *                modified item(s), where the first element of the array is
     *                the Object stored at the root node and the last element is
     *                the Object stored at the parent node
     * @see TreeModelListener#treeStructureChanged(TreeModelEvent)
     */
    protected void fireTreeStructureChanged(Object[] path) {
        fireTreeStructureChanged(this, path);
    }

    /**
     * Notify all listeners that the tree has drastically changed
     * structure from a given node down. If the path returned by e.getPath() is
     * of length one and the first element does not identify the current root
     * node the first element should become the new root of the tree.
     * <p>
     * <em>NOTE:</em> This is a Swing method which must be invoked on the 
     * Swing/AWT event thread.
     * 
     * @param source the Object responsible for generating the event (typically
     *                the creator of the event object passes this for its value)
     * @param path an array of Object identifying the path to the parent of the
     *                modified item(s), where the first element of the array is
     *                the Object stored at the root node and the last element is
     *                the Object stored at the parent node
     * @see TreeModelListener#treeStructureChanged(TreeModelEvent)
     */
    protected void fireTreeStructureChanged(Object source, Object[] path) {
        fireTreeStructureChanged(source, path, null, null);
    }

    /**
     * Notify all listeners that the tree has drastically changed structure from
     * a given node down. If the path returned by e.getPath() is of length one
     * and the first element does not identify the current root node the first
     * element should become the new root of the tree.
     * <p>
     * <em>NOTE:</em> This is a Swing method which must be invoked on the
     * Swing/AWT event thread.
     * 
     * @param source the Object responsible for generating the event (typically
     *                the creator of the event object passes this for its value)
     * @param path an array of Object identifying the path to the parent of the
     *                modified item(s), where the first element of the array is
     *                the Object stored at the root node and the last element is
     *                the Object stored at the parent node
     * @param childIndices an array of int that specifies the index values of
     *                the removed items. The indices must be in sorted order,
     *                from lowest to highest
     * @param children an array of Object containing the inserted, removed, or
     *                changed objects
     * @see TreeModelListener#treeStructureChanged(TreeModelEvent)
     */
    public void fireTreeStructureChanged(
					 Object source,
					 Object[] path,
					 int[] childIndices,
					 Object[] children) {

        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e =
                        new TreeModelEvent(
					   source,
					   path,
					   childIndices,
					   children);
                }
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }

}
