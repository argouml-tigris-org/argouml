// $Id$
// Copyright (c) 1996-2003 The Regents of the University of California. All
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
//import org.argouml.uml.ui.ActionEmptyTrash;
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
import org.tigris.gef.base.CmdSelectNext;
import org.tigris.gef.base.CmdZoom;
import org.tigris.gef.util.Localizer;
import org.tigris.toolbar.ToolBar;

/** GenericArgoMenuBar defines the menubar for all
 *  operating systems which do not explicitely ask
 *  for a different kind of menu bar, such as
 *  Mac OS X.
 * 
 *  Menu's and the mnemonics of menu's and the menuitems are separated 
 *  in the PropertyResourceBundle <b>menu.properties<b/>.
 * 
 *  menuitems are separated in the PropertyResourceBundle 
 *  <b>action.properties<b/>.
 * 
 *  The key's in menu.properties have the following structure:
 * 
 *  menu:                    [file].[name of menu]
 *   e.g:                    menu.file
 * 
 *  mnemonics of menu's:     [file].[name of menu].mnemonic
 *   e.g:                    menu.file.mnemonic
 * 
 *  mnemonics of menuitems:  [file].[flag for item].[name of menuitem].mnemonic
 *   e.g:                    menu.item.new.mnemonic
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

    private JToolBar _fileToolbar;
    private JToolBar _editToolbar;
    private JToolBar _viewToolbar;
    private JToolBar _createDiagramToolbar;
    
    /** lru project list */
    private LastRecentlyUsedMenuList _lruList = null;

    /** Edit menu
     */
    protected JMenu _edit = null;
    /** unknown where this appears in the UI
     */
    protected JMenu _select = null;
    /** toolbar: view under which is the goto diagram/ find
     * zoom!!! this should be activated as a right click command.
     * editor tabs/details tabs/ adjust grid etc.
     */
    protected ArgoJMenu _view = null;
    /** Toolbar:create diagram
     */
    protected JMenu _createDiagrams = null;
    /** currently disactivated
     */
    protected JMenu _tools = null;
    /** currently supports rudimentary java generation,
     * new modules for php and html/javadocs are planned
     * feel free to contribute here!
     */
    protected JMenu _generate = null;
    /** this should be invoked automatically when
     * importing sources.
     */
    protected ArgoJMenu _arrange = null;
    /** currently undergoing significant testing
     */
    protected ArgoJMenu _critique = null;
    /** It needs it. Currently there is only an
     * about text. hyperlinking to online docs at
     * argouml.org considered basic improvement.
     */
    protected JMenu _help = null;


    public GenericArgoMenuBar() {
        initMenus();
    }

    /** Gets the shortcut for a key.
     *
     * @deprecated in 0.15.1. No longer needed since we have moved from
     *             the MenuResouceBundle.java to menu.properties.
     */
    protected static final KeyStroke getShortcut(String key) {
        return Localizer.getShortcut("CoreMenu", key);
    }

    /** This should be a user specified option. New laws
     * for handicapped people who cannot use the
     * mouse require software developers in US to
     * make all components of User interface accessible
     * through keyboard
     * 
     * @deprecated since 0.15.2. Use setMnemonic(JMenuItem item, String key) instead.
     * @author Braun.Harald
     * @since 09/14/2003
     */
    protected static final void setMnemonic(JMenuItem item,
					    String key, char defMnemonic)
    {
	String propertykey = new String();
	if (item instanceof JMenu) {
	    propertykey = MENU + prepareKey(key) + ".mnemonic";
	}
	else {
	    propertykey = MENUITEM + prepareKey(key) + ".mnemonic";
	}
		
	String localMnemonic = Translator.localize(propertykey);
        char mnemonic = defMnemonic;
        if (localMnemonic != null && localMnemonic.length() == 1) {
            mnemonic = localMnemonic.charAt(0);
        }
        item.setMnemonic(mnemonic);
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

    public void moduleLoaded(ArgoModuleEvent event) {
        if (event.getSource() instanceof PluggableMenu) {
            PluggableMenu module = (PluggableMenu) event.getSource();
            Object[] context = new Object[] {
		_tools, "Tools" 
	    };
            if (module.inContext(context)) {
                _tools.add(module.getMenuItem(context));
                _tools.setEnabled(true);
            }
            //context = new Object[] { _import, "File:Import" };
            //if (module.inContext(context)) {
	    //  _import.add(module.getMenuItem(context));
            //}
            context = new Object[] {
		_generate, "Generate" 
	    };
            if (module.inContext(context)) {
                _generate.add(module.getMenuItem(context));
            }
            context = new Object[] {
		_edit, "Edit" 
	    };
            if (module.inContext(context)) {
                _edit.add(module.getMenuItem(context));
            }
            context = new Object[] {
		_view, "View" 
	    };
            if (module.inContext(context)) {
      	        _view.add(module.getMenuItem(context));
            }
            context = new Object[] {
		_createDiagrams, "Create Diagrams" 
	    };
            if (module.inContext(context)) {
       	        _createDiagrams.add(module.getMenuItem(context));
            }
            context = new Object[] {
		_arrange, "Arrange" 
	    };
            if (module.inContext(context)) {
       	        _arrange.add(module.getMenuItem(context));
            }
            context = new Object[] {
		_help, "Help" 
	    };
            if (module.inContext(context)) {
                if (_help.getItemCount() == 1) {
                    _help.insertSeparator(0);
                }
                _help.insert(module.getMenuItem(context), 0);
            }
        }
    }

    public void moduleUnloaded(ArgoModuleEvent event) {
        // TODO:  Disable menu
    }

    public void moduleEnabled(ArgoModuleEvent event) {
        // TODO:  Enable menu
    }

    public void moduleDisabled(ArgoModuleEvent event) {
        // TODO:  Disable menu
    }



    /** construct the ordinary all purpose Argo Menu Bar.
     */
    protected void initMenus() {
        int menuShortcut = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        KeyStroke ctrlN = KeyStroke.getKeyStroke(KeyEvent.VK_N, menuShortcut);
        KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, menuShortcut);
        KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S, menuShortcut);
        KeyStroke ctrlP = KeyStroke.getKeyStroke(KeyEvent.VK_P, menuShortcut);
        KeyStroke ctrlA = KeyStroke.getKeyStroke(KeyEvent.VK_A, menuShortcut);
        KeyStroke ctrlC = KeyStroke.getKeyStroke(KeyEvent.VK_C, menuShortcut);
        KeyStroke ctrlV = KeyStroke.getKeyStroke(KeyEvent.VK_V, menuShortcut);
        KeyStroke ctrlX = KeyStroke.getKeyStroke(KeyEvent.VK_X, menuShortcut);
        KeyStroke ctrlMinus =
	    KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, menuShortcut);
        KeyStroke ctrlEquals =
	    KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, menuShortcut);

        
        KeyStroke F3 = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
        KeyStroke F7 = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);
        KeyStroke altF4 =
	    KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK);
        KeyStroke altLeft =
	    KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.ALT_MASK);
        KeyStroke altRight =
	    KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.ALT_MASK);

        KeyStroke delKey  = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
        KeyStroke ctrlDel =
	    KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, menuShortcut);

        JMenuItem mi;
        // ------------------------------------- File Menu
        
        JMenu _file = new JMenu(menuLocalize("File"));
        add(_file);
        setMnemonic(_file, "File");
        _fileToolbar = new ToolBar("File Toolbar");
        _fileToolbar.putClientProperty("JToolBar.isRollover",  Boolean.TRUE);
        JMenuItem newItem = _file.add(ActionNew.SINGLETON);
        setMnemonic(newItem, "New");
        setAccelerator(newItem, ctrlN);
        _fileToolbar.add((ActionNew.SINGLETON));
        JMenuItem openProjectItem = _file.add(ActionOpenProject.SINGLETON);
        setMnemonic(openProjectItem, "Open");
        setAccelerator(openProjectItem, ctrlO);
        _fileToolbar.add((ActionOpenProject.SINGLETON));
        _file.addSeparator();

        JMenuItem saveProjectItem = _file.add(ActionSaveProject.SINGLETON);
        setMnemonic(saveProjectItem, "Save");
        setAccelerator(saveProjectItem, ctrlS);
        _fileToolbar.add((ActionSaveProject.SINGLETON));
        JMenuItem saveProjectAsItem = _file.add(ActionSaveProjectAs.SINGLETON);
        setMnemonic(saveProjectAsItem, "SaveAs");
        JMenuItem revertToSavedItem = _file.add(ActionRevertToSaved.SINGLETON);
        setMnemonic(revertToSavedItem, "Revert To Saved");
        _file.addSeparator();

        //_import = new JMenu(menuLocalize("action.import"));
        //JMenuItem importProjectAsItem =
        //_import.add(ActionImportFromSources.SINGLETON);
        //appendPluggableMenus(_import,
        //PluggableMenu.KEY_FILE_IMPORT);
        JMenuItem importFromSources = _file.add(ActionImportFromSources.SINGLETON);
        setMnemonic(importFromSources, "Import");
        _file.addSeparator();
    
        JMenuItem pageSetupItem = _file.add(Actions.PageSetup);
	setMnemonic(pageSetupItem, "PageSetup");
        JMenuItem printItem = _file.add(Actions.Print);
        setMnemonic(printItem, "Print");
        setAccelerator(printItem, ctrlP);
        _fileToolbar.add((Actions.Print));
        JMenuItem saveGraphicsItem = _file.add(ActionSaveGraphics.SINGLETON);
        setMnemonic(saveGraphicsItem, "SaveGraphics");
        _file.addSeparator();
        JMenuItem saveConfiguration = 
            _file.add(ActionSaveConfiguration.SINGLETON);
        setMnemonic(saveConfiguration, "Save Configuration");
        _file.addSeparator();

        // add last recently used list _before_ exit menu
        _lruList = new LastRecentlyUsedMenuList( _file);

        // and exit menu entry starting with separator
        _file.addSeparator();
        JMenuItem exitItem = _file.add(ActionExit.SINGLETON);
        setMnemonic(exitItem, "Exit");
        setAccelerator(exitItem, altF4);

	// ------------------------------------- Edit Menu
		
        _edit = add(new JMenu(menuLocalize("Edit")));
        setMnemonic(_edit, "Edit");

        _select = new JMenu(menuLocalize("Select"));
	setMnemonic(_select, "Select");
        _edit.add(_select);
        
        JMenuItem selectAllItem = _select.add(new CmdSelectAll());
        setMnemonic(selectAllItem, "Select All");
        setAccelerator(selectAllItem, ctrlA);
        _select.addSeparator();
        JMenuItem backItem =
	    _select.add(NavigateTargetBackAction.getInstance());
	setMnemonic(backItem, "Navigate Back");
        //setAccelerator(backItem,altLeft);
        JMenuItem forwardItem =
	    _select.add(NavigateTargetForwardAction.getInstance());
	setMnemonic(forwardItem, "Navigate Forward");
        //setAccelerator(forwardItem,altRight);
        _select.addSeparator();
        /* MVW: The following 2 are replaced by the "Navigate Back" and
           "Navigate Forward". */
        // JMenuItem selectPrevItem = _select.add(new CmdSelectNext(false));
        // setMnemonic(selectPrevItem, "Select Previous");
        //tab
        // JMenuItem selectNextItem = _select.add(new CmdSelectNext(true));
        // setMnemonic(selectNextItem, "Select Next");
        // shift tab
        JMenuItem selectInvert = _select.add(new CmdSelectInvert());
        setMnemonic(selectInvert, "Invert Selection");

        // TODO These are not yet implemented - Bob Tarling 12 Oct 2002
        // _edit.add(Actions.Undo);
        // editToolbar.add((Actions.Undo));
        // _edit.add(Actions.Redo);
        // editToolbar.add((Actions.Redo));

        _edit.addSeparator();

        JMenuItem cutItem = _edit.add(ActionCut.getInstance());
        setMnemonic(cutItem, "Cut");
        setAccelerator(cutItem, ctrlX);

        JMenuItem copyItem = _edit.add(ActionCopy.getInstance());
        setMnemonic(copyItem, "Copy");
        setAccelerator(copyItem, ctrlC);

        JMenuItem pasteItem = _edit.add(ActionPaste.getInstance());
        setMnemonic(pasteItem, "Paste");
        setAccelerator(pasteItem, ctrlV);

        _edit.addSeparator();

        JMenuItem removeItem = _edit.add(ActionDeleteFromDiagram.SINGLETON);
        setMnemonic(removeItem, "Remove from Diagram");
        setAccelerator(removeItem, delKey);

        JMenuItem deleteItem = _edit.add(ActionRemoveFromModel.SINGLETON);
        setMnemonic(deleteItem, "Delete from Model");
        setAccelerator(deleteItem, ctrlDel);
        // TODO Bob Tarling: no toolbarbutton till a new one is
        // designed for Erase
        //_editToolbar.add(ActionRemoveFromModel.SINGLETON);

        // TODO: MVW: The trash is not yet implemented. Hence remove for now...
        // See issue 2471.
        //JMenuItem emptyItem = _edit.add(ActionEmptyTrash.SINGLETON);
        //setMnemonic(emptyItem, "Empty Trash");

        _edit.addSeparator();

        JMenuItem settingsItem = _edit.add(ActionSettings.getInstance());
	setMnemonic(settingsItem, "Settings");
		
	// ------------------------------------- View Menu
		
        _view = (ArgoJMenu) add(new ArgoJMenu(menuLocalize("View")));
        setMnemonic(_view, "View");

        JMenuItem gotoDiagram = _view.add(Actions.GotoDiagram);
        setMnemonic(gotoDiagram, "Goto-Diagram");
        
        JMenuItem findItem =  _view.add(Actions.Find);
        setMnemonic(findItem, "Find");
        setAccelerator(findItem, F3);

        _view.addSeparator();

        JMenu zoom = (JMenu) _view.add(new JMenu(menuLocalize("Zoom")));
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

        _view.addSeparator();
        JMenuItem adjustGrid = _view.add(new CmdAdjustGrid());
        setMnemonic(adjustGrid, "Adjust Grid");
        JMenuItem adjustGuide = _view.add(new CmdAdjustGuide());
        setMnemonic(adjustGuide, "Adjust Guide");
        JMenuItem adjustPageBreaks = _view.add(new CmdAdjustPageBreaks());
        setMnemonic(adjustPageBreaks, "Adjust Pagebreaks");
        JMenuItem buttonsOnSelection = 
            _view.addCheckItem(Actions.ShowRapidButtons);
        setMnemonic(buttonsOnSelection, "Buttons on Selection");
        //_view.addCheckItem(Actions.ShowDiagramList);
        //_view.addCheckItem(Actions.ShowToDoList);
        //_showDetailsMenuItem = _view.addCheckItem(Actions.ShowDetails);

        _view.addSeparator();
        JMenu notation = (JMenu) _view.add(
            org.argouml.language.ui.ActionNotation.getInstance().getMenu());
	setMnemonic(notation, "Notation");


        appendPluggableMenus(_view, PluggableMenu.KEY_VIEW);

        //JMenu create = (JMenu) add(new JMenu(menuLocalize("Create")));
        //setMnemonic(create,"Create",'C');
        //create.add(Actions.CreateMultiple);
        //create.addSeparator();

	// ------------------------------------- Create Menu
		
        _createDiagrams =
	    (JMenu) add(new JMenu(menuLocalize("Create Diagram")));
        setMnemonic(_createDiagrams, "Create Diagram");
        _createDiagramToolbar = new ToolBar("Create Diagram Toolbar");
        _createDiagramToolbar.putClientProperty("JToolBar.isRollover",  
                                                Boolean.TRUE);
        JMenuItem classDiagram = 
            _createDiagrams.add(ActionClassDiagram.SINGLETON);
        setMnemonic(classDiagram, "Class Diagram");
        _createDiagramToolbar.add((ActionClassDiagram.SINGLETON));
        JMenuItem usecaseDiagram = 
            _createDiagrams.add(ActionUseCaseDiagram.SINGLETON);
        setMnemonic(usecaseDiagram, "Usecase Diagram");
        _createDiagramToolbar.add((ActionUseCaseDiagram.SINGLETON));
        JMenuItem stateDiagram = 
            _createDiagrams.add(ActionStateDiagram.SINGLETON);
        setMnemonic(stateDiagram, "State Diagram");
        _createDiagramToolbar.add((ActionStateDiagram.SINGLETON));
        JMenuItem activityDiagram = 
            _createDiagrams.add(ActionActivityDiagram.SINGLETON);
        setMnemonic(activityDiagram, "Activity Diagram");
        _createDiagramToolbar.add((ActionActivityDiagram.SINGLETON));
        JMenuItem collaborationDiagram = 
            _createDiagrams.add(ActionCollaborationDiagram.SINGLETON);
        setMnemonic(collaborationDiagram, "Collaboration Diagram");
        _createDiagramToolbar.add((ActionCollaborationDiagram.SINGLETON));
        JMenuItem deploymentDiagram = 
            _createDiagrams.add(ActionDeploymentDiagram.SINGLETON);
        setMnemonic(deploymentDiagram, "Deployment Diagram");
        _createDiagramToolbar.add((ActionDeploymentDiagram.SINGLETON));
        JMenuItem sequenzDiagram = 
            _createDiagrams.add(ActionSequenceDiagram.SINGLETON);
        setMnemonic(sequenzDiagram, "Sequenz Diagram");
        _createDiagramToolbar.add((ActionSequenceDiagram.SINGLETON));
        appendPluggableMenus(_createDiagrams,
			     PluggableMenu.KEY_CREATE_DIAGRAMS);

	// ------------------------------------- Arrange Menu
		
        _arrange = (ArgoJMenu) add(new ArgoJMenu(menuLocalize("Arrange")));
        setMnemonic(_arrange, "Arrange");

        JMenu align = 
        (JMenu) _arrange.add(new JMenu(menuLocalize("Align")));
        setMnemonic(align, "Align");
        JMenu distribute =
	    (JMenu) _arrange.add(new JMenu(menuLocalize("Distribute")));
	setMnemonic(distribute, "Distribute");
        JMenu reorder =
	    (JMenu) _arrange.add(new JMenu(menuLocalize("Reorder")));
	setMnemonic(reorder, "Reorder");
        JMenu nudge = 
        (JMenu) _arrange.add(new JMenu(menuLocalize("Nudge")));
        setMnemonic(nudge, "Nudge");

        JMenuItem preferredSize = _arrange.
            add(new CmdSetPreferredSize(CmdSetPreferredSize.MINIMUM_SIZE));
        setMnemonic(preferredSize, "Preferred Size");

        JMenuItem autoResize = 
        _arrange.addCheckItem(new ActionAutoResize());
	setMnemonic(autoResize, "Toggle Auto Resize");
		
        JMenu layout = (JMenu) _arrange.add(new JMenu(menuLocalize("Layout")));
        setMnemonic(layout, "Layout");
        appendPluggableMenus(_arrange, PluggableMenu.KEY_ARRANGE);

        Runnable initLater = new InitMenusLater(align, distribute,
                                                reorder, nudge,
                                                layout
                                                //,editTabs //MVW  Issue 2321 and 2322
                                                );  

        org.argouml.application.Main.addPostLoadAction(initLater);

	// ------------------------------------- Generation Menu
		
        _generate = (JMenu) add(new JMenu(menuLocalize("Generation")));
        setMnemonic(_generate, "Generation");
        JMenuItem genOne = _generate.add(ActionGenerateOne.SINGLETON);
        setMnemonic(genOne, "Generate Selected Classes");
        JMenuItem genAllItem = _generate.add(ActionGenerateAll.SINGLETON);
        setMnemonic(genAllItem, "Generate all classes");
        setAccelerator(genAllItem, F7);
        _generate.addSeparator();
        JMenuItem genProject = 
            _generate.add(ActionGenerateProjectCode.SINGLETON);
        setMnemonic(genProject, "Generate code for project");
        JMenuItem generationSettings = 
            _generate.add(ActionGenerationSettings.SINGLETON);
        setMnemonic(generationSettings, "Settings for project code generation");
        //generate.add(Actions.GenerateWeb);
        appendPluggableMenus(_generate, PluggableMenu.KEY_GENERATE);

	// ------------------------------------- Critique Menu
		
        _critique = (ArgoJMenu) add(new ArgoJMenu(menuLocalize("Critique")));
        setMnemonic(_critique, "Critique");
        JMenuItem toggleAutoCritique = _critique.addCheckItem(Actions.AutoCritique);
        setMnemonic(toggleAutoCritique, "Toggle Auto Critique");
        _critique.addSeparator();
        JMenuItem designIssues = _critique.add(Actions.OpenDecisions);
        setMnemonic(designIssues, "Design Issues");
        JMenuItem designGoals = _critique.add(Actions.OpenGoals);
        setMnemonic(designGoals, "Design Goals");
        JMenuItem browseCritics = _critique.add(Actions.OpenCritics);
        setMnemonic(browseCritics, "Browse Critics");

	// ------------------------------------- Tools Menu
		
        _tools = new JMenu(menuLocalize("Tools"));
        setMnemonic(_tools, "Tools");
        _tools.setEnabled(false);
        appendPluggableMenus(_tools, PluggableMenu.KEY_TOOLS);
        add(_tools);

        // tools.add(ActionTest.getInstance());

	// ------------------------------------- Help Menu
		
        _help = new JMenu(menuLocalize("Help"));
        setMnemonic(_help, "Help");
        appendPluggableMenus(_help, PluggableMenu.KEY_HELP);
        if (_help.getItemCount() > 0) {
            _help.insertSeparator(0);
        }

        JMenuItem systemInfo = _help.add(Actions.SystemInfo);
        setMnemonic(systemInfo, "System Information");
        _help.addSeparator();
        JMenuItem aboutArgoUML = _help.add(Actions.AboutArgoUML);
        setMnemonic(aboutArgoUML, "About ArgoUML");

        //setHelpMenu(help);
        add(_help);

        ArgoEventPump.addListener(ArgoEventTypes.ANY_MODULE_EVENT, this);
    }

    /** Get the create diagram toolbar.
     * @return Value of property _createDiagramToolbar.
     */
    public JToolBar getCreateDiagramToolbar() {
        return _createDiagramToolbar;
    }
    
    /** Get the edit toolbar.
     * @return the edit toolbar.
     */
    public JToolBar getEditToolbar() {
        if (_editToolbar == null) {
            _editToolbar = new ToolBar("Edit Toolbar");
            _editToolbar.putClientProperty("JToolBar.isRollover",  Boolean.TRUE);
            _editToolbar.add(ActionCut.getInstance());
            _editToolbar.add(ActionCopy.getInstance());
            _editToolbar.add(ActionPaste.getInstance());
            _editToolbar.addFocusListener(ActionPaste.getInstance());
            _editToolbar.add(ActionDeleteFromDiagram.SINGLETON);
            _editToolbar.add(NavigateTargetBackAction.getInstance());
            _editToolbar.add(NavigateTargetForwardAction.getInstance());
        }
        return _editToolbar;
    }
    
    /** Getter for the file toolbar.
     * @return the file toolbar.
     *
     */
    public JToolBar getFileToolbar() {
        return _fileToolbar;
    }
    
    /** Getter for the view toolbar.
     * @return the view toolbar.
     */
    public JToolBar getViewToolbar() {
        if (_viewToolbar == null) {
            _viewToolbar = new ToolBar("View Toolbar");
            _viewToolbar.putClientProperty("JToolBar.isRollover",  Boolean.TRUE);
            _viewToolbar.add((Actions.Find));
            _viewToolbar.add(new ZoomSliderButton());
        }
        return _viewToolbar;
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
        _lruList.addEntry(filename);
    }
    
}
