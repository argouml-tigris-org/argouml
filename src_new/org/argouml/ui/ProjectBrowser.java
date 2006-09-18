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

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
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
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.ui.IStatusBar;
import org.tigris.gef.undo.UndoManager;
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
public final class ProjectBrowser
    extends JFrame
    implements IStatusBar, PropertyChangeListener, TargetListener {

    /**
     * Default width.
     */
    public static final int DEFAULT_COMPONENTWIDTH = 220;

    /**
     * Default height.
     */
    public static final int DEFAULT_COMPONENTHEIGHT = 200;

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(ProjectBrowser.class);

    ////////////////////////////////////////////////////////////////
    // class variables

    /**
     * Member attribute to contain the singleton.
     */
    private static ProjectBrowser theInstance;

    ////////////////////////////////////////////////////////////////
    // instance variables

    private String appName = "ProjectBrowser";

    private MultiEditorPane editorPane;

    /*
     * TODO: Work in progress here to allow multiple details panes with
     * different contents - Bob Tarling
     */
    private DetailsPane northEastPane;
    private DetailsPane northPane;
    private DetailsPane northWestPane;
    private DetailsPane eastPane;
    private DetailsPane southEastPane;
    private DetailsPane southPane;

    private Map detailsPanesByCompassPoint = new HashMap();

    private GenericArgoMenuBar menuBar;

    /**
     * Partially implemented. Needs work to display
     * import of source and saving of zargo.
     */
    private StatusBar statusBar = new StatusBar();

    /**
     * TODO: this needs work so that users can set the font
     * size through a gui preference window.
     */
    private Font defaultFont = new Font("Dialog", Font.PLAIN, 10);

    private BorderSplitPane workAreaPane;

    /**
     * The explorer (formerly called navigator) pane
     * containing the modelstructure.
     */
    private NavigatorPane explorerPane;

    /**
     * The todopane (lower left corner of screen).
     */
    private ToDoPane todoPane;

    /**
     * A class that handles the title of this frame, 
     * e.g. to indicate save status.
     */
    private TitleHandler titleHandler = new TitleHandler();


    /**
     * For testing purposes. In tests this constructor can be called so
     * TheInstance is filled.
     */
    private ProjectBrowser() {
        this("ArgoUML", null);
    }

    /**
     * The constructor.
     *
     * @param applicationName  the title of the frame
     * @param splash the splash screen to show at startup
     */
    private ProjectBrowser(String applicationName, SplashScreen splash) {
        super(applicationName);
        theInstance = this;

        ArgoActions.initialize();
        ProjectManager.getManager().setSaveAction(ArgoActions.getSaveAction());

        if (splash != null) {
	    splash.getStatusBar().showStatus(
	        Translator.localize("statusmsg.bar.making-project-browser"));
            splash.getStatusBar().showProgress(10);
            splash.setVisible(true);
        }

        menuBar = new GenericArgoMenuBar();

        editorPane = new MultiEditorPane();
        getContentPane().setFont(defaultFont);
        getContentPane().setLayout(new BorderLayout());
        this.setJMenuBar(menuBar);
        //getContentPane().add(_menuBar, BorderLayout.NORTH);
        getContentPane().add(createPanels(splash), BorderLayout.CENTER);
        getContentPane().add(statusBar, BorderLayout.SOUTH);

        setAppName(applicationName);

        // allows me to ask "Do you want to save first?"
        setDefaultCloseOperation(ProjectBrowser.DO_NOTHING_ON_CLOSE);
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

        // Add a listener to focus changes.
        // Rationale: reset the undo manager to start a new chain.
        KeyboardFocusManager kfm =
            KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kfm.addPropertyChangeListener(new PropertyChangeListener() {
            private Object obj;

            /**
             * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
             */
            public void propertyChange(PropertyChangeEvent evt) {
                if ("focusOwner".equals(evt.getPropertyName())
                        && (evt.getNewValue() != null)
                        /* We get many many events (why?), so let's filter: */
                        && (obj != evt.getNewValue())) {
                    obj = evt.getNewValue();
                    UndoManager.getInstance().startChain();
                    /* This next line is ideal for debugging the taborder
                     * (focus traversal), see e.g. issue 1849.
                     */
//                  System.out.println("Focus changed " + obj);
                }
            }
        });
    }

    /**
     * Singleton retrieval method for the projectbrowser. Lazely instantiates
     * the projectbrowser.
     * @return the singleton instance of the projectbrowser
     */
    public static synchronized ProjectBrowser getInstance() {
        if (theInstance == null) {
            theInstance = new ProjectBrowser();
        }
        return theInstance;
    }

    /**
     * Creator method for the projectbrowser.
     *
     * @return the singleton instance of the projectbrowser
     *
     * @param splash true if we are allowed to show a splash screen
     */
    public static ProjectBrowser makeInstance(SplashScreen splash) {
        return new ProjectBrowser("ArgoUML", splash);
    }

    /**
     * @see java.awt.Component#getLocale()
     */
    public Locale getLocale() {
        return Locale.getDefault();
    }


    /**
     * Creates the panels in the working area.
     *
     * @param splash true if we show  the splashscreen during startup
     * @return Component the area between the menu and the statusbar.
     *                   It contains the workarea at centre and the toolbar
     *                   position north, south, east or west.
     *
     */
    protected Component createPanels(SplashScreen splash) {
        if (splash != null) {
	    splash.getStatusBar().showStatus(
	            Translator.localize(
	                    "statusmsg.bar.making-project-browser-explorer"));
            splash.getStatusBar().incProgress(5);
        }
        explorerPane = new NavigatorPane(splash);

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
        if (eastPane != null) {
            detailsPanesByCompassPoint.put(BorderSplitPane.EAST, eastPane);
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

        // The workarea is all the visible space except the menu,
        // toolbar and status bar.  Workarea is layed out as a
        // BorderSplitPane where the various components that make up
        // the argo application can be positioned.
        workAreaPane = new BorderSplitPane();
        // create the todopane
        if (splash != null) {
	    splash.getStatusBar().showStatus(Translator.localize(
		    "statusmsg.bar.making-project-browser-to-do-pane"));
            splash.getStatusBar().incProgress(5);
        }
        todoPane = new ToDoPane(splash.getStatusBar());
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
            addPanel((Component) entry.getValue(), position);
        }
        addPanel(explorerPane, BorderSplitPane.WEST);

        getTab(TabToDo.class); // TODO: If this doesn't have side effects,
                               //       it can be removed alltogether.
        //todo.setTree(_todoPane);
        addPanel(todoPane, BorderSplitPane.SOUTHWEST);
        addPanel(editorPane, BorderSplitPane.CENTER);
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

    /**
     * Add a panel to a split pane area.
     *
     * @param comp the panel to add
     * @param obj the position (BorderSplitPane.EAST etc)
     */
    void addPanel(Component comp, Object obj) {
        workAreaPane.add(comp, obj);
    }

    /**
     * Remove a panel from a split pane area.
     *
     * @param comp the panel to remove
     */
    void removePanel(Component comp) {
        workAreaPane.remove(comp);
        workAreaPane.validate();
        workAreaPane.repaint();
    }

    /**
     * Set the size of each panel to that last saved in the configuration file.
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

    /**
     * Handle the title-bar of the window.
     * 
     * @author michiel
     */
    private class TitleHandler implements PropertyChangeListener {
        
        private ArgoDiagram monitoredDiagram = null;

    /**
     * Create a title for the main window's title.
     *
         * @param projectFileName the project-file name
         * @param activeDiagram the (new) current diagram
     */
        protected void buildTitle(String projectFileName, 
                ArgoDiagram activeDiagram) {
            if (projectFileName == null || "".equals(projectFileName)) {
                if (ProjectManager.getManager().getCurrentProject() != null) {
                    projectFileName = ProjectManager.getManager()
                        .getCurrentProject().getName();
                }
            }
            if (activeDiagram == null) {
                activeDiagram = ProjectManager.getManager()
                    .getCurrentProject().getActiveDiagram();
            }
            String changeIndicator = "";
            Action saveAction = ArgoActions.getSaveAction();
            if ( saveAction != null && saveAction.isEnabled()) {
                changeIndicator = " *";
            }
            if (activeDiagram != null) {
                if (monitoredDiagram != null) {
                    monitoredDiagram.removePropertyChangeListener("name", this);
                }
                activeDiagram.addPropertyChangeListener("name", this);
                monitoredDiagram = activeDiagram;
                setTitle(projectFileName + " - " + activeDiagram.getName()
                        + " - " + getAppName() + changeIndicator);
            } else {
                setTitle(projectFileName + " - " + getAppName() 
                        + changeIndicator);
            }
        }
        /**
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("name")
                    && evt.getSource() instanceof ArgoDiagram) {
                buildTitle(
                    ProjectManager.getManager().getCurrentProject().getName(), 
                    (ArgoDiagram) evt.getSource());
            }            
        }
    }
    /**
     * Set the save indicator (the * after the title) to appear depending on
     * the curreny save action enabled status.
     */
    public void showSaveIndicator() {
        titleHandler.buildTitle(null, null);
    }

    /**
     * @return the application name ("ArgoUML") as shown in the titlebar
     */
    public String getAppName() {
        return appName;
    }

    /**
     * @param n the application name ("ArgoUML") as shown in the titlebar
     */
    public void setAppName(String n) {
        appName = n;
    }

    /**
     * The method used by the NavigatorPane, MultiEditor and DetailsPane
     * to set the target of the application.<p>
     *
     * the target is either a Model Element (usually selected in
     * the Navigation pane or Properties panel) or a Fig (selected in
     * a diagram).<p>
     *
     * The concept of a selection transaction is used to prevent a change
     * of target in one view creating a call back to this method, which
     * would then change the target in all views again...<p>
     *
     * @param o the target
     */
    private void setTarget(Object o) {
        TargetManager.getInstance().setTarget(o);
    }

    /**
     * Select the tab page containing the todo item.
     *
     * TODO: should introduce an instance variable to go straight to
     * the correct tab instead of trying all
     *
     * TODO:  Have the DetailsPane listen for target changes instead
     * of managing directly from ToDoPane - tfm 20060531
     * 
     * @param o the todo item to select
     */
    public void setToDoItem(Object o) {
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        while (it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane) it.next();
            if (detailsPane.setToDoItem(o)) {
                return;
            }
        }
    }

    /**
     * Get the tab page containing the properties.
     *
     * @return the TabProps tabpage
     */
//    public TabProps getTabProps() {
//        // In theory there can be multiple details pane (work in
//        // progress). It must first be determined which details
//        // page contains the properties tab. Bob Tarling 7 Dec 2002
//        Iterator it = detailsPanesByCompassPoint.values().iterator();
//        while (it.hasNext()) {
//            DetailsPane detailsPane = (DetailsPane) it.next();
//            TabProps tabProps = detailsPane.getTabProps();
//            if (tabProps != null) {
//                return tabProps;
//            }
//        }
//        throw new IllegalStateException("No properties tab found");
//    }

    /**
     * Get the tab page instance of the given class.
     *
     * @param tabClass the given class
     * @return the tabpage
     */
    public AbstractArgoJPanel getTab(Class tabClass) {
        // In theory there can be multiple details pane (work in
        // progress). It must first be determined which details
        // page contains the properties tab. Bob Tarling 7 Dec 2002
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        while (it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane) it.next();
            AbstractArgoJPanel tab = detailsPane.getTab(tabClass);
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
     * Find the tabpage with the given label and make it the front tab.
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
     * Find the tabpage with the given label.
     *
     * @param tabName The tabpage label
     * @return the tabpage
     * @deprecated by Andrea Nironi (0.22 August 2006). Replaced by
     *             {@link org.argouml.ui.ProjectBrowser#getTab(Class tabClass)}.
     *             See issue 3278.
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

    /**
     * Given a list of targets, displays the according diagram.
     * This method jumps to the diagram showing the targets,
     * and scrolls to make it visible.
     *
     * @param targets Collection of targets to show
     */
    public void jumpToDiagramShowing(Collection targets) {

        if (targets == null || targets.size() == 0) {
            return;
        }
        Vector dms = new Vector(targets);
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
            if (nc == dms.size()) {
                break;
            }
        }
        if (bestDiagram != null) {
            if (!ProjectManager.getManager().getCurrentProject()
                    .getActiveDiagram().equals(bestDiagram)) {
                setTarget(bestDiagram);
            }
            setTarget(first);
        }
        // making it possible to jump to the modelroot
        if (first.equals(ProjectManager.getManager().getCurrentProject()
			 .getRoot())) {
            setTarget(first);
        }

        // and finally, adjust the scrollbars to show the Fig
        Project p = ProjectManager.getManager().getCurrentProject();
        if (p != null) {
            Object f = TargetManager.getInstance().getFigTarget();
            if (f instanceof Fig) {
                Globals.curEditor().scrollToShow((Fig) f);
            }
        }
    }

    ////////////////////////////////////////////////////////////////
    // window operations

    /**
     * @see java.awt.Component#setVisible(boolean)
     */
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            Globals.setStatusBar(this);
        }
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
     * Save the positions of the screen splitters, sizes and postion
     * of main window in the properties file.
     */
    private void saveScreenConfiguration() {
        if (explorerPane != null) {
	    Configuration.setInteger(Argo.KEY_SCREEN_WEST_WIDTH,
				     explorerPane.getWidth());
        }

        if (eastPane != null) {
	    Configuration.setInteger(Argo.KEY_SCREEN_EAST_WIDTH,
				     eastPane.getWidth());
        }

        if (northPane != null) {
	    Configuration.setInteger(Argo.KEY_SCREEN_NORTH_HEIGHT,
				     northPane.getHeight());
        }

        if (southPane != null) {
	    Configuration.setInteger(Argo.KEY_SCREEN_SOUTH_HEIGHT,
				     southPane.getHeight());
        }

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
     * Build a new details pane for the given compass point.
     *
     * @param compassPoint the position for which to build the pane
     * @param orientation the required orientation of the pane.
     * @return the details pane or null if none is required for the given
     *         compass point.
     */
    private DetailsPane makeDetailsPane(String compassPoint,
					Orientation orientation) {
        DetailsPane detailsPane =
	    new DetailsPane(compassPoint.toLowerCase(), orientation);
        if (detailsPane.getTabCount() == 0) {
            return null;
        }
        return detailsPane;
    }

    /**
     * Exit the application if no save is required.
     * If a save is required then prompt the user if they wish to,
     * save and exit, exit without saving or cancel the exit operation.
     */
    public void tryExit() {
        Action saveAction = ArgoActions.getSaveAction();
        if (saveAction != null && saveAction.isEnabled()) {
            Project p = ProjectManager.getManager().getCurrentProject();

            String t =
                MessageFormat.format(Translator.localize(
                        "optionpane.exit-save-changes-to"),
                    new Object[] {p.getName()});
            int response =
                JOptionPane.showConfirmDialog(
                    this, t, t, JOptionPane.YES_NO_CANCEL_OPTION);

            if (response == JOptionPane.CANCEL_OPTION
                    || response == JOptionPane.CLOSED_OPTION) {
                return;
            }
            if (response == JOptionPane.YES_OPTION) {
                ProjectLoadSave.trySave(ProjectManager.getManager()
                        .getCurrentProject() != null
                        && ProjectManager.getManager().getCurrentProject()
                                .getURI() != null);
                if (saveAction.isEnabled()) {
                    return;
                }
            }
        }
        saveScreenConfiguration();
        Configuration.save();
        System.exit(0);
    }

    /**
     * @see java.awt.Window#dispose()
     */
    public void dispose() {

    }

    /**
     * Receives window events.
     */
    class WindowCloser extends WindowAdapter {
        /**
         * Constructor.
         */
        public WindowCloser() {
        }

        /**
         * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
         */
        public void windowClosing(WindowEvent e) {
            tryExit();
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
                titleHandler.buildTitle(p.getName(), null);
                //Designer.TheDesigner.getToDoList().removeAllElements();
                Designer.setCritiquingRoot(p);
                // update all panes
                TargetManager.getInstance().setTarget(p.getInitialTarget());
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////
    // TargetListener methods implemented so notified when selected
    // diagram changes. Used to update the window title.

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        Object target = e.getNewTarget();
        if (target instanceof ArgoDiagram) {
            titleHandler.buildTitle(null, (ArgoDiagram) target);
        }
        determineRemoveEnabled();
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        Object target = e.getNewTarget();
        if (target instanceof ArgoDiagram) {
            titleHandler.buildTitle(null, (ArgoDiagram) target);
        }
        determineRemoveEnabled();
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        Object target = e.getNewTarget();
        if (target instanceof ArgoDiagram) {
            titleHandler.buildTitle(null, (ArgoDiagram) target);
        }
        determineRemoveEnabled();
    }

    /**
     * Enabled the remove action if an item is selected in anything other then
     * the activity or state diagrams.
     */
    private void determineRemoveEnabled() {
        Editor editor = Globals.curEditor();
        Collection figs = editor.getSelectionManager().getFigs();
        boolean removeEnabled = !figs.isEmpty();
        GraphModel gm = editor.getGraphModel();
        if (gm instanceof UMLMutableGraphSupport) {
            removeEnabled =
                ((UMLMutableGraphSupport) gm).isRemoveFromDiagramAllowed(figs);
        }
        ArgoActions.getRemoveFromDiagramAction().setEnabled(removeEnabled);
    }

    /**
     * Returns the todopane.
     * @return ToDoPane
     */
    public ToDoPane getTodoPane() {
        return todoPane;
    }

    /**
     * @return Returns the defaultFont.
     */
    public Font getDefaultFont() {
        return defaultFont;
    }

    /**
     * Rebuild the title using the name of the current project.
     *
     */
    public void buildTitleWithCurrentProjectName() {
        titleHandler.buildTitle(
                ProjectManager.getManager().getCurrentProject().getName(), 
                null);
    }
    
    /**
     * We should remove all open dialogs. They have as parent the
     * ProjectBrowser. This is needed for the non-modal dialogs
     * such as Find and Goto.
     */
    public void clearDialogs() {
        Window[] windows = getOwnedWindows();
        for (int i = 0; i < windows.length; i++) {
            windows[i].dispose();
        }
        FindDialog.getInstance().doClearTabs();
        FindDialog.getInstance().doResetFields();
    }

    /**
     * Register a new file saved.
     *
     * @param file The file.
     * @throws IOException if we cannot get the file name from the file.
     */
    public void addFileSaved(File file) throws IOException {
        GenericArgoMenuBar menu = (GenericArgoMenuBar) getJMenuBar();
        menu.addFileSaved(file.getCanonicalPath());
    }


    /**
     * The UID.
     */
    private static final long serialVersionUID = 6974246679451284917L;
} /* end class ProjectBrowser */
