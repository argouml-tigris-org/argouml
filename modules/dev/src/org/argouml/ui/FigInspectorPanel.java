package org.argouml.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.tigris.gef.base.Globals;
import org.tigris.gef.event.GraphSelectionEvent;
import org.tigris.gef.event.GraphSelectionListener;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigText;

/**
 * The fig inspector listens for selection of a single fig.
 * It presents the composite structure of the selected Fig
 * in the dev panel.
 * @author Bob Tarling
 */
public class FigInspectorPanel extends JPanel implements GraphSelectionListener {

    private static final long serialVersionUID = -3483456053389473380L;
    
    private static FigInspectorPanel INSTANCE = new FigInspectorPanel();
    
    public static FigInspectorPanel getInstance() {
        return INSTANCE;
    }
    
    private FigInspectorPanel() {
        Globals.curEditor().getSelectionManager().addGraphSelectionListener(this);
        setLayout(new BorderLayout());
    }
    
    public void selectionChanged(GraphSelectionEvent selectionEvent) {
        removeAll();
        if (selectionEvent.getSelections().size() == 1) {
            Fig selectedFig = (Fig)selectionEvent.getSelections().get(0);
            DefaultMutableTreeNode tn = new DefaultMutableTreeNode(getDescr(selectedFig));
            buildTree(selectedFig, tn);
            JTree tree = new JTree(tn);
            add(tree);
        }
    }
    
    private void buildTree(Fig f, DefaultMutableTreeNode tn) {
        if (f instanceof FigGroup) {
            FigGroup fg = (FigGroup)f;
            for (int i=0; i < fg.getFigCount(); ++i) {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(getDescr(fg.getFigAt(i)));
                buildTree(fg.getFigAt(i), childNode);
                tn.add(childNode);
            }
        }
    }
    
    private String getDescr(Fig f) {
        String className = f.getClass().getName();
        String descr = className.substring(className.lastIndexOf(".") + 1);
        if (f instanceof FigText) {
            descr += " [" + ((FigText)f).getText() + "]";
        }
        return descr;
    }
}
