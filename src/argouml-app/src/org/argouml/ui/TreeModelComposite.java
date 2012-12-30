/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
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

package org.argouml.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * This class is the TreeModel for the navigator and todo list panels.<p>
 *
 * It is called <strong>Composite</strong> because there are a set of rules
 * that determine how to link parents to children in the tree. Those
 * rules can now be found in PerspectiveSupport.<p>
 */
public class TreeModelComposite extends TreeModelSupport implements TreeModel {

    private static final Logger LOG =
        Logger.getLogger(TreeModelComposite.class.getName());

    /** The root of the model. */
    private Object root;

    /**
     * The constructor.
     *
     * @param name the name that will be localized
     */
    public TreeModelComposite(String name) {
        super(name);
    }


    /*
     * @see javax.swing.tree.TreeModel#getRoot()
     */
    public Object getRoot() {
        return root;
    }

    /*
     * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
     */
    public Object getChild(Object parent, int index) {
        for (TreeModel tm : getGoRuleList()) {
            int childCount = tm.getChildCount(parent);
            if (index < childCount) {
                return tm.getChild(parent, index);
            }
            index -= childCount;
        }
        return null;
    }

    /*
     * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
     */
    public int getChildCount(Object parent) {
        int childCount = 0;
        for (TreeModel tm : getGoRuleList()) {
            childCount += tm.getChildCount(parent);
        }
        return childCount;
    }

    /*
     * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object,
     * java.lang.Object)
     */
    public int getIndexOfChild(Object parent, Object child) {
        int childCount = 0;
        for (TreeModel tm : getGoRuleList()) {
            int childIndex = tm.getIndexOfChild(parent, child);
            if (childIndex != -1) {
                return childIndex + childCount;
            }
            childCount += tm.getChildCount(parent);
        }
        LOG.log(Level.FINE, "child not found!");

        //The child is sometimes not found when the tree is being updated
        return -1;
    }

    /*
     * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
     */
    public boolean isLeaf(Object node) {
        for (TreeModel tm : getGoRuleList()) {
            if (!tm.isLeaf(node)) {
                return false;
            }
        }
        return true;
    }


    /*
     * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
     */
    public void valueForPathChanged(TreePath path, Object newValue) {
        //  Empty implementation - not used.
    }


    /**
     * @param r the root of the model
     */
    public void setRoot(Object r) {
        root = r;
    }

}
