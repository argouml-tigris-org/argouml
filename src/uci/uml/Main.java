// Copyright (c) 1996-99 The Regents of the University of California. All
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




package uci.uml;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;
import java.beans.*;
import java.io.File;
import com.sun.java.swing.*;
import com.sun.java.swing.plaf.metal.MetalLookAndFeel;

import uci.util.*;
import uci.uml.ui.*;
import uci.uml.ui.nav.*;
import uci.uml.ui.todo.*;
import uci.uml.visual.*;
import uci.uml.critics.*;
import uci.uml.Model_Management.Model;
import uci.uml.test.omg.*;

import uci.argo.kernel.*;

import uci.xml.argo.ArgoParser;

public class Main {
  ////////////////////////////////////////////////////////////////
  // constants

  public static int WIDTH = 1024;
  public static int HEIGHT = 768;

  ////////////////////////////////////////////////////////////////
  // main

  public static void main(String args[]) {
    boolean doSplash = true;
    File projectFile = null;
    Project p = null;
    String projectName = null;
    URL urlToOpen = null;

    Vector argv = new Vector();
    for (int i = 0; i < args.length; ++i) argv.addElement(args[i]);

    /* This is the default */
    MetalLookAndFeel.setCurrentTheme(new uci.uml.ui.JasonsTheme());

    //--------------------------------------------
    // Parse command line args:
    // The assumption is that all options precede
    // the name of a project file to load.
    //--------------------------------------------

    for (int i=0; i < args.length; i++) {
      if (args[i].startsWith("-")) {
	if (args[i].equals("-big")) {
	  MetalLookAndFeel.setCurrentTheme(new uci.uml.ui.JasonsBigTheme());
	} else if (args[i].equals("-huge")) {
	  MetalLookAndFeel.setCurrentTheme(new uci.uml.ui.JasonsHugeTheme());
	} else if (args[i].equals("-help") || args[i].equals("-h")) {
	  System.err.println("Usage: [options] [project-file]");
	  System.err.println("Options include: ");
	  System.err.println("  -big       use big fonts");
	  System.err.println("  -huge      use huge fonts");
	  System.err.println("  -nosplash  dont display Argo/UML logo");
	  System.exit(0);
	} else if (args[i].equals("-nosplash")) {
	  doSplash = false;
	} else {
	  System.err.println("Ingoring unknown option '" + args[i] + "'");
	}
      } else {
	projectName = args[i];
	if (!projectName.endsWith(Project.FILE_EXT))
	  projectName += Project.FILE_EXT;
	projectFile = new File(projectName);
	if (!projectFile.exists()) {
	  System.err.println("Project file '" + projectFile +
			     "' does not exist.");
	  /* this will cause an empty project to be created */
	  p = null;
	} else {
	  try { urlToOpen = Util.fileToURL(projectFile); }
	  catch (Exception e) {
	    System.out.println("Exception opening project in main()");
	    e.printStackTrace();
	  }
	}
	/* by our assumption, any following args will be ignored */
	break;
      }
    }

    SplashScreen splash = new SplashScreen("Loading Argo/UML...", "Splash");
    splash.getStatusBar().showStatus("Making Project Browser");
    splash.getStatusBar().showProgress(10);

    splash.setVisible(doSplash);

    ProjectBrowser pb = new ProjectBrowser("Argo/UML", splash.getStatusBar());
    JOptionPane.setRootFrame(pb);
    //pb.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
    Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
    pb.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    int w = Math.min(WIDTH, scrSize.width);
    int h = Math.min(HEIGHT, scrSize.height);
    pb.setLocation(scrSize.width/2 - w/2,
		       scrSize.height/2 - h/2);
    pb.setSize(w, h);

    if (splash != null) {
      if (urlToOpen == null)
	splash.getStatusBar().showStatus("Making Default Project");
      else
	splash.getStatusBar().showStatus("Reading " + projectName);

      splash.getStatusBar().showProgress(40);
    }

    if (urlToOpen == null) p = Project.makeEmptyProject();
    else {
      ArgoParser.SINGLETON.readProject(urlToOpen);
      p = ArgoParser.SINGLETON.getProject();
      p.loadAllMembers();
      p.postLoad();
    }
    pb.setProject(p);

    if (splash != null) {
      splash.getStatusBar().showStatus("Setting Perspectives");
      splash.getStatusBar().showProgress(50);
    }

    pb.setPerspectives(NavPerspective.getRegisteredPerspectives());
    pb.setToDoPerspectives(ToDoPerspective.getRegisteredPerspectives());

    pb.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    //pb.validate();
    //pb.repaint();
    //pb.requestDefaultFocus();

    if (splash != null) {
      splash.getStatusBar().showStatus("Setting up critics");
      splash.getStatusBar().showProgress(70);
    }

    Designer dsgr = Designer.theDesigner();
    uci.uml.critics.Init.init();
    uci.uml.checklist.Init.init();

    if (splash != null) {
      splash.getStatusBar().showStatus("Opening Project Browser");
      splash.getStatusBar().showProgress(90);
    }

    pb.setVisible(true);
    Object model = p.getModels().elementAt(0);
    Object diag = p.getDiagrams().elementAt(0);
    //pb.setTarget(diag);
    pb.getNavPane().setSelection(model, diag);
    if (splash != null) {
	splash.setVisible(false);
	splash.dispose();
	splash = null;
    }

    //should be done in ProjectBrowser.setProject();
    dsgr.spawnCritiquer(pb.getProject());
    dsgr.setChildGenerator(new ChildGenUML());
    System.out.println("spawned critiquing thread");

    // should be in logon wizard?
    dsgr.startConsidering(uci.uml.critics.CrUML.decINHERITANCE);
    dsgr.startConsidering(uci.uml.critics.CrUML.decCONTAINMENT);

    //ToolTipManager.sharedInstance().setInitialDelay(500);
    ToolTipManager.sharedInstance().setDismissDelay(50000000);
    Designer._userWorking = true;
  }


//   private static void defineMockHistory() {
//     History h = History.TheHistory;
//     h.addItem("In the beginning there was Argo");
//     h.addItem("And then I wrote a bunch of papers");
//     h.addItem("Now there is ArgoUML!");
//   }

} /* end Class Main */




