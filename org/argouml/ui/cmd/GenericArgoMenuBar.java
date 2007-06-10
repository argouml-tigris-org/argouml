// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.ui.cmd;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.argouml.i18n.Translator;
import org.argouml.kernel.UndoEnabler;
import org.argouml.notation.ui.ActionNotation;
import org.argouml.ui.ActionExportXMI;
import org.argouml.ui.ActionImportXMI;
import org.argouml.ui.ActionProjectSettings;
import org.argouml.ui.ActionSettings;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.ZoomSliderButton;
import org.argouml.ui.explorer.ActionPerspectiveConfig;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.ActionActivityDiagram;
import org.argouml.uml.ui.ActionClassDiagram;
import org.argouml.uml.ui.ActionCollaborationDiagram;
import org.argouml.uml.ui.ActionDeleteModelElements;
import org.argouml.uml.ui.ActionDeploymentDiagram;
import org.argouml.uml.ui.ActionGenerateAll;
import org.argouml.uml.ui.ActionGenerateOne;
import org.argouml.uml.ui.ActionGenerateProjectCode;
import org.argouml.uml.ui.ActionGenerationSettings;
import org.argouml.uml.ui.ActionImportFromSources;
import org.argouml.uml.ui.ActionLayout;
import org.argouml.uml.ui.ActionOpenProject;
import org.argouml.uml.ui.ActionPaste;
import org.argouml.uml.ui.ActionRevertToSaved;
import org.argouml.uml.ui.ActionSaveAllGraphics;
import org.argouml.uml.ui.ActionSaveGraphics;
import org.argouml.uml.ui.ActionSaveProjectAs;
import org.argouml.uml.ui.ActionSequenceDiagram;
import org.argouml.uml.ui.ActionStateDiagram;
import org.argouml.uml.ui.ActionUseCaseDiagram;
import org.tigris.gef.base.AdjustPageBreaksAction;
import org.tigris.gef.base.SelectAllAction;
import org.tigris.gef.base.SelectInvertAction;
import org.tigris.gef.base.ZoomAction;
import org.tigris.toolbar.ToolBar;

/**
 * GenericArgoMenuBar defines the menubar for all operating systems which do not
 * explicitely ask for a different kind of menu bar, such as Mac OS X.
 * <p>
 * 
 * Menu's and the mnemonics of menu's and the menuitems are separated in the
 * PropertyResourceBundle <em>menu.properties</em>.
 * <p>
 * 
 * menuitems are separated in the PropertyResourceBundle
 * <em>action.properties</em>.
 * <p>
 * 
 * The key's in menu.properties have the following structure:
 * 
 * <pre>
 *   menu:                    [file].[name of menu]
 *    e.g:                    menu.file
 * 
 *   mnemonics of menu's:     [file].[name of menu].mnemonic
 *    e.g:                    menu.file.mnemonic
 * 
 *   mnemonics of menuitems:  [file].[flag for item].[name of menuitem].mnemonic
 *    e.g:                    menu.item.new.mnemonic
 * </pre>
 * 
 * TODO: Add registration for new menu items.
 */
public class GenericArgoMenuBar extends JMenuBar implements
        TargetListener {

    /**
     * The zooming factor.
     */
    public static final double ZOOM_FACTOR = 0.9;

    /**
     * Name and prepareKey-Strings of/for the i18n menu.properties. Prefix for
     * menu-keys.
     */
    private static final String MENU = "menu.";

    /**
     * Prefix for menuitem-keys.
     */
    private static final String MENUITEM = "menu.item.";

    /**
     * The toolbars.
     */
    private JToolBar fileToolbar;

    private JToolBar editToolbar;

    private JToolBar viewToolbar;

    private JToolBar createDiagramToolbar;

    /**
     * lru project list.
     */
    private LastRecentlyUsedMenuList lruList;

    /**
     * Edit menu.
     */
    private JMenu edit;

    /**
     * The Select menu is a submenu of Edit.
     */
    private JMenu select;

    /**
     * View under which is the Goto Diagram, Find, Zoom, Adjust grid etc.
     */
    private ArgoJMenu view;

    /**
     * Toolbar:create diagram.
     */
    private JMenu createDiagramMenu;

    /**
     * Currently disactivated.
     */
    private JMenu tools;

    /**
     * Supports java generation, modules for php and html/javadocs are planned
     * feel free to contribute here!
     */
    private JMenu generate;

    /**
     * This should be invoked automatically when importing sources.
     */
    private ArgoJMenu arrange;

    /**
     * The critique menu.
     */
    private ArgoJMenu critique;

    /**
     * It needs it. Currently there is only system information and an about
     * text. Hyperlinking to online docs at argouml.org is considered to be a
     * basic improvement.
     */
    private JMenu help;

    private Action navigateTargetForwardAction;

    private Action navigateTargetBackAction;

    /**
     * The constructor.
     */
    public GenericArgoMenuBar() {
        initActions();
        initMenus();
    }

    private void initActions() {
        navigateTargetForwardAction = new NavigateTargetForwardAction();
        navigateTargetBackAction = new NavigateTargetBackAction();
        TargetManager.getInstance().addTargetListener(this);
    }

    /**
     * This should be a user specified option. New laws for handicapped people
     * who cannot use the mouse require software developers in US to make all
     * components of User interface accessible through keyboard
     * 
     * @param item
     *            is the JMenuItem to do this for.
     * @param key
     *            is the key that we do this for.
     */
    protected static final void setMnemonic(JMenuItem item, String key) {
        String propertykey = "";
        if (item instanceof JMenu) {
            propertykey = MENU + prepareKey(key) + ".mnemonic";
        } else {
            propertykey = MENUITEM + prepareKey(key) + ".mnemonic";
        }

        String localMnemonic = Translator.localize(propertykey);
        char mnemonic = ' ';
        if (localMnemonic != null && localMnemonic.length() == 1) {
            mnemonic = localMnemonic.charAt(0);
        }
        item.setMnemonic(mnemonic);
    }

    /**
     * @param key
     *            the key to localize
     * @return the localized string
     */
    protected static final String menuLocalize(String key) {
        return Translator.localize(MENU + prepareKey(key));
    }
    
    /**
     * @param key the key to localize
     * @return the localized string
     */
    static final String menuItemLocalize(String key) {
        return Translator.localize(MENUITEM + prepareKey(key));
    }

    /**
     * Construct the ordinary all purpose Argo Menu Bar.
     */
    protected void initMenus() {
        // ------------------------------------- File Menu
        initMenuFile();

        // ------------------------------------- Edit Menu
        initMenuEdit();

        // ------------------------------------- View Menu
        initMenuView();

        // ------------------------------------- Create Menu
        initMenuCreate();

        // ------------------------------------- Arrange Menu
        initMenuArrange();

        // ------------------------------------- Generation Menu
        initMenuGeneration();

        // ------------------------------------- Critique Menu
        initMenuCritique();

        // ------------------------------------- Tools Menu
        initMenuTools();

        // ------------------------------------- Help Menu
        initMenuHelp();
    }

    /**
     * Build the menu "File".
     */
    private void initMenuFile() {

        JMenu file = new JMenu(menuLocalize("File"));
        add(file);
        setMnemonic(file, "File");
        fileToolbar = new ToolBar("File Toolbar");
        JMenuItem newItem = file.add(new ActionNew());
        setMnemonic(newItem, "New");
        ShortcutMgr.assignAccelerator(newItem, ShortcutMgr.ACTION_NEW_PROJECT);
        fileToolbar.add((new ActionNew()));
        JMenuItem openProjectItem = file.add(new ActionOpenProject());
        setMnemonic(openProjectItem, "Open");
        ShortcutMgr.assignAccelerator(openProjectItem,
                ShortcutMgr.ACTION_OPEN_PROJECT);
        fileToolbar.add(new ActionOpenProject());
        file.addSeparator();

        JMenuItem saveProjectItem = file.add(ProjectBrowser.getInstance()
                .getSaveAction());
        setMnemonic(saveProjectItem, "Save");
        ShortcutMgr.assignAccelerator(saveProjectItem,
                ShortcutMgr.ACTION_SAVE_PROJECT);
        fileToolbar.add((ProjectBrowser.getInstance().getSaveAction()));
        JMenuItem saveProjectAsItem = file.add(new ActionSaveProjectAs());
        setMnemonic(saveProjectAsItem, "SaveAs");
        ShortcutMgr.assignAccelerator(saveProjectAsItem,
                ShortcutMgr.ACTION_SAVE_PROJECT_AS);
        JMenuItem revertToSavedItem = file.add(new ActionRevertToSaved());
        setMnemonic(revertToSavedItem, "Revert To Saved");
        ShortcutMgr.assignAccelerator(revertToSavedItem,
                ShortcutMgr.ACTION_REVERT_TO_SAVED);
        file.addSeparator();

        ShortcutMgr.assignAccelerator(file.add(new ActionImportXMI()),
                ShortcutMgr.ACTION_IMPORT_XMI);
        ShortcutMgr.assignAccelerator(file.add(new ActionExportXMI()),
                ShortcutMgr.ACTION_EXPORT_XMI);

        JMenuItem importFromSources = file.add(ActionImportFromSources
                .getInstance());
        setMnemonic(importFromSources, "Import");
        ShortcutMgr.assignAccelerator(importFromSources,
                ShortcutMgr.ACTION_IMPORT_FROM_SOURCES);
        file.addSeparator();

        Action a = new ActionProjectSettings();
        fileToolbar.add(a);

        JMenuItem pageSetupItem = file.add(new ActionPageSetup());
        setMnemonic(pageSetupItem, "PageSetup");
        ShortcutMgr.assignAccelerator(pageSetupItem,
                ShortcutMgr.ACTION_PAGE_SETUP);

        JMenuItem printItem = file.add(new ActionPrint());
        setMnemonic(printItem, "Print");
        ShortcutMgr.assignAccelerator(printItem, ShortcutMgr.ACTION_PRINT);
        fileToolbar.add((new ActionPrint()));
        JMenuItem saveGraphicsItem = file.add(new ActionSaveGraphics());
        setMnemonic(saveGraphicsItem, "SaveGraphics");
        ShortcutMgr.assignAccelerator(saveGraphicsItem,
                ShortcutMgr.ACTION_SAVE_GRAPHICS);

        ShortcutMgr.assignAccelerator(file.add(new ActionSaveAllGraphics()),
                ShortcutMgr.ACTION_SAVE_ALL_GRAPHICS);

        file.addSeparator();

        JMenu notation = (JMenu) file.add(new ActionNotation().getMenu());
        setMnemonic(notation, "Notation");

        JMenuItem propertiesItem = file.add(new ActionProjectSettings());
        setMnemonic(propertiesItem, "Properties");
        ShortcutMgr.assignAccelerator(propertiesItem,
                ShortcutMgr.ACTION_PROJECT_SETTINGS);

        file.addSeparator();

        // add last recently used list _before_ exit menu
        lruList = new LastRecentlyUsedMenuList(file);

        // and exit menu entry starting with separator. 
        file.addSeparator();
        JMenuItem exitItem = file.add(new ActionExit());
        setMnemonic(exitItem, "Exit");
        // exit shortcut is not user configurable!
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
              InputEvent.ALT_MASK));
    }

    /**
     * Build the menu "Edit".
     */
    private void initMenuEdit() {

        edit = add(new JMenu(menuLocalize("Edit")));
        setMnemonic(edit, "Edit");

        JMenuItem undoItem = edit.add(ProjectBrowser.getInstance()
                .getUndoAction());
        setMnemonic(undoItem, "Undo");
        ShortcutMgr.assignAccelerator(undoItem, ShortcutMgr.ACTION_UNDO);
        undoItem.setVisible(UndoEnabler.isEnabled());

        JMenuItem redoItem = edit.add(ProjectBrowser.getInstance()
                .getRedoAction());
        setMnemonic(redoItem, "Redo");
        ShortcutMgr.assignAccelerator(redoItem, ShortcutMgr.ACTION_REDO);
        redoItem.setVisible(UndoEnabler.isEnabled());

        if (UndoEnabler.isEnabled()) {
            edit.addSeparator();
        }

        select = new JMenu(menuLocalize("Select"));
        setMnemonic(select, "Select");
        edit.add(select);

        JMenuItem selectAllItem = select.add(new SelectAllAction());
        setMnemonic(selectAllItem, "Select All");
        ShortcutMgr.assignAccelerator(selectAllItem,
                ShortcutMgr.ACTION_SELECT_ALL);
        select.addSeparator();
        JMenuItem backItem = select.add(navigateTargetBackAction);
        setMnemonic(backItem, "Navigate Back");
        ShortcutMgr.assignAccelerator(backItem,
                ShortcutMgr.ACTION_NAVIGATE_BACK);
        JMenuItem forwardItem = select.add(navigateTargetForwardAction);
        setMnemonic(forwardItem, "Navigate Forward");
        ShortcutMgr.assignAccelerator(forwardItem,
                ShortcutMgr.ACTION_NAVIGATE_FORWARD);
        select.addSeparator();

        JMenuItem selectInvert = select.add(new SelectInvertAction());
        setMnemonic(selectInvert, "Invert Selection");
        ShortcutMgr.assignAccelerator(selectInvert,
                ShortcutMgr.ACTION_SELECT_INVERT);

        edit.addSeparator();

        // JMenuItem cutItem = edit.add(ActionCut.getInstance());
        // setMnemonic(cutItem, "Cut");
        // setAccelerator(cutItem, ctrlX);
        //
        // JMenuItem copyItem = edit.add(ActionCopy.getInstance());
        // setMnemonic(copyItem, "Copy");
        // setAccelerator(copyItem, ctrlC);
        //
        // JMenuItem pasteItem = edit.add(ActionPaste.getInstance());
        // setMnemonic(pasteItem, "Paste");
        // setAccelerator(pasteItem, ctrlV);
        //
        // edit.addSeparator();

        Action removeFromDiagram = ProjectBrowser.getInstance()
                .getRemoveFromDiagramAction();
        JMenuItem removeItem = edit.add(removeFromDiagram);

        setMnemonic(removeItem, "Remove from Diagram");
        ShortcutMgr.assignAccelerator(removeItem,
                ShortcutMgr.ACTION_REMOVE_FROM_DIAGRAM);

        JMenuItem deleteItem = edit.add(ActionDeleteModelElements
                .getTargetFollower());
        setMnemonic(deleteItem, "Delete from Model");
        ShortcutMgr.assignAccelerator(deleteItem,
                ShortcutMgr.ACTION_DELETE_MODEL_ELEMENTS);

        edit.addSeparator();

        ShortcutMgr.assignAccelerator(edit.add(new ActionPerspectiveConfig()),
                ShortcutMgr.ACTION_PERSPECTIVE_CONFIG);

        JMenuItem settingsItem = edit.add(new ActionSettings());
        setMnemonic(settingsItem, "Settings");
        ShortcutMgr
                .assignAccelerator(settingsItem, ShortcutMgr.ACTION_SETTINGS);
    }

    /**
     * Build the menu "View".
     */
    private void initMenuView() {

        view = (ArgoJMenu) add(new ArgoJMenu(MENU + prepareKey("View")));
        setMnemonic(view, "View");

        JMenuItem gotoDiagram = view.add(new ActionGotoDiagram());
        setMnemonic(gotoDiagram, "Goto-Diagram");
        ShortcutMgr.assignAccelerator(gotoDiagram,
                ShortcutMgr.ACTION_GO_TO_DIAGRAM);

        JMenuItem findItem = view.add(new ActionFind());
        setMnemonic(findItem, "Find");
        ShortcutMgr.assignAccelerator(findItem, ShortcutMgr.ACTION_FIND);

        view.addSeparator();

        JMenu zoom = (JMenu) view.add(new JMenu(menuLocalize("Zoom")));
        setMnemonic(zoom, "Zoom");

        ZoomAction zoomOutAction = new ZoomAction(ZOOM_FACTOR);
        JMenuItem zoomOut = zoom.add(zoomOutAction);
        setMnemonic(zoomOut, "Zoom Out");
        ShortcutMgr.assignAccelerator(zoomOut, ShortcutMgr.ACTION_ZOOM_OUT);

        JMenuItem zoomReset = zoom.add(new ZoomAction(0.0));
        setMnemonic(zoomReset, "Zoom Reset");
        ShortcutMgr.assignAccelerator(zoomReset, ShortcutMgr.ACTION_ZOOM_RESET);

        ZoomAction zoomInAction = new ZoomAction((1.0) / (ZOOM_FACTOR));
        JMenuItem zoomIn = zoom.add(zoomInAction);
        setMnemonic(zoomIn, "Zoom In");
        ShortcutMgr.assignAccelerator(zoomIn, ShortcutMgr.ACTION_ZOOM_IN);

        view.addSeparator();
        
        JMenu grid = (JMenu) view.add(new JMenu(menuLocalize("Adjust Grid")));
        setMnemonic(grid, "Grid");
        List gridActions = ActionAdjustGrid.createAdjustGridActions(false);
        ButtonGroup groupGrid = new ButtonGroup();
        ActionAdjustGrid.setGroup(groupGrid);
        Iterator i = gridActions.iterator();
        while (i.hasNext()) {
            Action cmdAG = (Action) i.next();
            JRadioButtonMenuItem mi = new JRadioButtonMenuItem(cmdAG);
            groupGrid.add(mi);
            JMenuItem adjustGrid = grid.add(mi);
            setMnemonic(adjustGrid, (String) cmdAG.getValue(Action.NAME));
            ShortcutMgr.assignAccelerator(adjustGrid,
                    ShortcutMgr.ACTION_ADJUST_GRID + cmdAG.getValue("ID"));
        }

        JMenu snap = (JMenu) view.add(new JMenu(menuLocalize("Adjust Snap")));
        setMnemonic(snap, "Snap");
        List snapActions = ActionAdjustSnap.createAdjustSnapActions();
        ButtonGroup groupSnap = new ButtonGroup();
        ActionAdjustSnap.setGroup(groupSnap);
        i = snapActions.iterator();
        while (i.hasNext()) {
            Action cmdAS = (Action) i.next();
            JRadioButtonMenuItem mi = new JRadioButtonMenuItem(cmdAS);
            groupSnap.add(mi);
            JMenuItem adjustSnap = snap.add(mi);
            setMnemonic(adjustSnap, (String) cmdAS.getValue(Action.NAME));
            ShortcutMgr.assignAccelerator(adjustSnap,
                    ShortcutMgr.ACTION_ADJUST_GUIDE + cmdAS.getValue("ID"));
        }

        JMenuItem adjustPageBreaks = view.add(new AdjustPageBreaksAction());
        setMnemonic(adjustPageBreaks, "Adjust Pagebreaks");
        ShortcutMgr.assignAccelerator(adjustPageBreaks,
                ShortcutMgr.ACTION_ADJUST_PAGE_BREAKS);

        view.addSeparator();
        JMenuItem showSaved = view.add(new ActionShowXMLDump());
        setMnemonic(showSaved, "Show Saved");
        ShortcutMgr.assignAccelerator(showSaved,
                ShortcutMgr.ACTION_SHOW_XML_DUMP);
    }

    /**
     * Build the menu "Create" and the toolbar for diagram creation. These are
     * build together to guarantee that the same items are present in both, and
     * in the same sequence.
     * <p>
     * 
     * The sequence of these items was determined by issue 1821.
     */
    private void initMenuCreate() {
        createDiagramMenu = add(new JMenu(menuLocalize("Create Diagram")));
        setMnemonic(createDiagramMenu, "Create Diagram");
        createDiagramToolbar = new ToolBar("Create Diagram Toolbar");
        JMenuItem usecaseDiagram = createDiagramMenu
                .add(new ActionUseCaseDiagram());
        setMnemonic(usecaseDiagram, "Usecase Diagram");
        createDiagramToolbar.add((new ActionUseCaseDiagram()));
        ShortcutMgr.assignAccelerator(usecaseDiagram,
                ShortcutMgr.ACTION_USE_CASE_DIAGRAM);

        JMenuItem classDiagram =
            createDiagramMenu.add(new ActionClassDiagram());
        setMnemonic(classDiagram, "Class Diagram");
        createDiagramToolbar.add((new ActionClassDiagram()));
        ShortcutMgr.assignAccelerator(classDiagram,
                ShortcutMgr.ACTION_CLASS_DIAGRAM);

        JMenuItem sequenzDiagram =
            createDiagramMenu.add(new ActionSequenceDiagram());
        setMnemonic(sequenzDiagram, "Sequenz Diagram");
        createDiagramToolbar.add((new ActionSequenceDiagram()));
        ShortcutMgr.assignAccelerator(sequenzDiagram,
                ShortcutMgr.ACTION_SEQUENCE_DIAGRAM);

        JMenuItem collaborationDiagram =
            createDiagramMenu.add(new ActionCollaborationDiagram());
        setMnemonic(collaborationDiagram, "Collaboration Diagram");
        createDiagramToolbar.add((new ActionCollaborationDiagram()));
        ShortcutMgr.assignAccelerator(collaborationDiagram,
                ShortcutMgr.ACTION_COLLABORATION_DIAGRAM);

        JMenuItem stateDiagram =
            createDiagramMenu.add(new ActionStateDiagram());
        setMnemonic(stateDiagram, "Statechart Diagram");
        createDiagramToolbar.add((new ActionStateDiagram()));
        ShortcutMgr.assignAccelerator(stateDiagram,
                ShortcutMgr.ACTION_STATE_DIAGRAM);

        JMenuItem activityDiagram =
            createDiagramMenu.add(new ActionActivityDiagram());
        setMnemonic(activityDiagram, "Activity Diagram");
        createDiagramToolbar.add((new ActionActivityDiagram()));
        ShortcutMgr.assignAccelerator(activityDiagram,
                ShortcutMgr.ACTION_ACTIVITY_DIAGRAM);

        JMenuItem deploymentDiagram =
            createDiagramMenu.add(new ActionDeploymentDiagram());
        setMnemonic(deploymentDiagram, "Deployment Diagram");
        createDiagramToolbar.add((new ActionDeploymentDiagram()));
        ShortcutMgr.assignAccelerator(deploymentDiagram,
                ShortcutMgr.ACTION_DEPLOYMENT_DIAGRAM);
    }

    /**
     * Build the menu "Arrange".
     */
    private void initMenuArrange() {
        arrange = (ArgoJMenu) add(new ArgoJMenu(MENU + prepareKey("Arrange")));
        setMnemonic(arrange, "Arrange");

        JMenu align = (JMenu) arrange.add(new JMenu(menuLocalize("Align")));
        setMnemonic(align, "Align");
        JMenu distribute = (JMenu) arrange.add(new JMenu(
                menuLocalize("Distribute")));
        setMnemonic(distribute, "Distribute");
        JMenu reorder = (JMenu) arrange.add(new JMenu(menuLocalize("Reorder")));
        setMnemonic(reorder, "Reorder");

        JMenuItem preferredSize = arrange.add(new CmdSetPreferredSize());
        setMnemonic(preferredSize, "Preferred Size");
        ShortcutMgr.assignAccelerator(preferredSize,
                ShortcutMgr.ACTION_PREFERRED_SIZE);

        arrange.add(new ActionLayout());

        // This used to be deferred, but it's only 30-40 msec of work.
        InitMenusLater.initMenus(align, distribute, reorder);
    }

    /**
     * Build the menu "Generation".
     */
    private void initMenuGeneration() {

        // KeyStroke f7 = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);

        generate = add(new JMenu(menuLocalize("Generation")));
        setMnemonic(generate, "Generation");
        JMenuItem genOne = generate.add(new ActionGenerateOne());
        setMnemonic(genOne, "Generate Selected Classes");
        ShortcutMgr.assignAccelerator(genOne, ShortcutMgr.ACTION_GENERATE_ONE);
        JMenuItem genAllItem = generate.add(new ActionGenerateAll());
        setMnemonic(genAllItem, "Generate all classes");
        ShortcutMgr.assignAccelerator(genAllItem,
                ShortcutMgr.ACTION_GENERATE_ALL_CLASSES);
        generate.addSeparator();
        JMenuItem genProject = generate.add(new ActionGenerateProjectCode());
        setMnemonic(genProject, "Generate code for project");
        ShortcutMgr.assignAccelerator(genProject,
                ShortcutMgr.ACTION_GENERATE_PROJECT_CODE);
        JMenuItem generationSettings = generate
                .add(new ActionGenerationSettings());
        setMnemonic(generationSettings, "Settings for project code generation");
        ShortcutMgr.assignAccelerator(generationSettings,
                ShortcutMgr.ACTION_GENERATION_SETTINGS);
        // generate.add(Actions.GenerateWeb);
    }

    /**
     * Build the menu "Critique".
     */
    private void initMenuCritique() {
        critique =
            (ArgoJMenu) add(new ArgoJMenu(MENU + prepareKey("Critique")));
        setMnemonic(critique, "Critique");
        JMenuItem toggleAutoCritique = critique
                .addCheckItem(new ActionAutoCritique());
        setMnemonic(toggleAutoCritique, "Toggle Auto Critique");
        ShortcutMgr.assignAccelerator(toggleAutoCritique,
                ShortcutMgr.ACTION_AUTO_CRITIQUE);
        critique.addSeparator();
        JMenuItem designIssues = critique.add(new ActionOpenDecisions());
        setMnemonic(designIssues, "Design Issues");
        ShortcutMgr.assignAccelerator(designIssues,
                ShortcutMgr.ACTION_OPEN_DECISIONS);
        JMenuItem designGoals = critique.add(new ActionOpenGoals());
        setMnemonic(designGoals, "Design Goals");
        ShortcutMgr.assignAccelerator(designGoals,
                ShortcutMgr.ACTION_OPEN_GOALS);
        JMenuItem browseCritics = critique.add(new ActionOpenCritics());
        setMnemonic(browseCritics, "Browse Critics");
        ShortcutMgr.assignAccelerator(designIssues,
                ShortcutMgr.ACTION_OPEN_CRITICS);
    }

    /**
     * Build the menu "Tools".
     */
    private void initMenuTools() {
        tools = new JMenu(menuLocalize("Tools"));
        setMnemonic(tools, "Tools");

        add(tools);
    }

    /**
     * Build the menu "Help".
     */
    private void initMenuHelp() {
        help = new JMenu(menuLocalize("Help"));
        setMnemonic(help, "Help");
        if (help.getItemCount() > 0) {
            help.insertSeparator(0);
        }

        JMenuItem systemInfo = help.add(new ActionSystemInfo());
        setMnemonic(systemInfo, "System Information");
        ShortcutMgr.assignAccelerator(systemInfo,
                ShortcutMgr.ACTION_SYSTEM_INFORMATION);
        help.addSeparator();
        JMenuItem aboutArgoUML = help.add(new ActionAboutArgoUML());
        setMnemonic(aboutArgoUML, "About ArgoUML");
        ShortcutMgr.assignAccelerator(aboutArgoUML,
                ShortcutMgr.ACTION_ABOUT_ARGOUML);

        // setHelpMenu(help);
        add(help);
    }

    /**
     * Get the create diagram toolbar.
     * 
     * @return Value of property _createDiagramToolbar.
     */
    public JToolBar getCreateDiagramToolbar() {
        return createDiagramToolbar;
    }


    /**
     * Get the create diagram menu. Provided to allow plugins
     * to appeand their own actions to this menu.
     * 
     * @return Value of property createDiagramMenu
     */
    public JToolBar getCreateDiagramMenu() {
        return createDiagramToolbar;
    }

    /**
     * Get the edit toolbar.
     * 
     * @return the edit toolbar.
     */
    public JToolBar getEditToolbar() {
        if (editToolbar == null) {
            editToolbar = new ToolBar("Edit Toolbar");
            // editToolbar.add(ActionCut.getInstance());
            // editToolbar.add(ActionCopy.getInstance());
            // editToolbar.add(ActionPaste.getInstance());
            editToolbar.addFocusListener(ActionPaste.getInstance());
            editToolbar.add(ProjectBrowser.getInstance()
                    .getRemoveFromDiagramAction());
            editToolbar.add(navigateTargetBackAction);
            editToolbar.add(navigateTargetForwardAction);
        }
        return editToolbar;
    }

    /**
     * Getter for the file toolbar.
     * 
     * @return the file toolbar.
     * 
     */
    public JToolBar getFileToolbar() {
        return fileToolbar;
    }

    /**
     * Getter for the view toolbar.
     * 
     * @return the view toolbar.
     */
    public JToolBar getViewToolbar() {
        if (viewToolbar == null) {
            viewToolbar = new ToolBar("View Toolbar");
            viewToolbar.add(new ActionFind());
            viewToolbar.add(new ZoomSliderButton());
        }
        return viewToolbar;
    }

    /**
     * Prepares one part of the key for menu- or/and menuitem-mnemonics used in
     * menu.properties.
     * 
     * The method changes the parameter str to lower cases. Spaces in the
     * parameter str are changed to hyphens.
     * 
     * @param str
     * @return the prepared str
     */
    private static String prepareKey(String str) {
        StringBuffer strb = new StringBuffer(str.toLowerCase());
        for (int i = 0; i < (strb.length() - 1); i++) {
            if (strb.charAt(i) == ' ') {
                strb.setCharAt(i, '-');
            }
        }
        return strb.toString();
    }

    /**
     * Adds the entry to the lru list.
     * 
     * @param filename
     *            of the project
     */
    public void addFileSaved(String filename) {
        lruList.addEntry(filename);
    }

    /**
     * Getter for the Tools menu.
     * 
     * @return The Tools menu.
     */
    public JMenu getTools() {
        return tools;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 2904074534530273119L;

    /**
     * Target changed - update the actions that depend on the target.
     */
    private void setTarget() {
        navigateTargetForwardAction.setEnabled(navigateTargetForwardAction
                .isEnabled());
        navigateTargetBackAction.setEnabled(navigateTargetBackAction
                .isEnabled());
    }

    public void targetAdded(TargetEvent e) {
        setTarget();
    }

    public void targetRemoved(TargetEvent e) {
        setTarget();
    }

    public void targetSet(TargetEvent e) {
        setTarget();
    }
}
