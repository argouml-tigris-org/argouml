// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
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
import uci.uml.visual.*;
import uci.uml.critics.*;
import uci.uml.Model_Management.Model;
import uci.uml.test.omg.*;

import uci.argo.kernel.*;


public class Main {
  ////////////////////////////////////////////////////////////////
  // constants

  public static int WIDTH = 800;
  public static int HEIGHT = 600;
  public static int SPLASH_WIDTH = 400;
  public static int SPLASH_HEIGHT = 200;

  ////////////////////////////////////////////////////////////////
  // static variables

  public static Vector UMLPerspectives = new Vector();
  public static Vector ToDoPerspectives = new Vector();

  // static initializer
  static {
    UMLPerspectives.addElement(new NavPackageCentric());
    UMLPerspectives.addElement(new NavDiagramCentric());
    UMLPerspectives.addElement(new NavInheritance());

    ToDoPerspectives.addElement(new ToDoByPriority());
    ToDoPerspectives.addElement(new ToDoByDecision());
    ToDoPerspectives.addElement(new ToDoByGoal());
    ToDoPerspectives.addElement(new ToDoByOffender());
    ToDoPerspectives.addElement(new ToDoByType());
    ToDoPerspectives.addElement(new ToDoByPoster());
  }

  ////////////////////////////////////////////////////////////////
  // main

  public static void main(String args[]) {
    defineMockHistory();
    MetalLookAndFeel.setCurrentTheme(new uci.uml.ui.JasonsTheme());
    SplashScreen splash = new SplashScreen("Loading Argo/UML...", "Splash");
    splash.setVisible(true);
    
    splash.getStatusBar().doFakeProgress("Creating Project Browser", 100);
    ProjectBrowser pb = new ProjectBrowser("Argo/UML");
    pb.addWindowListener(new WindowCloser());
    JOptionPane.setRootFrame(pb);
    //pb.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
    Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
    pb.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    pb.setLocation(scrSize.width/2 - WIDTH/2,
		       scrSize.height/2 - HEIGHT/2);
    pb.setSize(WIDTH, HEIGHT);
    splash.getStatusBar().showStatus("Making Mock Project");
    pb.setProject(new MockProject());
    pb.setPerspectives(UMLPerspectives);
    pb.setToDoPerspectives(ToDoPerspectives);
    splash.getStatusBar().showStatus("Opening Project Browser");
    pb.setVisible(true);
    splash.setVisible(false);

    pb.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    //pb.validate();
    //pb.repaint();
    //pb.requestDefaultFocus();

    Designer dsgr = Designer.theDesigner();
    uci.uml.critics.Init.init();


    //should be done in ProjectBrowser.setProject();
    Designer.theDesigner().spawnCritiquer(pb.getProject());
    Designer.theDesigner().setChildGenerator(new ChildGenUML());
    System.out.println("spawned");

    // should be in logon wizard?
    dsgr.startConsidering(uci.uml.critics.CrUML.decINHERITANCE);
    dsgr.startConsidering(uci.uml.critics.CrUML.decCONTAINMENT);

  }


  private static void defineMockHistory() {
    History h = History.TheHistory;
    h.addItem("In the beginning there was Argo");
    h.addItem("And then I wrote a bunch of papers");
    h.addItem("Now there is ArgoUML!");
  }

} /* end Class Main */


class WindowCloser extends WindowAdapter {
  public WindowCloser() { }
  public void windowClosing(WindowEvent e) { System.exit(0); }
};

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
    HumanResourcesExample hre = new HumanResourcesExample();
    return hre.model;
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
