// $Id$
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

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.PluggableMenu;
import org.argouml.application.events.ArgoModuleEvent;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ui.TabToDo;
import org.argouml.cognitive.ui.ToDoPane;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.swingext.BorderSplitPane;
import org.argouml.swingext.DockLayout;
import org.argouml.swingext.Horizontal;
import org.argouml.swingext.Orientation;
import org.argouml.swingext.Vertical;
import org.argouml.ui.menubar.GenericArgoMenuBar;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.ui.ActionExit;
import org.argouml.uml.ui.TabProps;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.ui.IStatusBar;
import org.tigris.gef.util.VectorSet;

/** The main window of the ArgoUML application. */

public class ProjectBrowser
    extends JFrame
    implements IStatusBar, PropertyChangeListener, TargetListener {

    protected static Logger cat = Logger.getLogger(ProjectBrowser.class);

    ////////////////////////////////////////////////////////////////
    // constants

    private static final String BUNDLE = "statusmsg";
	
    public static final int DEFAULT_COMPONENTWIDTH = 220;
    public static final int DEFAULT_COMPONENTHEIGHT = 200;

    ////////////////////////////////////////////////////////////////
    // class variables

    /**
     * ArgoUML will not support this method of invocation of the projectbrowser
     * very soon.
     * @deprecated As of ArgoUml version 0.13.5, replaced by
     *             {@link org.argouml.ui.ProjectBrowser#getInstance()}
     */
    private static ProjectBrowser TheInstance;

    private static String _Title = "ArgoUML";
    private static boolean _Splash = false;

    // ----- diagrams

    ////////////////////////////////////////////////////////////////
    // instance variables

    protected String _appName = "ProjectBrowser";

    protected MultiEditorPane _editorPane;

    /* Work in progress here to allow multiple details panes with 
    ** different contents - Bob Tarling
    */
    protected DetailsPane _northEastPane;
    protected DetailsPane _northPane;
    protected DetailsPane _northWestPane;
    protected JPanel _westPane;
    protected DetailsPane _eastPane;
    protected DetailsPane _southEastPane;
    protected JPanel _southWestPane;
    protected DetailsPane _southPane;

    private Map detailsPanesByCompassPoint = new HashMap();

    private GenericArgoMenuBar _menuBar;

    /** partially implemented. needs work to display
     * import of source and saving of zargo
     */
    protected StatusBar _statusBar = new StatusBar();

    /** this needs work so that users can set the font
     * size through a gui preference window
     */
    public Font defaultFont = new Font("Dialog", Font.PLAIN, 10);

    protected BorderSplitPane _workarea;

    /**
     * The splash screen shown at startup
     */
    private SplashScreen _splash;

    /**
     * The navigator pane containing the modelstructure
     */
    private NavigatorPane _navPane;

    /**
     * The todopane (lower left corner of screen)
     */
    private ToDoPane _todoPane;

    /**
     * The target the user has selected     
     */
    private Object _target;

    /**
     * flag to prevent the ProjectBrowser and the GEF SelectionManager
     * from creating cycles of setTarget(..)
     */
    private boolean isDoingSelection;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * For testing purposes. In tests this constructor can be called so
     * TheInstance is filled.
     */
    private ProjectBrowser() {
        this("ArgoUML", false);
    }

    private ProjectBrowser(String appName, boolean doSplash) {
        super(appName);
        TheInstance = this;
        SplashScreen.setDoSplash(doSplash);
        if (doSplash) {
            _splash = SplashScreen.getInstance();
	    _splash.getStatusBar().showStatus(
		     Argo.localize(BUNDLE,
				   "statusmsg.bar.making-project-browser"));
            _splash.getStatusBar().showProgress(10);
            _splash.setVisible(true);
        }

        _menuBar = new GenericArgoMenuBar();

        _editorPane = new MultiEditorPane();
        getContentPane().setFont(defaultFont);
        getContentPane().setLayout(new BorderLayout());
        this.setJMenuBar(_menuBar);
        //getContentPane().add(_menuBar, BorderLayout.NORTH);
        getContentPane().add(createPanels(doSplash), BorderLayout.CENTER);
        getContentPane().add(_statusBar, BorderLayout.SOUTH);

        setAppName(appName);

        // allows me to ask "Do you want to save first?"
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowCloser());
        ImageIcon argoImage =
            ResourceLoaderWrapper
	    .getResourceLoaderWrapper()
	    .lookupIconResource(
				"ArgoIcon");
        this.setIconImage(argoImage.getImage());
        // 

        // adds this as listener to projectmanager so it gets updated when the 
        // project changes
        ProjectManager.getManager().addPropertyChangeListener(this);
        
        // adds this as listener to TargetManager so gets notified
        // when the active diagram changes
        TargetManager.getInstance().addTargetListener(this);

        isDoingSelection = false;
    }

    public Locale getLocale() {
        return Locale.getDefault();
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
     * Creates the panels in the working area
     * @return Component
     */
    protected Component createPanels(boolean doSplash) {
        if (doSplash) {
	    _splash.getStatusBar().showStatus(
		     Argo.localize(BUNDLE, 
			     "statusmsg.bar.making-project-browser-explorer"));
            _splash.getStatusBar().incProgress(5);
        }
        //_navPane = new NavigatorPane(doSplash);
        _navPane = NavigatorPane.getInstance();
        /* Work in progress here to allow multiple details panes with 
        ** different contents - Bob Tarling
        */
        _eastPane  =
	    makeDetailsPane(BorderSplitPane.EAST,  Vertical.getInstance());
        _southPane =
	    makeDetailsPane(BorderSplitPane.SOUTH, Horizontal.getInstance());
        _southEastPane =
	    makeDetailsPane(BorderSplitPane.SOUTHEAST,
			    Horizontal.getInstance());
        _northWestPane =
	    makeDetailsPane(BorderSplitPane.NORTHWEST,
			    Horizontal.getInstance());
        _northPane =
	    makeDetailsPane(BorderSplitPane.NORTH, Horizontal.getInstance());
        _northEastPane =
	    makeDetailsPane(BorderSplitPane.NORTHEAST,
			    Horizontal.getInstance());
        
        if (_southPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.SOUTH, _southPane);
        }
        if (_southEastPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.SOUTHEAST,
					   _southEastPane);
        }
        if (_southWestPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.SOUTHWEST,
					   _southWestPane);
        }
        if (_eastPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.EAST, _eastPane);
        }
        if (_westPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.WEST, _westPane);
        }
        if (_northWestPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.NORTHWEST,
					   _northWestPane);
        }
        if (_northPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.NORTH, _northPane);
        }
        if (_northEastPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.NORTHEAST,
					   _northEastPane);
        }
        if (_westPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.WEST, _westPane);
        }
        if (_southWestPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.SOUTHWEST,
					   _southWestPane);
        }

        // The workarea is all the visible space except the menu,
        // toolbar and status bar.  Workarea is layed out as a
        // BorderSplitPane where the various components that make up
        // the argo application can be positioned.
        _workarea = new BorderSplitPane();
        // create the todopane
        if (doSplash) {
			_splash.getStatusBar().showStatus(Argo.localize(BUNDLE, 
											  "statusmsg.bar.making-project-browser-to-do-pane"));
            _splash.getStatusBar().incProgress(5);
        }
        _todoPane = new ToDoPane(doSplash);
        restorePanelSizes();

        // There are various details panes all of which could hold
        // different tabs pages according to users settings.
        // Place each pane in the required border area.
        Iterator it = detailsPanesByCompassPoint.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String position = (String) entry.getKey();
            if (entry.getValue() instanceof DetailsPane) {
                DetailsPane detailsPane = (DetailsPane) entry.getValue();
                TargetManager.getInstance().addTargetListener(detailsPane);
            }
            _workarea.add((Component) entry.getValue(), position);
        }
        _workarea.add(_navPane, BorderSplitPane.WEST);
        
        TabToDo todo = (TabToDo) getTab(TabToDo.class);
        //todo.setTree(_todoPane);
        _workarea.add(_todoPane, BorderSplitPane.SOUTHWEST);
        _workarea.add(_editorPane);
        // Toolbar boundry is the area between the menu and the status
        // bar. It contains the workarea at centre and the toolbar
        // position north, south, east or west.
        JPanel toolbarBoundry = new JPanel();
        toolbarBoundry.setLayout(new DockLayout());
        // TODO - should save and restore the last positions of the toolbars
        toolbarBoundry.add(_menuBar.getFileToolbar(), BorderLayout.NORTH);
        toolbarBoundry.add(_menuBar.getEditToolbar(), BorderLayout.NORTH);
        toolbarBoundry.add(_menuBar.getViewToolbar(), BorderLayout.NORTH);
        toolbarBoundry.add(_menuBar.getCreateDiagramToolbar(),
			   BorderLayout.NORTH);
        toolbarBoundry.add(_workarea, BorderLayout.CENTER);

        return toolbarBoundry;
    }

    /** Set the size of each panel to that last saved in the configuration file
     */
    private void restorePanelSizes() {
        if (_northPane != null) {
            _northPane.setPreferredSize(
		    new Dimension(0,
				  Configuration.getInteger(
					  Argo.KEY_SCREEN_NORTH_HEIGHT,
					  DEFAULT_COMPONENTHEIGHT)));
        }
        if (_southPane != null) {
            _southPane.setPreferredSize(
		    new Dimension(0,
				  Configuration.getInteger(
					  Argo.KEY_SCREEN_SOUTH_HEIGHT,
					  DEFAULT_COMPONENTHEIGHT)));
        }
        if (_eastPane != null) {
            _eastPane.setPreferredSize(
		    new Dimension(Configuration.getInteger(
					  Argo.KEY_SCREEN_EAST_WIDTH,
					  DEFAULT_COMPONENTHEIGHT),
				  0));
        }
        if (_navPane != null) {
            _navPane.setPreferredSize(
		    new Dimension(Configuration.getInteger(
					  Argo.KEY_SCREEN_WEST_WIDTH,
					  DEFAULT_COMPONENTHEIGHT),
				  0));
        }
	//        if (_northWestPane != null) {
	//            _northWestPane.setPreferredSize(new Dimension(
	//                Configuration.getInteger(
	// Argo.KEY_SCREEN_NORTHWEST_WIDTH, DEFAULT_COMPONENTWIDTH),
	//                Configuration.getInteger(
	// Argo.KEY_SCREEN_NORTH_HEIGHT, DEFAULT_COMPONENTHEIGHT)
	//            ));
	//        }
	//        if (_todoPane != null) {
	//            _todoPane.setPreferredSize(new Dimension(
	//                Configuration.getInteger(
	// Argo.KEY_SCREEN_SOUTHWEST_WIDTH, DEFAULT_COMPONENTWIDTH),
	//                Configuration.getInteger(
	// Argo.KEY_SCREEN_SOUTH_HEIGHT, DEFAULT_COMPONENTHEIGHT)
	//            ));
	//        }
	//        if (_northEastPane != null) {
	//            _northEastPane.setPreferredSize(new Dimension(
	//                Configuration.getInteger(
	// Argo.KEY_SCREEN_NORTHEAST_WIDTH, DEFAULT_COMPONENTWIDTH),
	//                Configuration.getInteger(
	// Argo.KEY_SCREEN_NORTH_HEIGHT, DEFAULT_COMPONENTHEIGHT)
	//            ));
	//        }
	//        if (_southEastPane != null) {
	//            _southEastPane.setPreferredSize(new Dimension(
	//                Configuration.getInteger(
	// Argo.KEY_SCREEN_SOUTHEAST_WIDTH, DEFAULT_COMPONENTWIDTH),
	//                Configuration.getInteger(
	// Argo.KEY_SCREEN_SOUTH_HEIGHT, DEFAULT_COMPONENTHEIGHT)
	//            ));
	//        }
    }
    ////////////////////////////////////////////////////////////////
    // accessors

    public void setTitle(String title) {
        if (title == null || "".equals(title)) {
            setTitle(getAppName());
        } else {
            String changeIndicator = ProjectManager.getManager()
                .getCurrentProject().getSaveRegistry().hasChanged()
                ? " *" : "";
            ArgoDiagram activeDiagram = ProjectManager.getManager()
                .getCurrentProject().getActiveDiagram();
            if (activeDiagram != null) {
                super.setTitle(title + " - " + activeDiagram.getName()
                    + " - " + getAppName() + changeIndicator);
            }
            else {
                super.setTitle(title + " - " + getAppName() + changeIndicator);
            }
        }
    }

    /**
     * Updates the window title to contain the latest values for 
     * project name, active diagram, and save status.
     */
    protected void updateTitle() {
        setTitle(ProjectManager.getManager().getCurrentProject().getName());
    }
    
    public String getAppName() {
        return _appName;
    }
    public void setAppName(String n) {
        _appName = n;
    }

    /**
     * avoids cyclic events between ProjectBrowser and the GEF SelectionManager.
     */
    private void startSelectionTransaction() {
        isDoingSelection = true;
    }

    /**
     * avoids cyclic events between ProjectBrowser and the GEF SelectionManager.
     */
    private boolean isInSelectionTransaction() {
        return isDoingSelection;
    }

    /**
     * avoids cyclic events between ProjectBrowser and the GEF SelectionManager.
     */
    private void selectionTransactionEnded() {
        isDoingSelection = false;
    }

    /**
     * The method used by the NavigatorPane, MultiEditor and DetailsPane
     * to set the target of the application.
     *
     * <p>the target is either a Model Element (usually selected in
     * the Navigation pane or Properties panel) or a Fig (selected in
     * a diagram).
     *
     * <p>The concept of a selection transaction is used to prevent a change
     * of target in one view creating a call back to this method, which 
     * would then change the target in all views again...
     * </p>
     * @deprecated As of ArgoUml version 0.13.5,replaced by {@link
     * org.argouml.ui.targetmanager.TargetManager#setTarget(Object)
     * TargetManager.getInstance().setTarget(Object)}
     */
    public void setTarget(Object o) {

        if (isInSelectionTransaction()) {
            return;
        } else {
            startSelectionTransaction();
        }

        if (getTarget() != o) {

            cat.debug("setting project target = " + o);
            
        }
        TargetManager.getInstance().setTarget(o);

        selectionTransactionEnded();
    }

    /** 
     * return the current target in the editor pane
     * @deprecated As of ArgoUml version 0.13.5,replaced by {@link
     * org.argouml.ui.targetmanager.TargetManager#getTarget()
     * TargetManager.getInstance().getTarget()}
     */
    
    public Object getTarget() {
        return TargetManager.getInstance().getTarget();
    }

    /**
     * Set the diagram on which the user is currently working. 
     * When importing sources this should call the name of the folder from
     * which the classes were imported.
     * It should also default the model name as well.
     *{@link #setTarget}.
     * @deprecated As of ArgoUml version 0.13.5,replaced by {@link
     * org.argouml.kernel.Project#setActiveDiagram(ArgoDiagram)}
     */
    public void setActiveDiagram(ArgoDiagram ad) {
        ProjectManager.getManager().getCurrentProject().setActiveDiagram(ad);
    }

    /**
     * Return the diagram, the user is currently working on.
     * @deprecated As of ArgoUml version 0.13.5,replaced by
     *             {@link org.argouml.kernel.Project#getActiveDiagram()}
     */
    public ArgoDiagram getActiveDiagram() {
        return ProjectManager
            .getManager()
            .getCurrentProject()
            .getActiveDiagram();
    }

    /**
     * Select the tab page containing the todo item
     *
     * TODO: should introduce an instance variable to go straight to
     * the correct tab instead of trying all
     */
    public void setToDoItem(Object o) {
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        while (it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane) it.next();
            if (detailsPane.setToDoItem(o))
                return;
        }
    }

    /**
     * Gets the target of the detailspane. This is exactly the same as
     * the target of the TargetManager.
     *
     * @deprecated As of ArgoUml version 0.13.5,replaced by {@link
     * org.argouml.ui.targetmanager.TargetManager#getTarget()
     * TargetManager.getInstance().getTarget()}
     * @return the target
     */
    public Object getDetailsTarget() {
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        if (it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane) it.next();
            return TargetManager.getInstance().getTarget();
        }
        throw new IllegalStateException("No detailspane present");
    }

    /**
     * Get the tab page containing the properties
     * @return the TabProps tabpage
     */
    public TabProps getTabProps() {
        // In theory there can be multiple details pane (work in
        // progress). It must first be determined which details
        // page contains the properties tab. Bob Tarling 7 Dec 2002
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        while (it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane) it.next();
            TabProps tabProps = detailsPane.getTabProps();
            if (tabProps != null) {
                return tabProps;
            }
        }
        throw new IllegalStateException("No properties tab found");
    }

    /**
     * Get the tab page instance of the given class
     * @return the tabpage
     */
    public TabSpawnable getTab(Class tabClass) {
        // In theory there can be multiple details pane (work in
        // progress). It must first be determined which details
        // page contains the properties tab. Bob Tarling 7 Dec 2002
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        while (it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane) it.next();
            TabSpawnable tab = detailsPane.getTab(tabClass);
            if (tab != null) {
                return tab;
            }
        }
        throw new IllegalStateException("No " + tabClass.getName()
					+ " tab found");
    }

    public StatusBar getStatusBar() {
        return _statusBar;
    }

    public JMenuBar getJMenuBar() {
        return _menuBar;
    }

    public MultiEditorPane getEditorPane() {
        return _editorPane;
    }

    /**
     * Find the tabpage with the given label and make it the front tab
     * @param The tabpage label
     * @return false if no tab was found of given name
     */
    public void selectTabNamed(String tabName) {
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        while (it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane) it.next();
            if (detailsPane.selectTabNamed(Argo.localize("UMLMenu", tabName))) {
                return;
            }
        }
        throw new IllegalArgumentException("No such tab named " + tabName);
    }

    /**
     * Find the tabpage with the given label
     * @param The tabpage label
     * @return the tabpage
     */
    public JPanel getNamedTab(String tabName) {
        JPanel panel;
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        while (it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane) it.next();
            panel = detailsPane.getNamedTab(tabName);
            if (panel != null) {
                return panel;
            }
        }
        //TODO I'd prefer to throw this exception here but doing Argo currently
        //falls over - needs more investigation Bob Tarling 8 Dec 2002
        //throw new IllegalArgumentException("No such tab named " + tabName);
        return null;
    }

    /** get a list of offenders and display the according diagram, aka
     *  implement a method which jumps to the offender.
     *  TODO: this probably needs a lot of work, as the code looks
     *  as if it can only jump to diagram offenders
     *
     *  @param dms vector of offenders
     *  @see org.argouml.cognitive.ui.ToDoPane
     */
    public void jumpToDiagramShowing(VectorSet dms) {
        if (dms.size() == 0) {
            return;
        }
        Object first = dms.elementAt(0);
        if (first instanceof Diagram && dms.size() > 1) {
            setTarget(first);
            setTarget(dms.elementAt(1));
            return;
        }
        if (first instanceof Diagram && dms.size() == 1) {
            setTarget(first);
            return;
        }
        Vector diagrams =
            ProjectManager.getManager().getCurrentProject().getDiagrams();
        Object target = _editorPane.getTarget();
        if ((target instanceof Diagram)
            && ((Diagram) target).countContained(dms) == dms.size()) {
            setTarget(first);
            return;
        }

        Diagram bestDiagram = null;
        int bestNumContained = 0;
        for (int i = 0; i < diagrams.size(); i++) {
            Diagram d = (Diagram) diagrams.elementAt(i);
            int nc = d.countContained(dms);
            if (nc > bestNumContained) {
                bestNumContained = nc;
                bestDiagram = d;
            }
            if (nc == dms.size())
                break;
        }
        if (bestDiagram != null) {
            setTarget(bestDiagram);
            setTarget(first);
        }
        // making it possible to jump to the modelroot
        if (first.equals(ProjectManager.getManager().getCurrentProject()
			 .getRoot()))
	{
            setTarget(first);
        }
    }

    ////////////////////////////////////////////////////////////////
    // window operations

    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b)
            org.tigris.gef.base.Globals.setStatusBar(this);
    }

    ////////////////////////////////////////////////////////////////
    // IStatusBar
    public void showStatus(String s) {
        _statusBar.showStatus(s);
    }

    /**    Called by a user interface element when a request to
     *    navigate to a model element has been received.
     */
    public void navigateTo(Object element) {  
        setTarget(element);
    }

    /**   Called by a user interface element when a request to
     *   open a model element in a new window has been recieved.
     */
    public void open(Object element) {
    }

    /**
     * @deprecated As of ArgoUml version 0.13.5,replaced by {@link
     * org.argouml.ui.targetmanager.TargetManager#navigateBack()
     * TargetManager.getInstance().navigateBack()}
     */
    public boolean navigateBack(boolean attempt) {
        boolean navigated = false;
        if (attempt && TargetManager.getInstance().navigateBackPossible()) {
	    TargetManager.getInstance().navigateBackward();
        }
        return navigated;
    }

    /**
     * @deprecated As of ArgoUml version 0.13.5,replaced by {@link
     * org.argouml.ui.targetmanager.TargetManager#navigateForward()
     * TargetManager.getInstance().navigateForward()}
     */
    public boolean navigateForward(boolean attempt) {
        boolean navigated = false;
        if (attempt) {
            if (attempt
		&& TargetManager.getInstance().navigateForwardPossible())
	    {
		TargetManager.getInstance().navigateForward();
	    }
        }
        return navigated;
    }

    /**
     * @deprecated As of ArgoUml version 0.13.5,replaced by {@link
     * org.argouml.ui.targetmanager.TargetManager#navigateBackPossible()
     * TargetManager.getInstance().navigateBackPossible()}
     */
    public boolean isNavigateBackEnabled() {
        return TargetManager.getInstance().navigateBackPossible();
    }

    /**
     * @deprecated As of ArgoUml version 0.13.5,replaced by {@link
     * org.argouml.ui.targetmanager.TargetManager#navigateForwardPossible()
     * TargetManager.getInstance().navigateForwardPossible()}
     */
    public boolean isNavigateForwardEnabled() {
        return TargetManager.getInstance().navigateForwardPossible();
    }

    /**
     * Save the positions of the screen splitters, sizes and postion
     * of main window in the properties file
     */
    public void saveScreenConfiguration() {
        if (_navPane != null)
	    Configuration.setInteger(Argo.KEY_SCREEN_WEST_WIDTH,
				     _navPane.getWidth());
        if (_eastPane != null)
	    Configuration.setInteger(Argo.KEY_SCREEN_EAST_WIDTH,
				     _eastPane.getWidth());
        if (_northPane != null)
	    Configuration.setInteger(Argo.KEY_SCREEN_NORTH_HEIGHT,
				     _northPane.getHeight());
        if (_southPane != null)
	    Configuration.setInteger(Argo.KEY_SCREEN_SOUTH_HEIGHT,
				     _southPane.getHeight());
	//        if (_todoPane != null)
	// Configuration.setInteger(Argo.KEY_SCREEN_SOUTHWEST_WIDTH,
	// _todoPane.getWidth());
	//        if (_southEastPane != null)
	// Configuration.setInteger(Argo.KEY_SCREEN_SOUTHEAST_WIDTH,
	// _southEastPane.getWidth());
	//        if (_northWestPane != null)
	// Configuration.setInteger(Argo.KEY_SCREEN_NORTHWEST_WIDTH,
	// _northWestPane.getWidth());
	//        if (_northEastPane != null)
	// Configuration.setInteger(Argo.KEY_SCREEN_NORTHEAST_WIDTH,
	// _northEastPane.getWidth());
        Configuration.setInteger(Argo.KEY_SCREEN_WIDTH, getWidth());
        Configuration.setInteger(Argo.KEY_SCREEN_HEIGHT, getHeight());
        Configuration.setInteger(Argo.KEY_SCREEN_LEFT_X, getX());
        Configuration.setInteger(Argo.KEY_SCREEN_TOP_Y, getY());
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

    /**
     * Build a new details pane for the given compass point
     * @param compassPoint the position for which to build the pane
     * @param orientation the required orientation of the pane.
     * @return the details pane or null if none is required for the given
     *         compass point.
     */
    private DetailsPane makeDetailsPane(String compassPoint,
					Orientation orientation)
    {
        DetailsPane detailsPane =
	    new DetailsPane(compassPoint.toLowerCase(), orientation);
        if (detailsPane.getTabCount() == 0)
            return null;
        return detailsPane;
    }

    class WindowCloser extends WindowAdapter {
        public WindowCloser() {
        }
        public void windowClosing(WindowEvent e) {

            ActionExit.SINGLETON.actionPerformed(null);
        }
    } /* end class WindowCloser */

    /**
     * @see
     * java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        // the project changed
        if (evt.getPropertyName()
            .equals(ProjectManager.CURRENT_PROJECT_PROPERTY_NAME)) {
            Project p = (Project) evt.getNewValue();
            if (p != null) {
                setTitle(p.getName());             
                //Designer.TheDesigner.getToDoList().removeAllElements();
                Designer.TheDesigner.setCritiquingRoot(p);
                // update all panes
                TargetManager.getInstance().setTarget(p.getInitialTarget());
            }
        }

        // the save state changed
        else if (evt.getPropertyName()
            .equals(ProjectManager.SAVE_STATE_PROPERTY_NAME))
        {
            updateTitle();
        }
    }

    /////////////////////////////////////////////////////////////////////////
    // TargetListener methods implemented so notified when selected
    // diagram changes. Uses this to update the window title.
    
    public void targetAdded(TargetEvent e) {
    }

    public void targetRemoved(TargetEvent e) {
    }

    public void targetSet(TargetEvent e) {
        Object target = e.getNewTarget();
        if (target instanceof UMLDiagram) {
            updateTitle();
        }
    }    
    // End TargetListener methods

    /**
     * Returns the todopane. 
     * @return ToDoPane
     */
    public ToDoPane getTodoPane() {
        return _todoPane;
    }

    /**
     * Returns the navigatorpane. 
     * @return NavigatorPane The navigatorpane
     * @deprecated 0.15 will be removed in 0.16 use 
     * NavigatorPane.getInstance() instead
     */
    public NavigatorPane getNavigatorPane() {
        return _navPane;
    }

    /**
     * Returns the splashscreen shown at startup. 
     * @return SplashScreen
     * @deprecated 0.15 will be removed in 0.16 use 
     * SplashScreen.getInstance() instead
     */
    public SplashScreen getSplashScreen() {
        return _splash;
    }

    /**
     * Sets the splashscreen. Sets the current splashscreen to invisible
     * @param splash
     */
    public void setSplashScreen(SplashScreen splash) {
        if (_splash != null && _splash != splash) {
            _splash.setVisible(false);
        }
        _splash = splash;
    }

    /**
     * Singleton retrieval method for the projectbrowser. Lazely instantiates
     * the projectbrowser. 
     * @return the singleton instance of the projectbrowser
     */
    public static synchronized ProjectBrowser getInstance() {
        if (TheInstance == null) {
            TheInstance = new ProjectBrowser("ArgoUML", _Splash);
        }
        return TheInstance;
    }

    public static synchronized void setSplash(boolean splash) {
        _Splash = splash;
    }
    
} /* end class ProjectBrowser */
