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

// Copyright (c) 2006-2007 The Regents of the University of California. All
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

package org.argouml.dev;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.tigris.gef.event.ModeChangeEvent;
import org.tigris.gef.event.ModeChangeListener;

/**
 * The event pump inspector displays the contents of the event
 * pump. There is no event mechanism to refresh this panel. The
 * user must press the refresh button to see the latest state.
 * @author Bob Tarling
 */
public final class ModeInspectorPanel extends JPanel implements ModeChangeListener {

    /**
     * The instance.
     */
    private static final ModeInspectorPanel INSTANCE =
        new ModeInspectorPanel();

    /**
     * @return The instance.
     */
    public static ModeInspectorPanel getInstance() {
        return INSTANCE;
    }

    private JScrollPane scroller;
    
    /**
     * Constructor.
     */
    private ModeInspectorPanel() {
        setLayout(new BorderLayout());
    }
    
    /**
     * Getter provided for the dev module. This supplies a tree
     * model of registered listeners.
     * @return a root tree node
     */
    TreeNode getTree(List modes) {
        DefaultMutableTreeNode root =
            new DefaultMutableTreeNode("Mode List");
        addSubtree(root, modes);
        return root;
    }
    
    private void addSubtree(DefaultMutableTreeNode root, List list) {
        DefaultMutableTreeNode node = null;
        for (Object o : list) {
            node = new DefaultMutableTreeNode(o.toString());
            root.add(node);
        }
    }

    public void modeChange(ModeChangeEvent mce) {
        JTree tree = new JTree(getTree(mce.getModes()));
        tree.expandRow(0);
        if (scroller != null) {
            remove(scroller);
        }
        scroller = new JScrollPane(tree);
        add(scroller);
        invalidate();
        validate();
    }

}
