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
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

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
  // static variables

  private static Vector postLoadActions = new Vector();

  ////////////////////////////////////////////////////////////////
  // main

  public static void main(String args[]) {
    boolean doSplash = true;
    boolean useEDEM = true;
    boolean preload = true;
    boolean profileLoad = false;

    File projectFile = null;
    Project p = null;
    String projectName = null;
    URL urlToOpen = null;

    long start, phase0, phase1, phase2, phase3, phase4, phase5, now;

    /* This is the default */
    MetalLookAndFeel.setCurrentTheme(new uci.uml.ui.JasonsTheme());

    //--------------------------------------------
    // Parse command line args:
    // The assumption is that all options precede
    // the name of a project file to load.
    //--------------------------------------------

    for (int i=0; i < args.length; i++) {
      if (args[i].startsWith("-")) {
	if (args[i].equalsIgnoreCase("-big")) {
	  MetalLookAndFeel.setCurrentTheme(new uci.uml.ui.JasonsBigTheme());
	} else if (args[i].equalsIgnoreCase("-huge")) {
	  MetalLookAndFeel.setCurrentTheme(new uci.uml.ui.JasonsHugeTheme());
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
    SplashScreen splash = new SplashScreen("Loading Argo/UML...", "Splash");
    splash.getStatusBar().showStatus("Making Project Browser");
    splash.getStatusBar().showProgress(10);

    splash.setVisible(doSplash);
    phase0 = System.currentTimeMillis();


    ProjectBrowser pb = new ProjectBrowser("Argo/UML", splash.getStatusBar());
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
      ArgoParser.SINGLETON.readProject(urlToOpen);
      p = ArgoParser.SINGLETON.getProject();
      p.loadAllMembers();
      p.postLoad();
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
    c = uci.gef.ModePlace.class;
    c = uci.gef.ModeModify.class;
    c = uci.gef.SelectionResize.class;
    c = uci.gef.FigPoly.class;
    c = uci.gef.PathConvPercentPlusConst.class;
    c = uci.gef.ArrowHeadNone.class;
    c = uci.gef.Geometry.class;

    c = uci.ui.ColorRenderer.class;

    c = uci.util.EnumerationEmpty.class;
    c = uci.util.EnumerationSingle.class;

    c = uci.uml.Foundation.Data_Types.ScopeKind.class;
    c = uci.uml.Foundation.Data_Types.ChangeableKind.class;

    c = uci.uml.visual.FigClass.class;
    c = uci.uml.visual.SelectionNodeClarifiers.class;
    c = uci.uml.visual.SelectionWButtons.class;
    c = uci.uml.visual.SelectionClass.class;
    c = uci.uml.visual.ModeCreateEdgeAndNode.class;
    c = uci.uml.visual.FigPackage.class;
    c = uci.uml.visual.FigInterface.class;

    c = java.lang.ClassNotFoundException.class;
    c = uci.uml.ui.DelayedChangeNotify.class;
    c = uci.graph.GraphEvent.class;
    c = uci.gef.NetEdge.class;
    c = uci.graph.GraphEdgeHooks.class;

    c = uci.uml.critics.WizMEName.class;
    c = uci.argo.kernel.Wizard.class;


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
    uci.uml.critics.Init.init();
    uci.uml.checklist.Init.init();
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    dsgr.spawnCritiquer(pb.getProject());
    dsgr.setChildGenerator(new ChildGenUML());
    uci.uml.Foundation.Core.ElementImpl.setStaticChangeListener(dsgr);
    System.out.println("spawned critiquing thread");

    // should be in logon wizard?
    dsgr.startConsidering(uci.uml.critics.CrUML.decINHERITANCE);
    dsgr.startConsidering(uci.uml.critics.CrUML.decCONTAINMENT);
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
      c = uci.gef.CmdSetMode.class;
      c = uci.gef.ModePlace.class;
      c = uci.gef.ModeModify.class;
      c = uci.gef.SelectionResize.class;
      c = uci.ui.ColorRenderer.class;
      c = uci.ui.Swatch.class;
      c = uci.util.EnumerationEmpty.class;
      c = uci.util.EnumerationSingle.class;
      c = uci.uml.util.GenCompositeClasses.class;
      c = uci.uml.visual.FigClass.class;
      c = uci.uml.visual.FigPackage.class;
      c = uci.uml.visual.FigInterface.class;
      c = uci.uml.visual.FigAssociation.class;
      c = uci.uml.visual.FigGeneralization.class;
      c = uci.uml.visual.FigRealization.class;
      c = uci.uml.ui.props.PropPanelClass.class;
      c = uci.uml.ui.props.PropPanelInterface.class;
      c = uci.uml.ui.props.PropPanelAssociation.class;
      c = uci.uml.ui.style.StylePanelFig.class;
      c = uci.uml.ui.style.StylePanelFigClass.class;
      c = uci.uml.ui.style.SPFigEdgeModelElement.class;

      c = java.lang.ClassNotFoundException.class;
      c = uci.uml.ui.UpdateTreeHack.class;
      c = uci.uml.ui.DelayedChangeNotify.class;
      c = uci.graph.GraphEvent.class;
      c = uci.gef.NetEdge.class;
      c = uci.graph.GraphEdgeHooks.class;

      c = uci.uml.critics.WizMEName.class;
      c = uci.argo.kernel.Wizard.class;

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
      c = uci.argo.kernel.HistoryItemResolve.class;
      c = uci.gef.event.ModeChangeEvent.class;
      c = uci.uml.critics.ClClassName.class;
      c = uci.argo.kernel.Clarifier.class;
      c = uci.uml.critics.ClAttributeCompartment.class;
      c = uci.uml.critics.ClOperationCompartment.class;
      c = java.lang.SecurityException.class;
      c = java.lang.NullPointerException.class;

      System.out.println(" done preloading");
  }
} /* end class PreloadClasses */
