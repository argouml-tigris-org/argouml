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

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

import ru.novosoft.uml.foundation.core.*;

import org.tigris.gef.base.*;
import org.tigris.gef.ui.*;
import org.tigris.gef.util.*;

import org.apache.log4j.Category;

import org.argouml.ui.menubar.GenericArgoMenuBar;
import org.argouml.application.api.*;
import org.argouml.application.events.*;
import org.argouml.kernel.*;
import org.argouml.cognitive.*;
import org.argouml.cognitive.ui.*;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.ui.*;
import org.argouml.swingext.*;

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
    protected DetailsPane _northEastPane;
    protected DetailsPane _northPane;
    protected DetailsPane _northWestPane;
    protected DetailsPane _eastPane;
    protected DetailsPane _southEastPane;
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


    ////////////////////////////////////////////////////////////////
    // constructors

   

    public ProjectBrowser(String appName, StatusBar sb, int theme) {
        super(appName);
        LookAndFeelMgr.SINGLETON.setCurrentTheme(theme);
        _menuBar = new GenericArgoMenuBar();
        sb.showStatus("Making Project Browser: Navigator Pane");
        sb.incProgress(5);
        NavigatorPane.getNavigatorPane();
        sb.showStatus("Making Project Browser: To Do Pane");
        sb.incProgress(5);
        // init todopane
        ToDoPane.getToDoPane();        
        _editorPane = new MultiEditorPane(sb);
        _editorPane.addNavigationListener(this);
        
        _eastPane      = makeDetailsPane(sb, BorderSplitPane.EAST.toLowerCase(), Vertical.getInstance());
        _southPane     = makeDetailsPane(sb, BorderSplitPane.SOUTH. toLowerCase(), Horizontal.getInstance());
        _southEastPane = makeDetailsPane(sb, BorderSplitPane.SOUTHEAST.toLowerCase(), Horizontal.getInstance());
        _northWestPane = makeDetailsPane(sb, BorderSplitPane.NORTHWEST.toLowerCase(), Horizontal.getInstance());
        _northPane     = makeDetailsPane(sb, BorderSplitPane.NORTH.toLowerCase(), Horizontal.getInstance());
        _northEastPane = makeDetailsPane(sb, BorderSplitPane.NORTHEAST.toLowerCase(), Horizontal.getInstance());

        if (_southPane != null) detailsPanesByCompassPoint.put(BorderSplitPane.SOUTH, _southPane);
        if (_southEastPane != null) detailsPanesByCompassPoint.put(BorderSplitPane.SOUTHEAST, _southEastPane);
        if (_eastPane != null) detailsPanesByCompassPoint.put(BorderSplitPane.EAST, _eastPane);
        if (_northWestPane != null) detailsPanesByCompassPoint.put(BorderSplitPane.NORTHWEST, _northWestPane);
        if (_northPane != null) detailsPanesByCompassPoint.put(BorderSplitPane.NORTH, _northPane);
        if (_northEastPane != null) detailsPanesByCompassPoint.put(BorderSplitPane.NORTHEAST, _northEastPane);

        getTabProps().addNavigationListener(this);

        setAppName(appName);
        if (TheInstance == null) TheInstance = this;
        //setName(title);
        //loadImages();
        getContentPane().setFont(defaultFont);
        getContentPane().setLayout(new BorderLayout());
        //initMenus();
        //initToolBar();
        getContentPane().add(_menuBar, BorderLayout.NORTH);
        //JPanel p = new JPanel();
        //p.setLayout(new BorderLayout());
        //getContentPane().add(p, BorderLayout.CENTER);
        //p.add(_toolBar, BorderLayout.NORTH);
        getContentPane().add(createPanels(), BorderLayout.CENTER);
        getContentPane().add(_statusBar, BorderLayout.SOUTH);

        // allows me to ask "Do you want to save first?"
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowCloser());
        ImageIcon argoImage = ResourceLoader.lookupIconResource("Model");
        this.setIconImage(argoImage.getImage());
        ProjectManager.getManager().addPropertyChangeListener(this);
    }
    
    //   void loadImages() {
    //     String s = "A blue bullet icon - to draw attention to a menu item";
    //     blueDot = loadImageIcon("images/dot.gif", s);
    //     s = "A red bullet icon - to draw attention to a menu item";
    //     redDot = loadImageIcon("images/redDot.gif", s);
    //   }

    public Locale getLocale() {
        return Locale.getDefault();
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
        return Argo.localize("CoreMenu",key);
    }

    static final protected void setAccelerator(JMenuItem item,KeyStroke keystroke) {
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

    protected Component createPanels() {
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
        _workarea.add(ToDoPane.getToDoPane(), BorderSplitPane.SOUTHWEST);
        _workarea.add(NavigatorPane.getNavigatorPane(), BorderSplitPane.WEST);
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
   

    public void updateTitle(Project p) {
        if (p == null) setTitle(null);
        setTitle(p.getName());
    }

    public void setTitle(String title) {
        if (title == null || "".equals(title)) setTitle(getAppName());
        else super.setTitle(getAppName() + " - " + title);
    }

    public String getAppName() { return _appName; }
    public void setAppName(String n) { _appName = n; }

    public void select(Object o) {
        _editorPane.select(o);
        setDetailsTarget(o);
    }

    public void setTarget(Object o) {
        _editorPane.setTarget(o);
        
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        while(it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane)it.next();
            detailsPane.setTarget(o);
        }
         
        if (o instanceof MNamespace) ProjectManager.getManager().getCurrentProject().setCurrentNamespace((MNamespace)o);
        if (o instanceof UMLDiagram) {
            MNamespace m = ((UMLDiagram)o).getNamespace();
            if (m != null) ProjectManager.getManager().getCurrentProject().setCurrentNamespace(m);
        }
        if (o instanceof ArgoDiagram) {
            setActiveDiagram ((ArgoDiagram) o);
        }
        if (o instanceof MModelElement) {
            MModelElement eo = (MModelElement)o;
            if (eo == null) { cat.debug("no path to model"); return; }
            if (eo.getNamespace() != null) {
                ProjectManager.getManager().getCurrentProject().setCurrentNamespace(eo.getNamespace());
            } else
                ProjectManager.getManager().getCurrentProject().setCurrentNamespace((MNamespace)ProjectManager.getManager().getCurrentProject().getUserDefinedModels().get(0));
	}
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
        Actions.updateAllEnabled();
    }

    public Object getDetailsTarget() {
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        if (it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane)it.next();
            return detailsPane.getTarget();
        }
        return null; // TODO Bob Tarling - Should probably throw exception here
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
        throw new IllegalStateException("No detailspane in ArgoUML");
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

    /**
     * Find the tabpage with the given label
     * @param The tabpage label
     * @return the tabpage
     */
    public TabProps getTabProps() {
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        while(it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane)it.next();
            TabProps tabProps = detailsPane.getTabProps();
            if (tabProps != null) {
                return tabProps;
            }
        }
        return null;
        //throw new IllegalStateException("No such tab named " + tabName);
    }
    
    public void jumpToDiagramShowing(VectorSet dms) {
        if (dms.size() == 0) return;
        Object first = dms.elementAt(0);
        if (first instanceof Diagram && dms.size() > 1) {
            setTarget(first);
            select(dms.elementAt(1));
            return;
        }
        if (first instanceof Diagram && dms.size() == 1) {
            setTarget(first);
            select(null);
            return;
        }
        Vector diagrams = ProjectManager.getManager().getCurrentProject().getDiagrams();
        Object target = _editorPane.getTarget();
        if ((target instanceof Diagram) && ((Diagram)target).countContained(dms) == dms.size()) {
            select(first);
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
            select(first);
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
        Configuration.setInteger(Argo.KEY_SCREEN_WEST_WIDTH, NavigatorPane.getNavigatorPane().getWidth());
        Configuration.setInteger(Argo.KEY_SCREEN_SOUTHWEST_WIDTH, ToDoPane.getToDoPane().getWidth());
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
    private DetailsPane makeDetailsPane(StatusBar sb, String compassPoint, Orientation orientation) {
        DetailsPane detailsPane = new DetailsPane(sb, compassPoint, orientation);
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
            updateTitle(p);
            Actions.updateAllEnabled();
            //Designer.TheDesigner.getToDoList().removeAllElements();
            Designer.TheDesigner.setCritiquingRoot(p);
            // update all panes
            setTarget(p.getInitialTarget());
            return;
        }
    }

} /* end class ProjectBrowser */



