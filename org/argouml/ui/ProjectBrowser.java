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

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.application.events.ArgoModuleEvent;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ui.TabToDo;
import org.argouml.cognitive.ui.ToDoPane;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.cmd.GenericArgoMenuBar;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.ActionExit;
import org.argouml.uml.ui.TabProps;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.ui.IStatusBar;
import org.tigris.gef.util.VectorSet;
import org.tigris.swidgets.BorderSplitPane;
import org.tigris.swidgets.Horizontal;
import org.tigris.swidgets.Orientation;
import org.tigris.swidgets.Vertical;
import org.tigris.toolbar.layouts.DockBorderLayout;

/**
 * The main window of the ArgoUML application.
 *
 * @stereotype singleton
 */
public class ProjectBrowser
    extends JFrame
    implements IStatusBar, PropertyChangeListener, TargetListener {

    /** Default width: DEFAULT_COMPONENTWIDTH */
    public static final int DEFAULT_COMPONENTWIDTH = 220;
    /** Default height: DEFAULT_COMPONENTHEIGHT */
    public static final int DEFAULT_COMPONENTHEIGHT = 200;

    ////////////////////////////////////////////////////////////////
    // class variables

    /**
     * Member attribute to contain the singleton.
     */
    private static ProjectBrowser theInstance;

    private static boolean showSplashScreen = false;

    // ----- diagrams

    ////////////////////////////////////////////////////////////////
    // instance variables

    private String appName = "ProjectBrowser";

    private MultiEditorPane editorPane;

    /* Work in progress here to allow multiple details panes with 
    ** different contents - Bob Tarling
    */
    private DetailsPane northEastPane;
    private DetailsPane northPane;
    private DetailsPane northWestPane;
    private JPanel westPane;
    private DetailsPane eastPane;
    private DetailsPane southEastPane;
    private JPanel southWestPane;
    private DetailsPane southPane;

    private Map detailsPanesByCompassPoint = new HashMap();

    private GenericArgoMenuBar menuBar;

    /** 
     * Partially implemented. Needs work to display
     * import of source and saving of zargo.
     */
    private StatusBar statusBar = new StatusBar();

    /** this needs work so that users can set the font
     * size through a gui preference window
     */
    private Font defaultFont = new Font("Dialog", Font.PLAIN, 10);

    private BorderSplitPane workAreaPane;

    /**
     * The splash screen shown at startup
     */
    private SplashScreen splashScreen;

    /**
     * The explorer (formerly called navigator) pane 
     * containing the modelstructure.
     */
    private NavigatorPane explorerPane;

    /**
     * The todopane (lower left corner of screen)
     */
    private ToDoPane todoPane;

    /**
     * For testing purposes. In tests this constructor can be called so
     * TheInstance is filled.
     */
    private ProjectBrowser() {
        this("ArgoUML", false);
    }

    /**
     * The constructor.
     * 
     * @param applicationName  the title of the frame
     * @param doSplash determines if we have to show 
     *                 the splash screen at startup
     */
    private ProjectBrowser(String applicationName, boolean doSplash) {
        super(applicationName);
        theInstance = this;
        SplashScreen.setDoSplash(doSplash);
        if (doSplash) {
            splashScreen = SplashScreen.getInstance();
	    splashScreen.getStatusBar().showStatus(
	            Translator.localize(
	                    "statusmsg.bar.making-project-browser"));
            splashScreen.getStatusBar().showProgress(10);
            splashScreen.setVisible(true);
        }

        menuBar = new GenericArgoMenuBar();

        editorPane = new MultiEditorPane();
        getContentPane().setFont(defaultFont);
        getContentPane().setLayout(new BorderLayout());
        this.setJMenuBar(menuBar);
        //getContentPane().add(_menuBar, BorderLayout.NORTH);
        getContentPane().add(createPanels(doSplash), BorderLayout.CENTER);
        getContentPane().add(statusBar, BorderLayout.SOUTH);

        setAppName(applicationName);

        // allows me to ask "Do you want to save first?"
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowCloser());
        ImageIcon argoImage =
            ResourceLoaderWrapper.lookupIconResource("ArgoIcon");
        this.setIconImage(argoImage.getImage());
        // 

        // adds this as listener to projectmanager so it gets updated when the 
        // project changes
        ProjectManager.getManager().addPropertyChangeListener(this);
        
        // adds this as listener to TargetManager so gets notified
        // when the active diagram changes
        TargetManager.getInstance().addTargetListener(this);
    }

    /**
     * @see java.awt.Component#getLocale()
     */
    public Locale getLocale() {
        return Locale.getDefault();
    }


    /**
     * Creates the panels in the working area
     *
     * @param doSplash true if we show  the splashscreen during startup
     * @return Component the area between the menu and the statusbar. 
     *                   It contains the workarea at centre and the toolbar
     *                   position north, south, east or west.
     *
     */
    protected Component createPanels(boolean doSplash) {
        if (doSplash) {
	    splashScreen.getStatusBar().showStatus(
	            Translator.localize(
	                    "statusmsg.bar.making-project-browser-explorer"));
            splashScreen.getStatusBar().incProgress(5);
        }
        //_navPane = new NavigatorPane(doSplash);
        explorerPane = NavigatorPane.getInstance();
        /* Work in progress here to allow multiple details panes with 
        ** different contents - Bob Tarling
        */
        eastPane  =
	    makeDetailsPane(BorderSplitPane.EAST,  Vertical.getInstance());
        southPane =
	    makeDetailsPane(BorderSplitPane.SOUTH, Horizontal.getInstance());
        southEastPane =
	    makeDetailsPane(BorderSplitPane.SOUTHEAST,
			    Horizontal.getInstance());
        northWestPane =
	    makeDetailsPane(BorderSplitPane.NORTHWEST,
			    Horizontal.getInstance());
        northPane =
	    makeDetailsPane(BorderSplitPane.NORTH, Horizontal.getInstance());
        northEastPane =
	    makeDetailsPane(BorderSplitPane.NORTHEAST,
			    Horizontal.getInstance());
        
        if (southPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.SOUTH, southPane);
        }
        if (southEastPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.SOUTHEAST,
					   southEastPane);
        }
        if (southWestPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.SOUTHWEST,
					   southWestPane);
        }
        if (eastPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.EAST, eastPane);
        }
        if (westPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.WEST, westPane);
        }
        if (northWestPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.NORTHWEST,
					   northWestPane);
        }
        if (northPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.NORTH, northPane);
        }
        if (northEastPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.NORTHEAST,
					   northEastPane);
        }
        if (westPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.WEST, westPane);
        }
        if (southWestPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.SOUTHWEST,
					   southWestPane);
        }

        // The workarea is all the visible space except the menu,
        // toolbar and status bar.  Workarea is layed out as a
        // BorderSplitPane where the various components that make up
        // the argo application can be positioned.
        workAreaPane = new BorderSplitPane();
        // create the todopane
        if (doSplash) {
	    splashScreen.getStatusBar().showStatus(Translator.localize(
		    "statusmsg.bar.making-project-browser-to-do-pane"));
            splashScreen.getStatusBar().incProgress(5);
        }
        todoPane = new ToDoPane(doSplash);
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
            workAreaPane.add((Component) entry.getValue(), position);
        }
        workAreaPane.add(explorerPane, BorderSplitPane.WEST);
        
        getTab(TabToDo.class); // TODO: If this doesn't have side effects, 
                               //       it can be removed alltogether.
        //todo.setTree(_todoPane);
        workAreaPane.add(todoPane, BorderSplitPane.SOUTHWEST);
        workAreaPane.add(editorPane);
        // Toolbar boundry is the area between the menu and the status
        // bar. It contains the workarea at centre and the toolbar
        // position north, south, east or west.
        JPanel toolbarBoundry = new JPanel();
        toolbarBoundry.setLayout(new DockBorderLayout());
        // TODO: - should save and restore the last positions of the toolbars
        toolbarBoundry.add(menuBar.getFileToolbar(), BorderLayout.NORTH);
        toolbarBoundry.add(menuBar.getEditToolbar(), BorderLayout.NORTH);
        toolbarBoundry.add(menuBar.getViewToolbar(), BorderLayout.NORTH);
        toolbarBoundry.add(menuBar.getCreateDiagramToolbar(),
			   BorderLayout.NORTH);
        toolbarBoundry.add(workAreaPane, BorderLayout.CENTER);

        return toolbarBoundry;
    }

    /** Set the size of each panel to that last saved in the configuration file
     */
    private void restorePanelSizes() {
        if (northPane != null) {
            northPane.setPreferredSize(
		    new Dimension(0,
				  Configuration.getInteger(
					  Argo.KEY_SCREEN_NORTH_HEIGHT,
					  DEFAULT_COMPONENTHEIGHT)));
        }
        if (southPane != null) {
            southPane.setPreferredSize(
		    new Dimension(0,
				  Configuration.getInteger(
					  Argo.KEY_SCREEN_SOUTH_HEIGHT,
					  DEFAULT_COMPONENTHEIGHT)));
        }
        if (eastPane != null) {
            eastPane.setPreferredSize(
		    new Dimension(Configuration.getInteger(
					  Argo.KEY_SCREEN_EAST_WIDTH,
					  DEFAULT_COMPONENTHEIGHT),
				  0));
        }
        if (explorerPane != null) {
            explorerPane.setPreferredSize(
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

    /**
     * @see java.awt.Frame#setTitle(java.lang.String)
     */
    public void setTitle(String title) {
        if (title == null || "".equals(title)) {
            setTitle(getAppName());
        } else {
            // ask the Project if we are "dirty" - i.e. need to save
            String changeIndicator = ProjectManager.getManager()
                .getCurrentProject().needsSave()
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
    
    /**
     * @return the application name as shown in the titlebar
     */
    public String getAppName() {
        return appName;
    }
    
    /**
     * @param n the application name as shown in the titlebar
     */
    public void setAppName(String n) {
        appName = n;
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
     * Only still used in tests.
     *
     * @param o the target
     */
    public void setTarget(Object o) {
        TargetManager.getInstance().setTarget(o);
    }

    /** 
     * return the current target in the editor pane
     * @deprecated As of ArgoUml version 0.13.5,replaced by {@link
     * org.argouml.ui.targetmanager.TargetManager#getTarget()
     * TargetManager.getInstance().getTarget()}
     *
     * @return the target object
     */
    public Object getTarget() {
        return TargetManager.getInstance().getTarget();
    }

    /**
     * Select the tab page containing the todo item
     *
     * TODO: should introduce an instance variable to go straight to
     * the correct tab instead of trying all
     *
     * @param o the todo item to select
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
     * 
     * @param tabClass the given class
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

    /**
     * @return the status bar
     */
    public StatusBar getStatusBar() {
        return statusBar;
    }

    /**
     * @see javax.swing.JFrame#getJMenuBar()
     */
    public JMenuBar getJMenuBar() {
        return menuBar;
    }

    /**
     * @return the editor pane
     */
    public MultiEditorPane getEditorPane() {
        return editorPane;
    }

    /**
     * Find the tabpage with the given label and make it the front tab
     *
     * @param tabName The tabpage label
     */
    public void selectTabNamed(String tabName) {
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        while (it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane) it.next();
            if (detailsPane.selectTabNamed(Translator.localize(tabName))) {
                return;
            }
        }
        throw new IllegalArgumentException("No such tab named " + tabName);
    }

    /**
     * Find the tabpage with the given label
     *
     * @param tabName The tabpage label
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
        //TODO: I'd prefer to throw this exception here but doing Argo currently
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
        Object target = TargetManager.getInstance().getTarget();
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

    /**
     * @see java.awt.Component#setVisible(boolean)
     */
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b)
            org.tigris.gef.base.Globals.setStatusBar(this);
    }

    ////////////////////////////////////////////////////////////////
    // IStatusBar
    /**
     * @see org.tigris.gef.ui.IStatusBar#showStatus(java.lang.String)
     */
    public void showStatus(String s) {
        statusBar.showStatus(s);
    }

    /**    
     * Called by a user interface element when a request to
     * navigate to a model element has been received.
     *
     * @param element the navigation target
     */
    public void navigateTo(Object element) {  
        setTarget(element);
    }

    /**   
     * Called by a user interface element when a request to
     * open a model element in a new window has been recieved.
     *
     * @param element the UI element to be opened
     */
    public void open(Object element) {
    }

    /**
     * Save the positions of the screen splitters, sizes and postion
     * of main window in the properties file
     */
    public void saveScreenConfiguration() {
        if (explorerPane != null)
	    Configuration.setInteger(Argo.KEY_SCREEN_WEST_WIDTH,
				     explorerPane.getWidth());
        if (eastPane != null)
	    Configuration.setInteger(Argo.KEY_SCREEN_EAST_WIDTH,
				     eastPane.getWidth());
        if (northPane != null)
	    Configuration.setInteger(Argo.KEY_SCREEN_NORTH_HEIGHT,
				     northPane.getHeight());
        if (southPane != null)
	    Configuration.setInteger(Argo.KEY_SCREEN_SOUTH_HEIGHT,
				     southPane.getHeight());
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

    /**
     * @param event the event
     */
    public void moduleUnloaded(ArgoModuleEvent event) {
        // TODO:  Disable menu
    }

    /**
     * @param event the event
     */
    public void moduleEnabled(ArgoModuleEvent event) {
        // TODO:  Enable menu
    }

    /**
     * @param event the event
     */
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
     * @see java.beans.PropertyChangeListener#propertyChange(
     *         java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        // the project changed
        if (evt.getPropertyName()
            .equals(ProjectManager.CURRENT_PROJECT_PROPERTY_NAME)) {
            Project p = (Project) evt.getNewValue();
            if (p != null) {
                setTitle(p.getName());             
                //Designer.TheDesigner.getToDoList().removeAllElements();
                Designer.setCritiquingRoot(p);
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
    
    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        Object target = e.getNewTarget();
        if (target instanceof ArgoDiagram) {
	    // ProjectManager.getManager().getCurrentProject().
	    // setActiveDiagram((ArgoDiagram) target);
            updateTitle();
        }
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        Object target = e.getNewTarget();
        if (target instanceof ArgoDiagram) {
	    // ProjectManager.getManager().getCurrentProject().
	    // setActiveDiagram((ArgoDiagram) target);
            updateTitle();
        }
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        Object target = e.getNewTarget();
        if (target instanceof ArgoDiagram) {
	    // ProjectManager.getManager().getCurrentProject().
	    // setActiveDiagram((ArgoDiagram) target);
            updateTitle();
        }
    }    
    // End TargetListener methods

    /**
     * Returns the todopane. 
     * @return ToDoPane
     */
    public ToDoPane getTodoPane() {
        return todoPane;
    }

    /**
     * Sets the splashscreen. Sets the current splashscreen to invisible
     * 
     * @param ss the new splashscreen
     */
    public void setSplashScreen(SplashScreen ss) {
        if (splashScreen != null && splashScreen != ss) {
            splashScreen.setVisible(false);
        }
        splashScreen = ss;
    }

    /**
     * Singleton retrieval method for the projectbrowser. Lazely instantiates
     * the projectbrowser. 
     * @return the singleton instance of the projectbrowser
     */
    public static synchronized ProjectBrowser getInstance() {
        if (theInstance == null) {
            theInstance = new ProjectBrowser("ArgoUML", showSplashScreen);
        }
        return theInstance;
    }

    /**
     * @param splash true if we have to show the splashscreen
     */
    public static synchronized void setSplash(boolean splash) {
        showSplashScreen = splash;
    }

    /**
     * @return Returns the defaultFont.
     */
    public Font getDefaultFont() {
        return defaultFont;
    }
    
} /* end class ProjectBrowser */
