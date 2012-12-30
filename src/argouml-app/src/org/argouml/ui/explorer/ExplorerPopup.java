/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *    Michiel van der Wulp
 *    Laurent Braud
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;

import org.argouml.i18n.Translator;
import org.argouml.kernel.ActionList;
import org.argouml.kernel.ProfileConfiguration;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.IllegalModelElementConnectionException;
import org.argouml.model.Model;
import org.argouml.profile.Profile;
import org.argouml.ui.ActionCreateContainedModelElement;
import org.argouml.ui.ActionCreateEdgeModelElement;
import org.argouml.ui.ContextActionFactoryManager;
import org.argouml.ui.UndoableAction;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.argouml.uml.diagram.SequenceDiagram;
import org.argouml.uml.diagram.activity.ui.UMLActivityDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.ui.ActionAddAllClassesFromModel;
import org.argouml.uml.diagram.ui.ActionAddExistingEdge;
import org.argouml.uml.diagram.ui.ActionAddExistingNode;
import org.argouml.uml.diagram.ui.ActionAddExistingNodes;
import org.argouml.uml.diagram.ui.ActionSaveDiagramToClipboard;
import org.argouml.uml.diagram.ui.ModeAddToDiagram;
import org.argouml.uml.ui.ActionActivityDiagram;
import org.argouml.uml.ui.ActionClassDiagram;
import org.argouml.uml.ui.ActionCollaborationDiagram;
import org.argouml.uml.ui.ActionDeleteModelElements;
import org.argouml.uml.ui.ActionDeploymentDiagram;
import org.argouml.uml.ui.ActionSequenceDiagram;
import org.argouml.uml.ui.ActionSetSourcePath;
import org.argouml.uml.ui.ActionStateDiagram;
import org.argouml.uml.ui.ActionUseCaseDiagram;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;

/**
 * PopUp for extra functionality for the Explorer.
 *
 * @author alexb
 * @since 0.15.2
 */
public class ExplorerPopup extends JPopupMenu {

    private JMenu createDiagrams =
        new JMenu(menuLocalize("menu.popup.create-diagram"));

    private static final Logger LOG =
        Logger.getLogger(ExplorerPopup.class.getName());

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

        boolean mutableModelElementsOnly = true;
        for (Object element : TargetManager.getInstance().getTargets()) {
            if (!Model.getFacade().isAUMLElement(element)
                    || Model.getModelManagementHelper().isReadOnly(element)) {
                mutableModelElementsOnly = false;
                break;
            }
        }

        final ArgoDiagram activeDiagram = DiagramUtils.getActiveDiagram();

        // TODO: I've made some attempt to rationalize the conditions here
        // and make them more readable. However I'd suggest that the
        // conditions should move to each diagram.
        // Break up one complex method into a few simple ones and
        // give the diagrams more knowledge of themselves
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

        // profile section: dealing with profiles in different states

        // 1. a noneditable applied profile
        try {
            if (!multiSelect && selectedItem instanceof Profile
                    && !((Profile) selectedItem).getProfilePackages().isEmpty()) {
                this.add(new ActionExportProfileXMI((Profile) selectedItem));
            }
        } catch (Exception e) {
            // just no item added in this case
        }

        // 2. the profile configuration, holding noneditable applied profiles
        if (!multiSelect && selectedItem instanceof ProfileConfiguration) {
            this.add(new ActionManageProfiles());
        }

        // 3. the editable unapplied profile

        if (Model.getFacade().isAProfile(selectedItem)) {
            this.add(new ActionDeployProfile(selectedItem));
        }

        // end of profile section

        if (!multiSelect && mutableModelElementsOnly) {
            initMenuCreateDiagrams();
            this.add(createDiagrams);
        }

        initMenuCreateModuleActions();

        if (mutableModelElementsOnly) {
            initMenuCreateModelElements();
        }

        final boolean modelElementSelected =
            Model.getFacade().isAUMLElement(selectedItem);

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
                activeDiagram instanceof SequenceDiagram;
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

            Collection projectModels =
                ProjectManager.getManager().getCurrentProject().getModels();
            if (!multiSelect) {
                if ((classifierSelected && !relationshipSelected)
                        || (packageSelected
                                // TODO: Allow adding models to a diagram - issue 4172.
                                && !projectModels.contains(selectedItem))
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
                    // TODO: Why can't we use ActionAddExistingNodes here? Bob.
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
            }


            if (mutableModelElementsOnly
                    // Can't delete last top level model
                    && !(projectModels.size() == 1
                            && projectModels.contains(selectedItem))) {
                // TODO: Shouldn't be creating a new instance here. We should
                // hold the delete action in some central place.
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
            List<Object> classifiers = new ArrayList<Object>();
            for (Object o : TargetManager.getInstance().getTargets()) {
                // TODO: Should be anything allowed by current diagram
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
            // Determine if only diagrams are in the target list.
            boolean haveAtLeastOneDiagram = false;
            boolean onlyDiagram = true;
            for (Object element : TargetManager.getInstance().getTargets()) {
                if (element instanceof Diagram) {
                    haveAtLeastOneDiagram = true;
                } else {
                    onlyDiagram = false;
                }
            }
            onlyDiagram = onlyDiagram && haveAtLeastOneDiagram;

            if (onlyDiagram) {
                ActionDeleteModelElements ad = new ActionDeleteModelElements();
                ad.setEnabled(ad.shouldBeEnabled());
                this.add(ad);
            }


        } else if (selectedItem instanceof Diagram) {
            this.add(new ActionSaveDiagramToClipboard());
            // TODO: Delete should be available on any combination of model
            // elements and diagrams.
            // TODO: Shouldn't be creating a new instance here. We should
            // hold the delete action in some central place.
            ActionDeleteModelElements ad = new ActionDeleteModelElements();
            ad.setEnabled(ad.shouldBeEnabled());
            this.add(ad);
        }
    }

    /**
     * initialize the menu for diagram construction in the explorer popup menu.
     *
     */
    private void initMenuCreateDiagrams() {
        createDiagrams.add(new ActionUseCaseDiagram());

        createDiagrams.add(new ActionClassDiagram());

        createDiagrams.add(new ActionSequenceDiagram());

        createDiagrams.add(new ActionCollaborationDiagram());

        createDiagrams.add(new ActionStateDiagram());

        createDiagrams.add(new ActionActivityDiagram());

        createDiagrams.add(new ActionDeploymentDiagram());
    }

    /**
     * initialize the menu for diagram construction in the explorer popup menu.
     *
     */
    private void initMenuCreateModelElements() {
        List targets = TargetManager.getInstance().getTargets();
        Set<JMenuItem> menuItems = new TreeSet<JMenuItem>();
        if (targets.size() >= 2) {
            // Check to see if all targets are classifiers
            // before adding an option to create an association between
            // them all
            boolean classifierRoleFound = false;
            boolean classifierRolesOnly = true;
            for (Iterator it = targets.iterator();
                    it.hasNext() && classifierRolesOnly; ) {
                if (Model.getFacade().isAClassifierRole(it.next())) {
                    classifierRoleFound = true;
                } else {
                    classifierRolesOnly = false;
                }
            }
            if (classifierRolesOnly) {
                menuItems.add(new OrderedMenuItem(
                        new ActionCreateAssociationRole(
                                Model.getMetaTypes().getAssociationRole(),
                                targets)));
            } else if (!classifierRoleFound) {
                boolean classifiersOnly = true;
                for (Iterator it = targets.iterator();
                        it.hasNext() && classifiersOnly; ) {
                    if (!Model.getFacade().isAClassifier(it.next())) {
                        classifiersOnly = false;
                    }
                }
                if (classifiersOnly) {
                    menuItems.add(new OrderedMenuItem(
                            new ActionCreateAssociation(
                                    Model.getMetaTypes().getAssociation(),
                                    targets)));
                }
            }
        }
        if (targets.size() == 2) {
            addCreateModelElementAction(
                    menuItems,
                    Model.getMetaTypes().getDependency(),
                    " " + menuLocalize("menu.popup.depends-on") + " ");

            addCreateModelElementAction(
                    menuItems,
                    Model.getMetaTypes().getGeneralization(),
                    " " + menuLocalize("menu.popup.generalizes") + " ");
            addCreateModelElementAction(
                    menuItems,
                    Model.getMetaTypes().getInclude(),
                    " " + menuLocalize("menu.popup.includes") + " ");
            addCreateModelElementAction(
                    menuItems,
                    Model.getMetaTypes().getExtend(),
                    " " + menuLocalize("menu.popup.extends") + " ");
            addCreateModelElementAction(
                    menuItems,
                    Model.getMetaTypes().getPackageImport(),
                    " " + menuLocalize("menu.popup.has-permission-on") + " ");
            addCreateModelElementAction(
                    menuItems,
                    Model.getMetaTypes().getUsage(),
                    " " + menuLocalize("menu.popup.uses") + " ");
            addCreateModelElementAction(
                    menuItems,
                    Model.getMetaTypes().getAbstraction(),
                    " " + menuLocalize("menu.popup.realizes") + " ");
        } else if (targets.size() == 1) {

            Object target = targets.get(0);

            // iterate through all possible model elements to determine which
            // are valid to be contained by the selected target
            for (Object metaType : Model.getMetaTypes().getAllMetaTypes()) {
                // test if this element can be contained by the target
                if (Model.getUmlFactory().isContainmentValid(metaType, target)) {
                    // this element can be contained add a menu item
                    // that allows the user to take that action
                    menuItems.add(new OrderedMenuItem(
                            new ActionCreateContainedModelElement(
                                    metaType, target)));
                }
            }
        }

        if (menuItems.size() == 1) {
            add(menuItems.iterator().next());
        } else if (menuItems.size() > 1) {
            JMenu menu =
                new JMenu(menuLocalize("menu.popup.create-model-element"));
            add(menu);
            for (JMenuItem item : menuItems) {
                menu.add(item);
            }
        }
    }

    private void initMenuCreateModuleActions() {
        final List<Action> contextActions =
            ContextActionFactoryManager.getContextPopupActions();
        
        LOG.log(Level.INFO, "{0}", contextActions);
        
        if (contextActions instanceof ActionList) {
            recursiveAdd(this, (Action) contextActions);
        } else {
            for (Action a : contextActions) {
                recursiveAdd(this, a);
            }
        }
    }

    private void recursiveAdd(MenuElement menu, Action a) {
        if (a instanceof List<?>) {
            JMenu m = new JMenu(a);
            if (menu instanceof JPopupMenu) {
                ((JPopupMenu) menu).add(m);
            } else if (menu instanceof JMenu) {
                ((JMenu) menu).add(m);
            }
            for (Action subAction : (List<Action>) a) {
                recursiveAdd(m, subAction);
            }
        } else {
            if (menu instanceof JPopupMenu) {
                ((JPopupMenu) menu).add(a);
            } else if (menu instanceof JMenu) {
                ((JMenu) menu).add(a);
            }
        }
    }

    private void addCreateModelElementAction(
                Set<JMenuItem> menuItems,
                Object metaType,
                String relationshipDescr) {
        List targets = TargetManager.getInstance().getTargets();
        Object source = targets.get(0);
        Object dest = targets.get(1);
        JMenu subMenu = new OrderedMenu(
                menuLocalize("menu.popup.create") + " "
                + Model.getMetaTypes().getName(metaType));
        buildDirectionalCreateMenuItem(
            metaType, dest, source, relationshipDescr, subMenu);
        buildDirectionalCreateMenuItem(
            metaType, source, dest, relationshipDescr, subMenu);
        if (subMenu.getMenuComponents().length > 0) {
            menuItems.add(subMenu);
        }
    }

    /**
     * Attempt to build a menu item to create the given model element type
     * as a relation betwen two existing model elements.
     * If succesful then the menu item is added to the given menu
     * @param metaType The type of model element the menu item should create
     * @param source The source model element
     * @param dest The destination model element
     * @param relationshipDescr A textual description that describes how
     *                          source relates to dest
     * @param menu The menu to which the menu item should be added
     */
    private void buildDirectionalCreateMenuItem(
            Object metaType,
            Object source,
            Object dest,
            String relationshipDescr,
            JMenu menu) {
        if (Model.getUmlFactory().isConnectionValid(
                    metaType, source, dest, true)) {
            JMenuItem menuItem = new JMenuItem(
                    new ActionCreateEdgeModelElement(
                            metaType,
                            source,
                            dest,
                            relationshipDescr));
            if (menuItem != null) {
                menu.add(menuItem);
            }
        }
    }


    /**
     * Localize a popup menu item in the navigator pane.
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
            Object parent = Model.getFacade().getGeneral(edge);
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


    private class ActionAddExistingRelatedNode extends UndoableAction {

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
            super(name);
            object = o;
        }

        /**
         * @see javax.swing.Action#isEnabled()
         */
        public boolean isEnabled() {
            ArgoDiagram dia = DiagramUtils.getActiveDiagram();
            if (dia == null) {
                return false;
            }
            MutableGraphModel gm = (MutableGraphModel) dia.getGraphModel();
            return gm.canAddNode(object);
        }

        public void actionPerformed(ActionEvent ae) {
            super.actionPerformed(ae);
            Editor ce = Globals.curEditor();
            GraphModel gm = ce.getGraphModel();
            if (!(gm instanceof MutableGraphModel)) {
                return;
            }

            String instructions = null;
            if (object != null) {
                instructions =
                    Translator.localize(
                        "misc.message.click-on-diagram-to-add",
                        new Object[] {
                                Model.getFacade().toString(object),
                        });
                Globals.showStatus(instructions);
            }

            ArrayList<Object> elementsToAdd = new ArrayList<Object>(1);
            elementsToAdd.add(object);
            final ModeAddToDiagram placeMode =
                new ModeAddToDiagram(elementsToAdd, instructions);

            Globals.mode(placeMode, false);
        }
    } /* end class ActionAddExistingRelatedNode */

    /**
     * A JMenuItem that will order by display name
     *
     * @author Bob Tarling
     */
    private class OrderedMenuItem extends JMenuItem implements Comparable {

        /**
         * Instantiate OrderedMenuItem
         * @param action
         */
        public OrderedMenuItem(Action action) {
            super(action);
        }

        /**
         * Instantiate OrderedMenuItem
         * @param name
         */
        public OrderedMenuItem(String name) {
            super(name);
            setName(name);
        }

        public int compareTo(Object o) {
            JMenuItem other = (JMenuItem) o;
            return toString().compareTo(other.toString());
        }
    }

    /**
     * A JMenuItem that will order by display name
     *
     * @author Bob Tarling
     */
    private class OrderedMenu extends JMenu implements Comparable {

        /**
         * Instantiate OrderedMenu
         * @param name
         */
        public OrderedMenu(String name) {
            super(name);
        }

        public int compareTo(Object o) {
            JMenuItem other = (JMenuItem) o;
            return toString().compareTo(other.toString());
        }
    }

    /**
     * An action to create an association between 2 or more model elements.
     *
     * @author Bob Tarling
     */
    private class ActionCreateAssociation extends AbstractAction {

        private Object metaType;
        private List classifiers;

        public ActionCreateAssociation(
                Object theMetaType,
                List classifiersList) {
            super(menuLocalize("menu.popup.create") + " "
                    + Model.getMetaTypes().getName(theMetaType));
            this.metaType = theMetaType;
            this.classifiers = classifiersList;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                Object newElement = Model.getUmlFactory().buildConnection(
                    metaType,
                    classifiers.get(0),
                    null,
                    classifiers.get(1),
                    null,
                    null,
                    null);
                for (int i = 2; i < classifiers.size(); ++i) {
                    Model.getUmlFactory().buildConnection(
                            Model.getMetaTypes().getAssociationEnd(),
                            newElement,
                            null,
                            classifiers.get(i),
                            null,
                            null,
                            null);
                }
            } catch (IllegalModelElementConnectionException e1) {
                LOG.log(Level.SEVERE, "Exception", e1);
            }
        }
    }


    /**
     * An action to create an association between 2 or more model elements.
     *
     * @author Bob Tarling
     */
    private class ActionCreateAssociationRole extends AbstractAction {

        private Object metaType;
        private List classifierRoles;

        public ActionCreateAssociationRole(
                Object theMetaType,
                List classifierRolesList) {
            super(menuLocalize("menu.popup.create") + " "
                    + Model.getMetaTypes().getName(theMetaType));
            this.metaType = theMetaType;
            this.classifierRoles = classifierRolesList;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                Object newElement = Model.getUmlFactory().buildConnection(
                        metaType,
                        classifierRoles.get(0),
                        null,
                        classifierRoles.get(1),
                        null,
                        null,
                        null);
                for (int i = 2; i < classifierRoles.size(); ++i) {
                    Model.getUmlFactory().buildConnection(
                            Model.getMetaTypes().getAssociationEndRole(),
                            newElement,
                            null,
                            classifierRoles.get(i),
                            null,
                            null,
                            null);
                }
            } catch (IllegalModelElementConnectionException e1) {
                LOG.log(Level.SEVERE, "Exception", e1);
            }
        }
    }
}
