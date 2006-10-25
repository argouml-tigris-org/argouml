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

package org.argouml.cognitive.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.cognitive.Decision;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Goal;
import org.argouml.cognitive.Poster;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLTreeCellRenderer;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Globals;
import org.tigris.gef.presentation.Fig;


/**
 * Displays an entry in the ToDo tree.
 *
 */
public class ToDoTreeRenderer extends DefaultTreeCellRenderer {
    ////////////////////////////////////////////////////////////////
    // class variables

    // general icons for poster
    private final ImageIcon postIt0     = lookupIconResource("PostIt0");
    private final ImageIcon postIt25    = lookupIconResource("PostIt25");
    private final ImageIcon postIt50    = lookupIconResource("PostIt50");
    private final ImageIcon postIt75    = lookupIconResource("PostIt75");
    private final ImageIcon postIt99    = lookupIconResource("PostIt99");
    private final ImageIcon postIt100   = lookupIconResource("PostIt100");

    // specialised icons for designer
    private final ImageIcon postItD0    = lookupIconResource("PostItD0");
    private final ImageIcon postItD25   = lookupIconResource("PostItD25");
    private final ImageIcon postItD50   = lookupIconResource("PostItD50");
    private final ImageIcon postItD75   = lookupIconResource("PostItD75");
    private final ImageIcon postItD99   = lookupIconResource("PostItD99");
    private final ImageIcon postItD100  = lookupIconResource("PostItD100");

    private UMLTreeCellRenderer treeCellRenderer = new UMLTreeCellRenderer();

    private static ImageIcon lookupIconResource(String name) {
        return ResourceLoaderWrapper.lookupIconResource(name);
    }

    ////////////////////////////////////////////////////////////////
    // TreeCellRenderer implementation

    /**
     * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(
     * javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int,
     * boolean)
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value,
    boolean sel,
    boolean expanded,
    boolean leaf, int row,
    boolean hasTheFocus) {

        Component r = super.getTreeCellRendererComponent(tree, value, sel,
							 expanded, leaf,
							 row, hasTheFocus);

        if (r instanceof JLabel) {
            JLabel lab = (JLabel) r;
            if (value instanceof ToDoItem) {
                ToDoItem item = (ToDoItem) value;
                Poster post = item.getPoster();
                if (post instanceof Designer) {
                    if (item.getProgress() == 0) lab.setIcon(postItD0);
                    else if (item.getProgress() <= 25) lab.setIcon(postItD25);
                    else if (item.getProgress() <= 50) lab.setIcon(postItD50);
                    else if (item.getProgress() <= 75) lab.setIcon(postItD75);
                    else if (item.getProgress() <= 100) lab.setIcon(postItD99);
                    else lab.setIcon(postItD100);
                } else {
                    if (item.getProgress() == 0) lab.setIcon(postIt0);
                    else if (item.getProgress() <= 25) lab.setIcon(postIt25);
                    else if (item.getProgress() <= 50) lab.setIcon(postIt50);
                    else if (item.getProgress() <= 75) lab.setIcon(postIt75);
                    else if (item.getProgress() <= 100) lab.setIcon(postIt99);
                    else lab.setIcon(postIt100);
                }

            } else if (value instanceof Decision) {
                lab.setIcon(MetalIconFactory.getTreeFolderIcon());
            } else if (value instanceof Goal) {
                lab.setIcon(MetalIconFactory.getTreeFolderIcon());
            } else if (value instanceof Poster) {
                lab.setIcon(MetalIconFactory.getTreeFolderIcon());
            } else if (value instanceof PriorityNode) {
                lab.setIcon(MetalIconFactory.getTreeFolderIcon());
            } else if (value instanceof KnowledgeTypeNode) {
                lab.setIcon(MetalIconFactory.getTreeFolderIcon());
            } else if (value instanceof Diagram) {
                return treeCellRenderer.getTreeCellRendererComponent(tree,
								 value,
								 sel,
								 expanded,
								 leaf,
								 row,
								 hasTheFocus);
            } else {
                Object newValue = value;
                if (newValue instanceof Fig) {
                    newValue = ((Fig) value).getOwner();
                }
                if (Model.getFacade().isAModelElement(newValue)) {
                    return treeCellRenderer.getTreeCellRendererComponent(tree,
								     newValue,
								     sel,
								     expanded,
								     leaf,
								     row,
								     hasTheFocus);
                }
            }



            String tip = lab.getText() + " ";
            lab.setToolTipText(tip);
            tree.setToolTipText(tip);

            if (!sel) {
                lab.setBackground(getBackgroundNonSelectionColor());
            } else {
                Color high = Globals.getPrefs().getHighlightColor();
                high = high.brighter().brighter();
                lab.setBackground(high);
            }
            lab.setOpaque(sel);
        }
        return r;
    }


} /* end class ToDoTreeRenderer */
