// Copyright (c) 1996-99 The Regents of the University of California. All
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
import javax.swing.tree.DefaultTreeCellRenderer;

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.ModelFacade;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.activity.ui.UMLActivityDiagram;
import org.argouml.uml.diagram.collaboration.ui.UMLCollaborationDiagram;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.diagram.use_case.ui.UMLUseCaseDiagram;

/**
 * UMTreeCellRenderer determines how the entries in the Navigationpane
 * and ToDoList will be represented graphically.
 *
 * <p>In particular it makes decisions about the icons
 *  to use in order to display a Navigationpane artifact depending on the kind
 *  of object to be displayed.
 */
public class UMLTreeCellRenderer extends DefaultTreeCellRenderer {

    private Category cat = Category.getInstance(this.getClass());

    ////////////////////////////////////////////////////////////////
    // TreeCellRenderer implementation

    public Component getTreeCellRendererComponent(
        JTree tree,
        Object value,
        boolean sel,
        boolean expanded,
        boolean leaf,
        int row,
        boolean _hasFocus) {
        if (TargetManager.getInstance().getTargets().contains(value)) {
            sel = true;           
        } else {
            sel = false;          
        }

        Component r =
            super.getTreeCellRendererComponent(
                tree,
                value,
                sel,
                expanded,
                leaf,
                row,
                _hasFocus);

        if (r instanceof JLabel) {
            JLabel lab = (JLabel)r;
            Icon icon =
                ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIcon(
                    value);

            if (icon != null)
                lab.setIcon(icon);
            else
                cat.warn("UMLTreeCellRenderer: using default Icon");

            // setting the tooltip
            String tip = null;
            if (ModelFacade.isAModelElement(value)) {
                tip = ModelFacade.getUMLClassName(value);
            } else if (value instanceof UMLDiagram) {

                if (value instanceof UMLActivityDiagram)
                    tip = Argo.localize("UMLMenu", "label.activity-diagram");
                else if (value instanceof UMLSequenceDiagram)
                    tip = Argo.localize("UMLMenu", "label.sequence-diagram");
                else if (value instanceof UMLCollaborationDiagram)
                    tip =
                        Argo.localize("UMLMenu", "label.collaboration-diagram");
                else if (value instanceof UMLDeploymentDiagram)
                    tip = Argo.localize("UMLMenu", "label.deployment-diagram");
                else if (value instanceof UMLStateDiagram)
                    tip = Argo.localize("UMLMenu", "label.state-chart-diagram");
                else if (value instanceof UMLUseCaseDiagram)
                    tip = Argo.localize("UMLMenu", "label.usecase-diagram");
                else if (value instanceof UMLClassDiagram)
                    tip = Argo.localize("UMLMenu", "label.class-diagram");
            } else
                // TODO I18n
                tip = (value == null) ? "empty" : value.toString() + " ";

            // lab.setToolTipText(tip);
            if ((tree.getToolTipText() != null
                && !tree.getToolTipText().equals(tip))
                || (tree.getToolTipText() == null && tip != null))
                tree.setToolTipText(tip);
        }
        return r;
    }

} /* end class UMLTreeCellRenderer */
