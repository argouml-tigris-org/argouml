package org.argouml.dev.figinspector;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class FigTree extends JTree {
    
    private static final long serialVersionUID = -1592265302177199132L;

    public FigTree(DefaultMutableTreeNode fn) {
        super(fn);
    }
    
    public void expandAll() {
        Enumeration e = ((DefaultMutableTreeNode)getModel().getRoot()).depthFirstEnumeration();
        for (; e.hasMoreElements();) {  
            TreePath t = new TreePath(((DefaultMutableTreeNode)e.nextElement()).getPath());
            setExpandedState(t, true);
        }
    }
}
