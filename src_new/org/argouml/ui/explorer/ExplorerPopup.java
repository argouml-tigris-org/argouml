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

package org.argouml.ui.explorer;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.activity.ui.UMLActivityDiagram;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.ui.ActionAddAllClassesFromModel;
import org.argouml.uml.diagram.ui.ActionAddExistingEdge;
import org.argouml.uml.diagram.ui.ActionAddExistingNode;
import org.argouml.uml.diagram.ui.ActionAddExistingNodes;
import org.argouml.uml.diagram.ui.ActionSaveDiagramToClipboard;
import org.argouml.uml.ui.ActionActivityDiagram;
import org.argouml.uml.ui.ActionAddPackage;
import org.argouml.uml.ui.ActionClassDiagram;
import org.argouml.uml.ui.ActionCollaborationDiagram;
import org.argouml.uml.ui.ActionDeleteModelElements;
import org.argouml.uml.ui.ActionDeploymentDiagram;
import org.argouml.uml.ui.ActionRESequenceDiagram;
import org.argouml.uml.ui.ActionSequenceDiagram;
import org.argouml.uml.ui.ActionSetSourcePath;
import org.argouml.uml.ui.ActionStateDiagram;
import org.argouml.uml.ui.ActionUseCaseDiagram;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.graph.MutableGraphModel;

/**
 * PopUp for extra functionality for the Explorer.
 *
 * @author alexb
 * @since 0.15.2
 */
public class ExplorerPopup extends JPopupMenu {

    private JMenu createDiagrams = new JMenu(menuLocalize("Create Diagram"));

    /**
     * Creates a new instance of ExplorerPopup.
     *
     * @param selectedItem
     *            is the item that we are pointing at.
     * @param me
     *            is the event.
     */
    public ExplorerPopup(Object selectedItem, MouseEvent me) {
        super("Explorer popup menu");

        /* Check if multiple items are selected. */
        boolean multiSelect =
                TargetManager.getInstance().getTargets().size() > 1;

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
        // if (activeDiagram.canAdd(selectedItem)) {
        // UMLAction action =
        // new ActionAddExistingNode(
        // menuLocalize("menu.popup.add-to-diagram"),
        // selectedItem);
        // action.setEnabled(action.shouldBeEnabled());
        // this.add(action);
        // }

        if (!multiSelect) {
            initMenuCreate();
            this.add(createDiagrams);
        }

        final Object projectModel = currentProject.getModel();
        final boolean modelElementSelected =
            Model.getFacade().isAModelElement(selectedItem);

        if (modelElementSelected) {
            final boolean nAryAssociationSelected =
                Model.getFacade().isANaryAssociation(selectedItem);
            final boolean classifierSelected =
                Model.getFacade().isAClassifier(selectedItem);
            final boolean packageSelected =
                Model.getFacade().isAPackage(selectedItem);
            final boolean commentSelected =
                Model.getFacade().isAComment(selectedItem);
            final boolean stateVertexSelected =
                Model.getFacade().isAStateVertex(selectedItem);
            final boolean instanceSelected =
                Model.getFacade().isAInstance(selectedItem);
            final boolean dataValueSelected =
                Model.getFacade().isADataValue(selectedItem);
            final boolean relationshipSelected =
                Model.getFacade().isARelationship(selectedItem);
            final boolean flowSelected =
                Model.getFacade().isAFlow(selectedItem);
            final boolean linkSelected =
                Model.getFacade().isALink(selectedItem);
            final boolean transitionSelected =
                Model.getFacade().isATransition(selectedItem);
            final boolean activityDiagramActive =
                activeDiagram instanceof UMLActivityDiagram;
            final boolean sequenceDiagramActive =
                activeDiagram instanceof UMLSequenceDiagram;
            final boolean stateDiagramActive =
                activeDiagram instanceof UMLStateDiagram;
            final Object selectedStateMachine =
                (stateVertexSelected) ? Model
                    .getStateMachinesHelper().getStateMachine(selectedItem)
                    : null;
            final Object diagramStateMachine =
                (stateDiagramActive) ? ((UMLStateDiagram) activeDiagram)
                    .getStateMachine()
                    : null;
            final Object diagramActivity =
                (activityDiagramActive)
                    ? ((UMLActivityDiagram) activeDiagram).getStateMachine()
                    : null;
                    
            if (!multiSelect) {
                if ((classifierSelected && !relationshipSelected)
                        || (packageSelected && selectedItem != projectModel)
                        || (stateVertexSelected
                                && activityDiagramActive
                                && diagramActivity == selectedStateMachine)
                        || (stateVertexSelected
                                && stateDiagramActive
                                && diagramStateMachine == selectedStateMachine)
                        || (instanceSelected
                                && !dataValueSelected
                                && !sequenceDiagramActive)
                        || nAryAssociationSelected || commentSelected) {
                    Action action =
                        new ActionAddExistingNode(
                            menuLocalize("menu.popup.add-to-diagram"),
                            selectedItem);
                    this.add(action);
                }

                if ((relationshipSelected
                        && !flowSelected
                        && !nAryAssociationSelected)
                        || (linkSelected && !sequenceDiagramActive)
                        || transitionSelected) {

                    Action action =
                        new ActionAddExistingEdge(
                            menuLocalize("menu.popup.add-to-diagram"),
                            selectedItem);
                    this.add(action);
                    addMenuItemForBothEndsOf(selectedItem);
                }

                if (classifierSelected
                        || packageSelected) {
                    this.add(new ActionSetSourcePath());
                }

                if (Model.getFacade().isAOperation(selectedItem)) {
                    this.add(new ActionRESequenceDiagram());
                }

                if (packageSelected
                        || Model.getFacade().isAModel(selectedItem)) {
                    this.add(new ActionAddPackage());
                }
            }

            if (selectedItem != projectModel) {
                this.add(new ActionDeleteModelElements());
            }
        }
        // TODO: Make sure this shouldn't go into a previous
        // condition -tml
        if (!multiSelect) {
            if (selectedItem instanceof UMLClassDiagram) {
                Action action =
                    new ActionAddAllClassesFromModel(
                        menuLocalize("menu.popup.add-all-classes-to-diagram"),
                        selectedItem);
                this.add(action);
            }
        }

        if (multiSelect) {
            Collection coll = TargetManager.getInstance().getTargets();
            Iterator iter = (coll != null) ? coll.iterator() : null;
            ArrayList classifiers = new ArrayList();
            while (iter != null && iter.hasNext()) {
                Object o = iter.next();
                if (Model.getFacade().isAClassifier(o)
                     && !Model.getFacade().isARelationship(o)) {
                    classifiers.add(o);
                }
            }
            if (!classifiers.isEmpty()) {
                Action action =
                    new ActionAddExistingNodes(
                        menuLocalize("menu.popup.add-to-diagram"),
                        classifiers);
                this.add(action);
            }
        }

        if (selectedItem instanceof Diagram) {
            this.add(new ActionSaveDiagramToClipboard());
            this.add(new ActionDeleteModelElements());
        }

    }

    /**
     * initialize the menu for diagram construction in the explorer popup menu.
     *
     */
    private void initMenuCreate() {
        createDiagrams.add(new ActionUseCaseDiagram());

        createDiagrams.add(new ActionClassDiagram());

        createDiagrams.add(new ActionSequenceDiagram());

        createDiagrams.add(new ActionCollaborationDiagram());

        createDiagrams.add(new ActionStateDiagram());

        createDiagrams.add(new ActionActivityDiagram());

        createDiagrams.add(new ActionDeploymentDiagram());
    }

    /**
     * Locale a popup menu item in the navigator pane.
     *
     * @param key
     *            The key for the string to localize.
     * @return The localized string.
     */
    private String menuLocalize(String key) {
        return Translator.localize(key);
    }
    
    /**
     * Add popup menu items for adding to diagram both edge ends.
     *
     * @param edge
     *            The edge for which the menu item will be added.
     */
    private void addMenuItemForBothEndsOf(Object edge) {
        Collection coll = null;
        if (Model.getFacade().isAAssociation(edge)
                || Model.getFacade().isALink(edge)) {
            coll = Model.getFacade().getConnections(edge);
        } else if (Model.getFacade().isAAbstraction(edge)
                || Model.getFacade().isADependency(edge)) {
            coll = new ArrayList();
            coll.addAll(Model.getFacade().getClients(edge));
            coll.addAll(Model.getFacade().getSuppliers(edge));
        } else if (Model.getFacade().isAGeneralization(edge)) {
            coll = new ArrayList();
            Object parent = Model.getFacade().getParent(edge);
            coll.add(parent);
            coll.addAll(Model.getFacade().getChildren(parent));
        }
        if (coll == null) {
            return;
        }
        Iterator iter = coll.iterator();
        while (iter.hasNext()) {
            Object me = iter.next();
            if (me != null) {
                if (Model.getFacade().isAAssociationEnd(me)) {
                    me = Model.getFacade().getType(me);
                }
                if (me != null) {
                    String name = Model.getFacade().getName(me);
                    if (name == null || name.length() == 0) {
                        name = "(anon element)";
                    }
                    Action action =
                        new ActionAddExistingRelatedNode(
                            menuLocalize("menu.popup.add-to-diagram") + ": "
                             + name,
                            me);
                    this.add(action);
                }
            }
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -5663884871599931780L;

    
    private class ActionAddExistingRelatedNode extends ActionAddExistingNode {

        /**
         * The UML object to be added to the diagram.
         */
        private Object object;

        /**
         * The Constructor.
         *
         * @param name the localized name of the action
         * @param o the node UML object to be added
         */
        public ActionAddExistingRelatedNode(String name, Object o) {
            super(name, o);
            object = o;
        }

        /**
         * @see javax.swing.Action#isEnabled()
         */
        public boolean isEnabled() {
            ArgoDiagram dia = ProjectManager.getManager().
                getCurrentProject().getActiveDiagram();
            if (dia == null) return false;
            MutableGraphModel gm = (MutableGraphModel) dia.getGraphModel();
            return gm.canAddNode(object);
        }
    } /* end class ActionAddExistingRelatedNode */
}
