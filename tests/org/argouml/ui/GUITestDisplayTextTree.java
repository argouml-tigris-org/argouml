// Copyright (c) 1996-2003 The Regents of the University of California. All
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

// $Id$
package org.argouml.ui;

import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import junit.framework.TestCase;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.model.uml.modelmanagement.ModelManagementFactory;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;

/**
 * @author jaap.branderhorst@xs4all.nl
 * Jul 27, 2003
 */
public class GUITestDisplayTextTree extends TestCase {    

    /**
     * Model with a classdiagram as root and classes as children
     */
    TreeModel _classdiagramModel;

    /**
     * Model with a package as root and different modelelements as children
     */
    TreeModel _packageModel;

    /**
     * @param arg0
     */
    public GUITestDisplayTextTree(String arg0) {
        super(arg0);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ArgoSecurityManager.getInstance().setAllowExit(true);
        UmlFactory.getFactory().setGuiEnabled(false);
        Vector children = new Vector();
        for (int i = 0; i < 10; i++) {
            children.add(CoreFactory.getFactory().createClass());
        }
        TreeNode root =
            new JTree.DynamicUtilTreeNode(new UMLClassDiagram(), children);
        _classdiagramModel = new DefaultTreeModel(root);
        Object pack = ModelManagementFactory.getFactory().createPackage();
        for (int i = 0; i < children.size(); i++) {
            ModelFacade.addOwnedElement(pack, children.get(i));
        }
        TreeNode root2 = new JTree.DynamicUtilTreeNode(pack, children);
        _packageModel = new DefaultTreeModel(root2);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testTargetSet() {
        DisplayTextTree pane = new DisplayTextTree();
        pane.setModel(_classdiagramModel);
        Object target = new Object();
        TargetEvent e =
            new TargetEvent(
                this,
                TargetEvent.TARGET_SET,
                new Object[] { null },
                new Object[] { target });
        pane.targetSet(e);
        // no nodes should be selected
        assertEquals(0, pane.getSelectionCount());
        target = ((JTree.DynamicUtilTreeNode) _classdiagramModel.getRoot()).getUserObject();
        e =
            new TargetEvent(
                this,
                TargetEvent.TARGET_SET,
                new Object[] { null },
                new Object[] { target });
        pane.targetSet(e);
        // new target is of type UMLClassDiagram and parent of a number of children. Both children as diagram should be selected
        assertEquals(
            _classdiagramModel.getChildCount(_classdiagramModel.getRoot()) + 1,
            pane.getSelectionCount());
        target = _classdiagramModel.getChild(_classdiagramModel.getRoot(), 0);
        e =
            new TargetEvent(
                this,
                TargetEvent.TARGET_SET,
                new Object[] { null },
                new Object[] { target });
        pane.targetSet(e);
        // new target represents one node
        assertEquals(1, pane.getSelectionCount());
        e =
            new TargetEvent(
                this,
                TargetEvent.TARGET_SET,
                new Object[] { target },
                new Object[] { null });
        pane.targetSet(e);
        // new target is null, nothing should be selected
        assertEquals(0, pane.getSelectionCount());
        Object target2 =
            _classdiagramModel.getChild(_classdiagramModel.getRoot(), 3);
        e =
            new TargetEvent(
                this,
                TargetEvent.TARGET_SET,
                new Object[] { target, target2 },
                new Object[] { null });
        pane.targetSet(e);
        // new target represents two nodes
        assertEquals(2, pane.getSelectionCount());
    }

}
