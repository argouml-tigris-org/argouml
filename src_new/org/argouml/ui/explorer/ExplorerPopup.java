// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

import org.argouml.application.api.Argo;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.behavioralelements.statemachines.StateMachinesHelper;
import org.argouml.ui.ActionGoToDetails;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.ui.ActionAddExistingEdge;
import org.argouml.uml.diagram.ui.ActionAddExistingNode;
import org.argouml.uml.ui.ActionAddPackage;
import org.argouml.uml.ui.ActionRemoveFromModel;
import org.argouml.uml.ui.ActionSetSourcePath;
import org.argouml.uml.ui.UMLAction;

import org.tigris.gef.base.Diagram;
import org.tigris.gef.ui.PopupGenerator;

/**
 *
 * @author  alexb
 */
public class ExplorerPopup extends JPopupMenu{
    
    /** Creates a new instance of ExplorerPopup */
    public ExplorerPopup(Object selectedItem, MouseEvent me) {
        
        super("Explorer popup menu");
        
        if (selectedItem instanceof PopupGenerator) {
            Vector actions = ((PopupGenerator) selectedItem).getPopUpActions(me);
            for (Enumeration e = actions.elements();
            e.hasMoreElements();
            ) {
                this.add((AbstractAction) e.nextElement());
            }
        } else {
            if ((ModelFacade.isAClassifier(selectedItem) &&
            !(ModelFacade.isADataType(selectedItem)))
            || ((ModelFacade.isAPackage(selectedItem))
            && (selectedItem
            != ProjectManager
            .getManager()
            .getCurrentProject()
            .getModel()))
            || ((ModelFacade.isAStateVertex(selectedItem))
            && ((ProjectManager
            .getManager()
            .getCurrentProject()
            .getActiveDiagram()
            instanceof UMLStateDiagram)
            && (((UMLStateDiagram) ProjectManager
            .getManager()
            .getCurrentProject()
            .getActiveDiagram())
            .getStateMachine()
            == StateMachinesHelper
            .getHelper()
            .getStateMachine(
            selectedItem))))
            || (ModelFacade.isAInstance(selectedItem)
            && !(ModelFacade.isADataValue(selectedItem))
            && !(ProjectManager
            .getManager()
            .getCurrentProject()
            .getActiveDiagram()
            instanceof UMLSequenceDiagram))) {
                UMLAction action =
                new ActionAddExistingNode(
                menuLocalize("menu.popup.add-to-diagram"),
                selectedItem);
                action.setEnabled(action.shouldBeEnabled());
                this.add(action);
            }
            if ((ModelFacade.isARelationship(selectedItem) &&
            !(ModelFacade.isAFlow(selectedItem)))
            || ((ModelFacade.isALink(selectedItem))
            && !(ProjectManager
            .getManager()
            .getCurrentProject()
            .getActiveDiagram()
            instanceof UMLSequenceDiagram))
            || (ModelFacade.isATransition(selectedItem))) {
                UMLAction action =
                new ActionAddExistingEdge(
                menuLocalize("menu.popup.add-to-diagram"),
                selectedItem);
                action.setEnabled(action.shouldBeEnabled());
                this.add(action);
            }
            
            if ((ModelFacade.isAModelElement(selectedItem)
            && (selectedItem
            != ProjectManager
            .getManager()
            .getCurrentProject()
            .getModel()))
            || selectedItem instanceof Diagram) {
                this.add(ActionRemoveFromModel.SINGLETON);
            }
            if (ModelFacade.isAClassifier(selectedItem) ||
            ModelFacade.isAPackage(selectedItem)) {
                this.add(ActionSetSourcePath.SINGLETON);
            }
            if (ModelFacade.isAPackage(selectedItem) || ModelFacade.isAModel(selectedItem)) {
                this.add(ActionAddPackage.SINGLETON);
            }
            if (selectedItem != null) {
                this.add(new ActionGoToDetails(
                menuLocalize("action.properties")));
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
        return Argo.localize("Tree", key);
    }
    
}
