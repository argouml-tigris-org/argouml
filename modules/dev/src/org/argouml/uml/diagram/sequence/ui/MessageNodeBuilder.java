package org.argouml.uml.diagram.sequence.ui;

import javax.swing.tree.DefaultMutableTreeNode;
import org.argouml.uml.diagram.sequence.MessageNode;

public class MessageNodeBuilder {

    public static void addNodeTree(
            DefaultMutableTreeNode treeNode,
            FigClassifierRole fcr) {
        int nodeCount = fcr.getNodeCount();
        for (int i=0; i < nodeCount; ++i) {
            MessageNode mn = fcr.getNode(i);
            String descr = "MessageNode y=" + fcr.getYCoordinate(mn);
            FigMessagePort fmp = mn.getFigMessagePort();
            if (fmp != null) {
                descr += " FigMessagePort registered";
            }
            DefaultMutableTreeNode tn = new DefaultMutableTreeNode(descr);
            treeNode.add(tn);
        }
    }
}
