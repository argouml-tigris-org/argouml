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

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.PluggableMenu;
import org.argouml.application.events.ArgoModuleEvent;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ui.ToDoPane;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.swingext.BorderSplitPane;
import org.argouml.swingext.Horizontal;
import org.argouml.swingext.Orientation;
import org.argouml.swingext.Vertical;
import org.argouml.ui.menubar.GenericArgoMenuBar;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.ui.ActionExit;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.ui.IStatusBar;
import org.tigris.gef.util.VectorSet;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;

/** The main window of the ArgoUML application. */

public class ProjectBrowser extends JFrame
    implements IStatusBar, NavigationListener, PropertyChangeListener {
    
    protected static Category cat = 
        Category.getInstance(ProjectBrowser.class);
    
    ////////////////////////////////////////////////////////////////
    // constants

    public final static int DEFAULT_COMPONENTWIDTH = 220;
    public final static int DEFAULT_COMPONENTHEIGHT = 200;

    ////////////////////////////////////////////////////////////////
    // class variables

    public static ProjectBrowser TheInstance;

    // ----- diagrams

    ////////////////////////////////////////////////////////////////
    // instance variables

    protected String _appName = "ProjectBrowser";

    protected MultiEditorPane _editorPane;
    
    /** 
     * the detailspane in the lower right part of the projectbrowser
     */
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
    private NavigationHistory _history = new NavigationHistory();

    /**
     * The diagram which the user is currently working on.
     */
    private ArgoDiagram _activeDiagram;

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

    ////////////////////////////////////////////////////////////////
    // constructors

   

    public ProjectBrowser(String appName, boolean doSplash) {
        super(appName);
        TheInstance = this;
        if (doSplash) {
            _splash = new SplashScreen("Loading ArgoUML...", "Splash");
            _splash.getStatusBar().showStatus("Making Project Browser");
            _splash.getStatusBar().showProgress(10);
            _splash.setVisible(true);
        }
        _menuBar = new GenericArgoMenuBar();
        
            
        _editorPane = new MultiEditorPane();
        _editorPane.addNavigationListener(this);
        
        DetailsPane _eastPane      = makeDetailsPane(BorderSplitPane.EAST.toLowerCase(), Vertical.getInstance());
        _southPane     = makeDetailsPane(BorderSplitPane.SOUTH. toLowerCase(), Horizontal.getInstance());
        DetailsPane _southEastPane = makeDetailsPane(BorderSplitPane.SOUTHEAST.toLowerCase(), Horizontal.getInstance());
        DetailsPane _northWestPane = makeDetailsPane(BorderSplitPane.NORTHWEST.toLowerCase(), Horizontal.getInstance());
        DetailsPane _northPane     = makeDetailsPane(BorderSplitPane.NORTH.toLowerCase(), Horizontal.getInstance());
        DetailsPane _northEastPane = makeDetailsPane(BorderSplitPane.NORTHEAST.toLowerCase(), Horizontal.getInstance());

        if (_southPane != null) detailsPanesByCompassPoint.put(BorderSplitPane.SOUTH, _southPane);
        if (_southEastPane != null) detailsPanesByCompassPoint.put(BorderSplitPane.SOUTHEAST, _southEastPane);
        if (_eastPane != null) detailsPanesByCompassPoint.put(BorderSplitPane.EAST, _eastPane);
        if (_northWestPane != null) detailsPanesByCompassPoint.put(BorderSplitPane.NORTHWEST, _northWestPane);
        if (_northPane != null) detailsPanesByCompassPoint.put(BorderSplitPane.NORTH, _northPane);
        if (_northEastPane != null) detailsPanesByCompassPoint.put(BorderSplitPane.NORTHEAST, _northEastPane);

        getDetailsPane().getTabProps().addNavigationListener(this);

        setAppName(appName);

        getContentPane().setFont(defaultFont);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(_menuBar, BorderLayout.NORTH);
        getContentPane().add(createPanels(doSplash), BorderLayout.CENTER);
        getContentPane().add(_statusBar, BorderLayout.SOUTH);

        // allows me to ask "Do you want to save first?"
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowCloser());
        ImageIcon argoImage = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Model");
        this.setIconImage(argoImage.getImage());
        // 
        
        // adds this as listener to projectmanager so it gets updated when the 
        // project changes
        ProjectManager.getManager().addPropertyChangeListener(this);
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
        Object[] context = { menuitem, key };
        ArrayList arraylist = Argo.getPlugins(PluggableMenu.class, context);
        ListIterator iterator = arraylist.listIterator();
        while (iterator.hasNext()) {
            PluggableMenu module = (PluggableMenu)iterator.next();
            menuitem.add(module.getMenuItem(context));
            menuitem.setEnabled(true);
        }
    }

    /**
     * Creates the panels in the working area
     * @return Component
     */
    protected Component createPanels(boolean doSplash) {
        // Set preferred sizes from config file
        if (_southPane != null) {
            _southPane.setPreferredSize(new Dimension(
                                        0, Configuration.getInteger(Argo.KEY_SCREEN_SOUTH_HEIGHT, DEFAULT_COMPONENTHEIGHT)
                                        ));
        }

        // The workarea is all the visible space except the menu, toolbar and status bar.
        // Workarea is layed out as a BorderSplitPane where the various components that make
        // up the argo application can be positioned.
        _workarea = new BorderSplitPane();
        // create the todopane
        if (doSplash) {
            _splash.getStatusBar().showStatus("Making Project Browser: To Do Pane");
            _splash.getStatusBar().incProgress(5);
        }
        _todoPane = new ToDoPane(doSplash);
        _workarea.add(_todoPane, BorderSplitPane.SOUTHWEST);
        // create the navpane
        if (doSplash) {
            _splash.getStatusBar().showStatus("Making Project Browser: Navigator Pane");
            _splash.getStatusBar().incProgress(5);
        }
        _navPane = new NavigatorPane(doSplash);
        _workarea.add(_navPane, BorderSplitPane.WEST);
        //_workarea.add(_northPane, BorderSplitPane.NORTH);
    
        Iterator it = detailsPanesByCompassPoint.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            _workarea.add((DetailsPane)entry.getValue(), (String)entry.getKey());
        }
        _workarea.add(_editorPane);

        // Toolbar boundry is the area between the menu and the status bar. It contains
        // the workarea at centre and the toolbar position north, south, east or west.
        // @todo - there is work in progress for toolbars - Bob Tarling
        JPanel toolbarBoundry = new JPanel();
        toolbarBoundry.setLayout(new BorderLayout());
        toolbarBoundry.add(_menuBar.getMultiToolbar(), BorderLayout.NORTH);
        toolbarBoundry.add(_workarea, BorderLayout.CENTER);

        return toolbarBoundry;
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public void setTitle(String title) {
        if (title == null || "".equals(title)) setTitle(getAppName());
        else super.setTitle(getAppName() + " - " + title);
    }

    public String getAppName() { return _appName; }
    public void setAppName(String n) { _appName = n; }

    public void setTarget(Object o) {
        
        if (o instanceof MNamespace) {
            ProjectManager.getManager().getCurrentProject().setCurrentNamespace((MNamespace)o);
        } else 
        if (o instanceof MModelElement) {
            if (((MModelElement)o).getNamespace() != null) {
                ProjectManager.getManager().getCurrentProject().setCurrentNamespace(((MModelElement)o).getNamespace());
            } else
                ProjectManager.getManager().getCurrentProject().setCurrentNamespace((MNamespace)ProjectManager.getManager().getCurrentProject().getRoot());
        }
        
        if (o instanceof ArgoDiagram) {
            setActiveDiagram ((ArgoDiagram) o);
            if (o instanceof UMLDiagram) {
                MNamespace m = ((UMLDiagram)o).getNamespace();
                if (m != null) 
                    ProjectManager.getManager().getCurrentProject().setCurrentNamespace(m);
            }       
        }
        _editorPane.setTarget(o);
        setDetailsTarget(o);         
	Actions.updateAllEnabled();
    }

    /** return the current target in the editor pane */
    public Object getTarget() {
        if (_editorPane == null) return null;
        return _editorPane.getTarget();
    }


    /**
     * Set the diagram on which the user is currently working. 
     * When importing sources this should call the name of the folder from
     * which the classes were imported.
     * It should also default the model name as well.
     *{@link #setTarget}.
     */
    public void setActiveDiagram (ArgoDiagram ad) {
        _activeDiagram = ad;    
        cat.debug ("Active diagram set to " + ad.getName());
    }


    /**
     * Return the diagram, the user is currently working on.
     */
    public ArgoDiagram getActiveDiagram() {
        return _activeDiagram;
    }

    /**
     * Select the tab page containing the todo item
     *
     * @todo should introduce an instance variable to go straight to the correct tab instead of trying all
     */
    public void setToDoItem(Object o) {
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        while(it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane)it.next();
            if (detailsPane.setToDoItem(o)) return;
        }
    }

    public void setDetailsTarget(Object o) {
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        while(it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane)it.next();
            detailsPane.setTarget(o);
        }
    }

    public Object getDetailsTarget() {
        return getDetailsPane().getTarget();
    }
    
    /**
     * Returns the detailsPane. This is the pane normally in the lower right 
     * corner of ArgoUML which contains the ToDoPane and the Property panels for
     * example.
     * @return DetailsPane
     */
    public DetailsPane getDetailsPane() {
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        if (it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane)it.next();
            return detailsPane;
        }
        return null;
    }

    public StatusBar getStatusBar() { return _statusBar; }

    public JMenuBar getJMenuBar() { return _menuBar; }

    public MultiEditorPane getEditorPane() { return _editorPane; }

    /**
     * Find the tabpage with the given label and make it the front tab
     * @param The tabpage label
     * @return false if no tab was found of given name
     */
    public void selectTabNamed(String tabName) {
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        while(it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane)it.next();
            if (detailsPane.selectTabNamed(tabName)) return;
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
        while(it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane)it.next();
            panel = detailsPane.getNamedTab(tabName);
            if (panel != null) return panel;
        }
        return null;
        //throw new IllegalArgumentException("No such tab named " + tabName);
    }
    
    public void jumpToDiagramShowing(VectorSet dms) {
        if (dms.size() == 0) return;
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
        Vector diagrams = ProjectManager.getManager().getCurrentProject().getDiagrams();
        Object target = _editorPane.getTarget();
        if ((target instanceof Diagram) && ((Diagram)target).countContained(dms) == dms.size()) {
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
            if (nc == dms.size()) break;
        }
        if (bestDiagram != null) {
            setTarget(bestDiagram);
            setTarget(first);
        }
        // making it possible to jump to the modelroot
        if (first.equals(ProjectManager.getManager().getCurrentProject().getRoot())) {
            setTarget(first);
        }
    }

    ////////////////////////////////////////////////////////////////
    // window operations

    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) org.tigris.gef.base.Globals.setStatusBar(this);
    }

    ////////////////////////////////////////////////////////////////
    // IStatusBar
    public void showStatus(String s) { _statusBar.showStatus(s); }

    /**    Called by a user interface element when a request to
     *    navigate to a model element has been received.
     */
    public void navigateTo(Object element) {
        _history.navigateTo(element);
        setTarget(element);
    }

    /**   Called by a user interface element when a request to
     *   open a model element in a new window has been recieved.
     */
    public void open(Object element) {
    }

    public boolean navigateBack(boolean attempt) {
        boolean navigated = false;
        if(attempt) {
            Object target = _history.navigateBack(attempt);
            if(target != null) {
                navigated = true;
                setTarget(target);
            }
        }
        return navigated;
    }

    public boolean navigateForward(boolean attempt) {
        boolean navigated = false;
        if(attempt) {
            Object target = _history.navigateForward(attempt);
            if(target != null) {
                navigated = true;
                setTarget(target);
            }
        }
        return navigated;
    }

    public boolean isNavigateBackEnabled() {
        return _history.isNavigateBackEnabled();
    }

    public boolean isNavigateForwardEnabled() {
        return _history.isNavigateForwardEnabled();
    }

    /**
     * Save the positions of the screen spliters in the properties file
     */
    public void saveScreenConfiguration() {
        Configuration.setInteger(Argo.KEY_SCREEN_WEST_WIDTH, getNavigatorPane().getWidth());
        Configuration.setInteger(Argo.KEY_SCREEN_SOUTHWEST_WIDTH, getTodoPane().getWidth());
        Configuration.setInteger(Argo.KEY_SCREEN_SOUTH_HEIGHT, _southPane.getHeight());
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
    private DetailsPane makeDetailsPane(String compassPoint, Orientation orientation) {
        DetailsPane detailsPane = new DetailsPane(compassPoint, orientation);
        if (detailsPane.getTabCount() == 0) return null;
        return detailsPane;
    }


    class WindowCloser extends WindowAdapter {
        public WindowCloser() { }
        public void windowClosing(WindowEvent e) {
      
            ActionExit.SINGLETON.actionPerformed(null);
        }
    } /* end class WindowCloser */


    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ProjectManager.CURRENT_PROJECT_PROPERTY_NAME)) {
            Project p = (Project)evt.getNewValue();
            setTitle(p.getName());
            Actions.updateAllEnabled();
            //Designer.TheDesigner.getToDoList().removeAllElements();
            Designer.TheDesigner.setCritiquingRoot(p);
            // update all panes
            setTarget(p.getInitialTarget());
            return;
        }
    }
    
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
     */
    public NavigatorPane getNavigatorPane() {
        return _navPane;    
    }
    
    /**
     * Returns the splashscreen shown at startup. 
     * @return SplashScreen
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
    

} /* end class ProjectBrowser */



