// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.argouml.application.api.Argo;
import org.argouml.application.api.CommandLineInterface;
import org.argouml.application.security.ArgoAwtExceptionHandler;
import org.argouml.cognitive.AbstractCognitiveTranslator;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ui.ToDoPane;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.language.java.generator.GeneratorJava;
import org.argouml.model.Model;
import org.argouml.moduleloader.InitModuleLoader;
import org.argouml.moduleloader.ModuleLoader2;
import org.argouml.notation.InitNotation;
import org.argouml.notation.providers.java.InitNotationJava;
import org.argouml.notation.providers.uml.InitNotationUml;
import org.argouml.notation.ui.InitNotationUI;
import org.argouml.persistence.PersistenceManager;
import org.argouml.ui.ArgoFrame;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.SplashScreen;
import org.argouml.ui.cmd.ActionExit;
import org.argouml.ui.cmd.InitUiCmdSubsystem;
import org.argouml.ui.cmd.PrintManager;
import org.argouml.uml.diagram.ui.InitDiagramAppearanceUI;
import org.argouml.uml.reveng.java.JavaImport;
import org.argouml.util.logging.SimpleTimer;
import org.tigris.gef.util.Util;

/**
 * This is the main class for two of the types 
 * of ArgoUML application invocation: 
 * non-GUI command line and Swing GUI.<p>
 * 
 * NOTE: Functionality which should be common to all types of application
 * invocation (e.g. extension modules to be loaded) should added to some
 * common class and <b>not</b> here.  Adding things here will cause behavior
 * to diverge for other application invocation types (e.g. ArgoEclipse).
 *
 */
public class Main {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(Main.class);

    /**
     * The location of the default logging configuration (.lcf) file.
     */
    public static final String DEFAULT_LOGGING_CONFIGURATION =
        "org/argouml/resource/default.lcf";

    /**
     * The default implementation to start.
     */
    private static final String DEFAULT_MODEL_IMPLEMENTATION =
        "org.argouml.model.mdr.MDRModelImplementation";

    private static List<Runnable> postLoadActions = new ArrayList<Runnable>();

    /**
     * The main entry point of ArgoUML.
     * @param args command line parameters
     */
    public static void main(String[] args) {
        try {
            LOG.info("ArgoUML Started.");

            SimpleTimer st = new SimpleTimer();
            st.mark("begin");

            checkJVMVersion();
            checkHostsFile();

            // Force the configuration to load
            Configuration.load();

            // Synchronize the startup directory
            String directory = Argo.getDirectory();
            org.tigris.gef.base.Globals.setLastDirectory(directory);

            // Initialise the version of this application
            initVersion();

            // Set the i18n locale
            Translator.init(Configuration.getString(Argo.KEY_LOCALE));

            // create an anonymous class as a kind of adaptor for the cognitive
            // System to provide proper translation/i18n.
            org.argouml.cognitive.Translator.setTranslator(
                    new AbstractCognitiveTranslator() {
                        public String i18nlocalize(String key) {
                            return Translator.localize(key);
                        }

                        public String i18nmessageFormat(String key,
                                Object[] iArgs) {
                            return Translator.messageFormat(key, iArgs);
                        }
                    });

            // then, print out some version info for debuggers...
            org.argouml.util.Tools.logVersionInfo();

            st.mark("arguments");

            /* set properties for application behaviour */
            System.setProperty("gef.imageLocation", "/org/argouml/Images");

            System.setProperty("apple.laf.useScreenMenuBar", "true");

            /* FIX: set the application name for Mac OS X */
            System.setProperty(
                    "com.apple.mrj.application.apple.menu.about.name",
                    "ArgoUML");

            boolean doSplash = Configuration.getBoolean(Argo.KEY_SPLASH, true);
            boolean reloadRecent = Configuration.getBoolean(
                    Argo.KEY_RELOAD_RECENT_PROJECT, false);
            boolean batch = false;
            List<String> commands = new ArrayList<String>();

            String projectName = null;

            // TODO: Move command line parsing to separate method
        
            // Parse command line args:
            // The assumption is that all options precede
            // the name of a project file to load.
            String theTheme = null;
            for (int i = 0; i < args.length; i++) {
                if (args[i].startsWith("-")) {
                    String themeName = LookAndFeelMgr.getInstance()
                            .getThemeClassNameFromArg(args[i]);
                    if (themeName != null) {
                        theTheme = themeName;
                    } else if (
                            args[i].equalsIgnoreCase("-help")
                            || args[i].equalsIgnoreCase("-h")
                            || args[i].equalsIgnoreCase("--help")
                            || args[i].equalsIgnoreCase("/?")) {
                        printUsage();
                        System.exit(0);
                    } else if (args[i].equalsIgnoreCase("-nosplash")) {
                        doSplash = false;
                    } else if (args[i].equalsIgnoreCase("-norecentfile")) {
                        reloadRecent = false;
                    } else if (args[i].equalsIgnoreCase("-command")
                            && i + 1 < args.length) {
                        commands.add(args[i + 1]);
                        i++;
                    } else if (args[i].equalsIgnoreCase("-locale")
                            && i + 1 < args.length) {
                        Translator.setLocale(args[i + 1]);
                        i++;
                    } else if (args[i].equalsIgnoreCase("-batch")) {
                        batch = true;
                    } else if (args[i].equalsIgnoreCase("-open") 
                            && i + 1 < args.length) {
                        projectName = args[++i];
                    } else if (args[i].equalsIgnoreCase("-print") 
                            && i + 1 < args.length) {
                        // let's load the project
                        String projectToBePrinted = 
                            PersistenceManager.getInstance().fixExtension(
                                    args[++i]);
                        URL urlToBePrinted = projectUrl(projectToBePrinted,
                                null);
                        ProjectBrowser.getInstance().loadProject(
                                new File(urlToBePrinted.getFile()), true, null);
                        // now, let's print it
                        PrintManager.getInstance().print();
                        // nothing else to do (?)
                        System.exit(0);
                    } else {
                        System.err
                                .println("Ignoring unknown/incomplete option '"
                                        + args[i] + "'");
                    }
                } else {
                    if (projectName == null) {
                        System.out.println(
                                "Setting projectName to '" + args[i] + "'");
                        projectName = args[i];
                    }
                }
            }

            // We have to do this to set the LAF for the splash screen
            st.mark("initialize laf");
            LookAndFeelMgr.getInstance().initializeLookAndFeel();
            if (theTheme != null) {
                LookAndFeelMgr.getInstance().setCurrentTheme(theTheme);
            }

            // Get the splash screen up as early as possible
            st.mark("create splash");
            SplashScreen splash = null;
            if (doSplash && !batch) {
                splash = initializeSplash();
            }

            // Initialize the Model subsystem
            st.mark("initialize model subsystem");

            initModel();

            updateProgress(splash, 5, "statusmsg.bar.model-subsystem");

            /*
             * Initialize the module loader. At least the plug-ins that provide
             * profiles need to be initialized before the project is loaded,
             * because some of these profile may have been set as default
             * profiles and need to be applied to the project as soon as it has
             * been created or loaded. the first instance of a Project is needed
             * during the gui initialization
             */
            st.mark("modules");

            SubsystemUtility.initSubsystem(new InitModuleLoader());

            // Initialize the Java code generator. (why so early? - tfm)
            GeneratorJava.getInstance();

            // The reason the gui is initialized before the commands are run
            // is that some of the commands will use the projectbrowser.
            st.mark("initialize gui");
            ProjectBrowser pb = initializeGUI(splash);

            SubsystemUtility.initSubsystem(new InitUiCmdSubsystem());
            SubsystemUtility.initSubsystem(new InitNotationUI());
            SubsystemUtility.initSubsystem(new InitNotation());
            SubsystemUtility.initSubsystem(new InitNotationUml());
            SubsystemUtility.initSubsystem(new InitNotationJava());
            SubsystemUtility.initSubsystem(new InitDiagramAppearanceUI());

            if (reloadRecent && projectName == null) {
                // If no project was entered on the command line,
                // try to reload the most recent project if that option is true
                String s = Configuration.getString(
                        Argo.KEY_MOST_RECENT_PROJECT_FILE, "");
                if (!("".equals(s))) {
                    File file = new File(s);
                    if (file.exists()) {
                        LOG.info("Re-opening project " + s);
                        projectName = s;
                    } else {
                        LOG.warn("Cannot re-open " + s
                                + " because it does not exist");
                    }
                }
            }

            URL urlToOpen = null;

            if (projectName != null) {
                projectName =
                    PersistenceManager.getInstance().fixExtension(projectName);
                urlToOpen = projectUrl(projectName, urlToOpen);
            }

            st.mark("perform commands");
            if (batch) {
                performCommands(commands);
                commands = null;

                System.out.println("Exiting because we are running in batch.");
                new ActionExit().doCommand(null);
                return;
            }

            if (splash != null) {
                if (urlToOpen == null) {
                    splash.getStatusBar().showStatus(
                            Translator.localize(
                                    "statusmsg.bar.defaultproject"));
                } else {
                    Object[] msgArgs = {projectName};
                    splash.getStatusBar().showStatus(
                            Translator.messageFormat(
                                    "statusmsg.bar.readingproject",
                                    msgArgs));
                }

                splash.getStatusBar().showProgress(40);
            }

            st.mark("make empty project");

            Designer.disableCritiquing();
            Designer.clearCritiquing();

            boolean projectLoaded = false;
            if (urlToOpen != null) {
                String filename = urlToOpen.getFile();
                File file = new File(filename);
                System.err.println("The url of the file to open is " 
                        + urlToOpen);
                System.err.println("The filename is " + filename);
                System.err.println("The file is " + file);
                System.err.println("File.exists = " + file.exists());
                projectLoaded = pb.loadProject(file, true, null);
            }

            if (!projectLoaded) {
                // Although this looks redundant, it's needed to get all
                // the initialization state set correctly.  
                // Too many side effects as part of initialization!
                ProjectManager.getManager().setCurrentProject(
                        ProjectManager.getManager().getCurrentProject());
                ProjectManager.getManager().setSaveEnabled(false);
            }

            st.mark("set project");

            Designer.enableCritiquing();

            st.mark("perspectives");

            if (splash != null) {
                splash.getStatusBar().showProgress(75);
            }

            st.mark("open window");

            updateProgress(splash, 95, "statusmsg.bar.open-project-browser");

            ArgoFrame.getInstance().setVisible(true);

            st.mark("close splash");
            if (splash != null) {
                splash.setVisible(false);
                splash.dispose();
                splash = null;
            }

            performCommands(commands);
            commands = null;

            st.mark("start critics");
            Runnable startCritics = new StartCritics();
            Main.addPostLoadAction(startCritics);

            st.mark("start loading modules");
            Runnable moduleLoader = new LoadModules();
            Main.addPostLoadAction(moduleLoader);

            PostLoad pl = new PostLoad(postLoadActions);
            Thread postLoadThead = new Thread(pl);
            postLoadThead.start();

            LOG.info("");
            LOG.info("profile of load time ############");
            for (Enumeration i = st.result(); i.hasMoreElements();) {
                LOG.info(i.nextElement());
            }
            LOG.info("#################################");
            LOG.info("");

            st = null;
            ArgoFrame.getInstance().setCursor(
                    Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            //ToolTipManager.sharedInstance().setInitialDelay(500);
            ToolTipManager.sharedInstance().setDismissDelay(50000000);
        } catch (Throwable t) {
            try {
                LOG.fatal("Fatal error on startup.  ArgoUML failed to start", 
                        t);
            } finally {
                System.out.println("Fatal error on startup.  "
                        + "ArgoUML failed to start.");
                t.printStackTrace();
                System.exit(1);
            }
        }
    }
    
    /**
     * Initialise the UMl model repository.
     */
    private static void initModel() {
        String className = System.getProperty(
                "argouml.model.implementation",
                DEFAULT_MODEL_IMPLEMENTATION);
        Throwable ret = Model.initialise(className);
        if (ret != null) {
            LOG.fatal("Model component not correctly initialized.", ret);
            System.err.println(className
                    + " is not a working Model implementation.");
            System.exit(1);
        }
    }

    /**
     * Helper to update progress if we have a splash screen displayed.
     *
     * @param splash <code>true</code> if the splash is to be shown
     * @param percent the new percentage for progress bar
     * @param message the messae to be shown in the splash
     */
    private static void updateProgress(SplashScreen splash, int percent,
            String message) {
        if (splash != null) {
            splash.getStatusBar().showStatus(Translator.localize(message));
            splash.getStatusBar().showProgress(percent);
        }
    }

    /**
     * Calculates the {@link URL} for the given project name.
     * If the file does not exist or cannot be converted the default
     * {@link URL} is returned.
     *
     * @param projectName is the file name of the project
     * @param urlToOpen is the default {@link URL}
     * @return the new URL.
     */
    private static URL projectUrl(final String projectName, URL urlToOpen) {
        File projectFile = new File(projectName);
        if (!projectFile.exists()) {
            System.err.println("Project file '" + projectFile
			       + "' does not exist.");
            /* this will cause an empty project to be created */
        } else {
            try {
                urlToOpen = Util.fileToURL(projectFile);
            } catch (Exception e) {
                LOG.error("Exception opening project in main()", e);
            }
        }

        return urlToOpen;
    }

    /**
     * Prints the usage message.
     */
    private static void printUsage() {
        System.err.println("Usage: [options] [project-file]");
        System.err.println("Options include: ");
        System.err.println("  -help           display this information");
        LookAndFeelMgr.getInstance().printThemeArgs();
        System.err.println("  -nosplash       don't display logo at startup");
        System.err.println("  -norecentfile   don't reload last saved file");
        System.err.println("  -command <arg>  command to perform on startup");
        System.err.println("  -batch          don't start GUI");
        System.err.println("  -locale <arg>   set the locale (e.g. 'en_GB')");
        System.err.println("");
        System.err.println("You can also set java settings which influence "
			   + "the behaviour of ArgoUML:");
        System.err.println("  -Xms250M -Xmx500M  [makes ArgoUML reserve "
			   + "more memory for large projects]");
        System.err.println("\n\n");
    }

    /**
     * Check tha JVM Version.
     * <p>
     * If it is a unsupported JVM version we exit immediately.
     * <p>
     * NOTE: In most cases the JVM classloader will complain about an
     * UnsupportedClassVersionError long before we get anywhere near this point
     * in the initialization.
     */
    private static void checkJVMVersion() {
        // check if we are using a supported java version
        String javaVersion = System.getProperty("java.version", "");
        // exit if unsupported java version.
        if (javaVersion.startsWith("1.4") 
                || javaVersion.startsWith("1.3")
                || javaVersion.startsWith("1.2")
                || javaVersion.startsWith("1.1")) {

	    System.err.println("You are using Java " + javaVersion + ", "
			       + "Please use Java 5 (aka 1.5) or later" 
                               + " with ArgoUML");
	    System.exit(0);
        }
    }

    /**
     * Check that we can get the InetAddress for localhost.
     * This can fail on Unix if /etc/hosts is not correctly set up.
     */
    private static void checkHostsFile() {
        try {
            InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            System.err.println("ERROR: unable to get localhost information.");
            e.printStackTrace(System.err);
            System.err.println("On Unix systems this usually indicates that"
                + "your /etc/hosts file is incorrectly setup.");
            System.err.println("Stopping execution of ArgoUML.");
            System.exit(0);
        }
    }


    /**
     * Add an element to the PostLoadActions list,
     * which contains actions that are run after ArgoUML has started.
     *
     * @param r a "Runnable" action
     */
    public static void addPostLoadAction(Runnable r) {
        postLoadActions.add(r);
    }

    /**
     * Perform a list of commands that were given on the command line.
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
	    } else {
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
	    } catch (InstantiationException e) {
		System.out.println(commandname
				   + " could not be instantiated - skipping"
				   + " (InstantiationException)");
		continue;
	    } catch (IllegalAccessException e) {
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
				  ? "" : commandargument) + " )");
	    boolean result = clio.doCommand(commandargument);
	    if (!result) {
		System.out.println("There was an error executing "
				   + "the command "
				   + commandname + "( "
				   + (commandargument == null
				      ? "" : commandargument) + " )");
		System.out.println("Aborting the rest of the commands.");
		return;
	    }
	}
    }

    /**
     * Install our security handlers,
     * and do basic initialization of log4j.
     *
     * Log4j initialization must be done as
     * part of the main class initializer, so that
     * the log4j initialization is complete
     * before any other static initializers.
     *
     * Also installs a trap to "eat" certain SecurityExceptions.
     * Refer to {@link java.awt.EventDispatchThread} for details.
     */
    static {

        /*  
         * Install the trap to "eat" SecurityExceptions.
         * 
         * NOTE: This is temporary and will go away in a "future" release
         * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4714232
         */
        System.setProperty(
            "sun.awt.exception.handler",
            ArgoAwtExceptionHandler.class.getName());

        /*
         *  The string <code>log4j.configuration</code> is the
         *  same string found in
         *  {@link org.apache.log4j.Configuration.DEFAULT_CONFIGURATION_FILE}
         *  but if we use the reference, then log4j configures itself
         *  and clears the system property and we never know if it was
         *  set.
         *
         *  If it is set, then we let the static initializer in
         * {@link Argo} perform the initialization.
         */

        // TODO: is it ok to do this in the static initializer if we
        // are running in a Java Web Start environment? - tfm
        // JWS property for logs is : 
        // deployment.user.logdir & deployment.user.tmp
        if (System.getProperty("log4j.configuration") == null) {
            Properties props = new Properties();
	    InputStream stream = null;
            try {
		stream =
                        Thread.currentThread().getContextClassLoader()
                                .getResourceAsStream(
                                        DEFAULT_LOGGING_CONFIGURATION);

		if (stream != null) {
		    props.load(stream);
		}
            } catch (IOException io) {
                io.printStackTrace();
                System.exit(-1);
            }

	    PropertyConfigurator.configure(props);

	    if (stream == null) {
		BasicConfigurator.configure();
		Logger.getRootLogger().getLoggerRepository().setThreshold(
                        Level.ERROR); // default level is DEBUG
                Logger.getRootLogger().error(
                        "Failed to find valid log4j properties"
                                + "in log4j.configuration"
                                + "using default logging configuration");
	    }
        }

        // initLogging();
    }

    /**
     * Create and display a splash screen.
     * @return the splash screen
     */
    private static SplashScreen initializeSplash() {
        SplashScreen splash = new SplashScreen();
        splash.setVisible(true);
        // On uniprocessors wait until we're sure the splash screen
        // has been painted so that we aren't competing for resources
        if (!EventQueue.isDispatchThread()
                && Runtime.getRuntime().availableProcessors() == 1) {
            synchronized (splash) {
                while (!splash.isPaintCalled()) {
                    try {
                        splash.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
        return splash;
    }
    
    /**
     * Do a part of the initialization that is very much GUI-stuff.
     *
     * @param splash the splash screeen
     */
    private static ProjectBrowser initializeGUI(SplashScreen splash) {
        // make the projectbrowser
        JPanel todoPane = new ToDoPane(splash);
	ProjectBrowser pb = ProjectBrowser.makeInstance(splash, true, todoPane);

	JOptionPane.setRootFrame(pb);

        pb.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        // Set the screen layout to what the user left it before, or
        // to reasonable defaults.
        Rectangle scrSize = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();

        int configFrameWidth =
            Configuration.getInteger(Argo.KEY_SCREEN_WIDTH, scrSize.width);
        int w = Math.min(configFrameWidth, scrSize.width);
        if (w == 0) {
            w = 600;
        }
        
        int configFrameHeight =
            Configuration.getInteger(Argo.KEY_SCREEN_HEIGHT, scrSize.height);
        int h = Math.min(configFrameHeight, scrSize.height);
        if (h == 0) {
            h = 400;
        }
        
        int x = Configuration.getInteger(Argo.KEY_SCREEN_LEFT_X, 0);
        int y = Configuration.getInteger(Argo.KEY_SCREEN_TOP_Y, 0);
        pb.setLocation(x, y);
        pb.setSize(w, h);
        
        UIManager.put("Button.focusInputMap", new UIDefaults.LazyInputMap(
                new Object[] {
                    "ENTER", "pressed",
                    "released ENTER", "released",
                    "SPACE", "pressed",
                    "released SPACE", "released"
                })
        );         
        return pb;
    }

    /**
     * Publish the version of the ArgoUML application. <p>
     * 
     * This function is intentionally public, 
     * since applications built on ArgoUML, 
     * that do not make use of Main.main(), 
     * can call this function and then access ArgoUML's version 
     * from the ApplicationVersion class. 
     */
    public static void initVersion() {
        ArgoVersion.init();
    }


} /* end Class Main */

/**
 * Class to hold a list of actions to be perform and to perform them
 * after the initializations is done.
 */
class PostLoad implements Runnable {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(PostLoad.class);

    /**
     * The list of actions to perform.
     */
    private List<Runnable> postLoadActions;


    /**
     * Constructor.
     *
     * @param v The actions to perform.
     */
    public PostLoad(List<Runnable> actions) {
        postLoadActions = actions;
    }
    
    /*
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (Exception ex) {
            LOG.error("post load no sleep", ex);
        }
        for (Runnable r : postLoadActions) {
            r.run();
            try {
                Thread.sleep(100);
            } catch (Exception ex) {
                LOG.error("post load no sleep2", ex);
            }
        }
    }
} /* end class PostLoad */

/**
 * Class to load modules.
 */
class LoadModules implements Runnable {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(LoadModules.class);

    
    private static final String[] OPTIONAL_INTERNAL_MODULES = {
        "org.argouml.dev.DeveloperModule",
    };
    
    /**
     * Load internal modules which should be found on the standard
     * classpath.
     */
    private void huntForInternalModules() {
        for (String module : OPTIONAL_INTERNAL_MODULES) {
            try {
                ModuleLoader2.addClass(module);
            } catch (ClassNotFoundException e) {
                /* We don't care if optional modules aren't found. */
                LOG.debug("Module " + module + " not found");
            }            
        }
        ModuleLoader2.doLoad(false);
    }

    /*
     * @see java.lang.Runnable#run()
     */
    public void run() {
	new JavaImport().init();
	huntForInternalModules();
        LOG.info("Module loading done");
    }

} /* end class LoadModules */
