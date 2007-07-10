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

package org.argouml.uml.ui;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.UMLDiagram;

/**
 * UMTreeCellRenderer determines how the entries in the Explorerpane
 * and ToDoList will be represented graphically.<p>
 *
 * In particular it makes decisions about the icons to use,
 * in order to display an Explorerpane artifact depending on the kind
 * of object to be displayed.<p>
 *
 * This class must be efficient as it is called many 1000's of times.
 */
public class UMLTreeCellRenderer extends DefaultTreeCellRenderer {

    // get localised strings once only
    private static String name = Translator.localize("label.name");
    private static String typeName = Translator.localize("label.type");

    ////////////////////////////////////////////////////////////////
    // TreeCellRenderer implementation

    /*
     * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(
     *      javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int,
     *      boolean)
     */
    public Component getTreeCellRendererComponent(
        JTree tree,
        Object value,
        boolean sel,
        boolean expanded,
        boolean leaf,
        int row,
        boolean hasFocusParam) {

	if (value instanceof DefaultMutableTreeNode) {
	    value = ((DefaultMutableTreeNode) value).getUserObject();
	}

        Component r =
            super.getTreeCellRendererComponent(
                tree,
                value,
                sel,
                expanded,
                leaf,
                row,
                hasFocusParam);

        if (r instanceof JLabel) {
            JLabel lab = (JLabel) r;

            // setting the icon
            Icon icon = ResourceLoaderWrapper.getInstance().lookupIcon(value);
            if (icon != null) {
                lab.setIcon(icon);
            }

            // setting the tooltip to type and name
            String type = null;
            if (Model.getFacade().isAModelElement(value)) {
                type = Model.getFacade().getUMLClassName(value);
            } else if (value instanceof UMLDiagram) {
                type = ((UMLDiagram) value).getLabelName();
            }

            if (type != null) {
                StringBuffer buf = new StringBuffer();
                buf.append("<html>");
                buf.append(name);
                buf.append(' ');
                buf.append(lab.getText());
                buf.append("<br>");
                buf.append(typeName);
                buf.append(' ');
                buf.append(type);
                lab.setToolTipText(buf.toString());
            } else {
                lab.setToolTipText(lab.getText());
            }
        }
        return r;
    }

} /* end class UMLTreeCellRenderer */
