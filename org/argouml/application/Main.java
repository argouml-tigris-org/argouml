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

package org.argouml.application;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.ToolTipManager;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.argouml.application.api.Argo;
import org.argouml.application.api.CommandLineInterface;
import org.argouml.application.api.Configuration;
import org.argouml.application.security.ArgoAwtExceptionHandler;
import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.cognitive.Designer;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.Actions;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.NavigatorPane;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.SplashScreen;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.ActionOpenProject;
import org.argouml.uml.ui.ActionExit;
import org.argouml.util.FileConstants;
import org.argouml.util.Trash;
import org.argouml.util.logging.SimpleTimer;
import org.tigris.gef.util.Util;

public class Main {

    // instantiation is done in main
    private static Logger cat = null;

    ////////////////////////////////////////////////////////////////
    // constants

    public static final String DEFAULT_LOGGING_CONFIGURATION =
        "org/argouml/resource/default.lcf";

    private static final String STATBUNDLE =
	"org/argouml/i18n/statusmsg.properties";	

    // Resourcebundle-keys
    private static final String LBLBUNDLE =
	"org/argouml/i18n/label.properties";
		
    private static final String LBLBUNDLE_PROJECTBROWSER_TITLE = 
	"label.projectbrowser-title";
	
    private static final String STATBUNDLE_BAR_DEFAULTPROJECT = 
	"statusmsg.bar.defaultproject";
    private static final String STATBUNDLE_BAR_READINGPROJECT = 
	"statusmsg.bar.readingproject";
    private static final String STATBUNDLE_BAR_OPEN_PROJECT_BROWSER = 
	"statusmsg.bar.open-project-browser";
    private static final String STATBUNDLE_BAR_LOADMODULES = 
	"statusmsg.bar.loadmodules";
		
    ////////////////////////////////////////////////////////////////
    // static variables

    private static Vector postLoadActions = new Vector();

    ////////////////////////////////////////////////////////////////
    // main

    public static void main(String args[]) {        

        // Force the configuration to load
        Configuration.load();

        // Synchronize the startup directory
        //
        // TODO:  This is a temporary hack.  The real change must
        //                   be to never refer to Globals.getLastDirectory
        //                   or Globals.setLastDirectory within Argo, but
        //                   use Argo.getDirectory and Argo.setDirectory.
        String directory = Argo.getDirectory();
        org.tigris.gef.base.Globals.setLastDirectory(directory);

        // load i18n bundles
        Translator.init();

        // then, print out some version info for debuggers...

        org.argouml.util.Tools.logVersionInfo();

        SimpleTimer st = new SimpleTimer("Main.main");
        st.mark("arguments");

        /* set properties for application behaviour */
        System.setProperty("gef.imageLocation", "/org/argouml/Images");

        /* TODO: disable apple menu bar to enable proper running
	 * of Mac OS X java web start 
	 */
        System.setProperty("com.apple.macos.useScreenMenuBar", "false");

        /* FIX: set the application name for Mac OS X */
        System.setProperty("com.apple.mrj.application.apple.menu.about.name",
			   "ArgoUML");


        boolean doSplash = Configuration.getBoolean(Argo.KEY_SPLASH, true);
        boolean useEDEM = Configuration.getBoolean(Argo.KEY_EDEM, true);
        boolean preload = Configuration.getBoolean(Argo.KEY_PRELOAD, true);
        boolean profileLoad = Configuration.getBoolean(Argo.KEY_PROFILE, false);
        boolean reloadRecent =
            Configuration.getBoolean(Argo.KEY_RELOAD_RECENT_PROJECT, false);
	boolean batch = false;
	List commands = new ArrayList();

        String projectName = null;

        //--------------------------------------------
        // Parse command line args:
        // The assumption is that all options precede
        // the name of a project file to load.
        //--------------------------------------------

        String themeMemory = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                if ((themeMemory =
                    LookAndFeelMgr.SINGLETON.getThemeFromArg(args[i]))
                    != null) {
                    // Remembered!
                } else if (
                    args[i].equalsIgnoreCase("-help")
                        || args[i].equalsIgnoreCase("-h")
                        || args[i].equalsIgnoreCase("--help")
                        || args[i].equalsIgnoreCase("/?")) {
                    System.err.println("Usage: [options] [project-file]");
                    System.err.println("Options include: ");
                    LookAndFeelMgr.SINGLETON.printThemeArgs();
                    System.err.println(
                        "  -nosplash       don't display Argo/UML logo");
                    System.err.println(
                        "  -noedem         don't report usage statistics");
                    System.err.println(
                        "  -nopreload      don't preload common classes");
                    System.err.println(
                        "  -profileload    report on load times");
                    System.err.println(
                        "  -norecentfile   don't reload last saved file");
                    System.err.println(
                        "  -command <arg>  command to perform on startup");
                    System.err.println(
                        "  -batch          don't start GUI");
                    System.err.println("");
                    System.err.println(
                        "You can also set java settings which influence "
			+ "the behaviour of ArgoUML:");
                    System.err.println("  -Duser.language    [e.g. en]");
                    System.err.println("  -Duser.region      [e.g. US]");
                    System.err.println(
                        "  -Dforce.nativelaf  [force ArgoUML to use "
			+ "the native look and feel. UNSUPPORTED]");
                    System.err.println("\n\n");
                    ArgoSecurityManager.getInstance().setAllowExit(true);
                    System.exit(0);
                } else if (args[i].equalsIgnoreCase("-nosplash")) {
                    doSplash = false;
                } else if (args[i].equalsIgnoreCase("-noedem")) {
                    useEDEM = false;
                } else if (args[i].equalsIgnoreCase("-nopreload")) {
                    preload = false;
                } else if (args[i].equalsIgnoreCase("-profileload")) {
                    profileLoad = true;
                } else if (args[i].equalsIgnoreCase("-norecentfile")) {
                    reloadRecent = false;
                } else if (args[i].equalsIgnoreCase("-command")
			   && i + 1 < args.length) {
		    commands.add(args[i + 1]);
		    i++;
                } else if (args[i].equalsIgnoreCase("-batch")) {
                    batch = true;
                } else {
                    System.err.println(
                        "Ignoring unknown option '" + args[i] + "'");
                }
            } else {
                if (projectName == null) {
                    System.out.println(
                        "Setting projectName to '" + args[i] + "'");
                    projectName = args[i];
                }
            }
        }

	// Register the default notation.
	org.argouml.uml.generator.GeneratorDisplay.getInstance();
        
	// Initialize the UMLActions
	Actions.getInstance();
                
	// The reason the gui is initialized before the commands are run
	// is that some of the commands will use the projectbrowser.
	st.mark("initialize gui");
	initializeGUI(doSplash && !batch, themeMemory);

        if (reloadRecent && projectName == null) {
            // If no project was entered on the command line,
            // try to reload the most recent project if that option is true
            String s =
                Configuration.getString(Argo.KEY_MOST_RECENT_PROJECT_FILE, "");
            if (!("".equals(s))) {
                File file = new File(s);
                if (file.exists()) {
                    Argo.log.info("Re-opening project " + s);
                    projectName = s;
                } else {
                    Argo.log.warn(
                        "Cannot re-open " + s + " because it does not exist");
                }
            }
        }

        File projectFile = null;
        URL urlToOpen = null;

        if (projectName != null) {
            if (!projectName.endsWith(FileConstants.COMPRESSED_FILE_EXT))
                projectName += FileConstants.COMPRESSED_FILE_EXT;
            projectFile = new File(projectName);
            if (!projectFile.exists()) {
                System.err.println(
                    "Project file '" + projectFile + "' does not exist.");
                /* this will cause an empty project to be created */
            } else {
                try {
                    urlToOpen = Util.fileToURL(projectFile);
                } catch (Exception e) {
                    Argo.log.error("Exception opening project in main()", e);
                }
            }
        }

	ProjectBrowser pb = ProjectBrowser.getInstance();

	performCommands(commands);
	commands = null;

	if (batch) {
	    System.out.println("Exiting because we are running in batch.");
	    ActionExit.SINGLETON.actionPerformed(null);
	    return;
	}

        if (doSplash) {
            SplashScreen splash = SplashScreen.getInstance();
            if (urlToOpen == null)
            {
		splash.getStatusBar().showStatus(Argo.localize(STATBUNDLE, STATBUNDLE_BAR_DEFAULTPROJECT));	
            }
            else
            {
		Object[] msgArgs = {
		    projectName
		};
		splash.getStatusBar().showStatus(Translator.messageFormat(STATBUNDLE,
									  STATBUNDLE_BAR_READINGPROJECT,
									  msgArgs));
            }

            splash.getStatusBar().showProgress(40);
        }

        st.mark("make empty project");

        Project p = null;

        if (urlToOpen != null) {
            
            ActionOpenProject.SINGLETON.loadProject(urlToOpen);

        }
        p = ProjectManager.getManager().getCurrentProject();

        st.mark("set project");

        // Touch the trash
        Trash.SINGLETON.getSize();

        Designer.disableCritiquing();
        Designer.clearCritiquing();
        ProjectManager.getManager().setCurrentProject(p);
        Designer.enableCritiquing();

        st.mark("perspectives");

        if (urlToOpen == null)
	    pb.setTitle(Argo.localize(LBLBUNDLE, 
				      LBLBUNDLE_PROJECTBROWSER_TITLE));	

        if (doSplash) {
            SplashScreen splash = SplashScreen.getInstance();
            splash.getStatusBar().showProgress(75);
        }

        // Initialize the module loader.
        st.mark("modules");
        Argo.initializeModules();

        st.mark("open window");

        if (doSplash) {
            SplashScreen splash = SplashScreen.getInstance();
	    splash.getStatusBar().showStatus(Argo.localize(STATBUNDLE,
							   STATBUNDLE_BAR_OPEN_PROJECT_BROWSER));	
            splash.getStatusBar().showProgress(95);
        }

        pb.setVisible(true);
        
        // set the initial target
        Object diag = p.getDiagrams().elementAt(0); 
        TargetManager.getInstance().setTarget(diag);
            
        st.mark("close splash");
        if (doSplash) {
            SplashScreen splash = SplashScreen.getInstance();
            splash.setVisible(false);
            splash.dispose();
            splash = null;
        }

        st.mark("start critics");
        Runnable startCritics = new StartCritics();
        Main.addPostLoadAction(startCritics);

        st.mark("start preloader");
        if (preload) {
            Runnable preloader = new PreloadClasses();
            Main.addPostLoadAction(preloader);
        }

        PostLoad pl = new PostLoad(postLoadActions);
        Thread postLoadThead = new Thread(pl);
        pl.setThread(postLoadThead);
        postLoadThead.start();

        if (profileLoad) {
            Argo.log.info("");
            Argo.log.info("profile of load time ############");
            for (Enumeration i = st.result(); i.hasMoreElements();) {
                Argo.log.info(i.nextElement());
            }

            Argo.log.info("#################################");
            Argo.log.info("");
        }
        st = null;
        pb.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        //ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(50000000);
    }

    public static void addPostLoadAction(Runnable r) {
        postLoadActions.addElement(r);
    }

    /** Perform a list of commands that were given on the command line.
     *
     * This first implementation just has a list of commands that
     * is possible to give.
     *
     * @param list The commands, a list of strings.
     */
    public static void performCommands(List list) {
	Iterator iter = list.iterator();

	while (iter.hasNext()) {
	    String commandstring = (String) iter.next();

	    int pos = commandstring.indexOf('=');

	    String commandname;
	    String commandargument;

	    if (pos == -1) {
		commandname = commandstring;
		commandargument = null;
	    }
	    else {
		commandname = commandstring.substring(0, pos);
		commandargument = commandstring.substring(pos + 1);
	    }

	    // Perform one command.
	    Class c;
	    try {
		c = Class.forName(commandname);
	    } catch (ClassNotFoundException e) {
		System.out.println("Cannot find the command: " + commandname);
		continue;
	    }

	    // Now create a new object.
	    Object o = null;
	    try {
		o = c.newInstance();
	    }
	    catch (InstantiationException e) { 
		System.out.println(commandname 
				   + " could not be instantiated - skipping"
				   + " (InstantiationException)");
		continue;
	    }
	    catch (IllegalAccessException e) { 
		System.out.println(commandname 
				   + " could not be instantiated - skipping"
				   + " (IllegalAccessException)");
		continue;
	    }


	    if (o == null || !(o instanceof CommandLineInterface)) {
		System.out.println(commandname 
				   + " is not a command - skipping.");
		continue;
	    }
		
	    CommandLineInterface clio = (CommandLineInterface) o;

	    System.out.println("Performing command " 
			       + commandname + "( " 
			       + (commandargument == null 
				  ? "" : commandargument ) + " )");
	    boolean result = clio.doCommand(commandargument);
	    if (!result) {
		System.out.println("There was an error executing "
				   + "the command "
				   + commandname + "( " 
				   + (commandargument == null 
				      ? "" : commandargument ) + " )");
		System.out.println("Aborting the rest of the commands.");
		return;
	    }
	}
    }

    /** Install our security handlers,
     *  and do basic initialization of log4j.
     *
     *  Log4j initialization must be done as
     *  part of the main class initializer, so that
     *  the log4j initialization is complete
     *  before any other static initializers.
     *
     *  Also installs a trap to "eat" certain SecurityExceptions.
     *  Refer to {@link java.awt.EventDispatchThread} for details.
     */
    static {

        /*  Install the trap to "eat" SecurityExceptions.
         */
        System.setProperty(
            "sun.awt.exception.handler",
            ArgoAwtExceptionHandler.class.getName());

        /*  Install our own security manager.
         *  Once this is done, no one else
         *  can change "sun.awt.exception.handler".
         */
        System.setSecurityManager(ArgoSecurityManager.getInstance());

        /*  The string <code>log4j.configuration</code> is the
         *  same string found in
         *  {@link org.apache.log4j.Configuration.DEFAULT_CONFIGURATION_FILE}
         *  but if we use the reference, then log4j configures itself
         *  and clears the system property and we never know if it was
         *  set.
         *
         *  If it is set, then we let the static initializer in
         * {@link Argo} perform the initialization.
         */
         
        if (System.getProperty("log4j.configuration") == null) {
            Properties props = new Properties();
            try {            
                props.load(ClassLoader.getSystemResourceAsStream(DEFAULT_LOGGING_CONFIGURATION));
            }
            catch (IOException io) {
                io.printStackTrace();
                System.exit(-1);
            }
            PropertyConfigurator.configure(props);
            // BasicConfigurator.configure();
            /*
	      Logger.getRootLogger().getLoggerRepository().setThreshold(
	      Level.WARN);
	    */
        }
        
        // initLogging();
    }
    
    private static void initializeGUI(boolean doSplash, String themeMemory) 
    {
	// initialize the correct look and feel
	LookAndFeelMgr.SINGLETON.initializeLookAndFeel();
	if (themeMemory != null)
	{
	    LookAndFeelMgr.SINGLETON.setCurrentTheme(themeMemory);
	}

	// make the projectbrowser
	ProjectBrowser.setSplash(doSplash);
	ProjectBrowser pb = ProjectBrowser.getInstance();

	JOptionPane.setRootFrame(pb);

	// Set the screen layout to what the user left it before, or
	// to reasonable defaults.
	Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
	pb.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

	int w =
	    Math.min(
		     Configuration.getInteger(
					      Argo.KEY_SCREEN_WIDTH,
					      (int) (0.95 * scrSize.width)),
		     scrSize.width);
	int h =
	    Math.min(
		     Configuration.getInteger(
					      Argo.KEY_SCREEN_HEIGHT,
					      (int) (0.95 * scrSize.height)),
		     scrSize.height);
	int x = Configuration.getInteger(Argo.KEY_SCREEN_LEFT_X, 0);
	int y = Configuration.getInteger(Argo.KEY_SCREEN_TOP_Y, 0);
	pb.setLocation(x, y);
	pb.setSize(w, h);
    }

    

} /* end Class Main */

class PostLoad implements Runnable {
    Vector postLoadActions = null;
    Thread myThread = null;
    public PostLoad(Vector v) {
        postLoadActions = v;
    }
    public void setThread(Thread t) {
        myThread = t;
    }
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (Exception ex) {
            Argo.log.error("post load no sleep", ex);
        }
        int size = postLoadActions.size();
        for (int i = 0; i < size; i++) {
            Runnable r = (Runnable) postLoadActions.elementAt(i);
            r.run();
            try {
                Thread.sleep(100);
            } catch (Exception ex) {
                Argo.log.error("post load no sleep2", ex);
            }
        }
    }
} /* end class PostLoad */

class PreloadClasses implements Runnable {
    public void run() {
        Class c = null;
        Argo.log.info("preloading...");

	// Alphabetic order
        c = java.beans.BeanDescriptor.class;
        c = java.beans.BeanInfo.class;
        c = java.beans.EventSetDescriptor.class;
        c = java.beans.FeatureDescriptor.class;
        c = java.beans.IndexedPropertyDescriptor.class;
        c = java.beans.Introspector.class;
        c = java.beans.MethodDescriptor.class;
        c = java.beans.PropertyDescriptor.class;
        c = java.beans.PropertyVetoException.class;
        c = java.beans.SimpleBeanInfo.class;
        c = java.lang.ClassNotFoundException.class;
        c = java.lang.CloneNotSupportedException.class;
        c = java.lang.InterruptedException.class;
        c = java.lang.NullPointerException.class;
        c = java.lang.SecurityException.class;
        c = java.lang.Void.class;
        c = java.lang.reflect.Modifier.class;
        c = java.util.TooManyListenersException.class;
        c = org.argouml.kernel.DelayedChangeNotify.class;
        c = org.argouml.kernel.Wizard.class;
        c = org.argouml.ui.Clarifier.class;
        c = org.argouml.ui.StylePanelFig.class;
        c = org.argouml.uml.GenCompositeClasses.class;
        c = org.argouml.uml.cognitive.critics.ClAttributeCompartment.class;
        c = org.argouml.uml.cognitive.critics.ClClassName.class;
        c = org.argouml.uml.cognitive.critics.ClOperationCompartment.class;
        c = org.argouml.uml.diagram.static_structure.ui.FigClass.class;
        c = org.argouml.uml.diagram.static_structure.ui.FigInterface.class;
        c = org.argouml.uml.diagram.static_structure.ui.FigPackage.class;
        c = org.argouml.uml.diagram.static_structure.ui.SelectionClass.class;
        c = org.argouml.uml.diagram.static_structure.ui
	    .StylePanelFigClass.class;
        c = org.argouml.uml.diagram.static_structure.ui
	    .StylePanelFigInterface.class;
        c = org.argouml.uml.diagram.ui.FigAssociation.class;
        c = org.argouml.uml.diagram.ui.FigGeneralization.class;
        c = org.argouml.uml.diagram.ui.FigRealization.class;
        c = org.argouml.uml.diagram.ui.ModeCreateEdgeAndNode.class;
        c = org.argouml.uml.diagram.ui.SPFigEdgeModelElement.class;
        c = org.argouml.uml.diagram.ui.SelectionNodeClarifiers.class;
        c = org.argouml.uml.diagram.ui.SelectionWButtons.class;
        c = org.argouml.uml.ui.foundation.core.PropPanelAssociation.class;
        c = org.argouml.uml.ui.foundation.core.PropPanelClass.class;
        c = org.argouml.uml.ui.foundation.core.PropPanelInterface.class;
        c = org.tigris.gef.base.CmdSetMode.class;
        c = org.tigris.gef.base.Geometry.class;
        c = org.tigris.gef.base.ModeModify.class;
        c = org.tigris.gef.base.ModePlace.class;
        c = org.tigris.gef.base.PathConvPercentPlusConst.class;
        c = org.tigris.gef.base.SelectionResize.class;
        c = org.tigris.gef.event.ModeChangeEvent.class;
        c = org.tigris.gef.graph.GraphEdgeHooks.class;
        c = org.tigris.gef.graph.GraphEvent.class;
        c = org.tigris.gef.graph.presentation.NetEdge.class;
        c = org.tigris.gef.presentation.ArrowHeadNone.class;
        c = org.tigris.gef.presentation.FigPoly.class;
        c = org.tigris.gef.ui.ColorRenderer.class;
        c = org.tigris.gef.ui.Swatch.class;
        c = org.tigris.gef.util.EnumerationEmpty.class;
        c = org.tigris.gef.util.EnumerationSingle.class;

        Argo.log.info(" done preloading");
    }

} /* end class PreloadClasses */
