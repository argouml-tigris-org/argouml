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

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.Notation;
import org.argouml.application.api.QuadrantPanel;
import org.argouml.kernel.PredAND;
import org.argouml.kernel.PredInstanceOf;
import org.argouml.kernel.PredNotInstanceOf;
import org.argouml.kernel.PredOR;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.uml.behavioralelements.statemachines.StateMachinesHelper;
import org.argouml.swingext.Toolbar;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.collaboration.ui.GoAssocRoleMessages;
import org.argouml.uml.diagram.collaboration.ui.GoClassifierToCollaboration;
import org.argouml.uml.diagram.collaboration.ui.GoCollaborationDiagram;
import org.argouml.uml.diagram.collaboration.ui.GoCollaborationInteraction;
import org.argouml.uml.diagram.collaboration.ui.GoInteractionMessage;
import org.argouml.uml.diagram.collaboration.ui.GoMessageAction;
import org.argouml.uml.diagram.collaboration.ui.GoModelToCollaboration;
import org.argouml.uml.diagram.collaboration.ui.GoOperationToCollaboration;
import org.argouml.uml.diagram.collaboration.ui.GoProjectToCollaboration;
import org.argouml.uml.diagram.deployment.ui.GoDiagramToNode;
import org.argouml.uml.diagram.sequence.ui.GoLinkStimuli;
import org.argouml.uml.diagram.sequence.ui.GoStimulusToAction;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
import org.argouml.uml.diagram.state.PredIsFinalState;
import org.argouml.uml.diagram.state.PredIsStartState;
import org.argouml.uml.diagram.state.ui.*;
import org.argouml.uml.diagram.static_structure.ui.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.use_case.ui.GoUseCaseToExtensionPoint;
import org.argouml.uml.ui.ActionAddPackage;
import org.argouml.uml.ui.ActionRemoveFromModel;
import org.argouml.uml.ui.ActionSetSourcePath;
import org.argouml.uml.ui.UMLAction;
import org.argouml.uml.ui.behavior.common_behavior.GoSignalToReception;
import org.argouml.uml.ui.foundation.core.GoModelElementToComment;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.ui.PopupGenerator;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;
import ru.novosoft.uml.behavior.collaborations.MCollaboration;
import ru.novosoft.uml.behavior.common_behavior.MComponentInstance;
import ru.novosoft.uml.behavior.common_behavior.MDataValue;
import ru.novosoft.uml.behavior.common_behavior.MInstance;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.common_behavior.MNodeInstance;
import ru.novosoft.uml.behavior.common_behavior.MObject;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.behavior.use_cases.MExtend;
import ru.novosoft.uml.behavior.use_cases.MInclude;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MDependency;
import ru.novosoft.uml.foundation.core.MFlow;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MRelationship;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.model_management.MPackage;
import ru.novosoft.uml.model_management.MSubsystem;

/**
 * The upper-left pane of the main Argo/UML window, shows a tree view
 * of the UML model.
 *
 * <p>The model can be viewed from different tree "Perspectives".
 *
 * <p>The public interface of this class provides the following:
 * <ol>
 *  <li>selection via getSelectedObject(), getTree().getSelectionPaths()</li>
 *  <li>model changed notification via forceUpdate()</li>
 *  <li>History via various *nav*() methods</li>
 *  <li>statistics gathering - not really used</li>
 *  <li>a collection of perspectives</li>
 * </ol>
 *
 * <p>Perspectives are now built here.
 *
 * $Id$
 */
public class NavigatorPane
    extends JPanel
    implements ItemListener, PropertyChangeListener, QuadrantPanel, MElementListener {

    protected static Category cat = Category.getInstance(NavigatorPane.class);

    public static final int MAX_HISTORY = 10;

    /** for collecting user statistics */
    public static int _clicksInNavPane = 0;
    /** for collecting user statistics */
    public static int _navPerspectivesChanged = 0;

    ////////////////////////////////////////////////////////////////
    // instance variables

    /** the java.swing.JTree component */
    protected DisplayTextTree _tree;

    /** toolbar for history navigation and perspectives config dialog*/
    protected Toolbar _toolbar;

    /** selects the perspective */
    protected JComboBox _combo;

    /** the current perspective */
    protected NavPerspective _curPerspective;

    /** vector of TreeModels, that are the perspectives */
    protected Vector _perspectives;

    /** tree root object */
    protected Object _root;

    /** a vector of history items - not used - to be factored out...*/
    protected Vector _navHistory;

    /** history variable - to be factored out */
    protected int _historyIndex;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Constructs a new navigator panel.
     * 
     * <p>This panel consists of a Combobox to select a navigation
     * perspective, a JTree to display the UML model, some history
     * (back and forward arrows) buttons that are currently disabled,
     * and a configuration dialog to tailor the perspectives (but this
     * is not saved).
     */
    public NavigatorPane(boolean doSplash) {

        _combo = new JComboBox();
        _tree = new DnDNavigatorTree();
        TargetManager.getInstance().addTargetListener(_tree);
        _toolbar = new Toolbar();
        JPanel toolbarPanel = new JPanel(new BorderLayout());

        setLayout(new BorderLayout());

        _toolbar.add(_combo);
        _toolbar.add(Actions.NavBack);
        _toolbar.add(Actions.NavForw);
        _toolbar.add(Actions.NavConfig);

        _combo.addItemListener(this);

        _tree.addMouseListener(new NavigatorMouseListener());
        _tree.addTreeSelectionListener(new NavigationTreeSelectionListener());

        toolbarPanel.add(_toolbar, BorderLayout.WEST);
        add(toolbarPanel, BorderLayout.NORTH);
        add(new JScrollPane(_tree), BorderLayout.CENTER);
        setPreferredSize(
            new Dimension(
                Configuration.getInteger(
                    Argo.KEY_SCREEN_WEST_WIDTH,
                    ProjectBrowser.DEFAULT_COMPONENTWIDTH),
                0));

        Configuration.addListener(Notation.KEY_USE_GUILLEMOTS, this);
        Configuration.addListener(Notation.KEY_SHOW_STEREOTYPES, this);
        ProjectManager.getManager().addPropertyChangeListener(this);

        if (doSplash) {
            SplashScreen splash =
                ProjectBrowser.getInstance().getSplashScreen();
            splash.getStatusBar().showStatus(
                "Making NavigatorPane: Setting Perspectives");
            splash.getStatusBar().showProgress(25);
        }

        _perspectives = buildPerspectives();
        setPerspectives(_perspectives);
        //NavPerspective.getRegisteredPerspectives());

        _navHistory = new Vector();
        _historyIndex = 0;
    }

    ////////////////////////////////////////////////////////////////
    // methods

    /** needs documenting */
    public void setRoot(Object r) {
        _root = r;
        if (_curPerspective != null) {
            _curPerspective.setRoot(_root);
            _tree.setModel(_curPerspective); //forces a redraw
        }
        clearHistory();
    }
    /** needs documenting */
    public Object getRoot() {
        return _root;
    }

    /** needs documenting */
    public Vector getPerspectives() {
        return _perspectives;
    }
    /** helper method for the 
     *    - constructor
     *     - NavConfigDialog
     */
    void setPerspectives(Vector pers) {
        _perspectives = pers;
        NavPerspective oldCurPers = _curPerspective;
        if (_combo.getItemCount() > 0)
            _combo.removeAllItems();
        java.util.Enumeration persEnum = _perspectives.elements();
        while (persEnum.hasMoreElements())
            _combo.addItem(persEnum.nextElement());

        if (_perspectives == null || _perspectives.isEmpty())
            _curPerspective = null;
        else if (oldCurPers != null && _perspectives.contains(oldCurPers))
            setCurPerspective(oldCurPers);
        else
            setCurPerspective((NavPerspective) _perspectives.elementAt(0));
        updateTree();
    }

    /** needs documenting */
    public NavPerspective getCurPerspective() {
        return _curPerspective;
    }
    /** needs documenting */
    public void setCurPerspective(NavPerspective per) {
        _curPerspective = per;
        if (_perspectives == null || !_perspectives.contains(per))
            return;
        _combo.setSelectedItem(_curPerspective);
    }

    /** selection accessor - to be moved into some selection manager
     * when used by ActionAddPackage and ActionSetSourcePath
     *
     * <p>then change to private for use by the NavigatorKeyListener, below.
     */
    public Object getSelectedObject() {
        return _tree.getLastSelectedPathComponent();
    }

    /**
     * Gets all selected objects (for multiselect)
     * @return all selected objects (for multiselect)
     */
    public Object[] getSelectedObjects() {
        TreePath[] paths = _tree.getSelectionPaths();
        if (paths != null) {
            Object[] objects = new Object[paths.length];
            for (int i = 0; i < paths.length; i++) {
                objects[i] = paths[i].getLastPathComponent();
            }
            return objects;
        }
        return new Object[0];
    }

    /**
     * Notification from Argo that the model has changed and
     * the Tree view needs updating.
     *
     * TODO: More specific information needs to be provided, it is 
     * expesive to update the whole tree.
     *
     * @see org.argouml.uml.ui.ActionRemoveFromModel
     * @see org.argouml.uml.ui.ActionAddDiagram
     * @see org.argouml.uml.ui.foundation.core.PropPanelGeneralization
     * @see org.argouml.uml.ui.UMLReflectionListModel
     */
    public void forceUpdate() {
        _tree.forceUpdate();
    }

    /**
     * Countpart to forceUpdate() that only updates viewable
     * rows, instead of rebuilding the whole tree; a vast improvement
     * in performance.
     *
     * @see org.argouml.model.uml.UmlModelListener
     */
    public void forceUpdate(Object changed) {

        _tree.forceUpdate(changed);
    }

    /**
     * selection mutator - to be moved into some selection manager.
     *
     * This is pretty limited, it is really only useful for selecting
     *  the default diagram when the user does New.  A general function
     *  to select a given object would have to find the shortest path to
     *  it.
     *
     * <p>called from main()
     */
    public void setSelection(Object level1, Object level2) {
        Object objs[] = new Object[3];
        objs[0] = _root;
        objs[1] = level1;
        objs[2] = level2;
        TreePath path = new TreePath(objs);
        _tree.setSelectionPath(path);
    }

    /** needs documenting */
    public Dimension getMinimumSize() {
        return new Dimension(120, 100);
    }

    ////////////////////////////////////////////////////////////////
    // event handlers

    /** called when the user selects a perspective from the perspective
     *  combo. */
    public void itemStateChanged(ItemEvent e) {
        updateTree();
        _navPerspectivesChanged++;
    }

    // ------------- history methods --------------------

    /** history method - to be moved into some HistoryManager
     *  that is linked to the not-yet-done SelectionManager ??
     */
    public void navDown() {
        int row = _tree.getMinSelectionRow();
        if (row == _tree.getRowCount())
            row = 0;
        else
            row = row + 1;
        _tree.setSelectionRow(row);
        ProjectBrowser.getInstance().setTarget(getSelectedObject());
    }

    /** history method - to be moved into some HistoryManager
     *  that is linked to the not-yet-done SelectionManager ??
     */
    public void navUp() {
        int row = _tree.getMinSelectionRow();
        if (row == 0)
            row = _tree.getRowCount();
        else
            row = row - 1;
        _tree.setSelectionRow(row);
        ProjectBrowser.getInstance().setTarget(getSelectedObject());
    }

    /** history method - to be moved into some HistoryManager
     *  that is linked to the not-yet-done SelectionManager ??
     */
    public void clearHistory() {
        _historyIndex = 0;
        _navHistory.removeAllElements();
    }

    /** history method - to be moved into some HistoryManager
     *  that is linked to the not-yet-done SelectionManager ??
     */
    public void addToHistory(Object sel) {
        if (_navHistory.size() == 0)
            _navHistory.addElement(ProjectBrowser.getInstance().getTarget());
        while (_navHistory.size() - 1 > _historyIndex) {
            _navHistory.removeElementAt(_navHistory.size() - 1);
        }
        if (_navHistory.size() > MAX_HISTORY) {
            _navHistory.removeElementAt(0);
        }
        _navHistory.addElement(sel);
        _historyIndex = _navHistory.size() - 1;
    }
    /** history method - to be moved into some HistoryManager
     *  that is linked to the not-yet-done SelectionManager ??
     */
    public boolean canNavBack() {
        return _navHistory.size() > 0 && _historyIndex > 0;
    }
    /** history method - to be moved into some HistoryManager
     *  that is linked to the not-yet-done SelectionManager ??
     */
    public void navBack() {
        _historyIndex = Math.max(0, _historyIndex - 1);
        if (_navHistory.size() <= _historyIndex)
            return;
        Object oldTarget = _navHistory.elementAt(_historyIndex);
        ProjectBrowser.getInstance().setTarget(oldTarget);
    }

    /** history method - to be moved into some HistoryManager
     *  that is linked to the not-yet-done SelectionManager ??
     */
    public boolean canNavForw() {
        return _historyIndex < _navHistory.size() - 1;
    }
    /** history method - to be moved into some HistoryManager
     *  that is linked to the not-yet-done SelectionManager ??
     */
    public void navForw() {
        _historyIndex = Math.min(_navHistory.size() - 1, _historyIndex + 1);
        Object oldTarget = _navHistory.elementAt(_historyIndex);
        ProjectBrowser.getInstance().setTarget(oldTarget);
    }

    ////////////////////////////////////////////////////////////////
    // internal methods

    /**
     * helper method to:
     *   - itemStateChanged(),
     *   - setPerspectives(Vector pers)
     */
    protected void updateTree() {
        NavPerspective tm = (NavPerspective) _combo.getSelectedItem();
        //if (tm == _curPerspective) return;
        _curPerspective = tm;
        if (_curPerspective == null) {
            cat.warn("null perspective!");
            _tree.setVisible(false);
        } else {
            _curPerspective.setRoot(_root);
            _tree.setModel(_curPerspective);
            _tree.setVisible(true); // blinks?
        }
    }

    /** QuadrantPanel implementation */
    public int getQuadrant() {
        return Q_TOP_LEFT;
    }

    /**
     * Listen for configuration changes that could require repaint
     *  of the navigator pane, calls forceUpdate(),
     * Listens for changes of the project fired by projectmanager.
     *
     *  @since ARGO0.11.2
     */
    public void propertyChange(PropertyChangeEvent pce) {
        if (pce
            .getPropertyName()
            .equals(ProjectManager.CURRENT_PROJECT_PROPERTY_NAME)) {
            setRoot(pce.getNewValue());
            forceUpdate();
            return;
        }
        if (Notation.KEY_USE_GUILLEMOTS.isChangedProperty(pce)
            || Notation.KEY_SHOW_STEREOTYPES.isChangedProperty(pce)) {
            _tree.forceUpdate();
        }
    }

    /**
     * used as a selection accessor - to be removed.
     *
     * Returns the DisplayTextTree.
     * @return DisplayTextTree
     */
    public DisplayTextTree getTree() {
        return _tree;
    }

    // -------------- building the perspectives --------------

    /**
     * the perspectives to be chosen in the combobox are built here.
     */
    public Vector buildPerspectives() {

        // should only build once
        if (_perspectives != null)
            return _perspectives;

        // this are meant for pane-1 of NavigatorPane, they all have
        // Project as their only prerequiste.  These trees tend to be 3
        // to 5 levels deep and sometimes have recursion.
        NavPerspective packageCentric =
            new NavPerspective("combobox.item.package-centric");

        NavPerspective diagramCentric =
            new NavPerspective("combobox.item.diagram-centric");

        NavPerspective inheritanceCentric =
            new NavPerspective("combobox.item.inheritance-centric");

        NavPerspective classAssociation =
            new NavPerspective("combobox.item.class-associations");

        NavPerspective navAssociation =
            new NavPerspective("combobox.item.navigable-associations");

        NavPerspective aggregateCentric =
            new NavPerspective("combobox.item.aggregate-centric");

        NavPerspective compositeCentric =
            new NavPerspective("combobox.item.composite-centric");

        NavPerspective classStates =
            new NavPerspective("combobox.item.class-states");

        NavPerspective stateCentric =
            new NavPerspective("combobox.item.state-centric");

        NavPerspective stateTransitions =
            new NavPerspective("combobox.item.state-transitions");

        NavPerspective transitionCentric =
            new NavPerspective("combobox.item.transitions-centric");

        NavPerspective transitionPaths =
            new NavPerspective("combobox.item.transitions-paths");

        NavPerspective collabCentric =
            new NavPerspective("combobox.item.collaboration-centric");

        NavPerspective depCentric =
            new NavPerspective("combobox.item.dependency-centric");

        // These are intended for pane-2 of NavigatorPane, the tend to be
        // simple and shallow, and have something in pane-1 as a prerequiste
        // TODO i8n
        NavPerspective useCaseToExtensionPoint =
            new NavPerspective("Extension Points of Use Case");

        NavPerspective classToBehStr =
            new NavPerspective("misc.features-of-class");

        NavPerspective classToBeh = new NavPerspective("misc.methods-of-class");

        NavPerspective classToStr =
            new NavPerspective("misc.attributes-of-class");

        NavPerspective machineToState =
            new NavPerspective("misc.states-of-class");

        NavPerspective machineToTransition =
            new NavPerspective("misc.transitions-of-class");

        // TODO i8n
        NavPerspective classCentric = new NavPerspective("Class - centric");

        // -------------- GO rules --------------------------

        // Subsystem is travsersed via Classifier. Eugenio
        GoFilteredChildren modelToPackages =
            new GoFilteredChildren(
                "misc.package.subpackages",
                new GoModelToElements(),
                new PredAND(
                    new PredInstanceOf(MPackage.class),
                    new PredNotInstanceOf(MSubsystem.class)));

        GoFilteredChildren modelToClassifiers =
            new GoFilteredChildren(
                "misc.package.classifiers",
                new GoModelToElements(),
                new PredInstanceOf(MClassifier.class));

        // AssociationClass is traversed via Classifier. Eugenio
        GoFilteredChildren modelToAssociations =
            new GoFilteredChildren(
                "misc.package.associations",
                new GoModelToElements(),
                new PredAND(
                    new PredInstanceOf(MAssociation.class),
                    new PredNotInstanceOf(MAssociationClass.class)));

        GoFilteredChildren modelToGeneralizations =
            new GoFilteredChildren(
                "misc.package.generalizations",
                new GoModelToElements(),
                new PredInstanceOf(MGeneralization.class));

        // Extend and include are traversed via use case.
        GoFilteredChildren modelToExtendsAndIncludes =
            new GoFilteredChildren(
                "Package->Extends/Includes",
                new GoModelToElements(),
                new PredOR(
                    new PredInstanceOf(MExtend.class),
                    new PredInstanceOf(MInclude.class)));

        GoFilteredChildren modelToDependencies =
            new GoFilteredChildren(
                "misc.package.dependencies",
                new GoModelToElements(),
                new PredInstanceOf(MDependency.class));

        GoFilteredChildren modelToInstances =
            new GoFilteredChildren(
                "misc.package.instances",
                new GoModelToElements(),
                new PredInstanceOf(MObject.class));
        GoFilteredChildren modelToLinks =
            new GoFilteredChildren(
                "misc.package.links",
                new GoModelToElements(),
                new PredInstanceOf(MLink.class));
        GoFilteredChildren modelToCollaboration =
            new GoFilteredChildren(
                "misc.package.collaborations",
                new GoModelToElements(),
                new PredInstanceOf(MCollaboration.class));

        GoFilteredChildren modelToComponentInstance =
            new GoFilteredChildren(
                "misc.package.componentinstance",
                new GoModelToElements(),
                new PredInstanceOf(MComponentInstance.class));

        GoFilteredChildren modelToNodeInstance =
            new GoFilteredChildren(
                "misc.package.nodeinstance",
                new GoModelToElements(),
                new PredInstanceOf(MNodeInstance.class));

        GoFilteredChildren machineToFinalState =
            new GoFilteredChildren(
                "misc.state-machine.final-states",
                new GoMachineToState(),
                PredIsFinalState.TheInstance);
        GoFilteredChildren machineToInitialState =
            new GoFilteredChildren(
                "misc.state-machine.initial-states",
                new GoMachineToState(),
                PredIsStartState.TheInstance);
        transitionPaths.addSubTreeModel(machineToInitialState);

        GoFilteredChildren compositeToFinalStates =
            new GoFilteredChildren(
                "misc.state.final-substates",
                new GoCompositeStateToSubvertex(),
                PredIsFinalState.TheInstance);
        GoFilteredChildren compositeToInitialStates =
            new GoFilteredChildren(
                "misc.state.initial-substates",
                new GoCompositeStateToSubvertex(),
                PredIsStartState.TheInstance);

        // ---------------- building the perspectives

        packageCentric.addSubTreeModel(new GoProjectToModel());
        packageCentric.addSubTreeModel(new GoModelToDiagram());
        packageCentric.addSubTreeModel(new GoModelElementToComment());
        packageCentric.addSubTreeModel(modelToPackages);
        packageCentric.addSubTreeModel(modelToClassifiers);
        packageCentric.addSubTreeModel(modelToAssociations);
        packageCentric.addSubTreeModel(modelToExtendsAndIncludes);
        packageCentric.addSubTreeModel(modelToInstances);
        packageCentric.addSubTreeModel(modelToLinks);
        packageCentric.addSubTreeModel(new GoModelToCollaboration());
        packageCentric.addSubTreeModel(modelToComponentInstance);
        packageCentric.addSubTreeModel(modelToNodeInstance);
        packageCentric.addSubTreeModel(modelToGeneralizations);
        packageCentric.addSubTreeModel(modelToDependencies);
        packageCentric.addSubTreeModel(new GoUseCaseToExtensionPoint());
        packageCentric.addSubTreeModel(new GoClassifierToStructuralFeature());
        packageCentric.addSubTreeModel(new GoClassifierToBeh());
        packageCentric.addSubTreeModel(new GoCollaborationInteraction());
        packageCentric.addSubTreeModel(new GoInteractionMessage());
        packageCentric.addSubTreeModel(new GoMessageAction());
        packageCentric.addSubTreeModel(new GoSignalToReception());
        packageCentric.addSubTreeModel(new GoLinkStimuli());
        packageCentric.addSubTreeModel(new GoStimulusToAction());
        packageCentric.addSubTreeModel(new GoClassifierToCollaboration());
        packageCentric.addSubTreeModel(new GoOperationToCollaboration());
        packageCentric.addSubTreeModel(new GoOperationToCollaborationDiagram());

        // rules for statemachinediagram and activitydiagram
        packageCentric.addSubTreeModel(new GoBehavioralFeatureToStateMachine());
        packageCentric.addSubTreeModel(new GoBehavioralFeatureToStateDiagram());
        packageCentric.addSubTreeModel(new GoClassifierToStateMachine());
        packageCentric.addSubTreeModel(new GoMachineToState());
        packageCentric.addSubTreeModel(new GoCompositeStateToSubvertex());
        packageCentric.addSubTreeModel(new GoStateToInternalTrans());
        packageCentric.addSubTreeModel(new GoStateMachineToTransition());
        packageCentric.addSubTreeModel(new GoStateToDoActivity());
        packageCentric.addSubTreeModel(new GoStateToEntry());
        packageCentric.addSubTreeModel(new GoStateToExit());

        diagramCentric.addSubTreeModel(new GoProjectToDiagram());
        diagramCentric.addSubTreeModel(new GoDiagramToNode());
        diagramCentric.addSubTreeModel(new GoDiagramToEdge());
        diagramCentric.addSubTreeModel(new GoUseCaseToExtensionPoint());
        diagramCentric.addSubTreeModel(new GoClassifierToStructuralFeature());
        diagramCentric.addSubTreeModel(new GoClassifierToBeh());

        inheritanceCentric.addSubTreeModel(new GoProjectToModel());
        inheritanceCentric.addSubTreeModel(new GoModelToBaseElements());
        inheritanceCentric.addSubTreeModel(new GoGenElementToDerived());

        classAssociation.addSubTreeModel(new GoProjectToModel());
        classAssociation.addSubTreeModel(new GoModelToDiagram());
        classAssociation.addSubTreeModel(new GoModelToClass());
        classAssociation.addSubTreeModel(new GoClassToAssociatedClass());

        navAssociation.addSubTreeModel(new GoProjectToModel());
        navAssociation.addSubTreeModel(new GoModelToDiagram());
        navAssociation.addSubTreeModel(new GoModelToClass());
        navAssociation.addSubTreeModel(new GoClassToNavigableClass());

        stateCentric.addSubTreeModel(new GoProjectToStateMachine());
        stateCentric.addSubTreeModel(new GoMachineDiagram());
        stateCentric.addSubTreeModel(new GoMachineToState());
        stateCentric.addSubTreeModel(new GoCompositeStateToSubvertex());
        stateCentric.addSubTreeModel(new GoStateToIncomingTrans());
        stateCentric.addSubTreeModel(new GoStateToOutgoingTrans());

        transitionCentric.addSubTreeModel(new GoProjectToStateMachine());
        transitionCentric.addSubTreeModel(new GoMachineDiagram());
        transitionCentric.addSubTreeModel(new GoMachineToTrans());
        transitionCentric.addSubTreeModel(new GoTransitionToSource());
        transitionCentric.addSubTreeModel(new GoTransitionToTarget());

        transitionPaths.addSubTreeModel(new GoProjectToStateMachine());
        transitionPaths.addSubTreeModel(new GoMachineDiagram());
        transitionPaths.addSubTreeModel(compositeToInitialStates);
        transitionPaths.addSubTreeModel(new GoStateToDownstream());

        collabCentric.addSubTreeModel(new GoProjectToCollaboration());
        collabCentric.addSubTreeModel(new GoCollaborationDiagram());
        collabCentric.addSubTreeModel(new GoModelToElements());
        collabCentric.addSubTreeModel(new GoAssocRoleMessages());
        collabCentric.addSubTreeModel(new GoCollaborationInteraction());
        collabCentric.addSubTreeModel(new GoInteractionMessages());

        useCaseToExtensionPoint.addSubTreeModel(
            new GoUseCaseToExtensionPoint());

        classToBehStr.addSubTreeModel(new GoClassifierToStructuralFeature());
        classToBehStr.addSubTreeModel(new GoClassifierToBeh());

        classToBeh.addSubTreeModel(new GoClassifierToBeh());

        classToStr.addSubTreeModel(new GoClassifierToStructuralFeature());

        machineToState.addSubTreeModel(new GoMachineToState());

        machineToTransition.addSubTreeModel(new GoMachineToTrans());

        classCentric.addSubTreeModel(new GoProjectToModel());
        classCentric.addSubTreeModel(new GoModelToDiagram());
        classCentric.addSubTreeModel(modelToPackages);
        classCentric.addSubTreeModel(modelToClassifiers);
        classCentric.addSubTreeModel(new GoClassToSummary());
        classCentric.addSubTreeModel(new GoSummaryToAssociation());
        classCentric.addSubTreeModel(new GoSummaryToAttribute());
        classCentric.addSubTreeModel(new GoSummaryToOperation());
        classCentric.addSubTreeModel(new GoSummaryToInheritance());
        classCentric.addSubTreeModel(new GoSummaryToIncomingDependency());
        classCentric.addSubTreeModel(new GoSummaryToOutgoingDependency());

        // add each perspective to a vector for the ComboBox:
        Vector perspectives = new Vector();

        perspectives.add(packageCentric);
        perspectives.add(classCentric);
        perspectives.add(diagramCentric);
        perspectives.add(inheritanceCentric);
        perspectives.add(classAssociation);
        perspectives.add(navAssociation);
        perspectives.add(stateCentric);
        perspectives.add(transitionCentric);
        perspectives.add(transitionPaths);
        perspectives.add(collabCentric);
        perspectives.add(depCentric);

        return perspectives;
    }

    /**
     * method for navconfig dialog to remove a perspective.
     */
    public void removePerspective(NavPerspective np) {

        _perspectives.remove(np);
        setPerspectives(_perspectives);
    }

    /**
     *
     */
    public void addPerspective(NavPerspective np) {

        _perspectives.add(np);
        setPerspectives(_perspectives);
    }

    ////////////////////////////////////////////////////////////////
    // inner classes - listeners

    /** Listens to mouse events coming from the *JTree*,
     * on right click, brings up the pop-up menu.
     */
    class NavigatorMouseListener extends MouseAdapter {

        /** brings up the pop-up menu */
        public void mousePressed(MouseEvent me) {
            if (me.isPopupTrigger()) {
                me.consume();
                showPopupMenu(me);
            }
        }

        /** brings up the pop-up menu 
         *
         * <p>On Windows and Motif platforms, the user brings up a popup menu
         *    by releasing the right mouse button while the cursor is over a
         *    component that is popup-enabled.
         */
        public void mouseReleased(MouseEvent me) {
            if (me.isPopupTrigger()) {
                me.consume();
                showPopupMenu(me);
            }
        }

        /** brings up the pop-up menu */
        public void mouseClicked(MouseEvent me) {
            if (me.isPopupTrigger()) {
                me.consume();
                showPopupMenu(me);
            }
        }

        /** builds a pop-up menu for extra functionality for the Tree*/
        public void showPopupMenu(MouseEvent me) {

            JPopupMenu popup = new JPopupMenu("test");
            Object obj = getSelectedObject();
            if (obj instanceof PopupGenerator) {
                Vector actions = ((PopupGenerator) obj).getPopUpActions(me);
                for (Enumeration e = actions.elements();
                    e.hasMoreElements();
                    ) {
                    popup.add((AbstractAction) e.nextElement());
                }
            } else {
                if ((obj instanceof MClassifier && !(obj instanceof MDataType))
                    || ((obj instanceof MPackage)
                        && (obj
                            != ProjectManager
                                .getManager()
                                .getCurrentProject()
                                .getModel()))
                    || ((obj instanceof MStateVertex)
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
                                    obj))))
                    || (obj instanceof MInstance
                        && !(obj instanceof MDataValue)
                        && !(ProjectManager
                            .getManager()
                            .getCurrentProject()
                            .getActiveDiagram()
                            instanceof UMLSequenceDiagram))) {
                    UMLAction action =
                        new ActionAddExistingNode(
                            menuLocalize("menu.popup.add-to-diagram"),
                            obj);
                    action.setEnabled(action.shouldBeEnabled());
                    popup.add(action);
                }
                if ((obj instanceof MRelationship && !(obj instanceof MFlow))
                    || ((obj instanceof MLink)
                        && !(ProjectManager
                            .getManager()
                            .getCurrentProject()
                            .getActiveDiagram()
                            instanceof UMLSequenceDiagram))
                    || (obj instanceof MTransition)) {
                    UMLAction action =
                        new ActionAddExistingEdge(
                            menuLocalize("menu.popup.add-to-diagram"),
                            obj);
                    action.setEnabled(action.shouldBeEnabled());
                    popup.add(action);
                }

                if ((obj instanceof MModelElement
                    && (obj
                        != ProjectManager
                            .getManager()
                            .getCurrentProject()
                            .getModel()))
                    || obj instanceof Diagram) {
                    popup.add(ActionRemoveFromModel.SINGLETON);
                }
                if (obj instanceof MClassifier || obj instanceof MPackage) {
                    popup.add(ActionSetSourcePath.SINGLETON);
                }
                if (obj instanceof MPackage || obj instanceof MModel) {
                    popup.add(ActionAddPackage.SINGLETON);
                }
                popup.add(
                    new ActionGoToDetails(menuLocalize("action.properties")));
            }
            popup.show(_tree, me.getX(), me.getY());
        }

        /**
         * Locale a popup menu item in the navigator pane.
         *
         * @param key The key for the string to localize.
         * @return The localized string.
         */
        final private String menuLocalize(String key) {
            return Argo.localize("Tree", key);
        }

    } /* end class NavigatorMouseListener */

    /**
     * manages selecting the item to show in Argo's other
     * views based on the highlighted row.
     */
    class NavigationTreeSelectionListener implements TreeSelectionListener {

        /**
         * change in nav tree selection -> set target in project browser.
         */
        public void valueChanged(TreeSelectionEvent e) {
            Object[] selections = getSelectedObjects();
            TargetManager.getInstance().setTargets(Arrays.asList(selections));
/*
            Object sel = getSelectedObject();
            if (sel != null) {
                ProjectBrowser.getInstance().setTarget(sel);
            }
            */
        }
    }

    /**
     * If a element changes, this will be catched by this method and reflected
     * in the tree.
     * @see ru.novosoft.uml.MElementListener#listRoleItemSet(ru.novosoft.uml.MElementEvent)
     */
    public void listRoleItemSet(MElementEvent e) {
        if (e.getAddedValue() != null
            || e.getRemovedValue() != null
            || (e.getNewValue() != null
                && !e.getNewValue().equals(e.getOldValue()))) {
            forceUpdate();
        }

    }

    /**
     * If a element changes, this will be catched by this method and reflected
     * in the tree.
     * @see ru.novosoft.uml.MElementListener#propertySet(ru.novosoft.uml.MElementEvent)
     */
    public void propertySet(MElementEvent e) {
        if (e.getAddedValue() != null
            || e.getRemovedValue() != null
            || (e.getNewValue() != null
                && !e.getNewValue().equals(e.getOldValue()))) {
            forceUpdate(e.getSource());
        }

    }

    /**
     * If a element changes, this will be catched by this method and reflected
     * in the tree.
     * @see ru.novosoft.uml.MElementListener#recovered(ru.novosoft.uml.MElementEvent)
     */
    public void recovered(MElementEvent e) {
        if (e.getAddedValue() != null
            || e.getRemovedValue() != null
            || (e.getNewValue() != null
                && !e.getNewValue().equals(e.getOldValue()))) {
            forceUpdate(e.getSource());
        }

    }

    /**
     * If a element changes, this will be catched by this method and reflected
     * in the tree.
     * @see ru.novosoft.uml.MElementListener#removed(ru.novosoft.uml.MElementEvent)
     */
    public void removed(MElementEvent e) {
        if (e.getAddedValue() != null
            || e.getRemovedValue() != null
            || (e.getNewValue() != null
                && !e.getNewValue().equals(e.getOldValue()))) {
            forceUpdate(e.getSource());
        }

    }

    /**
     * If a element changes, this will be catched by this method and reflected
     * in the tree.
     * @see ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent e) {
        if (e.getAddedValue() != null
            || e.getRemovedValue() != null
            || (e.getNewValue() != null
                && !e.getNewValue().equals(e.getOldValue()))) {
            forceUpdate(e.getSource());
        }

    }

    /**
     * If a element changes, this will be catched by this method and reflected
     * in the tree.
     * @see ru.novosoft.uml.MElementListener#roleRemoved(ru.novosoft.uml.MElementEvent)
     */
    public void roleRemoved(MElementEvent e) {
        if (e.getAddedValue() != null
            || e.getRemovedValue() != null
            || (e.getNewValue() != null
                && !e.getNewValue().equals(e.getOldValue()))) {
            forceUpdate(e.getSource());
        }

    }

} /* end class NavigatorPane */
