// Copyright (c) 1996-2002 The Regents of the University of California. All
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

//$Id$

package org.argouml.ui.menubar;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.argouml.application.api.Argo;
import org.argouml.application.api.PluggableMenu;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoModuleEvent;
import org.argouml.application.events.ArgoModuleEventListener;
import org.argouml.swingext.Toolbar;
import org.argouml.ui.ActionAutoResize;
import org.argouml.ui.ActionSaveConfiguration;
import org.argouml.ui.ActionSettings;
import org.argouml.ui.Actions;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.cmd.CmdSetPreferredSize;
import org.argouml.ui.targetmanager.NavigateTargetBackAction;
import org.argouml.ui.targetmanager.NavigateTargetForwardAction;
import org.argouml.uml.ui.ActionActivityDiagram;
import org.argouml.uml.ui.ActionClassDiagram;
import org.argouml.uml.ui.ActionCollaborationDiagram;
import org.argouml.uml.ui.ActionCopy;
import org.argouml.uml.ui.ActionCut;
import org.argouml.uml.ui.ActionDeleteFromDiagram;
import org.argouml.uml.ui.ActionDeploymentDiagram;
import org.argouml.uml.ui.ActionEmptyTrash;
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
import org.argouml.uml.ui.ActionSaveGraphics;
import org.argouml.uml.ui.ActionSaveProject;
import org.argouml.uml.ui.ActionSaveProjectAs;
import org.argouml.uml.ui.ActionSequenceDiagram;
import org.argouml.uml.ui.ActionStateDiagram;
import org.argouml.uml.ui.ActionUseCaseDiagram;
import org.argouml.uml.ui.UMLAction;
import org.tigris.gef.base.CmdAdjustGrid;
import org.tigris.gef.base.CmdAdjustGuide;
import org.tigris.gef.base.CmdAdjustPageBreaks;
import org.tigris.gef.base.CmdSelectAll;
import org.tigris.gef.base.CmdSelectInvert;
import org.tigris.gef.base.CmdSelectNext;
import org.tigris.gef.base.CmdZoom;
import org.tigris.gef.util.Localizer;

/** GenericArgoMenuBar defines the menubar for all
 *  operating systems which do not explicitely ask
 *  for a different kind of menu bar, such as
 *  Mac OS X.
 */
public class GenericArgoMenuBar extends JMenuBar
    implements ArgoModuleEventListener {

    private Toolbar _fileToolbar;
    private Toolbar _editToolbar;
    private Toolbar _viewToolbar;
    private Toolbar _createDiagramToolbar;
    
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

    static final protected KeyStroke getShortcut(String key) {
        return Localizer.getShortcut("CoreMenu",key);
    }

    /** This should be a user specified option. New laws
     * for handicapped people who cannot use the
     * mouse require software developers in US to
     * make all components of User interface accessible
     * through keyboard
     */
    static final protected void setMnemonic(JMenuItem item,String key,char defMnemonic) {
        String localMnemonic = Argo.localize("CoreMenu","Mnemonic_" + key);
        char mnemonic = defMnemonic;
        if(localMnemonic != null && localMnemonic.length() == 1) {
            mnemonic = localMnemonic.charAt(0);
        }
        item.setMnemonic(mnemonic);
    }

    static final protected String menuLocalize(String key) {
        return Argo.localize("CoreMenu", key);
    }

    static final void setAccelerator(JMenuItem item,KeyStroke keystroke) {
        if(keystroke != null) {
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
        Object[] context = { menuitem, key };
        ArrayList arraylist = Argo.getPlugins(PluggableMenu.class, context);
        ListIterator iterator = arraylist.listIterator();
        while (iterator.hasNext()) {
            PluggableMenu module = (PluggableMenu)iterator.next();
            menuitem.add(module.getMenuItem(context));
            menuitem.setEnabled(true);
        }
    }

    public void moduleLoaded(ArgoModuleEvent event) {
        if (event.getSource() instanceof PluggableMenu) {
            PluggableMenu module = (PluggableMenu)event.getSource();
            Object[] context = new Object[] { _tools, "Tools" };
            if (module.inContext(context)) {
                _tools.add(module.getMenuItem(context));
                _tools.setEnabled(true);
            }
            //context = new Object[] { _import, "File:Import" };
            //if (module.inContext(context)) {
              //  _import.add(module.getMenuItem(context));
            //}
            context = new Object[] { _generate, "Generate" };
            if (module.inContext(context)) {
                _generate.add(module.getMenuItem(context));
            }
            context = new Object[] { _edit, "Edit" };
            if (module.inContext(context)) {
                _edit.add(module.getMenuItem(context));
            }
            context = new Object[] { _view, "View" };
            if (module.inContext(context)) {
      	        _view.add(module.getMenuItem(context));
            }
            context = new Object[] { _createDiagrams, "Create Diagrams" };
            if (module.inContext(context)) {
       	        _createDiagrams.add(module.getMenuItem(context));
            }
            context = new Object[] { _arrange, "Arrange" };
            if (module.inContext(context)) {
       	        _arrange.add(module.getMenuItem(context));
            }
            context = new Object[] { _help, "Help" };
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
        KeyStroke ctrlMinus = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, menuShortcut);
        KeyStroke ctrlEquals = KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, menuShortcut);

        
        KeyStroke F3 = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
        KeyStroke F7 = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);
        KeyStroke altF4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK);
        KeyStroke altLeft = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.ALT_MASK);
        KeyStroke altRight = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.ALT_MASK);

        KeyStroke delKey  = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
        KeyStroke ctrlDel = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, menuShortcut);

        JMenuItem mi;
        // File Menu
        JMenu _file = new JMenu(menuLocalize("File"));
        add(_file);
        setMnemonic(_file,"File",'F');
        _fileToolbar = new Toolbar("File Toolbar");
        JMenuItem newItem = _file.add(ActionNew.SINGLETON);
        setMnemonic(newItem,"New",'N');
        setAccelerator(newItem,ctrlN);
        _fileToolbar.add((ActionNew.SINGLETON));
        JMenuItem openProjectItem = _file.add(ActionOpenProject.SINGLETON);
        setMnemonic(openProjectItem,"Open",'O');
        setAccelerator(openProjectItem,ctrlO);
        _fileToolbar.add((ActionOpenProject.SINGLETON));

        JMenuItem saveProjectItem = _file.add(ActionSaveProject.SINGLETON);
        setMnemonic(saveProjectItem,"Save",'S');
        setAccelerator(saveProjectItem,ctrlS);
        _fileToolbar.add((ActionSaveProject.SINGLETON));
        JMenuItem saveProjectAsItem = _file.add(ActionSaveProjectAs.SINGLETON);
        setMnemonic(saveProjectAsItem,"SaveAs",'A');
        _file.addSeparator();

        //_import = new JMenu(menuLocalize("action.import"));
        //JMenuItem importProjectAsItem = _import.add(ActionImportFromSources.SINGLETON);
        //appendPluggableMenus(_import, PluggableMenu.KEY_FILE_IMPORT);
        _file.add(ActionImportFromSources.SINGLETON);
        _file.addSeparator();
    
        JMenuItem printItem = _file.add(Actions.Print);
        setMnemonic(printItem,"Print",'P');
        setAccelerator(printItem,ctrlP);
        _fileToolbar.add((Actions.Print));
        JMenuItem pageSetupItem = _file.add(Actions.PageSetup);
        JMenuItem saveGraphicsItem = _file.add(ActionSaveGraphics.SINGLETON);
        setMnemonic(saveGraphicsItem,"SaveGraphics",'G');
        _file.addSeparator();
        _file.add(ActionSaveConfiguration.SINGLETON);
        _file.addSeparator();
        JMenuItem exitItem = _file.add(ActionExit.SINGLETON);
        setMnemonic(exitItem,"Exit",'x');
        setAccelerator(exitItem,altF4);

        _edit = (JMenu) add(new JMenu(menuLocalize("Edit")));
        setMnemonic(_edit,"Edit",'E');

        _select = new JMenu(menuLocalize("Select"));
        _edit.add(_select);
        JMenuItem selectAllItem = _select.add(new CmdSelectAll());
        setAccelerator(selectAllItem,ctrlA);
        _select.addSeparator();
        JMenuItem backItem = _select.add(NavigateTargetBackAction.getInstance());
        //setAccelerator(backItem,altLeft);
        JMenuItem forwardItem = _select.add(NavigateTargetForwardAction.getInstance());
        //setAccelerator(forwardItem,altRight);
        _select.addSeparator();
        JMenuItem selectNextItem = _select.add(new CmdSelectNext(false));
        //tab
        JMenuItem selectPrevItem = _select.add(new CmdSelectNext(true));
        // shift tab
        _select.add(new CmdSelectInvert());

        // TODO These are not yet implmeneted - Bob Tarling 12 Oct 2002
        // _edit.add(Actions.Undo);
        // editToolbar.add((Actions.Undo));
        // _edit.add(Actions.Redo);
        // editToolbar.add((Actions.Redo));

        _edit.addSeparator();

        JMenuItem cutItem = _edit.add(ActionCut.getInstance());
        setMnemonic(cutItem,"Cut",'X');
        setAccelerator(cutItem,ctrlX);

        JMenuItem copyItem = _edit.add(ActionCopy.getInstance());
        setMnemonic(copyItem,"Copy",'C');
        setAccelerator(copyItem,ctrlC);

        JMenuItem pasteItem = _edit.add(ActionPaste.getInstance());
        setMnemonic(pasteItem,"Paste",'V');
        setAccelerator(pasteItem,ctrlV);

        _edit.addSeparator();

        JMenuItem removeItem = _edit.add(ActionDeleteFromDiagram.SINGLETON);
        setMnemonic(removeItem,"Remove",'R');
        setAccelerator(removeItem,delKey);

        JMenuItem deleteItem = _edit.add(ActionRemoveFromModel.SINGLETON);
        setMnemonic(deleteItem,"Delete",'D');
        setAccelerator(deleteItem,ctrlDel);
        // TODO Bob Tarling: no toolbarbutton till a new one is designed for Erase
        //_editToolbar.add(ActionRemoveFromModel.SINGLETON);

        JMenuItem emptyItem = _edit.add(ActionEmptyTrash.SINGLETON);

        _edit.addSeparator();

        _edit.add(ActionSettings.getInstance());

        _view = (ArgoJMenu) add(new ArgoJMenu(menuLocalize("View")));
        setMnemonic(_view,"View",'V');

        _view.add(Actions.GotoDiagram);
        JMenuItem findItem =  _view.add(Actions.Find);
        setAccelerator(findItem,F3);

        _view.addSeparator();

        JMenu zoom = (JMenu) _view.add(new JMenu(menuLocalize("Zoom")));
      
        JMenuItem zoomOut = zoom.add(new CmdZoom(0.9));
        zoomOut.setAccelerator(ctrlMinus);

        zoom.add(new CmdZoom(0.0));

        JMenuItem zoomIn = zoom.add(new CmdZoom((1.0)/(0.9)));
        zoomIn.setAccelerator(ctrlEquals);

        _view.addSeparator();

        JMenu editTabs = (JMenu) _view.add(new JMenu(menuLocalize("Editor Tabs")));
        // JMenu detailsTabs = (JMenu) _view.add(new JMenu(menuLocalize("Details Tabs")));

        _view.addSeparator();
        _view.add(new CmdAdjustGrid());
        _view.add(new CmdAdjustGuide());
        _view.add(new CmdAdjustPageBreaks());
        _view.addCheckItem(Actions.ShowRapidButtons);
        //_view.addCheckItem(Actions.ShowDiagramList);
        //_view.addCheckItem(Actions.ShowToDoList);
        //_showDetailsMenuItem = _view.addCheckItem(Actions.ShowDetails);

        _view.addSeparator();
        _view.add(org.argouml.language.ui.ActionNotation.getInstance().getMenu());


        appendPluggableMenus(_view, PluggableMenu.KEY_VIEW);

        //JMenu create = (JMenu) add(new JMenu(menuLocalize("Create")));
        //setMnemonic(create,"Create",'C');
        //create.add(Actions.CreateMultiple);
        //create.addSeparator();

        _createDiagrams = (JMenu) add(new JMenu(menuLocalize("Create Diagram")));
        setMnemonic(_createDiagrams, "Create Diagram",'C');
        _createDiagramToolbar = new Toolbar("Create Diagram Toolbar");
        _createDiagrams.add(ActionClassDiagram.SINGLETON);
        _createDiagramToolbar.add((ActionClassDiagram.SINGLETON));
        _createDiagrams.add(ActionUseCaseDiagram.SINGLETON);
        _createDiagramToolbar.add((ActionUseCaseDiagram.SINGLETON));
        _createDiagrams.add(ActionStateDiagram.SINGLETON);
        _createDiagramToolbar.add((ActionStateDiagram.SINGLETON));
        _createDiagrams.add(ActionActivityDiagram.SINGLETON);
        _createDiagramToolbar.add((ActionActivityDiagram.SINGLETON));
        _createDiagrams.add(ActionCollaborationDiagram.SINGLETON);
        _createDiagramToolbar.add((ActionCollaborationDiagram.SINGLETON));
        _createDiagrams.add(ActionDeploymentDiagram.SINGLETON);
        _createDiagramToolbar.add((ActionDeploymentDiagram.SINGLETON));
        _createDiagrams.add(ActionSequenceDiagram.SINGLETON);
        _createDiagramToolbar.add((ActionSequenceDiagram.SINGLETON));
        appendPluggableMenus(_createDiagrams, PluggableMenu.KEY_CREATE_DIAGRAMS);

        //JMenu createModelElements = (JMenu) create.add(new JMenu("Model Elements"));
        //createModelElements.add(Actions.AddTopLevelPackage);
        //createModelElements.add(_actionClass);
        //createModelElements.add(_actionInterface);
        //createModelElements.add(_actionActor);
        //createModelElements.add(_actionUseCase);
        //createModelElements.add(_actionState);
        //createModelElements.add(_actionPseudostate);
        //createModelElements.add(_actionAttr);
        //createModelElements.add(_actionOper);

        //JMenu createFig = (JMenu) create.add(new JMenu("Shapes"));
        //createFig.add(_actionRectangle);
        //createFig.add(_actionRRectangle);
        //createFig.add(_actionCircle);
        //createFig.add(_actionLine);
        //createFig.add(_actionText);
        //createFig.add(_actionPoly);
        //createFig.add(_actionInk);

        _arrange = (ArgoJMenu) add(new ArgoJMenu(menuLocalize("Arrange")));
        setMnemonic(_arrange,"Arrange",'A');

        JMenu align = (JMenu) _arrange.add(new JMenu(menuLocalize("Align")));
        JMenu distribute = (JMenu) _arrange.add(new JMenu(menuLocalize("Distribute")));
        JMenu reorder = (JMenu) _arrange.add(new JMenu(menuLocalize("Reorder")));
        JMenu nudge = (JMenu) _arrange.add(new JMenu(menuLocalize("Nudge")));

        _arrange.
            add(new CmdSetPreferredSize(CmdSetPreferredSize.MINIMUM_SIZE));

        _arrange.addCheckItem((UMLAction) new ActionAutoResize());

        JMenu layout = (JMenu) _arrange.add(new JMenu(menuLocalize("Layout")));
        appendPluggableMenus(_arrange, PluggableMenu.KEY_ARRANGE);

        Runnable initLater = new InitMenusLater(align, distribute,
                                                reorder, nudge,
                                                layout,
                                                editTabs);

        org.argouml.application.Main.addPostLoadAction(initLater);

        _generate = (JMenu) add(new JMenu(menuLocalize("Generation")));
        setMnemonic(_generate,"Generate",'G');
        _generate.add(ActionGenerateOne.SINGLETON);
        JMenuItem genAllItem = _generate.add(ActionGenerateAll.SINGLETON);
        setAccelerator(genAllItem,F7);
        _generate.addSeparator();
        _generate.add(ActionGenerateProjectCode.SINGLETON);
        _generate.add(ActionGenerationSettings.SINGLETON);
        //generate.add(Actions.GenerateWeb);
        appendPluggableMenus(_generate, PluggableMenu.KEY_GENERATE);

        _critique = (ArgoJMenu) add(new ArgoJMenu(menuLocalize("Critique")));
        setMnemonic(_critique,"Critique",'R');
        _critique.addCheckItem(Actions.AutoCritique);
        _critique.addSeparator();
        _critique.add(Actions.OpenDecisions);
        _critique.add(Actions.OpenGoals);
        _critique.add(Actions.OpenCritics);

        // Tools Menu
        _tools = new JMenu(menuLocalize("Tools"));
        _tools.setEnabled(false);
        appendPluggableMenus(_tools, PluggableMenu.KEY_TOOLS);
        add(_tools);

        // tools.add(ActionTest.getInstance());

        // Help Menu
        _help = new JMenu(menuLocalize("Help"));
        setMnemonic(_help,"Help",'H');
        appendPluggableMenus(_help, PluggableMenu.KEY_HELP);
        if (_help.getItemCount() > 0) {
            _help.insertSeparator(0);
        }

        _help.add(Actions.SystemInfo);
        _help.addSeparator();
        _help.add(Actions.AboutArgoUML);

        //setHelpMenu(help);
        add(_help);

        ArgoEventPump.addListener(ArgoEventTypes.ANY_MODULE_EVENT, this);
    }

    /** Getter for the create diagram toolbar.
     * @return Value of property _createDiagramToolbar.
     */
    public org.argouml.swingext.Toolbar getCreateDiagramToolbar() {
        return _createDiagramToolbar;
    }
    
    /** Getter for the edit toolbar.
     * @return the edit toolbar.
     */
    public org.argouml.swingext.Toolbar getEditToolbar() {
        if (_editToolbar == null) {
            _editToolbar = new Toolbar("Edit Toolbar");
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
    public org.argouml.swingext.Toolbar getFileToolbar() {
        return _fileToolbar;
    }
    
    /** Getter for the view toolbar.
     * @return the view toolbar.
     */
    public Toolbar getViewToolbar() {
        if (_viewToolbar == null) {
            _viewToolbar = new Toolbar("View Toolbar");
            _viewToolbar.add((Actions.Find));
        }
        return _viewToolbar;
    }
}
