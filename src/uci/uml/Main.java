// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.




package uci.uml;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
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


public class Main {
  ////////////////////////////////////////////////////////////////
  // constants

  public static int WIDTH = 1024;
  public static int HEIGHT = 768;

  ////////////////////////////////////////////////////////////////
  // main

  public static void main(String args[]) {
    //defineMockHistory();
    Vector argv = new Vector();
    for (int i = 0; i < args.length; ++i) argv.addElement(args[i]);

//     try {
//       UIManager.setLookAndFeel(new JasonsLookAndFeel());
//     }
//     catch (Exception ex) {
//       System.out.println("could not set look and feel!");
//     }

    if (argv.contains("-big")) {
      MetalLookAndFeel.setCurrentTheme(new uci.uml.ui.JasonsBigTheme());
    }
    else if (argv.contains("-huge")) {
      MetalLookAndFeel.setCurrentTheme(new uci.uml.ui.JasonsHugeTheme());
    }
    else {
      MetalLookAndFeel.setCurrentTheme(new uci.uml.ui.JasonsTheme());
    }

    boolean doSplash = !argv.contains("-nosplash");

    SplashScreen splash = null;

    if (doSplash) {
      splash = new SplashScreen("Loading Argo/UML...", "Splash");
      splash.setVisible(true);
      splash.getStatusBar().showStatus("Making Project Browser");
      splash.getStatusBar().showProgress(10);
    }

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
      splash.getStatusBar().showStatus("Making Default Project");
      splash.getStatusBar().showProgress(40);
    }
    Project empty = Project.makeEmptyProject();
    pb.setProject(empty);

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
    Object model = empty.getModels().elementAt(0);
    Object diag = empty.getDiagrams().elementAt(0);
    //pb.setTarget(diag);
    pb.getNavPane().setSelection(model, diag);
    if (splash != null) splash.setVisible(false);


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



class MockProject extends Project {

  public MockProject() {
    super("MockProject");
    Model m1 = makeModel1();
    Model m2 = makeModel2();
    Model m3 = makeModel3();
    try {
      addDiagram(makeDiagram(m1));
      addDiagram(makeDiagram(m3));
      addModel(m1);
      addModel(m2);
      addModel(m3);
    }
    catch (PropertyVetoException pve) { }
  }

  public UMLDiagram makeDiagram(Model m) {
    return new UMLClassDiagram(m);
  }

  public Model makeModel1() {
    ConcurrentSubstatesExample cse = new ConcurrentSubstatesExample();
    return cse.model;
//     HumanResourcesExample hre = new HumanResourcesExample();
//     return hre.model;
  }

  public Model makeModel2() {
    ShapesExample she = new ShapesExample();
    return she.model;
  }

  public Model makeModel3() {
    WindowExample we = new WindowExample();
    return we.model;
  }

} /* end class MockProject */

