// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.dev.figinspector;

import java.awt.BorderLayout;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;

import org.argouml.uml.diagram.sequence.ui.FigClassifierRole;
import org.argouml.uml.diagram.sequence.ui.MessageNodeBuilder;
import org.tigris.gef.base.Globals;
import org.tigris.gef.event.GraphSelectionEvent;
import org.tigris.gef.event.GraphSelectionListener;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigText;

/**
 * The fig inspector listens for selection of a single fig.
 * It presents the composite structure of the selected Fig
 * in the dev panel.
 * @author Bob Tarling
 */
public final class FigInspectorPanel
    extends JPanel implements GraphSelectionListener {

    /**
     * The UID.
     */
    private static final long serialVersionUID = -3483456053389473380L;

    /**
     * The instance.
     */
    private static final FigInspectorPanel INSTANCE =
        new FigInspectorPanel();

    /**
     * @return The instance.
     */
    public static FigInspectorPanel getInstance() {
        return INSTANCE;
    }

    /**
     * Constructor.
     */
    private FigInspectorPanel() {
        Globals.curEditor().getSelectionManager()
            .addGraphSelectionListener(this);
        setLayout(new BorderLayout());
    }

    public void selectionChanged(GraphSelectionEvent selectionEvent) {
        removeAll();
        if (selectionEvent.getSelections().size() == 1) {
            Fig selectedFig = (Fig) selectionEvent.getSelections().get(0);
            DefaultMutableTreeNode tn =
                new DefaultMutableTreeNode(getDescr(selectedFig));
            buildTree(selectedFig, tn);
            if (selectedFig instanceof FigClassifierRole) {
                MessageNodeBuilder.addNodeTree(tn,
                        (FigClassifierRole) selectedFig);
            }
            FigTree tree = new FigTree(tn);
            tree.expandAll();

            JScrollPane scroller = new JScrollPane(tree);
            add(scroller);
        }
    }

    private void buildTree(Fig f, DefaultMutableTreeNode tn) {
        if (f instanceof FigGroup) {
            FigGroup fg = (FigGroup) f;
            for (int i = 0; i < fg.getFigCount(); ++i) {
                addNode(tn, fg.getFigAt(i));
            }
        } else if (f instanceof FigEdge) {
            FigEdge fe = (FigEdge) f;
            Fig lineFig = fe.getFig();
            addNode(tn, lineFig);
            addNode(tn, fe.getSourceFigNode());
            addNode(tn, fe.getSourcePortFig());
            addNode(tn, fe.getDestFigNode());
            addNode(tn, fe.getDestPortFig());
            for (Iterator it = fe.getPathItemFigs().iterator(); it.hasNext(); ) {
                Fig pathFig = (Fig) it.next();
                addNode(tn, pathFig);
            }
        }
    }

    private void addNode(DefaultMutableTreeNode tn, Fig fig) {
        DefaultMutableTreeNode childNode =
            new DefaultMutableTreeNode(getDescr(fig));
        buildTree(fig, childNode);
        tn.add(childNode);
    }
    
    private String getDescr(Fig f) {
        String className = f.getClass().getName();
        String descr = className.substring(className.lastIndexOf(".") + 1);
        descr += " " + f.getBounds().toString();
        if (f instanceof FigText) {
            descr += " \"" + ((FigText) f).getText() + "\"";
        }
        if (!f.isVisible()) {
            descr += " - INVISIBLE";
        }
        descr += " - lay=" + f.getLayer() + " - grp=" + f.getGroup();
        return descr;
    }
}
