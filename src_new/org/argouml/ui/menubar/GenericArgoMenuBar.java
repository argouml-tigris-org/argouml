// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

package org.argouml.ui.menubar;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.argouml.application.api.Argo;
import org.argouml.application.api.PluggableMenu;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoModuleEvent;
import org.argouml.application.events.ArgoModuleEventListener;
import org.argouml.i18n.Translator;
import org.argouml.ui.ActionAutoResize;
import org.argouml.ui.ActionSaveConfiguration;
import org.argouml.ui.ActionSettings;
import org.argouml.ui.Actions;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.ZoomSliderButton;
import org.argouml.ui.cmd.CmdSetPreferredSize;
import org.argouml.ui.targetmanager.NavigateTargetBackAction;
import org.argouml.ui.targetmanager.NavigateTargetForwardAction;
import org.argouml.uml.ui.ActionActivityDiagram;
import org.argouml.uml.ui.ActionClassDiagram;
import org.argouml.uml.ui.ActionCollaborationDiagram;
import org.argouml.uml.ui.ActionCopy;
import org.argouml.uml.ui.ActionCut;
import org.argouml.uml.diagram.ui.ActionDeleteFromDiagram;
import org.argouml.uml.ui.ActionDeploymentDiagram;
import org.argouml.uml.ui.ActionExit;
import org.argouml.uml.ui.ActionGenerateAll;
import org.argouml.uml.ui.ActionGenerateOne;
import org.argouml.uml.ui.ActionGenerateProjectCode;
import org.argouml.uml.ui.ActionGenerationSettings;
import org.argouml.uml.ui.ActionImportFromSources;
import org.argouml.uml.ui.ActionNew;
import org.argouml.uml.ui.ActionOpenProject;
import org.argouml.uml.ui.ActionPaste;
import org.argouml.uml.ui.ActionRemoveFromModel;
import org.argouml.uml.ui.ActionRevertToSaved;
import org.argouml.uml.ui.ActionSaveGraphics;
import org.argouml.uml.ui.ActionSaveProject;
import org.argouml.uml.ui.ActionSaveProjectAs;
import org.argouml.uml.ui.ActionSequenceDiagram;
import org.argouml.uml.ui.ActionStateDiagram;
import org.argouml.uml.ui.ActionUseCaseDiagram;
import org.tigris.gef.base.CmdAdjustGrid;
import org.tigris.gef.base.CmdAdjustGuide;
import org.tigris.gef.base.CmdAdjustPageBreaks;
import org.tigris.gef.base.CmdSelectAll;
import org.tigris.gef.base.CmdSelectInvert;
import org.tigris.gef.base.CmdZoom;
import org.tigris.toolbar.ToolBar;

/**
 * GenericArgoMenuBar defines the menubar for all
 * operating systems which do not explicitely ask
 * for a different kind of menu bar, such as
 * Mac OS X.<p>
 *
 * Menu's and the mnemonics of menu's and the menuitems are separated 
 * in the PropertyResourceBundle <b>menu.properties<b/>.<p>
 *
 * menuitems are separated in the PropertyResourceBundle 
 * <b>action.properties<b/>.<p>
 *
 * The key's in menu.properties have the following structure:<p>
 * <pre>
 * menu:                    [file].[name of menu]
 *  e.g:                    menu.file
 *
 * mnemonics of menu's:     [file].[name of menu].mnemonic
 *  e.g:                    menu.file.mnemonic
 *
 * mnemonics of menuitems:  [file].[flag for item].[name of menuitem].mnemonic
 *  e.g:                    menu.item.new.mnemonic
 * </pre>
 */
public class GenericArgoMenuBar extends JMenuBar
    implements ArgoModuleEventListener {

    /** Name and prepareKey-Strings of/for the PropertyResourceBundle 
     * menu.properties.
     * MENU     = Prefix for menu-keys
     * MENUITEM = Prefix for menuitem-keys
     */
    private static final String MENU = "menu.";
    private static final String MENUITEM = "menu.item.";

    private JToolBar fileToolbar;
    private JToolBar editToolbar;
    private JToolBar viewToolbar;
    private JToolBar createDiagramToolbar;
    
    /** lru project list */
    private LastRecentlyUsedMenuList lruList = null;

    /** Edit menu
     */
    private JMenu edit = null;
    /** unknown where this appears in the UI
     */
    private JMenu select = null;
    /** toolbar: view under which is the goto diagram/ find
     * zoom!!! this should be activated as a right click command.
     * editor tabs/details tabs/ adjust grid etc.
     */
    private ArgoJMenu view = null;
    /** Toolbar:create diagram
     */
    private JMenu createDiagrams = null;
    /** currently disactivated
     */
    private JMenu tools = null;
    /** currently supports rudimentary java generation,
     * new modules for php and html/javadocs are planned
     * feel free to contribute here!
     */
    private JMenu generate = null;
    /** this should be invoked automatically when
     * importing sources.
     */
    private ArgoJMenu arrange = null;
    /** currently undergoing significant testing
     */
    private ArgoJMenu critique = null;
    /** It needs it. Currently there is only an
     * about text. hyperlinking to online docs at
     * argouml.org considered basic improvement.
     */
    private JMenu help = null;


    /**
     * The constructor.
     * 
     */
    public GenericArgoMenuBar() {
        initMenus();
    }

    /** This should be a user specified option. New laws
     * for handicapped people who cannot use the
     * mouse require software developers in US to
     * make all components of User interface accessible
     * through keyboard
     * 
     * @param item is the JMenuItem to do this for.
     * @param key is the key that we do this for.
     */
    protected static final void setMnemonic(JMenuItem item, String key)
    {
	String propertykey = new String();
	if (item instanceof JMenu) {
	    propertykey = MENU + prepareKey(key) + ".mnemonic";
	}
	else {
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
     * @param key the key to localize
     * @return the localized string
     */
    protected static final String menuLocalize(String key) {
	return Translator.localize(MENU + prepareKey(key));
    }

    static final void setAccelerator(JMenuItem item, KeyStroke keystroke) {
        if (keystroke != null) {
            item.setAccelerator(keystroke);
        }
    }


    /** Scans through all loaded modules to see if it has an item to add
     * in this diagram.
     *
     * @param menuitem The menuitem which this menuitem would attach to.
     * @param key Non-localized string that tells the module where we are.
     */
    private void appendPluggableMenus(JMenuItem menuitem, String key) {
        Object[] context = {
	    menuitem, key 
	};
        ArrayList arraylist = Argo.getPlugins(PluggableMenu.class, context);
        ListIterator iterator = arraylist.listIterator();
        while (iterator.hasNext()) {
            PluggableMenu module = (PluggableMenu) iterator.next();
            menuitem.add(module.getMenuItem(context));
            menuitem.setEnabled(true);
        }
    }

    /**
     * @see ArgoModuleEventListener#moduleLoaded(ArgoModuleEvent)
     */
    public void moduleLoaded(ArgoModuleEvent event) {
        if (event.getSource() instanceof PluggableMenu) {
            PluggableMenu module = (PluggableMenu) event.getSource();
            Object[] context = new Object[] {
		tools, "Tools" 
	    };
            if (module.inContext(context)) {
                tools.add(module.getMenuItem(context));
                tools.setEnabled(true);
            }
            //context = new Object[] { _import, "File:Import" };
            //if (module.inContext(context)) {
	    //  _import.add(module.getMenuItem(context));
            //}
            context = new Object[] {
		generate, "Generate" 
	    };
            if (module.inContext(context)) {
                generate.add(module.getMenuItem(context));
            }
            context = new Object[] {
		edit, "Edit" 
	    };
            if (module.inContext(context)) {
                edit.add(module.getMenuItem(context));
            }
            context = new Object[] {
		view, "View" 
	    };
            if (module.inContext(context)) {
      	        view.add(module.getMenuItem(context));
            }
            context = new Object[] {
		createDiagrams, "Create Diagrams" 
	    };
            if (module.inContext(context)) {
       	        createDiagrams.add(module.getMenuItem(context));
            }
            context = new Object[] {
		arrange, "Arrange" 
	    };
            if (module.inContext(context)) {
       	        arrange.add(module.getMenuItem(context));
            }
            context = new Object[] {
		help, "Help" 
	    };
            if (module.inContext(context)) {
                if (help.getItemCount() == 1) {
                    help.insertSeparator(0);
                }
                help.insert(module.getMenuItem(context), 0);
            }
        }
    }

    /**
     * @see ArgoModuleEventListener#moduleUnloaded(ArgoModuleEvent)
     */
    public void moduleUnloaded(ArgoModuleEvent event) {
        // TODO:  Disable menu
    }

    /**
     * @see ArgoModuleEventListener#moduleEnabled(ArgoModuleEvent)
     */
    public void moduleEnabled(ArgoModuleEvent event) {
        // TODO:  Enable menu
    }

    /**
     * @see ArgoModuleEventListener#moduleDisabled(ArgoModuleEvent)
     */
    public void moduleDisabled(ArgoModuleEvent event) {
        // TODO:  Disable menu
    }

    /**
     * Construct the ordinary all purpose Argo Menu Bar.
     */
    protected void initMenus() {
        int menuShortcut = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        // These are currently not used.
/*        KeyStroke ctrlMinus =
	    KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, menuShortcut);
        KeyStroke ctrlEquals =
	    KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, menuShortcut);
        KeyStroke f3 = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
        KeyStroke altLeft =
	    KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.ALT_MASK);
        KeyStroke altRight =
	    KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.ALT_MASK);
*/


        // ------------------------------------- File Menu
        initMenuFile(menuShortcut);

	// ------------------------------------- Edit Menu
        initMenuEdit(menuShortcut);
		
	// ------------------------------------- View Menu
        initMenuView(menuShortcut);

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

        ArgoEventPump.addListener(ArgoEventTypes.ANY_MODULE_EVENT, this);
    }

    /**
     * Build the menu "File".
     * 
     * @param mask menu shortcut key mask
     */
    private void initMenuFile(int mask) {
        
        KeyStroke ctrlN = KeyStroke.getKeyStroke(KeyEvent.VK_N, mask);
        KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, mask);
        KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S, mask);
        KeyStroke ctrlP = KeyStroke.getKeyStroke(KeyEvent.VK_P, mask);
        KeyStroke altF4 =
            KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK);
        
        JMenu file = new JMenu(menuLocalize("File"));
        add(file);
        setMnemonic(file, "File");
        fileToolbar = new ToolBar("File Toolbar");
        fileToolbar.putClientProperty("JToolBar.isRollover",  Boolean.TRUE);
        JMenuItem newItem = file.add(ActionNew.SINGLETON);
        setMnemonic(newItem, "New");
        setAccelerator(newItem, ctrlN);
        fileToolbar.add((ActionNew.SINGLETON));
        JMenuItem openProjectItem = file.add(new ActionOpenProject());
        setMnemonic(openProjectItem, "Open");
        setAccelerator(openProjectItem, ctrlO);
        fileToolbar.add(new ActionOpenProject());
        file.addSeparator();

        JMenuItem saveProjectItem = file.add(ActionSaveProject.SINGLETON);
        setMnemonic(saveProjectItem, "Save");
        setAccelerator(saveProjectItem, ctrlS);
        fileToolbar.add((ActionSaveProject.SINGLETON));
        JMenuItem saveProjectAsItem = file.add(ActionSaveProjectAs.SINGLETON);
        setMnemonic(saveProjectAsItem, "SaveAs");
        JMenuItem revertToSavedItem = file.add(new ActionRevertToSaved());
        setMnemonic(revertToSavedItem, "Revert To Saved");
        file.addSeparator();

        //_import = new JMenu(menuLocalize("action.import"));
        //JMenuItem importProjectAsItem =
        //_import.add(ActionImportFromSources.SINGLETON);
        //appendPluggableMenus(_import,
        //PluggableMenu.KEY_FILE_IMPORT);
        JMenuItem importFromSources =
	    file.add(ActionImportFromSources.getInstance());
        setMnemonic(importFromSources, "Import");
        file.addSeparator();
    
        JMenuItem pageSetupItem = file.add(Actions.pageSetup);
	setMnemonic(pageSetupItem, "PageSetup");
        JMenuItem printItem = file.add(Actions.print);
        setMnemonic(printItem, "Print");
        setAccelerator(printItem, ctrlP);
        fileToolbar.add((Actions.print));
        JMenuItem saveGraphicsItem = file.add(new ActionSaveGraphics());
        setMnemonic(saveGraphicsItem, "SaveGraphics");
        file.addSeparator();
        JMenuItem saveConfiguration = 
            file.add(new ActionSaveConfiguration());
        setMnemonic(saveConfiguration, "Save Configuration");
        file.addSeparator();

        // add last recently used list _before_ exit menu
        lruList = new LastRecentlyUsedMenuList( file);

        // and exit menu entry starting with separator
        file.addSeparator();
        JMenuItem exitItem = file.add(ActionExit.SINGLETON);
        setMnemonic(exitItem, "Exit");
        setAccelerator(exitItem, altF4);
    }

    /**
     * Build the menu "Edit".
     * 
     * @param mask menu shortcut key mask
     */
    private void initMenuEdit(int mask) {
        
        KeyStroke ctrlA = KeyStroke.getKeyStroke(KeyEvent.VK_A, mask);
        KeyStroke ctrlC = KeyStroke.getKeyStroke(KeyEvent.VK_C, mask);
        KeyStroke ctrlV = KeyStroke.getKeyStroke(KeyEvent.VK_V, mask);
        KeyStroke ctrlX = KeyStroke.getKeyStroke(KeyEvent.VK_X, mask);
        KeyStroke delKey  = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
        KeyStroke ctrlDel = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, mask);
        
        edit = add(new JMenu(menuLocalize("Edit")));
        setMnemonic(edit, "Edit");

        select = new JMenu(menuLocalize("Select"));
	setMnemonic(select, "Select");
        edit.add(select);
        
        JMenuItem selectAllItem = select.add(new CmdSelectAll());
        setMnemonic(selectAllItem, "Select All");
        setAccelerator(selectAllItem, ctrlA);
        select.addSeparator();
        JMenuItem backItem =
	    select.add(NavigateTargetBackAction.getInstance());
	setMnemonic(backItem, "Navigate Back");
        //setAccelerator(backItem,altLeft);
        JMenuItem forwardItem =
	    select.add(NavigateTargetForwardAction.getInstance());
	setMnemonic(forwardItem, "Navigate Forward");
        //setAccelerator(forwardItem,altRight);
        select.addSeparator();
        /* MVW: The following 2 are replaced by the "Navigate Back" and
           "Navigate Forward". */
        // JMenuItem selectPrevItem = _select.add(new CmdSelectNext(false));
        // setMnemonic(selectPrevItem, "Select Previous");
        //tab
        // JMenuItem selectNextItem = _select.add(new CmdSelectNext(true));
        // setMnemonic(selectNextItem, "Select Next");
        // shift tab
        JMenuItem selectInvert = select.add(new CmdSelectInvert());
        setMnemonic(selectInvert, "Invert Selection");

        // TODO: These are not yet implemented - Bob Tarling 12 Oct 2002
        // _edit.add(Actions.Undo);
        // editToolbar.add((Actions.Undo));
        // _edit.add(Actions.Redo);
        // editToolbar.add((Actions.Redo));

        edit.addSeparator();

        JMenuItem cutItem = edit.add(ActionCut.getInstance());
        setMnemonic(cutItem, "Cut");
        setAccelerator(cutItem, ctrlX);

        JMenuItem copyItem = edit.add(ActionCopy.getInstance());
        setMnemonic(copyItem, "Copy");
        setAccelerator(copyItem, ctrlC);

        JMenuItem pasteItem = edit.add(ActionPaste.getInstance());
        setMnemonic(pasteItem, "Paste");
        setAccelerator(pasteItem, ctrlV);

        edit.addSeparator();

        JMenuItem removeItem = 
            edit.add(ActionDeleteFromDiagram.getSingleton());
        setMnemonic(removeItem, "Remove from Diagram");
        setAccelerator(removeItem, delKey);

        JMenuItem deleteItem = edit.add(new ActionRemoveFromModel());
        setMnemonic(deleteItem, "Delete from Model");
        setAccelerator(deleteItem, ctrlDel);
        // TODO: Bob Tarling: no toolbarbutton till a new one is
        // designed for Erase
        //_editToolbar.add(ActionRemoveFromModel.SINGLETON);

        // TODO: MVW: The trash is not yet implemented. Hence remove for now...
        // See issue 2471.
        //JMenuItem emptyItem = _edit.add(ActionEmptyTrash.SINGLETON);
        //setMnemonic(emptyItem, "Empty Trash");

        edit.addSeparator();

        JMenuItem settingsItem = edit.add(new ActionSettings());
	setMnemonic(settingsItem, "Settings");
    }

    /**
     * Build the menu "View".
     * 
     * @param mask menu shortcut key mask
     */
    private void initMenuView(int mask) {
        
        KeyStroke ctrlMinus = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, mask);
        KeyStroke ctrlEquals = KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, mask);
        KeyStroke f3 = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
        
        view = (ArgoJMenu) add(new ArgoJMenu(menuLocalize("View")));
        setMnemonic(view, "View");

        JMenuItem gotoDiagram = view.add(Actions.gotoDiagram);
        setMnemonic(gotoDiagram, "Goto-Diagram");
        
        JMenuItem findItem =  view.add(Actions.find);
        setMnemonic(findItem, "Find");
        setAccelerator(findItem, f3);

        view.addSeparator();

        JMenu zoom = (JMenu) view.add(new JMenu(menuLocalize("Zoom")));
        setMnemonic(zoom, "Zoom");
      
        JMenuItem zoomOut = zoom.add(new CmdZoom(0.9));
        setMnemonic(zoomOut, "Zoom Out");
        zoomOut.setAccelerator(ctrlMinus);

        JMenuItem zoomReset = zoom.add(new CmdZoom(0.0));
	setMnemonic(zoomReset, "Zoom Reset");

        JMenuItem zoomIn = zoom.add(new CmdZoom((1.0) / (0.9)));
	setMnemonic(zoomIn, "Zoom In");
        zoomIn.setAccelerator(ctrlEquals);

        //_view.addSeparator();    //MVW   Issue 2321 and 2322

        //JMenu editTabs =  //MVW
	//    (JMenu) _view.add(new JMenu(menuLocalize("Editor Tabs"))); //MVW
	//setMnemonic(editTabs, "Editor Tabs");  //MVW
        // JMenu detailsTabs = (JMenu) _view.add(new
        // JMenu(menuLocalize("Details Tabs")));

        view.addSeparator();
        JMenuItem adjustGrid = view.add(new CmdAdjustGrid());
        setMnemonic(adjustGrid, "Adjust Grid");
        JMenuItem adjustGuide = view.add(new CmdAdjustGuide());
        setMnemonic(adjustGuide, "Adjust Guide");
        JMenuItem adjustPageBreaks = view.add(new CmdAdjustPageBreaks());
        setMnemonic(adjustPageBreaks, "Adjust Pagebreaks");
        JMenuItem buttonsOnSelection = 
            view.addCheckItem(Actions.showRapidButtons);
        setMnemonic(buttonsOnSelection, "Buttons on Selection");
        //_view.addCheckItem(Actions.ShowDiagramList);
        //_view.addCheckItem(Actions.ShowToDoList);
        //_showDetailsMenuItem = _view.addCheckItem(Actions.ShowDetails);

        view.addSeparator();
        JMenu notation = (JMenu) view.add(
            org.argouml.language.ui.ActionNotation.getInstance().getMenu());
	setMnemonic(notation, "Notation");


        appendPluggableMenus(view, PluggableMenu.KEY_VIEW);
    }

    /**
     * Build the menu "Create".
     */
    private void initMenuCreate() {
        createDiagrams = add(new JMenu(menuLocalize("Create Diagram")));
        setMnemonic(createDiagrams, "Create Diagram");
        createDiagramToolbar = new ToolBar("Create Diagram Toolbar");
        createDiagramToolbar.putClientProperty("JToolBar.isRollover",  
                                                Boolean.TRUE);
        JMenuItem classDiagram = 
            createDiagrams.add(ActionClassDiagram.SINGLETON);
        setMnemonic(classDiagram, "Class Diagram");
        createDiagramToolbar.add((ActionClassDiagram.SINGLETON));
        JMenuItem usecaseDiagram = 
            createDiagrams.add(ActionUseCaseDiagram.SINGLETON);
        setMnemonic(usecaseDiagram, "Usecase Diagram");
        createDiagramToolbar.add((ActionUseCaseDiagram.SINGLETON));
        JMenuItem stateDiagram = 
            createDiagrams.add(ActionStateDiagram.SINGLETON);
        setMnemonic(stateDiagram, "State Diagram");
        createDiagramToolbar.add((ActionStateDiagram.SINGLETON));
        JMenuItem activityDiagram = 
            createDiagrams.add(ActionActivityDiagram.SINGLETON);
        setMnemonic(activityDiagram, "Activity Diagram");
        createDiagramToolbar.add((ActionActivityDiagram.SINGLETON));
        JMenuItem collaborationDiagram = 
            createDiagrams.add(ActionCollaborationDiagram.SINGLETON);
        setMnemonic(collaborationDiagram, "Collaboration Diagram");
        createDiagramToolbar.add((ActionCollaborationDiagram.SINGLETON));
        JMenuItem deploymentDiagram = 
            createDiagrams.add(ActionDeploymentDiagram.SINGLETON);
        setMnemonic(deploymentDiagram, "Deployment Diagram");
        createDiagramToolbar.add((ActionDeploymentDiagram.SINGLETON));
        JMenuItem sequenzDiagram = 
            createDiagrams.add(ActionSequenceDiagram.SINGLETON);
        setMnemonic(sequenzDiagram, "Sequenz Diagram");
        createDiagramToolbar.add((ActionSequenceDiagram.SINGLETON));
        appendPluggableMenus(createDiagrams,
			     PluggableMenu.KEY_CREATE_DIAGRAMS);
    }

    /**
     * Build the menu "Arrange".
     */
    private void initMenuArrange() {
        arrange = (ArgoJMenu) add(new ArgoJMenu(menuLocalize("Arrange")));
        setMnemonic(arrange, "Arrange");

        JMenu align = 
	    (JMenu) arrange.add(new JMenu(menuLocalize("Align")));
        setMnemonic(align, "Align");
        JMenu distribute =
	    (JMenu) arrange.add(new JMenu(menuLocalize("Distribute")));
	setMnemonic(distribute, "Distribute");
        JMenu reorder =
	    (JMenu) arrange.add(new JMenu(menuLocalize("Reorder")));
	setMnemonic(reorder, "Reorder");
        JMenu nudge = 
	    (JMenu) arrange.add(new JMenu(menuLocalize("Nudge")));
        setMnemonic(nudge, "Nudge");

        JMenuItem preferredSize = arrange.
            add(new CmdSetPreferredSize(CmdSetPreferredSize.MINIMUM_SIZE));
        setMnemonic(preferredSize, "Preferred Size");

        JMenuItem autoResize = 
	    arrange.addCheckItem(new ActionAutoResize());
	setMnemonic(autoResize, "Toggle Auto Resize");
		
        JMenu layout = (JMenu) arrange.add(new JMenu(menuLocalize("Layout")));
        setMnemonic(layout, "Layout");
        appendPluggableMenus(arrange, PluggableMenu.KEY_ARRANGE);

        Runnable initLater = new InitMenusLater(align, distribute,
                                                reorder, nudge,
                                                layout);  

        org.argouml.application.Main.addPostLoadAction(initLater);
    }

    /**
     * Build the menu "Generation".
     */
    private void initMenuGeneration() {
        
        KeyStroke f7 = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);
        
        generate = add(new JMenu(menuLocalize("Generation")));
        setMnemonic(generate, "Generation");
        JMenuItem genOne = generate.add(ActionGenerateOne.getInstance());
        setMnemonic(genOne, "Generate Selected Classes");
        JMenuItem genAllItem = generate.add(new ActionGenerateAll());
        setMnemonic(genAllItem, "Generate all classes");
        setAccelerator(genAllItem, f7);
        generate.addSeparator();
        JMenuItem genProject = 
            generate.add(ActionGenerateProjectCode.getInstance());
        setMnemonic(genProject, "Generate code for project");
        JMenuItem generationSettings = 
            generate.add(ActionGenerationSettings.getInstance());
        setMnemonic(generationSettings, "Settings for project code generation");
        //generate.add(Actions.GenerateWeb);
        appendPluggableMenus(generate, PluggableMenu.KEY_GENERATE);
    }

    /**
     * Build the menu "Critique".
     */
    private void initMenuCritique() {
        critique = (ArgoJMenu) add(new ArgoJMenu(menuLocalize("Critique")));
        setMnemonic(critique, "Critique");
        JMenuItem toggleAutoCritique =
	    critique.addCheckItem(Actions.autoCritique);
        setMnemonic(toggleAutoCritique, "Toggle Auto Critique");
        critique.addSeparator();
        JMenuItem designIssues = critique.add(Actions.openDecisions);
        setMnemonic(designIssues, "Design Issues");
        JMenuItem designGoals = critique.add(Actions.openGoals);
        setMnemonic(designGoals, "Design Goals");
        JMenuItem browseCritics = critique.add(Actions.openCritics);
        setMnemonic(browseCritics, "Browse Critics");
    }

    /**
     * Build the menu "Tools".
     */
    private void initMenuTools() {
        tools = new JMenu(menuLocalize("Tools"));
        setMnemonic(tools, "Tools");
        tools.setEnabled(false);
        appendPluggableMenus(tools, PluggableMenu.KEY_TOOLS);
        add(tools);
        
        // tools.add(ActionTest.getInstance());
    }

    /**
     * Build the menu "Help".
     */
    private void initMenuHelp() {
        help = new JMenu(menuLocalize("Help"));
        setMnemonic(help, "Help");
        appendPluggableMenus(help, PluggableMenu.KEY_HELP);
        if (help.getItemCount() > 0) {
            help.insertSeparator(0);
        }

        JMenuItem systemInfo = help.add(Actions.systemInfo);
        setMnemonic(systemInfo, "System Information");
        help.addSeparator();
        JMenuItem aboutArgoUML = help.add(Actions.aboutArgoUML);
        setMnemonic(aboutArgoUML, "About ArgoUML");

        //setHelpMenu(help);
        add(help);
    }

    /** Get the create diagram toolbar.
     * @return Value of property _createDiagramToolbar.
     */
    public JToolBar getCreateDiagramToolbar() {
        return createDiagramToolbar;
    }
    
    /** Get the edit toolbar.
     * @return the edit toolbar.
     */
    public JToolBar getEditToolbar() {
        if (editToolbar == null) {
            editToolbar = new ToolBar("Edit Toolbar");
            editToolbar.putClientProperty("JToolBar.isRollover",
					   Boolean.TRUE);
            editToolbar.add(ActionCut.getInstance());
            editToolbar.add(ActionCopy.getInstance());
            editToolbar.add(ActionPaste.getInstance());
            editToolbar.addFocusListener(ActionPaste.getInstance());
            editToolbar.add(ActionDeleteFromDiagram.getSingleton());
            editToolbar.add(NavigateTargetBackAction.getInstance());
            editToolbar.add(NavigateTargetForwardAction.getInstance());
        }
        return editToolbar;
    }
    
    /** Getter for the file toolbar.
     * @return the file toolbar.
     *
     */
    public JToolBar getFileToolbar() {
        return fileToolbar;
    }
    
    /** Getter for the view toolbar.
     * @return the view toolbar.
     */
    public JToolBar getViewToolbar() {
        if (viewToolbar == null) {
            viewToolbar = new ToolBar("View Toolbar");
            viewToolbar.putClientProperty("JToolBar.isRollover",
					   Boolean.TRUE);
            viewToolbar.add((Actions.find));
            viewToolbar.add(new ZoomSliderButton());
        }
        return viewToolbar;
    }
    
    /**
     * Prepares one part of the key for menu- or/and menuitem-mnemonics used
     * in menu.properties.
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
     * @param filename of the project
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
}
