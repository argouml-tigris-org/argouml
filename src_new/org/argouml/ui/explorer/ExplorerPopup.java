// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.ui.explorer;

import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.ModelFacade;
import org.argouml.ui.ActionGoToDetails;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.ui.ActionAddAllClassesFromModel;
import org.argouml.uml.diagram.ui.ActionAddExistingEdge;
import org.argouml.uml.diagram.ui.ActionAddExistingNode;
import org.argouml.uml.ui.ActionAddPackage;
import org.argouml.uml.ui.ActionRemoveFromModel;
import org.argouml.uml.ui.ActionSetSourcePath;
import org.argouml.uml.ui.UMLAction;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.ui.PopupGenerator;

/**
 * PopUp for extra functionality for the Explorer.
 *
 * @author  alexb
 * @since 0.15.2
 */
public class ExplorerPopup extends JPopupMenu {

    /**
     * Creates a new instance of ExplorerPopup.
     *
     * @param selectedItem is the item that we are pointing at.
     * @param me is the event.
     */
    public ExplorerPopup(Object selectedItem, MouseEvent me) {

        super("Explorer popup menu");

        if (selectedItem instanceof PopupGenerator) {
            Vector actions =
		((PopupGenerator) selectedItem).getPopUpActions(me);
            for (Enumeration e = actions.elements(); e.hasMoreElements();) {
                this.add((AbstractAction) e.nextElement());
            }
        } else {
            final Project currentProject =
                ProjectManager.getManager().getCurrentProject();
            final Diagram activeDiagram = currentProject.getActiveDiagram();

            // TODO: I've made some attempt to rationalize the conditions here
            // and make them more readable. However I'd suggest that the
            // conditions should move to each diagram.
            // Break up one complex method into a few simple ones and
            // give the diagrams more knowledge of themselelves
            // (although the diagrams may in fact delegate this in
            // turn to the Model component).
            // Bob Tarling 31 Jan 2004
            // eg the code here should be something like -
            //if (activeDiagram.canAdd(selectedItem)) {
            //    UMLAction action =
            //        new ActionAddExistingNode(
            //            menuLocalize("menu.popup.add-to-diagram"),
            //            selectedItem);
            //    action.setEnabled(action.shouldBeEnabled());
            //    this.add(action);
            //}

            final Object projectModel = currentProject.getModel();
            final boolean modelElementSelected =
                ModelFacade.isAModelElement(selectedItem);

            if (modelElementSelected) {
                final boolean nAryAssociationSelected =
                        ModelFacade.isANaryAssociation(selectedItem);
                final boolean classifierAndRelationShipSelected =
                    ModelFacade.isAClassifierAndARelationship(selectedItem);
                final boolean classifierSelected =
                    ModelFacade.isAClassifier(selectedItem);
                final boolean dataTypeSelected =
                    ModelFacade.isADataType(selectedItem);
                final boolean packageSelected =
                    ModelFacade.isAPackage(selectedItem);
                final boolean stateVertexSelected =
                    ModelFacade.isAStateVertex(selectedItem);
                final boolean instanceSelected =
                    ModelFacade.isAInstance(selectedItem);
                final boolean dataValueSelected =
                    ModelFacade.isADataValue(selectedItem);
                final boolean relationshipSelected =
                    ModelFacade.isARelationship(selectedItem);
                final boolean flowSelected =
                    ModelFacade.isAFlow(selectedItem);
                final boolean linkSelected =
                    ModelFacade.isALink(selectedItem);
                final boolean transitionSelected =
                    ModelFacade.isATransition(selectedItem);
                final boolean diagramSelected =
                    selectedItem instanceof Diagram;

                final boolean sequenceDiagramActive =
                    activeDiagram instanceof UMLSequenceDiagram;
                final boolean stateDiagramActive =
                    activeDiagram instanceof UMLStateDiagram;

                final Object selectedStateMachine
                    = (stateVertexSelected)
                    ? Model.getStateMachinesHelper()
		          .getStateMachine(selectedItem)
                    : null;

                final Object diagramStateMachine
                    = (stateDiagramActive)
                    ? ((UMLStateDiagram) activeDiagram).getStateMachine()
                    : null;

                if ((classifierSelected && !dataTypeSelected
                    && !classifierAndRelationShipSelected)
                        || (packageSelected && selectedItem != projectModel)
                        || (stateVertexSelected && stateDiagramActive
                            && diagramStateMachine == selectedStateMachine)
                        || (instanceSelected && !dataValueSelected
                            && !sequenceDiagramActive)
                        || nAryAssociationSelected) {
                    UMLAction action =
                        new ActionAddExistingNode(
                            menuLocalize("menu.popup.add-to-diagram"),
                            selectedItem);
                    action.setEnabled(action.shouldBeEnabled());
                    this.add(action);
                }

                if ((relationshipSelected && !flowSelected && !nAryAssociationSelected)
                        || (linkSelected && !sequenceDiagramActive)
                        || transitionSelected) {
                    UMLAction action = new ActionAddExistingEdge(
                        menuLocalize("menu.popup.add-to-diagram"),
			selectedItem);
                    action.setEnabled(action.shouldBeEnabled());
                    this.add(action);
                }

                if (selectedItem != projectModel || diagramSelected) {
                    this.add(new ActionRemoveFromModel());
                }

                if (ModelFacade.isAClassifier(selectedItem)
		    || ModelFacade.isAPackage(selectedItem)) {
                    this.add(ActionSetSourcePath.SINGLETON);
                }

                if (ModelFacade.isAPackage(selectedItem)
                        || ModelFacade.isAModel(selectedItem)) {
                    this.add(ActionAddPackage.SINGLETON);
                }
            }

	    // TODO: Make sure this shouldn't go into a previous
            // condition -tml
	    if (selectedItem instanceof UMLClassDiagram) {
                UMLAction action =
		    new ActionAddAllClassesFromModel(
		        menuLocalize("menu.popup.add-all-classes-to-diagram"),
			selectedItem);
                action.setEnabled(action.shouldBeEnabled());
                this.add(action);
	    }

            if (selectedItem != null) {
                this.add(
                    new ActionGoToDetails(menuLocalize("action.properties"))
                );
            }
        }
    }

    /**
     * Locale a popup menu item in the navigator pane.
     *
     * @param key The key for the string to localize.
     * @return The localized string.
     */
    private final String menuLocalize(String key) {
        return Translator.localize(key);
    }

}
