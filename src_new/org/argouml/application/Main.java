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

package org.argouml.application;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;
import java.beans.*;
import java.io.File;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.*;

import org.tigris.gef.util.*;

import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.cognitive.*;
import org.argouml.cognitive.ui.*;
import org.argouml.uml.cognitive.critics.*;
import org.argouml.xml.argo.ArgoParser;
import org.argouml.uml.ui.UMLAction;

public class Main {
  ////////////////////////////////////////////////////////////////
  // constants

  public static int WIDTH = 1024;
  public static int HEIGHT = 768;

  ////////////////////////////////////////////////////////////////
  // static variables

  private static Vector postLoadActions = new Vector();

  ////////////////////////////////////////////////////////////////
  // main

  public static void main(String args[]) {

      // first, print out some version info for debuggers...

      System.out.println(org.argouml.util.Tools.getVersionInfo());

    boolean doSplash = true;
    boolean useEDEM = true;
    boolean preload = true;
    boolean profileLoad = false;

    File projectFile = null;
    Project p = null;
    String projectName = null;
    URL urlToOpen = null;

    long start, phase0, phase1, phase2, phase3, phase4, phase5, now;

	/* set properties for application behaviour */
	System.setProperty("gef.imageLocation","/org/argouml/Images");

    /* This is the default */
    MetalLookAndFeel.setCurrentTheme(new org.argouml.ui.JasonsTheme());

    //--------------------------------------------
    // Parse command line args:
    // The assumption is that all options precede
    // the name of a project file to load.
    //--------------------------------------------

    for (int i=0; i < args.length; i++) {
      if (args[i].startsWith("-")) {
	if (args[i].equalsIgnoreCase("-big")) {
	  MetalLookAndFeel.setCurrentTheme(new org.argouml.ui.JasonsBigTheme());
	} else if (args[i].equalsIgnoreCase("-huge")) {
	  MetalLookAndFeel.setCurrentTheme(new org.argouml.ui.JasonsHugeTheme());
	} else if (args[i].equalsIgnoreCase("-help") ||
		   args[i].equalsIgnoreCase("-h")) {
	  System.err.println("Usage: [options] [project-file]");
	  System.err.println("Options include: ");
	  System.err.println("  -big            use big fonts");
	  System.err.println("  -huge           use huge fonts");
	  System.err.println("  -nosplash       dont display Argo/UML logo");
	  System.err.println("  -noedem         dont report usage statistics");
	  System.err.println("  -nopreload      dont preload common classes");
	  System.err.println("  -profileload    report on load times");
	  System.exit(0);
	} else if (args[i].equalsIgnoreCase("-nosplash")) {
	  doSplash = false;
	} else if (args[i].equalsIgnoreCase("-noedem")) {
	  useEDEM = false;
	} else if (args[i].equalsIgnoreCase("-nopreload")) {
	  preload = false;
	} else if (args[i].equalsIgnoreCase("-profileload")) {
	  profileLoad = true;
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


    start = System.currentTimeMillis();
    SplashScreen splash = new SplashScreen("Loading ArgoUML...", "Splash");
    splash.getStatusBar().showStatus("Making Project Browser");
    splash.getStatusBar().showProgress(10);

    splash.setVisible(doSplash);
    phase0 = System.currentTimeMillis();


	MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_IMMEDIATE);
    //
    //  sets locale for menus
    //
    Locale.setDefault(new Locale(System.getProperty("user.language"),
				 System.getProperty("user.region")));
    UMLAction.setLocale(Locale.getDefault());
    ProjectBrowser pb = new ProjectBrowser("ArgoUML", splash.getStatusBar());
    phase1 = System.currentTimeMillis();

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

    phase2 = System.currentTimeMillis();

    if (urlToOpen == null) p = Project.makeEmptyProject();
    else {
	p = Project.loadProject(urlToOpen);
    }

    phase3 = System.currentTimeMillis();

    pb.setProject(p);
    phase4 = System.currentTimeMillis();

    if (urlToOpen == null) pb.setTitle("Untitled");

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
      splash.getStatusBar().showStatus("Opening Project Browser");
      splash.getStatusBar().showProgress(95);
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

    phase5 = System.currentTimeMillis();

    Class c = null;
    c = org.tigris.gef.base.ModePlace.class;
    c = org.tigris.gef.base.ModeModify.class;
    c = org.tigris.gef.base.SelectionResize.class;
    c = org.tigris.gef.presentation.FigPoly.class;
    c = org.tigris.gef.base.PathConvPercentPlusConst.class;
    c = org.tigris.gef.presentation.ArrowHeadNone.class;
    c = org.tigris.gef.base.Geometry.class;

    c = org.tigris.gef.ui.ColorRenderer.class;

    c = org.tigris.gef.util.EnumerationEmpty.class;
    c = org.tigris.gef.util.EnumerationSingle.class;

    c = ru.novosoft.uml.foundation.data_types.MScopeKind.class;
    c = ru.novosoft.uml.foundation.data_types.MChangeableKind.class;

    c = org.argouml.uml.diagram.static_structure.ui.FigClass.class;
    c = org.argouml.uml.diagram.ui.SelectionNodeClarifiers.class;
    c = org.argouml.uml.diagram.ui.SelectionWButtons.class;
    c = org.argouml.uml.diagram.static_structure.ui.SelectionClass.class;
    c = org.argouml.uml.diagram.ui.ModeCreateEdgeAndNode.class;
    c = org.argouml.uml.diagram.static_structure.ui.FigPackage.class;
    c = org.argouml.uml.diagram.static_structure.ui.FigInterface.class;

    c = java.lang.ClassNotFoundException.class;
    c = org.argouml.kernel.DelayedChangeNotify.class;
    c = org.tigris.gef.graph.GraphEvent.class;
    c = org.tigris.gef.graph.presentation.NetEdge.class;
    c = org.tigris.gef.graph.GraphEdgeHooks.class;

    c = org.argouml.uml.cognitive.critics.WizMEName.class;
    c = org.argouml.kernel.Wizard.class;


    //if (splash != null) {
    //  splash.getStatusBar().showStatus("Setting up critics");
    //  splash.getStatusBar().showProgress(70);
    //}

    Runnable startCritics = new StartCritics();
    Main.addPostLoadAction(startCritics);


    if (preload) {
      Runnable preloader = new PreloadClasses();
      Main.addPostLoadAction(preloader);
    }

    PostLoad pl = new PostLoad(postLoadActions);
    Thread postLoadThead = new Thread(pl);
    pl.setThread(postLoadThead);
    postLoadThead.start();

    now = System.currentTimeMillis();

    if (profileLoad) {
      System.out.println("");
      System.out.println("");
      System.out.println("profile of load time ################");
      System.out.println("phase 0    " + (phase0 - start));
      System.out.println("phase 1    " + (phase1 - phase0));
      System.out.println("phase 2    " + (phase2 - phase1));
      System.out.println("phase 3    " + (phase3 - phase2));
      System.out.println("phase 4    " + (phase4 - phase3));
      System.out.println("phase 5    " + (phase5 - phase4));
      System.out.println("================");
      System.out.println("total      " + (now - start));
      System.out.println("time to show " + (phase5 - start));

      System.out.println("#######################");
      System.out.println("");
    }


    //ToolTipManager.sharedInstance().setInitialDelay(500);
    ToolTipManager.sharedInstance().setDismissDelay(50000000);
  }


//   private static void defineMockHistory() {
//     History h = History.TheHistory;
//     h.addItem("In the beginning there was Argo");
//     h.addItem("And then I wrote a bunch of papers");
//     h.addItem("Now there is ArgoUML!");
//   }


  public static void  addPostLoadAction(Runnable r) {
    postLoadActions.addElement(r);
  }


} /* end Class Main */



class StartCritics implements Runnable {
  public void run() {
    Designer dsgr = Designer.theDesigner();
    Locale locale = Locale.getDefault();
    CrUML.setLocale(locale);
    org.argouml.uml.cognitive.critics.Init.init();
    org.argouml.uml.cognitive.checklist.Init.init(locale);
    ProjectBrowser pb = ProjectBrowser.TheInstance;
	Project p = pb.getProject();
    dsgr.spawnCritiquer(p);
    dsgr.setChildGenerator(new ChildGenUML());
	java.util.Enumeration models = (p.getModels()).elements();
	while (models.hasMoreElements())
	{
		((ru.novosoft.uml.model_management.MModel)models.nextElement()).addMElementListener(dsgr);
	}
    System.out.println("spawned critiquing thread");

    // should be in logon wizard?
    dsgr.startConsidering(org.argouml.uml.cognitive.critics.CrUML.decINHERITANCE);
    dsgr.startConsidering(org.argouml.uml.cognitive.critics.CrUML.decCONTAINMENT);
    Designer._userWorking = true;
  }

} /* end class StartCritics */

class PostLoad implements Runnable {
  Vector postLoadActions = null;
  Thread myThread = null;
  public PostLoad(Vector v) { postLoadActions = v; }
  public void setThread(Thread t) { myThread = t; }
  public void run() {
    try { myThread.sleep(1000); }
    catch (Exception ex) { System.out.println("post load no sleep"); }
    int size = postLoadActions.size();
    for (int i = 0; i < size; i++) {
      Runnable r = (Runnable) postLoadActions.elementAt(i);
      r.run();
      try { myThread.sleep(100); }
      catch (Exception ex) { System.out.println("post load no sleep2"); }
    }
  }
} /* end class PostLoad */


class PreloadClasses implements Runnable {
  public void run() {
      //if (splash != null) {
      //splash.getStatusBar().showStatus("Preloading classes");
      //splash.getStatusBar().showProgress(90);
      //}


      Class c = null;
      System.out.print("preloading...");
      c = org.tigris.gef.base.CmdSetMode.class;
      c = org.tigris.gef.base.ModePlace.class;
      c = org.tigris.gef.base.ModeModify.class;
      c = org.tigris.gef.base.SelectionResize.class;
      c = org.tigris.gef.ui.ColorRenderer.class;
      c = org.tigris.gef.ui.Swatch.class;
      c = org.tigris.gef.util.EnumerationEmpty.class;
      c = org.tigris.gef.util.EnumerationSingle.class;
      c = org.argouml.uml.GenCompositeClasses.class;
      c = org.argouml.uml.diagram.static_structure.ui.FigClass.class;
      c = org.argouml.uml.diagram.static_structure.ui.FigPackage.class;
      c = org.argouml.uml.diagram.static_structure.ui.FigInterface.class;
      c = org.argouml.uml.diagram.ui.FigAssociation.class;
      c = org.argouml.uml.diagram.ui.FigGeneralization.class;
      c = org.argouml.uml.diagram.ui.FigRealization.class;
      c = org.argouml.uml.ui.foundation.core.PropPanelClass.class;
      c = org.argouml.uml.ui.foundation.core.PropPanelInterface.class;
      c = org.argouml.uml.ui.foundation.core.PropPanelAssociation.class;
      c = org.argouml.ui.StylePanelFig.class;
      c = org.argouml.uml.diagram.static_structure.ui.StylePanelFigClass.class;
      c = org.argouml.uml.diagram.ui.SPFigEdgeModelElement.class;

      c = java.lang.ClassNotFoundException.class;
      c = org.argouml.ui.UpdateTreeHack.class;
      c = org.argouml.kernel.DelayedChangeNotify.class;
      c = org.tigris.gef.graph.GraphEvent.class;
      c = org.tigris.gef.graph.presentation.NetEdge.class;
      c = org.tigris.gef.graph.GraphEdgeHooks.class;

      c = org.argouml.uml.cognitive.critics.WizMEName.class;
      c = org.argouml.kernel.Wizard.class;

      c = java.beans.Introspector.class;
      c = java.beans.BeanInfo.class;
      c = java.beans.BeanDescriptor.class;
      c = java.beans.FeatureDescriptor.class;
      c = java.lang.InterruptedException.class;
      c = java.lang.CloneNotSupportedException.class;
      c = java.lang.reflect.Modifier.class;
      c = java.beans.EventSetDescriptor.class;
      c = java.beans.PropertyDescriptor.class;
      c = java.lang.Void.class;
      c = java.beans.MethodDescriptor.class;
      c = java.beans.SimpleBeanInfo.class;
      c = java.util.TooManyListenersException.class;
      c = java.beans.PropertyVetoException.class;
      c = java.beans.IndexedPropertyDescriptor.class;
      c = org.argouml.kernel.HistoryItemResolve.class;
      c = org.tigris.gef.event.ModeChangeEvent.class;
      c = org.argouml.uml.cognitive.critics.ClClassName.class;
      c = org.argouml.ui.Clarifier.class;
      c = org.argouml.uml.cognitive.critics.ClAttributeCompartment.class;
      c = org.argouml.uml.cognitive.critics.ClOperationCompartment.class;
      c = java.lang.SecurityException.class;
      c = java.lang.NullPointerException.class;

      System.out.println(" done preloading");
  }
} /* end class PreloadClasses */
