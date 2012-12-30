/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Luis Sergio Oliveira
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

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.application.api.Argo;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoStatusEvent;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.cognitive.Designer;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Command;
import org.argouml.kernel.NonUndoableCommand;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.XmiReferenceException;
import org.argouml.model.XmiReferenceRuntimeException;
import org.argouml.persistence.AbstractFilePersister;
import org.argouml.persistence.OpenException;
import org.argouml.persistence.PersistenceManager;
import org.argouml.persistence.ProjectFilePersister;
import org.argouml.persistence.ProjectFileView;
import org.argouml.persistence.UmlVersionException;
import org.argouml.persistence.VersionException;
import org.argouml.persistence.XmiFormatException;
import org.argouml.taskmgmt.ProgressMonitor;
import org.argouml.ui.cmd.GenericArgoMenuBar;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.ui.ActionRemoveFromDiagram;
import org.argouml.uml.ui.ActionSaveProject;
import org.argouml.util.ArgoFrame;
import org.argouml.util.ThreadUtils;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.util.Util;
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
    implements PropertyChangeListener, TargetListener {

    /**
     * Default width.
     */
    public static final int DEFAULT_COMPONENTWIDTH = 400;

    /**
     * Default height.
     */
    public static final int DEFAULT_COMPONENTHEIGHT = 350;

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ProjectBrowser.class.getName());


    /**
     * Position of pane in overall browser window.
     */
    public enum Position {
        Center, North, South, East, West,
        NorthEast, SouthEast, SouthWest, NorthWest
    }

    // Make sure the correspondence that we depend on doesn't change
    static {
        assert Position.Center.toString().equals(BorderSplitPane.CENTER);
        assert Position.North.toString().equals(BorderSplitPane.NORTH);
        assert Position.NorthEast.toString().equals(BorderSplitPane.NORTHEAST);
        assert Position.South.toString().equals(BorderSplitPane.SOUTH);
    }

    /**
     * Flag to indicate if we are the main application
     * or being integrated in another top level application such
     * as Eclipse (via the ArgoEclipse plugin).
     * TODO: This is a temporary measure until ProjectBrowser
     * can be refactored more appropriately. - tfm
     */
    private static boolean isMainApplication;


    /**
     * Member attribute to contain the singleton.
     */
    private static ProjectBrowser theInstance;


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

    private Map<Position, DetailsPane> detailsPanesByCompassPoint =
        new HashMap<Position, DetailsPane>();

    private GenericArgoMenuBar menuBar;

    /**
     * Partially implemented. Needs work to display
     * import of source and saving of zargo.
     */
    private StatusBar statusBar = new ArgoStatusBar();

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
     * The todopane (lower left corner of screen). This may actually be a blank
     * JPanel if the ProjectBrowser was lazily initialized via
     * {@link #getInstance()}.
     */
    private JPanel todoPane;

    /**
     * A class that handles the title of this frame,
     * e.g. to indicate save status.
     */
    private TitleHandler titleHandler = new TitleHandler();

    /**
     * The action to save the current project.
     */
    private AbstractAction saveAction;

    /**
     * The action to remove the current selected Figs from the diagram.
     */
    private final ActionRemoveFromDiagram removeFromDiagram =
        new ActionRemoveFromDiagram(
                Translator.localize("action.remove-from-diagram"));

    /**
     * For testing purposes. In tests this constructor can be called so
     * TheInstance is filled.
     */
    private ProjectBrowser() {
        this("ArgoUML", null, true, null);
    }

    /**
     * The constructor.
     *
     * @param applicationName
     *            the title of the frame
     * @param splash
     *            the splash screen to show at startup
     * @param mainApplication
     *            flag indicating whether we are the top level application.
     *            False if we are providing components to another top level app.
     * @param leftBottomPane
     *            the panel to fit in the left bottom corner
     */
    private ProjectBrowser(String applicationName, SplashScreen splash,
             boolean mainApplication, JPanel leftBottomPane) {
        super(applicationName);
        theInstance = this;
        isMainApplication = mainApplication;

        getContentPane().setFont(defaultFont);

        // TODO: This causes a cyclic depencency with ActionSaveProject
        saveAction = new ActionSaveProject();
        ProjectManager.getManager().setSaveAction(saveAction);

        createPanels(splash, leftBottomPane);

        if (isMainApplication) {
            menuBar = MenuBarFactory.createApplicationMenuBar();
            getContentPane().setLayout(new BorderLayout());
            this.setJMenuBar(menuBar);
            //getContentPane().add(_menuBar, BorderLayout.NORTH);
            getContentPane().add(assemblePanels(), BorderLayout.CENTER);

            JPanel bottom = new JPanel();
            bottom.setLayout(new BorderLayout());
            bottom.add(statusBar, BorderLayout.CENTER);
            bottom.add(new HeapMonitor(), BorderLayout.EAST);
            getContentPane().add(bottom, BorderLayout.SOUTH);

            setAppName(applicationName);

            // allows me to ask "Do you want to save first?"
            setDefaultCloseOperation(ProjectBrowser.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowCloser());

            setApplicationIcon();

            // Add listener for project changes
            ProjectManager.getManager().addPropertyChangeListener(this);

            // add listener to get notified when active diagram changes
            TargetManager.getInstance().addTargetListener(this);

            // Add a listener to focus changes.
            // Rationale: reset the undo manager to start a new chain.
            addKeyboardFocusListener();
        }
    }

    private void addKeyboardFocusListener() {
        KeyboardFocusManager kfm =
            KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kfm.addPropertyChangeListener(new PropertyChangeListener() {
            private Object obj;

            /*
             * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
             */
            public void propertyChange(PropertyChangeEvent evt) {
                if ("focusOwner".equals(evt.getPropertyName())
                        && (evt.getNewValue() != null)
                    /* We get many many events (why?), so let's filter: */
                        && (obj != evt.getNewValue())) {
                    obj = evt.getNewValue();
                    // TODO: Bob says -
                    // We're looking at focus change to
                    // flag the start of an interaction. This
                    // is to detect when focus is gained in a prop
                    // panel field on the assumption editing of that
                    // field is about to start.
                    // Not a good assumption. We Need to see if we can get
                    // rid of this.
                    Project p =
                        ProjectManager.getManager().getCurrentProject();
                    if (p != null) {
                        p.getUndoManager().startInteraction("Focus");
                    }
                    /* This next line is ideal for debugging the taborder
                     * (focus traversal), see e.g. issue 1849.
                     */
//                      System.out.println("Focus changed " + obj);
                }
            }
        });
    }

    private void setApplicationIcon() {
        final ImageIcon argoImage16x16 =
            ResourceLoaderWrapper.lookupIconResource("ArgoIcon16x16");

        final ImageIcon argoImage32x32 = ResourceLoaderWrapper
                .lookupIconResource("ArgoIcon32x32");
        final List<Image> argoImages = new ArrayList<Image>(2);
        argoImages.add(argoImage16x16.getImage());
        argoImages.add(argoImage32x32.getImage());

        setIconImages(argoImages);
    }

    /**
     * Singleton retrieval method for the projectbrowser.
     * Do not use this before makeInstance is called!
     *
     * @return the singleton instance of the projectbrowser
     */
    public static synchronized ProjectBrowser getInstance() {
        assert theInstance != null;
        return theInstance;
    }

    /**
     * Creator method for the ProjectBrowser which optionally allows all
     * components to be created without making a top level application window
     * visible.
     *
     * @param splash
     *            true if we are allowed to show a splash screen
     * @param mainApplication
     *            true to create a top level application, false if integrated
     *            with something else.
     * @param leftBottomPane panel to place in the bottom left corner of the GUI
     *
     * @return the singleton instance of the projectbrowser
     */
    public static ProjectBrowser makeInstance(SplashScreen splash,
            boolean mainApplication, JPanel leftBottomPane) {
        return new ProjectBrowser("ArgoUML", splash,
                mainApplication, leftBottomPane);
    }

    /*
     * @see java.awt.Component#getLocale()
     */
    @Override
    public Locale getLocale() {
        return Locale.getDefault();
    }


    /**
     * Creates the panels in the working area.
     *
     * @param splash true if we show  the splashscreen during startup
     * @param leftBottomPane panel to be placed in the bottom left (southwest)
     *                corner of the UI.
     */
    protected void createPanels(SplashScreen splash, JPanel leftBottomPane) {

        if (splash != null) {
            splash.getStatusBar().showStatus(
                Translator.localize("statusmsg.bar.making-project-browser"));
            splash.getStatusBar().showProgress(10);
            splash.setVisible(true);
        }

        editorPane = new MultiEditorPane();
        if (splash != null) {
            splash.getStatusBar().showStatus(
                    Translator.localize(
                            "statusmsg.bar.making-project-browser-explorer"));
            splash.getStatusBar().incProgress(5);
        }
        explorerPane = new NavigatorPane(splash);

        // The workarea is all the visible space except the menu,
        // toolbar and status bar.  Workarea is laid out as a
        // BorderSplitPane where the various components that make up
        // the argo application can be positioned.
        workAreaPane = new BorderSplitPane();

        // create the todopane
        if (splash != null) {
            splash.getStatusBar().showStatus(Translator.localize(
                    "statusmsg.bar.making-project-browser-to-do-pane"));
            splash.getStatusBar().incProgress(5);
        }
        todoPane = leftBottomPane;
        createDetailsPanes();
        restorePanelSizes();
    }

    private Component assemblePanels() {
        addPanel(editorPane, Position.Center);
        addPanel(explorerPane, Position.West);
        addPanel(todoPane, Position.SouthWest);

        // There are various details panes all of which could hold
        // different tabs pages according to users settings.
        // Place each pane in the required border area.
        for (Map.Entry<Position, DetailsPane> entry
                : detailsPanesByCompassPoint.entrySet()) {
            Position position = entry.getKey();
            addPanel(entry.getValue(), position);
        }

        // Toolbar boundary is the area between the menu and the status
        // bar. It contains the workarea at centre and the toolbar
        // position north, south, east or west.
        final JPanel toolbarBoundary = new JPanel();
        toolbarBoundary.setLayout(new DockBorderLayout());
        // TODO: - should save and restore the last positions of the toolbars
        final String toolbarPosition = BorderLayout.NORTH;
        toolbarBoundary.add(menuBar.getFileToolbar(), toolbarPosition);
        toolbarBoundary.add(menuBar.getEditToolbar(), toolbarPosition);
        toolbarBoundary.add(menuBar.getViewToolbar(), toolbarPosition);
        toolbarBoundary.add(menuBar.getCreateDiagramToolbar(),
                        toolbarPosition);
        toolbarBoundary.add(workAreaPane, BorderLayout.CENTER);


        /**
         * Registers all toolbars and enables north panel hiding when all
         * toolbars are hidden.
         */
        ArgoToolbarManager.getInstance().registerToolbar(
                menuBar.getFileToolbar(), menuBar.getFileToolbar(), 0);
        ArgoToolbarManager.getInstance().registerToolbar(
                menuBar.getEditToolbar(), menuBar.getEditToolbar(), 1);
        ArgoToolbarManager.getInstance().registerToolbar(
                menuBar.getViewToolbar(), menuBar.getViewToolbar(), 2);
        ArgoToolbarManager.getInstance().registerToolbar(
                menuBar.getCreateDiagramToolbar(),
                menuBar.getCreateDiagramToolbar(), 3);

        final JToolBar[] toolbars = new JToolBar[] {menuBar.getFileToolbar(),
                menuBar.getEditToolbar(), menuBar.getViewToolbar(),
                menuBar.getCreateDiagramToolbar() };
        for (JToolBar toolbar : toolbars) {
            toolbar.addComponentListener(new ComponentAdapter() {
                public void componentHidden(ComponentEvent e) {
                    boolean allHidden = true;
                    for (JToolBar bar : toolbars) {
                        if (bar.isVisible()) {
                            allHidden = false;
                            break;
                        }
                    }

                    if (allHidden) {
                        for (JToolBar bar : toolbars) {
                            toolbarBoundary.getLayout().removeLayoutComponent(
                                    bar);
                        }
                        toolbarBoundary.getLayout().layoutContainer(
                                toolbarBoundary);
                    }
                }

                public void componentShown(ComponentEvent e) {
                    JToolBar oneVisible = null;
                    for (JToolBar bar : toolbars) {
                        if (bar.isVisible()) {
                            oneVisible = bar;
                            break;
                        }
                    }

                    if (oneVisible != null) {
                        toolbarBoundary.add(oneVisible, toolbarPosition);
                        toolbarBoundary.getLayout().layoutContainer(
                                toolbarBoundary);
                    }
                }
            });
        }
        /**
         * END registering toolbar
         */

        return toolbarBoundary;
    }

    private void createDetailsPanes() {
        /*
         * Work in progress here to allow multiple details panes with different
         * contents - Bob Tarling
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
            detailsPanesByCompassPoint.put(Position.South, southPane);
        }
        if (southEastPane != null) {
            detailsPanesByCompassPoint.put(Position.SouthEast,
                    southEastPane);
        }
        if (eastPane != null) {
            detailsPanesByCompassPoint.put(Position.East, eastPane);
        }
        if (northWestPane != null) {
            detailsPanesByCompassPoint.put(Position.NorthWest,
                    northWestPane);
        }
        if (northPane != null) {
            detailsPanesByCompassPoint.put(Position.North, northPane);
        }
        if (northEastPane != null) {
            detailsPanesByCompassPoint.put(Position.NorthEast,
                    northEastPane);
        }

        // Add target listeners for details panes
        Iterator it = detailsPanesByCompassPoint.entrySet().iterator();
        while (it.hasNext()) {
            TargetManager.getInstance().addTargetListener(
                    (DetailsPane) ((Map.Entry) it.next()).getValue());
        }
    }


    /**
     * Add a panel to a split pane area.
     *
     * @param comp the panel to add
     * @param position the position where the panel should be added
     */
    public void addPanel(Component comp, Position position) {
        workAreaPane.add(comp, position.toString());
    }

    /**
     * Remove a panel from a split pane area.
     *
     * @param comp the panel to remove
     */
    public void removePanel(Component comp) {
        workAreaPane.remove(comp);
        workAreaPane.validate();
        workAreaPane.repaint();
    }

    /**
     * Set the size of each panel to that last saved in the configuration file.
     */
    private void restorePanelSizes() {
        if (northPane != null) {
            northPane.setPreferredSize(new Dimension(
                    0, getSavedHeight(Argo.KEY_SCREEN_NORTH_HEIGHT)));
        }
        if (southPane != null) {
            southPane.setPreferredSize(new Dimension(
                    0, getSavedHeight(Argo.KEY_SCREEN_SOUTH_HEIGHT)));
        }
        if (eastPane != null) {
            eastPane.setPreferredSize(new Dimension(
                    getSavedWidth(Argo.KEY_SCREEN_EAST_WIDTH), 0));
        }
        if (explorerPane != null) {
            explorerPane.setPreferredSize(new Dimension(
                    getSavedWidth(Argo.KEY_SCREEN_WEST_WIDTH), 0));
        }
        if (northWestPane != null) {
            northWestPane.setPreferredSize(getSavedDimensions(
                    Argo.KEY_SCREEN_NORTHWEST_WIDTH,
                    Argo.KEY_SCREEN_NORTH_HEIGHT));
        }
        if (todoPane != null) {
            todoPane.setPreferredSize(getSavedDimensions(
                    Argo.KEY_SCREEN_SOUTHWEST_WIDTH,
                    Argo.KEY_SCREEN_SOUTH_HEIGHT));
        }
        if (northEastPane != null) {
            northEastPane.setPreferredSize(getSavedDimensions(
                    Argo.KEY_SCREEN_NORTHEAST_WIDTH,
                    Argo.KEY_SCREEN_NORTH_HEIGHT));
        }
        if (southEastPane != null) {
            southEastPane.setPreferredSize(getSavedDimensions(
                    Argo.KEY_SCREEN_SOUTHEAST_WIDTH,
                    Argo.KEY_SCREEN_SOUTH_HEIGHT));
        }
    }

    // Convenience methods to return saved width and height values
    private Dimension getSavedDimensions(ConfigurationKey width,
            ConfigurationKey height) {
        return new Dimension(getSavedWidth(width), getSavedHeight(height));
    }
    private int getSavedWidth(ConfigurationKey width) {
        return Configuration.getInteger(width, DEFAULT_COMPONENTWIDTH);
    }
    private int getSavedHeight(ConfigurationKey height) {
        return Configuration.getInteger(height, DEFAULT_COMPONENTHEIGHT);
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
            // TODO: Why would this be null?
            if (activeDiagram == null) {
                activeDiagram = DiagramUtils.getActiveDiagram();
            }
            String changeIndicator = "";
            if (saveAction != null && saveAction.isEnabled()) {
                changeIndicator = " *";
            }
            if (activeDiagram != null) {
                if (monitoredDiagram != null) {
                    monitoredDiagram.removePropertyChangeListener("name", this);
                }
                activeDiagram.addPropertyChangeListener("name", this);
                monitoredDiagram = activeDiagram;
                setTitleInternal(projectFileName + " - "
                        + activeDiagram.getName() + " - " + getAppName()
                        + changeIndicator);
            } else {
                setTitleInternal(projectFileName + " - " + getAppName()
                        + changeIndicator);
            }
        }

        private void setTitleInternal(final String title) {
            if (SwingUtilities.isEventDispatchThread()) {
                setTitle(title);
            } else {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        setTitle(title);
                    }
                });
            }
        }

        /*
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
     * the current save action enabled status.
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
     * @param o the todo item to select
     * @deprecated for 0.25.5 by tfmorris. Send an event that the
     *             DetailsPane/TabToDo will be listening for.
     */
    @Deprecated
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
     * Get the tab page instance of the given class.
     *
     * @param tabClass the given class
     * @return the tabpage
     * @deprecated by for 0.25.5 by tfmorris. Tabs should register themselves
     *             with whoever they need to communicate with in a distributed
     *             fashion rather than relying on a central registry. Currently
     *             the only place this is used is to communicate between WizStep
     *             and TabToDo in the Cognitive subsystem.
     */
    @Deprecated
    public AbstractArgoJPanel getTab(Class tabClass) {
        // In theory there can be multiple details pane (work in
        // progress). It must first be determined which details
        // page contains the properties tab. Bob Tarling 7 Dec 2002
        for (DetailsPane detailsPane : detailsPanesByCompassPoint.values())  {
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

    /*
     * @see javax.swing.JFrame#getJMenuBar()
     */
    @Override
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
     * @return the explorer pane
     */
    public NavigatorPane getExplorerPane() {
        return explorerPane;
    }

    /**
     * @return the details pane
     */
    public JPanel getDetailsPane() {
        return southPane;
    }


    /*
     * @see java.awt.Component#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            Globals.setStatusBar(getStatusBar());
        }
    }

    private void updateStatus(String status) {
        ArgoEventPump.fireEvent(new ArgoStatusEvent(ArgoEventTypes.STATUS_TEXT,
                this, status));
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
        if (todoPane != null) {
            Configuration.setInteger(Argo.KEY_SCREEN_SOUTHWEST_WIDTH,
                    todoPane.getWidth());
        }
        if (southEastPane != null) {
            Configuration.setInteger(Argo.KEY_SCREEN_SOUTHEAST_WIDTH,
                    southEastPane.getWidth());
        }
        if (northWestPane != null) {
            Configuration.setInteger(Argo.KEY_SCREEN_NORTHWEST_WIDTH,
                    northWestPane.getWidth());
        }
        if (northEastPane != null) {
            Configuration.setInteger(Argo.KEY_SCREEN_NORTHEAST_WIDTH,
                    northEastPane.getWidth());
        }

        boolean maximized = getExtendedState() == MAXIMIZED_BOTH;
        if (!maximized) {
            Configuration.setInteger(Argo.KEY_SCREEN_WIDTH, getWidth());
            Configuration.setInteger(Argo.KEY_SCREEN_HEIGHT, getHeight());
            Configuration.setInteger(Argo.KEY_SCREEN_LEFT_X, getX());
            Configuration.setInteger(Argo.KEY_SCREEN_TOP_Y, getY());
        }
        Configuration.setBoolean(Argo.KEY_SCREEN_MAXIMIZED,
                maximized);
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
        if (!detailsPane.hasTabs()) {
            return null;
        }
        return detailsPane;
    }

    /**
     * Exit the application if no save is required.
     * If a save is required then prompt the user if they wish to,
     * save and exit, exit without saving or cancel the exit operation.
     */
    public boolean tryExit() {
        LOG.log(Level.INFO, "Trying to exit the application");
        if (saveAction != null && saveAction.isEnabled()) {
            LOG.log(Level.INFO, "A save is needed - prompting the user");
            Project p = ProjectManager.getManager().getCurrentProject();

            String t =
                MessageFormat.format(Translator.localize(
                        "optionpane.exit-save-changes-to"),
                    new Object[] {p.getName()});
            int response =
                JOptionPane.showConfirmDialog(
                    this, t, t, JOptionPane.YES_NO_CANCEL_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                // The trySave method results in the save taking place in another thread.
                // If that completes without error the ProjectBrowser.exit() method will
                // be called which will actually exist the system.
                LOG.log(Level.INFO, "The user chose to exit and save");
                trySave(ProjectManager.getManager().getCurrentProject() != null
                        && ProjectManager.getManager().getCurrentProject()
                                .getURI() != null,
                                false, true);
                return false;
            } else if (response == JOptionPane.CANCEL_OPTION) {
                LOG.log(Level.INFO, "The user cancelled the save dialog");
                return false;
            }
        }
        LOG.log(Level.INFO, "We will now exit");
        exit();
        return true;
    }


    /**
     * Exit the application saving the current user settings.
     */
    public void exit() {
        saveScreenConfiguration();
        Configuration.save();
        System.exit(0);
    }

    /*
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

        /*
         * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
         */
        public void windowClosing(WindowEvent e) {
            LOG.log(Level.INFO,
                    "Detected attempt to close application - "
                    + "checking if save needed");
            tryExit();
        }
    } /* end class WindowCloser */

    /*
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
            // TODO: Do we want to use the Project here instead of just its name?
            ArgoEventPump.fireEvent(new ArgoStatusEvent(
                    ArgoEventTypes.STATUS_PROJECT_LOADED, this, p.getName()));
        }
    }

    /////////////////////////////////////////////////////////////////////////
    // TargetListener methods implemented so notified when selected
    // diagram changes. Used to update the window title.

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
    	targetChanged(e.getNewTarget());
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
    	targetChanged(e.getNewTarget());
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
    	targetChanged(e.getNewTarget());
    }

    /**
     * Called to update the current namespace and active diagram after
     * the target has changed.
     *
     * @param target the new target
     */
    private void targetChanged(Object target) {
        if (target instanceof ArgoDiagram) {
            titleHandler.buildTitle(null, (ArgoDiagram) target);
        }
        determineRemoveEnabled();

        Project p = ProjectManager.getManager().getCurrentProject();

        Object theCurrentNamespace = null;
        target = TargetManager.getInstance().getTarget();
        if (target instanceof ArgoDiagram) {
            theCurrentNamespace = ((ArgoDiagram) target).getNamespace();
        } else if (Model.getFacade().isANamespace(target)) {
            theCurrentNamespace = target;
        } else if (Model.getFacade().isAModelElement(target)) {
            theCurrentNamespace = Model.getFacade().getNamespace(target);
        } else {
            theCurrentNamespace = p.getRoot();
        }
        p.setCurrentNamespace(theCurrentNamespace);

        if (target instanceof ArgoDiagram) {
            p.setActiveDiagram((ArgoDiagram) target);
        }
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
        removeFromDiagram.setEnabled(removeEnabled);
    }

    /**
     * Returns the todopane.
     * @return ToDoPane
     */
    public JPanel getTodoPane() {
        return todoPane;
    }

    /**
     * @return Returns the defaultFont.
     */
    public Font getDefaultFont() {
        return defaultFont;
    }

    /**
     * Try to save the project, possibly not creating a new file
     * @param overwrite if true, then we overwrite without asking
     */
    public void trySave(boolean overwrite) {
        this.trySave(overwrite, false);
    }

    /**
     * Try to save the project.
     * @param overwrite if true, then we overwrite without asking
     * @param saveNewFile if true, we'll ask for a new file even if
     *                    the current project already had one
     */
    public void trySave(boolean overwrite, boolean saveNewFile) {
        trySave(overwrite, saveNewFile, false);
    }

    /**
     * Try to save the project.
     * @param overwrite if true, then we overwrite without asking
     * @param saveNewFile if true, we'll ask for a new file even if
     *                    the current project already had one
     * @param exitAfterSave The application will exit when the save has
     * completed successfully
     */
    public void trySave(
            final boolean overwrite,
            boolean saveNewFile,
            final boolean exitAfterSave) {
        URI uri = ProjectManager.getManager().getCurrentProject().getURI();

        File file = null;

        // this method is invoked from several places, so we have to check
        // whether if the project uri is set or not
        if (uri != null && !saveNewFile) {
            file = new File(uri);

            // does the file really exists?
            if (!file.exists()) {
                LOG.log(Level.WARNING, "Project file not found - URI: " + uri);
                // project file doesn't exist. let's pop up a message dialog..
                int response = JOptionPane.showConfirmDialog(
                        this,
                        Translator.localize(
                                "optionpane.save-project-file-not-found"),
                        Translator.localize(
                                "optionpane.save-project-file-not-found-title"),
                        JOptionPane.YES_NO_OPTION);

                // ..and let's ask the user whether he wants to save the actual
                // project into a new file or not
                if (response == JOptionPane.YES_OPTION) {
                    saveNewFile = true;
                } else {
                    // save action has been cancelled
                    return;
                }
            }
        } else {
            // Attempt to save this project under a new name.
            saveNewFile = true;
        }

        // Prompt the user for the new name.
        if (saveNewFile) {
            file = getNewFile();

            // if the user cancelled the operation,
            // we don't have to save anything
            if (file == null) {
                return;
            }
        }

        // let's call the real save method
        trySaveWithProgressMonitor(overwrite, file, exitAfterSave);
    }

    /**
     * Checks if the given file is writable or read-only
     * @param file the file to be checked
     * @return true if the given file is read-only
     */
    private boolean isFileReadonly(File file) {
        try {
            return (file == null)
                || (file.exists() && !file.canWrite())
                || (!file.exists() && !file.createNewFile());

        } catch (IOException ioExc) {
            return true;
        }
    }

    /**
     * Loads a project displaying a nice ProgressMonitor
     *
     * @param overwrite if true, the file is going to be overwritten
     * @param file      the target file
     *
     * TODO: Separate this into a Swing specific class - tfm
     * @param exit if true: exit ArgoUML when done
     */
    public void trySaveWithProgressMonitor(
            final boolean overwrite,
            final File file,
            final boolean exit) {
        if (!PersistenceManager.getInstance().confirmOverwrite(
                ArgoFrame.getFrame(), overwrite, file)) {
            return;
        }
        if (this.isFileReadonly(file)) {
            JOptionPane.showMessageDialog(this,
                    Translator.localize(
                            "optionpane.save-project-read-only"),
                    Translator.localize(
                            "optionpane.save-project-read-only-title"),
                          JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        SaveSwingWorker worker = new SaveSwingWorker(
                ProjectManager.getManager().getCurrentProject(),
                file,
                exit);
        LOG.log(Level.INFO, "Starting save thread");
        worker.start();
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
     * Try to save the project.
     * @param overwrite if true, then we overwrite without asking
     * @param file the File to save to
     * @param pmw       the ProgressMonitor to be updated;
     * @return true if successful
     *
     * TODO: Separate this into a Swing specific class - tfm
     * @deprecated in 0.29.1 by Bob Tarling use trySaveWithProgressMonitor
     */
    @Deprecated
    public boolean trySave(boolean overwrite,
            File file,
            ProgressMonitor pmw) {
        LOG.log(Level.INFO, "Saving the project");

        if (!PersistenceManager.getInstance().confirmOverwrite(
                ArgoFrame.getFrame(), overwrite, file)) {
            return false;
        }

        if (this.isFileReadonly(file)) {
            JOptionPane.showMessageDialog(this,
                    Translator.localize(
                            "optionpane.save-project-read-only"),
                    Translator.localize(
                            "optionpane.save-project-read-only-title"),
                          JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        Project project = ProjectManager.getManager().getCurrentProject();
        return trySave(file, pmw, project);
    }

    /**
     * Save the project.
     * @param file the File to save to
     * @param pmw       the ProgressMonitor to be updated;
     * @return true if successful
     *
     * TODO: Separate this into a Swing specific class - tfm
     */
    boolean trySave(
            final File file,
            final ProgressMonitor pmw,
            final Project project) {
        LOG.log(Level.INFO, "Saving the project");
        PersistenceManager pm = PersistenceManager.getInstance();
        ProjectFilePersister persister = null;

        try {
            String sStatus =
                MessageFormat.format(Translator.localize(
                    "statusmsg.bar.save-project-status-writing"),
                         new Object[] {file});
            updateStatus (sStatus);

            persister = pm.getSavePersister();
            pm.setSavePersister(null);
            if (persister == null) {
                persister = pm.getPersisterFromFileName(file.getName());
            }
            if (persister == null) {
                throw new IllegalStateException("Filename " + project.getName()
                            + " is not of a known file type");
            }

            testSimulateErrors();

            // Repair any errors in the project
            String report = project.repair();
            if (report.length() > 0) {
                // TODO: i18n
                report =
                    "An inconsistency has been detected when saving the model."
                        + "These have been repaired and are reported below. "
                        + "The save will continue with the model having been "
                        + "amended as described.\n" + report;
                reportError(
                        pmw,
                        Translator.localize("dialog.repair") + report, true);
            }

            if (pmw != null) {
                pmw.updateProgress(25);
                persister.addProgressListener(pmw);
            }

            project.preSave();
            persister.save(project, file);
            project.postSave();

            ArgoEventPump.fireEvent(new ArgoStatusEvent(
                    ArgoEventTypes.STATUS_PROJECT_SAVED, this,
                    file.getAbsolutePath()));
            LOG.fine ("setting most recent project file to "
                   + file.getCanonicalPath());

            /*
             * notification of menu bar
             */
            if (saveAction != null) {
                // Bob says - not sure how saveAction could be null here but
                // NPE has been reported. See issue 6233. As Tom comments
                // elsewhere we should be listening for file save events.
                // That would allow us to have a final saveAction instance
                // that can never be null
                saveAction.setEnabled(false);
            }

            addFileSaved(file);

            Configuration.setString(Argo.KEY_MOST_RECENT_PROJECT_FILE,
                        file.getCanonicalPath());

            return true;
        } catch (Exception ex) {
            String sMessage =
                MessageFormat.format(Translator.localize(
                    "optionpane.save-project-general-exception"),
                         new Object[] {ex.getMessage()});

            JOptionPane.showMessageDialog(this, sMessage,
                    Translator.localize(
                    "optionpane.save-project-general-exception-title"),
                          JOptionPane.ERROR_MESSAGE);

            reportError(
                    pmw,
                    Translator.localize(
                            "dialog.error.save.error",
                            new Object[] {file.getName()}),
                    true, ex);

            LOG.log(Level.SEVERE,sMessage, ex);
        }

        return false;
    }

    /*
     * Simulate some errors to repair.
     * Replace with junits - Bob
     */
    private void testSimulateErrors() {
        // Change to true to enable testing
        if (false) {
            Layer lay =
                Globals.curEditor().getLayerManager().getActiveLayer();
            List figs = lay.getContentsNoEdges();
            // A Fig with a null owner
            if (figs.size() > 0) {
                Fig fig = (Fig) figs.get(0);
                LOG.log(Level.SEVERE, "Setting owner of "
                        + fig.getClass().getName() + " to null");
                fig.setOwner(null);
            }
            // A Fig with a null layer
            if (figs.size() > 1) {
                Fig fig = (Fig) figs.get(1);
                fig.setLayer(null);
            }
            // A Fig with a removed model element
            if (figs.size() > 2) {
                Fig fig = (Fig) figs.get(2);
                Object owner = fig.getOwner();
                Model.getUmlFactory().delete(owner);
            }
        }
    }

    /**
     * Register a new file saved.
     *
     * @param file The file.
     * @throws IOException if we cannot get the file name from the file.
     */
    public void addFileSaved(File file) throws IOException {
        // TODO: This should listen for file save events - tfm
        GenericArgoMenuBar menu = (GenericArgoMenuBar) getJMenuBar();
        if (menu != null) {
            menu.addFileSaved(file.getCanonicalPath());
        }
    }

    /**
     * If the current project is dirty (needs saving) then this function will
     * ask confirmation from the user.
     * If the user indicates that saving is needed, then saving is attempted.
     * See ActionExit.actionPerformed() for a very similar procedure!
     *
     * @return true if we can continue with opening
     */
    public boolean askConfirmationAndSave() {
        ProjectBrowser pb = ProjectBrowser.getInstance();
        Project p = ProjectManager.getManager().getCurrentProject();


        if (p != null && saveAction.isEnabled()) {
            String t =
                MessageFormat.format(Translator.localize(
                        "optionpane.open-project-save-changes-to"),
                        new Object[] {p.getName()});

            int response =
                JOptionPane.showConfirmDialog(pb, t, t,
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (response == JOptionPane.CANCEL_OPTION
                    || response == JOptionPane.CLOSED_OPTION) {
                return false;
            }
            if (response == JOptionPane.YES_OPTION) {

                trySave(ProjectManager.getManager().getCurrentProject() != null
                        && ProjectManager.getManager().getCurrentProject()
                                .getURI() != null);
                if (saveAction.isEnabled()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Loads a project displaying a nice ProgressMonitor
     *
     * @param file      the project to be opened
     * @param showUI    whether to show the GUI or not
     *
     * TODO: This needs to be refactored to be GUI independent - tfm
     */
    public void loadProjectWithProgressMonitor(File file, boolean showUI) {
        LoadSwingWorker worker = new LoadSwingWorker(file, showUI);
        worker.start();
    }

    /**
     * Loads the project file and opens all kinds of error message windows
     * if it doesn't work for some reason. In those cases it preserves
     * the old project.
     *
     * @param file the file to open.
     * @param showUI true if an error message may be shown to the user,
     *               false if run in commandline mode
     * @param pmw       the ProgressMonitor to be updated;
     *                          if not needed, use null
     * @return true if the file was successfully opened
     *
     * TODO: Separate this into a Swing specific class - tfm
     */
    public boolean loadProject(File file, boolean showUI,
            ProgressMonitor pmw) {
        return (loadProject2(file, showUI, pmw) != null);
    }

    /**
     * Loads the project file and opens all kinds of error message windows
     * if it doesn't work for some reason. In those cases it preserves
     * the old project.
     *
     * @param file the file to open.
     * @param showUI true if an error message may be shown to the user,
     *               false if run in commandline mode
     * @param pmw 	the ProgressMonitor to be updated;
     * 				if not needed, use null
     * @return project the project that was created based on the file that was
     *                 successfully opened
     *
     * TODO: Separate this into a Swing specific class - tfm
     */
    public Project loadProject2(File file, boolean showUI,
            ProgressMonitor pmw) {
        LOG.log(Level.INFO, "Loading project.");

        PersistenceManager pm = PersistenceManager.getInstance();
        Project oldProject = ProjectManager.getManager().getCurrentProject();
        if (oldProject != null) {
            // Remove the old project first.  It's wasteful to create a temp
            // empty project, but too much of ArgoUML depends on having a
            // current project
            Project p = ProjectManager.getManager().makeEmptyProject();
            ProjectManager.getManager().setCurrentProject(p);
            ProjectManager.getManager().removeProject(oldProject);
            oldProject = p;
        }

        boolean success = false;

        // TODO:
        // This is actually a hack! Some diagram types
        // (like the statechart diagrams) access the current
        // diagram to get some info. This might cause
        // problems if there's another statechart diagram
        // active, so I remove the current project, before
        // loading the new one.

        Designer.disableCritiquing();
        Designer.clearCritiquing();
        clearDialogs();
        Project project = null;

        if (!(file.canRead())) {
            reportError(pmw, "File not found " + file + ".", showUI);
            Designer.enableCritiquing();
            success = false;
        } else {
            // Hide save action during load. Otherwise we get the
            // * appearing in title bar and the save enabling as models are
            // updated
            // TODO: Do we still need this now the save enablement is improved?
            final AbstractAction rememberedSaveAction = this.saveAction;
            this.saveAction = null;
            ProjectManager.getManager().setSaveAction(null);
            try {
                ProjectFilePersister persister =
                    pm.getPersisterFromFileName(file.getName());
                if (persister == null) {
                    throw new IllegalStateException("Filename "
                            + file.getName()
                            + " is not of a known file type");
                }

                if (pmw != null) {
                    persister.addProgressListener(pmw);
                }


                project = persister.doLoad(file);

                if (pmw != null) {
                    persister.removeProgressListener(pmw);
                }
                ThreadUtils.checkIfInterrupted();

//                if (Model.getDiagramInterchangeModel() != null) {
                // TODO: This assumes no more than one project at a time
                // will be loaded.  If it is ever reinstituted, this needs to
                // be fixed
//                    Collection diagrams =
//                        DiagramFactory.getInstance().getDiagram();
//                    Iterator diag = diagrams.iterator();
//                    while (diag.hasNext()) {
//                        project.addMember(diag.next());
//                    }
//                    if (!diagrams.isEmpty()) {
//                        project.setActiveDiagram(
//                                (ArgoDiagram) diagrams.iterator().next());
//                    }
//                }

                // Let's save this project in the mru list
                this.addFileSaved(file);
                // Let's save this project as the last used one
                // in the configuration file
                Configuration.setString(Argo.KEY_MOST_RECENT_PROJECT_FILE,
                        file.getCanonicalPath());

                updateStatus(
                        Translator.localize(
                                "statusmsg.bar.open-project-status-read",
                                new Object[] {file.getName(), }));
                success = true;
            } catch (VersionException ex) {
                reportError(
                        pmw,
                        Translator.localize(
                                "dialog.error.file.version.error",
                                new Object[] {ex.getMessage()}),
                        showUI);
            } catch (OutOfMemoryError ex) {
                LOG.log(Level.SEVERE,
                        "Out of memory while loading project", ex);
                reportError(
                        pmw,
                        Translator.localize("dialog.error.memory.limit"),
                        showUI);
            } catch (java.lang.InterruptedException ex) {
                LOG.log(Level.SEVERE, "Project loading interrupted by user");
            } catch (UmlVersionException ex) {
                reportError(
                        pmw,
                        Translator.localize(
                                "dialog.error.file.version.error",
                                new Object[] {ex.getMessage()}),
                        showUI, ex);
            } catch (XmiFormatException ex) {
                if (ex.getCause() instanceof XmiReferenceException) {
                    // an error that can be corrected by the user, so no stack
                    // trace, but instead an explanation and a hint how to fix
                    String reference =
                        ((XmiReferenceException) ex.getCause()).getReference();
                    reportError(
                            pmw,
                            Translator.localize(
                                    "dialog.error.xmi.reference.error",
                                    new Object[] {reference, ex.getMessage()}),
                            ex.toString(),
                            showUI);
                } else {
                    reportError(
                            pmw,
                            Translator.localize(
                                    "dialog.error.xmi.format.error",
                                    new Object[] {ex.getMessage()}),
                                    showUI, ex);
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Exception while loading project", ex);
                reportError(
                        pmw,
                        Translator.localize(
                                "dialog.error.open.error",
                                new Object[] {file.getName()}),
                        showUI, ex);
            } catch (OpenException ex) {
                LOG.log(Level.SEVERE, "Exception while loading project", ex);
                reportError(
                        pmw,
                        Translator.localize(
                                "dialog.error.open.error",
                                new Object[] {file.getName()}),
                        showUI, ex);
            } catch (XmiReferenceRuntimeException ex) {
                // an error that can be corrected by the user, so no stack
                // trace, but instead an explanation and a hint how to fix
                reportError(pmw,
                    Translator.localize("dialog.error.xmi.reference.error",
                        new Object[] {ex.getReference(), ex.getMessage()}),
                    ex.toString(), showUI);
            } catch (RuntimeException ex) {
                LOG.log(Level.SEVERE, "Exception while loading project", ex);
                reportError(
                        pmw,
                        Translator.localize(
                                "dialog.error.open.error",
                                new Object[] {file.getName()}),
                        showUI, ex);
            } finally {

                try {
                    if (!success) {
                        project =
                            ProjectManager.getManager().makeEmptyProject();
                    }
                    ProjectManager.getManager().setCurrentProject(project);
                    if (oldProject != null) {
                        ProjectManager.getManager().removeProject(oldProject);
                    }

                    project.getProjectSettings().init();

                    Command cmd = new NonUndoableCommand() {
                        public Object execute() {
                            // This is temporary. Load project
                            // should create a new project
                            // with its own UndoManager and so
                            // there should be no Command
                            return null;
                        }
                    };
                    project.getUndoManager().addCommand(cmd);

                    LOG.log(Level.INFO,
                            "There are {0} diagrams in the current project",
                            project.getDiagramList().size());

                    Designer.enableCritiquing();
                } finally {
                    // Make sure save action is always reinstated
                    this.saveAction = rememberedSaveAction;

                    // We clear the save-required flag on the Swing event thread
                    // in the hopes that it gets done after any other background
                    // work (listener updates) that is being done there
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            ProjectManager.getManager().setSaveAction(
                                    rememberedSaveAction);
                            rememberedSaveAction.setEnabled(false);
                        }
                    });
                }
            }
        }
        if (!success) {
            return null;
        }
        return project;
    }

    /**
     * Open a Message Dialog with an error message.
     *
     * @param message the message to display.
     * @param showUI true if an error message may be shown to the user,
     *               false if run in commandline mode
     */
    private void reportError(ProgressMonitor monitor, final String message,
            boolean showUI) {
        if (showUI) {
            if (monitor != null) {
                monitor.notifyMessage(
                        Translator.localize("dialog.error.title"),
                        Translator.localize("dialog.error.open.save.error"),
                        message);
            } else {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        JDialog dialog =
                            new ExceptionDialog(
                                    ArgoFrame.getFrame(),
                                    Translator.localize("dialog.error.title"),
                                    Translator.localize(
                                            "dialog.error.open.save.error"),
                                    message);
                        dialog.setVisible(true);
                    }
                });
            }
        } else {
            System.err.print(message);
        }
    }

    /**
     * Open a Message Dialog with an error message.
     *
     * @param message the message to display.
     * @param showUI true if an error message may be shown to the user,
     *               false if run in commandline mode
     * @param ex The exception that was thrown.
     */
    private void reportError(ProgressMonitor monitor, final String message,
            boolean showUI, final Throwable ex) {
        if (showUI) {
            if (monitor != null) {
                monitor.notifyMessage(
                        Translator.localize("dialog.error.title"),
                        message,
                        ExceptionDialog.formatException(
                                message, ex, ex instanceof OpenException));
            } else {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        JDialog dialog =
                            new ExceptionDialog(
                                    ArgoFrame.getFrame(),
                                    Translator.localize("dialog.error.title"),
                                    message,
                                    ExceptionDialog.formatException(
                                                message, ex,
                                                ex instanceof OpenException));
                        dialog.setVisible(true);
                    }
                });
            }
        } else {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exception = sw.toString();
            // TODO:  Does anyone use command line?
            // If so, localization is needed - tfm
            reportError(monitor, "Please report the error below to the ArgoUML"
                    + "development team at http://argouml.tigris.org.\n"
                    + message + "\n\n" + exception, showUI);
        }
    }

    /**
     * Open a Message Dialog with an error message and an explanation. No
     * stack trace, since it's not an application error, but a user issue.
     *
     * @param message the message to display.
     * @param explanation the explanation to display.
     * @param showUI true if an error message may be shown to the user,
     *               false if run in commandline mode
     */
    private void reportError(ProgressMonitor monitor, final String message,
            final String explanation, boolean showUI) {
        if (showUI) {
            if (monitor != null) {
                monitor.notifyMessage(
                        Translator.localize("dialog.error.title"),
                        explanation,
                        message);
            } else {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        JDialog dialog =
                            new ExceptionDialog(
                                    ArgoFrame.getFrame(),
                                    Translator.localize("dialog.error.title"),
                                    explanation,
                                    message);
                        dialog.setVisible(true);
                    }
                });
            }
        } else {
            reportError(monitor, message + "\n" + explanation + "\n\n",
                    showUI);
        }
    }

    /**
     * We should remove all open dialogs. They have as parent the
     * ProjectBrowser. This is needed for the non-modal dialogs
     * such as Find and Goto.
     */
    public void clearDialogs() {
        Window[] windows = getOwnedWindows();
        for (int i = 0; i < windows.length; i++) {
            if (!(windows[i] instanceof FindDialog)) {
                windows[i].dispose();
            }
        }
        FindDialog.getInstance().reset();
    }

    /**
     * Get the action that can save the current project.
     * @return the save action.
     */
    public AbstractAction getSaveAction() {
        return saveAction;
    }

    /**
     * Get the action that removes selected figs from the diagram.
     * @return the remove from diagram action.
     */
    public AbstractAction getRemoveFromDiagramAction() {
        return removeFromDiagram;
    }

    /**
     * @return the File to save to
     */
    protected File getNewFile() {
        ProjectBrowser pb = ProjectBrowser.getInstance();
        Project p = ProjectManager.getManager().getCurrentProject();

        JFileChooser chooser = null;
        URI uri = p.getURI();

        if (uri != null) {
            File projectFile = new File(uri);
            if (projectFile.length() > 0) {
                chooser = new JFileChooser(projectFile);
            } else {
                chooser = new JFileChooser();
            }
            chooser.setSelectedFile(projectFile);
        } else {
            chooser = new JFileChooser();
        }

        String sChooserTitle =
            Translator.localize("filechooser.save-as-project");
        chooser.setDialogTitle(sChooserTitle + " " + p.getName());

        // adding project files icon
        chooser.setFileView(ProjectFileView.getInstance());

        chooser.setAcceptAllFileFilterUsed(false);

        PersistenceManager.getInstance().setSaveFileChooserFilters(
                chooser,
                uri != null ? Util.URIToFilename(uri.toString()) : null);

        int retval = chooser.showSaveDialog(pb);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File theFile = chooser.getSelectedFile();
            AbstractFilePersister filter =
                (AbstractFilePersister) chooser.getFileFilter();
            if (theFile != null) {
                Configuration.setString(
                        PersistenceManager.KEY_PROJECT_NAME_PATH,
                        PersistenceManager.getInstance().getBaseName(
                                theFile.getPath()));
                String name = theFile.getName();
                if (!name.endsWith("." + filter.getExtension())) {
                    theFile =
                        new File(
                            theFile.getParent(),
                            name + "." + filter.getExtension());
                }
            }
            PersistenceManager.getInstance().setSavePersister(filter);
            return theFile;
        }
        return null;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 6974246679451284917L;
} /* end class ProjectBrowser */
